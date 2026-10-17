package com.bleachmod.common;

import com.bleachmod.common.data.PlayerData;
import com.bleachmod.common.network.SyncHelper;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.phys.Vec3;

public final class DodgeService {
    private DodgeService() { }

    private static boolean terrestrial(ServerPlayer p) {
        return p.isAlive() && !p.isSpectator() && p.onGround()
                && !p.getAbilities().flying && !p.isFallFlying() && !p.isPassenger()
                && !p.isInWaterOrBubble() && !p.isInLava() && !p.onClimbable()
                && !p.isSleeping() && !p.isUsingItem() && !p.isFullyFrozen()
                && !p.hasEffect(MobEffects.MOVEMENT_SLOWDOWN)
                && p.getAttributeValue(net.minecraft.world.entity.ai.attributes.Attributes.MOVEMENT_SPEED) > 0;
    }

    private static boolean pathFree(ServerPlayer p, Vec3 motion) {
        var sweep = p.getBoundingBox().expandTowards(motion);
        return p.level().hasChunksAt(net.minecraft.core.BlockPos.containing(sweep.minX, sweep.minY, sweep.minZ),
                net.minecraft.core.BlockPos.containing(sweep.maxX, sweep.maxY, sweep.maxZ))
                && p.level().getWorldBorder().isWithinBounds(sweep)
                && p.level().noCollision(p, sweep);
    }

    public static boolean request(ServerPlayer p, PlayerData data, int forward, int left) {
        if (com.bleachmod.common.technique.ReleaseAnimationService.busy(p)) return false;
        var state = data.getStatus();
        if (com.bleachmod.common.technique.BenihimeTechniqueService.busy(p) || com.bleachmod.common.technique.SenbonzakuraTechniqueService.busy(p)
                || data.getGuard().active() || data.getGuard().broken() || !DodgePolicy.validInput(forward, left)
                || !state.hasCreatedCharacter() || !terrestrial(p) || state.isActionCharging()
                || state.getFlameDashTicks() > 0 || !state.canDodge(p.level().getGameTime())) return false;
        Vec3 direction = DodgePolicy.direction(forward, left, p.getYRot());
        if (direction.lengthSqr() == 0 || !pathFree(p, direction.scale(DodgePolicy.SPEED))
                || !data.getResources().trySpendDodgeStamina()) return false;
        state.beginDodge(direction, p.level().getGameTime());
        p.setSprinting(false);
        p.serverLevel().playSound(null, p.blockPosition(), SoundEvents.PLAYER_ATTACK_SWEEP,
                p.getSoundSource(), .35F, 1.4F);
        SyncHelper.resources(p);
        return true;
    }

    public static void tick(ServerPlayer p, PlayerData data) {
        var state = data.getStatus();
        if (!state.isDodging()) return;
        if (!terrestrial(p) || state.isActionCharging() || state.getFlameDashTicks() > 0
                || state.getDodgeTicks() == 0) {
            stop(p, data);
            return;
        }
        Vec3 motion = state.getDodgeDirection().scale(DodgePolicy.SPEED);
        if (!pathFree(p, motion)) {
            stop(p, data);
            return;
        }
        p.setSprinting(false);
        // Vanilla movement resolves collisions again; never accepts a destination from the client.
        p.setDeltaMovement(motion.x, p.getDeltaMovement().y, motion.z);
        p.hurtMarked = true;
        data.getResources().markStaminaEffort();
        state.advanceDodge();
        p.serverLevel().sendParticles(ParticleTypes.CLOUD, p.getX(), p.getY() + .1, p.getZ(),
                2, .12, .03, .12, .01);
    }

    private static void stop(ServerPlayer p, PlayerData data) {
        data.getStatus().stopDodge();
        Vec3 velocity = p.getDeltaMovement();
        p.setDeltaMovement(0, velocity.y, 0);
        p.hurtMarked = true;
    }
}
