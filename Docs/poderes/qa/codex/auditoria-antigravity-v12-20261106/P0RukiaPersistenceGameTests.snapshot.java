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
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

import java.util.UUID;

/**
 * P0 Rukia and Save Persistence Audit GameTests.
 *
 * NOTA DE ESCOPO:
 * Esta suíte exercita os fluxos de serialização NBT, idempotência de recompensas,
 * integridade de checkpoints em morte/clone, detecção física de ItemEntity no chão,
 * recuperação de boss ausente sem softlock e isolamento de lifecycles da normalização Quincy.
 *
 * Distinções de escopo declaradas:
 * - Testes de "sessão restaurada" simulam a desserialização de capability e inventário
 *   em nível de servidor/GameTest. A homologação de relog com desconexão TCP de rede
 *   exige observador de cliente dedicado real.
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

    private static void setupEncounterGround(GameTestHelper helper) {
        for (int x = 4; x <= 10; x++) {
            for (int z = -2; z <= 4; z++) {
                helper.setBlock(new BlockPos(x, 2, z), Blocks.STONE);
            }
        }
    }

    @GameTest(template = "empty")
    public static void saveAndLoadPreservesAllRukiaStagesAndAttemptId(GameTestHelper helper) {
        setupEncounterGround(helper);
        var player = createShinigamiPlayer(helper, "P0_Stages");
        var rukia = spawnRukia(helper, new BlockPos(1, 3, 1), "karakura_inicio");
        player.setPos(rukia.position());

        QuestService.startQuest(player, RukiaOpeningQuest.KEY, rukia);
        var data = PlayerCapability.get(player).orElseThrow(() -> new IllegalStateException("Missing capability"));
        var q = QuestRegistry.getQuest(RukiaOpeningQuest.KEY);
        helper.assertTrue(q != null, "Missing MAIN-SUB-01 quest definition");

        // 1. Accept Quest (Stage 0)
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

        StoryQuestService.cleanup(player, RukiaOpeningQuest.KEY);
        rukia.discard();
        helper.succeed();
    }

    @GameTest(template = "empty")
    public static void simulatedSessionRestorationMaintainsSingleAsauchiClaim(GameTestHelper helper) {
        var player = createShinigamiPlayer(helper, "P0_SimulatedSession");
        var rukia = spawnRukia(helper, new BlockPos(1, 3, 1), "karakura_inicio");
        player.setPos(rukia.position());

        var data = PlayerCapability.get(player).orElseThrow(() -> new IllegalStateException("Missing capability"));
        var q = QuestRegistry.getQuest(RukiaOpeningQuest.KEY);
        data.getPlayerQuestData().acceptQuest(RukiaOpeningQuest.KEY, q);

        // Advance to stage 2 and talk to complete initial delivery
        var p = data.getPlayerQuestData().getProgress(RukiaOpeningQuest.KEY);
        p.setStoryTargetUuid(rukia.getUUID());
        p.setEncounterStarted(true);
        QuestService.incrementObjective(player, data, RukiaOpeningQuest.KEY, q, 0, 1);
        QuestService.incrementObjective(player, data, RukiaOpeningQuest.KEY, q, 1, 1);
        RukiaOpeningQuest.dialogue(player, rukia);

        helper.assertTrue(p.isStarterAsauchiGranted(), "Must have granted starter sword");
        int countInitial = player.getInventory().countItem(ModItems.ASAUCHI.get());
        helper.assertTrue(countInitial == 1, "Player must hold exactly 1 Asauchi after delivery, got: " + countInitial);

        // Capture both capability state (PlayerData) AND vanilla inventory state
        CompoundTag savedCaps = data.save();
        ListTag savedInventory = new ListTag();
        player.getInventory().save(savedInventory);

        // Create restored player session with identical GameProfile
        var restoredPlayer = new FakePlayer(helper.getLevel(), player.getGameProfile());
        restoredPlayer.setPos(rukia.position());
        restoredPlayer.getInventory().load(savedInventory);
        var restoredData = PlayerCapability.get(restoredPlayer).orElseThrow(() -> new IllegalStateException("Missing capability"));
        restoredData.load(savedCaps);
        ProgressionService.normalize(restoredPlayer, restoredData);

        // Verify restored session state before re-dialogue
        helper.assertTrue(restoredPlayer.getInventory().countItem(ModItems.ASAUCHI.get()) == 1,
                "Restored player inventory must contain exactly 1 Asauchi");
        var restoredProgress = restoredData.getPlayerQuestData().getProgress(RukiaOpeningQuest.KEY);
        helper.assertTrue(restoredProgress.isStarterAsauchiGranted(), "Restored capability retains starterAsauchiGranted flag");

        // Execute dialogue again on restored player
        AABB area = restoredPlayer.getBoundingBox().inflate(6);
        int droppedBefore = helper.getLevel().getEntitiesOfClass(ItemEntity.class, area, e -> e.getItem().is(ModItems.ASAUCHI.get())).size();
        RukiaOpeningQuest.dialogue(restoredPlayer, rukia);
        int droppedAfter = helper.getLevel().getEntitiesOfClass(ItemEntity.class, area, e -> e.getItem().is(ModItems.ASAUCHI.get())).size();

        // Exact delta assertions: zero new items in inventory and zero new dropped items
        int countAfterSecond = restoredPlayer.getInventory().countItem(ModItems.ASAUCHI.get());
        helper.assertTrue(countAfterSecond == countInitial, "Inventory Asauchi count delta must be 0 (no duplicate): " + countAfterSecond);
        helper.assertTrue(droppedAfter == droppedBefore, "World dropped Asauchi delta must be 0 (no dropped duplicate): " + (droppedAfter - droppedBefore));

        rukia.discard();
        helper.succeed();
    }

    @GameTest(template = "empty")
    public static void cloneOnDeathPreservesCheckpointsAndPointsWithoutDataLoss(GameTestHelper helper) {
        setupEncounterGround(helper);
        var player = createShinigamiPlayer(helper, "P0_DeathOriginal");
        var rukia = spawnRukia(helper, new BlockPos(1, 3, 1), "karakura_inicio");
        player.setPos(rukia.position());

        QuestService.startQuest(player, RukiaOpeningQuest.KEY, rukia);
        RukiaOpeningQuest.dialogue(player, rukia); // Stage 1 active
        var data = PlayerCapability.get(player).orElseThrow(() -> new IllegalStateException("Missing capability"));

        // Add exact 250 SP, purchase Zanjutsu level 1 (costs exactly 100 SP)
        data.getResources().addTrainingPoints(250);
        boolean bought = data.getAttributes().purchase(AttributeData.ZANJUTSU, data.getResources());
        helper.assertTrue(bought, "Purchase of Zanjutsu rank 1 must succeed");
        int expectedSp = 150; // 250 - 100
        int expectedZanjutsu = 1;
        double expectedMastery = 15.5;
        data.getCharacter().setZanpakutoMastery(CharacterData.ASAUCHI, "shikai", expectedMastery);

        // Player dies during boss encounter -> onPlayerDeath rollbacks attempt but keeps checkpoint
        RukiaOpeningQuest.onPlayerDeath(player);
        var p = data.getPlayerQuestData().getProgress(RukiaOpeningQuest.KEY);
        helper.assertTrue(RukiaOpeningQuest.isRetryPending(p), "Retry must be pending after death");
        helper.assertTrue(RukiaOpeningQuest.stage(p) == 1, "Stage 1 must be preserved on death checkpoint");
        UUID expectedAttempt = p.getAttemptId();
        UUID expectedTarget = p.getStoryTargetUuid();

        // Simulate Clone event: serialize provider data, deserialize into respawned player, execute resetTransientState
        CompoundTag savedNbt = data.save();
        var respawned = createShinigamiPlayer(helper, "P0_DeathRespawned");
        var respawnedData = PlayerCapability.get(respawned).orElseThrow(() -> new IllegalStateException("Missing capability"));
        respawnedData.load(savedNbt);
        respawnedData.resetTransientState();
        respawnedData.getCharacter().setActiveForm("zanpakuto", "sealed");

        // Verify EXACT equality without loose inequalities
        helper.assertTrue(respawnedData.getResources().getTrainingPoints() == expectedSp,
                "SP must be exactly " + expectedSp + " after clone, got: " + respawnedData.getResources().getTrainingPoints());
        helper.assertTrue(respawnedData.getAttributes().level(AttributeData.ZANJUTSU) == expectedZanjutsu,
                "Zanjutsu rank must be exactly " + expectedZanjutsu + " after clone, got: " + respawnedData.getAttributes().level(AttributeData.ZANJUTSU));
        helper.assertTrue(respawnedData.getCharacter().getZanpakutoMastery(CharacterData.ASAUCHI, "shikai") == expectedMastery,
                "Mastery must be exactly " + expectedMastery + " after clone");

        var pCloned = respawnedData.getPlayerQuestData().getProgress(RukiaOpeningQuest.KEY);
        helper.assertTrue(expectedAttempt.equals(pCloned.getAttemptId()), "AttemptId must match exactly after clone");
        helper.assertTrue(expectedTarget.equals(pCloned.getStoryTargetUuid()), "StoryTargetUuid must match Rukia after clone");
        helper.assertTrue(RukiaOpeningQuest.isRetryPending(pCloned), "Retry pending must survive clone");
        helper.assertTrue(RukiaOpeningQuest.stage(pCloned) == 1, "Stage 1 checkpoint must survive clone");

        // Omission safety verification: omit attributes tag on load
        CompoundTag omittedTag = data.save();
        omittedTag.remove("attributes");
        PlayerData omittedData = new PlayerData();
        omittedData.load(omittedTag);
        helper.assertTrue(omittedData.getAttributes().level(AttributeData.ZANJUTSU) == 0,
                "Omitted attributes tag must default safely to 0 without throwing");

        StoryQuestService.cleanup(player, RukiaOpeningQuest.KEY);
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

        // Observe ItemEntity before dialogue in 6-block radius
        AABB area = player.getBoundingBox().inflate(6);
        int before = helper.getLevel().getEntitiesOfClass(ItemEntity.class, area,
                e -> e.getItem().is(ModItems.ASAUCHI.get())).size();
        helper.assertTrue(before == 0, "No Asauchi ItemEntity must exist before dialogue");

        // Trigger dialogue with full inventory
        RukiaOpeningQuest.dialogue(player, rukia);

        // Verify ItemEntity was dropped in the world
        var droppedEntities = helper.getLevel().getEntitiesOfClass(ItemEntity.class, area,
                e -> e.getItem().is(ModItems.ASAUCHI.get()));
        helper.assertTrue(droppedEntities.size() == 1,
                "Exactly 1 Asauchi ItemEntity must be dropped on ground, found: " + droppedEntities.size());

        ItemEntity dropped = droppedEntities.get(0);
        helper.assertTrue(dropped.getItem().getCount() == 1, "Dropped Asauchi stack count must be exactly 1");
        helper.assertTrue(dropped.getItem().is(ModItems.ASAUCHI.get()), "Dropped item must be ModItems.ASAUCHI");
        helper.assertTrue(p.isStarterAsauchiGranted(), "Sword marked granted upon drop");

        // Second dialogue call must be idempotent: no extra entity dropped
        RukiaOpeningQuest.dialogue(player, rukia);
        var droppedAfterSecond = helper.getLevel().getEntitiesOfClass(ItemEntity.class, area,
                e -> e.getItem().is(ModItems.ASAUCHI.get()));
        helper.assertTrue(droppedAfterSecond.size() == 1,
                "Second dialogue call must not duplicate dropped Asauchi");

        rukia.discard();
        helper.succeed();
    }

    @GameTest(template = "empty")
    public static void reproduceQuincyFormNormalizationToSealed(GameTestHelper helper) {
        // Lifecycle 1: Form normalization via ProgressionService.normalize
        var quincyPlayer = new FakePlayer(helper.getLevel(), new GameProfile(UUID.randomUUID(), "P0_QuincyNorm"));
        var data = PlayerCapability.get(quincyPlayer).orElseThrow(() -> new IllegalStateException("Missing capability"));
        data.initializeQuincy();

        data.getCharacter().setSelectedForm("quincy", "vollstandig");
        data.getCharacter().setActiveForm("quincy", "vollstandig");

        helper.assertTrue("quincy".equals(data.getCharacter().getRace()), "Race must be quincy");
        helper.assertTrue("vollstandig".equals(data.getCharacter().getActiveForm()), "Active form set before normalize");

        // ProgressionService.normalize validates registered forms in FormRegistry.
        // Because FormRegistry only knows 'shinigami', for quincy it returns null -> forces sealed.
        ProgressionService.normalize(data);

        helper.assertTrue("sealed".equals(data.getCharacter().getActiveForm()),
                "DIAGNOSTICO CONFIRMADO: normalize() forca activeForm para 'sealed' por falta de suporte racial no FormRegistry");
        helper.assertTrue("sealed".equals(data.getCharacter().getSelectedForm()),
                "DIAGNOSTICO CONFIRMADO: normalize() forca selectedForm para 'sealed'");

        helper.succeed();
    }

    @GameTest(template = "empty")
    public static void testQuincyTransientResetLifecycle(GameTestHelper helper) {
        // Lifecycle 2: Transient state reset via resetTransientState (called on clone, logout, dimension travel)
        var quincyPlayer = new FakePlayer(helper.getLevel(), new GameProfile(UUID.randomUUID(), "P0_QuincyTrans"));
        var data = PlayerCapability.get(quincyPlayer).orElseThrow(() -> new IllegalStateException("Missing capability"));
        data.initializeQuincy();

        data.getQuincy().grantVollstandig();
        data.getQuincy().setVollstandigActive(true);
        helper.assertTrue(data.getQuincy().isVollstandigActive(), "Vollstandig active before reset");
        helper.assertTrue(data.getQuincy().hasVollstandig(), "Vollstandig learned before reset");

        // Executing resetTransientState directly
        data.resetTransientState();

        helper.assertTrue(!data.getQuincy().isVollstandigActive(),
                "resetTransientState() desativa a flag transitiva vollstandigActive com sucesso");
        helper.assertTrue(data.getQuincy().hasVollstandig(),
                "resetTransientState() preserva a concessao permanente de Vollstandig");

        helper.succeed();
    }

    @GameTest(template = "empty")
    public static void missingBossEncounterRecoversViaRukiaWithoutSoftlock(GameTestHelper helper) {
        // Segundo caso equivalente proposto pela frente: Recuperacao de boss ausente/despawnado
        setupEncounterGround(helper);
        var player = createShinigamiPlayer(helper, "P0_MissingBossRecovery");
        var rukia = spawnRukia(helper, new BlockPos(1, 3, 1), "karakura_inicio");
        player.setPos(rukia.position());

        QuestService.startQuest(player, RukiaOpeningQuest.KEY, rukia);
        // Inicia o encontro (Stage 1 ativo)
        RukiaOpeningQuest.dialogue(player, rukia);
        var data = PlayerCapability.get(player).orElseThrow(() -> new IllegalStateException("Missing capability"));
        var p = data.getPlayerQuestData().getProgress(RukiaOpeningQuest.KEY);
        helper.assertTrue(p.isEncounterStarted(), "Encounter started");
        helper.assertTrue(RukiaOpeningQuest.stage(p) == 1, "Must be at stage 1");
        UUID initialAttempt = p.getAttemptId();

        // Discard any spawned boss to simulate missing/despawned boss in the world
        for (var entity : helper.getLevel().getAllEntities()) {
            if (entity instanceof com.bleachmod.entity.HollowBossEntity boss
                    && entity.getPersistentData().hasUUID(Reference.TAG_QUEST_OWNER)
                    && player.getUUID().equals(entity.getPersistentData().getUUID(Reference.TAG_QUEST_OWNER))) {
                boss.discard();
            }
        }

        // Chamada de recoverMissingEncounter detecta a ausencia do boss e reseta a tentativa:
        RukiaOpeningQuest.recoverMissingEncounter(player, rukia);

        helper.assertTrue(RukiaOpeningQuest.isRetryPending(p), "Retry must become pending after missing boss recovery");
        helper.assertTrue(!initialAttempt.equals(p.getAttemptId()), "AttemptId must be renewed to prevent stale credits");
        helper.assertTrue(!p.isEncounterStarted(), "Encounter must be marked unstarted so dialogue can respawn");
        helper.assertTrue(RukiaOpeningQuest.stage(p) == 1, "Stage 1 checkpoint must be preserved");

        // Interagir novamente com Rukia reinicia o boss sem softlock
        RukiaOpeningQuest.dialogue(player, rukia);
        helper.assertTrue(p.isEncounterStarted(), "Boss encounter successfully restarted after recovery");

        StoryQuestService.cleanup(player, RukiaOpeningQuest.KEY);
        rukia.discard();
        helper.succeed();
    }
}
