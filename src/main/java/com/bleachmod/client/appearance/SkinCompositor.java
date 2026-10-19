package com.bleachmod.client.appearance;

import com.bleachmod.BleachMod;
import com.bleachmod.Reference;
import com.bleachmod.common.appearance.AppearanceCatalog;
import com.bleachmod.common.appearance.AppearanceData;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;

import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Bakes a look into one 128x128 player texture: body base, outfit, eyes, mouth and hair, with the
 * tintable layers multiplied by the chosen colors. Built once per distinct look, so drawing the player
 * costs the same as a vanilla skin. Runs on the render thread only.
 */
public final class SkinCompositor {
    public static final int SIZE = 128;
    /** Draw order, bottom to top; hair last so the fringe covers the forehead. */
    private static final String[] ORDER = {"base", "outfit", "eyes", "mouth", "hair"};
    private static final int MAX_TEXTURES = 64;
    private static final Map<String, ResourceLocation> TEXTURES = new LinkedHashMap<>(16, .75F, true) {
        @Override protected boolean removeEldestEntry(Map.Entry<String, ResourceLocation> eldest) {
            if (size() <= MAX_TEXTURES) return false;
            Minecraft.getInstance().getTextureManager().release(eldest.getValue());
            return true;
        }
    };
    /** Decoded layer images; null marks a missing texture so it is not retried every frame. */
    private static final Map<String, NativeImage> LAYERS = new HashMap<>();
    private static int nextId;

    private SkinCompositor() { }

    public static ResourceLocation texture(AppearanceData look) {
        String key = look.textureKey();
        ResourceLocation id = TEXTURES.get(key);
        if (id != null) return id;
        NativeImage image = compose(look);
        id = Reference.id("appearance_skin/" + Integer.toHexString(nextId++));
        Minecraft.getInstance().getTextureManager().register(id, new DynamicTexture(image));
        TEXTURES.put(key, id);
        return id;
    }

    private static NativeImage compose(AppearanceData look) {
        NativeImage out = new NativeImage(SIZE, SIZE, true);
        AppearanceCatalog catalog = AppearanceCatalog.get();
        for (String category : ORDER) {
            catalog.piece(look.piece(category)).ifPresent(piece -> {
                for (AppearanceCatalog.Layer layer : piece.layers()) {
                    NativeImage src = layer(layer.texture());
                    if (src != null) draw(out, src, tint(layer.tint(), look, piece.defaultColor()));
                }
            });
        }
        return out;
    }

    /** 0xRRGGBB multiplier for a layer, or -1 when the layer keeps its own colors. */
    public static int tint(String channel, AppearanceData look, int pieceDefault) {
        return switch (channel) {
            case "skin" -> look.skinColor();
            case "hair" -> look.hairColor() < 0 ? pieceDefault : look.hairColor();
            case "eye" -> look.eyeColor() < 0 ? pieceDefault : look.eyeColor();
            default -> -1;
        };
    }

    private static void draw(NativeImage out, NativeImage src, int tint) {
        int w = src.getWidth(), h = src.getHeight();
        for (int y = 0; y < SIZE; y++) for (int x = 0; x < SIZE; x++) {
            int abgr = src.getPixelRGBA(x * w / SIZE, y * h / SIZE);
            if ((abgr >>> 24) == 0) continue;
            out.setPixelRGBA(x, y, blend(out.getPixelRGBA(x, y), abgr, tint));
        }
    }

    /** Source-over of a (tinted) ABGR pixel onto an ABGR pixel. */
    public static int blend(int dst, int src, int tint) {
        int sa = src >>> 24, sb = src >> 16 & 255, sg = src >> 8 & 255, sr = src & 255;
        if (tint >= 0) { sr = sr * (tint >> 16 & 255) / 255; sg = sg * (tint >> 8 & 255) / 255; sb = sb * (tint & 255) / 255; }
        if (sa == 255) return 0xFF000000 | sb << 16 | sg << 8 | sr;
        int da = dst >>> 24, db = dst >> 16 & 255, dg = dst >> 8 & 255, dr = dst & 255;
        int oa = sa + da * (255 - sa) / 255;
        if (oa == 0) return 0;
        int r = (sr * sa + dr * da * (255 - sa) / 255) / oa, g = (sg * sa + dg * da * (255 - sa) / 255) / oa, b = (sb * sa + db * da * (255 - sa) / 255) / oa;
        return oa << 24 | b << 16 | g << 8 | r;
    }

    private static NativeImage layer(String texture) {
        if (LAYERS.containsKey(texture)) return LAYERS.get(texture);
        NativeImage image = null;
        try {
            var resource = Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation(texture));
            if (resource.isPresent()) try (InputStream in = resource.get().open()) { image = NativeImage.read(in); }
            else BleachMod.LOGGER.warn("Missing appearance layer {}", texture);
        } catch (Exception e) {
            BleachMod.LOGGER.warn("Unreadable appearance layer {}", texture, e);
        }
        LAYERS.put(texture, image);
        return image;
    }

    /** Releases every baked texture and decoded layer; called on world leave and resource reload. */
    public static void clear() {
        TEXTURES.values().forEach(id -> Minecraft.getInstance().getTextureManager().release(id));
        TEXTURES.clear();
        LAYERS.values().forEach(image -> { if (image != null) image.close(); });
        LAYERS.clear();
    }
}
