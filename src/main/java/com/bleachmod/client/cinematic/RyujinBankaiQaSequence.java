package com.bleachmod.client.cinematic;

import com.bleachmod.Reference;
import com.bleachmod.common.data.PlayerCapability;
import com.bleachmod.common.network.SyncHelper;
import com.bleachmod.common.technique.ReleaseAnimationService;
import com.bleachmod.common.technique.RyujinBankaiEntranceService;
import com.bleachmod.init.ModItems;
import com.bleachmod.server.events.FormModeHandler;
import com.mojang.logging.LogUtils;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.ServerLifecycleHooks;
import java.nio.file.*;
import java.util.*;

/** Presentation QA only: explicit property, integrated admin and a fresh disposable QA world. */
@Mod.EventBusSubscriber(modid=Reference.MOD_ID,value=Dist.CLIENT)
public final class RyujinBankaiQaSequence {
    private static final String[] CASES={"day_back","night_front","first_return","left_front","near_wall","move_after_commit","cancel_before_commit","cancel_after_commit"};
    private record Trial(UUID owner,long start,int index) { }
    private static volatile Trial trial;
    private static int stage,wait,lastFrame=-1;
    private static Path root,folder;
    private RyujinBankaiQaSequence() { }
    private static boolean enabled() {
        var mc=Minecraft.getInstance();
        return !net.minecraftforge.fml.loading.FMLEnvironment.production && Boolean.getBoolean("bleachmod.ryujinBankaiQa")
                && Boolean.getBoolean("bleachmod.benihimeQa") && mc.player!=null && mc.level!=null
                && mc.hasSingleplayerServer() && mc.player.hasPermissions(2)
                && mc.gameDirectory.getAbsolutePath().replace('\\','/').contains("/run-benihime-qa/")
                && mc.getSingleplayerServer().getWorldData().getLevelName().startsWith("Benihime-QA-68-");
    }
    @SubscribeEvent public static void tick(TickEvent.ClientTickEvent e) {
        if(e.phase!=TickEvent.Phase.END || !enabled() || !BenihimeQaBootstrap.automatedFinished())return;
        var mc=Minecraft.getInstance();
        if(mc.screen!=null || mc.isPaused())return;
        if(trial==null) {
            if(stage>=CASES.length)return;
            if(++wait<45)return;wait=0;
            mc.options.renderDistance().set(6);mc.options.hideGui=false;mc.options.pauseOnLostFocus=false;
            mc.options.setCameraType(stage==1 || stage==3 || stage==4?CameraType.THIRD_PERSON_FRONT:stage==2?CameraType.FIRST_PERSON:CameraType.THIRD_PERSON_BACK);
            mc.options.mainHand().set(stage==3?HumanoidArm.LEFT:HumanoidArm.RIGHT);mc.options.broadcastOptions();
            if(root==null)root=mc.gameDirectory.toPath().resolve("ryujin-bankai-qa/cycle109-"+System.currentTimeMillis());
            folder=root.resolve(String.format(Locale.ROOT,"%02d_%s",stage+1,CASES[stage]));lastFrame=-1;
            var uuid=mc.player.getUUID();int index=stage;
            mc.getSingleplayerServer().execute(()->prepare(uuid,index));
        } else if(mc.level.getGameTime()-trial.start>158) {
            LogUtils.getLogger().info("Ryujin Bankai QA completed {} perspective={}",CASES[trial.index],mc.options.getCameraType());
            trial=null;stage++;
            if(stage>=CASES.length && Boolean.getBoolean("bleachmod.qaAutoExit"))mc.stop();
        }
    }
    private static void prepare(UUID uuid,int index) {
        var server=ServerLifecycleHooks.getCurrentServer();if(server==null)return;
        var p=server.getPlayerList().getPlayer(uuid);if(p==null)return;
        ReleaseAnimationService.cancel(p);RyujinBankaiEntranceService.cancel(p);
        p.teleportTo(p.serverLevel(),index*48+.5,-60,.5,0,0);p.yBodyRot=0;p.yRotO=0;p.setXRot(0);
        p.setMainArm(index==3?HumanoidArm.LEFT:HumanoidArm.RIGHT);
        p.serverLevel().setDayTime(index==1?18000:6000);p.setHealth(p.getMaxHealth());
        var d=PlayerCapability.get(p).orElseThrow(()->new IllegalStateException("Missing player capability"));d.initializeShinigami();d.getCharacter().bindZanpakuto("ryujin_jakka");
        d.getSkills().get("zanpakuto").setLevel(2);
        for(String form:List.of("sealed","shikai","bankai")) {d.getCharacter().unlockForm(form);d.getCharacter().setMastery("zanpakuto",form,100);}
        d.getCharacter().preserveLegacyTechniques();d.getCharacter().setActiveForm("zanpakuto","shikai");d.getCharacter().setSelectedForm("zanpakuto","bankai");
        d.getStatus().clearCooldowns();d.getGuard().interrupt();d.getResources().setCurrentReiatsu(d.getResources().getMaxReiatsu());
        p.setItemSlot(EquipmentSlot.MAINHAND,new ItemStack(ModItems.RYUJIN_JAKKA.get()));p.setItemSlot(EquipmentSlot.OFFHAND,ItemStack.EMPTY);
        if(index==4)for(int x=-3;x<=3;x++)for(int y=0;y<6;y++)p.serverLevel().setBlock(p.blockPosition().offset(x,y,6),Blocks.STONE.defaultBlockState(),3);
        SyncHelper.full(p);SyncHelper.appearance(p);
        trial=new Trial(uuid,p.level().getGameTime()+20,index);
        LogUtils.getLogger().info("Ryujin Bankai QA prepared {}",CASES[index]);
    }
    @SubscribeEvent public static void server(TickEvent.ServerTickEvent e) {
        var t=trial;if(e.phase!=TickEvent.Phase.END || t==null || !Boolean.getBoolean("bleachmod.ryujinBankaiQa"))return;
        var server=ServerLifecycleHooks.getCurrentServer();if(server==null)return;
        var p=server.getPlayerList().getPlayer(t.owner);if(p==null)return;long age=p.level().getGameTime()-t.start;
        if(age==0)PlayerCapability.get(p).ifPresent(d->FormModeHandler.instantTransform(p,d));
        if(t.index==5 && age==82)p.teleportTo(p.serverLevel(),p.getX()+3,p.getY(),p.getZ()+2,35,0);
        if((t.index==6 && age==36) || (t.index==7 && age==76)) {ReleaseAnimationService.cancel(p);RyujinBankaiEntranceService.cancel(p);}
        if(age==150)LogUtils.getLogger().info("Ryujin Bankai QA result {} releaseBusy={} scene={} form={}",CASES[t.index],ReleaseAnimationService.busy(p),RyujinBankaiEntranceService.snapshot(p),PlayerCapability.get(p).map(d->d.getCharacter().getActiveForm()).orElse("missing"));
    }
    @SubscribeEvent public static void capture(TickEvent.RenderTickEvent e) {
        var t=trial;if(e.phase!=TickEvent.Phase.END || t==null || folder==null || !enabled())return;
        var mc=Minecraft.getInstance();int age=(int)(mc.level.getGameTime()-t.start);
        if(age<0 || age>152 || age%2!=0 || age==lastFrame || mc.screen!=null)return;lastFrame=age;
        String file=String.format(Locale.ROOT,"frame-t%03d.png",age);
        try {
            Files.createDirectories(folder);Screenshot.grab(folder.toFile(),file,mc.getMainRenderTarget(),message->{});
            var c=mc.gameRenderer.getMainCamera();var p=c.getPosition();
            Files.writeString(folder.resolve("frames.jsonl"),String.format(Locale.ROOT,
                    "{\"file\":\"screenshots/%s\",\"tick\":%d,\"wallMs\":%d,\"fps\":%d,\"cameraType\":\"%s\",\"active\":%b,\"camPos\":[%.5f,%.5f,%.5f],\"yaw\":%.3f,\"pitch\":%.3f}%n",
                    file,age,System.currentTimeMillis(),mc.getFps(),mc.options.getCameraType(),RyujinBankaiEntranceClient.localEntranceActive(),p.x,p.y,p.z,c.getYRot(),c.getXRot()),StandardOpenOption.CREATE,StandardOpenOption.APPEND);
        }catch(java.io.IOException failure){LogUtils.getLogger().warn("Ryujin Bankai QA capture",failure);}
    }
}
