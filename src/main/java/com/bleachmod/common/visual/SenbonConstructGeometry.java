package com.bleachmod.common.visual;

import net.minecraft.world.phys.Vec3;
import java.util.*;
import static com.bleachmod.common.network.s2c.SenbonPetalsS2C.*;

/** Bounded, code-native luminous geometry; also usable by previews and regression tests. */
public final class SenbonConstructGeometry {
    public record Quad(Vec3 a,Vec3 b,Vec3 c,Vec3 d,int red,int green,int blue,int alpha) { }
    private SenbonConstructGeometry() { }
    public static List<Quad> build(int shape,Vec3 origin,Vec3 forward,double radius,double progress,int pattern) {
        var out=new ArrayList<Quad>();Vec3 left=SenbonGeometry.left(forward),up=new Vec3(0,1,0);
        if(shape==SWORDS) {
            if(pattern==4){
                for(int side:new int[]{-1,1})for(int i=0;i<6;i++){
                    Vec3 base=origin.add(left.scale(side*3.5)).add(forward.scale(-4+i*2));
                    sword(out,base,up,forward,7,false);
                }
                return List.copyOf(out);
            }
            double collapse=pattern==2 ? Math.max(.12,1-progress*.88) : pattern==1 ? Math.max(.12,1-(progress-.5)*1.75) : 1;
            for(int row=0;row<2;row++) for(int i=0;i<16;i++) {
                double angle=2*Math.PI*(i+row*.5)/16;
                Vec3 radial=new Vec3(Math.cos(angle),0,Math.sin(angle));
                Vec3 base=origin.add(radial.scale(radius*collapse)).add(0,.25+row*(pattern==2?1.5:3.1),0);
                sword(out,base,pattern==2?radial.scale(-1):up,pattern==2?up:new Vec3(-radial.z,0,radial.x),2.5,false);
            }
        } else if(shape==WINGS) {
            double unfold=.25+.75*progress;
            for(int side:new int[]{-1,1}) for(int i=0;i<12;i++) {
                double t=i/12.0,next=(i+1)/12.0;Vec3 shoulder=origin.add(0,1.45,0).add(forward.scale(-.35));
                Vec3 top=wingTop(shoulder,left,forward,side,t,unfold),edge=wingTop(shoulder,left,forward,side,next,unfold);
                Vec3 tip=top.add(edge).scale(.5).add(left.scale(side*(.3+.35*t))).add(0,-(.45+1.15*t)*unfold,0);
                out.add(new Quad(top,edge,tip,tip,255,248,255,175));
                Vec3 middle=top.add(edge).scale(.5);
                ribbon(out,middle,tip,left.scale(.025),255,255,255,245);
            }
            Vec3 center=origin.add(0,1.9,0).add(forward.scale(-.7));
            for(int i=0;i<40;i++) {
                double a=i*Math.PI/20,b=(i+1)*Math.PI/20;
                Vec3 start=center.add(left.scale(Math.cos(a)*1.25)).add(up.scale(Math.sin(a)*1.25));
                Vec3 end=center.add(left.scale(Math.cos(b)*1.25)).add(up.scale(Math.sin(b)*1.25));
                ribbon(out,start,end,start.subtract(center).normalize().scale(.025),255,240,255,195);
            }
            // The bright sword silhouette indicates exactly where the final thrust will travel.
            sword(out,origin.add(0,.9,0).add(forward.scale(.7)),forward,left,2.6,true);
        } else if(shape==LANCE) {
            Vec3 end=origin.add(forward.scale(radius));int green=pattern==1 ? 155 : 245;
            ribbon(out,origin,end,left.scale(.2),255,green,255,100);
            ribbon(out,origin,end,up.scale(.2),255,green,255,100);
            ribbon(out,origin,end,left.scale(.045),255,255,255,235);
            ribbon(out,origin,end,up.scale(.045),255,255,255,235);
        }
        return List.copyOf(out);
    }
    private static Vec3 wingTop(Vec3 shoulder,Vec3 left,Vec3 forward,int side,double t,double unfold) {
        return shoulder.add(left.scale(side*(.35+3.8*t)*unfold)).add(0,Math.sin(t*Math.PI)*1.5+.4*t,0).add(forward.scale(-t*.65));
    }
    private static void sword(List<Quad> out,Vec3 base,Vec3 axis,Vec3 side,double height,boolean white) {
        // Hilt, square crossguard, luminous pink blade with a thin white core and pointed tip.
        ribbon(out,base,base.add(axis.scale(.4)),side.scale(.065),150,70,165,235);
        ribbon(out,base.add(axis.scale(.43)).add(side.scale(-.3)),base.add(axis.scale(.43)).add(side.scale(.3)),axis.scale(.045),255,180,220,230);
        Vec3 heel=base.add(axis.scale(.5)),neck=base.add(axis.scale(height-.3)),tip=base.add(axis.scale(height));
        ribbon(out,heel,neck,side.scale(.12),white?255:245,white?245:115,white?255:225,155);
        ribbon(out,heel,neck,side.scale(.035),255,245,255,245);
        out.add(new Quad(neck.subtract(side.scale(.12)),neck.add(side.scale(.12)),tip,tip,255,white?255:215,250,225));
    }
    private static void ribbon(List<Quad> out,Vec3 a,Vec3 b,Vec3 half,int r,int g,int blue,int alpha) {
        out.add(new Quad(a.subtract(half),a.add(half),b.add(half),b.subtract(half),r,g,blue,alpha));
    }
}
