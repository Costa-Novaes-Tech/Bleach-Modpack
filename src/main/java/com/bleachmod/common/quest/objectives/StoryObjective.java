package com.bleachmod.common.quest.objectives;

import com.google.gson.JsonObject;
import net.minecraft.network.chat.Component;

/** Typed content contracts. Pending routes/training cannot be completed by a kill or a conversation. */
public final class StoryObjective extends QuestObjective {
    public enum Action { INTERACT, PURSUIT, HIT, EXPEDITION, SPHERE_AND_LAUNCH, ROUTE, CIRCUIT }
    private final Action action;
    private final String description;

    public StoryObjective(Action action, String description) {
        super(ObjectiveType.STORY, 1);
        this.action = action;
        if (!description.matches("[a-z0-9_.]{1,160}")) throw new IllegalArgumentException("Invalid objective description");
        this.description = description;
    }
    public Action action() { return action; }
    @Override public Component describe() { return Component.translatable(description); }
    @Override public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("type", "STORY"); json.addProperty("action", action.name());
        json.addProperty("description", description); json.addProperty("count", 1);
        return json;
    }
}
