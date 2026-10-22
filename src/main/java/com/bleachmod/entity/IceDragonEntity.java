package com.bleachmod.entity;

import com.bleachmod.common.data.PlayerCapability;
import com.bleachmod.common.technique.HyorinmaruTechniqueService;
import com.bleachmod.common.visual.IceTechniqueVisuals;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import java.util.UUID;

/** Transient projectile; Phantom is retained only as the registered entity/attribute base. */
public final class IceDragonEntity extends Phantom implements software.bernie.geckolib.animatable.GeoEntity {
    private final software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache geoCache=software.bernie.geckolib.util.GeckoLibUtil.createInstanceCache(this);
    private java.util.Set<UUID> hits=new java.util.HashSet<>();
    private String form;
    private final java.util.Map<UUID,Float> explosionDamage=new java.util.HashMap<>();
    public static final float HEAD_DAMAGE=8, MAX_EXPLOSION_DAMAGE=18;
    public static final double EXPLOSION_RADIUS=6;
    private boolean bankai;
    private UUID owner;
    private net.minecraft.server.level.ServerPlayer caster;
    private Vec3 origin = Vec3.ZERO, direction = Vec3.ZERO;
    private int life;
    private static final net.minecraft.network.syncher.EntityDataAccessor<Integer> SCENE_START = net.minecraft.network.syncher.SynchedEntityData.defineId(IceDragonEntity.class, net.minecraft.network.syncher.EntityDataSerializers.INT);
    private static final net.minecraft.network.syncher.EntityDataAccessor<Float> ASSAULT_RANGE = net.minecraft.network.syncher.SynchedEntityData.defineId(IceDragonEntity.class, net.minecraft.network.syncher.EntityDataSerializers.FLOAT);
    private static final net.minecraft.network.syncher.EntityDataAccessor<net.minecraft.nbt.CompoundTag> IMPACT_POINTS=net.minecraft.network.syncher.SynchedEntityData.defineId(IceDragonEntity.class,net.minecraft.network.syncher.EntityDataSerializers.COMPOUND_TAG);
    private static final net.minecraft.network.syncher.EntityDataAccessor<Integer> IMPACT_MASK=net.minecraft.network.syncher.SynchedEntityData.defineId(IceDragonEntity.class,net.minecraft.network.syncher.EntityDataSerializers.INT);
    @Override protected void defineSynchedData() {
        super.defineSynchedData(); entityData.define(IMPACT_POINTS,new net.minecraft.nbt.CompoundTag());entityData.define(IMPACT_MASK,0); entityData.define(SCENE_START, 0); entityData.define(ASSAULT_RANGE, 32F);
    }
    /** Subtraction is rollover-safe for transient scenes shorter than 2^31 ticks. */
    public double sceneAge(float partial) { return Math.max(0, (int)level().getGameTime()-entityData.get(SCENE_START))+partial; }
    public boolean isAssault() { return getPhantomSize()==14; }
    public float assaultRange() { return entityData.get(ASSAULT_RANGE); }
    public void configureAssault(net.minecraft.server.level.ServerPlayer p, float range) { configureAssault(p,p.position(),p.getYRot(),range); }
    public void configureAssault(net.minecraft.server.level.ServerPlayer p, Vec3 anchor,float yaw,float range) {
        configureStationary(p,anchor,yaw,com.bleachmod.common.visual.HydraMotion.ASSAULT_TICKS);
        setPhantomSize(14);entityData.set(ASSAULT_RANGE,net.minecraft.util.Mth.clamp(range,1,32));
        entityData.set(IMPACT_MASK,0);explosionDamage.clear();
        Vec3 right=new Vec3(direction.z,0,-direction.x);var points=new net.minecraft.nbt.CompoundTag();
        Vec3 aimed=assaultAim(p,anchor);
        for(int head=0;head<3;head++){
            double side=head==0?0:head==1?6:-6;
            Vec3 eye=anchor.add(0,1.2,0),end=new Vec3(aimed.x,anchor.y+1.2,aimed.z).add(right.scale(side));
            var wall=level().clip(new net.minecraft.world.level.ClipContext(eye,end,net.minecraft.world.level.ClipContext.Block.COLLIDER,net.minecraft.world.level.ClipContext.Fluid.NONE,p));
            if(wall.getType()!=net.minecraft.world.phys.HitResult.Type.MISS)end=wall.getLocation().subtract(end.subtract(eye).normalize().scale(.35));
            var ground=level().clip(new net.minecraft.world.level.ClipContext(end.add(0,.5,0),end.add(0,-10,0),net.minecraft.world.level.ClipContext.Block.COLLIDER,net.minecraft.world.level.ClipContext.Fluid.NONE,p));
            if(ground.getType()!=net.minecraft.world.phys.HitResult.Type.MISS)end=ground.getLocation().add(0,.12,0);
            else end=new Vec3(end.x,anchor.y+.15,end.z);
            Vec3 delta=end.subtract(origin);points.putDouble("x"+head,delta.dot(right));points.putDouble("y"+head,delta.y);points.putDouble("z"+head,delta.dot(direction));
        }
        entityData.set(IMPACT_POINTS,points);
    }
    private Vec3 assaultAim(net.minecraft.server.level.ServerPlayer p,Vec3 anchor){
        Vec3 eye=anchor.add(0,p.getEyeHeight(),0),look=Vec3.directionFromRotation(p.getXRot(),getYRot());
        Vec3 end=eye.add(look.scale(assaultRange()));
        var block=level().clip(new net.minecraft.world.level.ClipContext(eye,end,net.minecraft.world.level.ClipContext.Block.COLLIDER,net.minecraft.world.level.ClipContext.Fluid.NONE,p));
        double limit=block.getType()==net.minecraft.world.phys.HitResult.Type.MISS?assaultRange():eye.distanceTo(block.getLocation());
        var selected=level().getEntitiesOfClass(LivingEntity.class,p.getBoundingBox().inflate(assaultRange(),8,assaultRange()),t->com.bleachmod.common.technique.TechniqueTargets.allowed(p,t)).stream().filter(t->{
            Vec3 delta=t.getBoundingBox().getCenter().subtract(eye);double along=delta.dot(look);
            return along>=1 && along<=limit && delta.subtract(look.scale(along)).length()<.8+t.getBbWidth()/2+along*.025
                    && level().clip(new net.minecraft.world.level.ClipContext(eye,t.getBoundingBox().getCenter(),net.minecraft.world.level.ClipContext.Block.COLLIDER,net.minecraft.world.level.ClipContext.Fluid.NONE,p)).getType()==net.minecraft.world.phys.HitResult.Type.MISS;
        }).min(java.util.Comparator.comparingDouble(t->t.distanceToSqr(eye))).orElse(null);
        if(selected!=null)return selected.position();
        if(block.getType()!=net.minecraft.world.phys.HitResult.Type.MISS)return block.getLocation();
        return anchor.add(direction.scale(assaultRange()));
    }
    public int impactMask(){return entityData.get(IMPACT_MASK);}
    public Vec3 impactLocal(int head){
        var points=entityData.get(IMPACT_POINTS);
        return points.contains("x"+head)?new Vec3(points.getDouble("x"+head),points.getDouble("y"+head),points.getDouble("z"+head))
                :new Vec3(head==0?0:head==1?6:-6,.6,8+assaultRange());
    }
    public Vec3 curvePoint(int head,double age,double u){
        return isAssault()?com.bleachmod.common.visual.HydraMotion.point(true,head,age,u,impactLocal(head).add(0,head==0?2.55:2.3,head==0?-3.6:-3.3))
                :com.bleachmod.common.visual.HydraMotion.point(false,head,age,u,assaultRange());
    }
    public Vec3 impactPosition(int head){
        Vec3 local=impactLocal(head),forward=Vec3.directionFromRotation(0,getYRot()),right=new Vec3(forward.z,0,-forward.x);
        return position().add(right.scale(local.x)).add(0,local.y,0).add(forward.scale(local.z));
    }
    private boolean stationary = false;
    public static final double SHIKAI_RANGE=15, SHIKAI_HIT_RADIUS=.7, BANKAI_HIT_RADIUS=2.3;
    public static final float BANKAI_VISUAL_SCALE=5.25F;
    public static final int MANIFESTATION_SIZE = 13, BANKAI_SIZE = 12, BANKAI_LIFETIME = 30;
    public static final double BANKAI_SPEED = 0.8;
    private double speed = 0.7;
    public IceDragonEntity(EntityType<? extends Phantom> type, Level level) {
        super(type, level); setNoAi(true); setNoGravity(true); noPhysics = true; setSilent(true);
    }
    public void configure(net.minecraft.server.level.ServerPlayer p, Vec3 aim, boolean bankai) {
        configure(p,aim,bankai,new java.util.HashSet<>());
    }
    public void configure(net.minecraft.server.level.ServerPlayer p, Vec3 aim, boolean bankai, java.util.Set<UUID> hits) {
        this.stationary = false;
        this.hits=hits;this.bankai=bankai;this.form=bankai?"bankai":"shikai";
        owner = p.getUUID(); caster = p; origin = p.position().add(aim.scale(1.5)).add(0, bankai?3.2:1.2, 0); direction = aim;
        life = bankai ? BANKAI_LIFETIME+2 : 23; speed = bankai ? BANKAI_SPEED : 0.7;
        float yaw=(float)Math.toDegrees(Math.atan2(-aim.x,aim.z));
        setPhantomSize(bankai ? BANKAI_SIZE : 2); setPos(p.position().add(0,bankai?3.2:1.2,0)); setYRot(yaw); setYBodyRot(yaw);
    }
    public void configureStationary(net.minecraft.server.level.ServerPlayer p, Vec3 anchor, float yaw, int durationTicks) {
        this.stationary = true;
        entityData.set(SCENE_START,(int)p.level().getGameTime());
        this.bankai = true;
        this.form = "bankai";
        this.owner = p.getUUID();
        this.caster = p;
        Vec3 back = Vec3.directionFromRotation(0, yaw).scale(-8.0);
        this.origin = anchor.add(back).add(0, 0.4, 0);
        this.direction = Vec3.directionFromRotation(0, yaw);
        this.life = durationTicks;
        setPhantomSize(MANIFESTATION_SIZE);
        setPos(this.origin);
        setYRot(yaw);
        setYBodyRot(yaw);
    }
    public boolean isManifestation() { return getPhantomSize() == MANIFESTATION_SIZE || isAssault(); }
    @Override public void tick() {
        super.tick();
        if (level().isClientSide) return;
        var p = caster;
        if (p == null || p.level() != level() || !p.isAlive() || tickCount >= life) {
            discard(); return;
        }
        if (stationary) {
            if(isAssault()){
                if(!PlayerCapability.get(p).map(d->HyorinmaruTechniqueService.equipped(p,d) && form.equals(d.getCharacter().getActiveForm())).orElse(false)){discard();return;}
                assaultTick(p);
            }
            Vec3 pos = origin;
            setPos(pos);
            setDeltaMovement(Vec3.ZERO);
            if (tickCount % 4 == 0) {
                IceTechniqueVisuals.dragon(p, pos);
            }
            return;
        }
        if (!PlayerCapability.get(p).map(d -> HyorinmaruTechniqueService.equipped(p, d) && form.equals(d.getCharacter().getActiveForm())).orElse(false)) {
            discard(); return;
        }
        double range=bankai?24:SHIKAI_RANGE;
        Vec3 next = origin.add(direction.scale(Math.min(range-1.5,tickCount * speed)));
        if (!level().hasChunkAt(net.minecraft.core.BlockPos.containing(next))) { discard(); return; }
        Vec3 previous=position();
        var block=level().clip(new net.minecraft.world.level.ClipContext(previous,next,net.minecraft.world.level.ClipContext.Block.COLLIDER,net.minecraft.world.level.ClipContext.Fluid.NONE,this));
        if(block.getType()!=net.minecraft.world.phys.HitResult.Type.MISS){discard();return;}
        var area=new net.minecraft.world.phys.AABB(previous,next).inflate(bankai?BANKAI_HIT_RADIUS:SHIKAI_HIT_RADIUS);
        for(var target:level().getEntitiesOfClass(LivingEntity.class,area,t->com.bleachmod.common.technique.TechniqueTargets.allowed(p,t))) {
            double along=target.getBoundingBox().getCenter().subtract(origin).dot(direction)+1.5;
            if(along<0 || along>range+.3)continue;
            if(hits.size()>=64 || hits.contains(target.getUUID()) || !p.hasLineOfSight(target))continue;
            hits.add(target.getUUID());
            if(com.bleachmod.common.technique.MasteryPracticeService.hurt(p,target,p.damageSources().playerAttack(p),bankai?12:8,1))
                com.bleachmod.common.technique.IceControlService.chill(p,target,60,bankai?2:1,1);
        }
        setPos(next); setDeltaMovement(Vec3.ZERO);
        IceTechniqueVisuals.dragon(p, position());
    }
    private void assaultTick(net.minecraft.server.level.ServerPlayer p) {
        int age=(int)sceneAge(0);
        if(age==10)p.serverLevel().playSound(null,blockPosition(),com.bleachmod.init.ModSounds.HYORINMARU_ROAR.get(),net.minecraft.sounds.SoundSource.PLAYERS,1.1F,1);
        for(int head=0;head<3;head++){
            Vec3 at=impactPosition(head);int scheduled=com.bleachmod.common.visual.HydraMotion.impactTick(head);
            if(age>=scheduled && (impactMask() & (1<<head))==0){
                entityData.set(IMPACT_MASK,impactMask() | (1<<head));explodeHead(p,at);
            }
            if(age>scheduled && age<scheduled+100 && age%8==0)emit(p,net.minecraft.core.particles.ParticleTypes.SNOWFLAKE,at,9,4,.25,4,.015);
        }
    }
    private <T extends net.minecraft.core.particles.ParticleOptions> void emit(net.minecraft.server.level.ServerPlayer p,T type,Vec3 at,int count,double x,double y,double z,double speed){
        for(var viewer:p.serverLevel().players())if(viewer.distanceToSqr(at)<96*96)p.serverLevel().sendParticles(viewer,type,true,at.x,at.y,at.z,count,x,y,z,speed);
    }
    private void explodeHead(net.minecraft.server.level.ServerPlayer p,Vec3 at){
        emit(p,net.minecraft.core.particles.ParticleTypes.SNOWFLAKE,at.add(0,.7,0),100,3,1.8,3,.18);
        emit(p,net.minecraft.core.particles.ParticleTypes.ITEM_SNOWBALL,at.add(0,.7,0),40,2.8,1.2,2.8,.15);
        emit(p,new net.minecraft.core.particles.BlockParticleOption(net.minecraft.core.particles.ParticleTypes.BLOCK,net.minecraft.world.level.block.Blocks.PACKED_ICE.defaultBlockState()),at.add(0,.5,0),48,2.5,.5,2.5,.15);
        p.serverLevel().playSound(null,net.minecraft.core.BlockPos.containing(at),net.minecraft.sounds.SoundEvents.GLASS_BREAK,net.minecraft.sounds.SoundSource.PLAYERS,1.4F,.65F);
        var area=new net.minecraft.world.phys.AABB(at,at).inflate(EXPLOSION_RADIUS,5,EXPLOSION_RADIUS);
        for(var target:level().getEntitiesOfClass(LivingEntity.class,area,t->com.bleachmod.common.technique.TechniqueTargets.allowed(p,t))){
            if(!com.bleachmod.common.technique.TechniqueGeometry.intersectsCylinder(at,EXPLOSION_RADIUS,1,5,target.getBoundingBox()))continue;
            if(explosionDamage.size()>=64 && !explosionDamage.containsKey(target.getUUID()))continue;
            var wall=level().clip(new net.minecraft.world.level.ClipContext(at.add(0,.55,0),target.getBoundingBox().getCenter(),net.minecraft.world.level.ClipContext.Block.COLLIDER,net.minecraft.world.level.ClipContext.Fluid.NONE,p));
            if(wall.getType()!=net.minecraft.world.phys.HitResult.Type.MISS)continue;
            float damage=Math.min(HEAD_DAMAGE,MAX_EXPLOSION_DAMAGE-explosionDamage.getOrDefault(target.getUUID(),0F));
            if(damage<=0)continue;
            if(com.bleachmod.common.technique.MasteryPracticeService.hurt(p,target,p.damageSources().playerAttack(p),damage,2)){
                explosionDamage.merge(target.getUUID(),damage,Float::sum);
                com.bleachmod.common.technique.IceControlService.chill(p,target,60,target instanceof net.minecraft.world.entity.player.Player || target.getMaxHealth()>=100?1:3,2);
            }
        }
    }
    @Override public boolean hurt(net.minecraft.world.damagesource.DamageSource source, float amount) { return false; }
    @Override protected boolean isSunBurnTick() { return false; }
    @Override public boolean fireImmune() { return true; }
    @Override public boolean isPickable() { return false; }
    @Override public boolean isPushable() { return false; }
    @Override public boolean shouldBeSaved() { return false; }
    @Override public void registerControllers(software.bernie.geckolib.core.animation.AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new software.bernie.geckolib.core.animation.AnimationController<>(this,"flight",0,state->state.setAndContinue(
                isAssault() ? software.bernie.geckolib.core.animation.RawAnimation.begin().thenPlay("animation.ice_hydra.assault")
                        : isManifestation() ? software.bernie.geckolib.core.animation.RawAnimation.begin().thenPlay("animation.ice_hydra.emerge").thenLoop("animation.ice_hydra.flight")
                        : software.bernie.geckolib.core.animation.RawAnimation.begin().thenLoop("animation.ice_dragon.flight"))));
    }
    @Override public software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache getAnimatableInstanceCache(){return geoCache;}
}
