package com.bleachmod.common.quest;

import com.bleachmod.common.data.PlayerData;

import java.util.List;

public final class QuestAvailabilityChecker {
    private QuestAvailabilityChecker() {
    }

    public static boolean isAvailable(PlayerData data, String questKey) {
        if (!data.getStatus().hasCreatedCharacter()) {
            return false;
        }
        Quest quest = QuestRegistry.getQuest(questKey);
        if (quest == null) {
            return false;
        }
        var progress = data.getPlayerQuestData().getProgress(questKey);
        if (progress != null && !progress.matchesDefinition(quest)) return false;
        QuestStatus status = data.getPlayerQuestData().getStatus(questKey);
        if (status == QuestStatus.ACCEPTED || (status == QuestStatus.SUCCESS && (progress == null || !progress.canRepeat(quest)))) {
            return false;
        }
        if (status == QuestStatus.FAILED && !quest.isStory()) {
            return true;
        }
        if (quest.getType() == QuestType.SAGA) {
            if (!previousSagaComplete(data, quest.getSagaId())) {
                return false;
            }
            if (!previousSagaQuestComplete(data, quest)) {
                return false;
            }
        }
        return quest.getPrerequisites().evaluate(data);
    }

    public static boolean isVisible(PlayerData data, Quest quest) {
        if (!quest.isStory()) return true;
        if (data == null || !data.getStatus().hasCreatedCharacter()
                || !"shinigami".equals(data.getCharacter().getRace())) return false;
        if (data.getPlayerQuestData().getStatus(quest.getQuestKey()) != QuestStatus.NOT_STARTED) return true;
        return quest.getPrerequisites().evaluate(data) && previousSagaComplete(data, quest.getSagaId())
                && previousSagaQuestComplete(data, quest);
    }

    public static boolean isOfferedBy(Quest quest, com.bleachmod.entity.QuestNpcEntity npc) {
        if (RukiaOpeningQuest.isOpening(quest)) return RukiaOpeningQuest.isRukia(npc);
        if (!quest.getQuestGiver().equals(npc.getNpcId())) return false;
        // Unscoped legacy/test offers must not leak into a story representation of the character.
        if (!quest.isStory()) return npc.getQuestContext().isEmpty()
                && (!"rukia_npc_test".equals(quest.getQuestKey()) || npc.level().dimension() == net.minecraft.world.level.Level.OVERWORLD);
        return quest.getStory().offerContext().equals(npc.getQuestContext())
                && quest.getStory().dimension().equals(npc.level().dimension().location());
    }

    public static boolean previousSagaComplete(PlayerData data, String sagaId) {
        Saga saga = QuestRegistry.getSaga(sagaId);
        if (saga == null || saga.getPreviousSaga() == null || saga.getPreviousSaga().isBlank()) {
            return true;
        }
        List<String> previous = QuestRegistry.sagaQuestKeys(saga.getPreviousSaga());
        Saga previousSaga = QuestRegistry.getSaga(saga.getPreviousSaga());
        if (previousSaga != null && !previousSaga.getCompletionQuest().isBlank())
            return data.getPlayerQuestData().isCompleted(previousSaga.getCompletionQuest());
        if (previous.isEmpty()) {
            return true;
        }
        return previous.stream().allMatch(data.getPlayerQuestData()::isCompleted);
    }

    public static boolean previousSagaQuestComplete(PlayerData data, Quest quest) {
        List<String> order = QuestRegistry.sagaQuestKeys(quest.getSagaId());
        int index = order.indexOf(quest.getQuestKey());
        if (index <= 0) {
            return true;
        }
        return data.getPlayerQuestData().isCompleted(order.get(index - 1));
    }
}
