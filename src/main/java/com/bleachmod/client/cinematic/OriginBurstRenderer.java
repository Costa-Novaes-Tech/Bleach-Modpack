package com.bleachmod.client.cinematic;

import com.bleachmod.common.network.s2c.OriginSceneS2C;
import com.bleachmod.common.visual.OriginTimeline;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import org.joml.Matrix4f;
import java.util.*;

/** Short-lived fullbright energy volume, independent of optional shaders and particle settings. */
final class OriginBurstRenderer {
    private static final class Types extends RenderType {
        private Types(){super("origin_unused",DefaultVertexFormat.POSITION_COLOR,VertexFormat.Mode.QUADS,4096,false,true,()->{},()->{});}
        static final RenderType ENERGY=create("origin_energy",DefaultVertexFormat.POSITION_COLOR,VertexFormat.Mode.QUADS,8192,false,true,
                CompositeState.builder().setShaderState(RENDERTYPE_LIGHTNING_SHADER).setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                        .setCullState(NO_CULL).setWriteMaskState(COLOR_WRITE).setOutputState(PARTICLES_TARGET).createCompositeState(false));
    }
    private static void quad(VertexConsumer b,Matrix4f m,int color,Vec3... corners) {
        for(var v:corners)b.vertex(m,(float)v.x,(float)v.y,(float)v.z).color(color>>16&255,color>>8&255,color&255,color>>>24).endVertex();
    }
    private static Vec3 sphere(Vec3 c,double r,double latitude,double longitude) {
        double ring=r*Math.cos(latitude);return c.add(ring*Math.cos(longitude),r*Math.sin(latitude)*1.45,ring*Math.sin(longitude));
    }
    static void render(RenderLevelStageEvent e,Collection<OriginSceneS2C> scenes) {
        var mc=Minecraft.getInstance();if(mc.level==null||mc.player==null||scenes.isEmpty())return;
        var buffers=mc.renderBuffers().bufferSource();var b=buffers.getBuffer(Types.ENERGY);var matrix=e.getPoseStack().last().pose();
        for(var s:scenes) {
            double age=OriginCinematicClient.age(s,e.getPartialTick()),t=age-OriginTimeline.BURST;
            if(t<0||t>34||mc.player.position().distanceToSqr(s.anchor())>96*96)continue;
            double fade=1-OriginTimeline.ease(t,10,34),r=.25+3.7*OriginTimeline.ease(t,0,15);
            Vec3 center=s.anchor().add(s.forward().scale(OriginTimeline.playerAdvance(age))).add(0,1.1,0).subtract(e.getCamera().getPosition());
            for(int layer=0;layer<2;layer++) {
                double radius=r*(layer==0?1:.57);int color=((int)(fade*(layer==0?100:190))<<24)|(layer==0?0x29BFFF:0xDDFBFF);
                for(int y=0;y<10;y++)for(int i=0;i<24;i++) {
                    double a=-Math.PI/2+y*Math.PI/10,c=a+Math.PI/10,u=i*Math.PI/12,v=u+Math.PI/12;
                    quad(b,matrix,color,sphere(center,radius,a,u),sphere(center,radius,a,v),sphere(center,radius,c,v),sphere(center,radius,c,u));
                }
            }
            double radius=.3+6.5*OriginTimeline.ease(t,0,30);
            Vec3 ground=center.add(0,-1.04,0);
            for(int i=0;i<64;i++) {
                double a=i*Math.PI/32,c=a+Math.PI/32;int color=((int)(220*fade)<<24)|0x90EDFF;
                quad(b,matrix,color,ground.add(Math.cos(a)*radius,0,Math.sin(a)*radius),ground.add(Math.cos(c)*radius,0,Math.sin(c)*radius),
                        ground.add(Math.cos(c)*(radius+.22),.08,Math.sin(c)*(radius+.22)),ground.add(Math.cos(a)*(radius+.22),.08,Math.sin(a)*(radius+.22)));
            }
            for(int i=0;i<14;i++) {
                double a=i*Math.PI/7+t*.04,w=.12+fade*.16;
                Vec3 base=center.add(Math.cos(a)*r*.35,-.6,Math.sin(a)*r*.35);
                Vec3 tip=center.add(Math.cos(a)*r*.8,fade*(4+i%3),Math.sin(a)*r*.8);
                quad(b,matrix,((int)(200*fade)<<24)|0xBBF6FF,base.add(-w,0,0),base.add(w,0,0),tip,tip);
                quad(b,matrix,((int)(200*fade)<<24)|0x67DFFF,base.add(0,0,-w),base.add(0,0,w),tip,tip);
            }
        }
        buffers.endBatch(Types.ENERGY);
    }
}
