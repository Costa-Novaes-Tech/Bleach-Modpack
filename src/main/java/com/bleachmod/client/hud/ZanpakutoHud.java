package com.bleachmod.client.hud;

import com.bleachmod.client.input.ModKeybinds;
import com.bleachmod.common.data.*;
import com.bleachmod.common.evolution.*;
import com.bleachmod.common.technique.MasteryFeedback;
import com.bleachmod.common.technique.TechniqueProgression;
import com.bleachmod.common.technique.TechniqueDefinitions;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

/** Compact right-hand guide; requirements come from installed form definitions, not invented mastery tiers. */
public final class ZanpakutoHud {
    private ZanpakutoHud() { }
    public static String abilityKey(String identity, String form, int slot) {
        if (CharacterData.ASAUCHI.equals(identity)) return "message.bleachmod.asauchi.no_techniques";
        return TechniqueDefinitions.find(identity, form, slot).map(TechniqueDefinitions.Definition::nameKey).orElse("message.bleachmod."+identity+".sealed");
    }
    /** "Shikai: train 0/20", "...: available" or "...: purchase bond"; null after Bankai. */
    public static Component nextUnlock(PlayerData d) {
        String form = d.getCharacter().getActiveForm();
        String next = "sealed".equals(form) ? "shikai" : "shikai".equals(form) ? "bankai" : "";
        FormData nextForm = next.isEmpty() ? null : FormRegistry.getForm(d.getCharacter().getRace(), "zanpakuto", next);
        if (nextForm == null) return null;
        int threshold = "shikai".equals(next) ? TechniqueProgression.SHIKAI_MASTERY : TechniqueProgression.BANKAI_MASTERY;
        String sourceForm = "shikai".equals(next) ? "sealed" : "shikai";
        double current = d.getCharacter().getMastery("zanpakuto", sourceForm);
        var nextName = com.bleachmod.common.evolution.FormNames.of(d.getCharacter(), next);
        return TransformationsHelper.isUnlocked(d, "zanpakuto", next)
                ? Component.translatable("hud.bleachmod.mastery.form_ready", nextName)
                : current < threshold ? Component.translatable("hud.bleachmod.mastery.form_train", nextName, MasteryFeedback.value(current), threshold)
                : Component.translatable("hud.bleachmod.mastery.form_purchase", nextName);
    }

    public static final IGuiOverlay OVERLAY = (gui, g, partialTick, w, h) -> {
        var mc = Minecraft.getInstance(); if (mc.player == null || mc.options.hideGui) return;
        PlayerCapability.get(mc.player).ifPresent(d -> {
            if (!d.isDataLoaded() || !d.getStatus().hasCreatedCharacter() || !"shinigami".equals(d.getCharacter().getRace())) return;
            String form = d.getCharacter().getActiveForm(), identity = d.getCharacter().getZanpakutoIdentity();
            float scale = Math.min(0.85F, Math.min(w / 500F, h / 260F));
            int width = 188, height = 167;
            float x = w / scale - width - 8, y = h / scale - height - 66;
            g.pose().pushPose(); g.pose().scale(scale, scale, 1); g.pose().translate(x, Math.max(110, y), 0);
            g.fill(0,0,width,height,0xCC102333); g.fill(0,0,2,height,0xFF85D7F0);
            g.drawString(mc.font, Component.translatable("item.bleachmod." + identity), 8, 6, 0xDFF7FF, false);
            String mastery = com.bleachmod.common.evolution.FormNames.of(identity, form).getString() + " · " + MasteryFeedback.value(d.getCharacter().getMastery("zanpakuto", form)) + "/100";
            g.drawString(mc.font, mc.font.plainSubstrByWidth(mastery, width - 16), 8, 19, 0x89CDE8, false);
            var keys = new net.minecraft.client.KeyMapping[]{ModKeybinds.TECHNIQUE_SLOT_1, ModKeybinds.TECHNIQUE_SLOT_2, ModKeybinds.TECHNIQUE_SLOT_3, ModKeybinds.TECHNIQUE_SLOT_4};
            boolean unlocked = TransformationsHelper.isUnlocked(d, "zanpakuto", form);
            boolean asauchi = CharacterData.ASAUCHI.equals(identity) || TechniqueDefinitions.find(identity,form,1).isEmpty();
            if (asauchi) g.drawString(mc.font, Component.translatable("hud.bleachmod.asauchi.practice"), 8, 38, 0xFFFFFF, false);
            for (int i = 0; i < 4; i++) {
                if (asauchi) break;
                int slot = i + 1;
                var definition = TechniqueDefinitions.require(identity, form, slot);
                String name = Component.translatable(abilityKey(identity, form, i + 1)).getString();
                if(identity.equals("hyorinmaru") && form.equals("bankai") && slot==4 && com.bleachmod.client.model.ElementalActionVisuals.bloomReady(mc.player))name=Component.translatable("ability.bleachmod.hyorinmaru.bloom").getString();
                var senbon=d.getStatus().getSenbonHud();
                if (CharacterData.SENBONZAKURA.equals(identity) && form.equals("bankai")) {
                    if (slot==2 && senbon.getInt("fieldRemaining")>0 && senbon.getInt("fieldReady")==0)
                        name=Component.translatable(TechniqueDefinitions.FLORAL_BLOOM.nameKey()).getString();
                    if (slot==3 && senbon.getInt("sceneRemaining")>0 && senbon.getInt("sceneReady")==0)
                        name=Component.translatable(TechniqueDefinitions.IKKA.nameKey()).getString();
                }
                String key = "[" + keys[i].getTranslatedKeyMessage().getString() + "] ";
                boolean available = unlocked && com.bleachmod.common.technique.TechniqueProgression.available(d.getCharacter(), form, slot);
                g.drawString(mc.font, mc.font.plainSubstrByWidth(key + name, width - 16), 8, 33 + i * 25, available ? 0xFFFFFF : 0xA6A6A6, false);
                if (definition.id().equals("unavailable")) continue;
                String requirement = available ? Component.translatable("hud.bleachmod.mastery.technique", MasteryFeedback.value(d.getCharacter().getTechniqueMastery(form, slot))).getString()
                        : !unlocked ? Component.translatable("hud.bleachmod.mastery.form_locked").getString()
                        : Component.translatable("hud.bleachmod.mastery.required", MasteryFeedback.value(d.getCharacter().getMastery("zanpakuto", form)), definition.mastery()).getString();
                g.drawString(mc.font, mc.font.plainSubstrByWidth(requirement, width - 16), 8, 42 + i * 25, 0x85AAC2, false);
                String resources = definition.drainPerTick() > 0
                        ? Component.translatable("hud.bleachmod.technique.drain", MasteryFeedback.value(definition.drainPerTick() * 20)).getString()
                        : Component.translatable("hud.bleachmod.technique.cost", MasteryFeedback.value(definition.cost()), MasteryFeedback.value(definition.cooldownTicks() / 20.0)).getString();
                if (CharacterData.SENBONZAKURA.equals(identity) && form.equals("bankai")) {
                    if (slot==2 && senbon.getInt("fieldRemaining")>0)
                        resources=Component.translatable(senbon.getBoolean("bloom") ? "hud.bleachmod.senbon.bloom" : "hud.bleachmod.senbon.field",
                                (senbon.getInt("fieldReady")+19)/20,(senbon.getInt("fieldRemaining")+19)/20).getString();
                    if (slot==3 && senbon.getInt("sceneRemaining")>0)
                        resources=Component.translatable("hud.bleachmod.senbon.scene",(senbon.getInt("sceneReady")+19)/20,(senbon.getInt("sceneRemaining")+19)/20).getString();
                    if(slot==4 && d.getCharacter().getSenbonBlade()==2)resources=Component.translatable("hud.bleachmod.senbon.haku_charge").getString();
                }
                if(CharacterData.SENBONZAKURA.equals(identity) && !form.equals("bankai") && slot==3 && senbon.getInt("prison")>0)
                    resources=Component.translatable("hud.bleachmod.senbon.prison",(senbon.getInt("prison")+19)/20).getString();
                if(CharacterData.BENIHIME.equals(identity)) {
                    var view=d.getStatus().getBenihimeHud();
                    if(form.equals("shikai") && slot==2 && view.getInt("shield")>0)resources=Component.translatable("hud.bleachmod.benihime.shield",view.getInt("shield")).getString();
                    if(form.equals("shikai") && slot==4)resources=Component.translatable(view.getInt("net")>0?"hud.bleachmod.benihime.detonate":"hud.bleachmod.benihime.thread_lash").getString();
                    if(form.equals("bankai") && slot==3)resources=Component.translatable("hud.bleachmod.benihime.path_charges",view.getInt("pathCharges")).getString();
                }
                g.drawString(mc.font, mc.font.plainSubstrByWidth(resources, width - 16), 8, 51 + i * 25, 0x85AAC2, false);
            }
            Component gate = nextUnlock(d);
            if (gate != null)
                g.drawString(mc.font, mc.font.plainSubstrByWidth(gate.getString(), width - 16), 8, 140, 0xD5BF78, false);
            if (d.getStatus().areCooldownsDisabled() || d.getResources().areCostsDisabled())
                g.drawString(mc.font, Component.translatable("hud.bleachmod.mastery.test_mode"), 8, 153, 0xF1B46B, false);
            else if (CharacterData.SENBONZAKURA.equals(identity) && form.equals("bankai"))
                g.drawString(mc.font,Component.translatable("hud.bleachmod.senbon.reach"),8,153,0xFFBBD8,false);
            else if(CharacterData.BENIHIME.equals(identity) && form.equals("bankai"))
                g.drawString(mc.font,Component.translatable("hud.bleachmod.benihime.regen"),8,153,0xF1BBC5,false);
            g.pose().popPose();
        });
    };
}
