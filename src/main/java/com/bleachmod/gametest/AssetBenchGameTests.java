package com.bleachmod.gametest;

import com.bleachmod.common.visual.VanillaAssetBench;
import com.bleachmod.common.data.PlayerCapability;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.*;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.gametest.*;
import java.util.*;

@GameTestHolder("bleachmod")
@PrefixGameTestTemplate(false)
public final class AssetBenchGameTests {
    private static FakePlayer player(GameTestHelper h) {
        var p = new FakePlayer(h.getLevel(), new GameProfile(UUID.randomUUID(), "AssetBench"));
        var pos = h.absolutePos(new BlockPos(1, 3, 1)); p.setPos(pos.getX() + .5, h.getLevel().getMaxBuildHeight() - 64, pos.getZ() + .5);
        h.getLevel().getChunkAt(BlockPos.containing(p.position().add(0, 0, 10))); return p;
    }
    @GameTest(template = "empty")
    public static void commandsRequireOperatorAndRejectInvalidPreview(GameTestHelper h) {
        var p = player(h); var dispatcher = p.getServer().getCommands().getDispatcher();
        try {
            boolean rejected = false;
            try { dispatcher.execute("bleachdev assets preview frost", p.createCommandSourceStack().withPermission(0)); }
            catch (com.mojang.brigadier.exceptions.CommandSyntaxException e) { rejected = true; }
            h.assertTrue(rejected && VanillaAssetBench.remaining(p) == 0, "Non-operator opened bench");
            h.assertTrue(dispatcher.execute("bleachdev assets preview missing", p.createCommandSourceStack().withPermission(2)) == 0, "Unknown preset accepted");
            h.assertTrue(dispatcher.execute("bleachdev assets preview frost 3", p.createCommandSourceStack().withPermission(2)) == 1, "Preview command failed");
            int emitted = VanillaAssetBench.emitted(p); rejected = false;
            try { dispatcher.execute("bleachdev assets preview frost 4", p.createCommandSourceStack().withPermission(2)); }
            catch (com.mojang.brigadier.exceptions.CommandSyntaxException e) { rejected = true; }
            h.assertTrue(rejected && VanillaAssetBench.emitted(p) == emitted, "Invalid intensity mutated session");
            h.assertTrue(dispatcher.execute("bleachdev assets stop", p.createCommandSourceStack().withPermission(2)) == 1 && VanillaAssetBench.remaining(p) == 0, "Stop failed");
        } catch (com.mojang.brigadier.exceptions.CommandSyntaxException e) { throw new IllegalStateException(e); }
        finally { VanillaAssetBench.stop(p); }
        h.succeed();
    }
    @GameTest(template = "empty", timeoutTicks = 160)
    public static void previewIsBoundedExpiresAndDoesNotChangeGameplay(GameTestHelper h) {
        var p = player(h); var data = PlayerCapability.get(p).orElseThrow(() -> new IllegalStateException("Missing capability")); data.initializeShinigami();
        var before = data.save(); var pos = BlockPos.containing(p.position().add(0, 0, 4)); var block = h.getLevel().getBlockState(pos);
        boolean rain = h.getLevel().isRaining();
        h.assertTrue(VanillaAssetBench.start(p, VanillaAssetBench.Preset.FROST, 3), "Preview failed");
        int emitted = VanillaAssetBench.emitted(p); VanillaAssetBench.tick(p);
        h.assertTrue(emitted == 48 && VanillaAssetBench.emitted(p) == emitted, "Frame budget or tick deduplication failed");
        h.assertTrue(data.save().equals(before) && h.getLevel().getBlockState(pos).equals(block) && h.getLevel().isRaining() == rain, "Preview changed character/terrain/weather");
        h.runAfterDelay(100, () -> { VanillaAssetBench.tick(p); h.assertTrue(VanillaAssetBench.remaining(p) == 0 && VanillaAssetBench.active(p).equals("none"), "Preview did not expire"); h.succeed(); });
    }
    @GameTest(template = "empty")
    public static void movingAwayAndLogoutRemovePreviewWithoutChangingOtherOwner(GameTestHelper h) {
        var p = player(h); var q = player(h);
        try {
            VanillaAssetBench.start(p, VanillaAssetBench.Preset.SOUL, 1); VanillaAssetBench.start(q, VanillaAssetBench.Preset.REIATSU, 1);
            p.setPos(p.getX() + 40, p.getY(), p.getZ()); VanillaAssetBench.tick(p);
            h.assertTrue(VanillaAssetBench.remaining(p) == 0 && VanillaAssetBench.remaining(q) > 0, "Distance cleanup affected wrong owner");
            VanillaAssetBench.logout(new net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedOutEvent(q));
            h.assertTrue(VanillaAssetBench.remaining(q) == 0, "Logout leaked preview");
        } finally { VanillaAssetBench.stop(p); VanillaAssetBench.stop(q); }
        h.succeed();
    }
    @GameTest(template = "empty", timeoutTicks = 200)
    public static void capacityIsBoundedAndExistingOwnerCanReplaceSession(GameTestHelper h) {
        // Wait until other bench fixtures expire; the capacity limit is intentionally shared by the server.
        h.runAfterDelay(130, () -> {
            var players = new ArrayList<FakePlayer>();
            try {
                for (int i = 0; i < VanillaAssetBench.SESSION_LIMIT; i++) {
                    var p = player(h); players.add(p); h.assertTrue(VanillaAssetBench.start(p, VanillaAssetBench.Preset.BEAM, 1), "Capacity rejected valid owner");
                }
                var excess = player(h); players.add(excess);
                h.assertTrue(!VanillaAssetBench.start(excess, VanillaAssetBench.Preset.FROST, 1), "Capacity exceeded");
                h.assertTrue(VanillaAssetBench.start(players.get(0), VanillaAssetBench.Preset.CRYSTAL, 2), "Existing owner could not replace preview");
            } finally { players.forEach(VanillaAssetBench::stop); }
            h.succeed();
        });
    }
}
