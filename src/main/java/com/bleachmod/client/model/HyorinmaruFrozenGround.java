package com.bleachmod.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.*;
import java.util.*;

/** Temporary textured support surfaces. Never changes world blocks, collision or saved chunks. */
public final class HyorinmaruFrozenGround {
    private record Tile(AABB box,BlockPos block,boolean blue,double radius,net.minecraft.world.level.block.state.BlockState original) { }
    private static final Map<Vec3,List<Tile>> CACHE=new LinkedHashMap<>();
    private static net.minecraft.client.multiplayer.ClientLevel world;
    private static final RenderType PACKED=RenderType.entityTranslucent(new ResourceLocation("minecraft","textures/block/packed_ice.png"));
    private static final RenderType BLUE=RenderType.entityTranslucent(new ResourceLocation("minecraft","textures/block/blue_ice.png"));
    private HyorinmaruFrozenGround() { }
    public static void clear(){CACHE.clear();world=null;}
    public static void forget(Vec3 anchor){CACHE.remove(anchor);}
    private static List<Tile> sample(Vec3 anchor,Vec3 forward){
        var mc=Minecraft.getInstance();var result=new ArrayList<Tile>();
        Vec3 center=anchor.subtract(forward.scale(4));
        for(int x=-10;x<=10;x++)for(int z=-13;z<=13;z++){
            double radius=Math.sqrt(x*x/100.0+z*z/169.0);if(radius>1)continue;
            Vec3 at=new Vec3(Math.floor(center.x)+x+.5,anchor.y,Math.floor(center.z)+z+.5);
            if(!mc.level.hasChunkAt(BlockPos.containing(at)))continue;
            var hit=mc.level.clip(new ClipContext(at.add(0,4,0),at.add(0,-7,0),ClipContext.Block.COLLIDER,ClipContext.Fluid.NONE,mc.player));
            if(hit.getType()==HitResult.Type.MISS || hit.getDirection()!=net.minecraft.core.Direction.UP)continue;
            var pos=hit.getBlockPos();var state=mc.level.getBlockState(pos);
            if(state.hasBlockEntity() || !state.getFluidState().isEmpty())continue;
            for(var box:state.getCollisionShape(mc.level,pos).toAabbs()){
                if((box.maxX-box.minX)*(box.maxZ-box.minZ)<.2 || Math.abs(pos.getY()+box.maxY-hit.getLocation().y)>.02)continue;
                result.add(new Tile(box.move(pos),pos.immutable(),Math.floorMod(pos.getX()*31+pos.getZ()*17,7)<3,radius,state));
            }
        }
        return result;
    }
    public static void render(PoseStack pose,Vec3 camera,Vec3 anchor,Vec3 forward,double elapsed){
        var mc=Minecraft.getInstance();if(mc.level==null || mc.player==null)return;
        if(world!=mc.level){clear();world=mc.level;}
        double age=com.bleachmod.common.visual.HyorinmaruTimeline.motionAge(elapsed);
        if(age<32 || age>=200 || mc.player.distanceToSqr(anchor)>96*96)return;
        if(!CACHE.containsKey(anchor)){
            if(CACHE.size()>=6)CACHE.remove(CACHE.keySet().iterator().next());
            CACHE.put(anchor,sample(anchor,forward));
        }
        double spread=(age-32)/48,fade=1-com.bleachmod.common.visual.HydraMotion.smooth((age-176)/24);
        var buffers=mc.renderBuffers().bufferSource();var m=pose.last().pose();
        for(var t:CACHE.get(anchor)){
            double alpha=com.bleachmod.common.visual.HydraMotion.smooth((spread-t.radius)*6)*fade;
            if(alpha<=.001 || mc.level.getBlockState(t.block)!=t.original)continue;
            var b=t.box;var out=buffers.getBuffer(t.blue?BLUE:PACKED);
            double y=b.maxY+.006-camera.y,x0=b.minX-camera.x,x1=b.maxX-camera.x,z0=b.minZ-camera.z,z1=b.maxZ-camera.z;
            double[][] corners={{x0,y,z1,0,1},{x1,y,z1,1,1},{x1,y,z0,1,0},{x0,y,z0,0,0}};
            for(var p:corners)out.vertex(m,(float)p[0],(float)p[1],(float)p[2]).color(255,255,255,(int)(255*alpha)).uv((float)p[3],(float)p[4])
                .overlayCoords(net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY).uv2(LevelRenderer.getLightColor(mc.level,t.block.above())).normal(0,1,0).endVertex();
        }
        buffers.endBatch(PACKED);buffers.endBatch(BLUE);
    }
}
