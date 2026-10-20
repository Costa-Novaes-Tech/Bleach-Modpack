package com.bleachmod.client.gui.layout;

import com.bleachmod.BleachMod;
import com.bleachmod.Reference;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * Loads assets/&lt;namespace&gt;/gui_layouts/*.json. Reloads with F3+T; in the development
 * client also re-reads the source file a moment after it is saved. A broken file keeps
 * the last good version on screen and shows the error message instead of crashing.
 */
public final class GuiLayouts extends SimpleJsonResourceReloadListener {
    public static final GuiLayouts INSTANCE = new GuiLayouts();
    private static final String FOLDER = "gui_layouts";

    private final Map<String, GuiLayout> layouts = new HashMap<>();
    private final Map<String, String> errors = new HashMap<>();
    private final Map<String, Long> devStamps = new HashMap<>();
    private final Map<String, Long> devChecks = new HashMap<>();

    private GuiLayouts() {
        super(new Gson(), FOLDER);
    }

    /** The layout for {@code bleachmod:<id>}, or null if it never loaded successfully. */
    public static GuiLayout get(String id) {
        INSTANCE.pollDevFile(id);
        return INSTANCE.layouts.get(id);
    }

    /** Last load error for this layout, or null when it is healthy. */
    public static String error(String id) {
        return INSTANCE.errors.get(id);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> files, ResourceManager manager, ProfilerFiller profiler) {
        layouts.clear();
        errors.clear();
        devStamps.clear();
        GuiTextures.clear();
        files.forEach((location, json) -> {
            if (!location.getNamespace().equals(Reference.MOD_ID)) return;
            String id = location.getPath();
            try {
                layouts.put(id, GuiLayoutParser.parse(id, json.getAsJsonObject()));
            } catch (RuntimeException e) {
                errors.put(id, e.getMessage());
                BleachMod.LOGGER.error("GUI layout {} failed to load: {}", location, e.getMessage());
            }
        });
    }

    private void pollDevFile(String id) {
        if (!DevResources.enabled()) return;
        long now = System.currentTimeMillis();
        Long lastCheck = devChecks.get(id);
        if (lastCheck != null && now - lastCheck < 300) return;
        devChecks.put(id, now);

        Path file = DevResources.dir(Reference.MOD_ID, FOLDER).resolve(id + ".json");
        try {
            if (!Files.isRegularFile(file)) return;
            long stamp = Files.getLastModifiedTime(file).toMillis();
            Long previous = devStamps.put(id, stamp);
            if (previous != null && previous == stamp) return;
            String json = Files.readString(file, StandardCharsets.UTF_8);
            layouts.put(id, GuiLayoutParser.parse(id, json));
            if (errors.remove(id) != null || previous != null)
                BleachMod.LOGGER.info("GUI layout {} reloaded from {}", id, file);
        } catch (GuiLayoutParser.LayoutException e) {
            errors.put(id, e.getMessage());
        } catch (IOException e) {
            errors.put(id, "nao foi possivel ler " + file + ": " + e.getMessage());
        }
    }
}
