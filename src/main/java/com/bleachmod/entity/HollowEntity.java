package com.bleachmod.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.*;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.control.*;
import net.minecraft.world.entity.ai.navigation.*;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.damagesource.DamageSource;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;
import java.util.EnumSet;

public class HollowEntity extends Monster implements GeoEntity {
    private static final EntityDataAccessor<String> VARIANT = SynchedEntityData.defineId(HollowEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Integer> FLIGHT_PHASE = SynchedEntityData.defineId(HollowEntity.class, EntityDataSerializers.INT);
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
    private static final RawAnimation[] IDLE = java.util.Arrays.stream(HollowVariant.values())
            .map(v -> RawAnimation.begin().thenLoop("animation.hollow." + v.id + ".idle")).toArray(RawAnimation[]::new);
    private static final RawAnimation[] WALK = java.util.Arrays.stream(HollowVariant.values())
            .map(v -> RawAnimation.begin().thenLoop("animation.hollow." + v.id + ".walk")).toArray(RawAnimation[]::new);
    private static final RawAnimation FLIGHT = RawAnimation.begin().thenLoop("animation.hollow.flying.flight");
    private static final RawAnimation DIVE = RawAnimation.begin().thenLoop("animation.hollow.flying.dive");
    private static final RawAnimation RETREAT = RawAnimation.begin().thenLoop("animation.hollow.flying.retreat");
    private LivingEntity pendingStrike;
    private int strikeWindup;
    private int meleeCooldown;
    private int successfulMeleeHits;
    private int lastMeleeStartTick = -1000000;
    private HollowFlightCombatGoal flightCombat;

    public HollowEntity(
            EntityType<? extends Monster> entityType,
            Level level
    ) {
        super(entityType, level);
        if (usesVariants()) {
            setVariant(HollowVariant.BRUTE);
            setHealth(getMaxHealth());
        }
    }

    // The legacy boss shares targeting/loot infrastructure, but keeps its own stats and model.
    public final boolean usesVariants() { return !(this instanceof HollowBossEntity); }
    public HollowVariant getVariant() { return HollowVariant.byId(entityData.get(VARIANT)); }
    public boolean isFlyingVariant() { return usesVariants() && getVariant() == HollowVariant.FLYING; }

    @Override protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(VARIANT, HollowVariant.BRUTE.id);
        entityData.define(FLIGHT_PHASE, HollowFlightCombatGoal.Phase.CIRCLE.ordinal());
    }

    public void setVariant(HollowVariant variant) {
        if (!usesVariants()) return;
        cancelMeleeStrike();
        meleeCooldown = 0;
        if (flightCombat != null) flightCombat.reset();
        entityData.set(VARIANT, variant.id);
        getAttribute(Attributes.MAX_HEALTH).setBaseValue(variant.health);
        getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(variant.speed);
        getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(variant.damage);
        getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(variant.knockbackResistance);
        setHealth(Math.min(getHealth(), getMaxHealth()));
        configureMovement();
        refreshDimensions();
    }

    private void configureMovement() {
        boolean flying = isFlyingVariant();
        if (flying != (navigation instanceof FlyingPathNavigation)) {
            navigation.stop();
            navigation = flying ? new FlyingPathNavigation(this, level()) : new GroundPathNavigation(this, level());
            navigation.setCanFloat(true);
            moveControl = flying ? new FlyingMoveControl(this, 20, true) : new MoveControl(this);
            setDeltaMovement(Vec3.ZERO);
        }
        setNoGravity(flying);
    }

    @Override public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if (VARIANT.equals(key)) refreshDimensions();
    }

    @Override public EntityDimensions getDimensions(Pose pose) {
        if (!usesVariants()) return super.getDimensions(pose);
        var v = getVariant();
        return EntityDimensions.scalable(v.width, v.height);
    }

    @Override protected float getStandingEyeHeight(Pose pose, EntityDimensions size) {
        return usesVariants() ? size.height * .86F : super.getStandingEyeHeight(pose, size);
    }

    @Override public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
            MobSpawnType reason, SpawnGroupData group, CompoundTag tag) {
        var result = super.finalizeSpawn(level, difficulty, reason, group, tag);
        if (usesVariants()) {
            var variants = HollowVariant.values();
            setVariant(tag != null && tag.contains("HollowVariant", 8)
                    ? HollowVariant.byId(tag.getString("HollowVariant")) : variants[random.nextInt(variants.length)]);
            setHealth(getMaxHealth());
        }
        return result;
    }

    @Override public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if (usesVariants()) {
            tag.putString("HollowVariant", getVariant().id);
            tag.putInt("HollowAttackCooldown", meleeCooldown);
        }
    }

    @Override public void readAdditionalSaveData(CompoundTag tag) {
        // Apply dimensions/health cap BEFORE LivingEntity restores health; a fresh brute cap
        // must never clamp a saved variant's health or reroll it when its chunk reloads.
        if (usesVariants()) setVariant(HollowVariant.byId(tag.getString("HollowVariant")));
        super.readAdditionalSaveData(tag);
        if (usesVariants()) {
            setVariant(getVariant());
            meleeCooldown = net.minecraft.util.Mth.clamp(tag.getInt("HollowAttackCooldown"), 0, getVariant().attackIntervalTicks);
        }
    }

    @Override public void travel(Vec3 input) {
        if (!isFlyingVariant()) { super.travel(input); return; }
        if (isEffectiveAi() || isControlledByLocalInstance()) {
            moveRelative(.1F, input);
            move(MoverType.SELF, getDeltaMovement());
            setDeltaMovement(getDeltaMovement().scale(.85));
        }
        calculateEntityAnimation(false);
    }

    @Override public boolean causeFallDamage(float distance, float multiplier, DamageSource source) {
        return !isFlyingVariant() && super.causeFallDamage(distance, multiplier, source);
    }

    @Override public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "locomotion", 4, state -> {
            int variant = getVariant().ordinal();
            if (isFlyingVariant()) return state.setAndContinue(switch (getFlightPhase()) {
                case DIVE -> DIVE;
                case RETREAT -> RETREAT;
                default -> FLIGHT;
            });
            return state.setAndContinue(state.isMoving() ? WALK[variant] : IDLE[variant]);
        }));
        var attack = new AnimationController<HollowEntity>(this, "attack", 0, state -> PlayState.STOP);
        for (var variant : HollowVariant.values()) attack.triggerableAnim("strike_" + variant.id,
                RawAnimation.begin().thenPlay("animation.hollow." + variant.id + ".attack"));
        controllers.add(attack);
    }

    @Override public boolean doHurtTarget(Entity target) {
        if (usesVariants() && !level().isClientSide) triggerAnim("attack", "strike_" + getVariant().id);
        return super.doHurtTarget(target);
    }

    public int meleeWindupTicks() {
        return switch (getVariant()) { case BRUTE -> 8; case SWIFT -> 3; case FLYING -> 5; };
    }

    private double meleeReachSqr(LivingEntity target) {
        // Small bodies otherwise stop at a completed navigation waypoint just
        // beyond their custom reach and never resume attacking narrow targets.
        double reach = Math.max(1.6, (getBbWidth() + target.getBbWidth()) * .5 + .7);
        return reach * reach;
    }

    /** The natural melee goal starts anticipation; only the contact frame applies damage. */
    public boolean beginMeleeStrike(LivingEntity target) {
        if (!usesVariants() || level().isClientSide || pendingStrike != null || meleeCooldown > 0 || !isAlive() || isNoAi()
                || isFlyingVariant() && getFlightPhase() == HollowFlightCombatGoal.Phase.RETREAT
                || !validStrikeTarget(target)) return false;
        pendingStrike = target;
        strikeWindup = meleeWindupTicks();
        meleeCooldown = getVariant().attackIntervalTicks;
        lastMeleeStartTick = tickCount;
        triggerAnim("attack", "strike_" + getVariant().id);
        return true;
    }

    public void cancelMeleeStrike() { pendingStrike = null; strikeWindup = 0; }
    public boolean hasPendingMeleeStrike() { return pendingStrike != null; }
    public int getMeleeCooldownTicks() { return meleeCooldown; }
    public int getSuccessfulMeleeHits() { return successfulMeleeHits; }
    public int getLastMeleeStartTick() { return lastMeleeStartTick; }
    public HollowFlightCombatGoal getFlightCombat() { return flightCombat; }
    public HollowFlightCombatGoal.Phase getFlightPhase() {
        int id = entityData.get(FLIGHT_PHASE);
        var phases = HollowFlightCombatGoal.Phase.values();
        return phases[Math.max(0, Math.min(phases.length - 1, id))];
    }
    public void setFlightPhase(HollowFlightCombatGoal.Phase phase) { entityData.set(FLIGHT_PHASE, phase.ordinal()); }

    boolean validStrikeTarget(LivingEntity target) {
        return target != null && target.isAlive() && target.level() == level()
                && (!(target instanceof Player player) || !player.isCreative() && !player.isSpectator())
                && distanceToSqr(target) <= meleeReachSqr(target) && hasLineOfSight(target);
    }

    @Override public void aiStep() {
        if (!level().isClientSide && meleeCooldown > 0) meleeCooldown--;
        if (!level().isClientSide && pendingStrike != null) {
            if (!isAlive() || isNoAi() || getTarget() != pendingStrike || !validStrikeTarget(pendingStrike)) cancelMeleeStrike();
            else if (--strikeWindup <= 0) {
                var target = pendingStrike;
                cancelMeleeStrike();
                swing(InteractionHand.MAIN_HAND);
                // Bypass the immediate-hit animation hook: its anticipation is already playing.
                if (super.doHurtTarget(target)) successfulMeleeHits++;
            }
        }
        super.aiStep();
    }

    @Override public AnimatableInstanceCache getAnimatableInstanceCache() { return geoCache; }

    @Override
    protected void registerGoals() {

        this.goalSelector.addGoal(
                0,
                new FloatGoal(this)
        );

        this.goalSelector.addGoal(
                1,
                new MeleeAttackGoal(
                        this,
                        1.2D,
                        true
                ) {
                    @Override public boolean canUse() { return !isFlyingVariant() && super.canUse(); }
                    @Override public boolean canContinueToUse() { return !isFlyingVariant() && super.canContinueToUse(); }
                    @Override protected void checkAndPerformAttack(LivingEntity target, double distance) {
                        if (!usesVariants()) { super.checkAndPerformAttack(target, distance); return; }
                        // Vanilla's private 20-tick timer would still throttle the swift
                        // variant. The common entity owns cadence, including canceled hits.
                        if (distanceToSqr(target) <= meleeReachSqr(target) && hasLineOfSight(target)) {
                            // Stop at striking distance instead of walking past the target
                            // and waiting for vanilla's random stationary-target repath.
                            getNavigation().stop();
                            getMoveControl().setWantedPosition(getX(), getY(), getZ(), 0);
                            beginMeleeStrike(target);
                        } else closeGroundMeleeGap(target);
                    }
                    @Override protected double getAttackReachSqr(LivingEntity target) {
                        if (!usesVariants()) return super.getAttackReachSqr(target);
                        return meleeReachSqr(target);
                    }
                    @Override public void stop() { super.stop(); cancelMeleeStrike(); }
                }
        );
        flightCombat = new HollowFlightCombatGoal(this);
        this.goalSelector.addGoal(1, flightCombat);

        this.goalSelector.addGoal(
                2,
                new WaterAvoidingRandomStrollGoal(
                        this,
                        1.0D
                ) {
                    @Override public boolean canUse() { return !isFlyingVariant() && super.canUse(); }
                    @Override public boolean canContinueToUse() { return !isFlyingVariant() && super.canContinueToUse(); }
                }
        );
        this.goalSelector.addGoal(2, new FlightWanderGoal());

        this.goalSelector.addGoal(
                3,
                new LookAtPlayerGoal(
                        this,
                        Player.class,
                        8.0F
                )
        );

        this.goalSelector.addGoal(
                4,
                new RandomLookAroundGoal(this)
        );

        this.targetSelector.addGoal(
                1,
                new HurtByTargetGoal(this)
        );

        this.targetSelector.addGoal(
                2,
                new NearestAttackableTargetGoal<>(
                        this,
                        Player.class,
                        true
                )
        );

        this.targetSelector.addGoal(
                2,
                new NearestAttackableTargetGoal<>(
                        this,
                        Villager.class,
                        true)
        );

        this.targetSelector.addGoal(
                2,
                new NearestAttackableTargetGoal<>(
                        this,
                        IronGolem.class,
                        true)
        );
    }

    private void closeGroundMeleeGap(LivingEntity target) {
        if (!getNavigation().isDone() || !onGround() || distanceToSqr(target) > 9
                || Math.abs(getY() - target.getY()) > .75 || !hasLineOfSight(target)) return;
        Vec3 offset = target.position().subtract(position()).multiply(1, 0, 1);
        if (offset.lengthSqr() < .01) return;
        Vec3 step = offset.normalize().scale(.35);
        BlockPos support = BlockPos.containing(position().add(step).add(0, -.2, 0));
        if (level().getBlockState(support).isFaceSturdy(level(), support, net.minecraft.core.Direction.UP)
                && level().noCollision(this, getBoundingBox().move(step))) {
            getMoveControl().setWantedPosition(target.getX(), target.getY(), target.getZ(), 1.2);
        }
    }

    private final class FlightWanderGoal extends Goal {
        private Vec3 destination;
        FlightWanderGoal() { setFlags(EnumSet.of(Flag.MOVE)); }
        @Override public boolean canUse() {
            if (!isFlyingVariant() || getTarget() != null || random.nextInt(30) != 0) return false;
            BlockPos ground = level().getHeightmapPos(net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    blockPosition().offset(random.nextInt(13) - 6, 0, random.nextInt(13) - 6));
            BlockPos candidate = ground.above(2 + random.nextInt(3));
            if (Math.abs(candidate.getY() - getY()) > 8 || !level().isEmptyBlock(candidate)
                    || !level().isEmptyBlock(candidate.above())) return false;
            destination = Vec3.atBottomCenterOf(candidate);
            return true;
        }
        @Override public void start() { navigation.moveTo(destination.x, destination.y, destination.z, 1); }
        @Override public boolean canContinueToUse() { return isFlyingVariant() && getTarget() == null && !navigation.isDone(); }
        @Override public void stop() { navigation.stop(); }
    }

    public static boolean canSpawn(
            EntityType<HollowEntity> entityType,
            LevelAccessor level,
            MobSpawnType spawnType,
            BlockPos pos,
            RandomSource random
    ) {
        return Mob.checkMobSpawnRules(
                entityType,
                level,
                spawnType,
                pos,
                random
        );
    }

    @Override
    public int getMaxSpawnClusterSize() {
        if (this.level().isDay()) {
            return 3;
        }

        return 6;
    }

    public static AttributeSupplier.Builder createAttributes() {

        return Monster.createMonsterAttributes()
                .add(Attributes.FLYING_SPEED, .32D)

                // Vida
                .add(
                        Attributes.MAX_HEALTH,
                        20.0D
                )

                // Velocidade
                .add(
                        Attributes.MOVEMENT_SPEED,
                        0.30D
                )

                // Dano
                .add(
                        Attributes.ATTACK_DAMAGE,
                        6.0D
                )

                // Distância de detecção
                .add(
                        Attributes.FOLLOW_RANGE,
                        32.0D
                )

                // Resistência a knockback
                .add(
                        Attributes.KNOCKBACK_RESISTANCE,
                        0.15D
                );
    }
}
