package com.bleachmod.common.technique;

/** Prototype values. Costs/cooldowns live exclusively in TechniqueDefinitions. */
public final class SenbonPolicy {
    public static final double CUT_RANGE=6, STREAM_RANGE=12, CONVERGENCE_RANGE=9, FIELD_RADIUS=10, REACH_BONUS=1.5;
    public static final int WARD_TICKS=60, BLOOM_WINDUP=16, HAKU_WINDUP=16, MAX_TARGETS=32, VISUAL_BUDGET=192;
    public static final float CUT_DAMAGE=7, BANKAI_CUT_DAMAGE=5, STREAM_DAMAGE=4, CONVERGENCE_DAMAGE=9,
            FIELD_OPEN_DAMAGE=6, FIELD_BLOOM_DAMAGE=14, IKKA_DAMAGE=18, HAKU_DAMAGE=20, WARD_REDUCTION=.45F, SENKEI_BONUS=.25F;
    private SenbonPolicy() { }
}
