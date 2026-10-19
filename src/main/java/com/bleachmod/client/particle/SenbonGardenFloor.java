package com.bleachmod.client.particle;

import com.bleachmod.Reference;
import com.bleachmod.common.network.s2c.SenbonPetalsS2C;
import com.bleachmod.common.visual.SenbonGardenGeometry;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import java.util.*;
import static com.bleachmod.common.network.s2c.SenbonPetalsS2C.*;

/** Terrain-anchored visual floor, independent of airborne petals and the floral outline. */
@Mod.EventBusSubscriber(modid=Reference.MOD_ID,value=Dist.CLIENT)
public final class SenbonGardenFloor {
    private static final class Field {
        final Vec3 center;final double radius;final int count;final List<Vec3> anchors=new ArrayList<>(),outline=new ArrayList<>();int built;long until,last,risingUntil;float bloom,opening;boolean finished;
        Field(Vec3 c,double r,int n){center=c;radius=r;count=n;}
    }
    private static final Map<Integer,Field> FIELDS=new LinkedHashMap<>();
    private static net.minecraft.client.multiplayer.ClientLevel world;
    private static final RenderType PETAL=RenderType.entityCutoutNoCull(ResourceLocation.fromNamespaceAndPath("minecraft","textures/particle/cherry_0.png"));
    private SenbonGardenFloor(){ }
    public static void accept(SenbonPetalsS2C m){var mc=Minecraft.getInstance();if(mc.level==null)return;
        if(!(m.shape()==FIELD||m.shape()==FLOWER||m.shape()==RADIAL&&m.pattern()==2||m.shape()==RISE&&m.pattern()==2))return;
        if(world!=mc.level){FIELDS.clear();world=mc.level;}
        var f=FIELDS.get(m.ownerId());if(f==null||f.center.distanceToSqr(m.origin())>.25||f.finished&&m.shape()==RADIAL){
            if(FIELDS.size()>=8)FIELDS.remove(FIELDS.keySet().iterator().next());int count=mc.options.particles().get()==net.minecraft.client.ParticleStatus.MINIMAL?240:mc.options.particles().get()==net.minecraft.client.ParticleStatus.DECREASED?640:1200;f=new Field(m.origin(),m.radius(),count);FIELDS.put(m.ownerId(),f);
        }
        long now=mc.level.getGameTime();f.last=now;f.until=now+10;
        if(m.shape()==FLOWER)f.bloom=m.progress();
        if(m.shape()==FIELD)f.opening=1;
        if(m.shape()==RADIAL)f.opening=Math.max(f.opening,m.progress());
        if(m.shape()==RISE){f.finished=true;f.until=now+40;f.risingUntil=now+12;f.opening=1;f.bloom=1;}
    }
    @SubscribeEvent public static void tick(TickEvent.ClientTickEvent e){if(e.phase!=TickEvent.Phase.END)return;var mc=Minecraft.getInstance();if(mc.level==null||world!=mc.level){FIELDS.clear();world=mc.level;return;}if(mc.isPaused())return;
        FIELDS.values().removeIf(f->f.until<mc.level.getGameTime());int rays=128;
        for(var f:FIELDS.values())while(f.built<f.count&&rays-->0){int index=f.built++;Vec3 p=f.center.add(SenbonGardenGeometry.point(f.radius,index,f.count,0));
            Vec3 anchor=null;if(mc.level.hasChunkAt(BlockPos.containing(p))){var hit=mc.level.clip(new ClipContext(p.add(0,4,0),p.add(0,-5,0),ClipContext.Block.COLLIDER,ClipContext.Fluid.NONE,mc.player));if(hit.getType()!=HitResult.Type.MISS)anchor=hit.getLocation().add(0,.025,0);}
            Vec3 end=anchor;
            if(anchor!=null&&index%3==0&&rays>0){rays--;var target=f.center.add(SenbonGardenGeometry.point(f.radius,index,f.count,1));
                if(mc.level.hasChunkAt(BlockPos.containing(target))){var hit=mc.level.clip(new ClipContext(target.add(0,4,0),target.add(0,-5,0),ClipContext.Block.COLLIDER,ClipContext.Fluid.NONE,mc.player));if(hit.getType()!=HitResult.Type.MISS)end=hit.getLocation().add(0,.025,0);}}
            f.anchors.add(anchor);f.outline.add(end);
        }
        int emission=192,scale=mc.options.particles().get()==net.minecraft.client.ParticleStatus.MINIMAL?4:1;
        for(var f:FIELDS.values())if(f.finished&&mc.level.getGameTime()<f.risingUntil&&!f.anchors.isEmpty())for(int i=0;i<24/scale&&emission-->0;i++){
            int index=Math.floorMod((int)(mc.level.getGameTime()*29+i*53),f.anchors.size());var at=f.anchors.get(index);if(at!=null)mc.level.addParticle(com.bleachmod.init.ModParticles.SENBON_PETAL.get(),at.x,at.y+.1,at.z,Math.sin(index)*.025,.20+(index%5)*.025,Math.cos(index)*.025);
        }
    }
    @SubscribeEvent public static void render(RenderLevelStageEvent e){if(e.getStage()!=RenderLevelStageEvent.Stage.AFTER_PARTICLES)return;var mc=Minecraft.getInstance();if(mc.level==null||world!=mc.level||mc.player==null||FIELDS.isEmpty())return;
        var buffers=mc.renderBuffers().bufferSource();var vertices=buffers.getBuffer(PETAL);var matrix=e.getPoseStack().last().pose();var camera=e.getCamera().getPosition();int budget=4096;
        for(var f:FIELDS.values()){if(mc.player.distanceToSqr(f.center)>96*96)continue;double fade=f.finished?Math.max(0,Math.min(1,(f.until-mc.level.getGameTime())/20.0)):1;
            for(int i=0;i<f.anchors.size()&&budget>0;i++){var anchor=f.anchors.get(i);if(anchor==null)continue;var base=SenbonGardenGeometry.point(f.radius,i,f.count,0);if(base.horizontalDistance()>f.radius*f.opening)continue;
                Vec3 target=f.outline.get(i); // Two thirds keep their exact terrain anchor throughout.
                Vec3 p=anchor.lerp(target==null?anchor:target,f.bloom).subtract(camera);double size=(.10+(i%7)*.021)*fade,a=i*2.399963;Vec3 x=new Vec3(Math.cos(a)*size,0,Math.sin(a)*size),z=new Vec3(-Math.sin(a)*size*.65,0,Math.cos(a)*size*.65);
                Vec3[] corners={p.subtract(x).subtract(z),p.add(x).subtract(z),p.add(x).add(z),p.subtract(x).add(z)};float[][] uv={{0,0},{1,0},{1,1},{0,1}};
                for(int j=0;j<4;j++){var q=corners[j];vertices.vertex(matrix,(float)q.x,(float)q.y,(float)q.z).color(255,210+i%30,230,255).uv(uv[j][0],uv[j][1]).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(0xF000F0).normal(0,1,0).endVertex();}budget--;
            }
        }
        buffers.endBatch(PETAL);
    }
    @SubscribeEvent public static void logout(ClientPlayerNetworkEvent.LoggingOut e){FIELDS.clear();world=null;}
}
