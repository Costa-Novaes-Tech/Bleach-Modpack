package com.bleachmod.gametest;

import com.bleachmod.common.DodgeService;
import com.bleachmod.common.StaminaService;
import com.bleachmod.common.data.PlayerCapability;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.gametest.*;
import java.util.UUID;

@GameTestHolder("bleachmod") @PrefixGameTestTemplate(false)
public final class DodgeGameTests {
    private static FakePlayer player(GameTestHelper h) {
        for (int x = 1; x <= 6; x++) for (int z = 1; z <= 6; z++) {
            h.setBlock(new BlockPos(x, 1, z), Blocks.STONE);
            for (int y = 2; y <= 4; y++) h.setBlock(new BlockPos(x, y, z), Blocks.AIR);
        }
        var p = new FakePlayer(h.getLevel(), new GameProfile(UUID.randomUUID(), "DodgeTest"));
        var position = h.absoluteVec(new Vec3(2.5, 2, 2.5));
        p.setPos(position); p.setYRot(-90); p.setOnGround(true);
        PlayerCapability.get(p).orElseThrow(() -> new IllegalStateException("Missing capability")).initializeShinigami();
        return p;
    }

    @GameTest(template = "empty")
    public static void dodgeMovesFiveStepsAndChargesOnce(GameTestHelper h) {
        var p = player(h); var d = PlayerCapability.get(p).orElseThrow(() -> new IllegalStateException("Missing capability"));
        double start = p.getX(); float reiatsu = d.getResources().getCurrentReiatsu();
        h.assertTrue(DodgeService.request(p, d, 1, 0), "Grounded dodge refused");
        h.assertTrue(!DodgeService.request(p, d, 1, 0), "Repeated request accepted");
        for (int i = 0; i < 5; i++) {
            p.setSprinting(true); StaminaService.tick(p, d); DodgeService.tick(p, d);
            h.assertTrue(!p.isSprinting(), "Sprint stacked on dodge");
            p.move(MoverType.SELF, p.getDeltaMovement()); p.setOnGround(true);
        }
        DodgeService.tick(p, d);
        h.assertTrue(Math.abs(p.getX() - start - 2.5) < .001, "Nominal distance incorrect");
        h.assertTrue(p.getDeltaMovement().horizontalDistanceSqr() == 0 && !d.getStatus().isDodging(), "Movement leaked");
        h.assertTrue(d.getResources().getStamina() == 85 && d.getResources().getCurrentReiatsu() == reiatsu,
                "Wrong cost or duplicate sprint charge");
        h.succeed();
    }

    @GameTest(template = "empty")
    public static void wallRejectsWithoutChargeAndStopsAcceptedDodge(GameTestHelper h) {
        var p = player(h); var d = PlayerCapability.get(p).orElseThrow(() -> new IllegalStateException("Missing capability"));
        h.setBlock(new BlockPos(3, 2, 2), Blocks.STONE);
        h.assertTrue(!DodgeService.request(p, d, 1, 0) && d.getResources().getStamina() == 100,
                "Blocked activation charged");
        h.setBlock(new BlockPos(3, 2, 2), Blocks.AIR);
        h.assertTrue(DodgeService.request(p, d, 1, 0), "Clear path refused");
        h.setBlock(new BlockPos(3, 2, 2), Blocks.STONE);
        double x = p.getX(); DodgeService.tick(p, d);
        h.assertTrue(!d.getStatus().isDodging() && p.getX() == x && p.getDeltaMovement().x == 0,
                "Wall did not stop motion");
        h.assertTrue(d.getResources().getStamina() == 85, "Accepted action refunded/charged twice");
        h.succeed();
    }

    @GameTest(template = "empty")
    public static void invalidStatesAndInsufficientStaminaRefuse(GameTestHelper h) {
        var p = player(h); var d = PlayerCapability.get(p).orElseThrow(() -> new IllegalStateException("Missing capability"));
        h.assertTrue(!DodgeService.request(p, d, 127, 0), "Malformed input accepted");
        p.setOnGround(false); h.assertTrue(!DodgeService.request(p, d, 1, 0), "Air dodge accepted"); p.setOnGround(true);
        p.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100));
        h.assertTrue(!DodgeService.request(p, d, 1, 0), "Slow bypassed"); p.removeAllEffects();
        d.getStatus().setActionCharging(true);
        h.assertTrue(!DodgeService.request(p, d, 1, 0), "Charge cancelled by dodge"); d.getStatus().setActionCharging(false);
        d.getStatus().setFlameDashTicks(5);
        h.assertTrue(!DodgeService.request(p, d, 1, 0), "Flame dash overlapped"); d.getStatus().setFlameDashTicks(0);
        h.assertTrue(d.getResources().getStamina() == 100, "Invalid request charged");
        for (int i = 0; i < 23; i++) d.getResources().spendBasicAttackStamina();
        h.assertTrue(!DodgeService.request(p, d, 1, 0) && d.getResources().getStamina() == 8, "Partial cost allowed");
        h.succeed();
    }

    @GameTest(template = "empty")
    public static void lifecycleClearsDodgeWithoutRefillingStamina(GameTestHelper h) {
        var p = player(h); var d = PlayerCapability.get(p).orElseThrow(() -> new IllegalStateException("Missing capability"));
        h.assertTrue(DodgeService.request(p, d, 0, 0), "Backstep refused");
        com.bleachmod.server.events.CapabilityEvents.ForgeBus.logout(new PlayerEvent.PlayerLoggedOutEvent(p));
        com.bleachmod.server.events.TickHandler.onLoginResetCharge(new PlayerEvent.PlayerLoggedInEvent(p));
        h.assertTrue(!d.getStatus().isDodging() && d.getResources().getStamina() == 85, "Reconnect leaked/refilled");
        h.assertTrue(DodgeService.request(p, d, 0, 1), "Second action refused");
        d.resetTransientState();
        h.assertTrue(!d.getStatus().isDodging() && d.getResources().getStamina() == 70, "Dimension reset leaked/refilled");
        h.succeed();
    }
}
