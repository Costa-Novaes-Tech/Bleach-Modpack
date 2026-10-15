package com.bleachmod.common.visual;

import net.minecraft.world.phys.Vec3;

/** Shared attack geometry. At yaw zero the anatomical left is +X. */
public final class SenbonGeometry {
    public static final int CUT_TICKS = 6, CUT_SPACING = 12;
    private SenbonGeometry() { }
    public static Vec3 forward(float yaw) { return Vec3.directionFromRotation(0, yaw).normalize(); }
    public static Vec3 left(Vec3 forward) { return new Vec3(forward.z, 0, -forward.x).normalize(); }
    public static Vec3 cut(Vec3 origin, Vec3 forward, int pattern, double progress, double radius) {
        double t = Math.max(0, Math.min(1, progress));
        if (pattern == 1 || pattern == 3) t = 1 - t;
        double angle = Math.toRadians(-70 + 140 * t), side = Math.sin(angle), up = 0;
        if (pattern == 2) { up = -side * .65; side *= .76; }
        if (pattern == 3) { up = side * .65; side *= .76; }
        if (pattern == 4) { up = side; side = 0; }
        return origin.add(forward.scale(Math.cos(angle) * radius)).add(left(forward).scale(side * radius)).add(0, up * radius, 0);
    }
    public static double flowerRadius(double radius, double angle) { return radius * (.8 + .2 * Math.cos(5 * angle)); }
    public static boolean inside(Vec3 center, Vec3 position, double radius, double height) {
        double dx = position.x-center.x, dz = position.z-center.z;
        return dx*dx+dz*dz <= radius*radius && Math.abs(position.y-center.y) <= height;
    }
}
