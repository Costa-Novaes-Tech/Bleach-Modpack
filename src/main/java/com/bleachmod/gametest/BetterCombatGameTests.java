package com.bleachmod.gametest;

import com.bleachmod.common.compat.BetterCombatCompat;
import com.bleachmod.common.data.*;
import com.bleachmod.init.ModItems;
import com.mojang.authlib.GameProfile;
import net.bettercombat.logic.*;
import net.minecraft.gametest.framework.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.*;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.gametest.*;
import java.util.UUID;

@GameTestHolder("bleachmod") @PrefixGameTestTemplate(false)
public final class BetterCombatGameTests {
    private static PlayerData data(FakePlayer p){return PlayerCapability.get(p).orElseThrow(()->new IllegalStateException("Missing capability"));}
    private static FakePlayer player(GameTestHelper h){var p=new FakePlayer(h.getLevel(),new GameProfile(UUID.randomUUID(),"CombatPilot"));
        var pos=h.absoluteVec(new Vec3(2.5,2,2.5));p.setPos(pos.x,h.getLevel().getMaxBuildHeight()-55,pos.z);p.setYRot(0);p.setXRot(0);p.setOnGround(true);p.setNoGravity(true);
        h.getLevel().getChunkAt(p.blockPosition());data(p).initializeShinigami();p.setItemSlot(EquipmentSlot.MAINHAND,new ItemStack(ModItems.ASAUCHI.get()));return p;}
    private static Zombie target(GameTestHelper h,FakePlayer p,double x,double z){var t=EntityType.ZOMBIE.create(h.getLevel());t.setPos(p.position().add(x,0,z));t.setNoAi(true);t.setNoGravity(true);t.setItemSlot(EquipmentSlot.HEAD,new ItemStack(Items.IRON_HELMET));h.getLevel().addFreshEntity(t);return t;}
    private static void begin(FakePlayer p,int combo){((PlayerAttackProperties)p).setComboCount(combo);}
    private static void end(FakePlayer p){((PlayerAttackProperties)p).setComboCount(-1);}
    @GameTest(template="empty",batch="bc_registry")
    public static void pilotProfileLoadsWithoutEnablingOtherKitsOrDualWield(GameTestHelper h){var p=player(h);
        var profile=WeaponRegistry.getAttributes(p.getMainHandItem());h.assertTrue(profile!=null && profile.attacks().length==3 && profile.attackRange()==3,"Pilot profile missing");
        for(var attack:profile.attacks())h.assertTrue(attack.damageMultiplier()==1,"Pilot unexpectedly changes damage multiplier");
        for(var item:new net.minecraft.world.item.Item[]{ModItems.SENBONZAKURA.get(),ModItems.BENIHIME.get()})
            h.assertTrue(WeaponRegistry.getAttributes(new ItemStack(item))==null,"Automatic fallback enabled an unreviewed kit");
        p.setItemSlot(EquipmentSlot.OFFHAND,new ItemStack(Items.DIAMOND_SWORD));h.assertTrue(!PlayerAttackHelper.isDualWielding(p),"Pilot enabled unapproved dual wield");h.succeed();}
    @GameTest(template="empty",batch="bc_scope")
    public static void injectedRequestScopeChargesOnceForMultipleTargets(GameTestHelper h){var p=player(h);var a=target(h,p,-.5,2);var b=target(h,p,.5,2);
        try {begin(p,0);h.assertTrue(BetterCombatCompat.insideSwing(p),"BC injection did not open request scope");p.attack(a);p.attack(b);
            h.assertTrue(data(p).getResources().getStamina()==96,"Multi-target request charged more than four stamina");
            float hp=a.getHealth();a.invulnerableTime=0;p.attack(a);h.assertTrue(a.getHealth()==hp,"Repeated target in one request took damage twice");
            end(p);h.assertTrue(!BetterCombatCompat.insideSwing(p),"BC request scope leaked");begin(p,1);p.attack(b);
            h.assertTrue(data(p).getResources().getStamina()==92,"Next combo strike did not have its own cost");
        } finally {end(p);a.discard();b.discard();}h.succeed();}
    @GameTest(template="empty",batch="bc_state")
    public static void guardDodgeAndChargingRejectPhysicalHits(GameTestHelper h){var p=player(h);var t=target(h,p,0,2);var d=data(p);float hp=t.getHealth();
        try {d.getGuard().request(h.getLevel().getGameTime());begin(p,0);p.attack(t);end(p);d.getGuard().release();
            d.getStatus().beginDodge(new Vec3(0,0,1),h.getLevel().getGameTime());begin(p,1);p.attack(t);end(p);d.getStatus().stopDodge();
            d.getStatus().setActionCharging(true);begin(p,2);p.attack(t);end(p);
            h.assertTrue(t.getHealth()==hp && d.getResources().getStamina()==100,"A blocked combat state permitted damage or charged stamina");
        } finally {d.getStatus().setActionCharging(false);end(p);t.discard();}h.succeed();}
    @GameTest(template="empty",batch="bc_reach")
    public static void remoteAndRearTargetsAreRejectedByServer(GameTestHelper h){var p=player(h);var far=target(h,p,0,6);var rear=target(h,p,0,-2);
        try {begin(p,0);p.attack(far);p.attack(rear);h.assertTrue(data(p).getResources().getStamina()==100 && far.getHealth()==20 && rear.getHealth()==20,"Remote/rear request was accepted");}
        finally{end(p);far.discard();rear.discard();}h.succeed();}
    @GameTest(template="empty",batch="bc_late_state")
    public static void stateChangeAfterAttackStartStillCancelsHit(GameTestHelper h){var p=player(h);var t=target(h,p,0,2);
        try {begin(p,0);data(p).getStatus().setActionCharging(true);p.attack(t);h.assertTrue(t.getHealth()==20 && data(p).getResources().getStamina()==100,"Late state change bypassed hit validation");}
        finally{data(p).getStatus().setActionCharging(false);end(p);t.discard();}h.succeed();}
}
