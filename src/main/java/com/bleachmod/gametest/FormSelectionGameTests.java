package com.bleachmod.gametest;

import com.bleachmod.common.data.PlayerCapability;
import com.bleachmod.common.data.PlayerData;
import com.bleachmod.common.network.c2s.SelectFormC2S;
import com.mojang.authlib.GameProfile;
import net.minecraft.gametest.framework.*;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.gametest.*;
import java.util.UUID;

/** Picking a release target is a menu action: it must never refund cooldowns or be eaten by combat input. */
@GameTestHolder("bleachmod") @PrefixGameTestTemplate(false)
public final class FormSelectionGameTests {
    private static PlayerData data(FakePlayer p) { return PlayerCapability.get(p).orElseThrow(() -> new IllegalStateException("Missing capability")); }

    private static FakePlayer player(GameTestHelper h) {
        var p = new FakePlayer(h.getLevel(), new GameProfile(UUID.randomUUID(), "FormPicker"));
        p.setPos(h.absoluteVec(new Vec3(2.5, 2, 2.5)));
        var d = data(p); d.initializeShinigami(); d.getCharacter().bindZanpakuto("ryujin_jakka");
        d.getSkills().get("zanpakuto").setLevel(2);
        d.getCharacter().unlockForm("shikai"); d.getCharacter().unlockForm("bankai");
        d.getCharacter().setMastery("zanpakuto", "shikai", 100); d.getCharacter().setMastery("zanpakuto", "bankai", 100);
        return p;
    }

    private static void spendCooldowns(PlayerData d) {
        d.getStatus().setTechniqueSlot1CooldownTicks(100); d.getStatus().setTechniqueSlot2CooldownTicks(120);
        d.getStatus().setTechniqueSlot3CooldownTicks(140); d.getStatus().setTechniqueSlot4CooldownTicks(160);
        d.getStatus().useBenihimePath();
    }

    private static boolean cooldownsIntact(PlayerData d) {
        var s = d.getStatus();
        return s.getTechniqueSlot1CooldownTicks() == 100 && s.getTechniqueSlot2CooldownTicks() == 120
                && s.getTechniqueSlot3CooldownTicks() == 140 && s.getTechniqueSlot4CooldownTicks() == 160
                && s.getBenihimePathCharges() == 2;
    }

    @GameTest(template = "empty")
    public static void selectingTargetKeepsCooldownsAndDropsOnlyCharge(GameTestHelper h) {
        var p = player(h); var d = data(p);
        spendCooldowns(d);
        d.getResources().setActionCharge(40); d.getStatus().setActionCharging(true);
        h.assertTrue(SelectFormC2S.apply(p, d, "zanpakuto", "bankai"), "Valid target refused");
        h.assertTrue(cooldownsIntact(d), "Selecting a target refunded cooldowns or Benihime uses");
        h.assertTrue(d.getResources().getActionCharge() == 0 && !d.getStatus().isActionCharging(), "Charge toward the old target survived");
        h.assertTrue("bankai".equals(d.getCharacter().getSelectedForm()) && "sealed".equals(d.getCharacter().getActiveForm()),
                "Selection changed the active form or was not stored");
        h.succeed();
    }

    @GameTest(template = "empty")
    public static void reselectingSameTargetChangesNothing(GameTestHelper h) {
        var p = player(h); var d = data(p);
        h.assertTrue(SelectFormC2S.apply(p, d, "zanpakuto", "shikai"), "First selection refused");
        spendCooldowns(d); d.getResources().setActionCharge(40); d.getStatus().setActionCharging(true);
        h.assertTrue(!SelectFormC2S.apply(p, d, "zanpakuto", "SHIKAI"), "Same target treated as a new selection");
        h.assertTrue(cooldownsIntact(d) && d.getResources().getActionCharge() == 40 && d.getStatus().isActionCharging(),
                "Re-selecting the same target had side effects");
        h.succeed();
    }

    @GameTest(template = "empty")
    public static void combatInputDoesNotSwallowSelection(GameTestHelper h) {
        var p = player(h); var d = data(p); long tick = h.getLevel().getGameTime();
        h.assertTrue(d.getStatus().allowAction(tick), "Combat action refused");
        h.assertTrue(SelectFormC2S.apply(p, d, "zanpakuto", "shikai"), "Selection blocked by the combat limiter");
        h.assertTrue(!SelectFormC2S.apply(p, d, "zanpakuto", "bankai") && "shikai".equals(d.getCharacter().getSelectedForm()),
                "Menu flood not rate limited");
        h.assertTrue(!d.getStatus().allowAction(tick), "Combat limiter stopped guarding combat");
        h.succeed();
    }

    @GameTest(template = "empty")
    public static void invalidSelectionChangesNothing(GameTestHelper h) {
        var p = player(h); var d = data(p); spendCooldowns(d);
        h.assertTrue(!SelectFormC2S.apply(p, d, "zanpakuto", "nonexistent"), "Unknown form accepted");
        h.assertTrue(cooldownsIntact(d) && "sealed".equals(d.getCharacter().getSelectedForm()), "Rejected selection had side effects");
        h.succeed();
    }

    @GameTest(template = "empty")
    public static void dimensionChangeKeepsCooldowns(GameTestHelper h) {
        var p = player(h); var d = data(p); spendCooldowns(d);
        d.getResources().setActionCharge(40); d.getStatus().setActionCharging(true); d.getStatus().refreshCombat(200);
        d.resetLevelBoundState();
        h.assertTrue(cooldownsIntact(d), "Changing dimension refunded cooldowns");
        h.assertTrue(!d.getStatus().isInCombat(), "Travel kept the player in combat");
        h.assertTrue(d.getResources().getActionCharge() == 0 && !d.getStatus().isActionCharging(), "Level-bound charge survived travel");
        h.succeed();
    }

    @GameTest(template = "empty")
    public static void targetFormReportsPendingRelease(GameTestHelper h) {
        var c = data(player(h)).getCharacter();
        h.assertTrue(!c.hasPendingTarget() && "sealed".equals(c.getTargetForm()), "Fresh character reports a pending target");
        c.setSelectedForm("zanpakuto", "bankai");
        h.assertTrue(c.hasPendingTarget() && "bankai".equals(c.getTargetForm()), "Chosen Bankai not reported as the target");
        c.setActiveForm("zanpakuto", "bankai");
        h.assertTrue(!c.hasPendingTarget(), "Completed release still pending");
        c.setSelectedForm("", "");
        h.assertTrue("bankai".equals(c.getTargetForm()), "Empty selection did not fall back to the active form");
        h.succeed();
    }
}
