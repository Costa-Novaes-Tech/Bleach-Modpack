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
    private final java.util.List<java.util.Set<UUID>> assaultHits=java.util.List.of(new java.util.HashSet<>(),new java.util.HashSet<>(),new java.util.HashSet<>());
    private boolean bankai;
    private UUID owner;
    private net.minecraft.server.level.ServerPlayer caster;
    private Vec3 origin = Vec3.ZERO, direction = Vec3.ZERO;
    private int life;
    private static final net.minecraft.network.syncher.EntityDataAccessor<Integer> SCENE_START = net.minecraft.network.syncher.SynchedEntityData.defineId(IceDragonEntity.class, net.minecraft.network.syncher.EntityDataSerializers.INT);
    private static final net.minecraft.network.syncher.EntityDataAccessor<Float> ASSAULT_RANGE = net.minecraft.network.syncher.SynchedEntityData.defineId(IceDragonEntity.class, net.minecraft.network.syncher.EntityDataSerializers.FLOAT);
    @Override protected void defineSynchedData() {
        super.defineSynchedData(); entityData.define(SCENE_START, 0); entityData.define(ASSAULT_RANGE, 32F);
    }
    /** Subtraction is rollover-safe for transient scenes shorter than 2^31 ticks. */
    public double sceneAge(float partial) { return Math.max(0, (int)level().getGameTime()-entityData.get(SCENE_START))+partial; }
    public boolean isAssault() { return getPhantomSize()==14; }
    public float assaultRange() { return entityData.get(ASSAULT_RANGE); }
    public void configureAssault(net.minecraft.server.level.ServerPlayer p, float range) { configureAssault(p,p.position(),p.getYRot(),range); }
    public void configureAssault(net.minecraft.server.level.ServerPlayer p, Vec3 anchor,float yaw,float range) {
        configureStationary(p,anchor,yaw,com.bleachmod.common.visual.HydraMotion.ASSAULT_TICKS);
        float clearRange=net.minecraft.util.Mth.clamp(range,1,32);
        Vec3 right=new Vec3(direction.z,0,-direction.x);
        for(int branch=-1;branch<=1;branch++){
            Vec3 eye=anchor.add(0,p.getEyeHeight(),0);
            Vec3 end=eye.add(direction.scale(clearRange)).add(right.scale(branch*5));
            var wall=level().clip(new net.minecraft.world.level.ClipContext(eye,end,net.minecraft.world.level.ClipContext.Block.COLLIDER,net.minecraft.world.level.ClipContext.Fluid.NONE,p));
            if(wall.getType()!=net.minecraft.world.phys.HitResult.Type.MISS)clearRange=(float)Math.max(1,Math.min(clearRange,wall.getLocation().subtract(anchor).dot(direction)-4));
        }
        setPhantomSize(14); entityData.set(ASSAULT_RANGE,clearRange);
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
    private Vec3 headPosition(int head,double age) {
        var local=com.bleachmod.common.visual.HydraMotion.point(true,head,age,1,assaultRange());
        Vec3 right=new Vec3(direction.z,0,-direction.x);
        return origin.add(right.scale(local.x)).add(0,local.y,0).add(direction.scale(local.z));
    }
    private void assaultTick(net.minecraft.server.level.ServerPlayer p) {
        int age=(int)sceneAge(0);
        if(age==10)p.serverLevel().playSound(null,blockPosition(),com.bleachmod.init.ModSounds.HYORINMARU_ROAR.get(),net.minecraft.sounds.SoundSource.PLAYERS,1.1F,1.0F);
        for(int head=0;head<3;head++){
            var at=headPosition(head,age);var before=headPosition(head,age-1);
            if(age==17+head*10){
                p.serverLevel().playSound(null,net.minecraft.core.BlockPos.containing(at),net.minecraft.sounds.SoundEvents.GLASS_BREAK,net.minecraft.sounds.SoundSource.PLAYERS,.9F,.7F);
                p.serverLevel().sendParticles(net.minecraft.core.particles.ParticleTypes.ITEM_SNOWBALL,at.x,at.y,at.z,24,2,.8,2,.12);
            }
            if(age<8+head*10 || age>28+head*10)continue;
            var wall=level().clip(new net.minecraft.world.level.ClipContext(p.getEyePosition(),at,net.minecraft.world.level.ClipContext.Block.COLLIDER,net.minecraft.world.level.ClipContext.Fluid.NONE,p));
            Vec3 end=wall.getType()==net.minecraft.world.phys.HitResult.Type.MISS?at:wall.getLocation();
            if(age%2==0)p.serverLevel().sendParticles(net.minecraft.core.particles.ParticleTypes.SNOWFLAKE,end.x,end.y,end.z,12,1.8,1.4,1.8,.07);
            var area=new net.minecraft.world.phys.AABB(before,at).inflate(3,2.5,3);
            for(var target:level().getEntitiesOfClass(LivingEntity.class,area,t->com.bleachmod.common.technique.TechniqueTargets.allowed(p,t))){
                var set=assaultHits.get(head);if(set.size()>=64 || set.contains(target.getUUID()))continue;
                var sight=level().clip(new net.minecraft.world.level.ClipContext(origin.add(direction.scale(8)).add(0,1.2,0),target.getBoundingBox().getCenter(),net.minecraft.world.level.ClipContext.Block.COLLIDER,net.minecraft.world.level.ClipContext.Fluid.NONE,p));
                if(sight.getType()!=net.minecraft.world.phys.HitResult.Type.MISS)continue;
                // Distance to the swept head path, rather than to its enclosing diagonal box.
                Vec3 a=before,b=at,delta=b.subtract(a),q=target.getBoundingBox().getCenter();
                double t=net.minecraft.util.Mth.clamp(q.subtract(a).dot(delta)/Math.max(.001,delta.lengthSqr()),0,1);
                if(q.distanceToSqr(a.add(delta.scale(t)))>12.25)continue;
                set.add(target.getUUID());
                if(com.bleachmod.common.technique.MasteryPracticeService.hurt(p,target,p.damageSources().playerAttack(p),head==2?10:4,2))
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
