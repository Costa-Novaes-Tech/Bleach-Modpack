package com.bleachmod.client.gui;

import com.bleachmod.client.gui.layout.LayoutContext;
import com.bleachmod.client.gui.layout.LayoutInteraction;
import com.bleachmod.client.gui.layout.LayoutScreen;
import com.bleachmod.common.data.*;
import com.bleachmod.common.evolution.FormNames;
import com.bleachmod.common.network.NetworkHandler;
import com.bleachmod.common.network.c2s.*;
import net.minecraft.network.chat.Component;

import java.util.List;

/**
 * Character sheet: player preview, resources, the seven attributes and the Zanpakuto
 * level purchase in the footer. Layout: gui_layouts/character.json.
 * Attribute i follows {@link AttributeData#IDS} (also the order of attribute_icons.png).
 */
public final class StatusScreen extends LayoutScreen {
    private float previewYaw;
    private boolean dragging;

    public StatusScreen() {
        super(Component.translatable("screen.bleachmod.status"), "character");
    }

    private PlayerData data() {
        return minecraft == null || minecraft.player == null ? null : PlayerCapability.get(minecraft.player).orElse(null);
    }

    @Override
    protected LayoutContext context() {
        LayoutContext ctx = new LayoutContext().text("title", title);
        PlayerData d = data();
        if (d == null) return ctx;
        ctx.entity("player", minecraft.player).number("preview_yaw", previewYaw);
        int points = (int) d.getResources().getTrainingPoints();
        ctx.text("sp", Component.translatable("hud.bleachmod.tp", points));
        ctx.text("bp", Component.translatable("screen.bleachmod.bp", Math.round(d.getBattlePower())));
        CharacterData character = d.getCharacter();
        ctx.text("forms", Component.translatable("screen.bleachmod.forms",
                FormNames.of(character, character.getActiveForm()), FormNames.of(character, character.getSelectedForm())));
        for (String form : List.of("shikai", "bankai"))
            ctx.text(form + "_mastery", Component.translatable("screen.bleachmod.mastery", FormNames.of(character, form),
                    (int) character.getMastery("zanpakuto", form)));

        List<String> ids = AttributeData.IDS;
        for (int i = 0; i < ids.size(); i++) {
            String id = ids.get(i);
            int cost = d.getAttributes().cost(id);
            boolean affordable = cost >= 0 && points >= cost;
            ctx.text("attr" + i + "_label", Component.translatable("attribute.bleachmod." + id).getString()
                    + "  " + d.getAttributes().level(id));
            ctx.text("attr" + i + "_cost", cost < 0 ? Component.translatable("screen.bleachmod.max")
                    : Component.translatable("screen.bleachmod.buy", cost));
            ctx.flag("attr" + i + "_ok", affordable);
            ctx.text("attr" + i + "_tip", Component.translatable("attribute.bleachmod." + id + ".desc").append("\n")
                    .append(Component.translatable(cost < 0 ? "screen.bleachmod.max" : affordable
                            ? "screen.bleachmod.purchase_remaining" : "screen.bleachmod.insufficient_points",
                            Math.max(0, points - cost))));
        }

        int level = d.getSkills().getLevel("zanpakuto");
        int cost = d.getSkills().skillCostForNextLevel("zanpakuto");
        boolean discovered = character.isFormDiscovered(level == 0 ? "shikai" : "bankai");
        boolean canBuy = level < 2 && discovered && points >= cost;
        ctx.flag("skill_ok", canBuy);
        ctx.text("skill_label", level >= 2 ? Component.translatable("screen.bleachmod.skill_max")
                : !discovered ? Component.translatable("screen.bleachmod.training_required")
                : Component.translatable("screen.bleachmod.skill_buy", level + 1, cost));
        ctx.text("skill_tip", Component.translatable(level >= 2 ? "screen.bleachmod.skill_max"
                : !discovered ? "screen.bleachmod.training_required"
                : canBuy ? "screen.bleachmod.purchase_remaining" : "screen.bleachmod.insufficient_points",
                Math.max(0, points - cost)));
        return ctx;
    }

    @Override
    protected void onAction(String action) {
        if (action.startsWith("buy:")) {
            int index = Integer.parseInt(action.substring(4));
            if (index >= 0 && index < AttributeData.IDS.size())
                NetworkHandler.sendToServer(new PurchaseAttributeC2S(AttributeData.IDS.get(index)));
        } else if (action.equals("skill")) {
            NetworkHandler.sendToServer(new UpdateSkillC2S("zanpakuto", UpdateSkillC2S.SkillAction.PURCHASE));
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        LayoutInteraction.Hit hit = interaction.hitAt(mouseX, mouseY);
        if (button == 0 && hit != null && "preview".equals(hit.id())) {
            dragging = true;
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (dragging && button == 0) {
            previewYaw = (previewYaw - (float) dragX * 2.0F) % 360.0F;
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0 && dragging) {
            dragging = false;
            return true;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }
}
