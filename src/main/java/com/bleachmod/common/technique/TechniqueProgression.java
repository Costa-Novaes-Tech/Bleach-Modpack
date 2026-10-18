package com.bleachmod.common.technique;

import com.bleachmod.common.data.*;
import com.bleachmod.common.evolution.TransformationsHelper;
import com.bleachmod.init.ModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

/** Shared access rules; existing kits retain their own executors and costs. */
public final class TechniqueProgression {
    public static final int SHIKAI_MASTERY = 20, BANKAI_MASTERY = 25;
    private TechniqueProgression() { }
    public static int requirement(int slot) { return TechniqueDefinitions.slotRequirement(slot); }
    public static boolean available(CharacterData character, String form, int slot) {
        var definition = TechniqueDefinitions.find(character.getZanpakutoIdentity(), form, slot);
        return definition.isPresent() && character.supportsForm(form) && (character.isTechniqueDiscovered(form, slot)
                || character.getMastery("zanpakuto", form) >= definition.get().mastery());
    }
    public static boolean equipped(ServerPlayer player, PlayerData data) {
        return switch (data.getCharacter().getZanpakutoIdentity()) {
            case CharacterData.ASAUCHI -> player.getMainHandItem().is(ModItems.ASAUCHI.get());
            case CharacterData.RYUJIN -> player.getMainHandItem().is(ModItems.RYUJIN_JAKKA.get());
            case CharacterData.HYORINMARU -> player.getMainHandItem().is(ModItems.HYORINMARU.get());
            case CharacterData.SENBONZAKURA -> player.getMainHandItem().is(ModItems.SENBONZAKURA.get());
            case CharacterData.BENIHIME -> player.getMainHandItem().is(ModItems.BENIHIME.get());
            case CharacterData.NOZARASHI -> player.getMainHandItem().is(ModItems.NOZARASHI.get());
            default -> false;
        };
    }
    public static boolean authorize(ServerPlayer player, PlayerData data, int slot) {
        if (data.getStatus().isDodging() || data.getGuard().active() || data.getGuard().broken()) return false;
        if (!data.getStatus().hasCreatedCharacter() || !player.isAlive() || player.isSpectator()
                || !"shinigami".equals(data.getCharacter().getRace()) || slot < 1 || slot > 4) return false;
        if (CharacterData.ASAUCHI.equals(data.getCharacter().getZanpakutoIdentity())) {
            player.displayClientMessage(Component.translatable("message.bleachmod.asauchi.no_techniques"), true); return false;
        }
        if (!equipped(player, data) || !"zanpakuto".equals(data.getCharacter().getActiveFormGroup())) return false;
        String form = data.getCharacter().getActiveForm();
        if (!TransformationsHelper.isUnlocked(data, "zanpakuto", form)) return false;
        if (TechniqueDefinitions.find(data.getCharacter().getZanpakutoIdentity(), form, slot).isEmpty()) {
            player.displayClientMessage(Component.translatable("message.bleachmod."+data.getCharacter().getZanpakutoIdentity()+".sealed"), true); return false;
        }
        if (!available(data.getCharacter(), form, slot)) {
            player.displayClientMessage(Component.translatable("message.bleachmod.technique.mastery_required",
                    TechniqueDefinitions.require(data.getCharacter().getZanpakutoIdentity(), form, slot).mastery()), true); return false;
        }
        MasteryPracticeService.noteIntent(player);
        return true;
    }
    public static void unlockEligible(CharacterData character) {
        String form = character.getActiveForm();
        for (int slot = 1; slot <= 4; slot++) if (available(character, form, slot)) character.unlockTechnique(form, slot);
        // Provisional practical milestones. Skill purchase and the transformation chain still apply.
        if (character.getMastery("zanpakuto", "sealed") >= SHIKAI_MASTERY) character.unlockForm("shikai");
        if (character.isFormDiscovered("shikai") && character.getMastery("zanpakuto", "shikai") >= BANKAI_MASTERY) character.unlockForm("bankai");
    }
}
