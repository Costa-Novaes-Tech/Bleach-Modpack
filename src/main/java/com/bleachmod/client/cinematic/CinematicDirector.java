package com.bleachmod.client.cinematic;

import com.bleachmod.Reference;
import com.bleachmod.common.technique.NozarashiReleaseEffects;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Client half of release cinematics. Every viewer plays the body animation; the releasing player also gets
 * a directed camera (five shots with hard cuts), letterbox bars, a crimson flash and the form title.
 * Input is suppressed only for the local releasing player and the previous camera mode is restored.
 */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT)
public final class CinematicDirector {
    public static final ResourceLocation ANIMATION_LAYER = Reference.id("release_cinematic");
    private record Running(String kind, long start, int duration, Vec3 anchor, Vec3 forward) { }
    private static final Map<Integer, Running> RUNNING = new HashMap<>();
    private static CameraType previousCamera;
    private static Method setPosition;
    private CinematicDirector() { }

    /** Client setup: give every player an animation layer above vanilla poses. */
    public static void init() {
        PlayerAnimationAccess.REGISTER_ANIMATION_EVENT.register((player, stack) -> {
            var layer = new ModifierLayer<IAnimation>();
            stack.addAnimLayer(1000, layer);
            PlayerAnimationAccess.getPlayerAssociatedData(player).set(ANIMATION_LAYER, layer);
        });
    }

    public static void start(int entityId, String kind, int duration) {
        var mc = Minecraft.getInstance();
        if (mc.level == null || !(mc.level.getEntity(entityId) instanceof AbstractClientPlayer player)) return;
        if (NozarashiReleaseEffects.IMPACT.equals(kind)) { shake(player, duration); return; }
        if (duration <= 0) { finish(entityId, player); return; }
        RUNNING.put(entityId, new Running(kind, mc.level.getGameTime(), duration, player.position(),
                Vec3.directionFromRotation(0, player.getYRot())));
        animate(player, kind);
        if (player == mc.player) {
            if (previousCamera == null) previousCamera = mc.options.getCameraType();
            mc.options.setCameraType(CameraType.THIRD_PERSON_BACK);
        }
    }
    private static void finish(int entityId, AbstractClientPlayer player) {
        RUNNING.remove(entityId);
        if (player != null) animate(player, null);
        var mc = Minecraft.getInstance();
        if (player == mc.player && previousCamera != null) { mc.options.setCameraType(previousCamera); previousCamera = null; }
    }
    @SuppressWarnings("unchecked")
    private static void animate(AbstractClientPlayer player, String kind) {
        var data = PlayerAnimationAccess.getPlayerAssociatedData(player).get(ANIMATION_LAYER);
        if (!(data instanceof ModifierLayer<?> raw)) return;
        var layer = (ModifierLayer<IAnimation>) raw;
        if (kind == null) { layer.setAnimation(null); return; }
        var animation = PlayerAnimationRegistry.getAnimation(Reference.id(kind));
        layer.setAnimation(animation == null ? null : new KeyframeAnimationPlayer(animation));
    }

    /** Ticks since the cinematic began for that player, or -1 when none runs. */
    public static float progress(Player player, float partial) {
        var mc = Minecraft.getInstance();
        Running running = RUNNING.get(player.getId());
        return running == null || mc.level == null ? -1 : mc.level.getGameTime() - running.start + partial;
    }
    /** The Berserk skin appears in the eruption's flash, not when the form packet arrives. */
    public static boolean transformationVisible(Player player) {
        float t = progress(player, 0);
        return t < 0 || t >= NozarashiReleaseEffects.REVEAL;
    }
    private static Running local() {
        var mc = Minecraft.getInstance();
        return mc.player == null ? null : RUNNING.get(mc.player.getId());
    }

    @SubscribeEvent public static void tick(TickEvent.ClientTickEvent event) {
        var mc = Minecraft.getInstance();
        if (event.phase != TickEvent.Phase.END) return;
        if (mc.level == null) { RUNNING.clear(); previousCamera = null; shakeTicks = 0; return; }
        if (shakeTicks > 0) shakeTicks--;
        RUNNING.entrySet().removeIf(entry -> {
            var running = entry.getValue();
            if (mc.level.getGameTime() - running.start < running.duration) return false;
            var entity = mc.level.getEntity(entry.getKey());
            if (entity instanceof AbstractClientPlayer player) animate(player, null);
            if (entity == mc.player && previousCamera != null) { mc.options.setCameraType(previousCamera); previousCamera = null; }
            return true;
        });
    }

    // ---- Camera ----
    private static double smooth(double x) { x = Mth.clamp(x, 0, 1); return x * x * (3 - 2 * x); }
    private static Vec3 lerp(Vec3 a, Vec3 b, double t) { return a.add(b.subtract(a).scale(t)); }
    private record Shot(Vec3 eye, Vec3 target, float roll, float fov) { }

    private static Shot shot(Running r, float t) {
        final int erupt = NozarashiReleaseEffects.ERUPTION, grab = NozarashiReleaseEffects.GRAB, slam = NozarashiReleaseEffects.SLAM;
        Vec3 s = r.anchor, f = r.forward, right = new Vec3(-f.z, 0, f.x), up = new Vec3(0, 1, 0);
        // Rage builds as a constant tremor until the eruption; impacts add their own shakes on top.
        float rumble = t < erupt ? .15F + t / erupt * .55F : Math.max(0, .5F - (t - erupt) / 60F);
        Vec3 jitter = new Vec3(Math.sin(t * 2.7) * .05, Math.cos(t * 3.1) * .04, Math.sin(t * 1.9) * .05).scale(rumble);
        float roll = (float) Math.sin(t * 2.3) * 3 * rumble;
        if (t < 20) { // A: extreme close-up on the face, trembling with rage, slowly pushing in
            double p = smooth(t / 20);
            return new Shot(s.add(f.scale(1.35 - .45 * p)).add(right.scale(.3)).add(up.scale(1.55)).add(jitter), s.add(up.scale(1.5)), roll, 45);
        }
        if (t < erupt) { // B: overhead while he collapses and pounds the ground
            double p = (t - 20) / (erupt - 20.0);
            Vec3 orbit = f.scale(Math.cos(p * 1.2)).add(right.scale(Math.sin(p * 1.2))).scale(.5);
            return new Shot(s.add(up.scale(5.5 - 1.8 * p)).add(orbit).add(jitter), s.add(up.scale(.3)), roll + (float) (p * 35), 62);
        }
        if (t < 80) { // C: wide low angle on the skull pillar, slowly craning up
            double p = (t - erupt) / (80.0 - erupt);
            Vec3 eye = s.add(f.scale(8 + 1.5 * p)).add(right.scale(3.5)).add(up.scale(.4 + .8 * p)).add(jitter.scale(2));
            float burst = (float) Math.max(0, 1 - (t - erupt) / 10.0);
            return new Shot(eye, s.add(up.scale(2 + 6 * smooth(p))), roll, 72 + 14 * burst);
        }
        if (t < grab) { // D: contre-plongée orbit while he rises, eyes burning
            double p = (t - 80) / (grab - 80.0), angle = Math.toRadians(-35 + 55 * smooth(p));
            Vec3 around = f.scale(Math.cos(angle)).add(right.scale(Math.sin(angle)));
            return new Shot(s.add(around.scale(2.6 - .4 * p)).add(up.scale(.2)), s.add(up.scale(1.7)), roll, 52);
        }
        // E: side shot of the blade ripped overhead and hammered down, then back behind the shoulder
        double p = smooth((t - grab) / (r.duration - grab));
        Vec3 side = s.add(right.scale(3.4)).add(f.scale(1.2)).add(up.scale(1.2));
        Vec3 behind = s.add(f.scale(-4)).add(up.scale(2.2));
        float punch = (float) Math.max(0, 1 - Math.abs(t - slam) / 6.0);
        Vec3 target = lerp(s.add(f.scale(1.5)).add(up.scale(1.2)), s.add(f.scale(6)).add(up.scale(1.4)), p);
        return new Shot(lerp(side, behind, p), target, roll, 64 - 14 * punch);
    }

    // ---- Impact shakes (any viewer near a heavy blow) ----
    private static float shakeAmplitude; private static int shakeTicks, shakeLength;
    private static void shake(AbstractClientPlayer source, int strength) {
        var mc = Minecraft.getInstance();
        if (mc.player == null) return;
        double distance = mc.player.distanceTo(source);
        if (distance > 40) return;
        float amplitude = strength * .07F * (float) (1 - distance / 40);
        if (amplitude >= shakeAmplitude * shakeTicks / Math.max(1, shakeLength)) {
            shakeAmplitude = amplitude; shakeLength = shakeTicks = 6 + strength * 2;
        }
    }
    private static Vec3 shakeOffset(float partial, float[] roll) {
        if (shakeTicks <= 0) { roll[0] = 0; return Vec3.ZERO; }
        float decay = (shakeTicks - partial) / shakeLength, a = shakeAmplitude * decay * decay;
        float time = (Minecraft.getInstance().level == null ? 0 : Minecraft.getInstance().level.getGameTime()) + partial;
        roll[0] = (float) Math.sin(time * 4.1) * a * 25;
        return new Vec3(Math.sin(time * 5.3), Math.cos(time * 6.7) * .7, Math.sin(time * 4.4 + 1)).scale(a);
    }

    @SubscribeEvent public static void camera(ViewportEvent.ComputeCameraAngles event) {
        var mc = Minecraft.getInstance(); Running r = local();
        float partial = (float) event.getPartialTick();
        float[] shakeRoll = {0};
        Vec3 offset = shakeOffset(partial, shakeRoll);
        if ((r == null || mc.player == null) && offset == Vec3.ZERO) return;
        Vec3 eye;
        if (r != null && mc.player != null) {
            Shot shot = shot(r, progress(mc.player, partial));
            Vec3 look = shot.target.subtract(shot.eye);
            event.setYaw((float) (Mth.atan2(look.z, look.x) * Mth.RAD_TO_DEG) - 90);
            event.setPitch((float) -(Mth.atan2(look.y, look.horizontalDistance()) * Mth.RAD_TO_DEG));
            event.setRoll(shot.roll + shakeRoll[0]);
            eye = shot.eye;
        } else {
            event.setRoll(event.getRoll() + shakeRoll[0]);
            eye = event.getCamera().getPosition();
        }
        try {
            if (setPosition == null) setPosition = ObfuscationReflectionHelper.findMethod(net.minecraft.client.Camera.class, "m_90581_", Vec3.class);
            setPosition.invoke(event.getCamera(), eye.add(offset));
        } catch (ReflectiveOperationException | RuntimeException ignored) { /* angles still apply */ }
    }
    @SubscribeEvent public static void fov(ViewportEvent.ComputeFov event) {
        var mc = Minecraft.getInstance(); Running r = local();
        if (r != null && mc.player != null && event.usedConfiguredFov())
            event.setFOV(shot(r, progress(mc.player, (float) event.getPartialTick())).fov);
    }

    // ---- Input lock and HUD ----
    @SubscribeEvent public static void input(MovementInputUpdateEvent event) {
        if (local() == null || event.getEntity() != Minecraft.getInstance().player) return;
        var in = event.getInput();
        in.forwardImpulse = 0; in.leftImpulse = 0; in.jumping = false; in.shiftKeyDown = false;
        in.up = in.down = in.left = in.right = false;
    }
    @SubscribeEvent public static void interact(InputEvent.InteractionKeyMappingTriggered event) {
        if (local() != null) { event.setCanceled(true); event.setSwingHand(false); }
    }
    @SubscribeEvent public static void hideHud(RenderGuiOverlayEvent.Pre event) { if (local() != null) event.setCanceled(true); }
    @SubscribeEvent public static void overlay(RenderGuiEvent.Post event) {
        var mc = Minecraft.getInstance(); Running r = local();
        if (r == null || mc.player == null) return;
        float t = progress(mc.player, event.getPartialTick());
        GuiGraphics g = event.getGuiGraphics();
        int w = g.guiWidth(), h = g.guiHeight();
        // Letterbox in and out.
        float bars = (float) smooth(Math.min(t / 8, (r.duration - t) / 8)) * h * .12F;
        g.fill(0, 0, w, (int) bars, 0xFF000000); g.fill(0, h - (int) bars, w, h, 0xFF000000);
        // Crimson pulse while the pressure builds, white-red flash at the eruption hiding the skin change.
        if (t < NozarashiReleaseEffects.ERUPTION) g.fill(0, 0, w, h, ((int) ((.08 + .08 * Math.sin(t * .6)) * t / NozarashiReleaseEffects.ERUPTION * 255) << 24) | 0x900000);
        float flash = (float) Math.max(0, 1 - Math.abs(t - (NozarashiReleaseEffects.ERUPTION + 1)) / 5.0);
        if (flash > 0) g.fill(0, 0, w, h, ((int) (flash * 235) << 24) | (flash > .6F ? 0xFFE0E0 : 0xD01010));
        if (t > NozarashiReleaseEffects.ERUPTION) g.fill(0, 0, w, h, (int) (Math.max(0, 1 - (t - NozarashiReleaseEffects.ERUPTION) / 50) * 80) << 24 | 0x500000);
        for (int pound : NozarashiReleaseEffects.POUNDS) { // red pulse on every fist blow
            float hit = (float) Math.max(0, 1 - Math.abs(t - pound) / 3.0);
            if (hit > 0) g.fill(0, 0, w, h, ((int) (hit * 110) << 24) | 0xB00000);
        }
        float slamFlash = (float) Math.max(0, 1 - Math.abs(t - NozarashiReleaseEffects.SLAM) / 4.0);
        if (slamFlash > 0) g.fill(0, 0, w, h, ((int) (slamFlash * 160) << 24) | 0xE02020);
        if (t > NozarashiReleaseEffects.SLAM - 2) { // Form title
            float alpha = (float) Math.min(1, (t - NozarashiReleaseEffects.SLAM + 2) / 5) * (float) Math.min(1, (r.duration - t) / 4);
            if (alpha > .05F) {
                var title = Component.translatable("form.bleachmod.nozarashi.bankai");
                g.pose().pushPose(); g.pose().translate(w / 2F, h * .7F, 0); g.pose().scale(3, 3, 1);
                int color = ((int) (alpha * 255) << 24) | 0xE01B1B;
                g.drawCenteredString(mc.font, title, 0, 0, color);
                g.pose().popPose();
            }
        }
    }
}
