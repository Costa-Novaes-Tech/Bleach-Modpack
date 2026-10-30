package com.bleachmod.client.gui;

import com.bleachmod.common.data.PlayerCapability;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import java.util.Locale;

/** Informational formation record. Reading never awards progress or teaches a skill. */
public final class AcademyScreen extends Screen {
    private final Screen parent;

    public AcademyScreen(Screen parent) {
        super(Component.translatable("screen.bleachmod.academy"));
        this.parent = parent;
    }

    @Override protected void init() {
        addRenderableWidget(Button.builder(Component.translatable("gui.back"), b -> onClose())
                .bounds(width / 2 - 70, height - 27, 140, 20).build());
    }

    @Override public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        renderBackground(g);
        int panelWidth = Math.min(460, width - 24);
        int left = (width - panelWidth) / 2;
        g.fill(left, 12, left + panelWidth, height - 35, 0xEB0A1220);
        g.fill(left, 12, left + panelWidth, 14, 0xFFD6B56E);
        g.drawCenteredString(font, title, width / 2, 24, 0xFFF0CC);
        var data = minecraft.player == null ? null : PlayerCapability.get(minecraft.player).orElse(null);
        String stage = data == null ? "not_started" : data.getAcademy().stage().name().toLowerCase(Locale.ROOT);
        int y = 44;
        for (String key : new String[]{"stage." + stage, "intro", "zanjutsu", "hakuda", "hoho", "kido", "notice"}) {
            var lines = font.split(Component.translatable("screen.bleachmod.academy." + key), panelWidth - 24);
            for (var line : lines) {
                g.drawString(font, line, left + 12, y, key.startsWith("stage.") ? 0xE9C979 : 0xD6DEEB);
                y += 10;
            }
            y += 5;
        }
        super.render(g, mouseX, mouseY, partialTick);
    }

    @Override public void onClose() { minecraft.setScreen(parent); }
    @Override public boolean isPauseScreen() { return false; }
}
