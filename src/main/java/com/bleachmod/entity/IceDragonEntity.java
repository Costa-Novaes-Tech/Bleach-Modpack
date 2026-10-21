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
    private boolean bankai;
    private UUID owner;
    private net.minecraft.server.level.ServerPlayer caster;
    private Vec3 origin = Vec3.ZERO, direction = Vec3.ZERO;
    private int life;
    private boolean stationary = false;
    public static final double SHIKAI_RANGE=15, SHIKAI_HIT_RADIUS=.7, BANKAI_HIT_RADIUS=2.3;
    public static final float BANKAI_VISUAL_SCALE=5.25F;
    public static final int BANKAI_SIZE = 12, BANKAI_LIFETIME = 30;
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
        this.bankai = true;
        this.form = "bankai";
        this.owner = p.getUUID();
        this.caster = p;
        Vec3 back = Vec3.directionFromRotation(0, yaw).scale(-1.8);
        this.origin = anchor.add(back).add(0, 0.4, 0);
        this.direction = Vec3.directionFromRotation(0, yaw);
        this.life = durationTicks;
        setPhantomSize(BANKAI_SIZE);
        setPos(this.origin);
        setYRot(yaw);
        setYBodyRot(yaw);
    }
    @Override public void tick() {
        super.tick();
        if (level().isClientSide) return;
        var p = caster;
        if (p == null || p.level() != level() || !p.isAlive() || tickCount >= life) {
            discard(); return;
        }
        if (stationary) {
            double rise = Math.min(2.0, tickCount * 0.08);
            Vec3 pos = origin.add(0, rise, 0);
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
    @Override public boolean hurt(net.minecraft.world.damagesource.DamageSource source, float amount) { return false; }
    @Override public boolean isPickable() { return false; }
    @Override public boolean isPushable() { return false; }
    @Override public boolean shouldBeSaved() { return false; }
    @Override public void registerControllers(software.bernie.geckolib.core.animation.AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new software.bernie.geckolib.core.animation.AnimationController<>(this,"flight",0,state->state.setAndContinue(
                software.bernie.geckolib.core.animation.RawAnimation.begin().thenLoop("animation.ice_dragon.flight"))));
    }
    @Override public software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache getAnimatableInstanceCache(){return geoCache;}
}
