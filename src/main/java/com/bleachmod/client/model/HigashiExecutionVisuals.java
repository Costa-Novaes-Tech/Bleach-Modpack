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
import net.minecraft.world.entity.LivingEntity;
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
    private static long proofSuppressedTick=Long.MIN_VALUE,proofSilhouetteTick=Long.MIN_VALUE;
    private record Pose(HigashiExecutionS2C packet,long start){ }
    private static final Map<Integer,Burn> BURNS=new LinkedHashMap<>();
    private static final Map<Integer,Pose> POSES=new LinkedHashMap<>();
    public record PendingTarget(int entityId, long expiryGameTime, int worldHash, boolean confirmed) { }
    private static final Map<Integer, PendingTarget> PENDING_TARGETS = new java.util.concurrent.ConcurrentHashMap<>();
    public static void notePendingTarget(int entityId, boolean confirmed) {
        var mc = Minecraft.getInstance();
        int worldHash = mc.level != null ? System.identityHashCode(mc.level) : 0;
        long gameTime = mc.level != null ? mc.level.getGameTime() : 0;
        PENDING_TARGETS.put(entityId, new PendingTarget(entityId, gameTime + 15, worldHash, confirmed));
    }
    public static void notePendingTarget(int entityId) { notePendingTarget(entityId, true); }
    public static void removePendingTarget(int entityId) { PENDING_TARGETS.remove(entityId); }
    public static boolean isPendingTarget(int entityId) {
        PendingTarget pending = PENDING_TARGETS.get(entityId);
        if (pending == null) return false;
        var mc = Minecraft.getInstance();
        if (mc.level == null || System.identityHashCode(mc.level) != pending.worldHash) {
            PENDING_TARGETS.remove(entityId);
            return false;
        }
        long now = mc.level.getGameTime();
        if (now > pending.expiryGameTime) {
            PENDING_TARGETS.remove(entityId);
            return false;
        }
        return true;
    }
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
    private static net.minecraft.world.entity.LivingEntity createPresentationSilhouette(net.minecraft.world.entity.LivingEntity living, Vec3 pos){
        if(living == null || world == null) return null;
        try {
            net.minecraft.world.entity.LivingEntity replica = (net.minecraft.world.entity.LivingEntity) living.getType().create(world);
            if(replica == null) {
                living.deathTime = 0;
                living.hurtTime = 0;
                living.clearFire();
                living.setRemainingFireTicks(0);
                living.setSharedFlagOnFire(false);
                return living;
            }
            replica.setPos(pos.x, pos.y, pos.z);
            replica.setYRot(living.getYRot());
            replica.setXRot(living.getXRot());
            replica.yRotO = living.getYRot();
            replica.xRotO = living.getXRot();
            replica.yBodyRot = living.yBodyRot;
            replica.yBodyRotO = living.yBodyRot;
            replica.yHeadRot = living.yHeadRot;
            replica.yHeadRotO = living.yHeadRot;
            for(var slot : net.minecraft.world.entity.EquipmentSlot.values()){
                var item = living.getItemBySlot(slot);
                if(!item.isEmpty()) replica.setItemSlot(slot, item.copy());
            }
            try {
                var nonDefault = living.getEntityData().getNonDefaultValues();
                if(nonDefault != null && !nonDefault.isEmpty()) replica.getEntityData().assignValues(nonDefault);
            } catch(Throwable ignored){ }
            if(living instanceof com.bleachmod.entity.HollowEntity hLiving && replica instanceof com.bleachmod.entity.HollowEntity hReplica){
                hReplica.setVariant(hLiving.getVariant());
            }
            replica.noPhysics = true;
            replica.deathTime = 0;
            replica.hurtTime = 0;
            replica.clearFire();
            replica.setRemainingFireTicks(0);
            replica.setSharedFlagOnFire(false);
            return replica;
        } catch(Throwable t){
            living.deathTime = 0;
            living.hurtTime = 0;
            living.clearFire();
            living.setRemainingFireTicks(0);
            living.setSharedFlagOnFire(false);
            return living;
        }
    }
    public static void accept(HigashiExecutionS2C m){var mc=Minecraft.getInstance();
        if(mc.level==null||!m.startsExecution()){
            if(m.isCancel())removePendingTarget(m.target());
            return;
        }
        try{
            if(world!=mc.level){reset();world=mc.level;}
            long now=now();var previous=POSES.get(m.owner());
            if(previous!=null&&previous.packet.target()==m.target()&&age(previous,0)<HigashiCinematicPath.END)return;
            // Ordinary Higashi already performed the draw/approach. Join at the confirmed impact,
            // never replay a lethal-looking scene speculatively against a survivor.
            var pose=new Pose(m,now);
            if(POSES.size()>=24&&!POSES.containsKey(m.owner()))POSES.remove(POSES.keySet().iterator().next());POSES.put(m.owner(),pose);
            if(m.confirmed()){
                if(BURNS.size()>=8)BURNS.remove(BURNS.keySet().iterator().next());
                ElementalActionVisuals.VIEWS.remove(m.owner());
                net.minecraft.world.entity.LivingEntity living = mc.level.getEntity(m.target()) instanceof net.minecraft.world.entity.LivingEntity l ? l : null;
                LivingEntity clientSilhouette = createPresentationSilhouette(living, m.victim());
                BURNS.put(m.target(),new Burn(m,now,clientSilhouette));
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
        }finally{
            if(m.confirmed()||m.isCancel())removePendingTarget(m.target());
        }
    }
    public static boolean isPerformingExecution(int entityId){
        var pose=POSES.get(entityId);return pose!=null&&world==Minecraft.getInstance().level&&now()-pose.start<HigashiCinematicPath.END;
    }
    public static boolean isExecuting(int entity){
        var burn=BURNS.get(entity);return burn!=null&&world==Minecraft.getInstance().level&&now()-burn.start<HigashiCinematicPath.END;
    }
    public static boolean shouldSuppressEntity(int entity){
        boolean suppressed=!renderingSilhouette && (isExecuting(entity) || isPendingTarget(entity));
        if(suppressed && Boolean.getBoolean("bleachmod.higashiPoseQa") && proofSuppressedTick!=now()){
            proofSuppressedTick=now();com.mojang.logging.LogUtils.getLogger().info("PROOF141 original render blocked entity={} tick={}",entity,now());
        }
        return suppressed;
    }
    public static float currentPoseAge(){
        var mc=Minecraft.getInstance();if(mc.player==null)return -1;
        var p=POSES.get(mc.player.getId());return p==null?-1:age(p,0);
    }
    public static float currentBurnAge(){
        if(BURNS.isEmpty())return -1;
        var b=BURNS.values().iterator().next();
        return now()-b.start;
    }
    public static int currentBurnTarget(){
        if(BURNS.isEmpty())return -1;
        return BURNS.keySet().iterator().next();
    }
    public static boolean isOriginalPresent(int entityId){
        var mc=Minecraft.getInstance();return mc.level!=null&&mc.level.getEntity(entityId)!=null;
    }
    public static boolean isOriginalSuppressed(int entityId){
        return shouldSuppressEntity(entityId);
    }
    public static boolean isReplicaVisible(int entityId){
        var burn=BURNS.get(entityId);
        if(burn==null||world!=Minecraft.getInstance().level)return false;
        long elapsed=now()-burn.start;
        return elapsed>=0&&elapsed<HigashiCinematicPath.IGNITE+12;
    }
    public static boolean holdingCorpse(int entity){
        return isExecuting(entity);
    }
    private static void restore(){var mc=Minecraft.getInstance();if(previousType!=null&&mc.options.getCameraType()==CameraType.THIRD_PERSON_BACK)mc.options.setCameraType(previousType);previousType=null;cinematic=null;sideSign=1.0F;}
    private static void dismiss(){var mc=Minecraft.getInstance();if(mc.player!=null)POSES.remove(mc.player.getId());dismissedUntil=now()+HigashiCinematicPath.END;restore();}
    public static void yieldToRelease(){if(cinematic!=null)dismiss();}
    private static void reset(){restore();BURNS.clear();POSES.clear();PENDING_TARGETS.clear();world=null;emitted=Long.MIN_VALUE;dismissedUntil=Long.MIN_VALUE;}
    public static boolean localExecutionActive(){return cameraActive();}
    private static boolean cameraActive(){var mc=Minecraft.getInstance();return cinematic!=null&&world==mc.level&&mc.player!=null&&mc.getCameraEntity()==mc.player&&mc.options.getCameraType()==CameraType.THIRD_PERSON_BACK;}
    private static Pose pose(AbstractClientPlayer p){var value=POSES.get(p.getId());
        if(value==null||world!=p.level()||!p.isAlive()||p.isSpectator()||ZanpakutoReleaseAnimation.active(p)||ZanpakutoGuardPose.engaged(p,0))return null;
        if(!p.getMainHandItem().is(com.bleachmod.init.ModItems.RYUJIN_JAKKA.get())||!com.bleachmod.common.data.PlayerCapability.get(p).map(d->d.getCharacter().getZanpakutoIdentity().equals("ryujin_jakka")&&d.getCharacter().getActiveForm().equals("bankai")).orElse(false))return null;
        for(var v:ElementalActionVisuals.VIEWS.getOrDefault(p.getId(),List.of())){if(!v.kind().equals("higashi")&&ElementalActionVisuals.age(v,0)<v.gesture())return null;}
        return age(value,0)<HigashiCinematicPath.END?value:null;
    }
    private static Vec3f mix(Vec3f from,Vec3 to,float w){return new Vec3f(net.minecraft.util.Mth.lerp(w,from.getX(),(float)to.x),net.minecraft.util.Mth.lerp(w,from.getY(),(float)to.y),net.minecraft.util.Mth.lerp(w,from.getZ(),(float)to.z));}
    private static final class Animation implements IAnimation {
        private final AbstractClientPlayer p;Animation(AbstractClientPlayer player){p=player;}
        @Override public boolean isActive(){return pose(p)!=null;}
        @Override public void setupAnim(float partial){ }
        @Override public FirstPersonMode getFirstPersonMode(float partial){return FirstPersonMode.VANILLA;}
        @Override public Vec3f get3DTransform(String part,TransformType type,float partial,Vec3f value){
            var pose=pose(p);if(pose==null)return value;
            if(part.equals("body")&&type==TransformType.POSITION)return new Vec3f(value.getX(),0,value.getZ());
            float sign=p.getMainArm()==HumanoidArm.RIGHT?1:-1;
            var execPose=RyujinChoreography.execution(age(pose,partial),sign);
            boolean item=part.equals(sign>0?"rightItem":"leftItem");
            if(item){
                float w=execPose.weight();
                if(type==TransformType.ROTATION){
                    return mix(value,execPose.item(),w);
                }
                if(type==TransformType.POSITION){
                    return mix(value,Vec3.ZERO,w);
                }
            }
            return RyujinActionVisuals.transform(p,execPose,part,type,value);
        }
    }
    @SubscribeEvent(priority=EventPriority.LOWEST) public static void camera(ViewportEvent.ComputeCameraAngles e){if(!cameraActive())return;var mc=Minecraft.getInstance();var p=cinematic.packet;
        var shot=HigashiCinematicPath.sample(p.attacker(),p.victim(),p.height(),age(cinematic,(float)e.getPartialTick()),sideSign);Vec3 eye=shot.eye();
        var hit=mc.level.clip(new ClipContext(shot.focus(),eye,ClipContext.Block.COLLIDER,ClipContext.Fluid.NONE,mc.player));
        if(hit.getType()!=HitResult.Type.MISS)eye=hit.getLocation().add(shot.focus().subtract(eye).normalize().scale(.25));
        if(eye.distanceToSqr(shot.focus())<1.2*1.2){dismiss();return;}
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
        long now=now();BURNS.values().removeIf(b->now-b.start>=HigashiCinematicPath.END);POSES.values().removeIf(p->age(p,0)>=HigashiCinematicPath.END);
        int currentWorld=System.identityHashCode(mc.level);PENDING_TARGETS.entrySet().removeIf(entry->entry.getValue().worldHash!=currentWorld||now>entry.getValue().expiryGameTime);
        if(cinematic!=null&&age(cinematic,0)>=HigashiCinematicPath.END)restore();
        if(cinematic!=null&&(mc.player==null||!mc.player.isAlive()||mc.player.hurtTime>0||mc.screen!=null||(age(cinematic,0)>4&&(mc.options.keyAttack.isDown()||mc.options.keyUse.isDown()||mc.options.keyUp.isDown()||mc.options.keyDown.isDown()||mc.options.keyLeft.isDown()||mc.options.keyRight.isDown()||mc.options.keyJump.isDown()))||mc.getCameraEntity()!=mc.player||mc.options.getCameraType()!=CameraType.THIRD_PERSON_BACK||!ClientVisualConfig.EXECUTION_CAMERA.get()||mc.player.distanceToSqr(cinematic.packet.attacker())>9||pose(mc.player)==null))dismiss();
        if(cinematic!=null&&mc.player!=null&&mc.level!=null&&age(cinematic,0)>=10&&age(cinematic,0)<=16){
            float cAge=age(cinematic,0);float sign=mc.player.getMainArm()==HumanoidArm.RIGHT?1:-1;
            var cutPose=RyujinChoreography.execution(cAge,sign);
            Vec3 f=cinematic.packet.victim().subtract(cinematic.packet.attacker()).multiply(1,0,1).normalize();
            if(f.lengthSqr()<.1)f=Vec3.directionFromRotation(0,mc.player.getYRot());
            Vec3 hand=RyujinActionVisuals.world(mc.player.position(),f,RyujinChoreography.bladeHand(cutPose,sign));
            Vec3 dir=RyujinActionVisuals.world(Vec3.ZERO,f,RyujinChoreography.bladeDirection(cutPose));
            for(int i=0;i<8;i++){Vec3 q=hand.add(dir.scale(.15+i*.14));if(i%4==0)mc.level.addParticle(ParticleTypes.ELECTRIC_SPARK,q.x,q.y,q.z,0,.01,0);else mc.level.addParticle(ParticleTypes.SMALL_FLAME,q.x,q.y,q.z,0,.015,0);}
        }
        if(mc.isPaused()||emitted==now)return;emitted=now;int budget=256,scale=mc.options.particles().get()==ParticleStatus.MINIMAL?4:1;
        for(var b:BURNS.values()){var m=b.packet;if(mc.player==null||mc.player.distanceToSqr(m.victim())>80*80)continue;int age=(int)(now-b.start)-HigashiCinematicPath.IGNITE;
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
            if(Boolean.getBoolean("bleachmod.higashiPoseQa") && proofSilhouetteTick!=now && elapsed%4==0){
                proofSilhouetteTick=now;com.mojang.logging.LogUtils.getLogger().info("PROOF141 silhouette target={} type={} elapsed={} variant={} originalPresent={}",b.packet.target(),actor.getType(),elapsed,actor instanceof com.bleachmod.entity.HollowEntity h?h.getVariant():"vanilla",mc.level.getEntity(b.packet.target())!=null);
            }
            if(elapsed<0||elapsed>=HigashiCinematicPath.IGNITE+12)continue;
            if(mc.player==null||mc.player.distanceToSqr(b.packet.victim())>80*80)continue;
            float alpha=(elapsed<=HigashiCinematicPath.IGNITE)?1.0F:Math.max(0.0F,1.0F-(float)(elapsed-HigashiCinematicPath.IGNITE)/12.0F);
            int death=actor.deathTime,hurt=actor.hurtTime,fire=actor.getRemainingFireTicks();boolean burning=actor.isOnFire();
            try{
                renderingSilhouette=true;actor.deathTime=0;actor.hurtTime=0;actor.setRemainingFireTicks(0);actor.setSharedFlagOnFire(false);
                Vec3 at=b.packet.victim().subtract(camera);
                var bufferSource=mc.renderBuffers().bufferSource();
                if(alpha>=0.999F){
                    mc.getEntityRenderDispatcher().render(actor,at.x,at.y,at.z,actor.getYRot(),0,stack,bufferSource,mc.getEntityRenderDispatcher().getPackedLightCoords(actor,e.getPartialTick()));
                }else{
                    float tint=Math.max(0.20F,alpha);
                    com.mojang.blaze3d.systems.RenderSystem.enableBlend();
                    com.mojang.blaze3d.systems.RenderSystem.defaultBlendFunc();
                    net.minecraft.client.renderer.MultiBufferSource alphaBuffers=renderType->{
                        var parent=bufferSource.getBuffer(renderType);
                        return new AlphaTintVertexConsumer(parent,alpha,tint);
                    };
                    mc.getEntityRenderDispatcher().render(actor,at.x,at.y,at.z,actor.getYRot(),0,stack,alphaBuffers,mc.getEntityRenderDispatcher().getPackedLightCoords(actor,e.getPartialTick()));
                    bufferSource.endBatch();
                    com.mojang.blaze3d.systems.RenderSystem.disableBlend();
                }
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
