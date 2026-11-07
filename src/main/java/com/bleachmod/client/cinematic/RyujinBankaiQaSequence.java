package com.bleachmod.client.cinematic;

import com.bleachmod.Reference;
import com.bleachmod.common.data.PlayerCapability;
import com.bleachmod.common.network.SyncHelper;
import com.bleachmod.common.technique.ReleaseAnimationService;
import com.bleachmod.common.technique.RyujinBankaiEntranceService;
import com.bleachmod.common.visual.RyujinQaSandbox;
import com.bleachmod.init.ModItems;
import com.bleachmod.server.events.FormModeHandler;
import com.mojang.logging.LogUtils;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.Musics;
import net.minecraft.sounds.SoundSource;
import com.bleachmod.mixin.client.MusicManagerAccess;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.ServerLifecycleHooks;
import java.nio.file.*;
import java.util.*;

/** Presentation QA only: explicit property, integrated admin and a fresh disposable QA world. */
@Mod.EventBusSubscriber(modid=Reference.MOD_ID,value=Dist.CLIENT)
public final class RyujinBankaiQaSequence {
    private static final String[] CASES={"day_back","night_front","first_return","left_front","near_wall","move_after_commit","cancel_before_commit","cancel_after_commit","army_and_supreme","supreme_front","supreme_profile","supreme_left_profile","irregular_day","eye_right_profile","death_after_commit"};
    private static final Set<Integer> CUE_FRAMES=Set.of(24,32,39,40,67,70,84,112,144,160,175,176,184,188,190,194,198,200,202,208,220,244,254,260,265,270,300);
    private record Trial(UUID owner,long start,int index) { }
    private static volatile Trial trial;
    private static int stage,wait,lastFrame=-1,repetition;
    private static boolean reloadTriggered;
    private static boolean musicWasActive, musicRetry, musicBlocked;
    private static float musicVolume;
    private static Path root,folder;
    private static boolean kit(int index){return index>=8&&index<=11;}
    private static int endTick(int index){return kit(index)?220:com.bleachmod.common.visual.RyujinBankaiTimeline.DURATION+20;}
    private RyujinBankaiQaSequence() { }
    private static boolean enabled() {
        var mc=Minecraft.getInstance();
        return (!net.minecraftforge.fml.loading.FMLEnvironment.production||RyujinQaSandbox.packaged(mc.gameDirectory.toPath()))
                && Boolean.getBoolean("bleachmod.ryujinBankaiQa")
                && Boolean.getBoolean("bleachmod.benihimeQa") && mc.player!=null && mc.level!=null
                && mc.hasSingleplayerServer() && mc.player.hasPermissions(2)
                && mc.gameDirectory.getAbsolutePath().replace('\\','/').contains("/run-benihime-qa/")
                && mc.getSingleplayerServer().getWorldData().getLevelName().startsWith("Benihime-QA-68-");
    }
    @SubscribeEvent public static void tick(TickEvent.ClientTickEvent e) {
        if(e.phase!=TickEvent.Phase.END || !enabled() || !BenihimeQaBootstrap.automatedFinished())return;
        var mc=Minecraft.getInstance();
        if(mc.screen!=null || mc.isPaused())return;
        if(trial==null) {
            if(stage==0){int selected=Integer.getInteger("bleachmod.ryujinBankaiQaCase",-1);
                if(selected>=0 && selected<CASES.length)stage=selected;
                else if(Boolean.getBoolean("bleachmod.ryujinBankaiQaKit"))stage=8;}
            if(stage>=CASES.length)return;
            if(++wait<(com.bleachmod.common.visual.RyujinSealFlow.enabled()?120:45))return;wait=0;
            mc.options.enableVsync().set(false);mc.options.framerateLimit().set(120);
            String detail=System.getProperty("bleachmod.ryujinFireDetail", "HIGH");
            com.bleachmod.client.ClientVisualConfig.RYUJIN_FIRE_DETAIL.set(com.bleachmod.common.visual.RyujinFireLayout.Detail.valueOf(detail));
            mc.options.renderDistance().set(6);mc.options.hideGui=false;mc.options.pauseOnLostFocus=false;
            mc.options.setCameraType(stage==1 || stage==3 || stage==4 || stage==8 || stage==9 || stage>=12?CameraType.THIRD_PERSON_FRONT:stage==2?CameraType.FIRST_PERSON:CameraType.THIRD_PERSON_BACK);
            com.bleachmod.client.ClientVisualConfig.RYUJIN_LONG.set(stage==0);
            mc.options.mainHand().set(stage==3 || stage==11?HumanoidArm.LEFT:HumanoidArm.RIGHT);mc.options.broadcastOptions();
            if(root==null)root=mc.gameDirectory.toPath().resolve("ryujin-bankai-qa/cycle118-"+System.currentTimeMillis());
            reloadTriggered=false;
            folder=root.resolve(String.format(Locale.ROOT,"%02d_%s",stage+1,CASES[stage])
                    +(repeats()>1?String.format(Locale.ROOT,"_r%02d",repetition+1):""));lastFrame=-1;
            if(Boolean.getBoolean("bleachmod.ryujinQaMusicProbe")&&!kit(stage)){
                mc.getMusicManager().stopPlaying();
                mc.getMusicManager().startPlaying(Musics.GAME);
                musicWasActive=false;musicRetry=false;musicBlocked=false;
                musicVolume=mc.options.getSoundSourceVolume(SoundSource.MUSIC);
            }
            var uuid=mc.player.getUUID();int index=stage;
            mc.getSingleplayerServer().execute(()->prepare(uuid,index));
        } else if(Boolean.getBoolean("bleachmod.ryujinQaReload")&&!reloadTriggered&&mc.level.getGameTime()-trial.start>=144){
            reloadTriggered=true;
            LogUtils.getLogger().info("Ryujin QA resource reload requested during sustained sun");
            mc.reloadResourcePacks().whenComplete((unused,error)->{
                if(error==null)LogUtils.getLogger().info("Ryujin QA resource reload completed");
                else LogUtils.getLogger().error("Ryujin QA resource reload failed",error);
            });
        } else if(mc.level.getGameTime()-trial.start>endTick(trial.index)+6) {
            if(Boolean.getBoolean("bleachmod.ryujinQaMusicProbe")&&!kit(trial.index)){
                boolean released=!RyujinBankaiEntranceClient.suppressesVanillaMusic();
                mc.getMusicManager().stopPlaying();
                mc.getMusicManager().startPlaying(Musics.GAME);
                LogUtils.getLogger().info("Ryujin QA vanilla music {} started={} blocked={} released={} canResume={} sliderPreserved={}",
                        CASES[trial.index],musicWasActive,musicBlocked,released,vanillaMusicActive(mc),
                        musicVolume==mc.options.getSoundSourceVolume(SoundSource.MUSIC));
            }
            LogUtils.getLogger().info("Ryujin Bankai QA completed {} perspective={}",CASES[trial.index],mc.options.getCameraType());
            RyujinFrameProfiler.write(folder);
            RyujinQaVideoRecorder.finish();
            trial=null;
            if(++repetition>=repeats()){
                repetition=0;stage++;
                if(Integer.getInteger("bleachmod.ryujinBankaiQaCase",-1)>=0)stage=CASES.length;
            }
            if(Boolean.getBoolean("bleachmod.ryujinBankaiQaFocus") && stage==1)stage=8;
            if(stage>=CASES.length && Boolean.getBoolean("bleachmod.qaAutoExit"))mc.stop();
        } else if(Boolean.getBoolean("bleachmod.ryujinQaMusicProbe")&&!kit(trial.index)){
            long age=mc.level.getGameTime()-trial.start;
            if(age<0)musicWasActive|=vanillaMusicActive(mc);
            if(age>=18&&!musicRetry){
                musicRetry=true;
                mc.getMusicManager().startPlaying(Musics.GAME);
                musicBlocked=RyujinBankaiEntranceClient.suppressesVanillaMusic()&&!vanillaMusicActive(mc);
            }
        }
    }
    private static boolean vanillaMusicActive(Minecraft mc) {
        var sound=((MusicManagerAccess) mc.getMusicManager()).bleachmod$currentMusic();
        return sound!=null && "minecraft".equals(sound.getLocation().getNamespace())
                && mc.getSoundManager().isActive(sound);
    }
    private static int repeats(){return Math.max(1,Math.min(5,Integer.getInteger("bleachmod.ryujinQaRepeats",1)));}
    private static void prepare(UUID uuid,int index) {
        var server=ServerLifecycleHooks.getCurrentServer();if(server==null)return;
        var p=server.getPlayerList().getPlayer(uuid);if(p==null)return;
        ReleaseAnimationService.cancel(p);RyujinBankaiEntranceService.cancel(p);
        p.teleportTo(p.serverLevel(),index*48+.5,kit(index)&&index>=9?-28:-60,.5,kit(index)?180:0,0);p.yBodyRot=p.getYRot();p.yRotO=p.getYRot();p.setXRot(0);
        // Isolated animation bench: hard platform above the destructive cut's volume.
        // Terrain destruction itself is covered by GameTests and the flat-ground case8.
        if(kit(index)&&index>=9)for(int x=-2;x<=2;x++)for(int z=-2;z<=2;z++)p.serverLevel().setBlock(p.blockPosition().offset(x,-1,z),Blocks.BEDROCK.defaultBlockState(),3);
        if(Boolean.getBoolean("bleachmod.ryujinLightProbe")){
            for(int x:new int[]{6,10,14})for(int y=0;y<4;y++)
                p.serverLevel().setBlock(p.blockPosition().offset(x,y,-8),Blocks.WHITE_CONCRETE.defaultBlockState(),3);
        }
        p.setMainArm(index==3 || index==11?HumanoidArm.LEFT:HumanoidArm.RIGHT);
        p.serverLevel().setDayTime(index==1?18000:6000);p.setHealth(p.getMaxHealth());
        var d=PlayerCapability.get(p).orElseThrow(()->new IllegalStateException("Missing player capability"));d.initializeShinigami();d.getCharacter().bindZanpakuto("ryujin_jakka");
        var appearance=d.getAppearance();
        appearance.setUseVanillaSkin(index!=13);
        if(index==13){
            appearance.applyDefaults(com.bleachmod.common.appearance.AppearanceCatalog.get(),d.getCharacter().getRace());
            appearance.setSlim(true);
            appearance.setSkinColor(0x956447);
            appearance.setHairColor(0x1E2846);
            appearance.setEyeColor(0x93D7FF);
        }
        d.getSkills().get("zanpakuto").setLevel(2);
        for(String form:List.of("sealed","shikai","bankai")) {d.getCharacter().unlockForm(form);d.getCharacter().setMastery("zanpakuto",form,100);}
        d.getCharacter().preserveLegacyTechniques();d.getCharacter().setActiveForm("zanpakuto","shikai");d.getCharacter().setSelectedForm("zanpakuto","bankai");
        // Presentation matrix must not be truncated by passive Reiatsu drain.
        // Normal costs and cancellation are covered separately by the server GameTests.
        d.getResources().setCostsDisabled(true);d.getStatus().clearCooldowns();d.getGuard().interrupt();d.getResources().setCurrentReiatsu(d.getResources().getMaxReiatsu());
        p.setItemSlot(EquipmentSlot.MAINHAND,new ItemStack(ModItems.RYUJIN_JAKKA.get()));p.setItemSlot(EquipmentSlot.OFFHAND,ItemStack.EMPTY);
        if(kit(index))server.setDifficulty(net.minecraft.world.Difficulty.NORMAL,true);
        if(index==4)for(int x=-3;x<=3;x++)for(int y=0;y<6;y++)p.serverLevel().setBlock(p.blockPosition().offset(x,y,6),Blocks.STONE.defaultBlockState(),3);
        if(index==12)for(int x=2;x<=6;x++)for(int z=-4;z<=1;z++){
            p.serverLevel().setBlock(p.blockPosition().offset(x,0,z),Blocks.STONE.defaultBlockState(),3);
            if(x>3)p.serverLevel().setBlock(p.blockPosition().offset(-x,-1,z),Blocks.AIR.defaultBlockState(),3);
        }
        SyncHelper.full(p);SyncHelper.appearance(p);
        trial=new Trial(uuid,p.level().getGameTime()+20,index);
        LogUtils.getLogger().info("Ryujin Bankai QA prepared {}",CASES[index]);
    }
    @SubscribeEvent public static void server(TickEvent.ServerTickEvent e) {
        var t=trial;if(e.phase!=TickEvent.Phase.END || t==null || !Boolean.getBoolean("bleachmod.ryujinBankaiQa"))return;
        var server=ServerLifecycleHooks.getCurrentServer();if(server==null)return;
        var p=server.getPlayerList().getPlayer(t.owner);if(p==null)return;long age=p.level().getGameTime()-t.start;
        if(age==0)PlayerCapability.get(p).ifPresent(d->FormModeHandler.instantTransform(p,d));
        if(t.index==5 && age==82)p.teleportTo(p.serverLevel(),p.getX()+3,p.getY(),p.getZ()+2,35,0);
        if((t.index==6 && age==36) || (t.index==7 && age==76)) {ReleaseAnimationService.cancel(p);RyujinBankaiEntranceService.cancel(p);}
        if(t.index==14 && age==86){
            p.serverLevel().getGameRules().getRule(net.minecraft.world.level.GameRules.RULE_DO_IMMEDIATE_RESPAWN).set(true,server);
            p.kill();
        }
        if(kit(t.index)){
            if(age==75)RyujinBankaiEntranceService.cancel(p);
            if(age==90){
                var mob=net.minecraft.world.entity.EntityType.HUSK.create(p.serverLevel());
                mob.moveTo(p.getX(),p.getY(),p.getZ()+11,180,0);mob.setNoAi(true);if(t.index>=9)mob.setNoGravity(true);
                mob.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH).setBaseValue(200);mob.setHealth(200);p.serverLevel().addFreshEntity(mob);
                if(t.index==8)PlayerCapability.get(p).ifPresent(d->com.bleachmod.common.technique.TechniqueService.executeSlot(p,d,3));
            }
            if(age==134)com.bleachmod.common.technique.ElementalTechniqueService.cancel(p);
            if(age==140){p.teleportTo(p.serverLevel(),p.getX(),p.getY(),p.getZ(),0,0);p.yBodyRot=0;p.setYHeadRot(0);PlayerCapability.get(p).ifPresent(d->com.bleachmod.common.technique.TechniqueService.executeSlot(p,d,4));}
        }
        if(t.index>=8 && age==120)LogUtils.getLogger().info("Ryujin QA army snapshot {}",com.bleachmod.common.technique.ElementalTechniqueService.snapshot(p));
        if(age==endTick(t.index))LogUtils.getLogger().info("Ryujin Bankai QA result {} releaseBusy={} scene={} form={}",CASES[t.index],ReleaseAnimationService.busy(p),RyujinBankaiEntranceService.snapshot(p),PlayerCapability.get(p).map(d->d.getCharacter().getActiveForm()).orElse("missing"));
    }
    private static java.lang.reflect.Method qaCameraPosition;
    @SubscribeEvent(priority=net.minecraftforge.eventbus.api.EventPriority.LOWEST)
    public static void profile(net.minecraftforge.client.event.ViewportEvent.ComputeCameraAngles e){
        var t=trial;if(t==null || t.index!=10&&t.index!=11&&t.index!=13 || !enabled())return;
        var mc=Minecraft.getInstance();double age=mc.level.getGameTime()-t.start;
        boolean eyeCheck=t.index==13;
        if(eyeCheck?(age<24||age>48):(age<140||age>200))return;
        Vec3 pos=mc.player.getPosition((float)e.getPartialTick());
        Vec3 focus=pos.add(0,eyeCheck?1.62:1,eyeCheck?0:.5);
        Vec3 eye=eyeCheck?pos.add(-2.15,1.75,age<36?1.85:.35):pos.add(t.index==11?4.5:-4.5,1.8,.5);
        try{
            if(qaCameraPosition==null)qaCameraPosition=net.minecraftforge.fml.util.ObfuscationReflectionHelper.findMethod(net.minecraft.client.Camera.class,"m_90581_",Vec3.class);
            qaCameraPosition.invoke(e.getCamera(),eye);
        }catch(ReflectiveOperationException failure){throw new IllegalStateException("QA profile camera failed",failure);}
        Vec3 dir=focus.subtract(eye).normalize();e.setYaw((float)Math.toDegrees(Math.atan2(-dir.x,dir.z)));e.setPitch((float)-Math.toDegrees(Math.asin(dir.y)));e.setRoll(0);
    }
    @SubscribeEvent public static void capture(TickEvent.RenderTickEvent e) {
        var t=trial;if(e.phase!=TickEvent.Phase.END || t==null || folder==null || !enabled())return;
        var mc=Minecraft.getInstance();int age=(int)(mc.level.getGameTime()-t.start);
        int duration=com.bleachmod.common.visual.RyujinBankaiTimeline.DURATION;
        RyujinFrameProfiler.sample(t,age);
        RyujinQaVideoRecorder.capture(t,age,folder);
        if(Boolean.getBoolean("bleachmod.qaNoScreenshots"))return;
        boolean detail=kit(t.index) && age>=140 && age<=198 && age%2==0;
        boolean ending=age>=duration-10 && age<=duration+2 && age%2==0;
        if(kit(t.index))mc.options.hideGui=age>=140 && age<=198;
        if(kit(t.index) && age>=140)mc.options.setCameraType(t.index==9?CameraType.THIRD_PERSON_FRONT:CameraType.THIRD_PERSON_BACK);
        if(age<0 || age>endTick(t.index) || !(age%10==0 || CUE_FRAMES.contains(age) || detail || ending) || age==lastFrame || mc.screen!=null)return;lastFrame=age;
        String file=String.format(Locale.ROOT,"frame-t%03d.png",age);
        try {
            Files.createDirectories(folder);Screenshot.grab(folder.toFile(),file,mc.getMainRenderTarget(),message->{});
            var c=mc.gameRenderer.getMainCamera();var p=c.getPosition();
            Files.writeString(folder.resolve("frames.jsonl"),String.format(Locale.ROOT,
                    "{\"file\":\"screenshots/%s\",\"tick\":%d,\"wallMs\":%d,\"fps\":%d,\"cameraType\":\"%s\",\"active\":%b,\"camPos\":[%.5f,%.5f,%.5f],\"yaw\":%.3f,\"pitch\":%.3f}%n",
                    file,age,System.currentTimeMillis(),mc.getFps(),mc.options.getCameraType(),RyujinBankaiEntranceClient.localEntranceActive(),p.x,p.y,p.z,c.getYRot(),c.getXRot()),StandardOpenOption.CREATE,StandardOpenOption.APPEND);
        }catch(java.io.IOException failure){LogUtils.getLogger().warn("Ryujin Bankai QA capture",failure);}
    }
}
