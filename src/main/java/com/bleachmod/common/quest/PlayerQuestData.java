package com.bleachmod.common.quest;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PlayerQuestData {
    private final Map<String, QuestProgress> quests = new LinkedHashMap<>();
    private final Map<String, Boolean> sagaUnlocks = new LinkedHashMap<>();
    private String trackedQuestId;
    private final Map<String, QuestWaypoint> waypoints = new LinkedHashMap<>();

    public QuestWaypoint getWaypoint(String key) { return isAccepted(key) ? waypoints.get(key) : null; }

    public boolean replaceWaypoints(Map<String, QuestWaypoint> values) {
        if (waypoints.equals(values)) return false;
        waypoints.clear();
        waypoints.putAll(values);
        return true;
    }

    public void bindDefinitions() {
        quests.forEach((key, progress) -> {
            Quest quest = QuestRegistry.getQuest(key);
            if (quest != null) progress.bindDefinition(quest);
        });
    }

    public QuestProgress getOrCreateProgress(String questId) {
        return quests.computeIfAbsent(questId, QuestProgress::new);
    }

    public QuestProgress getProgress(String questId) {
        return quests.get(questId);
    }

    public QuestStatus getStatus(String questId) {
        QuestProgress progress = quests.get(questId);
        return progress == null ? QuestStatus.NOT_STARTED : progress.getStatus();
    }

    public void acceptQuest(String questId, Quest quest) {
        QuestProgress progress = getOrCreateProgress(questId);
        progress.initializeRequirements(quest);
        progress.setStatus(QuestStatus.ACCEPTED);
        waypoints.remove(questId);
    }

    public void completeQuest(String questId) {
        QuestProgress progress = getOrCreateProgress(questId);
        progress.setStatus(QuestStatus.SUCCESS);
        waypoints.remove(questId);
        if (questId.equals(trackedQuestId)) {
            trackedQuestId = null;
        }
    }

    public void failQuest(String questId) {
        QuestProgress progress = getOrCreateProgress(questId);
        progress.markFailed();
        waypoints.remove(questId);
        if (questId.equals(trackedQuestId)) {
            trackedQuestId = null;
        }
    }

    public boolean isAccepted(String questId) {
        return getStatus(questId) == QuestStatus.ACCEPTED;
    }

    public boolean isCompleted(String questId) {
        return getStatus(questId) == QuestStatus.SUCCESS;
    }

    public List<String> getAcceptedQuestIds() {
        List<String> ids = new ArrayList<>();
        quests.forEach((id, progress) -> {
            if (progress.getStatus() == QuestStatus.ACCEPTED) {
                ids.add(id);
            }
        });
        return ids;
    }

    public String getTrackedQuestId() {
        List<Quest> accepted = getAcceptedQuestsForHud();
        return accepted.isEmpty() ? null : accepted.get(0).getQuestKey();
    }

    public List<Quest> getAcceptedQuestsForHud() {
        return QuestTracking.orderedAccepted(this, QuestRegistry.allQuests());
    }

    /** Optional manual preference; importance still takes precedence. Null means automatic. */
    public String getPreferredTrackedQuestId() {
        return trackedQuestId;
    }

    public void setTrackedQuestId(String trackedQuestId) {
        this.trackedQuestId = trackedQuestId == null || trackedQuestId.isBlank() ? null : trackedQuestId;
    }

    public void setSagaUnlocked(String sagaId, boolean unlocked) {
        sagaUnlocks.put(sagaId, unlocked);
    }

    public boolean isSagaUnlocked(String sagaId) {
        return sagaUnlocks.getOrDefault(sagaId, false) || sagaId == null || sagaId.isBlank();
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        ListTag questList = new ListTag();
        for (QuestProgress progress : quests.values()) {
            questList.add(progress.save());
        }
        tag.put("quests", questList);
        CompoundTag navigation = new CompoundTag();
        waypoints.forEach((key, target) -> navigation.put(key, target.save()));
        tag.put("waypoints", navigation);
        CompoundTag unlocks = new CompoundTag();
        sagaUnlocks.forEach(unlocks::putBoolean);
        tag.put("sagaUnlocks", unlocks);
        if (trackedQuestId != null) {
            tag.putString("trackedQuestId", trackedQuestId);
        }
        return tag;
    }

    public void load(CompoundTag tag) {
        waypoints.clear();
        CompoundTag navigation = tag.getCompound("waypoints");
        for (String key : navigation.getAllKeys()) {
            QuestWaypoint target = QuestWaypoint.load(navigation.getCompound(key));
            if (target != null) waypoints.put(key, target);
        }
        if (tag.contains("quests")) {
            quests.clear();
            ListTag questList = tag.getList("quests", Tag.TAG_COMPOUND);
            for (int i = 0; i < questList.size(); i++) {
                QuestProgress progress = QuestProgress.load(questList.getCompound(i));
                quests.put(progress.getQuestId(), progress);
            }
        }
        if (tag.contains("sagaUnlocks")) {
            sagaUnlocks.clear();
            CompoundTag unlocks = tag.getCompound("sagaUnlocks");
            for (String key : unlocks.getAllKeys()) {
                sagaUnlocks.put(key, unlocks.getBoolean(key));
            }
        }
        trackedQuestId = null;
        if (tag.contains("trackedQuestId")) {
            trackedQuestId = tag.getString("trackedQuestId");
            if (trackedQuestId.isBlank()) {
                trackedQuestId = null;
            }
        }
    }
}
