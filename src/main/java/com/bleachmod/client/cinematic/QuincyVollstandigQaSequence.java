package com.bleachmod.client.cinematic;

import com.bleachmod.Reference;
import com.bleachmod.common.network.SyncHelper;
import com.bleachmod.common.quincy.QuincyVollstandigService;
import net.minecraft.client.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import java.nio.file.*;

/** Opt-in disposable-world proof of the real server transformation and ordinary bow release. */
@Mod.EventBusSubscriber(modid=Reference.MOD_ID,value=Dist.CLIENT)
public final class QuincyVollstandigQaSequence {
    private static int tick;private static Path output;private static volatile boolean prepared;private static volatile String failure;
    private QuincyVollstandigQaSequence(){ }
    @SubscribeEvent public static void tick(TickEvent.ClientTickEvent e){
        var mc=Minecraft.getInstance();
        if(e.phase!=TickEvent.Phase.END||net.minecraftforge.fml.loading.FMLEnvironment.production||!Boolean.getBoolean("bleachmod.quincyVollstandigQa")||!Boolean.getBoolean("bleachmod.benihimeQa")||!BenihimeQaBootstrap.automatedFinished()||!mc.hasSingleplayerServer()||mc.player==null||tick>280||!mc.getSingleplayerServer().getWorldData().getLevelName().startsWith("Benihime-QA-68-"))return;
        if(output==null){output=mc.gameDirectory.toPath().resolve("quincy-vollstandig-qa/"+System.currentTimeMillis());try{Files.createDirectories(output);}catch(Exception x){throw new IllegalStateException(x);}
            mc.options.setCameraType(CameraType.THIRD_PERSON_FRONT);mc.options.mainHand().set(net.minecraft.world.entity.HumanoidArm.RIGHT);mc.options.broadcastOptions();
            var server=mc.getSingleplayerServer();var uuid=mc.player.getUUID();server.execute(()->{try{
                var p=server.getPlayerList().getPlayer(uuid);var source=p.createCommandSourceStack().withPermission(2);var commands=server.getCommands().getDispatcher();
                commands.execute("bleachdev kit quincy",source);commands.execute("bleachdev cheats off",source);commands.execute("bleachdev progress vollstandig",source);
                commands.execute("time set noon",source);p.setYRot(0);p.setXRot(0);QuincyVollstandigService.toggle(p,SyncHelper.require(p));
            }catch(Exception x){failure=x.toString();}finally{prepared=true;}});return;
        }
        if(!prepared)return;if(tick==0)mc.gui.getChat().clearMessages(false);tick++;mc.player.setYRot(0);mc.player.setXRot(0);mc.player.yBodyRot=0;mc.player.yHeadRot=0;
        if(tick==204)mc.options.hideGui=true;if(tick==206)mc.options.hideGui=false;
        if(tick==40||tick==110||tick==170||tick==205||tick==229||tick==270){
            Screenshot.grab(output.toFile(),"frame-"+tick+".png",mc.getMainRenderTarget(),ignored->{});
            var d=com.bleachmod.common.data.PlayerCapability.get(mc.player).orElseThrow(()->new IllegalStateException("capability"));
            long arrows=java.util.stream.StreamSupport.stream(mc.level.entitiesForRendering().spliterator(),false).filter(a->a instanceof com.bleachmod.common.quincy.QuincyArrowEntity).count();
            String row="tick="+tick+" active="+d.getQuincy().isVollstandigActive()+" arrows="+arrows+" camera="+mc.options.getCameraType()+" failure="+failure+"\n";
            try{Files.writeString(output.resolve("results.txt"),row,StandardOpenOption.CREATE,StandardOpenOption.APPEND);}catch(Exception x){throw new IllegalStateException(x);}
            com.mojang.logging.LogUtils.getLogger().info("QUINCY_VOLLSTANDIG_QA {} output={}",row.trim(),output);
        }
        if(tick==215&&mc.gameMode!=null){mc.options.keyUse.setDown(true);mc.gameMode.useItem(mc.player,net.minecraft.world.InteractionHand.MAIN_HAND);}
        if(tick==227&&mc.gameMode!=null){mc.options.keyUse.setDown(false);mc.gameMode.releaseUsingItem(mc.player);}
        if(tick==245){var server=mc.getSingleplayerServer();var id=mc.player.getUUID();server.execute(()->{var p=server.getPlayerList().getPlayer(id);QuincyVollstandigService.stop(p,SyncHelper.require(p));});}
        if(tick==280&&Boolean.getBoolean("bleachmod.qaAutoExit"))mc.stop();
    }
    @SubscribeEvent public static void camera(net.minecraftforge.client.event.ViewportEvent.ComputeCameraAngles e){
        var mc=Minecraft.getInstance();if(!Boolean.getBoolean("bleachmod.quincyVollstandigQa")||!Boolean.getBoolean("bleachmod.benihimeQa")||!mc.hasSingleplayerServer()||mc.player==null||tick<220||tick>235||!mc.getSingleplayerServer().getWorldData().getLevelName().startsWith("Benihime-QA-68-"))return;
        var focus=mc.player.position().add(0,1.4,5);var eye=focus.add(7,.6,0);var delta=focus.subtract(eye);
        try{var method=net.minecraftforge.fml.util.ObfuscationReflectionHelper.findMethod(Camera.class,"m_90581_",net.minecraft.world.phys.Vec3.class);method.invoke(e.getCamera(),eye);}catch(ReflectiveOperationException x){throw new IllegalStateException(x);}
        e.setYaw((float)Math.toDegrees(Math.atan2(-delta.x,delta.z)));e.setPitch((float)-Math.toDegrees(Math.atan2(delta.y,delta.horizontalDistance())));
    }
}
