package com.bleachmod.gametest;
import com.bleachmod.common.ZanpakutoCarry;
import com.bleachmod.common.data.*;
import com.bleachmod.init.ModItems;
import com.mojang.authlib.GameProfile;
import net.minecraft.gametest.framework.*;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.gametest.*;
import java.util.UUID;

@GameTestHolder("bleachmod") @PrefixGameTestTemplate(false)
public final class CarryGameTests {
    @GameTest(template="empty") public static void onlyBoundInventoryWeaponIsCarriedAndPublicStateIsTransient(GameTestHelper h) {
        var p = new FakePlayer(h.getLevel(),new GameProfile(UUID.randomUUID(),"CarryTest"));
        var d = PlayerCapability.get(p).orElseThrow(() -> new IllegalStateException("Missing capability")); d.initializeShinigami();
        h.assertTrue(ZanpakutoCarry.state(p,d)==0,"Absent weapon displayed");
        p.getInventory().setItem(10,new ItemStack(ModItems.RYUJIN_JAKKA.get()));
        h.assertTrue(ZanpakutoCarry.state(p,d)==0,"Unbound identity displayed");
        p.getInventory().setItem(11,new ItemStack(ModItems.ASAUCHI.get()));
        h.assertTrue(ZanpakutoCarry.update(p,d) && d.getCharacter().getCarryState()==1,"Stored sword not detected");
        h.assertTrue(!ZanpakutoCarry.update(p,d),"Stable inventory produced another change");
        p.setItemInHand(InteractionHand.MAIN_HAND,new ItemStack(ModItems.ASAUCHI.get()));
        h.assertTrue(ZanpakutoCarry.state(p,d)==2,"Main hand not detected");
        p.setItemInHand(InteractionHand.MAIN_HAND,ItemStack.EMPTY); p.setItemInHand(InteractionHand.OFF_HAND,new ItemStack(ModItems.ASAUCHI.get()));
        h.assertTrue(ZanpakutoCarry.state(p,d)==3,"Off hand not detected");
        ZanpakutoCarry.update(p,d); var appearance=d.getCharacter().saveAppearance();
        h.assertTrue(appearance.getInt("carryState")==3 && !appearance.contains("inventory") && !d.getCharacter().save().contains("carryState"),"Public state leaked inventory or persisted");
        d.getCharacter().bindZanpakuto("hyorinmaru"); h.assertTrue(ZanpakutoCarry.state(p,d)==0,"Identity switch retained wrong sword"); h.succeed();
    }
}
