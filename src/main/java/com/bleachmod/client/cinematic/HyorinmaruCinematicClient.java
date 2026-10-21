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
 * Total presentation duration: 200 ticks (10 seconds).
 */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT)
public final class HyorinmaruCinematicClient {
    public static final int DURATION = com.bleachmod.common.technique.HyorinmaruCinematicService.DURATION;

    private record Scene(long start, Vec3 anchor, Vec3 forward, float yaw) { }
    private record Shot(Vec3 eye, Vec3 focus, float fov) { }

    private static final Map<Integer, Scene> SCENES = new HashMap<>();
    private static net.minecraft.client.multiplayer.ClientLevel world;
    private static CameraType previousCameraType;
    private static boolean cameraActive = false;
    private static java.lang.reflect.Method cameraPositionMethod;

    private HyorinmaruCinematicClient() { }

    public static void init() { }

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

        if (fresh && mc.player != null && mc.player.getId() == id) {
            if (previousCameraType == null) {
                previousCameraType = mc.options.getCameraType();
            }
            cameraActive = true;
            mc.options.setCameraType(CameraType.THIRD_PERSON_BACK);
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
            previousCameraType = null;
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

    private static double smooth(double t) {
        t=Mth.clamp(t,0,1);return t*t*(3-2*t);
    }

    private static Shot computeShot(Scene s, double time) {
        var mc=Minecraft.getInstance();
        float fov=mc.options.fov().get().floatValue();
        Vec3 side=new Vec3(-s.forward.z,0,s.forward.x);
        Vec3 close=s.anchor.add(s.forward.scale(2.8)).add(0,1.7,0);
        Vec3 face=s.anchor.add(0,1.45,0);
        Vec3 wide=s.anchor.add(s.forward.scale(24)).add(side.scale(7)).add(0,8,0);
        Vec3 whole=s.anchor.subtract(s.forward.scale(3)).add(0,6.5,0);
        Vec3 finale=s.anchor.add(s.forward.scale(23)).add(side.scale(-5)).add(0,7,0);
        if(time<25)return new Shot(close,face,fov*.94F);
        if(time<88){
            double t=smooth((time-25)/63);
            return new Shot(close.lerp(wide,t),face.lerp(whole,t),(float)Mth.lerp(t,fov*.94F,fov));
        }
        if(time<184){
            double t=smooth((time-88)/96);
            return new Shot(wide.lerp(finale,t),whole,fov);
        }
        // Return from the actual preceding shot, respecting the previous F5 mode.
        double t=smooth((time-184)/16);
        Vec3 eye=mc.player!=null?mc.player.getEyePosition():face;
        Vec3 look=mc.player!=null?mc.player.getViewVector(1):s.forward;
        Vec3 end=eye,focus=eye.add(look.scale(6));
        if(previousCameraType==CameraType.THIRD_PERSON_BACK)end=eye.subtract(look.scale(4));
        if(previousCameraType==CameraType.THIRD_PERSON_FRONT){end=eye.add(look.scale(4));focus=eye;}
        return new Shot(finale.lerp(end,t),whole.lerp(focus,t),fov);
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onCameraAngles(ViewportEvent.ComputeCameraAngles event) {
        if (!cameraActive || world == null) return;
        var s = local();
        if (s == null) {
            restoreCamera();
            return;
        }

        var mc = Minecraft.getInstance();
        double age = age(s, (float) event.getPartialTick());
        if (age >= DURATION || age < 0) {
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

        if (eye.distanceToSqr(sh.focus) < 0.25) {
            restoreCamera();
            return;
        }

        try {
            if (cameraPositionMethod == null) {
                cameraPositionMethod = net.minecraftforge.fml.util.ObfuscationReflectionHelper.findMethod(Camera.class, "m_90581_", Vec3.class);
            }
            cameraPositionMethod.invoke(event.getCamera(), eye);
        } catch (ReflectiveOperationException | RuntimeException ignored) {
            restoreCamera();
            return;
        }

        Vec3 d = sh.focus.subtract(eye);
        event.setYaw((float) Math.toDegrees(Math.atan2(-d.x, d.z)));
        event.setPitch((float) -Math.toDegrees(Math.atan2(d.y, Math.sqrt(d.x * d.x + d.z * d.z))));
        event.setRoll(0);
    }

    @SubscribeEvent public static void breathVisual(net.minecraftforge.client.event.RenderLevelStageEvent e){
        if(e.getStage()!=net.minecraftforge.client.event.RenderLevelStageEvent.Stage.AFTER_PARTICLES || world!=Minecraft.getInstance().level)return;
        int count=0;for(var scene:SCENES.values()){
            if(++count>6)break;
            com.bleachmod.client.model.HyorinmaruBreathVisual.render(e.getPoseStack(),e.getCamera().getPosition(),scene.anchor,scene.forward,age(scene,e.getPartialTick()));
        }
    }

    @SubscribeEvent
    public static void onFov(ViewportEvent.ComputeFov event) {
        if (!cameraActive || world == null) return;
        var s = local();
        if (s == null) return;
        double age = age(s, (float) event.getPartialTick());
        if (age < DURATION && age >= 0 && event.usedConfiguredFov()) {
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
        if (age >= DURATION || age < 0) return;

        GuiGraphics g = event.getGuiGraphics();
        int barHeight = (int) (g.guiHeight() * 0.065);

        // Letterbox cinematográfico suave
        g.fill(0, 0, g.guiWidth(), barHeight, 0xEE000000);
        g.fill(0, g.guiHeight() - barHeight, g.guiWidth(), g.guiHeight(), 0xEE000000);

        var font = Minecraft.getInstance().font;
        if (age >= 10 && age < 65) {
            g.drawCenteredString(font, "Bankai", g.guiWidth() / 2, g.guiHeight() - barHeight - 20, 0xCEF0FF);
        } else if (age >= 165 && age < 195) {
            g.drawCenteredString(font, "Daiguren Hyōrinmaru", g.guiWidth() / 2, g.guiHeight() - barHeight - 20, 0x8BE0FF);
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

        // Se o jogador pressionar qualquer tecla de movimento, ataque, pulo ou uso, restaura a câmera imediatamente!
        if (cameraActive && mc.player != null && (mc.player.hurtTime > 0
                || mc.options.keyUp.isDown()
                || mc.options.keyDown.isDown()
                || mc.options.keyLeft.isDown()
                || mc.options.keyRight.isDown()
                || mc.options.keyAttack.isDown()
                || mc.options.keyUse.isDown()
                || mc.options.keyJump.isDown()
                || CinematicDirector.localActive())) {
            restoreCamera();
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
