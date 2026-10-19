package com.bleachmod.server.world;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/** Map manifest of the running History Mode world: which map lives in which dimension and where it spawns. */
public final class HistoryWorld {
    public static final String HUMAN_WORLD = "human_world";
    public static final String SOUL_SOCIETY = "soul_society";
    public static final String HUECO_MUNDO = "hueco_mundo";

    public record Destination(String id, ResourceLocation dimensionId, double x, double y, double z, float yaw) {
        public ResourceKey<Level> dimension() {
            return ResourceKey.create(Registries.DIMENSION, dimensionId);
        }
    }

    private static volatile Map<String, Destination> active = Map.of();

    private HistoryWorld() {
    }

    public static boolean isActive() {
        return !active.isEmpty();
    }

    public static Optional<Destination> destination(String id) {
        return Optional.ofNullable(active.get(id));
    }

    static void activate(Map<String, Destination> destinations) {
        active = Collections.unmodifiableMap(new LinkedHashMap<>(destinations));
    }

    static void clear() {
        active = Map.of();
    }

    /** Reads {@code maps.json}; any malformed entry fails the whole file so a half-valid manifest is never used. */
    public static Map<String, Destination> parse(Path manifest) throws IOException {
        JsonObject root;
        try (Reader reader = Files.newBufferedReader(manifest)) {
            root = JsonParser.parseReader(reader).getAsJsonObject();
        } catch (RuntimeException e) {
            throw new IOException("invalid map manifest " + manifest, e);
        }
        Map<String, Destination> result = new LinkedHashMap<>();
        try {
            for (Map.Entry<String, com.google.gson.JsonElement> entry : root.getAsJsonObject("maps").entrySet()) {
                String id = entry.getKey();
                if (!id.matches("[a-z0-9_]{1,64}")) throw new IOException("invalid map id '" + id + "'");
                JsonObject map = entry.getValue().getAsJsonObject();
                JsonObject spawn = map.getAsJsonObject("spawn");
                double x = spawn.get("x").getAsDouble();
                double y = spawn.get("y").getAsDouble();
                double z = spawn.get("z").getAsDouble();
                float yaw = spawn.has("yaw") ? spawn.get("yaw").getAsFloat() : 0.0F;
                if (!Double.isFinite(x) || !Double.isFinite(y) || !Double.isFinite(z) || !Float.isFinite(yaw)) {
                    throw new IOException("non-finite spawn for map '" + id + "'");
                }
                ResourceLocation dimension = new ResourceLocation(map.get("dimension").getAsString());
                result.put(id, new Destination(id, dimension, x, y, z, yaw));
            }
        } catch (RuntimeException e) {
            throw new IOException("invalid map manifest " + manifest, e);
        }
        return result;
    }
}
