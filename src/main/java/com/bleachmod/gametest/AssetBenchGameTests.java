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
    public static void migratedCutsAndFieldsRetainFullDensityWithoutGameplayMutation(GameTestHelper h) {
        var p = player(h); var forward = new net.minecraft.world.phys.Vec3(0, 0, 1);
        var data = PlayerCapability.get(p).orElseThrow(() -> new IllegalStateException("Missing capability")); data.initializeShinigami();
        var before = data.save();
        // Load the fixture explicitly, never through the production emitter.
        for (int x = -32; x <= 32; x += 8) for (int z = -32; z <= 32; z += 8)
            h.getLevel().getChunkAt(BlockPos.containing(p.position().add(x, 0, z)));
        for (boolean released : new boolean[]{false, true}) for (boolean bankai : new boolean[]{false, true}) {
            if (bankai && !released) continue;
            for (int slot = 1; slot <= 2; slot++) {
                var cut = new com.bleachmod.common.visual.IceTechniqueVisuals.Cut(slot, released, bankai);
                h.assertTrue(com.bleachmod.common.visual.IceTechniqueVisuals.cut(p, cut, forward) == cut.budget(), "Cut clipped particles");
            }
        }
        for (boolean barrier : new boolean[]{false, true}) for (boolean bankai : new boolean[]{false, true}) {
            int expected = barrier ? (bankai ? 224 : 128) : (bankai ? 896 : 256);
            h.assertTrue(com.bleachmod.common.visual.IceTechniqueVisuals.field(p, p.position(), forward, barrier, bankai, bankai ? 30 : 10, 100) == expected, "Field clipped particles");
        }
        h.assertTrue(com.bleachmod.common.visual.IceTechniqueVisuals.duel(p, p.position().add(0, 0, 3)) == 45, "Duel burst changed");
        h.assertTrue(data.save().equals(before), "Visual profiles changed character resources or progression"); h.succeed();
    }
    @GameTest(template = "empty")
    public static void techniqueAnchorPreservesFixedFieldsAndRejectsOverflow(GameTestHelper h) {
        var p = player(h); var anchor = p.position(); var point = anchor.add(0, 1, 0);
        h.getLevel().getChunkAt(BlockPos.containing(point));
        p.setPos(p.getX() + 40, p.getY(), p.getZ());
        var frame = com.bleachmod.common.visual.VisualFrame.technique(p, anchor, 896);
        var snow = net.minecraft.core.particles.ParticleTypes.SNOWFLAKE;
        h.assertTrue(!frame.emit(snow, point, 897, .6, 2, .6, .04) && frame.requested() == 0, "Technique overflow accepted");
        h.assertTrue(frame.emit(snow, point, 896, .6, 2, .6, .04), "Owner movement clipped fixed field");
        h.assertTrue(!frame.emit(snow, point, .1, .01), "Additional layer bypassed technique budget");
        h.assertTrue(!new com.bleachmod.common.visual.VisualFrame(p, h.getLevel()).emit(snow, p.position(), 1, .1, 2, .1, .01), "Technique spread leaked into private bench contract");
        var moving = com.bleachmod.common.visual.VisualFrame.technique(p, anchor, 10);
        h.assertTrue(!moving.emit(snow, anchor.add(33, 0, 0), .1, .01), "Distant point bypassed anchor range");
        h.assertTrue(!moving.emit(snow, point, 1, .1, 6.1, .1, .01), "Excessive spread accepted"); h.succeed();
    }
    @GameTest(template = "empty")
    public static void dragonTrailKeepsItsOwnPositionAndStopsWithInvalidCaster(GameTestHelper h) {
        var p = player(h); var dragonPosition = p.position().add(0, 1.2, 0);
        h.getLevel().getChunkAt(BlockPos.containing(dragonPosition));
        p.setPos(p.getX() + 40, p.getY(), p.getZ());
        h.assertTrue(com.bleachmod.common.visual.IceTechniqueVisuals.dragon(p, dragonPosition) == 10, "Moving caster clipped dragon trail");
        p.setHealth(0);
        h.assertTrue(com.bleachmod.common.visual.IceTechniqueVisuals.dragon(p, dragonPosition) == 0, "Dead caster left trail active"); h.succeed();
    }
    @GameTest(template = "empty")
    public static void productionAudienceAndBatchBudgetAreBounded(GameTestHelper h) {
        var p = player(h); var q = player(h); q.setPos(p.getX() + 2, p.getY(), p.getZ());
        var position = p.position().add(0, 1, 0);
        var frame = com.bleachmod.common.visual.VisualFrame.nearby(p, 10);
        h.assertTrue(frame.includes(p, position) && frame.includes(q, position), "Nearby audience excluded players");
        h.assertTrue(!new com.bleachmod.common.visual.VisualFrame(p, h.getLevel()).includes(q, position), "Private bench leaked to another player");
        q.setPos(p.getX() + 33, p.getY(), p.getZ());
        h.assertTrue(!frame.includes(q, position), "Distant recipient accepted");
        var snow = net.minecraft.core.particles.ParticleTypes.SNOWFLAKE;
        h.assertTrue(!frame.emit(snow, position, 0, .2, .8, .2, .08), "Invalid batch accepted");
        h.assertTrue(!frame.emit(snow, position, 4, .2, Double.NaN, .2, .08), "Invalid axis accepted");
        h.assertTrue(frame.emit(snow, position, 8, .2, .8, .2, .08), "Valid batch rejected");
        h.assertTrue(!frame.emit(snow, position, 4, .2, .8, .2, .08) && frame.requested() == 8, "Overflow batch partially emitted");
        h.assertTrue(frame.emit(snow, position, 2, .2, .8, .2, .08) && frame.requested() == 10, "Remaining budget unavailable");
        // FakePlayer ignores normal damage/kill; set the fixture's health directly.
        p.setHealth(0);
        h.assertTrue(!com.bleachmod.common.visual.VisualFrame.nearby(p, 10).emit(snow, position, .2, .08), "Dead source emitted particles");
        h.succeed();
    }
    @GameTest(template = "empty")
    public static void approvedIceManifestationsKeepDensityAndDoNotMutateGameplay(GameTestHelper h) {
        var p = player(h); var data = PlayerCapability.get(p).orElseThrow(() -> new IllegalStateException("Missing capability")); data.initializeShinigami();
        for (int i = 0; i < 40; i++) h.getLevel().getChunkAt(BlockPos.containing(com.bleachmod.common.visual.IceManifestation.releasePoint(p.position(), true, i)));
        var before = data.save(); var pos = BlockPos.containing(p.position()); var block = h.getLevel().getBlockState(pos);
        h.assertTrue(com.bleachmod.common.visual.IceManifestation.release(p, false) == 96, "Shikai density changed");
        h.assertTrue(com.bleachmod.common.visual.IceManifestation.release(p, true) == 160, "Bankai density changed");
        h.assertTrue(com.bleachmod.common.visual.IceManifestation.aura(p, false) == 4, "Shikai aura changed");
        h.assertTrue(com.bleachmod.common.visual.IceManifestation.aura(p, true) == 10, "Bankai aura changed");
        h.assertTrue(before.equals(data.save()) && block.equals(h.getLevel().getBlockState(pos)), "Visual emission mutated gameplay");
        var other = new FakePlayer(p.getServer().getLevel(net.minecraft.world.level.Level.NETHER), new GameProfile(UUID.randomUUID(), "OtherWorld"));
        other.setPos(p.getX(), p.getY(), p.getZ());
        h.assertTrue(!com.bleachmod.common.visual.VisualFrame.nearby(p, 10).includes(other, p.position()), "Cross-dimension recipient accepted");
        h.assertTrue(!new com.bleachmod.common.visual.VisualFrame(other, h.getLevel()).emit(net.minecraft.core.particles.ParticleTypes.SNOWFLAKE, p.position(), .2, .08), "Cross-dimension source emitted");
        h.succeed();
    }
    @GameTest(template = "empty")
    public static void compositionSharesTheBudgetAndRejectsInvalidChanges(GameTestHelper h) {
        var p = player(h); var data = PlayerCapability.get(p).orElseThrow(() -> new IllegalStateException("Missing capability")); data.initializeShinigami();
        var before = data.save(); var dispatcher = p.getServer().getCommands().getDispatcher();
        try {
            for (String palette : com.bleachmod.common.visual.EffectLibrary.palettes()) for (String shape : com.bleachmod.common.visual.EffectLibrary.shapes()) {
                h.assertTrue(dispatcher.execute("bleachdev assets compose " + palette + " " + shape + " 3", p.createCommandSourceStack().withPermission(2)) == 1, "Composition failed");
                h.assertTrue(VanillaAssetBench.emitted(p) == 48 && VanillaAssetBench.active(p).equals(palette + "/" + shape), "Composed frame exceeded budget or wrong profile");
                VanillaAssetBench.tick(p); h.assertTrue(VanillaAssetBench.emitted(p) == 48, "Composition duplicated frame");
            }
            String active = VanillaAssetBench.active(p);
            h.assertTrue(dispatcher.execute("bleachdev assets compose missing aura", p.createCommandSourceStack().withPermission(2)) == 0 && VanillaAssetBench.active(p).equals(active), "Invalid palette changed preview");
            boolean rejected = false;
            try { dispatcher.execute("bleachdev assets compose ice aura", p.createCommandSourceStack().withPermission(0)); }
            catch (com.mojang.brigadier.exceptions.CommandSyntaxException e) { rejected = true; }
            h.assertTrue(rejected && data.save().equals(before), "Non-operator composition or gameplay mutation");
        } catch (com.mojang.brigadier.exceptions.CommandSyntaxException e) { throw new IllegalStateException(e); }
        finally { VanillaAssetBench.stop(p); }
        h.succeed();
    }
    @GameTest(template = "empty")
    public static void sharedFrameRejectsOverflowAndInvalidPositions(GameTestHelper h) {
        var p = player(h); var frame = new com.bleachmod.common.visual.VisualFrame(p, h.getLevel());
        var position = p.position().add(0, 1, 4);
        h.assertTrue(!frame.emit(net.minecraft.core.particles.ParticleTypes.FLAME, new net.minecraft.world.phys.Vec3(Double.NaN, 0, 0), .03, .01), "Non-finite position accepted");
        h.assertTrue(!frame.emit(net.minecraft.core.particles.ParticleTypes.FLAME, p.position().add(100, 0, 0), .03, .01), "Distant emission accepted");
        h.assertTrue(!frame.emit(net.minecraft.core.particles.ParticleTypes.FLAME, position, .03, Double.NaN), "Non-finite speed accepted");
        for (int i = 0; i < 48; i++) h.assertTrue(frame.emit(net.minecraft.core.particles.ParticleTypes.FLAME, position, .03, .01), "Valid frame rejected");
        h.assertTrue(!frame.emit(net.minecraft.core.particles.ParticleTypes.SMOKE, position, .03, .01) && frame.requested() == 48, "Additional layer bypassed shared budget"); h.succeed();
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
