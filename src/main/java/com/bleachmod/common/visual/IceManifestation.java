package com.bleachmod.common.visual;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

/** Approved Hyorinmaru appearance; intentionally distinct from experimental bench profiles. */
public final class IceManifestation {
    private IceManifestation() { }
    public static int releaseSamples(boolean bankai) { return bankai ? 40 : 24; }
    public static int releaseBudget(boolean bankai) { return releaseSamples(bankai) * 4; }
    public static int auraBudget(boolean bankai) { return bankai ? 10 : 4; }
    public static Vec3 releasePoint(Vec3 origin, boolean bankai, int index) {
        int samples = releaseSamples(bankai);
        if (index < 0 || index >= samples) throw new IllegalArgumentException("Invalid release sample");
        double angle = index * Math.PI * 2 / samples, radius = bankai ? 3 : 1.5;
        return origin.add(Math.cos(angle) * radius, .8, Math.sin(angle) * radius);
    }
    public static int release(ServerPlayer owner, boolean bankai) {
        VisualFrame frame = VisualFrame.nearby(owner, releaseBudget(bankai));
        for (int i = 0; i < releaseSamples(bankai); i++)
            frame.emit(ParticleTypes.SNOWFLAKE, releasePoint(owner.position(), bankai, i), 4, .2, .8, .2, .08);
        return frame.requested();
    }
    public static int aura(ServerPlayer owner, boolean bankai) {
        VisualFrame frame = VisualFrame.nearby(owner, auraBudget(bankai));
        frame.emit(ParticleTypes.SNOWFLAKE, owner.position().add(0, 1.2, 0), auraBudget(bankai), bankai ? .9 : .4, .6, .4, .015);
        return frame.requested();
    }
}
