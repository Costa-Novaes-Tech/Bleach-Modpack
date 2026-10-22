package com.bleachmod.common.visual;

/** Shared server/client clock; the authored ten-second movement is sampled continuously at half speed. */
public final class HyorinmaruTimeline {
    public static final int SCALE=2, DURATION=400, COMMIT=240;
    public static final double BREATH_REACH=36;
    public static final int BANKAI_VOICE=24, NAME_VOICE=330;
    public static double motionAge(double elapsed){return elapsed/SCALE;}
    public static boolean cueDue(double previous,double age,int cue){return previous<cue && age>=cue && age-cue<=4;}
    private HyorinmaruTimeline() { }
}
