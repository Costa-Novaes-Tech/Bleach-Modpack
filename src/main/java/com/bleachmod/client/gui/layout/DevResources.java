package com.bleachmod.client.gui.layout;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * In the development client (runClient), layouts and GUI textures are read straight
 * from src/main/resources so saving a file updates the HUD without a rebuild or F3+T.
 * Packaged builds never touch the filesystem here.
 */
final class DevResources {
    private static final Path ROOT = locate();

    private DevResources() {
    }

    private static Path locate() {
        if (FMLEnvironment.production) return null;
        Path game = FMLPaths.GAMEDIR.get().toAbsolutePath();
        for (Path dir = game; dir != null; dir = dir.getParent()) {
            Path resources = dir.resolve("src/main/resources");
            if (Files.isDirectory(resources.resolve("assets/bleachmod"))) return resources;
        }
        return null;
    }

    static boolean enabled() {
        return ROOT != null;
    }

    /** The source file behind a resource, or null outside development. */
    static Path file(ResourceLocation location) {
        if (ROOT == null) return null;
        Path path = ROOT.resolve("assets").resolve(location.getNamespace()).resolve(location.getPath());
        return Files.isRegularFile(path) ? path : null;
    }

    static Path dir(String namespace, String folder) {
        return ROOT == null ? null : ROOT.resolve("assets").resolve(namespace).resolve(folder);
    }
}
