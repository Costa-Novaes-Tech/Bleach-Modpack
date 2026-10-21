package com.bleachmod.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.bleachmod.common.visual.HydraMotion;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

/** Textured snow clouds keep the three converging breaths legible from the wide shot. */
public final class HyorinmaruBreathVisual {
    private HyorinmaruBreathVisual() { }
    public static void render(PoseStack pose,Vec3 camera,Vec3 anchor,Vec3 forward,double age){
        if(age<108 || age>156)return;
        var mc=Minecraft.getInstance();if(mc.player==null || mc.player.distanceToSqr(anchor)>96*96)return;
        var buffers=mc.renderBuffers().bufferSource();var out=buffers.getBuffer(RenderType.entityTranslucent(new net.minecraft.resources.ResourceLocation("minecraft","textures/particle/generic_7.png")));
        var matrix=pose.last().pose();var side=new Vec3(forward.z,0,-forward.x);
        double weight=HydraMotion.smooth((age-108)/8)*(1-HydraMotion.smooth((age-144)/12));
        Vec3 merge=anchor.add(0,1.2,0);
        for(int head=0;head<3;head++){
            var local=HydraMotion.point(false,head,age-44,1,32);
            Vec3 mouth=anchor.subtract(forward.scale(8)).add(side.scale(local.x)).add(0,local.y+.4,0).add(forward.scale(local.z));
            ribbon(out,matrix,camera,mouth,merge,side,age+head*2,.9,weight);
        }
        for(int lane=-3;lane<=3;lane++){
            double reach=24*Mth.clamp((age-105)/24,0,1);
            Vec3 end=anchor.add(forward.scale(reach)).add(side.scale(lane*reach*.27)).add(0,1,0);
            var wall=mc.level.clip(new net.minecraft.world.level.ClipContext(merge,end,net.minecraft.world.level.ClipContext.Block.COLLIDER,net.minecraft.world.level.ClipContext.Fluid.NONE,mc.player));
            if(wall.getType()!=net.minecraft.world.phys.HitResult.Type.MISS)end=wall.getLocation();
            ribbon(out,matrix,camera,merge,end,side,age+lane*3,2.8,weight);
        }
        buffers.endBatch(RenderType.entityTranslucent(new net.minecraft.resources.ResourceLocation("minecraft","textures/particle/generic_7.png")));
    }
    private static void ribbon(VertexConsumer out,org.joml.Matrix4f matrix,Vec3 camera,Vec3 start,Vec3 end,Vec3 side,double age,double width,double opacity){
        var rotation=Minecraft.getInstance().gameRenderer.getMainCamera().rotation();
        var r=new org.joml.Vector3f(1,0,0).rotate(rotation);var upVector=new org.joml.Vector3f(0,1,0).rotate(rotation);
        Vec3 right=new Vec3(r.x,r.y,r.z),up=new Vec3(upVector.x,upVector.y,upVector.z);
        for(int i=0;i<24;i++){
            double u=((i/24.0+age*.025)%1+1)%1;
            Vec3 center=center(start,end,side,u,age,i%3,width).subtract(camera);
            double size=width*(.7+u*.7),fade=Math.sin(u*Math.PI)*opacity;
            Vec3[] corners={center.subtract(right.scale(size)).subtract(up.scale(size)),center.add(right.scale(size)).subtract(up.scale(size)),center.add(right.scale(size)).add(up.scale(size)),center.subtract(right.scale(size)).add(up.scale(size))};
            float[][] uv={{0,1},{1,1},{1,0},{0,0}};
            for(int corner=0;corner<4;corner++){
                var q=corners[corner];out.vertex(matrix,(float)q.x,(float)q.y,(float)q.z).color(235,248,255,(int)(130*fade))
                    .uv(uv[corner][0],uv[corner][1]).overlayCoords(net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY)
                    .uv2(net.minecraft.client.renderer.LightTexture.FULL_BRIGHT).normal(0,1,0).endVertex();
            }
        }
    }
    private static Vec3 center(Vec3 start,Vec3 end,Vec3 side,double u,double age,int strip,double width){
        double wave=u*Math.PI*5-age*.3+strip*2.1;
        return start.lerp(end,u).add(side.scale(Math.sin(wave)*width*.3)).add(0,Math.cos(wave)*width*.2,0);
    }
}
