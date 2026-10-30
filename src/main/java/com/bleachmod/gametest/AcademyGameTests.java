package com.bleachmod.gametest;

import com.bleachmod.common.data.*;
import net.minecraft.gametest.framework.*;
import net.minecraftforge.gametest.*;

@GameTestHolder("bleachmod") @PrefixGameTestTemplate(false)
public final class AcademyGameTests {
    @GameTest(template = "empty")
    public static void formationSurvivesSaveCloneAndTransientCleanup(GameTestHelper h) {
        var data = new PlayerData();
        data.initializeShinigami();
        h.assertTrue(data.getAcademy().stage() == AcademyData.Stage.AWAITING_GUIDANCE, "New student skips orientation");
        h.assertTrue(data.getAcademy().beginTraining(), "Orientation cannot advance");
        h.assertTrue(!data.getAcademy().beginTraining(), "Repeated interaction advances again");
        data.resetTransientState(); data.resetLevelBoundState();
        var clone = new PlayerData(); clone.copyFrom(data);
        h.assertTrue(clone.getAcademy().stage() == AcademyData.Stage.TRAINING, "Formation lost after clone/save/lifecycle");
        h.assertTrue(clone.getCharacter().getMastery("zanpakuto", "shikai") == 0, "Formation grants mastery");
        clone.load(new net.minecraft.nbt.CompoundTag());
        h.assertTrue(clone.getAcademy().stage() == AcademyData.Stage.TRAINING, "Partial sync clears formation");
        h.succeed();
    }

    @GameTest(template = "empty")
    public static void legacyAndMalformedFormationNeverGrantProgress(GameTestHelper h) {
        var original = new PlayerData(); original.initializeShinigami();
        original.getCharacter().setMastery("zanpakuto", "shikai", 45);
        var legacy = original.save(); legacy.remove("academy");
        var loaded = new PlayerData(); loaded.load(legacy);
        h.assertTrue(loaded.getAcademy().stage() == AcademyData.Stage.LEGACY_UNTRACKED, "Legacy student is falsely graduated or reset");
        h.assertTrue(loaded.getCharacter().getMastery("zanpakuto", "shikai") == 45, "Legacy power was erased");
        h.assertTrue(!loaded.getAcademy().beginTraining(), "Legacy implicitly enters new curriculum");
        legacy.remove("schemaVersion");
        loaded.load(legacy);
        h.assertTrue(loaded.getAcademy().stage() == AcademyData.Stage.LEGACY_UNTRACKED, "Unversioned save treated as new student");
        var invalid = new net.minecraft.nbt.CompoundTag(); invalid.putInt("version", 1); invalid.putString("stage", "GRADUATED");
        loaded.getAcademy().load(invalid);
        h.assertTrue(loaded.getAcademy().stage() == AcademyData.Stage.NOT_STARTED, "Unknown stage grants access");
        loaded.load(legacy); loaded.initializeShinigami();
        h.assertTrue(loaded.getAcademy().stage() == AcademyData.Stage.AWAITING_GUIDANCE, "Explicit new journey did not restart formation");
        h.succeed();
    }
}
