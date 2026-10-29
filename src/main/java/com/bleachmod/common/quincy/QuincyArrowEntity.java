package com.bleachmod.common.quincy;

import com.bleachmod.common.data.PlayerCapability;
import com.bleachmod.common.technique.MasteryPracticeService;
import com.bleachmod.common.technique.TechniqueTargets;
import com.bleachmod.init.ModItems;
import com.bleachmod.registry.ModEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

/**
 * Spiritual Reishi arrow for Quincy Kojaku bow and Licht Regen.
 * Common shot flies without gravity up to 64 blocks with straight trajectory.
 * Licht Regen retains descending rain ballistics up to 40 blocks.
 */
public class QuincyArrowEntity extends AbstractArrow {
    public static final double COMMON_MAX_RANGE = 64.0D;
    public static final double LICHT_REGEN_MAX_RANGE = 40.0D;
    public static final float COMMON_BASE_DAMAGE = 6.0F;
    public static final float LICHT_REGEN_DAMAGE = 2.0F;
    public static final int LICHT_REGEN_MAX_HITS_PER_TARGET = 4; // 4 hits * 2 HP = 8 HP ceiling
    public static final int COMMON_MAX_LIFE_TICKS = 60; // 3 seconds at 20 tps
    public static final int LICHT_REGEN_MAX_LIFE_TICKS = 40; // 2 seconds

    private Vec3 spawnPos;
    private double maxRange = COMMON_MAX_RANGE;
    private float spiritualDamage = COMMON_BASE_DAMAGE;
    private boolean isLichtRegen;
    private int lifeTicks = 0;

    public QuincyArrowEntity(EntityType<? extends QuincyArrowEntity> type, Level level) {
        super(type, level);
        this.pickup = Pickup.DISALLOWED;
    }

    public QuincyArrowEntity(Level level, LivingEntity shooter) {
        super(ModEntities.QUINCY_ARROW.get(), shooter, level);
        this.pickup = Pickup.DISALLOWED;
        this.spawnPos = shooter.position();
    }

    @Override
    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        super.shoot(x, y, z, velocity, inaccuracy);
        this.spawnPos = position();
    }

    public void configureCommon(float power) {
        this.isLichtRegen = false;
        this.maxRange = COMMON_MAX_RANGE;
        this.spiritualDamage = COMMON_BASE_DAMAGE * Math.max(0.25F, Math.min(1.0F, power));
        this.setNoGravity(true);
    }

    public void configureLichtRegen() {
        this.isLichtRegen = true;
        this.maxRange = LICHT_REGEN_MAX_RANGE;
        this.spiritualDamage = LICHT_REGEN_DAMAGE;
        this.setNoGravity(false);
    }

    public boolean isLichtRegen() {
        return isLichtRegen;
    }

    public float getSpiritualDamage() {
        return spiritualDamage;
    }

    public double getMaxRange() {
        return maxRange;
    }

    public int getLifeTicks() {
        return lifeTicks;
    }

    @Override
    public void tick() {
        if (spawnPos == null) {
            spawnPos = position();
        }
        super.tick();

        lifeTicks++;
        int maxLife = isLichtRegen ? LICHT_REGEN_MAX_LIFE_TICKS : COMMON_MAX_LIFE_TICKS;

        // Enforce maximum range, explicit lifetime, and chunk safety without loading distant chunks
        if (lifeTicks >= maxLife || position().distanceTo(spawnPos) > maxRange || !level().hasChunkAt(blockPosition())) {
            discard();
            return;
        }

        // Budgeted Reishi particle trail in client view
        if (level().isClientSide) {
            Vec3 pos = position();
            Vec3 motion = getDeltaMovement();
            level().addParticle(ParticleTypes.ELECTRIC_SPARK,
                    pos.x - motion.x * 0.25, pos.y - motion.y * 0.25, pos.z - motion.z * 0.25,
                    0, 0, 0);
        }
    }

    private void dissipateOnImpact(Vec3 hitPos) {
        Vec3 pos = hitPos != null ? hitPos : position();
        if (level().isClientSide) {
            for (int i = 0; i < 6; i++) {
                double vx = (random.nextDouble() - 0.5) * 0.2;
                double vy = (random.nextDouble() - 0.5) * 0.2;
                double vz = (random.nextDouble() - 0.5) * 0.2;
                level().addParticle(ParticleTypes.ELECTRIC_SPARK, pos.x, pos.y, pos.z, vx, vy, vz);
            }
        } else if (level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.ELECTRIC_SPARK, pos.x, pos.y, pos.z, 6, 0.1, 0.1, 0.1, 0.05);
        }
    }

    @Override
    public void onHitEntity(EntityHitResult result) {
        dissipateOnImpact(result.getLocation());
        if (level().isClientSide) {
            return;
        }
        if (result.getEntity() instanceof LivingEntity target) {
            var owner = getOwner();
            if (owner instanceof ServerPlayer player) {
                if (TechniqueTargets.allowed(player, target)) {
                    if (isLichtRegen) {
                        PlayerCapability.get(player).ifPresent(data -> {
                            var quincy = data.getQuincy();
                            int currentHits = quincy.getLichtRegenHitsOn(target.getUUID());
                            if (currentHits < LICHT_REGEN_MAX_HITS_PER_TARGET) {
                                // Authoritative hit without bypassing vanilla hurtTime
                                MasteryPracticeService.hurt(player, target, damageSources().playerAttack(player), spiritualDamage, 2);
                                quincy.recordLichtRegenHit(target.getUUID());
                            }
                        });
                    } else {
                        MasteryPracticeService.hurt(player, target, damageSources().playerAttack(player), spiritualDamage, 0);
                    }
                    target.playSound(SoundEvents.PLAYER_HURT, 0.8F, 1.4F);
                }
            } else {
                target.hurt(damageSources().arrow(this, owner), spiritualDamage);
            }
        }
        discard();
    }

    @Override
    public void onHitBlock(BlockHitResult result) {
        dissipateOnImpact(result.getLocation());
        super.onHitBlock(result);
        if (!level().isClientSide) {
            discard();
        }
    }

    @Override
    protected ItemStack getPickupItem() {
        return new ItemStack(ModItems.QUINCY_ARROW.get());
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putDouble("maxRange", maxRange);
        tag.putFloat("spiritualDamage", spiritualDamage);
        tag.putBoolean("isLichtRegen", isLichtRegen);
        tag.putInt("lifeTicks", lifeTicks);
        if (spawnPos != null) {
            tag.putDouble("spawnX", spawnPos.x);
            tag.putDouble("spawnY", spawnPos.y);
            tag.putDouble("spawnZ", spawnPos.z);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("maxRange")) maxRange = tag.getDouble("maxRange");
        if (tag.contains("spiritualDamage")) spiritualDamage = tag.getFloat("spiritualDamage");
        if (tag.contains("isLichtRegen")) isLichtRegen = tag.getBoolean("isLichtRegen");
        if (tag.contains("lifeTicks")) lifeTicks = tag.getInt("lifeTicks");
        if (tag.contains("spawnX")) {
            spawnPos = new Vec3(tag.getDouble("spawnX"), tag.getDouble("spawnY"), tag.getDouble("spawnZ"));
        }
    }
}
