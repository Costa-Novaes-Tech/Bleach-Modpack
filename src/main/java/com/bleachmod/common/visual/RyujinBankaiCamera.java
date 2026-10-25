package com.bleachmod.common.visual;

import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

/**
 * Camera framing and directed movement for Ryūjin Jakka / Zanka no Tachi Bankai cinematic (140 ticks).
 *
 * Sequence:
 * 1. Ticks 0..30: Frontal 3/4 wide shot with discreet lateral offset, framing full body silhouette and the 18-block sun behind.
 * 2. Ticks 30..52: Moderate physical dolly push-in during flame absorption, keeping the entire fire-to-blade trajectory clearly framed.
 * 3. Ticks 52..68: Brief detail shot of the wielding hand and carbonized katana blade, spacious enough to clearly recognize outward blade orientation.
 * 4. Ticks 68..115: Medium 3/4 shot opening back up to face, torso, and blade during name pronunciation ("Zanka no Tachi").
 * 5. Ticks 115..140: Smooth physical and FOV transition back to gameplay perspective without clipping or abrupt cuts.
 */
public final class RyujinBankaiCamera {
    public record Frame(Vec3 eye, Vec3 focus, float fov) { }

    private RyujinBankaiCamera() { }

    public static Frame sample(Vec3 anchor, Vec3 forward, double age) {
        return sample(anchor, forward, age, 1.0F);
    }

    public static Frame sample(Vec3 anchor, Vec3 forward, double age, float sign) {
        Vec3 side = new Vec3(forward.z, 0, -forward.x);
        age = RyujinBankaiTimeline.clamp(age);
        sign = sign < 0 ? -1.0F : 1.0F;

        // --- Shot 1: Frontal 3/4 Wide Shot (Ticks 0..30) ---
        // Discreet lateral offset showing full body silhouette against the monumental 18-block sun
        Vec3 wideEye = anchor.add(forward.scale(10.8)).add(side.scale(sign * 1.8)).add(0, 2.3, 0);
        Vec3 wideFocus = anchor.add(0, 1.35, 0);
        float wideFov = 68.0F;

        if (age <= 30.0) {
            // Subtle slow push-in during initial growth
            double t = RyujinBankaiTimeline.progress(age, 0.0, 30.0);
            Vec3 subtleEye = wideEye.add(forward.scale(-0.8 * t)).add(side.scale(sign * -0.2 * t)).add(0, -0.15 * t, 0);
            return new Frame(subtleEye, wideFocus, wideFov);
        }

        // --- Shot 2: Absorption Moderate Dolly Push-in (Ticks 30..52) ---
        // Closes distance while keeping the entire curving fire streamer trajectory from sun to blade legible
        Vec3 absorbEyeTarget = anchor.add(forward.scale(6.4)).add(side.scale(sign * 2.2)).add(0, 1.55, 0);
        Vec3 absorbFocusTarget = anchor.add(side.scale(sign * 0.45)).add(0, 1.15, 0);
        float absorbFovTarget = 58.0F;

        if (age <= 52.0) {
            double t = RyujinBankaiTimeline.progress(age, 30.0, 52.0);
            Vec3 absorbEye = wideEye.add(forward.scale(-0.8)).add(side.scale(sign * -0.2)).add(0, -0.15, 0).lerp(absorbEyeTarget, t);
            Vec3 absorbFocus = wideFocus.lerp(absorbFocusTarget, t);
            float absorbFov = (float) Mth.lerp(t, wideFov, absorbFovTarget);
            return new Frame(absorbEye, absorbFocus, absorbFov);
        }

        // --- Shot 3: Carbonized Blade Detail Shot (Ticks 52..68) ---
        // Brief close-up of wielding hand and carbonized katana blade, clearly framing outward orientation
        // Wielding hand is at anchor + side*(sign*0.42) + forward*0.28, blade tip at + side*(sign*1.2)
        Vec3 detailEye = anchor.add(side.scale(sign * 1.85)).add(forward.scale(2.1)).add(0, 0.95, 0);
        Vec3 detailFocus = anchor.add(side.scale(sign * 0.65)).add(forward.scale(0.12)).add(0, 0.62, 0);
        float detailFov = 48.0F;

        if (age < 56.0) {
            // Smooth transition into detail shot (52..56)
            double t = RyujinBankaiTimeline.progress(age, 52.0, 56.0);
            Vec3 blendedEye = absorbEyeTarget.lerp(detailEye, t);
            Vec3 blendedFocus = absorbFocusTarget.lerp(detailFocus, t);
            float blendedFov = (float) Mth.lerp(t, absorbFovTarget, detailFov);
            return new Frame(blendedEye, blendedFocus, blendedFov);
        }

        if (age <= 66.0) {
            // Sustained close-up with micro-drift framing the carbonized blade and smoke
            double micro = Math.sin((age - 56.0) * 0.12) * 0.03;
            Vec3 microEye = detailEye.add(side.scale(sign * micro)).add(0, micro * 0.5, 0);
            return new Frame(microEye, detailFocus, detailFov);
        }

        // --- Shot 4: Medium 3/4 Shot (Ticks 68..115) ---
        // Opens back up to face, torso, and sword during name pronunciation ("Zanka no Tachi")
        Vec3 medEye = anchor.add(forward.scale(4.5)).add(side.scale(sign * 1.55)).add(0, 1.45, 0);
        Vec3 medFocus = anchor.add(side.scale(sign * 0.18)).add(0, 1.35, 0);
        float medFov = 56.0F;

        if (age < 74.0) {
            // Smooth dolly out from detail shot to medium shot (66..74)
            double t = RyujinBankaiTimeline.progress(age, 66.0, 74.0);
            Vec3 blendedEye = detailEye.lerp(medEye, t);
            Vec3 blendedFocus = detailFocus.lerp(medFocus, t);
            float blendedFov = (float) Mth.lerp(t, detailFov, medFov);
            return new Frame(blendedEye, blendedFocus, blendedFov);
        }

        if (age <= 115.0) {
            // Sustained medium hold with subtle breathing drift
            double drift = Math.sin((age - 74.0) * 0.08) * 0.05;
            Vec3 driftingEye = medEye.add(side.scale(sign * drift)).add(0, drift * 0.4, 0);
            return new Frame(driftingEye, medFocus, medFov);
        }

        // --- Shot 5: Preparation and Smooth Return to Gameplay (Ticks 115..140) ---
        double t = RyujinBankaiTimeline.progress(age, 115.0, 140.0);
        Vec3 returnEyeTarget = anchor.subtract(forward.scale(4.0)).add(0, 1.8, 0);
        Vec3 returnFocusTarget = anchor.add(0, 1.6, 0);
        float returnFovTarget = 70.0F;

        Vec3 returnEye = medEye.lerp(returnEyeTarget, t);
        Vec3 returnFocus = medFocus.lerp(returnFocusTarget, t);
        float returnFov = (float) Mth.lerp(t, medFov, returnFovTarget);
        return new Frame(returnEye, returnFocus, returnFov);
    }
}
