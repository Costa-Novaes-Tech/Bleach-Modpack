package com.bleachmod.common.quincy;

import com.bleachmod.registry.ModEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

import java.util.Optional;
import java.util.UUID;

/**
 * Seele Schneider node planted at one of the 5 vertices of a Sprenger formation.
 */
public class SprengerNodeEntity extends Entity {
    private static final EntityDataAccessor<Optional<UUID>> OWNER_UUID =
            SynchedEntityData.defineId(SprengerNodeEntity.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<Integer> VERTEX_INDEX =
            SynchedEntityData.defineId(SprengerNodeEntity.class, EntityDataSerializers.INT);

    public SprengerNodeEntity(EntityType<?> type, Level level) {
        super(type, level);
        this.noPhysics = true;
    }

    public void setOwner(UUID ownerId, int index) {
        this.entityData.set(OWNER_UUID, Optional.ofNullable(ownerId));
        this.entityData.set(VERTEX_INDEX, index);
    }

    public Optional<UUID> getOwnerUUID() {
        return this.entityData.get(OWNER_UUID);
    }

    public int getVertexIndex() {
        return this.entityData.get(VERTEX_INDEX);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(OWNER_UUID, Optional.empty());
        this.entityData.define(VERTEX_INDEX, 0);
    }

    @Override
    public void tick() {
        super.tick();

        // Expire after 120 ticks (6.0s)
        if (tickCount > 120) {
            discard();
            return;
        }

        if (level().isClientSide) {
            // Reishi vapor and catalyst sparks at the node
            level().addParticle(ParticleTypes.SOUL_FIRE_FLAME, getX(), getY() + 0.5, getZ(), 0, 0.05, 0);
            level().addParticle(ParticleTypes.ELECTRIC_SPARK, getX(), getY() + 0.8, getZ(), 0, 0.02, 0);
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        if (tag.hasUUID("ownerId")) {
            entityData.set(OWNER_UUID, Optional.of(tag.getUUID("ownerId")));
        }
        if (tag.contains("vertexIndex")) {
            entityData.set(VERTEX_INDEX, tag.getInt("vertexIndex"));
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        getOwnerUUID().ifPresent(id -> tag.putUUID("ownerId", id));
        tag.putInt("vertexIndex", getVertexIndex());
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
