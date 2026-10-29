package com.bleachmod.gametest;

import com.bleachmod.common.data.*;
import com.bleachmod.common.quincy.*;
import com.bleachmod.init.ModItems;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.*;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.*;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.gametest.*;
import java.util.*;

@GameTestHolder("bleachmod") @PrefixGameTestTemplate(false)
public final class QuincyVollstandigGameTests {
    private static FakePlayer player(GameTestHelper h){var p=new FakePlayer(h.getLevel(),new GameProfile(UUID.randomUUID(),"ReishiTest"));p.setPos(h.absoluteVec(new Vec3(2.5,3,2.5)));p.setYRot(0);p.setXRot(0);data(p).initializeQuincy();p.setItemInHand(InteractionHand.MAIN_HAND,new ItemStack(ModItems.QUINCY_BOW.get()));return p;}
    private static PlayerData data(FakePlayer p){return PlayerCapability.get(p).orElseThrow(()->new IllegalStateException("capability"));}
    private static QuincyArrowEntity arrow(GameTestHelper h,FakePlayer p,Vec3 start){var a=new QuincyArrowEntity(h.getLevel(),p);a.setPos(start);a.configureVollstandig(1);a.shoot(1,0,0,3.6F,0);return a;}
    private static Zombie target(GameTestHelper h,Vec3 pos){var z=EntityType.ZOMBIE.create(h.getLevel());z.setPos(pos);z.setNoAi(true);z.setNoGravity(true);h.getLevel().addFreshEntity(z);return z;}
    @GameTest(template="empty",timeoutTicks=240)
    public static void activationCommitsOnceAndFiveArrowsCostOnce(GameTestHelper h){
        var p=player(h);var d=data(p);d.getQuincy().grantVollstandig();QuincyVollstandigService.toggle(p,d);
        h.assertTrue(!QuincyVollstandigService.active(p)&&QuincyVollstandigService.preparing(p),"Preparation does not grant passive");
        h.assertTrue(d.getResources().getCurrentReiatsu()==100,"No upfront charge for cancellable preparation");
        h.runAtTickTime(180,()->{
            QuincyVollstandigService.tick(p,d);
            h.assertTrue(QuincyVollstandigService.active(p),"Server commits learned form");
            // At age 180 both entrance and first maintenance may be due: measure the actual baseline.
            float before=d.getResources().getCurrentReiatsu();QuincyVollstandigService.tick(p,d);
            h.assertTrue(d.getResources().getCurrentReiatsu()==before,"A repeated service tick cannot charge maintenance twice");
            var box=p.getBoundingBox().inflate(5);int old=h.getLevel().getEntitiesOfClass(QuincyArrowEntity.class,box).size();
            var bow=(QuincyBowItem)p.getMainHandItem().getItem();bow.releaseUsing(p.getMainHandItem(),h.getLevel(),p,72000-12);
            var shots=h.getLevel().getEntitiesOfClass(QuincyArrowEntity.class,box);
            h.assertTrue(shots.size()-old==5,"One release spawns central arrow and exactly four satellites");
            h.assertTrue(shots.stream().filter(QuincyArrowEntity::isVollstandigArrow).count()==1,"Only central projectile pierces");
            h.assertTrue(d.getResources().getCurrentReiatsu()==before-4,"Five projectiles charge one 4 PE volley");
            h.assertTrue(QuincyBowItem.chargeTicks(p)==12,"Faster full charge");
            d.getResources().setCurrentReiatsu(100);
            QuincyTechniqueService.executeSlot(p,d,1);h.assertTrue(d.getResources().getCurrentReiatsu()==84&&d.getStatus().getTechniqueSlot1CooldownTicks()==120,"Advanced H fires and charges once");
            QuincyTechniqueService.executeSlot(p,d,1);h.assertTrue(d.getResources().getCurrentReiatsu()==84,"H cooldown blocks duplicate fire");
            QuincyTechniqueService.executeSlot(p,d,2);h.assertTrue(d.getResources().getCurrentReiatsu()==59&&d.getStatus().getTechniqueSlot2CooldownTicks()==240,"Advanced N schedules finite arsenal");
            QuincyTechniqueService.executeSlot(p,d,4);h.assertTrue(d.getResources().getCurrentReiatsu()==19&&d.getStatus().getTechniqueSlot4CooldownTicks()==400,"Advanced C schedules warned detonation");
            shots.forEach(a->a.discard());QuincyVollstandigService.stop(p,d);
            h.assertTrue(!d.getQuincy().isVollstandigActive()&&QuincyBowItem.chargeTicks(p)==20,"Manual exit restores base bow");h.succeed();
        });
    }
    @GameTest(template="empty") public static void interruptedPreparationPreservesEnergyAndLearning(GameTestHelper h){
        var p=player(h);var d=data(p);d.getQuincy().grantVollstandig();QuincyVollstandigService.toggle(p,d);p.setPos(p.position().add(3,0,0));QuincyVollstandigService.tick(p,d);
        h.assertTrue(!QuincyVollstandigService.preparing(p)&&!QuincyVollstandigService.active(p),"Movement cancels preparation");h.assertTrue(d.getResources().getCurrentReiatsu()==100,"Cancelled preparation costs nothing");
        var saved=d.getQuincy().save();var copy=new QuincyData();copy.load(saved);h.assertTrue(copy.hasVollstandig()&&!copy.isVollstandigActive(),"Learned form persists independently of activation");h.succeed();
    }
    @GameTest(template="empty") public static void mainArrowHitsFiveDistinctTargetsAndStopsBeforeSixth(GameTestHelper h){
        var p=player(h);Vec3 start=h.absoluteVec(new Vec3(2.5,160,2.5));List<Zombie> targets=new ArrayList<>();
        for(int i=0;i<6;i++)targets.add(target(h,start.add(3+i*2, -.5,0)));
        var a=arrow(h,p,start);try{
            for(int i=0;i<8&&!a.isRemoved();i++)a.tick();
            for(int i=0;i<5;i++)h.assertTrue(targets.get(i).getHealth()<20,"Target "+i+" takes central arrow damage: start="+start+" pos="+a.position()+" removed="+a.isRemoved()+" hits="+a.piercedEntityCount()+" blocks="+a.piercedBlockCount()+" hp="+targets.stream().map(z->z.getHealth()).toList());
            h.assertTrue(targets.get(5).getHealth()==20&&a.piercedEntityCount()==5&&a.isRemoved(),"Sixth target untouched after five distinct hits");
        }finally{a.discard();targets.forEach(z->z.discard());}h.succeed();
    }
    @GameTest(template="empty") public static void twoSeparatedBlocksPassThirdStopsAndTerrainRemains(GameTestHelper h){
        var p=player(h);Vec3 start=h.absoluteVec(new Vec3(2.5,170.5,2.5));var wall1=BlockPos.containing(start.add(3,0,0));var wall2=BlockPos.containing(start.add(7,0,0));var wall3=BlockPos.containing(start.add(11,0,0));
        var z1=target(h,start.add(9,-.5,0));var z2=target(h,start.add(14,-.5,0));var a=arrow(h,p,start);
        try{for(var b:List.of(wall1,wall2,wall3))h.getLevel().setBlockAndUpdate(b,Blocks.STONE.defaultBlockState());
            for(int i=0;i<8&&!a.isRemoved();i++)a.tick();
            h.assertTrue(z1.getHealth()<20,"Target after two separated blocks hit: start="+start+" pos="+a.position()+" removed="+a.isRemoved()+" blocks="+a.piercedBlockCount()+" hits="+a.piercedEntityCount());h.assertTrue(z2.getHealth()==20&&a.isRemoved(),"Third solid block stops arrow before target");h.assertTrue(a.piercedBlockCount()==2,"Block budget does not reset between walls");
            for(var b:List.of(wall1,wall2,wall3))h.assertTrue(h.getLevel().getBlockState(b).is(Blocks.STONE),"No block destruction");
            var saved=new net.minecraft.nbt.CompoundTag();a.addAdditionalSaveData(saved);var restored=arrow(h,p,start);restored.readAdditionalSaveData(saved);h.assertTrue(restored.piercedBlockCount()==2&&restored.piercedEntityCount()==1,"Saved flight keeps consumed budgets");restored.discard();
        }finally{a.discard();z1.discard();z2.discard();for(var b:List.of(wall1,wall2,wall3))h.getLevel().setBlockAndUpdate(b,Blocks.AIR.defaultBlockState());}h.succeed();
    }
    @GameTest(template="empty") public static void satellitesCannotInheritMainPiercing(GameTestHelper h){
        var p=player(h);Vec3 start=h.absoluteVec(new Vec3(2.5,180.5,2.5));var wall=BlockPos.containing(start.add(3,0,0));var a=arrow(h,p,start);a.configureSatellite(1.5F);
        try{h.getLevel().setBlockAndUpdate(wall,Blocks.STONE.defaultBlockState());for(int i=0;i<4&&!a.isRemoved();i++)a.tick();h.assertTrue(!a.isVollstandigArrow()&&a.isRemoved(),"Auxiliary dissipates on first solid block: start="+start+" pos="+a.position()+" removed="+a.isRemoved()+" piercing="+a.isVollstandigArrow());}
        finally{a.discard();h.getLevel().setBlockAndUpdate(wall,Blocks.AIR.defaultBlockState());}h.succeed();
    }
    @GameTest(template="empty",timeoutTicks=240) public static void convergenceWarnsThenDamagesOnlySixteenTargetsOnce(GameTestHelper h){
        var p=player(h);p.setPos(h.absoluteVec(new Vec3(2.5,210,2.5)));var d=data(p);d.getQuincy().grantVollstandig();QuincyVollstandigService.toggle(p,d);
        h.runAtTickTime(180,()->{
            QuincyVollstandigService.tick(p,d);d.getResources().setCurrentReiatsu(100);
            Vec3 center=p.getEyePosition().add(p.getLookAngle().scale(32)).add(0,.15,0);List<Zombie> victims=new ArrayList<>();
            for(int i=0;i<18;i++){var z=target(h,center.add((i%6-2.5)*1.2,-.5,(i/6-1)*1.2));z.setItemSlot(net.minecraft.world.entity.EquipmentSlot.HEAD,new ItemStack(net.minecraft.world.item.Items.IRON_HELMET));victims.add(z);}
            QuincyTechniqueService.executeSlot(p,d,4);h.assertTrue(d.getResources().getCurrentReiatsu()==60,"C charges 40 PE once");
            h.assertTrue(victims.stream().allMatch(z->z.getHealth()==20),"Warning does not deal immediate damage");
            h.runAfterDelay(30,()->{try{
                QuincyVollstandigService.tick(p,d);
                h.assertTrue(victims.stream().filter(z->z.getHealth()<20).count()==16,"Detonation applies to at most sixteen targets");
                var health=victims.stream().map(z->z.getHealth()).toList();QuincyVollstandigService.tick(p,d);
                h.assertTrue(health.equals(victims.stream().map(z->z.getHealth()).toList()),"Convergence cannot detonate twice");
                h.succeed();
            }finally{QuincyVollstandigService.stop(p,d);victims.forEach(z->z.discard());}});
        });
    }
    @GameTest(template="empty",timeoutTicks=220) public static void exhaustionEndsFormWithoutErasingLearnedMastery(GameTestHelper h){
        var p=player(h);var d=data(p);d.getQuincy().grantVollstandig();d.getQuincy().practiceAdvanced(1);QuincyVollstandigService.toggle(p,d);
        h.runAtTickTime(180,()->{
            QuincyVollstandigService.tick(p,d);h.assertTrue(QuincyVollstandigService.active(p),"Committed before exhaustion");
            d.getResources().setCurrentReiatsu(0);QuincyVollstandigService.tick(p,d);
            h.assertTrue(!QuincyVollstandigService.active(p)&&!d.getQuincy().isVollstandigActive(),"Exhaustion returns to base immediately");
            h.assertTrue(d.getQuincy().hasVollstandig()&&d.getQuincy().getAdvancedMastery(1)==1,"Learning and mastery preserved");h.succeed();
        });
    }
}
