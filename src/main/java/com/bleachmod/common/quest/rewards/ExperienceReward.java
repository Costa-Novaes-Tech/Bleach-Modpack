package com.bleachmod.common.quest.rewards;

import com.bleachmod.common.data.PlayerData;
import com.google.gson.JsonObject;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

/** Vanilla experience points, independent of spiritual progression. */
public final class ExperienceReward extends QuestReward {
    private final int amount;
    public ExperienceReward(int amount) {
        if (amount <= 0 || amount > 100000) throw new IllegalArgumentException("Invalid XP reward");
        this.amount = amount;
    }
    @Override public void give(ServerPlayer player, PlayerData data) { player.giveExperiencePoints(amount); }
    @Override public Component describe() { return Component.translatable("bleachmod.reward.xp", amount); }
    @Override public JsonObject toJson() {
        var json = new JsonObject(); json.addProperty("type", "XP"); json.addProperty("amount", amount); return json;
    }
}
