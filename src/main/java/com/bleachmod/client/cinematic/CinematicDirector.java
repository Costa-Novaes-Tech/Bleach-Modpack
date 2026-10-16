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
        if (mc.level == null) { RUNNING.clear(); previousCamera = null; return; }
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
        Vec3 s = r.anchor, f = r.forward, right = new Vec3(-f.z, 0, f.x), up = new Vec3(0, 1, 0);
        // Impact shake: strongest at the eruption, decaying over 14 ticks.
        float impact = (float) Math.max(0, 1 - Math.abs(t - NozarashiReleaseEffects.ERUPTION) / 14.0);
        float rumble = t < NozarashiReleaseEffects.ERUPTION ? t / NozarashiReleaseEffects.ERUPTION * .25F : 0;
        float shake = (impact * impact * 2.5F + rumble);
        Vec3 jitter = new Vec3(Math.sin(t * 2.7) * .06, Math.cos(t * 3.1) * .05, Math.sin(t * 1.9) * .06).scale(shake);
        float roll = (float) Math.sin(t * 2.3) * 4 * shake;
        if (t < 12) { // A: close-up on the face, slowly pushing in
            double p = smooth(t / 12);
            return new Shot(s.add(f.scale(1.5 - .4 * p)).add(right.scale(.35)).add(up.scale(1.55)), s.add(up.scale(1.5)), roll, 50);
        }
        if (t < 30) { // B: overhead as he collapses, slow spin
            double p = (t - 12) / 18;
            Vec3 orbit = f.scale(Math.cos(p * .8)).add(right.scale(Math.sin(p * .8))).scale(.4);
            return new Shot(s.add(up.scale(5 - 1.2 * p)).add(orbit).add(jitter), s.add(up.scale(.4)), roll + (float) (p * 25), 60);
        }
        if (t < 55) { // C: wide low angle on the skull pillar
            double p = (t - 30) / 25;
            Vec3 eye = s.add(f.scale(7 + p)).add(right.scale(3.2)).add(up.scale(.4)).add(jitter.scale(2));
            return new Shot(eye, s.add(up.scale(2 + 4 * smooth(p))), roll, 70 + 12 * impact);
        }
        if (t < 80) { // D: contre-plongée as he rises, yellow eyes catching the light
            double p = (t - 55) / 25, angle = Math.toRadians(-25 + 40 * smooth(p));
            Vec3 around = f.scale(Math.cos(angle)).add(right.scale(Math.sin(angle)));
            return new Shot(s.add(around.scale(2.4)).add(up.scale(.25)), s.add(up.scale(1.7)), roll, 55);
        }
        // E: sweep from his side to behind the shoulder, handing control back
        double p = smooth((t - 80) / 20);
        Vec3 side = s.add(right.scale(3)).add(f.scale(1.5)).add(up.scale(1.4));
        Vec3 behind = s.add(f.scale(-4)).add(up.scale(2.2));
        float punch = (float) Math.max(0, 1 - Math.abs(t - NozarashiReleaseEffects.GRAB) / 6.0);
        return new Shot(lerp(side, behind, p), lerp(s.add(up.scale(1.4)), s.add(f.scale(6)).add(up.scale(1.4)), p), roll, 62 - 10 * punch);
    }

    @SubscribeEvent public static void camera(ViewportEvent.ComputeCameraAngles event) {
        var mc = Minecraft.getInstance(); Running r = local();
        if (r == null || mc.player == null) return;
        Shot shot = shot(r, progress(mc.player, (float) event.getPartialTick()));
        Vec3 look = shot.target.subtract(shot.eye);
        event.setYaw((float) (Mth.atan2(look.z, look.x) * Mth.RAD_TO_DEG) - 90);
        event.setPitch((float) -(Mth.atan2(look.y, look.horizontalDistance()) * Mth.RAD_TO_DEG));
        event.setRoll(shot.roll);
        try {
            if (setPosition == null) setPosition = ObfuscationReflectionHelper.findMethod(net.minecraft.client.Camera.class, "m_90581_", Vec3.class);
            setPosition.invoke(event.getCamera(), shot.eye);
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
        if (t < NozarashiReleaseEffects.ERUPTION) g.fill(0, 0, w, h, ((int) ((.08 + .08 * Math.sin(t * .6)) * t / 30 * 255) << 24) | 0x900000);
        float flash = (float) Math.max(0, 1 - Math.abs(t - (NozarashiReleaseEffects.ERUPTION + 1)) / 5.0);
        if (flash > 0) g.fill(0, 0, w, h, ((int) (flash * 235) << 24) | (flash > .6F ? 0xFFE0E0 : 0xD01010));
        if (t > NozarashiReleaseEffects.ERUPTION) g.fill(0, 0, w, h, (int) (Math.max(0, 1 - (t - 31) / 40) * 70) << 24 | 0x500000);
        if (t > 84) { // Form title
            float alpha = (float) Math.min(1, (t - 84) / 5) * (float) Math.min(1, (r.duration - t) / 4);
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
