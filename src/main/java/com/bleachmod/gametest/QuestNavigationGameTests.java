package com.bleachmod.gametest;

import com.bleachmod.common.data.PlayerCapability;
import com.bleachmod.common.quest.*;
import com.bleachmod.entity.QuestNpcEntity;
import com.bleachmod.registry.ModEntities;
import com.bleachmod.server.events.QuestNavigationEvents;
import com.mojang.authlib.GameProfile;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

import java.util.UUID;

@GameTestHolder("bleachmod")
@PrefixGameTestTemplate(false)
public final class QuestNavigationGameTests {
    @GameTest(template = "empty", timeoutTicks = 100)
    public static void npcLocationSurvivesUnloadAndReloadButNotDestruction(GameTestHelper helper) {
        var level = helper.getLevel();
        var pos = helper.absolutePos(new net.minecraft.core.BlockPos(1, 2, 1));
        QuestNpcEntity npc = ModEntities.NPC_URYUU.get().create(level);
        npc.moveTo(pos.getX() + .5, pos.getY(), pos.getZ() + .5);
        level.addFreshEntity(npc);
        var index = QuestNpcLocations.get(level.getServer());
        var id = npc.getUUID();
        helper.assertTrue(index.resolve("uryuu", id, level.dimension().location(), 0, 0, 0) != null, "Spawn not registered");
        npc.moveTo(pos.getX() + 2.5, pos.getY(), pos.getZ() + .5);
        npc.remove(Entity.RemovalReason.UNLOADED_TO_CHUNK);
        helper.runAfterDelay(3, () -> {
            helper.assertTrue(index.resolve("uryuu", id, level.dimension().location(), 0, 0, 0) != null, "Unload erased destination");
            helper.assertTrue(index.resolve("uryuu", id, level.dimension().location(), 0, 0, 0).pos().equals(npc.blockPosition()), "Unload lost last movement");
            QuestNpcEntity reloaded = ModEntities.NPC_URYUU.get().create(level);
            reloaded.setUUID(id);
            reloaded.moveTo(pos.getX() + 4.5, pos.getY(), pos.getZ() + .5);
            level.addFreshEntity(reloaded);
            helper.assertTrue(index.resolve("uryuu", id, level.dimension().location(), 0, 0, 0).pos().equals(reloaded.blockPosition()), "Reload did not update destination");
            reloaded.discard();
            helper.runAfterDelay(3, () -> {
                helper.assertTrue(index.resolve("uryuu", id, level.dimension().location(), 0, 0, 0) == null, "Destroyed NPC retained marker");
                helper.succeed();
            });
        });
    }

    @GameTest(template = "empty")
    public static void acceptedQuestBindsItsActualGiverAndSynchronizesWaypoint(GameTestHelper helper) {
        var level = helper.getLevel();
        var player = new FakePlayer(level, new GameProfile(UUID.randomUUID(), "NavigationQA"));
        var data = PlayerCapability.get(player).orElseThrow(() -> new IllegalStateException("Missing QA capability"));
        data.initializeShinigami();
        var pos = helper.absolutePos(new net.minecraft.core.BlockPos(1, 2, 1));
        QuestNpcEntity giver = ModEntities.NPC_RUKIA.get().create(level);
        giver.moveTo(pos.getX() + 2.5, pos.getY(), pos.getZ() + .5);
        giver.setQuestContext("karakura_inicio"); level.addFreshEntity(giver);
        player.setPos(giver.position());
        QuestService.startQuest(player, "shinigami_substitute:1", giver);
        QuestNpcEntity duplicate = ModEntities.NPC_RUKIA.get().create(level);
        duplicate.moveTo(pos.getX() + .5, pos.getY(), pos.getZ() + .5);
        duplicate.setQuestContext("karakura_inicio"); level.addFreshEntity(duplicate);
        player.setPos(duplicate.position());
        QuestNavigationEvents.refresh(player);
        var quests = data.getPlayerQuestData();
        helper.assertTrue(quests.isAccepted("shinigami_substitute:1"), "Quest not accepted");
        helper.assertTrue(giver.getUUID().equals(quests.getProgress("shinigami_substitute:1").getStoryTargetUuid()), "Wrong opening Rukia binding");
        var client = new PlayerQuestData(); client.load(quests.save());
        helper.assertTrue(giver.getUUID().equals(client.getWaypoint("shinigami_substitute:1").entityId()), "Snapshot switched to closer duplicate");
        quests.failQuest("shinigami_substitute:1");
        QuestNavigationEvents.refresh(player);
        helper.assertTrue(quests.getWaypoint("shinigami_substitute:1") == null, "Failed quest retained a waypoint");
        giver.discard(); duplicate.discard();
        helper.succeed();
    }
}
