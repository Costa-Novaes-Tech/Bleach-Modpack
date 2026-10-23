package com.bleachmod.common.visual;

import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

/**
 * PlayerAnimator pose choreography for Byakuya Kuchiki during Senbonzakura Kageyoshi Bankai.
 * Total presentation duration: 290 ticks (14.5 seconds).
 *
 * Choreography:
 *   t = 0..10:   Serene upright stance holding inverted katana pointing straight down.
 *   t = 10..35:  Hand opens and arm slightly retracts as katana separates and falls under gravity.
 *   t = 35..76:  Stoic captain stance watching the katana sink into spiritual water ripples.
 *   t = 76..160: Monumental blades rising; dignified stance observing the soaring steel.
 *   t = 160..228: "Senbonzakura Kageyoshi" name invocation; right arm outstretched forward
 *                in commanding authority pointing down the blade corridor.
 *   t = 228..290: Graceful recovery and settle into combat-ready posture as petals swirl.
 */
public final class SenbonzakuraPlayerPose {
    private SenbonzakuraPlayerPose() { }

    public record Transform(Vec3 rotation, Vec3 position) { }

    public static float weight(float age, int duration) {
        if (age < 0 || age >= duration) return 0.0F;
        float in = Mth.clamp(age / 6.0F, 0.0F, 1.0F);
        float out = Mth.clamp((duration - age) / 10.0F, 0.0F, 1.0F);
        return in * out;
    }

    public static Transform mainArm(boolean leftHanded, float age) {
        float sign = leftHanded ? -1.0F : 1.0F;
        Vec3 rot;

        if (age < 10.0F) {
            // Holding inverted katana pointing straight down
            rot = new Vec3(-0.55, 0.05 * sign, -0.15 * sign);
        } else if (age < 35.0F) {
            // Hand opens, releases hilt, floats back to side with open fingers
            float t = (age - 10.0F) / 25.0F;
            float smooth = t * t * (3.0F - 2.0F * t);
            rot = new Vec3(Mth.lerp(smooth, -0.55, 0.06), 0.05 * (1.0F - smooth) * sign, Mth.lerp(smooth, -0.15, 0.08) * sign);
        } else if (age < 160.0F) {
            // Stoic captain stance: arms relaxed at sides watching the 20 colossal blades rise
            rot = new Vec3(0.06, 0.0, 0.08 * sign);
        } else if (age < 228.0F) {
            // "Senbonzakura Kageyoshi" Command Gesture: right arm raised forward with commanding authority
            float t = Mth.clamp((age - 160.0F) / 12.0F, 0.0F, 1.0F);
            float smooth = t * t * (3.0F - 2.0F * t);
            rot = new Vec3(Mth.lerp(smooth, 0.06, -1.48), -0.10 * sign, 0.08 * sign);
        } else {
            // Gentle settle into combat readiness as petals swirl and dissipate
            float t = Mth.clamp((age - 228.0F) / 45.0F, 0.0F, 1.0F);
            float smooth = t * t * (3.0F - 2.0F * t);
            rot = new Vec3(Mth.lerp(smooth, -1.48, -0.35), -0.05 * (1.0F - smooth) * sign, 0.08 * sign);
        }
        return new Transform(rot, Vec3.ZERO);
    }

    public static Transform supportArm(boolean leftHanded, float age) {
        float sign = leftHanded ? -1.0F : 1.0F;
        Vec3 rot;
        if (age < 160.0F) {
            // Left arm relaxed at side
            rot = new Vec3(0.06, 0.0, -0.08 * sign);
        } else if (age < 228.0F) {
            // Left arm slightly retracted at waist to balance commanding posture
            float t = Mth.clamp((age - 160.0F) / 12.0F, 0.0F, 1.0F);
            float smooth = t * t * (3.0F - 2.0F * t);
            rot = new Vec3(Mth.lerp(smooth, 0.06, 0.20), 0.04 * sign, -0.14 * sign);
        } else {
            // Settling back to natural stance
            float t = Mth.clamp((age - 228.0F) / 45.0F, 0.0F, 1.0F);
            float smooth = t * t * (3.0F - 2.0F * t);
            rot = new Vec3(Mth.lerp(smooth, 0.20, 0.06), 0.0, -0.08 * sign);
        }
        return new Transform(rot, Vec3.ZERO);
    }

    public static Vec3 itemRotation(boolean leftHanded, float age) {
        // Pointing vertically down when held
        return new Vec3(Math.PI, 0, 0);
    }

    public static Vec3 itemPosition(boolean leftHanded, float age) {
        if (age < 10.0F) {
            return Vec3.ZERO;
        }
        // Once released at tick 10, hide the hand item so the world-rendered falling blade takes over seamlessly
        return new Vec3(0, -999.0, 0);
    }
}
