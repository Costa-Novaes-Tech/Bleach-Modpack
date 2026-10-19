package com.bleachmod.common.technique;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.BlockEvent;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Permanent terrain destruction for Zaraki's blows (user decision, cycle 38). Only natural terrain is
 * carved: soil, sand, gravel, base stone, netherrack, snow, ice, leaves and plants. Never block entities
 * (chests, furnaces), ores, crafted blocks or unbreakable blocks. Each block passes Forge's BreakEvent as
 * the wielder, so claim/protection mods and spawn protection can refuse it. No item drops (avoids item
 * spam); a few blocks are hurled up as falling rubble and land nearby.
 */
public final class BerserkTerrain {
    public static final int MAX_BLOCKS = 420, MAX_RUBBLE = 12, NOISY_BREAKS = 20;
    private BerserkTerrain() { }

    public static boolean natural(BlockState state) {
        if (state.isAir() || state.hasBlockEntity() || !state.getFluidState().isEmpty()) return false;
        return state.is(BlockTags.DIRT) || state.is(BlockTags.SAND) || state.is(BlockTags.BASE_STONE_OVERWORLD)
                || state.is(BlockTags.BASE_STONE_NETHER) || state.is(BlockTags.NYLIUM) || state.is(BlockTags.TERRACOTTA)
                || state.is(BlockTags.SNOW) || state.is(BlockTags.ICE) || state.is(BlockTags.LEAVES)
                || state.is(BlockTags.REPLACEABLE_BY_TREES) || state.is(Blocks.GRAVEL) || state.is(Blocks.CLAY)
                || state.is(Blocks.SANDSTONE) || state.is(Blocks.RED_SANDSTONE) || state.is(Blocks.END_STONE)
                || state.is(Blocks.CALCITE) || state.is(Blocks.DRIPSTONE_BLOCK) || state.is(Blocks.SOUL_SAND)
                || state.is(Blocks.SOUL_SOIL) || state.is(Blocks.MAGMA_BLOCK) || state.is(Blocks.SNOW_BLOCK);
    }

    /** Topmost non-air block of a column, searched from a little above the impact height. */
    private static int surface(ServerLevel level, int x, int y, int z) {
        var pos = new BlockPos.MutableBlockPos(x, y + 2, z);
        for (int i = 0; i < 8; i++, pos.move(0, -1, 0)) if (!level.getBlockState(pos).isAir()) return pos.getY();
        return Integer.MIN_VALUE;
    }

    /** Bowl-shaped crater: deepest at the center, following the local surface of each column. */
    public static int crater(ServerPlayer owner, Vec3 center, double radius, int depth) {
        return crater(owner, center, radius, depth, 0);
    }
    /** As crater, sparing a central column of radius {@code spare} (the wielder stays on solid ground). */
    public static int crater(ServerPlayer owner, Vec3 center, double radius, int depth, double spare) {
        Map<BlockPos, Integer> columns = new LinkedHashMap<>();
        addDisk(columns, center, radius, depth, spare);
        return carve(owner, columns, (int) Math.floor(center.y));
    }
    /** Trench along the ground, tapering at both ends. */
    public static int trench(ServerPlayer owner, Vec3 origin, Vec3 forward, double length, double halfWidth, int depth) {
        Map<BlockPos, Integer> columns = new LinkedHashMap<>();
        Vec3 dir = new Vec3(forward.x, 0, forward.z).normalize();
        for (double d = 1; d <= length; d += .5) {
            double tail = Math.min(1, (length - d) / 3 + .34); // shallower over the last three blocks
            addDisk(columns, origin.add(dir.scale(d)), halfWidth, Math.max(1, (int) Math.round(depth * tail)), 0);
        }
        return carve(owner, columns, (int) Math.floor(origin.y));
    }
    /** Bulldozes natural terrain in the way of a charge at body height. */
    public static int bulldoze(ServerPlayer owner, Vec3 forward) {
        Vec3 ahead = owner.position().add(new Vec3(forward.x, 0, forward.z).normalize().scale(1.2));
        int broken = 0;
        for (int side = -1; side <= 1; side++) for (int dy = 0; dy <= 2; dy++) {
            var pos = BlockPos.containing(ahead.x - forward.z * side * .8, owner.getY() + dy + .2, ahead.z + forward.x * side * .8);
            if (breakOne(owner, pos, broken < 6, false)) broken++;
        }
        return broken;
    }

    private static void addDisk(Map<BlockPos, Integer> columns, Vec3 center, double radius, int depth, double spare) {
        int r = (int) Math.ceil(radius);
        for (int dx = -r; dx <= r; dx++) for (int dz = -r; dz <= r; dz++) {
            double dist = Math.sqrt(dx * dx + dz * dz);
            if (dist > radius || dist < spare) continue;
            int d = Math.max(1, (int) Math.round(depth * Math.sqrt(1 - (dist / radius) * (dist / radius))));
            columns.merge(BlockPos.containing(center.x + dx, 0, center.z + dz), d, Math::max);
        }
    }

    private static int carve(ServerPlayer owner, Map<BlockPos, Integer> columns, int y) {
        ServerLevel level = owner.serverLevel();
        int broken = 0, rubble = 0;
        for (var entry : columns.entrySet()) {
            int x = entry.getKey().getX(), z = entry.getKey().getZ();
            if (!level.hasChunkAt(new BlockPos(x, y, z))) continue;
            int top = surface(level, x, y, z);
            if (top == Integer.MIN_VALUE) continue;
            int remaining = entry.getValue();
            for (int layer = 0; remaining > 0 && broken < MAX_BLOCKS; layer++) {
                var pos = new BlockPos(x, top - layer, z);
                var state = level.getBlockState(pos);
                // Plants and snow layers are cleared too but do not count toward the crater depth.
                boolean cover = state.is(BlockTags.REPLACEABLE_BY_TREES) || state.is(Blocks.SNOW);
                boolean hurl = !cover && rubble < MAX_RUBBLE && level.random.nextFloat() < .12F;
                if (!breakOne(owner, pos, broken < NOISY_BREAKS, hurl)) break; // stop at the first protected/unnatural block
                broken++;
                if (hurl) rubble++;
                if (!cover) remaining--;
            }
        }
        return broken;
    }

    private static boolean breakOne(ServerPlayer owner, BlockPos pos, boolean noisy, boolean hurl) {
        ServerLevel level = owner.serverLevel();
        BlockState state = level.getBlockState(pos);
        if (!natural(state) || state.getDestroySpeed(level, pos) < 0 || !level.mayInteract(owner, pos)
                || MinecraftForge.EVENT_BUS.post(new BlockEvent.BreakEvent(level, pos, state, owner))) return false;
        if (hurl && !state.is(BlockTags.LEAVES)) {
            FallingBlockEntity rubble = FallingBlockEntity.fall(level, pos, state);
            rubble.disableDrop();
            Vec3 away = Vec3.atCenterOf(pos).subtract(owner.position()).multiply(1, 0, 1).normalize();
            rubble.setDeltaMovement(away.x * .35 + (level.random.nextDouble() - .5) * .3, .55 + level.random.nextDouble() * .4,
                    away.z * .35 + (level.random.nextDouble() - .5) * .3);
            rubble.hurtMarked = true;
            return true;
        }
        if (noisy) level.destroyBlock(pos, false, owner);
        else level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
        return true;
    }

    /** Crater radius/depth of a plain hammer blow per form. */
    public static double hammerRadius(String form) { return "bankai".equals(form) ? 2.2 : "shikai".equals(form) ? 1.6 : 1.0; }
    public static int hammerDepth(String form) { return "bankai".equals(form) ? 2 : 1; }
}
