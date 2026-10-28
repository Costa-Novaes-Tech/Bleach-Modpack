package com.bleachmod.common.quincy;

import com.bleachmod.common.DodgePolicy;
import com.bleachmod.common.data.PlayerData;
import com.bleachmod.common.network.SyncHelper;
import com.bleachmod.common.technique.BenihimeTechniqueService;
import com.bleachmod.common.technique.ElementalTechniqueService;
import com.bleachmod.common.technique.ReleaseAnimationService;
import com.bleachmod.common.technique.SenbonzakuraTechniqueService;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

/**
 * Hirenkyaku (Flying Screen Step) movement service (Slot 3 - B).
 * High-speed spiritual Reishi glide under feet: 6 blocks horizontally.
 * Validates swept collision volume, thin walls, and precipices, ensuring safe stops without flight or clipping.
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

        // Action and stance blocks
        if (data.getStatus().isDodging() || data.getStatus().isActionCharging()) {
            return false;
        }
        if (data.getGuard().active() || data.getGuard().broken()) {
            return false;
        }
        if (ReleaseAnimationService.busy(player) || ElementalTechniqueService.busy(player)
                || BenihimeTechniqueService.busy(player) || SenbonzakuraTechniqueService.busy(player)) {
            return false;
        }
        if (data.getStatus().getFlameDashTicks() > 0 || player.isPassenger()) {
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

        // Resolve horizontal direction: inspect network input, server movement state, or retreat
        int f = forward;
        int l = left;
        if (f == 0 && l == 0) {
            if (player.zza != 0) {
                f = player.zza > 0 ? 1 : -1;
            }
            if (player.xxa != 0) {
                l = player.xxa > 0 ? 1 : -1;
            }
        }

        Vec3 dir;
        if (f == 0 && l == 0) {
            // Default to backward retreat if neutral
            dir = DodgePolicy.direction(-1, 0, player.getYRot());
        } else {
            dir = DodgePolicy.direction(f, l, player.getYRot());
        }

        ServerLevel level = player.serverLevel();
        Vec3 start = player.position();
        Vec3 current = start;
        AABB currentBox = player.getBoundingBox();

        int maxSteps = (int) Math.round(TOTAL_DISTANCE / STEP_SIZE);
        for (int i = 1; i <= maxSteps; i++) {
            Vec3 candidate = start.add(dir.scale(i * STEP_SIZE));
            AABB candidateBox = player.getBoundingBox().move(candidate.subtract(start));
            AABB sweepBox = currentBox.minmax(candidateBox);

            // Swept volume collision check: prevents tunneling through thin walls, fences, or bars
            if (!level.noCollision(player, sweepBox) || !level.getWorldBorder().isWithinBounds(candidateBox)) {
                break;
            }

            // Real ground support and precipice check
            BlockPos groundProbe = BlockPos.containing(candidate.x, candidate.y - 0.2, candidate.z);
            boolean safeGround = false;
            for (int dy = 0; dy <= 3; dy++) {
                BlockPos check = groundProbe.below(dy);
                BlockState state = level.getBlockState(check);
                if (state.liquid()) {
                    // Water without solid base stops the glide safely at the shore
                    break;
                }
                if (state.isSolid() || !state.getCollisionShape(level, check).isEmpty()) {
                    safeGround = true;
                    break;
                }
            }

            if (!safeGround && !player.getAbilities().flying) {
                // Stop safely at the edge of cliffs or chasms
                break;
            }

            current = candidate;
            currentBox = candidateBox;
        }

        if (current.distanceToSqr(start) < 0.25D) {
            // Nowhere to step safely
            return false;
        }

        // Consume resources: PE honors reiatsu cheats, Stamina is always consumed
        resources.consumeReiatsu(PE_COST);
        resources.spendStamina(STAMINA_COST);
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
