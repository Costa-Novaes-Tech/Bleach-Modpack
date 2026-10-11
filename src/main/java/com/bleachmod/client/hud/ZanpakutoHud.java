package com.bleachmod.client.hud;

import com.bleachmod.client.input.ModKeybinds;
import com.bleachmod.common.data.*;
import com.bleachmod.common.evolution.*;
import com.bleachmod.common.technique.MasteryFeedback;
import com.bleachmod.common.technique.TechniqueProgression;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

/** Compact right-hand guide; requirements come from installed form definitions, not invented mastery tiers. */
public final class ZanpakutoHud {
    private ZanpakutoHud() { }
    public static String abilityKey(String identity, String form, int slot) {
        if (CharacterData.ASAUCHI.equals(identity)) return "message.bleachmod.asauchi.no_techniques";
        boolean bankai = "bankai".equals(form), sealed = "sealed".equals(form);
        if (CharacterData.HYORINMARU.equals(identity)) return "ability.bleachmod.hyorinmaru." + (sealed ? "sealed_" : "slot_") + slot;
        String name = switch (slot) {
            case 1 -> bankai ? "flame_dash" : "ignition";
            case 2 -> bankai ? "flame_fan" : "flame_barrage";
            case 3 -> bankai ? "flame_tornado" : "flame_wall";
            default -> bankai ? "steam_cut" : "flame_bats";
        };
        return "ability.bleachmod.ryujin." + name;
    }
    public static final IGuiOverlay OVERLAY = (gui, g, partialTick, w, h) -> {
        var mc = Minecraft.getInstance(); if (mc.player == null || mc.options.hideGui) return;
        PlayerCapability.get(mc.player).ifPresent(d -> {
            if (!d.isDataLoaded() || !d.getStatus().hasCreatedCharacter() || !"shinigami".equals(d.getCharacter().getRace())) return;
            String form = d.getCharacter().getActiveForm(), identity = d.getCharacter().getZanpakutoIdentity();
            float scale = Math.min(0.85F, Math.min(w / 500F, h / 260F));
            int width = 188, height = 134;
            float x = w / scale - width - 8, y = h / scale - height - 66;
            g.pose().pushPose(); g.pose().scale(scale, scale, 1); g.pose().translate(x, Math.max(110, y), 0);
            g.fill(0,0,width,height,0xCC102333); g.fill(0,0,2,height,0xFF85D7F0);
            g.drawString(mc.font, Component.translatable("item.bleachmod." + identity), 8, 6, 0xDFF7FF, false);
            String mastery = Component.translatable("form.bleachmod." + form).getString() + " · " + MasteryFeedback.value(d.getCharacter().getMastery("zanpakuto", form)) + "/100";
            g.drawString(mc.font, mc.font.plainSubstrByWidth(mastery, width - 16), 8, 19, 0x89CDE8, false);
            var keys = new net.minecraft.client.KeyMapping[]{ModKeybinds.TECHNIQUE_SLOT_1, ModKeybinds.TECHNIQUE_SLOT_2, ModKeybinds.TECHNIQUE_SLOT_3, ModKeybinds.TECHNIQUE_SLOT_4};
            boolean unlocked = TransformationsHelper.isUnlocked(d, "zanpakuto", form);
            boolean asauchi = CharacterData.ASAUCHI.equals(identity);
            if (asauchi) g.drawString(mc.font, Component.translatable("hud.bleachmod.asauchi.practice"), 8, 38, 0xFFFFFF, false);
            for (int i = 0; i < 4; i++) {
                if (asauchi) break;
                int slot = i + 1;
                String name = Component.translatable(abilityKey(identity, form, i + 1)).getString();
                String key = "[" + keys[i].getTranslatedKeyMessage().getString() + "] ";
                boolean available = unlocked && com.bleachmod.common.technique.TechniqueProgression.available(d.getCharacter(), form, slot);
                g.drawString(mc.font, mc.font.plainSubstrByWidth(key + name, width - 16), 8, 33 + i * 17, available ? 0xFFFFFF : 0xA6A6A6, false);
                String requirement = available ? Component.translatable("hud.bleachmod.mastery.technique", MasteryFeedback.value(d.getCharacter().getTechniqueMastery(form, slot))).getString()
                        : !unlocked ? Component.translatable("hud.bleachmod.mastery.form_locked").getString()
                        : Component.translatable("hud.bleachmod.mastery.required", MasteryFeedback.value(d.getCharacter().getMastery("zanpakuto", form)), TechniqueProgression.requirement(slot)).getString();
                g.drawString(mc.font, mc.font.plainSubstrByWidth(requirement, width - 16), 8, 42 + i * 17, 0x85AAC2, false);
            }
            String next = "sealed".equals(form) ? "shikai" : "shikai".equals(form) ? "bankai" : "";
            FormData nextForm = next.isEmpty() ? null : FormRegistry.getForm(d.getCharacter().getRace(), "zanpakuto", next);
            if (nextForm != null) {
                int threshold = "shikai".equals(next) ? TechniqueProgression.SHIKAI_MASTERY : TechniqueProgression.BANKAI_MASTERY;
                String sourceForm = "shikai".equals(next) ? "sealed" : "shikai";
                double current = d.getCharacter().getMastery("zanpakuto", sourceForm);
                var nextName = Component.translatable("form.bleachmod." + next);
                String gate = TransformationsHelper.isUnlocked(d, "zanpakuto", next)
                        ? Component.translatable("hud.bleachmod.mastery.form_ready", nextName).getString()
                        : current < threshold ? Component.translatable("hud.bleachmod.mastery.form_train", nextName, MasteryFeedback.value(current), threshold).getString()
                        : Component.translatable("hud.bleachmod.mastery.form_purchase", nextName).getString();
                g.drawString(mc.font, mc.font.plainSubstrByWidth(gate, width - 16), 8, 107, 0xD5BF78, false);
            }
            if (d.getStatus().areCooldownsDisabled() || d.getResources().areCostsDisabled())
                g.drawString(mc.font, Component.translatable("hud.bleachmod.mastery.test_mode"), 8, 120, 0xF1B46B, false);
            g.pose().popPose();
        });
    };
}
