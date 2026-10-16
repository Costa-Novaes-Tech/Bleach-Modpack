package com.bleachmod.common.visual;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.DustColorTransitionOptions;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

/**
 * Zaraki's crimson reiatsu (cycle 37 overdrive budget, cycle 38 density cut so the wielder can see):
 * blood-red dust, embers fading to black, smoke, explosions and debris. Particles only; real terrain
 * destruction lives in BerserkTerrain.
 */
public final class BerserkVisuals {
    public static final ParticleOptions BLOOD = new DustParticleOptions(new Vector3f(.9F, .04F, .04F), 1.3F);
    public static final ParticleOptions BLOOD_LARGE = new DustParticleOptions(new Vector3f(1F, .08F, .05F), 2.4F);
    public static final ParticleOptions BLOOD_DARK = new DustParticleOptions(new Vector3f(.35F, 0, 0), 2.0F);
    public static final ParticleOptions EMBER = new DustColorTransitionOptions(new Vector3f(1F, .25F, .05F), new Vector3f(.12F, 0, 0), 1.2F);
    /** 11x9 skull silhouette for the release pillar; '#' emits, '.' is empty. Row 0 is the top. */
    private static final String[] SKULL = {
            "...#####...", ".#########.", "###########", "##..###..##", "##..###..##",
            "#####.#####", ".####.####.", "..#.#.#.#..", "..#######.."};
    private BerserkVisuals() { }
    private static Vec3 right(Vec3 forward) { return new Vec3(-forward.z, 0, forward.x); }
    /** Cycle 38: the full overdrive blinded the player; every emitter now runs at this fraction of its count. */
    public static final float DENSITY = .4F;
    private static boolean dense(VisualFrame frame, ParticleOptions particle, Vec3 point, int count, double dx, double dy, double dz, double speed) {
        return frame.emit(particle, point, Math.max(1, Math.round(count * DENSITY)), dx, dy, dz, speed);
    }

    /** Fragments of whatever the point stands on (stone if it floats), thrown upward like rubble. */
    private static ParticleOptions debris(ServerPlayer owner, Vec3 point) {
        var state = owner.serverLevel().getBlockState(BlockPos.containing(point.x, point.y - .6, point.z));
        if (state.isAir() || !state.getFluidState().isEmpty()) state = Blocks.COBBLESTONE.defaultBlockState();
        return new BlockParticleOption(ParticleTypes.BLOCK, state);
    }

    /** Idle aura for the eyepatch and Shikai; Berserk Mode uses the client-side spiral instead. */
    public static int aura(ServerPlayer owner, int stage) {
        int budget = stage >= 3 ? 40 : stage == 2 ? 24 : 16;
        VisualFrame frame = VisualFrame.nearby(owner, budget);
        Vec3 c = owner.position().add(0, 1.1, 0);
        dense(frame, BLOOD, c, budget - 6, stage >= 3 ? .8 : .5, 1, stage >= 3 ? .8 : .5, .03);
        dense(frame, EMBER, c, 3, .4, .6, .4, .04);
        dense(frame, stage >= 3 ? ParticleTypes.LARGE_SMOKE : ParticleTypes.SMOKE, c.add(0, .9, 0), 3, .3, .4, .3, .01);
        return frame.requested();
    }

    /** Hammer blow landing on a target: crater ring, rubble and a burst of blood-red reiatsu. */
    public static int impact(ServerPlayer owner, Vec3 point, float strength) {
        VisualFrame frame = VisualFrame.overdrive(owner, point, 400);
        int n = (int) (20 * strength);
        dense(frame, BLOOD_LARGE, point.add(0, .4, 0), n, .5, .3, .5, .08);
        dense(frame, debris(owner, point), point.add(0, .2, 0), n, .7, .2, .7, .3);
        dense(frame, EMBER, point.add(0, .5, 0), n / 2, .6, .4, .6, .12);
        dense(frame, ParticleTypes.LARGE_SMOKE, point.add(0, .2, 0), Math.max(1, n / 4), .5, .1, .5, .03);
        if (strength >= 1.5F) dense(frame, ParticleTypes.EXPLOSION, point.add(0, .3, 0), 1, 0, 0, 0, 0);
        for (int i = 0; i < 16; i++) {
            double angle = i * Math.PI / 8;
            dense(frame, BLOOD, point.add(Math.cos(angle) * 1.4 * strength, .15, Math.sin(angle) * 1.4 * strength), 2, .1, .05, .1, .02);
        }
        return frame.requested();
    }

    /** Vertical crescent swept down in front of the owner, like the blade's path in an overhead smash. */
    public static int crescent(ServerPlayer owner, Vec3 forward, double reach, double height, boolean heavy) {
        int samples = (int) Math.ceil(height * 6), per = heavy ? 14 : 8;
        VisualFrame frame = VisualFrame.overdrive(owner, owner.position(), samples * (per + 8) + 40);
        Vec3 base = owner.position().add(forward.scale(reach * .5));
        for (int i = 0; i <= samples; i++) {
            double t = i / (double) samples, arc = Math.sin(Math.PI * t);
            Vec3 point = base.add(forward.scale(arc * reach * .5)).add(0, .2 + height * t, 0);
            dense(frame, heavy ? BLOOD_LARGE : BLOOD, point, per, .2, .08, .2, .03);
            dense(frame, EMBER, point.add(forward.scale(-.3)), 4, .15, .15, .15, .04);
            dense(frame, BLOOD_DARK, point.add(forward.scale(-.6)), 3, .2, .2, .2, .01);
        }
        Vec3 ground = owner.position().add(forward.scale(reach));
        dense(frame, debris(owner, ground), ground.add(0, .2, 0), 30, .8, .2, .8, .35);
        if (heavy) dense(frame, ParticleTypes.EXPLOSION, ground.add(0, .4, 0), 2, .4, .1, .4, 0);
        return frame.requested();
    }

    /** Horizontal fan cut used by the sealed strikes and Fury combo. */
    public static int sweep(ServerPlayer owner, Vec3 forward, double range, double halfAngle) {
        int samples = (int) (range * 5);
        VisualFrame frame = VisualFrame.overdrive(owner, owner.position(), samples * 16);
        Vec3 right = right(forward);
        for (int i = 0; i < samples; i++) {
            double t = i / (double) (samples - 1), angle = -halfAngle + 2 * halfAngle * t;
            Vec3 dir = forward.scale(Math.cos(angle)).add(right.scale(Math.sin(angle)));
            Vec3 point = owner.position().add(dir.scale(range * .8)).add(0, 1.1 - .4 * t, 0);
            dense(frame, BLOOD_LARGE, point, 8, .1, .1, .1, .02);
            dense(frame, EMBER, point, 5, .15, .15, .15, .05);
            dense(frame, BLOOD_DARK, point.subtract(dir.scale(.3)), 2, .1, .1, .1, .01);
        }
        return frame.requested();
    }

    /** Fissure torn through the ground: rubble, lava sparks and explosions along the cut. Blocks stay intact. */
    public static int fissure(ServerPlayer owner, Vec3 origin, Vec3 forward, double length, boolean bankai) {
        int samples = (int) (length * 2), per = bankai ? 12 : 8;
        VisualFrame frame = VisualFrame.overdrive(owner, origin, samples * (per * 2 + 12));
        for (int i = 1; i <= samples; i++) {
            Vec3 point = origin.add(forward.scale(i * .5)).add(0, .15, 0);
            dense(frame, BLOOD_LARGE, point, per, .2, .08, .2, .03);
            dense(frame, debris(owner, point), point.add(0, .3, 0), per, .45, .2, .45, .35);
            dense(frame, ParticleTypes.LAVA, point, 2, .25, .1, .25, .02);
            dense(frame, ParticleTypes.LARGE_SMOKE, point, 3, .3, .2, .3, .03);
            dense(frame, EMBER, point.add(0, .8, 0), 4, .15, .8, .15, .06);
            if (i % (bankai ? 3 : 5) == 0) dense(frame, ParticleTypes.EXPLOSION, point.add(0, .4, 0), 1, .2, .1, .2, 0);
        }
        return frame.requested();
    }

    /** Expanding ground ring for slams, roars and the release shockwave. */
    public static int ring(ServerPlayer owner, Vec3 center, double radius, int samples, boolean smoke) {
        VisualFrame frame = VisualFrame.overdrive(owner, center, samples * (smoke ? 14 : 10));
        for (int i = 0; i < samples; i++) {
            double angle = i * Math.PI * 2 / samples;
            Vec3 point = center.add(Math.cos(angle) * radius, .2, Math.sin(angle) * radius);
            dense(frame, BLOOD_LARGE, point, 4, .15, .08, .15, .02);
            dense(frame, EMBER, point, 3, .15, .3, .15, .05);
            dense(frame, debris(owner, point), point, 3, .2, .1, .2, .25);
            if (smoke) dense(frame, ParticleTypes.CAMPFIRE_COSY_SMOKE, point, 2, .15, .1, .15, .02);
        }
        return frame.requested();
    }

    /** Release pillar: a twisting column of crimson reiatsu crowned by a skull, faced toward the owner's look. */
    public static int pillar(ServerPlayer owner, Vec3 forward, double height) {
        VisualFrame frame = VisualFrame.overdrive(owner, owner.position(), 3800);
        Vec3 base = owner.position();
        for (int i = 0; i < (int) (height * 5); i++) {
            double y = i / 5.0;
            for (int strand = 0; strand < 3; strand++) {
                double angle = i * .7 + strand * Math.PI * 2 / 3, radius = 1.4 - y * .06;
                Vec3 point = base.add(Math.cos(angle) * radius, y, Math.sin(angle) * radius);
                dense(frame, BLOOD_LARGE, point, 5, .25, .2, .25, .03);
                dense(frame, EMBER, point, 3, .35, .25, .35, .07);
            }
            dense(frame, BLOOD_DARK, base.add(0, y, 0), 6, .5, .2, .5, .02);
        }
        Vec3 right = right(forward); double cell = .8;
        Vec3 skullTop = base.add(0, height + SKULL.length * cell, 0).add(forward.scale(-1.5));
        for (int row = 0; row < SKULL.length; row++) for (int col = 0; col < SKULL[row].length(); col++) {
            if (SKULL[row].charAt(col) != '#') continue;
            Vec3 point = skullTop.add(right.scale((col - 5) * cell)).add(0, -row * cell, 0);
            dense(frame, BLOOD_LARGE, point, 8, .2, .2, .2, .005);
            dense(frame, EMBER, point, 2, .2, .2, .2, .01);
        }
        dense(frame, ParticleTypes.EXPLOSION_EMITTER, base.add(0, .5, 0), 1, 0, 0, 0, 0);
        dense(frame, debris(owner, base), base.add(0, .3, 0), 160, 3, .4, 3, .5);
        dense(frame, ParticleTypes.LARGE_SMOKE, base.add(0, .5, 0), 60, 3, .5, 3, .08);
        return frame.requested();
    }

    /** Trail left behind during a charge. */
    public static int trail(ServerPlayer owner) {
        VisualFrame frame = VisualFrame.overdrive(owner, owner.position(), 80);
        dense(frame, BLOOD_LARGE, owner.position().add(0, 1, 0), 30, .4, .6, .4, .04);
        dense(frame, EMBER, owner.position().add(0, 1, 0), 15, .4, .6, .4, .08);
        dense(frame, debris(owner, owner.position()), owner.position().add(0, .2, 0), 20, .4, .1, .4, .3);
        dense(frame, ParticleTypes.LARGE_SMOKE, owner.position().add(0, .4, 0), 8, .4, .2, .4, .02);
        return frame.requested();
    }
}
