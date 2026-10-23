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
 * Total presentation duration: 400 ticks (20 seconds).
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
        if(fresh)com.bleachmod.client.model.HyorinmaruFrozenGround.forget(anchor);
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
        var removed=SCENES.remove(id);
        if(removed!=null)com.bleachmod.client.model.HyorinmaruFrozenGround.forget(removed.anchor);
        HyorinmaruEntranceAudio.stop(id);
        var mc = Minecraft.getInstance();
        if (mc.player != null && mc.player.getId() == id) {
            restoreCamera();
        }
    }

    private static void clear() {
        SCENES.clear();
        HyorinmaruEntranceAudio.clear();
        com.bleachmod.client.model.HyorinmaruFrozenGround.clear();
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
        var frame=com.bleachmod.common.visual.HyorinmaruCameraPath.sample(time);
        Vec3 side=new Vec3(-s.forward.z,0,s.forward.x);
        Vec3 eye=s.anchor.add(side.scale(frame.eye().x)).add(0,frame.eye().y,0).add(s.forward.scale(frame.eye().z));
        Vec3 focus=s.anchor.add(side.scale(frame.focus().x)).add(0,frame.focus().y,0).add(s.forward.scale(frame.focus().z));
        float fov=frame.fov();
        if(time>com.bleachmod.common.visual.HyorinmaruCameraPath.RETURN_START && mc.player!=null){
            double t=com.bleachmod.common.visual.HyorinmaruCameraPath.ease((time-com.bleachmod.common.visual.HyorinmaruCameraPath.RETURN_START)/(DURATION/2.0-com.bleachmod.common.visual.HyorinmaruCameraPath.RETURN_START));
            Vec3 normalEye=mc.player.getEyePosition(), look=mc.player.getViewVector(1);
            Vec3 end=normalEye;
            if(previousCameraType==CameraType.THIRD_PERSON_BACK)end=normalEye.subtract(look.scale(4));
            if(previousCameraType==CameraType.THIRD_PERSON_FRONT)end=normalEye.add(look.scale(4));
            Vec3 targetDirection=previousCameraType==CameraType.THIRD_PERSON_FRONT?look.scale(-1):look;
            Vec3 direction=com.bleachmod.common.visual.HyorinmaruCameraPath.blendDirection(focus.subtract(eye),targetDirection,t);
            eye=com.bleachmod.common.visual.HyorinmaruCameraPath.returnEye(eye,normalEye,end,look,t);
            focus=eye.add(direction.scale(8));
            fov=(float)Mth.lerp(t,fov,mc.options.fov().get());
        }
        return new Shot(eye,focus,fov);
    }

    /** Do not show the inside of the caster's head on the final first-person approach. */
    @SubscribeEvent
    public static void hidePlayerOnReturn(net.minecraftforge.client.event.RenderPlayerEvent.Pre event) {
        var mc=Minecraft.getInstance();var s=local();
        if(!cameraActive || s==null || event.getEntity()!=mc.player || previousCameraType!=CameraType.FIRST_PERSON)return;
        if(com.bleachmod.common.visual.HyorinmaruTimeline.motionAge(age(s,event.getPartialTick()))>=com.bleachmod.common.visual.HyorinmaruCameraPath.RETURN_START
                && mc.gameRenderer.getMainCamera().getPosition().distanceToSqr(mc.player.getEyePosition())<1.8)event.setCanceled(true);
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

        Shot sh = computeShot(s, com.bleachmod.common.visual.HyorinmaruTimeline.motionAge(age));
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
            com.bleachmod.client.model.HyorinmaruBreathVisual.render(e.getPoseStack(),e.getCamera().getPosition(),scene.anchor,scene.forward,com.bleachmod.common.visual.HyorinmaruTimeline.motionAge(age(scene,e.getPartialTick())));
            com.bleachmod.client.model.HyorinmaruFrozenGround.render(e.getPoseStack(),e.getCamera().getPosition(),scene.anchor,scene.forward,age(scene,e.getPartialTick()));
        }
    }

    @SubscribeEvent
    public static void onFov(ViewportEvent.ComputeFov event) {
        if (!cameraActive || world == null) return;
        var s = local();
        if (s == null) return;
        double age = age(s, (float) event.getPartialTick());
        if (age < DURATION && age >= 0 && event.usedConfiguredFov()) {
            event.setFOV(computeShot(s, com.bleachmod.common.visual.HyorinmaruTimeline.motionAge(age)).fov);
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
        double envelope=smooth(age/12)*(1-smooth((age-(DURATION-16))/16));
        int barHeight = (int) (g.guiHeight() * 0.065 * envelope);

        // Letterbox cinematográfico suave
        g.fill(0, 0, g.guiWidth(), barHeight, 0xEE000000);
        g.fill(0, g.guiHeight() - barHeight, g.guiWidth(), g.guiHeight(), 0xEE000000);

        var font = Minecraft.getInstance().font;
        if (age >= 20 && age < 100) {
            g.drawCenteredString(font, "Bankai", g.guiWidth() / 2, g.guiHeight() - barHeight - 20, 0xCEF0FF);
        } else if (age >= 330 && age < 390) {
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

        for(var entry:SCENES.entrySet())HyorinmaruEntranceAudio.update(entry.getKey(),age(entry.getValue(),0),entry.getValue().anchor,mc.player!=null && mc.player.getId()==entry.getKey());
        SCENES.entrySet().removeIf(e -> {
            boolean done = (world.getGameTime() - e.getValue().start) >= DURATION;
            if (done && mc.player != null && mc.player.getId() == e.getKey()) {
                restoreCamera();
            }
            if(done){HyorinmaruEntranceAudio.stop(e.getKey());com.bleachmod.client.model.HyorinmaruFrozenGround.forget(e.getValue().anchor);}
            return done;
        });
    }
}
