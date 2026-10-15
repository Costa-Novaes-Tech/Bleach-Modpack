package com.bleachmod.gametest;

import com.bleachmod.common.data.*;
import com.bleachmod.common.technique.*;
import com.bleachmod.init.ModItems;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.*;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.gametest.*;
import java.util.*;

@GameTestHolder("bleachmod") @PrefixGameTestTemplate(false)
public final class SenbonGameTests {
    private static PlayerData data(FakePlayer p) { return PlayerCapability.get(p).orElseThrow(()->new IllegalStateException("Missing capability")); }
    private static FakePlayer player(GameTestHelper h,String form) {
        var p=new FakePlayer(h.getLevel(),new GameProfile(UUID.randomUUID(),"SenbonTest"));
        // Each scenario has its own batch: area attacks must not touch concurrent fixtures.
        Vec3 pos=h.absoluteVec(new Vec3(2.5,2,2.5)); p.setPos(pos.x,h.getLevel().getMaxBuildHeight()-30,pos.z);
        p.setYRot(0);p.setXRot(0);p.setOnGround(true);
        for(int x=-1;x<=1;x++) for(int z=-1;z<=1;z++) h.getLevel().getChunkAt(p.blockPosition().offset(x*16,0,z*16));
        var d=data(p);d.initializeShinigami();d.getCharacter().bindZanpakuto(CharacterData.SENBONZAKURA);
        d.getSkills().get("zanpakuto").setLevel(2);d.getCharacter().unlockForm("shikai");d.getCharacter().unlockForm("bankai");
        d.getCharacter().setMastery("zanpakuto","shikai",100);d.getCharacter().setMastery("zanpakuto","bankai",100);
        d.getCharacter().setActiveForm("zanpakuto",form);
        p.setItemSlot(EquipmentSlot.MAINHAND,new ItemStack(ModItems.SENBONZAKURA.get()));
        return p;
    }
    private static Zombie target(GameTestHelper h,FakePlayer p,double x,double z) {
        var t=EntityType.ZOMBIE.create(h.getLevel());t.setPos(p.position().add(x,0,z));t.setNoAi(true);t.setNoGravity(true);
        t.setItemSlot(EquipmentSlot.HEAD,new ItemStack(Items.IRON_HELMET));
        t.getAttribute(Attributes.MAX_HEALTH).setBaseValue(1000);t.setHealth(1000);h.getLevel().addFreshEntity(t);return t;
    }
    private static void ticks(GameTestHelper h,FakePlayer p) {
        h.onEachTick(()->{p.tickCount++;data(p).getStatus().tickTransientState();SenbonzakuraTechniqueService.tick(p,data(p));});
    }
    private static void done(GameTestHelper h,FakePlayer p,LivingEntity... targets) {
        SenbonzakuraTechniqueService.cancel(p);for(var t:targets)t.discard();h.succeed();
    }
    @GameTest(template="empty",batch="senbon_1")
    public static void sealedAndUntrainedSlotsCannotSpendEnergy(GameTestHelper h) {
        var p=player(h,"sealed");var d=data(p);float before=d.getResources().getCurrentReiatsu();
        h.assertTrue(!SenbonzakuraTechniqueService.executeSlot(p,d,1),"Sealed cast accepted");
        d.getCharacter().setActiveForm("zanpakuto","shikai");d.getCharacter().setMastery("zanpakuto","shikai",0);
        h.assertTrue(!SenbonzakuraTechniqueService.executeSlot(p,d,4),"Locked slot accepted");
        h.assertTrue(d.getResources().getCurrentReiatsu()==before,"Rejected cast charged energy");done(h,p);
    }
    @GameTest(template="empty",batch="senbon_2",timeoutTicks=90)
    public static void fiveCutsReachForwardButNeverHitBehind(GameTestHelper h) {
        var p=player(h,"bankai");var front=target(h,p,0,3);var back=target(h,p,0,-3);ticks(h,p);
        h.assertTrue(SenbonzakuraTechniqueService.executeSlot(p,data(p),1),"Combo refused");
        final int[] hits={0};final float[] previous={1000};
        h.onEachTick(()->{if(front.getHealth()<previous[0])hits[0]++;previous[0]=front.getHealth();});
        h.runAfterDelay(60,()->{h.assertTrue(hits[0]==5,"Expected five distinct hits, got "+hits[0]);
            h.assertTrue(back.getHealth()==1000,"Combo hit behind owner");
            h.assertTrue(!SenbonzakuraTechniqueService.busy(p),"Combo did not finish");done(h,p,front,back);});
    }
    @GameTest(template="empty",batch="senbon_3",timeoutTicks=170)
    public static void fixedFieldReactivatesOnceAfterFiveSeconds(GameTestHelper h) {
        var p=player(h,"bankai");var d=data(p);var original=target(h,p,-8,0);var outside=target(h,p,12,0);ticks(h,p);
        float energy=d.getResources().getCurrentReiatsu();
        h.assertTrue(SenbonzakuraTechniqueService.executeSlot(p,d,2),"Field refused");
        h.assertTrue(!SenbonzakuraTechniqueService.executeSlot(p,d,2),"Early bloom accepted");
        h.runAfterDelay(25,()->{h.assertTrue(original.getHealth()<1000,"Initial burst missing");
            h.assertTrue(outside.getHealth()==1000,"Initial burst exceeded radius");p.setPos(p.position().add(8,0,0));});
        final float[] opening={0};
        h.runAfterDelay(101,()->{opening[0]=original.getHealth();
            h.assertTrue(SenbonzakuraTechniqueService.executeSlot(p,d,2),"Valid bloom refused despite cooldown");
            h.assertTrue(!SenbonzakuraTechniqueService.executeSlot(p,d,2),"Duplicate bloom accepted");
            h.assertTrue(d.getResources().getCurrentReiatsu()==energy-35,"Bloom charged extra reiatsu");});
        h.runAfterDelay(122,()->{h.assertTrue(original.getHealth()<opening[0],"Bloom followed owner instead of original center");
            h.assertTrue(outside.getHealth()==1000,"Bloom used moving center");
            h.assertTrue(!SenbonzakuraTechniqueService.hud(p).contains("fieldRemaining"),"Finished field retained continuation");
            done(h,p,original,outside);});
    }
    @GameTest(template="empty",batch="senbon_4",timeoutTicks=240)
    public static void fieldOutsideAndExpiredContinuationsAreRejected(GameTestHelper h) {
        var p=player(h,"bankai");var d=data(p);Vec3 origin=p.position();ticks(h,p);
        SenbonzakuraTechniqueService.executeSlot(p,d,2);
        h.runAfterDelay(101,()->{p.setPos(origin.add(11,0,0));h.assertTrue(!SenbonzakuraTechniqueService.executeSlot(p,d,2),"Bloom outside field accepted");p.setPos(origin);});
        h.runAfterDelay(202,()->{h.assertTrue(!SenbonzakuraTechniqueService.hud(p).contains("fieldRemaining"),"Field did not expire");
            h.assertTrue(!SenbonzakuraTechniqueService.executeSlot(p,d,2),"Expired field bypassed cooldown");done(h,p);});
    }
    @GameTest(template="empty",batch="senbon_5",timeoutTicks=70)
    public static void senkeiContinuesIntoIkkaWithoutExtraCost(GameTestHelper h) {
        var p=player(h,"bankai");var d=data(p);var t=target(h,p,0,6);ticks(h,p);float before=d.getResources().getCurrentReiatsu();
        h.assertTrue(SenbonzakuraTechniqueService.executeSlot(p,d,3),"Senkei refused");
        h.assertTrue(SenbonzakuraTechniqueService.offensiveBonus(p)>.2,"Senkei bonus missing");
        h.assertTrue(!SenbonzakuraTechniqueService.executeSlot(p,d,3),"Early Ikka accepted");
        h.runAfterDelay(21,()->{h.assertTrue(SenbonzakuraTechniqueService.executeSlot(p,d,3),"Ikka refused");
            h.assertTrue(!SenbonzakuraTechniqueService.scene(p),"Ikka retained Senkei");
            h.assertTrue(d.getResources().getCurrentReiatsu()==before-25,"Ikka charged twice");});
        h.runAfterDelay(39,()->{h.assertTrue(t.getHealth()<1000,"Ikka did not hit");
            h.assertTrue(!SenbonzakuraTechniqueService.executeSlot(p,d,3),"Ikka bypassed cooldown twice");done(h,p,t);});
    }
    @GameTest(template="empty",batch="senbon_6",timeoutTicks=60)
    public static void hakuteikenChargesThenHitsAndRestoresBlade(GameTestHelper h) {
        var p=player(h,"bankai");var t=target(h,p,0,7);ticks(h,p);
        SenbonzakuraTechniqueService.executeSlot(p,data(p),4);
        h.runAfterDelay(5,()->{h.assertTrue(t.getHealth()==1000,"Hakuteiken skipped windup");
            h.assertTrue(data(p).getCharacter().getSenbonBlade()==2,"White blade appearance missing");});
        h.runAfterDelay(22,()->{h.assertTrue(t.getHealth()<1000,"Hakuteiken missed forward target");
            h.assertTrue(data(p).getCharacter().getSenbonBlade()==0,"White blade did not expire");done(h,p,t);});
    }
    @GameTest(template="empty",batch="senbon_7")
    public static void reachIsTransientUniqueAndRemovedWhenUnequipped(GameTestHelper h) {
        var p=player(h,"bankai");var d=data(p);var a=p.getAttribute(ForgeMod.ENTITY_REACH.get());double before=a.getValue();
        SenbonzakuraTechniqueService.tick(p,d);SenbonzakuraTechniqueService.tick(p,d);
        h.assertTrue(Math.abs(a.getValue()-before-1.5)<.001,"Reach duplicated or missing");
        p.setItemSlot(EquipmentSlot.MAINHAND,ItemStack.EMPTY);SenbonzakuraTechniqueService.tick(p,d);
        h.assertTrue(a.getValue()==before && a.getModifier(SenbonzakuraTechniqueService.REACH_ID)==null,"Unequipping retained reach");done(h,p);
    }
    @GameTest(template="empty",batch="senbon_8")
    public static void petalWardIsFrontalAndCannotStackWithManualGuard(GameTestHelper h) {
        var p=player(h,"shikai");var d=data(p);var t=target(h,p,0,2);
        h.assertTrue(SenbonzakuraTechniqueService.executeSlot(p,d,4),"Ward refused");
        com.bleachmod.common.GuardService.request(p,d,true);
        h.assertTrue(!d.getGuard().active(),"Ward stacked active manual guard");
        h.assertTrue(SenbonzakuraTechniqueService.protection(p,p.damageSources().mobAttack(t))==.45F,"Frontal ward missing");
        t.setPos(p.position().add(0,0,-2));
        h.assertTrue(SenbonzakuraTechniqueService.protection(p,p.damageSources().mobAttack(t))==0,"Ward blocked behind");
        h.assertTrue(SenbonzakuraTechniqueService.protection(p,p.damageSources().magic())==0,"Ward blocked magic");
        done(h,p,t);
    }
    @GameTest(template="empty",batch="senbon_9",timeoutTicks=60)
    public static void streamAndConvergenceDealDamage(GameTestHelper h) {
        var p=player(h,"shikai");var d=data(p);var stream=target(h,p,0,5);var converge=target(h,p,0,9);ticks(h,p);
        final float[] before={1000};
        h.assertTrue(SenbonzakuraTechniqueService.executeSlot(p,d,2),"Stream refused");
        h.runAfterDelay(28,()->{h.assertTrue(stream.getHealth()<1000,"Stream missed");
            before[0]=converge.getHealth();
            h.assertTrue(SenbonzakuraTechniqueService.executeSlot(p,d,3),"Convergence refused");});
        h.runAfterDelay(44,()->{h.assertTrue(converge.getHealth()<before[0],"Convergence missed");done(h,p,stream,converge);});
    }
    @GameTest(template="empty",batch="senbon_10",timeoutTicks=35)
    public static void shikaiCutCrossesRightTargetBeforeLeftTarget(GameTestHelper h) {
        var p=player(h,"shikai");var right=target(h,p,-3,2);var left=target(h,p,3,2);ticks(h,p);
        SenbonzakuraTechniqueService.executeSlot(p,data(p),1);
        final long[] times={-1,-1};
        h.onEachTick(()->{if(right.getHealth()<1000 && times[0]<0)times[0]=h.getTick();
            if(left.getHealth()<1000 && times[1]<0)times[1]=h.getTick();});
        h.runAfterDelay(10,()->{h.assertTrue(times[0]>=0 && times[1]>times[0],"Shikai did not sweep right to left");done(h,p,right,left);});
    }
    @GameTest(template="empty",batch="senbon_11")
    public static void lifecycleCancelRemovesAllLeasesAndReach(GameTestHelper h) {
        var p=player(h,"bankai");var d=data(p);SenbonzakuraTechniqueService.tick(p,d);
        SenbonzakuraTechniqueService.executeSlot(p,d,2);SenbonzakuraTechniqueService.executeSlot(p,d,3);
        com.bleachmod.common.technique.RyujinTechniqueService.cancel(p);
        h.assertTrue(!SenbonzakuraTechniqueService.scene(p) && !SenbonzakuraTechniqueService.busy(p),"Scene survived lifecycle cancellation");
        h.assertTrue(!SenbonzakuraTechniqueService.hud(p).contains("fieldRemaining") && d.getCharacter().getSenbonBlade()==0,"Field/appearance survived cancellation");
        h.assertTrue(p.getAttribute(ForgeMod.ENTITY_REACH.get()).getModifier(SenbonzakuraTechniqueService.REACH_ID)==null,"Reach survived cancellation");done(h,p);
    }
}
