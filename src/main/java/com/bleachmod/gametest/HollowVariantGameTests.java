package com.bleachmod.gametest;

import com.bleachmod.entity.*;
import com.bleachmod.registry.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.navigation.*;
import net.minecraftforge.gametest.*;
import java.util.EnumSet;

@GameTestHolder("bleachmod") @PrefixGameTestTemplate(false)
public final class HollowVariantGameTests {
    private static HollowEntity hollow(GameTestHelper h, HollowVariant variant) {
        var mob = ModEntities.HOLLOW.get().create(h.getLevel());
        mob.setVariant(variant);
        mob.setHealth(mob.getMaxHealth());
        return mob;
    }

    @GameTest(template="empty")
    public static void sameEntityTypeRollsAllVariantsForEggAndNaturalSpawn(GameTestHelper h) {
        for (var reason : new MobSpawnType[]{MobSpawnType.SPAWN_EGG, MobSpawnType.NATURAL, MobSpawnType.COMMAND, MobSpawnType.SPAWNER}) {
            var seen = EnumSet.noneOf(HollowVariant.class);
            for (int i = 0; i < 36; i++) {
                var mob = ModEntities.HOLLOW.get().create(h.getLevel());
                mob.getRandom().setSeed(928374L + i * 7919L);
                mob.finalizeSpawn(h.getLevel(), h.getLevel().getCurrentDifficultyAt(h.absolutePos(BlockPos.ZERO)), reason, null, null);
                h.assertTrue(mob.getType() == ModEntities.HOLLOW.get(), "Variant changed registry identity");
                h.assertTrue(mob.getHealth() == mob.getMaxHealth(), "Spawned with partial health");
                seen.add(mob.getVariant());
            }
            h.assertTrue(seen.size() == 3, "Missing spawn variants for " + reason + ": " + seen);
        }
        h.succeed();
    }

    @GameTest(template="empty")
    public static void variantsHaveDistinctCombatStatsAndRealHitboxes(GameTestHelper h) {
        for (var v : HollowVariant.values()) {
            var mob = hollow(h, v);
            h.assertTrue(mob.getMaxHealth() == v.health && mob.getAttributeValue(Attributes.ATTACK_DAMAGE) == v.damage
                    && mob.getAttributeValue(Attributes.MOVEMENT_SPEED) == v.speed, "Incorrect stats: " + v);
            h.assertTrue(Math.abs(mob.getBbHeight() - v.height) < .001 && Math.abs(mob.getBbWidth() - v.width) < .001,
                    "Collision size does not match variant: " + v);
        }
        h.succeed();
    }

    @GameTest(template="empty")
    public static void saveReloadPreservesVariantDamageAndFlightWithoutHealing(GameTestHelper h) {
        for (var v : HollowVariant.values()) {
            var mob = hollow(h, v);
            mob.setHealth(11);
            var tag = new CompoundTag();
            mob.addAdditionalSaveData(tag);
            var restored = ModEntities.HOLLOW.get().create(h.getLevel());
            restored.readAdditionalSaveData(tag);
            h.assertTrue(restored.getVariant() == v && restored.getHealth() == 11, "Reload rerolled or healed " + v);
            h.assertTrue(restored.getAttributeValue(Attributes.ATTACK_DAMAGE) == v.damage && restored.isNoGravity() == (v == HollowVariant.FLYING),
                    "Reload lost combat/movement profile " + v);
        }
        h.succeed();
    }

    @GameTest(template="empty")
    public static void syncedVariantUpdatesObserverModelAndDimensions(GameTestHelper h) {
        for (var v : HollowVariant.values()) {
            var server = hollow(h, v);
            var observer = ModEntities.HOLLOW.get().create(h.getLevel());
            var packet = server.getEntityData().getNonDefaultValues();
            if (packet != null) observer.getEntityData().assignValues(packet);
            h.assertTrue(observer.getVariant() == v && Math.abs(observer.getBbHeight() - v.height) < .001,
                    "Observer retained wrong model/hitbox " + v);
        }
        h.succeed();
    }

    @GameTest(template="empty")
    public static void legacyAndInvalidTagsFallBackWithoutChangingBoss(GameTestHelper h) {
        var mob = hollow(h, HollowVariant.FLYING);
        var tag = new CompoundTag();
        tag.putString("HollowVariant", "invalid");
        mob.readAdditionalSaveData(tag);
        h.assertTrue(mob.getVariant() == HollowVariant.BRUTE && !mob.isNoGravity(), "Invalid id did not fall back");
        mob.readAdditionalSaveData(new CompoundTag());
        h.assertTrue(mob.getVariant() == HollowVariant.BRUTE, "Legacy Hollow cannot load");
        var boss = ModEntities.HOLLOW_BOSS.get().create(h.getLevel());
        boss.setVariant(HollowVariant.FLYING);
        boss.finalizeSpawn(h.getLevel(), h.getLevel().getCurrentDifficultyAt(BlockPos.ZERO), MobSpawnType.SPAWN_EGG, null, null);
        h.assertTrue(!boss.usesVariants() && !boss.isFlyingVariant() && boss.getMaxHealth() == 50
                && boss.getAttributeValue(Attributes.ATTACK_DAMAGE) == 8 && Math.abs(boss.getBbHeight() - 5.4F) < .001, "Boss profile changed");
        h.succeed();
    }

    @GameTest(template="empty")
    public static void switchingFlightRestoresGroundNavigationAndGravity(GameTestHelper h) {
        var mob = hollow(h, HollowVariant.FLYING);
        h.assertTrue(mob.getNavigation() instanceof FlyingPathNavigation && mob.isNoGravity(), "Flying navigation missing");
        h.assertTrue(!mob.causeFallDamage(30, 1, mob.damageSources().fall()), "Flyer took fall damage");
        mob.setVariant(HollowVariant.SWIFT);
        h.assertTrue(mob.getNavigation() instanceof GroundPathNavigation && !mob.isNoGravity(), "Ground navigation not restored");
        h.succeed();
    }

    @GameTest(template="empty", timeoutTicks=120)
    public static void flyerNavigatesVerticallyAlongAerialPath(GameTestHelper h) {
        var mob = hollow(h, HollowVariant.FLYING);
        var pos = h.absolutePos(new BlockPos(1, 6, 1));
        mob.setPos(pos.getX(), pos.getY(), pos.getZ());
        mob.setPersistenceRequired();
        h.getLevel().addFreshEntity(mob);
        double startY = mob.getY();
        h.assertTrue(mob.getNavigation().moveTo(mob.getX() + 2, startY + 4, mob.getZ() + 2, 1), "No aerial path");
        h.runAfterDelay(35, () -> {
            h.assertTrue(mob.getY() > startY + .5, "Flyer did not ascend with its actual movement controller");
            mob.discard();
            h.succeed();
        });
    }

    @GameTest(template="empty", timeoutTicks=160)
    public static void flyerPursuesAndDamagesAnElevatedTarget(GameTestHelper h) {
        var mob = hollow(h, HollowVariant.FLYING);
        var pos = h.absolutePos(new BlockPos(2, 12, 2));
        var target = net.minecraft.world.entity.EntityType.IRON_GOLEM.create(h.getLevel());
        target.setNoAi(true);
        target.setNoGravity(true);
        target.setPos(pos.getX() + 4, pos.getY() + 3, pos.getZ());
        mob.setPos(pos.getX(), pos.getY(), pos.getZ());
        mob.setPersistenceRequired();
        h.getLevel().addFreshEntity(target);
        h.getLevel().addFreshEntity(mob);
        mob.setTarget(target);
        h.succeedWhen(() -> {
            h.assertTrue(target.getHealth() < target.getMaxHealth(), "Flyer never reached and attacked the elevated target");
            mob.discard();
            target.discard();
        });
    }

    @GameTest(template="empty")
    public static void meleeHitsUseTheVariantDamage(GameTestHelper h) {
        for (var v : HollowVariant.values()) {
            var mob = hollow(h, v);
            var target = net.minecraft.world.entity.EntityType.IRON_GOLEM.create(h.getLevel());
            var pos = h.absolutePos(new BlockPos(1, 16, 1));
            mob.setPos(pos.getX(), pos.getY(), pos.getZ());
            target.setPos(pos.getX() + 1, pos.getY(), pos.getZ());
            h.getLevel().addFreshEntity(mob);
            h.getLevel().addFreshEntity(target);
            float before = target.getHealth();
            h.assertTrue(mob.doHurtTarget(target), "Variant failed to deal damage " + v);
            h.assertTrue(Math.abs(before - target.getHealth() - v.damage) < .001, "Incorrect melee damage " + v);
            mob.discard();
            target.discard();
        }
        h.succeed();
    }

    private record StrikeFixture(HollowEntity mob, net.minecraft.world.entity.animal.IronGolem target) { }
    private static StrikeFixture strike(GameTestHelper h) {
        var mob = hollow(h, HollowVariant.BRUTE);
        var target = net.minecraft.world.entity.EntityType.IRON_GOLEM.create(h.getLevel());
        var pos = h.absolutePos(new BlockPos(1, 1, 1));
        double y = h.getLevel().getMaxBuildHeight() - 32;
        h.getLevel().getChunkAt(new BlockPos(pos.getX(), (int)y, pos.getZ()));
        for (int x = -3; x <= 4; x++) for (int z = -3; z <= 3; z++) {
            h.getLevel().setBlockAndUpdate(new BlockPos(pos.getX() + x, (int)y - 1, pos.getZ() + z),
                    net.minecraft.world.level.block.Blocks.STONE.defaultBlockState());
        }
        mob.setPos(pos.getX(), y, pos.getZ());
        // Outside the sum of the two collision radii, but inside melee reach.
        // Overlapping floating fixtures repel each other before the contact tick.
        target.setPos(pos.getX() + 1.9, y, pos.getZ());
        target.setNoAi(true);
        target.setOnGround(true);
        mob.setOnGround(true);
        // Keep AI enabled to exercise the real aiStep contact clock, while
        // separating this timing fixture from autonomous path/target decisions.
        // Pursuit is covered by flyerPursuesAndDamagesAnElevatedTarget above.
        mob.goalSelector.removeAllGoals(goal -> true);
        mob.targetSelector.removeAllGoals(goal -> true);
        mob.setTarget(target);
        h.getLevel().addFreshEntity(target);
        h.getLevel().addFreshEntity(mob);
        h.assertTrue(mob.beginMeleeStrike(target), "Could not start anticipation: alive=" + mob.isAlive()
                + ", noAi=" + mob.isNoAi() + ", targetAlive=" + target.isAlive()
                + ", distance=" + mob.distanceToSqr(target) + ", sight=" + mob.getSensing().hasLineOfSight(target)
                + ", positions=" + mob.position() + "/" + target.position());
        h.assertTrue(target.getHealth() == target.getMaxHealth(), "Damage applied before contact");
        return new StrikeFixture(mob, target);
    }

    @GameTest(template="empty", batch="hollow_contact_clock", timeoutTicks=30)
    public static void tankAnticipatesAndDealsDamageAtItsContactFrame(GameTestHelper h) {
        var f = strike(h);
        h.runAfterDelay(5, () -> h.assertTrue(f.target.getHealth() == f.target.getMaxHealth(), "Tank hit during windup"));
        h.runAfterDelay(10, () -> {
            h.assertTrue(Math.abs(f.target.getMaxHealth() - f.target.getHealth() - 12) < .001,
                    "Contact failed or dealt damage twice: health=" + f.target.getHealth() + ", target=" + (f.mob.getTarget() == f.target)
                            + ", distance=" + f.mob.distanceToSqr(f.target) + ", sight=" + f.mob.getSensing().hasLineOfSight(f.target)
                            + ", positions=" + f.mob.position() + "/" + f.target.position()
                            + ", gravity=" + f.mob.isNoGravity() + "/" + f.target.isNoGravity());
            f.mob.discard(); f.target.discard(); h.succeed();
        });
    }

    @GameTest(template="empty", batch="hollow_escape_clock", timeoutTicks=30)
    public static void escapingTheWindupCancelsTheHit(GameTestHelper h) {
        var f = strike(h);
        h.runAfterDelay(2, () -> f.target.setPos(f.target.getX() + 12, f.target.getY(), f.target.getZ()));
        h.runAfterDelay(11, () -> {
            h.assertTrue(f.target.getHealth() == f.target.getMaxHealth(), "Strike hit a target outside reach");
            f.mob.discard(); f.target.discard(); h.succeed();
        });
    }

    @GameTest(template="empty", batch="hollow_disabled_clock", timeoutTicks=30)
    public static void disablingAiCancelsPendingContact(GameTestHelper h) {
        var f = strike(h);
        f.mob.setNoAi(true);
        h.runAfterDelay(11, () -> {
            h.assertTrue(f.target.getHealth() == f.target.getMaxHealth(), "Disabled mob retained a delayed hit");
            f.mob.discard(); f.target.discard(); h.succeed();
        });
    }
}
