package com.bleachmod.gametest;

import com.bleachmod.common.CombatStateService;
import com.bleachmod.common.data.*;
import com.mojang.authlib.GameProfile;
import net.minecraft.gametest.framework.*;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.gametest.*;
import java.util.UUID;

@GameTestHolder("bleachmod") @PrefixGameTestTemplate(false)
public final class CombatStateGameTests {
    private static FakePlayer player(GameTestHelper h) {
        var p = new FakePlayer(h.getLevel(), new GameProfile(UUID.randomUUID(), "CombatStateTest"));
        PlayerCapability.get(p).orElseThrow(() -> new IllegalStateException("Missing capability")).initializeShinigami(); return p;
    }
    private static StatusData status(FakePlayer p) { return PlayerCapability.get(p).orElseThrow(() -> new IllegalStateException("Missing capability")).getStatus(); }
    @GameTest(template = "empty")
    public static void acceptedDamageMarksBothPlayersAndIgnoresInvalidEvents(GameTestHelper h) {
        var p = player(h); var q = player(h); var source = p.damageSources().playerAttack(p);
        var event = new LivingDamageEvent(q, source, 1);
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event);
        h.assertTrue(status(p).isInCombat() && status(q).isInCombat(), "PvP did not mark both participants");
        status(p).clearCombat(); status(q).clearCombat();
        CombatStateService.damage(new LivingDamageEvent(q, source, 0));
        var canceled = new LivingDamageEvent(q, source, 1); canceled.setCanceled(true); CombatStateService.damage(canceled);
        CombatStateService.damage(new LivingDamageEvent(q, source, Float.NaN));
        CombatStateService.damage(new LivingDamageEvent(p, source, 1));
        CombatStateService.damage(new LivingDamageEvent(q, q.damageSources().fall(), 1));
        h.assertTrue(!status(p).isInCombat() && !status(q).isInCombat(), "Canceled/zero/self/environment damage activated combat");
        var scoreboard = h.getLevel().getScoreboard(); var team = scoreboard.addPlayerTeam("cs" + UUID.randomUUID().toString().substring(0, 8));
        scoreboard.addPlayerToTeam(p.getScoreboardName(), team); scoreboard.addPlayerToTeam(q.getScoreboardName(), team);
        try {
            CombatStateService.damage(new LivingDamageEvent(q, source, 1));
            h.assertTrue(!status(p).isInCombat() && !status(q).isInCombat(), "Allied event activated combat");
        } finally { scoreboard.removePlayerTeam(team); }
        var mob = EntityType.ZOMBIE.create(h.getLevel());
        CombatStateService.damage(new LivingDamageEvent(p, p.damageSources().mobAttack(mob), 1));
        h.assertTrue(status(p).isInCombat(), "Mob attack did not activate combat"); h.succeed();
    }
    @GameTest(template = "empty")
    public static void combatClearsOnLifecycleWithoutMutatingCharacter(GameTestHelper h) {
        var p = player(h); var data = PlayerCapability.get(p).orElseThrow(() -> new IllegalStateException("Missing capability")); var before = data.save();
        status(p).refreshCombat(200);
        CombatStateService.death(new LivingDeathEvent(p, p.damageSources().generic())); h.assertTrue(!status(p).isInCombat(), "Death retained combat");
        status(p).refreshCombat(200);
        com.bleachmod.server.events.CapabilityEvents.ForgeBus.changedDimension(new PlayerEvent.PlayerChangedDimensionEvent(p, net.minecraft.world.level.Level.OVERWORLD, net.minecraft.world.level.Level.NETHER));
        h.assertTrue(!status(p).isInCombat(), "Dimension retained combat");
        status(p).refreshCombat(200); com.bleachmod.server.events.CapabilityEvents.ForgeBus.logout(new PlayerEvent.PlayerLoggedOutEvent(p));
        h.assertTrue(!status(p).isInCombat() && data.save().equals(before), "Logout changed character or retained combat");
        status(p).refreshCombat(200); com.bleachmod.server.events.TickHandler.onLoginResetCharge(new PlayerEvent.PlayerLoggedInEvent(p));
        h.assertTrue(!status(p).isInCombat(), "Login retained combat"); h.succeed();
    }
}
