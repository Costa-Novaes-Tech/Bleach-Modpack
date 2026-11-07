package com.bleachmod.common.quest;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

/** Narrative identity is independent of an entity UUID and of generated coordinates. */
public record StoryQuestInfo(String id, String offerContext, StartMode startMode,
                             String targetNpc, String targetContext, ResourceLocation dimension,
                             String pendingContent, String epilogue) {
    public enum StartMode { NPC, PANEL }

    public static StoryQuestInfo fromJson(JsonObject json) {
        String id = json.get("id").getAsString();
        if (!id.matches("MAIN-[A-Z0-9-]{1,48}")) throw new IllegalArgumentException("Invalid story id");
        String offer = json.get("offer_context").getAsString();
        String npc = json.get("target_npc").getAsString();
        String context = json.get("target_context").getAsString();
        if (!validContext(offer) || !validContext(context) || !npc.matches("[a-z0-9_]{1,64}"))
            throw new IllegalArgumentException("Invalid narrative context");
        ResourceLocation dimension = ResourceLocation.tryParse(json.get("dimension").getAsString());
        if (dimension == null) throw new IllegalArgumentException("Invalid story dimension");
        return new StoryQuestInfo(id, offer, StartMode.valueOf(json.get("start_mode").getAsString()), npc,
                context, dimension, json.has("pending_content") ? json.get("pending_content").getAsString() : "",
                json.has("epilogue") ? json.get("epilogue").getAsString() : "");
    }

    public static boolean validContext(String value) { return value != null && value.matches("[a-z0-9_]{1,64}"); }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("id", id); json.addProperty("offer_context", offerContext);
        json.addProperty("start_mode", startMode.name()); json.addProperty("target_npc", targetNpc);
        json.addProperty("target_context", targetContext); json.addProperty("dimension", dimension.toString());
        if (!pendingContent.isBlank()) json.addProperty("pending_content", pendingContent);
        if (!epilogue.isBlank()) json.addProperty("epilogue", epilogue);
        return json;
    }
}
