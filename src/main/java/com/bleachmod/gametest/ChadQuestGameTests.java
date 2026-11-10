package com.bleachmod.gametest;

import com.bleachmod.common.data.PlayerCapability;
import com.bleachmod.common.data.PlayerData;
import com.bleachmod.common.quest.*;
import com.bleachmod.entity.QuestNpcEntity;
import com.bleachmod.registry.ModEntities;
import com.bleachmod.server.events.QuestNavigationEvents;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@GameTestHolder("bleachmod")
@PrefixGameTestTemplate(false)
public final class ChadQuestGameTests {
    private static final String KEY = RukiaOpeningQuest.CHAD_KEY;
    private static final String KON = "shinigami_substitute:4";

    @GameTest(template = "empty")
    public static void chadRequiresCorrectNpcContextAndInstance(GameTestHelper helper) {
        String context = RukiaOpeningQuest.CHAD_CONTEXT;
        var chad = npc(helper, ModEntities.NPC_SADO_YASUTORA.get(), context, 1);
        var otherChad = npc(helper, ModEntities.NPC_SADO_YASUTORA.get(), context, 2);
        var unscopedChad = npc(helper, ModEntities.NPC_SADO_YASUTORA.get(), "", 3);
        var rukia = npc(helper, ModEntities.NPC_RUKIA.get(), context, 4);
        var player = player(helper, chad);
        var quests = data(player).getPlayerQuestData();
        Quest quest = QuestRegistry.getQuest(KEY);
        helper.assertTrue(!QuestAvailabilityChecker.isVisible(data(player), quest), "Chad was unlocked before Orihime");
        quests.completeQuest(RukiaOpeningQuest.ORIHIME_KEY);
        QuestService.startQuest(player, KEY, unscopedChad);
        QuestService.startQuest(player, KEY, rukia);
        helper.assertTrue(!quests.isAccepted(KEY), "Wrong NPC type or context accepted the mission");
        QuestService.startQuest(player, KEY, chad);
        var progress = quests.getProgress(KEY);
        helper.assertTrue(progress != null && RukiaOpeningQuest.stage(progress) == 0 && !progress.isEncounterStarted(), "Accepting skipped the introduction");
        helper.assertTrue(chad.getUUID().equals(quests.getWaypoint(KEY).entityId()), "Compass did not bind to the offering Chad");
        RukiaOpeningQuest.dialogue(player, otherChad, KEY);
        helper.assertTrue(RukiaOpeningQuest.stage(progress) == 0, "Another Chad instance confirmed the dialogue");
        RukiaOpeningQuest.dialogue(player, chad, KEY);
        helper.assertTrue(StoryQuestService.pendingReason(quest) != null && RukiaOpeningQuest.isRetryPending(progress)
                && progress.getEncounterActor() == null, "Missing Shrieker lost the introduction or spawned a replacement");
        helper.assertTrue(chad.getUUID().equals(quests.getWaypoint(KEY).entityId())
                && !QuestAvailabilityChecker.isVisible(data(player), QuestRegistry.getQuest(KON)), "Pending combat changed navigation or unlocked Kon");
        chad.discard(); otherChad.discard(); unscopedChad.discard(); rukia.discard();
        helper.succeed();
    }

    @GameTest(template = "empty")
    public static void chadCompassTracksEncounterAndVictoryUnlocksKon(GameTestHelper helper) {
        String sagas = QuestRegistry.serializeSagas();
        String quests = QuestRegistry.serializeQuests();
        var fixture = JsonParser.parseString(quests).getAsJsonArray();
        for (var entry : fixture) {
            var json = entry.getAsJsonObject();
            if (json.has("story") && "MAIN-SUB-03".equals(json.getAsJsonObject("story").get("id").getAsString())) {
                // Only this synchronous test uses a registered fixture actor to exercise the quest engine.
                json.getAsJsonArray("objectives").get(1).getAsJsonObject().addProperty("entity", "minecraft:husk");
            }
        }
        var chad = npc(helper, ModEntities.NPC_SADO_YASUTORA.get(), RukiaOpeningQuest.CHAD_CONTEXT, 1);
        var player = player(helper, chad);
        var data = data(player);
        var progressData = data.getPlayerQuestData();
        progressData.completeQuest(RukiaOpeningQuest.ORIHIME_KEY);
        for (int x = 4; x <= 10; x++) for (int z = -2; z <= 4; z++)
            helper.setBlock(new BlockPos(x, 2, z), Blocks.STONE);
        try {
            QuestRegistry.installServer(QuestRegistry.parse(sagas, fixture.toString()));
            QuestService.startQuest(player, KEY, chad);
            RukiaOpeningQuest.dialogue(player, chad, KEY);
            var progress = progressData.getProgress(KEY);
            var actor = (LivingEntity) helper.getLevel().getEntity(progress.getEncounterActor());
            helper.assertTrue(actor != null && actor.isAlive() && actor.getUUID().equals(progressData.getWaypoint(KEY).entityId()), "Compass did not switch to the encounter actor");
            actor.setPos(actor.getX() + 1, actor.getY(), actor.getZ());
            QuestNavigationEvents.refresh(player);
            helper.assertTrue(actor.blockPosition().equals(progressData.getWaypoint(KEY).pos())
                    && chad.getUUID().equals(progress.getStoryTargetUuid()), "Compass did not follow the actor or overwrote the Chad binding");
            actor.discard();
            RukiaOpeningQuest.recoverMissingEncounter(player);
            helper.assertTrue(RukiaOpeningQuest.isRetryPending(progress) && chad.getUUID().equals(progressData.getWaypoint(KEY).entityId()), "Missing actor did not redirect to Chad");
            RukiaOpeningQuest.retry(player, KEY);
            helper.assertTrue(!progress.isEncounterStarted(), "The Journal retried a Chad-only encounter");
            RukiaOpeningQuest.dialogue(player, chad, KEY);
            var nextActor = (LivingEntity) helper.getLevel().getEntity(progress.getEncounterActor());
            helper.assertTrue(nextActor != null && !nextActor.getUUID().equals(actor.getUUID()), "Retry reused the missing actor");
            nextActor.hurt(helper.getLevel().damageSources().playerAttack(player), 1000);
            helper.assertTrue(progressData.isCompleted(KEY) && progressData.getWaypoint(KEY) == null
                    && QuestAvailabilityChecker.isVisible(data, QuestRegistry.getQuest(KON)), "Victory required return, left navigation active, or did not unlock Kon");
            helper.assertTrue(!progressData.isAccepted(KON), "Victory started Kon automatically");
        } finally {
            StoryQuestService.cleanup(player, KEY);
            chad.discard();
            QuestRegistry.installServer(QuestRegistry.parse(sagas, quests));
        }
        helper.succeed();
    }

    @GameTest(template = "empty")
    public static void chadLegacyMigrationPreservesCompletionsAndCustomFiles(GameTestHelper helper) throws Exception {
        var old = StoryQuestDefaults.bundled().getAsJsonArray("quests").get(2).getAsJsonObject();
        old.remove("version");
        old.getAsJsonArray("objectives").remove(0);
        var progress = new QuestProgress(KEY);
        progress.initializeRequirements(QuestParser.parseQuest(old, "shinigami_substitute"));
        progress.setStatus(QuestStatus.SUCCESS);
        var current = QuestRegistry.getQuest(KEY);
        helper.assertTrue(progress.bindDefinition(current) && progress.allObjectivesComplete(current), "A saved completion was not retained");
        var root = Path.of("qa-chad-migration", UUID.randomUUID().toString());
        var path = root.resolve("quests/shinigami_substitute/03.json");
        Files.createDirectories(path.getParent());
        Files.writeString(path, old.toString());
        StoryQuestDefaults.writeIfMissing(root);
        helper.assertTrue(Files.readString(path.resolveSibling("03.json.pre-stages.bak")).equals(old.toString())
                && JsonParser.parseString(Files.readString(path)).getAsJsonObject().get("version").getAsInt() == 2,
                "The bundled old definition was not backed up and migrated");
        old.getAsJsonArray("objectives").get(0).getAsJsonObject().addProperty("count", 2);
        Files.writeString(path, old.toString());
        StoryQuestDefaults.writeIfMissing(root);
        helper.assertTrue(Files.readString(path).equals(old.toString()), "A customized quest file was overwritten");
        helper.succeed();
    }

    private static FakePlayer player(GameTestHelper helper, QuestNpcEntity chad) {
        var player = new FakePlayer(helper.getLevel(), new GameProfile(UUID.randomUUID(), "ChadQA"));
        data(player).initializeShinigami();
        data(player).getPlayerQuestData().completeQuest(RukiaOpeningQuest.KEY);
        player.setPos(chad.position());
        return player;
    }

    private static PlayerData data(FakePlayer player) {
        return PlayerCapability.get(player).orElseThrow(() -> new IllegalStateException("Missing capability"));
    }

    private static QuestNpcEntity npc(GameTestHelper helper, EntityType<QuestNpcEntity> type, String context, int x) {
        var npc = type.create(helper.getLevel());
        var pos = helper.absolutePos(new BlockPos(x, 3, 1));
        npc.moveTo(pos.getX() + .5, pos.getY(), pos.getZ() + .5);
        npc.setQuestContext(context);
        helper.getLevel().addFreshEntity(npc);
        return npc;
    }
}
