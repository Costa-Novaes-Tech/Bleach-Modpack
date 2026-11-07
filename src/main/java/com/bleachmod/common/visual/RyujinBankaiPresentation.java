package com.bleachmod.common.visual;

import net.minecraft.world.phys.Vec3;

/** Shared pose-space anchors: anatomical right / up / forward. Presentation only. */
public final class RyujinBankaiPresentation {
    public record Blade(Vec3 hand, Vec3 tip) { }
    private RyujinBankaiPresentation() { }
    private static Vec3 world(Vec3 origin, Vec3 forward, Vec3 local) {
        Vec3 right = new Vec3(-forward.z, 0, forward.x);
        return origin.add(right.scale(local.x)).add(0, local.y, 0).add(forward.scale(local.z));
    }
    public static Blade blade(Vec3 origin, Vec3 forward, double age, float sign) {
        var pose = RyujinChoreography.bankaiEntrance((float) age, sign);
        Vec3 hand = world(origin, forward, RyujinChoreography.bladeHand(pose, sign));
        Vec3 direction = world(Vec3.ZERO, forward, RyujinChoreography.bladeDirection(pose));
        return new Blade(hand, hand.add(direction.scale(1.35)));
    }
    public static Vec3 sunCenter(Vec3 origin, Vec3 forward) {
        return origin.subtract(forward.scale(8)).add(0,6.0,0);
    }
    public static double heat(double age) {
        double preparation = .12 * RyujinBankaiTimeline.progress(age, 0, 12);
        double burst = .88 * RyujinBankaiTimeline.progress(age, 19, 22)
                * (1 - RyujinBankaiTimeline.progress(age, 26, 53));
        return preparation + burst;
    }
    /** Presentation-only ignition; early waist/blade fire is separate from the large burst. */
    public static double ignition(double age) {
        return RyujinBankaiTimeline.progress(age,6,12)
                * (1-RyujinBankaiTimeline.progress(age,32,52));
    }
    public static double actorShadow(double age) {
        return .72*RyujinBankaiTimeline.progress(age,8,12)
                * (1-RyujinBankaiTimeline.progress(age,32,54));
    }
    /** Foreground tongues stay below the player's waist and vary across time and space. */
    public static double frontFlameHeight(double age,int index) {
        return ignition(age)*(.60+.23*Math.sin(age*.43+index*1.71)
                +.16*Math.sin(age*.71-index*.93));
    }
}
