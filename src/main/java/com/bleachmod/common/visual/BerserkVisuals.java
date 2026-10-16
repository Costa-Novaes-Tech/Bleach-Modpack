package com.bleachmod.common.visual;

import net.minecraft.core.particles.DustColorTransitionOptions;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

/** Zaraki's crimson reiatsu: blood-red dust, embers fading to black, heavy smoke. Visual only. */
public final class BerserkVisuals {
    public static final ParticleOptions BLOOD = new DustParticleOptions(new Vector3f(.9F, .04F, .04F), 1.6F);
    public static final ParticleOptions BLOOD_LARGE = new DustParticleOptions(new Vector3f(1F, .08F, .05F), 3.2F);
    public static final ParticleOptions EMBER = new DustColorTransitionOptions(new Vector3f(1F, .2F, .05F), new Vector3f(.12F, 0, 0), 1.4F);
    public static final ParticleOptions EYE = new DustParticleOptions(new Vector3f(1F, .86F, .1F), 1.2F);
    /** 11x9 skull silhouette for the release pillar; '#' emits, '.' is empty. Row 0 is the top. */
    private static final String[] SKULL = {
            "...#####...", ".#########.", "###########", "##..###..##", "##..###..##",
            "#####.#####", ".####.####.", "..#.#.#.#..", "..#######.."};
    private BerserkVisuals() { }
    private static Vec3 right(Vec3 forward) { return new Vec3(-forward.z, 0, forward.x); }

    /** Idle aura: rises from the shoulders; denser and larger in Berserk Mode. */
    public static int aura(ServerPlayer owner, int stage) {
        int budget = stage >= 3 ? 14 : stage == 2 ? 8 : 5;
        VisualFrame frame = VisualFrame.nearby(owner, budget);
        Vec3 c = owner.position().add(0, 1.1, 0);
        frame.emit(BLOOD, c, budget - 2, stage >= 3 ? .7 : .45, .9, stage >= 3 ? .7 : .45, .02);
        frame.emit(stage >= 3 ? ParticleTypes.LARGE_SMOKE : ParticleTypes.SMOKE, c.add(0, .9, 0), 2, .3, .4, .3, .01);
        return frame.requested();
    }

    /** Vertical crescent swept in front of the owner; width scales with the form. */
    public static int crescent(ServerPlayer owner, Vec3 forward, double reach, double height, boolean heavy) {
        int samples = (int) Math.ceil(height * 4), per = heavy ? 6 : 3;
        VisualFrame frame = VisualFrame.technique(owner, owner.position(), samples * (per + 1) * 2);
        Vec3 base = owner.position().add(forward.scale(reach * .5));
        for (int i = 0; i <= samples; i++) {
            double t = i / (double) samples, arc = Math.sin(Math.PI * t);
            Vec3 point = base.add(forward.scale(arc * reach * .5)).add(0, .2 + height * t, 0);
            frame.emit(heavy ? BLOOD_LARGE : BLOOD, point, per, .15, .05, .15, .02);
            frame.emit(EMBER, point.add(forward.scale(-.3)), 1, .1, .1, .1, .01);
        }
        return frame.requested();
    }

    /** Horizontal fan cut used by the sealed strikes and Fury combo. */
    public static int sweep(ServerPlayer owner, Vec3 forward, double range, double halfAngle) {
        int samples = (int) (range * 3);
        VisualFrame frame = VisualFrame.technique(owner, owner.position(), samples * 6);
        Vec3 right = right(forward);
        for (int i = 0; i < samples; i++) {
            double t = i / (double) (samples - 1), angle = -halfAngle + 2 * halfAngle * t;
            Vec3 dir = forward.scale(Math.cos(angle)).add(right.scale(Math.sin(angle)));
            Vec3 point = owner.position().add(dir.scale(range * .8)).add(0, 1.1 - .4 * t, 0);
            frame.emit(BLOOD, point, 4, .08, .08, .08, .01);
            frame.emit(EMBER, point, 2, .1, .1, .1, .02);
        }
        return frame.requested();
    }

    /** Fissure along the ground: Nozarashi's cut splits the earth visually without breaking blocks. */
    public static int fissure(ServerPlayer owner, Vec3 origin, Vec3 forward, double length, boolean bankai) {
        int samples = (int) (length * 2), per = bankai ? 5 : 3;
        VisualFrame frame = VisualFrame.technique(owner, origin, samples * (per + 2));
        for (int i = 1; i <= samples; i++) {
            Vec3 point = origin.add(forward.scale(i * .5)).add(0, .15, 0);
            frame.emit(BLOOD, point, per, .12, .05, .12, .02);
            frame.emit(i % 2 == 0 ? ParticleTypes.LAVA : ParticleTypes.LARGE_SMOKE, point, 1, .2, .1, .2, .02);
            frame.emit(EMBER, point.add(0, .6, 0), 1, .1, .5, .1, .03);
        }
        return frame.requested();
    }

    /** Expanding ground ring for slams, roars and the release shockwave. */
    public static int ring(ServerPlayer owner, Vec3 center, double radius, int samples, boolean smoke) {
        VisualFrame frame = VisualFrame.technique(owner, center, samples * (smoke ? 4 : 3));
        for (int i = 0; i < samples; i++) {
            double angle = i * Math.PI * 2 / samples;
            Vec3 point = center.add(Math.cos(angle) * radius, .2, Math.sin(angle) * radius);
            frame.emit(BLOOD_LARGE, point, 2, .1, .05, .1, .01);
            frame.emit(EMBER, point, 1, .1, .2, .1, .04);
            if (smoke) frame.emit(ParticleTypes.CAMPFIRE_COSY_SMOKE, point, 1, .1, .1, .1, .01);
        }
        return frame.requested();
    }

    /** Release pillar crowned by a skull of reiatsu, faced toward the owner's look direction. */
    public static int pillar(ServerPlayer owner, Vec3 forward, double height) {
        VisualFrame frame = VisualFrame.technique(owner, owner.position(), 900);
        Vec3 base = owner.position();
        for (int i = 0; i < (int) (height * 3); i++) {
            double y = i / 3.0, angle = i * .9;
            Vec3 point = base.add(Math.cos(angle) * .9, y, Math.sin(angle) * .9);
            frame.emit(BLOOD_LARGE, point, 3, .2, .15, .2, .02);
            frame.emit(EMBER, point, 2, .3, .2, .3, .05);
        }
        Vec3 right = right(forward); double cell = .7;
        Vec3 skullTop = base.add(0, height + SKULL.length * cell, 0).add(forward.scale(-1.5));
        for (int row = 0; row < SKULL.length; row++) for (int col = 0; col < SKULL[row].length(); col++) {
            if (SKULL[row].charAt(col) != '#') continue;
            Vec3 point = skullTop.add(right.scale((col - 5) * cell)).add(0, -row * cell, 0);
            frame.emit(BLOOD_LARGE, point, 3, .15, .15, .15, .005);
        }
        return frame.requested();
    }

    /** Eye glints left behind during a charge. */
    public static int trail(ServerPlayer owner) {
        VisualFrame frame = VisualFrame.nearby(owner, 12);
        frame.emit(BLOOD_LARGE, owner.position().add(0, 1, 0), 8, .3, .5, .3, .02);
        frame.emit(ParticleTypes.LARGE_SMOKE, owner.position().add(0, .4, 0), 4, .3, .2, .3, .01);
        return frame.requested();
    }
}
