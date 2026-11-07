package com.bleachmod.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.Map;

/** Shared vertices across each spiral keep its projected energy texture free of cube seams. */
final class RyujinSolarBands {
    private static final int STEPS=64;
    private static final Map<String,Section[]> BANDS=Map.of(
            "inner_swirl", band(49,.1,1.07),
            "mid_swirl", band(66,1.9,1.02),
            "outer_swirl", band(76,3.5,1.01));
    private record Point(float x,float y,float z){}
    private record Section(Point inner,Point outer,float v){}

    private RyujinSolarBands(){}

    private static Section[] band(double base,double phase,double turns){
        var sections=new Section[STEPS+1];
        for(int i=0;i<=STEPS;i++){
            double t=(double)i/STEPS;
            double angle=phase+t*Math.PI*2*turns;
            double radius=base*(.55+.53*t)+2.8*Math.sin(2*angle+.3)+1.2*Math.sin(5*angle);
            double width=(3.8+2.9*(.5+.5*Math.sin(3*angle+.8)))*(.4+.6*Math.sin(Math.PI*t));
            sections[i]=new Section(point(radius-width*.5,angle),point(radius+width*.5,angle),
                    (float)((2632+t*240)/4096));
        }
        return sections;
    }

    private static Point point(double radius,double angle){
        double front=50-40*Math.sqrt(Math.max(0,1-radius*radius/(72*72)))-5.5;
        return new Point((float)(Math.cos(angle)*radius/16),(float)((80+Math.sin(angle)*radius)/16),(float)(front/16));
    }

    static boolean draw(String name,PoseStack.Pose pose,VertexConsumer out,int light,int overlay,
                        float red,float green,float blue,float alpha){
        var sections=BANDS.get(name);
        if(sections==null)return false;
        for(int i=1;i<sections.length;i++){
            var a=sections[i-1];
            var b=sections[i];
            vertex(out,pose,a.inner,18/256F,a.v,light,overlay,red,green,blue,alpha);
            vertex(out,pose,a.outer,238/256F,a.v,light,overlay,red,green,blue,alpha);
            vertex(out,pose,b.outer,238/256F,b.v,light,overlay,red,green,blue,alpha);
            vertex(out,pose,b.inner,18/256F,b.v,light,overlay,red,green,blue,alpha);
        }
        return true;
    }

    private static void vertex(VertexConsumer out,PoseStack.Pose pose,Point p,float u,float v,int light,int overlay,
                               float red,float green,float blue,float alpha){
        out.vertex(pose.pose(),p.x,p.y,p.z).color(red,green,blue,alpha).uv(u,v)
                .overlayCoords(overlay).uv2(light).normal(pose.normal(),0,0,-1).endVertex();
    }
}
