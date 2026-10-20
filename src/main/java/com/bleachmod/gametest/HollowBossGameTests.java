package com.bleachmod.gametest;

import com.bleachmod.entity.HollowBossEntity;
import com.bleachmod.entity.HollowBossEntity.Attack;
import com.bleachmod.registry.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;
import net.minecraft.gametest.framework.AfterBatch;
import net.minecraft.server.level.ServerLevel;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/** Contact clocks and spatial damage run against real server entities and collision. */
@GameTestHolder("bleachmod") @PrefixGameTestTemplate(false)
public final class HollowBossGameTests {
    private static final Map<ServerLevel, Fixture> ACTIVE = new IdentityHashMap<>();
    private record Fixture(HollowBossEntity boss, IronGolem target, AABB area, Map<BlockPos, BlockState> originals) {
        void clear() {
            var level = (ServerLevel)boss.level();
            level.getEntitiesOfClass(net.minecraft.world.entity.Mob.class, area,
                    mob -> mob.getTags().contains("boss_combat_fixture")).forEach(Entity::discard);
            // This rig extends far above the template's automatic cleanup bounds.
            // Restore every modified cell, including anticipation/occlusion walls,
            // so later batches see precisely the terrain that preceded this fixture.
            originals.forEach(level::setBlockAndUpdate);
            originals.clear();
            ACTIVE.remove(level, this);
        }
    }

    private static void cleanup(ServerLevel level) {
        var fixture = ACTIVE.get(level);
        if (fixture != null) fixture.clear();
    }
    @AfterBatch(batch="boss_melee_contact") public static void cleanupMelee(ServerLevel level) { cleanup(level); }
    @AfterBatch(batch="boss_cooldown_save") public static void cleanupSave(ServerLevel level) { cleanup(level); }
    @AfterBatch(batch="boss_charge_contact") public static void cleanupCharge(ServerLevel level) { cleanup(level); }
    @AfterBatch(batch="boss_charge_last_segment") public static void cleanupLastSegment(ServerLevel level) { cleanup(level); }
    @AfterBatch(batch="boss_charge_wall") public static void cleanupWall(ServerLevel level) { cleanup(level); }
    @AfterBatch(batch="boss_slam_falloff") public static void cleanupFalloff(ServerLevel level) { cleanup(level); }
    @AfterBatch(batch="boss_cancel_no_ai") public static void cleanupNoAi(ServerLevel level) { cleanup(level); }
    @AfterBatch(batch="boss_cancel_target_loss") public static void cleanupLoss(ServerLevel level) { cleanup(level); }
    @AfterBatch(batch="boss_slam_occlusion") public static void cleanupOcclusion(ServerLevel level) { cleanup(level); }
    @AfterBatch(batch="boss_cancel_death") public static void cleanupDeath(ServerLevel level) { cleanup(level); }
    @AfterBatch(batch="boss_natural_charge") public static void cleanupNaturalCharge(ServerLevel level) { cleanup(level); }
    @AfterBatch(batch="boss_natural_melee") public static void cleanupNaturalMelee(ServerLevel level) { cleanup(level); }
    @AfterBatch(batch="boss_exact_charge_cooldown") public static void cleanupExactCooldown(ServerLevel level) { cleanup(level); }
    @AfterBatch(batch="boss_cancel_changed_target") public static void cleanupSwap(ServerLevel level) { cleanup(level); }
    @AfterBatch(batch="boss_cancel_cleared_target") public static void cleanupNull(ServerLevel level) { cleanup(level); }

    private static IronGolem target(GameTestHelper h, Vec3 position) {
        var target = EntityType.IRON_GOLEM.create(h.getLevel());
        target.addTag("boss_combat_fixture");
        target.setNoAi(true);
        target.setNoGravity(true);
        target.getAttribute(Attributes.MAX_HEALTH).setBaseValue(500);
        target.getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(1);
        target.setHealth(500);
        target.setPos(position);
        h.getLevel().addFreshEntity(target);
        return target;
    }

    private static Fixture fixture(GameTestHelper h, double distance) {
        return fixture(h, distance, false);
    }

    private static Fixture fixture(GameTestHelper h, double distance, boolean naturalAi) {
        var p = h.absolutePos(new BlockPos(1, 1, 1));
        int y = h.getLevel().getMaxBuildHeight() - 40;
        var area = new AABB(p.getX() - 12, y - 2, p.getZ() - 12, p.getX() + 18, y + 12, p.getZ() + 24);
        h.getLevel().getEntitiesOfClass(net.minecraft.world.entity.Mob.class, area,
                mob -> mob.getTags().contains("boss_combat_fixture")).forEach(Entity::discard);
        var originals = new LinkedHashMap<BlockPos, BlockState>();
        for (int x = -12; x <= 18; x++) for (int z = -12; z <= 24; z++) {
            var floor = new BlockPos(p.getX() + x, y - 1, p.getZ() + z);
            originals.put(floor, h.getLevel().getBlockState(floor));
            h.getLevel().setBlockAndUpdate(floor, Blocks.STONE.defaultBlockState());
            for (int dy = 0; dy < 8; dy++) {
                var cell = new BlockPos(p.getX() + x, y + dy, p.getZ() + z);
                originals.put(cell, h.getLevel().getBlockState(cell));
                h.getLevel().setBlockAndUpdate(cell, Blocks.AIR.defaultBlockState());
            }
        }
        var boss = ModEntities.HOLLOW_BOSS.get().create(h.getLevel());
        boss.addTag("boss_combat_fixture");
        boss.setPersistenceRequired();
        if (!naturalAi) {
            boss.goalSelector.removeAllGoals(goal -> true);
            boss.targetSelector.removeAllGoals(goal -> true);
        }
        boss.setPos(p.getX() + .5, y, p.getZ() + .5);
        boss.setYRot(0); boss.setYBodyRot(0); boss.setYHeadRot(0);
        boss.setOnGround(true);
        var target = target(h, boss.position().add(0, 0, distance));
        boss.setTarget(target);
        h.getLevel().addFreshEntity(boss);
        var fixture = new Fixture(boss, target, area, originals);
        ACTIVE.put(h.getLevel(), fixture);
        return fixture;
    }

    @GameTest(template="empty", batch="boss_profile")
    public static void bossIsThreePlayersTallAndKeepsItsRegistryAndStats(GameTestHelper h) {
        var boss = ModEntities.HOLLOW_BOSS.get().create(h.getLevel());
        h.assertTrue(Math.abs(boss.getBbHeight() - 5.4) < .001 && Math.abs(boss.getBbWidth() - 2.8) < .001,
                "Boss physical dimensions do not match its authored 3x player model");
        h.assertTrue(!boss.usesVariants() && !boss.isFlyingVariant()
                && boss.getMaxHealth() == 100 && boss.getHealth() == 100
                && boss.getAttributeValue(Attributes.ATTACK_DAMAGE) == 5, "Boss identity/stats changed unexpectedly");
        h.assertTrue(HollowBossEntity.CHARGE_COOLDOWN == 140 && HollowBossEntity.MELEE_COOLDOWN > 20,
                "Charge must use 7s and basic strikes must be slower than vanilla's 20 ticks");
        h.succeed();
    }

    @GameTest(template="empty", batch="boss_melee_contact", timeoutTicks=55)
    public static void basicStrikeAnticipatesThenDamagesOnce(GameTestHelper h) {
        var f = fixture(h, 2.7);
        h.assertTrue(f.boss.beginAttack(Attack.MELEE, f.target), "Could not begin basic strike");
        h.runAfterDelay(6, () -> h.assertTrue(f.target.getHealth() == 500, "Melee damaged before contact"));
        h.runAfterDelay(32, () -> {
            h.assertTrue(Math.abs(f.target.getHealth() - 495) < .001, "Basic strike missing or dealt duplicate damage: " + f.target.getHealth());
            f.clear(); h.succeed();
        });
    }

    @GameTest(template="empty", batch="boss_cooldown_save")
    public static void cancelAndReloadCannotBypassSevenSecondChargeCooldown(GameTestHelper h) {
        var f = fixture(h, 8);
        h.assertTrue(f.boss.beginAttack(Attack.CHARGE, f.target), "Could not begin charge");
        f.boss.cancelBossAttack();
        h.assertTrue(f.boss.getChargeCooldownTicks() == 140 && !f.boss.beginAttack(Attack.CHARGE, f.target),
                "Cancel bypassed seven-second cooldown");
        var tag = new CompoundTag();
        f.boss.addAdditionalSaveData(tag);
        var restored = ModEntities.HOLLOW_BOSS.get().create(h.getLevel());
        restored.readAdditionalSaveData(tag);
        h.assertTrue(restored.getChargeCooldownTicks() == 140 && restored.getAttack() == Attack.IDLE,
                "Reload lost cooldown or resurrected stale charge");
        f.clear(); h.succeed();
    }

    @GameTest(template="empty", batch="boss_charge_contact", timeoutTicks=80)
    public static void chargeMovesAndOnlyHitsItsVictimOnce(GameTestHelper h) {
        var f = fixture(h, 7);
        double startZ = f.boss.getZ();
        h.assertTrue(f.boss.beginAttack(Attack.CHARGE, f.target), "Could not begin charge");
        h.runAfterDelay(8, () -> h.assertTrue(f.target.getHealth() == 500, "Charge hit during anticipation"));
        h.runAfterDelay(55, () -> {
            h.assertTrue(f.boss.getZ() > startZ + 1, "Charge never advanced");
            h.assertTrue(Math.abs(f.target.getHealth() - 490) < .001, "Charge missing or hit repeatedly: " + f.target.getHealth());
            h.assertTrue(f.boss.getChargeCooldownTicks() > 0 && !f.boss.beginAttack(Attack.CHARGE, f.target),
                    "Charge restarted before seven-second cooldown");
            f.clear(); h.succeed();
        });
    }

    @GameTest(template="empty", batch="boss_charge_last_segment", timeoutTicks=80)
    public static void chargeSweepsItsLastTravelledSegmentWithoutTakingAnExtraStep(GameTestHelper h) {
        var f = fixture(h, 7);
        double startZ = f.boss.getZ();
        // Eighteen .85-block steps travel 15.3 blocks. The boss's inflated
        // leading face only reaches this second golem during step eighteen:
        // step seventeen reaches 14.45 + 1.4 + .15 = 16; its near face is 16.4.
        var endpoint = target(h, f.boss.position().add(0, 0, 17.1));
        h.assertTrue(f.boss.beginAttack(Attack.CHARGE, f.target), "Could not begin endpoint charge");
        boolean[] sawLastTravel = {false}, sawFinalSweep = {false};
        h.onEachTick(() -> {
            int tick = f.boss.getAttackTick();
            if (f.boss.getAttack() != Attack.CHARGE) return;
            if (tick == HollowBossEntity.CHARGE_WINDUP + 18) {
                sawLastTravel[0] = true;
                h.assertTrue(endpoint.getHealth() == 500, "Endpoint victim was hit before the final travelled segment was swept");
            }
            if (tick == HollowBossEntity.CHARGE_WINDUP + 19) {
                sawFinalSweep[0] = true;
                h.assertTrue(endpoint.getHealth() == 490, "Last travelled segment did not deal exactly ten damage");
            }
        });
        h.runAfterDelay(55, () -> {
            h.assertTrue(sawLastTravel[0] && sawFinalSweep[0], "Fixture did not observe both final travel and recovery sweep");
            h.assertTrue(f.target.getHealth() == 490 && endpoint.getHealth() == 490,
                    "Charge missed or hit either victim repeatedly: " + f.target.getHealth() + "/" + endpoint.getHealth());
            h.assertTrue(f.boss.getZ() - startZ <= 15.35, "Final damage sweep added an extra charge movement step");
            f.clear(); h.succeed();
        });
    }

    @GameTest(template="empty", batch="boss_charge_wall", timeoutTicks=80)
    public static void chargeStopsAtSolidWallWithoutDamageBehindIt(GameTestHelper h) {
        var f = fixture(h, 10);
        h.assertTrue(f.boss.beginAttack(Attack.CHARGE, f.target), "Could not begin unobstructed windup");
        int wallZ = (int)Math.floor(f.boss.getZ()) + 5;
        for (int x = -5; x <= 5; x++) for (int y = 0; y < 7; y++) h.getLevel().setBlockAndUpdate(
                BlockPos.containing(f.boss.getX() + x, f.boss.getY() + y, wallZ), Blocks.STONE.defaultBlockState());
        h.runAfterDelay(55, () -> {
            h.assertTrue(f.boss.getZ() + f.boss.getBbWidth() / 2 <= wallZ + .05, "Charge tunneled through wall");
            h.assertTrue(f.target.getHealth() == 500 && !f.boss.noPhysics, "Charge damaged through wall or bypassed collision");
            f.clear(); h.succeed();
        });
    }

    @GameTest(template="empty", batch="boss_slam_falloff", timeoutTicks=65)
    public static void doubleFistSlamHitsAtContactWithRadialFalloff(GameTestHelper h) {
        var f = fixture(h, 2.8);
        h.assertTrue(f.boss.beginAttack(Attack.SLAM, f.target), "Could not begin slam");
        Vec3 impact = f.boss.getImpactPosition();
        // Opposite sides avoid mutual entity pushing before impact.
        var inner = target(h, impact.add(1.9, 0, 0));
        var outer = target(h, impact.add(-4.4, 0, 0));
        var outside = target(h, impact.add(0, 0, 6.5));
        h.runAfterDelay(10, () -> h.assertTrue(inner.getHealth() == 500 && outer.getHealth() == 500,
                "Slam dealt damage while fists were still rising"));
        h.runAfterDelay(45, () -> {
            float innerDamage = 500 - inner.getHealth(), outerDamage = 500 - outer.getHealth();
            h.assertTrue(innerDamage > outerDamage && outerDamage > 0 && innerDamage <= 16,
                    "Slam radial damage incorrect: inner=" + innerDamage + ", outer=" + outerDamage);
            h.assertTrue(outside.getHealth() == 500, "Slam exceeded its five-block radius");
            h.assertTrue(HollowBossEntity.calculateSlamDamage(0) == 16 && HollowBossEntity.calculateSlamDamage(5) == 4,
                    "Slam center/edge tuning is incorrect");
            h.assertTrue(h.getLevel().getBlockState(BlockPos.containing(impact.add(0, -1, 0))).is(Blocks.STONE),
                    "Ground slam unexpectedly destroyed its supporting blocks");
            f.clear(); h.succeed();
        });
    }

    @GameTest(template="empty", batch="boss_cancel_no_ai", timeoutTicks=50)
    public static void disablingAiDuringWindupCancelsDelayedAreaDamage(GameTestHelper h) {
        var f = fixture(h, 2.8);
        h.assertTrue(f.boss.beginAttack(Attack.SLAM, f.target), "Could not begin slam");
        f.boss.setNoAi(true);
        h.runAfterDelay(35, () -> {
            h.assertTrue(f.target.getHealth() == 500 && f.boss.getAttack() == Attack.IDLE,
                    "Disabled AI retained a pending ground slam");
            f.clear(); h.succeed();
        });
    }

    @GameTest(template="empty", batch="boss_cancel_target_loss", timeoutTicks=45)
    public static void targetDeathCancelsPendingStrike(GameTestHelper h) {
        var f = fixture(h, 2.8);
        h.assertTrue(f.boss.beginAttack(Attack.SLAM, f.target), "Could not begin slam");
        var bystander = target(h, f.boss.getImpactPosition().add(2, 0, 0));
        f.target.discard();
        h.runAfterDelay(35, () -> {
            h.assertTrue(f.boss.getAttack() == Attack.IDLE && bystander.getHealth() == 500,
                    "Target death left a damaging pending attack");
            f.clear(); h.succeed();
        });
    }

    @GameTest(template="empty", batch="boss_slam_occlusion", timeoutTicks=60)
    public static void groundSlamRespectsWallsAndElevatedVictims(GameTestHelper h) {
        var f = fixture(h, 2.8);
        h.assertTrue(f.boss.beginAttack(Attack.SLAM, f.target), "Could not begin slam");
        Vec3 impact = f.boss.getImpactPosition();
        var shielded = target(h, impact.add(3.8, 0, 0));
        var elevated = target(h, impact.add(-3, 3, 0));
        int wallX = (int)Math.floor(impact.x) + 2;
        for (int z = -2; z <= 2; z++) for (int y = 0; y < 4; y++) h.getLevel().setBlockAndUpdate(
                BlockPos.containing(wallX, impact.y + y, impact.z + z), Blocks.STONE.defaultBlockState());
        h.runAfterDelay(42, () -> {
            h.assertTrue(f.target.getHealth() < 500, "Unshielded control target did not take slam damage");
            h.assertTrue(shielded.getHealth() == 500 && elevated.getHealth() == 500,
                    "Ground slam crossed a solid wall or struck a victim three blocks above the floor");
            f.clear(); h.succeed();
        });
    }

    @GameTest(template="empty", batch="boss_cancel_death", timeoutTicks=50)
    public static void bossDeathDuringWindupCannotLeavePosthumousDamage(GameTestHelper h) {
        var f = fixture(h, 2.8);
        h.assertTrue(f.boss.beginAttack(Attack.SLAM, f.target), "Could not begin slam");
        f.boss.hurt(f.boss.damageSources().genericKill(), 1000);
        h.runAfterDelay(35, () -> {
            h.assertTrue(f.target.getHealth() == 500 && f.boss.getAttack() == Attack.IDLE,
                    "Dead boss left a pending ground slam");
            f.clear(); h.succeed();
        });
    }

    @GameTest(template="empty", batch="boss_natural_charge", timeoutTicks=85)
    public static void naturalAiChoosesChargeAtDistanceAndActuallyConnects(GameTestHelper h) {
        var f = fixture(h, 7, true);
        Attack[] first = {Attack.IDLE};
        h.onEachTick(() -> {
            if (first[0] == Attack.IDLE && f.boss.getAttack() != Attack.IDLE) first[0] = f.boss.getAttack();
        });
        h.runAfterDelay(60, () -> {
            h.assertTrue(first[0] == Attack.CHARGE, "Natural AI did not choose charge at range: " + first[0]);
            h.assertTrue(f.target.getHealth() <= 490, "Natural charge never connected with its real target");
            h.assertTrue(f.boss.getChargeCooldownTicks() > 0, "Natural charge bypassed its cooldown");
            f.clear(); h.succeed();
        });
    }

    @GameTest(template="empty", batch="boss_natural_melee", timeoutTicks=180)
    public static void naturalAiFollowsItsSlamWithSustainedBasicStrikes(GameTestHelper h) {
        var f = fixture(h, 2.7, true);
        boolean[] sawSlam = {false};
        var starts = new java.util.ArrayList<Integer>();
        Attack[] previous = {Attack.IDLE};
        float[] healthAfterSlam = {500};
        h.onEachTick(() -> {
            Attack attack = f.boss.getAttack();
            if (attack == Attack.SLAM) sawSlam[0] = true;
            if (previous[0] == Attack.SLAM && attack != Attack.SLAM) healthAfterSlam[0] = f.target.getHealth();
            if (attack == Attack.MELEE && previous[0] != Attack.MELEE) starts.add(f.boss.tickCount);
            previous[0] = attack;
        });
        h.runAfterDelay(155, () -> {
            h.assertTrue(sawSlam[0] && healthAfterSlam[0] < 500, "Natural close-range slam never completed");
            h.assertTrue(starts.size() >= 3, "Natural AI stopped making basic strikes after slam: " + starts);
            for (int i = 1; i < starts.size(); i++) h.assertTrue(starts.get(i) - starts.get(i - 1) >= HollowBossEntity.MELEE_COOLDOWN,
                    "Natural AI bypassed the slower basic interval: " + starts);
            h.assertTrue(f.target.getHealth() <= healthAfterSlam[0] - 15,
                    "Natural basic strike animations did not produce three actual hits: " + f.target.getHealth());
            f.clear(); h.succeed();
        });
    }

    @GameTest(template="empty", batch="boss_exact_charge_cooldown", timeoutTicks=165)
    public static void chargeBecomesAvailableAfterExactlyOneHundredFortyEntityTicks(GameTestHelper h) {
        var f = fixture(h, 8);
        h.assertTrue(f.boss.beginAttack(Attack.CHARGE, f.target), "Could not begin charge");
        f.boss.cancelBossAttack();
        int started = f.boss.tickCount;
        h.onEachTick(() -> {
            int elapsed = f.boss.tickCount - started;
            int expected = Math.max(0, 140 - elapsed);
            h.assertTrue(f.boss.getChargeCooldownTicks() == expected,
                    "Seven-second cooldown drifted at entity tick " + elapsed + ": " + f.boss.getChargeCooldownTicks());
            if (elapsed < 140) h.assertTrue(!f.boss.beginAttack(Attack.CHARGE, f.target), "Charge restarted early at tick " + elapsed);
            else {
                h.assertTrue(elapsed == 140 && f.boss.beginAttack(Attack.CHARGE, f.target),
                        "Charge was not available on its exact 140th entity tick");
                f.clear(); h.succeed();
            }
        });
    }

    @GameTest(template="empty", batch="boss_cancel_changed_target", timeoutTicks=50)
    public static void changingTargetDuringWindupCancelsTheOriginalAttack(GameTestHelper h) {
        var f = fixture(h, 2.8);
        h.assertTrue(f.boss.beginAttack(Attack.SLAM, f.target), "Could not begin slam");
        var replacement = target(h, f.boss.getImpactPosition().add(2, 0, 0));
        f.boss.setTarget(replacement);
        h.runAfterDelay(35, () -> {
            h.assertTrue(f.boss.getAttack() == Attack.IDLE && f.target.getHealth() == 500 && replacement.getHealth() == 500,
                    "Target swap retained a damaging attack committed to the old target");
            f.clear(); h.succeed();
        });
    }

    @GameTest(template="empty", batch="boss_cancel_cleared_target", timeoutTicks=50)
    public static void clearingTargetDuringWindupCancelsTheOriginalAttack(GameTestHelper h) {
        var f = fixture(h, 2.8);
        h.assertTrue(f.boss.beginAttack(Attack.SLAM, f.target), "Could not begin slam");
        f.boss.setTarget(null);
        h.runAfterDelay(35, () -> {
            h.assertTrue(f.boss.getAttack() == Attack.IDLE && f.target.getHealth() == 500,
                    "Cleared target retained pending area damage");
            f.clear(); h.succeed();
        });
    }
}
