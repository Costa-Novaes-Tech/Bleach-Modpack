package com.bleachmod.server.world;

import com.bleachmod.BleachMod;
import com.bleachmod.Reference;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Copies the pre-built maps into a History Mode save exactly once, before any level loads its chunks.
 * Source: {@code <maps dir>/<map id>/{region,entities,poi}} plus {@code maps.json}; the maps dir is the
 * {@code bleachmod.mapsDir} system property (set by the Gradle runs to the repository's {@code history-maps/})
 * or {@code <gameDir>/bleachmod/maps} otherwise.
 * The manifest is copied into the save so the world keeps working without the source folder.
 * <p>
 * Installation is all-or-nothing: every map is copied into {@code <folder>.staging} and checked before any
 * of them is moved into place, and a {@code maps_installing} marker stays in the save until the installed
 * manifest is written. An interrupted install leaves the marker behind, so the next start wipes the folders
 * it owns and starts over instead of refusing to touch half-copied chunks.
 */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public final class HistoryMapInstaller {
    private static final String MANIFEST = "maps.json";
    private static final String INSTALLED = "maps_installed.json";
    private static final String INSTALLING = "maps_installing";
    private static final String STAGING = ".staging";
    private static final List<String> MAP_FOLDERS = List.of("region", "entities", "poi");
    /** Why this save's maps are missing, shown to players as they join; null when the install is fine. */
    private static volatile String failure;

    private HistoryMapInstaller() {
    }

    /** Fired before {@code loadLevel()} on both the integrated and the dedicated server. */
    @SubscribeEvent
    public static void onServerAboutToStart(ServerAboutToStartEvent event) {
        MinecraftServer server = event.getServer();
        HistoryWorld.clear();
        failure = null;
        if (!BleachDimensions.isHistoryMode(server)) return;
        Path root = server.getWorldPath(LevelResource.ROOT);
        Path installed = root.resolve(Reference.MOD_ID).resolve(INSTALLED);
        try {
            if (Files.notExists(installed)) install(server, root, installed);
            HistoryWorld.activate(HistoryWorld.parse(installed));
        } catch (IOException e) {
            failure = e.getMessage();
            BleachMod.LOGGER.error("History Mode: maps could not be installed into {}; the next start retries", root, e);
        }
    }

    @SubscribeEvent
    public static void onServerStopped(ServerStoppedEvent event) {
        HistoryWorld.clear();
        failure = null;
    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        String reason = failure;
        if (reason != null) event.getEntity().sendSystemMessage(
                Component.translatable("message.bleachmod.history.install_failed", reason).withStyle(ChatFormatting.RED));
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
        if (Files.notExists(manifest)) throw new IOException(manifest + " not found");
        Map<String, HistoryWorld.Destination> maps = HistoryWorld.parse(manifest);
        var stems = server.registryAccess().registryOrThrow(Registries.LEVEL_STEM);
        Map<String, Path> targets = new LinkedHashMap<>();
        for (HistoryWorld.Destination map : maps.values()) {
            if (!stems.containsKey(map.dimensionId())) {
                throw new IOException("map '" + map.id() + "' targets unknown dimension " + map.dimensionId());
            }
            targets.put(map.id(), DimensionType.getStorageFolder(map.dimension(), root));
        }
        installMaps(source, manifest, targets, installed);
    }

    /**
     * Copies {@code source/<id>} into each target dimension folder and then writes {@code installed}.
     * Either every map ends up in place with the installed manifest, or the marker is left for a retry.
     */
    public static void installMaps(Path source, Path manifest, Map<String, Path> targets, Path installed) throws IOException {
        for (String id : targets.keySet()) {
            if (!Files.isDirectory(source.resolve(id).resolve("region"))) {
                throw new IOException("map '" + id + "' has no region folder in " + source.resolve(id));
            }
        }
        Path marker = installed.resolveSibling(INSTALLING);
        boolean resuming = Files.exists(marker);
        for (Path target : targets.values()) {
            for (String folder : MAP_FOLDERS) {
                if (resuming) {
                    deleteTree(target.resolve(folder));
                } else if (containsFiles(target.resolve(folder))) {
                    throw new IOException("refusing to overwrite existing chunks in " + target.resolve(folder));
                }
                deleteTree(target.resolve(folder + STAGING));
            }
        }
        if (resuming) BleachMod.LOGGER.warn("History Mode: resuming an interrupted install, map folders were reset");
        Files.createDirectories(installed.getParent());
        Files.writeString(marker, "History Mode maps are being installed; the next start resets the map folders while this file exists.");

        for (var entry : targets.entrySet()) {
            for (String folder : MAP_FOLDERS) {
                Path from = source.resolve(entry.getKey()).resolve(folder);
                if (!Files.isDirectory(from)) continue;
                Path staging = entry.getValue().resolve(folder + STAGING);
                copyTree(from, staging);
                verifyCopy(from, staging);
            }
        }
        for (var entry : targets.entrySet()) {
            for (String folder : MAP_FOLDERS) {
                Path staging = entry.getValue().resolve(folder + STAGING);
                if (!Files.isDirectory(staging)) continue;
                Path dest = entry.getValue().resolve(folder);
                deleteTree(dest);
                Files.move(staging, dest);
            }
            BleachMod.LOGGER.info("History Mode: installed map '{}' into {}", entry.getKey(), entry.getValue());
        }
        Path pending = installed.resolveSibling(INSTALLED + ".tmp");
        Files.copy(manifest, pending, StandardCopyOption.REPLACE_EXISTING);
        Files.move(pending, installed, StandardCopyOption.REPLACE_EXISTING);
        Files.delete(marker);
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
        try (Stream<Path> files = Files.walk(from)) {
            for (Path file : (Iterable<Path>) files::iterator) {
                Path dest = to.resolve(from.relativize(file).toString());
                if (Files.isDirectory(file)) Files.createDirectories(dest);
                else Files.copy(file, dest);
            }
        }
    }

    /** A short copy (full disk, antivirus, sync client) must not be committed as a finished map. */
    private static void verifyCopy(Path from, Path to) throws IOException {
        long[] expected = footprint(from), actual = footprint(to);
        if (expected[0] != actual[0] || expected[1] != actual[1]) {
            throw new IOException("incomplete copy of " + from + ": " + actual[0] + "/" + expected[0] + " files, "
                    + actual[1] + "/" + expected[1] + " bytes");
        }
    }

    private static long[] footprint(Path folder) throws IOException {
        long[] result = new long[2];
        try (Stream<Path> files = Files.walk(folder)) {
            for (Path file : (Iterable<Path>) files::iterator) {
                if (!Files.isRegularFile(file)) continue;
                result[0]++;
                result[1] += Files.size(file);
            }
        }
        return result;
    }

    private static void deleteTree(Path folder) throws IOException {
        if (Files.notExists(folder)) return;
        try (Stream<Path> files = Files.walk(folder)) {
            for (Path file : files.sorted(Comparator.reverseOrder()).toList()) Files.delete(file);
        }
    }
}
