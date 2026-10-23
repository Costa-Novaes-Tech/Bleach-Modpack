package com.bleachmod.client.cinematic;

import com.bleachmod.Reference;
import com.bleachmod.common.data.PlayerCapability;
import com.bleachmod.common.network.SyncHelper;
import com.bleachmod.common.technique.*;
import com.bleachmod.common.visual.HigashiCinematicPath;
import com.bleachmod.init.ModItems;
import com.mojang.logging.LogUtils;
import net.minecraft.client.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Husk;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.*;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.ServerLifecycleHooks;
import java.nio.file.*;
import java.util.*;

/** Opt-in development QA. Actual vanilla attacks/technique service, no release-scene changes. */
@Mod.EventBusSubscriber(modid=Reference.MOD_ID,value=Dist.CLIENT)
public final class BenihimeCombatQaSequence {
    private static final String[] CASES={"sealed-melee","shikai-melee","bankai-melee","shikai-first-person","nake","shield","shibari","hiasobi","dissection","reconstruction","path","final-stitch","hiasobi-standalone","sealed-draw","sealed-byakurai","sealed-rods","sealed-gigai","entrance-walk"};
    private record Trial(UUID owner,UUID target,long start,int index,Vec3 origin) { }
    private static volatile Trial trial;
    private static int stage,wait,lastFrame=-1;
    private static Path evidence;
    private static CameraType previousCamera;
    private static boolean previousHud;
    private static java.lang.reflect.Method cameraPosition;
    private BenihimeCombatQaSequence() { }
    private static boolean enabled(){
        var mc=Minecraft.getInstance();return !net.minecraftforge.fml.loading.FMLEnvironment.production
                &&Boolean.getBoolean("bleachmod.benihimeCombatQa")&&Boolean.getBoolean("bleachmod.benihimeQa")
                &&mc.hasSingleplayerServer()&&mc.player!=null&&mc.player.hasPermissions(2)
                &&mc.gameDirectory.getAbsolutePath().replace('\\','/').contains("/run-benihime-qa/")
                &&mc.getSingleplayerServer().getWorldData().getLevelName().startsWith("Benihime-QA-68-");
    }
    @SubscribeEvent public static void tick(TickEvent.ClientTickEvent e){
        if(Boolean.getBoolean("bleachmod.hyorinmaruQa")&&!HyorinmaruQaSequence.automatedFinished())return;
        if(e.phase!=TickEvent.Phase.END||!enabled()||!(Boolean.getBoolean("bleachmod.cinematicQa")?CinematicQaSequence.automatedFinished():BenihimeQaBootstrap.automatedFinished()))return;
        var mc=Minecraft.getInstance();if(mc.screen!=null||mc.isPaused())return;
        var current=trial;if(current!=null){long age=mc.level.getGameTime()-current.start();mc.options.keyAttack.setDown(current.index()<4&&age>=30&&age<95);mc.options.keyUp.setDown(current.index()==17&&age>=180&&age<200);}
        wait++;if(stage==0&&wait<40)return;
        if(stage==0){LogUtils.getLogger().info("Benihime QA 81 loaded attacks: {}",net.bettercombat.client.animation.AnimationRegistry.animations.keySet().stream().filter(n->n.contains("benihime")).toList());previousCamera=mc.options.getCameraType();previousHud=mc.options.hideGui;}
        if(stage<(Boolean.getBoolean("bleachmod.benihimeMeleeQa")?4:CASES.length)&&(stage==0||wait>=(trial!=null&&trial.index()==17?660:140))){
            var name=CASES[stage];var id=mc.player.getUUID();int index=stage++;wait=0;lastFrame=-1;
            mc.options.setCameraType(index==3?CameraType.FIRST_PERSON:CameraType.THIRD_PERSON_BACK);mc.options.hideGui=index!=3;
            evidence=mc.gameDirectory.toPath().resolve("benihime-combat-qa/"+System.currentTimeMillis()+"-"+name);
            mc.getSingleplayerServer().execute(()->prepare(mc.getSingleplayerServer().getPlayerList().getPlayer(id),index));
        }else if(stage==(Boolean.getBoolean("bleachmod.benihimeMeleeQa")?4:CASES.length)&&wait>=(trial!=null&&trial.index()==17?660:140)){
            stage++;trial=null;mc.options.keyAttack.setDown(false);mc.options.keyUp.setDown(false);mc.options.setCameraType(previousCamera);mc.options.hideGui=previousHud;
            LogUtils.getLogger().info("Benihime combat QA 84 complete; normal camera/HUD restored");
            if(Boolean.getBoolean("bleachmod.qaAutoExit"))mc.stop();
        }
    }
    private static void prepare(ServerPlayer p,int index){
        if(p==null)return;p.getServer().setDifficulty(net.minecraft.world.Difficulty.NORMAL,true);
        ReleaseAnimationService.cancel(p);BenihimeEntranceService.cancel(p);RyujinTechniqueService.cancel(p);BenihimeTechniqueService.cancel(p);SignatureAnimationService.cancel(p);
        var d=PlayerCapability.get(p).orElse(null);if(d==null)return;
        int y=p.serverLevel().getHeight(net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,20,0);
        p.teleportTo(p.serverLevel(),20.5,y,.5,0,0);
        d.getCharacter().bindZanpakuto("benihime");d.getSkills().get("zanpakuto").setLevel(2);
        for(String form:List.of("sealed","shikai","bankai")){d.getCharacter().unlockForm(form);d.getCharacter().setMastery("zanpakuto",form,100);}
        d.getCharacter().preserveLegacyTechniques();String form=index==0||index>=13&&index<=16?"sealed":index==2||index>=8&&index<=11?"bankai":"shikai";
        d.getCharacter().setActiveForm("zanpakuto",form);d.getCharacter().setSelectedForm("zanpakuto",form);
        d.getStatus().clearCooldowns();d.getGuard().interrupt();d.getResources().setCurrentReiatsu(d.getResources().getMaxReiatsu());
        p.setItemSlot(EquipmentSlot.MAINHAND,new ItemStack(ModItems.BENIHIME.get()));p.setItemSlot(EquipmentSlot.OFFHAND,ItemStack.EMPTY);
        p.setHealth(index==9?Math.max(1,p.getMaxHealth()-8):p.getMaxHealth());
        Husk h=EntityType.HUSK.create(p.serverLevel());if(h==null)return;
        h.setPos(p.position().add(0,0,index==8||index==11?12:index==14||index==15?6:2));h.setNoAi(true);h.setNoGravity(true);h.setPersistenceRequired();
        h.getAttribute(Attributes.MAX_HEALTH).setBaseValue(1000);h.setHealth(1000);h.setYRot(180);p.serverLevel().addFreshEntity(h);
        SyncHelper.full(p);SyncHelper.appearance(p);
        trial=new Trial(p.getUUID(),h.getUUID(),p.level().getGameTime(),index,p.position());
        LogUtils.getLogger().info("Benihime combat QA 81 prepared {} target={} health={}",CASES[index],h.getId(),h.getHealth());
    }
    @SubscribeEvent public static void serverTick(TickEvent.ServerTickEvent e){
        var t=trial;if(e.phase!=TickEvent.Phase.END||t==null||!Boolean.getBoolean("bleachmod.benihimeCombatQa"))return;
        var server=ServerLifecycleHooks.getCurrentServer();if(server==null)return;var p=server.getPlayerList().getPlayer(t.owner());if(p==null)return;
        long age=p.level().getGameTime()-t.start();var target=p.serverLevel().getEntity(t.target());
        if(t.index()<4&&(age==30||age==44||age==58)&&target!=null){
            // Keep the test target in normal melee reach after knockback.
            target.setPos(p.position().add(0,0,2));target.setDeltaMovement(Vec3.ZERO);
            // Damage and animation come from the real client BC attack request.
            LogUtils.getLogger().info("Benihime combat QA 81 swing {} age={}",CASES[t.index()],age);
        }
        if(t.index()==17){
            if(age==20)PlayerCapability.get(p).ifPresent(d->ReleaseAnimationService.begin(p,d,"zanpakuto","bankai",0));
            if(age==240||age==590)LogUtils.getLogger().info("Benihime QA 81 entrance-walk age={} active={} moved={}",age,!BenihimeEntranceService.snapshot(p).isEmpty(),p.position().distanceTo(t.origin()));
            if(age==640&&target!=null)target.discard();return;
        }
        if(t.index()>=4){
            int slot=t.index()>=13?t.index()-12:t.index()==12?4:t.index()<8?t.index()-3:t.index()-7;
            if(t.index()==7&&age==10)PlayerCapability.get(p).ifPresent(d->TechniqueService.executeSlot(p,d,3));
            if(age==40)PlayerCapability.get(p).ifPresent(d->{TechniqueService.executeSlot(p,d,slot);LogUtils.getLogger().info("Benihime combat QA 81 cast {} lease={}",CASES[t.index()],SignatureAnimationService.snapshot(p));});
        }
        if(age==125){LogUtils.getLogger().info("Benihime combat QA 81 result {} targetPresent={} targetHealth={}",CASES[t.index()],target!=null,target instanceof Husk h?h.getHealth():-1);if(target!=null)target.discard();}
    }
    @SubscribeEvent(priority=EventPriority.LOWEST) public static void camera(ViewportEvent.ComputeCameraAngles e){
        var t=trial;if(t==null||t.index()==3||t.index()==17||!enabled())return;var mc=Minecraft.getInstance();
        Vec3 focus=mc.player.getPosition((float)e.getPartialTick()).add(0,t.index()>=8&&t.index()<=11?4:1.1,t.index()>=8&&t.index()<=11?-2:.7),eye=focus.add(t.index()>=8&&t.index()<=11?19:5,t.index()>=8&&t.index()<=11?8:2,t.index()>=8&&t.index()<=11?22:3);
        try{if(cameraPosition==null)cameraPosition=net.minecraftforge.fml.util.ObfuscationReflectionHelper.findMethod(Camera.class,"m_90581_",Vec3.class);cameraPosition.invoke(e.getCamera(),eye);}
        catch(ReflectiveOperationException|RuntimeException failure){LogUtils.getLogger().warn("Benihime combat QA camera unavailable",failure);trial=null;return;}
        e.setYaw(HigashiCinematicPath.yaw(eye,focus));e.setPitch(HigashiCinematicPath.pitch(eye,focus));e.setRoll(0);
    }
    @SubscribeEvent public static void capture(TickEvent.RenderTickEvent e){
        var t=trial;if(e.phase!=TickEvent.Phase.END||t==null||!enabled()||evidence==null)return;var mc=Minecraft.getInstance();
        int age=(int)(mc.level.getGameTime()-t.start());if(age<28||age>(t.index()==17?620:115)||t.index()==17&&age%4!=0||lastFrame==age||mc.screen!=null)return;lastFrame=age;
        String file=String.format(Locale.ROOT,"frame-t%03d.png",age);
        try{Files.createDirectories(evidence);Screenshot.grab(evidence.toFile(),file,mc.getMainRenderTarget(),m->{});
            Files.writeString(evidence.resolve("frames.jsonl"),String.format(Locale.ROOT,"{\"file\":\"screenshots/%s\",\"tick\":%d,\"case\":\"%s\",\"fpsObserved\":%d,\"gestureActive\":%s,\"source\":\"Minecraft framebuffer\"}%n",file,age,CASES[t.index()],mc.getFps(),com.bleachmod.client.model.SignatureActionVisuals.active(mc.player)),StandardOpenOption.CREATE,StandardOpenOption.APPEND);
        }catch(java.io.IOException failure){LogUtils.getLogger().warn("Benihime combat QA capture failed",failure);}
    }
}
