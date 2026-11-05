package com.bleachmod.server.events;

import com.bleachmod.Reference;
import com.bleachmod.common.data.PlayerCapability;
import com.bleachmod.common.network.SyncHelper;
import com.bleachmod.common.quest.*;
import com.bleachmod.entity.QuestNpcEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.EntityLeaveLevelEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.LinkedHashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public final class QuestNavigationEvents {
    private QuestNavigationEvents() {}

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void join(EntityJoinLevelEvent event) {
        if (event.getLevel() instanceof ServerLevel level && event.getEntity() instanceof QuestNpcEntity npc)
            QuestNpcLocations.get(level.getServer()).update(npc);
    }

    @SubscribeEvent public static void leave(EntityLeaveLevelEvent event) {
        if (event.getLevel() instanceof ServerLevel level && event.getEntity() instanceof QuestNpcEntity npc) {
            var reason = npc.getRemovalReason();
            // Unloaded entities remain navigable; only real destruction removes their marker.
            if (reason != null && reason.shouldDestroy())
                QuestNpcLocations.get(level.getServer()).remove(npc.getUUID(), level.dimension().location());
            else if (reason == net.minecraft.world.entity.Entity.RemovalReason.UNLOADED_TO_CHUNK
                    || reason == net.minecraft.world.entity.Entity.RemovalReason.UNLOADED_WITH_PLAYER)
                QuestNpcLocations.get(level.getServer()).update(npc);
        }
    }

    @SubscribeEvent public static void tick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END && event.player instanceof ServerPlayer player && player.tickCount % 20 == 0)
            refresh(player);
    }

    public static void refresh(ServerPlayer player) {
        PlayerCapability.get(player).ifPresent(data -> {
            var quests = data.getPlayerQuestData();
            var index = QuestNpcLocations.get(player.server);
            Map<String, QuestWaypoint> targets = new LinkedHashMap<>();
            for (String key : quests.getAcceptedQuestIds()) {
                Quest quest = QuestRegistry.getQuest(key);
                if (quest == null || (!quest.isStory() && quest.getQuestGiver().isBlank())) continue;
                QuestProgress progress = quests.getProgress(key);
                if (quest.isStory()) {
                    if (!"shinigami".equals(data.getCharacter().getRace())) continue;
                    var target = StoryQuestService.target(player, quest, progress);
                    if (target != null) {
                        progress.setStoryTargetUuid(target.entityId());
                        targets.put(key, target);
                    }
                    continue;
                }
                var target = index.resolve(quest.getQuestGiver(), progress.getQuestGiverUuid(),
                        player.level().dimension().location(), player.getX(), player.getY(), player.getZ());
                if (target != null) {
                    // Legacy/admin-started quests acquire a stable giver on first resolution.
                    progress.setQuestGiverUuid(target.entityId());
                    targets.put(key, target);
                }
            }
            if (quests.replaceWaypoints(targets)) SyncHelper.progression(player);
        });
    }
}
