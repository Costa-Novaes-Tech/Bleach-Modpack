package com.bleachmod.common.quest;

import com.bleachmod.common.data.PlayerCapability;
import com.bleachmod.common.network.SyncHelper;
import com.bleachmod.entity.QuestNpcEntity;
import com.bleachmod.init.ModItems;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

/** Server-owned introductions and combat, with a return dialogue for the two Rukia encounters. */
public final class RukiaOpeningQuest {
    public static final String KEY = "shinigami_substitute:1";
    public static final String ORIHIME_KEY = "shinigami_substitute:2";
    public static final String CHAD_KEY = "shinigami_substitute:3";
    public static final String CHAD_NPC = "sado_yasutora";
    public static final String CHAD_CONTEXT = "karakura_periquito";
    public static final String SHRIEKER_ACTOR = "story_shrieker";
    public static boolean isOrihime(Quest q) { return q != null && q.isStory() && "MAIN-SUB-02".equals(q.getStory().id()); }
    public static boolean isChad(Quest q) { return q != null && q.isStory() && "MAIN-SUB-03".equals(q.getStory().id()); }
    public static boolean isStaged(Quest q) {
        if (isOpening(q)) return true;
        boolean chad = isChad(q);
        if ((!chad && !isOrihime(q)) || q.getObjectives().size() != (chad ? 2 : 3)
                || q.getObjectives().stream().anyMatch(o -> o.getRequired() != 1)) return false;
        return q.getObjectives().get(0) instanceof com.bleachmod.common.quest.objectives.StoryObjective first
                && first.action() == com.bleachmod.common.quest.objectives.StoryObjective.Action.INTERACT
                && q.getObjectives().get(1) instanceof com.bleachmod.common.quest.objectives.KillObjective
                && (chad || (q.getObjectives().get(2) instanceof com.bleachmod.common.quest.objectives.StoryObjective last
                && last.action() == com.bleachmod.common.quest.objectives.StoryObjective.Action.INTERACT));
    }
    public static String textPrefix(Quest q) {
        if (isChad(q)) return "bleachmod.story.sub_03.";
        return isOrihime(q) ? "bleachmod.story.sub_02." : "bleachmod.story.sub_01.";
    }
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
    public static boolean legacyOrihimeDefinition(com.google.gson.JsonObject json) {
        try {
            var expected = StoryQuestDefaults.bundled().getAsJsonArray("quests").get(1).getAsJsonObject().deepCopy();
            expected.addProperty("version", 1);
            expected.addProperty("quest_giver", "rukia");
            expected.getAsJsonObject("story").addProperty("start_mode", "NPC");
            var objectives = new com.google.gson.JsonArray();
            objectives.add(expected.getAsJsonArray("objectives").get(1));
            expected.add("objectives", objectives); expected.add("rewards", new com.google.gson.JsonArray());
            var contract = json.deepCopy();
            // Saved progress contains normalized contracts, while files contain full definitions.
            if (!contract.has("title")) {
                var old = QuestParser.parseQuest(expected, "shinigami_substitute");
                return com.google.gson.JsonParser.parseString(old.progressDefinition()).equals(contract);
            }
            return QuestParser.parseQuest(expected, "shinigami_substitute").toJson().equals(
                    QuestParser.parseQuest(contract, "shinigami_substitute").toJson());
        } catch (java.io.IOException | RuntimeException error) { return false; }
    }
    public static boolean legacyChadDefinition(com.google.gson.JsonObject json) {
        try {
            var expected = StoryQuestDefaults.bundled().getAsJsonArray("quests").get(2).getAsJsonObject().deepCopy();
            expected.addProperty("version", 1);
            expected.getAsJsonArray("objectives").remove(0);
            var old = QuestParser.parseQuest(expected, "shinigami_substitute");
            if (!json.has("title")) return com.google.gson.JsonParser.parseString(old.progressDefinition()).equals(json);
            return old.toJson().equals(QuestParser.parseQuest(json, "shinigami_substitute").toJson());
        } catch (java.io.IOException | RuntimeException error) { return false; }
    }
    public static int stage(QuestProgress p) {
        if (p == null || p.getObjectiveProgress(0) == 0) return 0;
        return p.getObjectiveProgress(1) == 0 ? 1 : 2;
    }
    public static boolean visibleObjective(Quest q, QuestProgress p, int index) {
        return !isStaged(q) || index <= stage(p);
    }
    public static boolean isRukia(QuestNpcEntity npc) {
        return npc.isAlive() && "rukia".equals(npc.getNpcId()) && "karakura_inicio".equals(npc.getQuestContext())
                && npc.level().dimension() == net.minecraft.world.level.Level.OVERWORLD;
    }
    public static boolean canTalk(QuestProgress p, QuestNpcEntity npc) {
        return canTalk(p, npc, KEY);
    }
    public static boolean canTalk(QuestProgress p, QuestNpcEntity npc, String key) {
        Quest q = QuestRegistry.getQuest(key);
        int stage = stage(p);
        return p != null && p.getStatus() == QuestStatus.ACCEPTED && isStaged(q) && QuestAvailabilityChecker.isOfferedBy(q, npc)
                && npc.getUUID().equals(p.getStoryTargetUuid())
                && (stage == 0 || isRetryPending(p) || (stage == 2 && !isChad(q)));
    }
    public static boolean isRetryPending(QuestProgress p) {
        return p != null && p.getStatus() == QuestStatus.ACCEPTED && stage(p) == 1 && !p.isEncounterStarted();
    }
    public static void retry(ServerPlayer player) { retry(player, KEY); }
    public static void retry(ServerPlayer player, String key) {
        Quest quest = QuestRegistry.getQuest(key);
        if (!isStaged(quest) || isChad(quest)) return;
        var data = PlayerCapability.get(player).orElse(null);
        if (data != null && isRetryPending(data.getPlayerQuestData().getProgress(key)))
            StoryQuestService.beginOpening(player, key);
    }
    /** Restarts only the active combat when this encounter's checkpoint policy applies. */
    public static void onPlayerDeath(ServerPlayer player) { onPlayerDeath(player, KEY); }
    public static void onPlayerDeath(ServerPlayer player, String key) {
        if (!isStaged(QuestRegistry.getQuest(key))) return;
        var data = PlayerCapability.get(player).orElse(null);
        if (data == null) return;
        var p = data.getPlayerQuestData().getProgress(key);
        if (p == null || p.getStatus() != QuestStatus.ACCEPTED) return;
        if (stage(p) == 1 && p.isEncounterStarted()) {
            StoryQuestService.cleanup(player, key);
            p.restartObjectiveAttempt(1);
        }
        // Introduction and return dialogue contain no partial action to roll back.
        com.bleachmod.server.events.QuestNavigationEvents.refresh(player);
        SyncHelper.progression(player);
    }
    /** Called after normal kill credit, so a legitimate victory cannot be turned into a retry. */
    public static void onUncreditedBossDeath(ServerPlayer owner, net.minecraft.world.entity.LivingEntity boss) {
        var data = PlayerCapability.get(owner).orElse(null);
        var q = QuestRegistry.getQuest(KEY);
        if (data == null || !isOpening(q)) return;
        var p = data.getPlayerQuestData().getProgress(KEY);
        var tag = boss.getPersistentData();
        if (p == null || p.getStatus() != QuestStatus.ACCEPTED || !p.matchesDefinition(q) || !p.isEncounterStarted() || stage(p) != 1
                || !tag.hasUUID(com.bleachmod.Reference.TAG_QUEST_OWNER) || !owner.getUUID().equals(tag.getUUID(com.bleachmod.Reference.TAG_QUEST_OWNER))
                || !tag.hasUUID("blc_quest_attempt") || !tag.getUUID("blc_quest_attempt").equals(p.getAttemptId())
                || !(q.getObjectives().get(1) instanceof com.bleachmod.common.quest.objectives.KillObjective kill)
                || !kill.matches(boss, KEY, 1)) return;
        onPlayerDeath(owner); // Same combat checkpoint policy; does not require the owner to die.
        owner.displayClientMessage(net.minecraft.network.chat.Component.translatable("bleachmod.story.sub_01.encounter_lost"), false);
    }

    /** Also repairs a save already stranded before this fix, without forcing any chunks to load. */
    public static void recoverMissingEncounter(ServerPlayer player, QuestNpcEntity npc) {
        var data = PlayerCapability.get(player).orElse(null);
        if (data == null || player.level() != npc.level() || player.distanceToSqr(npc) > 36 || !isRukia(npc)) return;
        var p = data.getPlayerQuestData().getProgress(KEY);
        var q = QuestRegistry.getQuest(KEY);
        if (!isOpening(q) || p == null || !p.matchesDefinition(q) || p.getStatus() != QuestStatus.ACCEPTED
                || !p.isEncounterStarted() || stage(p) != 1 || !npc.getUUID().equals(p.getStoryTargetUuid())) return;
        for (var level : player.server.getAllLevels()) for (var entity : level.getAllEntities()) {
            var tag = entity.getPersistentData();
            if (entity.isAlive() && KEY.equals(tag.getString(com.bleachmod.Reference.TAG_QUEST_KEY))
                    && tag.hasUUID(com.bleachmod.Reference.TAG_QUEST_OWNER) && player.getUUID().equals(tag.getUUID(com.bleachmod.Reference.TAG_QUEST_OWNER))
                    && tag.hasUUID("blc_quest_attempt") && p.getAttemptId().equals(tag.getUUID("blc_quest_attempt"))) return;
        }
        onPlayerDeath(player);
    }
    /** Recover vanished/dead actors only in loaded space. An unloaded chunk never grants victory. */
    public static void recoverMissingEncounter(ServerPlayer player) {
        var data = PlayerCapability.get(player).orElse(null);
        if (data == null) return;
        for (String key : new String[]{KEY, ORIHIME_KEY, CHAD_KEY}) {
            Quest q = QuestRegistry.getQuest(key);
            var p = data.getPlayerQuestData().getProgress(key);
            if (!isStaged(q) || p == null || !p.matchesDefinition(q) || p.getStatus() != QuestStatus.ACCEPTED || stage(p) != 1
                    || !p.isEncounterStarted() || p.getEncounterActor() == null || p.getEncounterPos() == null) continue;
            var level = player.server.getLevel(net.minecraft.resources.ResourceKey.create(net.minecraft.core.registries.Registries.DIMENSION, q.getStory().dimension()));
            if (level == null) continue;
            var actor = level.getEntity(p.getEncounterActor());
            if (actor instanceof net.minecraft.world.entity.LivingEntity living && living.isAlive()) { p.trackEncounter(actor); continue; }
            if (level.hasChunkAt(p.getEncounterPos())) onPlayerDeath(player, key);
        }
    }
    public static void dialogue(ServerPlayer player, QuestNpcEntity npc) { dialogue(player, npc, KEY); }
    public static void dialogue(ServerPlayer player, QuestNpcEntity npc, String key) {
        var data = PlayerCapability.get(player).orElse(null);
        Quest q = QuestRegistry.getQuest(key);
        if (data == null || !isStaged(q) || !data.getStatus().hasCreatedCharacter()
                || !"shinigami".equals(data.getCharacter().getRace()) || player.level() != npc.level()
                || player.distanceToSqr(npc) > 36) return;
        var p = data.getPlayerQuestData().getProgress(key);
        if (!canTalk(p, npc, key) || !p.matchesDefinition(q)) return;
        if (stage(p) == 0 || isRetryPending(p)) {
            if (!isOpening(q) && stage(p) == 0) {
                QuestService.incrementObjective(player, data, key, q, 0, 1);
                if (stage(p) == 0) return;
            }
            StoryQuestService.beginOpening(player, key);
        } else {
            QuestService.incrementObjective(player, data, key, q, 2, 1);
            if (isOrihime(q) && data.getPlayerQuestData().isCompleted(key)) QuestService.claimReward(player, key, -1);
            if (data.getPlayerQuestData().isCompleted(key) && isOpening(q) && !p.isStarterAsauchiGranted()) {
                ItemStack sword = new ItemStack(ModItems.ASAUCHI.get());
                if (player.getInventory().countItem(ModItems.ASAUCHI.get()) == 0 && !player.getOffhandItem().is(ModItems.ASAUCHI.get()))
                    if (!player.getInventory().add(sword)) player.drop(sword, false);
                p.markStarterAsauchiGranted();
                SyncHelper.progression(player);
            }
        }
    }
}
