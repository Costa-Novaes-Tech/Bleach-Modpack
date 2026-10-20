package com.bleachmod.client.gui;

import com.bleachmod.client.gui.layout.LayoutContext;
import com.bleachmod.client.gui.layout.LayoutScreen;
import com.bleachmod.client.hud.ZanpakutoHud;
import com.bleachmod.common.data.*;
import com.bleachmod.common.evolution.FormNames;
import com.bleachmod.common.evolution.TransformationsHelper;
import com.bleachmod.common.network.NetworkHandler;
import com.bleachmod.common.network.c2s.SelectFormC2S;
import com.bleachmod.common.technique.MasteryFeedback;
import com.bleachmod.common.technique.TechniqueDefinitions;
import net.minecraft.network.chat.Component;

import java.util.List;

/**
 * Form selection wheel plus Zanpakuto guide. Layout: gui_layouts/form_wheel.json.
 * Clicking an available medal selects that form as the target and closes the screen
 * (R transforms), exactly like the previous button wheel.
 */
public final class FormWheelScreen extends LayoutScreen {
    static final List<String> FORMS = List.of("sealed", "shikai", "bankai");

    public FormWheelScreen() {
        super(Component.translatable("screen.bleachmod.wheel"), "form_wheel");
    }

    private PlayerData data() {
        return minecraft == null || minecraft.player == null ? null : PlayerCapability.get(minecraft.player).orElse(null);
    }

    @Override
    public void tick() {
        if (minecraft.player == null) onClose();
    }

    @Override
    protected LayoutContext context() {
        LayoutContext ctx = new LayoutContext().text("title", title);
        PlayerData d = data();
        if (d == null) return ctx;
        CharacterData character = d.getCharacter();
        String active = character.getActiveForm();
        for (String form : FORMS) {
            boolean unlocked = TransformationsHelper.isUnlocked(d, "zanpakuto", form);
            boolean selectable = TransformationsHelper.isSelectable(d, "zanpakuto", form);
            ctx.flag(form + "_selectable", selectable);
            ctx.flag(form + "_active", form.equals(active));
            // The Bankai art already carries its padlock; the others get the overlay.
            ctx.flag(form + "_show_lock", !selectable && !"bankai".equals(form));
            ctx.text(form + "_tip", selectable ? "" : Component.translatable(unlocked
                    ? "screen.bleachmod.wheel_chain" : "hud.bleachmod.mastery.form_locked"));
        }

        String identity = character.getZanpakutoIdentity();
        double mastery = character.getMastery("zanpakuto", active);
        ctx.text("zanpakuto_name", Component.translatable("item.bleachmod." + identity));
        ctx.text("current_form", FormNames.of(character, active));
        ctx.text("progress_text", MasteryFeedback.value(mastery) + " / 100");
        ctx.value("progress", (float) (mastery / 100.0));
        for (String form : FORMS) ctx.flag("progress_" + form, form.equals(active));
        boolean practice = CharacterData.ASAUCHI.equals(identity) || TechniqueDefinitions.find(identity, active, 1).isEmpty();
        ctx.flag("has_practice", practice);
        ctx.text("practice", Component.translatable("hud.bleachmod.asauchi.practice"));
        Component next = ZanpakutoHud.nextUnlock(d);
        ctx.flag("has_next", next != null);
        if (next != null) ctx.text("next_unlock", next);
        ctx.text("hint", Component.translatable("screen.bleachmod.wheel_hint"));
        ctx.text("hint_skills", Component.translatable("screen.bleachmod.no_active_skills"));
        return ctx;
    }

    @Override
    protected void onAction(String action) {
        if (!action.startsWith("select:")) return;
        String form = action.substring("select:".length());
        PlayerData d = data();
        if (d == null || !TransformationsHelper.isSelectable(d, "zanpakuto", form)) return;
        NetworkHandler.sendToServer(new SelectFormC2S("zanpakuto", form));
        onClose();
    }
}
