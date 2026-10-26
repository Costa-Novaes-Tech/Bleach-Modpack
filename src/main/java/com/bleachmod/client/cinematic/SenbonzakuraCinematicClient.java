package com.bleachmod.client.cinematic;

import com.bleachmod.Reference;
import com.bleachmod.common.visual.BenihimeEntranceGeometry;
import com.bleachmod.common.visual.BenihimeGeometry;
import com.bleachmod.common.visual.SenbonAtmosphere;
import com.bleachmod.common.visual.SenbonCameraPath;
import com.bleachmod.common.visual.SenbonGeometry;
import com.bleachmod.common.visual.SenbonStage;
import com.bleachmod.init.ModParticles;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Camera;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Client-side camera director, dark spiritual floor & petal backdrop renderer, monumental blade renderer,
 * atmospheric controller, and cinematic subtitle presenter for Byakuya Kuchiki's Bankai: Senbonzakura Kageyoshi.
 * Total presentation duration: 290 ticks (14.5 seconds).
 */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT)
public final class SenbonzakuraCinematicClient {
    public static final int DURATION = com.bleachmod.common.technique.SenbonzakuraCinematicService.DURATION;

    private static final class Types extends RenderType {
        private Types() {
            super("senbon_unused", DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS, 4096, false, true, () -> {}, () -> {});
        }
        static final RenderType STAGE_BACKDROP = create("senbon_stage_backdrop", DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS, 65536, false, true,
                CompositeState.builder().setShaderState(RENDERTYPE_LIGHTNING_SHADER).setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                        .setCullState(NO_CULL).setWriteMaskState(COLOR_WRITE).createCompositeState(false));
        static final RenderType STAGE_FLOOR = create("senbon_stage_floor", DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS, 65536, false, true,
                CompositeState.builder().setShaderState(RENDERTYPE_LIGHTNING_SHADER).setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                        .setCullState(NO_CULL).setWriteMaskState(COLOR_WRITE).createCompositeState(false));
        static final RenderType GLOW = create("senbon_cinematic_glow", DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS, 65536, false, true,
                CompositeState.builder().setShaderState(RENDERTYPE_LIGHTNING_SHADER).setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                        .setCullState(NO_CULL).setWriteMaskState(COLOR_WRITE).setOutputState(PARTICLES_TARGET).createCompositeState(false));
    }

    private record Scene(long start, Vec3 anchor, Vec3 forward, float yaw) { }

    private static final Map<Integer, Scene> SCENES = new HashMap<>();
    private static net.minecraft.client.multiplayer.ClientLevel world;
    private static CameraType previousCameraType;
    private static boolean cameraActive = false;
    private static Method cameraPositionMethod;
    private static boolean reenteringPlayerRender = false;

    private SenbonzakuraCinematicClient() { }

    public static void init() {
        dev.kosmx.playerAnim.minecraftApi.PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(
                Reference.id("senbonzakura_cinematic"), 4500, Body::new);
    }

    public static boolean active(int id) {
        return world == Minecraft.getInstance().level && SCENES.containsKey(id);
    }

    public static boolean isCameraActive() {
        return cameraActive;
    }

    /**
     * True while the vertical cinematic katana is active or the blades are manifested (age 0..264),
     * suppressing both the held item and hip scabbard so visually only a single weapon exists.
     */
    public static boolean hideHeldWeapon(int id, float partialTick) {
        if (world != Minecraft.getInstance().level) return false;
        var s = SCENES.get(id);
        if (s == null) return false;
        return com.bleachmod.common.visual.SenbonzakuraPlayerPose.hideHeldWeapon(age(s, partialTick));
    }

    private static Vec3 dropPoint(Scene s, int entityId) {
        var mc = Minecraft.getInstance();
        boolean leftMain = false;
        if (mc.level != null && mc.level.getEntity(entityId) instanceof LivingEntity living) {
            leftMain = living.getMainArm() == HumanoidArm.LEFT;
        }
        double sideOffset = leftMain ? -SenbonStage.DROP_X : SenbonStage.DROP_X;
        Vec3 left = SenbonGeometry.left(s.forward);
        return s.anchor.add(s.forward.scale(SenbonStage.DROP_Z)).add(left.scale(sideOffset));
    }

    private static Scene local() {
        var mc = Minecraft.getInstance();
        if (mc.player == null) return null;
        return SCENES.get(mc.player.getId());
    }

    private static double age(Scene s, float partial) {
        if (world == null) return 0.0;
        return (world.getGameTime() - s.start) + partial;
    }

    public static void accept(int id, CompoundTag tag) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;
        if (world != mc.level) {
            clear();
            world = mc.level;
        }

        int elapsed = tag.getInt("elapsed");
        int duration = tag.getInt("duration");
        if (duration <= 0 || elapsed < 0 || elapsed >= duration) {
            finish(id);
            return;
        }

        Vec3 anchor = new Vec3(tag.getDouble("x"), tag.getDouble("y"), tag.getDouble("z"));
        float yaw = tag.getFloat("yaw");
        if (!Double.isFinite(anchor.lengthSqr()) || !Float.isFinite(yaw)) return;
        Vec3 forward = Vec3.directionFromRotation(0, yaw);

        boolean fresh = !SCENES.containsKey(id);
        if (fresh && SCENES.size() >= 8) {
            if (mc.player == null || id != mc.player.getId()) return;
            SCENES.keySet().stream().filter(other -> other != id).findFirst().ifPresent(SenbonzakuraCinematicClient::finish);
        }
        SCENES.put(id, new Scene(mc.level.getGameTime() - elapsed, anchor, forward, yaw));

        if (fresh && mc.player != null && mc.player.getId() == id) {
            if (previousCameraType == null) {
                previousCameraType = mc.options.getCameraType();
            }
            cameraActive = true;
            mc.options.setCameraType(CameraType.THIRD_PERSON_BACK);
        }
    }

    @SubscribeEvent
    public static void onLogout(ClientPlayerNetworkEvent.LoggingOut e) {
        clear();
    }

    @SubscribeEvent
    public static void onLevelUnload(LevelEvent.Unload e) {
        if (e.getLevel().isClientSide()) {
            clear();
        }
    }

    private static void finish(int id) {
        SCENES.remove(id);
        SenbonzakuraEntranceAudio.stop(id);
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null && mc.player.getId() == id) {
            restoreCamera(mc);
        }
    }

    private static void clear() {
        SCENES.clear();
        SenbonzakuraEntranceAudio.clear();
        restoreCamera(Minecraft.getInstance());
    }

    private static void restoreCamera(Minecraft mc) {
        if (cameraActive) {
            cameraActive = false;
            if (previousCameraType != null && mc.options != null) {
                mc.options.setCameraType(previousCameraType);
            }
            previousCameraType = null;
        }
    }

    private static float currentFov(float partialTick) {
        var s = local();
        if (s == null) return 70.0F;
        double age = age(s, partialTick);
        float pathFov = SenbonCameraPath.sample(age).fov();
        if (age >= SenbonCameraPath.RETURN_START) {
            var mc = Minecraft.getInstance();
            double blend = (age - SenbonCameraPath.RETURN_START) / (SenbonCameraPath.RETURN_SETTLE - SenbonCameraPath.RETURN_START);
            double t = SenbonCameraPath.ease(blend);
            return (float) Mth.lerp(t, pathFov, mc.options.fov().get());
        }
        return pathFov;
    }

    private static Vec3 viewDirection(float partialTick) {
        var mc = Minecraft.getInstance();
        var camera = mc.gameRenderer.getMainCamera();
        var s = local();
        if (cameraActive && s != null) {
            double age = age(s, partialTick);
            var frame = SenbonCameraPath.sample(age);
            Vec3 left = SenbonGeometry.left(s.forward);
            Vec3 focus = s.anchor.add(left.scale(frame.focus().x)).add(0, frame.focus().y, 0).add(s.forward.scale(frame.focus().z));
            return focus.subtract(camera.getPosition()).normalize();
        }
        return new Vec3(camera.getLookVector());
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onComputeCameraAngles(ViewportEvent.ComputeCameraAngles e) {
        if (!cameraActive || world == null) return;
        Minecraft mc = Minecraft.getInstance();
        var s = local();
        if (s == null || mc.player == null) {
            restoreCamera(mc);
            return;
        }

        float partial = (float) e.getPartialTick();
        double age = age(s, partial);
        if (age < 0.0 || age >= DURATION) {
            restoreCamera(mc);
            return;
        }

        var frame = SenbonCameraPath.sample(age);
        Vec3 forward = s.forward;
        Vec3 left = SenbonGeometry.left(forward);

        // Convert camera coordinates from relative space (X lateral, Y vertical, Z forward) to world coordinates
        Vec3 eye = s.anchor.add(left.scale(frame.eye().x)).add(0, frame.eye().y, 0).add(forward.scale(frame.eye().z));
        Vec3 focus = s.anchor.add(left.scale(frame.focus().x)).add(0, frame.focus().y, 0).add(forward.scale(frame.focus().z));

        // Smooth return to the player's actual previous perspective (settling by RETURN_SETTLE = 286 so zero residual jump occurs at 290)
        if (age >= SenbonCameraPath.RETURN_START) {
            double blend = (age - SenbonCameraPath.RETURN_START) / (SenbonCameraPath.RETURN_SETTLE - SenbonCameraPath.RETURN_START);
            double t = SenbonCameraPath.ease(blend);
            Vec3 normalEye = mc.player.getEyePosition(partial);
            Vec3 look = mc.player.getViewVector(partial);
            CameraType mode = previousCameraType == null ? CameraType.FIRST_PERSON : previousCameraType;
            Vec3 end = normalEye;
            if (mode == CameraType.THIRD_PERSON_BACK) {
                end = normalEye.subtract(look.scale(4.0));
            } else if (mode == CameraType.THIRD_PERSON_FRONT) {
                end = normalEye.add(look.scale(4.0));
            }
            if (mode != CameraType.FIRST_PERSON) {
                var targetHit = world.clip(new ClipContext(normalEye, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, mc.player));
                if (targetHit.getType() != HitResult.Type.MISS) {
                    end = targetHit.getLocation().add(normalEye.subtract(end).normalize().scale(0.2));
                }
            }
            Vec3 targetDirection = (mode == CameraType.THIRD_PERSON_FRONT) ? look.scale(-1.0) : look;
            Vec3 direction = SenbonCameraPath.blendDirection(focus.subtract(eye), targetDirection, t);
            eye = (mode == CameraType.THIRD_PERSON_FRONT)
                    ? eye.lerp(end, t)
                    : SenbonCameraPath.returnEye(eye, normalEye, end, look, t);
            focus = eye.add(direction.scale(8.0));
        }

        // Block collision check preventing camera clipping through walls
        Vec3 safeOrigin = mc.player.getEyePosition(partial);
        var hit = world.clip(new ClipContext(safeOrigin, eye, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, mc.player));
        if (hit.getType() != HitResult.Type.MISS) {
            eye = hit.getLocation().add(safeOrigin.subtract(eye).normalize().scale(0.2));
        }

        if (eye.distanceToSqr(focus) < 0.15) {
            restoreCamera(mc);
            return;
        }

        try {
            if (cameraPositionMethod == null) {
                cameraPositionMethod = net.minecraftforge.fml.util.ObfuscationReflectionHelper.findMethod(Camera.class, "m_90581_", Vec3.class);
            }
            cameraPositionMethod.invoke(e.getCamera(), eye);
            Vec3 delta = focus.subtract(eye);
            e.setYaw((float) Math.toDegrees(Math.atan2(-delta.x, delta.z)));
            e.setPitch((float) -Math.toDegrees(Math.atan2(delta.y, delta.horizontalDistance())));
            e.setRoll(0.0F);
            if (age >= SenbonCameraPath.RETURN_SETTLE) {
                restoreCamera(mc);
            }
        } catch (ReflectiveOperationException ignored) {
            restoreCamera(mc);
        }
    }

    @SubscribeEvent
    public static void onComputeFov(ViewportEvent.ComputeFov e) {
        if (!cameraActive || world == null) return;
        var s = local();
        if (s == null) return;
        float partial = (float) e.getPartialTick();
        double age = age(s, partial);
        if (age >= 0.0 && age < DURATION && e.usedConfiguredFov()) {
            float pathFov = SenbonCameraPath.sample(age).fov();
            if (age >= SenbonCameraPath.RETURN_START) {
                double blend = (age - SenbonCameraPath.RETURN_START) / (SenbonCameraPath.RETURN_SETTLE - SenbonCameraPath.RETURN_START);
                double t = SenbonCameraPath.ease(blend);
                // Interpolate directly to e.getFOV() (which includes speed/Bankai attribute FOV modifiers) so zero FOV jump occurs at t=290
                e.setFOV(Mth.lerp(t, pathFov, e.getFOV()));
            } else {
                e.setFOV(pathFov);
            }
        }
    }

    @SubscribeEvent
    public static void onRenderHand(RenderHandEvent e) {
        var mc = Minecraft.getInstance();
        if (!cameraActive || mc.player == null || e.getHand() != InteractionHand.MAIN_HAND) return;
        if (hideHeldWeapon(mc.player.getId(), e.getPartialTick())) {
            e.setCanceled(true);
        }
    }

    /**
     * 1) Prevents viewing inside the player's head on first-person camera return.
     * 2) Provides subtle spiritual key-light readability on the caster's face and torso during dark/night scenes
     *    without brightening the surrounding stage or altering world blocks.
     */
    @SubscribeEvent
    public static void onRenderPlayerPre(RenderPlayerEvent.Pre event) {
        var mc = Minecraft.getInstance();
        var s = local();
        if (cameraActive && s != null && event.getEntity() == mc.player && previousCameraType == CameraType.FIRST_PERSON) {
            double age = age(s, event.getPartialTick());
            if (age >= SenbonCameraPath.RETURN_START && mc.gameRenderer.getMainCamera().getPosition().distanceToSqr(mc.player.getEyePosition(event.getPartialTick())) < 1.8) {
                event.setCanceled(true);
                return;
            }
        }

        if (reenteringPlayerRender || world != mc.level) return;
        var scene = SCENES.get(event.getEntity().getId());
        if (scene == null) return;
        double age = age(scene, event.getPartialTick());
        double stageW = SenbonStage.stageWeight(age);
        if (stageW <= 0.02) return;

        int packed = event.getPackedLight();
        int block = LightTexture.block(packed);
        int sky = LightTexture.sky(packed);
        int targetBlock = Math.max(block, Math.round(12.0F * (float) stageW));
        if (targetBlock > block && event.getEntity() instanceof net.minecraft.client.player.AbstractClientPlayer clientPlayer) {
            int boosted = LightTexture.pack(targetBlock, sky);
            reenteringPlayerRender = true;
            try {
                float yaw = Mth.lerp(event.getPartialTick(), clientPlayer.yRotO, clientPlayer.getYRot());
                event.getRenderer().render(clientPlayer, yaw, event.getPartialTick(), event.getPoseStack(), event.getMultiBufferSource(), boosted);
                event.setCanceled(true);
            } finally {
                reenteringPlayerRender = false;
            }
        }
    }

    /**
     * Atmospheric twilight darkening: blends distant fog towards a deep spiritual indigo/violet night
     * without touching global weather, daytime, or player brightness.
     */
    @SubscribeEvent
    public static void onComputeFogColor(ViewportEvent.ComputeFogColor e) {
        if (world == null || SCENES.isEmpty()) return;
        double shade = computeAtmosphereShade((float) e.getPartialTick());
        if (shade <= 0.005) return;

        float dark = (float) (shade * 0.88);
        e.setRed(Mth.lerp(dark, e.getRed(), 0.035F));
        e.setGreen(Mth.lerp(dark, e.getGreen(), 0.020F));
        e.setBlue(Mth.lerp(dark, e.getBlue(), 0.075F));
    }

    @SubscribeEvent
    public static void onRenderFog(ViewportEvent.RenderFog e) {
        if (world == null || SCENES.isEmpty()) return;
        double shade = computeAtmosphereShade((float) e.getPartialTick());
        if (shade <= 0.01) return;

        float dark = (float) shade;
        float vanillaNear = e.getNearPlaneDistance();
        float vanillaFar = e.getFarPlaneDistance();
        e.setNearPlaneDistance(Mth.lerp(dark * 0.85F, vanillaNear, 16.0F));
        e.setFarPlaneDistance(Mth.lerp(dark * 0.85F, vanillaFar, 40.0F));
        e.setCanceled(true);
    }

    private static double computeAtmosphereShade(float partialTick) {
        var mc = Minecraft.getInstance();
        if (mc.player == null) return 0.0;
        double maxShade = 0.0;
        Vec3 camPos = mc.gameRenderer.getMainCamera().getPosition();
        Vec3 viewDir = viewDirection(partialTick);

        for (var scene : SCENES.values()) {
            double age = age(scene, partialTick);
            if (age < 0.0 || age >= DURATION) continue;

            double dark = SenbonAtmosphere.darkness(age);
            if (dark <= 0.0) continue;

            boolean isLocalCaster = mc.player.getId() == getIdForScene(scene);
            if (isLocalCaster && cameraActive) {
                if (dark > maxShade) maxShade = dark;
            } else {
                // Observer: proximity (< 48 blocks) and orientation check towards the scene
                Vec3 delta = scene.anchor.subtract(camPos);
                double distanceFactor = Mth.clamp((48.0 - delta.length()) / 24.0, 0.0, 1.0);
                double facingFactor = Mth.clamp((viewDir.dot(delta.normalize()) - 0.25) / 0.55, 0.0, 1.0);
                double observerShade = 0.65 * distanceFactor * facingFactor * dark;
                if (observerShade > maxShade) maxShade = observerShade;
            }
        }
        return maxShade;
    }

    private static int getIdForScene(Scene scene) {
        for (var entry : SCENES.entrySet()) {
            if (entry.getValue() == scene) return entry.getKey();
        }
        return -1;
    }

    @SubscribeEvent
    public static void onHudOverlay(RenderGuiOverlayEvent.Pre event) {
        if (cameraActive) {
            var s = local();
            if (s != null && age(s, event.getPartialTick()) < 278.0) {
                // Cancel vanilla combat HUD while cinematic letterbox is active; restore smoothly at age 278 before scene end
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onGuiRender(RenderGuiEvent.Post event) {
        var mc = Minecraft.getInstance();
        if (mc.level != world || mc.player == null || mc.screen != null) return;

        // 1. Atmosphere Screen-space Spotlight Penumbra (Local and Observers)
        double shade = computeAtmosphereShade(event.getPartialTick());
        Scene targetScene = local();
        if (targetScene == null) {
            for (var s : SCENES.values()) {
                targetScene = s;
                break;
            }
        }
        if (shade > 0.01 && targetScene != null) {
            renderSpotlightPenumbra(event, targetScene, shade);
        }

        if (!cameraActive) return;
        var s = local();
        if (s == null) return;

        double age = age(s, event.getPartialTick());
        if (age < 0.0 || age >= DURATION) return;

        GuiGraphics g = event.getGuiGraphics();
        // Smoothly retract letterbox bars between 252..278 so HUD and gameplay framing are clean before t=290
        double envelope = SenbonCameraPath.ease(age / 12.0) * (1.0 - SenbonCameraPath.ease((age - 252.0) / 26.0));
        int barHeight = (int) (g.guiHeight() * 0.065 * envelope);

        // 2. Cinematic Letterbox Bars
        if (barHeight > 0) {
            g.fill(0, 0, g.guiWidth(), barHeight, 0xEE000000);
            g.fill(0, g.guiHeight() - barHeight, g.guiWidth(), g.guiHeight(), 0xEE000000);
        }

        // 3. Cinematic Subtitles (Centered at the lower screen with subtle dark plate)
        var sub = SenbonAtmosphere.subtitle(age);
        if (sub != null && sub.alpha() > 0.01F) {
            var font = mc.font;
            String text = Component.translatable(sub.translationKey()).getString();
            int textWidth = font.width(text);
            int y = g.guiHeight() - barHeight - 22;

            int padX = 8, padY = 3;
            int x1 = (g.guiWidth() - textWidth) / 2 - padX;
            int x2 = (g.guiWidth() + textWidth) / 2 + padX;
            int y1 = y - padY;
            int y2 = y + font.lineHeight + padY;

            // Subtle dark translucent background pill
            int bgAlpha = (int) (140 * sub.alpha());
            g.fill(x1, y1, x2, y2, (bgAlpha << 24) | 0x0A0B12);

            // Ivory text with drop shadow
            int textAlpha = (int) (255 * sub.alpha());
            int textColor = (textAlpha << 24) | 0xF0F4F8;
            g.drawCenteredString(font, text, g.guiWidth() / 2, y, textColor);
        }
    }

    /**
     * Renders a screen-space vignette with a generous elliptical cutout centered on the player's upper torso/head,
     * maintaining full clarity on the Shinigami's face and torso while dimming peripheral background elements.
     */
    private static void renderSpotlightPenumbra(RenderGuiEvent.Post e, Scene scene, double shade) {
        var mc = Minecraft.getInstance();
        var g = e.getGuiGraphics();
        double w = g.guiWidth(), h = g.guiHeight();
        var camera = mc.gameRenderer.getMainCamera();
        Vec3 actor = scene.anchor;
        Vec3 relative = actor.add(0, 1.30, 0).subtract(camera.getPosition());
        Vec3 look = viewDirection((float) e.getPartialTick());
        Vec3 right = look.cross(new Vec3(0, 1, 0)).normalize();
        Vec3 up = right.cross(look).normalize();
        double depth = relative.dot(look);

        float fovVal = cameraActive ? currentFov((float) e.getPartialTick()) : mc.options.fov().get().floatValue();
        double tan = Math.tan(Math.toRadians(fovVal) / 2.0);
        double scale = h / (2.0 * tan * Math.max(0.1, depth));
        double cx = w * 0.5 + relative.dot(right) * scale;
        double cy = h * 0.5 - relative.dot(up) * scale;
        double rx = Math.max(28.0, 1.70 * scale);
        double ry = Math.max(44.0, 2.60 * scale);

        var vertices = g.bufferSource().getBuffer(RenderType.gui());
        var matrix = g.pose().last().pose();
        int step = 16;
        for (int y = 0; y < h; y += step) {
            for (int x = 0; x < w; x += step) {
                int x2 = (int) Math.min(w, x + step);
                int y2 = (int) Math.min(h, y + step);
                for (int[] p : new int[][]{{x, y}, {x, y2}, {x2, y2}, {x2, y}}) {
                    double mask = depth > 0.1 ? SenbonAtmosphere.ellipseMask(p[0], p[1], cx, cy, rx, ry) : 1.0;
                    vertices.vertex(matrix, p[0], p[1], 0).color(6, 4, 12, (int) (145 * shade * mask)).endVertex();
                }
            }
        }
        g.flush();
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent e) {
        if (e.phase != TickEvent.Phase.END) return;
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) {
            clear();
            return;
        }
        if (world != mc.level) {
            clear();
            world = mc.level;
            return;
        }

        // If player gives movement/attack input after combat lock (tick 60), smoothly restore camera
        if (cameraActive && mc.player != null) {
            var s = local();
            if (s != null && (mc.level.getGameTime() - s.start) >= 60) {
                if (mc.player.hurtTime > 0
                        || mc.options.keyUp.isDown()
                        || mc.options.keyDown.isDown()
                        || mc.options.keyLeft.isDown()
                        || mc.options.keyRight.isDown()
                        || mc.options.keyAttack.isDown()
                        || mc.options.keyUse.isDown()
                        || mc.options.keyJump.isDown()
                        || CinematicDirector.localActive()) {
                    restoreCamera(mc);
                }
            }
        }

        // Cleanup expired or invalid scenes & update audio
        for (var it = SCENES.entrySet().iterator(); it.hasNext(); ) {
            var entry = it.next();
            int id = entry.getKey();
            Scene s = entry.getValue();
            var actor = mc.level.getEntity(id);
            long age = mc.level.getGameTime() - s.start;
            if (actor == null || !actor.isAlive() || age >= DURATION || actor.position().distanceToSqr(s.anchor) > 512 * 512) {
                SenbonzakuraEntranceAudio.stop(id);
                it.remove();
                if (mc.player != null && mc.player.getId() == id) {
                    restoreCamera(mc);
                }
                continue;
            }
            boolean isLocal = mc.player != null && mc.player.getId() == id;
            SenbonzakuraEntranceAudio.update(id, age, s.anchor, isLocal);
        }

        // Particle simulation during cinematic
        for (var entry : SCENES.entrySet()) {
            int entityId = entry.getKey();
            Scene s = entry.getValue();
            long age = mc.level.getGameTime() - s.start;
            if (age < 0 || age >= DURATION) continue;

            Vec3 anchor = s.anchor;
            Vec3 forward = s.forward;
            Vec3 left = SenbonGeometry.left(forward);
            Vec3 drop = dropPoint(s, entityId);

            // Phase 1: Sparse Reishi ripple petals on the ground around the sinking katana (age 30..130)
            if (age >= 30 && age < 130 && (age % 2 == 0)) {
                int count = 8;
                double rippleRadius = Math.min(5.0, (age - 30) * 0.08);
                for (int i = 0; i < count; i++) {
                    double ang = i * (Math.PI * 2.0 / count) + age * 0.04;
                    double px = drop.x + Math.cos(ang) * rippleRadius;
                    double pz = drop.z + Math.sin(ang) * rippleRadius;
                    mc.level.addParticle(ModParticles.SENBON_PETAL.get(), px, anchor.y + 0.04, pz, 0, 0.01, 0);
                }
            }

            // Phase 4: Dual cyclone vortex of petals during blade dispersion (age 224..282),
            // progressively losing density during the final camera travel (250..282)
            if (age >= 224 && age < 282) {
                float petalProgress = (age - 224.0F) / 58.0F;
                float densityEnvelope = age < 248
                        ? Mth.clamp((age - 224.0F) / 10.0F, 0.25F, 1.0F)
                        : Mth.clamp(1.0F - (age - 248.0F) / 34.0F, 0.0F, 1.0F);
                int petalCount = Math.max(2, Math.round(24 * densityEnvelope));
                for (int side : new int[]{-1, 1}) {
                    Vec3 colCenter = anchor.add(left.scale(side * 4.5));
                    for (int i = 0; i < petalCount; i++) {
                        double ang = i * (Math.PI * 2.0 / petalCount) + age * 0.28;
                        double radius = 1.4 + Math.sin(age * 0.15 + i) * 1.1 + petalProgress * 1.2;
                        double height = (i % 8) * 2.4 + petalProgress * 6.5;
                        double vx = -Math.sin(ang) * 0.28 - left.x * side * 0.08 * petalProgress;
                        double vz = Math.cos(ang) * 0.28 + forward.z * 0.12 * petalProgress;
                        mc.level.addParticle(ModParticles.SENBON_PETAL.get(),
                                colCenter.x + Math.cos(ang) * radius,
                                anchor.y + height,
                                colCenter.z + Math.sin(ang) * radius,
                                vx, 0.14 * (1.0 - 0.5 * petalProgress), vz);
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
        float partialTick = event.getPartialTick();
        pose.pushPose();

        var buffers = mc.renderBuffers().bufferSource();
        var matrix = pose.last().pose();

        // Pass 1: Curved 3D Spatial Dark Backdrop & Depth Layers of Sakura Petals (furthest back, dissolves 220..276)
        var backdropBuf = buffers.getBuffer(Types.STAGE_BACKDROP);
        for (Scene s : SCENES.values()) {
            double age = age(s, partialTick);
            if (age < 0.0 || age >= DURATION || mc.player.distanceToSqr(s.anchor) > 96 * 96) continue;
            emitQuads(backdropBuf, matrix, SenbonStage.backdrop(s.anchor.subtract(camera), s.forward, age));
        }
        buffers.endBatch(Types.STAGE_BACKDROP);

        // Pass 2: Terrain-Conforming Dark Spiritual Floor (0..35 entry, 35..224 sustain, 224..280 dissolution)
        var floorBuf = buffers.getBuffer(Types.STAGE_FLOOR);
        for (Scene s : SCENES.values()) {
            double age = age(s, partialTick);
            if (age < 0.0 || age >= DURATION || mc.player.distanceToSqr(s.anchor) > 96 * 96) continue;
            var ground = sampleTerrain(s.anchor, s.forward);
            emitQuads(floorBuf, matrix, SenbonStage.floor(s.anchor.subtract(camera), s.forward, age, ground));
        }
        buffers.endBatch(Types.STAGE_FLOOR);

        // Pass 3: Foreground Elements (Surface Petals/Trails, Contact Ripples, Single Vertical Katana, 20 Monumental Blades + Dissolution Streams)
        var vertices = buffers.getBuffer(Types.GLOW);
        for (var entry : SCENES.entrySet()) {
            int entityId = entry.getKey();
            Scene s = entry.getValue();
            double age = age(s, partialTick);
            if (age < 0.0 || age >= DURATION || mc.player.distanceToSqr(s.anchor) > 96 * 96) continue;

            Vec3 anchor = s.anchor;
            Vec3 forward = s.forward;
            Vec3 left = SenbonGeometry.left(forward);
            Vec3 originRelCam = anchor.subtract(camera);
            Vec3 drop = dropPoint(s, entityId);
            double groundY = anchor.y + 0.024;
            var ground = sampleTerrain(anchor, forward);

            // 1. Sparse low-lying sakura petals and soft luminous trails above the dark floor
            emitQuads(vertices, matrix, SenbonStage.surfaceDetails(originRelCam, forward, age, ground));

            // 2. Concentric Reishi Water Ripple Discs on the dark floor (age 30..180)
            if (age >= 30.0 && age < 180.0) {
                renderReishiRippleDiscs(vertices, matrix, drop.subtract(camera), age);
            }

            // 3. Single Vertical Katana: held in hand (0..10), released & falling (10..35), touching surface (35), sinking (35..76)
            if (age >= 0.0 && age < 76.0) {
                renderFallingSinkingKatana(vertices, matrix, drop.subtract(camera), groundY - camera.y, age, forward, left);
            }

            // 4. 20 Monumental Colossal Blades & Progressive Petal Dissolution Streams (age 76..272)
            if (age >= 76.0 && age < 272.0) {
                renderMonumentalBladeCorridor(vertices, matrix, originRelCam, forward, left, age);
            }
        }

        buffers.endBatch(Types.GLOW);
        pose.popPose();
    }

    private static void emitQuads(com.mojang.blaze3d.vertex.VertexConsumer buf, org.joml.Matrix4f matrix, List<BenihimeGeometry.Quad> quads) {
        for (var q : quads) {
            int c = q.color();
            int r = c >>> 24;
            int g = (c >>> 16) & 255;
            int b = (c >>> 8) & 255;
            int a = c & 255;
            for (var v : List.of(q.a(), q.b(), q.c(), q.d())) {
                buf.vertex(matrix, (float) v.x, (float) v.y, (float) v.z).color(r, g, b, a).endVertex();
            }
        }
    }

    /**
     * Client collision-shape sampling aligned with Senbonzakura's local coordinates (+X left, +Z forward).
     * Never loads chunks, modifies blocks, or alters entity collision.
     */
    private static BenihimeEntranceGeometry.Ground sampleTerrain(Vec3 anchor, Vec3 forward) {
        var mc = Minecraft.getInstance();
        var level = mc.level;
        Vec3 flat = new Vec3(forward.x, 0.0, forward.z);
        Vec3 normForward = flat.lengthSqr() > 1.0E-5 ? flat.normalize() : new Vec3(0.0, 0.0, 1.0);
        Vec3 left = SenbonGeometry.left(normForward);
        return (x, z) -> {
            if (level == null || mc.player == null) return 0.0;
            Vec3 point = anchor.add(left.scale(x)).add(normForward.scale(z));
            if (!level.hasChunkAt(BlockPos.containing(point))) return 0.0;
            var hit = level.clip(new ClipContext(
                    point.add(0.0, 2.2, 0.0),
                    point.add(0.0, -2.2, 0.0),
                    ClipContext.Block.COLLIDER,
                    ClipContext.Fluid.NONE,
                    mc.player));
            return hit.getType() == HitResult.Type.MISS ? 0.0 : hit.getLocation().y - anchor.y;
        };
    }

    /**
     * Renders the single vertical katana: held in the caster's main hand (age 0..10),
     * released at age 10 and accelerating downward under gravity (age 10..35) to touch the dark surface at age 35,
     * and progressively sinking into the spiritual water surface by clipping quads at ground level (age 35..76).
     */
    private static void renderFallingSinkingKatana(com.mojang.blaze3d.vertex.VertexConsumer v, org.joml.Matrix4f m, Vec3 dropOrigin, double groundRelY, double age, Vec3 forward, Vec3 left) {
        float initialHiltTop = 1.16F;
        float fallDist;
        if (age < 10.0) {
            fallDist = 0.0F;
        } else if (age < 35.0) {
            float t = (float) ((age - 10.0) / 25.0);
            fallDist = t * t * 0.216F;
        } else {
            float t = (float) ((age - 35.0) / 41.0);
            fallDist = 0.216F + t * 0.96F;
        }

        double hiltTopY = dropOrigin.y + (initialHiltTop - fallDist);
        double tsubaY = hiltTopY - 0.24;
        double tipY = tsubaY - 0.68;

        if (hiltTopY <= groundRelY) return;

        Vec3 up = new Vec3(0, 1, 0);

        // Subtle luminous collar exactly at the surface contact line while the katana sinks (age 34..75)
        if (age >= 34.0 && tipY <= groundRelY + 0.04) {
            int collarAlpha = (int) (185 * Mth.clamp((76.0 - age) / 14.0, 0.0, 1.0));
            Vec3 surfaceContact = new Vec3(dropOrigin.x, groundRelY - dropOrigin.y, dropOrigin.z);
            drawRing(v, m, dropOrigin.add(0, surfaceContact.y, 0), 0.03, 0.17, 245, 175, 222, collarAlpha, 0.014F);
        }

        // 1. Blade Body (Silver steel with glowing hamon edge)
        if (tsubaY > groundRelY) {
            double bladeBottom = Math.max(tipY, groundRelY);
            double bladeTop = tsubaY;
            double bladeLength = bladeTop - bladeBottom;
            if (bladeLength > 0.01) {
                Vec3 bladeBase = new Vec3(dropOrigin.x, bladeBottom, dropOrigin.z);
                quadPillar(v, m, bladeBase, up, left, forward, 0.022F, (float) bladeLength, 215, 225, 240, 245);
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
                if (hiltTopY - 0.03 >= groundRelY) {
                    Vec3 pommelBase = new Vec3(dropOrigin.x, hiltTopY - 0.03, dropOrigin.z);
                    quadPillar(v, m, pommelBase, up, left, forward, 0.022F, 0.03F, 240, 195, 75, 255);
                }
            }
        }
    }

    private static void renderReishiRippleDiscs(com.mojang.blaze3d.vertex.VertexConsumer v, org.joml.Matrix4f m, Vec3 origin, double age) {
        double[] baseRadii = {0.55, 1.35, 2.45, 3.80, 5.35, 7.10};
        float life = (float) Mth.clamp((age - 30.0) / 150.0, 0.0, 1.0);
        float entry = (float) SenbonStage.smoothstep(30.0, 38.0, age);
        float fade = (age >= 136.0) ? (float) (1.0 - (age - 136.0) / 44.0) : 1.0F;

        for (int r = 0; r < baseRadii.length; r++) {
            double rad = baseRadii[r] * (0.32 + life * 0.96) + Math.sin(age * 0.14 + r * 0.9) * 0.14;
            float ringFade = entry * fade * Mth.clamp(1.0F - (float) r / 6.2F, 0.20F, 1.0F);
            // Discreetly reduced ripple intensity so the dark water and katana remain the primary focus
            int crestAlpha = (int) (136 * ringFade);
            int haloAlpha = (int) (56 * ringFade);
            float yOffset = 0.036F + r * 0.0015F;

            // Soft indigo-rose outer reflection halo on the dark spiritual water
            drawRing(v, m, origin, Math.max(0.02, rad - 0.14), rad + 0.42, 138, 56, 132, haloAlpha, yOffset - 0.002F);
            // Subtle sakura-pink inner ripple crest
            drawRing(v, m, origin, rad, rad + 0.16, 236, 138, 198, crestAlpha, yOffset);
        }
    }

    private static void drawRing(com.mojang.blaze3d.vertex.VertexConsumer v, org.joml.Matrix4f m, Vec3 origin, double rInner, double rOuter, int r, int g, int b, int a, float yOffset) {
        if (a <= 2) return;
        int segments = 32;
        float y = (float) origin.y + yOffset;
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

            v.vertex(m, x0i, y, z0i).color(r, g, b, a).endVertex();
            v.vertex(m, x0o, y, z0o).color(r, g, b, a).endVertex();
            v.vertex(m, x1o, y, z1o).color(r, g, b, a).endVertex();
            v.vertex(m, x1i, y, z1i).color(r, g, b, a).endVertex();
        }
    }

    /**
     * Renders the monumental corridor of 20 colossal blades (10 left, 10 right),
     * standing 22 blocks tall, spanning 35 blocks along the corridor, and dissolving progressively
     * into legible ascending petal streams between ticks 224 and 272.
     */
    private static void renderMonumentalBladeCorridor(com.mojang.blaze3d.vertex.VertexConsumer v, org.joml.Matrix4f m, Vec3 origin, Vec3 forward, Vec3 left, double age) {
        int bladeCountPerRow = 10;
        double corridorHalfWidth = 4.5;
        double bladeSpacingZ = 3.8;
        double startZ = -16.0;

        for (int side : new int[]{-1, 1}) {
            for (int i = 0; i < bladeCountPerRow; i++) {
                Vec3 bladeBase = origin.add(left.scale(side * corridorHalfWidth)).add(forward.scale(startZ + i * bladeSpacingZ));

                float delay = 76.0F + (bladeCountPerRow - 1 - i) * 3.2F;
                float rawGrow = (float) Mth.clamp((age - delay) / 32.0, 0.0, 1.0);
                if (rawGrow <= 0.0F) continue;

                float grow = (float) Math.sin(rawGrow * Math.PI * 0.5);

                float tremor = 0.0F;
                if (age >= 160.0 && age <= 228.0) {
                    tremor = (float) Math.sin((age * 0.75) + i * 0.6) * 0.06F;
                }

                // Staggered progressive dissolution along the corridor (back-to-front wave: dissStart = 224 + i * 1.6)
                float dissStart = 224.0F + i * 1.6F;
                float dissolve = 0.0F;
                float fade = 1.0F;
                if (age >= dissStart) {
                    float dissT = (float) Mth.clamp((age - dissStart) / 28.0, 0.0, 1.0);
                    dissolve = (float) SenbonStage.smoothstep(0.0, 1.0, dissT);
                    fade = (float) Mth.clamp(1.0 - (age - dissStart) / 30.0, 0.0, 1.0);
                }

                renderMonumentalBlade(v, m, bladeBase, forward, left, grow, tremor, dissolve, fade);
                if (age >= dissStart && age < dissStart + 38.0F) {
                    float streamLife = (float) ((age - dissStart) / 38.0);
                    renderBladeDissolutionPetals(v, m, bladeBase, forward, left, side, i, streamLife);
                }
            }
        }
    }

    /**
     * Renders legible 3D streams of sakura petals peeling off each dissolving colossal blade
     * and spiraling inward/upward along coherent trajectories before fading out.
     */
    private static void renderBladeDissolutionPetals(
            com.mojang.blaze3d.vertex.VertexConsumer v,
            org.joml.Matrix4f m,
            Vec3 bladeBase,
            Vec3 forward,
            Vec3 left,
            int side,
            int bladeIndex,
            float streamLife) {
        float envelope = (float) (Math.sin(Mth.clamp(streamLife, 0.0F, 1.0F) * Math.PI));
        if (envelope <= 0.02F) return;

        Vec3 up = new Vec3(0, 1, 0);
        int clusterCount = 12;
        for (int k = 0; k < clusterCount; k++) {
            double seed = bladeIndex * 17.3 + k * 5.7 + (side > 0 ? 3.1 : 0.0);
            double startY = 2.0 + (k / (double) clusterCount) * 19.0;
            // As the blade dissolves from top to bottom, petals peel off and arc inward + forward + upward
            double localProgress = Mth.clamp(streamLife * 1.35 - (1.0 - k / (double) clusterCount) * 0.35, 0.0, 1.0);
            if (localProgress <= 0.0) continue;

            double inward = -side * (localProgress * (2.6 + 0.8 * Math.sin(seed)));
            double driftZ = localProgress * (3.2 + 1.1 * Math.cos(seed * 0.7)) + Math.sin(localProgress * 4.5 + seed) * 0.55;
            double driftY = startY * (1.0 - 0.38 * localProgress) + Math.sin(localProgress * Math.PI) * 2.2;

            Vec3 pos = bladeBase.add(left.scale(inward)).add(forward.scale(driftZ)).add(0, driftY, 0);
            Vec3 tangent = left.scale(-side * 0.55).add(forward.scale(0.75)).add(0, 0.35, 0).normalize();
            double size = (0.28 + 0.14 * (k % 3)) * (1.0 - 0.35 * localProgress);
            int alpha = (int) (215 * envelope * (1.0 - 0.45 * localProgress));
            if (alpha <= 4) continue;

            Vec3 halfLen = tangent.scale(size * 1.35);
            Vec3 halfWid = up.scale(size * 0.52).add(left.scale(size * 0.25));
            int r = (k % 3 == 0) ? 255 : 242;
            int g = (k % 3 == 0) ? 205 : 148;
            int b = (k % 3 == 0) ? 232 : 206;

            v.vertex(m, (float) (pos.x - halfLen.x), (float) (pos.y - halfLen.y), (float) (pos.z - halfLen.z)).color(r, g, b, alpha).endVertex();
            v.vertex(m, (float) (pos.x + halfWid.x), (float) (pos.y + halfWid.y), (float) (pos.z + halfWid.z)).color(r, g, b, alpha).endVertex();
            v.vertex(m, (float) (pos.x + halfLen.x), (float) (pos.y + halfLen.y), (float) (pos.z + halfLen.z)).color(255, 232, 245, alpha).endVertex();
            v.vertex(m, (float) (pos.x - halfWid.x), (float) (pos.y - halfWid.y), (float) (pos.z - halfWid.z)).color(r, g, b, alpha).endVertex();
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

            quadPillar(v, m, bladeStart, up, left, forward, 0.36F, bladeLen, 210, 222, 238, (int) (235 * fade));
            Vec3 edgeStart = bladeStart.add(left.scale(-0.38F));
            quadRibbon(v, m, edgeStart, edgeStart.add(0, bladeLen, 0), forward.scale(0.16F), 255, 175, 220, alpha);
            quadRibbon(v, m, edgeStart, edgeStart.add(0, bladeLen, 0), up.scale(0.06F), 255, 255, 255, alpha);

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
                if (com.bleachmod.common.visual.SenbonzakuraPlayerPose.hideHeldWeapon(age)) {
                    return new dev.kosmx.playerAnim.core.util.Vec3f(0.0F, -999.0F, 0.0F);
                }
                return new dev.kosmx.playerAnim.core.util.Vec3f(x, y, z);
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
