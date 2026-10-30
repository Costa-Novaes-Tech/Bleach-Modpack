package com.bleachmod.common.quest;

import com.bleachmod.Reference;
import com.bleachmod.common.data.PlayerCapability;
import com.bleachmod.common.network.SyncHelper;
import com.bleachmod.common.quest.objectives.KillObjective;
import com.bleachmod.common.quest.objectives.StoryObjective;
import com.bleachmod.entity.QuestNpcEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

/** Server-authoritative, explicit encounter start. No replacement bosses or automatic story skips. */
public final class StoryQuestService {
    private StoryQuestService() {}

    public static Component pendingReason(Quest quest) {
        if (!quest.isStory()) return null;
        if (!quest.getStory().pendingContent().isBlank())
            return Component.translatable("message.bleachmod.story.pending", Component.translatable(quest.getStory().pendingContent()));
        for (var objective : quest.getObjectives()) {
            if (objective instanceof KillObjective kill && !ForgeRegistries.ENTITY_TYPES.containsKey(
                    com.bleachmod.common.util.ResourceIds.parse(kill.getEntityId())))
                return Component.translatable("message.bleachmod.story.boss_pending", objective.describe());
        }
        return null;
    }

    public static QuestWaypoint target(ServerPlayer player, Quest quest, QuestProgress progress) {
        var story = quest.getStory();
        return QuestNpcLocations.get(player.server).resolveContext(story.targetNpc(), story.targetContext(),
                story.dimension(), progress.getStoryTargetUuid(), player.getX(), player.getY(), player.getZ());
    }

    public static void begin(ServerPlayer player, String key) {
        if (RukiaOpeningQuest.isOpening(QuestRegistry.getQuest(key))) return;
        beginEncounter(player, key);
    }

    static void beginOpening(ServerPlayer player, String key) { cleanup(player, key); beginEncounter(player, key); }

    private static void beginEncounter(ServerPlayer player, String key) {
        var data = PlayerCapability.get(player).orElse(null);
        Quest quest = QuestRegistry.getQuest(key);
        if (data == null || quest == null || !quest.isStory() || !"shinigami".equals(data.getCharacter().getRace())) return;
        QuestProgress progress = data.getPlayerQuestData().getProgress(key);
        if (progress == null || progress.getStatus() != QuestStatus.ACCEPTED || !progress.matchesDefinition(quest)
                || progress.isEncounterStarted() || !quest.getPrerequisites().evaluate(data)) return;
        Component pending = pendingReason(quest);
        if (pending != null) { player.displayClientMessage(pending, false); return; }
        QuestWaypoint target = target(player, quest, progress);
        if (target == null || !target.dimension().equals(player.level().dimension().location())
                || target.pos().distToCenterSqr(player.position()) > 48 * 48
                || !(player.serverLevel().getEntity(target.entityId()) instanceof QuestNpcEntity npc)
                || !npc.isAlive() || !npc.getQuestContext().equals(target.context())) {
            player.displayClientMessage(Component.translatable("message.bleachmod.story.location_required"), false); return;
        }
        if (!spawnEncounter(player, quest, target)) {
            player.displayClientMessage(Component.translatable("message.bleachmod.story.no_space"), false); return;
        }
        progress.setStoryTargetUuid(target.entityId());
        progress.setEncounterStarted(true);
        if (RukiaOpeningQuest.isOpening(quest)) QuestService.incrementObjective(player, data, key, quest, 0, 1);
        com.bleachmod.server.events.QuestNavigationEvents.refresh(player);
        SyncHelper.progression(player);
    }

    private static boolean spawnEncounter(ServerPlayer player, Quest quest, QuestWaypoint target) {
        var level = player.serverLevel();
        List<Mob> prepared = new ArrayList<>();
        for (int i = 0; i < quest.getObjectives().size(); i++) {
            if (!(quest.getObjectives().get(i) instanceof KillObjective kill)) continue;
            var id = com.bleachmod.common.util.ResourceIds.parse(kill.getEntityId());
            if (!ForgeRegistries.ENTITY_TYPES.containsKey(id)) return false;
            var type = ForgeRegistries.ENTITY_TYPES.getValue(id);
            if (type == null || kill.getSpawnMode() != KillObjective.SpawnMode.QUEST) return false;
            for (int n = 0; n < kill.getRequired(); n++) {
                if (!(type.create(level) instanceof Mob mob)) return false;
                boolean placed = false;
                // Search only loaded space near the authored anchor; never place blocks or force chunks.
                for (int radius = 6; radius <= 20 && !placed; radius += 2) {
                    for (int angle = 0; angle < 360 && !placed; angle += 30) {
                        BlockPos base = target.pos().offset((int) Math.round(Math.cos(Math.toRadians(angle)) * radius), 0,
                                (int) Math.round(Math.sin(Math.toRadians(angle)) * radius));
                        for (int dy = 2; dy >= -3 && !placed; dy--) {
                            BlockPos pos = base.offset(0, dy, 0);
                            if (!level.hasChunkAt(pos) || !level.getBlockState(pos.below()).isFaceSturdy(level, pos.below(), net.minecraft.core.Direction.UP)) continue;
                            mob.moveTo(pos.getX() + .5, pos.getY(), pos.getZ() + .5, player.getYRot(), 0);
                            if (!level.getWorldBorder().isWithinBounds(mob.getBoundingBox()) || !level.noCollision(mob)
                                    || level.containsAnyLiquid(mob.getBoundingBox()) || mob.distanceToSqr(player) < 16) continue;
                            if (prepared.stream().anyMatch(other -> other.getBoundingBox().intersects(mob.getBoundingBox()))) continue;
                            placed = true;
                        }
                    }
                }
                if (!placed) { mob.discard(); prepared.forEach(Mob::discard); return false; }
                mob.getPersistentData().putString(Reference.TAG_QUEST_KEY, quest.getQuestKey());
                mob.getPersistentData().putInt(Reference.TAG_QUEST_OBJECTIVE_INDEX, i);
                mob.getPersistentData().putUUID(Reference.TAG_QUEST_OWNER, player.getUUID());
                var progress = com.bleachmod.common.data.PlayerCapability.get(player).orElseThrow(
                        () -> new IllegalStateException("Missing quest owner")).getPlayerQuestData().getProgress(quest.getQuestKey());
                mob.getPersistentData().putUUID("blc_quest_attempt", progress.getAttemptId());
                mob.finalizeSpawn(level, level.getCurrentDifficultyAt(mob.blockPosition()), MobSpawnType.EVENT, null, null);
                mob.setPersistenceRequired();
                prepared.add(mob);
            }
        }
        for (Mob mob : prepared) {
            if (!level.addFreshEntity(mob)) { prepared.forEach(Mob::discard); return false; }
        }
        return true;
    }

    public static boolean interact(ServerPlayer player, QuestNpcEntity npc) {
        var data = PlayerCapability.get(player).orElse(null);
        if (data == null || !data.getStatus().hasCreatedCharacter() || !"shinigami".equals(data.getCharacter().getRace())
                || player.level() != npc.level() || player.distanceToSqr(npc) > 36) return false;
        for (String key : data.getPlayerQuestData().getAcceptedQuestIds()) {
            Quest quest = QuestRegistry.getQuest(key);
            if (quest == null || !quest.isStory()) continue;
            QuestProgress progress = data.getPlayerQuestData().getProgress(key);
            if (RukiaOpeningQuest.isOpening(quest)) continue; // Dialogue advances only on explicit UI confirmation.
            if (!progress.isEncounterStarted() || !npc.getUUID().equals(progress.getStoryTargetUuid())
                    || !npc.getQuestContext().equals(quest.getStory().targetContext())
                    || !quest.getStory().dimension().equals(npc.level().dimension().location())) continue;
            for (int i = 0; i < quest.getObjectives().size(); i++) {
                if (quest.getObjectives().get(i) instanceof StoryObjective objective
                        && objective.action() == StoryObjective.Action.INTERACT && QuestService.isObjectiveActive(quest, progress, i)) {
                    QuestService.incrementObjective(player, data, key, quest, i, 1);
                    return true;
                }
            }
        }
        return false;
    }

    public static void cleanup(ServerPlayer player, String key) {
        for (var level : player.server.getAllLevels()) for (var entity : level.getAllEntities()) {
            var tag = entity.getPersistentData();
            if (key.equals(tag.getString(Reference.TAG_QUEST_KEY)) && tag.hasUUID(Reference.TAG_QUEST_OWNER)
                    && player.getUUID().equals(tag.getUUID(Reference.TAG_QUEST_OWNER))) entity.discard();
        }
    }
}
