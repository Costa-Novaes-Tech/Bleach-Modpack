package com.bleachmod.client.gui.layout;

import com.bleachmod.client.gui.layout.GuiLayout.Anchor;

/** Pure placement math, kept free of rendering so the regression suite can check it. */
public final class LayoutGeometry {
    private LayoutGeometry() {
    }

    /**
     * Where the layout lands on a screen of the given GUI size.
     *
     * @param x     screen GUI coordinate of the layout's art pixel (0,0)
     * @param scale GUI units per art pixel
     */
    public record Placement(float x, float y, float scale) {
        public float width(GuiLayout layout) {
            return layout.width() * scale;
        }

        public float height(GuiLayout layout) {
            return layout.height() * scale;
        }

        /** GUI-space coordinate of an art-pixel position. */
        public float toScreenX(float artX) {
            return x + artX * scale;
        }

        public float toScreenY(float artY) {
            return y + artY * scale;
        }
    }

    public static Placement place(GuiLayout layout, int screenWidth, int screenHeight) {
        Anchor anchor = layout.anchor();
        Anchor pivot = layout.pivot();
        int offsetX = layout.offsetX();
        int offsetY = layout.offsetY();
        for (GuiLayout.Responsive r : layout.responsive()) {
            if (screenWidth <= r.maxScreenWidth()) {
                anchor = r.anchor();
                pivot = r.pivot();
                offsetX = r.offsetX();
                offsetY = r.offsetY();
                break;
            }
        }

        float scale = layout.scale() / layout.density();
        if (layout.maxWidthFraction() > 0 && layout.width() > 0) {
            // Shrink only: growing pixel art past its native size just blurs it.
            float limit = screenWidth * layout.maxWidthFraction() / layout.width();
            scale = Math.min(scale, limit);
        }
        if (layout.maxHeightFraction() > 0 && layout.height() > 0)
            scale = Math.min(scale, screenHeight * layout.maxHeightFraction() / layout.height());
        float w = layout.width() * scale;
        float h = layout.height() * scale;
        float x = screenWidth * anchor.fx - w * pivot.fx + offsetX;
        float y = screenHeight * anchor.fy - h * pivot.fy + offsetY;
        return new Placement(Math.round(x), Math.round(y), scale);
    }

    /**
     * Horizontal span [left, right) of row {@code row} of a slanted bar, relative to the
     * bar's x. See {@link GuiLayout.Bar} for the meaning of skew and skewEnd.
     */
    public static int[] barRow(int w, int h, int skew, int skewEnd, int row) {
        float t = h <= 1 ? 0F : row / (float) (h - 1);
        float left = skew >= 0 ? skew * (1 - t) : -skew * t;
        float rightInset = skewEnd >= 0 ? skewEnd * t : -skewEnd * (1 - t);
        return new int[]{Math.round(left), w - Math.round(rightInset)};
    }
}
