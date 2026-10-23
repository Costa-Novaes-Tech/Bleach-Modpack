package com.bleachmod.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import java.util.EnumSet;

/** Collision-aware aerial passes: circle, descend, strike 2-3 times, climb away. */
public final class HollowFlightCombatGoal extends Goal {
    public enum Phase { CIRCLE, DIVE, BURST, RETREAT }
    public static final int MIN_RETREAT_TICKS = 60, MAX_RETREAT_TICKS = 100;
    private final HollowEntity mob;
    private LivingEntity target;
    private int age, repath, attempts, budget, lastStrikeAge, passes, retreats, lastBurstAttempts;
    private double angle;
    private Vec3 retreatDirection = Vec3.ZERO;

    public HollowFlightCombatGoal(HollowEntity mob) {
        this.mob = mob;
        setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override public boolean canUse() { return eligible(mob.getTarget()); }
    @Override public boolean canContinueToUse() { return mob.getTarget() == target && eligible(target); }
    private boolean eligible(LivingEntity candidate) {
        double range = mob.getAttributeValue(Attributes.FOLLOW_RANGE) + 12;
        return mob.isFlyingVariant() && mob.isAlive() && !mob.isNoAi() && candidate != null
                && candidate.isAlive() && candidate.level() == mob.level()
                && (!(candidate instanceof Player p) || !p.isCreative() && !p.isSpectator())
                && mob.distanceToSqr(candidate) < range * range && mob.level().hasChunkAt(candidate.blockPosition());
    }

    @Override public void start() {
        target = mob.getTarget();
        angle = mob.getRandom().nextDouble() * Math.PI * 2;
        attempts = 0;
        budget = 2 + mob.getRandom().nextInt(2);
        phase(Phase.CIRCLE);
    }

    @Override public boolean requiresUpdateEveryTick() { return true; }
    @Override public void tick() {
        if (!eligible(target)) return;
        age++;
        repath--;
        mob.getLookControl().setLookAt(target, 30, 30);
        switch (mob.getFlightPhase()) {
            case CIRCLE -> {
                angle += .045;
                if (repath <= 0) orbit(7, 4.5, 1.1);
                if (age >= 35) phase(Phase.DIVE);
            }
            case DIVE -> {
                if (repath <= 0) approach(1.35);
                if (mob.distanceToSqr(target) <= 6 && mob.hasLineOfSight(target)) {
                    phase(Phase.BURST);
                    mob.setAggressive(true);
                } else if (age >= 100) retreat();
            }
            case BURST -> {
                if (repath <= 0) approach(1.05);
                if (attempts < budget && mob.beginMeleeStrike(target)) {
                    attempts++;
                    lastStrikeAge = age;
                }
                // Finish the last contact AND its follow-through before climbing.
                if (attempts >= budget && !mob.hasPendingMeleeStrike() && age - lastStrikeAge >= 13
                        || age >= 125 || mob.distanceToSqr(target) > 64) retreat();
            }
            case RETREAT -> {
                if (repath <= 0) {
                    angle = Math.atan2(retreatDirection.z, retreatDirection.x);
                    orbit(8.5, 5, 1.25);
                }
                if (age >= MIN_RETREAT_TICKS && mob.distanceToSqr(target) >= 25 && mob.getY() > target.getY() + 2
                        || age >= MAX_RETREAT_TICKS) {
                    attempts = 0;
                    budget = 2 + mob.getRandom().nextInt(2);
                    angle += Math.PI * .5;
                    phase(Phase.CIRCLE);
                }
            }
        }
    }

    private void approach(double speed) {
        if (mob.getFlightPhase() == Phase.BURST && mob.validStrikeTarget(target)) {
            // Brake before anticipation: dive inertia must not carry the creature
            // back outside contact range while its claws are winding up.
            repath = 1;
            mob.getNavigation().stop();
            mob.getMoveControl().setWantedPosition(mob.getX(), mob.getY(), mob.getZ(), 0);
            mob.setDeltaMovement(Vec3.ZERO);
            return;
        }
        repath = 7;
        Vec3 radial = mob.position().subtract(target.position()).multiply(1, 0, 1);
        if (radial.lengthSqr() < .01) radial = new Vec3(Math.cos(angle), 0, Math.sin(angle));
        radial = radial.normalize();
        // A diagonal point outside a circle can still overlap both axes of a
        // rectangular entity hitbox. Clear the target's actual AABB on one axis.
        double radii = (mob.getBbWidth() + target.getBbWidth()) * .5;
        double standOff = radii / Math.max(Math.abs(radial.x), Math.abs(radial.z)) + .15;
        Vec3 point = target.position().add(radial.scale(standOff)).add(0, .2, 0);
        // FlyingPathNavigation computes the path, including walls and ceilings.
        moveTo(point, speed);
    }

    private void orbit(double radius, double height, double speed) {
        repath = 12;
        // Try neighboring directions and lower clearances in confined spaces.
        // Never use direct no-physics steering as a fallback through solid blocks.
        for (double clearance : new double[]{height, 3, 1.5}) for (int i = 0; i < 8; i++) {
            double candidate = angle + i * Math.PI / 4;
            Vec3 point = target.position().add(Math.cos(candidate) * radius, clearance, Math.sin(candidate) * radius);
            if (moveTo(point, speed)) return;
        }
    }

    private boolean moveTo(Vec3 point, double speed) {
        double y = Mth.clamp(point.y, mob.level().getMinBuildHeight() + 1,
                mob.level().getMaxBuildHeight() - mob.getBbHeight() - 1);
        var destination = new Vec3(point.x, y, point.z);
        if (!mob.level().hasChunkAt(BlockPos.containing(destination))
                || !mob.level().noCollision(mob, mob.getBoundingBox().move(destination.subtract(mob.position())))) return false;
        // Navigation waypoints are block-centered. Resolve the final short segment
        // precisely, checking the whole body along it, so the flyer doesn't hover
        // forever just outside melee reach. Entity.move still resolves collisions.
        Vec3 delta = destination.subtract(mob.position());
        if (delta.lengthSqr() < 9 && clearLocalSegment(delta)) {
            repath = 1; // FlyingMoveControl consumes MOVE_TO each tick; keep the final steering active.
            mob.getNavigation().stop();
            mob.getMoveControl().setWantedPosition(destination.x, destination.y, destination.z, speed);
            return true;
        }
        return mob.getNavigation().moveTo(destination.x, destination.y, destination.z, speed);
    }

    private boolean clearLocalSegment(Vec3 delta) {
        int steps = Math.max(1, (int)Math.ceil(delta.length() * 4));
        for (int i = 1; i <= steps; i++) if (!mob.level().noCollision(mob,
                mob.getBoundingBox().move(delta.scale((double)i / steps)))) return false;
        return true;
    }

    private void retreat() {
        lastBurstAttempts = attempts;
        passes++;
        retreats++;
        Vec3 away = mob.position().subtract(target.position()).multiply(1, 0, 1);
        retreatDirection = away.lengthSqr() < .01 ? new Vec3(Math.cos(angle), 0, Math.sin(angle)) : away.normalize();
        mob.cancelMeleeStrike();
        mob.setAggressive(false);
        phase(Phase.RETREAT);
    }

    private void phase(Phase phase) {
        mob.getNavigation().stop();
        mob.setFlightPhase(phase);
        age = 0;
        repath = 0;
    }

    public void reset() {
        mob.cancelMeleeStrike();
        target = null;
        attempts = 0;
        age = 0;
        repath = 0;
        mob.setFlightPhase(Phase.CIRCLE);
    }
    @Override public void stop() {
        mob.getNavigation().stop();
        mob.setAggressive(false);
        if (mob.getTarget() == target && !eligible(target)) mob.setTarget(null);
        reset();
    }
    public int getStartedStrikes() { return attempts; }
    public int getStrikeBudget() { return budget; }
    public int getCompletedPasses() { return passes; }
    public int getRetreats() { return retreats; }
    public int getLastBurstAttempts() { return lastBurstAttempts; }
    public int getPhaseAge() { return age; }
}
