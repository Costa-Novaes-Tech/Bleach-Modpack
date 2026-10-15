package com.bleachmod.gametest;

import com.bleachmod.common.*;
import com.bleachmod.common.data.*;
import com.bleachmod.init.ModItems;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.gametest.*;
import java.util.UUID;

@GameTestHolder("bleachmod") @PrefixGameTestTemplate(false)
public final class GuardGameTests {
    private static FakePlayer player(GameTestHelper h) {
        var p = new FakePlayer(h.getLevel(), new GameProfile(UUID.randomUUID(), "GuardTest"));
        p.setPos(h.absoluteVec(new net.minecraft.world.phys.Vec3(2.5, 2, 2.5))); p.setYRot(0); p.setOnGround(true);
        data(p).initializeShinigami();
        return p;
    }
    private static PlayerData data(FakePlayer p) {
        return PlayerCapability.get(p).orElseThrow(() -> new IllegalStateException("Missing capability"));
    }
    @GameTest(template = "empty")
    public static void frontalSwordGuardReducesDamageButBackAndMagicDoNot(GameTestHelper h) {
        var p = player(h); var d = data(p);
        p.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(ModItems.ASAUCHI.get()));
        var z = h.spawn(EntityType.ZOMBIE, new BlockPos(2, 2, 4)); z.setNoAi(true);
        z.setPos(p.position().add(0,0,2)); GuardService.request(p, d, true);
        var front = new LivingHurtEvent(p, p.damageSources().mobAttack(z), 10); MinecraftForge.EVENT_BUS.post(front);
        h.assertTrue(Math.abs(front.getAmount() - 3.5F) < .001 && d.getGuard().posture() == 60, "Frontal sword guard incorrect");
        z.setPos(p.position().add(0,0,-2));
        var back = new LivingHurtEvent(p, p.damageSources().mobAttack(z), 10); MinecraftForge.EVENT_BUS.post(back);
        var magic = new LivingHurtEvent(p, p.damageSources().magic(), 10); MinecraftForge.EVENT_BUS.post(magic);
        h.assertTrue(back.getAmount() == 10 && magic.getAmount() == 10 && d.getGuard().posture() == 60, "Back/magic blocked");
        h.assertTrue(d.getResources().getStamina() == 100, "Guard consumed stamina"); h.succeed();
    }
    @GameTest(template = "empty")
    public static void unarmedGuardAndProjectileExclusion(GameTestHelper h) {
        var p = player(h); var d = data(p);
        var z = h.spawn(EntityType.ZOMBIE, new BlockPos(2, 2, 4)); z.setNoAi(true); z.setPos(p.position().add(0,0,2));
        GuardService.request(p, d, true);
        var hit = new LivingHurtEvent(p, p.damageSources().mobAttack(z), 10); MinecraftForge.EVENT_BUS.post(hit);
        h.assertTrue(Math.abs(hit.getAmount() - 6.5F) < .001, "Unarmed reduction incorrect");
        var arrow = new net.minecraft.world.entity.projectile.Arrow(h.getLevel(), z);
        var shot = new LivingHurtEvent(p, p.damageSources().arrow(arrow, z), 10); MinecraftForge.EVENT_BUS.post(shot);
        h.assertTrue(shot.getAmount() == 10 && d.getGuard().posture() == 60, "Projectile consumed guard"); h.succeed();
    }
    @GameTest(template = "empty")
    public static void guardBreakStopsDefenseAndPreventsDodgeWithoutFullStun(GameTestHelper h) {
        var p = player(h); var d = data(p);
        var z = h.spawn(EntityType.ZOMBIE, new BlockPos(2, 2, 4)); z.setNoAi(true); z.setPos(p.position().add(0,0,2));
        GuardService.request(p, d, true);
        var attack = new AttackEntityEvent(p, z); MinecraftForge.EVENT_BUS.post(attack);
        h.assertTrue(attack.isCanceled(), "Attacked while holding guard");
        for (int i = 0; i < 2; i++) MinecraftForge.EVENT_BUS.post(new LivingHurtEvent(p, p.damageSources().mobAttack(z), 20));
        h.assertTrue(d.getGuard().broken() && !d.getGuard().active(), "Guard did not break");
        h.assertTrue(!DodgeService.request(p, d, 0, 0) && d.getResources().getStamina() == 100, "Dodge bypassed break");
        var exposed = new LivingHurtEvent(p, p.damageSources().mobAttack(z), 10); MinecraftForge.EVENT_BUS.post(exposed);
        h.assertTrue(exposed.getAmount() == 10, "Broken guard still mitigated");
        var reply = new AttackEntityEvent(p, z); MinecraftForge.EVENT_BUS.post(reply);
        h.assertTrue(!reply.isCanceled(), "Break caused full attack stun"); h.succeed();
    }
    @GameTest(template = "empty")
    public static void reconnectAndDimensionPreservePostureButReleaseGuard(GameTestHelper h) {
        var p = player(h); var d = data(p); GuardService.request(p, d, true); d.getGuard().absorb(40);
        com.bleachmod.server.events.CapabilityEvents.ForgeBus.logout(new PlayerEvent.PlayerLoggedOutEvent(p));
        com.bleachmod.server.events.TickHandler.onLoginResetCharge(new PlayerEvent.PlayerLoggedInEvent(p));
        h.assertTrue(!d.getGuard().active() && d.getGuard().posture() == 60, "Reconnect restored pool/input");
        var copy = new PlayerData(); copy.load(d.save());
        h.assertTrue(copy.getGuard().posture() == 60 && !copy.getGuard().active(), "Save lost posture");
        com.bleachmod.server.events.CapabilityEvents.ForgeBus.changedDimension(new PlayerEvent.PlayerChangedDimensionEvent(
                p, net.minecraft.world.level.Level.OVERWORLD, net.minecraft.world.level.Level.NETHER));
        h.assertTrue(d.getGuard().posture() == 60, "Dimension refilled posture");
        com.bleachmod.server.events.CapabilityEvents.ForgeBus.respawn(new PlayerEvent.PlayerRespawnEvent(p, false));
        h.assertTrue(d.getGuard().posture() == 100, "Respawn did not restore posture"); h.succeed();
    }
}
