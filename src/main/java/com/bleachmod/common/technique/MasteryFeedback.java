package com.bleachmod.common.technique;

import com.bleachmod.common.data.CharacterData;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.ArrayList;

/** Pure display and transition data; never grants mastery or changes unlocks. */
public final class MasteryFeedback {
    private MasteryFeedback() { }
    public record Snapshot(String identity, String form, int availableSlots, boolean shikai, boolean bankai) { }
    public record Unlock(int slot, String form) { }
    public static String value(double mastery) {
        if (!Double.isFinite(mastery)) return "0";
        return BigDecimal.valueOf(Math.max(0, Math.min(100, mastery))).setScale(1, RoundingMode.DOWN).stripTrailingZeros().toPlainString();
    }
    public static Snapshot snapshot(CharacterData character) {
        String form = character.getActiveForm(); int slots = 0;
        for (int slot = 1; slot <= 4; slot++) if (TechniqueProgression.available(character, form, slot)) slots |= 1 << slot;
        return new Snapshot(character.getZanpakutoIdentity(), form, slots, character.isFormDiscovered("shikai"), character.isFormDiscovered("bankai"));
    }
    public static List<Unlock> gained(Snapshot before, CharacterData character) {
        Snapshot after = snapshot(character);
        if (!before.identity.equals(after.identity) || !before.form.equals(after.form)) return List.of();
        List<Unlock> result = new ArrayList<>();
        for (int slot = 1; slot <= 4; slot++) if ((before.availableSlots & 1 << slot) == 0 && (after.availableSlots & 1 << slot) != 0)
            result.add(new Unlock(slot, after.form));
        if (!before.shikai && after.shikai) result.add(new Unlock(0, "shikai"));
        if (!before.bankai && after.bankai) result.add(new Unlock(0, "bankai"));
        return List.copyOf(result);
    }
}
