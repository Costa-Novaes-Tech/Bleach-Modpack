package com.bleachmod.gametest;

import com.bleachmod.common.data.*;
import com.bleachmod.common.technique.MasteryPracticeService;
import com.bleachmod.init.ModItems;
import com.bleachmod.server.commands.DevKitService;
import com.mojang.authlib.GameProfile;
import net.minecraft.gametest.framework.*;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.gametest.*;
import java.util.UUID;

@GameTestHolder("bleachmod") @PrefixGameTestTemplate(false)
public final class QuincyProgressGameTests {
    private static FakePlayer player(GameTestHelper h){
        var p=new FakePlayer(h.getLevel(),new GameProfile(UUID.randomUUID(),"QuincyProgressQA"));
        p.setPos(h.absoluteVec(new net.minecraft.world.phys.Vec3(2,3,2)));
        data(p).initializeShinigami();DevKitService.quincy(p,data(p));return p;
    }
    private static PlayerData data(FakePlayer p){return PlayerCapability.get(p).orElseThrow(()->new IllegalStateException("Capability missing"));}
    @GameTest(template="empty") public static void quincyMasterySurvivesKitSwapSaveAndTransientReset(GameTestHelper h){
        var p=player(h);var d=data(p);d.getQuincy().addPractice(1,37,23);
        DevKitService.shinigami(p,d,"benihime");d.getCharacter().setMastery("zanpakuto","sealed",71);
        DevKitService.quincy(p,d);DevKitService.resetCombat(p,d,true);
        var copy=new PlayerData();copy.load(d.save());
        h.assertTrue(copy.getQuincy().getMastery()==37&&copy.getQuincy().getTechniqueMastery(1)==23,"Quincy progress lost on swap/save/reset");
        h.assertTrue(copy.getCharacter().getZanpakutoMastery("benihime","sealed")==71,"Quincy overwrote Shinigami progress");
        var malformed=new net.minecraft.nbt.CompoundTag();malformed.putDouble("profileMastery",Double.NaN);
        copy.getQuincy().load(malformed);h.assertTrue(copy.getQuincy().getMastery()==0,"Nonfinite mastery accepted");h.succeed();
    }
    @GameTest(template="empty") public static void quincyTestPresetMaximizesOnlyItsOwnMastery(GameTestHelper h){
        var p=player(h);var d=data(p);double old=d.getCharacter().getMastery("zanpakuto","sealed");
        DevKitService.cheatsAll(p,d);for(int slot=1;slot<=4;slot++)h.assertTrue(d.getQuincy().getTechniqueMastery(slot)==100,"Preset missed Quincy mastery");
        h.assertTrue(d.getQuincy().getMastery()==100&&d.getCharacter().getMastery("zanpakuto","sealed")==old,"Preset conflated races");
        DevKitService.cheatsOff(p,d);DevKitService.quincy(p,d);h.assertTrue(d.getQuincy().getTechniqueMastery(4)==100,"Off/swap reset permanent mastery");h.succeed();
    }
    @GameTest(template="empty") public static void quincyMasteryRequiresRealPracticeAndKeepsAntiFarmRules(GameTestHelper h){
        var p=player(h);var d=data(p);p.setItemInHand(net.minecraft.world.InteractionHand.MAIN_HAND,new ItemStack(ModItems.QUINCY_BOW.get()));
        var enemy=net.minecraft.world.entity.EntityType.ZOMBIE.create(h.getLevel());enemy.setNoAi(true);enemy.setPos(p.position().add(0,0,2));h.getLevel().addFreshEntity(enemy);
        MasteryPracticeService.noteIntent(p);MasteryPracticeService.hurt(p,enemy,h.getLevel().damageSources().playerAttack(p),1,1);
        h.assertTrue(d.getQuincy().getMastery()==MasteryPracticeService.FORM_GAIN&&d.getQuincy().getTechniqueMastery(1)==MasteryPracticeService.TECHNIQUE_GAIN,"Confirmed Quincy hit not credited");
        MasteryPracticeService.control(p,enemy,1);h.assertTrue(d.getQuincy().getTechniqueMastery(1)==1,"Farm guard bypassed");
        d.getResources().setCostsDisabled(true);var other=net.minecraft.world.entity.EntityType.ZOMBIE.create(h.getLevel());other.setPos(p.position().add(1,0,2));h.getLevel().addFreshEntity(other);
        MasteryPracticeService.noteIntent(p);MasteryPracticeService.control(p,other,2);
        h.assertTrue(d.getQuincy().getTechniqueMastery(2)==0,"Test mode granted practice");enemy.discard();other.discard();h.succeed();
    }
}
