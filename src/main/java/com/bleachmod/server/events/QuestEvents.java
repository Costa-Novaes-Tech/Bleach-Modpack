package com.bleachmod.server.events;

import com.bleachmod.Reference;
import com.bleachmod.common.data.PlayerCapability;
import com.bleachmod.common.data.PlayerData;
import com.bleachmod.common.quest.PlayerQuestData;
import com.bleachmod.common.quest.Quest;
import com.bleachmod.common.quest.QuestProgress;
import com.bleachmod.common.quest.QuestRegistry;
import com.bleachmod.common.quest.QuestService;
import com.bleachmod.common.quest.QuestStatus;
import com.bleachmod.common.quest.RukiaOpeningQuest;
import com.bleachmod.common.quest.objectives.ItemObjective;
import com.bleachmod.common.quest.objectives.KillObjective;
import com.bleachmod.common.quest.objectives.QuestObjective;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public class QuestEvents {
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.player.level().isClientSide) {
            return;
        }
        if (event.player.tickCount % 20 != 0) {
            return;
        }
        if (!(event.player instanceof ServerPlayer player)) {
            return;
        }
        PlayerCapability.get(player).ifPresent(data -> processItemObjectives(player, data));
    }

    @SubscribeEvent
    public static void onEntityKill(LivingDeathEvent event) {
        if(event.getEntity().getPersistentData().getBoolean("bleachmod_ash_soldier"))return;
        if (event.getEntity() instanceof ServerPlayer deadPlayer) {
            handlePlayerQuestFailure(deadPlayer);
        }
        if (event.getSource().getEntity() instanceof ServerPlayer killer) {
            creditQuestKill(killer, event.getEntity());
        }
    }

    @SubscribeEvent(priority = net.minecraftforge.eventbus.api.EventPriority.LOWEST)
    public static void openingBossDiedWithoutCredit(LivingDeathEvent event) {
        if (!(event.getEntity().level() instanceof net.minecraft.server.level.ServerLevel level)) return;
        var tag = event.getEntity().getPersistentData();
        if (!RukiaOpeningQuest.KEY.equals(tag.getString(com.bleachmod.Reference.TAG_QUEST_KEY))
                || !tag.hasUUID(com.bleachmod.Reference.TAG_QUEST_OWNER)) return;
        var ownerId = tag.getUUID(com.bleachmod.Reference.TAG_QUEST_OWNER);
        ServerPlayer owner = level.getServer().getPlayerList().getPlayer(ownerId);
        if (owner == null && level.getPlayerByUUID(ownerId) instanceof ServerPlayer localOwner) owner = localOwner;
        if (owner != null) RukiaOpeningQuest.onUncreditedBossDeath(owner, event.getEntity());
    }

    private static void processItemObjectives(ServerPlayer player, PlayerData data) {
        for (String questKey : data.getPlayerQuestData().getAcceptedQuestIds()) {
            Quest quest = QuestRegistry.getQuest(questKey);
            if (quest == null) {
                continue;
            }
            QuestProgress progress = data.getPlayerQuestData().getProgress(questKey);
            if (progress == null) {
                continue;
            }
            for (int i = 0; i < quest.getObjectives().size(); i++) {
                QuestObjective objective = quest.getObjectives().get(i);
                if (!(objective instanceof ItemObjective itemObjective)) {
                    continue;
                }
                if (!QuestService.isObjectiveActive(quest, progress, i)) {
                    continue;
                }
                int count = itemObjective.countInInventory(player);
                QuestService.setObjectiveProgress(player, data, questKey, quest, i, count);
            }
        }
    }

    private static void creditQuestKill(ServerPlayer killer, LivingEntity killed) {
        PlayerCapability.get(killer).ifPresent(data -> {
            for (String questKey : data.getPlayerQuestData().getAcceptedQuestIds()) {
                Quest quest = QuestRegistry.getQuest(questKey);
                if (quest == null) {
                    continue;
                }
                QuestProgress progress = data.getPlayerQuestData().getProgress(questKey);
                if (progress == null || progress.getStatus() != QuestStatus.ACCEPTED) {
                    continue;
                }
                if (quest.isStory() && (!progress.isEncounterStarted()
                        || !"shinigami".equals(data.getCharacter().getRace())
                        || !quest.getStory().dimension().equals(killed.level().dimension().location())
                        || !killed.getPersistentData().hasUUID(Reference.TAG_QUEST_OWNER)
                        || !killer.getUUID().equals(killed.getPersistentData().getUUID(Reference.TAG_QUEST_OWNER))
                        || !killed.getPersistentData().hasUUID("blc_quest_attempt")
                        || !killed.getPersistentData().getUUID("blc_quest_attempt").equals(progress.getAttemptId()))) continue;
                for (int i = 0; i < quest.getObjectives().size(); i++) {
                    if (!(quest.getObjectives().get(i) instanceof KillObjective kill)) {
                        continue;
                    }
                    if (!QuestService.isObjectiveActive(quest, progress, i)) continue;
                    if (!kill.matches(killed, questKey, i)) {
                        continue;
                    }
                    QuestService.incrementObjective(killer, data, questKey, quest, i, 1);
                    if (!quest.isParallelObjectives()) break;
                }
            }
        });
    }

    private static void handlePlayerQuestFailure(ServerPlayer deadPlayer) {
        PlayerCapability.get(deadPlayer).ifPresent(data -> {
            PlayerQuestData pqd = data.getPlayerQuestData();
            for (String questKey : pqd.getAcceptedQuestIds()) {
                Quest quest = QuestRegistry.getQuest(questKey);
                if (quest != null && quest.hasKillObjective()) {
                    if (RukiaOpeningQuest.isOpening(quest)) RukiaOpeningQuest.onPlayerDeath(deadPlayer);
                    else QuestService.failQuest(deadPlayer, data, questKey);
                }
            }
        });
    }

    @SubscribeEvent public static void discardStaleStoryActor(net.minecraftforge.event.entity.EntityJoinLevelEvent event) {
        if (!(event.getLevel() instanceof net.minecraft.server.level.ServerLevel level)) return;
        var tag = event.getEntity().getPersistentData();
        if (!tag.hasUUID("blc_quest_attempt") || !tag.hasUUID(Reference.TAG_QUEST_OWNER)) return;
        Quest quest = QuestRegistry.getQuest(tag.getString(Reference.TAG_QUEST_KEY));
        if (quest == null || !quest.isStory()) return;
        var owner = level.getServer().getPlayerList().getPlayer(tag.getUUID(Reference.TAG_QUEST_OWNER));
        if (owner == null) return;
        PlayerCapability.get(owner).ifPresent(data -> {
            var progress = data.getPlayerQuestData().getProgress(quest.getQuestKey());
            if (progress == null || progress.getStatus() != QuestStatus.ACCEPTED
                    || !tag.getUUID("blc_quest_attempt").equals(progress.getAttemptId())) event.setCanceled(true);
        });
    }
}
