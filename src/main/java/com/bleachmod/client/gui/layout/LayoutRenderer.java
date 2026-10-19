package com.bleachmod.client.gui.layout;

import com.bleachmod.client.gui.EntityPreview;
import com.bleachmod.client.gui.layout.GuiLayout.*;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

/**
 * Draws a {@link GuiLayout}. Everything happens inside one pose transform, so element
 * coordinates are plain art pixels and every piece stays locked together at any GUI
 * scale. With a {@link LayoutInteraction}, buttons react to the mouse and their screen
 * regions are recorded for clicks.
 */
public final class LayoutRenderer {
    private static final int DEBUG_LAYOUT = 0xFFFF00FF;
    private static final int DEBUG_ELEMENT = 0xFF00FFFF;
    private static final int ERROR_COLOR = 0xFFFF5555;

    private LayoutRenderer() {
    }

    /** Draws layout {@code id}; returns where it landed, or null if it is unavailable. */
    public static LayoutGeometry.Placement render(GuiGraphics g, Font font, String id, LayoutContext context,
                                                  int screenWidth, int screenHeight) {
        return render(g, font, id, context, screenWidth, screenHeight, null);
    }

    public static LayoutGeometry.Placement render(GuiGraphics g, Font font, String id, LayoutContext context,
                                                  int screenWidth, int screenHeight, LayoutInteraction interaction) {
        GuiLayout layout = GuiLayouts.get(id);
        String error = GuiLayouts.error(id);
        if (layout == null) {
            drawError(g, font, id, error != null ? error : "layout nao encontrado (gui_layouts/" + id + ".json)", 4);
            return null;
        }
        LayoutGeometry.Placement placement = render(g, font, layout, context, screenWidth, screenHeight, interaction);
        if (error != null) drawError(g, font, id, error, 4);
        return placement;
    }

    public static LayoutGeometry.Placement render(GuiGraphics g, Font font, GuiLayout layout, LayoutContext context,
                                                  int screenWidth, int screenHeight, LayoutInteraction interaction) {
        LayoutGeometry.Placement placement = LayoutGeometry.place(layout, screenWidth, screenHeight);
        Frame frame = new Frame(g, font, layout, context, placement, interaction);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        g.pose().pushPose();
        g.pose().translate(placement.x(), placement.y(), 0);
        g.pose().scale(placement.scale(), placement.scale(), 1F);
        for (Element element : layout.elements()) {
            if (!context.test(element.common().visibleIf())) continue;
            int[] bounds;
            if (element instanceof Image image) bounds = frame.image(image);
            else if (element instanceof Bar bar) bounds = frame.bar(bar);
            else if (element instanceof Text text) bounds = frame.text(text);
            else if (element instanceof Button button) bounds = frame.button(button);
            else if (element instanceof Entity entity) bounds = frame.entity(entity);
            else bounds = frame.rect((Rect) element);
            if (layout.debug() && bounds != null) debugBox(g, font, element.common().id(), bounds, layout.density());
        }
        if (layout.debug()) outline(g, 0, 0, layout.width(), layout.height(), DEBUG_LAYOUT);
        g.pose().popPose();
        return placement;
    }

    /** One render pass; each element method returns its bounds {x, y, w, h} in art pixels. */
    private record Frame(GuiGraphics g, Font font, GuiLayout layout, LayoutContext context,
                         LayoutGeometry.Placement placement, LayoutInteraction interaction) {

        int[] image(Image image) {
            GuiTextures.Info tex = GuiTextures.get(image.texture());
            int[] uv = image.uv();
            if (uv == null) {
                int cells = image.states().count();
                int cellW = tex.width() / cells;
                int index = cellIndex(image.states(), context, null);
                uv = new int[]{index * cellW, 0, cellW, tex.height()};
            }
            int w = image.w() > 0 ? image.w() : uv[2];
            int h = image.h() > 0 ? image.h() : uv[3];
            int x = image.common().x();
            int y = image.common().y();
            if (image.slideValue() != null) y += Math.round(context.value(image.slideValue()) * image.slideRange());
            tint(image.tint());
            if (image.slice() == null) {
                g.blit(tex.location(), x, y, w, h, uv[0], uv[1], uv[2], uv[3], tex.width(), tex.height());
            } else {
                nineSlice(g, tex, x, y, w, h, uv, image.slice());
            }
            tint(0xFFFFFFFF);
            return new int[]{x, y, w, h};
        }

        int[] bar(Bar bar) {
            int x = bar.common().x(), y = bar.common().y();
            float ratio = context.value(bar.value());
            int color = context.color(bar.color(), bar.colorIf());
            GuiTextures.Info tex = bar.texture() != null ? GuiTextures.get(bar.texture()) : null;
            boolean vertical = bar.direction() == Direction.UP || bar.direction() == Direction.DOWN;
            int filledRows = Math.round(bar.h() * ratio);

            for (int row = 0; row < bar.h(); row++) {
                int[] span = LayoutGeometry.barRow(bar.w(), bar.h(), bar.skew(), bar.skewEnd(), row);
                int left = span[0], right = span[1];
                if (right <= left) continue;
                if ((bar.background() >>> 24) != 0) g.fill(x + left, y + row, x + right, y + row + 1, bar.background());

                int from = left, to = right;
                if (vertical) {
                    boolean filled = bar.direction() == Direction.UP ? row >= bar.h() - filledRows : row < filledRows;
                    if (!filled) continue;
                } else {
                    int cut = Math.round((right - left) * ratio);
                    if (cut <= 0) continue;
                    if (bar.direction() == Direction.RIGHT) to = left + cut;
                    else from = right - cut;
                }
                if (tex == null) {
                    g.fill(x + from, y + row, x + to, y + row + 1, color);
                } else {
                    // The fill texture covers the whole bar box; reveal it without stretching.
                    float u = from * tex.width() / (float) bar.w();
                    float v = row * tex.height() / (float) bar.h();
                    int uw = Math.max(1, Math.round((to - from) * tex.width() / (float) bar.w()));
                    int vh = Math.max(1, Math.round(tex.height() / (float) bar.h()));
                    tint(color);
                    g.blit(tex.location(), x + from, y + row, to - from, 1, u, v, uw, vh, tex.width(), tex.height());
                    tint(0xFFFFFFFF);
                }
            }
            return new int[]{x, y, bar.w(), bar.h()};
        }

        int[] text(Text text) {
            Label label = text.label();
            Component component = context.component(label);
            float scale = label.scale() * layout.density();
            int color = context.color(label.color(), label.colorIf());
            if (text.wrap() && text.maxWidth() > 0) {
                List<FormattedCharSequence> lines = font.split(component, Math.max(1, Math.round(text.maxWidth() / scale)));
                if (text.maxLines() > 0 && lines.size() > text.maxLines()) lines = lines.subList(0, text.maxLines());
                float step = text.lineHeight() > 0 ? text.lineHeight() : font.lineHeight * scale;
                int widest = 0;
                for (int i = 0; i < lines.size(); i++) {
                    widest = Math.max(widest, font.width(lines.get(i)));
                    float lineX = alignX(text.common().x(), font.width(lines.get(i)) * scale, text.align());
                    drawScaled(g, font, lines.get(i), lineX, text.common().y() + i * step, scale, color, label.shadow());
                }
                return new int[]{text.common().x(), text.common().y(), Math.round(widest * scale),
                        Math.max(1, Math.round(lines.size() * step))};
            }
            float spacing = label.letterSpacing();
            float width = spacedWidth(font, component, scale, spacing);
            if (text.maxWidth() > 0 && width > text.maxWidth()) {
                float shrink = text.maxWidth() / width;
                scale *= shrink;
                spacing *= shrink;
            }
            if (spacing != 0) return drawSpaced(component, text.common().x(), text.common().y(), text.align(),
                    text.valign(), scale, spacing, color, label.shadow());
            return drawAligned(component, text.common().x(), text.common().y(), text.align(), text.valign(),
                    scale, color, label.shadow());
        }

        int[] rect(Rect rect) {
            int x = rect.common().x(), y = rect.common().y();
            g.fill(x, y, x + rect.w(), y + rect.h(), context.color(rect.color(), rect.colorIf()));
            return new int[]{x, y, rect.w(), rect.h()};
        }

        int[] button(Button button) {
            GuiTextures.Info tex = GuiTextures.get(button.texture());
            int cells = button.states().count();
            int cellW = tex.width() / cells;
            int x = button.common().x(), y = button.common().y();
            int w = button.w() > 0 ? button.w() : cellW;
            int h = button.h() > 0 ? button.h() : tex.height();
            boolean enabled = context.test(button.enabledIf());
            float sx0 = placement.toScreenX(x), sy0 = placement.toScreenY(y);
            float sx1 = placement.toScreenX(x + w), sy1 = placement.toScreenY(y + h);
            boolean hover = interaction != null && interaction.hovering(sx0, sy0, sx1, sy1);
            boolean pressed = hover && interaction.pressedId() != null && interaction.pressedId().equals(button.common().id());
            int index = cellIndex(button.states(), context, new boolean[]{enabled, pressed, hover});
            g.blit(tex.location(), x, y, w, h, index * cellW, 0, cellW, tex.height(), tex.width(), tex.height());

            if (button.label() != null) {
                Component component = context.component(button.label());
                float scale = button.label().scale() * layout.density();
                int color = enabled ? context.color(button.label().color(), button.label().colorIf()) : button.disabledColor();
                int lx = button.labelX() == Integer.MIN_VALUE ? x + w / 2 : x + button.labelX();
                int ly = button.labelY() == Integer.MIN_VALUE ? y + h / 2 : y + button.labelY();
                Align align = button.labelX() == Integer.MIN_VALUE ? Align.CENTER : Align.LEFT;
                int maxWidth = button.labelX() == Integer.MIN_VALUE ? w - 8 : w - button.labelX() - 6;
                if (font.width(component) * scale > maxWidth) scale = maxWidth / (float) Math.max(1, font.width(component));
                drawAligned(component, lx, ly + (pressed ? 1 : 0), align, VAlign.MIDDLE, scale, color, button.label().shadow());
            }
            if (interaction != null) {
                Object tooltip = button.tooltip() == null ? null : context.resolve(button.tooltip());
                Component tip = tooltip == null ? null : tooltip instanceof Component c ? c : Component.literal(tooltip.toString());
                if (tip != null && tip.getString().isEmpty()) tip = null;
                interaction.record(new LayoutInteraction.Hit(button.common().id(), button.action(),
                        sx0, sy0, sx1, sy1, enabled, tip));
            }
            return new int[]{x, y, w, h};
        }

        int[] entity(Entity entity) {
            LivingEntity living = context.entity(entity.source());
            int x = entity.common().x(), y = entity.common().y();
            if (living != null) {
                EntityPreview.render(g, x + entity.w() / 2, y + entity.h(), entity.scale(),
                        entity.yawValue() == null ? 0F : context.number(entity.yawValue()), living);
            }
            if (interaction != null) {
                interaction.record(new LayoutInteraction.Hit(entity.common().id(), null,
                        placement.toScreenX(x), placement.toScreenY(y),
                        placement.toScreenX(x + entity.w()), placement.toScreenY(y + entity.h()), true, null));
            }
            return new int[]{x, y, entity.w(), entity.h()};
        }

        /** Draws one character at a time so the gap between letters can change. */
        private int[] drawSpaced(Component component, float x, float y, Align align, VAlign valign,
                                 float scale, float spacing, int color, boolean shadow) {
            float w = spacedWidth(font, component, scale, spacing);
            float h = (font.lineHeight - 1) * scale;
            x = alignX(x, w, align);
            if (valign == VAlign.MIDDLE) y -= h / 2F;
            else if (valign == VAlign.BOTTOM) y -= h;
            net.minecraft.network.chat.Style style = component.getStyle();
            String value = component.getString();
            float cursor = x;
            for (int i = 0; i < value.length(); ) {
                int codePoint = value.codePointAt(i);
                Component glyph = Component.literal(new String(Character.toChars(codePoint))).withStyle(style);
                drawScaled(g, font, glyph.getVisualOrderText(), cursor, y, scale, color, shadow);
                cursor += font.width(glyph) * scale + spacing;
                i += Character.charCount(codePoint);
            }
            return new int[]{Math.round(x), Math.round(y), Math.max(1, Math.round(w)), Math.max(1, Math.round(h))};
        }

        private int[] drawAligned(Component component, float x, float y, Align align, VAlign valign,
                                  float scale, int color, boolean shadow) {
            float w = font.width(component) * scale;
            float h = (font.lineHeight - 1) * scale;
            x = alignX(x, w, align);
            if (valign == VAlign.MIDDLE) y -= h / 2F;
            else if (valign == VAlign.BOTTOM) y -= h;
            drawScaled(g, font, component.getVisualOrderText(), x, y, scale, color, shadow);
            return new int[]{Math.round(x), Math.round(y), Math.max(1, Math.round(w)), Math.max(1, Math.round(h))};
        }
    }

    /**
     * Which cell of a multi-state texture to draw. {@code mouse} is {enabled, pressed, hover}
     * for buttons and null for plain images.
     */
    static int cellIndex(States states, LayoutContext context, boolean[] mouse) {
        List<String> names = states.names();
        if (names.isEmpty()) return 0;
        if (states.cell() >= 0) return Math.min(states.cell(), names.size() - 1);
        if (mouse != null) {
            if (!mouse[0]) {
                int disabled = names.indexOf("disabled");
                if (disabled < 0) disabled = names.indexOf("locked");
                return Math.max(0, disabled);
            }
            if (mouse[1] && names.contains("pressed")) return names.indexOf("pressed");
        }
        for (var entry : states.stateIf().entrySet())
            if (context.test(entry.getValue())) return names.indexOf(entry.getKey());
        if (mouse != null && mouse[2] && names.contains("hover")) return names.indexOf("hover");
        return Math.max(0, names.indexOf("normal"));
    }

    private static float spacedWidth(Font font, Component component, float scale, float spacing) {
        int count = component.getString().codePointCount(0, component.getString().length());
        return font.width(component) * scale + spacing * Math.max(0, count - 1);
    }

    private static float alignX(float x, float width, Align align) {
        if (align == Align.CENTER) return x - width / 2F;
        if (align == Align.RIGHT) return x - width;
        return x;
    }

    private static void drawScaled(GuiGraphics g, Font font, FormattedCharSequence text, float x, float y,
                                   float scale, int color, boolean shadow) {
        g.pose().pushPose();
        g.pose().translate(x, y, 0);
        g.pose().scale(scale, scale, 1F);
        g.drawString(font, text, 0, 0, color, shadow);
        g.pose().popPose();
    }

    private static void tint(int color) {
        RenderSystem.setShaderColor(((color >> 16) & 255) / 255F, ((color >> 8) & 255) / 255F,
                (color & 255) / 255F, (color >>> 24) / 255F);
    }

    /** Corners stay native, edges and center stretch. slice = {left, top, right, bottom}. */
    private static void nineSlice(GuiGraphics g, GuiTextures.Info tex, int x, int y, int w, int h, int[] uv, int[] s) {
        int[] srcX = {uv[0], uv[0] + s[0], uv[0] + uv[2] - s[2]};
        int[] srcW = {s[0], uv[2] - s[0] - s[2], s[2]};
        int[] srcY = {uv[1], uv[1] + s[1], uv[1] + uv[3] - s[3]};
        int[] srcH = {s[1], uv[3] - s[1] - s[3], s[3]};
        int[] dstX = {x, x + s[0], x + w - s[2]};
        int[] dstW = {s[0], w - s[0] - s[2], s[2]};
        int[] dstY = {y, y + s[1], y + h - s[3]};
        int[] dstH = {s[1], h - s[1] - s[3], s[3]};
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (dstW[col] <= 0 || dstH[row] <= 0 || srcW[col] <= 0 || srcH[row] <= 0) continue;
                g.blit(tex.location(), dstX[col], dstY[row], dstW[col], dstH[row],
                        srcX[col], srcY[row], srcW[col], srcH[row], tex.width(), tex.height());
            }
        }
    }

    // ---- debug helpers

    private static void debugBox(GuiGraphics g, Font font, String id, int[] b, float density) {
        outline(g, b[0], b[1], b[2], b[3], DEBUG_ELEMENT);
        g.pose().pushPose();
        g.pose().translate(b[0], b[1] - 4 * density, 0);
        g.pose().scale(density * .5F, density * .5F, 1F);
        g.drawString(font, id + " " + b[0] + "," + b[1], 0, 0, DEBUG_ELEMENT, false);
        g.pose().popPose();
    }

    private static void outline(GuiGraphics g, int x, int y, int w, int h, int color) {
        g.fill(x, y, x + w, y + 1, color);
        g.fill(x, y + h - 1, x + w, y + h, color);
        g.fill(x, y, x + 1, y + h, color);
        g.fill(x + w - 1, y, x + w, y + h, color);
    }

    private static void drawError(GuiGraphics g, Font font, String id, String message, int y) {
        String line = "[gui_layouts/" + id + ".json] " + message;
        int lineY = y;
        for (var part : font.split(Component.literal(line), 360)) {
            g.fill(2, lineY - 1, 6 + font.width(part), lineY + font.lineHeight, 0xC0000000);
            g.drawString(font, part, 4, lineY, ERROR_COLOR, false);
            lineY += font.lineHeight + 1;
        }
    }
}
