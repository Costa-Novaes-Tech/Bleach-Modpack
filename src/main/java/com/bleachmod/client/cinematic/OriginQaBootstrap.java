package com.bleachmod.client.cinematic;

import com.bleachmod.Reference;
import com.bleachmod.common.data.*;
import com.bleachmod.common.network.*;
import com.bleachmod.common.network.c2s.ConfirmCharacterC2S;
import com.bleachmod.common.story.ShinigamiOriginService;
import com.bleachmod.entity.HollowBossEntity;
import com.bleachmod.common.visual.OriginTimeline;
import net.minecraft.client.*;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.world.level.*;
import net.minecraft.world.level.levelgen.WorldOptions;
import net.minecraft.world.level.levelgen.presets.WorldPresets;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import java.io.File;
import java.nio.file.*;

/** Developer-only new-save fixture. Exercises the production confirmation packet and scene clock. */
@Mod.EventBusSubscriber(modid=Reference.MOD_ID,value=Dist.CLIENT)
public final class OriginQaBootstrap {
    private static boolean created,started,ended;
    private static int ticks,after,lastFrame=-1;
    private static String capture;
    private static File evidence;
    private static float lockedYaw;
    private static net.minecraft.world.phys.Vec3 anchor;
    private static boolean enabled() { return !net.minecraftforge.fml.loading.FMLEnvironment.production&&Boolean.getBoolean("bleachmod.originQa"); }
    @SubscribeEvent public static void tick(TickEvent.ClientTickEvent e) {
        if(e.phase!=TickEvent.Phase.END||!enabled())return;
        var mc=Minecraft.getInstance();
        if(!created&&mc.screen instanceof TitleScreen) {
            created=true;String name="Origin-QA-"+System.currentTimeMillis();evidence=new File(mc.gameDirectory,"evidence/"+name);
            var rules=new GameRules();rules.getRule(GameRules.RULE_DOMOBSPAWNING).set(false,null);
            rules.getRule(GameRules.RULE_DAYLIGHT).set(false,null);rules.getRule(GameRules.RULE_WEATHER_CYCLE).set(false,null);
            mc.createWorldOpenFlows().createFreshLevel(name,new LevelSettings(name,GameType.SURVIVAL,false,net.minecraft.world.Difficulty.NORMAL,true,rules,WorldDataConfiguration.DEFAULT),
                    new WorldOptions(102826,false,false),r->r.registryOrThrow(net.minecraft.core.registries.Registries.WORLD_PRESET).getHolderOrThrow(WorldPresets.FLAT).value().createWorldDimensions());
        }
        if(mc.player==null||mc.level==null||!mc.hasSingleplayerServer()||!mc.getSingleplayerServer().getWorldData().getLevelName().startsWith("Origin-QA-"))return;
        ticks++;
        if(ticks==10) {
            mc.options.pauseOnLostFocus=false;mc.options.guiScale().set(2);mc.resizeDisplay();mc.options.setCameraType(CameraType.FIRST_PERSON);
            mc.getTutorial().setStep(net.minecraft.client.tutorial.TutorialSteps.NONE);mc.getTutorial().stop();mc.getToasts().clear();
            var id=mc.player.getUUID();mc.getSingleplayerServer().execute(()->{
                var p=mc.getSingleplayerServer().getPlayerList().getPlayer(id);p.serverLevel().getChunk(75,75);
                int y=p.serverLevel().getHeight(net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING,1200,1200);
                p.connection.teleport(1200.5,y,1200.5,67,0);p.serverLevel().setDayTime(1000);p.setInvulnerable(true);
            });
        }
        if(ticks==65) { mc.setScreen(null);NetworkHandler.sendToServer(new ConfirmCharacterC2S(Reference.RACE_SHINIGAMI,new net.minecraft.nbt.CompoundTag())); }
        float age=OriginCinematicClient.localAge();
        if(age>=0) {
            if(!started) { started=true;anchor=mc.player.position();lockedYaw=mc.player.getYRot(); }
            // Use the actual Entity.turn path used by the mouse; injected yaw must be ignored.
            mc.player.turn(40,30);
            float scriptedYaw=lockedYaw+OriginTimeline.playerTurn(age);
            require(Math.abs(net.minecraft.util.Mth.wrapDegrees(mc.player.getYRot()-scriptedYaw))<.01,"mouse cannot override directed turn");
            require(Math.abs(net.minecraft.util.Mth.wrapDegrees(mc.player.yBodyRot-scriptedYaw))<.01,"body follows directed turn");
            var direction=net.minecraft.world.phys.Vec3.directionFromRotation(0,lockedYaw);
            require(mc.player.position().distanceToSqr(anchor.add(direction.scale(OriginTimeline.playerAdvance(age))))<.02,"player follows only scripted approach");
            int frame=(int)age;
            if(frame!=lastFrame&&frame%2==0) { lastFrame=frame;capture=String.format("scene-%03d",frame); }
        }
        if(started&&age<0&&!ended) {
            ended=true;require(mc.options.getCameraType()==CameraType.FIRST_PERSON,"previous camera restored");
            require(PlayerCapability.get(mc.player).map(d->d.getOrigin().stage()==OriginData.Stage.FIGHT).orElse(false),"combat checkpoint synchronized");
            var boss=mc.level.entitiesForRendering().iterator();boolean missing=false;
            while(boss.hasNext()) { var entity=boss.next();if(entity instanceof HollowBossEntity b)missing|=b.isOriginArmLost(); }
            require(missing,"boss missing arm after reveal");
            require(mc.player.getMainHandItem().is(com.bleachmod.init.ModItems.ASAUCHI.get()),"starter sword equipped on client");
            capture="01-control-restored";
        }
        if(ended) {
            after++;
            if(after==25) {
                for(int i=0;i<9;i++)if(mc.player.getInventory().getItem(i).is(com.bleachmod.init.ModItems.STORY_JOURNAL.get())) { mc.player.getInventory().selected=i;break; }
                mc.gameMode.useItem(mc.player,net.minecraft.world.InteractionHand.MAIN_HAND);
                require(mc.screen instanceof com.bleachmod.client.gui.StoryBookScreen,"book item opens live pages");capture="02-live-book-fight";
            }
            if(after==45) {
                var id=mc.player.getUUID();mc.getSingleplayerServer().execute(()->{
                    var p=mc.getSingleplayerServer().getPlayerList().getPlayer(id);
                    PlayerCapability.get(p).ifPresent(d->{var b=p.serverLevel().getEntity(d.getOrigin().boss());
                        require(b instanceof HollowBossEntity,"boss retained after camera exit");
                        b.hurt(p.damageSources().playerAttack(p),10000);
                    });
                });
            }
            if(after==65) { require(PlayerCapability.get(mc.player).map(d->d.getOrigin().stage()==OriginData.Stage.COMPLETE&&d.getPlayerQuestData().isCompleted(ShinigamiOriginService.QUEST)).orElse(false),"victory updates book and quest");capture="03-live-book-complete"; }
            if(after==70) {
                var next=mc.screen.children().stream().filter(c->c instanceof net.minecraft.client.gui.components.Button b&&b.getMessage().getString().equals("›"))
                        .map(c->(net.minecraft.client.gui.components.Button)c).findFirst().orElseThrow();
                for(int i=0;i<20;i++)next.onPress();capture="04-study-notes-last-page";
            }
            if(after==75) {
                mc.screen.children().stream().filter(c->c instanceof net.minecraft.client.gui.components.Button b&&b.getMessage().getString().equals("‹"))
                        .map(c->(net.minecraft.client.gui.components.Button)c).findFirst().orElseThrow().onPress();capture="05-study-notes-disciplines";
            }
            if(after==80) { float before=mc.player.getYRot();mc.player.turn(10,0);require(mc.player.getYRot()!=before,"mouse turn restored");com.mojang.logging.LogUtils.getLogger().info("Origin QA COMPLETE: {}",evidence);mc.stop(); }
        }
        if(ticks>700)throw new IllegalStateException("Origin QA timed out; scene did not finish");
    }
    private static void require(boolean condition,String label) { if(!condition)throw new IllegalStateException("Origin QA FAIL: "+label); }
    @SubscribeEvent public static void render(TickEvent.RenderTickEvent e) {
        if(e.phase!=TickEvent.Phase.END||!enabled()||capture==null||evidence==null)return;
        var mc=Minecraft.getInstance();String name=capture;capture=null;
        try {
            Files.createDirectories(evidence.toPath());
            Screenshot.grab(evidence,name+".png",mc.getMainRenderTarget(),m->{});
            var meta=new com.google.gson.JsonObject();meta.addProperty("frame",name);meta.addProperty("sceneTick",OriginCinematicClient.localAge());
            meta.addProperty("source","Minecraft Screenshot.grab framebuffer");meta.addProperty("camera",mc.options.getCameraType().name());
            meta.addProperty("cameraX",mc.gameRenderer.getMainCamera().getPosition().x);meta.addProperty("cameraY",mc.gameRenderer.getMainCamera().getPosition().y);meta.addProperty("cameraZ",mc.gameRenderer.getMainCamera().getPosition().z);
            meta.addProperty("playerX",mc.player.getX());meta.addProperty("playerZ",mc.player.getZ());meta.addProperty("playerYaw",mc.player.getYRot());
            for(var entity:mc.level.entitiesForRendering()) {
                if(entity instanceof HollowBossEntity b) { meta.addProperty("armLost",b.isOriginArmLost());meta.addProperty("bossX",b.getX());meta.addProperty("bossZ",b.getZ()); }
                if(entity instanceof com.bleachmod.entity.QuestNpcEntity npc&&npc.getNpcId().equals("rukia")) {
                    meta.addProperty("rukiaX",npc.getX());meta.addProperty("rukiaZ",npc.getZ());meta.addProperty("rukiaPose",npc.getPose().name());
                }
            }
            Files.writeString(evidence.toPath().resolve("frames.jsonl"),meta+System.lineSeparator(),StandardOpenOption.CREATE,StandardOpenOption.APPEND);
        }catch(Exception ex) { throw new IllegalStateException("Origin QA screenshot",ex); }
    }
}
