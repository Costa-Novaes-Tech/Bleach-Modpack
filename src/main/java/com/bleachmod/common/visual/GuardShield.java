package com.bleachmod.common.visual;

/** Shared code-native shield silhouette. Horizontal runs avoid one quad per pixel. */
public final class GuardShield {
    private static final int[] HALF = {6,8,9,9,9,9,9,9,9,9,9,9,9,9,8,8,7,7,6,5,4,3,2,0};
    @FunctionalInterface public interface Rect { void draw(int x, int y, int width, int height, int argb); }
    private GuardShield() { }

    public static void draw(float ratio, boolean broken, Rect out) {
        ratio = Float.isFinite(ratio) ? Math.max(0, Math.min(1, ratio)) : 0;
        for (int y = 0; y < HALF.length; y++) {
            int half = HALF[y], start = -half, previous = color(start, y, ratio, broken);
            for (int x = start + 1; x <= half + 1; x++) {
                int current = x <= half ? color(x, y, ratio, broken) : 0;
                // Keep the two broken halves separate even when their colors match.
                if (current != previous || x == half + 1 || (broken && x == 0)) {
                    if (previous != 0) out.draw(start + (broken ? (start < 0 ? -3 : 3) : 0),
                            y + (broken && start >= 0 ? 2 : 0), x - start, 1, previous);
                    start = x; previous = current;
                }
            }
        }
        out.draw(-11, 28, 23, 5, 0xFF152330);
        out.draw(-10, 29, 21, 3, 0xFF314453);
        int filled = Math.round(21 * ratio);
        if (filled > 0) out.draw(-10, 29, filled, 3, broken ? 0xFFFF6B63 : ratio <= .25F ? 0xFFFFBB65 : 0xFF6DD7EF);
    }

    private static int color(int x, int y, float ratio, boolean broken) {
        if (broken && Math.abs(x) < 2) return 0;
        int half = HALF[y];
        boolean outer = Math.abs(x) == half || y == 0 || y == HALF.length - 1;
        if (outer) return broken ? 0xFFDC7B75 : x <= 0 ? 0xFFB7D8E9 : 0xFF5B7B94;
        boolean rim = Math.abs(x) >= half - 1 || y == 1;
        if (rim) return broken ? 0xFFFFC4AF : x <= 0 ? 0xFFF0FAFF : 0xFF8FAFC2;
        boolean filled = y >= Math.round(24 * (1 - ratio));
        int pane = !filled ? 0xE0233544 : broken ? 0xFF9A4345
                : ratio <= .25F ? (x < 0 ? 0xFFE9A75C : 0xFFAD653F)
                : x < 0 ? 0xFF439DB8 : 0xFF28627E;
        boolean crack = ratio < .75F && y >= 4 && y <= 19 && x == (y / 3) % 3 - 1;
        crack |= ratio < .4F && y >= 8 && y <= 15 && x == 11 - y;
        if (crack) return 0xFF101B27;
        // Small central crest: remains readable without hiding depleted fill.
        if (!broken && Math.abs(x) + Math.abs(y - 10) == 3) return filled ? 0xFFB6EDF4 : 0xFF4E6B7D;
        if (Math.abs(x) == half - 2 && y > 2 && y < 18) return 0xFF182B3C;
        return pane;
    }
}
