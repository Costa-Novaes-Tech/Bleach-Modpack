package com.bleachmod.client.particle;

import com.bleachmod.Reference;
import com.bleachmod.common.network.s2c.BenihimeFrameS2C;
import com.bleachmod.common.visual.BenihimeGeometry;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import java.util.*;
import static com.bleachmod.common.network.s2c.BenihimeFrameS2C.*;

@Mod.EventBusSubscriber(modid=Reference.MOD_ID,value=Dist.CLIENT)
public final class BenihimeRenderer {
    private static final class Types extends RenderType {
        private Types(){super("benihime_unused",DefaultVertexFormat.POSITION_COLOR,VertexFormat.Mode.QUADS,8192,false,true,()->{},()->{});}
        static final RenderType FIGURE=create("benihime_figure",DefaultVertexFormat.POSITION_COLOR,VertexFormat.Mode.QUADS,8192,false,false,
                CompositeState.builder().setShaderState(RENDERTYPE_LIGHTNING_SHADER).setCullState(NO_CULL).setWriteMaskState(COLOR_DEPTH_WRITE).setOutputState(PARTICLES_TARGET).createCompositeState(false));
        static final RenderType THREAD=create("benihime_threads",DefaultVertexFormat.POSITION_COLOR,VertexFormat.Mode.QUADS,4096,false,true,
                CompositeState.builder().setShaderState(RENDERTYPE_LIGHTNING_SHADER).setTransparencyState(TRANSLUCENT_TRANSPARENCY).setCullState(NO_CULL).setWriteMaskState(COLOR_WRITE).setOutputState(PARTICLES_TARGET).createCompositeState(false));
    }
    private record Key(int owner,int kind){ }
    private record Frame(BenihimeFrameS2C packet,long until){ }
    private static final class Pose {com.bleachmod.common.visual.BenihimeFollowMotion.Pose follow;double time,idleStart;Pose(net.minecraft.world.phys.Vec3 p,float yaw,double t){follow=new com.bleachmod.common.visual.BenihimeFollowMotion.Pose(p,yaw);time=t;idleStart=t;}}
    private static final Map<Integer,Pose> POSES=new HashMap<>();
    private static final Map<Key,Frame> FRAMES=new LinkedHashMap<>();
    private BenihimeRenderer(){ }
    public static void beginFollowing(int owner,net.minecraft.world.phys.Vec3 anchor,net.minecraft.world.phys.Vec3 forward){
        var level=Minecraft.getInstance().level;if(level==null)return;
        float yaw=(float)Math.toDegrees(Math.atan2(-forward.x,forward.z));
        POSES.put(owner,new Pose(anchor,yaw,level.getGameTime()));
    }
    public static void accept(BenihimeFrameS2C m){var level=Minecraft.getInstance().level;if(level==null || !m.valid())return;
        if(m.kind()==CLEAR){FRAMES.keySet().removeIf(k->k.owner==m.owner());POSES.remove(m.owner());return;}
        var key=new Key(m.owner(),m.kind());
        if(!FRAMES.containsKey(key) && FRAMES.size()>=32)FRAMES.remove(FRAMES.keySet().iterator().next());
        if(m.kind()==QUEEN && !FRAMES.containsKey(key) && FRAMES.keySet().stream().filter(k->k.kind==QUEEN).count()>=8)return;
        FRAMES.put(key,new Frame(m,level.getGameTime()+8));
    }
    @SubscribeEvent public static void render(RenderLevelStageEvent event){
        if(event.getStage()!=RenderLevelStageEvent.Stage.AFTER_PARTICLES)return;var mc=Minecraft.getInstance();if(mc.level==null || mc.player==null){FRAMES.clear();return;}
        FRAMES.values().removeIf(f->f.until<mc.level.getGameTime());POSES.keySet().removeIf(id->!FRAMES.containsKey(new Key(id,QUEEN)));if(FRAMES.isEmpty())return;
        var camera=event.getCamera().getPosition();var matrix=event.getPoseStack().last().pose();var buffers=mc.renderBuffers().bufferSource();
        for(int pass=0;pass<2;pass++){
            var type=pass==0?Types.FIGURE:Types.THREAD;var vertices=buffers.getBuffer(type);boolean emitted=false;
            for(var frame:FRAMES.values()){
                var m=frame.packet;if((m.kind()==QUEEN)!=(pass==0) || mc.player.distanceToSqr(m.origin())>80*80)continue;
                if(m.kind()==QUEEN && com.bleachmod.client.cinematic.BenihimeEntranceClient.active(m.owner())){POSES.remove(m.owner());continue;}
                var origin=m.origin();var end=m.end();var forward=m.forward();var entity=mc.level.getEntity(m.owner());
                if(entity!=null && (m.kind()==QUEEN || m.kind()==SHIELD || m.kind()==BUFF)){origin=entity.getPosition(event.getPartialTick());if(m.kind()!=QUEEN)end=origin;forward=com.bleachmod.common.visual.SenbonGeometry.forward(entity.getYRot());}
                float progress=m.progress();
                if(m.kind()==QUEEN){double now=mc.level.getGameTime()+event.getPartialTick();var pose=POSES.get(m.owner());
                    float yaw=entity!=null?entity.getYRot():(float)Math.toDegrees(Math.atan2(-forward.x,forward.z));
                    if(pose==null){pose=new Pose(origin,yaw,now);POSES.put(m.owner(),pose);}
                    pose.follow=com.bleachmod.common.visual.BenihimeFollowMotion.advance(pose.follow,origin,yaw,now-pose.time);pose.time=now;
                    origin=pose.follow.anchor();forward=pose.follow.forward();progress=m.variant()==0?0:(float)Math.min(1,m.progress()+(8-(frame.until-now))/(m.variant()==1?24:m.variant()==2?30:m.variant()==3?6:60));
                }
                var local=new BenihimeFrameS2C(m.owner(),m.kind(),origin.subtract(camera),end.subtract(camera),forward,m.radius(),progress,m.variant());
                float growth=m.kind()==QUEEN && entity instanceof net.minecraft.client.player.AbstractClientPlayer player?com.bleachmod.client.model.ZanpakutoReleaseAnimation.manifestationGrowth(player,event.getPartialTick()):1;
                var geometry=m.kind()==QUEEN?com.bleachmod.common.visual.BenihimeEntranceGeometry.buildCombat(local.origin(),local.forward(),mc.level.getGameTime()+event.getPartialTick()-POSES.get(m.owner()).idleStart,m.variant(),progress,com.bleachmod.client.cinematic.BenihimeTerrain.sample(origin,forward),local.end()):BenihimeGeometry.build(local);
                for(var q:geometry)for(var v:List.of(q.a(),q.b(),q.c(),q.d())){
                    int c=q.color();double y=local.origin().y+(v.y-local.origin().y)*growth;vertices.vertex(matrix,(float)v.x,(float)y,(float)v.z).color(c>>>24,(c>>>16)&255,(c>>>8)&255,(int)((c&255)*growth)).endVertex();emitted=true;
                }
            }
            if(emitted)buffers.endBatch(type);
        }
    }
    @SubscribeEvent public static void logout(ClientPlayerNetworkEvent.LoggingOut event){FRAMES.clear();POSES.clear();}
    @SubscribeEvent public static void unload(LevelEvent.Unload event){if(event.getLevel().isClientSide()){FRAMES.clear();POSES.clear();}}
}
