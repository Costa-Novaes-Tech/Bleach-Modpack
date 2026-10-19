package com.bleachmod.client.berserk;

import com.bleachmod.Reference;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;
import java.util.HashMap;
import java.util.Map;

/**
 * Berserk Mode skin: a copy of the player's own skin where only skin-tone pixels turn crimson,
 * so clothes, hair and accessories keep their colors. Built once per skin on the render thread.
 * Placeholder for a future Dragon Block C style skin system.
 */
public final class BerserkSkins {
    private static final Map<ResourceLocation, ResourceLocation> CACHE = new HashMap<>();
    private BerserkSkins() { }

    public static ResourceLocation of(ResourceLocation skin) {
        ResourceLocation cached = CACHE.get(skin);
        if (cached != null) return cached;
        var texture = Minecraft.getInstance().getTextureManager().getTexture(skin);
        RenderSystem.bindTexture(texture.getId());
        // GL_TEXTURE_2D level 0: GL_TEXTURE_WIDTH / GL_TEXTURE_HEIGHT.
        int width = com.mojang.blaze3d.platform.GlStateManager._getTexLevelParameter(3553, 0, 4096);
        int height = com.mojang.blaze3d.platform.GlStateManager._getTexLevelParameter(3553, 0, 4097);
        if (width <= 0 || height <= 0) return skin; // not uploaded yet; try again next frame
        NativeImage image = new NativeImage(width, height, false);
        image.downloadTexture(0, false);
        int changed = recolor(image);
        // Skins without any recognizable skin tone (masks, robots) fall back to a full crimson tint.
        if (changed < width * height / 64) tintAll(image);
        ResourceLocation id = Reference.id("berserk_skin/" + Integer.toHexString(skin.hashCode()));
        Minecraft.getInstance().getTextureManager().register(id, new DynamicTexture(image));
        CACHE.put(skin, id);
        return id;
    }
    /** Called when resources reload or the world is left; the texture manager releases the images. */
    public static void clear() {
        CACHE.values().forEach(id -> Minecraft.getInstance().getTextureManager().release(id));
        CACHE.clear();
    }

    /** True for warm, moderately saturated colors in the human skin-tone range (light to dark). */
    public static boolean skinTone(int r, int g, int b) {
        int max = Math.max(r, Math.max(g, b)), min = Math.min(r, Math.min(g, b));
        if (max < 45 || r < g || g < b * .75) return false;
        float saturation = (max - min) / (float) max;
        float hue = max == min ? 0 : r == max ? 60F * (g - b) / (max - min) : 999;
        return hue >= 5 && hue <= 50 && saturation >= .12F && saturation <= .72F;
    }
    /** Keeps the shading of the original pixel while shifting it to Kenpachi's crimson. */
    public static int crimson(int r, int g, int b) {
        float lum = (.3F * r + .59F * g + .11F * b) / 255F;
        int nr = Math.min(255, (int) (70 + lum * 200)), ng = (int) (lum * 55), nb = (int) (lum * 50);
        return (nr << 16) | (ng << 8) | nb;
    }
    private static int recolor(NativeImage image) {
        int changed = 0;
        for (int y = 0; y < image.getHeight(); y++) for (int x = 0; x < image.getWidth(); x++) {
            int abgr = image.getPixelRGBA(x, y);
            int a = abgr >>> 24 & 255, b = abgr >> 16 & 255, g = abgr >> 8 & 255, r = abgr & 255;
            if (a == 0 || !skinTone(r, g, b)) continue;
            int rgb = crimson(r, g, b);
            image.setPixelRGBA(x, y, a << 24 | (rgb & 255) << 16 | (rgb >> 8 & 255) << 8 | rgb >> 16 & 255);
            changed++;
        }
        return changed;
    }
    private static void tintAll(NativeImage image) {
        for (int y = 0; y < image.getHeight(); y++) for (int x = 0; x < image.getWidth(); x++) {
            int abgr = image.getPixelRGBA(x, y);
            int a = abgr >>> 24 & 255; if (a == 0) continue;
            int b = abgr >> 16 & 255, g = abgr >> 8 & 255, r = abgr & 255;
            image.setPixelRGBA(x, y, a << 24 | (b * 45 / 100) << 16 | (g * 35 / 100) << 8 | Math.min(255, r + 60));
        }
    }
}
