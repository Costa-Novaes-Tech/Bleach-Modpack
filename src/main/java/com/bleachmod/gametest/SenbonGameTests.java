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
    public static void sealedHasItsArsenalButUntrainedSlotsCannotSpendEnergy(GameTestHelper h) {
        var p=player(h,"sealed");var d=data(p);float before=d.getResources().getCurrentReiatsu();
        h.assertTrue(SenbonzakuraTechniqueService.executeSlot(p,d,1),"Sealed cut refused");
        h.assertTrue(d.getResources().getCurrentReiatsu()==before-10,"Sealed cut cost incorrect");
        SenbonzakuraTechniqueService.cancel(p);before=d.getResources().getCurrentReiatsu();
        h.assertTrue(!SenbonzakuraTechniqueService.executeSlot(p,d,4),"Untrained sealed guard accepted");
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
    public static void fixedFieldReactivatesOnceAfterTwoSeconds(GameTestHelper h) {
        var p=player(h,"bankai");var d=data(p);loadField(h,p);var original=target(h,p,-30,0);var outside=target(h,p,36,0);ticks(h,p);
        float energy=d.getResources().getCurrentReiatsu();
        h.assertTrue(SenbonzakuraTechniqueService.executeSlot(p,d,2),"Field refused");
        h.assertTrue(!SenbonzakuraTechniqueService.executeSlot(p,d,2),"Early bloom accepted");
        h.runAfterDelay(25,()->{h.assertTrue(original.getHealth()<1000,"Initial burst missing");
            h.assertTrue(outside.getHealth()==1000,"Initial burst exceeded radius");p.setPos(p.position().add(8,0,0));});
        final float[] opening={0};
        h.runAfterDelay(35,()->h.assertTrue(!SenbonzakuraTechniqueService.executeSlot(p,d,2),"Bloom accepted before two seconds"));
        h.runAfterDelay(45,()->{opening[0]=original.getHealth();
            h.assertTrue(SenbonzakuraTechniqueService.executeSlot(p,d,2),"Valid bloom refused despite cooldown");
            h.assertTrue(!SenbonzakuraTechniqueService.executeSlot(p,d,2),"Duplicate bloom accepted");
            h.assertTrue(d.getResources().getCurrentReiatsu()==energy-35,"Bloom charged extra reiatsu");});
        h.runAfterDelay(66,()->{h.assertTrue(original.getHealth()<opening[0],"Bloom followed owner instead of original center");
            h.assertTrue(outside.getHealth()==1000,"Bloom used moving center");
            h.assertTrue(!SenbonzakuraTechniqueService.hud(p).contains("fieldRemaining"),"Finished field retained continuation");
            done(h,p,original,outside);});
    }
    @GameTest(template="empty",batch="senbon_4",timeoutTicks=240)
    public static void fieldOutsideAndExpiredContinuationsAreRejected(GameTestHelper h) {
        var p=player(h,"bankai");var d=data(p);Vec3 origin=p.position();ticks(h,p);
        SenbonzakuraTechniqueService.executeSlot(p,d,2);
        h.runAfterDelay(45,()->{p.setPos(origin.add(36,0,0));h.assertTrue(!SenbonzakuraTechniqueService.executeSlot(p,d,2),"Bloom outside field accepted");p.setPos(origin);});
        h.runAfterDelay(122,()->{h.assertTrue(!SenbonzakuraTechniqueService.hud(p).contains("fieldRemaining"),"Field did not expire at six seconds");
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
        var p=player(h,"bankai");var t=target(h,p,0,11);var beyond=target(h,p,0,17);ticks(h,p);Vec3 start=p.position();
        SenbonzakuraTechniqueService.executeSlot(p,data(p),4);
        h.runAfterDelay(5,()->{h.assertTrue(t.getHealth()==1000,"Hakuteiken skipped windup");
            h.assertTrue(data(p).getCharacter().getSenbonBlade()==2,"White blade appearance missing");});
        h.runAfterDelay(32,()->{h.assertTrue(t.getHealth()<1000,"Hakuteiken missed forward target");
            h.assertTrue(beyond.getHealth()==1000,"Hakuteiken exceeded sixteen-block reach");
            h.assertTrue(Math.abs(p.position().subtract(start).z-16)<.001,"Player did not follow the sixteen-block thrust");
            h.assertTrue(data(p).getCharacter().getSenbonBlade()==0,"White blade did not expire");done(h,p,t,beyond);});
    }
    private static void loadField(GameTestHelper h,FakePlayer p) {
        for(int x=-3;x<=3;x++)for(int z=-3;z<=3;z++)h.getLevel().getChunkAt(p.blockPosition().offset(x*16,0,z*16));
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
    @GameTest(template="empty",batch="senbon_prison",timeoutTicks=110)
    public static void prisonLevitatesAndDealsThreeSeparatePulses(GameTestHelper h) {
        var p=player(h,"shikai");var t=target(h,p,0,11);ticks(h,p);
        final int[] hits={0};final float[] previous={1000};
        SenbonzakuraTechniqueService.executeSlot(p,data(p),3);
        h.onEachTick(()->{if(t.getHealth()<previous[0])hits[0]++;previous[0]=t.getHealth();});
        h.runAfterDelay(8,()->h.assertTrue(t.getHealth()==1000,"Prison dealt an immediate burst"));
        h.runAfterDelay(17,()->{
            h.assertTrue(t.hasEffect(net.minecraft.world.effect.MobEffects.LEVITATION),"Shulker levitation absent");
            h.assertTrue(t.hasEffect(net.minecraft.world.effect.MobEffects.MOVEMENT_SLOWDOWN),"Prison did not restrain movement");
            h.assertTrue(!SenbonzakuraTechniqueService.busy(p),"Prison immobilized caster after windup");
        });
        h.runAfterDelay(85,()->{
            h.assertTrue(hits[0]==3,"Expected three damage pulses, got "+hits[0]);
            h.assertTrue(!t.hasEffect(net.minecraft.world.effect.MobEffects.LEVITATION),"Prison levitation never expired");
            done(h,p,t);
        });
    }
    @GameTest(template="empty",batch="senbon_prison_cancel",timeoutTicks=65)
    public static void prisonCancelExpiresOwnEffectsAndPreservesOtherEffects(GameTestHelper h) {
        var p=player(h,"shikai");var t=target(h,p,-1,11);var external=target(h,p,1,11);ticks(h,p);
        external.addEffect(new net.minecraft.world.effect.MobEffectInstance(net.minecraft.world.effect.MobEffects.LEVITATION,200,2));
        SenbonzakuraTechniqueService.executeSlot(p,data(p),3);
        final float[] health={0};
        h.runAfterDelay(18,()->{health[0]=t.getHealth();SenbonzakuraTechniqueService.cancel(p);p.setItemSlot(EquipmentSlot.MAINHAND,ItemStack.EMPTY);});
        h.runAfterDelay(38,()->{
            h.assertTrue(t.getHealth()==health[0],"Cancelled prison continued dealing damage");
            h.assertTrue(!t.hasEffect(net.minecraft.world.effect.MobEffects.LEVITATION) && !t.hasEffect(net.minecraft.world.effect.MobEffects.MOVEMENT_SLOWDOWN),"Cancelled effects retained lease");
            var effect=external.getEffect(net.minecraft.world.effect.MobEffects.LEVITATION);
            h.assertTrue(effect!=null && effect.getAmplifier()==2,"Cleanup removed an unrelated stronger levitation");done(h,p,t,external);
        });
    }
    @GameTest(template="empty",batch="senbon_prison_cover",timeoutTicks=50)
    public static void prisonCannotCaptureThroughSolidCover(GameTestHelper h) {
        var p=player(h,"shikai");var t=target(h,p,0,11);var blocks=new ArrayList<BlockPos>();ticks(h,p);
        for(int x=-1;x<=1;x++)for(int y=0;y<3;y++) {
            var pos=p.blockPosition().offset(x,y,6);blocks.add(pos);h.getLevel().setBlockAndUpdate(pos,net.minecraft.world.level.block.Blocks.STONE.defaultBlockState());
        }
        SenbonzakuraTechniqueService.executeSlot(p,data(p),3);
        h.runAfterDelay(30,()->{try {
            h.assertTrue(t.getHealth()==1000 && !t.hasEffect(net.minecraft.world.effect.MobEffects.LEVITATION),"Prison crossed cover");
        } finally {for(var pos:blocks)h.getLevel().removeBlock(pos,false);}done(h,p,t);});
    }
    @GameTest(template="empty",batch="senbon_range",timeoutTicks=45)
    public static void sealedCutAndStreamUseExpandedRanges(GameTestHelper h) {
        var p=player(h,"sealed");var d=data(p);d.getCharacter().setMastery("zanpakuto","sealed",100);
        var cut=target(h,p,0,7.5);var stream=target(h,p,0,13);ticks(h,p);
        h.assertTrue(SenbonzakuraTechniqueService.executeSlot(p,d,1),"Sealed cut refused");
        h.runAfterDelay(9,()->{h.assertTrue(cut.getHealth()<1000,"Expanded cut failed at 7.5 blocks");
            h.assertTrue(SenbonzakuraTechniqueService.executeSlot(p,d,2),"Sealed stream refused");});
        h.runAfterDelay(18,()->{h.assertTrue(stream.getHealth()<1000,"Expanded stream failed at 13 blocks");done(h,p,cut,stream);});
    }
    @GameTest(template="empty",batch="senbon_stream_start",timeoutTicks=25)
    public static void streamKeepsFirstPulseWhenInitialTickIsDelayed(GameTestHelper h) {
        var p=player(h,"sealed");data(p).getCharacter().setMastery("zanpakuto","sealed",100);var t=target(h,p,0,13);
        h.assertTrue(SenbonzakuraTechniqueService.executeSlot(p,data(p),2),"Sealed stream refused");
        // No fixture tick loop: the first actual service tick is deliberately two ticks after the packet.
        h.runAfterDelay(2,()->{SenbonzakuraTechniqueService.tick(p,data(p));
            h.assertTrue(t.getHealth()<1000,"First pulse depended on processing on the activation tick");done(h,p,t);});
    }
    @GameTest(template="empty",batch="senbon_dash_sweep",timeoutTicks=60)
    public static void hakuteikenSweepsPathOncePerTargetAndDoesNotHitAheadEarly(GameTestHelper h) {
        var p=player(h,"bankai");var near=target(h,p,0,3);var middle=target(h,p,0,9);var far=target(h,p,0,15);var side=target(h,p,3,9);ticks(h,p);
        var tracked=List.of(near,middle,far);int[] hits={0,0,0};float[] before={1000,1000,1000};
        h.onEachTick(()->{for(int i=0;i<3;i++){float hp=tracked.get(i).getHealth();if(hp<before[i])hits[i]++;before[i]=hp;}});
        SenbonzakuraTechniqueService.executeSlot(p,data(p),4);
        h.runAfterDelay(18,()->h.assertTrue(far.getHealth()==1000,"Dash damaged the end of the path before reaching it"));
        h.runAfterDelay(34,()->{h.assertTrue(hits[0]==1 && hits[1]==1 && hits[2]==1,"Dash did not hit every path target exactly once");
            h.assertTrue(side.getHealth()==1000,"Dash hit outside its corridor");done(h,p,near,middle,far,side);});
    }
    @GameTest(template="empty",batch="senbon_dash_wall",timeoutTicks=60)
    public static void hakuteikenStopsBeforeWallAndCannotDamageThroughIt(GameTestHelper h) {
        var p=player(h,"bankai");Vec3 start=p.position();var near=target(h,p,0,3);var covered=target(h,p,0,10);ticks(h,p);var wall=new ArrayList<BlockPos>();
        for(int x=-1;x<=1;x++)for(int y=0;y<3;y++){var pos=p.blockPosition().offset(x,y,6);wall.add(pos);h.getLevel().setBlockAndUpdate(pos,net.minecraft.world.level.block.Blocks.STONE.defaultBlockState());}
        SenbonzakuraTechniqueService.executeSlot(p,data(p),4);
        h.runAfterDelay(34,()->{try{
            h.assertTrue(p.position().subtract(start).z>0 && p.position().subtract(start).z<6,"Dash crossed wall or never moved");
            h.assertTrue(near.getHealth()<1000 && covered.getHealth()==1000,"Dash damage crossed solid cover");
            h.assertTrue(!SenbonzakuraTechniqueService.busy(p),"Collision did not end dash");
        }finally{for(var pos:wall)h.getLevel().removeBlock(pos,false);}done(h,p,near,covered);});
    }
    @GameTest(template="empty",batch="senbon_dash_cancel",timeoutTicks=60)
    public static void cancellingHakuteikenStopsMovementAndLateDamage(GameTestHelper h) {
        var p=player(h,"bankai");var t=target(h,p,0,15);ticks(h,p);final Vec3[] stopped={Vec3.ZERO};
        SenbonzakuraTechniqueService.executeSlot(p,data(p),4);
        h.runAfterDelay(19,()->{SenbonzakuraTechniqueService.cancel(p);p.setItemSlot(EquipmentSlot.MAINHAND,ItemStack.EMPTY);stopped[0]=p.position();});
        h.runAfterDelay(34,()->{h.assertTrue(p.position().distanceToSqr(stopped[0])<.001,"Cancelled dash kept moving");
            h.assertTrue(t.getHealth()==1000 && !SenbonzakuraTechniqueService.busy(p),"Cancelled dash kept damaging");done(h,p,t);});
    }
    @GameTest(template="empty",batch="senbon_ikka_area",timeoutTicks=65)
    public static void ikkaHitsFrontSideAndBackButRespectsAreaAndCover(GameTestHelper h) {
        var p=player(h,"bankai");var d=data(p);var front=target(h,p,0,6);var side=target(h,p,6,0);var back=target(h,p,0,-6);
        var outside=target(h,p,0,15);var covered=target(h,p,6,6);var wall=new ArrayList<BlockPos>();ticks(h,p);
        for(int x=2;x<=4;x++)for(int y=0;y<3;y++){var pos=p.blockPosition().offset(x,y,3);wall.add(pos);h.getLevel().setBlockAndUpdate(pos,net.minecraft.world.level.block.Blocks.STONE.defaultBlockState());}
        SenbonzakuraTechniqueService.executeSlot(p,d,3);
        h.runAfterDelay(21,()->h.assertTrue(SenbonzakuraTechniqueService.executeSlot(p,d,3),"Ikka continuation refused"));
        h.runAfterDelay(39,()->{try{
            h.assertTrue(front.getHealth()<1000 && side.getHealth()<1000 && back.getHealth()<1000,"Ikka retained a facing-only damage filter");
            h.assertTrue(outside.getHealth()==1000 && covered.getHealth()==1000,"Ikka ignored radius or solid cover");
        }finally{for(var pos:wall)h.getLevel().removeBlock(pos,false);}done(h,p,front,side,back,outside,covered);});
    }
    @GameTest(template="empty",batch="senbon_garden_deadline",timeoutTicks=150)
    public static void lateGardenReactivationFinishesBeforeSixSecondDeadline(GameTestHelper h) {
        var p=player(h,"bankai");var d=data(p);var t=target(h,p,0,8);ticks(h,p);final float[] initial={0};
        SenbonzakuraTechniqueService.executeSlot(p,d,2);
        h.runAfterDelay(30,()->initial[0]=t.getHealth());
        h.runAfterDelay(118,()->h.assertTrue(SenbonzakuraTechniqueService.executeSlot(p,d,2),"Reactivation inside the six-second window was refused"));
        h.runAfterDelay(122,()->{h.assertTrue(t.getHealth()<initial[0],"Late reactivation lost its final explosion");
            h.assertTrue(!SenbonzakuraTechniqueService.hud(p).contains("fieldRemaining"),"Bloom survived six-second deadline");done(h,p,t);});
    }
}
