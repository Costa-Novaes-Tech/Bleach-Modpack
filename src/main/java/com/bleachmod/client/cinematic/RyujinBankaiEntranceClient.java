package com.bleachmod.client.cinematic;

import com.bleachmod.Reference;
import com.bleachmod.client.model.RyujinActionVisuals;
import com.bleachmod.client.model.RyujinSunVisual;
import com.bleachmod.common.visual.RyujinBankaiCamera;
import com.bleachmod.common.visual.RyujinBankaiPresentation;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.util.Mth;
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
 * and audio manager for Ryūjin Jakka / Zanka no Tachi Bankai transformation (800 real ticks; authored beat clock 0..140).
 */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT)
public final class RyujinBankaiEntranceClient {
    public static final int DURATION = RyujinBankaiTimeline.DURATION;

    private record Scene(long start, Vec3 anchor, Vec3 forward, float yaw, int viewTicks) { }

    private static final Map<Integer, Scene> SCENES = new HashMap<>();
    private static net.minecraft.client.multiplayer.ClientLevel world;
    private static CameraType previousCamera;
    private static Method cameraPositionMethod;
    private static boolean cameraEnabled = true;
    private static long lastEmissionTick = Long.MIN_VALUE;
    private static boolean reenteringPlayerRender;
    private static long hidePlayerUntil = Long.MIN_VALUE;

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
        SCENES.put(id, new Scene(mc.level.getGameTime() - elapsed, anchor, Vec3.directionFromRotation(0, yaw), yaw, fresh ? (com.bleachmod.client.ClientVisualConfig.RYUJIN_LONG.get() ? DURATION : RyujinBankaiTimeline.COMPACT_DURATION) : SCENES.get(id).viewTicks));

        if (fresh && mc.player != null && mc.player.getId() == id && elapsed < 20) {
            cameraEnabled = true;
        }
    }

    private static double age(Scene s, float partial) {
        return RyujinBankaiTimeline.storyAge(rawAge(s, partial), s.viewTicks);
    }

    private static double rawAge(Scene s, float partial) { return world.getGameTime()-s.start+partial; }

    private static void finish(int id) {
        SCENES.remove(id);
        RyujinBankaiAudio.stop(id);
        if (Minecraft.getInstance().player != null && Minecraft.getInstance().player.getId() == id) restore();
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
        if (previousCamera != null && mc.options.getCameraType() == sceneCameraType()) {
            mc.options.setCameraType(previousCamera);
            if(previousCamera == CameraType.FIRST_PERSON && mc.level != null) hidePlayerUntil = mc.level.getGameTime()+1;
        }
        previousCamera = null;
    }

    private static CameraType sceneCameraType() {
        return previousCamera == CameraType.THIRD_PERSON_FRONT ? CameraType.THIRD_PERSON_FRONT : CameraType.THIRD_PERSON_BACK;
    }
    private static RyujinBankaiPresentation.Blade blade(Scene s, int id, float partial) {
        var actor = world.getEntity(id);
        Vec3 position = actor == null ? s.anchor : actor.getPosition(partial);
        Vec3 forward = actor == null ? s.forward : Vec3.directionFromRotation(0, actor.getYRot());
        float sign = actor instanceof AbstractClientPlayer p && p.getMainArm() == HumanoidArm.LEFT ? -1 : 1;
        return RyujinBankaiPresentation.blade(position, forward, age(s, partial), sign);
    }
    public static void clear() {
        restore();
        RyujinBankaiAudio.clear();
        SCENES.clear();
        lastEmissionTick = Long.MIN_VALUE;
        hidePlayerUntil = Long.MIN_VALUE;
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
            if (p == null || !p.isAlive() || rawAge(s, 0) >= DURATION || p.position().distanceToSqr(s.anchor) > 512 * 512) {
                finish(id);
            }
        }

        // Particle budget is per game tick and shared by all casters, independent of render FPS.
        if (!mc.isPaused() && lastEmissionTick != world.getGameTime()) {
            lastEmissionTick = world.getGameTime();
            int budget = 48;
            for (var entry : SCENES.entrySet()) {
                var scene = entry.getValue();
                budget -= RyujinSunVisual.emit(scene.anchor, scene.forward, age(scene, 0), blade(scene, entry.getKey(), 0), budget);
                if (budget <= 0) break;
            }
        }
        // Update audio
        RyujinBankaiAudio.retain(SCENES.keySet());
        for (var entry : SCENES.entrySet()) {
            boolean owner = entry.getKey() == mc.player.getId();
            var scene = entry.getValue();
            var actor = mc.level.getEntity(entry.getKey());
            Vec3 voicePos = actor == null ? scene.anchor.add(0, 1.3, 0) : actor.position().add(0, 1.3, 0);
            RyujinBankaiAudio.update(entry.getKey(), rawAge(scene, 0), scene.viewTicks, voicePos, owner,
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
        if (rawAge(s, 0) >= 60 && (mc.options.keyUp.isDown() || mc.options.keyDown.isDown() || mc.options.keyLeft.isDown()
                || mc.options.keyRight.isDown() || mc.options.keyJump.isDown() || mc.options.keyAttack.isDown())) {
            cameraEnabled = false;
        }

        if (cameraActive()) {
            if (previousCamera == null) {
                previousCamera = mc.options.getCameraType();
                mc.options.setCameraType(sceneCameraType());
            } else if (mc.options.getCameraType() != sceneCameraType()) {
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

        float sign = (mc.player != null && mc.player.getMainArm() == HumanoidArm.LEFT) ? -1.0F : 1.0F;
        RyujinBankaiCamera.Frame frame = RyujinBankaiCamera.sample(s.anchor, s.forward, age, sign);
        Vec3 eye = frame.eye(), focus = frame.focus();
        if (age >= RyujinBankaiCamera.RETURN_START && mc.player != null) {
            double t = RyujinBankaiTimeline.progress(age, RyujinBankaiCamera.RETURN_START, RyujinBankaiCamera.RETURN_SETTLE);
            Vec3 center = mc.player.getEyePosition((float) event.getPartialTick());
            Vec3 look = mc.player.getViewVector((float) event.getPartialTick());
            boolean first = previousCamera == CameraType.FIRST_PERSON;
            Vec3 end = first ? center : event.getCamera().getPosition();
            Vec3 direction = first ? look : Vec3.directionFromRotation(event.getPitch(), event.getYaw());
            eye = RyujinBankaiCamera.returnEye(eye, center, end, look, t, previousCamera == CameraType.THIRD_PERSON_FRONT);
            focus = eye.add(RyujinBankaiCamera.blendDirection(frame.focus().subtract(frame.eye()), direction, t).scale(8));
        }
        Vec3 safeOrigin = mc.player != null ? mc.player.getEyePosition() : s.anchor.add(0, 1.6, 0);

        // Clip terrain to avoid camera clipping inside blocks
        var hit = world.clip(new ClipContext(safeOrigin, eye, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, mc.player));
        if (hit.getType() != HitResult.Type.MISS) {
            eye = hit.getLocation().add(safeOrigin.subtract(eye).normalize().scale(0.25));
        }

        if (eye.distanceToSqr(focus) < 0.25) {
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

        Vec3 d = focus.subtract(eye);
        event.setYaw((float) Math.toDegrees(Math.atan2(-d.x, d.z)));
        event.setPitch((float) -Math.toDegrees(Math.atan2(d.y, Math.sqrt(d.x * d.x + d.z * d.z))));
        event.setRoll(0);
        if (age >= RyujinBankaiCamera.RETURN_SETTLE) {
            cameraEnabled = false;
            restore();
        }
    }

    @SubscribeEvent
    public static void onFov(ViewportEvent.ComputeFov event) {
        if (!cameraActive() || world == null) return;
        var s = local();
        if (s == null) return;
        double age = age(s, (float) event.getPartialTick());
        if (age < DURATION && age >= 0 && event.usedConfiguredFov()) {
            float sign = (Minecraft.getInstance().player != null && Minecraft.getInstance().player.getMainArm() == HumanoidArm.LEFT) ? -1.0F : 1.0F;
            double t = RyujinBankaiTimeline.progress(age, RyujinBankaiCamera.RETURN_START, RyujinBankaiCamera.RETURN_SETTLE);
            event.setFOV(Mth.lerp(t, RyujinBankaiCamera.sample(s.anchor, s.forward, age, sign).fov(), event.getFOV()));
        }
    }

    @SubscribeEvent
    public static void warmAir(ViewportEvent.ComputeFogColor e) {
        var s=local();if(s==null || !cameraActive())return;
        float w=(float)RyujinBankaiPresentation.heat(age(s,(float)e.getPartialTick()))*.22F;
        e.setRed(Mth.lerp(w,e.getRed(),.72F));
        e.setGreen(Mth.lerp(w,e.getGreen(),.33F));
        e.setBlue(Mth.lerp(w,e.getBlue(),.10F));
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

        double heat = RyujinBankaiPresentation.heat(age);
        int warm = ((int)(heat * 22) << 24) | 0xEC5819;
        g.fillGradient(0, barHeight, width, height / 3, warm, 0x00EC5819);
        g.fillGradient(0, height * 2 / 3, width, height - barHeight, 0x00EC5819, warm);
        int bars = ((int)(255 * (1 - RyujinBankaiTimeline.progress(age,112,138))) << 24);
        g.fill(0, 0, width, barHeight, bars);
        g.fill(0, height - barHeight, width, height, bars);
        subtitle(g, "Bankai", RyujinBankaiTimeline.speechAlpha(rawAge(s,0),s.viewTicks,12,52), width, height-barHeight-20);
        subtitle(g, "Zanka no Tachi", RyujinBankaiTimeline.speechAlpha(rawAge(s,0),s.viewTicks,60,65), width, height-barHeight-20);
    }
    private static void subtitle(GuiGraphics g, String text, float alpha, int width, int y) {
        if(alpha <= .01F)return;
        var font=Minecraft.getInstance().font;
        int half=font.width(text)/2+6;
        g.fill(width/2-half,y-4,width/2+half,y+font.lineHeight+4,((int)(alpha*155)<<24)|0x140E0B);
        g.drawCenteredString(font,text,width/2,y,((int)(alpha*255)<<24)|0xFFF1D5);
    }
    @SubscribeEvent
    public static void onPlayerRender(RenderPlayerEvent.Pre e) {
        var mc=Minecraft.getInstance();
        if(world!=mc.level)return;
        var s=SCENES.get(e.getEntity().getId());
        double age=s==null ? -1 : age(s,e.getPartialTick());
        // Camera.detached is computed before the angle event. Hide the actor on the restore frame too.
        if(e.getEntity()==mc.player && (previousCamera==CameraType.FIRST_PERSON || mc.options.getCameraType()==CameraType.FIRST_PERSON)
                && (world.getGameTime()<=hidePlayerUntil || age>=RyujinBankaiCamera.RETURN_START)
                && mc.gameRenderer.getMainCamera().getPosition().distanceToSqr(mc.player.getEyePosition(e.getPartialTick()))<1.4) {
            e.setCanceled(true);return;
        }
        if(s==null || reenteringPlayerRender)return;
        double weight=RyujinBankaiTimeline.progress(age,0,10)*(1-RyujinBankaiTimeline.progress(age,110,138));
        int old=e.getPackedLight(),block=LightTexture.block(old),target=Math.max(block,(int)(11*weight));
        if(target<=block || !(e.getEntity() instanceof AbstractClientPlayer actor))return;
        reenteringPlayerRender=true;
        try {
            e.getRenderer().render(actor,Mth.lerp(e.getPartialTick(),actor.yRotO,actor.getYRot()),
                    e.getPartialTick(),e.getPoseStack(),e.getMultiBufferSource(),LightTexture.pack(target,LightTexture.sky(old)));
            e.setCanceled(true);
        } finally { reenteringPlayerRender=false; }
    }

    @SubscribeEvent
    public static void onRenderLevel(RenderLevelStageEvent e) {
        if (e.getStage() != RenderLevelStageEvent.Stage.AFTER_PARTICLES || world != Minecraft.getInstance().level || SCENES.isEmpty()) return;
        var mc = Minecraft.getInstance();
        Vec3 cameraPos = e.getCamera().getPosition();

        for (var entry : SCENES.entrySet().stream().sorted(Comparator.comparingDouble(
                (Map.Entry<Integer, Scene> entry) -> entry.getValue().anchor.distanceToSqr(cameraPos))).limit(4).toList()) {
            var scene = entry.getValue();
            var entity = world.getEntity(entry.getKey());
            float sign = (entity instanceof AbstractClientPlayer p && p.getMainArm() == HumanoidArm.LEFT) ? -1.0F : 1.0F;
            double age = age(scene, e.getPartialTick());
            if (age >= 0 && age < DURATION) {
                RyujinSunVisual.render(e.getPoseStack(), cameraPos, scene.anchor, scene.forward, age, blade(scene,entry.getKey(),e.getPartialTick()));
            }
        }
    }

    /**
     * PlayerAnimator stance layer over the independent authored beat clock of the transformation.
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
