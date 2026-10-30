package com.bleachmod.common.data;

import net.minecraft.nbt.CompoundTag;

/** Persistent personal formation, independent of rank, powers and temporary combat state. */
public final class AcademyData {
    public enum Stage { NOT_STARTED, AWAITING_GUIDANCE, TRAINING, LEGACY_UNTRACKED }
    private Stage stage = Stage.NOT_STARTED;

    public Stage stage() { return stage; }

    public void startJourney() { stage = Stage.AWAITING_GUIDANCE; }

    /** Called by a future validated server-side instructor interaction, never by a UI request. */
    public boolean beginTraining() {
        if (stage != Stage.AWAITING_GUIDANCE) return false;
        stage = Stage.TRAINING;
        return true;
    }

    public void loadLegacy(boolean existingShinigami) {
        stage = existingShinigami ? Stage.LEGACY_UNTRACKED : Stage.NOT_STARTED;
    }

    public CompoundTag save() {
        var tag = new CompoundTag();
        tag.putInt("version", 1);
        tag.putString("stage", stage.name());
        return tag;
    }

    public void load(CompoundTag tag) {
        // Unknown/malformed records never infer graduation or grant access.
        stage = Stage.NOT_STARTED;
        if (tag.getInt("version") != 1) return;
        try { stage = Stage.valueOf(tag.getString("stage")); }
        catch (IllegalArgumentException ignored) { }
    }
}
