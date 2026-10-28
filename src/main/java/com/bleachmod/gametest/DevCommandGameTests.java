package com.bleachmod.gametest;

import com.bleachmod.common.data.*;
import com.bleachmod.common.quincy.SprengerNodeEntity;
import com.bleachmod.init.ModItems;
import com.bleachmod.registry.ModEntities;
import com.bleachmod.server.commands.DevKitService;
import com.mojang.authlib.GameProfile;
import net.minecraft.gametest.framework.*;
import net.minecraft.world.item.*;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.gametest.*;
import java.util.*;

@GameTestHolder("bleachmod")
@PrefixGameTestTemplate(false)
public final class DevCommandGameTests {
    private static FakePlayer player(GameTestHelper h) {
        var p = new FakePlayer(h.getLevel(), new GameProfile(UUID.randomUUID(), "DevCommandQA"));
        p.setPos(h.absoluteVec(new net.minecraft.world.phys.Vec3(2.5, 3, 2.5)));
        data(p).initializeShinigami();
        return p;
    }
    private static PlayerData data(FakePlayer p) { return PlayerCapability.get(p).orElseThrow(() -> new IllegalStateException("Missing capability")); }
    private static int run(FakePlayer p, String cmd) {
        try { return p.getServer().getCommands().getDispatcher().execute("bleachdev " + cmd, p.createCommandSourceStack().withPermission(2)); }
        catch (com.mojang.brigadier.exceptions.CommandSyntaxException ex) { throw new IllegalStateException(cmd, ex); }
    }
    private static boolean rejected(FakePlayer p, String cmd, int permission) {
        try { p.getServer().getCommands().getDispatcher().execute("bleachdev " + cmd, p.createCommandSourceStack().withPermission(permission)); return false; }
        catch (com.mojang.brigadier.exceptions.CommandSyntaxException ex) { return true; }
    }
    private static int count(FakePlayer p, Item item) {
        int total = 0; for (int i=0;i<36;i++) if (p.getInventory().getItem(i).is(item)) total+=p.getInventory().getItem(i).getCount(); return total;
    }

    @GameTest(template="empty")
    public static void devKitRoundTripKeepsProgressAndEquipment(GameTestHelper h) {
        var p=player(h);var d=data(p);
        d.getResources().addTrainingPoints(123);d.getResources().setCurrentReiatsu(37);
        d.getCharacter().bindZanpakuto("benihime");d.getCharacter().setMastery("zanpakuto","sealed",72);
        p.setItemInHand(net.minecraft.world.InteractionHand.MAIN_HAND,new ItemStack(Items.DIAMOND,3));
        p.setItemInHand(net.minecraft.world.InteractionHand.OFF_HAND,new ItemStack(Items.SHIELD));
        run(p,"kit quincy");
        h.assertTrue(d.getCharacter().getRace().equals("quincy") && d.getCharacter().getActiveFormGroup().isEmpty(),"Quincy leaked zanpakuto form");
        run(p,"kit shinigami benihime");run(p,"kit quincy");run(p,"kit shinigami benihime");
        h.assertTrue(d.getCharacter().getRace().equals("shinigami") && d.getCharacter().getMastery("zanpakuto","sealed")==72,"Round trip lost old progress");
        h.assertTrue(d.getResources().getTrainingPoints()==123 && d.getResources().getCurrentReiatsu()==37,"Kit reset resources or points");
        h.assertTrue(count(p,Items.DIAMOND)==3 && count(p,ModItems.BENIHIME.get())==1 && count(p,ModItems.QUINCY_BOW.get())==1 && p.getOffhandItem().is(Items.SHIELD),"Equipment lost or duplicated");
        h.succeed();
    }

    @GameTest(template="empty")
    public static void devCheatsAllIsIdempotentAndOffPreservesGrants(GameTestHelper h) {
        var p=player(h);var d=data(p);run(p,"kit shinigami benihime");
        float stamina=d.getResources().getStamina();
        run(p,"cheats all");run(p,"cheats all @s");
        for(String form:List.of("sealed","shikai","bankai")){
            h.assertTrue(d.getCharacter().getMastery("zanpakuto",form)==100,"Missing form mastery "+form);
            for(int slot=1;slot<=4;slot++)h.assertTrue(d.getCharacter().isTechniqueDiscovered(form,slot)&&d.getCharacter().getTechniqueMastery(form,slot)==100,"Missing technique mastery");
        }
        h.assertTrue(d.getResources().getTrainingPoints()==10000 && d.getStatus().areCooldownsDisabled() && d.getResources().areCostsDisabled(),"Preset missing or accumulated points");
        h.assertTrue(d.getResources().getStamina()==stamina,"Preset changed stamina");
        run(p,"kit quincy");run(p,"kit shinigami senbonzakura");
        h.assertTrue(d.getCharacter().getTechniqueMastery("bankai",4)==100 && DevKitService.allEnabled(p),"Preset did not follow kit");
        run(p,"cheats off");
        h.assertTrue(!d.getStatus().areCooldownsDisabled()&&!d.getResources().areCostsDisabled()&&!DevKitService.allEnabled(p),"Off left cheats enabled");
        h.assertTrue(d.getResources().getTrainingPoints()==10000&&d.getCharacter().getTechniqueMastery("bankai",4)==100,"Off deleted permanent progress");
        d.getResources().setCurrentReiatsu(0);
        h.assertTrue(!d.getResources().consumeReiatsu(1),"Off did not restore actual resource cost");
        d.getStatus().setTechniqueSlot1CooldownTicks(80);
        h.assertTrue(d.getStatus().getTechniqueSlot1CooldownTicks()==80,"Off did not restore cooldowns");
        h.succeed();
    }

    @GameTest(template="empty")
    public static void devCommandsRejectUnauthorizedInvalidAndFullInventoryAtomically(GameTestHelper h) {
        var p=player(h);var d=data(p);String before=d.save().toString();
        h.assertTrue(rejected(p,"kit quincy",0)&&before.equals(d.save().toString()),"Permission failure mutated character");
        h.assertTrue(rejected(p,"kit shinigami invalid",2)&&before.equals(d.save().toString()),"Invalid kit mutated character");
        for(int i=0;i<36;i++)p.getInventory().setItem(i,new ItemStack(Items.DIAMOND,64));
        h.assertTrue(rejected(p,"kit quincy",2)&&before.equals(d.save().toString())&&count(p,Items.DIAMOND)==36*64,"Full inventory lost item or changed race");
        p.getInventory().setItem(10,new ItemStack(ModItems.QUINCY_BOW.get()));
        run(p,"kit quincy");
        h.assertTrue(p.getMainHandItem().is(ModItems.QUINCY_BOW.get())&&count(p,Items.DIAMOND)==35*64,"Full inventory swap lost old hand");
        h.succeed();
    }

    @GameTest(template="empty")
    public static void devResetRemovesOnlyOwnedFormationAndKeepsProgress(GameTestHelper h) {
        var p=player(h);var d=data(p);var other=UUID.randomUUID();
        var own=new SprengerNodeEntity(ModEntities.SPRENGER_NODE.get(),h.getLevel());own.setOwner(p.getUUID(),0);own.setPos(p.position());h.getLevel().addFreshEntity(own);
        var foreign=new SprengerNodeEntity(ModEntities.SPRENGER_NODE.get(),h.getLevel());foreign.setOwner(other,1);foreign.setPos(p.position());h.getLevel().addFreshEntity(foreign);
        d.getQuincy().startLichtRegen(new net.minecraft.world.phys.Vec3(0,0,1));
        d.getStatus().setTechniqueSlot2CooldownTicks(80);d.getResources().addTrainingPoints(19);
        run(p,"reset combat");
        h.assertTrue(own.isRemoved()&&!foreign.isRemoved()&&!d.getQuincy().isLichtRegenActive(),"Reset removed foreign entity or retained owned action");
        h.assertTrue(d.getStatus().getTechniqueSlot2CooldownTicks()==0&&d.getResources().getTrainingPoints()==19,"Reset changed progression or retained cooldown");
        foreign.discard();h.succeed();
    }

    @GameTest(template="empty")
    public static void devProgressPersistsButCheatsDoNot(GameTestHelper h) {
        var p=player(h);run(p,"kit shinigami benihime");run(p,"cheats all");
        var copy=new PlayerData();copy.load(data(p).save());
        h.assertTrue(copy.getCharacter().getTechniqueMastery("sealed",4)==100&&copy.getResources().getTrainingPoints()==10000,"Grants lost after load");
        h.assertTrue(!copy.getResources().areCostsDisabled()&&!copy.getStatus().areCooldownsDisabled(),"Cheats persisted in save");
        DevKitService.cheatsOff(p,data(p));h.succeed();
    }

    @GameTest(template="empty")
    public static void devLegacyCommandsAndAsauchiStillWork(GameTestHelper h) {
        var p=player(h);var d=data(p);run(p,"quincy @s");run(p,"bankai @s benihime");
        h.assertTrue(d.getCharacter().getRace().equals("shinigami")&&p.getMainHandItem().is(ModItems.BENIHIME.get()),"Legacy bankai failed to recover Quincy profile");
        run(p,"reiatsu free @s");run(p,"cooldowns disable @s");
        h.assertTrue(d.getResources().areCostsDisabled()&&d.getStatus().areCooldownsDisabled(),"Legacy toggles broken");
        run(p,"cheats off");run(p,"kit shinigami asauchi");
        h.assertTrue(d.getCharacter().getActiveForm().equals("sealed")&&d.getCharacter().getSelectedForm().equals("sealed"),"Asauchi selected Bankai");
        h.succeed();
    }

    @GameTest(template="empty")
    public static void devHelpStatusAndFineControlsAreSafe(GameTestHelper h) {
        var p=player(h);var d=data(p);String before=d.save().toString();
        run(p,"help");run(p,"help kits");run(p,"help cheats");run(p,"help progress");run(p,"help legacy");run(p,"status");run(p,"status @s");
        h.assertTrue(before.equals(d.save().toString()),"Read-only status mutated character");
        run(p,"kit shinigami benihime");
        h.assertTrue(com.bleachmod.common.evolution.TransformationsHelper.isUnlocked(d,"zanpakuto","shikai")
                &&com.bleachmod.common.evolution.TransformationsHelper.isUnlocked(d,"zanpakuto","bankai"),"Kit without all failed previous-form prerequisites");
        run(p,"progress mastery sealed 50");run(p,"progress points 25");
        h.assertTrue(d.getCharacter().getMastery("zanpakuto","sealed")==50&&d.getResources().getTrainingPoints()==25,"Fine controls wrong");
        run(p,"cheats cooldowns on");run(p,"cheats reiatsu off");
        h.assertTrue(d.getStatus().areCooldownsDisabled()&&!d.getResources().areCostsDisabled(),"Independent toggles wrong");
        run(p,"cheats off");d.getStatus().setHasCreatedCharacter(false);
        h.assertTrue(rejected(p,"kit quincy",2),"Uncreated character accepted");h.succeed();
    }
}
