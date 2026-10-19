package com.bleachmod.gametest;

import com.bleachmod.common.data.*;
import com.bleachmod.common.technique.*;
import com.bleachmod.init.ModItems;
import com.mojang.authlib.GameProfile;
import net.minecraft.gametest.framework.*;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.gametest.*;
import java.util.*;

@GameTestHolder("bleachmod") @PrefixGameTestTemplate(false)
public final class SignatureAnimationGameTests {
    private static PlayerData d(FakePlayer p){return PlayerCapability.get(p).orElseThrow(()->new IllegalStateException("Missing capability"));}
    private static FakePlayer player(GameTestHelper h,String identity){
        var p=new FakePlayer(h.getLevel(),new GameProfile(UUID.randomUUID(),"SignatureTest"));var pos=h.absolutePos(new net.minecraft.core.BlockPos(2,2,2));p.setPos(pos.getX(),h.getLevel().getMaxBuildHeight()-110,pos.getZ());p.setYRot(0);p.setNoGravity(true);p.setOnGround(true);h.getLevel().getChunkAt(p.blockPosition());
        var d=d(p);d.initializeShinigami();d.getCharacter().bindZanpakuto(identity);d.getSkills().get("zanpakuto").setLevel(2);
        for(String f:List.of("sealed","shikai","bankai")){d.getCharacter().unlockForm(f);d.getCharacter().setMastery("zanpakuto",f,100);}
        p.setItemSlot(EquipmentSlot.MAINHAND,new ItemStack(switch(identity){case "senbonzakura"->ModItems.SENBONZAKURA.get();case "benihime"->ModItems.BENIHIME.get();default->ModItems.ASAUCHI.get();}));return p;
    }
    private static void clean(FakePlayer p){ReleaseAnimationService.cancel(p);RyujinTechniqueService.cancel(p);}
    @GameTest(template="empty",batch="signature_bankai",timeoutTicks=90)
    public static void remainingBankaisCommitOnceAndReleaseTheirActionLocks(GameTestHelper h){
        var players=List.of(player(h,"senbonzakura"),player(h,"benihime"),player(h,"asauchi"));
        for(var p:players){d(p).getCharacter().setSelectedForm("zanpakuto","bankai");com.bleachmod.server.events.FormModeHandler.attemptTransform(p,d(p));h.assertTrue(ReleaseAnimationService.busy(p)&&d(p).getCharacter().getActiveForm().equals("sealed"),"Remaining Bankai bypassed its release timeline");h.assertTrue(!TechniqueProgression.authorize(p,d(p),1),"Technique escaped release lock");}
        h.onEachTick(()->players.forEach(ReleaseAnimationService::tick));final Map<UUID,Float> balances=new HashMap<>();
        h.runAfterDelay(45,()->{for(var p:players){h.assertTrue(d(p).getCharacter().getActiveForm().equals("bankai"),"Bankai not committed");balances.put(p.getUUID(),d(p).getResources().getCurrentReiatsu());}});
        h.runAfterDelay(66,()->{try{for(var p:players)h.assertTrue(!ReleaseAnimationService.busy(p)&&balances.get(p.getUUID())==d(p).getResources().getCurrentReiatsu(),"Remaining release stuck or charged twice");}finally{players.forEach(SignatureAnimationGameTests::clean);}h.succeed();});
    }
    @GameTest(template="empty",batch="signature_shikai",timeoutTicks=50)
    public static void newShikaiReleaseCanBeCancelledBeforeDebit(GameTestHelper h){var p=player(h,"benihime");d(p).getCharacter().setSelectedForm("zanpakuto","shikai");com.bleachmod.server.events.FormModeHandler.attemptTransform(p,d(p));
        h.assertTrue(ReleaseAnimationService.snapshot(p).getInt("duration")==24,"Shikai has wrong timing");p.setItemSlot(EquipmentSlot.MAINHAND,ItemStack.EMPTY);ReleaseAnimationService.tick(p);
        h.assertTrue(!ReleaseAnimationService.busy(p)&&d(p).getResources().getCurrentReiatsu()==100&&d(p).getCharacter().getActiveForm().equals("sealed"),"Interrupted release debited or committed");clean(p);h.succeed();}
    @GameTest(template="empty",batch="signature_lease",timeoutTicks=45)
    public static void acceptedTechniquePublishesGestureWithoutAnotherDebitOrPersistentState(GameTestHelper h){var p=player(h,"senbonzakura");d(p).getCharacter().setActiveForm("zanpakuto","bankai");
        h.assertTrue(SenbonzakuraTechniqueService.executeSlot(p,d(p),2),"Garden rejected");float paid=d(p).getResources().getCurrentReiatsu();var snap=SignatureAnimationService.snapshot(p);
        h.assertTrue(snap.getString("kind").equals("floral_garden")&&snap.getInt("duration")==16,"Accepted cast missing from public timeline");
        h.assertTrue(!d(p).save().contains("signatureView"),"Cosmetic state leaked into persistent save");
        h.runAfterDelay(20,()->{try{SignatureAnimationService.tick(p);h.assertTrue(SignatureAnimationService.snapshot(p).isEmpty()&&d(p).getResources().getCurrentReiatsu()==paid,"Expired gesture stuck or debited again");}finally{clean(p);}h.succeed();});}
    @GameTest(template="empty",batch="signature_rejected")
    public static void rejectedTechniqueDoesNotCreateGesture(GameTestHelper h){var p=player(h,"benihime");d(p).getCharacter().setActiveForm("zanpakuto","shikai");d(p).getResources().setCurrentReiatsu(0);
        h.assertTrue(!BenihimeTechniqueService.executeSlot(p,d(p),1)&&SignatureAnimationService.snapshot(p).isEmpty(),"Rejected cast created a gesture");clean(p);h.succeed();}
}
