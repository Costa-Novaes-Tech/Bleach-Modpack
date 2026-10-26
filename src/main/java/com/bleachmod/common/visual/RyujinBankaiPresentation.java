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
        return origin.subtract(forward.scale(8)).add(0,8.5,0);
    }
    public static double heat(double age) {
        return RyujinBankaiTimeline.progress(age, 0, 10)
                * (1 - RyujinBankaiTimeline.progress(age, 40, 62));
    }
}
