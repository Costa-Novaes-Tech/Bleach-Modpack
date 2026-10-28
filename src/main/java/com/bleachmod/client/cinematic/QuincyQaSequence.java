package com.bleachmod.client.cinematic;

import com.bleachmod.Reference;
import com.bleachmod.common.data.PlayerCapability;
import com.bleachmod.common.network.SyncHelper;
import net.minecraft.client.*;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import java.nio.file.*;

/** Real bow use and framebuffer evidence, exclusively in the explicit disposable dev world. */
@Mod.EventBusSubscriber(modid=Reference.MOD_ID,value=Dist.CLIENT)
public final class QuincyQaSequence {
    private static final String[] CASES={"idle-right-profile","draw-right-profile","draw-right-front","idle-left-profile","draw-left-profile","first-idle-right","first-draw-right","first-draw-left","mastery-after-kit-return"};
    private static int stage,wait;private static volatile int prepared=-1;private static Path out;
    private static volatile String failure;private static java.lang.reflect.Method position;
    private QuincyQaSequence(){ }
    private static boolean enabled(){var mc=Minecraft.getInstance();return !net.minecraftforge.fml.loading.FMLEnvironment.production&&Boolean.getBoolean("bleachmod.quincyQa")&&Boolean.getBoolean("bleachmod.benihimeQa")&&mc.hasSingleplayerServer()&&mc.player!=null&&mc.getSingleplayerServer().getWorldData().getLevelName().startsWith("Benihime-QA-68-");}
    @SubscribeEvent public static void tick(TickEvent.ClientTickEvent event){
        if(event.phase!=TickEvent.Phase.END||!enabled()||!BenihimeQaBootstrap.automatedFinished()||stage>=CASES.length)return;
        var mc=Minecraft.getInstance();
        if(out==null){out=mc.gameDirectory.toPath().resolve("quincy-qa/"+System.currentTimeMillis());try{Files.createDirectories(out);}catch(Exception ex){throw new IllegalStateException(ex);}}
        boolean left=stage==3||stage==4||stage==7;boolean first=stage>=5&&stage<=7;boolean draw=stage==1||stage==2||stage==4||stage==6||stage==7;
        mc.options.mainHand().set(left?HumanoidArm.LEFT:HumanoidArm.RIGHT);
        mc.options.setCameraType(first?CameraType.FIRST_PERSON:CameraType.THIRD_PERSON_BACK);
        mc.player.setYRot(0);mc.player.setXRot(0);mc.player.yBodyRot=0;mc.player.yHeadRot=0;
        if(wait==0){mc.options.broadcastOptions();final int test=stage;var server=mc.getSingleplayerServer();var uuid=mc.player.getUUID();
            server.execute(()->{try{var p=server.getPlayerList().getPlayer(uuid);var cmd=server.getCommands().getDispatcher();var source=p.createCommandSourceStack().withPermission(2);
                if(test==0){cmd.execute("bleachdev kit quincy",source);cmd.execute("bleachdev cheats off",source);SyncHelper.require(p).getQuincy().addPractice(1,37,23);}
                if(test==8){cmd.execute("bleachdev kit shinigami benihime",source);cmd.execute("bleachdev kit quincy",source);}
                p.stopUsingItem();SyncHelper.full(p);
            }catch(Exception ex){failure=ex.toString();}finally{prepared=test;}});
        }
        wait++;mc.options.keyUse.setDown(draw&&wait>=15);
        if(draw&&wait==15&&mc.gameMode!=null)mc.gameMode.useItem(mc.player,net.minecraft.world.InteractionHand.MAIN_HAND);
        if(wait<60||prepared!=stage)return;
        var d=PlayerCapability.get(mc.player).orElseThrow(()->new IllegalStateException("Missing client data"));
        boolean ok=failure==null&&d.getCharacter().getRace().equals("quincy")&&d.getQuincy().getMastery()==37&&d.getQuincy().getTechniqueMastery(1)==23;
        if(draw)ok&=mc.player.isUsingItem();
        String row=CASES[stage]+" "+(ok?"PASS":"FAIL")+" using="+mc.player.isUsingItem()+" mastery="+d.getQuincy().getMastery()+" failure="+failure+"\n";
        try{Files.writeString(out.resolve("results.txt"),row,StandardOpenOption.CREATE,StandardOpenOption.APPEND);}catch(Exception ex){throw new IllegalStateException(ex);}
        Screenshot.grab(out.toFile(),CASES[stage]+".png",mc.getMainRenderTarget(),ignored->{});
        com.mojang.logging.LogUtils.getLogger().info("QUINCY_QA {} output={}",row.trim(),out.toAbsolutePath());
        mc.options.keyUse.setDown(false);if(mc.player.isUsingItem()&&mc.gameMode!=null)mc.gameMode.releaseUsingItem(mc.player);
        stage++;wait=0;
        if(stage==CASES.length){mc.options.mainHand().set(HumanoidArm.RIGHT);mc.options.setCameraType(CameraType.FIRST_PERSON);if(Boolean.getBoolean("bleachmod.qaAutoExit"))mc.stop();}
    }
    @SubscribeEvent public static void camera(ViewportEvent.ComputeCameraAngles event){
        if(!enabled()||stage>=CASES.length||stage>=5&&stage<=7)return;
        var p=Minecraft.getInstance().player;var focus=p.position().add(0,1.2,0);var eye=focus.add(stage==2?0:4,.2,stage==2?4:0);var delta=focus.subtract(eye);
        try{if(position==null){position=Camera.class.getDeclaredMethod("setPosition",double.class,double.class,double.class);position.setAccessible(true);}position.invoke(event.getCamera(),eye.x,eye.y,eye.z);}catch(Exception ex){throw new IllegalStateException(ex);}
        event.setYaw((float)Math.toDegrees(Math.atan2(-delta.x,delta.z)));event.setPitch((float)-Math.toDegrees(Math.atan2(delta.y,Math.hypot(delta.x,delta.z))));
    }
}
