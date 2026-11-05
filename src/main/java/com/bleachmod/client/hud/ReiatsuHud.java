package com.bleachmod.client.hud;

import com.bleachmod.client.gui.layout.LayoutContext;
import com.bleachmod.client.gui.layout.LayoutRenderer;
import com.bleachmod.common.CombatBalance;
import com.bleachmod.common.data.AttributeData;
import com.bleachmod.common.data.PlayerCapability;
import com.bleachmod.common.data.PlayerData;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

/**
 * Renders the compact Bleach HUD: VIDA / REIATSU / TRANSFORMACAO stay in a
 * responsive panel at the upper-left, while the detailed resource information
 * sits beside the hotbar. Vanilla hearts are hidden by VanillaHealthHider once a
 * character exists, so this HUD owns the player's HP visualization.
 */
public final class ReiatsuHud {
    private static float shownCharge;
    private static long previousRender;

    private ReiatsuHud() {
    }

    public static final IGuiOverlay OVERLAY = (gui, graphics, partialTick, screenWidth, screenHeight) -> {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.options.hideGui) {
            return;
        }
        PlayerCapability.get(mc.player).ifPresent(data -> {
            if (!data.isDataLoaded() || !data.getStatus().hasCreatedCharacter()) {
                return;
            }
            renderPanel(mc, graphics, screenWidth, screenHeight, data);
            GuardVisuals.hud(graphics, screenWidth, screenHeight, data);
        });
    };

    private static void renderPanel(Minecraft mc, net.minecraft.client.gui.GuiGraphics g,
                                    int screenWidth, int screenHeight, PlayerData data) {
        // Positions, sizes and colors live in assets/bleachmod/gui_layouts/hud_*.json;
        // this method only supplies the live values.
        var resources = data.getResources();
        LayoutContext context = new LayoutContext()
                .value("health", computeHealthRatio(mc, data))
                .value("reiatsu", resources.getCurrentReiatsu() / Math.max(1f, resources.getMaxReiatsu()))
                .value("transform", computeTransformRatio(mc, data))
                .value("stamina", resources.getStamina() / Math.max(1f, resources.getMaxStamina()))
                .text("stamina_now", (int) Math.ceil(resources.getStamina()))
                .text("stamina_max", (int) Math.ceil(resources.getMaxStamina()))
                .text("reiatsu_now", (int) resources.getCurrentReiatsu())
                .text("reiatsu_max", (int) resources.getMaxReiatsu())
                .text("active_form", "quincy".equals(data.getCharacter().getRace()) ? net.minecraft.network.chat.Component.literal("Quincy") : com.bleachmod.common.evolution.FormNames.of(data.getCharacter(), data.getCharacter().getActiveForm()))
                .text("target_form", "quincy".equals(data.getCharacter().getRace()) ? net.minecraft.network.chat.Component.literal("Quincy") : com.bleachmod.common.evolution.FormNames.of(data.getCharacter(), data.getCharacter().getTargetForm()))
                .flag("form_pending", data.getCharacter().hasPendingTarget())
                .text("tp", (int) resources.getTrainingPoints())
                .text("cooldowns", formatCooldowns(data))
                .flag("stamina_exhausted", resources.isStaminaExhausted())
                .flag("in_combat", data.getStatus().isHudInCombat());

        LayoutRenderer.render(g, mc.font, "hud_status", context, screenWidth, screenHeight);
        LayoutRenderer.render(g, mc.font, "hud_info", context, screenWidth, screenHeight);
    }

    private static String formatCooldowns(PlayerData data) {
        if (data.getStatus().areHudCooldownsDisabled()) return "TEST";
        int[] values = data.getStatus().getHudCooldowns();
        String[] slots = {"1", "2", "3", "4"};
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < slots.length; i++) {
            if (i > 0) result.append("  ");
            result.append(slots[i]).append(":").append(values[i] == 0 ? "OK" : (int) Math.ceil(values[i] / 20.0) + "s");
        }
        return result.toString();
    }

    private static float computeHealthRatio(Minecraft mc, PlayerData data) {
        float max = computeMaxHealth(mc, data);
        if (max <= 0f) return 0f;
        return Math.max(0f, Math.min(1f, mc.player.getHealth() / max));
    }

    private static float computeMaxHealth(Minecraft mc, PlayerData data) {
        // Client-side: vanilla max HP plus the vitality bonus our server transient modifier adds.
        // The transient modifier is applied server-side only; mirror it locally so the bar matches
        // the real server HP.
        float base = (float) mc.player.getAttributeBaseValue(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH);
        int vitalityRank = Math.max(0, data.getAttributes().level(AttributeData.VITALITY));
        return base + vitalityRank * CombatBalance.HEALTH_PER_VITALITY_RANK;
    }

    private static float computeTransformRatio(Minecraft mc, PlayerData data) {
        int target = data.getResources().getActionCharge();
        if (data.getStatus().isActionCharging()) {
            long now = System.nanoTime();
            float seconds = previousRender == 0L ? 0f : Math.min(.1f, (now - previousRender) / 1_000_000_000f);
            previousRender = now;
            shownCharge += (target - shownCharge) * Math.min(1f, seconds * 18f);
        } else {
            shownCharge = target;
            previousRender = 0L;
        }
        return Math.max(0f, Math.min(1f, shownCharge / 100f));
    }
}
