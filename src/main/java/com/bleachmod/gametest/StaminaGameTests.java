package com.bleachmod.gametest;

import com.bleachmod.common.StaminaService;
import com.bleachmod.common.data.PlayerCapability;
import com.mojang.authlib.GameProfile;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;
import java.util.UUID;

@GameTestHolder("bleachmod") @PrefixGameTestTemplate(false)
public final class StaminaGameTests {
    @GameTest(template = "empty")
    public static void movingSprintExhaustsAndLifecyclePreservesPool(GameTestHelper h) {
        var p = new FakePlayer(h.getLevel(), new GameProfile(UUID.randomUUID(), "StaminaTest"));
        var d = PlayerCapability.get(p).orElseThrow(() -> new IllegalStateException("Missing capability")); d.initializeShinigami();
        float energy = d.getResources().getCurrentReiatsu();
        StaminaService.tick(p, d);
        p.setSprinting(true);
        for (int i = 0; i < 400; i++) { p.setPos(p.getX() + .1, p.getY(), p.getZ()); StaminaService.tick(p, d); }
        h.assertTrue(d.getResources().getStamina() == 0 && !p.isSprinting(), "Moving sprint did not exhaust");
        h.assertTrue(d.getResources().getCurrentReiatsu() == energy, "Sprint consumed reiatsu");
        com.bleachmod.server.events.CapabilityEvents.ForgeBus.logout(new PlayerEvent.PlayerLoggedOutEvent(p));
        com.bleachmod.server.events.TickHandler.onLoginResetCharge(new PlayerEvent.PlayerLoggedInEvent(p));
        h.assertTrue(d.getResources().getStamina() == 0, "Reconnect refilled stamina");
        com.bleachmod.server.events.CapabilityEvents.ForgeBus.respawn(new PlayerEvent.PlayerRespawnEvent(p, false));
        h.assertTrue(d.getResources().getStamina() == 100 && !d.getResources().isStaminaExhausted(), "Respawn did not refill");
        h.succeed();
    }
    @GameTest(template = "empty")
    public static void stationaryWalkingAndFlightDoNotDrain(GameTestHelper h) {
        var p = new FakePlayer(h.getLevel(), new GameProfile(UUID.randomUUID(), "StaminaIdle"));
        var d = PlayerCapability.get(p).orElseThrow(() -> new IllegalStateException("Missing capability")); d.initializeShinigami(); StaminaService.tick(p, d);
        p.setSprinting(true); for (int i = 0; i < 30; i++) StaminaService.tick(p, d);
        p.setSprinting(false); p.setPos(p.getX() + 1, p.getY(), p.getZ()); StaminaService.tick(p, d);
        p.setSprinting(true); p.getAbilities().flying = true;
        p.setPos(p.getX() + 1, p.getY(), p.getZ()); StaminaService.tick(p, d);
        h.assertTrue(d.getResources().getStamina() == 100, "Idle/walking/flight consumed stamina"); h.succeed();
    }
}
