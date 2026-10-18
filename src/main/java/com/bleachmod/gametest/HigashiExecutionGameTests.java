package com.bleachmod.gametest;

import com.bleachmod.common.data.*;
import com.bleachmod.common.technique.*;
import com.bleachmod.init.ModItems;
import com.mojang.authlib.GameProfile;
import net.minecraft.gametest.framework.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.*;
import net.minecraft.world.effect.*;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.gametest.*;
import java.util.*;

@GameTestHolder("bleachmod") @PrefixGameTestTemplate(false)
public final class HigashiExecutionGameTests {
    private static PlayerData d(FakePlayer p){return PlayerCapability.get(p).orElseThrow(()->new IllegalStateException("Missing capability"));}
    private static FakePlayer player(GameTestHelper h){var p=new FakePlayer(h.getLevel(),new GameProfile(UUID.randomUUID(),"ExecuteTest"));var at=h.absolutePos(new net.minecraft.core.BlockPos(2,2,2));p.setPos(at.getX()+.5,h.getLevel().getMaxBuildHeight()-100,at.getZ()+.5);p.setYRot(0);p.setXRot(0);p.setNoGravity(true);h.getLevel().getChunkAt(p.blockPosition());
        var d=d(p);d.initializeShinigami();d.getCharacter().bindZanpakuto("ryujin_jakka");d.getSkills().get("zanpakuto").setLevel(2);d.getCharacter().unlockForm("bankai");d.getCharacter().unlockForm("shikai");d.getCharacter().setMastery("zanpakuto","shikai",100);d.getCharacter().setMastery("zanpakuto","bankai",100);d.getCharacter().preserveLegacyTechniques();d.getCharacter().setActiveForm("zanpakuto","bankai");p.setItemSlot(EquipmentSlot.MAINHAND,new ItemStack(ModItems.RYUJIN_JAKKA.get()));return p;
    }
    private static Zombie target(GameTestHelper h,FakePlayer p,double x,float health){var t=EntityType.ZOMBIE.create(h.getLevel());t.setNoAi(true);t.setNoGravity(true);t.setPos(p.position().add(x,0,3));t.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH).setBaseValue(1000);t.setHealth(health);t.setItemSlot(EquipmentSlot.HEAD,new ItemStack(Items.NETHERITE_HELMET));h.getLevel().addFreshEntity(t);return t;}
    @GameTest(template="empty",batch="execution_defense",timeoutTicks=40)
    public static void sevenPercentExecutionBypassesArmorResistanceAbsorptionAndTotem(GameTestHelper h){var p=player(h);var t=target(h,p,0,70);t.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE,200,255));t.setAbsorptionAmount(200);t.invulnerableTime=100;t.setItemSlot(EquipmentSlot.OFFHAND,new ItemStack(Items.TOTEM_OF_UNDYING));TechniqueService.executeSlot(p,d(p),2);
        h.assertTrue(t.isAlive(),"Execution happened before impact");h.runAfterDelay(15,()->{try{h.assertTrue(!t.isAlive(),"Threshold victim survived defensive layers");h.assertTrue(t.getLastDamageSource()!=null&&t.getLastDamageSource().is(HigashiExecutionService.damageKey()),"Death did not use the execution source");h.assertTrue(d(p).getResources().getCurrentReiatsu()==75,"Execution added another cost");}finally{RyujinTechniqueService.cancel(p);t.discard();}h.succeed();});}
    @GameTest(template="empty",batch="execution_single",timeoutTicks=40)
    public static void onlyOneLowHealthVictimGetsExecutedPerHigashi(GameTestHelper h){var p=player(h);var center=target(h,p,0,69);var side=target(h,p,.8,69);TechniqueService.executeSlot(p,d(p),2);
        h.runAfterDelay(15,()->{try{h.assertTrue(!center.isAlive()&&side.isAlive(),"Execution did not choose the crosshair victim or executed the whole cone");}finally{RyujinTechniqueService.cancel(p);center.discard();side.discard();}h.succeed();});}
    @GameTest(template="empty",batch="execution_threshold",timeoutTicks=40)
    public static void aboveThresholdIsNormalDamageAndAlliesRemainProtected(GameTestHelper h){var p=player(h);var above=target(h,p,0,71);above.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE,200,255));var ally=target(h,p,.5,50);var protectedMob=target(h,p,-.5,50);protectedMob.setInvulnerable(true);
        var score=h.getLevel().getScoreboard();var team=score.addPlayerTeam("exe"+UUID.randomUUID().toString().substring(0,7));score.addPlayerToTeam(p.getScoreboardName(),team);score.addPlayerToTeam(ally.getScoreboardName(),team);TechniqueService.executeSlot(p,d(p),2);
        h.runAfterDelay(15,()->{try{h.assertTrue(above.isAlive()&&above.getHealth()==71&&ally.getHealth()==50&&protectedMob.getHealth()==50,"Threshold, ally or invulnerability policy bypassed");}finally{score.removePlayerTeam(team);RyujinTechniqueService.cancel(p);above.discard();ally.discard();protectedMob.discard();}h.succeed();});}
}
