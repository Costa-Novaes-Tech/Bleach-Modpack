package com.bleachmod.client.cinematic;

import com.bleachmod.Reference;
import com.bleachmod.client.model.RyujinActionVisuals;
import com.bleachmod.client.model.RyujinSunVisual;
import com.bleachmod.common.visual.RyujinBankaiCamera;
import com.bleachmod.common.visual.RyujinBankaiTimeline;
import com.bleachmod.common.visual.RyujinChoreography;
import dev.kosmx.playerAnim.api.TransformType;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.core.util.Vec3f;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationFactory;
import net.minecraft.client.Camera;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.lang.reflect.Method;
import java.util.*;

/**
 * Client-side cinematic director, camera controller, on-screen subtitles,
 * and audio manager for Ryūjin Jakka / Zanka no Tachi Bankai transformation (140 ticks).
 */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT)
public final class RyujinBankaiEntranceClient {
    public static final int DURATION = RyujinBankaiTimeline.DURATION;

    private record Scene(long start, Vec3 anchor, Vec3 forward, float yaw) { }

    private static final Map<Integer, Scene> SCENES = new HashMap<>();
    private static net.minecraft.client.multiplayer.ClientLevel world;
    private static CameraType previousCamera;
    private static Method cameraPositionMethod;
    private static boolean cameraEnabled = true;

    private RyujinBankaiEntranceClient() { }

    public static void init() {
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(
                Reference.id("ryujin_bankai_entrance"), 4500, Body::new);
    }

    public static boolean active(int id) {
        return world == Minecraft.getInstance().level && SCENES.containsKey(id);
    }

    public static void accept(int id, CompoundTag tag) {
        var mc = Minecraft.getInstance();
        if (mc.level == null) return;
        if (world != mc.level) {
            clear();
            world = mc.level;
        }

        if (tag == null || !tag.contains("duration") || tag.getInt("duration") != DURATION) {
            finish(id);
            return;
        }

        int elapsed = tag.getInt("elapsed");
        if (elapsed < 0 || elapsed >= DURATION) {
            finish(id);
            return;
        }

        Vec3 anchor = new Vec3(tag.getDouble("x"), tag.getDouble("y"), tag.getDouble("z"));
        float yaw = tag.getFloat("yaw");
        if (!Double.isFinite(anchor.lengthSqr()) || !Float.isFinite(yaw)) {
            finish(id);
            return;
        }

        boolean fresh = !SCENES.containsKey(id);
        SCENES.put(id, new Scene(mc.level.getGameTime() - elapsed, anchor, Vec3.directionFromRotation(0, yaw), yaw));

        if (fresh && mc.player != null && mc.player.getId() == id && elapsed < 20) {
            cameraEnabled = true;
        }
    }

    private static double age(Scene s, float partial) {
        return world.getGameTime() - s.start + partial;
    }

    private static void finish(int id) {
        SCENES.remove(id);
        RyujinBankaiAudio.stop(id);
        restore();
    }

    private static Scene local() {
        var mc = Minecraft.getInstance();
        return mc.player == null ? null : SCENES.get(mc.player.getId());
    }

    public static boolean localEntranceActive() {
        return local() != null;
    }

    private static boolean cameraActive() {
        var mc = Minecraft.getInstance();
        return local() != null && cameraEnabled && !CinematicDirector.localActive()
                && mc.screen == null && mc.getCameraEntity() == mc.player;
    }

    private static void restore() {
        var mc = Minecraft.getInstance();
        if (previousCamera != null && mc.options.getCameraType() == CameraType.THIRD_PERSON_BACK) {
            mc.options.setCameraType(previousCamera);
        }
        previousCamera = null;
    }

    public static void clear() {
        restore();
        RyujinBankaiAudio.clear();
        SCENES.clear();
        world = null;
        cameraEnabled = true;
    }

    @SubscribeEvent
    public static void logout(ClientPlayerNetworkEvent.LoggingOut e) {
        clear();
    }

    @SubscribeEvent
    public static void tick(TickEvent.ClientTickEvent e) {
        if (e.phase != TickEvent.Phase.END) return;
        var mc = Minecraft.getInstance();
        if (world != mc.level) {
            clear();
            world = mc.level;
        }
        if (mc.level == null || mc.player == null) return;

        // Clean finished or out-of-range scenes
        for (var id : List.copyOf(SCENES.keySet())) {
            var p = mc.level.getEntity(id);
            var s = SCENES.get(id);
            if (p == null || !p.isAlive() || age(s, 0) >= DURATION || p.position().distanceToSqr(s.anchor) > 512 * 512) {
                finish(id);
            }
        }

        // Update audio
        RyujinBankaiAudio.retain(SCENES.keySet());
        for (var entry : SCENES.entrySet()) {
            boolean owner = entry.getKey() == mc.player.getId();
            var scene = entry.getValue();
            var actor = mc.level.getEntity(entry.getKey());
            Vec3 voicePos = actor == null ? scene.anchor.add(0, 1.3, 0) : actor.position().add(0, 1.3, 0);
            RyujinBankaiAudio.update(entry.getKey(), age(scene, 0), voicePos, owner,
                    !owner || (cameraEnabled && mc.screen == null && !CinematicDirector.localActive()));
        }

        var s = local();
        if (s == null) {
            restore();
            return;
        }

        double age = age(s, 0);

        // Player voluntary movement or damage handling
        if (mc.player.hurtTime > 0) {
            cameraEnabled = false;
        }

        // After commit and initial sequence (tick 60+), player movement releases camera smoothly
        if (age >= 60 && (mc.options.keyUp.isDown() || mc.options.keyDown.isDown() || mc.options.keyLeft.isDown()
                || mc.options.keyRight.isDown() || mc.options.keyJump.isDown() || mc.options.keyAttack.isDown())) {
            cameraEnabled = false;
        }

        if (cameraActive()) {
            if (previousCamera == null) {
                previousCamera = mc.options.getCameraType();
                mc.options.setCameraType(CameraType.THIRD_PERSON_BACK);
            } else if (mc.options.getCameraType() != CameraType.THIRD_PERSON_BACK) {
                cameraEnabled = false;
                restore();
            }
        } else {
            restore();
        }
    }

    @SubscribeEvent
    public static void onCameraAngles(ViewportEvent.ComputeCameraAngles event) {
        if (!cameraActive() || world == null) return;
        var s = local();
        if (s == null) return;

        var mc = Minecraft.getInstance();
        double age = age(s, (float) event.getPartialTick());
        if (age >= DURATION || age < 0) {
            restore();
            return;
        }

        RyujinBankaiCamera.Frame frame = RyujinBankaiCamera.sample(s.anchor, s.forward, age);
        Vec3 eye = frame.eye();
        Vec3 safeOrigin = mc.player != null ? mc.player.getEyePosition() : s.anchor.add(0, 1.6, 0);

        // Clip terrain to avoid camera clipping inside blocks
        var hit = world.clip(new ClipContext(safeOrigin, eye, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, mc.player));
        if (hit.getType() != HitResult.Type.MISS) {
            eye = hit.getLocation().add(safeOrigin.subtract(eye).normalize().scale(0.25));
        }

        if (eye.distanceToSqr(frame.focus()) < 0.25) {
            restore();
            return;
        }

        try {
            if (cameraPositionMethod == null) {
                cameraPositionMethod = net.minecraftforge.fml.util.ObfuscationReflectionHelper.findMethod(Camera.class, "m_90581_", Vec3.class);
            }
            cameraPositionMethod.invoke(event.getCamera(), eye);
        } catch (ReflectiveOperationException | RuntimeException ignored) {
            restore();
            return;
        }

        Vec3 d = frame.focus().subtract(eye);
        event.setYaw((float) Math.toDegrees(Math.atan2(-d.x, d.z)));
        event.setPitch((float) -Math.toDegrees(Math.atan2(d.y, Math.sqrt(d.x * d.x + d.z * d.z))));
        event.setRoll(0);
    }

    @SubscribeEvent
    public static void onFov(ViewportEvent.ComputeFov event) {
        if (!cameraActive() || world == null) return;
        var s = local();
        if (s == null) return;
        double age = age(s, (float) event.getPartialTick());
        if (age < DURATION && age >= 0 && event.usedConfiguredFov()) {
            event.setFOV(RyujinBankaiCamera.sample(s.anchor, s.forward, age).fov());
        }
    }

    @SubscribeEvent
    public static void onHudOverlay(RenderGuiOverlayEvent.Pre event) {
        if (cameraActive() && local() != null) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onGuiRender(RenderGuiEvent.Post event) {
        if (!cameraActive()) return;
        var s = local();
        if (s == null) return;

        double age = age(s, event.getPartialTick());
        GuiGraphics g = event.getGuiGraphics();
        int width = g.guiWidth();
        int height = g.guiHeight();
        int barHeight = Math.max(16, height / 10);

        // Letterbox bars
        g.fill(0, 0, width, barHeight, 0xFF000000);
        g.fill(0, height - barHeight, width, height, 0xFF000000);

        var font = Minecraft.getInstance().font;

        // Subtitle 1: "Bankai" (ticks 15..42)
        float bankaiAlpha = RyujinBankaiTimeline.bankaiSubtitleAlpha(age);
        if (bankaiAlpha > 0.01F) {
            int alphaInt = Math.min(255, (int) (bankaiAlpha * 255));
            int color = (alphaInt << 24) | 0xFFD700; // Fiery Gold
            g.drawCenteredString(font, "Bankai", width / 2, height - barHeight - 20, color);
        }

        // Subtitle 2: "Zanka no Tachi" (ticks 65..124)
        float nameAlpha = RyujinBankaiTimeline.nameSubtitleAlpha(age);
        if (nameAlpha > 0.01F) {
            int alphaInt = Math.min(255, (int) (nameAlpha * 255));
            int color = (alphaInt << 24) | 0xFF6F00; // Deep Fiery Orange
            g.drawCenteredString(font, "Zanka no Tachi", width / 2, height - barHeight - 20, color);
        }
    }

    @SubscribeEvent
    public static void onRenderLevel(RenderLevelStageEvent e) {
        if (e.getStage() != RenderLevelStageEvent.Stage.AFTER_PARTICLES || world != Minecraft.getInstance().level || SCENES.isEmpty()) return;
        var mc = Minecraft.getInstance();
        Vec3 cameraPos = e.getCamera().getPosition();

        for (var entry : SCENES.entrySet()) {
            var scene = entry.getValue();
            var entity = world.getEntity(entry.getKey());
            float sign = (entity instanceof AbstractClientPlayer p && p.getMainArm() == HumanoidArm.LEFT) ? -1.0F : 1.0F;
            double age = age(scene, e.getPartialTick());
            if (age >= 0 && age < DURATION) {
                RyujinSunVisual.render(e.getPoseStack(), cameraPos, scene.anchor, scene.forward, age, sign);
            }
        }
    }

    /**
     * PlayerAnimator stance layer during the 140 ticks of transformation.
     */
    private static final class Body implements IAnimation {
        final AbstractClientPlayer player;

        Body(AbstractClientPlayer p) {
            this.player = p;
        }

        @Override
        public boolean isActive() {
            return active(player.getId()) && !CinematicDirector.localActive()
                    && (player != Minecraft.getInstance().player || cameraEnabled);
        }

        @Override
        public void setupAnim(float partial) { }

        @Override
        public FirstPersonMode getFirstPersonMode(float partial) {
            return FirstPersonMode.VANILLA;
        }

        @Override
        public Vec3f get3DTransform(String part, TransformType type, float partial, Vec3f value) {
            var s = SCENES.get(player.getId());
            if (s == null) return value;
            double age = age(s, partial);
            float sign = player.getMainArm() == HumanoidArm.RIGHT ? 1.0F : -1.0F;
            RyujinChoreography.Pose pose = RyujinChoreography.bankaiEntrance((float) age, sign);
            return RyujinActionVisuals.transform(player, pose, part, type, value);
        }
    }
}
