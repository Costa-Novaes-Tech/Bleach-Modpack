package com.bleachmod.common.visual;

/** Visual laboratory clock. Does not change the 60/40-tick gameplay release contract. */
public final class BenihimeEntranceTimeline {
    public static final int DURATION = 560;
    public enum Phase {
        KNEEL(0), CURTAIN_RISE(72), CLOSED(108), REACH(138), GRIP(166),
        PULL(176), FACE(236), CRAWL(260), GROUND_HOLD(340), RISE(376),
        THREADS(450), SETTLE(520);
        private final int start;
        Phase(int start) { this.start = start; }
        public int start() { return start; }
    }
    private BenihimeEntranceTimeline() { }
    public static double clamp(double ticks) {
        return Double.isFinite(ticks) ? Math.max(0, Math.min(DURATION, ticks)) : 0;
    }
    public static Phase phase(double ticks) {
        Phase result = Phase.KNEEL;
        for (Phase phase : Phase.values()) if (clamp(ticks) >= phase.start) result = phase;
        return result;
    }
    /** Cubic easing, continuous at planted-hand/key-pose boundaries. */
    public static double progress(double ticks, double from, double to) {
        double t = Math.max(0, Math.min(1, (clamp(ticks) - from) / (to - from)));
        return t * t * (3 - 2 * t);
    }
    public static double seconds(double ticks) { return clamp(ticks) / 20; }
}
