package com.bleachmod.client.gui.layout;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;

/**
 * A screen drawn entirely from a layout file. Subclasses supply the live values
 * ({@link #context()}) and react to button actions ({@link #onAction(String)}); the
 * look, positions and states of every piece stay in gui_layouts/&lt;layoutId&gt;.json.
 */
public abstract class LayoutScreen extends Screen {
    protected final String layoutId;
    protected final LayoutInteraction interaction = new LayoutInteraction();

    protected LayoutScreen(Component title, String layoutId) {
        super(title);
        this.layoutId = layoutId;
    }

    /** Values, texts and flags for this frame. */
    protected abstract LayoutContext context();

    /** A button with this action was clicked (and was enabled). */
    protected abstract void onAction(String action);

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics);
        interaction.begin(mouseX, mouseY);
        LayoutRenderer.render(graphics, font, layoutId, context(), width, height, interaction);
        interaction.end();
        super.render(graphics, mouseX, mouseY, partialTick);
        Component tooltip = interaction.tooltip();
        if (tooltip != null) graphics.renderTooltip(font, font.split(tooltip, 220), mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            LayoutInteraction.Hit hit = interaction.hitAt(mouseX, mouseY);
            if (hit != null && hit.enabled()) {
                interaction.press(hit.id());
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        String pressed = interaction.pressedId();
        if (button == 0 && pressed != null) {
            interaction.press(null);
            LayoutInteraction.Hit hit = interaction.hitAt(mouseX, mouseY);
            if (hit != null && hit.enabled() && hit.action() != null && pressed.equals(hit.id())) {
                minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                onAction(hit.action());
            }
            return true;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
