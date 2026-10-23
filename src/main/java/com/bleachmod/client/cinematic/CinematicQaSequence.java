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
    private static boolean seenExecution;
    public static boolean automatedFinished(){return stage>=8;}
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
        if(scenario!=null)seenExecution|=com.bleachmod.client.model.HigashiExecutionVisuals.localExecutionActive();
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
            start("miss");stage=7;wait=0;
        }else if(stage==7&&++wait>=160){
            finish();scenario=null;stage=8;
            com.mojang.logging.LogUtils.getLogger().info("Cinematic QA 77 automatic sequence complete; client remains available for manual testing");
            mc.player.displayClientMessage(net.minecraft.network.chat.Component.literal("Testes: /cinematicqa benihime | /cinematicqa higashi lethal | threshold | survive | miss"),false);
        }
    }
    private static void start(String name){
        var mc=Minecraft.getInstance();if(scenario!=null)finish();seenExecution=false;scenario=name;captureStart=mc.level.getGameTime();serial=0;lastFrame=-1;
        evidence=new File(mc.gameDirectory,"cinematic-qa/"+System.currentTimeMillis()+"-"+name);
        if(Boolean.getBoolean("bleachmod.higashiPoseQa"))mc.options.setCameraType(name.equals("threshold")?net.minecraft.client.CameraType.THIRD_PERSON_BACK:net.minecraft.client.CameraType.FIRST_PERSON);
        command("cinematicqa higashi "+name);
    }
    private static void finish(){
        boolean expected=scenario.equals("lethal")||scenario.equals("threshold");
        com.mojang.logging.LogUtils.getLogger().info("Cinematic QA 77 camera scenario={} seen={} expected={} passed={}",scenario,seenExecution,expected,seenExecution==expected);
    }
    @SubscribeEvent public static void capture(TickEvent.RenderTickEvent e){
        if(e.phase!=TickEvent.Phase.END||scenario==null||!enabled())return;
        var mc=Minecraft.getInstance();if(mc.screen!=null)return;
        int elapsed=(int)(mc.level.getGameTime()-captureStart);
        int stride=Boolean.getBoolean("bleachmod.higashiPoseQa")?1:2;
        if(elapsed>130||elapsed/stride<=lastFrame)return;lastFrame=elapsed/stride;
        int age=elapsed-20;
        String phase=age<0?"SETUP":age<10?"DRAW":age<16?"CUT":age<56?"HOLD":age<72?"IGNITE":age<88?"ASH":"RETURN";
        String file=String.format(Locale.ROOT,"frame-%04d-t%04d-%s.png",serial++,elapsed,phase);
        try{
            Files.createDirectories(evidence.toPath());
            Screenshot.grab(evidence,file,mc.getMainRenderTarget(),message->{});
            String row=String.format(Locale.ROOT,"{\"file\":\"screenshots/%s\",\"gameTick\":%d,\"wallMs\":%d,\"phase\":\"%s\",\"view\":\"%s\",\"scenario\":\"%s\",\"fpsObserved\":%d,\"width\":%d,\"height\":%d,\"origin\":[%.4f,%.4f,%.4f],\"source\":\"Minecraft Screenshot.grab framebuffer\"}%n",
                    file,elapsed,System.currentTimeMillis(),phase,mc.options.getCameraType()==net.minecraft.client.CameraType.THIRD_PERSON_BACK?"side":"front",scenario,mc.getFps(),mc.getWindow().getWidth(),mc.getWindow().getHeight(),mc.player.getX(),mc.player.getY(),mc.player.getZ());
            var metadata=com.google.gson.JsonParser.parseString(row).getAsJsonObject();metadata.addProperty("executionCamera",com.bleachmod.client.model.HigashiExecutionVisuals.localExecutionActive());
            row=metadata.toString()+System.lineSeparator();
            Files.writeString(evidence.toPath().resolve("frames.jsonl"),row,StandardOpenOption.CREATE,StandardOpenOption.APPEND);
            if(serial==1){
                var env=new com.google.gson.JsonObject();
                env.addProperty("world",mc.getSingleplayerServer().getWorldData().getLevelName());
                env.addProperty("renderer",org.lwjgl.opengl.GL11.glGetString(org.lwjgl.opengl.GL11.GL_RENDERER));
                env.addProperty("scenario",scenario);env.addProperty("captureSamplingTicks",stride);
                env.addProperty("maxHeapBytes",Runtime.getRuntime().maxMemory());env.addProperty("renderDistance",mc.options.renderDistance().get());
                Files.writeString(evidence.toPath().resolve("environment.json"),env.toString());
            }
        }catch(java.io.IOException error){com.mojang.logging.LogUtils.getLogger().warn("Cinematic QA capture failed",error);}
    }
}
