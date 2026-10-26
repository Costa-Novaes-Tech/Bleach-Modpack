package com.bleachmod.gametest;

import com.bleachmod.common.data.*;
import com.bleachmod.common.technique.*;
import com.bleachmod.entity.IceDragonEntity;
import com.bleachmod.init.ModItems;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.*;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.gametest.*;
import java.util.*;

@GameTestHolder("bleachmod") @PrefixGameTestTemplate(false)
public final class ElementalReworkGameTests {
    private static PlayerData d(FakePlayer p){return PlayerCapability.get(p).orElseThrow(()->new IllegalStateException("Missing capability"));}
    private static FakePlayer player(GameTestHelper h,String identity,String form){var p=new FakePlayer(h.getLevel(),new GameProfile(UUID.randomUUID(),"ElementalRework"));var pos=h.absolutePos(new BlockPos(2,2,2));
        p.setPos(pos.getX()+.5,h.getLevel().getMaxBuildHeight()-70,pos.getZ()+.5);p.setYRot(0);p.setXRot(0);p.setNoGravity(true);p.setOnGround(true);h.getLevel().getChunkAt(p.blockPosition());var d=d(p);d.initializeShinigami();d.getCharacter().bindZanpakuto(identity);d.getCharacter().preserveLegacyTechniques();
        d.getSkills().get("zanpakuto").setLevel(2);d.getCharacter().unlockForm("shikai");d.getCharacter().unlockForm("bankai");d.getCharacter().setMastery("zanpakuto","shikai",100);d.getCharacter().setMastery("zanpakuto","bankai",100);d.getCharacter().setActiveForm("zanpakuto",form);
        p.setItemSlot(EquipmentSlot.MAINHAND,new ItemStack(identity.equals("hyorinmaru")?ModItems.HYORINMARU.get():ModItems.RYUJIN_JAKKA.get()));return p;}
    private static Zombie target(GameTestHelper h,FakePlayer p,double x,double z){var t=EntityType.ZOMBIE.create(h.getLevel());t.setNoAi(true);t.setNoGravity(true);t.setItemSlot(EquipmentSlot.HEAD,new ItemStack(Items.IRON_HELMET));t.setPos(p.position().add(x,0,z));t.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH).setBaseValue(80);t.setHealth(80);h.getLevel().getChunkAt(t.blockPosition());h.getLevel().addFreshEntity(t);return t;}
    private static float expectedHit(FakePlayer p,LivingEntity t,float raw){
        float scaled=com.bleachmod.common.CombatBalance.outgoingDamage(raw,d(p).getAttributes().level(AttributeData.ZANJUTSU),com.bleachmod.common.CombatBalance.formDamageBonus(d(p).getCharacter().getActiveForm()));
        return net.minecraft.world.damagesource.CombatRules.getDamageAfterAbsorb(scaled,t.getArmorValue(),(float)t.getAttributeValue(net.minecraft.world.entity.ai.attributes.Attributes.ARMOR_TOUGHNESS));
    }
    private static void clean(FakePlayer p,LivingEntity... targets){RyujinTechniqueService.cancel(p);for(var t:targets)t.discard();}
    @GameTest(template="empty",batch="bankai_no_weather")
    public static void bankaiAndGlacialZoneDoNotChangeClearOrNaturalStormWeather(GameTestHelper h){
        var p=player(h,"hyorinmaru","bankai");var level=h.getLevel();var w=level.getServer().getWorldData().overworldData();
        var prior=new int[]{w.getClearWeatherTime(),w.getRainTime(),w.getThunderTime()};boolean rain=w.isRaining(),thunder=w.isThundering();float r=level.getRainLevel(1),t=level.getThunderLevel(1);
        try{
            for(boolean storm:List.of(false,true)){
                level.setWeatherParameters(storm?0:400,800,storm,storm);level.setRainLevel(storm?1:0);level.setThunderLevel(storm?.7F:0);
                for(int i=0;i<50;i++)HyorinmaruTechniqueService.tick(p,d(p));
                d(p).getStatus().clearCooldowns();TechniqueService.executeSlot(p,d(p),4);HyorinmaruTechniqueService.cancel(p);
                h.assertTrue(w.isRaining()==storm && w.isThundering()==storm && w.getClearWeatherTime()==(storm?0:400) && w.getRainTime()==800,"Bankai changed weather flags/timers");
                h.assertTrue(level.getRainLevel(1)==(storm?1:0),"Bankai changed precipitation intensity");
            }
        }finally{clean(p);w.setClearWeatherTime(prior[0]);w.setRainTime(prior[1]);w.setThunderTime(prior[2]);w.setRaining(rain);w.setThundering(thunder);level.setRainLevel(r);level.setThunderLevel(r>0?t/r:0);}h.succeed();
    }
    @GameTest(template="empty",batch="ice_eruption_new",timeoutTicks=90)
    public static void eruptionArrivesAsWaveAndRupturesOnlyHitGroundedTargets(GameTestHelper h){
        var p=player(h,"hyorinmaru","bankai");var t=target(h,p,0,24);var side=target(h,p,22,24);var rear=target(h,p,0,-5);
        var floor=new ArrayList<BlockPos>();for(int z=0;z<=25;z++){var b=p.blockPosition().offset(0,-1,z);floor.add(b);h.getLevel().setBlock(b,net.minecraft.world.level.block.Blocks.STONE.defaultBlockState(),3);}
        TechniqueService.executeSlot(p,d(p),3);
        h.runAfterDelay(15,()->h.assertTrue(t.getHealth()==80,"Eruption hit before wave arrived"));
        h.runAfterDelay(65,()->{try{
            h.assertTrue(Math.abs(t.getHealth()-(80-expectedHit(p,t,8)-expectedHit(p,t,16)))<2.1,"Expected one arrival hit and one rupture after zombie armor; health="+t.getHealth());
            h.assertTrue(side.getHealth()==80 && rear.getHealth()==80,"Eruption escaped its frontal fan");
            h.assertTrue(d(p).getResources().getCurrentReiatsu()==65,"Eruption charged more than once");
        }finally{clean(p,t,side,rear);for(var b:floor)h.getLevel().setBlock(b,net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(),3);}h.succeed();});
    }
    @GameTest(template="empty",batch="ryusenka_rupture",timeoutTicks=80)
    public static void ryusenkaSeparatesContactFromDelayedCocoonRupture(GameTestHelper h){
        var p=player(h,"hyorinmaru","bankai");var t=target(h,p,0,4);TechniqueService.executeSlot(p,d(p),1);
        h.runAfterDelay(25,()->h.assertTrue(Math.abs(t.getHealth()-(80-expectedHit(p,t,8)))<2.1,"Ryusenka contact before pause, after armor; health="+t.getHealth()));
        h.runAfterDelay(55,()->{try{h.assertTrue(Math.abs(t.getHealth()-(80-expectedHit(p,t,8)-expectedHit(p,t,6)))<2.1,"Ryusenka delayed rupture after armor; health="+t.getHealth());}finally{clean(p,t);}h.succeed();});
    }
    @GameTest(template="empty",batch="fragility_new")
    public static void fragilityConsumesOneCasterStrikeAndIgnoresEnvironmentalDamage(GameTestHelper h){
        var p=player(h,"hyorinmaru","bankai");var t=target(h,p,0,4);GlacialFragilityService.mark(p,t);
        var environment=new net.minecraftforge.event.entity.living.LivingHurtEvent(t,t.damageSources().freeze(),10);GlacialFragilityService.hurt(environment);
        var first=new net.minecraftforge.event.entity.living.LivingHurtEvent(t,p.damageSources().playerAttack(p),10);GlacialFragilityService.hurt(first);
        var second=new net.minecraftforge.event.entity.living.LivingHurtEvent(t,p.damageSources().playerAttack(p),10);GlacialFragilityService.hurt(second);
        h.assertTrue(environment.getAmount()==10 && first.getAmount()==11.5F && second.getAmount()==10,"Fragility was consumed by DOT or more than once");clean(p,t);h.succeed();
    }
    @GameTest(template="empty",batch="new_profiles")
    public static void bothElementalProfilesUseDistinctAnimationsWithoutDamageInflation(GameTestHelper h){
        var fire=net.bettercombat.logic.WeaponRegistry.getAttributes(new ItemStack(ModItems.RYUJIN_JAKKA.get()));var ice=net.bettercombat.logic.WeaponRegistry.getAttributes(new ItemStack(ModItems.HYORINMARU.get()));
        h.assertTrue(fire!=null && ice!=null && fire.attacks().length==3 && ice.attacks().length==3,"Missing elemental combos");
        h.assertTrue(!fire.attacks()[0].animation().equals(ice.attacks()[0].animation()),"Identical opening movements");
        for(var profile:List.of(fire,ice))for(var a:profile.attacks())h.assertTrue(a.damageMultiplier()==1,"Combo added an extra damage multiplier");h.succeed();
    }
    @GameTest(template="empty",batch="new_sealed")
    public static void sealedFireStaysDisabledWhileIceArmorUsesItsNewPaidSlot(GameTestHelper h){var p=player(h,"ryujin_jakka","sealed");
        TechniqueService.executeSlot(p,d(p),3);TechniqueService.executeSlot(p,d(p),4);h.assertTrue(d(p).getResources().getCurrentReiatsu()==100 && !ElementalTechniqueService.busy(p),"Sealed fire still casts elemental area/summons");
        d(p).getCharacter().bindZanpakuto("hyorinmaru");d(p).getCharacter().preserveLegacyTechniques();p.setItemSlot(EquipmentSlot.MAINHAND,new ItemStack(ModItems.HYORINMARU.get()));TechniqueService.executeSlot(p,d(p),3);
        h.assertTrue(IceArmorService.active(p) && d(p).getResources().getCurrentReiatsu()==88 && d(p).getStatus().getTechniqueSlot3CooldownTicks()==240,"Sealed armor did not use its approved cost and cooldown");clean(p);h.succeed();}
    @GameTest(template="empty",batch="new_sealed_cut",timeoutTicks=40)
    public static void sealedCutHasPreparationAndHitsAtFiveBlocks(GameTestHelper h){var p=player(h,"hyorinmaru","sealed");var t=target(h,p,0,5);TechniqueService.executeSlot(p,d(p),1);
        h.assertTrue(t.getHealth()==80,"Cut applied damage before its gesture");h.runAfterDelay(10,()->{try{h.assertTrue(t.getHealth()<80 && d(p).getResources().getCurrentReiatsu()==92,"Prepared physical cut failed");}finally{clean(p,t);}h.succeed();});}
    @GameTest(template="empty",batch="new_rajada",timeoutTicks=45)
    public static void rajadaTravelsNarrowlyAndChargesOnce(GameTestHelper h){var p=player(h,"ryujin_jakka","shikai");var front=target(h,p,0,6);var side=target(h,p,3,3);TechniqueService.executeSlot(p,d(p),1);TechniqueService.executeSlot(p,d(p),1);
        h.assertTrue(front.getHealth()==80 && d(p).getResources().getCurrentReiatsu()==85,"Rajada hit instantly or charged twice");
        h.runAfterDelay(18,()->{try{h.assertTrue(front.getHealth()<80 && side.getHealth()==80,"Rajada is not a narrow traveling front");}finally{clean(p,front,side);}h.succeed();});}
    @GameTest(template="empty",batch="new_leque",timeoutTicks=45)
    public static void lequeSweepsFromRightToLeftInsteadOfHittingConeAtOnce(GameTestHelper h){var p=player(h,"ryujin_jakka","shikai");var early=target(h,p,-4.3,2.5);var late=target(h,p,4.3,2.5);TechniqueService.executeSlot(p,d(p),2);
        h.runAfterDelay(10,()->h.assertTrue(early.getHealth()<80 && late.getHealth()==80,"Leque did not resolve in angular order"));
        h.runAfterDelay(24,()->{try{h.assertTrue(late.getHealth()<80,"Leque never reached its other side");}finally{clean(p,early,late);}h.succeed();});}
    @GameTest(template="empty",batch="new_higashi",timeoutTicks=45)
    public static void higashiIsAnOffensiveShortCutAndPreservesAllies(GameTestHelper h){var p=player(h,"ryujin_jakka","bankai");var near=target(h,p,0,3);var far=target(h,p,0,7);var ally=target(h,p,.5,2);
        var scoreboard=h.getLevel().getScoreboard();var team=scoreboard.addPlayerTeam("hig"+UUID.randomUUID().toString().substring(0,7));scoreboard.addPlayerToTeam(p.getScoreboardName(),team);scoreboard.addPlayerToTeam(ally.getScoreboardName(),team);
        TechniqueService.executeSlot(p,d(p),2);h.runAfterDelay(16,()->{try{h.assertTrue(near.getHealth()<80 && far.getHealth()==80 && ally.getHealth()==80,"Higashi range or ally policy failed");h.assertTrue(d(p).getResources().getCurrentReiatsu()==75,"Higashi cost mismatch");}finally{scoreboard.removePlayerTeam(team);clean(p,near,far,ally);}h.succeed();});}
    @GameTest(template="empty",batch="new_jokaku",timeoutTicks=55)
    public static void flamePrisonUsesFixedRegionAndStopsAfterCancellation(GameTestHelper h){var p=player(h,"ryujin_jakka","shikai");var inside=target(h,p,0,6.5);var outside=target(h,p,0,14);TechniqueService.executeSlot(p,d(p),4);p.setYRot(180);
        h.runAfterDelay(25,()->{h.assertTrue(inside.getHealth()<80 && outside.getHealth()==80,"Prison did not use its fixed region");clean(p);inside.clearFire();inside.setHealth(80);});
        h.runAfterDelay(45,()->{try{h.assertTrue(inside.getHealth()==80,"Cancelled prison dealt late damage");}finally{clean(p,inside,outside);}h.succeed();});}
    @GameTest(template="empty",batch="new_ash",timeoutTicks=100)
    public static void ashArmyMovesAttacksAndCleansWithoutVanillaExperience(GameTestHelper h){var p=player(h,"ryujin_jakka","bankai");var t=target(h,p,0,5);
        var floor=new ArrayList<BlockPos>();for(int x=-4;x<=4;x++)for(int z=-4;z<=7;z++){var pos=p.blockPosition().offset(x,-1,z);floor.add(pos);h.getLevel().setBlock(pos,net.minecraft.world.level.block.Blocks.STONE.defaultBlockState(),3);}
        // Saved GameTest weather may be rainy; cover the target without changing global weather.
        for(int x=-2;x<=2;x++)for(int z=3;z<=7;z++){var roof=p.blockPosition().offset(x,4,z);floor.add(roof);h.getLevel().setBlock(roof,net.minecraft.world.level.block.Blocks.STONE.defaultBlockState(),3);}
        TechniqueService.executeSlot(p,d(p),3);final List<net.minecraft.world.entity.monster.WitherSkeleton> soldiers=new ArrayList<>();
        h.runAfterDelay(20,()->{soldiers.addAll(h.getLevel().getEntitiesOfClass(net.minecraft.world.entity.monster.WitherSkeleton.class,p.getBoundingBox().inflate(8),e->e.getPersistentData().hasUUID("ashOwner") && e.getPersistentData().getUUID("ashOwner").equals(p.getUUID())));
            h.assertTrue(soldiers.size()==10,"Minami did not create ten owned soldiers");
            h.assertTrue(soldiers.stream().allMatch(e->e.getMaxHealth()==6 && e.getHealth()<=6),"Ash soldiers did not retain fragile six-HP profile");
            var view=ElementalTechniqueService.snapshot(p).getList("casts",10).getCompound(0);var ids=view.getIntArray("soldiers");
            h.assertTrue(ids.length==10 && Arrays.stream(ids).allMatch(id->soldiers.stream().anyMatch(s->s.getId()==id)),"Late observer snapshot has missing or foreign ash soldiers");
            var xp=new net.minecraftforge.event.entity.living.LivingExperienceDropEvent(soldiers.get(0),p,20);ElementalTechniqueService.xp(xp);h.assertTrue(xp.getDroppedExperience()==0,"Soldier leaked vanilla XP");});
        h.runAfterDelay(70,()->{try{h.assertTrue(t.getHealth()<80 && t.isOnFire(),"Army never attacked/ignited its hostile target: health="+t.getHealth()+" fire="+t.getRemainingFireTicks()+" positions="+soldiers.stream().map(m->m.position()+" alive="+m.isAlive()).toList());clean(p);h.assertTrue(soldiers.stream().allMatch(Entity::isRemoved),"Army survived cancellation");}finally{clean(p,t);for(var pos:floor)h.getLevel().setBlock(pos,net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(),3);}h.succeed();});}
    @GameTest(template="empty",batch="new_freecast",timeoutTicks=60)
    public static void freeTestModeDoesNotBypassGestureButAllowsNextCast(GameTestHelper h){var p=player(h,"ryujin_jakka","shikai");var d=d(p);d.getResources().setCostsDisabled(true);d.getResources().setCurrentReiatsu(0);d.getStatus().setCooldownsDisabled(true);
        TechniqueService.executeSlot(p,d,1);TechniqueService.executeSlot(p,d,2);h.assertTrue(ElementalTechniqueService.snapshot(p).getList("casts",10).size()==1,"Free mode bypassed action lock");
        h.runAfterDelay(25,()->{try{TechniqueService.executeSlot(p,d,2);h.assertTrue(ElementalTechniqueService.busy(p) && d.getResources().getCurrentReiatsu()==0 && d.getStatus().getTechniqueSlot2CooldownTicks()==0,"Free mode rejected next eligible cast");}finally{clean(p);}h.succeed();});}
    @GameTest(template="empty",batch="new_chain",timeoutTicks=55)
    public static void crescentChainContainsOnlySelectedForwardEnemy(GameTestHelper h){var p=player(h,"hyorinmaru","shikai");var near=target(h,p,0,7);var side=target(h,p,5,7);TechniqueService.executeSlot(p,d(p),2);
        h.runAfterDelay(22,()->{try{h.assertTrue(near.getHealth()<80 && near.getTicksFrozen()>0 && side.getHealth()==80 && side.getTicksFrozen()==0,"Chain targeted the wrong enemies");h.assertTrue(d(p).getResources().getCurrentReiatsu()==80,"Chain cost mismatch");}finally{clean(p,near,side);}h.succeed();});}
    @GameTest(template="empty",batch="new_ryusenka",timeoutTicks=50)
    public static void ryusenkaAdvancesWithItsStab(GameTestHelper h){var p=player(h,"hyorinmaru","bankai");var t=target(h,p,0,4);double start=p.getZ();TechniqueService.executeSlot(p,d(p),1);
        h.runAfterDelay(17,()->{try{h.assertTrue(p.getZ()-start>=5 && t.getHealth()<80 && t.getTicksFrozen()>0,"Ryusenka did not advance and freeze on contact");}finally{clean(p,t);}h.succeed();});}
    @GameTest(template="empty",batch="new_sennen",timeoutTicks=90)
    public static void sennenReplacesBarrierWithAreaContainment(GameTestHelper h){var p=player(h,"hyorinmaru","shikai");var inside=target(h,p,0,16);var outside=target(h,p,0,29);TechniqueService.executeSlot(p,d(p),3);p.setYRot(180);
        h.runAfterDelay(55,()->{try{h.assertTrue(inside.getHealth()<80 && inside.getTicksFrozen()>0 && outside.getHealth()==80,"Pillars did not close around the fixed target area");}finally{clean(p,inside,outside);}h.succeed();});}
    @GameTest(template="empty",batch="new_hyoten_roar",timeoutTicks=70)
    public static void hyotenDirectRoarSummonsHydraAndDealsContinuousDamage(GameTestHelper h){
        var p=player(h,"hyorinmaru","bankai");var a=target(h,p,0,3);var b=target(h,p,0,28);var outside=target(h,p,0,33);
        h.onEachTick(()->HyorinmaruTechniqueService.tick(p,d(p)));
        TechniqueService.executeSlot(p,d(p),4);
        h.assertTrue(HyorinmaruTechniqueService.hasBankaiZone(p),"Hyoten glacial zone was not activated");
        h.assertTrue(d(p).getResources().getCurrentReiatsu()==65,"Hyoten reiatsu cost mismatch");
        h.runAfterDelay(35,()->{try{
            h.assertTrue(a.getHealth()<80 && b.getHealth()<80 && outside.getHealth()==80,"Hyoten damage did not cover 30-block zone correctly");
            h.assertTrue(a.getTicksFrozen()>0 && b.getTicksFrozen()>0,"Hyoten continuous chill not applied");
        }finally{clean(p,a,b,outside);}h.succeed();});
    }
    @GameTest(template="empty",batch="new_dragon_reach",timeoutTicks=65)
    public static void shikaiDragonReachesThirteenBlocksAndHasWiderBite(GameTestHelper h){var p=player(h,"hyorinmaru","shikai");var near=target(h,p,.75,13);var outside=target(h,p,0,17);TechniqueService.executeSlot(p,d(p),1);
        h.runAfterDelay(25,()->{try{h.assertTrue(near.getHealth()<80 && outside.getHealth()==80,"Shikai dragon range/width refinement failed");}finally{clean(p,near,outside);}h.succeed();});}
    @GameTest(template="empty",batch="new_steam",timeoutTicks=60)
    public static void steamCutWaitsForGestureAndKeepsItsTerrainContract(GameTestHelper h){
        var p=player(h,"ryujin_jakka","bankai");p.setPos(p.getX(),h.getLevel().getMaxBuildHeight()-110,p.getZ());
        var target=target(h,p,0,30);var stone=p.blockPosition().offset(0,0,6);var bedrock=p.blockPosition().offset(0,0,7);
        h.getLevel().setBlock(stone,net.minecraft.world.level.block.Blocks.STONE.defaultBlockState(),3);
        h.getLevel().setBlock(bedrock,net.minecraft.world.level.block.Blocks.BEDROCK.defaultBlockState(),3);
        TechniqueService.executeSlot(p,d(p),4);
        h.runAfterDelay(8,()->h.assertTrue(target.getHealth()==80 && h.getLevel().getBlockState(stone).is(net.minecraft.world.level.block.Blocks.STONE),"Steam cut executed during preparation"));
        h.runAfterDelay(35,()->h.assertTrue(target.getHealth()==80 && !h.getLevel().getBlockState(stone).isAir(),"Supreme cut struck before the release beat"));
        h.runAfterDelay(44,()->{try{
            h.assertTrue(target.getHealth()<80 && h.getLevel().getBlockState(stone).isAir() && h.getLevel().getBlockState(bedrock).is(net.minecraft.world.level.block.Blocks.BEDROCK),"Steam impact lost its terrain/damage contract");
            h.assertTrue(d(p).getResources().getCurrentReiatsu()==55 && d(p).getStatus().getTechniqueSlot4CooldownTicks()==1200,"Steam cut charged incorrectly");
        }finally{clean(p,target);h.getLevel().setBlock(stone,net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(),3);h.getLevel().setBlock(bedrock,net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(),3);}h.succeed();});
    }
    @GameTest(template="empty",batch="ryujin_authored_dash")
    public static void flameDashUsesItsOwnActionWithoutVanillaRiptideAttack(GameTestHelper h){
        var p=player(h,"ryujin_jakka","bankai");TechniqueService.executeSlot(p,d(p),1);
        try{h.assertTrue(!p.isAutoSpinAttack() && d(p).getStatus().getFlameDashTicks()==16,"Vanilla spinning attack overrides the sword dash");
            h.assertTrue(d(p).getResources().getCurrentReiatsu()==80 && d(p).getStatus().getTechniqueSlot1CooldownTicks()==100,"Dash resource contract changed");
            h.assertTrue(ElementalTechniqueService.snapshot(p).getList("casts",10).getCompound(0).getString("kind").equals("flame_dash"),"Missing authored dash timeline");
        }finally{d(p).getStatus().setFlameDashTicks(0);clean(p);}h.succeed();
    }
    @GameTest(template="empty",batch="new_cinematic_locks",timeoutTicks=50)
    public static void specialGestureCannotBeBypassedByPhysicalAttackGuardOrDodge(GameTestHelper h){var p=player(h,"ryujin_jakka","bankai");var t=target(h,p,0,2);TechniqueService.executeSlot(p,d(p),2);
        p.attack(t);h.assertTrue(t.getHealth()==80 && d(p).getResources().getStamina()==100,"Physical hit escaped technique gesture");h.assertTrue(!com.bleachmod.common.GuardService.eligible(p,d(p)) && !com.bleachmod.common.DodgeService.request(p,d(p),1,0),"Defense escaped gesture");
        p.setItemSlot(EquipmentSlot.MAINHAND,ItemStack.EMPTY);ElementalTechniqueService.tick(p);h.assertTrue(!ElementalTechniqueService.busy(p),"Unequip did not clear action");clean(p,t);h.succeed();}
}
