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
 * Renders the monumental Scenic Sun of Flames ("Sol de Chamas") behind Yamamoto/player,
 * its structured volume, turbulent coronal tongues, curved collapsing absorption towards
 * the katana blade, and the carbonized blade smoke.
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

        // 1. Structured Scenic Sun volume (layers of fire)
        if (radius > 0.1) {
            renderSunVolume(consumer, matrix, cameraPos, sunCenter, forward, side, radius, age);
        }

        // Katana anchors in world coordinates
        // Sword held low, directed outward away from body (mirrored for lefties)
        Vec3 bladeHand = anchor.add(side.scale(sign * 0.42)).add(forward.scale(0.28)).add(0, 0.72, 0);
        Vec3 bladeTip = bladeHand.add(side.scale(sign * 0.78)).add(forward.scale(-0.22)).add(0, -0.42, 0);

        // 2. Converging Absorption Streamers (ticks 30..52)
        double abs = RyujinBankaiTimeline.absorption(age);
        if (abs > 0.01) {
            renderAbsorptionStreamers(consumer, matrix, cameraPos, sunCenter, bladeHand, bladeTip, forward, side, age, abs, sign);
        }

        // 3. Carbonized blade rendering and rising smoke
        float carb = RyujinBankaiTimeline.carbonization(age);
        if (carb > 0.05F) {
            renderCarbonizedBlade(consumer, matrix, cameraPos, bladeHand, bladeTip, side, carb);
        }

        bufferSource.endBatch(SunRenderTypes.FLAME_VOLUME);

        // 4. Tick particle emissions (budgeted, max 32 particles)
        emitBudgetedParticles(mc, sunCenter, bladeHand, bladeTip, radius, age, abs, carb, sign);
    }

    private static void renderSunVolume(VertexConsumer out, Matrix4f matrix, Vec3 cam, Vec3 center, Vec3 forward, Vec3 side, double radius, double age) {
        // Camera billboard vectors
        var rotation = Minecraft.getInstance().gameRenderer.getMainCamera().rotation();
        var rVec = new org.joml.Vector3f(1, 0, 0).rotate(rotation);
        var uVec = new org.joml.Vector3f(0, 1, 0).rotate(rotation);
        Vec3 camRight = new Vec3(rVec.x, rVec.y, rVec.z);
        Vec3 camUp = new Vec3(uVec.x, uVec.y, uVec.z);

        // Concentric volumetric shells:
        // Shell 0: Bright Core (Yellow/White)
        // Shell 1: Mid Body (Golden Orange)
        // Shell 2: Outer Body (Deep Orange/Fire)
        // Shell 3: Corona tongues (Crimson/Red flame tongues)
        int layers = 4;
        for (int l = 0; l < layers; l++) {
            double layerFraction = (l + 1.0) / layers;
            double r = radius * layerFraction;
            int segments = 16 + l * 4;

            int red, green, blue, alpha;
            if (l == 0) {
                red = 255; green = 245; blue = 160; alpha = 210;
            } else if (l == 1) {
                red = 255; green = 190; blue = 40; alpha = 175;
            } else if (l == 2) {
                red = 255; green = 110; blue = 15; alpha = 145;
            } else {
                red = 220; green = 35; blue = 5; alpha = 115;
            }

            // Distribute slices with slight depth offsets along forward axis for 3D body
            for (double depthOffset : new double[]{-1.8, 0.0, 1.8}) {
                Vec3 sliceCenter = center.add(forward.scale(depthOffset * (1.0 - layerFraction * 0.5)));
                double depthScale = Math.sqrt(Math.max(0.2, 1.0 - (depthOffset * depthOffset) / 9.0));
                double sliceRadius = r * depthScale;

                for (int i = 0; i < segments; i++) {
                    double a1 = (i * 2.0 * Math.PI) / segments;
                    double a2 = ((i + 1) * 2.0 * Math.PI) / segments;

                    // Coronal tongues turbulence
                    double turb1 = 1.0 + 0.12 * Math.sin(a1 * 6.0 + age * 0.28 + l);
                    double turb2 = 1.0 + 0.12 * Math.sin(a2 * 6.0 + age * 0.28 + l);

                    Vec3 p0 = sliceCenter.subtract(cam);
                    Vec3 p1 = sliceCenter.add(camRight.scale(Math.cos(a1) * sliceRadius * turb1))
                            .add(camUp.scale(Math.sin(a1) * sliceRadius * turb1)).subtract(cam);
                    Vec3 p2 = sliceCenter.add(camRight.scale(Math.cos(a2) * sliceRadius * turb2))
                            .add(camUp.scale(Math.sin(a2) * sliceRadius * turb2)).subtract(cam);

                    quad(out, matrix, p0, p1, p2, p0, red, green, blue, alpha);
                }
            }
        }
    }

    private static void renderAbsorptionStreamers(VertexConsumer out, Matrix4f matrix, Vec3 cam, Vec3 sunCenter,
                                                 Vec3 bladeHand, Vec3 bladeTip, Vec3 forward, Vec3 side,
                                                 double age, double abs, float sign) {
        int streamers = 7;
        for (int s = 0; s < streamers; s++) {
            double angle = (s * 2.0 * Math.PI) / streamers + age * 0.08;
            double sourceRadius = 8.0 * (1.0 - abs * 0.5);
            Vec3 source = sunCenter.add(side.scale(Math.cos(angle) * sourceRadius))
                    .add(0, Math.sin(angle) * sourceRadius * 0.8, 0);

            // Streamer curves from sun to the sword blade (hand to tip)
            double targetFraction = (double) s / (streamers - 1);
            Vec3 target = bladeHand.lerp(bladeTip, targetFraction);

            int steps = 14;
            Vec3 prev = source;
            for (int step = 1; step <= steps; step++) {
                double t = (double) step / steps;
                Vec3 current = curvePoint(source, target, forward, side, t, age, s);

                // Ribbon billboard
                Vec3 dir = current.subtract(prev).normalize();
                Vec3 norm = new Vec3(-dir.z, dir.y, dir.x).normalize();
                double width = (0.28 + 0.22 * (1.0 - t)) * abs;

                Vec3 a = prev.add(norm.scale(width)).subtract(cam);
                Vec3 b = prev.subtract(norm.scale(width)).subtract(cam);
                Vec3 c = current.subtract(norm.scale(width)).subtract(cam);
                Vec3 d = current.add(norm.scale(width)).subtract(cam);

                int red = 255;
                int green = (int) Mth.lerp(t, 220, 90);
                int blue = (int) Mth.lerp(t, 40, 0);
                int alpha = (int) (160 * abs * Math.sin(t * Math.PI));

                quad(out, matrix, a, b, c, d, red, green, blue, alpha);
                prev = current;
            }
        }
    }

    private static Vec3 curvePoint(Vec3 start, Vec3 end, Vec3 forward, Vec3 side, double t, double age, int seed) {
        Vec3 linear = start.lerp(end, t);
        double arcHeight = Math.sin(t * Math.PI) * 2.8;
        double wave = Math.sin(t * Math.PI * 3.0 + age * 0.4 + seed * 1.7) * 0.6;
        return linear.add(forward.scale(arcHeight * 0.5)).add(side.scale(wave)).add(0, arcHeight * 0.7, 0);
    }

    private static void renderCarbonizedBlade(VertexConsumer out, Matrix4f matrix, Vec3 cam, Vec3 hand, Vec3 tip, Vec3 side, float carb) {
        Vec3 bladeDir = tip.subtract(hand);
        double len = bladeDir.length();
        if (len < 0.05) return;
        Vec3 dirNorm = bladeDir.scale(1.0 / len);
        Vec3 edgeNorm = side.cross(dirNorm).normalize();
        double bladeWidth = 0.045;

        // Black / dark carbonized core overlay on blade
        Vec3 h1 = hand.add(edgeNorm.scale(bladeWidth)).subtract(cam);
        Vec3 h2 = hand.subtract(edgeNorm.scale(bladeWidth)).subtract(cam);
        Vec3 t1 = tip.add(edgeNorm.scale(bladeWidth)).subtract(cam);
        Vec3 t2 = tip.subtract(edgeNorm.scale(bladeWidth)).subtract(cam);

        int blackAlpha = (int) (230 * carb);
        quad(out, matrix, h1, h2, t2, t1, 15, 12, 10, blackAlpha);

        // Thin glowing ember hairline along the edge
        double edgeOffset = bladeWidth * 0.6;
        Vec3 eh1 = hand.add(edgeNorm.scale(edgeOffset)).subtract(cam);
        Vec3 eh2 = hand.subtract(cam);
        Vec3 et1 = tip.add(edgeNorm.scale(edgeOffset)).subtract(cam);
        Vec3 et2 = tip.subtract(cam);

        int emberAlpha = (int) (140 * carb * 0.7);
        quad(out, matrix, eh1, eh2, et2, et1, 230, 80, 10, emberAlpha);
    }

    private static void emitBudgetedParticles(Minecraft mc, Vec3 sunCenter, Vec3 bladeHand, Vec3 bladeTip,
                                             double radius, double age, double abs, float carb, float sign) {
        if (mc.level == null || mc.isPaused()) return;

        // Sun corona embers drifting upwards (ticks 0..30)
        if (radius > 2.0 && age < 30) {
            for (int i = 0; i < 4; i++) {
                double a = RNG.nextDouble() * Math.PI * 2.0;
                double r = radius * (0.8 + 0.2 * RNG.nextDouble());
                mc.level.addParticle(ParticleTypes.FLAME,
                        sunCenter.x + Math.cos(a) * r,
                        sunCenter.y + Math.sin(a) * r * 0.8,
                        sunCenter.z + (RNG.nextDouble() - 0.5) * 2.0,
                        (RNG.nextDouble() - 0.5) * 0.05, 0.08, (RNG.nextDouble() - 0.5) * 0.05);
            }
        }

        // Absorption sparks into the blade (ticks 30..52)
        if (abs > 0.1) {
            for (int i = 0; i < 3; i++) {
                double t = RNG.nextDouble();
                Vec3 p = bladeHand.lerp(bladeTip, t);
                mc.level.addParticle(ParticleTypes.SMALL_FLAME,
                        p.x + (RNG.nextDouble() - 0.5) * 0.1,
                        p.y + (RNG.nextDouble() - 0.5) * 0.1,
                        p.z + (RNG.nextDouble() - 0.5) * 0.1,
                        0.0, 0.02, 0.0);
            }
            // Rising dust/smoke
            Vec3 tip = bladeTip;
            mc.level.addParticle(ParticleTypes.SMOKE,
                    tip.x, tip.y + 0.1, tip.z,
                    0.0, 0.04, 0.0);
        }

        // Carbonized blade thin smoke wisps (ticks 52..124)
        if (carb > 0.2F && age >= 52 && age < 124) {
            if (RNG.nextFloat() < 0.65F) {
                double t = RNG.nextDouble();
                Vec3 p = bladeHand.lerp(bladeTip, t);
                mc.level.addParticle(ParticleTypes.SMOKE,
                        p.x + (RNG.nextDouble() - 0.5) * 0.06,
                        p.y + (RNG.nextDouble() - 0.5) * 0.06,
                        p.z + (RNG.nextDouble() - 0.5) * 0.06,
                        (RNG.nextDouble() - 0.5) * 0.01, 0.025, (RNG.nextDouble() - 0.5) * 0.01);
            }
        }
    }

    private static void quad(VertexConsumer out, Matrix4f matrix, Vec3 a, Vec3 b, Vec3 c, Vec3 d, int r, int g, int bl, int al) {
        out.vertex(matrix, (float) a.x, (float) a.y, (float) a.z).color(r, g, bl, al).endVertex();
        out.vertex(matrix, (float) b.x, (float) b.y, (float) b.z).color(r, g, bl, al).endVertex();
        out.vertex(matrix, (float) c.x, (float) c.y, (float) c.z).color(r, g, bl, al).endVertex();
        out.vertex(matrix, (float) d.x, (float) d.y, (float) d.z).color(r, g, bl, al).endVertex();
    }
}
