package com.bleachmod.client.model;

import com.bleachmod.Reference;
import com.bleachmod.client.ClientVisualConfig;
import com.bleachmod.client.compat.PhotonPilotBridge;
import com.bleachmod.common.network.s2c.HigashiExecutionS2C;
import com.bleachmod.common.visual.HigashiCinematicPath;
import com.bleachmod.common.visual.RyujinChoreography;
import dev.kosmx.playerAnim.api.TransformType;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.core.util.Vec3f;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationFactory;
import net.minecraft.client.*;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.*;
import net.minecraftforge.fml.common.Mod;
import java.util.*;

/** Render-time camera shots and body poses. Only a confirmed server death produces a burn. */
@Mod.EventBusSubscriber(modid=Reference.MOD_ID,value=Dist.CLIENT)
public final class HigashiExecutionVisuals {
    private record Burn(HigashiExecutionS2C packet,long start,net.minecraft.world.entity.LivingEntity silhouette){ }
    private static boolean renderingSilhouette;
    private record Pose(HigashiExecutionS2C packet,long start){ }
    private static final Map<Integer,Burn> BURNS=new LinkedHashMap<>();
    private static final Map<Integer,Pose> POSES=new LinkedHashMap<>();
    private static net.minecraft.client.multiplayer.ClientLevel world;
    private static CameraType previousType;
    private static java.lang.reflect.Method cameraPosition;
    private static Pose cinematic;
    private static float sideSign = 1.0F;
    private static long emitted=Long.MIN_VALUE,dismissedUntil=Long.MIN_VALUE;
    private HigashiExecutionVisuals(){ }
    public static void init(){PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(Reference.id("higashi_execution"),3600,Animation::new);}
    private static long now(){return world==null?0:world.getGameTime();}
    private static float age(Pose pose,float partial){return now()-pose.start+partial;}
    public static void accept(HigashiExecutionS2C m){var mc=Minecraft.getInstance();if(mc.level==null||!m.startsExecution())return;
        if(world!=mc.level){reset();world=mc.level;}
        long now=now();var previous=POSES.get(m.owner());
        if(previous!=null&&previous.packet.target()==m.target()&&age(previous,0)<HigashiCinematicPath.END)return;
        // Ordinary Higashi already performed the draw/approach. Join at the confirmed impact,
        // never replay a lethal-looking scene speculatively against a survivor.
        var pose=new Pose(m,now-HigashiCinematicPath.IMPACT);
        if(POSES.size()>=24&&!POSES.containsKey(m.owner()))POSES.remove(POSES.keySet().iterator().next());POSES.put(m.owner(),pose);
        if(m.confirmed()){
            if(BURNS.size()>=8)BURNS.remove(BURNS.keySet().iterator().next());
            net.minecraft.world.entity.LivingEntity living = mc.level.getEntity(m.target()) instanceof net.minecraft.world.entity.LivingEntity l ? l : null;
            if(living != null){
                living.clearFire();
                living.setRemainingFireTicks(0);
                living.setSharedFlagOnFire(false);
            }
            BURNS.put(m.target(),new Burn(m,now,living));
        }
        if(mc.player!=null&&mc.player.getId()==m.owner()&&!com.bleachmod.client.cinematic.CinematicDirector.localActive()&&ClientVisualConfig.EXECUTION_CAMERA.get()&&mc.screen==null&&mc.getCameraEntity()==mc.player&&now>=dismissedUntil){
            if(cinematic==null){previousType=mc.options.getCameraType();mc.options.setCameraType(CameraType.THIRD_PERSON_BACK);}
            cinematic=pose;
            var shotR=HigashiCinematicPath.sample(m.attacker(),m.victim(),m.height(),0,1.0F);
            var shotL=HigashiCinematicPath.sample(m.attacker(),m.victim(),m.height(),0,-1.0F);
            var hitR=mc.level.clip(new ClipContext(shotR.focus(),shotR.eye(),ClipContext.Block.COLLIDER,ClipContext.Fluid.NONE,mc.player));
            var hitL=mc.level.clip(new ClipContext(shotL.focus(),shotL.eye(),ClipContext.Block.COLLIDER,ClipContext.Fluid.NONE,mc.player));
            double dR=(hitR.getType()==HitResult.Type.MISS)?shotR.eye().distanceToSqr(shotR.focus()):hitR.getLocation().distanceToSqr(shotR.focus());
            double dL=(hitL.getType()==HitResult.Type.MISS)?shotL.eye().distanceToSqr(shotL.focus()):hitL.getLocation().distanceToSqr(shotL.focus());
            sideSign=(dR>=dL)?1.0F:-1.0F;
            if(mc instanceof net.bettercombat.api.MinecraftClient_BetterCombat bc)bc.cancelUpswing();
        }
    }
    public static boolean isExecuting(int entity){
        var burn=BURNS.get(entity);return burn!=null&&world==Minecraft.getInstance().level&&now()-burn.start<HigashiCinematicPath.END;
    }
    public static boolean holdingCorpse(int entity){
        return isExecuting(entity);
    }
    private static void restore(){var mc=Minecraft.getInstance();if(previousType!=null&&mc.options.getCameraType()==CameraType.THIRD_PERSON_BACK)mc.options.setCameraType(previousType);previousType=null;cinematic=null;sideSign=1.0F;}
    private static void dismiss(){var mc=Minecraft.getInstance();if(mc.player!=null)POSES.remove(mc.player.getId());dismissedUntil=now()+HigashiCinematicPath.END;restore();}
    public static void yieldToRelease(){if(cinematic!=null)dismiss();}
    private static void reset(){restore();BURNS.clear();POSES.clear();world=null;emitted=Long.MIN_VALUE;dismissedUntil=Long.MIN_VALUE;}
    public static boolean localExecutionActive(){return cameraActive();}
    private static boolean cameraActive(){var mc=Minecraft.getInstance();return cinematic!=null&&world==mc.level&&mc.player!=null&&mc.getCameraEntity()==mc.player&&mc.options.getCameraType()==CameraType.THIRD_PERSON_BACK;}
    private static Pose pose(AbstractClientPlayer p){var value=POSES.get(p.getId());
        if(value==null||world!=p.level()||!p.isAlive()||p.isSpectator()||ZanpakutoReleaseAnimation.active(p)||ZanpakutoGuardPose.engaged(p,0))return null;
        if(!p.getMainHandItem().is(com.bleachmod.init.ModItems.RYUJIN_JAKKA.get())||!com.bleachmod.common.data.PlayerCapability.get(p).map(d->d.getCharacter().getZanpakutoIdentity().equals("ryujin_jakka")&&d.getCharacter().getActiveForm().equals("bankai")).orElse(false))return null;
        for(var v:ElementalActionVisuals.VIEWS.getOrDefault(p.getId(),List.of())){if(!v.kind().equals("higashi")&&ElementalActionVisuals.age(v,0)<v.gesture())return null;}
        return age(value,0)<HigashiCinematicPath.END?value:null;
    }
    private static final class Animation implements IAnimation {
        private final AbstractClientPlayer p;Animation(AbstractClientPlayer player){p=player;}
        @Override public boolean isActive(){return pose(p)!=null;}
        @Override public void setupAnim(float partial){ }
        @Override public FirstPersonMode getFirstPersonMode(float partial){return FirstPersonMode.VANILLA;}
        @Override public Vec3f get3DTransform(String part,TransformType type,float partial,Vec3f value){var pose=pose(p);if(pose==null)return value;
            if(part.equals("body")&&type==TransformType.POSITION)return new Vec3f(value.getX(),0,value.getZ());
            return RyujinActionVisuals.transform(p,RyujinChoreography.execution(age(pose,partial),p.getMainArm()==HumanoidArm.RIGHT?1:-1),part,type,value);}
    }
    @SubscribeEvent(priority=EventPriority.LOWEST) public static void camera(ViewportEvent.ComputeCameraAngles e){if(!cameraActive())return;var mc=Minecraft.getInstance();var p=cinematic.packet;
        var shot=HigashiCinematicPath.sample(p.attacker(),p.victim(),p.height(),age(cinematic,(float)e.getPartialTick()),sideSign);Vec3 eye=shot.eye();
        var hit=mc.level.clip(new ClipContext(shot.focus(),eye,ClipContext.Block.COLLIDER,ClipContext.Fluid.NONE,mc.player));
        if(hit.getType()!=HitResult.Type.MISS)eye=hit.getLocation().add(shot.focus().subtract(eye).normalize().scale(.25));
        if(eye.distanceToSqr(shot.focus())<1.5*1.5){
            var altShot=HigashiCinematicPath.sample(p.attacker(),p.victim(),p.height(),age(cinematic,(float)e.getPartialTick()),-sideSign);Vec3 altEye=altShot.eye();
            var altHit=mc.level.clip(new ClipContext(altShot.focus(),altEye,ClipContext.Block.COLLIDER,ClipContext.Fluid.NONE,mc.player));
            if(altHit.getType()!=HitResult.Type.MISS)altEye=altHit.getLocation().add(altShot.focus().subtract(altEye).normalize().scale(.25));
            if(altEye.distanceToSqr(altShot.focus())>=1.5*1.5){
                sideSign=-sideSign;shot=altShot;eye=altEye;
            }else{dismiss();return;}
        }
        try{
            if(cameraPosition==null)cameraPosition=net.minecraftforge.fml.util.ObfuscationReflectionHelper.findMethod(Camera.class,"m_90581_",Vec3.class);
            cameraPosition.invoke(e.getCamera(),eye);
        }catch(ReflectiveOperationException|RuntimeException failure){com.mojang.logging.LogUtils.getLogger().warn("Higashi camera unavailable; restoring normal view",failure);dismiss();return;}
        e.setYaw(HigashiCinematicPath.yaw(eye,shot.focus()));e.setPitch(HigashiCinematicPath.pitch(eye,shot.focus()));e.setRoll(0);
    }
    @SubscribeEvent public static void fov(ViewportEvent.ComputeFov e){if(cameraActive()&&e.usedConfiguredFov()){var p=cinematic.packet;e.setFOV(HigashiCinematicPath.sample(p.attacker(),p.victim(),p.height(),age(cinematic,(float)e.getPartialTick()),sideSign).fov());}}
    @SubscribeEvent public static void hideHud(RenderGuiOverlayEvent.Pre e){if(cameraActive())e.setCanceled(true);}
    @SubscribeEvent public static void bars(RenderGuiEvent.Post e){if(!cameraActive())return;float t=age(cinematic,e.getPartialTick());int size=(int)(e.getGuiGraphics().guiHeight()*.075*Math.max(0,Math.min(1,Math.min(t/3,(HigashiCinematicPath.END-t)/5))));var g=e.getGuiGraphics();g.fill(0,0,g.guiWidth(),size,0xff000000);g.fill(0,g.guiHeight()-size,g.guiWidth(),g.guiHeight(),0xff000000);}
    @SubscribeEvent public static void tick(TickEvent.ClientTickEvent e){if(e.phase!=TickEvent.Phase.END)return;var mc=Minecraft.getInstance();
        if(mc.level==null||world!=mc.level){reset();world=mc.level;return;}
        long now=now();BURNS.values().removeIf(b->now-b.start>=HigashiCinematicPath.END-HigashiCinematicPath.IMPACT);POSES.values().removeIf(p->age(p,0)>=HigashiCinematicPath.END);
        if(cinematic!=null&&age(cinematic,0)>=HigashiCinematicPath.END)restore();
        if(cinematic!=null&&(mc.player==null||!mc.player.isAlive()||mc.player.hurtTime>0||mc.screen!=null||mc.options.keyAttack.isDown()||mc.options.keyUse.isDown()||mc.options.keyUp.isDown()||mc.options.keyDown.isDown()||mc.options.keyLeft.isDown()||mc.options.keyRight.isDown()||mc.options.keyJump.isDown()||mc.getCameraEntity()!=mc.player||mc.options.getCameraType()!=CameraType.THIRD_PERSON_BACK||!ClientVisualConfig.EXECUTION_CAMERA.get()||mc.player.distanceToSqr(cinematic.packet.attacker())>9||pose(mc.player)==null))dismiss();
        if(mc.isPaused()||emitted==now)return;emitted=now;int budget=256,scale=mc.options.particles().get()==ParticleStatus.MINIMAL?4:1;
        for(var b:BURNS.values()){var m=b.packet;if(mc.player==null||mc.player.distanceToSqr(m.victim())>80*80)continue;int age=(int)(now-b.start)-(HigashiCinematicPath.IGNITE-HigashiCinematicPath.IMPACT);
            if(age<0)continue;
            if(age==0){PhotonPilotBridge.incinerate(m.victim(),m.width(),m.height());mc.level.playLocalSound(m.victim().x,m.victim().y,m.victim().z,net.minecraft.sounds.SoundEvents.BLAZE_DEATH,net.minecraft.sounds.SoundSource.PLAYERS,.8F,.65F,false);}
            var random=new Random(m.target()*31L+age);
            for(int i=0;i<48/scale&&budget-->0;i++){double a=random.nextDouble()*Math.PI*2,r=random.nextDouble()*Math.max(.3,m.width())*.6;Vec3 at=m.victim().add(Math.cos(a)*r,random.nextDouble()*Math.min(4,m.height())+age*.025,Math.sin(a)*r);
                var type=age<16?(i%4==0?ParticleTypes.ELECTRIC_SPARK:ParticleTypes.FLAME):(i%3==0?ParticleTypes.SMOKE:ParticleTypes.ASH);mc.level.addParticle(type,at.x,at.y,at.z,Math.cos(a)*.025,.04+random.nextDouble()*.08,Math.sin(a)*.025);
            }
        }
    }
    /** Keep only a client render reference after death; never resurrect or delay server loot. */
    @SubscribeEvent public static void silhouette(RenderLevelStageEvent e){
        if(e.getStage()!=RenderLevelStageEvent.Stage.AFTER_ENTITIES||world!=Minecraft.getInstance().level)return;
        var mc=Minecraft.getInstance();var stack=e.getPoseStack();var camera=e.getCamera().getPosition();
        long now=now();
        for(var b:BURNS.values()){
            var actor=b.silhouette;if(actor==null)continue;
            long elapsed=now-b.start;
            if(elapsed<0||elapsed>=HigashiCinematicPath.IGNITE+12)continue;
            if(mc.player==null||mc.player.distanceToSqr(b.packet.victim())>80*80)continue;
            int death=actor.deathTime,hurt=actor.hurtTime,fire=actor.getRemainingFireTicks();boolean burning=actor.isOnFire();
            try{
                renderingSilhouette=true;actor.deathTime=0;actor.hurtTime=0;actor.setRemainingFireTicks(0);actor.setSharedFlagOnFire(false);
                Vec3 at=b.packet.victim().subtract(camera);
                mc.getEntityRenderDispatcher().render(actor,at.x,at.y,at.z,actor.getYRot(),0,stack,mc.renderBuffers().bufferSource(),mc.getEntityRenderDispatcher().getPackedLightCoords(actor,e.getPartialTick()));
            }finally{actor.deathTime=death;actor.hurtTime=hurt;actor.setRemainingFireTicks(fire);actor.setSharedFlagOnFire(burning);renderingSilhouette=false;}
        }
    }
    @SubscribeEvent public static void hideIncinerated(RenderLivingEvent.Pre<?,?> e){
        int id=e.getEntity().getId();
        if(!renderingSilhouette&&isExecuting(id)){
            e.getEntity().clearFire();
            e.getEntity().setRemainingFireTicks(0);
            e.getEntity().setSharedFlagOnFire(false);
            e.setCanceled(true);
        }
    }
    @SubscribeEvent public static void logout(ClientPlayerNetworkEvent.LoggingOut e){reset();}
}
