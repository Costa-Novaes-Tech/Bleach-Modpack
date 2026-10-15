package com.bleachmod.gametest;

import com.bleachmod.common.data.*;
import com.bleachmod.common.technique.*;
import com.bleachmod.init.ModItems;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.*;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.gametest.*;
import java.util.*;

@GameTestHolder("bleachmod") @PrefixGameTestTemplate(false)
public final class BenihimeGameTests {
    private static PlayerData data(FakePlayer p){return PlayerCapability.get(p).orElseThrow(()->new IllegalStateException("Missing capability"));}
    private static FakePlayer player(GameTestHelper h,String form){
        var id=UUID.randomUUID();var p=new FakePlayer(h.getLevel(),new GameProfile(id,"Beni"+id.toString().substring(0,8)));
        Vec3 pos=h.absoluteVec(new Vec3(2.5,2,2.5));p.setPos(pos.x,h.getLevel().getMaxBuildHeight()-34,pos.z);p.setYRot(0);p.setXRot(0);p.setOnGround(true);p.setNoGravity(true);
        for(int x=-2;x<=2;x++)for(int z=-2;z<=2;z++)h.getLevel().getChunkAt(p.blockPosition().offset(x*16,0,z*16));
        var d=data(p);d.initializeShinigami();d.getCharacter().bindZanpakuto("benihime");d.getSkills().get("zanpakuto").setLevel(2);
        d.getCharacter().unlockForm("shikai");d.getCharacter().unlockForm("bankai");d.getCharacter().setMastery("zanpakuto","shikai",100);d.getCharacter().setMastery("zanpakuto","bankai",100);d.getCharacter().setActiveForm("zanpakuto",form);
        p.setItemSlot(EquipmentSlot.MAINHAND,new ItemStack(ModItems.BENIHIME.get()));return p;
    }
    private static Zombie target(GameTestHelper h,FakePlayer p,double x,double z){var t=EntityType.ZOMBIE.create(h.getLevel());t.setPos(p.position().add(x,0,z));t.setNoAi(true);t.setNoGravity(true);t.setItemSlot(EquipmentSlot.HEAD,new ItemStack(Items.IRON_HELMET));t.getAttribute(Attributes.MAX_HEALTH).setBaseValue(1000);t.setHealth(1000);h.getLevel().addFreshEntity(t);return t;}
    private static void ticks(GameTestHelper h,FakePlayer... owners){h.onEachTick(()->{for(var p:owners){p.tickCount++;data(p).getStatus().tickTransientState();BenihimeTechniqueService.tick(p,data(p));}});}
    private static void done(GameTestHelper h,FakePlayer p,LivingEntity... targets){BenihimeTechniqueService.cancel(p);for(var t:targets)t.discard();h.succeed();}
    private static FakePlayer ally(GameTestHelper h,FakePlayer owner,double x,double z){
        var p=player(h,"sealed");data(p).getCharacter().bindZanpakuto("asauchi");p.setItemSlot(EquipmentSlot.MAINHAND,ItemStack.EMPTY);p.setPos(owner.position().add(x,0,z));p.setHealth(10);h.getLevel().addNewPlayer(p);return p;
    }
    private static net.minecraft.world.scores.PlayerTeam team(GameTestHelper h,FakePlayer... players){var board=h.getLevel().getScoreboard();var t=board.addPlayerTeam("beni"+UUID.randomUUID().toString().substring(0,8));for(var p:players)board.addPlayerToTeam(p.getScoreboardName(),t);return t;}
    private static void removeAlly(GameTestHelper h,FakePlayer p){h.getLevel().removePlayerImmediately(p,Entity.RemovalReason.DISCARDED);}
    @GameTest(template="empty",batch="beni_access")
    public static void identityEquipmentAndSealedLocksRejectWithoutCost(GameTestHelper h){var p=player(h,"sealed");var d=data(p);float before=d.getResources().getCurrentReiatsu();
        h.assertTrue(!BenihimeTechniqueService.executeSlot(p,d,1),"Sealed elemental skill accepted");d.getCharacter().setActiveForm("zanpakuto","shikai");p.setItemSlot(EquipmentSlot.MAINHAND,new ItemStack(ModItems.SENBONZAKURA.get()));
        h.assertTrue(!BenihimeTechniqueService.executeSlot(p,d,1),"Wrong identity item cast accepted");h.assertTrue(d.getResources().getCurrentReiatsu()==before,"Rejected skill charged");done(h,p);}
    @GameTest(template="empty",batch="beni_shot",timeoutTicks=40)
    public static void nakeHitsForwardTargetAndCooldownPreventsDebit(GameTestHelper h){var p=player(h,"shikai");var t=target(h,p,0,10);var back=target(h,p,0,-4);ticks(h,p);var d=data(p);
        h.assertTrue(BenihimeTechniqueService.executeSlot(p,d,1),"Nake refused");h.assertTrue(!BenihimeTechniqueService.executeSlot(p,d,1),"Duplicate Nake accepted");
        h.runAfterDelay(18,()->{h.assertTrue(t.getHealth()<1000 && back.getHealth()==1000,"Nake trajectory incorrect");h.assertTrue(d.getResources().getCurrentReiatsu()==88,"Nake debited twice");done(h,p,t,back);});}
    @GameTest(template="empty",batch="beni_shield")
    public static void shieldHasCapacityFrontalityAndExcludesManualGuard(GameTestHelper h){var p=player(h,"shikai");var d=data(p);var t=target(h,p,0,3);
        BenihimeTechniqueService.executeSlot(p,d,2);com.bleachmod.common.GuardService.request(p,d,true);h.assertTrue(!d.getGuard().active(),"Manual guard stacked");
        var front=new LivingHurtEvent(p,p.damageSources().mobAttack(t),10);MinecraftForge.EVENT_BUS.post(front);h.assertTrue(Math.abs(front.getAmount()-1.5)<.001,"Shield reduction incorrect");
        t.setPos(p.position().add(0,0,-3));var back=new LivingHurtEvent(p,p.damageSources().mobAttack(t),10);MinecraftForge.EVENT_BUS.post(back);h.assertTrue(back.getAmount()==10,"Shield blocked behind");
        var magic=new LivingHurtEvent(p,p.damageSources().magic(),10);MinecraftForge.EVENT_BUS.post(magic);h.assertTrue(magic.getAmount()==10,"Shield blocked magic");
        t.setPos(p.position().add(0,0,3));for(int i=0;i<3;i++)MinecraftForge.EVENT_BUS.post(new LivingHurtEvent(p,p.damageSources().mobAttack(t),10));
        h.assertTrue(!BenihimeTechniqueService.ward(p),"Shield capacity never broke");done(h,p,t);}
    @GameTest(template="empty",batch="beni_combo",timeoutTicks=60)
    public static void shibariIsRequiredAndHiasobiConsumesItsNet(GameTestHelper h){var p=player(h,"shikai");var d=data(p);var t=target(h,p,0,12);ticks(h,p);
        h.assertTrue(!BenihimeTechniqueService.executeSlot(p,d,4) && d.getResources().getCurrentReiatsu()==100,"Missing net charged detonation");
        h.assertTrue(BenihimeTechniqueService.executeSlot(p,d,3),"Shibari refused");
        h.runAfterDelay(25,()->{h.assertTrue(t.getHealth()==1000 && t.hasEffect(net.minecraft.world.effect.MobEffects.MOVEMENT_SLOWDOWN),"Net damaged or failed to bind");
            h.assertTrue(BenihimeTechniqueService.executeSlot(p,d,4),"Prepared detonation refused");h.assertTrue(!BenihimeTechniqueService.hud(p).contains("net"),"Detonation retained net");
            h.assertTrue(d.getResources().getCurrentReiatsu()==55 && d.getStatus().getTechniqueSlot4CooldownTicks()==200,"Hiasobi metadata mismatch");});
        h.runAfterDelay(46,()->{h.assertTrue(t.getHealth()<1000,"Detonation failed");done(h,p,t);});}
    @GameTest(template="empty",batch="beni_incision",timeoutTicks=95)
    public static void dissectionDealsDelayedDamageAndExposureExpires(GameTestHelper h){var p=player(h,"bankai");var t=target(h,p,0,16);ticks(h,p);BenihimeTechniqueService.executeSlot(p,data(p),1);
        h.runAfterDelay(6,()->h.assertTrue(t.getHealth()==1000,"Dissection skipped its warning"));
        h.runAfterDelay(18,()->h.assertTrue(t.getHealth()<1000 && BenihimeTechniqueService.damageBonus(p,t)>.19,"Dissection/exposure absent"));
        h.runAfterDelay(80,()->{h.assertTrue(BenihimeTechniqueService.damageBonus(p,t)==0,"Exposure never expired");done(h,p,t);});}
    @GameTest(template="empty",batch="beni_self_heal",timeoutTicks=50)
    public static void reconstructionHealsInPulsesAndOnlyActiveHealingTrains(GameTestHelper h){var p=player(h,"bankai");var d=data(p);p.setHealth(10);ticks(h,p);
        h.assertTrue(BenihimeTechniqueService.executeSlot(p,d,2),"Reconstruction refused");h.runAfterDelay(5,()->h.assertTrue(p.getHealth()==10,"Reconstruction skipped preparation"));
        h.runAfterDelay(35,()->{h.assertTrue(p.getHealth()>=17,"Reconstruction/self regeneration missing");
            h.assertTrue(d.getCharacter().getTechniqueMastery("bankai",2)>0,"Valid active healing did not train");done(h,p);});}
    @GameTest(template="empty",batch="beni_ally_heal",timeoutTicks=55)
    public static void reconstructionTargetsAnInjuredTeammate(GameTestHelper h){var p=player(h,"bankai");var other=ally(h,p,0,6);var t=team(h,p,other);ticks(h,p);
        h.assertTrue(BenihimeTechniqueService.executeSlot(p,data(p),2),"Targeted allied reconstruction refused");
        h.runAfterDelay(35,()->{try{h.assertTrue(other.getHealth()>=16,"Aimed ally did not receive reconstruction");}
            finally{removeAlly(h,other);h.getLevel().getScoreboard().removePlayerTeam(t);}done(h,p);});}
    @GameTest(template="empty",batch="beni_passives",timeoutTicks=65)
    public static void bankaiRegenerationHealsTeamOnlyAndDoesNotTrain(GameTestHelper h){var p=player(h,"bankai");p.setHealth(10);var friend=ally(h,p,0,5);var stranger=ally(h,p,3,5);var far=ally(h,p,0,11);var t=team(h,p,friend,far);ticks(h,p);
        h.runAfterDelay(45,()->{try{
            h.assertTrue(p.getHealth()==12,"Self regeneration should be one HP each second");h.assertTrue(friend.getHealth()==11,"Ally aura cadence incorrect");
            h.assertTrue(stranger.getHealth()==10 && far.getHealth()==10,"Aura healed a stranger or exceeded radius");h.assertTrue(data(p).getCharacter().getTechniqueMastery("bankai",2)==0,"Passive healing trained mastery");
        }finally{removeAlly(h,friend);removeAlly(h,stranger);removeAlly(h,far);h.getLevel().getScoreboard().removePlayerTeam(t);}done(h,p);});}
    @GameTest(template="empty",batch="beni_aura_overlap",timeoutTicks=65)
    public static void overlappingBenihimeAurasDoNotMultiplyAlliedHealing(GameTestHelper h){var p=player(h,"bankai");var second=player(h,"bankai");second.setPos(p.position().add(2,0,0));var friend=ally(h,p,0,5);var t=team(h,p,second,friend);ticks(h,p,second);
        h.runAfterDelay(45,()->{try{h.assertTrue(friend.getHealth()==11,"Two auras stacked allied healing");}
            finally{removeAlly(h,friend);h.getLevel().getScoreboard().removePlayerTeam(t);BenihimeTechniqueService.cancel(second);}done(h,p);});}
    @GameTest(template="empty",batch="beni_buff")
    public static void restructuringHasOnlyThreeSuccessfulStrikes(GameTestHelper h){var p=player(h,"bankai");var t=target(h,p,0,3);var nearby=target(h,p,1,3);var d=data(p);BenihimeTechniqueService.executeSlot(p,d,3);
        h.assertTrue(BenihimeTechniqueService.damageBonus(p,t)>.29,"Restructured arm bonus absent");
        for(int i=0;i<3;i++){t.invulnerableTime=0;t.hurt(p.damageSources().playerAttack(p),4);}
        h.assertTrue(BenihimeTechniqueService.hud(p).getInt("charges")==0 && BenihimeTechniqueService.damageBonus(p,t)==0,"Restructuring exceeded three successful hits");h.assertTrue(nearby.getHealth()==1000 && nearby.getDeltaMovement().horizontalDistanceSqr()>.01,"Restructured impact must push nearby enemies without extra damage");done(h,p,t,nearby);}
    @GameTest(template="empty",batch="beni_path",timeoutTicks=100)
    public static void pathTravelsOnceAndExpiresWithoutChangingTerrain(GameTestHelper h){var p=player(h,"bankai");var d=data(p);Vec3 start=p.position();var floor=new ArrayList<BlockPos>();ticks(h,p);
        for(int x=-1;x<=1;x++)for(int z=0;z<=13;z++){var pos=p.blockPosition().offset(x,-1,z);floor.add(pos);h.getLevel().setBlockAndUpdate(pos,net.minecraft.world.level.block.Blocks.STONE.defaultBlockState());}
        h.assertTrue(BenihimeTechniqueService.executeSlot(p,d,4),"Supported path refused");h.assertTrue(d.getResources().getCurrentReiatsu()==70 && d.getStatus().getTechniqueSlot4CooldownTicks()==0 && d.getStatus().getBenihimePathCharges()==2,"Path metadata mismatch");
        h.runAfterDelay(18,()->h.assertTrue(p.position().z-start.z>11,"Owner did not traverse corridor"));
        h.runAfterDelay(80,()->{try{h.assertTrue(!BenihimeTechniqueService.hud(p).contains("path"),"Corridor did not expire");for(var pos:floor)h.assertTrue(h.getLevel().getBlockState(pos).is(net.minecraft.world.level.block.Blocks.STONE),"Path modified terrain");}
            finally{for(var pos:floor)h.getLevel().removeBlock(pos,false);}done(h,p);});}
    @GameTest(template="empty",batch="beni_path_obstacle",timeoutTicks=45)
    public static void corridorRechecksNewWallsBeforeTraversal(GameTestHelper h){var p=player(h,"bankai");var start=p.position();var blocks=new ArrayList<BlockPos>();ticks(h,p);
        for(int x=-1;x<=1;x++)for(int z=0;z<=13;z++){var pos=p.blockPosition().offset(x,-1,z);blocks.add(pos);h.getLevel().setBlockAndUpdate(pos,net.minecraft.world.level.block.Blocks.STONE.defaultBlockState());}
        h.assertTrue(BenihimeTechniqueService.executeSlot(p,data(p),4),"Supported corridor refused");
        for(int y=0;y<3;y++){var pos=p.blockPosition().offset(0,y,5);blocks.add(pos);h.getLevel().setBlockAndUpdate(pos,net.minecraft.world.level.block.Blocks.STONE.defaultBlockState());}
        h.runAfterDelay(20,()->{try{h.assertTrue(p.position().distanceToSqr(start)<.1,"Corridor traversed a newly placed wall");}finally{for(var pos:blocks)h.getLevel().removeBlock(pos,false);}done(h,p);});}
    @GameTest(template="empty",batch="beni_vertical_charges",timeoutTicks=65)
    public static void threePathsTravelVerticallyThenStartCooldown(GameTestHelper h){var p=player(h,"bankai");var d=data(p);d.getResources().setCostsDisabled(true);ticks(h,p);Vec3 start=p.position();p.setXRot(-90);
        h.assertTrue(BenihimeTechniqueService.executeSlot(p,d,4),"Upward path refused");
        h.runAfterDelay(12,()->{h.assertTrue(p.getY()>start.y+11 && d.getStatus().getBenihimePathCharges()==2,"First vertical traversal/charge failed");p.setXRot(90);h.assertTrue(BenihimeTechniqueService.executeSlot(p,d,4),"Downward reuse refused");});
        h.runAfterDelay(24,()->{h.assertTrue(Math.abs(p.getY()-start.y)<.1 && d.getStatus().getBenihimePathCharges()==1,"Downward traversal failed");p.setXRot(-90);h.assertTrue(BenihimeTechniqueService.executeSlot(p,d,4),"Third charge refused");h.assertTrue(d.getStatus().getTechniqueSlot4CooldownTicks()==260,"Third use did not start cooldown");});
        h.runAfterDelay(38,()->{h.assertTrue(!BenihimeTechniqueService.executeSlot(p,d,4) && d.getStatus().getBenihimePathCharges()==0,"Fourth use bypassed cooldown");d.getStatus().setTechniqueSlot4CooldownTicks(0);h.assertTrue(d.getStatus().getBenihimePathCharges()==3,"Charge batch did not recover");done(h,p);});}
    @GameTest(template="empty",batch="beni_contracting_net",timeoutTicks=110)
    public static void expandedNetCapturesDistantMobAndContractsTowardCenter(GameTestHelper h){var p=player(h,"shikai");var t=target(h,p,8,12);var outside=target(h,p,11,12);ticks(h,p);double start=t.getX();
        h.assertTrue(BenihimeTechniqueService.executeSlot(p,data(p),3),"Expanded net refused");
        h.runAfterDelay(26,()->h.assertTrue(t.hasEffect(net.minecraft.world.effect.MobEffects.MOVEMENT_SLOWDOWN) && !outside.hasEffect(net.minecraft.world.effect.MobEffects.MOVEMENT_SLOWDOWN),"Net radius incorrect"));
        h.runAfterDelay(90,()->{h.assertTrue(t.getX()<start-3,"Contracting net did not drag the distant mob");h.assertTrue(t.getHealth()==1000,"Net unexpectedly damaged victim");done(h,p,t,outside);});}
    private static List<BlockPos> netFloor(GameTestHelper h,FakePlayer p){
        var blocks=new ArrayList<BlockPos>();for(int x=-13;x<=13;x++)for(int z=-13;z<=13;z++){var pos=p.blockPosition().offset(x,-1,z);blocks.add(pos);h.getLevel().setBlockAndUpdate(pos,net.minecraft.world.level.block.Blocks.STONE.defaultBlockState());}return blocks;
    }
    private static void removeBlocks(GameTestHelper h,List<BlockPos> blocks){for(var pos:blocks)h.getLevel().removeBlock(pos,false);}
    @GameTest(template="empty",batch="beni_net_floor",timeoutTicks=100)
    public static void downwardNetOnFloorCapturesAllQuadrants(GameTestHelper h){var p=player(h,"shikai");var floor=netFloor(h,p);var victims=List.of(target(h,p,8,0),target(h,p,-8,0),target(h,p,0,8),target(h,p,0,-8));var outside=target(h,p,11,0);p.setXRot(90);ticks(h,p);
        h.assertTrue(BenihimeTechniqueService.executeSlot(p,data(p),3),"Downward grounded cast refused");
        h.runAfterDelay(26,()->{for(var t:victims)h.assertTrue(t.hasEffect(net.minecraft.world.effect.MobEffects.MOVEMENT_SLOWDOWN),"Floor cast lost a quadrant");h.assertTrue(!outside.hasEffect(net.minecraft.world.effect.MobEffects.MOVEMENT_SLOWDOWN),"Floor cast exceeded radius ten");});
        h.runAfterDelay(80,()->{try{for(var t:victims)h.assertTrue(t.position().multiply(1,0,1).distanceTo(p.position().multiply(1,0,1))<4,"Grounded tether failed to contract");}finally{removeBlocks(h,floor);for(var t:victims)t.discard();}done(h,p,outside);});}
    @GameTest(template="empty",batch="beni_net_radial_blast",timeoutTicks=65)
    public static void hiasobiHitsWholeNetWithoutCasterVisibility(GameTestHelper h){var p=player(h,"shikai");var floor=netFloor(h,p);var origin=p.position();var victims=List.of(target(h,p,8,0),target(h,p,-8,0),target(h,p,0,8),target(h,p,0,-8));var outside=target(h,p,11,0);var friend=target(h,p,6,2);var board=h.getLevel().getScoreboard();var group=board.addPlayerTeam("net"+UUID.randomUUID().toString().substring(0,8));board.addPlayerToTeam(p.getScoreboardName(),group);board.addPlayerToTeam(friend.getScoreboardName(),group);
        p.setXRot(90);ticks(h,p);BenihimeTechniqueService.executeSlot(p,data(p),3);
        h.runAfterDelay(25,()->{
            for(int z=-2;z<=2;z++)for(int y=0;y<4;y++){var pos=BlockPos.containing(origin).offset(12,y,z);floor.add(pos);h.getLevel().setBlockAndUpdate(pos,net.minecraft.world.level.block.Blocks.STONE.defaultBlockState());}
            p.setPos(origin.add(14,0,0));p.setXRot(0);p.setYRot(-90);
            h.assertTrue(!p.hasLineOfSight(victims.get(0)),"Fixture did not occlude caster");h.assertTrue(BenihimeTechniqueService.executeSlot(p,data(p),4),"Radial detonation refused");});
        h.runAfterDelay(47,()->{try{for(var t:victims)h.assertTrue(t.getHealth()<1000,"Detonation missed a peripheral quadrant");h.assertTrue(outside.getHealth()==1000 && friend.getHealth()==1000,"Detonation hit outside field or ally");}finally{board.removePlayerTeam(group);removeBlocks(h,floor);for(var t:victims)t.discard();friend.discard();}done(h,p,outside);});}
    @GameTest(template="empty",batch="beni_net_resistance",timeoutTicks=85)
    public static void netOvercomesOutwardMovementWithoutChangingShrinkTiming(GameTestHelper h){var p=player(h,"shikai");var t=target(h,p,8,12);var center=p.position().add(0,0,12);ticks(h,p);BenihimeTechniqueService.executeSlot(p,data(p),3);
        h.onEachTick(()->{if(p.tickCount>=35 && p.tickCount<=60)t.move(MoverType.SELF,new Vec3(.4,0,0));});
        h.runAfterDelay(65,()->{h.assertTrue(t.position().multiply(1,0,1).distanceTo(center.multiply(1,0,1))<6,"Outward movement overcame the tether");h.assertTrue(BenihimeTechniqueService.hud(p).getInt("net")>45,"Contraction timer changed");done(h,p,t);});}
    @GameTest(template="empty",batch="beni_invalid_path")
    public static void unsupportedPathAndFullHealthReconstructionDoNotSpendReiatsu(GameTestHelper h){var p=player(h,"bankai");var d=data(p);
        h.assertTrue(!BenihimeTechniqueService.executeSlot(p,d,4),"Unsupported path accepted");h.assertTrue(!BenihimeTechniqueService.executeSlot(p,d,2),"Full-health reconstruction accepted");
        h.assertTrue(d.getResources().getCurrentReiatsu()==100 && d.getStatus().getTechniqueSlot4CooldownTicks()==0,"Rejected cast charged resources");done(h,p);}
    @GameTest(template="empty",batch="beni_cleanup",timeoutTicks=70)
    public static void unequippingCancelsSupportAndAllRuntimeLeases(GameTestHelper h){var p=player(h,"bankai");var d=data(p);p.setHealth(10);ticks(h,p);BenihimeTechniqueService.executeSlot(p,d,3);
        h.runAfterDelay(10,()->{p.setItemSlot(EquipmentSlot.MAINHAND,ItemStack.EMPTY);RyujinTechniqueService.cancel(p);});
        h.runAfterDelay(45,()->{h.assertTrue(p.getHealth()==10 && !BenihimeTechniqueService.busy(p) && BenihimeTechniqueService.hud(p).isEmpty(),"Unequipping retained regeneration or leases");done(h,p);});}
}
