package com.bleachmod.gametest;

import com.bleachmod.common.data.PlayerCapability;
import com.bleachmod.common.data.PlayerData;
import com.bleachmod.common.technique.RyujinTechniqueService;
import com.bleachmod.common.technique.TechniqueService;
import com.bleachmod.init.ModItems;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;
import java.util.UUID;

@GameTestHolder("bleachmod")
@PrefixGameTestTemplate(false)
public final class RyujinGameTests {
    @GameTest(template = "empty")
    public static void spiritFlameSurvivesSaveAndPreservesOverlapAndReplacement(GameTestHelper helper) {
        var level = helper.getLevel();
        BlockPos pos = helper.absolutePos(new BlockPos(2, 2, 2));
        level.setBlock(pos.below(), net.minecraft.world.level.block.Blocks.STONE.defaultBlockState(), 3);
        level.removeBlock(pos, false);
        var service = new com.bleachmod.common.technique.SpiritFlameService();
        UUID first = UUID.randomUUID(), second = UUID.randomUUID();
        helper.assertTrue(service.place(level, pos, first) && service.place(level, pos, second), "Owners cannot overlap");
        var tag = service.save(new net.minecraft.nbt.CompoundTag());
        var owners = tag.getList("flames", 10).getCompound(0).getList("owners", 10);
        owners.getCompound(0).putLong("expires", level.getGameTime() - 1);
        var reloaded = com.bleachmod.common.technique.SpiritFlameService.load(tag);
        reloaded.expire(level);
        helper.assertTrue(level.getBlockState(pos).is(com.bleachmod.init.ModBlocks.SPIRIT_FLAME.get()), "One expired owner erased another's flame");
        var remaining = reloaded.save(new net.minecraft.nbt.CompoundTag());
        remaining.getList("flames", 10).getCompound(0).getList("owners", 10).getCompound(0).putLong("expires", level.getGameTime() - 1);
        com.bleachmod.common.technique.SpiritFlameService.load(remaining).expire(level);
        helper.assertTrue(level.getBlockState(pos).isAir(), "Persisted expired flame remains");
        helper.assertTrue(service.place(level, pos, first), "Could not replace expired flame");
        level.setBlock(pos, net.minecraft.world.level.block.Blocks.GOLD_BLOCK.defaultBlockState(), 3);
        service.expire(level);
        helper.assertTrue(level.getBlockState(pos).is(net.minecraft.world.level.block.Blocks.GOLD_BLOCK), "Cleanup erased a subsequent block");
        helper.assertTrue(!service.place(level, pos, second), "Flame overwrote solid terrain");
        level.removeBlock(pos, false);
        helper.succeed();
    }

    @GameTest(template = "empty", timeoutTicks = 160)
    public static void spiritFlameExpiresInWorld(GameTestHelper helper) {
        var level = helper.getLevel();
        BlockPos pos = helper.absolutePos(new BlockPos(2, 2, 2));
        level.setBlock(pos.below(), net.minecraft.world.level.block.Blocks.STONE.defaultBlockState(), 3);
        level.removeBlock(pos, false);
        helper.assertTrue(com.bleachmod.common.technique.SpiritFlameService.get(level).place(level, pos, UUID.randomUUID()), "World flame not placed");
        helper.runAfterDelay(125, () -> {
            helper.assertTrue(level.getBlockState(pos).isAir(), "World tick did not remove expired flame");
            helper.succeed();
        });
    }



    private static void command(FakePlayer player, String command) {
        try {
            int result = player.getServer().getCommands().getDispatcher().execute(
                    "bleachdev " + command + " @s", player.createCommandSourceStack().withPermission(2));
            if (result != 1) throw new IllegalStateException("Command failed: " + command);
        } catch (com.mojang.brigadier.exceptions.CommandSyntaxException exception) {
            throw new IllegalStateException(exception);
        }
    }



    @GameTest(template = "empty")
    public static void operatorCommandsRequirePermission(GameTestHelper helper) {
        FakePlayer player = player(helper);
        for (String command : java.util.List.of("cooldowns disable", "cooldowns restore", "cooldowns clear",
                "reiatsu free", "reiatsu restore")) {
            boolean rejected = false;
            try {
                player.getServer().getCommands().getDispatcher().execute("bleachdev " + command + " @s",
                        player.createCommandSourceStack().withPermission(0));
            } catch (com.mojang.brigadier.exceptions.CommandSyntaxException expected) { rejected = true; }
            helper.assertTrue(rejected, "Non-operator command allowed: " + command);
        }
        helper.succeed();
    }

    @GameTest(template = "empty")
    public static void freeReiatsuMaintainsTransformationsAtZeroBalance(GameTestHelper helper) {
        FakePlayer player = player(helper);
        PlayerData data = data(player);
        data.getSkills().get("zanpakuto").setLevel(2);
        data.getCharacter().unlockForm("shikai");
        data.getCharacter().unlockForm("bankai");
        data.getCharacter().setMastery("zanpakuto", "shikai", 100);
        data.getCharacter().setMastery("zanpakuto", "bankai", 100);
        data.getResources().setCurrentReiatsu(0);
        command(player, "reiatsu free");
        helper.onEachTick(() -> com.bleachmod.common.technique.ReleaseAnimationService.tick(player));
        data.getCharacter().setSelectedForm("zanpakuto", "shikai");
        com.bleachmod.server.events.FormModeHandler.attemptTransform(player,data);
        helper.runAfterDelay(25,()->{
            helper.assertTrue("shikai".equals(data.getCharacter().getActiveForm()),"Free animated Shikai failed");
            data.getCharacter().setSelectedForm("zanpakuto", "bankai");
            com.bleachmod.server.events.FormModeHandler.attemptTransform(player,data);
        });
        helper.runAfterDelay(90,()->{try{
            helper.assertTrue("bankai".equals(data.getCharacter().getActiveForm()),"Free animated Bankai failed");
            com.bleachmod.server.events.TickHandler.onPlayerTick(new net.minecraftforge.event.TickEvent.PlayerTickEvent(
                    net.minecraftforge.event.TickEvent.Phase.END, player));
            helper.assertTrue("bankai".equals(data.getCharacter().getActiveForm()) && data.getResources().getCurrentReiatsu()==0,"Free form/balance changed");
            command(player,"reiatsu restore");
            com.bleachmod.server.events.TickHandler.onPlayerTick(new net.minecraftforge.event.TickEvent.PlayerTickEvent(
                    net.minecraftforge.event.TickEvent.Phase.END, player));
            helper.assertTrue("sealed".equals(data.getCharacter().getActiveForm()),"Normal drain did not revert empty Bankai");
        }finally{com.bleachmod.common.technique.ReleaseAnimationService.cancel(player);}helper.succeed();});
    }

    private static FakePlayer player(GameTestHelper helper) {
        FakePlayer player = new FakePlayer(helper.getLevel(), new GameProfile(UUID.randomUUID(), "RyujinTest"));
        data(player).initializeShinigami(); data(player).getCharacter().bindZanpakuto("ryujin_jakka"); data(player).getCharacter().preserveLegacyTechniques();
        data(player).getSkills().get("zanpakuto").setLevel(2);
        data(player).getCharacter().unlockForm("shikai"); data(player).getCharacter().unlockForm("bankai");
        data(player).getCharacter().setMastery("zanpakuto", "shikai", 25);
        player.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.RYUJIN_JAKKA.get()));
        BlockPos pos = helper.absolutePos(new BlockPos(1, 3, 1));
        player.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
        player.setYRot(0);
        return player;
    }
    private static PlayerData data(FakePlayer player) {
        return PlayerCapability.get(player).orElseThrow(() -> new IllegalStateException("Capability missing"));
    }
    private static Zombie target(GameTestHelper helper, Vec3 position) {
        Zombie target = EntityType.ZOMBIE.create(helper.getLevel());
        target.setNoAi(true);
        target.setNoGravity(true);
        target.setItemSlot(EquipmentSlot.HEAD, new ItemStack(net.minecraft.world.item.Items.IRON_HELMET));
        target.setPos(position);
        helper.getLevel().addFreshEntity(target);
        return target;
    }





    @GameTest(template = "empty")
    public static void armyUsesBankaiCostAndSharedSlot(GameTestHelper helper) {
        FakePlayer player = player(helper);
        PlayerData data = data(player);
        data.getCharacter().setActiveForm("zanpakuto", "bankai");
        try {
            TechniqueService.executeSlot(player, data, 3);
            helper.assertTrue(data.getResources().getCurrentReiatsu() == 60, "Wrong tornado debit");
            helper.assertTrue(data.getStatus().getTechniqueSlot3CooldownTicks() == 300, "Wrong tornado cooldown");
            data.getCharacter().setActiveForm("zanpakuto", "shikai");
            data.getStatus().clearTransformationState();
            TechniqueService.executeSlot(player, data, 3);
            helper.assertTrue(data.getResources().getCurrentReiatsu() == 60, "Changing form bypassed slot cooldown");
        } finally { RyujinTechniqueService.cancel(player); }
        helper.succeed();
    }
    @GameTest(template = "empty")
    public static void tornadoSurroundsPlayerWithThreeBlockClearCenter(GameTestHelper helper) {
        var p = player(helper); data(p).getCharacter().setActiveForm("zanpakuto", "shikai");
        // Other fixtures cast long-range skills in open air at maxHeight-64; isolate this negative control vertically.
        p.setPos(p.getX(), helper.getLevel().getMaxBuildHeight() - 24, p.getZ());
        helper.getLevel().getChunkAt(BlockPos.containing(p.position().add(0, 0, 8)));
        helper.getLevel().getChunkAt(BlockPos.containing(p.position().add(0, 0, -4)));
        var forward = target(helper, p.position().add(0, 0, 8));
        var behind = target(helper, p.position().add(0, 0, -4));
        var inner = target(helper, p.position().add(0, 0, 1));
        var outside = target(helper, p.position().add(0, 0, -10));
        helper.runAfterDelay(5, () -> { try {
            TechniqueService.executeSlot(p, data(p), 3); RyujinTechniqueService.tickEffects(p);
            helper.assertTrue(forward.getHealth() < 20, "Tornado missed forward annulus target");
            helper.assertTrue(behind.getHealth() < 20, "Tornado must also hit behind player");
            helper.assertTrue(inner.getHealth() == 20 && outside.getHealth() == 20, "Tornado hit clear center or exceeded outer radius");
            p.setPos(p.getX(), p.getY(), p.getZ() + 5); RyujinTechniqueService.tickEffects(p);
            helper.assertTrue(inner.getHealth() < 20, "Tornado did not follow player movement");
        } finally { RyujinTechniqueService.cancel(p); forward.discard(); behind.discard(); inner.discard(); outside.discard(); } helper.succeed(); });
    }



    @GameTest(template = "empty")
    public static void steamCutCooldownSurvivesFormSwitch(GameTestHelper helper) {
        FakePlayer player = player(helper);
        PlayerData data = data(player);
        data.getCharacter().setActiveForm("zanpakuto", "bankai");
        data.getStatus().setTechniqueSlot4CooldownTicks(TechniqueService.STEAM_CUT_COOLDOWN_TICKS);
        data.getStatus().clearTransformationState();
        TechniqueService.executeSlot(player, data, 4);
        helper.assertTrue(data.getResources().getCurrentReiatsu() == 100, "Cooldown charged steam cut");
        helper.assertTrue(data.getStatus().getTechniqueSlot4CooldownTicks() == 1200, "Minute cooldown lost");
        helper.succeed();
    }




}
