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

    @GameTest(template = "empty")
    public static void quincyArrow64BlockFlightWithoutGravityAndZeroVerticalDrop(GameTestHelper h) {
        var p = quincyPlayer(h);
        var arrow = new com.bleachmod.common.quincy.QuincyArrowEntity(h.getLevel(), p);
        arrow.configureCommon(1.0F);

        h.assertTrue(arrow.isNoGravity(), "Quincy common arrow must have no gravity");
        h.assertTrue(arrow.getMaxRange() == 64.0D, "Quincy common arrow max range is 64 blocks");

        // Set initial position and ensure flight path chunks are loaded in GameTestServer
        Vec3 startPos = h.absoluteVec(new Vec3(2.5, 5.0, 2.5));
        arrow.setPos(startPos);
        BlockPos startBlock = BlockPos.containing(startPos);
        for (int chunkOffset = 0; chunkOffset <= 4; chunkOffset++) {
            h.getLevel().getChunk(startBlock.offset(chunkOffset * 16, 0, 0));
        }

        double initialY = arrow.getY();
        arrow.shoot(1.0, 0.0, 0.0, 3.0F, 0.0F);

        // Natural tick-by-tick flight without artificial setPos teleportation
        // At 3.0 blocks/tick with 0.99 air resistance:
        // Tick 6: distance ~17.556m (milestone near 16m)
        // Tick 12: distance ~34.085m (milestone near 32m)
        // Tick 23: distance ~61.916m (milestone approaching 64m)
        // Tick 24: distance ~64.297m (> 64.0m -> discarded)
        for (int tick = 1; tick <= 23; tick++) {
            arrow.tick();
            double currentY = arrow.getY();
            double yDrop = Math.abs(currentY - initialY);
            double dist = arrow.position().distanceTo(startPos);

            h.assertTrue(yDrop < 0.0001D, "Vertical drop at tick " + tick + " must be 0.000 (drop: " + yDrop + ")");
            h.assertTrue(Math.abs(arrow.getDeltaMovement().y) < 0.0001D, "Vertical delta motion must remain 0.000");
            h.assertTrue(!arrow.isRemoved(), "Arrow must survive during flight before reaching 64m (tick " + tick + ", dist " + dist + ")");

            if (tick == 6) {
                h.assertTrue(dist >= 16.0D && dist < 20.0D, "Milestone at tick 6 must pass 16m (measured: " + dist + "m)");
            } else if (tick == 12) {
                h.assertTrue(dist >= 32.0D && dist < 36.0D, "Milestone at tick 12 must pass 32m (measured: " + dist + "m)");
            } else if (tick == 23) {
                h.assertTrue(dist >= 60.0D && dist <= 64.0D, "Milestone at tick 23 must approach 64m (measured: " + dist + "m)");
            }
        }

        // Tick 24: natural trajectory crosses 64.0m threshold -> arrow must discard strictly by range
        arrow.tick();
        double finalDist = arrow.position().distanceTo(startPos);
        h.assertTrue(finalDist > 64.0D, "Distance at tick 24 must exceed 64.0m (measured: " + finalDist + "m)");
        h.assertTrue(arrow.isRemoved(), "Arrow exceeding 64 blocks must be discarded naturally");
        h.succeed();
    }

    @GameTest(template = "empty")
    public static void quincyArrowMaxLifetimeCleanup(GameTestHelper h) {
        var p = quincyPlayer(h);
        var arrow = new com.bleachmod.common.quincy.QuincyArrowEntity(h.getLevel(), p);
        arrow.configureCommon(1.0F);

        Vec3 startPos = h.absoluteVec(new Vec3(2.5, 5.0, 2.5));
        arrow.setPos(startPos);
        arrow.setDeltaMovement(Vec3.ZERO);
        h.getLevel().getChunk(arrow.blockPosition());

        // Step arrow up to tick 59: arrow must stay alive in loaded chunk without moving past range
        for (int i = 1; i <= 59; i++) {
            arrow.tick();
            h.assertTrue(!arrow.isRemoved(), "Arrow must remain alive at tick " + i + " (< 60 ticks)");
            h.assertTrue(arrow.getLifeTicks() == i, "lifeTicks must match elapsed tick " + i);
        }

        // At tick 60: reaches COMMON_MAX_LIFE_TICKS (60) and must discard strictly by lifetime expiration
        arrow.tick();
        h.assertTrue(arrow.isRemoved(), "Arrow must be discarded at tick 60 upon reaching COMMON_MAX_LIFE_TICKS");
        h.assertTrue(arrow.getLifeTicks() == 60, "lifeTicks must equal 60 at discard");
        h.assertTrue(arrow.position().distanceTo(startPos) < 1.0D, "Arrow did not exceed max range; discard was purely by lifetime");
        h.succeed();
    }

    @GameTest(template = "empty")
    public static void quincyBowEarlyReleaseConsumesNoReiatsu(GameTestHelper h) {
        var p = quincyPlayer(h);
        var d = data(p);
        var bowStack = new ItemStack(ModItems.QUINCY_BOW.get());
        p.setItemInHand(InteractionHand.MAIN_HAND, bowStack);

        // Release at 3 ticks (MIN_CHARGE_TICKS is 5)
        QuincyBowItem bow = (QuincyBowItem) bowStack.getItem();
        bow.releaseUsing(bowStack, h.getLevel(), p, 72000 - 3);

        h.assertTrue(d.getResources().getCurrentReiatsu() == 100.0F, "Early release (< 5 ticks) must NOT consume Reiatsu");
        h.succeed();
    }

    @GameTest(template = "empty")
    public static void quincyLichtRegenRetains40BlockRangeAndGravityArc(GameTestHelper h) {
        var p = quincyPlayer(h);
        var arrow = new com.bleachmod.common.quincy.QuincyArrowEntity(h.getLevel(), p);
        arrow.configureLichtRegen();

        h.assertTrue(!arrow.isNoGravity(), "Licht Regen arrow retains gravity for descending rain arc");
        h.assertTrue(arrow.getMaxRange() == 40.0D, "Licht Regen arrow max range is 40 blocks");
        h.assertTrue(arrow.getSpiritualDamage() == 2.0F, "Licht Regen arrow damage is 2.0 HP");
        h.assertTrue(arrow.isLichtRegen(), "isLichtRegen flag is true");
        h.succeed();
    }

    @GameTest(template = "empty")
    public static void quincyArrowWallCollisionAndTargetValidation(GameTestHelper h) {
        var p = quincyPlayer(h);
        var arrow = new com.bleachmod.common.quincy.QuincyArrowEntity(h.getLevel(), p);
        arrow.configureCommon(1.0F);

        // Target validation: cannot target owner
        h.assertTrue(!com.bleachmod.common.technique.TechniqueTargets.allowed(p, p), "TechniqueTargets must reject owner");

        // Target validation: hostile mob is allowed
        var zombie = net.minecraft.world.entity.EntityType.ZOMBIE.create(h.getLevel());
        h.assertTrue(zombie != null, "Zombie entity created");
        h.assertTrue(com.bleachmod.common.technique.TechniqueTargets.allowed(p, zombie), "TechniqueTargets allows hostile mob");

        // Build a physical solid wall of stone at X=6 in the GameTest level
        for (int y = 1; y <= 4; y++) {
            for (int z = 1; z <= 4; z++) {
                h.getLevel().setBlockAndUpdate(h.absolutePos(new BlockPos(6, y, z)), net.minecraft.world.level.block.Blocks.STONE.defaultBlockState());
            }
        }

        try {
            // Position zombie behind the wall at X=10
            Vec3 zombiePos = h.absoluteVec(new Vec3(10.5, 2.0, 2.5));
            zombie.setPos(zombiePos);
            h.getLevel().addFreshEntity(zombie);
            float initialZombieHealth = zombie.getHealth();

            // Position arrow at X=2.5 and shoot towards the wall along +X
            Vec3 arrowStart = h.absoluteVec(new Vec3(2.5, 2.5, 2.5));
            arrow.setPos(arrowStart);
            arrow.shoot(1.0, 0.0, 0.0, 3.0F, 0.0F);

            // Step arrow motion: continuous raycast collision must detect stone wall at X=6
            // Tick 1: moves from X=2.5 to X=5.5 (does not reach wall at X=6 yet)
            arrow.tick();
            h.assertTrue(!arrow.isRemoved(), "Arrow must still be flying before reaching wall");

            // Tick 2: raycast from X=5.5 to X=8.47 intersects stone wall at X=6.0
            arrow.tick();
            h.assertTrue(arrow.isRemoved(), "Arrow must collide with solid stone wall and be discarded immediately");
            h.assertTrue(arrow.getX() <= h.absolutePos(new BlockPos(6, 2, 2)).getX() + 1.0D, "Arrow must stop at the wall face and not pass through");

            // Prove target behind the wall took 0 damage
            h.assertTrue(zombie.getHealth() == initialZombieHealth, "Zombie behind solid wall must take 0 damage (health: " + zombie.getHealth() + ")");
        } finally {
            for (int y = 1; y <= 4; y++) {
                for (int z = 1; z <= 4; z++) {
                    h.getLevel().setBlockAndUpdate(h.absolutePos(new BlockPos(6, y, z)), net.minecraft.world.level.block.Blocks.AIR.defaultBlockState());
                }
            }
            zombie.discard();
        }
        h.succeed();
    }
}
