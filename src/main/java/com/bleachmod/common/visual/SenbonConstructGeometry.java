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
            boolean dash=pattern==1;
            double unfold=dash?.45:(.2+.8*progress),sweep=dash?.8:.15;
            for(int side:new int[]{-1,1}) {
                Vec3 scapula=origin.add(0,1.35,0).add(forward.scale(-.25)).add(left.scale(side*.22));
                // Tier 1: Coverts (upper tier, 5 layered feathers)
                for(int i=0;i<5;i++) {
                    double t=i/5.0,next=(i+1)/5.0;
                    Vec3 root=scapula.add(left.scale(side*t*.8*unfold)).add(0,.15*Math.sin(t*Math.PI),0).add(forward.scale(.04));
                    Vec3 edge=scapula.add(left.scale(side*next*.8*unfold)).add(0,.15*Math.sin(next*Math.PI),0).add(forward.scale(.04));
                    Vec3 tipPos=root.add(edge).scale(.5).add(left.scale(side*(.3+.8*t)*unfold)).add(0,.45+.5*Math.sin(t*Math.PI),0).add(forward.scale(-sweep*(.3+.4*t)));
                    Vec3 tipW=left.scale(side*.08);
                    out.add(new Quad(root,edge,tipPos.add(tipW),tipPos.subtract(tipW),255,250,255,215));
                    ribbon(out,root.add(edge).scale(.5),tipPos,left.scale(.02),255,255,255,250);
                }
                // Tier 2: Primaries (main flight feathers, 8 layered feathers)
                for(int i=0;i<8;i++) {
                    double t=i/8.0,next=(i+1)/8.0;
                    Vec3 root=scapula.add(left.scale(side*(.2+3.2*t)*unfold)).add(0,.3*Math.sin(t*Math.PI),0).add(forward.scale(-.04));
                    Vec3 edge=scapula.add(left.scale(side*(.2+3.2*next)*unfold)).add(0,.3*Math.sin(next*Math.PI),0).add(forward.scale(-.04));
                    double span=(.5+3.6*t)*unfold,arch=Math.sin(t*Math.PI)*1.5+.3*t,drop=-(.35+.95*t)*(dash?.4:1.2);
                    Vec3 tipPos=scapula.add(left.scale(side*span)).add(0,arch+drop,0).add(forward.scale(-sweep*(.5+1.2*t)));
                    Vec3 tipW=left.scale(side*.12*(1.0-.4*t));
                    out.add(new Quad(root,edge,tipPos.add(tipW),tipPos.subtract(tipW),255,248,255,195));
                    ribbon(out,root.add(edge).scale(.5),tipPos,left.scale(.025),255,255,255,245);
                }
            }
            Vec3 center=origin.add(0,1.8,0).add(forward.scale(-.5));
            for(int i=0;i<24;i++) {
                double a=i*Math.PI/12,b=(i+1)*Math.PI/12;
                Vec3 p1=center.add(left.scale(Math.cos(a)*1.15)).add(up.scale(Math.sin(a)*1.15));
                Vec3 p2=center.add(left.scale(Math.cos(b)*1.15)).add(up.scale(Math.sin(b)*1.15));
                ribbon(out,p1,p2,p1.subtract(center).normalize().scale(.025),255,242,255,205);
            }
            // The bright sword silhouette indicates exactly where the final thrust will travel.
            sword(out,origin.add(0,.95,0).add(forward.scale(.6)),forward,left,2.7,true);
        } else if(shape==LANCE) {
            Vec3 end=origin.add(forward.scale(radius));
            ribbon(out,origin,end,left.scale(.12),255,245,255,70);
            ribbon(out,origin,end,up.scale(.12),255,245,255,70);
            ribbon(out,origin,end,left.scale(.03),255,255,255,200);
            ribbon(out,origin,end,up.scale(.03),255,255,255,200);
        }
        return List.copyOf(out);
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
