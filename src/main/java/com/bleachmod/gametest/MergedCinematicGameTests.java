package com.bleachmod.gametest;

import com.bleachmod.common.*;
import com.bleachmod.common.data.*;
import com.bleachmod.common.technique.*;
import com.bleachmod.init.ModItems;
import com.bleachmod.server.events.FormModeHandler;
import com.mojang.authlib.GameProfile;
import net.minecraft.gametest.framework.*;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.gametest.*;
import java.util.UUID;

@GameTestHolder("bleachmod") @PrefixGameTestTemplate(false)
public final class MergedCinematicGameTests {
    @GameTest(template="empty",batch="merged_nozarashi",timeoutTicks=40)
    public static void nozarashiReleaseSharesCombatLocksWithoutEnteringBetterCombatFallback(GameTestHelper h){
        var p=new FakePlayer(h.getLevel(),new GameProfile(UUID.randomUUID(),"MergedRelease"));var at=h.absolutePos(new net.minecraft.core.BlockPos(2,2,2));p.setPos(at.getX()+.5,h.getLevel().getMaxBuildHeight()-80,at.getZ()+.5);p.setOnGround(true);p.setNoGravity(true);h.getLevel().getChunkAt(p.blockPosition());
        var d=PlayerCapability.get(p).orElseThrow(()->new IllegalStateException("Missing capability"));d.initializeShinigami();d.getCharacter().bindZanpakuto("nozarashi");d.getSkills().get("zanpakuto").setLevel(2);d.getCharacter().unlockForm("shikai");d.getCharacter().unlockForm("bankai");d.getCharacter().setMastery("zanpakuto","bankai",100);d.getCharacter().setMastery("zanpakuto","shikai",100);d.getCharacter().setMastery("zanpakuto","sealed",100);d.getCharacter().setSelectedForm("zanpakuto","bankai");p.setItemSlot(EquipmentSlot.MAINHAND,new ItemStack(ModItems.NOZARASHI.get()));
        try{
            h.assertTrue(com.bleachmod.common.compat.BetterCombatCompat.excluded(p.getMainHandItem())&&net.bettercombat.logic.WeaponRegistry.getAttributes(p.getMainHandItem())==null,"Nozarashi acquired an implicit Better Combat hitbox");
            FormModeHandler.instantTransform(p,d);
            h.assertTrue(NozarashiReleaseEffects.inCinematic(p)&&ReleaseAnimationService.busy(p),"Zaraki release did not participate in common locks");float balance=d.getResources().getCurrentReiatsu();
            h.assertTrue(!GuardService.eligible(p,d)&&!DodgeService.request(p,d,1,0)&&!com.bleachmod.common.compat.BetterCombatCompat.canAttack(p),"Guard, dodge or physical attack escaped the cinematic lock");
            TechniqueService.executeSlot(p,d,1);h.assertTrue(d.getResources().getCurrentReiatsu()==balance&&d.getStatus().getTechniqueSlot1CooldownTicks()==0,"A technique charged during the cinematic");
            var normal=new LivingAttackEvent(p,p.damageSources().generic(),2);MinecraftForge.EVENT_BUS.post(normal);h.assertTrue(normal.isCanceled(),"Nozarashi lost its existing cinematic protection");
            var holder=p.level().registryAccess().registryOrThrow(net.minecraft.core.registries.Registries.DAMAGE_TYPE).getHolderOrThrow(HigashiExecutionService.damageKey());var execution=new LivingAttackEvent(p,new net.minecraft.world.damagesource.DamageSource(holder),2);MinecraftForge.EVENT_BUS.post(execution);h.assertTrue(!execution.isCanceled(),"Cinematic protection overrode a bypassing execution source");
        }finally{FormModeHandler.descend(p,d);NozarashiTechniqueService.cancel(p);}
        h.assertTrue(!ReleaseAnimationService.busy(p),"Cinematic lock persisted after form cancellation");h.succeed();
    }
}
