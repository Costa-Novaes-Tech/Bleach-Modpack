package com.bleachmod.client.model;

import com.bleachmod.common.visual.RyujinBankaiTimeline;
import com.bleachmod.common.visual.RyujinBankaiPresentation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

/**
 * Animated vanilla flame sprites in a bounded 3D fire volume.
 * Provides the monumental scenic sun, solar ground base around the feet,
 * helical heat vortex around the wielder, and progressive absorption onto the katana.
 *
 * Strict particle and card budget: emission is driven by client game ticks, never by render FPS.
 */
public final class RyujinSunVisual {
    public static final int MAX_FIRE_CARDS = 288, MAX_PARTICLES_PER_SCENE_TICK = 12;

    private record Flame(Vec3 at, double width, double height, int alpha, boolean alternate) { }

    private RyujinSunVisual() { }

    private static double hash(int i) {
        return (Math.sin(i * 127.1 + 311.7) * 43758.5453) % 1 * 0.5 + 0.5;
    }

    public static void render(PoseStack pose, Vec3 camera, Vec3 anchor, Vec3 forward, double age,
                              RyujinBankaiPresentation.Blade blade) {
        var mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null || camera.distanceToSqr(anchor) > 128 * 128) return;

        Vec3 side = new Vec3(forward.z, 0, -forward.x);
        Vec3 center = RyujinBankaiPresentation.sunCenter(anchor, forward);
        double radius = RyujinBankaiTimeline.sunRadius(age);
        double absorption = RyujinBankaiTimeline.absorption(age);

        var flames = new ArrayList<Flame>(MAX_FIRE_CARDS);

        // 1. Scenic Sun Disk & Corona (behind the player)
        if (radius > 0.08) {
            // Golden-angle distribution with depth
            for (int i = 0; i < 112; i++) {
                double a = i * 2.3999632297;
                double r = Math.sqrt((i + 0.5) / 112.0) * radius * 0.88;
                double drift = Math.sin(age * 0.19 + i * 1.7) * radius * 0.025;
                Vec3 at = center.add(side.scale(Math.cos(a) * r + drift))
                        .add(0, Math.sin(a) * r * 0.85 + Math.sin(age * 0.23 + i) * radius * 0.035, 0)
                        .add(forward.scale((hash(i) - 0.5) * radius * 0.65));
                flames.add(new Flame(at, radius * (0.22 + hash(i + 3) * 0.10), radius * (0.34 + hash(i + 9) * 0.14), 225, i % 2 == 0));
            }
            // Rising tongues around the corona
            for (int i = 0; i < 20; i++) {
                double a = i * Math.PI * 2 / 20;
                double r = radius * (0.82 + 0.06 * Math.sin(age * 0.21 + i));
                Vec3 at = center.add(side.scale(Math.cos(a) * r)).add(0, Math.sin(a) * r * 0.85, 0)
                        .add(forward.scale(Math.sin(i * 1.9) * radius * 0.18));
                flames.add(new Flame(at, radius * 0.20, radius * (0.38 + 0.12 * hash(i)), 205, i % 2 == 0));
            }
        }

        // 2. Solar Ground Base (Chamas sob os pés e ao redor da base)
        // Active from beat 0 to 52 (0s to 7s), fully extinguishes on absorption finish
        double baseIntensity = 0.0;
        if (age < 12) {
            baseIntensity = RyujinBankaiTimeline.progress(age, 0, 12);
        } else if (age < 30) {
            baseIntensity = 1.0 + Math.sin(age * 0.35) * 0.06;
        } else if (age < 52) {
            baseIntensity = 1.0 - RyujinBankaiTimeline.progress(age, 30, 52);
        }

        if (baseIntensity > 0.02) {
            double baseRadius = baseIntensity * 3.0;
            for (int i = 0; i < 32; i++) {
                double theta = i * Math.PI * 2 / 32;
                double cos = Math.cos(theta); // projection along forward
                double sin = Math.sin(theta); // projection along side
                double r = baseRadius * (0.45 + 0.55 * hash(i * 3 + 1));
                // Maintain foot support legibility: lower flame height in front, higher on sides and rear
                boolean isFront = cos > 0.15;
                double flameH = baseIntensity * (isFront ? 0.38 : 0.82) * (0.8 + 0.4 * hash(i + 5));
                double flameW = baseIntensity * 0.42 * (0.75 + 0.35 * hash(i + 2));
                double yDrift = 0.05 + Math.sin(age * 0.25 + i * 1.3) * 0.03;
                Vec3 at = anchor.add(side.scale(sin * r))
                        .add(forward.scale(cos * r))
                        .add(0, yDrift, 0);
                int alpha = (int) (215 * Math.min(1.0, baseIntensity) * (isFront ? 0.72 : 1.0));
                flames.add(new Flame(at, flameW, flameH, alpha, i % 2 == 0));
            }
        }

        // 3. Helical Heat Vortex (Turbilhão de calor em faixas helicoidais largas ao redor do corpo)
        // Begins during ignition, reaches full swirl in sustained sun, contracts into blade during sealing
        double vortexIntensity = 0.0;
        if (age < 12) {
            vortexIntensity = RyujinBankaiTimeline.progress(age, 2, 12);
        } else if (age < 30) {
            vortexIntensity = 1.0;
        } else if (age < 52) {
            vortexIntensity = 1.0 - RyujinBankaiTimeline.progress(age, 30, 52);
        }

        if (vortexIntensity > 0.02) {
            double absorbP = (age >= 30 && age <= 52) ? RyujinBankaiTimeline.progress(age, 30, 52) : 0.0;
            Vec3 bladeTarget = blade.hand().lerp(blade.tip(), 0.5);

            for (int strand = 0; strand < 2; strand++) {
                double strandOffset = strand * Math.PI;
                for (int step = 0; step < 14; step++) {
                    double frac = (step + ((age * 0.16 + strand * 0.5) % 1.0)) / 14.0;
                    double y = 0.35 + frac * 3.6;
                    double phi = strandOffset + frac * 2.6 * Math.PI + age * 0.22;
                    // Wide radius (2.3m to 2.7m) with clear central cavity; contracts towards blade on sealing
                    double r = (2.3 + 0.4 * Math.sin(frac * Math.PI)) * (1.0 - absorbP * 0.75);
                    Vec3 centerAxis = anchor.lerp(bladeTarget, absorbP * (1.0 - frac * 0.4));
                    Vec3 at = centerAxis.add(side.scale(Math.cos(phi) * r))
                            .add(forward.scale(Math.sin(phi) * r))
                            .add(0, y, 0);
                    double cardW = 0.30 * vortexIntensity;
                    double cardH = 0.52 * vortexIntensity;
                    int alpha = (int) (180 * vortexIntensity * Math.sin(frac * Math.PI));
                    flames.add(new Flame(at, cardW, cardH, alpha, (strand + step) % 2 == 0));
                }
            }
        }

        // 4. Flame Streams Converging onto Katana (during absorption 4.5s–7s)
        if (absorption > 0.01) {
            for (int stream = 0; stream < 4; stream++) {
                double a = stream * Math.PI * 2 / 4;
                Vec3 from = center.add(side.scale(Math.cos(a) * Math.max(0.2, radius * 0.85)))
                        .add(0, Math.sin(a) * Math.max(0.2, radius * 0.85), 0);
                Vec3 end = blade.hand().lerp(blade.tip(), 0.25 + 0.18 * stream);
                for (int bead = 0; bead < 14; bead++) {
                    double t = (bead + ((age * 0.23 + stream * 0.17) % 1.0)) / 14.0;
                    Vec3 at = from.lerp(end, t).add(side.scale(Math.sin(t * Math.PI) * Math.cos(a) * 1.6))
                            .add(0, Math.sin(t * Math.PI) * 1.6, 0);
                    double size = (0.22 + 0.75 * (1.0 - t)) * absorption;
                    flames.add(new Flame(at, size, size * 2.6, (int) (230 * absorption), bead % 2 == 0));
                }
            }
        }

        // Back-to-front sorting avoids opaque-looking intersecting sprite planes
        flames.sort(Comparator.comparingDouble((Flame f) -> f.at.distanceToSqr(camera)).reversed());

        var type = RenderType.entityTranslucentEmissive(InventoryMenu.BLOCK_ATLAS);
        var buffers = mc.renderBuffers().bufferSource();
        var out = buffers.getBuffer(type);
        var rotation = mc.gameRenderer.getMainCamera().rotation();
        var rv = new org.joml.Vector3f(1, 0, 0).rotate(rotation);
        var uv = new org.joml.Vector3f(0, 1, 0).rotate(rotation);
        Vec3 right = new Vec3(rv.x, rv.y, rv.z), up = new Vec3(uv.x, uv.y, uv.z);

        for (var fire : flames) {
            sprite(out, pose.last().pose(), camera, fire, right, up,
                    fire.alternate ? ModelBakery.FIRE_1.sprite() : ModelBakery.FIRE_0.sprite());
        }
        buffers.endBatch(type);
    }

    private static void sprite(VertexConsumer out, Matrix4f matrix, Vec3 cam, Flame f, Vec3 right, Vec3 up, TextureAtlasSprite tex) {
        Vec3 c = f.at.subtract(cam), r = right.scale(f.width * 0.5), u = up.scale(f.height * 0.5);
        vertex(out, matrix, c.subtract(r).subtract(u), tex.getU0(), tex.getV1(), f.alpha);
        vertex(out, matrix, c.add(r).subtract(u), tex.getU1(), tex.getV1(), f.alpha);
        vertex(out, matrix, c.add(r).add(u), tex.getU1(), tex.getV0(), f.alpha);
        vertex(out, matrix, c.subtract(r).add(u), tex.getU0(), tex.getV0(), f.alpha);
    }

    private static void vertex(VertexConsumer out, Matrix4f matrix, Vec3 p, float u, float v, int alpha) {
        out.vertex(matrix, (float) p.x, (float) p.y, (float) p.z).color(255, 255, 255, alpha).uv(u, v)
                .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT).normal(0, 1, 0).endVertex();
    }

    /**
     * Emits client-side smoke, ash, and flame particles according to the authored beat clock.
     * Enforces the per-tick particle budget and Minecraft's particle setting.
     *
     * 0-2s   (beats 0..12):   Ground solar base flames & ascending embers around feet.
     * 2-4.5s (beats 12..30):  Helical heat swirl of smoke and embers around the body.
     * 4.5-7s (beats 30..52):  Swirl and embers converge into the katana blade.
     * 7-8s   (beats 52..60):  Fire volume drops; fine smoke and ash wisps on carbonized blade.
     * 8-10.5s(beats 60..112): Slow ash drift; smoke dissipates; face, blade and subtitle clear.
     * 10.5-12s(beats 112..140):Emissions shut down; remaining particles fade before combat handoff.
     */
    public static int emit(Vec3 anchor, Vec3 forward, double age, RyujinBankaiPresentation.Blade blade, int allowance) {
        var mc = Minecraft.getInstance();
        if (mc.level == null || mc.isPaused() || mc.player == null || mc.player.distanceToSqr(anchor) > 96 * 96) return 0;

        int limit = Math.min(MAX_PARTICLES_PER_SCENE_TICK, allowance);
        if (mc.options.particles().get() == net.minecraft.client.ParticleStatus.MINIMAL) limit = Math.min(3, limit);
        else if (mc.options.particles().get() == net.minecraft.client.ParticleStatus.DECREASED) limit = Math.min(6, limit);
        if (limit <= 0) return 0;

        // 10.5-12s: Emitters shut down cleanly so the air is clear at return
        if (age >= 112) return 0;

        Random rng = new Random(Double.doubleToLongBits(anchor.x + anchor.z * 31) + (long) age * 73);
        double radius = RyujinBankaiTimeline.sunRadius(age);
        Vec3 side = new Vec3(forward.z, 0, -forward.x);
        Vec3 center = RyujinBankaiPresentation.sunCenter(anchor, forward);
        int n = 0;

        for (int i = 0; i < limit; i++) {
            Vec3 p, velocity;
            net.minecraft.core.particles.SimpleParticleType particle;

            if (age < 12) {
                // 0-2s (Ignição): Chamas e brasas ao redor dos pés, apoio visível, sem parede diante do rosto
                if (i % 2 == 0) {
                    double a = rng.nextDouble() * Math.PI * 2;
                    double cos = Math.cos(a);
                    double r = 0.8 + rng.nextDouble() * 1.8 * (age / 12.0);
                    // Avoid spawning right in front of player's face
                    boolean isFront = cos > 0.3;
                    p = anchor.add(side.scale(Math.sin(a) * r))
                            .add(forward.scale(cos * r))
                            .add(0, 0.05 + (isFront ? 0.08 : 0.25) * rng.nextDouble(), 0);
                    particle = (i % 4 == 0) ? ParticleTypes.LAVA : ParticleTypes.FLAME;
                    velocity = new Vec3(0, 0.04 + rng.nextDouble() * 0.05, 0);
                } else {
                    // Scenic sun igniting behind
                    p = center.add(side.scale((rng.nextDouble() - 0.5) * radius * 0.8))
                            .add(0, (rng.nextDouble() - 0.5) * radius * 0.8, 0);
                    particle = (i % 3 == 0) ? ParticleTypes.LARGE_SMOKE : ParticleTypes.FLAME;
                    velocity = new Vec3(0, 0.06 + rng.nextDouble() * 0.04, 0);
                }
            } else if (age < 30) {
                // 2-4.5s (Sol pleno): Turbilhão helicoidal largo de calor e fumaça ao redor do corpo
                if (i < limit * 2 / 3) {
                    double a = rng.nextDouble() * Math.PI * 2;
                    double h = 0.3 + rng.nextDouble() * 3.4;
                    double r = 2.1 + rng.nextDouble() * 0.7; // Wide swirl with space around actor
                    p = anchor.add(side.scale(Math.cos(a) * r))
                            .add(forward.scale(Math.sin(a) * r))
                            .add(0, h, 0);
                    // Tangential swirl + upward heat velocity
                    velocity = side.scale(-Math.sin(a) * 0.06)
                            .add(forward.scale(Math.cos(a) * 0.06))
                            .add(0, 0.065 + rng.nextDouble() * 0.035, 0);
                    particle = (i % 3 == 0) ? ParticleTypes.SMOKE : (i % 3 == 1 ? ParticleTypes.ASH : ParticleTypes.FLAME);
                } else {
                    // Monumental sun behind
                    p = center.add(side.scale((rng.nextDouble() - 0.5) * radius * 0.9))
                            .add(0, (rng.nextDouble() - 0.5) * radius * 0.9, 0);
                    particle = ParticleTypes.FLAME;
                    velocity = new Vec3(0, 0.07 + rng.nextDouble() * 0.05, 0);
                }
            } else if (age < 52) {
                // 4.5-7s (Selamento): O turbilhão e brasas se contraem e convergem para a espada
                Vec3 target = blade.hand().lerp(blade.tip(), 0.2 + rng.nextDouble() * 0.7);
                double contract = 1.0 - RyujinBankaiTimeline.progress(age, 30, 52);
                double a = rng.nextDouble() * Math.PI * 2;
                double r = (1.6 + rng.nextDouble() * 1.0) * contract;
                double h = 0.2 + rng.nextDouble() * 2.2;
                p = anchor.add(side.scale(Math.cos(a) * r))
                        .add(forward.scale(Math.sin(a) * r))
                        .add(0, h, 0);
                Vec3 dir = target.subtract(p);
                double dist = Math.max(0.1, dir.length());
                velocity = dir.scale(Math.min(0.24, 0.10 + 0.08 / dist)).add(0, 0.025, 0);
                particle = (i % 2 == 0) ? ParticleTypes.FLAME : ParticleTypes.SMOKE;
            } else if (age < 60) {
                // 7-8s (Lâmina carbonizada): Volume reduzido, fios de fumaça e cinzas sobre a lâmina
                if (i >= 3) break;
                p = blade.hand().lerp(blade.tip(), rng.nextDouble());
                particle = (i == 0) ? ParticleTypes.SMOKE : ParticleTypes.ASH;
                velocity = new Vec3(0, 0.016 + rng.nextDouble() * 0.014, 0);
            } else {
                // 8-10.5s (Nome da Bankai): Apenas cinzas flutuando muito lentamente no ar, rosto e legenda desobstruídos
                if (i >= 2) break;
                p = anchor.add(side.scale((rng.nextDouble() - 0.5) * 4.0))
                        .add(forward.scale((rng.nextDouble() - 0.5) * 4.0))
                        .add(0, 0.5 + rng.nextDouble() * 2.2, 0);
                particle = ParticleTypes.ASH;
                velocity = new Vec3((rng.nextDouble() - 0.5) * 0.008, -0.004 + rng.nextDouble() * 0.008, (rng.nextDouble() - 0.5) * 0.008);
            }

            mc.level.addParticle(particle, p.x, p.y, p.z, velocity.x, velocity.y, velocity.z);
            n++;
        }

        return n;
    }
}
