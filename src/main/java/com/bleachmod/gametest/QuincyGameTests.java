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
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

import java.util.UUID;

@GameTestHolder("bleachmod")
@PrefixGameTestTemplate(false)
public final class QuincyGameTests {

    private static FakePlayer quincyPlayer(GameTestHelper h) {
        var p = new FakePlayer(h.getLevel(), new GameProfile(UUID.randomUUID(), "QuincyTester"));
        p.setPos(h.absoluteVec(new Vec3(2.5, 2, 2.5)));
        p.setYRot(0);
        p.setOnGround(true);
        var data = PlayerCapability.get(p).orElseThrow(() -> new IllegalStateException("Missing PlayerData capability"));
        data.initializeQuincy();
        return p;
    }

    private static FakePlayer shinigamiPlayer(GameTestHelper h) {
        var p = new FakePlayer(h.getLevel(), new GameProfile(UUID.randomUUID(), "ShinigamiTester"));
        p.setPos(h.absoluteVec(new Vec3(2.5, 2, 2.5)));
        p.setYRot(0);
        p.setOnGround(true);
        var data = PlayerCapability.get(p).orElseThrow(() -> new IllegalStateException("Missing PlayerData capability"));
        data.getStatus().setHasCreatedCharacter(true);
        data.getCharacter().setRace("shinigami");
        data.getCharacter().bindZanpakuto(com.bleachmod.common.data.CharacterData.RYUJIN);
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
    public static void nonQuincyHoldingBowCannotUseOrAuthorizeTechniques(GameTestHelper h) {
        var p = shinigamiPlayer(h);
        var d = data(p);
        p.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(ModItems.QUINCY_BOW.get()));

        // Possessing the bow must NOT grant Quincy status or authorization
        h.assertTrue(!QuincyTechniqueService.isQuincy(d), "Shinigami holding bow is not Quincy");
        h.assertTrue(!QuincyTechniqueService.authorize(p, d, 1), "Cannot authorize Seele Schneider without Quincy race");
        h.assertTrue(!QuincyTechniqueService.authorize(p, d, 2), "Cannot authorize Licht Regen without Quincy race");
        h.assertTrue(!QuincyTechniqueService.authorize(p, d, 3), "Cannot authorize Hirenkyaku without Quincy race");
        h.assertTrue(!QuincyTechniqueService.authorize(p, d, 4), "Cannot authorize Sprenger without Quincy race");
        h.succeed();
    }

    @GameTest(template = "empty")
    public static void quincyBowRequiresBowForTechniques1And2(GameTestHelper h) {
        var p = quincyPlayer(h);
        var d = data(p);

        // Empty hands: bow-specific techniques (1 & 2) fail, foot/field techniques (3 & 4) succeed
        p.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
        h.assertTrue(!QuincyTechniqueService.authorize(p, d, 1), "Seele Schneider requires bow in main hand");
        h.assertTrue(!QuincyTechniqueService.authorize(p, d, 2), "Licht Regen requires bow in main hand");
        h.assertTrue(QuincyTechniqueService.authorize(p, d, 3), "Hirenkyaku does not require bow");
        h.assertTrue(QuincyTechniqueService.authorize(p, d, 4), "Sprenger does not require bow");

        // Equip bow: all techniques authorize
        p.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(ModItems.QUINCY_BOW.get()));
        h.assertTrue(QuincyTechniqueService.authorize(p, d, 1), "Seele Schneider authorized with bow");
        h.assertTrue(QuincyTechniqueService.authorize(p, d, 2), "Licht Regen authorized with bow");
        h.succeed();
    }

    @GameTest(template = "empty")
    public static void seeleSchneiderExecutionChargesPEAndSetsCooldown(GameTestHelper h) {
        var p = quincyPlayer(h);
        var d = data(p);
        p.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(ModItems.QUINCY_BOW.get()));
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
        p.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(ModItems.QUINCY_BOW.get()));
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
        h.assertTrue(d.getResources().getStamina() == 90.0F, "Hirenkyaku consumed 10 stamina");
        h.assertTrue(d.getStatus().getTechniqueSlot3CooldownTicks() == 120, "Hirenkyaku 6s (120 ticks) cooldown");
        h.succeed();
    }

    @GameTest(template = "empty")
    public static void hirenkyakuRetreatsWithoutInputAndBlocksUnderBusyStates(GameTestHelper h) {
        var p = quincyPlayer(h);
        var d = data(p);

        // Neutral input (0, 0): retreats backward
        boolean moved = HirenkyakuService.execute(p, d, 0, 0);
        h.assertTrue(moved, "Neutral input executes backward retreat");

        // Clear cooldown for test
        d.getStatus().setTechniqueSlot3CooldownTicks(0);

        // Action block: dodging
        d.getStatus().beginDodge(new Vec3(1, 0, 0), h.getLevel().getGameTime());
        h.assertTrue(!HirenkyakuService.execute(p, d, 1, 0), "Hirenkyaku blocked while dodging");

        // Action block: low stamina
        d.getStatus().stopDodge();
        d.getResources().spendAllStamina(); // sets stamina to 0 (< 10)
        h.assertTrue(!HirenkyakuService.execute(p, d, 1, 0), "Hirenkyaku blocked when stamina < 10");
        h.succeed();
    }

    @GameTest(template = "empty")
    public static void hirenkyakuConsumesStaminaEvenWithReiatsuCostsDisabledCheat(GameTestHelper h) {
        var p = quincyPlayer(h);
        var d = data(p);

        // Enable Reiatsu cheat
        d.getResources().setCostsDisabled(true);
        float startPE = d.getResources().getCurrentReiatsu();
        float startStamina = d.getResources().getStamina();

        boolean moved = HirenkyakuService.execute(p, d, 1, 0);
        h.assertTrue(moved, "Hirenkyaku executed");
        h.assertTrue(d.getResources().getCurrentReiatsu() == startPE, "Reiatsu cheat prevented PE consumption");
        h.assertTrue(d.getResources().getStamina() == startStamina - 10.0F, "Stamina is strictly consumed even under Reiatsu cheat");
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

    @GameTest(template = "empty")
    public static void sprengerClearTransientStateCleansField(GameTestHelper h) {
        var p = quincyPlayer(h);
        var d = data(p);

        var center = new BlockPos(2, 2, 2);
        d.getQuincy().startSprenger(center, java.util.List.of(
                center.offset(6, 0, 0),
                center.offset(2, 0, 6),
                center.offset(-5, 0, 4),
                center.offset(-5, 0, -4),
                center.offset(2, 0, -6)
        ), h.getLevel().getGameTime());

        h.assertTrue(d.getQuincy().isSprengerActive(), "Sprenger active");
        QuincyTechniqueService.clearQuincyTransientState(p, d);
        h.assertTrue(!d.getQuincy().isSprengerActive(), "Sprenger cleared by clearQuincyTransientState");
        h.succeed();
    }
}
