package com.bleachmod.common.quest;

public enum QuestType {
    SAGA(0),
    SIDEQUEST(1);

    private final int hudPriority;

    QuestType(int hudPriority) {
        this.hudPriority = hudPriority;
    }

    /** Lower values take precedence in the player's accepted quest tracker. */
    public int getHudPriority() {
        return hudPriority;
    }

    public static QuestType fromName(String name) {
        if (name == null) {
            return SIDEQUEST;
        }
        return QuestType.valueOf(name.toUpperCase());
    }
}
