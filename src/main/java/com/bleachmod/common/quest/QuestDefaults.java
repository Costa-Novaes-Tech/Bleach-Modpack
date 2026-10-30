package com.bleachmod.common.quest;

import java.io.IOException;
import java.nio.file.Path;

/** Only the authored main campaign is generated for new worlds. */
public final class QuestDefaults {
    private QuestDefaults() {}

    public static void writeIfMissing(Path root) throws IOException {
        StoryQuestDefaults.writeIfMissing(root);
    }
}
