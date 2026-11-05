package com.bleachmod.client.hud;

import com.bleachmod.Reference;
import com.bleachmod.client.gui.NpcQuestScreen;
import com.bleachmod.common.data.PlayerCapability;
import com.bleachmod.common.network.NetworkHandler;
import com.bleachmod.common.network.SyncHelper;
import com.bleachmod.common.network.c2s.QuestActionC2S;
import com.bleachmod.common.quest.*;
import com.bleachmod.entity.AcidwireEntity;
import com.bleachmod.entity.QuestNpcEntity;
import com.bleachmod.registry.ModEntities;
import net.minecraft.client.*;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.tutorial.TutorialSteps;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.*;
import net.minecraft.world.level.levelgen.WorldOptions;
import net.minecraft.world.level.levelgen.presets.WorldPresets;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import java.io.File;
import java.nio.file.*;
import java.util.UUID;
import java.util.function.Consumer;

/** Opt-in model/quest demonstration in a fresh disposable real Minecraft client world. */
@Mod.EventBusSubscriber(modid=Reference.MOD_ID,value=Dist.CLIENT)
public final class AcidwireQaBootstrap {
    private static final String KEY=RukiaOpeningQuest.ORIHIME_KEY;
    private static boolean created;
    private static volatile boolean busy;
    private static volatile RuntimeException failure;
    private static int ticks,waiting,rukiaId,previewId,xpBefore;
    private static UUID rukia,preview,victim,firstActor;
    private static File evidence;
    private static String capture;
    private static boolean enabled() {
        return !net.minecraftforge.fml.loading.FMLEnvironment.production && Boolean.getBoolean("bleachmod.acidwireQa")
                && Minecraft.getInstance().gameDirectory.toPath().toAbsolutePath().normalize().toString().replace('\\','/').endsWith("/run/acidwire-qa");
    }
    private static void require(boolean condition,String message) { if(!condition)throw new IllegalStateException(message); }
    private static void server(Consumer<ServerPlayer> action) {
        var mc=Minecraft.getInstance(); var id=mc.player.getUUID(); busy=true;
        mc.getSingleplayerServer().execute(()->{
            try { var p=mc.getSingleplayerServer().getPlayerList().getPlayer(id); action.accept(p);
                com.bleachmod.server.events.QuestNavigationEvents.refresh(p); SyncHelper.full(p);
            } catch(RuntimeException error){failure=error;}finally{busy=false;}
        });
    }
    private static void action(QuestActionC2S.Action action,int npc) { NetworkHandler.sendToServer(new QuestActionC2S(action,KEY,npc)); }
    private static AcidwireEntity actor(ServerPlayer p) {
        var progress=PlayerCapability.get(p).orElseThrow(() -> new IllegalStateException("Missing capability")).getPlayerQuestData().getProgress(KEY);
        var entity=p.serverLevel().getEntity(progress.getEncounterActor());
        require(entity instanceof AcidwireEntity && entity.isAlive(),"Real Acidwire quest actor missing");
        return (AcidwireEntity)entity;
    }
    private static QuestNpcEntity rukia(ServerPlayer p) { return (QuestNpcEntity)p.serverLevel().getEntity(rukia); }
    @SubscribeEvent public static void tick(TickEvent.ClientTickEvent event) {
        if(event.phase!=TickEvent.Phase.END||!enabled())return;
        var mc=Minecraft.getInstance();
        if(!created&&mc.screen instanceof TitleScreen){
            created=true; var name="Acidwire-QA-"+System.currentTimeMillis(); evidence=new File(mc.gameDirectory,"evidence/"+name);
            var rules=new GameRules(); rules.getRule(GameRules.RULE_DOMOBSPAWNING).set(false,null); rules.getRule(GameRules.RULE_DAYLIGHT).set(false,null);
            var settings=new LevelSettings(name,GameType.CREATIVE,false,net.minecraft.world.Difficulty.NORMAL,true,rules,WorldDataConfiguration.DEFAULT);
            mc.getTutorial().setStep(TutorialSteps.NONE); mc.options.languageCode="pt_br"; mc.getLanguageManager().setSelected("pt_br"); mc.reloadResourcePacks();
            mc.createWorldOpenFlows().createFreshLevel(name,settings,new WorldOptions(110326,false,false),
                    registry->registry.registryOrThrow(net.minecraft.core.registries.Registries.WORLD_PRESET).getHolderOrThrow(WorldPresets.FLAT).value().createWorldDimensions());
        }
        if(mc.player==null||!mc.hasSingleplayerServer()||!mc.getSingleplayerServer().getWorldData().getLevelName().startsWith("Acidwire-QA-"))return;
        if(failure!=null)throw failure; if(busy)return; ticks++;
        var data=PlayerCapability.get(mc.player).orElse(null);
        boolean ready=ticks==250?data!=null&&data.getPlayerQuestData().isAccepted(KEY)
                :ticks==275||ticks==605||ticks==705?mc.screen instanceof NpcQuestScreen
                :ticks==320?data!=null&&data.getPlayerQuestData().getProgress(KEY)!=null&&data.getPlayerQuestData().getProgress(KEY).isEncounterStarted()
                :ticks==405?mc.player.isAlive()&&data!=null&&RukiaOpeningQuest.isRetryPending(data.getPlayerQuestData().getProgress(KEY))
                :ticks==470||ticks==540?data!=null&&data.getPlayerQuestData().getProgress(KEY).isEncounterStarted()
                :ticks==565?data!=null&&RukiaOpeningQuest.stage(data.getPlayerQuestData().getProgress(KEY))==2
                :ticks==650?data!=null&&data.getPlayerQuestData().isCompleted(KEY):true;
        if(!ready){if(++waiting>300)throw new IllegalStateException("Acidwire QA sync timeout "+ticks);ticks--;return;}waiting=0;
        if(ticks==20){
            mc.options.pauseOnLostFocus=false;mc.options.guiScale().set(2);mc.resizeDisplay();mc.setScreen(null);
            server(p->{
                var d=PlayerCapability.get(p).orElseThrow(() -> new IllegalStateException("Missing capability"));d.initializeShinigami();d.getPlayerQuestData().completeQuest(RukiaOpeningQuest.KEY);
                var npc=ModEntities.NPC_RUKIA.get().create(p.serverLevel());npc.moveTo(20.5,-60,.5,180,0);npc.setQuestContext("karakura_orihime");p.serverLevel().addFreshEntity(npc);rukia=npc.getUUID();rukiaId=npc.getId();
                com.bleachmod.server.events.OrihimeQuestEvents.provision(p);
                var a=ModEntities.ACIDWIRE.get().create(p.serverLevel());a.moveTo(.5,-60,.5,180,0);
                a.goalSelector.removeAllGoals(g->true);a.targetSelector.removeAllGoals(g->true);p.serverLevel().addFreshEntity(a);preview=a.getUUID();previewId=a.getId();
                p.connection.teleport(.5,-60,-10.5,0,-12);
            });
        }
        if(ticks>20&&ticks<45)mc.setScreen(null);
        if(ticks==60)capture="01-acidwire-front-idle";
        if(ticks==75)server(p->p.connection.teleport(10.5,-60,.5,90,-12));
        if(ticks==95)capture="02-acidwire-side-idle";
        if(ticks==110)server(p->p.connection.teleport(.5,-60,10.5,180,-12));
        if(ticks==125)capture="03-acidwire-rear-idle";
        if(ticks==130)server(p->{p.connection.teleport(5.5,-60,-10.5,0,-12);((AcidwireEntity)p.serverLevel().getEntity(preview)).getNavigation().moveTo(8.5,-60,.5,1);});
        if(ticks==140)capture="04-acidwire-walk";
        if(ticks==175)server(p->{
            var a=(AcidwireEntity)p.serverLevel().getEntity(preview);a.getNavigation().stop();a.moveTo(.5,-60,.5,180,0);
            var z=EntityType.HUSK.create(p.serverLevel());z.setNoAi(true);z.setPos(.5,-60,-2);p.serverLevel().addFreshEntity(z);victim=z.getUUID();
            a.setTarget(z);a.beginAttack(z);require(a.isAttacking(),"Authored attack did not start in client");p.connection.teleport(6.5,-60,-5.5,45,-12);
        });
        if(ticks==179)capture="05-acidwire-claw-windup";
        if(ticks==187)capture="06-acidwire-claw-impact";
        if(ticks==193)capture="07-acidwire-claw-recovery";
        if(ticks==215){server(p->{p.serverLevel().getEntity(preview).discard();p.serverLevel().getEntity(victim).discard();});mc.setScreen(null);action(QuestActionC2S.Action.START,-1);}
        if(ticks==250)server(p->{var n=rukia(p);p.connection.teleport(n.getX(),n.getY(),n.getZ()-2,0,0);n.mobInteract(p,net.minecraft.world.InteractionHand.MAIN_HAND);});
        if(ticks==280)capture="08-quest-introduction";
        if(ticks==295){mc.setScreen(null);action(QuestActionC2S.Action.BEGIN,rukiaId);}
        if(ticks==320)server(p->{var a=actor(p);firstActor=a.getUUID();p.connection.teleport(a.getX(),a.getY(),a.getZ()-9,0,-12);});
        if(ticks==335)capture="09-real-acidwire-quest-boss";
        if(ticks==350)server(p->p.kill());
        if(ticks==380)mc.player.respawn();
        if(ticks==405)server(p->{var n=rukia(p);p.connection.teleport(n.getX(),n.getY(),n.getZ()-3,0,0);
            var progress=PlayerCapability.get(p).orElseThrow(() -> new IllegalStateException("Missing capability")).getPlayerQuestData().getProgress(KEY);
            require(progress.getObjectiveProgress(0)==1&&rukia.equals(progress.getStoryTargetUuid()),"Player death lost introduction or Rukia");
            require(p.serverLevel().getEntity(firstActor)==null,"Player death left previous Acidwire");});
        if(ticks==425)capture="10-player-respawn-combat-checkpoint";
        if(ticks==440)action(QuestActionC2S.Action.BEGIN,-1);
        if(ticks==470)server(p->{var a=actor(p);require(!firstActor.equals(a.getUUID()),"Retry reused Acidwire actor");a.hurt(p.damageSources().generic(),100000);
            RukiaOpeningQuest.recoverMissingEncounter(p);require(RukiaOpeningQuest.isRetryPending(PlayerCapability.get(p).orElseThrow(() -> new IllegalStateException("Missing capability")).getPlayerQuestData().getProgress(KEY)),"Environmental death left combat limbo");});
        if(ticks==490)capture="11-environmental-death-retry";
        if(ticks==510)action(QuestActionC2S.Action.BEGIN,-1);
        if(ticks==540)server(p->{actor(p).hurt(p.damageSources().playerAttack(p),100000);xpBefore=p.totalExperience;
            var d=PlayerCapability.get(p).orElseThrow(() -> new IllegalStateException("Missing capability"));require(RukiaOpeningQuest.stage(d.getPlayerQuestData().getProgress(KEY))==2&&!d.getPlayerQuestData().isCompleted(KEY),"Owner kill skipped return stage");
            require(!QuestAvailabilityChecker.isVisible(d,QuestRegistry.getQuest("shinigami_substitute:3")),"Chad unlocked before final dialogue");});
        if(ticks==565)capture="12-victory-return-stage";
        if(ticks==580)server(p->{var n=rukia(p);p.connection.teleport(n.getX(),n.getY(),n.getZ()-2,0,0);n.mobInteract(p,net.minecraft.world.InteractionHand.MAIN_HAND);});
        if(ticks==610)capture="13-final-dialogue";
        if(ticks==625){mc.setScreen(null);action(QuestActionC2S.Action.BEGIN,rukiaId);}
        if(ticks==650)server(p->{var d=PlayerCapability.get(p).orElseThrow(() -> new IllegalStateException("Missing capability"));require(p.totalExperience==xpBefore+100,"Final dialogue did not give exactly100XP");
            RukiaOpeningQuest.dialogue(p,rukia(p),KEY);QuestService.claimReward(p,KEY,-1);require(p.totalExperience==xpBefore+100,"Repeated claim duplicatedXP");
            require(QuestAvailabilityChecker.isVisible(d,QuestRegistry.getQuest("shinigami_substitute:3")),"Final dialogue did not unlock Chad");});
        if(ticks==675)capture="14-quest-complete-reward";
        if(ticks==690)server(p->rukia(p).mobInteract(p,net.minecraft.world.InteractionHand.MAIN_HAND));
        if(ticks==715)capture="15-completed-dialogue-reward-claimed";
        if(ticks==735){com.mojang.logging.LogUtils.getLogger().info("Acidwire QA complete: {}",evidence);mc.stop();}
    }
    @SubscribeEvent public static void render(TickEvent.RenderTickEvent event){
        if(event.phase!=TickEvent.Phase.END||!enabled()||capture==null)return;
        var mc=Minecraft.getInstance();var name=capture+".png";capture=null;
        try{Files.createDirectories(evidence.toPath());Screenshot.grab(evidence,name,mc.getMainRenderTarget(),message->{});
            int attackTick=-1;boolean attacking=false;
            if(mc.level!=null&&mc.level.getEntity(previewId) instanceof AcidwireEntity a){attackTick=a.getAttackTick();attacking=a.isAttacking();}
            Files.writeString(evidence.toPath().resolve("frames.jsonl"),"{\"file\":\"screenshots/"+name+"\",\"tick\":"+ticks+",\"attackTick\":"+attackTick+",\"attacking\":"+attacking+",\"source\":\"Minecraft Screenshot.grab framebuffer\"}\n",StandardOpenOption.CREATE,StandardOpenOption.APPEND);
        }catch(Exception error){throw new IllegalStateException("Acidwire QA capture",error);}
    }
}
