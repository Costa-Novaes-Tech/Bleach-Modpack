package com.bleachmod.common.visual;

import net.minecraft.core.particles.*;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.phys.Vec3;
import java.util.*;

/** Pure profile/geometry definitions plus frame composition. No casts, damage or persistent runtime. */
public final class EffectLibrary {
    public enum Palette { FIRE, ICE, REIATSU, BERSERK }
    public enum Shape { AURA, CUT, BEAM, RELEASE }
    public record Profile(Palette palette, Shape shape) {
        public Profile { Objects.requireNonNull(palette); Objects.requireNonNull(shape); }
        public String id() { return palette.name().toLowerCase(Locale.ROOT) + "/" + shape.name().toLowerCase(Locale.ROOT); }
    }
    private EffectLibrary() { }
    public static List<String> palettes() { return Arrays.stream(Palette.values()).map(p -> p.name().toLowerCase(Locale.ROOT)).toList(); }
    public static List<String> shapes() { return Arrays.stream(Shape.values()).map(p -> p.name().toLowerCase(Locale.ROOT)).toList(); }
    public static Profile resolve(String palette, String shape) {
        return new Profile(Palette.valueOf(palette.toUpperCase(Locale.ROOT)), Shape.valueOf(shape.toUpperCase(Locale.ROOT)));
    }
    public static int budget(int intensity) { return VisualFrame.budget(intensity); }
    public static Vec3 point(Shape shape, Vec3 origin, Vec3 forward, int index, int count, double age) {
        double t = index / (double) Math.max(1, count - 1), angle = index * Math.PI * 2 / count + age * .09;
        Vec3 right = new Vec3(forward.z, 0, -forward.x);
        return switch (shape) {
            case AURA -> origin.add(Math.cos(angle) * .9, .2 + 2.8 * t, Math.sin(angle) * .9);
            case CUT -> origin.add(right.scale(Math.cos(Math.PI * t) * 2)).add(0, .3 + 2 * Math.sin(Math.PI * t), 0);
            case BEAM -> origin.add(forward.scale(6 * t)).add(right.scale(Math.cos(angle) * .12)).add(0, 1 + Math.sin(angle) * .12, 0);
            case RELEASE -> origin.add(Math.cos(angle) * (1 + 2 * (age % 20) / 20), .5 + Math.sin(Math.PI * t), Math.sin(angle) * (1 + 2 * (age % 20) / 20));
        };
    }
    private static ParticleOptions layer(Palette palette, int layer) {
        return switch (palette) {
            case FIRE -> layer == 0 ? ParticleTypes.FLAME : layer == 1
                    ? new DustParticleOptions(new org.joml.Vector3f(1, .4F, .08F), 1) : ParticleTypes.SMOKE;
            case ICE -> layer == 0 ? ParticleTypes.SNOWFLAKE : layer == 1
                    ? new DustParticleOptions(new org.joml.Vector3f(.4F, .8F, 1), 1) : ParticleTypes.CLOUD;
            case REIATSU -> layer == 0 ? ParticleTypes.END_ROD : layer == 1
                    ? new DustParticleOptions(new org.joml.Vector3f(.75F, .5F, 1), 1) : ParticleTypes.ENCHANT;
            case BERSERK -> layer == 0 ? BerserkVisuals.BLOOD : layer == 1 ? BerserkVisuals.EMBER : ParticleTypes.LARGE_SMOKE;
        };
    }
    public static SoundEvent sound(Palette palette) {
        return switch (palette) { case FIRE -> SoundEvents.BLAZE_SHOOT; case ICE -> SoundEvents.GLASS_BREAK; case REIATSU -> SoundEvents.BEACON_ACTIVATE; case BERSERK -> SoundEvents.RAVAGER_ROAR; };
    }
    public static void emit(Profile profile, VisualFrame frame, Vec3 origin, Vec3 forward, int intensity, double age) {
        int count = budget(intensity);
        ParticleOptions[] layers = {layer(profile.palette, 0), layer(profile.palette, 1), layer(profile.palette, 2)};
        for (int i = 0; i < count; i++) {
            // Two thirds are the main material; accents and residue share the remaining budget.
            int layer = i % 6 < 4 ? 0 : i % 6 == 4 ? 1 : 2;
            frame.emit(layers[layer], point(profile.shape, origin, forward, i, count, age), layer == 2 ? .1 : .03, .01);
        }
    }
}
