package com.bleachmod.client.cinematic;

import com.bleachmod.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/** Explicit development-only, fresh QA-save scenarios; never touches the user's worlds. */
@Mod.EventBusSubscriber(modid=Reference.MOD_ID,value=Dist.CLIENT)
public final class BenihimeStageQaSequence {
    private static int scenario,wait,elapsed,endedAt;
    private static boolean seen;
    private BenihimeStageQaSequence(){ }
    private static boolean enabled(){
        var mc=Minecraft.getInstance();
        return !net.minecraftforge.fml.loading.FMLEnvironment.production&&Boolean.getBoolean("bleachmod.benihimeQa")
                &&Boolean.getBoolean("bleachmod.benihimeStageQa")&&mc.player!=null&&mc.player.hasPermissions(2)
                &&mc.hasSingleplayerServer()&&mc.gameDirectory.getAbsolutePath().replace('\\','/').contains("/run-benihime-qa/")
                &&mc.getSingleplayerServer().getWorldData().getLevelName().startsWith("Benihime-QA-68-");
    }
    @SubscribeEvent public static void tick(TickEvent.ClientTickEvent e){
        if(e.phase!=TickEvent.Phase.END||!enabled()||!BenihimeQaBootstrap.automatedFinished())return;
        var mc=Minecraft.getInstance();if(mc.screen!=null||mc.isPaused()||scenario>=6)return;
        if(scenario==0||scenario==2||scenario==4){
            if(++wait<35)return;wait=0;
            int current=scenario;var uuid=mc.player.getUUID();var server=mc.getSingleplayerServer();
            server.execute(()->{
                var p=server.getPlayerList().getPlayer(uuid);if(p==null)return;
                var level=p.serverLevel();int x=40+current*30,z=40;
                int y=level.getHeight(net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING,x,z);
                level.setDayTime(6000);
                if(current<4){
                    for(int dx=-6;dx<=6;dx++)for(int dy=0;dy<13;dy++)
                        level.setBlockAndUpdate(new BlockPos(x+dx,y+dy,z-7),current==0?Blocks.TERRACOTTA.defaultBlockState():Blocks.OAK_LEAVES.defaultBlockState());
                }
                p.teleportTo(level,x+.5,y+.05,z+.5,0,0);
            });
            scenario++;elapsed=0;endedAt=0;seen=false;
        }else{
            elapsed++;
            if(elapsed==20){
                mc.options.setCameraType(net.minecraft.client.CameraType.FIRST_PERSON);
                net.minecraftforge.client.ClientCommandHandler.runCommand("benihimeqa release");
            }
            if(BenihimeEntranceClient.localEntranceActive())seen=true;
            else if(seen&&endedAt==0)endedAt=elapsed;
            if(scenario==5&&elapsed==210){
                var server=mc.getSingleplayerServer();var uuid=mc.player.getUUID();
                server.execute(()->{var p=server.getPlayerList().getPlayer(uuid);if(p!=null)p.teleportTo(p.serverLevel(),p.getX()+3,p.getY(),p.getZ(),p.getYRot(),0);});
            }
            if(scenario==5&&elapsed==240){
                BenihimeEntranceClient.captureFollowing("MOVE-retained");
                com.mojang.logging.LogUtils.getLogger().info("Benihime stage QA movement preservation: sceneActive={}, camera={}",BenihimeEntranceClient.localEntranceActive(),mc.options.getCameraType());
            }
            if(seen&&!BenihimeEntranceClient.localEntranceActive()&&elapsed>240){
                BenihimeEntranceClient.captureFollowing("STAGE-"+scenario+"-return");
                boolean passed=(endedAt>=540)
                        &&mc.options.getCameraType()==net.minecraft.client.CameraType.FIRST_PERSON;
                com.mojang.logging.LogUtils.getLogger().info("Benihime stage QA scenario {}: passed={}, endedAt={}, camera={}",scenario,passed,endedAt,mc.options.getCameraType());
                scenario++;wait=0;
                if(scenario==6)com.mojang.logging.LogUtils.getLogger().info("Benihime stage QA 74 complete");
            }
        }
    }
}
