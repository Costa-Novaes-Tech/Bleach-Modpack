package com.bleachmod.client.cinematic;

import com.bleachmod.Reference;
import com.bleachmod.common.data.PlayerCapability;
import com.bleachmod.common.network.SyncHelper;
import com.bleachmod.common.technique.*;
import com.bleachmod.init.ModItems;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.*;
import static net.minecraft.commands.Commands.literal;

/** Opt-in, physical-client QA only. Never accepts a remote server or a personal save. */
@Mod.EventBusSubscriber(modid=Reference.MOD_ID,value=Dist.CLIENT)
public final class CinematicQaCommands {
    private static final String TARGET="bleachmod_cinematic_qa_71";
    private record Pending(UUID owner,UUID victim,net.minecraft.resources.ResourceKey<net.minecraft.world.level.Level> dimension,
                           long executeAt,long cleanAt,String scenario) { }
    private static final Map<UUID,Pending> PENDING=new HashMap<>();
    private CinematicQaCommands() { }

    private static boolean enabled() {
        return !net.minecraftforge.fml.loading.FMLEnvironment.production&&Boolean.getBoolean("bleachmod.benihimeQa");
    }
    private static boolean safe(ServerPlayer p) {
        return enabled()&&p!=null&&p.hasPermissions(2)
                &&p.getServer().getWorldData().getLevelName().startsWith("Benihime-QA-68-");
    }
    @SubscribeEvent public static void commands(RegisterClientCommandsEvent e) {
        if(!enabled())return;
        e.getDispatcher().register(literal("cinematicqa").requires(s->Minecraft.getInstance().hasSingleplayerServer()
                        &&Minecraft.getInstance().player!=null&&Minecraft.getInstance().player.hasPermissions(2))
                .then(literal("higashi")
                        .then(literal("lethal").executes(c->request("lethal")))
                        .then(literal("threshold").executes(c->request("threshold")))
                        .then(literal("survive").executes(c->request("survive")))
                        .then(literal("miss").executes(c->request("miss"))))
                .then(literal("benihime").executes(c->request("benihime")))
                .then(literal("terrain").executes(c->request("terrain"))));
    }
    private static int request(String scenario) {
        var mc=Minecraft.getInstance();
        if(!enabled()||mc.player==null||!mc.hasSingleplayerServer())return 0;
        var server=mc.getSingleplayerServer();var uuid=mc.player.getUUID();
        if(!server.getWorldData().getLevelName().startsWith("Benihime-QA-68-"))return 0;
        server.execute(()->{
            var p=server.getPlayerList().getPlayer(uuid);if(!safe(p))return;
            if(scenario.equals("terrain"))terrain(p);
            else if(scenario.equals("benihime"))benihime(p);
            else higashi(p,scenario);
        });
        return 1;
    }
    private static void cleanTargets(ServerPlayer p) {
        var previous=PENDING.get(p.getUUID());if(previous!=null)cleanTask(p.getServer(),previous);
        for(var entity:p.serverLevel().getEntitiesOfClass(Mob.class,p.getBoundingBox().inflate(80),
                m->m.getPersistentData().hasUUID(TARGET)&&m.getPersistentData().getUUID(TARGET).equals(p.getUUID())))entity.discard();
        PENDING.remove(p.getUUID());
    }
    private static void cleanTask(net.minecraft.server.MinecraftServer server,Pending task) {
        var level=server.getLevel(task.dimension());if(level==null)return;
        var target=level.getEntity(task.victim());
        if(target!=null&&target.getPersistentData().hasUUID(TARGET)&&target.getPersistentData().getUUID(TARGET).equals(task.owner()))target.discard();
    }
    @SubscribeEvent public static void stopping(ServerStoppingEvent e) {
        for(var task:PENDING.values())cleanTask(e.getServer(),task);PENDING.clear();
    }
    private static void higashi(ServerPlayer p,String scenario) {
        cleanTargets(p);ReleaseAnimationService.cancel(p);BenihimeEntranceService.cancel(p);RyujinTechniqueService.cancel(p);
        var d=PlayerCapability.get(p).orElse(null);if(d==null)return;
        p.getServer().setDifficulty(Difficulty.NORMAL,true);p.setGameMode(GameType.CREATIVE);
        d.getCharacter().bindZanpakuto("ryujin_jakka");d.getSkills().get("zanpakuto").setLevel(2);
        for(String form:List.of("sealed","shikai","bankai")) {
            d.getCharacter().unlockForm(form);d.getCharacter().setMastery("zanpakuto",form,100);
        }
        d.getCharacter().preserveLegacyTechniques();d.getCharacter().setActiveForm("zanpakuto","bankai");
        d.getCharacter().setSelectedForm("zanpakuto","bankai");d.getStatus().setTechniqueSlot2CooldownTicks(0);
        d.getResources().setCurrentReiatsu(d.getResources().getMaxReiatsu());
        p.setItemSlot(EquipmentSlot.MAINHAND,new ItemStack(ModItems.RYUJIN_JAKKA.get()));
        p.setItemSlot(EquipmentSlot.OFFHAND,ItemStack.EMPTY);
        p.teleportTo(p.serverLevel(),p.getX(),p.getY(),p.getZ(),0,0);
        var target=EntityType.HUSK.create(p.serverLevel());if(target==null)return;
        Vec3 at=p.position().add(scenario.equals("miss")?6:0,0,3);
        target.setPos(at);target.setYRot(180);target.setNoAi(true);target.setNoGravity(true);target.setPersistenceRequired();
        target.getAttribute(Attributes.ARMOR).setBaseValue(0);
        target.getAttribute(Attributes.MAX_HEALTH).setBaseValue((scenario.equals("survive")||scenario.equals("miss"))?1000:scenario.equals("threshold")?100:20);
        target.setHealth((scenario.equals("survive")||scenario.equals("miss"))?1000:scenario.equals("threshold")?7:10);
        target.getPersistentData().putUUID(TARGET,p.getUUID());
        if(!p.level().noCollision(target,target.getBoundingBox())||!p.serverLevel().addFreshEntity(target))return;
        SyncHelper.full(p);SyncHelper.appearance(p);
        long now=p.level().getGameTime();
        PENDING.put(p.getUUID(),new Pending(p.getUUID(),target.getUUID(),p.level().dimension(),now+20,now+150,scenario));
        p.sendSystemMessage(Component.literal("QA Higashi "+scenario+": execução em 20 ticks; mantenha os controles livres."));
        LogUtils.getLogger().info("Cinematic QA 71 prepared {} owner={} victim={} health={}/{} at={}",scenario,p.getId(),target.getId(),target.getHealth(),target.getMaxHealth(),at);
    }
    @SubscribeEvent public static void tick(TickEvent.ServerTickEvent e) {
        if(e.phase!=TickEvent.Phase.END||!enabled())return;
        var server=ServerLifecycleHooks.getCurrentServer();if(server==null){PENDING.clear();return;}
        for(var task:List.copyOf(PENDING.values())) {
            var p=server.getPlayerList().getPlayer(task.owner());
            if(!safe(p)||p.level().dimension()!=task.dimension()){cleanTask(server,task);PENDING.remove(task.owner());continue;}
            long now=p.level().getGameTime();
            if(now==task.executeAt()) {
                PlayerCapability.get(p).ifPresent(d->TechniqueService.executeSlot(p,d,2));
                LogUtils.getLogger().info("Cinematic QA 71 cast {} tick={} victim={}",task.scenario(),now,task.victim());
            }
            if(now>=task.cleanAt()) {
                var target=p.serverLevel().getEntity(task.victim());
                boolean alive=target!=null&&target.isAlive(),expected=task.scenario().equals("survive")||task.scenario().equals("miss");
                LogUtils.getLogger().info("Cinematic QA finished {} victimPresent={} victimAlive={} expectedAlive={} passed={}",task.scenario(),target!=null,alive,expected,alive==expected);
                if(target!=null&&target.getPersistentData().hasUUID(TARGET)&&target.getPersistentData().getUUID(TARGET).equals(p.getUUID()))target.discard();
                PENDING.remove(task.owner());
            }
        }
    }
    private static void benihime(ServerPlayer p){
        cleanTargets(p);ReleaseAnimationService.cancel(p);BenihimeEntranceService.cancel(p);RyujinTechniqueService.cancel(p);
        var d=PlayerCapability.get(p).orElse(null);if(d==null)return;
        d.getCharacter().bindZanpakuto("benihime");d.getSkills().get("zanpakuto").setLevel(2);
        for(String form:List.of("sealed","shikai","bankai")){
            d.getCharacter().unlockForm(form);d.getCharacter().setMastery("zanpakuto",form,100);
        }
        d.getCharacter().preserveLegacyTechniques();d.getCharacter().setActiveForm("zanpakuto","shikai");
        d.getCharacter().setSelectedForm("zanpakuto","bankai");d.getGuard().interrupt();d.getStatus().clearCooldowns();
        d.getResources().setCurrentReiatsu(d.getResources().getMaxReiatsu());
        p.setItemSlot(EquipmentSlot.MAINHAND,new ItemStack(ModItems.BENIHIME.get()));
        p.setItemSlot(EquipmentSlot.OFFHAND,ItemStack.EMPTY);
        SyncHelper.full(p);SyncHelper.appearance(p);
        com.bleachmod.server.events.FormModeHandler.instantTransform(p,d);
        p.sendSystemMessage(Component.literal("QA Benihime: invocação real preparada. Para repetir: /cinematicqa benihime"));
    }
    private static void terrain(ServerPlayer p) {
        // Three local pads, with an unchanged reference at the pelvis. QA save only.
        Vec3 f=Vec3.directionFromRotation(0,p.getYRot()),left=com.bleachmod.common.visual.SenbonGeometry.left(f);
        for(int side:new int[]{-1,1})for(int along=5;along<=10;along++)for(int across=2;across<=4;across++) {
            Vec3 at=p.position().add(left.scale(side*across)).subtract(f.scale(along));
            int top=side<0?1:2;
            for(int y=0;y<top;y++)p.serverLevel().setBlock(BlockPos.containing(at.x,p.getY()+y,at.z),Blocks.STONE.defaultBlockState(),3);
        }
        p.sendSystemMessage(Component.literal("QA terreno: apoios +1/+2 blocos atrás do jogador; centro preservado. Use /benihimeqa record."));
        LogUtils.getLogger().info("Cinematic QA 71 terrain pads prepared origin={} yaw={} heights=0,1,2",p.position(),p.getYRot());
    }
}
