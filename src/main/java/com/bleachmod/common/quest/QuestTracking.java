package com.bleachmod.common.quest;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/** Shared client/server policy. Stable sorting preserves acceptance order on ties. */
public final class QuestTracking {
    private QuestTracking() {}

    private static final class CampaignOrder {
        static final java.util.Map<String, Integer> INDEX = load();
        private static java.util.Map<String, Integer> load() {
            var result = new java.util.HashMap<String, Integer>();
            try {
                for (var entry : StoryQuestDefaults.bundled().getAsJsonArray("quests")) {
                    var quest = entry.getAsJsonObject();
                    result.put(quest.get("sagaId").getAsString() + ":" + quest.get("id").getAsInt(), result.size());
                }
            } catch (java.io.IOException failure) { throw new IllegalStateException("Missing campaign order", failure); }
            return java.util.Map.copyOf(result);
        }
    }

    public static List<Quest> orderedJournal(Collection<Quest> definitions) {
        return definitions.stream().sorted(Comparator.comparingInt(Quest::getHudPriority)
                .thenComparingInt(q -> CampaignOrder.INDEX.getOrDefault(q.getQuestKey(), Integer.MAX_VALUE))
                .thenComparing(Quest::getQuestKey)).toList();
    }

    public static List<Quest> orderedAccepted(PlayerQuestData data, Collection<Quest> definitions) {
        var byKey = new java.util.HashMap<String, Quest>();
        definitions.forEach(quest -> byKey.put(quest.getQuestKey(), quest));
        return data.getAcceptedQuestIds().stream()
                .map(byKey::get)
                .filter(Objects::nonNull)
                .sorted(Comparator.comparingInt(Quest::getHudPriority)
                        .thenComparingInt(quest -> quest.getQuestKey().equals(data.getPreferredTrackedQuestId()) ? 0 : 1))
                .toList();
    }
}
