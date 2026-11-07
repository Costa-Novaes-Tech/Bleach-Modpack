package com.bleachmod.client.cinematic;

import com.bleachmod.Reference;
import com.bleachmod.common.data.PlayerCapability;
import com.bleachmod.common.network.SyncHelper;
import com.bleachmod.common.technique.ReleaseAnimationService;
import net.minecraft.client.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import java.nio.file.*;

/** Explicit opt-in: real dispatcher, synchronized client and screenshots in the disposable QA world. */
@Mod.EventBusSubscriber(modid=Reference.MOD_ID,value=Dist.CLIENT)
public final class DevCommandQaSequence {
    private static int stage, wait;
    private static volatile int completed=-1;
    private static volatile String failure;
    private static Path output;
    private static CameraType initialCamera;
    private static final String[] NAMES={"benihime-all","quincy","senbon-all","cheats-off","benihime-entrance","reset-camera","help"};
    private DevCommandQaSequence() { }

    @SubscribeEvent public static void tick(TickEvent.ClientTickEvent event) {
        var mc=Minecraft.getInstance();
        if(event.phase!=TickEvent.Phase.END || net.minecraftforge.fml.loading.FMLEnvironment.production
                || !Boolean.getBoolean("bleachmod.devCommandsQa") || !Boolean.getBoolean("bleachmod.benihimeQa")
                || !mc.hasSingleplayerServer() || mc.player==null || !BenihimeQaBootstrap.automatedFinished()
                || !mc.getSingleplayerServer().getWorldData().getLevelName().startsWith("Benihime-QA-68-"))return;
        if(output==null){
            output=mc.gameDirectory.toPath().resolve("dev-command-qa/"+System.currentTimeMillis());
            try{Files.createDirectories(output);}catch(Exception ex){throw new IllegalStateException(ex);}
            initialCamera=mc.options.getCameraType();
            com.mojang.logging.LogUtils.getLogger().info("DEV_COMMAND_QA output={}",output.toAbsolutePath());
        }
        if(stage>=NAMES.length){return;}
        if(wait==0){
            final int test=stage;var server=mc.getSingleplayerServer();var uuid=mc.player.getUUID();
            server.execute(()->{
                try{
                    var p=server.getPlayerList().getPlayer(uuid);if(p==null)throw new IllegalStateException("Player missing");
                    var source=p.createCommandSourceStack().withPermission(2);var dispatcher=server.getCommands().getDispatcher();
                    java.util.function.Consumer<String> command=cmd->{try{dispatcher.execute("bleachdev "+cmd,source);}catch(Exception ex){throw new IllegalStateException(cmd,ex);}};
                    switch(test){
                        case 0->{command.accept("kit shinigami benihime");command.accept("cheats all");}
                        case 1->command.accept("kit quincy");
                        case 2->command.accept("kit shinigami senbonzakura");
                        case 3->{command.accept("cheats off");var d=SyncHelper.require(p);d.getResources().setCurrentReiatsu(0);if(d.getResources().consumeReiatsu(1))throw new IllegalStateException("Off failed to restore cost");d.getResources().fillReiatsu();SyncHelper.full(p);}
                        case 4->{command.accept("kit shinigami benihime");command.accept("cheats all");ReleaseAnimationService.begin(p,SyncHelper.require(p),"zanpakuto","bankai",0);}
                        case 5->command.accept("reset combat");
                        case 6->command.accept("help");
                    }
                    com.mojang.logging.LogUtils.getLogger().info("DEV_COMMAND_QA server={} state={}",NAMES[test],SyncHelper.require(p).save());
                }catch(Exception ex){failure=ex.toString();com.mojang.logging.LogUtils.getLogger().error("DEV_COMMAND_QA failed",ex);}
                finally{completed=test;}
            });
        }
        if(++wait<(stage==4?100:35) || completed!=stage)return;
        var data=PlayerCapability.get(mc.player).orElseThrow(()->new IllegalStateException("Missing client capability"));
        boolean ok=failure==null;
        switch(stage){
            case 0->ok&=data.getCharacter().getRace().equals("shinigami")&&data.getCharacter().getMastery("zanpakuto","sealed")==100;
            case 1->ok&=data.getCharacter().getRace().equals("quincy")&&data.getCharacter().getActiveFormGroup().isEmpty();
            case 2->ok&=data.getCharacter().getZanpakutoIdentity().equals("senbonzakura")&&data.getCharacter().getTechniqueMastery("bankai",4)==100;
            case 3->ok&=!data.getStatus().areHudCooldownsDisabled();
            case 4->ok&=BenihimeEntranceClient.localEntranceActive();
            case 5->ok&=!BenihimeEntranceClient.localEntranceActive()&&mc.options.getCameraType()==initialCamera;
        }
        String result=NAMES[stage]+" "+(ok?"PASS":"FAIL")+" camera="+mc.options.getCameraType()+" failure="+failure+"\n";
        try{Files.writeString(output.resolve("results.txt"),result,StandardOpenOption.CREATE,StandardOpenOption.APPEND);}catch(Exception ex){throw new IllegalStateException(ex);}
        Screenshot.grab(output.toFile(),NAMES[stage]+".png",mc.getMainRenderTarget(),ignored->{});
        com.mojang.logging.LogUtils.getLogger().info("DEV_COMMAND_QA {}",result.trim());
        stage++;wait=0;
        if(stage==NAMES.length && Boolean.getBoolean("bleachmod.qaAutoExit"))mc.stop();
    }
}
