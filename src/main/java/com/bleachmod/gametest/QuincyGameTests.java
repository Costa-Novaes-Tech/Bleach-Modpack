package com.bleachmod.gametest;

import com.bleachmod.Reference;
import com.bleachmod.common.data.PlayerCapability;
import com.bleachmod.common.data.PlayerData;
import com.bleachmod.common.quincy.HirenkyakuService;
import com.bleachmod.common.quincy.QuincyBowItem;
import com.bleachmod.common.quincy.QuincyTechniqueService;
import com.bleachmod.init.ModItems;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

import java.util.UUID;

@GameTestHolder("bleachmod")
@PrefixGameTestTemplate(false)
public final class QuincyGameTests {

    private static FakePlayer quincyPlayer(GameTestHelper h) {
        var p = new FakePlayer(h.getLevel(), new GameProfile(UUID.randomUUID(), "QuincyTester"));
        p.setPos(h.absoluteVec(new net.minecraft.world.phys.Vec3(2.5, 2, 2.5)));
        p.setYRot(0);
        p.setOnGround(true);
        var data = PlayerCapability.get(p).orElseThrow(() -> new IllegalStateException("Missing PlayerData capability"));
        data.initializeQuincy();
        return p;
    }

    private static PlayerData data(FakePlayer p) {
        return PlayerCapability.get(p).orElseThrow(() -> new IllegalStateException("Missing PlayerData capability"));
    }

    @GameTest(template = "empty")
    public static void quincyProfileInitializationAndBowEquip(GameTestHelper h) {
        var p = quincyPlayer(h);
        var d = data(p);
        h.assertTrue(Reference.RACE_QUINCY.equals(d.getCharacter().getRace()), "Player race should be quincy");
        h.assertTrue(d.getResources().getCurrentReiatsu() == 100.0F, "Quincy should start with 100 PE");
        p.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(ModItems.QUINCY_BOW.get()));
        h.assertTrue(p.getMainHandItem().getItem() instanceof QuincyBowItem, "Holding quincy bow");
        h.succeed();
    }

    @GameTest(template = "empty")
    public static void quincyBowConsumesReiatsuAndFiresWithoutVanillaArrows(GameTestHelper h) {
        var p = quincyPlayer(h);
        var d = data(p);
        var bowStack = new ItemStack(ModItems.QUINCY_BOW.get());
        p.setItemInHand(InteractionHand.MAIN_HAND, bowStack);

        // Ensure player inventory has no vanilla arrows
        p.getInventory().clearContent();

        // Simulate bow release with full charge (20 ticks)
        QuincyBowItem bow = (QuincyBowItem) bowStack.getItem();
        bow.releaseUsing(bowStack, h.getLevel(), p, 72000 - 20);

        h.assertTrue(d.getResources().getCurrentReiatsu() == 98.0F, "Bow shot consumed 2 PE");
        h.succeed();
    }

    @GameTest(template = "empty")
    public static void seeleSchneiderExecutionChargesPEAndSetsCooldown(GameTestHelper h) {
        var p = quincyPlayer(h);
        var d = data(p);
        QuincyTechniqueService.executeSeeleSchneider(p, d);
        h.assertTrue(d.getResources().getCurrentReiatsu() == 88.0F, "Seele Schneider consumed 12 PE");
        h.assertTrue(d.getStatus().getTechniqueSlot1CooldownTicks() == 120, "Seele Schneider 6s (120 ticks) cooldown");

        // Second call within cooldown must be blocked
        QuincyTechniqueService.executeSeeleSchneider(p, d);
        h.assertTrue(d.getResources().getCurrentReiatsu() == 88.0F, "Second shot blocked by cooldown");
        h.succeed();
    }

    @GameTest(template = "empty")
    public static void lichtRegenExecutionChargesPEAndSetsCooldown(GameTestHelper h) {
        var p = quincyPlayer(h);
        var d = data(p);
        QuincyTechniqueService.executeLichtRegen(p, d);
        h.assertTrue(d.getResources().getCurrentReiatsu() == 80.0F, "Licht Regen consumed 20 PE");
        h.assertTrue(d.getStatus().getTechniqueSlot2CooldownTicks() == 200, "Licht Regen 10s (200 ticks) cooldown");
        h.assertTrue(d.getQuincy().isLichtRegenActive(), "Licht Regen sequence is active");
        h.succeed();
    }

    @GameTest(template = "empty")
    public static void hirenkyakuChargesPEAndStaminaAndSetsCooldown(GameTestHelper h) {
        var p = quincyPlayer(h);
        var d = data(p);
        boolean moved = HirenkyakuService.execute(p, d, 1, 0);
        h.assertTrue(moved, "Hirenkyaku should execute");
        h.assertTrue(d.getResources().getCurrentReiatsu() == 92.0F, "Hirenkyaku consumed 8 PE");
        h.assertTrue(d.getStatus().getTechniqueSlot3CooldownTicks() == 120, "Hirenkyaku 6s (120 ticks) cooldown");
        h.succeed();
    }

    @GameTest(template = "empty")
    public static void sprengerLifecycleCreationAndDetonation(GameTestHelper h) {
        var p = quincyPlayer(h);
        var d = data(p);

        // Manually start sprenger on floor for test
        var center = new BlockPos(2, 2, 2);
        d.getQuincy().startSprenger(center, java.util.List.of(
                center.offset(6, 0, 0),
                center.offset(2, 0, 6),
                center.offset(-5, 0, 4),
                center.offset(-5, 0, -4),
                center.offset(2, 0, -6)
        ), h.getLevel().getGameTime());
        d.getStatus().setTechniqueSlot4CooldownTicks(QuincyTechniqueService.SPRENGER_COOLDOWN_TICKS);

        h.assertTrue(d.getQuincy().isSprengerActive(), "Sprenger active");
        h.assertTrue(d.getStatus().getTechniqueSlot4CooldownTicks() == 400, "Sprenger 20s (400 ticks) cooldown");

        // Reactivate before prep window (should be ignored)
        QuincyTechniqueService.executeSprenger(p, d);
        h.assertTrue(d.getQuincy().isSprengerActive(), "Cannot detonate during 1.5s prep window");

        // Advance past prep window (e.g. 35 ticks)
        d.getQuincy().startSprenger(center, d.getQuincy().getSprengerVertices(), h.getLevel().getGameTime() - 35);
        QuincyTechniqueService.executeSprenger(p, d);
        h.assertTrue(!d.getQuincy().isSprengerActive(), "Sprenger field dissolved after detonation");
        h.succeed();
    }
}
