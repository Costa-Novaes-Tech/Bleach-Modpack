package com.bleachmod.common.quest;

import com.bleachmod.common.data.PlayerCapability;
import com.bleachmod.common.network.SyncHelper;
import com.bleachmod.entity.QuestNpcEntity;
import com.bleachmod.init.ModItems;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

/** The opening has three server-owned stages: introduction, boss, return dialogue. */
public final class RukiaOpeningQuest {
    public static final String KEY = "shinigami_substitute:1";
    private RukiaOpeningQuest() {}
    public static boolean isOpening(Quest q) { return q != null && q.isStory() && "MAIN-SUB-01".equals(q.getStory().id()); }
    public static boolean legacyDefinition(com.google.gson.JsonObject json) {
        if (!json.has("story") || !"MAIN-SUB-01".equals(json.getAsJsonObject("story").get("id").getAsString())
                || (json.has("version") && json.get("version").getAsInt() != 1)
                || !json.has("objectives") || json.getAsJsonArray("objectives").size() != 1) return false;
        var objective = json.getAsJsonArray("objectives").get(0).getAsJsonObject();
        return objective.has("entity") && java.util.Set.of("bleachmod:hollow", "bleachmod:hollow_boss").contains(objective.get("entity").getAsString())
                && "KILL".equals(objective.get("type").getAsString()) && objective.get("count").getAsInt() == 1;
    }
    public static int stage(QuestProgress p) {
        if (p == null || p.getObjectiveProgress(0) == 0) return 0;
        return p.getObjectiveProgress(1) == 0 ? 1 : 2;
    }
    public static boolean visibleObjective(Quest q, QuestProgress p, int index) {
        return !isOpening(q) || index <= stage(p);
    }
    public static boolean isRukia(QuestNpcEntity npc) {
        return npc.isAlive() && "rukia".equals(npc.getNpcId()) && "karakura_inicio".equals(npc.getQuestContext())
                && npc.level().dimension() == net.minecraft.world.level.Level.OVERWORLD;
    }
    public static boolean canTalk(QuestProgress p, QuestNpcEntity npc) {
        return p != null && p.getStatus() == QuestStatus.ACCEPTED && isRukia(npc)
                && npc.getUUID().equals(p.getStoryTargetUuid()) && (stage(p) != 1 || isRetryPending(p));
    }
    public static boolean isRetryPending(QuestProgress p) {
        return p != null && p.getStatus() == QuestStatus.ACCEPTED && stage(p) == 1 && !p.isEncounterStarted();
    }
    public static void retry(ServerPlayer player) {
        var data = PlayerCapability.get(player).orElse(null);
        if (data != null && isRetryPending(data.getPlayerQuestData().getProgress(KEY)))
            StoryQuestService.beginOpening(player, KEY);
    }
    /** Only this opening uses checkpoints on death. Other quests keep their own failure rules. */
    public static void onPlayerDeath(ServerPlayer player) {
        var data = PlayerCapability.get(player).orElse(null);
        if (data == null) return;
        var p = data.getPlayerQuestData().getProgress(KEY);
        if (p == null || p.getStatus() != QuestStatus.ACCEPTED) return;
        if (stage(p) == 1 && p.isEncounterStarted()) {
            StoryQuestService.cleanup(player, KEY);
            p.restartObjectiveAttempt(1);
        }
        // Introduction and return dialogue contain no partial action to roll back.
        com.bleachmod.server.events.QuestNavigationEvents.refresh(player);
        SyncHelper.progression(player);
    }
    public static void dialogue(ServerPlayer player, QuestNpcEntity npc) {
        var data = PlayerCapability.get(player).orElse(null);
        Quest q = QuestRegistry.getQuest(KEY);
        if (data == null || !isOpening(q) || !data.getStatus().hasCreatedCharacter()
                || !"shinigami".equals(data.getCharacter().getRace()) || player.level() != npc.level()
                || player.distanceToSqr(npc) > 36) return;
        var p = data.getPlayerQuestData().getProgress(KEY);
        if (!canTalk(p, npc) || !p.matchesDefinition(q)) return;
        if (stage(p) == 0 || isRetryPending(p)) {
            StoryQuestService.beginOpening(player, KEY);
        } else {
            QuestService.incrementObjective(player, data, KEY, q, 2, 1);
            if (data.getPlayerQuestData().isCompleted(KEY) && !p.isStarterAsauchiGranted()) {
                ItemStack sword = new ItemStack(ModItems.ASAUCHI.get());
                if (player.getInventory().countItem(ModItems.ASAUCHI.get()) == 0 && !player.getOffhandItem().is(ModItems.ASAUCHI.get()))
                    if (!player.getInventory().add(sword)) player.drop(sword, false);
                p.markStarterAsauchiGranted();
                SyncHelper.progression(player);
            }
        }
    }
}
