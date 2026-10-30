package com.bleachmod.gametest;

import com.bleachmod.Reference;
import com.bleachmod.common.data.PlayerCapability;
import com.bleachmod.common.quest.*;
import com.bleachmod.entity.QuestNpcEntity;
import com.bleachmod.registry.ModEntities;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.gametest.*;
import java.util.UUID;

@GameTestHolder("bleachmod")
@PrefixGameTestTemplate(false)
public final class StoryQuestGameTests {
    private static FakePlayer player(GameTestHelper helper) {
        var player = new FakePlayer(helper.getLevel(), new GameProfile(UUID.randomUUID(), "StoryQA"));
        PlayerCapability.get(player).orElseThrow(() -> new IllegalStateException("Missing capability")).initializeShinigami();
        return player;
    }
    private static QuestNpcEntity rukia(GameTestHelper helper, String context) {
        var npc = ModEntities.NPC_RUKIA.get().create(helper.getLevel());
        var pos = helper.absolutePos(new BlockPos(1, 3, 1));
        npc.moveTo(pos.getX() + .5, pos.getY(), pos.getZ() + .5);
        npc.setQuestContext(context); helper.getLevel().addFreshEntity(npc); return npc;
    }

    @GameTest(template = "empty")
    public static void wrongContextAndRaceCannotStartEvenAfterFailure(GameTestHelper helper) {
        var player = player(helper); var npc = rukia(helper, "karakura_cemiterio"); player.setPos(npc.position());
        helper.assertTrue(!QuestAvailabilityChecker.isOfferedBy(QuestRegistry.getQuest("rukia_npc_test"), npc), "Legacy boss leaked into a story context");
        var data = PlayerCapability.get(player).orElseThrow(() -> new IllegalStateException("Missing capability"));
        QuestService.startQuest(player, "shinigami_substitute:1", npc);
        helper.assertTrue(!data.getPlayerQuestData().isAccepted("shinigami_substitute:1"), "Wrong Rukia offered the opening");
        npc.setQuestContext("karakura_inicio"); data.getCharacter().setRace("hollow");
        data.getPlayerQuestData().acceptQuest("shinigami_substitute:1", QuestRegistry.getQuest("shinigami_substitute:1"));
        data.getPlayerQuestData().failQuest("shinigami_substitute:1");
        QuestService.startQuest(player, "shinigami_substitute:1", npc);
        helper.assertTrue(data.getPlayerQuestData().getStatus("shinigami_substitute:1") == QuestStatus.FAILED, "Failure bypassed race");
        npc.discard(); helper.succeed();
    }

    @GameTest(template = "empty")
    public static void missingBossIsAcceptedWithoutSpawnCompletionOrNextQuestUnlock(GameTestHelper helper) {
        var player = player(helper); var npc = rukia(helper, "karakura_orihime"); player.setPos(npc.position());
        var data = PlayerCapability.get(player).orElseThrow(() -> new IllegalStateException("Missing capability"));
        data.getPlayerQuestData().completeQuest("shinigami_substitute:1");
        QuestService.startQuest(player, "shinigami_substitute:2", npc);
        var progress = data.getPlayerQuestData().getProgress("shinigami_substitute:2");
        helper.assertTrue(progress != null && progress.getStatus() == QuestStatus.ACCEPTED, "Pending mission not accepted");
        helper.assertTrue(!progress.isEncounterStarted(), "Unregistered boss started");
        helper.assertTrue(StoryQuestService.pendingReason(QuestRegistry.getQuest("shinigami_substitute:2")) != null, "No pending explanation");
        QuestService.incrementObjective(player, data, "shinigami_substitute:2", QuestRegistry.getQuest("shinigami_substitute:2"), 0, 1);
        helper.assertTrue(!data.getPlayerQuestData().isCompleted("shinigami_substitute:2"), "Absent boss granted victory");
        helper.assertTrue(!QuestAvailabilityChecker.isVisible(data, QuestRegistry.getQuest("shinigami_substitute:3")), "Skipped pending encounter");
        npc.discard(); helper.succeed();
    }

    @GameTest(template = "empty")
    public static void realOpeningEncounterIsOwnedUniqueAndCompletes(GameTestHelper helper) {
        var player = player(helper); var npc = rukia(helper, "karakura_inicio"); player.setPos(npc.position());
        // Small platform for the actual six-block spawn; no existing save is involved.
        for (int x = 5; x <= 9; x++) for (int z = 0; z <= 3; z++) helper.setBlock(new BlockPos(x, 2, z), Blocks.STONE);
        var data = PlayerCapability.get(player).orElseThrow(() -> new IllegalStateException("Missing capability"));
        String key = "shinigami_substitute:1";
        QuestService.startQuest(player, key, npc);
        var progress = data.getPlayerQuestData().getProgress(key);
        helper.assertTrue(progress != null && progress.isEncounterStarted(), "Actual opening did not start");
        StoryQuestService.begin(player, key); QuestService.startQuest(player, key, npc);
        var actors = new java.util.ArrayList<net.minecraft.world.entity.Entity>();
        for (var entity : helper.getLevel().getAllEntities()) if (key.equals(entity.getPersistentData().getString(Reference.TAG_QUEST_KEY))
                && entity.getPersistentData().hasUUID(Reference.TAG_QUEST_OWNER)
                && player.getUUID().equals(entity.getPersistentData().getUUID(Reference.TAG_QUEST_OWNER))) actors.add(entity);
        helper.assertTrue(actors.size() == 1, "Repeated action duplicated encounter");
        var second = player(helper);
        var other = PlayerCapability.get(second).orElseThrow(() -> new IllegalStateException("Missing capability"));
        other.getPlayerQuestData().acceptQuest(key, QuestRegistry.getQuest(key));
        other.getPlayerQuestData().getProgress(key).setEncounterStarted(true);
        var actor = (net.minecraft.world.entity.LivingEntity) actors.get(0);
        var currentAttempt = actor.getPersistentData().getUUID("blc_quest_attempt");
        actor.getPersistentData().putUUID("blc_quest_attempt", UUID.randomUUID());
        com.bleachmod.server.events.QuestEvents.onEntityKill(new net.minecraftforge.event.entity.living.LivingDeathEvent(actor,
                helper.getLevel().damageSources().playerAttack(player)));
        helper.assertTrue(!data.getPlayerQuestData().isCompleted(key), "An old attempt granted new credit");
        actor.getPersistentData().putUUID("blc_quest_attempt", currentAttempt);
        com.bleachmod.server.events.QuestEvents.onEntityKill(new net.minecraftforge.event.entity.living.LivingDeathEvent(actor,
                helper.getLevel().damageSources().playerAttack(second)));
        helper.assertTrue(!other.getPlayerQuestData().isCompleted(key), "Another player stole the personal encounter");
        actor.hurt(helper.getLevel().damageSources().playerAttack(player), 100000);
        helper.assertTrue(data.getPlayerQuestData().isCompleted(key), "Valid owned kill did not complete");
        helper.assertTrue(QuestAvailabilityChecker.isVisible(data, QuestRegistry.getQuest("shinigami_substitute:2")), "Next mission stayed hidden");
        var saved = npc.saveWithoutId(new net.minecraft.nbt.CompoundTag());
        var copy = ModEntities.NPC_RUKIA.get().create(helper.getLevel()); copy.load(saved);
        helper.assertTrue("karakura_inicio".equals(copy.getQuestContext()), "Context did not survive NBT");
        npc.discard(); copy.discard(); helper.succeed();
    }
}
