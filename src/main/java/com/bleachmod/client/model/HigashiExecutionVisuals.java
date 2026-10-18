package com.bleachmod.client.model;

import com.bleachmod.Reference;
import com.bleachmod.client.ClientVisualConfig;
import com.bleachmod.common.network.s2c.HigashiExecutionS2C;
import net.minecraft.client.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import java.util.*;

/** Confirmed kill replay. Camera is entirely local and never teleports either participant. */
@Mod.EventBusSubscriber(modid=Reference.MOD_ID,value=Dist.CLIENT)
public final class HigashiExecutionVisuals {
    private record Burn(HigashiExecutionS2C packet,long start){ }
    private static final Map<Integer,Burn> BURNS=new LinkedHashMap<>();
    private static net.minecraft.client.multiplayer.ClientLevel world;
    private static ArmorStand camera;
    private static Entity previousCamera;
    private static CameraType previousType;
    private static Burn cinematic;
    private static long emitted=Long.MIN_VALUE;
    private HigashiExecutionVisuals(){ }
    public static void accept(HigashiExecutionS2C m){var mc=Minecraft.getInstance();if(mc.level==null||!m.valid())return;if(world!=mc.level){restore();BURNS.clear();world=mc.level;}
        if(BURNS.size()>=8)BURNS.remove(BURNS.keySet().iterator().next());var burn=new Burn(m,mc.level.getGameTime());BURNS.put(m.target(),burn);
        if(mc.player!=null&&mc.player.getId()==m.owner()&&ClientVisualConfig.EXECUTION_CAMERA.get()&&mc.screen==null&&mc.getCameraEntity()==mc.player){
            restore();previousCamera=mc.getCameraEntity();previousType=mc.options.getCameraType();camera=new ArmorStand(mc.level,m.attacker().x,m.attacker().y,m.attacker().z);camera.setInvisible(true);camera.setNoGravity(true);cinematic=burn;
            mc.options.setCameraType(CameraType.FIRST_PERSON);mc.setCameraEntity(camera);updateCamera(0);
        }
    }
    private static void restore(){var mc=Minecraft.getInstance();if(camera!=null&&mc.getCameraEntity()==camera){mc.setCameraEntity(previousCamera!=null&&previousCamera.level()==mc.level&&previousCamera.isAlive()?previousCamera:mc.player);if(previousType!=null&&mc.options.getCameraType()==CameraType.FIRST_PERSON)mc.options.setCameraType(previousType);}camera=null;previousCamera=null;previousType=null;cinematic=null;}
    private static void updateCamera(float partial){var mc=Minecraft.getInstance();if(camera==null||cinematic==null||mc.level==null)return;
        var m=cinematic.packet;double age=mc.level.getGameTime()-cinematic.start+partial,t=Math.max(0,Math.min(1,age/32));Vec3 attacker=mc.level.getEntity(m.owner()) instanceof LivingEntity actor?actor.getPosition(partial):m.attacker();
        Vec3 direction=m.victim().subtract(attacker).multiply(1,0,1).normalize();if(direction.lengthSqr()<.1)direction=new Vec3(0,0,1);Vec3 side=new Vec3(-direction.z,0,direction.x);
        Vec3 focus=attacker.lerp(m.victim(),.65).add(0,Math.min(1.5,m.height()*.6)+t*.5,0);
        Vec3 eye=focus.add(side.scale(2.7-t*.5)).subtract(direction.scale(2.5-t*.6)).add(0,.7+Math.sin(t*Math.PI)*.6,0);
        var hit=mc.level.clip(new ClipContext(focus,eye,ClipContext.Block.COLLIDER,ClipContext.Fluid.NONE,mc.player));if(hit.getType()!=HitResult.Type.MISS)eye=hit.getLocation().add(focus.subtract(eye).normalize().scale(.2));
        Vec3 look=focus.subtract(eye);float yaw=(float)Math.toDegrees(Math.atan2(-look.x,look.z)),pitch=(float)-Math.toDegrees(Math.atan2(look.y,look.horizontalDistance()));
        camera.setPos(eye.x,eye.y-camera.getEyeHeight(),eye.z);camera.xo=camera.getX();camera.yo=camera.getY();camera.zo=camera.getZ();camera.setYRot(yaw);camera.setXRot(pitch);camera.yRotO=yaw;camera.xRotO=pitch;
    }
    @SubscribeEvent public static void renderTick(TickEvent.RenderTickEvent e){if(e.phase==TickEvent.Phase.START)updateCamera(e.renderTickTime);}
    @SubscribeEvent public static void tick(TickEvent.ClientTickEvent e){if(e.phase!=TickEvent.Phase.END)return;var mc=Minecraft.getInstance();
        if(mc.level==null||world!=mc.level){restore();BURNS.clear();world=mc.level;return;}
        long now=mc.level.getGameTime();BURNS.values().removeIf(b->now-b.start>=32);
        if(cinematic!=null&&(mc.player==null||!mc.player.isAlive()||mc.player.hurtTime>0||mc.screen!=null||mc.options.keyAttack.isDown()||mc.options.keyUse.isDown()||mc.getCameraEntity()!=camera||mc.options.getCameraType()!=CameraType.FIRST_PERSON||!ClientVisualConfig.EXECUTION_CAMERA.get()||now-cinematic.start>=32))restore();
        if(mc.isPaused()||emitted==now)return;emitted=now;int budget=256,scale=mc.options.particles().get()==ParticleStatus.MINIMAL?4:1;
        for(var b:BURNS.values()){var m=b.packet;if(mc.player==null||mc.player.distanceToSqr(m.victim())>80*80)continue;int age=(int)(now-b.start);var random=new Random(m.target()*31L+age);
            for(int i=0;i<48/scale&&budget-->0;i++){double a=random.nextDouble()*Math.PI*2,r=random.nextDouble()*Math.max(.3,m.width())*.6;Vec3 at=m.victim().add(Math.cos(a)*r,random.nextDouble()*Math.min(4,m.height())+age*.025,Math.sin(a)*r);
                var type=age<16?(i%4==0?ParticleTypes.ELECTRIC_SPARK:ParticleTypes.FLAME):(i%3==0?ParticleTypes.SMOKE:ParticleTypes.ASH);mc.level.addParticle(type,at.x,at.y,at.z,Math.cos(a)*.025,.04+random.nextDouble()*.08,Math.sin(a)*.025);
            }
        }
    }
    @SubscribeEvent public static void hideIncinerated(RenderLivingEvent.Pre<?,?> event){var b=BURNS.get(event.getEntity().getId());if(b!=null&&world==event.getEntity().level()&&world.getGameTime()-b.start>=6)event.setCanceled(true);}
    @SubscribeEvent public static void logout(ClientPlayerNetworkEvent.LoggingOut e){restore();BURNS.clear();world=null;}
}
