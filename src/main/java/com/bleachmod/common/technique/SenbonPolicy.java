package com.bleachmod.common.technique;

/** Prototype values. Costs/cooldowns live exclusively in TechniqueDefinitions. */
public final class SenbonPolicy {
    public static final double CUT_RANGE=8, STREAM_RANGE=14, CONVERGENCE_RANGE=11, CONVERGENCE_RADIUS=5,
            FIELD_RADIUS=40, IKKA_RANGE=14, HAKU_RANGE=12, SCENE_RADIUS=7, WARD_RADIUS=3.5, REACH_BONUS=1.5;
    public static final int WARD_TICKS=60, BLOOM_WINDUP=16, HAKU_WINDUP=16, MAX_TARGETS=32, VISUAL_BUDGET=192;
    public static final int PRISON_TICKS=60, PRISON_PULSE=20, CONTROL_LEASE=8;
    public static final float CUT_DAMAGE=7, BANKAI_CUT_DAMAGE=5, STREAM_DAMAGE=4, CONVERGENCE_DAMAGE=3,
            FIELD_OPEN_DAMAGE=6, FIELD_BLOOM_DAMAGE=14, IKKA_DAMAGE=18, HAKU_DAMAGE=20, WARD_REDUCTION=.45F, SENKEI_BONUS=.25F;
    private SenbonPolicy() { }
}
