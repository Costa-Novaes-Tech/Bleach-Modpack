package com.bleachmod.common.quest;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

/** Bundled authored content is copied once; administrator edits and existing player records survive. */
public final class StoryQuestDefaults {
    private StoryQuestDefaults() {}
    public static JsonObject bundled() throws IOException {
        try (InputStream input = StoryQuestDefaults.class.getResourceAsStream("/data/bleachmod/quests/story_catalog.json")) {
            if (input == null) throw new IOException("Missing story catalog");
            return JsonParser.parseReader(new InputStreamReader(input, StandardCharsets.UTF_8)).getAsJsonObject();
        }
    }
    public static void writeIfMissing(Path root) throws IOException {
        JsonObject catalog = bundled();
        for (var entry : catalog.getAsJsonArray("sagas")) {
            Saga saga = Saga.fromJson(entry.getAsJsonObject());
            write(root.resolve("sagas").resolve(saga.getId() + ".json"), entry.getAsJsonObject());
        }
        for (var entry : catalog.getAsJsonArray("quests")) {
            JsonObject json = entry.getAsJsonObject().deepCopy();
            String sagaId = json.remove("sagaId").getAsString();
            Path path = root.resolve("quests").resolve(sagaId).resolve(String.format("%02d.json", json.get("id").getAsInt()));
            int id = json.get("id").getAsInt();
            if ("shinigami_substitute".equals(sagaId) && id <= 3 && Files.exists(path) && needsStagedMigration(path, id)) {
                Path backup = path.resolveSibling(path.getFileName() + ".pre-stages.bak");
                if (!Files.exists(backup)) Files.copy(path, backup);
                Files.writeString(path, new com.google.gson.GsonBuilder().setPrettyPrinting().create().toJson(json), StandardCharsets.UTF_8);
            } else write(path, json);
        }
    }
    private static boolean needsStagedMigration(Path path, int id) throws IOException {
        var previous = JsonParser.parseString(Files.readString(path)).getAsJsonObject();
        return switch (id) {
            case 1 -> RukiaOpeningQuest.legacyDefinition(previous);
            case 2 -> RukiaOpeningQuest.legacyOrihimeDefinition(previous);
            case 3 -> RukiaOpeningQuest.legacyChadDefinition(previous);
            default -> false;
        };
    }
    private static void write(Path path, JsonObject json) throws IOException {
        if (Files.exists(path)) return;
        Files.createDirectories(path.getParent());
        Files.writeString(path, new com.google.gson.GsonBuilder().setPrettyPrinting().create().toJson(json), StandardCharsets.UTF_8,
                StandardOpenOption.CREATE_NEW);
    }
}
