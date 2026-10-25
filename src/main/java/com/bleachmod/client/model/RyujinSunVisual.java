package com.bleachmod.client.model;

import com.bleachmod.common.visual.RyujinBankaiTimeline;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

import java.util.Random;

/**
 * Renders the monumental Scenic Sun of Flames ("Sol de Chamas") behind Yamamoto/player.
 * Reconstructed with structured, organic fire: incandescant plasma core, recognizable volumetric
 * flame mantle, turbulent irregular corona, moving tongues with 3D depth, curving absorption ribbons
 * that detach from the perimeter and converge into the katana blade, ending with carbonization,
 * rising fine smoke, and cooling embers.
 */
public final class RyujinSunVisual {
    private static final Random RNG = new Random(42);

    private static final class SunRenderTypes extends RenderType {
        private SunRenderTypes() {
            super("unused", DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS, 8192, false, true, () -> {}, () -> {});
        }

        private static final RenderType FLAME_VOLUME = create("ryujin_sun_flame",
                DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS, 262144, false, true,
                CompositeState.builder()
                        .setShaderState(RENDERTYPE_LIGHTNING_SHADER)
                        .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                        .setCullState(NO_CULL)
                        .setWriteMaskState(COLOR_WRITE)
                        .createCompositeState(false));
    }

    private RyujinSunVisual() { }

    public static void render(PoseStack pose, Vec3 cameraPos, Vec3 anchor, Vec3 forward, double age, float sign) {
        var mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null) return;
        if (mc.player.distanceToSqr(anchor) > 128 * 128) return;

        Vec3 side = new Vec3(forward.z, 0, -forward.x);
        Vec3 sunCenter = anchor.subtract(forward.scale(8.0)).add(0, 9.0, 0);
        double radius = RyujinBankaiTimeline.sunRadius(age);
        Matrix4f matrix = pose.last().pose();
        var bufferSource = mc.renderBuffers().bufferSource();
        VertexConsumer consumer = bufferSource.getBuffer(SunRenderTypes.FLAME_VOLUME);

        // Katana anchors in world coordinates (held low, directed outward away from body)
        Vec3 bladeHand = anchor.add(side.scale(sign * 0.42)).add(forward.scale(0.28)).add(0, 0.72, 0);
        Vec3 bladeTip = bladeHand.add(side.scale(sign * 0.78)).add(forward.scale(-0.22)).add(0, -0.42, 0);

        // 1. Structured Scenic Sun volume (hot core, flame mantle, and irregular corona)
        if (radius > 0.08) {
            renderStructuredSun(consumer, matrix, cameraPos, sunCenter, forward, side, radius, age);
        }

        // 2. Converging Absorption Tongues (ticks 30..52)
        double abs = RyujinBankaiTimeline.absorption(age);
        if (abs > 0.005) {
            renderCurvedAbsorptionTongues(consumer, matrix, cameraPos, sunCenter, bladeHand, bladeTip, forward, side, age, abs, sign);
        }

        // 3. Carbonized blade rendering and glowing hairline
        float carb = RyujinBankaiTimeline.carbonization(age);
        if (carb > 0.05F) {
            renderCarbonizedBlade(consumer, matrix, cameraPos, bladeHand, bladeTip, side, carb);
        }

        bufferSource.endBatch(SunRenderTypes.FLAME_VOLUME);

        // 4. Client particle emissions (budgeted, max 24 particles peak, fine smoke on blade)
        emitBudgetedParticles(mc, sunCenter, bladeHand, bladeTip, radius, age, abs, carb, sign);
    }

    /**
     * Renders a living, structured fire body composed of:
     * - Incandescent white-gold core
     * - Recognizable volumetric flame mantle with 3D convection curves
     * - Irregular turbulent corona with animated, moving tongues of fire
     */
    private static void renderStructuredSun(VertexConsumer out, Matrix4f matrix, Vec3 cam, Vec3 center,
                                            Vec3 forward, Vec3 side, double radius, double age) {
        var rotation = Minecraft.getInstance().gameRenderer.getMainCamera().rotation();
        var rVec = new org.joml.Vector3f(1, 0, 0).rotate(rotation);
        var uVec = new org.joml.Vector3f(0, 1, 0).rotate(rotation);
        Vec3 camRight = new Vec3(rVec.x, rVec.y, rVec.z);
        Vec3 camUp = new Vec3(uVec.x, uVec.y, uVec.z);

        // --- Layer A: Dense Incandescent Plasma Core ---
        double coreR = radius * 0.38;
        int coreSteps = 12;
        for (int i = 0; i < coreSteps; i++) {
            double a1 = (i * 2.0 * Math.PI) / coreSteps;
            double a2 = ((i + 1) * 2.0 * Math.PI) / coreSteps;
            double pulse = 1.0 + 0.06 * Math.sin(age * 0.35 + i);

            Vec3 c0 = center.subtract(cam);
            Vec3 c1 = center.add(camRight.scale(Math.cos(a1) * coreR * pulse))
                    .add(camUp.scale(Math.sin(a1) * coreR * pulse)).subtract(cam);
            Vec3 c2 = center.add(camRight.scale(Math.cos(a2) * coreR * pulse))
                    .add(camUp.scale(Math.sin(a2) * coreR * pulse)).subtract(cam);

            quad(out, matrix, c0, c1, c2, c0, 255, 250, 220, 235);
        }

        // --- Layer B: Volumetric Convective Flame Columns (3D Depth) ---
        // 10 flame petal columns positioned with depth along the forward axis
        int columns = 10;
        for (int c = 0; c < columns; c++) {
            double angle = (c * 2.0 * Math.PI) / columns + age * 0.05;
            double depth = Math.sin(angle * 2.0 + age * 0.2) * 1.8;
            Vec3 columnBase = center.add(forward.scale(depth))
                    .add(side.scale(Math.cos(angle) * radius * 0.45))
                    .add(0, Math.sin(angle) * radius * 0.45, 0);

            // Upward flame tongue shape
            int segments = 4;
            double tongueLen = radius * 0.65;
            Vec3 prevL = columnBase;
            Vec3 prevR = columnBase;

            for (int s = 1; s <= segments; s++) {
                double t = (double) s / segments;
                double wave = Math.sin(t * Math.PI * 2.0 + age * 0.3 + c) * 0.35;
                double w = (1.0 - t * 0.7) * (radius * 0.18);
                Vec3 p = columnBase.add(0, t * tongueLen, 0).add(side.scale(wave));

                Vec3 curL = p.add(camRight.scale(-w)).subtract(cam);
                Vec3 curR = p.add(camRight.scale(w)).subtract(cam);

                if (s > 1) {
                    int red = 255;
                    int green = (int) Mth.lerp(t, 210, 80);
                    int blue = (int) Mth.lerp(t, 40, 5);
                    int alpha = (int) Mth.lerp(t, 190, 110);
                    quad(out, matrix, prevL, prevR, curR, curL, red, green, blue, alpha);
                }
                prevL = curL;
                prevR = curR;
            }
        }

        // --- Layer C: Irregular Corona & Drifting Tongues (Perimeter) ---
        // 16 turbulent coronal tongues giving the sun an organic, raging bonfire perimeter
        int tongues = 16;
        for (int i = 0; i < tongues; i++) {
            double a1 = (i * 2.0 * Math.PI) / tongues;
            double a2 = ((i + 1) * 2.0 * Math.PI) / tongues;

            // Multi-scale harmonic turbulence for jagged fiery tongues
            double turb1 = 1.0 + 0.22 * Math.sin(a1 * 4.0 + age * 0.32) + 0.12 * Math.cos(a1 * 7.0 - age * 0.24);
            double turb2 = 1.0 + 0.22 * Math.sin(a2 * 4.0 + age * 0.32) + 0.12 * Math.cos(a2 * 7.0 - age * 0.24);

            double r1 = radius * turb1;
            double r2 = radius * turb2;

            double depth1 = Math.sin(a1 * 3.0 + age * 0.18) * 1.5;
            double depth2 = Math.sin(a2 * 3.0 + age * 0.18) * 1.5;

            Vec3 pCore1 = center.add(forward.scale(depth1 * 0.5)).subtract(cam);
            Vec3 pCore2 = center.add(forward.scale(depth2 * 0.5)).subtract(cam);

            Vec3 pRim1 = center.add(forward.scale(depth1))
                    .add(camRight.scale(Math.cos(a1) * r1))
                    .add(camUp.scale(Math.sin(a1) * r1)).subtract(cam);
            Vec3 pRim2 = center.add(forward.scale(depth2))
                    .add(camRight.scale(Math.cos(a2) * r2))
                    .add(camUp.scale(Math.sin(a2) * r2)).subtract(cam);

            // Inner warm gold to outer deep flame red
            quad(out, matrix, pCore1, pCore2, pRim2, pRim1, 255, 145, 20, 155);

            // Extended corona tongue tips
            if (i % 2 == 0) {
                double tipTurb = turb1 * 1.25;
                Vec3 pTip = center.add(forward.scale(depth1 * 1.2))
                        .add(camRight.scale(Math.cos(a1) * radius * tipTurb))
                        .add(camUp.scale(Math.sin(a1) * radius * tipTurb)).subtract(cam);
                quad(out, matrix, pRim1, pRim2, pTip, pTip, 225, 40, 10, 100);
            }
        }
    }

    /**
     * Renders curving 3D flame tongues that detach from the collapsing sun's perimeter
     * and travel along smooth helical, convex trajectories directly into the katana blade.
     */
    private static void renderCurvedAbsorptionTongues(VertexConsumer out, Matrix4f matrix, Vec3 cam, Vec3 sunCenter,
                                                      Vec3 bladeHand, Vec3 bladeTip, Vec3 forward, Vec3 side,
                                                      double age, double abs, float sign) {
        int tongues = 8;
        for (int s = 0; s < tongues; s++) {
            // Detach angle around the perimeter of the contracting sun
            double angle = (s * 2.0 * Math.PI) / tongues + age * 0.12;
            double currentSunR = RyujinBankaiTimeline.sunRadius(age);
            double sourceR = Math.max(0.5, currentSunR * (0.85 + 0.15 * Math.sin(age * 0.4 + s)));

            double sourceDepth = Math.sin(angle * 2.0 + s) * 1.4;
            Vec3 source = sunCenter.add(forward.scale(sourceDepth))
                    .add(side.scale(Math.cos(angle) * sourceR))
                    .add(0, Math.sin(angle) * sourceR * 0.85, 0);

            // Target distributed along the actual katana blade (from hilt to tip)
            double bladeFraction = (double) s / (tongues - 1);
            Vec3 target = bladeHand.lerp(bladeTip, bladeFraction);

            // Sample curved helical path in 14 continuous segments
            int steps = 14;
            Vec3 prev = source;

            for (int step = 1; step <= steps; step++) {
                double t = (double) step / steps;
                Vec3 current = sampleCurvedTrajectory(source, target, forward, side, t, age, s, sign);

                // Billboard ribbon normal
                Vec3 dir = current.subtract(prev);
                double len = dir.length();
                if (len < 0.001) continue;
                Vec3 dirNorm = dir.scale(1.0 / len);
                Vec3 norm = new Vec3(-dirNorm.z, dirNorm.y, dirNorm.x).normalize();

                // Flame tongue tapers as it enters the blade: wide at source, sharp at blade entry
                double width = (0.34 * (1.0 - t * 0.75) + 0.06) * abs;

                Vec3 a = prev.add(norm.scale(width)).subtract(cam);
                Vec3 b = prev.subtract(norm.scale(width)).subtract(cam);
                Vec3 c = current.subtract(norm.scale(width)).subtract(cam);
                Vec3 d = current.add(norm.scale(width)).subtract(cam);

                // Fiery core gradient: hot gold -> fiery orange -> dark crimson tip entering blade
                int red = 255;
                int green = (int) Mth.lerp(t, 230, 85);
                int blue = (int) Mth.lerp(t, 45, 0);
                int alpha = (int) (175 * abs * Math.sin(t * Math.PI * 0.95 + 0.05));

                quad(out, matrix, a, b, c, d, red, green, blue, alpha);
                prev = current;
            }
        }
    }

    /**
     * Samples a smooth, arched 3D curve with natural convective swirl from sun perimeter to blade.
     */
    private static Vec3 sampleCurvedTrajectory(Vec3 start, Vec3 end, Vec3 forward, Vec3 side,
                                                double t, double age, int tongueIndex, float sign) {
        Vec3 base = start.lerp(end, t);
        // Upward thermal arc
        double arcHeight = Math.sin(t * Math.PI) * 2.6;
        // Lateral helical drift wrapping around the player safely
        double helical = Math.sin(t * Math.PI * 2.5 + age * 0.35 + tongueIndex * 1.4) * 0.55;
        double forwardOffset = Math.sin(t * Math.PI) * 0.6;

        return base.add(forward.scale(forwardOffset))
                .add(side.scale(sign * helical))
                .add(0, arcHeight * 0.75, 0);
    }

    /**
     * Renders the scorched, carbonized katana blade overlay with a subtle incandescent ember hairline.
     */
    private static void renderCarbonizedBlade(VertexConsumer out, Matrix4f matrix, Vec3 cam,
                                              Vec3 hand, Vec3 tip, Vec3 side, float carb) {
        Vec3 bladeDir = tip.subtract(hand);
        double len = bladeDir.length();
        if (len < 0.05) return;
        Vec3 dirNorm = bladeDir.scale(1.0 / len);
        Vec3 edgeNorm = side.cross(dirNorm).normalize();
        double bladeWidth = 0.048;

        // Pitch-black carbonized blade body (matte charred steel/wood texture)
        Vec3 h1 = hand.add(edgeNorm.scale(bladeWidth)).subtract(cam);
        Vec3 h2 = hand.subtract(edgeNorm.scale(bladeWidth)).subtract(cam);
        Vec3 t1 = tip.add(edgeNorm.scale(bladeWidth)).subtract(cam);
        Vec3 t2 = tip.subtract(edgeNorm.scale(bladeWidth)).subtract(cam);

        int blackAlpha = (int) (240 * carb);
        quad(out, matrix, h1, h2, t2, t1, 18, 14, 12, blackAlpha);

        // Thin incandescent glowing ember hairline along the edge
        double edgeOffset = bladeWidth * 0.65;
        Vec3 eh1 = hand.add(edgeNorm.scale(edgeOffset)).subtract(cam);
        Vec3 eh2 = hand.subtract(cam);
        Vec3 et1 = tip.add(edgeNorm.scale(edgeOffset)).subtract(cam);
        Vec3 et2 = tip.subtract(cam);

        int emberAlpha = (int) (150 * carb * 0.75);
        quad(out, matrix, eh1, eh2, et2, et1, 245, 85, 12, emberAlpha);
    }

    /**
     * Budgeted particle emissions:
     * - Corona embers during sun swell (ticks 0..30)
     * - Absorption sparks and smoke into blade (ticks 30..52)
     * - Fine smoke wisps and cooling ashes rising from carbonized blade (ticks 52..124)
     */
    private static void emitBudgetedParticles(Minecraft mc, Vec3 sunCenter, Vec3 bladeHand, Vec3 bladeTip,
                                             double radius, double age, double abs, float carb, float sign) {
        if (mc.level == null || mc.isPaused()) return;

        // 1. Drifting embers around the sun corona (ticks 0..30)
        if (radius > 2.0 && age < 30) {
            for (int i = 0; i < 3; i++) {
                double a = RNG.nextDouble() * Math.PI * 2.0;
                double r = radius * (0.75 + 0.25 * RNG.nextDouble());
                mc.level.addParticle(ParticleTypes.FLAME,
                        sunCenter.x + Math.cos(a) * r,
                        sunCenter.y + Math.sin(a) * r * 0.85,
                        sunCenter.z + (RNG.nextDouble() - 0.5) * 2.0,
                        (RNG.nextDouble() - 0.5) * 0.04, 0.07, (RNG.nextDouble() - 0.5) * 0.04);
            }
        }

        // 2. Converging sparks and smoke into the blade during absorption (ticks 30..52)
        if (abs > 0.08) {
            for (int i = 0; i < 2; i++) {
                double t = RNG.nextDouble();
                Vec3 p = bladeHand.lerp(bladeTip, t);
                mc.level.addParticle(ParticleTypes.SMALL_FLAME,
                        p.x + (RNG.nextDouble() - 0.5) * 0.08,
                        p.y + (RNG.nextDouble() - 0.5) * 0.08,
                        p.z + (RNG.nextDouble() - 0.5) * 0.08,
                        0.0, 0.025, 0.0);
            }
            // Fine ash / smoke at the blade tip
            Vec3 tip = bladeTip;
            mc.level.addParticle(ParticleTypes.SMOKE,
                    tip.x, tip.y + 0.08, tip.z,
                    0.0, 0.035, 0.0);
        }

        // 3. Fine smoke wisps and cooling ashes rising from the carbonized blade (ticks 52..124)
        if (carb > 0.25F && age >= 52 && age < 124) {
            if (RNG.nextFloat() < 0.55F) {
                double t = RNG.nextDouble();
                Vec3 p = bladeHand.lerp(bladeTip, t);
                mc.level.addParticle(ParticleTypes.SMOKE,
                        p.x + (RNG.nextDouble() - 0.5) * 0.04,
                        p.y + (RNG.nextDouble() - 0.5) * 0.04,
                        p.z + (RNG.nextDouble() - 0.5) * 0.04,
                        (RNG.nextDouble() - 0.5) * 0.008, 0.02, (RNG.nextDouble() - 0.5) * 0.008);
            }
        }
    }

    private static void quad(VertexConsumer out, Matrix4f matrix, Vec3 a, Vec3 b, Vec3 c, Vec3 d,
                             int r, int g, int bl, int al) {
        out.vertex(matrix, (float) a.x, (float) a.y, (float) a.z).color(r, g, bl, al).endVertex();
        out.vertex(matrix, (float) b.x, (float) b.y, (float) b.z).color(r, g, bl, al).endVertex();
        out.vertex(matrix, (float) c.x, (float) c.y, (float) c.z).color(r, g, bl, al).endVertex();
        out.vertex(matrix, (float) d.x, (float) d.y, (float) d.z).color(r, g, bl, al).endVertex();
    }
}
