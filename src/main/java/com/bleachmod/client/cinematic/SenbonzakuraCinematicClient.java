package com.bleachmod.client.cinematic;

import com.bleachmod.Reference;
import com.bleachmod.common.visual.SenbonGeometry;
import com.bleachmod.init.ModParticles;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Camera;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Client-side camera director, monumental blade renderer, sinking katana simulator,
 * and petal gale simulator for Byakuya Kuchiki's Bankai: Senbonzakura Kageyoshi.
 * Total presentation duration: 290 ticks (14.5 seconds).
 */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT)
public final class SenbonzakuraCinematicClient {
    public static final int DURATION = com.bleachmod.common.technique.SenbonzakuraCinematicService.DURATION;

    private static final class Types extends RenderType {
        private Types() {
            super("senbon_unused", DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS, 4096, false, true, () -> {}, () -> {});
        }
        static final RenderType GLOW = create("senbon_cinematic_glow", DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS, 4096, false, true,
                CompositeState.builder().setShaderState(RENDERTYPE_LIGHTNING_SHADER).setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                        .setCullState(NO_CULL).setWriteMaskState(COLOR_WRITE).setOutputState(PARTICLES_TARGET).createCompositeState(false));
    }

    private record Scene(long start, Vec3 anchor, Vec3 forward, float yaw) { }

    private static final Map<Integer, Scene> SCENES = new HashMap<>();
    private static net.minecraft.client.multiplayer.ClientLevel world;
    private static CameraType previousCameraType;
    private static boolean cameraActive = false;
    private static Method cameraPositionMethod;

    private SenbonzakuraCinematicClient() { }

    public static void init() {
        dev.kosmx.playerAnim.minecraftApi.PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(
                Reference.id("senbonzakura_cinematic"), 4500, Body::new);
    }

    public static boolean active(int id) {
        return world == Minecraft.getInstance().level && SCENES.containsKey(id);
    }

    public static void accept(int id, CompoundTag tag) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;
        if (world != mc.level) {
            SenbonzakuraEntranceAudio.clear();
            SCENES.clear();
            world = mc.level;
        }

        int elapsed = tag.getInt("elapsed");
        int duration = tag.getInt("duration");
        if (duration <= 0 || elapsed < 0 || elapsed >= duration) {
            SCENES.remove(id);
            SenbonzakuraEntranceAudio.stop(id);
            if (mc.player != null && mc.player.getId() == id && cameraActive) {
                restoreCamera(mc);
            }
            return;
        }

        Vec3 anchor = new Vec3(tag.getDouble("x"), tag.getDouble("y"), tag.getDouble("z"));
        float yaw = tag.getFloat("yaw");
        Vec3 forward = Vec3.directionFromRotation(0, yaw);
        SCENES.put(id, new Scene(mc.level.getGameTime() - elapsed, anchor, forward, yaw));
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onComputeCameraAngles(ViewportEvent.ComputeCameraAngles e) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) return;

        Scene scene = SCENES.get(mc.player.getId());
        if (scene == null) {
            if (cameraActive) restoreCamera(mc);
            return;
        }

        long age = mc.level.getGameTime() - scene.start;
        if (age < 0 || age >= DURATION) {
            if (cameraActive) restoreCamera(mc);
            return;
        }

        if (!cameraActive) {
            previousCameraType = mc.options.getCameraType();
            mc.options.setCameraType(CameraType.THIRD_PERSON_BACK);
            cameraActive = true;
        }

        Vec3 forward = scene.forward;
        Vec3 left = SenbonGeometry.left(forward);
        Vec3 anchor = scene.anchor;

        Vec3 eye;
        Vec3 focus;

        if (age < 10) {
            // Plano I: Preparação serena (0.0..0.5s) - Enquadramento elegante do ritual
            eye = anchor.add(forward.scale(1.9)).add(left.scale(0.75)).add(0, 1.35, 0);
            focus = anchor.add(0, 1.05, 0);
        } else if (age < 76) {
            // Plano II: A Queda & Afundamento (0.5..3.8s) - Acompanha a katana caindo e afundando no espelho d'água
            float t = (age - 10.0F) / 66.0F;
            float smooth = t * t * (3.0F - 2.0F * t);
            eye = anchor.add(forward.scale(Mth.lerp(smooth, 1.9, 2.7))).add(left.scale(Mth.lerp(smooth, 0.75, 1.2))).add(0, Mth.lerp(smooth, 1.35, 0.55), 0);
            focus = anchor.add(forward.scale(0.35 * (1.0F - smooth))).add(0, Mth.lerp(smooth, 0.95, 0.05), 0);
        } else if (age < 160) {
            // Plano III: O Corredor de Lâminas Monumentais (3.8..8.0s) - Contrapicado dramático revelando a escala monumental
            float t = (age - 76.0F) / 84.0F;
            float smooth = t * t * (3.0F - 2.0F * t);
            eye = anchor.add(forward.scale(Mth.lerp(smooth, 3.2, 7.8))).add(left.scale(Math.sin(smooth * Math.PI) * 0.5)).add(0, 0.65, 0);
            focus = anchor.add(0, Mth.lerp(smooth, 4.0, 14.5), 0).add(forward.scale(Mth.lerp(smooth, 0.0, -3.5)));
        } else if (age < 228) {
            // Plano IV: O Chamado Soberano ("Senbonzakura Kageyoshi") (8.0..11.4s) - Byakuya ao centro do corredor de luz
            float t = (age - 160.0F) / 68.0F;
            float smooth = t * t * (3.0F - 2.0F * t);
            eye = anchor.add(forward.scale(Mth.lerp(smooth, 5.8, 4.8))).add(left.scale(Mth.lerp(smooth, 1.2, 0.0))).add(0, Mth.lerp(smooth, 1.45, 1.75), 0);
            focus = anchor.add(0, 1.35, 0);
        } else if (age < 266) {
            // Plano V: Dispersão Progressiva em Pétalas (11.4..13.3s) - Recuo amplo mostrando as lâminas se desfazendo
            float t = (age - 228.0F) / 38.0F;
            float smooth = t * t * (3.0F - 2.0F * t);
            eye = anchor.add(forward.scale(Mth.lerp(smooth, 4.8, 8.2))).add(left.scale(0.0)).add(0, Mth.lerp(smooth, 1.75, 3.2), 0);
            focus = anchor.add(0, 1.4, 0);
        } else {
            // Retorno Suave ao Controle do Jogador (13.3..14.5s)
            float t = (age - 266.0F) / 24.0F;
            float smooth = t * t * (3.0F - 2.0F * t);
            eye = anchor.add(forward.scale(Mth.lerp(smooth, 8.2, 4.0))).add(0, Mth.lerp(smooth, 3.2, 1.7), 0);
            focus = anchor.add(0, 1.4, 0);
        }

        applyCamera(e, eye, focus);
    }

    private static void applyCamera(ViewportEvent.ComputeCameraAngles e, Vec3 eye, Vec3 focus) {
        try {
            if (cameraPositionMethod == null) {
                cameraPositionMethod = net.minecraftforge.fml.util.ObfuscationReflectionHelper.findMethod(Camera.class, "m_90581_", Vec3.class);
            }
            cameraPositionMethod.invoke(e.getCamera(), eye);
            Vec3 delta = focus.subtract(eye);
            e.setYaw((float) Math.toDegrees(Math.atan2(-delta.x, delta.z)));
            e.setPitch((float) -Math.toDegrees(Math.atan2(delta.y, delta.horizontalDistance())));
            e.setRoll(0.0F);
        } catch (ReflectiveOperationException ignored) { }
    }

    private static void restoreCamera(Minecraft mc) {
        cameraActive = false;
        if (previousCameraType != null) {
            mc.options.setCameraType(previousCameraType);
            previousCameraType = null;
        }
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent e) {
        if (e.phase != TickEvent.Phase.END) return;
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;

        // Cleanup expired scenes & audio
        for (var it = SCENES.entrySet().iterator(); it.hasNext(); ) {
            var entry = it.next();
            if (mc.level.getGameTime() - entry.getValue().start >= DURATION) {
                SenbonzakuraEntranceAudio.stop(entry.getKey());
                it.remove();
            }
        }

        if (mc.player != null && !SCENES.containsKey(mc.player.getId()) && cameraActive) {
            restoreCamera(mc);
        }

        // Particle simulation & audio update during cinematic
        for (Map.Entry<Integer, Scene> entry : SCENES.entrySet()) {
            int id = entry.getKey();
            Scene s = entry.getValue();
            long age = mc.level.getGameTime() - s.start;
            if (age < 0 || age >= DURATION) continue;

            boolean isLocal = mc.player != null && mc.player.getId() == id;
            SenbonzakuraEntranceAudio.update(id, age, s.anchor, isLocal);

            Vec3 anchor = s.anchor;
            Vec3 forward = s.forward;
            Vec3 left = SenbonGeometry.left(forward);
            Vec3 dropPoint = anchor.add(forward.scale(0.35)).add(left.scale(0.32));

            // Phase 1: Reishi ripple rings on the ground around the sinking katana (age 30..140)
            if (age >= 30 && age < 140) {
                int count = 14;
                double rippleRadius = Math.min(5.2, (age - 30) * 0.08);
                for (int i = 0; i < count; i++) {
                    double ang = i * (Math.PI * 2.0 / count) + age * 0.04;
                    double px = dropPoint.x + Math.cos(ang) * rippleRadius;
                    double pz = dropPoint.z + Math.sin(ang) * rippleRadius;
                    mc.level.addParticle(ModParticles.SENBON_PETAL.get(), px, anchor.y + 0.04, pz, 0, 0.01, 0);
                }
            }

            // Phase 4: Massive dual cyclone vortex of petals during blade dispersion (age 228..290)
            if (age >= 228) {
                float petalProgress = (age - 228.0F) / 62.0F;
                int petalCount = 32;
                for (int side : new int[]{-1, 1}) {
                    Vec3 colCenter = anchor.add(left.scale(side * 4.5));
                    for (int i = 0; i < petalCount; i++) {
                        double ang = i * (Math.PI * 2.0 / petalCount) + age * 0.28;
                        double radius = 1.4 + Math.sin(age * 0.15 + i) * 1.1;
                        double height = (i % 8) * 2.6 + petalProgress * 8.0;
                        double vx = -Math.sin(ang) * 0.32;
                        double vz = Math.cos(ang) * 0.32;
                        mc.level.addParticle(ModParticles.SENBON_PETAL.get(),
                                colCenter.x + Math.cos(ang) * radius,
                                anchor.y + height,
                                colCenter.z + Math.sin(ang) * radius,
                                vx, 0.15, vz);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onRenderLevelStage(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_PARTICLES) return;
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null || SCENES.isEmpty()) return;

        var pose = event.getPoseStack();
        Vec3 camera = event.getCamera().getPosition();
        pose.pushPose();

        var buffers = mc.renderBuffers().bufferSource();
        var vertices = buffers.getBuffer(Types.GLOW);
        var matrix = pose.last().pose();

        for (Scene s : SCENES.values()) {
            long age = mc.level.getGameTime() - s.start;
            if (age < 0 || age >= DURATION) continue;

            Vec3 anchor = s.anchor;
            Vec3 forward = s.forward;
            Vec3 left = SenbonGeometry.left(forward);
            Vec3 dropPoint = anchor.add(forward.scale(0.35)).add(left.scale(0.32));
            double groundY = anchor.y + 0.02;

            // 1. Render Falling & Sinking Katana with physical floor clipping (age 10..76)
            if (age >= 10 && age < 76) {
                renderFallingSinkingKatana(vertices, matrix, dropPoint.subtract(camera), groundY - camera.y, (int) age, forward, left);
            }

            // 2. Render Concentric Reishi Water Ripple Discs on the ground (age 30..180)
            if (age >= 30 && age < 180) {
                renderReishiRippleDiscs(vertices, matrix, dropPoint.subtract(camera), (int) age);
            }

            // 3. Render 20 Monumental Colossal Blades (10 left, 10 right) (age 76..266)
            if (age >= 76 && age < 266) {
                renderMonumentalBladeCorridor(vertices, matrix, anchor.subtract(camera), forward, left, (int) age);
            }
        }

        buffers.endBatch(Types.GLOW);
        pose.popPose();
    }

    /**
     * Renders the falling katana physically separating from the hand, falling under gravity,
     * and progressively sinking into the spiritual water surface by clipping quads at ground level.
     */
    private static void renderFallingSinkingKatana(com.mojang.blaze3d.vertex.VertexConsumer v, org.joml.Matrix4f m, Vec3 dropOrigin, double groundRelY, int age, Vec3 forward, Vec3 left) {
        // Drop starts at hand height (1.25m above ground)
        // Katana dimensions: 1.05m total (blade 0.75m, tsuba 0.04m, tsuka hilt 0.26m)
        float initialHiltTop = 1.25F;
        float fallDist;
        if (age < 35) {
            // Freefall from hand to ground contact (ticks 10..35 = 25 ticks)
            float t = (age - 10.0F) / 25.0F;
            fallDist = t * t * 0.20F; // Tip starts at 0.20m above ground, reaches ground at tick 35
        } else {
            // Progressively sinking into the spiritual water floor (ticks 35..76 = 41 ticks)
            float t = (age - 35.0F) / 41.0F;
            fallDist = 0.20F + t * 1.05F; // Entire 1.05m sword sinks through the water plane
        }

        double hiltTopY = dropOrigin.y + (initialHiltTop - fallDist);
        double tsubaY = hiltTopY - 0.26;
        double tipY = tsubaY - 0.75;

        // If completely submerged, nothing to render
        if (hiltTopY <= groundRelY) return;

        Vec3 up = new Vec3(0, 1, 0);

        // 1. Blade Body (Silver steel with glowing hamon edge)
        if (tsubaY > groundRelY) {
            double bladeBottom = Math.max(tipY, groundRelY);
            double bladeTop = tsubaY;
            double bladeLength = bladeTop - bladeBottom;
            if (bladeLength > 0.01) {
                Vec3 bladeBase = new Vec3(dropOrigin.x, bladeBottom, dropOrigin.z);
                // Steel blade core
                quadPillar(v, m, bladeBase, up, left, forward, 0.022F, (float) bladeLength, 215, 225, 240, 245);
                // Glowing hamon cutting edge
                Vec3 edgeStart = bladeBase.add(left.scale(-0.024));
                quadRibbon(v, m, edgeStart, edgeStart.add(0, bladeLength, 0), forward.scale(0.012), 255, 175, 220, 255);
            }
        }

        // 2. Tsuba (Golden open-frame guard)
        if (tsubaY >= groundRelY && (tsubaY + 0.04) > groundRelY) {
            Vec3 tsubaPos = new Vec3(dropOrigin.x, Math.max(tsubaY, groundRelY), dropOrigin.z);
            quadRibbon(v, m, tsubaPos.add(left.scale(-0.07)), tsubaPos.add(left.scale(0.07)), forward.scale(0.05), 235, 190, 70, 255);
            quadRibbon(v, m, tsubaPos.add(forward.scale(-0.05)), tsubaPos.add(forward.scale(0.05)), left.scale(0.07), 235, 190, 70, 255);
        }

        // 3. Tsuka (Purple hilt with golden wrapping and pommel)
        if (hiltTopY > groundRelY) {
            double hiltBottom = Math.max(tsubaY + 0.04, groundRelY);
            double hiltLength = hiltTopY - hiltBottom;
            if (hiltLength > 0.01) {
                Vec3 hiltBase = new Vec3(dropOrigin.x, hiltBottom, dropOrigin.z);
                quadPillar(v, m, hiltBase, up, left, forward, 0.018F, (float) hiltLength, 95, 35, 125, 255);
                // Golden kashira pommel
                if (hiltTopY - 0.03 >= groundRelY) {
                    Vec3 pommelBase = new Vec3(dropOrigin.x, hiltTopY - 0.03, dropOrigin.z);
                    quadPillar(v, m, pommelBase, up, left, forward, 0.022F, 0.03F, 240, 195, 75, 255);
                }
            }
        }
    }

    private static void renderReishiRippleDiscs(com.mojang.blaze3d.vertex.VertexConsumer v, org.joml.Matrix4f m, Vec3 origin, int age) {
        // Multi-tiered glowing rings expanding on the floor
        double[] baseRadii = {0.8, 1.8, 3.2, 4.8, 6.5};
        float life = Mth.clamp((age - 30.0F) / 150.0F, 0.0F, 1.0F);
        float fade = (age >= 140) ? (1.0F - (age - 140.0F) / 40.0F) : 1.0F;

        for (int r = 0; r < baseRadii.length; r++) {
            double rad = baseRadii[r] * (0.4 + life * 0.9) + Math.sin(age * 0.12 + r) * 0.18;
            int alpha = (int) (200 * fade * Mth.clamp(1.0F - (float) r / 5.0F, 0.25F, 1.0F));
            drawRing(v, m, origin, rad, rad + 0.32, 255, 130, 210, alpha);
        }
    }

    private static void drawRing(com.mojang.blaze3d.vertex.VertexConsumer v, org.joml.Matrix4f m, Vec3 origin, double rInner, double rOuter, int r, int g, int b, int a) {
        int segments = 24;
        for (int i = 0; i < segments; i++) {
            double a0 = i * (Math.PI * 2.0 / segments);
            double a1 = (i + 1) * (Math.PI * 2.0 / segments);

            float x0i = (float) (origin.x + Math.cos(a0) * rInner);
            float z0i = (float) (origin.z + Math.sin(a0) * rInner);
            float x0o = (float) (origin.x + Math.cos(a0) * rOuter);
            float z0o = (float) (origin.z + Math.sin(a0) * rOuter);

            float x1i = (float) (origin.x + Math.cos(a1) * rInner);
            float z1i = (float) (origin.z + Math.sin(a1) * rInner);
            float x1o = (float) (origin.x + Math.cos(a1) * rOuter);
            float z1o = (float) (origin.z + Math.sin(a1) * rOuter);

            float y = (float) (origin.y + 0.035);
            v.vertex(m, x0i, y, z0i).color(r, g, b, a).endVertex();
            v.vertex(m, x0o, y, z0o).color(r, g, b, a).endVertex();
            v.vertex(m, x1o, y, z1o).color(r, g, b, a).endVertex();
            v.vertex(m, x1i, y, z1i).color(r, g, b, a).endVertex();
        }
    }

    /**
     * Renders the monumental corridor of 20 colossal blades (10 left, 10 right),
     * standing 22 blocks tall, spanning 35 blocks along the corridor.
     */
    private static void renderMonumentalBladeCorridor(com.mojang.blaze3d.vertex.VertexConsumer v, org.joml.Matrix4f m, Vec3 origin, Vec3 forward, Vec3 left, int age) {
        int bladeCountPerRow = 10;
        double corridorHalfWidth = 4.5;
        double bladeSpacingZ = 3.8;
        double startZ = -16.0;

        for (int side : new int[]{-1, 1}) {
            for (int i = 0; i < bladeCountPerRow; i++) {
                Vec3 bladeBase = origin.add(left.scale(side * corridorHalfWidth)).add(forward.scale(startZ + i * bladeSpacingZ));

                // Sequential emergence from back to front
                float delay = 76.0F + (bladeCountPerRow - 1 - i) * 3.2F;
                float rawGrow = Mth.clamp((age - delay) / 32.0F, 0.0F, 1.0F);
                if (rawGrow <= 0.0F) continue;

                // Smooth ease-out growth curve
                float grow = (float) Math.sin(rawGrow * Math.PI * 0.5);

                // Subtle resonant tremor during name invocation (age 160..228)
                float tremor = 0.0F;
                if (age >= 160 && age <= 228) {
                    tremor = (float) Math.sin((age * 0.75F) + i * 0.6F) * 0.06F;
                }

                // Progressive dissolution into petals from top to bottom (age 228..266)
                float dissolve = 0.0F;
                float fade = 1.0F;
                if (age >= 228) {
                    float dissT = Mth.clamp((age - 228.0F) / 35.0F, 0.0F, 1.0F);
                    dissolve = dissT;
                    fade = Mth.clamp(1.0F - (age - 228.0F) / 38.0F, 0.0F, 1.0F);
                }

                renderMonumentalBlade(v, m, bladeBase, forward, left, grow, tremor, dissolve, fade);
            }
        }
    }

    private static void renderMonumentalBlade(com.mojang.blaze3d.vertex.VertexConsumer v, org.joml.Matrix4f m, Vec3 base, Vec3 forward, Vec3 left, float grow, float tremor, float dissolve, float fade) {
        float fullHeight = 22.0F;
        float totalHeight = fullHeight * grow * (1.0F - dissolve * 0.85F);
        if (totalHeight <= 0.2F || fade <= 0.01F) return;

        Vec3 up = new Vec3(0, 1, 0);
        Vec3 bladeOrigin = base.add(left.scale(tremor));
        int alpha = (int) (255 * fade);

        // 1. Tsuka (Purple Hilt) & Pommel
        float hiltHeight = Math.min(3.2F, totalHeight * 0.16F);
        quadPillar(v, m, bladeOrigin, up, left, forward, 0.28F, hiltHeight, 85, 30, 120, alpha);

        // Kashira golden pommel at base
        quadPillar(v, m, bladeOrigin, up, left, forward, 0.32F, 0.45F, 240, 195, 75, alpha);

        // 2. Tsuba (Rectangular Open-Frame Gold Guard)
        if (totalHeight > 3.2F) {
            Vec3 tsubaPos = bladeOrigin.add(0, hiltHeight, 0);
            quadRibbon(v, m, tsubaPos.add(left.scale(-0.95)), tsubaPos.add(left.scale(0.95)), forward.scale(0.65), 245, 195, 70, alpha);
            quadRibbon(v, m, tsubaPos.add(forward.scale(-0.65)), tsubaPos.add(forward.scale(0.65)), left.scale(0.95), 245, 195, 70, alpha);
        }

        // 3. Colossal Steel Blade Body (Silver Steel with Glowing Hamon Cutting Edge)
        if (totalHeight > 3.5F) {
            Vec3 bladeStart = bladeOrigin.add(0, hiltHeight + 0.35F, 0);
            float bladeLen = totalHeight - (hiltHeight + 0.35F);

            // Steel blade core
            quadPillar(v, m, bladeStart, up, left, forward, 0.36F, bladeLen, 210, 222, 238, (int) (235 * fade));

            // Glowing pink/white cutting edge with hamon temper line
            Vec3 edgeStart = bladeStart.add(left.scale(-0.38F));
            quadRibbon(v, m, edgeStart, edgeStart.add(0, bladeLen, 0), forward.scale(0.16F), 255, 175, 220, alpha);

            // Razor shine apex (pure bright line)
            quadRibbon(v, m, edgeStart, edgeStart.add(0, bladeLen, 0), up.scale(0.06F), 255, 255, 255, alpha);

            // Angled Kissaki Tip (only if not yet dissolved away)
            if (dissolve < 0.15F) {
                Vec3 tipBase = bladeStart.add(0, bladeLen, 0);
                Vec3 tipApex = tipBase.add(0, 1.2F, 0).add(left.scale(0.18F));
                v.vertex(m, (float) (tipBase.x - left.x * 0.36), (float) tipBase.y, (float) (tipBase.z - left.z * 0.36)).color(255, 220, 240, alpha).endVertex();
                v.vertex(m, (float) (tipBase.x + left.x * 0.36), (float) tipBase.y, (float) (tipBase.z + left.z * 0.36)).color(210, 222, 238, alpha).endVertex();
                v.vertex(m, (float) tipApex.x, (float) tipApex.y, (float) tipApex.z).color(255, 255, 255, alpha).endVertex();
                v.vertex(m, (float) tipApex.x, (float) tipApex.y, (float) tipApex.z).color(255, 255, 255, alpha).endVertex();
            }
        }
    }

    private static void quadPillar(com.mojang.blaze3d.vertex.VertexConsumer v, org.joml.Matrix4f m, Vec3 base, Vec3 axis, Vec3 sideA, Vec3 sideB, float radius, float length, int red, int green, int blue, int alpha) {
        Vec3 top = base.add(axis.scale(length));
        quadRibbon(v, m, base.subtract(sideB.scale(radius)), top.subtract(sideB.scale(radius)), sideA.scale(radius), red, green, blue, alpha);
        quadRibbon(v, m, base.add(sideB.scale(radius)), top.add(sideB.scale(radius)), sideA.scale(radius), red, green, blue, alpha);
        quadRibbon(v, m, base.subtract(sideA.scale(radius)), top.subtract(sideA.scale(radius)), sideB.scale(radius), red, green, blue, alpha);
        quadRibbon(v, m, base.add(sideA.scale(radius)), top.add(sideA.scale(radius)), sideB.scale(radius), red, green, blue, alpha);
    }

    private static void quadRibbon(com.mojang.blaze3d.vertex.VertexConsumer v, org.joml.Matrix4f m, Vec3 a, Vec3 b, Vec3 halfWidth, int red, int green, int blue, int alpha) {
        v.vertex(m, (float) (a.x - halfWidth.x), (float) (a.y - halfWidth.y), (float) (a.z - halfWidth.z)).color(red, green, blue, alpha).endVertex();
        v.vertex(m, (float) (a.x + halfWidth.x), (float) (a.y + halfWidth.y), (float) (a.z + halfWidth.z)).color(red, green, blue, alpha).endVertex();
        v.vertex(m, (float) (b.x + halfWidth.x), (float) (b.y + halfWidth.y), (float) (b.z + halfWidth.z)).color(red, green, blue, alpha).endVertex();
        v.vertex(m, (float) (b.x - halfWidth.x), (float) (b.y - halfWidth.y), (float) (b.z - halfWidth.z)).color(red, green, blue, alpha).endVertex();
    }

    private static final class Body implements dev.kosmx.playerAnim.api.layered.IAnimation {
        final net.minecraft.client.player.AbstractClientPlayer player;
        Body(net.minecraft.client.player.AbstractClientPlayer p) { player = p; }
        @Override public boolean isActive() {
            return active(player.getId());
        }
        @Override public void setupAnim(float partial) { }
        @Override public dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode getFirstPersonMode(float partial) {
            return dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode.VANILLA;
        }
        @Override public dev.kosmx.playerAnim.core.util.Vec3f get3DTransform(String part, dev.kosmx.playerAnim.api.TransformType type, float partial, dev.kosmx.playerAnim.core.util.Vec3f value) {
            var s = SCENES.get(player.getId());
            if (s == null || world == null) return value;
            float age = (float) (world.getGameTime() - s.start) + partial;
            float w = com.bleachmod.common.visual.SenbonzakuraPlayerPose.weight(age, DURATION);
            if (w <= 0.0F) return value;

            float x = value.getX(), y = value.getY(), z = value.getZ();
            boolean leftMain = player.getMainArm() == net.minecraft.world.entity.HumanoidArm.LEFT;
            boolean main = part.equals(leftMain ? "leftArm" : "rightArm");
            boolean support = part.equals(leftMain ? "rightArm" : "leftArm");
            boolean item = part.equals(leftMain ? "leftItem" : "rightItem");

            if (support && !player.getOffhandItem().isEmpty()) return value;

            if (type == dev.kosmx.playerAnim.api.TransformType.BEND) {
                if (part.equals("rightLeg") || part.equals("leftLeg") || main || support) {
                    return new dev.kosmx.playerAnim.core.util.Vec3f(0, 0, 0);
                }
            }
            if (type == dev.kosmx.playerAnim.api.TransformType.POSITION && item) {
                var target = com.bleachmod.common.visual.SenbonzakuraPlayerPose.itemPosition(leftMain, age);
                return new dev.kosmx.playerAnim.core.util.Vec3f((float) Mth.lerp(w, x, target.x), (float) Mth.lerp(w, y, target.y), (float) Mth.lerp(w, z, target.z));
            }
            if (type == dev.kosmx.playerAnim.api.TransformType.ROTATION) {
                if (part.equals("rightLeg") || part.equals("leftLeg")) {
                    return new dev.kosmx.playerAnim.core.util.Vec3f((float) (x * (1 - w)), (float) (y * (1 - w)), (float) (z * (1 - w)));
                }
                if (main) {
                    var target = com.bleachmod.common.visual.SenbonzakuraPlayerPose.mainArm(leftMain, age).rotation();
                    return new dev.kosmx.playerAnim.core.util.Vec3f((float) Mth.lerp(w, x, target.x), (float) Mth.lerp(w, y, target.y), (float) Mth.lerp(w, z, target.z));
                }
                if (support) {
                    var target = com.bleachmod.common.visual.SenbonzakuraPlayerPose.supportArm(leftMain, age).rotation();
                    return new dev.kosmx.playerAnim.core.util.Vec3f((float) Mth.lerp(w, x, target.x), (float) Mth.lerp(w, y, target.y), (float) Mth.lerp(w, z, target.z));
                }
                if (item) {
                    var target = com.bleachmod.common.visual.SenbonzakuraPlayerPose.itemRotation(leftMain, age);
                    return new dev.kosmx.playerAnim.core.util.Vec3f((float) Mth.lerp(w, x, target.x), (float) Mth.lerp(w, y, target.y), (float) Mth.lerp(w, z, target.z));
                }
                if (part.equals("body") || part.equals("head")) {
                    return new dev.kosmx.playerAnim.core.util.Vec3f((float) (x * (1 - w)), (float) (y * (1 - w)), (float) (z * (1 - w)));
                }
            }
            return new dev.kosmx.playerAnim.core.util.Vec3f(x, y, z);
        }
    }
}
