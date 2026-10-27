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

    @GameTest(template="empty",batch="execution_healed",timeoutTicks=40)
    public static void previewDoesNotExecuteVictimHealedBeforeImpact(GameTestHelper h){var p=player(h);var t=target(h,p,0,70);t.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE,200,255));TechniqueService.executeSlot(p,d(p),2);
        h.runAfterDelay(6,()->t.setHealth(200));h.runAfterDelay(15,()->{try{h.assertTrue(t.isAlive()&&t.getHealth()==200,"Cinematic preview committed a kill despite healing");h.assertTrue(d(p).getResources().getCurrentReiatsu()==75,"Preview duplicated the cost");}finally{RyujinTechniqueService.cancel(p);t.discard();}h.succeed();});}

    @GameTest(template="empty",batch="execution_cancel_preview",timeoutTicks=40)
    public static void canceledCastAfterPreviewCannotKill(GameTestHelper h){var p=player(h);var t=target(h,p,0,70);TechniqueService.executeSlot(p,d(p),2);h.runAfterDelay(6,()->RyujinTechniqueService.cancel(p));
        h.runAfterDelay(15,()->{try{h.assertTrue(t.isAlive()&&t.getHealth()==70,"Preview escaped normal cast cancellation");}finally{RyujinTechniqueService.cancel(p);t.discard();}h.succeed();});}

    @GameTest(template="empty",batch="execution_lethal",timeoutTicks=40)
    public static void normalLethalDamageAboveSevenPercentConfirmsDeathWithoutExecutionBypass(GameTestHelper h){
        var p=player(h);var t=target(h,p,0,10);
        t.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH).setBaseValue(20);
        t.setItemSlot(EquipmentSlot.HEAD,ItemStack.EMPTY);t.setHealth(10);
        h.assertTrue(!HigashiExecutionService.eligible(p,t),"Lethal case accidentally uses threshold bypass");
        var origin=p.position();TechniqueService.executeSlot(p,d(p),2);
        h.runAfterDelay(15,()->{try{
            h.assertTrue(p.position().distanceTo(origin)>=1.8&&p.position().distanceTo(origin)<=2.801,"Higashi approach is missing or exceeds its safe bound");
            h.assertTrue(!t.isAlive(),"Normal lethal damage did not kill above threshold");
            h.assertTrue(t.getLastDamageSource()!=null&&!t.getLastDamageSource().is(HigashiExecutionService.damageKey()),"Normal lethality gained defense bypass");
            h.assertTrue(d(p).getResources().getCurrentReiatsu()==75,"Normal lethal scene charged twice");
        }finally{RyujinTechniqueService.cancel(p);t.discard();}h.succeed();});
    }

    @GameTest(template="empty",batch="execution_timing")
    public static void cinematicHoldsForOneSecondAfterBladePassesAndReturnsAfterAsh(GameTestHelper h){
        var path=com.bleachmod.common.visual.HigashiCinematicPath.class;
        h.assertTrue(com.bleachmod.common.visual.HigashiCinematicPath.IGNITE-com.bleachmod.common.visual.HigashiCinematicPath.SLASH_END==20,"Dramatic hold is not one game second");
        var a=com.bleachmod.common.visual.RyujinChoreography.execution(16,1);
        var b=com.bleachmod.common.visual.RyujinChoreography.execution(35,1);
        h.assertTrue(a.arm().distanceTo(b.arm())<1e-8&&a.weight()==1&&b.weight()==1,"Follow-through moves before ignition");
        h.assertTrue(com.bleachmod.common.visual.HigashiCinematicPath.END>com.bleachmod.common.visual.HigashiCinematicPath.IGNITE+20,"Camera returns before incineration completes");
        h.succeed();
    }

    public static final class CancelDeathListener {
        private final LivingEntity target;
        public CancelDeathListener(LivingEntity target) { this.target = target; }
        @net.minecraftforge.eventbus.api.SubscribeEvent(priority = net.minecraftforge.eventbus.api.EventPriority.LOW)
        public void onDeath(net.minecraftforge.event.entity.living.LivingDeathEvent e) {
            if (e.getEntity() == target) {
                e.setCanceled(true);
                target.setHealth(10.0F);
            }
        }
    }

    @GameTest(template="empty",batch="execution_cancel_listener",timeoutTicks=60)
    public static void normalLethalDamageCancelledByLaterListenerLeavesEntityAliveAndZeroConfirmed(GameTestHelper h){
        var p=player(h);var t=target(h,p,0,10);
        t.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH).setBaseValue(20);
        t.setItemSlot(EquipmentSlot.HEAD,ItemStack.EMPTY);t.setHealth(10);
        HigashiExecutionService.resetTestCounters();
        var listener=new CancelDeathListener(t);
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(listener);
        TechniqueService.executeSlot(p,d(p),2);
        h.runAfterDelay(15,()->{
            try{
                h.assertTrue(t.isAlive(),"Target was killed despite LivingDeathEvent cancellation");
                h.assertTrue(t.getHealth()==10.0F,"Target health was not preserved at 10 HP");
                h.assertTrue(HigashiExecutionService.confirmedCountFor(t.getUUID())==0,"Cancelled death emitted confirmed execution");
                h.assertTrue(!t.getPersistentData().getBoolean("bleachmod_higashi_confirmed"),"Cancelled death left persistent execution lock");
                net.minecraftforge.common.MinecraftForge.EVENT_BUS.unregister(listener);
                RyujinTechniqueService.cancel(p);
                d(p).getStatus().setTechniqueSlot2CooldownTicks(0);
                d(p).getResources().setCurrentReiatsu(d(p).getResources().getMaxReiatsu());
                t.invulnerableTime = 0;
                TechniqueService.executeSlot(p,d(p),2);
                h.runAfterDelay(15,()->{
                    try{
                        h.assertTrue(!t.isAlive(),"Target survived uncancelled second lethal strike");
                        h.assertTrue(HigashiExecutionService.confirmedCountFor(t.getUUID())==1,"Uncancelled strike did not produce exactly 1 confirmed execution");
                    }finally{
                        RyujinTechniqueService.cancel(p);t.discard();
                    }
                    h.succeed();
                });
            }catch(Throwable err){
                net.minecraftforge.common.MinecraftForge.EVENT_BUS.unregister(listener);
                RyujinTechniqueService.cancel(p);t.discard();
                throw err;
            }
        });
    }

    @GameTest(template="empty",batch="execution_totem_lethal",timeoutTicks=40)
    public static void totemOfUndyingPreventsHigashiExecutionOnNormalLethalDamage(GameTestHelper h){
        var p=player(h);var t=target(h,p,0,10);
        t.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH).setBaseValue(20);
        t.setItemSlot(EquipmentSlot.HEAD,ItemStack.EMPTY);t.setHealth(10);
        t.setItemSlot(EquipmentSlot.OFFHAND,new ItemStack(Items.TOTEM_OF_UNDYING));
        HigashiExecutionService.resetTestCounters();
        TechniqueService.executeSlot(p,d(p),2);
        h.runAfterDelay(15,()->{
            try{
                h.assertTrue(t.isAlive(),"Totem holder was killed by normal lethal Higashi damage");
                h.assertTrue(HigashiExecutionService.confirmedCountFor(t.getUUID())==0,"Totem survivor received confirmed execution");
                h.assertTrue(!t.getPersistentData().getBoolean("bleachmod_higashi_confirmed"),"Totem survivor retained confirmed execution flag");
            }finally{
                RyujinTechniqueService.cancel(p);t.discard();
            }
            h.succeed();
        });
    }

    public static final class SecondaryKillListener {
        private final LivingEntity target;
        private final LivingEntity secondary;
        public SecondaryKillListener(LivingEntity target, LivingEntity secondary) { this.target = target; this.secondary = secondary; }
        @net.minecraftforge.eventbus.api.SubscribeEvent(priority = net.minecraftforge.eventbus.api.EventPriority.HIGHEST)
        public void onHurt(net.minecraftforge.event.entity.living.LivingHurtEvent e) {
            if (e.getEntity() == target) {
                secondary.kill();
            }
        }
    }

    @GameTest(template="empty",batch="execution_secondary_death",timeoutTicks=40)
    public static void reflectedDamageAndSecondaryDeathCannotTriggerHigashiExecution(GameTestHelper h){
        var p=player(h);var t=target(h,p,0,10);
        t.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH).setBaseValue(20);
        t.setItemSlot(EquipmentSlot.HEAD,ItemStack.EMPTY);t.setHealth(10);
        var secondary=target(h,p,5,1);
        HigashiExecutionService.resetTestCounters();
        var listener=new SecondaryKillListener(t,secondary);
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(listener);
        TechniqueService.executeSlot(p,d(p),2);
        h.runAfterDelay(15,()->{
            try{
                net.minecraftforge.common.MinecraftForge.EVENT_BUS.unregister(listener);
                h.assertTrue(!secondary.isAlive(),"Secondary mob was not killed during cast");
                h.assertTrue(HigashiExecutionService.confirmedCountFor(secondary.getUUID())==0,"Secondary mob received Higashi confirmed execution event");
                h.assertTrue(HigashiExecutionService.confirmedCountFor(p.getUUID())==0,"Caster received Higashi confirmed execution event");
                h.assertTrue(!t.isAlive(),"True target was not killed by lethal Higashi strike");
                h.assertTrue(HigashiExecutionService.confirmedCountFor(t.getUUID())==1,"True target did not receive exactly 1 execution");
            }finally{
                net.minecraftforge.common.MinecraftForge.EVENT_BUS.unregister(listener);
                RyujinTechniqueService.cancel(p);t.discard();secondary.discard();
            }
            h.succeed();
        });
    }

    @GameTest(template="empty",batch="execution_single_count",timeoutTicks=40)
    public static void singleConfirmationForNormalLethalAndThresholdExecution(GameTestHelper h){
        var p=player(h);var thresholdTarget=target(h,p,0,70);
        HigashiExecutionService.resetTestCounters();
        TechniqueService.executeSlot(p,d(p),2);
        h.runAfterDelay(15,()->{
            try{
                h.assertTrue(!thresholdTarget.isAlive(),"Threshold target survived");
                h.assertTrue(HigashiExecutionService.confirmedCountFor(thresholdTarget.getUUID())==1,"Threshold target received multiple or zero confirmations");
                h.assertTrue(HigashiExecutionService.totalConfirmedCount()==1,"Total confirmed count exceeded 1");
            }finally{
                RyujinTechniqueService.cancel(p);thresholdTarget.discard();
            }
            h.succeed();
        });
    }
}
