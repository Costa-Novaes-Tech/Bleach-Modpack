package com.bleachmod.entity;

import com.bleachmod.Reference;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.*;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.EnumSet;

/** Acidwire's supplied one-second claw clip; contact at 0.55 seconds is server-owned.
 * Stats are provisional early-campaign tuning, not canonical power measurements.
 */
public final class AcidwireEntity extends Monster implements GeoEntity {
    public static final int WINDUP_TICKS = 11, ATTACK_DURATION = 20, COOLDOWN_TICKS = 36;
    public static final double MELEE_REACH = 2.5;
    private static final EntityDataAccessor<Integer> ATTACK_TICK = SynchedEntityData.defineId(AcidwireEntity.class, EntityDataSerializers.INT);
    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("animation.acidwire.idle");
    private static final RawAnimation WALK = RawAnimation.begin().thenLoop("animation.acidwire.walk");
    private static final RawAnimation CLAW = RawAnimation.begin().thenPlay("animation.acidwire.attack");
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final ServerBossEvent bossBar = new ServerBossEvent(getDisplayName(), BossEvent.BossBarColor.PURPLE, BossEvent.BossBarOverlay.PROGRESS);
    private LivingEntity attackTarget;
    private int cooldown;
    private float attackYaw;

    public AcidwireEntity(EntityType<? extends AcidwireEntity> type, Level level) {
        super(type, level);
        xpReward = 0; // Campaign reward belongs to the final conversation, not the kill.
        setPersistenceRequired();
    }
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 160)
                .add(Attributes.ATTACK_DAMAGE, 6).add(Attributes.MOVEMENT_SPEED, .25)
                .add(Attributes.FOLLOW_RANGE, 40).add(Attributes.KNOCKBACK_RESISTANCE, .6);
    }
    @Override protected void defineSynchedData() { super.defineSynchedData(); entityData.define(ATTACK_TICK, -1); }
    public boolean isAttacking() { return getAttackTick() >= 0; }
    public int getAttackTick() { return entityData.get(ATTACK_TICK); }
    public int getAttackCooldownTicks() { return cooldown; }
    public boolean canTarget(LivingEntity target) {
        if (target == null || !target.isAlive() || target.isRemoved() || target.level() != level()
                || target instanceof Player player && (player.isCreative() || player.isSpectator())) return false;
        return !getPersistentData().hasUUID(Reference.TAG_QUEST_OWNER)
                || target.getUUID().equals(getPersistentData().getUUID(Reference.TAG_QUEST_OWNER));
    }
    public boolean beginAttack(LivingEntity target) {
        if (level().isClientSide || !isAlive() || isNoAi() || isAttacking() || cooldown > 0
                || !canTarget(target) || distanceToSqr(target) > MELEE_REACH*MELEE_REACH || !hasLineOfSight(target)) return false;
        setTarget(target); attackTarget = target;
        Vec3 direction = target.position().subtract(position());
        attackYaw = (float)Math.toDegrees(Math.atan2(-direction.x, direction.z));
        setYRot(attackYaw); setYBodyRot(attackYaw); setYHeadRot(attackYaw);
        getNavigation().stop(); setDeltaMovement(0, getDeltaMovement().y, 0);
        cooldown = COOLDOWN_TICKS; entityData.set(ATTACK_TICK, 0);
        playSound(SoundEvents.SPIDER_AMBIENT, .65F, .65F);
        return true;
    }
    public void cancelAttack() {
        entityData.set(ATTACK_TICK, -1); attackTarget = null;
    }
    @Override public void setNoAi(boolean value) { super.setNoAi(value); if (value) cancelAttack(); }
    @Override public boolean doHurtTarget(Entity target) { return target instanceof LivingEntity living && beginAttack(living); }
    @Override public void travel(Vec3 input) {
        if (isAttacking()) { setDeltaMovement(0, getDeltaMovement().y, 0); super.travel(Vec3.ZERO); }
        else super.travel(input);
    }
    @Override public void aiStep() {
        if (!level().isClientSide) {
            bossBar.setName(getDisplayName()); bossBar.setProgress(Mth.clamp(getHealth()/getMaxHealth(),0,1));
            if (cooldown > 0) cooldown--;
            if (isAttacking()) {
                if (!isAlive() || isNoAi() || !canTarget(attackTarget) || getTarget() != attackTarget) cancelAttack();
                else {
                    getNavigation().stop();
                    setYRot(attackYaw); setYBodyRot(attackYaw); setYHeadRot(attackYaw);
                    int age = getAttackTick()+1; entityData.set(ATTACK_TICK, age);
                    if (age == WINDUP_TICKS) {
                        var direction = attackTarget.position().subtract(position()).multiply(1,0,1).normalize();
                        var facing = new Vec3(-Math.sin(Math.toRadians(attackYaw)),0,Math.cos(Math.toRadians(attackYaw)));
                        if (distanceToSqr(attackTarget) <= MELEE_REACH*MELEE_REACH && hasLineOfSight(attackTarget)
                                && facing.dot(direction) >= .25) {
                            super.doHurtTarget(attackTarget);
                            playSound(SoundEvents.PLAYER_ATTACK_SWEEP, .9F, .7F);
                        }
                    }
                    if (age >= ATTACK_DURATION) cancelAttack();
                }
            }
        }
        super.aiStep();
    }
    @Override protected void registerGoals() {
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(1, new Goal() {
            { setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK)); }
            @Override public boolean canUse() { return canTarget(getTarget()); }
            @Override public boolean canContinueToUse() { return canUse(); }
            @Override public boolean requiresUpdateEveryTick() { return true; }
            @Override public void tick() {
                var target = getTarget();
                if (!canTarget(target) || isAttacking()) return;
                getLookControl().setLookAt(target, 30, 30);
                if (distanceToSqr(target) <= MELEE_REACH*MELEE_REACH && hasLineOfSight(target)) {
                    getNavigation().stop(); beginAttack(target);
                } else getNavigation().moveTo(target, 1.1);
            }
            @Override public void stop() { getNavigation().stop(); cancelAttack(); }
        });
        goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, .7));
        goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 12));
        goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, this::canTarget));
    }
    @Override public boolean hurt(DamageSource source, float amount) {
        boolean hurt = super.hurt(source, amount);
        if (hurt && source.getEntity() instanceof LivingEntity attacker && canTarget(attacker)) setTarget(attacker);
        return hurt;
    }
    @Override public void die(DamageSource source) { cancelAttack(); bossBar.removeAllPlayers(); super.die(source); }
    @Override public void remove(RemovalReason reason) { bossBar.removeAllPlayers(); super.remove(reason); }
    @Override public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        if (!getPersistentData().hasUUID(Reference.TAG_QUEST_OWNER)
                || player.getUUID().equals(getPersistentData().getUUID(Reference.TAG_QUEST_OWNER))) bossBar.addPlayer(player);
    }
    @Override public void stopSeenByPlayer(ServerPlayer player) { super.stopSeenByPlayer(player); bossBar.removePlayer(player); }
    @Override public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag); tag.putInt("AcidwireCooldown", cooldown);
    }
    @Override public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag); cancelAttack(); cooldown = Mth.clamp(tag.getInt("AcidwireCooldown"),0,COOLDOWN_TICKS);
    }
    @Override public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "body", 0, state ->
                state.setAndContinue(isAttacking() ? CLAW : state.isMoving() ? WALK : IDLE)));
    }
    @Override public AnimatableInstanceCache getAnimatableInstanceCache() { return cache; }
}
