package com.bleachmod.entity;

import com.bleachmod.common.network.NetworkHandler;
import com.bleachmod.common.network.s2c.OpenNpcQuestScreenS2C;
import com.bleachmod.registry.ModEntities;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;

public final class QuestNpcEntity extends Mob {
    private static final net.minecraft.network.syncher.EntityDataAccessor<String> QUEST_CONTEXT =
            net.minecraft.network.syncher.SynchedEntityData.defineId(QuestNpcEntity.class,
                    net.minecraft.network.syncher.EntityDataSerializers.STRING);
    @Override protected void defineSynchedData() { super.defineSynchedData(); entityData.define(QUEST_CONTEXT, ""); }
    public String getQuestContext() { return entityData.get(QUEST_CONTEXT); }
    public void setQuestContext(String value) {
        if (!value.isEmpty() && !com.bleachmod.common.quest.StoryQuestInfo.validContext(value))
            throw new IllegalArgumentException("Invalid quest context");
        entityData.set(QUEST_CONTEXT, value);
        if (level() instanceof net.minecraft.server.level.ServerLevel server)
            com.bleachmod.common.quest.QuestNpcLocations.get(server.getServer()).update(this);
    }
    @Override public void addAdditionalSaveData(net.minecraft.nbt.CompoundTag tag) {
        super.addAdditionalSaveData(tag); tag.putString("QuestContext", getQuestContext());
    }
    @Override public void readAdditionalSaveData(net.minecraft.nbt.CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        String value = tag.getString("QuestContext");
        entityData.set(QUEST_CONTEXT, com.bleachmod.common.quest.StoryQuestInfo.validContext(value) ? value : "");
    }
    public QuestNpcEntity(EntityType<? extends Mob> entityType, Level level) {
        super(entityType, level);
        setPersistenceRequired();
        setCustomName(entityType.getDescription());
        setCustomNameVisible(true);
    }

    public String getNpcId() {
        return ModEntities.getNpcId(getType());
    }

    @Override public void tick() {
        super.tick();
        if (level() instanceof net.minecraft.server.level.ServerLevel serverLevel && tickCount % 20 == 0)
            com.bleachmod.common.quest.QuestNpcLocations.get(serverLevel.getServer()).update(this);
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(0, new LookAtPlayerGoal(this, Player.class, 8.0F, 1.0F));
    }

    @Override
    public void travel(Vec3 movement) {
        setDeltaMovement(Vec3.ZERO);
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void doPush(Entity entity) {
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return true;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, net.minecraft.world.DifficultyInstance difficulty,
                                        MobSpawnType reason, SpawnGroupData spawnData,
                                        net.minecraft.nbt.CompoundTag dataTag) {
        setPersistenceRequired();
        setYBodyRot(getYRot());
        setYHeadRot(getYRot());
        return super.finalizeSpawn(level, difficulty, reason, spawnData, dataTag);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (hand != InteractionHand.MAIN_HAND) {
            return InteractionResult.PASS;
        }
        if (!level().isClientSide && player instanceof ServerPlayer serverPlayer) {
            if (com.bleachmod.common.quest.StoryQuestService.interact(serverPlayer, this)) return InteractionResult.CONSUME;
            NetworkHandler.sendToPlayer(new OpenNpcQuestScreenS2C(getNpcId(), getId()), serverPlayer);
        }
        return InteractionResult.sidedSuccess(level().isClientSide);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
                .add(Attributes.FOLLOW_RANGE, 0.0D);
    }
}
