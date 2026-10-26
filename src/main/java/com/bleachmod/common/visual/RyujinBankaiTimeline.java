package com.bleachmod.common.visual;

import net.minecraft.util.Mth;

/**
 * Authored presentation timeline for Ryūjin Jakka / Zanka no Tachi Bankai transformation.
 * Real presentation: 800 ticks (40 seconds), compact client option 360 ticks (18 seconds).
 * Curves below use a separate 0..140 authored beat clock.
 *
 * Preserves the existing 60/40 gameplay contract (release 60, commit 40).
 * Presentation is a separate visual/camera layer.
 */
public final class RyujinBankaiTimeline {
    public static final int DURATION = 800;
    public static final int COMPACT_DURATION = 360;
    public static final int AUTHORED_END = 140;
    private static final double[] REAL_BEATS={0,100,280,400,580,680,740,800};
    private static final double[] STORY_BEATS={0,4,12,30,52,60,112,140};

    /** Real scene ticks -> authored beats. Linear intervals preserve continuous motion;
     * the individual pose/camera curves apply their own easing. Never used for gameplay. */
    public static double storyAge(double elapsed, int duration) {
        if (!Double.isFinite(elapsed)) return 0;
        double t = Math.max(0, Math.min(1, elapsed / duration)) * 800;
        double[] real=REAL_BEATS, beat=STORY_BEATS;
        for (int i=1;i<real.length;i++) if(t<=real[i])
            return beat[i-1]+(beat[i]-beat[i-1])*(t-real[i-1])/(real[i]-real[i-1]);
        return AUTHORED_END;
    }
    public static double realAge(double beat, int duration) {
        double[] real=REAL_BEATS, beats=STORY_BEATS;
        for(int i=1;i<beats.length;i++) if(beat<=beats[i])
            return (real[i-1]+(real[i]-real[i-1])*(beat-beats[i-1])/(beats[i]-beats[i-1]))*duration/800.0;
        return duration;
    }
    public static final int CUE_BANKAI = 12;
    public static final int CUE_NAME = 60;
    public static final int ABSORPTION_START = 30;
    public static final int ABSORPTION_END = 52;
    public static final int RETURN_START = 124;

    /** Speech windows are real ticks, so slow scenic beats cannot stretch subtitles. */
    public static float speechAlpha(double elapsed,int duration,int cue,int length){
        double since=elapsed-realAge(cue,duration);
        return (float)(smooth(since/4)*(1-smooth((since-length+6)/6)));
    }

    public enum Phase {
        GROWTH(0),
        SUSTAINED_SUN(12),
        ABSORPTION(30),
        CARBONIZATION_HOLD(52),
        NAME_PRONUNCIATION(60),
        RETURN(124);

        private final int start;
        Phase(int start) { this.start = start; }
        public int start() { return start; }
    }

    private RyujinBankaiTimeline() { }

    public static double clamp(double ticks) {
        return Double.isFinite(ticks) ? Math.max(0, Math.min(AUTHORED_END, ticks)) : 0;
    }

    public static Phase phase(double ticks) {
        Phase result = Phase.GROWTH;
        for (Phase p : Phase.values()) {
            if (clamp(ticks) >= p.start) result = p;
        }
        return result;
    }

    /** Smooth cubic Hermite interpolation (smoothstep). */
    public static double smooth(double t) {
        t = Math.max(0, Math.min(1, t));
        return t * t * (3 - 2 * t);
    }

    public static double progress(double ticks, double from, double to) {
        if (to <= from) return 1.0;
        double t = Math.max(0, Math.min(1, (clamp(ticks) - from) / (to - from)));
        return smooth(t);
    }

    /** Scenic sun scale radius: ~9 blocks maximum (18 blocks diameter). */
    public static double sunRadius(double ticks) {
        double age = clamp(ticks);
        if (age < 12) {
            return 1.5 + 7.5 * progress(age, 0, 12);
        } else if (age < 30) {
            double pulse = Math.sin(age * 0.4) * 0.15;
            return 9.0 + pulse;
        } else if (age < 52) {
            double p = progress(age, 30, 52);
            return 9.0 * (1.0 - p);
        }
        return 0.0;
    }

    /** 0.0 to 1.0 intensity of flame absorption into the sword blade. */
    public static double absorption(double ticks) {
        double age = clamp(ticks);
        if (age < 30 || age > 52) return 0.0;
        return progress(age, 30, 48) * (1.0 - progress(age, 50, 52));
    }

    /** 0.0 to 1.0 carbonization level of the katana blade. */
    public static float carbonization(double ticks) {
        double age = (double) clamp(ticks);
        if (age < 46) return 0.0F;
        if (age < 54) return (float) progress(age, 46, 54);
        if (age < 124) return 1.0F;
        return (float) (1.0 - progress(age, 124, 140));
    }

    /** Player pose layer weight (0.0 to 1.0). */
    public static float poseWeight(double ticks) {
        double age = (double) clamp(ticks);
        if (age < 8) return (float) progress(age, 0, 8);
        if (age < 120) return 1.0F;
        return (float) (1.0 - progress(age, 120, 140));
    }

    /** Subtitle alpha for on-screen cinematic text. */
    public static float bankaiSubtitleAlpha(double ticks) {
        double age = clamp(ticks);
        if (age < 14 || age > 42) return 0.0F;
        if (age < 18) return (float) progress(age, 14, 18);
        if (age < 35) return 1.0F;
        return (float) (1.0 - progress(age, 35, 42));
    }

    public static float nameSubtitleAlpha(double ticks) {
        double age = clamp(ticks);
        if (age < 64 || age > 106) return 0.0F;
        if (age < 68) return (float) progress(age, 64, 68);
        if (age < 102) return 1.0F;
        return (float) (1.0 - progress(age, 102, 106));
    }
}
