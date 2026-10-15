package com.bleachmod.common;

import net.minecraft.world.phys.Vec3;

/** Universal physical dodge, separate from learned movement techniques. */
public final class DodgePolicy {
    public static final float COST = 15;
    public static final int DURATION = 5, INTERVAL = 12;
    public static final double SPEED = .7;
    private DodgePolicy() { }

    public static boolean validInput(int forward, int left) {
        return Math.abs((long) forward) <= 1 && Math.abs((long) left) <= 1;
    }

    public static Vec3 direction(int forward, int left, float yaw) {
        if (!validInput(forward, left) || !Float.isFinite(yaw)) return Vec3.ZERO;
        if (forward == 0 && left == 0) forward = -1;
        double radians = Math.toRadians(yaw);
        return new Vec3(-Math.sin(radians) * forward + Math.cos(radians) * left, 0,
                Math.cos(radians) * forward + Math.sin(radians) * left).normalize();
    }
}
