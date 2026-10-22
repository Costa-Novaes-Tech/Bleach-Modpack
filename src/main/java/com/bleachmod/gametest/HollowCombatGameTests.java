package com.bleachmod.gametest;

import com.bleachmod.entity.*;
import com.bleachmod.registry.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.gametest.*;
import java.util.ArrayList;

@GameTestHolder("bleachmod") @PrefixGameTestTemplate(false)
public final class HollowCombatGameTests {
    private record Fixture(HollowEntity mob, Mob target) {
        void clear() { mob.discard(); target.discard(); }
    }
    private static Fixture fixture(GameTestHelper h, HollowVariant variant, boolean naturalAi) {
        return fixture(h, variant, naturalAi, EntityType.IRON_GOLEM);
    }
    private static Fixture fixture(GameTestHelper h, HollowVariant variant, boolean naturalAi, EntityType<? extends Mob> targetType) {
        var base = h.absolutePos(new BlockPos(1, 1, 1));
        int y = h.getLevel().getMaxBuildHeight() - 40;
        var level = h.getLevel();
        level.getChunkAt(new BlockPos(base.getX(), y, base.getZ()));
        // Tests run in separate batches. Remove stale fixtures if an earlier failed
        // run left entities above the template's automatic cleanup volume.
        var area = new AABB(base.getX() - 10, y - 3, base.getZ() - 10, base.getX() + 14, y + 12, base.getZ() + 14);
        level.getEntitiesOfClass(HollowEntity.class, area).forEach(Entity::discard);
        level.getEntitiesOfClass(IronGolem.class, area).forEach(Entity::discard);
        level.getEntitiesOfClass(Mob.class, area, m -> m.getTags().contains("hollow_combat_fixture")).forEach(Entity::discard);
        for (int x = -10; x <= 12; x++) for (int z = -10; z <= 12; z++) {
            level.setBlockAndUpdate(new BlockPos(base.getX() + x, y - 1, base.getZ() + z), Blocks.STONE.defaultBlockState());
        }
        var mob = ModEntities.HOLLOW.get().create(level);
        mob.setVariant(variant);
        mob.setHealth(mob.getMaxHealth());
        mob.setPersistenceRequired();
        var target = targetType.create(level);
        mob.addTag("hollow_combat_fixture"); target.addTag("hollow_combat_fixture");
        target.getAttribute(Attributes.MAX_HEALTH).setBaseValue(500);
        target.getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(1);
        target.setHealth(500);
        target.setNoAi(true);
        target.setNoGravity(true);
        double distance = (variant.width + target.getBbWidth()) / 2 + .3;
        mob.setPos(base.getX(), y, base.getZ());
        target.setPos(base.getX() + distance, y, base.getZ());
        if (!naturalAi) {
            mob.goalSelector.removeAllGoals(goal -> true);
            mob.targetSelector.removeAllGoals(goal -> true);
        }
        mob.setTarget(target);
        level.addFreshEntity(target);
        level.addFreshEntity(mob);
        return new Fixture(mob, target);
    }

    private static void cadence(GameTestHelper h, HollowVariant variant, int expected) {
        var f = fixture(h, variant, true, variant == HollowVariant.SWIFT ? EntityType.HUSK : EntityType.IRON_GOLEM);
        var starts = new ArrayList<Integer>();
        h.onEachTick(() -> {
            int started = f.mob.getLastMeleeStartTick();
            if (started >= 0 && (starts.isEmpty() || starts.get(starts.size() - 1) != started)) starts.add(started);
        });
        h.runAfterDelay(expected * 5 + 30, () -> {
            h.assertTrue(starts.size() >= 6, "Natural AI stopped attacking after closing on its target: " + starts);
            for (int i = 1; i < starts.size(); i++) h.assertTrue(starts.get(i) - starts.get(i - 1) == expected,
                    "Wrong natural cadence for " + variant + ": " + starts);
            h.assertTrue(f.mob.getSuccessfulMeleeHits() >= 6, "Animations started without sustained actual damage");
            f.clear(); h.succeed();
        });
    }

    @GameTest(template="empty", batch="hollow_brute_cadence", timeoutTicks=320)
    public static void tankNaturalAiAttacksEveryFortyEightTicks(GameTestHelper h) { cadence(h, HollowVariant.BRUTE, 48); }

    @GameTest(template="empty", batch="hollow_swift_cadence", timeoutTicks=130)
    public static void swiftNaturalAiBypassesTheOldTwentyTickThrottle(GameTestHelper h) { cadence(h, HollowVariant.SWIFT, 14); }

    @GameTest(template="empty", batch="hollow_cooldown_save")
    public static void canceledStrikesAndReloadCannotBypassCooldown(GameTestHelper h) {
        var f = fixture(h, HollowVariant.BRUTE, false);
        h.assertTrue(f.mob.beginMeleeStrike(f.target), "Could not begin attack");
        f.mob.cancelMeleeStrike();
        h.assertTrue(!f.mob.beginMeleeStrike(f.target), "Canceled windup reset the cooldown");
        var tag = new CompoundTag();
        f.mob.addAdditionalSaveData(tag);
        var copy = ModEntities.HOLLOW.get().create(h.getLevel());
        copy.readAdditionalSaveData(tag);
        h.assertTrue(copy.getMeleeCooldownTicks() == 48 && !copy.hasPendingMeleeStrike(), "Reload lost cooldown or restored a stale hit");
        f.clear(); h.succeed();
    }

    @GameTest(template="empty", batch="hollow_aerial_passes", timeoutTicks=1200)
    public static void flyerStrikesTwoOrThreeTimesRetreatsAndReturns(GameTestHelper h) {
        var f = fixture(h, HollowVariant.FLYING, true, EntityType.HUSK);
        // Start far enough away to exercise a real descent instead of a contact-only fixture.
        f.mob.setPos(f.mob.getX() - 5, f.mob.getY() + 4, f.mob.getZ() - 3);
        int[] retreats = {0}, hitsBeforeRetreat = {0}, retreatAge = {0}, lastStart = {-1};
        var burstStarts = new ArrayList<Integer>();
        h.onEachTick(() -> {
            var goal = f.mob.getFlightCombat();
            if (f.mob.getFlightPhase() == HollowFlightCombatGoal.Phase.BURST) {
                int start = f.mob.getLastMeleeStartTick();
                if (goal.getStartedStrikes() > burstStarts.size() && start >= 0 && start != lastStart[0]) {
                    if (!burstStarts.isEmpty()) h.assertTrue(start - burstStarts.get(burstStarts.size() - 1) >= 28,
                            "Flyer bypassed its interval inside a moving aerial burst: " + burstStarts + " -> " + start);
                    burstStarts.add(start); lastStart[0] = start;
                }
            }
            if (f.mob.getFlightPhase() == HollowFlightCombatGoal.Phase.RETREAT) {
                if (goal.getRetreats() != retreats[0]) {
                    retreats[0] = goal.getRetreats();
                    int count = goal.getLastBurstAttempts();
                    h.assertTrue(count >= 2 && count <= 3, "Flyer broke off before/after its 2-3 strikes: " + count);
                    h.assertTrue(f.mob.getSuccessfulMeleeHits() - hitsBeforeRetreat[0] >= 2, "Burst failed to land its actual attacks");
                    hitsBeforeRetreat[0] = f.mob.getSuccessfulMeleeHits();
                    retreatAge[0] = 0;
                    burstStarts.clear(); lastStart[0] = -1;
                }
                retreatAge[0]++;
                if (retreatAge[0] == 40) {
                    // Cooldown has elapsed: phase itself must prevent a nearby follower
                    // from restarting melee while the creature is climbing away.
                    h.assertTrue(f.mob.getMeleeCooldownTicks() == 0, "Fixture did not reach the post-cooldown retreat");
                    var old = f.target.position();
                    f.target.setPos(f.mob.position().add(1.4, 0, 0));
                    h.assertTrue(!f.mob.beginMeleeStrike(f.target), "Retreat permitted another strike");
                    f.target.setPos(old);
                }
                h.assertTrue(!f.mob.hasPendingMeleeStrike() && f.mob.getSuccessfulMeleeHits() == hitsBeforeRetreat[0],
                        "Flyer continued damaging during retreat");
                if (retreatAge[0] == 55) h.assertTrue(f.mob.getY() > f.target.getY() + 1.5
                                && f.mob.distanceToSqr(f.target) > 16, "Retreat did not create vertical/horizontal separation");
            } else if (retreatAge[0] > 0) {
                h.assertTrue(retreatAge[0] >= HollowFlightCombatGoal.MIN_RETREAT_TICKS - 1, "Retreat ended too early");
                retreatAge[0] = 0;
            }
            h.assertTrue(!f.mob.noPhysics, "Aerial AI enabled passage through blocks");
            if (retreats[0] >= 5) { f.clear(); h.succeed(); }
        });
    }

    @GameTest(template="empty", batch="hollow_aerial_target_loss", timeoutTicks=90)
    public static void targetDeathResetsTheFlightPassAndPendingStrike(GameTestHelper h) {
        var f = fixture(h, HollowVariant.FLYING, true);
        // Isolate loss of this target from legitimate acquisition of another test's golem.
        f.mob.targetSelector.removeAllGoals(goal -> true);
        h.runAfterDelay(40, f.target::discard);
        h.runAfterDelay(48, () -> {
            h.assertTrue(f.mob.getFlightPhase() == HollowFlightCombatGoal.Phase.CIRCLE && !f.mob.hasPendingMeleeStrike()
                    && f.mob.getTarget() == null, "Target loss retained an active aerial pass");
            f.clear(); h.succeed();
        });
    }

    @GameTest(template="empty", batch="hollow_flying_cadence", timeoutTicks=110)
    public static void flyerRepeatedRequestsObserveTwentyEightTicks(GameTestHelper h) {
        var f = fixture(h, HollowVariant.FLYING, false);
        var starts = new ArrayList<Long>();
        h.onEachTick(() -> {
            if (f.mob.beginMeleeStrike(f.target)) starts.add(h.getLevel().getGameTime());
        });
        h.runAfterDelay(90, () -> {
            h.assertTrue(starts.size() >= 3, "Flyer did not restart after its cooldown: " + starts);
            for (int i = 1; i < starts.size(); i++) h.assertTrue(starts.get(i) - starts.get(i - 1) == 28,
                    "Wrong flyer clock under repeated requests: " + starts);
            h.assertTrue(f.mob.getSuccessfulMeleeHits() >= 3, "Cadence did not result in actual damage");
            f.clear(); h.succeed();
        });
    }
}
