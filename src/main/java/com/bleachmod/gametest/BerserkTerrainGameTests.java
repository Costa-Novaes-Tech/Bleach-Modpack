package com.bleachmod.gametest;

import com.bleachmod.common.technique.BerserkTerrain;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.gametest.*;
import java.util.UUID;

@GameTestHolder("bleachmod")
@PrefixGameTestTemplate(false)
public final class BerserkTerrainGameTests {
    private BerserkTerrainGameTests() { }

    /** Craters carve natural terrain permanently but never touch containers or crafted blocks. */
    @GameTest(template = "empty", batch = "berserk_terrain")
    public static void craterCarvesOnlyNaturalTerrain(GameTestHelper h) {
        for (int x = 0; x < 5; x++) for (int z = 0; z < 5; z++) {
            h.setBlock(new BlockPos(x, 1, z), Blocks.STONE);
            h.setBlock(new BlockPos(x, 2, z), Blocks.GRASS_BLOCK);
        }
        h.setBlock(new BlockPos(2, 3, 2), Blocks.GRASS); // plant cover does not count as depth
        h.setBlock(new BlockPos(1, 2, 2), Blocks.CHEST);
        h.setBlock(new BlockPos(3, 2, 2), Blocks.IRON_BLOCK);
        var player = new FakePlayer(h.getLevel(), new GameProfile(UUID.randomUUID(), "BerserkTerrain"));
        var center = h.absolutePos(new BlockPos(2, 3, 2));
        player.setPos(center.getX() + 5.5, center.getY(), center.getZ() + .5);
        int broken = BerserkTerrain.crater(player, Vec3.atBottomCenterOf(center), 2.2, 2);
        h.assertTrue(broken > 0, "Crater broke nothing");
        h.assertBlockPresent(Blocks.AIR, new BlockPos(2, 3, 2));
        h.assertBlockPresent(Blocks.AIR, new BlockPos(2, 2, 2));
        h.assertBlockPresent(Blocks.AIR, new BlockPos(2, 1, 2));
        h.assertBlockPresent(Blocks.CHEST, new BlockPos(1, 2, 2));
        h.assertBlockPresent(Blocks.IRON_BLOCK, new BlockPos(3, 2, 2));
        h.assertTrue(!BerserkTerrain.natural(Blocks.DIAMOND_ORE.defaultBlockState()) && !BerserkTerrain.natural(Blocks.OAK_PLANKS.defaultBlockState())
                && BerserkTerrain.natural(Blocks.DEEPSLATE.defaultBlockState()) && BerserkTerrain.natural(Blocks.SAND.defaultBlockState()), "Natural terrain policy");
        h.killAllEntities();
        h.succeed();
    }
}
