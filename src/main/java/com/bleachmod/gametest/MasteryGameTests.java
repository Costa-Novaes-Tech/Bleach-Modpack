package com.bleachmod.gametest;

import com.bleachmod.common.data.*;
import com.bleachmod.common.technique.*;
import com.bleachmod.init.ModItems;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.gametest.*;
import java.util.UUID;

@GameTestHolder("bleachmod")
@PrefixGameTestTemplate(false)
public final class MasteryGameTests {
    private static PlayerData data(FakePlayer p) { return PlayerCapability.get(p).orElseThrow(() -> new IllegalStateException("Capability missing")); }
    private static FakePlayer player(GameTestHelper h) {
        var p = new FakePlayer(h.getLevel(), new GameProfile(UUID.randomUUID(), "MasteryTest"));
        data(p).initializeShinigami(); p.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.ASAUCHI.get()));
        var pos = h.absolutePos(new BlockPos(1, 3, 1)); p.setPos(pos.getX() + .5, h.getLevel().getMaxBuildHeight() - 64, pos.getZ() + .5); p.setYRot(0);
        return p;
    }
    private static Zombie target(GameTestHelper h, FakePlayer p) {
        var z = EntityType.ZOMBIE.create(h.getLevel()); z.setNoAi(true); z.setNoGravity(true);
        z.setItemSlot(EquipmentSlot.HEAD, new ItemStack(net.minecraft.world.item.Items.IRON_HELMET));
        z.setPos(p.getX(), p.getY(), p.getZ() + 2); h.getLevel().getChunkAt(z.blockPosition()); h.getLevel().addFreshEntity(z); return z;
    }
    @GameTest(template = "empty", timeoutTicks = 200)
    public static void realPracticeCrossesThresholdWithoutPurchasingFormOrDebitingPoints(GameTestHelper h) {
        var p = player(h); var d = data(p); d.getCharacter().setMastery("zanpakuto", "sealed", 19.5);
        float points = d.getResources().getTrainingPoints(); var z = target(h, p);
        h.runAfterDelay(5, () -> {
            try {
                z.hurt(p.damageSources().playerAttack(p), 1);
                h.assertTrue(d.getCharacter().getMastery("zanpakuto", "sealed") == 20, "Threshold practice missing");
                h.assertTrue(d.getCharacter().isFormDiscovered("shikai"), "Shikai discovery missing");
                h.assertTrue(!com.bleachmod.common.evolution.TransformationsHelper.isUnlocked(d, "zanpakuto", "shikai"), "Discovery bypassed skill purchase");
                h.assertTrue(d.getResources().getTrainingPoints() == points, "Discovery debited training points");
                z.invulnerableTime = 0; z.hurt(p.damageSources().playerAttack(p), 1);
                h.assertTrue(d.getCharacter().getMastery("zanpakuto", "sealed") == 20, "Threshold notification bypassed repetition limit");
            } finally { z.discard(); }
            h.succeed();
        });
    }
    @GameTest(template = "empty", timeoutTicks = 200)
    public static void validSwordPracticeHasLimitsAndDoesNotAccumulateWhileIdle(GameTestHelper h) {
        var p = player(h); var d = data(p); var z = target(h, p);
        h.runAfterDelay(2, () -> {
            z.hurt(p.damageSources().playerAttack(p), 1);
            h.assertTrue(d.getCharacter().getMastery("zanpakuto", "sealed") == .5, "Valid practice missing");
            z.invulnerableTime = 0; z.hurt(p.damageSources().playerAttack(p), 1);
            var other = target(h, p); other.hurt(p.damageSources().playerAttack(p), 1);
            h.assertTrue(d.getCharacter().getMastery("zanpakuto", "sealed") == .5, "Repeat/global limit failed");
        });
        h.runAfterDelay(110, () -> {
            h.assertTrue(d.getCharacter().getMastery("zanpakuto", "sealed") == .5, "Idle XP granted");
            z.invulnerableTime = 0; z.hurt(p.damageSources().playerAttack(p), 1);
            h.assertTrue(d.getCharacter().getMastery("zanpakuto", "sealed") == 1, "Practice did not resume"); h.succeed();
        });
    }
    @GameTest(template = "empty", timeoutTicks = 200)
    public static void testFlagsAndWrongIdentityCannotEarnPractice(GameTestHelper h) {
        var p = player(h); var d = data(p); var z = target(h, p);
        d.getResources().setCostsDisabled(true); z.hurt(p.damageSources().playerAttack(p), 1);
        d.getResources().setCostsDisabled(false); d.getStatus().setCooldownsDisabled(true); z.invulnerableTime = 0; z.hurt(p.damageSources().playerAttack(p), 1);
        d.getStatus().setCooldownsDisabled(false); d.getCharacter().bindZanpakuto("hyorinmaru"); z.invulnerableTime = 0; z.hurt(p.damageSources().playerAttack(p), 1);
        h.assertTrue(d.getCharacter().getMastery("zanpakuto", "sealed") == 0 && d.getCharacter().getZanpakutoMastery("asauchi", "sealed") == 0, "Test/wrong-identity XP granted"); h.succeed();
    }
    @GameTest(template = "empty", timeoutTicks = 200)
    public static void lockedSlotDoesNotConsumeResourcesAndControlTrainsItsOwnTechnique(GameTestHelper h) {
        var p = player(h); var d = data(p); d.getCharacter().bindZanpakuto("hyorinmaru");
        // Isolate the selected target from nearby mobs created by parallel long-range kit fixtures.
        p.setPos(p.getX(), h.getLevel().getMaxBuildHeight() - 8, p.getZ());
        // The persistent GameTest world may retain earlier fixture mobs in this open-air cell.
        h.getLevel().getChunkAt(p.blockPosition());
        h.getLevel().getEntitiesOfClass(Zombie.class, p.getBoundingBox().inflate(5, 2, 5)).forEach(Entity::discard);
        p.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.HYORINMARU.get())); var z = target(h, p);
        h.runAfterDelay(5, () -> {
        float before = d.getResources().getCurrentReiatsu(); TechniqueService.executeSlot(p, d, 4);
        h.assertTrue(d.getResources().getCurrentReiatsu() == before && d.getStatus().getTechniqueSlot4CooldownTicks() == 0, "Locked technique consumed resources");
        d.getCharacter().setMastery("zanpakuto", "sealed", 60);
        TechniqueService.executeSlot(p, d, 4);
        h.assertTrue(d.getCharacter().getMastery("zanpakuto", "sealed") == 60.5 && d.getCharacter().getTechniqueMastery("sealed", 4) == 1, "Accepted control practice missing");
        h.assertTrue(z.hasEffect(net.minecraft.world.effect.MobEffects.MOVEMENT_SLOWDOWN), "Control did not occur"); h.succeed();
        });
    }
    @GameTest(template = "empty", timeoutTicks = 200)
    public static void passiveFireAndExpiredIntentCannotEarnTechniquePractice(GameTestHelper h) {
        var p = player(h); var d = data(p); var z = target(h, p);
        MasteryPracticeService.hurt(p, z, p.damageSources().playerAttack(p), 1, 0);
        h.assertTrue(d.getCharacter().getMastery("zanpakuto", "sealed") == 0, "Passive fire earned XP");
        d.getCharacter().bindZanpakuto("hyorinmaru"); p.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.HYORINMARU.get()));
        MasteryPracticeService.noteIntent(p);
        h.runAfterDelay(110, () -> {
            z.invulnerableTime = 0; MasteryPracticeService.hurt(p, z, p.damageSources().playerAttack(p), 1, 1);
            h.assertTrue(d.getCharacter().getMastery("zanpakuto", "sealed") == 0, "Expired intent earned XP"); h.succeed();
        });
    }
    @GameTest(template = "empty", timeoutTicks = 200)
    public static void asauchiReleaseOnlyChangesSwordDamage(GameTestHelper h) {
        var p = player(h); var d = data(p); d.getSkills().get("zanpakuto").setLevel(2);
        d.getCharacter().unlockForm("shikai"); d.getCharacter().unlockForm("bankai"); d.getCharacter().setMastery("zanpakuto", "shikai", 25);
        for (String form : java.util.List.of("sealed", "shikai", "bankai")) {
            d.getCharacter().setActiveForm("zanpakuto", form);
            var z = target(h, p); var hit = new net.minecraftforge.event.entity.living.LivingHurtEvent(z, p.damageSources().playerAttack(p), 10);
            com.bleachmod.server.events.CombatEvents.hurt(hit);
            h.assertTrue(hit.getAmount() == ("bankai".equals(form) ? 15 : "shikai".equals(form) ? 12 : 10), "Asauchi release damage wrong");
            float before = d.getResources().getCurrentReiatsu(); TechniqueService.executeSlot(p, d, 1);
            h.assertTrue(d.getResources().getCurrentReiatsu() == before && !TechniqueService.isIgnitionActive(d), "Asauchi invoked elemental technique");
            double mastery = d.getCharacter().getMastery("zanpakuto", form); p.tickCount = 100;
            com.bleachmod.server.events.TickHandler.onPlayerTick(new net.minecraftforge.event.TickEvent.PlayerTickEvent(net.minecraftforge.event.TickEvent.Phase.END, p));
            h.assertTrue(d.getCharacter().getMastery("zanpakuto", form) == mastery, "Released form granted idle mastery");
        }
        h.succeed();
    }
    @GameTest(template = "empty", timeoutTicks = 200)
    public static void ignitionTrainsThroughAcceptedSwordHitsNotThroughItsToggle(GameTestHelper h) {
        var p = player(h); var d = data(p); d.getCharacter().bindZanpakuto("ryujin_jakka");
        p.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.RYUJIN_JAKKA.get()));
        TechniqueService.executeSlot(p, d, 1);
        h.assertTrue(TechniqueService.isIgnitionActive(d) && d.getCharacter().getTechniqueMastery("sealed", 1) == 0, "Toggle awarded practice");
        var z = target(h, p); z.hurt(p.damageSources().playerAttack(p), 1);
        h.assertTrue(d.getCharacter().getMastery("zanpakuto", "sealed") == .5 && d.getCharacter().getTechniqueMastery("sealed", 1) == 1, "Ignited melee practice missing or duplicated");
        h.succeed();
    }
    @GameTest(template = "empty", timeoutTicks = 200)
    public static void activeArmorTrainsDefenseButInvulnerableAndAlliedTargetsDoNot(GameTestHelper h) {
        var p = player(h); var d = data(p); var z = target(h, p);
        z.setInvulnerable(true); z.hurt(p.damageSources().playerAttack(p), 1);
        h.assertTrue(d.getCharacter().getMastery("zanpakuto", "sealed") == 0, "Invulnerable target earned practice");
        z.setInvulnerable(false); d.getCharacter().bindZanpakuto("hyorinmaru");
        p.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.HYORINMARU.get()));
        d.getCharacter().setMastery("zanpakuto", "sealed", 40); TechniqueService.executeSlot(p, d, 3);
        // Forge FakePlayer rejects vanilla damage; dispatch the accepted hurt/damage pipeline explicitly.
        var hit = new net.minecraftforge.event.entity.living.LivingHurtEvent(p, p.damageSources().mobAttack(z), 1);
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(hit);
        h.assertTrue(hit.getAmount() == .75F, "Active armor did not reduce the incoming blow");
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.entity.living.LivingDamageEvent(p, hit.getSource(), hit.getAmount()));
        h.assertTrue(d.getCharacter().getMastery("zanpakuto", "sealed") == 40.5 && d.getCharacter().getTechniqueMastery("sealed", 3) == 1, "Active armor practice missing");
        var team = h.getLevel().getScoreboard().addPlayerTeam("xp" + UUID.randomUUID().toString().substring(0, 8));
        h.getLevel().getScoreboard().addPlayerToTeam(p.getScoreboardName(), team);
        h.getLevel().getScoreboard().addPlayerToTeam(z.getScoreboardName(), team);
        h.runAfterDelay(25, () -> {
            try {
                MasteryPracticeService.noteIntent(p); MasteryPracticeService.control(p, z, 1);
                h.assertTrue(d.getCharacter().getMastery("zanpakuto", "sealed") == 40.5, "Ally earned practice");
            } finally { h.getLevel().getScoreboard().removePlayerTeam(team); }
            h.succeed();
        });
    }
}
