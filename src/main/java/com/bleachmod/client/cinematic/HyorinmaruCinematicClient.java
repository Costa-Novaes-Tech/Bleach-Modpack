package com.bleachmod.client.cinematic;

import com.bleachmod.Reference;
import net.minecraft.client.Camera;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;

/**
 * Client-side camera director and presentation renderer for Daiguren Hyorinmaru's Bankai Transformation.
 */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT)
public final class HyorinmaruCinematicClient {
    public static final int DURATION = 140;

    private record Scene(long start, Vec3 anchor, Vec3 forward, float yaw) { }
    private record Shot(Vec3 eye, Vec3 focus, float fov) { }

    private static final Map<Integer, Scene> SCENES = new HashMap<>();
    private static net.minecraft.client.multiplayer.ClientLevel world;
    private static CameraType previousCameraType;
    private static boolean cameraActive = false;
    private static java.lang.reflect.Method cameraPositionMethod;

    private HyorinmaruCinematicClient() { }

    public static void init() {
        // Reserved for initialization hooks if needed
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

        int elapsed = tag.getInt("elapsed");
        if (elapsed < 0 || elapsed >= DURATION) {
            finish(id);
            return;
        }

        Vec3 anchor = new Vec3(tag.getDouble("x"), tag.getDouble("y"), tag.getDouble("z"));
        float yaw = tag.getFloat("yaw");
        if (!Double.isFinite(anchor.lengthSqr()) || !Float.isFinite(yaw)) return;

        boolean fresh = !SCENES.containsKey(id);
        SCENES.put(id, new Scene(mc.level.getGameTime() - elapsed, anchor, Vec3.directionFromRotation(0, yaw), yaw));

        if (fresh && mc.player != null && mc.player.getId() == id) {
            previousCameraType = mc.options.getCameraType();
            cameraActive = true;
        }
    }

    private static void finish(int id) {
        SCENES.remove(id);
        var mc = Minecraft.getInstance();
        if (mc.player != null && mc.player.getId() == id) {
            restoreCamera();
        }
    }

    private static void clear() {
        SCENES.clear();
        restoreCamera();
    }

    private static void restoreCamera() {
        if (cameraActive) {
            cameraActive = false;
            var mc = Minecraft.getInstance();
            if (previousCameraType != null && mc.options != null) {
                mc.options.setCameraType(previousCameraType);
            }
        }
    }

    private static Scene local() {
        var mc = Minecraft.getInstance();
        if (mc.player == null) return null;
        return SCENES.get(mc.player.getId());
    }

    private static double age(Scene s, float partial) {
        if (world == null) return 0;
        return (world.getGameTime() - s.start) + partial;
    }

    private static Shot computeShot(Scene s, double time) {
        var mc = Minecraft.getInstance();
        float baseFov = mc.options.fov().get().floatValue();
        Vec3 forward = s.forward;
        Vec3 left = new Vec3(-forward.z, 0, forward.x);
        Vec3 base = s.anchor.add(0, 1.2, 0);

        // Phase 1 (Ticks 0-30): Low close-up on the Shinigami, slight FOV punch
        if (time < 30.0) {
            double p = time / 30.0;
            Vec3 eye = base.add(forward.scale(2.2 - p * 0.4)).add(0, 0.2 + p * 0.4, 0);
            Vec3 focus = base.add(0, 0.4, 0);
            float fov = (float) Mth.lerp(p, baseFov, baseFov * 0.88f);
            return new Shot(eye, focus, fov);
        }
        // Phase 2 (Ticks 30-75): Orbit upward and pull back showing the rising Ice Hydra
        else if (time < 75.0) {
            double p = (time - 30.0) / 45.0;
            double angle = p * Math.PI * 0.45;
            double dist = Mth.lerp(p, 2.5, 7.5);
            double height = Mth.lerp(p, 0.6, 3.2);

            Vec3 offset = forward.scale(Math.cos(angle) * dist)
                    .add(left.scale(Math.sin(angle) * dist))
                    .add(0, height, 0);
            Vec3 eye = base.add(offset);
            Vec3 focus = base.add(0, 1.8, 0);
            float fov = (float) Mth.lerp(p, baseFov * 0.88f, baseFov * 1.05f);
            return new Shot(eye, focus, fov);
        }
        // Phase 3 (Ticks 75-120): Majestic panoramic angle of the 3 Hydra heads and wings
        else if (time < 120.0) {
            double p = (time - 75.0) / 45.0;
            double angle = Math.PI * 0.45 + p * 0.2;
            Vec3 eye = base.add(forward.scale(Math.cos(angle) * 7.5))
                    .add(left.scale(Math.sin(angle) * 7.5))
                    .add(0, 3.2 - p * 0.6, 0);
            Vec3 focus = base.add(0, 2.0, 0);
            return new Shot(eye, focus, baseFov * 1.02f);
        }
        // Phase 4 (Ticks 120-140): Smooth transition back to player view
        else {
            double p = (time - 120.0) / 20.0;
            Vec3 normalEye = mc.player != null ? mc.player.getEyePosition() : base;
            Vec3 startEye = base.add(forward.scale(5.0)).add(0, 2.0, 0);
            Vec3 eye = startEye.lerp(normalEye, p);
            Vec3 focus = normalEye.add(forward.scale(10.0));
            float fov = (float) Mth.lerp(p, baseFov * 1.02f, baseFov);
            return new Shot(eye, focus, fov);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onCameraAngles(ViewportEvent.ComputeCameraAngles event) {
        if (!cameraActive || world == null) return;
        var s = local();
        if (s == null) return;

        var mc = Minecraft.getInstance();
        double age = age(s, (float) event.getPartialTick());
        if (age >= DURATION) {
            restoreCamera();
            return;
        }

        Shot sh = computeShot(s, age);
        Vec3 eye = sh.eye;
        Vec3 safeOrigin = mc.player != null ? mc.player.getEyePosition() : s.anchor.add(0, 1.6, 0);

        var hit = world.clip(new ClipContext(safeOrigin, eye, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, mc.player));
        if (hit.getType() != HitResult.Type.MISS) {
            eye = hit.getLocation().add(safeOrigin.subtract(eye).normalize().scale(0.2));
        }

        try {
            if (cameraPositionMethod == null) {
                cameraPositionMethod = net.minecraftforge.fml.util.ObfuscationReflectionHelper.findMethod(Camera.class, "m_90581_", Vec3.class);
            }
            cameraPositionMethod.invoke(event.getCamera(), eye);
        } catch (ReflectiveOperationException | RuntimeException ignored) {
            cameraActive = false;
            restoreCamera();
            return;
        }

        Vec3 d = sh.focus.subtract(eye);
        event.setYaw((float) Math.toDegrees(Math.atan2(-d.x, d.z)));
        event.setPitch((float) -Math.toDegrees(Math.atan2(d.y, Math.sqrt(d.x * d.x + d.z * d.z))));
        event.setRoll(0);
    }

    @SubscribeEvent
    public static void onFov(ViewportEvent.ComputeFov event) {
        if (!cameraActive || world == null) return;
        var s = local();
        if (s == null) return;
        double age = age(s, (float) event.getPartialTick());
        if (age < DURATION && event.usedConfiguredFov()) {
            event.setFOV(computeShot(s, age).fov);
        }
    }

    @SubscribeEvent
    public static void onHudOverlay(RenderGuiOverlayEvent.Pre event) {
        if (cameraActive && local() != null) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onGuiRender(RenderGuiEvent.Post event) {
        if (!cameraActive) return;
        var s = local();
        if (s == null) return;

        double age = age(s, event.getPartialTick());
        if (age >= DURATION) return;

        GuiGraphics g = event.getGuiGraphics();
        int barHeight = (int) (g.guiHeight() * 0.08);

        // Letterbox cinematográfico
        g.fill(0, 0, g.guiWidth(), barHeight, 0xFF000000);
        g.fill(0, g.guiHeight() - barHeight, g.guiWidth(), g.guiHeight(), 0xFF000000);

        var font = Minecraft.getInstance().font;
        if (age > 15 && age < 50) {
            g.drawCenteredString(font, "Bankai", g.guiWidth() / 2, g.guiHeight() - barHeight - 24, 0xCEF0FF);
        } else if (age >= 50 && age < 125) {
            g.drawCenteredString(font, "Daiguren Hyōrinmaru", g.guiWidth() / 2, g.guiHeight() - barHeight - 24, 0x8BE0FF);
        }
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        var mc = Minecraft.getInstance();
        if (mc.level == null) {
            clear();
            return;
        }
        if (world != mc.level) {
            clear();
            world = mc.level;
            return;
        }
        SCENES.entrySet().removeIf(e -> {
            boolean done = (world.getGameTime() - e.getValue().start) >= DURATION;
            if (done && mc.player != null && mc.player.getId() == e.getKey()) {
                restoreCamera();
            }
            return done;
        });
    }
}
