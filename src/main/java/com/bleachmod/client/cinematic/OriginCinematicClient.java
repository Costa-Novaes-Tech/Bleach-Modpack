package com.bleachmod.client.cinematic;

import com.bleachmod.Reference;
import com.bleachmod.common.network.s2c.OriginSceneS2C;
import com.bleachmod.common.visual.OriginTimeline;
import net.minecraft.client.*;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import java.lang.reflect.Method;
import java.util.*;

/** Directed only for the owner. Tracking clients see the same actor poses without losing control. */
@Mod.EventBusSubscriber(modid=Reference.MOD_ID,value=Dist.CLIENT)
public final class OriginCinematicClient {
    private static final Map<Integer,OriginSceneS2C> SCENES=new HashMap<>();
    private static ClientLevel world;
    private static CameraType previous;
    private static Method setPosition;
    private static SimpleSoundInstance voice;
    private static boolean spoken;
    private OriginCinematicClient() { }
    public static void init() {
        dev.kosmx.playerAnim.minecraftApi.PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(
                Reference.id("origin_pose"),6000,PlayerPose::new);
    }
    private static final class PlayerPose implements dev.kosmx.playerAnim.api.layered.IAnimation {
        private final net.minecraft.client.player.AbstractClientPlayer player;
        PlayerPose(net.minecraft.client.player.AbstractClientPlayer p) { player=p; }
        @Override public void setupAnim(float partial) { }
        @Override public boolean isActive() { return SCENES.containsKey(player.getId()); }
        @Override public dev.kosmx.playerAnim.core.util.Vec3f get3DTransform(String part,dev.kosmx.playerAnim.api.TransformType type,
                float partial,dev.kosmx.playerAnim.core.util.Vec3f value) {
            var s=SCENES.get(player.getId());if(s==null)return value;
            float t=age(s,partial);
            float recoil=OriginTimeline.ease((t-173)/5)*(1-OriginTimeline.ease((t-188)/13));
            float contact=(float)(OriginTimeline.ease(t,166,OriginTimeline.THRUST)*(1-OriginTimeline.ease(t,190,210)));
            float walk=(float)(Math.sin((t-128)*.46)*.5*OriginTimeline.ease((t-128)/6)*(1-OriginTimeline.ease((t-164)/10)));
            float cut=OriginTimeline.ease((t-212)/9),settle=OriginTimeline.ease((t-226)/20);
            if(type==dev.kosmx.playerAnim.api.TransformType.POSITION&&part.equals("body"))
                return new dev.kosmx.playerAnim.core.util.Vec3f(0,0,0);
            if(type==dev.kosmx.playerAnim.api.TransformType.ROTATION) {
                return switch(part) {
                    case "body"->new dev.kosmx.playerAnim.core.util.Vec3f(0,0,0);
                    case "torso"->new dev.kosmx.playerAnim.core.util.Vec3f(.04F*contact-.07F*recoil,(cut-settle)*-.3F,.04F*recoil);
                    case "head"->new dev.kosmx.playerAnim.core.util.Vec3f(.04F*contact-.17F*recoil,0,-.06F*recoil);
                    case "rightArm"->new dev.kosmx.playerAnim.core.util.Vec3f(-walk-.6F*recoil-1.4F*cut+1.2F*settle,-.2F*recoil,-.1F-.45F*cut+.3F*settle);
                    case "leftArm"->new dev.kosmx.playerAnim.core.util.Vec3f(walk-.1F*contact-.2F*recoil,.1F,-.12F);
                    case "rightLeg"->new dev.kosmx.playerAnim.core.util.Vec3f(walk,.03F,0);
                    case "leftLeg"->new dev.kosmx.playerAnim.core.util.Vec3f(-walk,-.03F,0);
                    default->value;
                };
            }
            return value;
        }
    }
    public static void receive(OriginSceneS2C scene) {
        var mc=Minecraft.getInstance(); if(mc.level==null)return;
        if(world!=mc.level) { clear();world=mc.level; }
        if(scene.stop()) {
            SCENES.remove(scene.playerId());
            if(mc.player!=null&&scene.playerId()==mc.player.getId())restore();
            return;
        }
        SCENES.put(scene.playerId(),scene);
        if(mc.player!=null&&scene.playerId()==mc.player.getId()) {
            if(mc.screen instanceof com.bleachmod.client.gui.ConfirmRaceScreen||mc.screen instanceof com.bleachmod.client.gui.CharacterAppearanceScreen)mc.setScreen(null);
            if(previous==null)previous=mc.options.getCameraType();
            mc.options.setCameraType(CameraType.THIRD_PERSON_BACK);spoken=false;
        }
    }
    public static boolean active() { return local()!=null; }
    private static OriginSceneS2C local() {
        var mc=Minecraft.getInstance();return mc.player==null?null:SCENES.get(mc.player.getId());
    }
    public static float age(OriginSceneS2C s,float partial) {
        var mc=Minecraft.getInstance();return mc.level==null?0:mc.level.getGameTime()-s.start()+partial;
    }
    public static float localAge() { var s=local();return s==null?-1:age(s,0); }
    private static Vec3 worldPoint(OriginSceneS2C s,Vec3 point) {
        return s.anchor().add(new Vec3(s.forward().z,0,-s.forward().x).scale(point.x)).add(0,point.y,0).add(s.forward().scale(point.z));
    }
    private static void restore() {
        var mc=Minecraft.getInstance();
        if(previous!=null) { mc.options.setCameraType(previous);previous=null; }
        if(voice!=null)mc.getSoundManager().stop(voice);
        voice=null;spoken=false;
    }
    private static void clear() { SCENES.clear();restore(); }
    @SubscribeEvent public static void tick(TickEvent.ClientTickEvent e) {
        if(e.phase!=TickEvent.Phase.END)return;
        var mc=Minecraft.getInstance();
        if(mc.level==null||world!=mc.level||mc.player==null) { clear();world=mc.level;return; }
        // Timeout is a fail-safe for a missed stop packet, death or dimension changes.
        SCENES.values().removeIf(s->age(s,0)>OriginTimeline.END+40 || mc.level.getEntity(s.playerId())==null);
        for(var scene:SCENES.values())if(mc.level.getEntity(scene.playerId()) instanceof net.minecraft.world.entity.LivingEntity actor) {
            float yaw=(float)Math.toDegrees(Math.atan2(-scene.forward().x,scene.forward().z))+OriginTimeline.playerTurn(age(scene,0));
            actor.setYRot(yaw);actor.yRotO=yaw;actor.setXRot(0);actor.xRotO=0;
            actor.setYBodyRot(yaw);actor.yBodyRotO=yaw;actor.setYHeadRot(yaw);actor.yHeadRotO=yaw;
        }
        var s=local();if(s==null) { if(previous!=null)restore();return; }
        if(!mc.player.isAlive()) { clear();return; }
        mc.options.setCameraType(CameraType.THIRD_PERSON_BACK);
        mc.getToasts().clear();
        mc.player.setDeltaMovement(Vec3.ZERO);
        for(var key:List.of(mc.options.keyAttack,mc.options.keyUse,mc.options.keyJump,mc.options.keyUp,mc.options.keyDown,
                mc.options.keyLeft,mc.options.keyRight,mc.options.keySprint,mc.options.keyShift,mc.options.keyInventory,mc.options.keyDrop))key.setDown(false);
        if(age(s,0)>=38&&!spoken) {
            spoken=true;
            voice=SimpleSoundInstance.forUI(SoundEvent.createVariableRangeEvent(Reference.id("rukia_origin")),1,1);
            mc.getSoundManager().play(voice);
        }
    }
    @SubscribeEvent public static void camera(ViewportEvent.ComputeCameraAngles e) {
        var s=local();if(s==null)return;
        var shot=OriginTimeline.shot(age(s,(float)e.getPartialTick()));
        Vec3 eye=worldPoint(s,shot.eye()),target=worldPoint(s,shot.target()),look=target.subtract(eye);
        float shake=(float)Math.max(0,1-Math.abs(age(s,(float)e.getPartialTick())-188)/14F)*.12F;
        eye=eye.add(Math.sin(age(s,0)*3.7)*shake,Math.cos(age(s,0)*4.1)*shake*.5,0);
        e.setYaw((float)(Mth.atan2(look.z,look.x)*Mth.RAD_TO_DEG)-90);
        e.setPitch((float)-(Mth.atan2(look.y,look.horizontalDistance())*Mth.RAD_TO_DEG));e.setRoll(0);
        try {
            if(setPosition==null)setPosition=ObfuscationReflectionHelper.findMethod(Camera.class,"m_90581_",Vec3.class);
            setPosition.invoke(e.getCamera(),eye);
        } catch(ReflectiveOperationException ex) { throw new IllegalStateException("Origin camera position unavailable",ex); }
    }
    @SubscribeEvent public static void fov(ViewportEvent.ComputeFov e) {
        var s=local();if(s!=null&&e.usedConfiguredFov())e.setFOV(OriginTimeline.shot(age(s,(float)e.getPartialTick())).fov());
    }
    @SubscribeEvent public static void input(MovementInputUpdateEvent e) {
        if(!active()||e.getEntity()!=Minecraft.getInstance().player)return;
        var in=e.getInput();in.forwardImpulse=in.leftImpulse=0;in.jumping=in.shiftKeyDown=false;
        in.up=in.down=in.left=in.right=false;
    }
    @SubscribeEvent public static void interact(InputEvent.InteractionKeyMappingTriggered e) {
        if(active()) { e.setCanceled(true);e.setSwingHand(false); }
    }
    @SubscribeEvent public static void hud(RenderGuiOverlayEvent.Pre e) { if(active())e.setCanceled(true); }
    @SubscribeEvent public static void overlay(RenderGuiEvent.Post e) {
        var s=local();if(s==null)return;
        var mc=Minecraft.getInstance();var g=e.getGuiGraphics();int w=g.guiWidth(),h=g.guiHeight();
        float t=age(s,e.getPartialTick());int bar=(int)(h*.115F*OriginTimeline.ease(Math.min(t,OriginTimeline.END-t)/8F));
        float flash=Math.max(0,1-Math.abs(t-186)/7F);
        if(t>=184&&t<188)flash=1;
        if(flash>0)g.fill(0,0,w,h,((int)(flash*255)<<24)|0xDAF7FF);
        float blue=(float)(OriginTimeline.ease(t,184,188)*(1-OriginTimeline.ease(t,196,214)));
        if(blue>0)g.fill(0,0,w,h,((int)(blue*65)<<24)|0x25A9FF);
        g.fill(0,0,w,bar,0xFF000000);g.fill(0,h-bar,w,h,0xFF000000);
        if(t>=38&&t<102) {
            var lines=mc.font.split(Component.translatable("story.bleachmod.origin.line"),w-50);
            int y=h-bar-12-lines.size()*11;
            for(var line:lines) { int x=(w-mc.font.width(line))/2;g.fill(x-5,y-2,x+mc.font.width(line)+5,y+10,0xB0000000);g.drawString(mc.font,line,x,y,0xFFFFFF,true);y+=11; }
        }
    }
    public static void poseNpc(com.bleachmod.entity.QuestNpcEntity npc,PlayerModel<?> model,float partial) {
        var scene=SCENES.values().stream().filter(s->s.rukiaId()==npc.getId()).findFirst().orElse(null);
        if(scene==null&&npc.getPose()!=net.minecraft.world.entity.Pose.SITTING)return;
        float t=scene==null?OriginTimeline.END:age(scene,partial);
        for(var part:List.of(model.body,model.head,model.rightArm,model.leftArm,model.rightLeg,model.leftLeg))part.y+=8;
        model.body.zRot=-.06F;model.body.xRot=-.16F;model.head.xRot=-.06F;model.head.yRot=0;
        model.rightArm.xRot=t<184?-1.85F:-.23F;model.rightArm.yRot=-.12F;model.rightArm.zRot=.08F;
        model.leftArm.xRot=-.65F;model.leftArm.zRot=-.15F;
        model.rightLeg.xRot=-1.28F;model.leftLeg.xRot=-1.38F;
        model.rightLeg.yRot=.18F;model.leftLeg.yRot=-.22F;
        model.hat.copyFrom(model.head);model.jacket.copyFrom(model.body);
        model.rightSleeve.copyFrom(model.rightArm);model.leftSleeve.copyFrom(model.leftArm);
        model.rightPants.copyFrom(model.rightLeg);model.leftPants.copyFrom(model.leftLeg);
    }
    public static boolean isRukia(net.minecraft.world.entity.LivingEntity npc) {
        return SCENES.values().stream().anyMatch(s->s.rukiaId()==npc.getId());
    }
    @SubscribeEvent public static void burst(RenderLevelStageEvent e) {
        if(e.getStage()==RenderLevelStageEvent.Stage.AFTER_PARTICLES)OriginBurstRenderer.render(e,SCENES.values());
    }
}
