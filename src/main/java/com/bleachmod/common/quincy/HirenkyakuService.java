package com.bleachmod.common.quincy;

import com.bleachmod.common.DodgePolicy;
import com.bleachmod.common.data.PlayerData;
import com.bleachmod.common.network.SyncHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

/**
 * Hirenkyaku (Flying Screen Step) movement service (Slot 3 - B).
 * High-speed spiritual Reishi glide under feet: 6 blocks horizontally.
 * Validates collision and precipices, ensuring safe stops without flight or clipping.
 */
public final class HirenkyakuService {
    public static final float PE_COST = 8.0F;
    public static final float STAMINA_COST = 10.0F;
    public static final int COOLDOWN_TICKS = 120; // 6s
    public static final double TOTAL_DISTANCE = 6.0D;
    public static final double STEP_SIZE = 0.5D;

    private HirenkyakuService() {
    }

    public static boolean execute(ServerPlayer player, PlayerData data, int forward, int left) {
        if (!player.isAlive() || player.isSpectator()) {
            return false;
        }

        // Check cooldown
        if (data.getStatus().getTechniqueSlot3CooldownTicks() > 0) {
            return false;
        }

        // Check resources: 8 PE and 10 Stamina
        var resources = data.getResources();
        if (!resources.canAffordReiatsu(PE_COST)) {
            player.displayClientMessage(Component.translatable("message.bleachmod.quincy.low_reiatsu"), true);
            return false;
        }
        if (resources.getStamina() < STAMINA_COST) {
            player.displayClientMessage(Component.translatable("message.bleachmod.quincy.low_stamina"), true);
            return false;
        }

        // Calculate horizontal direction
        Vec3 dir = DodgePolicy.direction(forward, left, player.getYRot());
        if (dir.lengthSqr() < 1e-4) {
            // Default to backward step if no input
            dir = DodgePolicy.direction(-1, 0, player.getYRot());
        }

        ServerLevel level = player.serverLevel();
        Vec3 start = player.position();
        Vec3 current = start;
        AABB box = player.getBoundingBox();

        int maxSteps = (int) Math.round(TOTAL_DISTANCE / STEP_SIZE);
        for (int i = 1; i <= maxSteps; i++) {
            Vec3 candidate = start.add(dir.scale(i * STEP_SIZE));
            AABB candidateBox = box.move(candidate.subtract(start));

            // Collision check: must have no block collisions
            if (!level.noCollision(player, candidateBox) || !level.getWorldBorder().isWithinBounds(candidateBox)) {
                break;
            }

            // Precipice check: ensure there is a solid surface within 3 blocks below, avoiding cliffs into void
            BlockPos groundProbe = BlockPos.containing(candidate.x, candidate.y - 0.2, candidate.z);
            boolean safeGround = false;
            for (int dy = 0; dy <= 3; dy++) {
                BlockPos check = groundProbe.below(dy);
                if (!level.getBlockState(check).isAir()) {
                    safeGround = true;
                    break;
                }
            }

            if (!safeGround && !player.getAbilities().flying) {
                // Stop at edge of cliff
                break;
            }

            current = candidate;
        }

        if (current.distanceToSqr(start) < 0.25D) {
            // Nowhere to step safely
            return false;
        }

        // Consume resources
        resources.consumeReiatsu(PE_COST);
        resources.tickStamina(true); // Registers effort
        data.getStatus().setTechniqueSlot3CooldownTicks(COOLDOWN_TICKS);

        // Perform safe displacement
        player.teleportTo(current.x, current.y, current.z);
        player.resetFallDistance();
        player.hurtMarked = true;

        // Visual Reishi slipstream particles under feet
        double dist = current.distanceTo(start);
        int particleCount = (int) (dist * 4);
        for (int p = 0; p <= particleCount; p++) {
            double frac = (double) p / particleCount;
            Vec3 pPos = start.lerp(current, frac);
            level.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, pPos.x, pPos.y + 0.1, pPos.z, 1, 0.1, 0.05, 0.1, 0.02);
            level.sendParticles(ParticleTypes.ELECTRIC_SPARK, pPos.x, pPos.y + 0.1, pPos.z, 2, 0.1, 0.05, 0.1, 0.02);
        }

        level.playSound(null, current.x, current.y, current.z, SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 0.8F, 1.8F);
        SyncHelper.resources(player);
        return true;
    }
}
