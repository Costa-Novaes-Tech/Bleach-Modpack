package com.bleachmod.gametest;

import com.bleachmod.Reference;
import com.bleachmod.common.data.PlayerCapability;
import com.bleachmod.common.data.PlayerData;
import com.bleachmod.common.quest.*;
import com.bleachmod.entity.QuestNpcEntity;
import com.bleachmod.registry.ModEntities;
import com.bleachmod.server.events.QuestEvents;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

import java.util.UUID;

/** Exercise server-owned stages in a disposable GameTest level; the actual Acidwire encounter and checkpoints. */
@GameTestHolder("bleachmod")
@PrefixGameTestTemplate(false)
public final class OrihimeQuestGameTests {
    private static final String KEY = "shinigami_substitute:2";
    private static final String CHAD = "shinigami_substitute:3";

    private static FakePlayer player(GameTestHelper helper) {
        var player = new FakePlayer(helper.getLevel(), new GameProfile(UUID.randomUUID(), "OrihimeQA"));
        data(player).initializeShinigami();
        return player;
    }

    private static PlayerData data(FakePlayer player) {
        return PlayerCapability.get(player).orElseThrow(() -> new IllegalStateException("Missing capability"));
    }

    private static QuestNpcEntity rukia(GameTestHelper helper, String context) {
        var npc = ModEntities.NPC_RUKIA.get().create(helper.getLevel());
        var pos = helper.absolutePos(new BlockPos(1, 3, 1));
        npc.moveTo(pos.getX() + .5, pos.getY(), pos.getZ() + .5);
        npc.setQuestContext(context);
        helper.getLevel().addFreshEntity(npc);
        return npc;
    }

    private static void platform(GameTestHelper helper) {
        for (int x = 4; x <= 10; x++) for (int z = -2; z <= 4; z++)
            helper.setBlock(new BlockPos(x, 2, z), Blocks.STONE);
    }

    private static void death(GameTestHelper helper, FakePlayer player) {
        QuestEvents.onEntityKill(new LivingDeathEvent(player, helper.getLevel().damageSources().generic()));
    }

    @GameTest(template = "empty")
    public static void secondRukiaRequiresOpeningAndCannotUseFirstRukia(GameTestHelper helper) {
        var player = player(helper);
        var first = rukia(helper, "karakura_inicio");
        var second = rukia(helper, "karakura_orihime");
        player.setPos(second.position());
        platform(helper);
        helper.assertTrue(!QuestAvailabilityChecker.isVisible(data(player), QuestRegistry.getQuest(KEY)), "Second quest visible before opening completion");
        QuestService.startQuest(player, KEY, second);
        helper.assertTrue(!data(player).getPlayerQuestData().isAccepted(KEY), "Second quest accepted before opening completion");
        data(player).getPlayerQuestData().completeQuest(RukiaOpeningQuest.KEY);
        helper.assertTrue(QuestAvailabilityChecker.isVisible(data(player), QuestRegistry.getQuest(KEY)), "Opening completion did not unlock second quest");
        QuestService.startQuest(player, KEY, second);
        var progress = data(player).getPlayerQuestData().getProgress(KEY);
        helper.assertTrue(progress != null && !progress.isEncounterStarted(), "Acceptance started second boss automatically");
        RukiaOpeningQuest.dialogue(player, first, KEY);
        helper.assertTrue(RukiaOpeningQuest.stage(progress) == 0 && !progress.isEncounterStarted(), "First Rukia started second quest");
        RukiaOpeningQuest.dialogue(player, second, KEY);
        helper.assertTrue(progress.isEncounterStarted() && RukiaOpeningQuest.stage(progress) == 1, "Second Rukia did not start Acidwire after introduction");
        helper.assertTrue(StoryQuestService.pendingReason(QuestRegistry.getQuest(KEY)) == null, "Registered Acidwire is still pending");
        helper.assertTrue(second.getUUID().equals(progress.getStoryTargetUuid()), "Second quest bound to wrong Rukia instance");
        RukiaOpeningQuest.dialogue(player, second, KEY);
        helper.assertTrue(progress.getObjectiveProgress(1) == 0 && !data(player).getPlayerQuestData().isCompleted(KEY), "Repeated dialogue granted victory");
        helper.assertTrue(!QuestAvailabilityChecker.isVisible(data(player), QuestRegistry.getQuest(CHAD)), "Pending boss unlocked Chad");
        StoryQuestService.cleanup(player, KEY);
        first.discard(); second.discard(); helper.succeed();
    }

    @GameTest(template = "empty")
    public static void secondQuestRespawnKeepsCheckpointAndFinalDialoguePaysOnlyOnce(GameTestHelper helper) {
        var player = player(helper);
        var second = rukia(helper, "karakura_orihime");
        var first = rukia(helper, "karakura_inicio");
        player.setPos(second.position()); platform(helper);
        data(player).getPlayerQuestData().completeQuest(RukiaOpeningQuest.KEY);
        QuestService.startQuest(player, KEY, second);
        var progress = data(player).getPlayerQuestData().getProgress(KEY);
        death(helper, player);
        helper.assertTrue(progress.getStatus() == QuestStatus.ACCEPTED && RukiaOpeningQuest.stage(progress) == 0, "Death before introduction reset acceptance");
        RukiaOpeningQuest.dialogue(player, second, KEY);
        helper.assertTrue(progress.isEncounterStarted(), "Real Acidwire was not started");
        UUID oldAttempt = progress.getAttemptId();
        death(helper, player);
        helper.assertTrue(RukiaOpeningQuest.isRetryPending(progress), "Death left combat active");
        helper.assertTrue(progress.getObjectiveProgress(0) == 1 && !oldAttempt.equals(progress.getAttemptId()), "Death erased introduction or reused attempt");

        var respawned = new FakePlayer(helper.getLevel(), player.getGameProfile());
        respawned.setPos(second.position()); player.invalidateCaps();
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.entity.player.PlayerEvent.Clone(respawned, player, true));
        var saved = data(respawned).getPlayerQuestData().getProgress(KEY);
        helper.assertTrue(RukiaOpeningQuest.isRetryPending(saved) && second.getUUID().equals(saved.getStoryTargetUuid()), "Respawn lost checkpoint or second Rukia binding");
        RukiaOpeningQuest.retry(respawned, KEY);
        RukiaOpeningQuest.retry(respawned, KEY);
        helper.assertTrue(saved.isEncounterStarted(), "Acidwire retry failed");
        var boss = (com.bleachmod.entity.AcidwireEntity) helper.getLevel().getEntity(saved.getEncounterActor());
        helper.assertTrue(boss != null, "Actual Acidwire missing on retry");
        boss.hurt(helper.getLevel().damageSources().playerAttack(respawned), 100000);
        helper.assertTrue(RukiaOpeningQuest.stage(saved) == 2 && saved.getStatus() == QuestStatus.ACCEPTED, "Victory skipped final conversation");
        helper.assertTrue(!QuestAvailabilityChecker.isVisible(data(respawned), QuestRegistry.getQuest(CHAD)), "Victory unlocked Chad before return");
        death(helper, respawned);
        helper.assertTrue(saved.getObjectiveProgress(1) == 1 && saved.getStatus() == QuestStatus.ACCEPTED, "Death after victory reset boss stage");
        var fromDisk = QuestProgress.load(saved.save());
        helper.assertTrue(fromDisk.getObjectiveProgress(1) == 1 && second.getUUID().equals(fromDisk.getStoryTargetUuid()), "Save/load lost return stage");
        int before = respawned.totalExperience;
        RukiaOpeningQuest.dialogue(respawned, first, KEY);
        helper.assertTrue(!data(respawned).getPlayerQuestData().isCompleted(KEY), "First Rukia concluded second quest");
        RukiaOpeningQuest.dialogue(respawned, second, KEY);
        helper.assertTrue(data(respawned).getPlayerQuestData().isCompleted(KEY), "Second Rukia did not conclude quest");
        helper.assertTrue(respawned.totalExperience == before + 100, "Final dialogue did not pay 100 test XP");
        RukiaOpeningQuest.dialogue(respawned, second, KEY);
        QuestService.claimReward(respawned, KEY, -1);
        helper.assertTrue(respawned.totalExperience == before + 100, "Repeated dialogue/reward claim duplicated XP");
        helper.assertTrue(QuestAvailabilityChecker.isVisible(data(respawned), QuestRegistry.getQuest(CHAD)), "Final dialogue did not unlock Chad");
        first.discard(); second.discard(); helper.succeed();
    }

    @GameTest(template = "empty")
    public static void lostActorRecoversCombatWithoutErasingIntroductionOrGrantingVictory(GameTestHelper helper) {
        var player = player(helper);
        var second = rukia(helper, "karakura_orihime");
        player.setPos(second.position()); platform(helper);
        data(player).getPlayerQuestData().completeQuest(RukiaOpeningQuest.KEY);
        QuestService.startQuest(player, KEY, second);
        RukiaOpeningQuest.dialogue(player, second, KEY);
        var progress = data(player).getPlayerQuestData().getProgress(KEY);
        var actor = (com.bleachmod.entity.AcidwireEntity) helper.getLevel().getEntity(progress.getEncounterActor());
        helper.assertTrue(actor != null && progress.isEncounterStarted(), "Acidwire missing for loss recovery");
        UUID attempt = progress.getAttemptId();
        RukiaOpeningQuest.recoverMissingEncounter(player);
        helper.assertTrue(progress.isEncounterStarted() && attempt.equals(progress.getAttemptId()), "Live encounter was reset");
        var saved = QuestProgress.load(progress.save());
        helper.assertTrue(actor.getUUID().equals(saved.getEncounterActor()) && actor.blockPosition().equals(saved.getEncounterPos()), "Actor tracking lost in save/load");
        actor.hurt(helper.getLevel().damageSources().generic(), 100000);
        helper.assertTrue(!actor.isAlive(), "Environmental death did not occur");
        RukiaOpeningQuest.recoverMissingEncounter(player);
        helper.assertTrue(RukiaOpeningQuest.isRetryPending(progress) && progress.getObjectiveProgress(0) == 1, "Environmental death left limbo or erased introduction");
        helper.assertTrue(progress.getObjectiveProgress(1) == 0 && !attempt.equals(progress.getAttemptId()), "Environmental death granted victory or reused attempt");
        helper.assertTrue(!QuestAvailabilityChecker.isVisible(data(player), QuestRegistry.getQuest(CHAD)), "Environmental death unlocked Chad");

        // Distinguish a discarded actor from a kill: neither may strand the saved battle.
        RukiaOpeningQuest.retry(player, KEY);
        var removed = helper.getLevel().getEntity(progress.getEncounterActor());
        helper.assertTrue(removed instanceof com.bleachmod.entity.AcidwireEntity, "Retry did not create Acidwire");
        removed.discard(); RukiaOpeningQuest.recoverMissingEncounter(player);
        helper.assertTrue(RukiaOpeningQuest.isRetryPending(progress) && progress.getObjectiveProgress(0) == 1, "Discarded actor stranded combat");
        actor.discard(); second.discard(); helper.succeed();
    }

    @GameTest(template = "empty")
    public static void canonicalOldDefinitionMigratesWithoutOverwritingCustomizedContracts(GameTestHelper helper) {
        try {
            var oldJson = StoryQuestDefaults.bundled().getAsJsonArray("quests").get(1).getAsJsonObject().deepCopy();
            oldJson.addProperty("version", 1); oldJson.addProperty("quest_giver", "rukia");
            oldJson.getAsJsonObject("story").addProperty("start_mode", "NPC");
            var objectives = new com.google.gson.JsonArray();
            objectives.add(oldJson.getAsJsonArray("objectives").get(1));
            oldJson.add("objectives", objectives); oldJson.add("rewards", new com.google.gson.JsonArray());
            helper.assertTrue(RukiaOpeningQuest.legacyOrihimeDefinition(oldJson), "Canonical old Orihime contract not recognized");
            var oldQuest = QuestParser.parseQuest(oldJson, "shinigami_substitute");
            var current = QuestRegistry.getQuest(KEY);
            var partial = new QuestProgress(KEY); partial.initializeRequirements(oldQuest);
            partial.setStatus(QuestStatus.ACCEPTED); partial.setEncounterStarted(true);
            var attempt = partial.getAttemptId();
            helper.assertTrue(partial.bindDefinition(current), "Canonical partial contract did not migrate");
            helper.assertTrue(partial.getStatus() == QuestStatus.ACCEPTED && RukiaOpeningQuest.stage(partial) == 0
                    && !partial.isEncounterStarted() && !attempt.equals(partial.getAttemptId()), "Partial migration retained stale battle or fabricated completed stages");
            var completed = new QuestProgress(KEY); completed.initializeRequirements(oldQuest);
            completed.setStatus(QuestStatus.SUCCESS); completed.setObjectiveProgress(0, 1);
            helper.assertTrue(completed.bindDefinition(current) && completed.getStatus() == QuestStatus.SUCCESS
                    && completed.allObjectivesComplete(current), "Migration erased an old completion");

            var custom = oldJson.deepCopy();
            custom.getAsJsonArray("objectives").get(0).getAsJsonObject().addProperty("count", 2);
            helper.assertTrue(!RukiaOpeningQuest.legacyOrihimeDefinition(custom), "Customized old objective was recognized as canonical");
            var customProgress = new QuestProgress(KEY);
            customProgress.initializeRequirements(QuestParser.parseQuest(custom, "shinigami_substitute"));
            customProgress.setStatus(QuestStatus.ACCEPTED);
            helper.assertTrue(!customProgress.bindDefinition(current), "Migration overwrote customized progress contract");

            var root = java.nio.file.Path.of("qa-orihime-migration", UUID.randomUUID().toString());
            var path = root.resolve("quests/shinigami_substitute/02.json");
            java.nio.file.Files.createDirectories(path.getParent());
            String original = oldJson.toString(); java.nio.file.Files.writeString(path, original);
            StoryQuestDefaults.writeIfMissing(root);
            helper.assertTrue(java.nio.file.Files.readString(path.resolveSibling("02.json.pre-stages.bak")).equals(original), "Migration did not preserve exact backup");
            var migrated = com.google.gson.JsonParser.parseString(java.nio.file.Files.readString(path)).getAsJsonObject();
            helper.assertTrue(migrated.get("version").getAsInt() == 2 && migrated.getAsJsonArray("objectives").size() == 3, "Old saved quest file was not migrated");
            StoryQuestDefaults.writeIfMissing(root);
            helper.assertTrue(java.nio.file.Files.readString(path.resolveSibling("02.json.pre-stages.bak")).equals(original), "Repeat provisioning overwrote backup");
            helper.succeed();
        } catch (java.io.IOException error) {
            throw new IllegalStateException("Orihime migration QA", error);
        }
    }
}
