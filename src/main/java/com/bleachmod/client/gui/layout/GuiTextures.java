package com.bleachmod.client.gui.layout;

import com.bleachmod.BleachMod;
import com.bleachmod.Reference;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * Knows each layout texture's pixel size (so JSON never has to repeat it) and, in
 * development, swaps in a live copy of the PNG from src/ whenever the file changes.
 */
public final class GuiTextures {
    /** Texture to bind plus its real size in pixels. */
    public record Info(ResourceLocation location, int width, int height) {
    }

    private static final Map<ResourceLocation, Info> CACHE = new HashMap<>();
    private static final Map<ResourceLocation, Long> DEV_STAMPS = new HashMap<>();
    private static long lastDevCheck;

    private GuiTextures() {
    }

    public static Info get(ResourceLocation texture) {
        if (DevResources.enabled()) {
            long now = System.currentTimeMillis();
            if (now - lastDevCheck > 500) {
                lastDevCheck = now;
                CACHE.keySet().removeIf(GuiTextures::devFileChanged);
            }
        }
        return CACHE.computeIfAbsent(texture, GuiTextures::load);
    }

    public static void clear() {
        CACHE.clear();
        DEV_STAMPS.clear();
    }

    private static boolean devFileChanged(ResourceLocation texture) {
        Path file = DevResources.file(texture);
        Long stamp = DEV_STAMPS.get(texture);
        if (file == null) return stamp != null;
        try {
            return stamp == null || Files.getLastModifiedTime(file).toMillis() != stamp;
        } catch (IOException e) {
            return true;
        }
    }

    private static Info load(ResourceLocation texture) {
        Path file = DevResources.file(texture);
        if (file != null) {
            try (InputStream in = Files.newInputStream(file)) {
                NativeImage image = NativeImage.read(in);
                ResourceLocation live = Reference.id("dev_gui/" + texture.getNamespace() + "/" + texture.getPath());
                int w = image.getWidth(), h = image.getHeight();
                Minecraft.getInstance().getTextureManager().register(live, new DynamicTexture(image));
                DEV_STAMPS.put(texture, Files.getLastModifiedTime(file).toMillis());
                return new Info(live, w, h);
            } catch (IOException | RuntimeException e) {
                BleachMod.LOGGER.warn("GUI texture {} could not be read from {}: {}", texture, file, e.toString());
            }
        }
        int[] size = pngSize(texture);
        return new Info(texture, size[0], size[1]);
    }

    /** Reads width/height from the PNG header without decoding the image. */
    private static int[] pngSize(ResourceLocation texture) {
        var resource = Minecraft.getInstance().getResourceManager().getResource(texture);
        if (resource.isEmpty()) {
            BleachMod.LOGGER.warn("GUI texture {} not found", texture);
            return new int[]{16, 16};
        }
        try (DataInputStream in = new DataInputStream(resource.get().open())) {
            in.skipNBytes(16); // signature (8) + IHDR length/type (8)
            return new int[]{in.readInt(), in.readInt()};
        } catch (IOException e) {
            BleachMod.LOGGER.warn("GUI texture {} has an unreadable header: {}", texture, e.toString());
            return new int[]{16, 16};
        }
    }
}
