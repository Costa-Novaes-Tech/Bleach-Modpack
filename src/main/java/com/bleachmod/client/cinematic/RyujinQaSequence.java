package com.bleachmod.client.cinematic;

import com.bleachmod.Reference;
import com.bleachmod.common.data.PlayerCapability;
import com.bleachmod.common.network.SyncHelper;
import com.bleachmod.common.technique.*;
import com.bleachmod.common.visual.HigashiCinematicPath;
import com.bleachmod.init.ModItems;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Camera;
import net.minecraft.client.CameraType;
import net.minecraft.client.Screenshot;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.Husk;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.*;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.ServerLifecycleHooks;
import java.nio.file.*;
import java.util.*;

/** Disposable development world only. Side camera is laboratory instrumentation, not gameplay. */
@Mod.EventBusSubscriber(modid=Reference.MOD_ID,value=Dist.CLIENT)
public final class RyujinQaSequence {
    private static final String[] CASES={"barrage_open","barrage_wall","dash_open","dash_wall","dash_up"};
    private record Trial(UUID owner,Vec3 origin,long start,boolean dash,boolean wall,List<UUID> targets) { }
    private static volatile Trial trial;
    private static int stage,wait,lastFrame=-1;
    private static Path evidence;
    private static CameraType previousCamera;
    private static boolean previousHud;
    private static java.lang.reflect.Method cameraPosition;
    private RyujinQaSequence() { }
    private static boolean enabled(){
        var mc=Minecraft.getInstance();return !net.minecraftforge.fml.loading.FMLEnvironment.production
                &&Boolean.getBoolean("bleachmod.ryujinQa")&&Boolean.getBoolean("bleachmod.benihimeQa")
                &&mc.hasSingleplayerServer()&&mc.player!=null&&mc.player.hasPermissions(2)
                &&mc.gameDirectory.getAbsolutePath().replace('\\','/').contains("/run-benihime-qa/")
                &&mc.getSingleplayerServer().getWorldData().getLevelName().startsWith("Benihime-QA-68-");
    }
    @SubscribeEvent public static void tick(TickEvent.ClientTickEvent e){
        if(e.phase!=TickEvent.Phase.END||!enabled())return;var mc=Minecraft.getInstance();
        if(mc.screen!=null||mc.isPaused()||!BenihimeQaBootstrap.automatedFinished())return;
        wait++;
        if(stage==0){previousCamera=mc.options.getCameraType();previousHud=mc.options.hideGui;}
        if(stage<CASES.length&&(stage==0||wait>=100)){
            var name=CASES[stage];var id=mc.player.getUUID();int index=stage++;
            wait=0;lastFrame=-1;mc.options.setCameraType(CameraType.THIRD_PERSON_BACK);mc.options.hideGui=true;
            evidence=mc.gameDirectory.toPath().resolve("ryujin-qa/"+System.currentTimeMillis()+"-"+name);
            mc.getSingleplayerServer().execute(()->prepare(mc.getSingleplayerServer().getPlayerList().getPlayer(id),index));
        }else if(stage==CASES.length&&wait>=100){
            stage++;trial=null;mc.options.setCameraType(previousCamera);mc.options.hideGui=previousHud;
            LogUtils.getLogger().info("Ryujin QA 76 sequence complete; normal camera restored; client available for manual testing");
        }
    }
    private static void prepare(ServerPlayer p,int index){
        if(p==null)return;p.getServer().setDifficulty(net.minecraft.world.Difficulty.NORMAL,true);ReleaseAnimationService.cancel(p);BenihimeEntranceService.cancel(p);RyujinTechniqueService.cancel(p);
        var d=PlayerCapability.get(p).orElse(null);if(d==null)return;
        boolean dash=index>=2,wall=index%2==1;int x=40*index;
        int y=p.serverLevel().getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,x,0);
        var origin=new Vec3(x+.5,y,.5);p.teleportTo(p.serverLevel(),origin.x,origin.y,origin.z,0,index==4?-90:0);
        d.getCharacter().bindZanpakuto("ryujin_jakka");d.getSkills().get("zanpakuto").setLevel(2);
        for(String form:List.of("sealed","shikai","bankai")){d.getCharacter().unlockForm(form);d.getCharacter().setMastery("zanpakuto",form,100);}
        d.getCharacter().preserveLegacyTechniques();String form=dash?"bankai":"shikai";
        d.getCharacter().setActiveForm("zanpakuto",form);d.getCharacter().setSelectedForm("zanpakuto",form);
        d.getStatus().clearCooldowns();d.getGuard().interrupt();d.getResources().setCurrentReiatsu(d.getResources().getMaxReiatsu());
        p.setItemSlot(EquipmentSlot.MAINHAND,new ItemStack(ModItems.RYUJIN_JAKKA.get()));p.setItemSlot(EquipmentSlot.OFFHAND,ItemStack.EMPTY);
        var targets=new ArrayList<UUID>();
        for(int z:new int[]{4,8,12}){Husk h=EntityType.HUSK.create(p.serverLevel());if(h==null)continue;
            h.setPos(origin.add(0,0,z));h.setNoAi(true);h.setNoGravity(true);h.setPersistenceRequired();
            h.getAttribute(Attributes.MAX_HEALTH).setBaseValue(1000);h.setHealth(1000);h.setYRot(180);
            p.serverLevel().addFreshEntity(h);targets.add(h.getUUID());}
        if(wall)for(int dx=-2;dx<=2;dx++)for(int dy=0;dy<4;dy++)p.serverLevel().setBlock(new BlockPos(x+dx,y+dy,6),Blocks.STONE.defaultBlockState(),3);
        SyncHelper.full(p);SyncHelper.appearance(p);p.serverLevel().setDayTime(6000);
        trial=new Trial(p.getUUID(),origin,p.level().getGameTime(),dash,wall,List.copyOf(targets));
        LogUtils.getLogger().info("Ryujin QA 76 prepared {} origin={} targets={}",CASES[index],origin,targets.size());
    }
    @SubscribeEvent public static void serverTick(TickEvent.ServerTickEvent e){
        var t=trial;if(e.phase!=TickEvent.Phase.END||t==null||!Boolean.getBoolean("bleachmod.ryujinQa"))return;
        var server=ServerLifecycleHooks.getCurrentServer();if(server==null)return;var p=server.getPlayerList().getPlayer(t.owner());if(p==null)return;
        long age=p.level().getGameTime()-t.start();
        if(age==20){PlayerCapability.get(p).ifPresent(d->TechniqueService.executeSlot(p,d,1));LogUtils.getLogger().info("Ryujin QA 76 cast dash={} wall={}",t.dash(),t.wall());}
        if(age==27&&t.dash()&&!t.wall())p.setYRot(-20);
        if(age==75){
            var health=new ArrayList<Float>();for(var id:t.targets()){var h=p.serverLevel().getEntity(id);health.add(h instanceof Husk m?m.getHealth():-1F);if(h!=null)h.discard();}
            double moved=p.position().distanceTo(t.origin());
            LogUtils.getLogger().info("Ryujin QA 76 result dash={} wall={} displacement={} health={} flameDashTicks={}",t.dash(),t.wall(),moved,health,PlayerCapability.get(p).map(d->d.getStatus().getFlameDashTicks()).orElse(-1));
        }
    }
    @SubscribeEvent(priority=EventPriority.LOWEST) public static void camera(ViewportEvent.ComputeCameraAngles e){
        var t=trial;if(t==null||!enabled())return;var mc=Minecraft.getInstance();
        Vec3 focus=(t.dash()?mc.player.getPosition((float)e.getPartialTick()):t.origin().add(0,0,3)).add(0,1.1,0),eye=focus.add(9,3,-5);
        try{if(cameraPosition==null)cameraPosition=net.minecraftforge.fml.util.ObfuscationReflectionHelper.findMethod(Camera.class,"m_90581_",Vec3.class);cameraPosition.invoke(e.getCamera(),eye);}
        catch(ReflectiveOperationException|RuntimeException failure){LogUtils.getLogger().warn("Ryujin QA camera unavailable",failure);trial=null;return;}
        e.setYaw(HigashiCinematicPath.yaw(eye,focus));e.setPitch(HigashiCinematicPath.pitch(eye,focus));e.setRoll(0);
    }
    @SubscribeEvent public static void capture(TickEvent.RenderTickEvent e){
        var t=trial;if(e.phase!=TickEvent.Phase.END||t==null||!enabled()||evidence==null)return;var mc=Minecraft.getInstance();
        int age=(int)(mc.level.getGameTime()-t.start());if(age<17||age>52||lastFrame==age||mc.screen!=null)return;lastFrame=age;
        String file=String.format(Locale.ROOT,"frame-t%03d.png",age);
        try{Files.createDirectories(evidence);Screenshot.grab(evidence.toFile(),file,mc.getMainRenderTarget(),m->{});
            Files.writeString(evidence.resolve("frames.jsonl"),String.format(Locale.ROOT,"{\"file\":\"screenshots/%s\",\"tick\":%d,\"position\":[%.4f,%.4f,%.4f],\"fpsObserved\":%d,\"source\":\"Minecraft framebuffer\"}%n",file,age,mc.player.getX(),mc.player.getY(),mc.player.getZ(),mc.getFps()),StandardOpenOption.CREATE,StandardOpenOption.APPEND);
        }catch(java.io.IOException failure){LogUtils.getLogger().warn("Ryujin QA capture failed",failure);}
    }
}
