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
    @GameTest(template="empty",batch="senbon_garden_prison_damage",timeoutTicks=180)
    public static void gardenPrisonDealsFiveBurstPlusThreePulsesForTotalFourteen(GameTestHelper h) {
        var p=player(h,"bankai");var d=data(p);
        loadField(h,p);
        var t=target(h,p,0,8);
        t.setItemSlot(EquipmentSlot.HEAD,ItemStack.EMPTY);
        t.getAttribute(Attributes.ARMOR).setBaseValue(0);
        ticks(h,p);
        final float[] hp={0,0,0,0,0};
        SenbonzakuraTechniqueService.executeSlot(p,d,2);
        h.runAfterDelay(25,()->h.assertTrue(Math.abs(t.getHealth()-991.0F)<0.01F,"Initial burst was not 6 base / 9 net HP: health was " + t.getHealth()));
        h.runAfterDelay(45,()->{
            hp[0]=t.getHealth();
            h.assertTrue(SenbonzakuraTechniqueService.executeSlot(p,d,2),"Bloom reactivation refused");
        });
        h.runAfterDelay(63,()->{
            hp[1]=t.getHealth();
            float burst=hp[0]-hp[1];
            h.assertTrue(Math.abs(burst-7.5F)<0.01F,"Bloom burst was not 5 base / 7.5 net HP: was "+burst);
            h.assertTrue(SenbonzakuraTechniqueService.hud(p).contains("gardenPrison"),"Garden prison state missing in HUD");
        });
        h.runAfterDelay(83,()->{
            hp[2]=t.getHealth();
            float pulse1=hp[1]-hp[2];
            h.assertTrue(Math.abs(pulse1-4.5F)<0.01F,"Pulse 1 was not 3 base / 4.5 net HP: was "+pulse1);
        });
        h.runAfterDelay(103,()->{
            hp[3]=t.getHealth();
            float pulse2=hp[2]-hp[3];
            h.assertTrue(Math.abs(pulse2-4.5F)<0.01F,"Pulse 2 was not 3 base / 4.5 net HP: was "+pulse2);
        });
        h.runAfterDelay(123,()->{
            hp[4]=t.getHealth();
            float pulse3=hp[3]-hp[4];
            float totalSecondPhase=hp[0]-hp[4];
            h.assertTrue(Math.abs(pulse3-4.5F)<0.01F,"Pulse 3 was not 3 base / 4.5 net HP: was "+pulse3);
            h.assertTrue(Math.abs(totalSecondPhase-21.0F)<0.01F,"Total second phase damage was not 14 base / 21 net HP: was "+totalSecondPhase);
            h.assertTrue(Math.abs(totalSecondPhase/1.5F-14.0F)<0.01F,"Total base second phase damage was not 14 HP");
        });
        h.runAfterDelay(128,()->{
            h.assertTrue(t.getHealth()==hp[4],"Extra damage dealt after prison expiration");
            h.assertTrue(!SenbonzakuraTechniqueService.hud(p).contains("gardenPrison"),"Garden prison survived expiration");
            done(h,p,t);
        });
    }
    @GameTest(template="empty",batch="senbon_garden_prison_cap",timeoutTicks=140)
    public static void gardenPrisonCapturesUpToSixteenTargetsAndSortsByProximity(GameTestHelper h) {
        var p=player(h,"bankai");var d=data(p);
        loadField(h,p);
        List<Zombie> targets=new ArrayList<>();
        for(int i=0;i<20;i++) {
            var t=target(h,p,0,2+i*1.5);
            t.setItemSlot(EquipmentSlot.HEAD,ItemStack.EMPTY);
            t.getAttribute(Attributes.ARMOR).setBaseValue(0);
            targets.add(t);
        }
        var occludedTarget=target(h,p,4,3);
        occludedTarget.setItemSlot(EquipmentSlot.HEAD,ItemStack.EMPTY);
        occludedTarget.getAttribute(Attributes.ARMOR).setBaseValue(0);

        var wall=new ArrayList<BlockPos>();
        for(int y=0;y<3;y++)for(int z=-1;z<=1;z++){
            var pos=p.blockPosition().offset(2,y,3+z);wall.add(pos);
            h.getLevel().setBlockAndUpdate(pos,net.minecraft.world.level.block.Blocks.STONE.defaultBlockState());
        }
        ticks(h,p);
        final float[] beforeBloom=new float[20];
        final float[] beforeBloomWall={0};
        SenbonzakuraTechniqueService.executeSlot(p,d,2);
        h.runAfterDelay(44,()->{
            for(int i=0;i<20;i++)beforeBloom[i]=targets.get(i).getHealth();
            beforeBloomWall[0]=occludedTarget.getHealth();
        });
        h.runAfterDelay(45,()->SenbonzakuraTechniqueService.executeSlot(p,d,2));
        h.runAfterDelay(125,()->{
            try {
                for(int i=0;i<16;i++){
                    float delta=beforeBloom[i]-targets.get(i).getHealth();
                    h.assertTrue(Math.abs(delta-21.0F)<0.01F,
                        "Target "+i+" within cap took delta "+delta+", expected exactly 21.0 net HP (14 base * 1.5 Bankai multiplier)");
                }
                for(int i=16;i<20;i++){
                    float delta=beforeBloom[i]-targets.get(i).getHealth();
                    h.assertTrue(delta==0.0F,"Target "+i+" beyond cap of 16 took bloom/pulse damage (delta="+delta+")");
                }
                float wallDelta=beforeBloomWall[0]-occludedTarget.getHealth();
                h.assertTrue(wallDelta==0.0F,"Occluded target behind wall took bloom/pulse damage (delta="+wallDelta+")");
            } finally {
                for(var pos:wall)h.getLevel().removeBlock(pos,false);
            }
            List<LivingEntity> all=new ArrayList<>(targets);all.add(occludedTarget);
            done(h,p,all.toArray(new LivingEntity[0]));
        });
    }
    @GameTest(template="empty",batch="senbon_garden_allies_team",timeoutTicks=130)
    public static void gardenPrisonAlliesSpectatorAndMidPrisonAlliance(GameTestHelper h) {
        var p=player(h,"bankai");var d=data(p);loadField(h,p);
        var scoreboard=h.getLevel().getScoreboard();
        var team=scoreboard.getPlayerTeam("senbon_allies_test");
        if(team==null)team=scoreboard.addPlayerTeam("senbon_allies_test");
        scoreboard.addPlayerToTeam(p.getScoreboardName(),team);

        var teamAlly=target(h,p,2,6);
        scoreboard.addPlayerToTeam(teamAlly.getStringUUID(),team);
        h.assertTrue(p.isAlliedTo(teamAlly),"Team ally was not allied to caster");

        var convertingEnemy=target(h,p,-2,6);
        convertingEnemy.setItemSlot(EquipmentSlot.HEAD,ItemStack.EMPTY);
        convertingEnemy.getAttribute(Attributes.ARMOR).setBaseValue(0);

        var regularEnemy=target(h,p,0,6);
        regularEnemy.setItemSlot(EquipmentSlot.HEAD,ItemStack.EMPTY);
        regularEnemy.getAttribute(Attributes.ARMOR).setBaseValue(0);

        ticks(h,p);
        float casterHp=p.getHealth();
        final float[] midHp={0,0};

        SenbonzakuraTechniqueService.executeSlot(p,d,2);
        h.runAfterDelay(45,()->SenbonzakuraTechniqueService.executeSlot(p,d,2));

        h.runAfterDelay(70,()->{
            midHp[0]=convertingEnemy.getHealth();
            midHp[1]=regularEnemy.getHealth();
            h.assertTrue(midHp[0]<1000,"Converting enemy took no damage in first pulse/burst");
            // Mid-prison alliance conversion:
            scoreboard.addPlayerToTeam(convertingEnemy.getStringUUID(),scoreboard.getPlayerTeam("senbon_allies_test"));
            h.assertTrue(p.isAlliedTo(convertingEnemy),"Converting enemy was not allied after scoreboard add");
        });

        h.runAfterDelay(115,()->{
            try {
                h.assertTrue(p.getHealth()==casterHp,"Caster took self-damage");
                h.assertTrue(teamAlly.getHealth()==1000.0F,"Team ally took damage from Garden");
                h.assertTrue(convertingEnemy.getHealth()==midHp[0],"Enemy converted to ally mid-prison continued taking pulses: was "+convertingEnemy.getHealth()+" vs "+midHp[0]);
                h.assertTrue(regularEnemy.getHealth()<midHp[1],"Regular enemy stopped taking pulses");
            } finally {
                scoreboard.removePlayerTeam(scoreboard.getPlayerTeam("senbon_allies_test"));
            }
            done(h,p,teamAlly,convertingEnemy,regularEnemy);
        });
    }
    @GameTest(template="empty",batch="senbon_garden_continuous_height",timeoutTicks=140)
    public static void gardenPrisonContinuousHeightCeilingAndExternalLevitation(GameTestHelper h) {
        var p=player(h,"bankai");var d=data(p);loadField(h,p);
        var floorBlocks=new ArrayList<BlockPos>();

        java.util.function.BiConsumer<BlockPos,List<BlockPos>> makeFloor=(center,list)->{
            for(int dx=-1;dx<=1;dx++)for(int dz=-1;dz<=1;dz++){
                var b=center.offset(dx,0,dz);
                list.add(b);
                h.getLevel().setBlockAndUpdate(b,net.minecraft.world.level.block.Blocks.STONE.defaultBlockState());
            }
        };

        var openTarget=target(h,p,0,6);
        openTarget.setNoAi(false);openTarget.setNoGravity(false);openTarget.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0);
        makeFloor.accept(openTarget.blockPosition().below(),floorBlocks);

        var ceilingTarget=target(h,p,3,6);
        ceilingTarget.setNoAi(false);ceilingTarget.setNoGravity(false);ceilingTarget.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0);
        makeFloor.accept(ceilingTarget.blockPosition().below(),floorBlocks);
        var ceilingBlock=ceilingTarget.blockPosition().above(2);
        h.getLevel().setBlockAndUpdate(ceilingBlock,net.minecraft.world.level.block.Blocks.STONE.defaultBlockState());

        var strongTarget=target(h,p,-3,6);
        strongTarget.setNoAi(false);strongTarget.setNoGravity(false);strongTarget.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0);
        makeFloor.accept(strongTarget.blockPosition().below(),floorBlocks);

        var longTarget=target(h,p,-6,6);
        longTarget.setNoAi(false);longTarget.setNoGravity(false);longTarget.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0);
        makeFloor.accept(longTarget.blockPosition().below(),floorBlocks);

        var strongCeilingTarget=target(h,p,6,6);
        strongCeilingTarget.setNoAi(false);strongCeilingTarget.setNoGravity(false);strongCeilingTarget.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0);
        makeFloor.accept(strongCeilingTarget.blockPosition().below(),floorBlocks);
        var strongCeilingBlock=strongCeilingTarget.blockPosition().above(2);
        h.getLevel().setBlockAndUpdate(strongCeilingBlock,net.minecraft.world.level.block.Blocks.STONE.defaultBlockState());

        ticks(h,p);
        double openX=openTarget.getX(), openZ=openTarget.getZ(), openFloorY=openTarget.getY();
        double strongX=strongTarget.getX(), strongZ=strongTarget.getZ(), strongFloorY=strongTarget.getY();
        double longX=longTarget.getX(), longZ=longTarget.getZ(), longFloorY=longTarget.getY();
        double ceilX=ceilingTarget.getX(), ceilZ=ceilingTarget.getZ();
        double scX=strongCeilingTarget.getX(), scZ=strongCeilingTarget.getZ();

        final double[] maxRise={0};
        final double[] strongMaxRise={0};
        final double[] longMaxRise={0};
        final boolean[] violatedMaxRise={false};
        final boolean[] strongViolatedMaxRise={false};
        final boolean[] longViolatedMaxRise={false};
        final boolean[] penetratedCeiling={false};
        final boolean[] strongPenetratedCeiling={false};
        final boolean[] strongDowngraded={false};
        final boolean[] strongCeilingDowngraded={false};
        final boolean[] longTruncated={false};

        h.onEachTick(()->{
            // Restrain horizontal wander off the stone platform
            openTarget.setPos(openX,openTarget.getY(),openZ);
            openTarget.setDeltaMovement(0,openTarget.getDeltaMovement().y,0);
            strongTarget.setPos(strongX,strongTarget.getY(),strongZ);
            strongTarget.setDeltaMovement(0,strongTarget.getDeltaMovement().y,0);
            longTarget.setPos(longX,longTarget.getY(),longZ);
            longTarget.setDeltaMovement(0,longTarget.getDeltaMovement().y,0);
            ceilingTarget.setPos(ceilX,ceilingTarget.getY(),ceilZ);
            ceilingTarget.setDeltaMovement(0,ceilingTarget.getDeltaMovement().y,0);
            strongCeilingTarget.setPos(scX,strongCeilingTarget.getY(),scZ);
            strongCeilingTarget.setDeltaMovement(0,strongCeilingTarget.getDeltaMovement().y,0);

            double rise=openTarget.getY()-openFloorY;
            maxRise[0]=Math.max(maxRise[0],rise);
            if(rise>2.55)violatedMaxRise[0]=true;

            double sRise=strongTarget.getY()-strongFloorY;
            strongMaxRise[0]=Math.max(strongMaxRise[0],sRise);
            if(sRise>2.55)strongViolatedMaxRise[0]=true;

            double lRise=longTarget.getY()-longFloorY;
            longMaxRise[0]=Math.max(longMaxRise[0],lRise);
            if(lRise>2.55)longViolatedMaxRise[0]=true;

            if(ceilingTarget.getBoundingBox().intersects(new net.minecraft.world.phys.AABB(ceilingBlock)))penetratedCeiling[0]=true;
            if(strongCeilingTarget.getBoundingBox().intersects(new net.minecraft.world.phys.AABB(strongCeilingBlock)))strongPenetratedCeiling[0]=true;

            var sEff=strongTarget.getEffect(net.minecraft.world.effect.MobEffects.LEVITATION);
            if(sEff!=null && sEff.getAmplifier()!=1)strongDowngraded[0]=true;

            var scEff=strongCeilingTarget.getEffect(net.minecraft.world.effect.MobEffects.LEVITATION);
            if(scEff!=null && scEff.getAmplifier()!=1)strongCeilingDowngraded[0]=true;

            var lEff=longTarget.getEffect(net.minecraft.world.effect.MobEffects.LEVITATION);
            if(lEff!=null && lEff.getDuration()<=SenbonPolicy.CONTROL_LEASE)longTruncated[0]=true;
        });

        SenbonzakuraTechniqueService.executeSlot(p,d,2);
        h.runAfterDelay(45,()->{
            strongTarget.addEffect(new net.minecraft.world.effect.MobEffectInstance(net.minecraft.world.effect.MobEffects.LEVITATION,200,1));
            longTarget.addEffect(new net.minecraft.world.effect.MobEffectInstance(net.minecraft.world.effect.MobEffects.LEVITATION,250,0));
            strongCeilingTarget.addEffect(new net.minecraft.world.effect.MobEffectInstance(net.minecraft.world.effect.MobEffects.LEVITATION,200,1));
            SenbonzakuraTechniqueService.executeSlot(p,d,2);
        });

        h.runAfterDelay(125,()->{
            try {
                h.assertTrue(!violatedMaxRise[0],"Open target exceeded 2.5m rise across ticks: max was "+maxRise[0]);
                h.assertTrue(maxRise[0]>0.3,"Open target never rose above ground: max was "+maxRise[0]);
                h.assertTrue(!penetratedCeiling[0],"Ceiling target penetrated ceiling block bounding box");
                h.assertTrue(!strongViolatedMaxRise[0],"Strong levitation target exceeded 2.5m rise: max was "+strongMaxRise[0]);
                h.assertTrue(strongMaxRise[0]>0.3,"Strong levitation target never rose: max was "+strongMaxRise[0]);
                h.assertTrue(!strongDowngraded[0],"Strong external levitation was downgraded during prison");
                h.assertTrue(!longViolatedMaxRise[0],"Long levitation target exceeded 2.5m rise: max was "+longMaxRise[0]);
                h.assertTrue(longMaxRise[0]>0.3,"Long levitation target never rose: max was "+longMaxRise[0]);
                h.assertTrue(!longTruncated[0],"Long external levitation was truncated to short lease");
                h.assertTrue(!strongPenetratedCeiling[0],"Strong levitation target penetrated ceiling block bounding box");
                h.assertTrue(!strongCeilingDowngraded[0],"Strong ceiling levitation was downgraded");
                h.assertTrue(!openTarget.hasEffect(net.minecraft.world.effect.MobEffects.LEVITATION),"Open target levitation lingered after expiration");
                h.assertTrue(strongTarget.hasEffect(net.minecraft.world.effect.MobEffects.LEVITATION),"Strong external levitation was dropped on expiration");
                h.assertTrue(longTarget.hasEffect(net.minecraft.world.effect.MobEffects.LEVITATION),"Long external levitation was dropped on expiration");
                h.assertTrue(strongCeilingTarget.hasEffect(net.minecraft.world.effect.MobEffects.LEVITATION),"Strong ceiling external levitation was dropped on expiration");
            } finally {
                h.getLevel().removeBlock(ceilingBlock,false);
                h.getLevel().removeBlock(strongCeilingBlock,false);
                for(var b:floorBlocks)h.getLevel().removeBlock(b,false);
            }
            done(h,p,openTarget,ceilingTarget,strongTarget,longTarget,strongCeilingTarget);
        });
    }
    @GameTest(template="empty",batch="senbon_garden_late_deltas",timeoutTicks=220)
    public static void lateGardenReactivationAtFivePointEightSecondsFinishesFullPrisonPhase(GameTestHelper h) {
        var p=player(h,"bankai");var d=data(p);loadField(h,p);
        var t=target(h,p,0,8);
        t.setItemSlot(EquipmentSlot.HEAD,ItemStack.EMPTY);
        t.getAttribute(Attributes.ARMOR).setBaseValue(0);
        ticks(h,p);

        final long[] prisonStart={-1};
        final float[] hpSamples={0,0,0,0,0}; // burst, pulse1, pulse2, pulse3, post

        h.onEachTick(()->{
            if(prisonStart[0]<0 && SenbonzakuraTechniqueService.hud(p).contains("gardenPrison")){
                prisonStart[0]=h.getTick();
                hpSamples[0]=t.getHealth();
            }
        });

        SenbonzakuraTechniqueService.executeSlot(p,d,2);
        h.runAfterDelay(116,()->h.assertTrue(SenbonzakuraTechniqueService.executeSlot(p,d,2),"Reactivation at 5.8s was refused"));

        h.runAfterDelay(125,()->h.assertTrue(prisonStart[0]>0,"Garden prison failed to start after 5.8s reactivation"));

        h.runAfterDelay(145,()->{
            hpSamples[1]=t.getHealth();
            float pulse1=hpSamples[0]-hpSamples[1];
            h.assertTrue(Math.abs(pulse1-4.5F)<0.01F,"Late prison pulse 1 was not 4.5 net HP: was "+pulse1);
        });
        h.runAfterDelay(165,()->{
            hpSamples[2]=t.getHealth();
            float pulse2=hpSamples[1]-hpSamples[2];
            h.assertTrue(Math.abs(pulse2-4.5F)<0.01F,"Late prison pulse 2 was not 4.5 net HP: was "+pulse2);
        });
        h.runAfterDelay(178,()->{
            h.assertTrue(SenbonzakuraTechniqueService.hud(p).contains("gardenPrison"),"Late prison died before tick 60");
        });
        h.runAfterDelay(185,()->{
            hpSamples[3]=t.getHealth();
            float pulse3=hpSamples[2]-hpSamples[3];
            h.assertTrue(Math.abs(pulse3-4.5F)<0.01F,"Late prison pulse 3 was not 4.5 net HP: was "+pulse3);
            h.assertTrue(!SenbonzakuraTechniqueService.hud(p).contains("gardenPrison"),"Late prison did not terminate after tick 60");
            h.assertTrue(t.getHealth()==hpSamples[3],"Late prison dealt extra damage after expiration");
            done(h,p,t);
        });
    }
    @GameTest(template="empty",batch="senbon_garden_lifecycle_cancel",timeoutTicks=90)
    public static void gardenPrisonLifecycleCleansUpWithoutDroppingExternalEffects(GameTestHelper h) {
        var p=player(h,"bankai");var d=data(p);loadField(h,p);
        var prisonTarget=target(h,p,0,6);
        var extTarget=target(h,p,3,6);
        extTarget.addEffect(new net.minecraft.world.effect.MobEffectInstance(net.minecraft.world.effect.MobEffects.LEVITATION,200,1));

        ticks(h,p);
        SenbonzakuraTechniqueService.executeSlot(p,d,2);
        h.runAfterDelay(45,()->SenbonzakuraTechniqueService.executeSlot(p,d,2));

        h.runAfterDelay(65,()->{
            h.assertTrue(SenbonzakuraTechniqueService.hud(p).contains("gardenPrison"),"Garden prison was not active before cancel");
            SenbonzakuraTechniqueService.cancel(p);
            h.assertTrue(!SenbonzakuraTechniqueService.hud(p).contains("gardenPrison"),"HUD retained gardenPrison after cancel");
            var extEff=extTarget.getEffect(net.minecraft.world.effect.MobEffects.LEVITATION);
            h.assertTrue(extEff!=null && extEff.getAmplifier()==1 && extEff.getDuration()>100,"External levitation was cleared by cancel()");
        });

        h.runAfterDelay(75,()->{
            h.assertTrue(!prisonTarget.hasEffect(net.minecraft.world.effect.MobEffects.LEVITATION),"Prison target levitation lingered after cancel");
            done(h,p,prisonTarget,extTarget);
        });
    }
    @GameTest(template="empty",batch="senbon_garden_owner_movement",timeoutTicks=100)
    public static void gardenPrisonOwnerCanMoveAndLookAwayWithoutDroppingField(GameTestHelper h) {
        var p=player(h,"bankai");var d=data(p);var t=target(h,p,0,8);ticks(h,p);
        float playerHp=p.getHealth();
        SenbonzakuraTechniqueService.executeSlot(p,d,2);
        h.runAfterDelay(45,()->{
            SenbonzakuraTechniqueService.executeSlot(p,d,2);
            p.setYRot(180);p.setPos(p.position().add(10,0,0));
        });
        h.runAfterDelay(85,()->{
            h.assertTrue(p.getHealth()==playerHp,"Caster took self-damage from Garden");
            h.assertTrue(t.getHealth()<1000,"Target was dropped when owner turned and walked away");
            done(h,p,t);
        });
    }
}
