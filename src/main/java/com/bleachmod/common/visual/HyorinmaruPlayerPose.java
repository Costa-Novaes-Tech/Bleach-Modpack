package com.bleachmod.common.visual;

import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

/**
 * Authored standing two-handed transformation grip and dynamic progression for Daiguren Hyorinmaru's Bankai.
 * Player feet remain grounded, katana held with both hands on the tsuka in front of the body,
 * blade pointing vertically downward away from legs and chest.
 * Native unbent solution: no BendyLib required. Units are radians (rotation) and model pixels (item translation).
 */
public final class HyorinmaruPlayerPose {
    public record Limb(Vec3 rotation, double bend) { }

    private HyorinmaruPlayerPose() { }

    public static double smooth(double t) {
        t = Mth.clamp(t, 0.0, 1.0);
        return t * t * (3.0 - 2.0 * t);
    }

    /**
     * Presentation weight across the 400-tick cinematic.
     * Smooth lead-in (0-20), sustained hold (20-340), smooth release to combat (340-400).
     */
    public static float weight(float age, float duration) {
        if (age <= 0.0F || age >= duration) return 0.0F;
        float leadIn = (float) smooth(age / 20.0);
        float leadOut = (float) (1.0 - smooth((age - 340.0) / 60.0));
        return leadIn * leadOut;
    }

    /** Torso (body) rotation in radians. */
    public static Vec3 body(boolean leftMain, float age) {
        // Ticks 0-24: Preparation slight forward lean
        double prep = smooth(age / 20.0);
        double pitch = -0.06 * prep;
        // Breathing oscillation during hold
        if (age > 20.0F && age < 340.0F) {
            pitch += 0.012 * Math.sin(age * 0.12);
        }
        // Ticks 88-112: Hydra eruption recoil from behind (straightening slightly)
        if (age >= 88.0F && age <= 112.0F) {
            double surge = smooth((age - 88.0) / 10.0) * (1.0 - smooth((age - 100.0) / 12.0));
            pitch += 0.04 * surge;
        }
        // Ticks 216-245: Bracing against the frost shockwave / roar pressure
        if (age >= 216.0F && age <= 245.0F) {
            double pressure = smooth((age - 216.0) / 8.0) * (1.0 - smooth((age - 232.0) / 13.0));
            double shudder = Math.sin((age - 216.0) * 1.5) * 0.014 * pressure;
            pitch += -0.045 * pressure + shudder;
        }
        return new Vec3(pitch, 0.0, 0.0);
    }

    /** Head rotation in radians. Solemn focus down, slight rise during roar. */
    public static Vec3 head(boolean leftMain, float age) {
        double prep = smooth(age / 20.0);
        double pitch = 0.08 * prep;
        if (age >= 216.0F && age <= 245.0F) {
            double pressure = smooth((age - 216.0) / 8.0) * (1.0 - smooth((age - 232.0) / 13.0));
            pitch += -0.05 * pressure;
        }
        return new Vec3(pitch, 0.0, 0.0);
    }

    /** Legs stay grounded with feet planted on the floor (Y=24, no walking displacement). */
    public static Vec3 legs(boolean leftMain, float age) {
        return Vec3.ZERO;
    }

    /**
     * Main arm holding upper tsuka grip at Y=3.0, Z=-8.422.
     * Native unbent solution (bend = 0.0).
     */
    public static Limb mainArm(boolean leftMain, float age) {
        double rotX = -0.986781598;
        double rotY = leftMain ? 0.586010111 : -0.586010111;
        double rotZ = leftMain ? 0.547101250 : -0.547101250;
        if (age >= 216.0F && age <= 245.0F) {
            double pressure = smooth((age - 216.0) / 8.0) * (1.0 - smooth((age - 232.0) / 13.0));
            double shudder = Math.sin((age - 216.0) * 1.5) * 0.012 * pressure;
            rotX += -0.02 * pressure + shudder;
        }
        return new Limb(new Vec3(rotX, rotY, rotZ), 0.0);
    }

    /**
     * Support arm holding lower tsuka grip at Y=6.0, Z=-8.422.
     * Native unbent solution (bend = 0.0).
     */
    public static Limb supportArm(boolean leftMain, float age) {
        double rotX = -0.821124155;
        double rotY = leftMain ? -0.384789654 : 0.384789654;
        double rotZ = leftMain ? -0.404645935 : 0.404645935;
        if (age >= 216.0F && age <= 245.0F) {
            double pressure = smooth((age - 216.0) / 8.0) * (1.0 - smooth((age - 232.0) / 13.0));
            double shudder = Math.sin((age - 216.0) * 1.5) * 0.012 * pressure;
            rotX += -0.02 * pressure + shudder;
        }
        return new Limb(new Vec3(rotX, rotY, rotZ), 0.0);
    }

    /** Solved item rotation in radians aligning blade vertically down (+Y) toward the floor. */
    public static Vec3 itemRotation(boolean leftMain) {
        return leftMain
                ? new Vec3(-0.152397469, 0.768508097, 2.448441417)
                : new Vec3(-2.640129334, 0.768508097, 0.693151237);
    }

    /** Solved item position offset in model pixels bringing tsuka to the palm center. */
    public static Vec3 itemPosition(boolean leftMain) {
        return leftMain
                ? new Vec3(0.856478328, -0.182147874, -0.801859753)
                : new Vec3(0.832721013, 0.229897075, 0.814515246);
    }
}
