package com.bleachmod.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.*;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import software.bernie.geckolib.core.animation.*;
import java.util.*;

/** First story boss. All attack timing/damage is authoritative on the server. */
public class HollowBossEntity extends HollowEntity {
    public enum Attack { IDLE, MELEE, CHARGE, SLAM }
    public static final int CHARGE_COOLDOWN = 140, MELEE_COOLDOWN = 36, SLAM_COOLDOWN = 200;
    public static final int MELEE_WINDUP = 12, CHARGE_WINDUP = 16, SLAM_WINDUP = 24;
    public static final double SLAM_RADIUS = 5.0;
    private static final EntityDataAccessor<Integer> ATTACK = SynchedEntityData.defineId(HollowBossEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> ATTACK_TICK = SynchedEntityData.defineId(HollowBossEntity.class, EntityDataSerializers.INT);
    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("animation.boss.idle");
    private static final RawAnimation WALK = RawAnimation.begin().thenLoop("animation.boss.walk");
    private static final RawAnimation[] ACTIONS = {IDLE,
            RawAnimation.begin().thenPlay("animation.boss.melee"),
            RawAnimation.begin().thenPlay("animation.boss.charge"),
            RawAnimation.begin().thenPlay("animation.boss.slam")};
    private int chargeCooldown, slamCooldown, basicCooldown;
    private LivingEntity attackTarget;
    private Vec3 chargeDirection = Vec3.ZERO, previousChargePosition = Vec3.ZERO;
    private Vec3 impactPosition = Vec3.ZERO;
    private final Set<UUID> chargeHits = new HashSet<>();

    public HollowBossEntity(EntityType<? extends HollowBossEntity> type, Level level) { super(type, level); }

    @Override protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(ATTACK, 0);
        entityData.define(ATTACK_TICK, 0);
    }
    @Override public EntityDimensions getDimensions(Pose pose) { return EntityDimensions.scalable(2.8F, 5.4F); }
    @Override protected float getStandingEyeHeight(Pose pose, EntityDimensions dimensions) { return 4.35F; }
    public Attack getAttack() { return Attack.values()[Mth.clamp(entityData.get(ATTACK), 0, 3)]; }
    public int getAttackTick() { return entityData.get(ATTACK_TICK); }
    public int getChargeCooldownTicks() { return chargeCooldown; }
    public int getSlamCooldownTicks() { return slamCooldown; }
    @Override public int getMeleeCooldownTicks() { return basicCooldown; }
    public Vec3 getImpactPosition() { return impactPosition; }

    private boolean validTarget(LivingEntity target) {
        return target != null && target.isAlive() && target.level() == level()
                && (!(target instanceof Player p) || !p.isCreative() && !p.isSpectator());
    }
    private double meleeReach(LivingEntity target) { return getBbWidth() / 2 + target.getBbWidth() / 2 + 1.25; }
    public boolean beginAttack(Attack attack, LivingEntity target) {
        if (level().isClientSide || !isAlive() || isNoAi() || getAttack() != Attack.IDLE
                || attack == Attack.IDLE || !validTarget(target) || !hasLineOfSight(target)) return false;
        double distance = distanceTo(target);
        if (attack == Attack.MELEE && (basicCooldown > 0 || distance > meleeReach(target))
                || attack == Attack.CHARGE && (chargeCooldown > 0 || distance < 4 || distance > 16 || !onGround())
                || attack == Attack.SLAM && (slamCooldown > 0 || distance > 6.5 || !onGround())) return false;
        attackTarget = target;
        Vec3 direction = target.position().subtract(position()).multiply(1, 0, 1).normalize();
        if (direction.lengthSqr() < .01) direction = getLookAngle().multiply(1, 0, 1).normalize();
        chargeDirection = direction;
        float yaw = (float)Math.toDegrees(Math.atan2(-direction.x, direction.z));
        setYRot(yaw); setYBodyRot(yaw); setYHeadRot(yaw);
        impactPosition = position().add(direction.scale(2.5));
        previousChargePosition = position();
        chargeHits.clear();
        getNavigation().stop();
        setDeltaMovement(0, getDeltaMovement().y, 0);
        entityData.set(ATTACK, attack.ordinal());
        entityData.set(ATTACK_TICK, 0);
        switch (attack) {
            case MELEE -> basicCooldown = MELEE_COOLDOWN;
            case CHARGE -> chargeCooldown = CHARGE_COOLDOWN;
            case SLAM -> slamCooldown = SLAM_COOLDOWN;
            default -> { }
        }
        return true;
    }
    public void cancelBossAttack() {
        if (getAttack() == Attack.IDLE) return;
        entityData.set(ATTACK, 0); entityData.set(ATTACK_TICK, 0);
        attackTarget = null;
        chargeHits.clear();
        getNavigation().stop();
        setDeltaMovement(0, getDeltaMovement().y, 0);
    }
    @Override public void setNoAi(boolean value) { super.setNoAi(value); if (value) cancelBossAttack(); }
    @Override public void die(net.minecraft.world.damagesource.DamageSource source) { cancelBossAttack(); super.die(source); }

    @Override public void aiStep() {
        if (!level().isClientSide) {
            if (chargeCooldown > 0) chargeCooldown--;
            if (slamCooldown > 0) slamCooldown--;
            if (basicCooldown > 0) basicCooldown--;
            if (getAttack() != Attack.IDLE) {
                if (!isAlive() || isNoAi() || !validTarget(attackTarget) || getTarget() != attackTarget) cancelBossAttack();
                else tickAttack();
            }
        }
        super.aiStep();
    }
    private void tickAttack() {
        int tick = getAttackTick() + 1;
        entityData.set(ATTACK_TICK, tick);
        getNavigation().stop();
        // Commit the facing at anticipation: players can dodge either special.
        float yaw = (float)Math.toDegrees(Math.atan2(-chargeDirection.x, chargeDirection.z));
        setYRot(yaw); setYBodyRot(yaw); setYHeadRot(yaw);
        setDeltaMovement(0, getDeltaMovement().y, 0);
        switch (getAttack()) {
            case MELEE -> {
                if (tick == MELEE_WINDUP && distanceTo(attackTarget) <= meleeReach(attackTarget) && hasLineOfSight(attackTarget)) {
                    super.doHurtTarget(attackTarget);
                    playSound(SoundEvents.IRON_GOLEM_ATTACK, 1.2F, .65F);
                }
                if (tick >= 26) cancelBossAttack();
            }
            case CHARGE -> {
                if (tick == CHARGE_WINDUP) playSound(SoundEvents.RAVAGER_ROAR, 1F, .75F);
                if (tick > CHARGE_WINDUP && tick <= CHARGE_WINDUP + 19) {
                    // Sweep only the distance actually travelled, after Minecraft resolves collisions.
                    var swept = getBoundingBox().minmax(getBoundingBox().move(previousChargePosition.subtract(position()))).inflate(.15, 0, .15);
                    for (var victim : level().getEntitiesOfClass(LivingEntity.class, swept, this::canDamage)) {
                        if (hasLineOfSight(victim) && chargeHits.add(victim.getUUID())) {
                            victim.hurt(damageSources().mobAttack(this), 10);
                            victim.knockback(.9, -chargeDirection.x, -chargeDirection.z);
                        }
                    }
                    previousChargePosition = position();
                    // Tick 35 accounts for the final step made by travel() on tick 34.
                    // It is a damage sweep only; recovery must not add another movement step.
                    if (tick == CHARGE_WINDUP + 19) break;
                    Vec3 step = chargeDirection.scale(.85);
                    BlockPos support = BlockPos.containing(position().add(step).add(0, -.25, 0));
                    if (horizontalCollision || !level().noCollision(this, getBoundingBox().move(step))
                            || !level().getBlockState(support).isFaceSturdy(level(), support, net.minecraft.core.Direction.UP)) {
                        cancelBossAttack();
                    } else {
                        setDeltaMovement(step.x, getDeltaMovement().y, step.z);
                        if (tick % 3 == 0) groundParticles(position(), 8);
                    }
                }
                if (tick >= 42) cancelBossAttack();
            }
            case SLAM -> {
                if (tick < SLAM_WINDUP && tick % 4 == 0) {
                    // Sparse ring shows the danger radius before contact.
                    var server = (ServerLevel)level();
                    for (int i = 0; i < 24; i++) {
                        double angle = i * Math.PI / 12;
                        server.sendParticles(ParticleTypes.CRIT, impactPosition.x + Math.cos(angle) * SLAM_RADIUS,
                                getY() + .12, impactPosition.z + Math.sin(angle) * SLAM_RADIUS, 1, 0, 0, 0, 0);
                    }
                }
                if (tick == SLAM_WINDUP && onGround()) slam();
                if (tick >= 42) cancelBossAttack();
            }
            default -> { }
        }
    }
    private boolean canDamage(LivingEntity victim) { return victim != this && !(victim instanceof HollowEntity) && !isAlliedTo(victim) && validTarget(victim); }
    public static float calculateSlamDamage(double distance) {
        if (distance < 0 || distance > SLAM_RADIUS) return 0;
        return (float)Mth.lerp(Mth.clamp(distance / SLAM_RADIUS, 0, 1), 16, 4);
    }
    private void slam() {
        var area = new AABB(impactPosition.x - SLAM_RADIUS, getY() - .6, impactPosition.z - SLAM_RADIUS,
                impactPosition.x + SLAM_RADIUS, getY() + 2.5, impactPosition.z + SLAM_RADIUS);
        for (var victim : level().getEntitiesOfClass(LivingEntity.class, area, this::canDamage)) {
            double distance = Math.hypot(victim.getX() - impactPosition.x, victim.getZ() - impactPosition.z);
            if (distance > SLAM_RADIUS || victim.getY() > getY() + 1.25 || victim.getBoundingBox().maxY < getY() - .5) continue;
            Vec3 from = impactPosition.add(0, .6, 0), to = victim.position().add(0, .6, 0);
            if (level().clip(new ClipContext(from, to, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this)).getType() != HitResult.Type.MISS) continue;
            victim.hurt(damageSources().mobAttack(this), calculateSlamDamage(distance));
            victim.knockback(.45 + .45 * (1 - distance / SLAM_RADIUS), impactPosition.x - victim.getX(), impactPosition.z - victim.getZ());
        }
        groundParticles(impactPosition, 90);
        ((ServerLevel)level()).sendParticles(ParticleTypes.EXPLOSION, impactPosition.x, getY() + .15, impactPosition.z, 1, 0, 0, 0, 0);
        level().playSound(null, impactPosition.x, getY(), impactPosition.z, SoundEvents.GENERIC_EXPLODE, SoundSource.HOSTILE, 1.5F, .65F);
    }
    private void groundParticles(Vec3 at, int count) {
        BlockPos floor = BlockPos.containing(at.x, getY() - .2, at.z);
        var state = level().getBlockState(floor);
        ((ServerLevel)level()).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, state), at.x, getY() + .15, at.z, count,
                count > 20 ? 2.1 : .6, .12, count > 20 ? 2.1 : .6, .13);
    }
    @Override protected void registerGoals() {
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(1, new Goal() {
            { setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK)); }
            @Override public boolean canUse() { return validTarget(getTarget()); }
            @Override public boolean canContinueToUse() { return canUse(); }
            @Override public boolean requiresUpdateEveryTick() { return true; }
            @Override public void tick() {
                if (getAttack() != Attack.IDLE) return;
                var target = getTarget();
                getLookControl().setLookAt(target, 25, 25);
                double distance = distanceTo(target);
                if (distance >= 5 && beginAttack(Attack.CHARGE, target)) return;
                if (distance <= 5 && beginAttack(Attack.SLAM, target)) return;
                if (distance <= meleeReach(target)) {
                    getNavigation().stop();
                    beginAttack(Attack.MELEE, target);
                } else getNavigation().moveTo(target, 1.1);
            }
            @Override public void stop() { cancelBossAttack(); getNavigation().stop(); }
        });
        goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, .85));
        goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 12));
        goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        targetSelector.addGoal(1, new HurtByTargetGoal(this));
        targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Villager.class, true));
        targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
    }
    @Override public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "boss", 0, state -> {
            if (getAttack() == Attack.IDLE) return state.setAndContinue(state.isMoving() ? WALK : IDLE);
            return state.setAndContinue(ACTIONS[getAttack().ordinal()]);
        }));
    }
    @Override public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("BossChargeCooldown", chargeCooldown);
        tag.putInt("BossSlamCooldown", slamCooldown);
        tag.putInt("BossMeleeCooldown", basicCooldown);
    }
    @Override public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        cancelBossAttack();
        chargeCooldown = Mth.clamp(tag.getInt("BossChargeCooldown"), 0, CHARGE_COOLDOWN);
        slamCooldown = Mth.clamp(tag.getInt("BossSlamCooldown"), 0, SLAM_COOLDOWN);
        basicCooldown = Mth.clamp(tag.getInt("BossMeleeCooldown"), 0, MELEE_COOLDOWN);
    }
    public static AttributeSupplier.Builder createAttributes() {
        return HollowEntity.createAttributes().add(Attributes.MAX_HEALTH, 50).add(Attributes.ATTACK_DAMAGE, 8)
                .add(Attributes.MOVEMENT_SPEED, .23).add(Attributes.KNOCKBACK_RESISTANCE, .75);
    }
}
