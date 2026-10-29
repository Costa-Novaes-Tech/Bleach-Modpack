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

    private static final net.minecraft.network.syncher.EntityDataAccessor<Boolean> PIERCING = net.minecraft.network.syncher.SynchedEntityData.defineId(QuincyArrowEntity.class, net.minecraft.network.syncher.EntityDataSerializers.BOOLEAN);
    private final java.util.Set<java.util.UUID> piercedEntities = new java.util.HashSet<>();
    private final java.util.Set<net.minecraft.core.BlockPos> piercedBlocks = new java.util.HashSet<>();
    private int techniqueSlot;
    @Override protected void defineSynchedData() { super.defineSynchedData(); entityData.define(PIERCING, false); }
    public boolean isVollstandigArrow() { return entityData.get(PIERCING); }
    public int piercedEntityCount() { return piercedEntities.size(); }
    public int piercedBlockCount() { return piercedBlocks.size(); }
    public void configureVollstandig(float power) { configureCommon(power); entityData.set(PIERCING, true); maxRange=96; }
    public void configureSatellite(float damage) { configureCommon(1); spiritualDamage=damage; maxRange=96; }
    public void setTechniqueSlot(int slot){if(slot<0||slot>4)throw new IllegalArgumentException("Quincy slot");techniqueSlot=slot;}
    public void setSpiritualDamage(float damage) { spiritualDamage=damage; techniqueSlot=1; }
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
        entityData.set(PIERCING,false);
        this.isLichtRegen = false;
        this.maxRange = COMMON_MAX_RANGE;
        this.spiritualDamage = COMMON_BASE_DAMAGE * Math.max(0.25F, Math.min(1.0F, power));
        this.setNoGravity(true);
    }

    public void configureLichtRegen() {
        entityData.set(PIERCING,false);
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
        if (isVollstandigArrow()) { tickPiercing(); return; }
        // Reject unloaded destinations before vanilla collision queries.
        if (!loadedSegment(position(), position().add(getDeltaMovement()))) { discard(); return; }
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

    private boolean loadedSegment(Vec3 start, Vec3 end) {
        for(var bp:net.minecraft.core.BlockPos.betweenClosed(net.minecraft.core.BlockPos.containing(Math.min(start.x,end.x),Math.min(start.y,end.y),Math.min(start.z,end.z)).offset(-1,-1,-1),net.minecraft.core.BlockPos.containing(Math.max(start.x,end.x),Math.max(start.y,end.y),Math.max(start.z,end.z)).offset(1,1,1)))
            if(!level().hasChunkAt(bp)) return false;
        // betweenClosed sorts each axis, so reverse-directed segments are covered too.
        return true;
    }
    private record Collision(double distance, Vec3 point, net.minecraft.core.BlockPos block, LivingEntity target) { }
    /** Ordered, swept collisions: block budget belongs to the complete flight, never a single tick. */
    private void tickPiercing() {
        if(!loadedSegment(position(),position().add(getDeltaMovement()))){discard();return;}
        baseTick();
        Vec3 start=position(), motion=getDeltaMovement();
        double remaining=maxRange-start.distanceTo(spawnPos);
        if(remaining<=0 || ++lifeTicks>=COMMON_MAX_LIFE_TICKS){discard();return;}
        Vec3 end=start.add(motion.length()>remaining?motion.normalize().scale(remaining):motion);
        var min=net.minecraft.core.BlockPos.containing(new Vec3(Math.min(start.x,end.x),Math.min(start.y,end.y),Math.min(start.z,end.z))).offset(-1,-1,-1);
        var max=net.minecraft.core.BlockPos.containing(new Vec3(Math.max(start.x,end.x),Math.max(start.y,end.y),Math.max(start.z,end.z))).offset(1,1,1);
        for(var bp:net.minecraft.core.BlockPos.betweenClosed(min,max)) if(!level().hasChunkAt(bp)){discard();return;}
        if(!level().isClientSide){
            java.util.List<Collision> hits=new java.util.ArrayList<>();
            for(var bp:net.minecraft.core.BlockPos.betweenClosed(min,max)){
                if(piercedBlocks.contains(bp))continue;
                var shape=level().getBlockState(bp).getCollisionShape(level(),bp);
                var hit=shape.clip(start,end,bp);
                if(hit!=null)hits.add(new Collision(start.distanceToSqr(hit.getLocation()),hit.getLocation(),bp.immutable(),null));
                else if(shape.toAabbs().stream().anyMatch(box->box.move(bp).contains(start)))hits.add(new Collision(0,start,bp.immutable(),null));
            }
            for(var t:level().getEntitiesOfClass(LivingEntity.class,new net.minecraft.world.phys.AABB(start,end).inflate(.3),t->t!=getOwner()&&!piercedEntities.contains(t.getUUID())&&t.isAlive())){
                if(getOwner() instanceof ServerPlayer owner&&!TechniqueTargets.allowed(owner,t))continue;
                var box=t.getBoundingBox().inflate(.15);
                var hit=box.contains(start)?java.util.Optional.of(start):box.clip(start,end);
                hit.ifPresent(v->hits.add(new Collision(start.distanceToSqr(v),v,null,t)));
            }
            hits.sort(java.util.Comparator.comparingDouble(Collision::distance).thenComparing(h->h.block()==null?1:0));
            for(var hit:hits){
                if(hit.block()!=null){
                    var state=level().getBlockState(hit.block());
                    if(piercedBlocks.size()>=2 || state.getDestroySpeed(level(),hit.block())<0 || state.hasBlockEntity()){
                        setPos(hit.point());dissipateOnImpact(hit.point());discard();return;
                    }
                    piercedBlocks.add(hit.block());dissipateOnImpact(hit.point());
                }else{
                    piercedEntities.add(hit.target().getUUID());onHitEntity(new EntityHitResult(hit.target(),hit.point()));
                    if(piercedEntities.size()>=5){setPos(hit.point());discard();return;}
                }
            }
        }
        setPos(end);setDeltaMovement(motion.scale(.99));
        double horizontal=motion.horizontalDistance();
        setYRot((float)(net.minecraft.util.Mth.atan2(motion.x,motion.z)*180/Math.PI));
        setXRot((float)(net.minecraft.util.Mth.atan2(motion.y,horizontal)*180/Math.PI));
        if(level().isClientSide)level().addParticle(ParticleTypes.ELECTRIC_SPARK,end.x,end.y,end.z,0,0,0);
        if(end.distanceTo(spawnPos)>=maxRange-.001)discard();
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
                        MasteryPracticeService.hurt(player, target, damageSources().playerAttack(player), spiritualDamage, techniqueSlot);
                    }
                    target.playSound(SoundEvents.PLAYER_HURT, 0.8F, 1.4F);
                }
            } else {
                target.hurt(damageSources().arrow(this, owner), spiritualDamage);
            }
        }
        if (!isVollstandigArrow()) discard();
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
        tag.putBoolean("vollstandigPiercing", isVollstandigArrow());
        tag.putInt("techniqueSlot",techniqueSlot);
        var entities=new net.minecraft.nbt.ListTag(); for(var id:piercedEntities) entities.add(net.minecraft.nbt.StringTag.valueOf(id.toString())); tag.put("piercedEntities",entities);
        tag.putLongArray("piercedBlocks",piercedBlocks.stream().mapToLong(net.minecraft.core.BlockPos::asLong).toArray());
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
        entityData.set(PIERCING, tag.getBoolean("vollstandigPiercing"));techniqueSlot=tag.getInt("techniqueSlot");
        piercedEntities.clear();var ids=tag.getList("piercedEntities",8);for(int i=0;i<Math.min(5,ids.size());i++)try{piercedEntities.add(java.util.UUID.fromString(ids.getString(i)));}catch(IllegalArgumentException ignored){}
        piercedBlocks.clear();for(long b:tag.getLongArray("piercedBlocks")){if(piercedBlocks.size()>=2)break;piercedBlocks.add(net.minecraft.core.BlockPos.of(b));}
        if (tag.contains("maxRange")) maxRange = tag.getDouble("maxRange");
        if (tag.contains("spiritualDamage")) spiritualDamage = tag.getFloat("spiritualDamage");
        if (tag.contains("isLichtRegen")) isLichtRegen = tag.getBoolean("isLichtRegen");
        if (tag.contains("lifeTicks")) lifeTicks = tag.getInt("lifeTicks");
        if (tag.contains("spawnX")) {
            spawnPos = new Vec3(tag.getDouble("spawnX"), tag.getDouble("spawnY"), tag.getDouble("spawnZ"));
        }
    }
}
