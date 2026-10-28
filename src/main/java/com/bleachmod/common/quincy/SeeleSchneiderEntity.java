package com.bleachmod.common.quincy;

import com.bleachmod.common.technique.MasteryPracticeService;
import com.bleachmod.common.technique.TechniqueTargets;
import com.bleachmod.registry.ModEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.*;
import net.minecraftforge.network.NetworkHooks;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Seele Schneider spiritual arrow/blade projectile (Slot 1 - H).
 * High speed piercing line (0.5 block corridor) that damages up to 3 distinct targets.
 * Stopped by solid walls.
 */
public class SeeleSchneiderEntity extends Projectile {
    public static final double MAX_RANGE = 48.0D;
    public static final float DAMAGE_PER_TARGET = 10.0F;
    public static final int MAX_TARGETS = 3;
    public static final double CORRIDOR_WIDTH = 0.5D;

    private Vec3 spawnPos;
    private final Set<UUID> hitTargets = new HashSet<>();

    public SeeleSchneiderEntity(EntityType<? extends SeeleSchneiderEntity> type, Level level) {
        super(type, level);
        this.noPhysics = false;
    }

    public SeeleSchneiderEntity(Level level, LivingEntity shooter) {
        super(ModEntities.SEELE_SCHNEIDER_PROJECTILE.get(), level);
        setOwner(shooter);
        setPos(shooter.getX(), shooter.getEyeY() - 0.1D, shooter.getZ());
        this.spawnPos = shooter.position();
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    public void tick() {
        super.tick();

        if (spawnPos == null) {
            spawnPos = position();
        }

        // Distance check
        if (position().distanceTo(spawnPos) > MAX_RANGE) {
            discard();
            return;
        }

        Vec3 motion = getDeltaMovement();
        Vec3 currentPos = position();
        Vec3 nextPos = currentPos.add(motion);

        // Block raycast (wall stops the Seele Schneider)
        HitResult blockHit = level().clip(new ClipContext(currentPos, nextPos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        if (blockHit.getType() != HitResult.Type.MISS) {
            onHitBlock((BlockHitResult) blockHit);
            return;
        }

        // Entity corridor raycast (0.5 block width)
        AABB corridorBox = getBoundingBox().expandTowards(motion).inflate(CORRIDOR_WIDTH);
        for (LivingEntity target : level().getEntitiesOfClass(LivingEntity.class, corridorBox)) {
            if (target == getOwner() || hitTargets.contains(target.getUUID())) {
                continue;
            }
            if (target.getBoundingBox().inflate(CORRIDOR_WIDTH).intersects(getBoundingBox().expandTowards(motion))) {
                onHitLiving(target);
                if (hitTargets.size() >= MAX_TARGETS) {
                    discard();
                    return;
                }
            }
        }

        setPos(nextPos);

        // Visual blade particles
        if (level().isClientSide) {
            level().addParticle(ParticleTypes.SOUL_FIRE_FLAME, currentPos.x, currentPos.y, currentPos.z, 0, 0, 0);
            level().addParticle(ParticleTypes.ELECTRIC_SPARK, currentPos.x, currentPos.y, currentPos.z, 0, 0, 0);
        }
    }

    private void onHitLiving(LivingEntity target) {
        if (level().isClientSide) return;
        var owner = getOwner();
        if (owner instanceof ServerPlayer player) {
            if (TechniqueTargets.allowed(player, target)) {
                hitTargets.add(target.getUUID());
                MasteryPracticeService.hurt(player, target, damageSources().playerAttack(player), DAMAGE_PER_TARGET, 1);
                target.playSound(SoundEvents.PLAYER_ATTACK_SWEEP, 1.0F, 1.6F);
            }
        } else {
            hitTargets.add(target.getUUID());
            target.hurt(damageSources().generic(), DAMAGE_PER_TARGET);
        }
    }

    protected void onHitBlock(BlockHitResult hit) {
        if (!level().isClientSide) {
            // Wall interrupts immediately
            discard();
        }
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        if (spawnPos != null) {
            tag.putDouble("spawnX", spawnPos.x);
            tag.putDouble("spawnY", spawnPos.y);
            tag.putDouble("spawnZ", spawnPos.z);
        }
        tag.putInt("hitsCount", hitTargets.size());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        if (tag.contains("spawnX")) {
            spawnPos = new Vec3(tag.getDouble("spawnX"), tag.getDouble("spawnY"), tag.getDouble("spawnZ"));
        }
    }
}
