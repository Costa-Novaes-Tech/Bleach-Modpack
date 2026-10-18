package com.bleachmod.server.world;

import com.bleachmod.BleachMod;
import com.bleachmod.Reference;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Copies the pre-built maps into a History Mode save exactly once, before any level loads its chunks.
 * Source: {@code <maps dir>/<map id>/{region,entities,poi}} plus {@code maps.json}; the maps dir is the
 * {@code bleachmod.mapsDir} system property (set by the Gradle runs to the repository's {@code history-maps/})
 * or {@code <gameDir>/bleachmod/maps} otherwise.
 * The manifest is copied into the save so the world keeps working without the source folder.
 */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public final class HistoryMapInstaller {
    private static final String MANIFEST = "maps.json";
    private static final String INSTALLED = "maps_installed.json";
    private static final List<String> MAP_FOLDERS = List.of("region", "entities", "poi");

    private HistoryMapInstaller() {
    }

    /** Fired before {@code loadLevel()} on both the integrated and the dedicated server. */
    @SubscribeEvent
    public static void onServerAboutToStart(ServerAboutToStartEvent event) {
        MinecraftServer server = event.getServer();
        HistoryWorld.clear();
        if (!BleachDimensions.isHistoryMode(server)) return;
        Path root = server.getWorldPath(LevelResource.ROOT);
        Path installed = root.resolve(Reference.MOD_ID).resolve(INSTALLED);
        try {
            if (Files.notExists(installed)) install(server, root, installed);
            if (Files.exists(installed)) HistoryWorld.activate(HistoryWorld.parse(installed));
        } catch (IOException e) {
            BleachMod.LOGGER.error("History Mode: maps could not be installed into {}", root, e);
        }
    }

    @SubscribeEvent
    public static void onServerStopped(ServerStoppedEvent event) {
        HistoryWorld.clear();
    }

    /** New worlds only: spawn everyone, whatever the race, at the human-world map spawn. */
    @SubscribeEvent
    public static void onCreateSpawnPosition(LevelEvent.CreateSpawnPosition event) {
        if (!(event.getLevel() instanceof ServerLevel level) || level.dimension() != Level.OVERWORLD) return;
        HistoryWorld.destination(HistoryWorld.HUMAN_WORLD).ifPresent(spawn -> {
            event.getSettings().setSpawn(BlockPos.containing(spawn.x(), spawn.y(), spawn.z()), spawn.yaw());
            level.getGameRules().getRule(GameRules.RULE_SPAWN_RADIUS).set(0, level.getServer());
            event.setCanceled(true);
        });
    }

    private static void install(MinecraftServer server, Path root, Path installed) throws IOException {
        Path source = mapsSource();
        Path manifest = source.resolve(MANIFEST);
        if (Files.notExists(manifest)) {
            BleachMod.LOGGER.error("History Mode world without maps: {} not found", manifest);
            return;
        }
        Map<String, HistoryWorld.Destination> maps = HistoryWorld.parse(manifest);
        var stems = server.registryAccess().registryOrThrow(Registries.LEVEL_STEM);
        for (HistoryWorld.Destination map : maps.values()) {
            if (!stems.containsKey(map.dimensionId())) {
                throw new IOException("map '" + map.id() + "' targets unknown dimension " + map.dimensionId());
            }
            Path target = DimensionType.getStorageFolder(map.dimension(), root);
            if (containsFiles(target.resolve("region"))) {
                throw new IOException("refusing to overwrite existing chunks in " + target);
            }
        }
        for (HistoryWorld.Destination map : maps.values()) {
            Path from = source.resolve(map.id());
            if (Files.notExists(from.resolve("region"))) {
                BleachMod.LOGGER.warn("History Mode: map '{}' has no region folder in {}, skipped", map.id(), from);
                continue;
            }
            Path target = DimensionType.getStorageFolder(map.dimension(), root);
            for (String folder : MAP_FOLDERS) copyTree(from.resolve(folder), target.resolve(folder));
            BleachMod.LOGGER.info("History Mode: installed map '{}' into {}", map.id(), map.dimensionId());
        }
        Files.createDirectories(installed.getParent());
        Files.copy(manifest, installed);
    }

    private static Path mapsSource() {
        String configured = System.getProperty("bleachmod.mapsDir");
        if (configured != null && !configured.isBlank()) return Path.of(configured);
        return FMLPaths.GAMEDIR.get().resolve(Reference.MOD_ID).resolve("maps");
    }

    private static boolean containsFiles(Path folder) throws IOException {
        if (!Files.isDirectory(folder)) return false;
        try (Stream<Path> files = Files.list(folder)) {
            return files.findAny().isPresent();
        }
    }

    private static void copyTree(Path from, Path to) throws IOException {
        if (!Files.isDirectory(from)) return;
        try (Stream<Path> files = Files.walk(from)) {
            for (Path file : (Iterable<Path>) files::iterator) {
                Path dest = to.resolve(from.relativize(file).toString());
                if (Files.isDirectory(file)) Files.createDirectories(dest);
                else Files.copy(file, dest);
            }
        }
    }
}
