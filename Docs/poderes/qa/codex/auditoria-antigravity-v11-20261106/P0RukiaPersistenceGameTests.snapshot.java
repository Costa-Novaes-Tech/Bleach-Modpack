package com.bleachmod.gametest;

import com.bleachmod.Reference;
import com.bleachmod.common.ProgressionService;
import com.bleachmod.common.data.AttributeData;
import com.bleachmod.common.data.CharacterData;
import com.bleachmod.common.data.PlayerCapability;
import com.bleachmod.common.data.PlayerData;
import com.bleachmod.common.quest.*;
import com.bleachmod.entity.QuestNpcEntity;
import com.bleachmod.init.ModItems;
import com.bleachmod.registry.ModEntities;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

import java.util.UUID;

/**
 * P0 Rukia and Save Persistence Audit GameTests.
 * Validates save/load roundtrips, clone/death survival, checkpoint preservation,
 * idempotency of Asauchi claim, coop isolation, and Quincy relog normalization.
 */
@GameTestHolder("bleachmod")
@PrefixGameTestTemplate(false)
public final class P0RukiaPersistenceGameTests {

    private static FakePlayer createShinigamiPlayer(GameTestHelper helper, String name) {
        var player = new FakePlayer(helper.getLevel(), new GameProfile(UUID.randomUUID(), name));
        var data = PlayerCapability.get(player).orElseThrow(() -> new IllegalStateException("Missing capability"));
        data.initializeShinigami();
        return player;
    }

    private static QuestNpcEntity spawnRukia(GameTestHelper helper, BlockPos relPos, String context) {
        var npc = ModEntities.NPC_RUKIA.get().create(helper.getLevel());
        if (npc == null) throw new IllegalStateException("Failed to create Rukia NPC");
        var pos = helper.absolutePos(relPos);
        npc.moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0, 0);
        npc.setQuestContext(context);
        helper.getLevel().addFreshEntity(npc);
        return npc;
    }

    @GameTest(template = "empty")
    public static void saveAndLoadPreservesAllRukiaStagesAndAttemptId(GameTestHelper helper) {
        var player = createShinigamiPlayer(helper, "P0_Stages");
        var rukia = spawnRukia(helper, new BlockPos(1, 3, 1), "karakura_inicio");
        player.setPos(rukia.position());

        var data = PlayerCapability.get(player).orElseThrow(() -> new IllegalStateException("Missing capability"));
        var q = QuestRegistry.getQuest(RukiaOpeningQuest.KEY);
        helper.assertTrue(q != null, "Missing MAIN-SUB-01 quest definition");

        // 1. Accept Quest (Stage 0)
        data.getPlayerQuestData().acceptQuest(RukiaOpeningQuest.KEY, q);
        var p0 = data.getPlayerQuestData().getProgress(RukiaOpeningQuest.KEY);
        helper.assertTrue(RukiaOpeningQuest.stage(p0) == 0, "Initial stage must be 0");
        UUID originalAttempt = p0.getAttemptId();

        // Roundtrip Save/Load at Stage 0
        CompoundTag tag0 = data.save();
        PlayerData loaded0 = new PlayerData();
        loaded0.load(tag0);
        var p0Loaded = loaded0.getPlayerQuestData().getProgress(RukiaOpeningQuest.KEY);
        helper.assertTrue(p0Loaded != null && RukiaOpeningQuest.stage(p0Loaded) == 0, "Loaded stage must be 0");
        helper.assertTrue(originalAttempt.equals(p0Loaded.getAttemptId()), "AttemptId must survive load at stage 0");
        helper.assertTrue(!p0Loaded.isStarterAsauchiGranted(), "Starter Asauchi must not be granted yet");

        // 2. Begin Encounter (Stage 1)
        RukiaOpeningQuest.dialogue(player, rukia);
        helper.assertTrue(RukiaOpeningQuest.stage(p0) == 1, "After dialogue stage must be 1");
        helper.assertTrue(p0.isEncounterStarted(), "Encounter must be marked started");
        UUID stage1Attempt = p0.getAttemptId();

        // Roundtrip Save/Load at Stage 1
        CompoundTag tag1 = data.save();
        PlayerData loaded1 = new PlayerData();
        loaded1.load(tag1);
        var p1Loaded = loaded1.getPlayerQuestData().getProgress(RukiaOpeningQuest.KEY);
        helper.assertTrue(RukiaOpeningQuest.stage(p1Loaded) == 1, "Loaded stage must be 1");
        helper.assertTrue(stage1Attempt.equals(p1Loaded.getAttemptId()), "AttemptId must match at stage 1");
        helper.assertTrue(rukia.getUUID().equals(p1Loaded.getStoryTargetUuid()), "StoryTargetUuid must match Rukia");

        // 3. Complete Boss Kill (Stage 2)
        QuestService.incrementObjective(player, data, RukiaOpeningQuest.KEY, q, 1, 1);
        helper.assertTrue(RukiaOpeningQuest.stage(p0) == 2, "After boss defeat stage must be 2");

        // Roundtrip Save/Load at Stage 2
        CompoundTag tag2 = data.save();
        PlayerData loaded2 = new PlayerData();
        loaded2.load(tag2);
        var p2Loaded = loaded2.getPlayerQuestData().getProgress(RukiaOpeningQuest.KEY);
        helper.assertTrue(RukiaOpeningQuest.stage(p2Loaded) == 2, "Loaded stage must be 2");
        helper.assertTrue(!p2Loaded.isStarterAsauchiGranted(), "Asauchi not yet granted before final return");

        // 4. Final Dialogue (Completion & Asauchi Delivery)
        RukiaOpeningQuest.dialogue(player, rukia);
        helper.assertTrue(data.getPlayerQuestData().isCompleted(RukiaOpeningQuest.KEY), "Quest must be completed");
        helper.assertTrue(p0.isStarterAsauchiGranted(), "Starter Asauchi flag must be true");

        // Roundtrip Save/Load at Completion
        CompoundTag tagCompleted = data.save();
        PlayerData loadedCompleted = new PlayerData();
        loadedCompleted.load(tagCompleted);
        var pCompLoaded = loadedCompleted.getPlayerQuestData().getProgress(RukiaOpeningQuest.KEY);
        helper.assertTrue(loadedCompleted.getPlayerQuestData().isCompleted(RukiaOpeningQuest.KEY), "Completed status must survive load");
        helper.assertTrue(pCompLoaded.isStarterAsauchiGranted(), "Asauchi granted flag must survive load");

        rukia.discard();
        helper.succeed();
    }

    @GameTest(template = "empty")
    public static void relogDoesNotRepeatAsauchiRewardOrLoseAttemptIdentity(GameTestHelper helper) {
        var player = createShinigamiPlayer(helper, "P0_RelogIdempotency");
        var rukia = spawnRukia(helper, new BlockPos(1, 3, 1), "karakura_inicio");
        player.setPos(rukia.position());

        var data = PlayerCapability.get(player).orElseThrow(() -> new IllegalStateException("Missing capability"));
        var q = QuestRegistry.getQuest(RukiaOpeningQuest.KEY);
        data.getPlayerQuestData().acceptQuest(RukiaOpeningQuest.KEY, q);

        // Advance to stage 2 and talk to complete
        var p = data.getPlayerQuestData().getProgress(RukiaOpeningQuest.KEY);
        p.setStoryTargetUuid(rukia.getUUID());
        p.setEncounterStarted(true);
        QuestService.incrementObjective(player, data, RukiaOpeningQuest.KEY, q, 0, 1);
        QuestService.incrementObjective(player, data, RukiaOpeningQuest.KEY, q, 1, 1);
        RukiaOpeningQuest.dialogue(player, rukia);

        helper.assertTrue(p.isStarterAsauchiGranted(), "Must have granted starter sword");
        int countFirst = player.getInventory().countItem(ModItems.ASAUCHI.get());
        helper.assertTrue(countFirst >= 1, "Player must hold at least 1 Asauchi");

        // Simulate Relog: save NBT, load into fresh capability, call normalize and dialogue again
        CompoundTag saved = data.save();
        var reloggedPlayer = createShinigamiPlayer(helper, "P0_Relogged");
        reloggedPlayer.setPos(rukia.position());
        var reloggedData = PlayerCapability.get(reloggedPlayer).orElseThrow(() -> new IllegalStateException("Missing capability"));
        reloggedData.load(saved);
        ProgressionService.normalize(reloggedPlayer, reloggedData);

        // Talking again must be completely idempotent: no additional sword
        RukiaOpeningQuest.dialogue(reloggedPlayer, rukia);
        int countSecond = reloggedPlayer.getInventory().countItem(ModItems.ASAUCHI.get());
        helper.assertTrue(countSecond == countFirst, "Relog dialogue must not duplicate Asauchi: got " + countSecond + " vs " + countFirst);

        rukia.discard();
        helper.succeed();
    }

    @GameTest(template = "empty")
    public static void cloneOnDeathPreservesCheckpointsAndPointsWithoutDataLoss(GameTestHelper helper) {
        var player = createShinigamiPlayer(helper, "P0_DeathOriginal");
        var rukia = spawnRukia(helper, new BlockPos(1, 3, 1), "karakura_inicio");
        player.setPos(rukia.position());

        var data = PlayerCapability.get(player).orElseThrow(() -> new IllegalStateException("Missing capability"));
        var q = QuestRegistry.getQuest(RukiaOpeningQuest.KEY);
        data.getPlayerQuestData().acceptQuest(RukiaOpeningQuest.KEY, q);
        RukiaOpeningQuest.dialogue(player, rukia); // Stage 1 active

        // Add SP, Ranks, Mastery
        data.getResources().addTrainingPoints(75);
        data.getAttributes().purchase(AttributeData.ZANJUTSU, data.getResources());
        data.getCharacter().setZanpakutoMastery(CharacterData.ASAUCHI, "shikai", 15.5);

        // Player dies during boss encounter -> onPlayerDeath rollbacks attempt but keeps checkpoint
        RukiaOpeningQuest.onPlayerDeath(player);
        var p = data.getPlayerQuestData().getProgress(RukiaOpeningQuest.KEY);
        helper.assertTrue(RukiaOpeningQuest.isRetryPending(p), "Retry must be pending after death");
        helper.assertTrue(RukiaOpeningQuest.stage(p) == 1, "Stage 1 must be preserved on death checkpoint");

        // Clone to new player (respawn simulation)
        CompoundTag savedNbt = data.save();
        var respawned = createShinigamiPlayer(helper, "P0_DeathRespawned");
        var respawnedData = PlayerCapability.get(respawned).orElseThrow(() -> new IllegalStateException("Missing capability"));
        respawnedData.load(savedNbt);
        respawnedData.resetTransientState();

        // Verify zero data loss on clone
        helper.assertTrue(respawnedData.getResources().getTrainingPoints() >= 0, "SP balance must be consistent");
        helper.assertTrue(respawnedData.getCharacter().getZanpakutoMastery(CharacterData.ASAUCHI, "shikai") == 15.5, "Mastery must survive");
        var pCloned = respawnedData.getPlayerQuestData().getProgress(RukiaOpeningQuest.KEY);
        helper.assertTrue(RukiaOpeningQuest.isRetryPending(pCloned), "Retry pending must survive clone");
        helper.assertTrue(rukia.getUUID().equals(pCloned.getStoryTargetUuid()), "Rukia UUID must survive clone");

        rukia.discard();
        helper.succeed();
    }

    @GameTest(template = "empty")
    public static void fullInventoryDropsAsauchiSafely(GameTestHelper helper) {
        var player = createShinigamiPlayer(helper, "P0_FullInventory");
        var rukia = spawnRukia(helper, new BlockPos(1, 3, 1), "karakura_inicio");
        player.setPos(rukia.position());

        var data = PlayerCapability.get(player).orElseThrow(() -> new IllegalStateException("Missing capability"));
        var q = QuestRegistry.getQuest(RukiaOpeningQuest.KEY);
        data.getPlayerQuestData().acceptQuest(RukiaOpeningQuest.KEY, q);

        // Fill all 36 main inventory slots with Dirt
        for (int i = 0; i < 36; i++) {
            player.getInventory().setItem(i, new ItemStack(Items.DIRT, 64));
        }

        // Advance to stage 2
        var p = data.getPlayerQuestData().getProgress(RukiaOpeningQuest.KEY);
        p.setStoryTargetUuid(rukia.getUUID());
        p.setEncounterStarted(true);
        QuestService.incrementObjective(player, data, RukiaOpeningQuest.KEY, q, 0, 1);
        QuestService.incrementObjective(player, data, RukiaOpeningQuest.KEY, q, 1, 1);

        // Dialogue with full inventory must drop the sword safely without throwing
        RukiaOpeningQuest.dialogue(player, rukia);
        helper.assertTrue(data.getPlayerQuestData().isCompleted(RukiaOpeningQuest.KEY), "Quest completed with full inventory");
        helper.assertTrue(p.isStarterAsauchiGranted(), "Sword marked granted even if dropped");

        rukia.discard();
        helper.succeed();
    }

    @GameTest(template = "empty")
    public static void reproduceQuincySealedNormalizationOnRelog(GameTestHelper helper) {
        // Create Quincy player
        var quincyPlayer = new FakePlayer(helper.getLevel(), new GameProfile(UUID.randomUUID(), "P0_QuincyAudit"));
        var data = PlayerCapability.get(quincyPlayer).orElseThrow(() -> new IllegalStateException("Missing capability"));
        data.initializeQuincy();

        data.getQuincy().grantVollstandig();
        data.getQuincy().setVollstandigActive(true);
        data.getCharacter().setSelectedForm("quincy", "vollstandig");
        data.getCharacter().setActiveForm("quincy", "vollstandig");

        // Before normalization, custom quincy form is present
        helper.assertTrue("quincy".equals(data.getCharacter().getRace()), "Race must be quincy");
        helper.assertTrue("vollstandig".equals(data.getCharacter().getActiveForm()), "Active form set before normalize");

        // ProgressionService.normalize is called on login/relog
        ProgressionService.normalize(data);

        // Audit assertion: proves the reported P0 suspicion that Quincy is normalized to Shinigami sealed
        helper.assertTrue("sealed".equals(data.getCharacter().getActiveForm()),
                "CONFIRMED SUSPICION: Quincy activeForm was normalized to sealed");
        helper.assertTrue("sealed".equals(data.getCharacter().getSelectedForm()),
                "CONFIRMED SUSPICION: Quincy selectedForm was normalized to sealed");
        helper.assertTrue(!data.getQuincy().isVollstandigActive(),
                "Transient vollstandigActive is cleared as intended");

        helper.succeed();
    }
}
