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
        helper.assertTrue(QuestRegistry.getQuest("rukia_npc_test") == null, "Legacy boss quest remains active");
        var data = PlayerCapability.get(player).orElseThrow(() -> new IllegalStateException("Missing capability"));
        QuestService.startQuest(player, "shinigami_substitute:1", npc);
        RukiaOpeningQuest.dialogue(player, npc);
        helper.assertTrue(!data.getPlayerQuestData().getProgress(RukiaOpeningQuest.KEY).isEncounterStarted(), "Wrong Rukia started the boss");
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
        helper.assertTrue(player.getInventory().countItem(com.bleachmod.init.ModItems.ASAUCHI.get()) == 0, "Character initialization gave Asauchi");
        // Small platform for the actual six-block spawn; no existing save is involved.
        for (int x = 4; x <= 10; x++) for (int z = -2; z <= 4; z++) helper.setBlock(new BlockPos(x, 2, z), Blocks.STONE);
        var data = PlayerCapability.get(player).orElseThrow(() -> new IllegalStateException("Missing capability"));
        String key = "shinigami_substitute:1";
        QuestService.startQuest(player, key, npc);
        var progress = data.getPlayerQuestData().getProgress(key);
        helper.assertTrue(progress != null && !progress.isEncounterStarted(), "Acceptance started the fight");
        helper.assertTrue(npc.getUUID().equals(data.getPlayerQuestData().getWaypoint(key).entityId()), "Compass did not bind first Rukia");
        RukiaOpeningQuest.dialogue(player, npc);
        helper.assertTrue(progress.isEncounterStarted(), "Dialogue did not start boss");
        helper.assertTrue(!progress.isStarterAsauchiGranted() && player.getInventory().countItem(com.bleachmod.init.ModItems.ASAUCHI.get()) == 0, "Sword given before final dialogue");
        StoryQuestService.begin(player, key); QuestService.startQuest(player, key, npc);
        helper.assertTrue(player.getInventory().countItem(com.bleachmod.init.ModItems.ASAUCHI.get()) == 0, "Repeated start gave Asauchi");
        var actors = new java.util.ArrayList<net.minecraft.world.entity.Entity>();
        for (var entity : helper.getLevel().getAllEntities()) if (key.equals(entity.getPersistentData().getString(Reference.TAG_QUEST_KEY))
                && entity.getPersistentData().hasUUID(Reference.TAG_QUEST_OWNER)
                && player.getUUID().equals(entity.getPersistentData().getUUID(Reference.TAG_QUEST_OWNER))) actors.add(entity);
        helper.assertTrue(actors.size() == 1, "Repeated action duplicated encounter");
        var second = player(helper);
        var other = PlayerCapability.get(second).orElseThrow(() -> new IllegalStateException("Missing capability"));
        other.getPlayerQuestData().acceptQuest(key, QuestRegistry.getQuest(key));
        other.getPlayerQuestData().getProgress(key).setEncounterStarted(true);
        other.getPlayerQuestData().getProgress(key).setObjectiveProgress(0, 1);
        var actor = (net.minecraft.world.entity.LivingEntity) actors.get(0);
        helper.assertTrue(actor instanceof com.bleachmod.entity.HollowBossEntity, "Opening spawned a common Hollow instead of the boss");
        ((com.bleachmod.entity.HollowBossEntity) actor).finishEntrance();
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
        helper.assertTrue(!data.getPlayerQuestData().isCompleted(key) && RukiaOpeningQuest.stage(progress) == 2, "Kill skipped return to Rukia");
        helper.assertTrue(!QuestAvailabilityChecker.isVisible(data, QuestRegistry.getQuest("shinigami_substitute:2")), "Next mission unlocked before final dialogue");
        var wrongRukia = rukia(helper, "karakura_inicio");
        RukiaOpeningQuest.dialogue(player, wrongRukia);
        helper.assertTrue(!data.getPlayerQuestData().isCompleted(key), "Duplicate Rukia completed the quest");
        wrongRukia.discard();
        RukiaOpeningQuest.dialogue(player, npc);
        RukiaOpeningQuest.dialogue(player, npc);
        helper.assertTrue(data.getPlayerQuestData().isCompleted(key), "Final dialogue did not complete");
        helper.assertTrue(progress.isStarterAsauchiGranted() && player.getInventory().countItem(com.bleachmod.init.ModItems.ASAUCHI.get()) == 1, "Final dialogue did not grant exactly one Asauchi");
        helper.assertTrue(QuestAvailabilityChecker.isVisible(data, QuestRegistry.getQuest("shinigami_substitute:2")), "Next mission stayed hidden");
        var saved = npc.saveWithoutId(new net.minecraft.nbt.CompoundTag());
        var copy = ModEntities.NPC_RUKIA.get().create(helper.getLevel()); copy.load(saved);
        helper.assertTrue("karakura_inicio".equals(copy.getQuestContext()), "Context did not survive NBT");
        npc.discard(); copy.discard(); helper.succeed();
    }

    @GameTest(template = "empty")
    public static void finalDialogueWithFullInventoryDropsOnlyOneSword(GameTestHelper helper) {
        var player = player(helper); var npc = rukia(helper, "karakura_inicio"); player.setPos(npc.position());
        var data = PlayerCapability.get(player).orElseThrow(() -> new IllegalStateException("Missing capability"));
        QuestService.startQuest(player, RukiaOpeningQuest.KEY);
        var p = data.getPlayerQuestData().getProgress(RukiaOpeningQuest.KEY);
        p.setEncounterStarted(true); p.setObjectiveProgress(0, 1); p.setObjectiveProgress(1, 1);
        for (int i = 0; i < player.getInventory().items.size(); i++)
            player.getInventory().items.set(i, new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.STONE, 64));
        var area = player.getBoundingBox().inflate(4);
        int before = helper.getLevel().getEntitiesOfClass(net.minecraft.world.entity.item.ItemEntity.class, area,
                e -> e.getItem().is(com.bleachmod.init.ModItems.ASAUCHI.get())).size();
        RukiaOpeningQuest.dialogue(player, npc); RukiaOpeningQuest.dialogue(player, npc);
        int after = helper.getLevel().getEntitiesOfClass(net.minecraft.world.entity.item.ItemEntity.class, area,
                e -> e.getItem().is(com.bleachmod.init.ModItems.ASAUCHI.get())).size();
        helper.assertTrue(after == before + 1 && p.isStarterAsauchiGranted(), "Full inventory lost or duplicated sword");
        helper.assertTrue(data.getPlayerQuestData().isCompleted(RukiaOpeningQuest.KEY), "Final conversation did not complete");
        npc.discard(); helper.succeed();
    }

    @GameTest(template = "empty")
    public static void deathAndRespawnPreserveOpeningCheckpoints(GameTestHelper helper) {
        var player = player(helper); var npc = rukia(helper, "karakura_inicio"); player.setPos(npc.position());
        var data = PlayerCapability.get(player).orElseThrow(() -> new IllegalStateException("Missing capability"));
        String key = RukiaOpeningQuest.KEY;
        QuestService.startQuest(player, key);
        var p = data.getPlayerQuestData().getProgress(key);
        com.bleachmod.server.events.QuestEvents.onEntityKill(new net.minecraftforge.event.entity.living.LivingDeathEvent(player, helper.getLevel().damageSources().generic()));
        helper.assertTrue(p.getStatus() == QuestStatus.ACCEPTED && RukiaOpeningQuest.stage(p) == 0, "Death before introduction removed acceptance");
        for (int x = 4; x <= 10; x++) for (int z = -2; z <= 4; z++) helper.setBlock(new BlockPos(x, 2, z), Blocks.STONE);
        RukiaOpeningQuest.dialogue(player, npc);
        var attempt = p.getAttemptId();
        com.bleachmod.entity.HollowBossEntity oldBoss = null;
        for (var entity : helper.getLevel().getAllEntities())
            if (entity instanceof com.bleachmod.entity.HollowBossEntity boss && entity.getPersistentData().hasUUID(Reference.TAG_QUEST_OWNER)
                    && player.getUUID().equals(entity.getPersistentData().getUUID(Reference.TAG_QUEST_OWNER))) oldBoss = boss;
        helper.assertTrue(oldBoss != null, "Boss missing before death");
        com.bleachmod.server.events.QuestEvents.onEntityKill(new net.minecraftforge.event.entity.living.LivingDeathEvent(player, helper.getLevel().damageSources().generic()));
        helper.assertTrue(oldBoss.isRemoved(), "Old boss survived failed combat attempt");
        helper.assertTrue(p.getObjectiveProgress(0) == 1 && RukiaOpeningQuest.isRetryPending(p), "Death reset the introduction");
        helper.assertTrue(!attempt.equals(p.getAttemptId()), "Dead attempt identity reused");
        helper.assertTrue(npc.getUUID().equals(p.getStoryTargetUuid()), "Death lost the first Rukia binding");
        var respawned = new FakePlayer(helper.getLevel(), player.getGameProfile()); respawned.setPos(npc.position());
        player.invalidateCaps();
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.entity.player.PlayerEvent.Clone(respawned, player, true));
        var restored = PlayerCapability.get(respawned).orElseThrow(() -> new IllegalStateException("Missing respawn capability"));
        var saved = restored.getPlayerQuestData().getProgress(key);
        helper.assertTrue(RukiaOpeningQuest.isRetryPending(saved) && saved.getObjectiveProgress(0) == 1, "Respawn lost checkpoint");
        RukiaOpeningQuest.retry(respawned); RukiaOpeningQuest.retry(respawned);
        helper.assertTrue(saved.isEncounterStarted() && saved.getObjectiveProgress(0) == 1, "Retry required introduction again");
        int actors = 0; com.bleachmod.entity.HollowBossEntity newBoss = null;
        for (var entity : helper.getLevel().getAllEntities())
            if (entity instanceof com.bleachmod.entity.HollowBossEntity boss && entity.getPersistentData().hasUUID(Reference.TAG_QUEST_OWNER)
                    && respawned.getUUID().equals(entity.getPersistentData().getUUID(Reference.TAG_QUEST_OWNER))) { actors++; newBoss = boss; }
        helper.assertTrue(actors == 1, "Retry duplicated boss");
        com.bleachmod.server.events.QuestEvents.onEntityKill(new net.minecraftforge.event.entity.living.LivingDeathEvent(oldBoss, helper.getLevel().damageSources().playerAttack(respawned)));
        helper.assertTrue(saved.getObjectiveProgress(1) == 0, "Old boss credited the retry");
        newBoss.finishEntrance(); newBoss.hurt(helper.getLevel().damageSources().playerAttack(respawned), 100000);
        helper.assertTrue(RukiaOpeningQuest.stage(saved) == 2, "Retry boss did not count");
        com.bleachmod.server.events.QuestEvents.onEntityKill(new net.minecraftforge.event.entity.living.LivingDeathEvent(respawned, helper.getLevel().damageSources().generic()));
        helper.assertTrue(saved.getStatus() == QuestStatus.ACCEPTED && saved.getObjectiveProgress(1) == 1, "Death on return reset boss victory");
        helper.assertTrue(!QuestAvailabilityChecker.isVisible(restored, QuestRegistry.getQuest("shinigami_substitute:2")), "Death unlocked next mission early");
        RukiaOpeningQuest.dialogue(respawned, npc); RukiaOpeningQuest.dialogue(respawned, npc);
        helper.assertTrue(restored.getPlayerQuestData().isCompleted(key) && respawned.getInventory().countItem(com.bleachmod.init.ModItems.ASAUCHI.get()) == 1,
                "Return after death lost or duplicated final sword");
        restored.getPlayerQuestData().acceptQuest("shinigami_substitute:2", QuestRegistry.getQuest("shinigami_substitute:2"));
        com.bleachmod.server.events.QuestEvents.onEntityKill(new net.minecraftforge.event.entity.living.LivingDeathEvent(respawned, helper.getLevel().damageSources().generic()));
        helper.assertTrue(restored.getPlayerQuestData().getStatus("shinigami_substitute:2") == QuestStatus.FAILED, "Opening checkpoint policy leaked into another quest");
        npc.discard(); helper.succeed();
    }

    @GameTest(template = "empty")
    public static void failedStartGivesNothingAndRetryCannotFarmAsauchi(GameTestHelper helper) {
        var player = player(helper); var npc = rukia(helper, "karakura_inicio");
        var data = PlayerCapability.get(player).orElseThrow(() -> new IllegalStateException("Missing capability"));
        player.setPos(npc.position().add(100, 0, 0));
        String key = "shinigami_substitute:1";
        QuestService.startQuest(player, key, npc);
        RukiaOpeningQuest.dialogue(player, npc);
        helper.assertTrue(player.getInventory().countItem(com.bleachmod.init.ModItems.ASAUCHI.get()) == 0, "Invalid start gave Asauchi");
        player.setPos(npc.position());
        for (int x = 4; x <= 10; x++) for (int z = -2; z <= 4; z++) helper.setBlock(new BlockPos(x, 2, z), Blocks.STONE);
        QuestService.startQuest(player, key, npc);
        RukiaOpeningQuest.dialogue(player, npc);
        helper.assertTrue(data.getPlayerQuestData().getProgress(key).isEncounterStarted(), "First encounter failed to start");
        player.getInventory().clearContent();
        StoryQuestService.cleanup(player, key);
        data.getPlayerQuestData().failQuest(key);
        QuestService.startQuest(player, key, npc);
        RukiaOpeningQuest.dialogue(player, npc);
        helper.assertTrue(data.getPlayerQuestData().getProgress(key).isEncounterStarted(), "Retry did not start");
        helper.assertTrue(player.getInventory().countItem(com.bleachmod.init.ModItems.ASAUCHI.get()) == 0, "Retry farmed another Asauchi");
        StoryQuestService.cleanup(player, key); npc.discard(); helper.succeed();
    }
}
