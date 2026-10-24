package com.bleachmod.common.visual;

import net.minecraft.util.Mth;

/**
 * Atmospheric lighting, progressive darkness curves, and subtitle timing
 * for Byakuya Kuchiki's Bankai: Senbonzakura Kageyoshi.
 */
public final class SenbonAtmosphere {
    private SenbonAtmosphere() { }

    /**
     * Atmospheric darkness curve:
     *   0..35:   Gradual entry of twilight/darkness (0.0 -> 1.0)
     *   35..228: Sustained dark spiritual atmosphere (1.0)
     *   228..266: Partial recovery during petal dispersion (1.0 -> 0.30)
     *   266..290: Complete return to normal appearance (0.30 -> 0.0)
     */
    public static double darkness(double age) {
        if (age < 0.0 || age >= 290.0) return 0.0;
        if (age < 35.0) {
            double t = age / 35.0;
            return t * t * (3.0 - 2.0 * t);
        } else if (age < 228.0) {
            return 1.0;
        } else if (age < 266.0) {
            double t = (age - 228.0) / 38.0;
            double smooth = t * t * (3.0 - 2.0 * t);
            return Mth.lerp(smooth, 1.0, 0.30);
        } else {
            double t = (age - 266.0) / 24.0;
            double smooth = t * t * (3.0 - 2.0 * t);
            return Mth.lerp(smooth, 0.30, 0.0);
        }
    }

    public record Subtitle(String translationKey, float alpha) { }

    /**
     * Subtitle timing aligned with measured speech audio:
     *   "Bankai." (cue tick 10, speech 0.04s..3.27s = ticks 11..75)
     *   "Senbonzakura Kageyoshi." (cue tick 160, speech starts at 0.43s = tick 168, ends 3.27s = tick 226, holds to tick 236)
     */
    public static Subtitle subtitle(double age) {
        if (age >= 10.0 && age <= 78.0) {
            float alpha;
            if (age < 14.0) {
                alpha = (float) ((age - 10.0) / 4.0);
            } else if (age > 74.0) {
                alpha = (float) ((78.0 - age) / 4.0);
            } else {
                alpha = 1.0F;
            }
            return new Subtitle("cinematic.bleachmod.senbonzakura.bankai", Mth.clamp(alpha, 0.0F, 1.0F));
        }

        if (age >= 165.0 && age <= 244.0) {
            float alpha;
            if (age < 171.0) {
                alpha = (float) ((age - 165.0) / 6.0);
            } else if (age > 236.0) {
                alpha = (float) ((244.0 - age) / 8.0);
            } else {
                alpha = 1.0F;
            }
            return new Subtitle("cinematic.bleachmod.senbonzakura.name", Mth.clamp(alpha, 0.0F, 1.0F));
        }

        return null;
    }

    /**
     * Screen-space radial cutout mask preserving the player's silhouette against the dark penumbra.
     */
    public static double ellipseMask(double x, double y, double cx, double cy, double rx, double ry) {
        double dx = (x - cx) / Math.max(1.0, rx);
        double dy = (y - cy) / Math.max(1.0, ry);
        double r = Math.sqrt(dx * dx + dy * dy);
        double t = Mth.clamp((r - 0.75) / 0.85, 0.0, 1.0);
        return t * t * (3.0 - 2.0 * t);
    }
}
