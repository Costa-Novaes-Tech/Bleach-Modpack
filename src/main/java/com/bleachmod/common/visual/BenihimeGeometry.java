package com.bleachmod.common.visual;

import com.bleachmod.common.network.s2c.BenihimeFrameS2C;
import net.minecraft.world.phys.Vec3;
import java.util.*;
import static com.bleachmod.common.network.s2c.BenihimeFrameS2C.*;

/** Original vertex model and stitched effects. No borrowed character textures or persistent entities. */
public final class BenihimeGeometry {
    public record Quad(Vec3 a,Vec3 b,Vec3 c,Vec3 d,int color) { }
    private static final int RED=0xD83453B0,WHITE=0xFFF0E7EE,SKIN=0xE1C7BFFF,HAIR=0x251C25FF,ROBE=0xAA304AFF,DARK=0x501B30FF;
    private static final Vec3 UP=new Vec3(0,1,0);
    private BenihimeGeometry(){ }
    public static List<Quad> build(BenihimeFrameS2C m){
        var out=new ArrayList<Quad>();Vec3 o=m.origin(),end=m.end(),f=m.forward().normalize(),left=SenbonGeometry.left(f);double p=m.progress(),r=m.radius();
        switch(m.kind()){
            case QUEEN->{queen(out,m);}
            case SLASH->{Vec3 axis=end.subtract(o);if(axis.lengthSqr()>.001)f=axis.normalize();left=side(f);
                ribbon(out,o,end,left.scale(.12),RED);ribbon(out,o,end,UP.scale(.06),WHITE);
                for(int i=0;i<12;i++){double a=-1.2+2.4*i/12,b=-1.2+2.4*(i+1)/12;Vec3 va=end.add(left.scale(Math.sin(a)*r)).add(0,Math.cos(a)*r,0),vb=end.add(left.scale(Math.sin(b)*r)).add(0,Math.cos(b)*r,0);ribbon(out,va,vb,left.scale(.045),RED);}}
            case SHIELD->{Vec3 c=o.add(f.scale(1.4)).add(0,1.2,0);for(int i=0;i<6;i++){double a=i*Math.PI/3,b=(i+1)*Math.PI/3;Vec3 va=c.add(left.scale(Math.cos(a)*r)).add(UP.scale(Math.sin(a)*r)),vb=c.add(left.scale(Math.cos(b)*r)).add(UP.scale(Math.sin(b)*r));
                    out.add(new Quad(c,va,vb,vb,0xB91B3D2A));ribbon(out,va,vb,va.subtract(c).normalize().scale(.035),RED);if(p<.5)stitch(out,c,va,UP,.12,5);}}
            case NET->{
                ring(out,o.add(0,-.8,0),new Vec3(1,0,0),new Vec3(0,0,1),r,RED,48);
                for(int i=-6;i<=6;i++){double x=i*r/6;double z=Math.sqrt(Math.max(0,r*r-x*x));stitch(out,o.add(x,-.8,-z),o.add(x,-.8,z),new Vec3(1,0,0),.1,5);stitch(out,o.add(-z,-.8,x),o.add(z,-.8,x),new Vec3(0,0,1),.1,5);}
                for(int i=0;i<8;i++){double a=i*Math.PI/4;Vec3 base=o.add(Math.cos(a)*r,-.8,Math.sin(a)*r);ribbon(out,base,o.add(0,Math.min(3,r*.4),0),side(base.subtract(o)).scale(.025),RED);}}
            case BEADS->{ribbon(out,o,end,UP.scale(.015),RED);for(int i=0;i<9;i++){double t=i/8.0;Vec3 c=o.lerp(end,t);double size=t<=p?.18:.07;cube(out,c.add(-size,-size,-size),c.add(size,size,size),t<=p?0xFFB0A3FF:0x822039FF);}}
            case INCISION->{Vec3 c=end.add(0,.7,0);double length=Math.max(.15,r*p);stitch(out,c.add(left.scale(-length)).add(0,-.6,0),c.add(left.scale(length)).add(0,1.2,0),UP,.22,9);
                if(m.variant()==2){ring(out,end.add(0,.3,0),left,UP,r,WHITE,24);stitch(out,c.add(left.scale(-r)).add(0,1,0),c.add(left.scale(r)).add(0,-1,0),UP,.2,7);}
                if(m.variant()==1)ring(out,end.add(0,-.7,0),new Vec3(1,0,0),new Vec3(0,0,1),r,RED,24);}
            case HEAL->{Vec3 c=end.add(0,1,0);for(int i=0;i<12;i++){double a=i*Math.PI/3+p*3,b=(i+1)*Math.PI/3+p*3;Vec3 va=end.add(Math.cos(a)*.5,i*.16,Math.sin(a)*.5),vb=end.add(Math.cos(b)*.5,(i+1)*.16,Math.sin(b)*.5);stitch(out,va,vb,UP,.1,2);}
                if(o.distanceToSqr(end)>1)ribbon(out,o.add(0,.2,0),c,UP.scale(.012),0xFFD3CF80);}
            case BUFF->{for(int sign:new int[]{-1,1}){Vec3 arm=o.add(left.scale(sign*.65));stitch(out,arm.add(0,.65,0),arm.add(0,1.5,0),left,.26,7);
                Vec3 fist=arm.add(f.scale(.4)).add(0,1.1,0);cube(out,fist.add(-.17,-.22,-.17),fist.add(.17,.22,.17),0xE1C7BFCC);stitch(out,fist.add(left.scale(-.22)),fist.add(left.scale(.22)),UP,.17,4);
                ribbon(out,o.add(left.scale(sign*2)).add(0,4,-1),fist,left.scale(.018),RED);}ring(out,o.add(0,.1,0),left,f,.7,RED,16);}
            case CORRIDOR->{Vec3 axis=end.subtract(o).normalize(),across=side(axis),portalUp=axis.cross(across).normalize();Vec3 a=o.add(0,.08,0),b=end.add(0,.08,0);stitch(out,a.add(across.scale(-.9)),b.add(across.scale(-.9)),across,.18,12);stitch(out,a.add(across.scale(.9)),b.add(across.scale(.9)),across,.18,12);
                for(Vec3 portal:List.of(o,end)){ring(out,portal.add(0,1.25,0),across,portalUp,1.2,RED,24);stitch(out,portal.add(across.scale(-.55)).add(0,.2,0),portal.add(across.scale(.55)).add(0,2.3,0),across,.18,7);}}
        }
        return List.copyOf(out);
    }
    private static Vec3 side(Vec3 direction){Vec3 v=direction.cross(UP);return v.lengthSqr()<.001?new Vec3(1,0,0):v.normalize();}
    private static void stitch(List<Quad> out,Vec3 a,Vec3 b,Vec3 cross,double width,int count){
        ribbon(out,a,b,cross.scale(.025),RED);for(int i=0;i<count;i++){Vec3 c=a.lerp(b,(i+.5)/count);Vec3 tangent=b.subtract(a).normalize().scale(.025);ribbon(out,c.subtract(cross.scale(width)),c.add(cross.scale(width)),tangent,WHITE);}
    }
    private static void ring(List<Quad> out,Vec3 c,Vec3 x,Vec3 y,double r,int color,int count){for(int i=0;i<count;i++){double a=i*Math.PI*2/count,b=(i+1)*Math.PI*2/count;Vec3 va=c.add(x.scale(Math.cos(a)*r)).add(y.scale(Math.sin(a)*r)),vb=c.add(x.scale(Math.cos(b)*r)).add(y.scale(Math.sin(b)*r));ribbon(out,va,vb,va.subtract(c).normalize().scale(.025),color);}}
    private static void ribbon(List<Quad> out,Vec3 a,Vec3 b,Vec3 width,int color){out.add(new Quad(a.subtract(width),a.add(width),b.add(width),b.subtract(width),color));}
    private static void cube(List<Quad> out,Vec3 min,Vec3 max,int color){
        Vec3[] v={new Vec3(min.x,min.y,min.z),new Vec3(max.x,min.y,min.z),new Vec3(max.x,max.y,min.z),new Vec3(min.x,max.y,min.z),new Vec3(min.x,min.y,max.z),new Vec3(max.x,min.y,max.z),new Vec3(max.x,max.y,max.z),new Vec3(min.x,max.y,max.z)};
        int[][] faces={{0,3,2,1},{4,5,6,7},{0,4,7,3},{1,2,6,5},{3,7,6,2},{0,1,5,4}};double[] shade={.65,1,.8,.72,1,.55};
        for(int i=0;i<faces.length;i++){var q=faces[i];out.add(new Quad(v[q[0]],v[q[1]],v[q[2]],v[q[3]],shade(color,shade[i])));}
    }
    private static int shade(int c,double amount){return ((int)((c>>>24)*amount)<<24)|((int)(((c>>>16)&255)*amount)<<16)|((int)(((c>>>8)&255)*amount)<<8)|(c&255);}
    private static void limb(List<Quad> out,Vec3 a,Vec3 b,double width,int color){
        Vec3 direction=b.subtract(a).normalize(),x=side(direction).scale(width),y=direction.cross(x).normalize().scale(width);
        Vec3[] v={a.subtract(x).subtract(y),a.add(x).subtract(y),a.add(x).add(y),a.subtract(x).add(y),b.subtract(x).subtract(y),b.add(x).subtract(y),b.add(x).add(y),b.subtract(x).add(y)};
        for(var face:new int[][]{{0,3,2,1},{4,5,6,7},{0,4,7,3},{1,2,6,5},{3,7,6,2},{0,1,5,4}})out.add(new Quad(v[face[0]],v[face[1]],v[face[2]],v[face[3]],color));
    }
    private static void queen(List<Quad> out,BenihimeFrameS2C m){
        var local=new ArrayList<Quad>();double phase=m.progress()*Math.PI*2,breathe=Math.sin(phase)*.10;
        // Kneeling dress: broad folded hem, fitted waist, layered crimson sleeves.
        for(int i=0;i<12;i++){double a=i*Math.PI/6,b=(i+1)*Math.PI/6;Vec3 lowerA=new Vec3(Math.cos(a)*3.4,.15,Math.sin(a)*1.8),lowerB=new Vec3(Math.cos(b)*3.4,.15,Math.sin(b)*1.8);
            Vec3 upperA=new Vec3(Math.cos(a)*1.25,3.5,Math.sin(a)*.75),upperB=new Vec3(Math.cos(b)*1.25,3.5,Math.sin(b)*.75);
            local.add(new Quad(lowerA,lowerB,upperB,upperA,i%2==0?ROBE:DARK));}
        cube(local,new Vec3(-1.35,3.2,-.65),new Vec3(1.35,5.05+breathe,.65),ROBE);
        local.add(new Quad(new Vec3(-1.2,5.08,.66),new Vec3(1.2,5.08,.66),new Vec3(0,3.35,.68),new Vec3(0,3.35,.68),DARK));
        local.add(new Quad(new Vec3(-.5,5.1,.68),new Vec3(.5,5.1,.68),new Vec3(0,4.45,.69),new Vec3(0,4.45,.69),SKIN));
        cube(local,new Vec3(-.38,5,-.25),new Vec3(.38,5.65,.35),SKIN);
        cube(local,new Vec3(-.67,5.5,-.4),new Vec3(.67,7.05,.55),SKIN);
        cube(local,new Vec3(-.78,6.9,-.52),new Vec3(.78,7.2,.45),HAIR);
        cube(local,new Vec3(-.8,3.9,-.62),new Vec3(.8,7.12,-.43),HAIR);
        for(int sign:new int[]{-1,1}){
            Vec3 hairTop=new Vec3(sign*.77,6.9,.15),hairMiddle=new Vec3(sign*.82,5.1,.28),hairEnd=new Vec3(sign*(.85+.08*Math.sin(phase)),3.55,.45+.12*Math.cos(phase));
            limb(local,hairTop,hairMiddle,.20,HAIR);limb(local,hairMiddle,hairEnd,.17,HAIR);
            // Looped braids with red ties, visible in the supplied reference.
            for(int i=0;i<16;i++){double a=i*Math.PI/8,b=(i+1)*Math.PI/8;limb(local,new Vec3(sign*.92+Math.cos(a)*.42,7.05+Math.sin(a)*.48,0),new Vec3(sign*.92+Math.cos(b)*.42,7.05+Math.sin(b)*.48,0),.13,HAIR);}
            cube(local,new Vec3(sign<0?-1.04:.8,6.62,-.16),new Vec3(sign<0?-.8:1.04,6.75,.25),ROBE);
            limb(local,new Vec3(sign*.57,7.05,.61),new Vec3(sign*.69,5.7,.61),.08,HAIR);
            // Closed eyes, lashes and restrained facial relief.
            limb(local,new Vec3(sign*.13,6.28,.64),new Vec3(sign*.48,6.23,.64),.026,HAIR);
            cube(local,new Vec3(sign<0?-.46:.4,6.18,.57),new Vec3(sign<0?-.4:.46,6.23,.61),HAIR);
            double wave=Math.sin(phase+sign*.7),sway=Math.cos(phase*.5+sign);
            Vec3 shoulder=new Vec3(sign*1.52,4.8+breathe,0),elbow=new Vec3(sign*(2.85+.18*wave),5.25+.3*wave,1.2),hand=new Vec3(sign*(2.1+.3*sway),3.9+.4*wave,3.3+.35*sway);
            if(m.variant()>0){double reach=.55+.45*Math.sin(phase+sign*.5);
                hand=new Vec3(sign*(m.variant()==4?2.8:1.2),3.2+.6*wave,3.5+1.5*reach);elbow=shoulder.lerp(hand,.5).add(sign*.9,1.3,0);}
            limb(local,shoulder,elbow,.43,SKIN);limb(local,elbow,hand,.31,SKIN);
            limb(local,shoulder,shoulder.lerp(elbow,.32),.61,ROBE);
            cube(local,elbow.add(-.4,-.12,-.35),elbow.add(.4,.12,.35),DARK);
            cube(local,hand.add(-.35,-.28,-.18),hand.add(.35,.22,.18),SKIN);
            for(int finger=0;finger<4;finger++){double x=-.26+finger*.17,curl=.13*Math.sin(phase+finger*.55+sign);Vec3 knuckle=hand.add(x,-.46,.15+curl);limb(local,hand.add(x,-.20,.03),knuckle,.065,SKIN);limb(local,knuckle,hand.add(x,-.72+(finger==0?.1:0),.28+curl),.055,SKIN);}
            limb(local,hand.add(sign*.3,.04,.05),hand.add(sign*.53,-.3,.18),.075,SKIN);
            stitch(local,hand.add(-.32,-.05,.2),hand.add(.32,-.05,.2),UP,.07,5);
            // Fine control threads connect the moving hands to the player's shoulders.
            for(int thread=0;thread<3;thread++)ribbon(local,hand.add((thread-1)*.2,-.5,.2),new Vec3(sign*(.22+thread*.08),.45+thread*.18,11/1.35),new Vec3(.008,0,0),0xE6456680);
        }
        cube(local,new Vec3(-.085,6.02,.55),new Vec3(.085,6.38,.72),shade(SKIN,.88));
        cube(local,new Vec3(-.18,5.87,.564),new Vec3(.18,5.91,.59),0x955563FF);
        Vec3 left=SenbonGeometry.left(m.forward()),forward=m.forward();
        for(var q:local)out.add(new Quad(transform(q.a,m.origin(),left,forward),transform(q.b,m.origin(),left,forward),transform(q.c,m.origin(),left,forward),transform(q.d,m.origin(),left,forward),q.color));
    }
    private static Vec3 transform(Vec3 v,Vec3 o,Vec3 left,Vec3 f){return o.add(left.scale(v.x*1.35)).add(0,1+v.y*1.35,0).add(f.scale(v.z*1.35-11));}
}
