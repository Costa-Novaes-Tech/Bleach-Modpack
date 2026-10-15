package com.bleachmod.common;

import net.minecraft.world.phys.Vec3;

/** Initial playtest values; posture only determines guard break. */
public final class GuardPolicy {
    public static final float MAX = 100, RESUME = 10, REGEN = 1;
    public static final int REST = 20, BREAK_TICKS = 20, INPUT_LEASE = 12;
    public static final float ARMED_REDUCTION = .65F, UNARMED_REDUCTION = .35F;
    private GuardPolicy() { }
    public static float pressure(float damage) {
        return Float.isFinite(damage) && damage > 0 ? Math.max(8, Math.min(50, damage * 4)) : 0;
    }
    public static boolean inFront(float yaw, Vec3 relative) {
        if (!Float.isFinite(yaw) || !Double.isFinite(relative.x) || !Double.isFinite(relative.z)) return false;
        Vec3 horizontal = new Vec3(relative.x, 0, relative.z);
        return horizontal.lengthSqr() >= .000001
                && Vec3.directionFromRotation(0, yaw).dot(horizontal.normalize()) >= .5;
    }
}
