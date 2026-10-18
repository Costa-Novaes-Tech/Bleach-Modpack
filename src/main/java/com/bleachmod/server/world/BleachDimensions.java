package com.bleachmod.server.world;

import com.bleachmod.Reference;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;

/** Dimensions declared by the History Mode world preset; normal worlds do not have them. */
public final class BleachDimensions {
    public static final ResourceKey<Level> SOUL_SOCIETY = ResourceKey.create(Registries.DIMENSION, Reference.id("soul_society"));
    public static final ResourceKey<Level> HUECO_MUNDO = ResourceKey.create(Registries.DIMENSION, Reference.id("hueco_mundo"));

    private BleachDimensions() {
    }

    /** True when the world was created with the History Mode preset (its stems live in level.dat). */
    public static boolean isHistoryMode(MinecraftServer server) {
        return server.registryAccess().registryOrThrow(Registries.LEVEL_STEM).containsKey(SOUL_SOCIETY.location());
    }
}
