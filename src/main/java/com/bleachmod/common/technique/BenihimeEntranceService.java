package com.bleachmod.common.technique;

import com.bleachmod.Reference;
import com.bleachmod.common.data.PlayerCapability;
import com.bleachmod.common.network.SyncHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import java.util.*;

/** Presentation only: the existing 60/40 release contract remains authoritative for gameplay. */
@Mod.EventBusSubscriber(modid=Reference.MOD_ID)
public final class BenihimeEntranceService {
    public static final int DURATION=560;
    private record Scene(long start, Vec3 anchor, float yaw, float stageYaw, net.minecraft.resources.ResourceKey<net.minecraft.world.level.Level> dimension) { }
    private static final Map<UUID,Scene> SCENES=new HashMap<>();
    private BenihimeEntranceService(){ }
    public static void begin(ServerPlayer player){
        Vec3 anchor=player.position();
        float stageYaw=com.bleachmod.common.visual.BenihimeStage.chooseYaw(player.getYRot(),forward->{
            Vec3 left=com.bleachmod.common.visual.SenbonGeometry.left(forward);double blocked=0;
            for(double back:new double[]{6,10,14,19})for(double across:new double[]{-5,0,5})for(double height:new double[]{1,4,8,11}){
                Vec3 point=anchor.subtract(forward.scale(back)).add(left.scale(across)).add(0,height,0);
                var pos=net.minecraft.core.BlockPos.containing(point);
                if(!player.level().hasChunkAt(pos)){blocked+=4;continue;}
                if(!player.level().getBlockState(pos).getCollisionShape(player.level(),pos).isEmpty())blocked+=2;
                Vec3 from=anchor.add(0,Math.min(height,2),0);
                boolean loaded=true;
                for(int step=0;step<=8;step++)if(!player.level().hasChunkAt(net.minecraft.core.BlockPos.containing(from.lerp(point,step/8.)))){loaded=false;break;}
                if(!loaded){blocked+=4;continue;}
                if(player.level().clip(new net.minecraft.world.level.ClipContext(from,point,
                        net.minecraft.world.level.ClipContext.Block.COLLIDER,net.minecraft.world.level.ClipContext.Fluid.NONE,player)).getType()!=net.minecraft.world.phys.HitResult.Type.MISS)blocked++;
            }
            return blocked;
        });
        SCENES.put(player.getUUID(),new Scene(player.level().getGameTime(),anchor,player.getYRot(),stageYaw,player.level().dimension()));
    }
    public static CompoundTag snapshot(ServerPlayer player){
        var tag=new CompoundTag();var scene=SCENES.get(player.getUUID());if(scene==null)return tag;
        long age=player.level().getGameTime()-scene.start;if(age<0||age>=DURATION)return tag;
        tag.putInt("elapsed",(int)age);tag.putInt("duration",DURATION);tag.putDouble("x",scene.anchor.x);tag.putDouble("y",scene.anchor.y);tag.putDouble("z",scene.anchor.z);tag.putFloat("yaw",scene.yaw);tag.putFloat("stageYaw",scene.stageYaw);return tag;
    }
    public static void cancel(ServerPlayer p){if(SCENES.remove(p.getUUID())!=null)SyncHelper.appearance(p);}
    @SubscribeEvent public static void tick(TickEvent.PlayerTickEvent event){
        if(event.phase!=TickEvent.Phase.END||!(event.player instanceof ServerPlayer p))return;
        var s=SCENES.get(p.getUUID());if(s==null)return;
        var d=PlayerCapability.get(p).orElse(null);long age=p.level().getGameTime()-s.start;
        boolean valid=d!=null&&d.getCharacter().getZanpakutoIdentity().equals("benihime")&&TechniqueProgression.equipped(p,d)
                &&(ReleaseAnimationService.busy(p)||(age>=40&&d.getCharacter().getActiveForm().equals("bankai")));
        if(age>=60&&d!=null&&(!SignatureAnimationService.snapshot(p).isEmpty()||d.getGuard().shownActive()||d.getStatus().isDodging()))valid=false;
        if(!valid||!p.isAlive()||p.isSpectator()||s.dimension!=p.level().dimension()||age>=DURATION||p.position().distanceToSqr(s.anchor)>2.25){cancel(p);return;}
        if(age==280||age==308||age==340)sound(p,s,7,0,net.minecraft.sounds.SoundEvents.WOOL_HIT,.65F,.65F);
    }
    private static void sound(ServerPlayer p,Scene s,double back,double height,net.minecraft.sounds.SoundEvent event,float volume,float pitch){
        var point=s.anchor.add(Vec3.directionFromRotation(0,s.stageYaw).scale(-back)).add(0,height,0);
        p.level().playSound(null,point.x,point.y,point.z,event,net.minecraft.sounds.SoundSource.PLAYERS,volume,pitch);
    }
    @SubscribeEvent public static void logout(PlayerEvent.PlayerLoggedOutEvent e){SCENES.remove(e.getEntity().getUUID());}
    @SubscribeEvent public static void hurt(net.minecraftforge.event.entity.living.LivingHurtEvent e){
        if(e.getEntity() instanceof ServerPlayer p)cancel(p);
        if(e.getSource().getEntity() instanceof ServerPlayer p)cancel(p);
    }
    @SubscribeEvent public static void stop(ServerStoppingEvent e){SCENES.clear();}
}
