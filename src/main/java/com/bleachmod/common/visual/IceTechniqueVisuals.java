package com.bleachmod.common.visual;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

/** Homologated ice slot visuals. These emitters never apply combat or terrain changes. */
public final class IceTechniqueVisuals {
    private IceTechniqueVisuals() { }
    public record Cut(int slot, boolean released, boolean bankai) {
        public Cut {
            if (slot < 1 || slot > 2 || bankai && !released) throw new IllegalArgumentException("Invalid ice cut");
        }
        public double range() { return !released ? 6 : slot == 1 ? (bankai ? 24 : 10) : (bankai ? 8 : 6); }
        public double halfAngle() { return Math.toRadians(slot == 1 ? 15 : 35); }
        public int samples() { return (int) (range() * 2); }
        public int snowCount() { return bankai ? 18 : released ? 12 : 8; }
        public int dustCount() { return released ? 4 : 2; }
        public int budget() { return samples() * (snowCount() + dustCount()); }
    }
    public static int cut(ServerPlayer owner, Cut cut, Vec3 forward) {
        VisualFrame frame = VisualFrame.technique(owner, owner.position(), cut.budget());
        var dust = new DustParticleOptions(new org.joml.Vector3f(.45F, .85F, 1), cut.bankai ? 1.6F : 1);
        for (int i = 1; i <= cut.samples(); i++) {
            double step = i * .5;
            Vec3 point = owner.position().add(forward.scale(step)).add(0, 1, 0);
            frame.emit(ParticleTypes.SNOWFLAKE, point, cut.snowCount(), cut.slot == 1 ? .3 : step * Math.tan(cut.halfAngle()), .6, .25, .04);
            frame.emit(dust, point, cut.dustCount(), .2, .4, .2, .01);
        }
        return frame.requested();
    }
    public static int fieldSamples(boolean barrier, boolean bankai) { return barrier ? 16 : bankai ? 64 : 32; }
    public static int fieldBudget(boolean barrier, boolean bankai) { return fieldSamples(barrier, bankai) * (bankai ? 14 : 8); }
    public static Vec3 fieldPoint(Vec3 origin, Vec3 forward, boolean barrier, boolean bankai, double radius, int index, long time) {
        int samples = fieldSamples(barrier, bankai);
        if (index < 0 || index >= samples || !Double.isFinite(radius) || radius < 0 || radius > 30)
            throw new IllegalArgumentException("Invalid ice field sample");
        double angle = index * 2.399963229728653 + time * .07;
        double distance = radius * Math.sqrt((index + .5) / samples);
        Vec3 point = barrier ? origin.add(-forward.z * (index / 2.5 - 3), (index % 4) * .8, forward.x * (index / 2.5 - 3))
                : origin.add(Math.cos(angle) * distance, .2, Math.sin(angle) * distance);
        return point.add(0, bankai ? 1 : .5, 0);
    }
    public static int field(ServerPlayer owner, Vec3 origin, Vec3 forward, boolean barrier, boolean bankai, double radius, long time) {
        VisualFrame frame = VisualFrame.technique(owner, origin, fieldBudget(barrier, bankai));
        for (int i = 0; i < fieldSamples(barrier, bankai); i++)
            frame.emit(ParticleTypes.SNOWFLAKE, fieldPoint(origin, forward, barrier, bankai, radius, i, time), bankai ? 14 : 8, .6, bankai ? 2 : .8, .6, .04);
        return frame.requested();
    }
    public static int duel(ServerPlayer owner, Vec3 target) {
        VisualFrame frame = VisualFrame.technique(owner, owner.position(), 45);
        frame.emit(ParticleTypes.SNOWFLAKE, target.add(0, 1, 0), 45, .5, .8, .5, .05);
        return frame.requested();
    }
    public static int dragon(ServerPlayer owner, Vec3 position) {
        VisualFrame frame = VisualFrame.technique(owner, position, 10);
        frame.emit(ParticleTypes.SNOWFLAKE, position, 10, .5, .4, .5, .03);
        return frame.requested();
    }
}
