package com.bleachmod.client.gui;

import com.bleachmod.Reference;
import com.bleachmod.client.BleachTextures;
import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

/** Origin choice is a local preview; creation is confirmed in the appearance editor. */
public class ConfirmRaceScreen extends Screen {
    private static final ResourceLocation PORTRAITS = BleachTextures.gui("character/class_portraits.png");
    private static final String[] CLASSES = {"shinigami", "hollow", "quincy", "fullbringer"};
    private static final int[] COLORS = {0xEAC777, 0x65C5C7, 0x79B5ED, 0xCC8595};

    public ConfirmRaceScreen() {
        super(Component.translatable("screen.bleachmod.confirm_race"));
    }

    @Override
    protected void init() {
        int gap = Mth.clamp(width / 45, 8, 18);
        int cardWidth = Math.min(180, (width - 40 - gap * 3) / 4);
        int cardHeight = Math.min(height - 116, Math.round(cardWidth * 2.35F));
        int left = (width - (cardWidth * 4 + gap * 3)) / 2;
        int top = 70 + Math.max(0, (height - 116 - cardHeight) / 2);
        for (int i = 0; i < CLASSES.length; i++) {
            addRenderableWidget(new ClassCard(left + i * (cardWidth + gap), top, cardWidth, cardHeight, i));
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        double scale = Math.max((double) width / BleachTextures.CONFIRM_BG_W,
                (double) height / BleachTextures.CONFIRM_BG_H);
        int bgW = (int) Math.ceil(BleachTextures.CONFIRM_BG_W * scale);
        int bgH = (int) Math.ceil(BleachTextures.CONFIRM_BG_H * scale);
        BleachTextures.blit(graphics, BleachTextures.CONFIRM_BG, (width - bgW) / 2, (height - bgH) / 2,
                bgW, bgH, BleachTextures.CONFIRM_BG_W, BleachTextures.CONFIRM_BG_H);
        graphics.fill(0, 0, width, height, 0xAD060B17);
        graphics.fillGradient(0, 0, width, height, 0x66030A16, 0xCE030711);
        graphics.drawCenteredString(font, Component.translatable("screen.bleachmod.class_select.eyebrow"),
                width / 2, 14, 0xD4B575);
        graphics.drawCenteredString(font, title, width / 2, 31, 0xFFF3DB);
        graphics.drawCenteredString(font, Component.translatable("screen.bleachmod.class_select.subtitle"),
                width / 2, 48, 0xA6ADBD);
        graphics.drawCenteredString(font, Component.translatable("screen.bleachmod.class_select.footer"),
                width / 2, height - 21, 0xA6ADBD);
        super.render(graphics, mouseX, mouseY, partialTick);
    }

    private final class ClassCard extends AbstractButton {
        private final int index;
        private float emphasis;
        private long lastFrame = Util.getMillis();

        private ClassCard(int x, int y, int width, int height, int index) {
            super(x, y, width, height, Component.translatable("screen.bleachmod.class_select." + CLASSES[index]));
            this.index = index;
            active = index == 0;
        }

        @Override
        public void onPress() {
            if (active) minecraft.setScreen(new CharacterAppearanceScreen(Reference.RACE_SHINIGAMI, true));
        }

        @Override
        protected void renderWidget(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
            long now = Util.getMillis();
            float delta = Math.min(100, now - lastFrame) / 1000F;
            lastFrame = now;
            float target = active && isHoveredOrFocused() ? 1F : 0F;
            emphasis += (target - emphasis) * (1F - (float) Math.exp(-14F * delta));
            float zoom = 1F + emphasis * .035F;
            g.pose().pushPose();
            g.pose().translate(getX() + width / 2F, getY() + height / 2F, 0);
            g.pose().scale(zoom, zoom, 1);
            g.pose().translate(-width / 2F, -height / 2F, 0);
            g.fill(-2, -2, width + 2, height + 3, 0x90000000);
            int accent = COLORS[index];
            g.fill(-1, -1, width + 1, height + 1, active ? 0xFF000000 | accent : 0xFF3C475B);
            // Crop each atlas column to cover the card without distorting its character.
            float sourceHeight = Math.min(1024F, 384F * height / width);
            float sourceWidth = Math.min(384F, 1024F * width / height);
            g.blit(PORTRAITS, 0, 0, width, height, index * 384F + (384F - sourceWidth) / 2F,
                    Math.min(96F, (1024F - sourceHeight) / 2F), Math.round(sourceWidth), Math.round(sourceHeight), 1536, 1024);
            if (!active) g.fill(0, 0, width, height, 0x66091220);
            g.fillGradient(0, height / 2, width, height, 0x00040A14, 0xFA040A14);
            g.fill(0, height - 2, width, height, 0xFF000000 | accent);
            if (active) {
                g.fill(0, 0, width, 2, 0xFF000000 | accent);
                if (emphasis > .01F) g.fill(0, 0, width, height, ((int) (emphasis * 18) << 24) | 0xEAC777);
            } else {
                drawLock(g, width / 2, Math.max(22, height / 2 - 12));
            }
            int nameY = height - 43;
            if (index == 1 && width < 100) {
                g.drawCenteredString(font, "Hollow /", width / 2, nameY - 8, 0xE1E5EE);
                g.drawCenteredString(font, "Arrancar", width / 2, nameY + 2, 0xE1E5EE);
            } else {
                float textScale = Math.min(1F, (width - 8F) / font.width(getMessage()));
                g.pose().pushPose();
                g.pose().translate(width / 2F, nameY, 0);
                g.pose().scale(textScale, textScale, 1);
                g.drawCenteredString(font, getMessage(), 0, 0, active ? 0xFFF0CC : 0xE1E5EE);
                g.pose().popPose();
            }
            Component status = Component.translatable(active ? "screen.bleachmod.class_select.available"
                    : "screen.bleachmod.class_select.locked");
            g.drawCenteredString(font, status, width / 2, height - 22, active ? accent : 0x949EAF);
            g.pose().popPose();
        }

        private void drawLock(GuiGraphics g, int cx, int cy) {
            g.fill(cx - 12, cy - 13, cx + 12, cy + 16, 0xCC08101E);
            g.fill(cx - 5, cy - 9, cx + 5, cy - 7, 0xFFE1D6BE);
            g.fill(cx - 5, cy - 7, cx - 3, cy, 0xFFE1D6BE);
            g.fill(cx + 3, cy - 7, cx + 5, cy, 0xFFE1D6BE);
            g.fill(cx - 8, cy - 1, cx + 8, cy + 11, 0xFFE1D6BE);
            g.fill(cx - 1, cy + 3, cx + 1, cy + 8, 0xFF283246);
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput output) {
            defaultButtonNarrationText(output);
        }
    }

    @Override public boolean shouldCloseOnEsc() { return false; }
    @Override public boolean isPauseScreen() { return false; }
}
