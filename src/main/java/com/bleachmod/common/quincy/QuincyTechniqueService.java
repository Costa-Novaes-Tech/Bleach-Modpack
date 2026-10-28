package com.bleachmod.common.quincy;



import com.bleachmod.Reference;

import com.bleachmod.common.data.PlayerData;

import com.bleachmod.common.network.SyncHelper;

import com.bleachmod.common.technique.BenihimeTechniqueService;

import com.bleachmod.common.technique.ElementalTechniqueService;

import com.bleachmod.common.technique.MasteryPracticeService;

import com.bleachmod.common.technique.ReleaseAnimationService;

import com.bleachmod.common.technique.SenbonzakuraTechniqueService;

import com.bleachmod.common.technique.TechniqueTargets;

import com.bleachmod.init.ModItems;

import com.bleachmod.registry.ModEntities;

import net.minecraft.core.BlockPos;

import net.minecraft.core.particles.ParticleTypes;

import net.minecraft.network.chat.Component;

import net.minecraft.server.level.ServerLevel;

import net.minecraft.server.level.ServerPlayer;

import net.minecraft.sounds.SoundEvents;

import net.minecraft.sounds.SoundSource;

import net.minecraft.world.entity.LivingEntity;

import net.minecraft.world.level.ClipContext;

import net.minecraft.world.level.block.state.BlockState;

import net.minecraft.world.phys.AABB;

import net.minecraft.world.phys.HitResult;

import net.minecraft.world.phys.Vec3;



import java.util.ArrayList;

import java.util.List;

import java.util.UUID;



/**

 * Authoritative technique executor for Quincy archetype:

 * - Common shot (handled via QuincyBowItem)

 * - Slot 1 (H): Seele Schneider

 * - Slot 2 (N): Licht Regen

 * - Slot 3 (B): Hirenkyaku

 * - Slot 4 (C): Sprenger

 */

public final class QuincyTechniqueService {

    // Seele Schneider constants

    public static final float SEELE_PE_COST = 12.0F;

    public static final int SEELE_COOLDOWN_TICKS = 120; // 6s



    // Licht Regen constants

    public static final float LICHT_REGEN_PE_COST = 20.0F;

    public static final int LICHT_REGEN_COOLDOWN_TICKS = 200; // 10s

    public static final int LICHT_REGEN_TOTAL_SHOTS = 12;

    public static final int LICHT_REGEN_DURATION_TICKS = 40; // 2s



    // Sprenger constants

    public static final float SPRENGER_PE_COST = 35.0F;

    public static final int SPRENGER_COOLDOWN_TICKS = 400; // 20s

    public static final double SPRENGER_MAX_CENTER_DISTANCE = 36.0D;

    public static final double SPRENGER_RADIUS = 6.0D;

    public static final double SPRENGER_HEIGHT = 4.0D;

    public static final int SPRENGER_PREP_TICKS = 30; // 1.5s

    public static final int SPRENGER_EXPIRATION_TICKS = 120; // 6.0s

    public static final float SPRENGER_DAMAGE = 18.0F;

    public static final int SPRENGER_MAX_TARGETS = 8;



    private QuincyTechniqueService() {

    }



    /**

     * Checks strictly whether the player data belongs to a created character of the Quincy race.

     * Possessing a bow does NOT grant Quincy race.

     */

    public static boolean isQuincy(PlayerData data) {

        return data != null && data.getStatus().hasCreatedCharacter()

                && Reference.RACE_QUINCY.equalsIgnoreCase(data.getCharacter().getRace());

    }



    public static boolean isQuincyActive(ServerPlayer player, PlayerData data) {

        return isQuincy(data);

    }



    /**

     * Authoritative technique check for Quincy slots.

     */

    public static boolean authorize(ServerPlayer player, PlayerData data, int slot) {

        if (player == null || data == null) return false;

        if (!player.isAlive() || player.isSpectator()) return false;

        if (!isQuincy(data)) return false;

        if (slot < 1 || slot > 4) return false;



        // Action and stance blocks

        if (data.getStatus().isDodging() || data.getStatus().isActionCharging()) return false;

        if (data.getGuard().active() || data.getGuard().broken()) return false;

        if (ReleaseAnimationService.busy(player) || ElementalTechniqueService.busy(player)) return false;

        if (BenihimeTechniqueService.busy(player) || SenbonzakuraTechniqueService.busy(player)) return false;



        // Bow requirement for bow-based techniques (Slot 1 - Seele Schneider, Slot 2 - Licht Regen)

        if ((slot == 1 || slot == 2) && !player.getMainHandItem().is(ModItems.QUINCY_BOW.get())) {

            player.displayClientMessage(Component.translatable("message.bleachmod.quincy.bow_required"), true);

            return false;

        }



        MasteryPracticeService.noteIntent(player);
        return true;

    }



    public static void executeSlot(ServerPlayer player, PlayerData data, int slot) {

        executeSlot(player, data, slot, 0, 0);

    }



    public static void executeSlot(ServerPlayer player, PlayerData data, int slot, int forward, int left) {

        if (!authorize(player, data, slot)) {

            return;

        }



        switch (slot) {

            case 1 -> executeSeeleSchneider(player, data);

            case 2 -> executeLichtRegen(player, data);

            case 3 -> HirenkyakuService.execute(player, data, forward, left);

            case 4 -> executeSprenger(player, data);

        }

    }



    /**

     * Minimal cleanup interface for Codex kit swapping, dimension changes, death, or logout.

     */

    public static void clearQuincyTransientState(ServerPlayer player, PlayerData data) {

        if (data != null) {

            data.getQuincy().resetTransientState();

        }

        if (player != null) {

            removeSprengerNodes(player.serverLevel(), player.getUUID(), null);

        }

    }



    // -----------------------------------------------------------------------

    // Slot 1 (H): Seele Schneider

    // -----------------------------------------------------------------------

    public static void executeSeeleSchneider(ServerPlayer player, PlayerData data) {

        if (data.getStatus().getTechniqueSlot1CooldownTicks() > 0) {

            return;

        }

        var resources = data.getResources();

        if (!resources.canAffordReiatsu(SEELE_PE_COST)) {

            player.displayClientMessage(Component.translatable("message.bleachmod.quincy.low_reiatsu"), true);

            return;

        }



        resources.consumeReiatsu(SEELE_PE_COST);

        data.getStatus().setTechniqueSlot1CooldownTicks(SEELE_COOLDOWN_TICKS);



        ServerLevel level = player.serverLevel();

        SeeleSchneiderEntity seele = new SeeleSchneiderEntity(level, player);

        Vec3 look = player.getLookAngle();

        seele.setDeltaMovement(look.scale(3.0D));

        level.addFreshEntity(seele);



        level.playSound(null, player.getX(), player.getY(), player.getZ(),

                SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.PLAYERS, 1.2F, 1.6F);

        SyncHelper.resources(player);

    }



    // -----------------------------------------------------------------------

    // Slot 2 (N): Licht Regen

    // -----------------------------------------------------------------------

    public static void executeLichtRegen(ServerPlayer player, PlayerData data) {

        if (data.getStatus().getTechniqueSlot2CooldownTicks() > 0) {

            return;

        }

        var quincy = data.getQuincy();

        if (quincy.isLichtRegenActive()) {

            return;

        }

        var resources = data.getResources();

        if (!resources.canAffordReiatsu(LICHT_REGEN_PE_COST)) {

            player.displayClientMessage(Component.translatable("message.bleachmod.quincy.low_reiatsu"), true);

            return;

        }



        resources.consumeReiatsu(LICHT_REGEN_PE_COST);

        data.getStatus().setTechniqueSlot2CooldownTicks(LICHT_REGEN_COOLDOWN_TICKS);



        quincy.startLichtRegen(player.getLookAngle());

        player.serverLevel().playSound(null, player.getX(), player.getY(), player.getZ(),

                SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.2F, 1.8F);

        SyncHelper.resources(player);

    }



    // -----------------------------------------------------------------------

    // Slot 4 (C): Sprenger

    // -----------------------------------------------------------------------

    public static void executeSprenger(ServerPlayer player, PlayerData data) {

        var quincy = data.getQuincy();

        long currentTick = player.level().getGameTime();



        if (quincy.isSprengerActive()) {

            long age = currentTick - quincy.getSprengerCreationTick();

            if (age < SPRENGER_PREP_TICKS) {

                // In preparation

                player.displayClientMessage(Component.translatable("message.bleachmod.quincy.sprenger_preparing"), true);

                return;

            }

            if (age <= SPRENGER_EXPIRATION_TICKS) {

                // Valid reactivation within window! Does NOT charge PE again and is NOT blocked by cooldown.

                detonateSprenger(player, data);

                return;

            }

            // Expired without detonating

            dissolveSprenger(player, data, false);

            return;

        }



        // Check initial cooldown

        if (data.getStatus().getTechniqueSlot4CooldownTicks() > 0) {

            return;

        }



        var resources = data.getResources();

        if (!resources.canAffordReiatsu(SPRENGER_PE_COST)) {

            player.displayClientMessage(Component.translatable("message.bleachmod.quincy.low_reiatsu"), true);

            return;

        }



        // Raycast center up to 36 blocks

        ServerLevel level = player.serverLevel();

        Vec3 eye = player.getEyePosition();

        Vec3 look = player.getLookAngle();

        Vec3 target = eye.add(look.scale(SPRENGER_MAX_CENTER_DISTANCE));

        HitResult hit = level.clip(new ClipContext(eye, target, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));

        Vec3 centerVec = hit.getType() == HitResult.Type.BLOCK ? hit.getLocation() : target;

        BlockPos centerPos = BlockPos.containing(centerVec);



        // Find 5 vertices around center at 6 blocks radius

        List<BlockPos> vertices = validateLocalTerrainPentagram(level, centerPos, SPRENGER_RADIUS);

        if (vertices == null || vertices.size() != 5) {

            player.displayClientMessage(Component.translatable("message.bleachmod.quincy.sprenger_invalid_terrain"), true);

            return;

        }



        // Valid terrain found: consume 35 PE once

        resources.consumeReiatsu(SPRENGER_PE_COST);

        data.getStatus().setTechniqueSlot4CooldownTicks(SPRENGER_COOLDOWN_TICKS);



        quincy.startSprenger(centerPos, vertices, currentTick);



        // Spawn 5 node entities

        for (int i = 0; i < vertices.size(); i++) {

            BlockPos vPos = vertices.get(i);

            SprengerNodeEntity node = new SprengerNodeEntity(ModEntities.SPRENGER_NODE.get(), level);

            node.setPos(vPos.getX() + 0.5D, vPos.getY() + 1.0D, vPos.getZ() + 0.5D);

            node.setOwner(player.getUUID(), i);

            level.addFreshEntity(node);

        }



        // Sound & feedback

        level.playSound(null, centerPos.getX(), centerPos.getY(), centerPos.getZ(),

                SoundEvents.BEACON_ACTIVATE, SoundSource.PLAYERS, 1.2F, 1.5F);

        player.displayClientMessage(Component.translatable("message.bleachmod.quincy.sprenger_started"), true);

        SyncHelper.resources(player);

    }



    private static void detonateSprenger(ServerPlayer player, PlayerData data) {

        var quincy = data.getQuincy();

        BlockPos center = quincy.getSprengerCenter();

        ServerLevel level = player.serverLevel();



        // Area bounding box: radius 6 blocks, height 4 blocks from floor

        AABB area = new AABB(center.getX() - SPRENGER_RADIUS, center.getY() - 1, center.getZ() - SPRENGER_RADIUS,

                center.getX() + SPRENGER_RADIUS + 1, center.getY() + SPRENGER_HEIGHT + 1, center.getZ() + SPRENGER_RADIUS + 1);



        int hitCount = 0;

        List<LivingEntity> targets = level.getEntitiesOfClass(LivingEntity.class, area);

        for (LivingEntity target : targets) {

            if (hitCount >= SPRENGER_MAX_TARGETS) break;

            if (!TechniqueTargets.allowed(player, target)) continue;



            // Distance check to cylinder

            double dx = target.getX() - (center.getX() + 0.5D);

            double dz = target.getZ() - (center.getZ() + 0.5D);

            double dy = target.getY() - center.getY();

            if (dx * dx + dz * dz <= SPRENGER_RADIUS * SPRENGER_RADIUS && dy >= -1 && dy <= SPRENGER_HEIGHT) {

                // Line of sight check to center (no damage through solid walls)

                HitResult los = level.clip(new ClipContext(target.getEyePosition(),

                        new Vec3(center.getX() + 0.5, center.getY() + 1.0, center.getZ() + 0.5),

                        ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, target));

                if (los.getType() == HitResult.Type.MISS) {

                    MasteryPracticeService.hurt(player, target, level.damageSources().playerAttack(player), SPRENGER_DAMAGE, 4);

                    hitCount++;

                }

            }

        }



        // Visual Reishi pillar explosion

        for (int p = 0; p < 80; p++) {

            double angle = (p / 80.0) * Math.PI * 2.0;

            double r = (p % 4 == 0) ? SPRENGER_RADIUS : (Math.random() * SPRENGER_RADIUS);

            double px = center.getX() + 0.5 + Math.cos(angle) * r;

            double pz = center.getZ() + 0.5 + Math.sin(angle) * r;

            double py = center.getY() + Math.random() * SPRENGER_HEIGHT;

            level.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, px, py, pz, 1, 0, 0.1, 0, 0.05);

            level.sendParticles(ParticleTypes.FLASH, px, py, pz, 1, 0, 0, 0, 0);

        }



        level.playSound(null, center.getX(), center.getY(), center.getZ(),

                SoundEvents.GENERIC_EXPLODE, SoundSource.PLAYERS, 1.5F, 1.2F);

        player.displayClientMessage(Component.translatable("message.bleachmod.quincy.sprenger_detonated"), true);



        // Remove field & nodes

        quincy.detonateSprenger();

        removeSprengerNodes(level, player.getUUID(), center);

    }



    public static void dissolveSprenger(ServerPlayer player, PlayerData data, boolean message) {

        var quincy = data.getQuincy();

        if (quincy.isSprengerActive()) {

            ServerLevel level = player.serverLevel();

            BlockPos center = quincy.getSprengerCenter();

            removeSprengerNodes(level, player.getUUID(), center);

            quincy.clearSprenger();

            if (message) {

                player.displayClientMessage(Component.translatable("message.bleachmod.quincy.sprenger_expired"), true);

            }

        }

    }



    public static void removeSprengerNodes(ServerLevel level, UUID ownerId, BlockPos center) {

        AABB box = center != null

                ? new AABB(center).inflate(SPRENGER_RADIUS + 8.0D)

                : new AABB(-30000000, -64, -30000000, 30000000, 320, 30000000);

        for (SprengerNodeEntity node : level.getEntitiesOfClass(SprengerNodeEntity.class, box)) {

            if (node.getOwnerUUID().map(id -> id.equals(ownerId)).orElse(false)) {

                node.discard();

            }

        }

    }



    /**

     * Finds 5 valid local ground positions in a pentagram around centerPos at the given radius.

     * Scans local terrain +/- 4 blocks vertically, supporting caves and slopes without global heightmap.

     */

    public static List<BlockPos> validateLocalTerrainPentagram(ServerLevel level, BlockPos center, double radius) {

        List<BlockPos> result = new ArrayList<>();

        double angleStep = 2.0 * Math.PI / 5.0;



        for (int i = 0; i < 5; i++) {

            double angle = i * angleStep;

            int targetX = (int) Math.round(center.getX() + Math.cos(angle) * radius);

            int targetZ = (int) Math.round(center.getZ() + Math.sin(angle) * radius);



            // Search local floor within center.y - 4 to center.y + 4

            BlockPos validFloor = null;

            for (int dy = 4; dy >= -4; dy--) {

                BlockPos probe = new BlockPos(targetX, center.getY() + dy, targetZ);

                BlockState state = level.getBlockState(probe);

                BlockState above1 = level.getBlockState(probe.above());

                BlockState above2 = level.getBlockState(probe.above(2));



                if (state.isSolid() && (above1.isAir() || above1.canBeReplaced()) && (above2.isAir() || above2.canBeReplaced())) {

                    validFloor = probe;

                    break;

                }

            }



            if (validFloor == null) {

                return null; // Invalid terrain at this vertex

            }

            result.add(validFloor);

        }

        return result;

    }



    // -----------------------------------------------------------------------

    // Tick loop integration

    // -----------------------------------------------------------------------

    public static void tickServer(ServerPlayer player, PlayerData data) {

        var quincy = data.getQuincy();



        // 1. Licht Regen tick

        if (quincy.isLichtRegenActive()) {

            quincy.advanceLichtRegen();

            int fired = quincy.getLichtRegenFiredCount();

            int ticksLeft = quincy.getLichtRegenTicksLeft();

            int targetFired = (int) Math.round(((LICHT_REGEN_DURATION_TICKS - ticksLeft) / (double) LICHT_REGEN_DURATION_TICKS) * LICHT_REGEN_TOTAL_SHOTS);



            if (targetFired > fired && fired < LICHT_REGEN_TOTAL_SHOTS) {

                ServerLevel level = player.serverLevel();

                QuincyArrowEntity arrow = new QuincyArrowEntity(level, player);

                arrow.configureLichtRegen();



                Vec3 look = quincy.getLichtRegenLook();

                // Cone of 20 degrees total: +/- 10 degrees random deflection

                net.minecraft.util.RandomSource rand = level.random;

                double yawOffset = Math.toRadians((rand.nextDouble() - 0.5) * 20.0);

                double pitchOffset = Math.toRadians((rand.nextDouble() - 0.5) * 20.0);



                Vec3 deflected = look.xRot((float) pitchOffset).yRot((float) yawOffset);

                arrow.shoot(deflected.x, deflected.y, deflected.z, 2.4F, 0.2F);

                level.addFreshEntity(arrow);



                quincy.recordLichtRegenShot();

                level.playSound(null, player.getX(), player.getY(), player.getZ(),

                        SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 0.7F, 1.6F + rand.nextFloat() * 0.4F);

            }

        }



        // 2. Sprenger timeout check

        if (quincy.isSprengerActive()) {

            long age = player.level().getGameTime() - quincy.getSprengerCreationTick();

            if (age > SPRENGER_EXPIRATION_TICKS) {

                dissolveSprenger(player, data, true);

            }

        }

    }

}

