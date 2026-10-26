package com.bleachmod.common.visual;

import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

/**
 * PlayerAnimator pose choreography for Byakuya Kuchiki during Senbonzakura Kageyoshi Bankai.
 * Total presentation duration: 290 ticks (14.5 seconds).
 *
 * Choreography:
 *   t = 0..10:    Serene upright stance holding the single inverted vertical katana in front of the main hand.
 *   t = 10..35:   Hand opens and arm smoothly retracts as the katana separates and falls vertically under gravity.
 *   t = 35..76:   Stoic captain stance watching the katana sink into the dark spiritual floor and ripples.
 *   t = 76..160:  Monumental blades rising; dignified stance observing the soaring steel.
 *   t = 160..224: "Senbonzakura Kageyoshi" name invocation; main arm outstretched forward
 *                 in commanding authority pointing down the blade corridor.
 *   t = 224..284: Graceful recovery into combat-ready posture as blades dissolve into petals and camera returns.
 */
public final class SenbonzakuraPlayerPose {
    public static final float WEAPON_RESTORE_TICK = 264.0F;
    public static final float POSE_END_TICK = 284.0F;

    private SenbonzakuraPlayerPose() { }

    public record Transform(Vec3 rotation, Vec3 position) { }

    public static float weight(float age, int duration) {
        if (age < 0.0F || age >= Math.min(duration, POSE_END_TICK)) return 0.0F;
        float in = Mth.clamp(age / 4.0F, 0.0F, 1.0F);
        float out = Mth.clamp((POSE_END_TICK - age) / 18.0F, 0.0F, 1.0F);
        float smoothIn = in * in * (3.0F - 2.0F * in);
        float smoothOut = out * out * (3.0F - 2.0F * out);
        return smoothIn * smoothOut;
    }

    /**
     * Returns true while the vanilla held item and hip scabbard should be suppressed
     * so only the single cinematic katana (and then the 20 colossal blades) exists visually.
     */
    public static boolean hideHeldWeapon(double age) {
        return age >= 0.0 && age < WEAPON_RESTORE_TICK;
    }

    public static Transform mainArm(boolean leftHanded, float age) {
        float sign = leftHanded ? -1.0F : 1.0F;
        Vec3 rot;

        if (age < 10.0F) {
            // Holding the vertical katana hilt directly in front of the main hand (tsuka at y=0.92..1.16, forward=0.44, side=-0.26*sign)
            rot = new Vec3(-0.88, -0.08 * sign, 0.04 * sign);
        } else if (age < 35.0F) {
            // Hand opens, releases hilt at t=10, and smoothly retracts to side as the blade falls vertically
            float t = Mth.clamp((age - 10.0F) / 25.0F, 0.0F, 1.0F);
            float smooth = t * t * (3.0F - 2.0F * t);
            rot = new Vec3(
                    Mth.lerp(smooth, -0.88, 0.06),
                    Mth.lerp(smooth, -0.08, 0.0) * sign,
                    Mth.lerp(smooth, 0.04, 0.09) * sign
            );
        } else if (age < 160.0F) {
            // Stoic captain stance: arms relaxed at sides watching the katana sink and the 20 colossal blades rise
            rot = new Vec3(0.06, 0.0, 0.09 * sign);
        } else if (age < 224.0F) {
            // "Senbonzakura Kageyoshi" Command Gesture: main arm raised forward with commanding authority
            float t = Mth.clamp((age - 160.0F) / 14.0F, 0.0F, 1.0F);
            float smooth = t * t * (3.0F - 2.0F * t);
            rot = new Vec3(Mth.lerp(smooth, 0.06, -1.42), -0.08 * sign, 0.08 * sign);
        } else {
            // Progressive settle into natural combat readiness during petal dispersion and camera return
            float t = Mth.clamp((age - 224.0F) / 52.0F, 0.0F, 1.0F);
            float smooth = t * t * (3.0F - 2.0F * t);
            rot = new Vec3(
                    Mth.lerp(smooth, -1.42, -0.18),
                    Mth.lerp(smooth, -0.08, 0.0) * sign,
                    Mth.lerp(smooth, 0.08, 0.05) * sign
            );
        }
        return new Transform(rot, Vec3.ZERO);
    }

    public static Transform supportArm(boolean leftHanded, float age) {
        float sign = leftHanded ? -1.0F : 1.0F;
        Vec3 rot;
        if (age < 160.0F) {
            // Support arm relaxed at side
            rot = new Vec3(0.05, 0.0, -0.08 * sign);
        } else if (age < 224.0F) {
            // Support arm slightly poised to balance commanding posture
            float t = Mth.clamp((age - 160.0F) / 14.0F, 0.0F, 1.0F);
            float smooth = t * t * (3.0F - 2.0F * t);
            rot = new Vec3(Mth.lerp(smooth, 0.05, 0.16), 0.04 * sign, -0.12 * sign);
        } else {
            // Settling back to natural stance
            float t = Mth.clamp((age - 224.0F) / 52.0F, 0.0F, 1.0F);
            float smooth = t * t * (3.0F - 2.0F * t);
            rot = new Vec3(Mth.lerp(smooth, 0.16, 0.04), 0.0, -0.06 * sign);
        }
        return new Transform(rot, Vec3.ZERO);
    }

    public static Vec3 itemRotation(boolean leftHanded, float age) {
        return Vec3.ZERO;
    }

    public static Vec3 itemPosition(boolean leftHanded, float age) {
        if (hideHeldWeapon(age)) {
            // Hide the hand-rendered item completely while the world-rendered katana / blade corridor represents the weapon
            return new Vec3(0, -999.0, 0);
        }
        return Vec3.ZERO;
    }
}
