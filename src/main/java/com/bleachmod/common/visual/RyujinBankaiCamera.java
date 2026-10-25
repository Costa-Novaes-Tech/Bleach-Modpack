package com.bleachmod.common.visual;

import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

/**
 * Camera framing for Ryūjin Jakka / Zanka no Tachi Bankai cinematic.
 * Coordinates are relative to player's activation anchor and forward vector.
 * Frontal 3/4 wide shot (ticks 0..52) -> smooth dolly to medium 3/4 (52..60) -> sustained hold (60..124) -> smooth return (124..140).
 */
public final class RyujinBankaiCamera {
    public record Frame(Vec3 eye, Vec3 focus, float fov) { }

    private RyujinBankaiCamera() { }

    public static Frame sample(Vec3 anchor, Vec3 forward, double age) {
        Vec3 side = new Vec3(forward.z, 0, -forward.x);
        age = RyujinBankaiTimeline.clamp(age);

        // Wide Shot: Frontal 3/4 showing full silhouette and the 18-block sun behind
        Vec3 wideEye = anchor.add(forward.scale(13.2)).add(side.scale(3.8)).add(0, 3.2, 0);
        Vec3 wideFocus = anchor.add(forward.scale(-1.2)).add(0, 3.6, 0);
        float wideFov = 70.0F;
        Frame wideFrame = new Frame(wideEye, wideFocus, wideFov);

        if (age <= 52.0) {
            // Gentle slow push-in during the absorption phase (30..52)
            if (age > 30.0) {
                double t = RyujinBankaiTimeline.progress(age, 30.0, 52.0);
                Vec3 absorbingEye = wideEye.add(forward.scale(-1.8 * t)).add(side.scale(-0.4 * t)).add(0, -0.3 * t, 0);
                Vec3 absorbingFocus = wideFocus.lerp(anchor.add(0, 1.8, 0), t * 0.5);
                float absorbingFov = (float) Mth.lerp(t, 70.0, 65.0);
                return new Frame(absorbingEye, absorbingFocus, absorbingFov);
            }
            return wideFrame;
        }

        // Medium Shot: Frontal 3/4 framing chest, face, and the carbonized katana held low outward
        Vec3 medEye = anchor.add(forward.scale(4.8)).add(side.scale(1.8)).add(0, 1.35, 0);
        Vec3 medFocus = anchor.add(side.scale(0.3)).add(0, 1.25, 0);
        float medFov = 52.0F;
        Frame medFrame = new Frame(medEye, medFocus, medFov);

        if (age < 60.0) {
            // Dolly transition from wide shot to medium close 3/4 (52..60)
            double t = RyujinBankaiTimeline.progress(age, 52.0, 60.0);
            Vec3 absorbingEye = wideEye.add(forward.scale(-1.8)).add(side.scale(-0.4)).add(0, -0.3, 0);
            Vec3 blendedEye = absorbingEye.lerp(medEye, t);
            Vec3 blendedFocus = anchor.add(0, 1.8, 0).lerp(medFocus, t);
            float blendedFov = (float) Mth.lerp(t, 65.0, 52.0);
            return new Frame(blendedEye, blendedFocus, blendedFov);
        }

        if (age <= 124.0) {
            // Sustained medium 3/4 hold with subtle breathing drift
            double drift = Math.sin((age - 60.0) * 0.06) * 0.08;
            Vec3 driftingEye = medEye.add(side.scale(drift)).add(0, drift * 0.5, 0);
            return new Frame(driftingEye, medFocus, medFov);
        }

        // Return phase (124..140): smooth return towards standard third-person rear camera
        double returnT = RyujinBankaiTimeline.progress(age, 124.0, 140.0);
        Vec3 naturalEye = anchor.subtract(forward.scale(4.0)).add(0, 1.9, 0);
        Vec3 naturalFocus = anchor.add(0, 1.5, 0);
        float naturalFov = 70.0F;

        Vec3 returnEye = medFrame.eye.lerp(naturalEye, returnT);
        Vec3 returnFocus = medFrame.focus.lerp(naturalFocus, returnT);
        float returnFov = (float) Mth.lerp(returnT, medFov, naturalFov);
        return new Frame(returnEye, returnFocus, returnFov);
    }
}
