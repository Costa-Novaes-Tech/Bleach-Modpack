package com.bleachmod.common.quest;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/** Shared client/server policy. Stable sorting preserves acceptance order on ties. */
public final class QuestTracking {
    private QuestTracking() {}

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
