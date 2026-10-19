package com.bleachmod.client.cinematic;

import com.bleachmod.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.world.level.*;
import net.minecraft.world.level.levelgen.WorldOptions;
import net.minecraft.world.level.levelgen.presets.WorldPresets;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/** Explicit QA launch property + isolated run directory. Never opens or replaces an existing save. */
@Mod.EventBusSubscriber(modid=Reference.MOD_ID,value=Dist.CLIENT)
public final class BenihimeQaBootstrap {
    private static boolean created,started;
    private static int ready;
    private static boolean actualStarted,actualSeen,followCompleted;
    private static int followTicks;
    private static net.minecraft.client.CameraType restoredCamera;
    private static boolean nightRun;
    private BenihimeQaBootstrap(){ }
    @SubscribeEvent public static void tick(TickEvent.ClientTickEvent e){
        if(e.phase!=TickEvent.Phase.END||net.minecraftforge.fml.loading.FMLEnvironment.production||!Boolean.getBoolean("bleachmod.benihimeQa"))return;
        var mc=Minecraft.getInstance();
        if(!mc.gameDirectory.getAbsolutePath().replace('\\','/').contains("/run-benihime-qa/"))return;
        if(!created&&mc.screen instanceof TitleScreen){
            created=true;String name="Benihime-QA-68-"+System.currentTimeMillis();
            var rules=new GameRules();rules.getRule(GameRules.RULE_DOMOBSPAWNING).set(false,null);
            rules.getRule(GameRules.RULE_DAYLIGHT).set(false,null);rules.getRule(GameRules.RULE_WEATHER_CYCLE).set(false,null);
            var settings=new LevelSettings(name,GameType.CREATIVE,false,net.minecraft.world.Difficulty.PEACEFUL,true,rules,WorldDataConfiguration.DEFAULT);
            mc.createWorldOpenFlows().createFreshLevel(name,settings,new WorldOptions(681019,false,false),registry->registry.registryOrThrow(net.minecraft.core.registries.Registries.WORLD_PRESET).getHolderOrThrow(WorldPresets.FLAT).value().createWorldDimensions());
        }
        if(!started&&mc.level!=null&&mc.player!=null&&mc.hasSingleplayerServer()
                &&mc.getSingleplayerServer().getWorldData().getLevelName().startsWith("Benihime-QA-68-")
                &&com.bleachmod.common.data.PlayerCapability.get(mc.player).map(d->d.getStatus().hasCreatedCharacter()).orElse(false)){
            if(++ready<100)return;started=true;mc.setScreen(null);mc.options.pauseOnLostFocus=false;mc.options.renderDistance().set(6);mc.getTutorial().setStep(net.minecraft.client.tutorial.TutorialSteps.NONE);mc.getTutorial().stop();mc.getToasts().clear();
            mc.player.connection.sendCommand("time set noon");
            mc.player.connection.sendCommand("weather clear");
            // Local commands are dispatched directly; a server must never receive this renderer bench.
            net.minecraftforge.client.ClientCommandHandler.runCommand("benihimeqa record");
        }
        if(started&&!actualStarted&&BenihimeEntranceClient.benchFollowing()){
            actualStarted=true;com.mojang.logging.LogUtils.getLogger().info("Benihime QA: starting authoritative release after local bench");
            net.minecraftforge.client.ClientCommandHandler.runCommand("benihimeqa release");
        }
        if(actualStarted&&BenihimeEntranceClient.localEntranceActive())actualSeen=true;
        if(actualSeen&&!BenihimeEntranceClient.localEntranceActive()&&!followCompleted&&mc.player!=null){
            followTicks++;
            if(followTicks==1){restoredCamera=mc.options.getCameraType();com.mojang.logging.LogUtils.getLogger().info("Benihime QA: production entrance ended, camera restored to {}",restoredCamera);mc.options.setCameraType(net.minecraft.client.CameraType.THIRD_PERSON_FRONT);}
            if(followTicks==20)BenihimeEntranceClient.captureFollowing("FOLLOW-before");
            if(followTicks==30){var server=mc.getSingleplayerServer();var uuid=mc.player.getUUID();server.execute(()->{var p=server.getPlayerList().getPlayer(uuid);if(p!=null)p.teleportTo(p.serverLevel(),p.getX()+6,p.getY(),p.getZ()+3,45,0);});}
            if(followTicks==70)BenihimeEntranceClient.captureFollowing("FOLLOW-after");
            if(followTicks==85){
                mc.options.setCameraType(restoredCamera);followCompleted=true;
                if(!nightRun){nightRun=true;actualSeen=false;followTicks=0;followCompleted=false;
                    mc.player.connection.sendCommand("time set midnight");
                    com.mojang.logging.LogUtils.getLogger().info("Benihime QA: daylight follow complete; repeating authoritative release at night");
                    net.minecraftforge.client.ClientCommandHandler.runCommand("benihimeqa release");
                }else {BenihimeEntranceClient.endRecording();com.mojang.logging.LogUtils.getLogger().info("Benihime QA: night follow translation/turn captured; automated run finished");}
            }
        }
    }
}
