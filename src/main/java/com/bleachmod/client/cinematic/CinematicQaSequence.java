package com.bleachmod.client.cinematic;

import com.bleachmod.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Locale;

/** Opt-in reproducible extension of the disposable QA world; absent from normal play. */
@Mod.EventBusSubscriber(modid=Reference.MOD_ID,value=Dist.CLIENT)
public final class CinematicQaSequence {
    private static int stage,wait,serial,lastFrame=-1;
    private static long captureStart;
    private static String scenario;
    private static File evidence;
    private CinematicQaSequence() { }
    private static boolean enabled(){
        var mc=Minecraft.getInstance();
        return !net.minecraftforge.fml.loading.FMLEnvironment.production
                &&Boolean.getBoolean("bleachmod.benihimeQa")&&Boolean.getBoolean("bleachmod.cinematicQa")
                &&mc.hasSingleplayerServer()&&mc.player!=null&&mc.player.hasPermissions(2)
                &&mc.getSingleplayerServer().getWorldData().getLevelName().startsWith("Benihime-QA-68-");
    }
    private static void command(String command){ClientCommandHandler.runCommand(command);}
    @SubscribeEvent public static void tick(TickEvent.ClientTickEvent e){
        if(e.phase!=TickEvent.Phase.END||!enabled())return;
        var mc=Minecraft.getInstance();if(mc.screen!=null||mc.isPaused())return;
        if(stage==0&&BenihimeQaBootstrap.automatedFinished()){
            if(Boolean.getBoolean("bleachmod.cinematicQaFast")){start("lethal");stage=4;wait=0;}
            else {stage=1;wait=0;command("cinematicqa terrain");}
        }else if(stage==1&&++wait>=20){
            stage=2;command("benihimeqa record");
        }else if(stage==2&&BenihimeEntranceClient.benchFollowing()){
            stage=3;wait=0;command("benihimeqa stop");
        }else if(stage==3&&++wait>=20){
            start("lethal");stage=4;wait=0;
        }else if(stage==4&&++wait>=160){
            start("threshold");stage=5;wait=0;
        }else if(stage==5&&++wait>=160){
            start("survive");stage=6;wait=0;
        }else if(stage==6&&++wait>=160){
            scenario=null;stage=7;
            com.mojang.logging.LogUtils.getLogger().info("Cinematic QA 75 automatic sequence complete; client remains available for manual testing");
            mc.player.displayClientMessage(net.minecraft.network.chat.Component.literal("Testes: /cinematicqa benihime | /cinematicqa higashi lethal | threshold | survive"),false);
        }
    }
    private static void start(String name){
        var mc=Minecraft.getInstance();scenario=name;captureStart=mc.level.getGameTime();serial=0;lastFrame=-1;
        evidence=new File(mc.gameDirectory,"cinematic-qa/"+System.currentTimeMillis()+"-"+name);
        command("cinematicqa higashi "+name);
    }
    @SubscribeEvent public static void capture(TickEvent.RenderTickEvent e){
        if(e.phase!=TickEvent.Phase.END||scenario==null||!enabled())return;
        var mc=Minecraft.getInstance();if(mc.screen!=null)return;
        int elapsed=(int)(mc.level.getGameTime()-captureStart);
        if(elapsed>130||elapsed/2==lastFrame)return;lastFrame=elapsed/2;
        int age=elapsed-20;
        String phase=age<0?"SETUP":age<10?"DRAW":age<16?"CUT":age<56?"HOLD":age<72?"IGNITE":age<88?"ASH":"RETURN";
        String file=String.format(Locale.ROOT,"frame-%04d-t%04d-%s.png",serial++,elapsed,phase);
        try{
            Files.createDirectories(evidence.toPath());
            Screenshot.grab(evidence,file,mc.getMainRenderTarget(),message->{});
            String row=String.format(Locale.ROOT,"{\"file\":\"screenshots/%s\",\"gameTick\":%d,\"wallMs\":%d,\"phase\":\"%s\",\"view\":\"%s\",\"scenario\":\"%s\",\"fpsObserved\":%d,\"width\":%d,\"height\":%d,\"origin\":[%.4f,%.4f,%.4f],\"source\":\"Minecraft Screenshot.grab framebuffer\"}%n",
                    file,elapsed,System.currentTimeMillis(),phase,mc.options.getCameraType()==net.minecraft.client.CameraType.THIRD_PERSON_BACK?"side":"front",scenario,mc.getFps(),mc.getWindow().getWidth(),mc.getWindow().getHeight(),mc.player.getX(),mc.player.getY(),mc.player.getZ());
            Files.writeString(evidence.toPath().resolve("frames.jsonl"),row,StandardOpenOption.CREATE,StandardOpenOption.APPEND);
            if(serial==1){
                var env=new com.google.gson.JsonObject();
                env.addProperty("world",mc.getSingleplayerServer().getWorldData().getLevelName());
                env.addProperty("renderer",org.lwjgl.opengl.GL11.glGetString(org.lwjgl.opengl.GL11.GL_RENDERER));
                env.addProperty("scenario",scenario);env.addProperty("captureSamplingTicks",2);
                env.addProperty("maxHeapBytes",Runtime.getRuntime().maxMemory());env.addProperty("renderDistance",mc.options.renderDistance().get());
                Files.writeString(evidence.toPath().resolve("environment.json"),env.toString());
            }
        }catch(java.io.IOException error){com.mojang.logging.LogUtils.getLogger().warn("Cinematic QA capture failed",error);}
    }
}
