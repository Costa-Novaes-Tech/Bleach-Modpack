package com.bleachmod.common.visual;

import net.minecraft.world.phys.Vec3;
import java.util.List;

/** Authored pose curves, in radians/model pixels. No client dependencies or gameplay movement. */
public final class RyujinChoreography {
    public static final List<String> ATTACKS=List.of("sealed_cut","sealed_sweep","flame_barrage","flame_fan","flame_tornado","jokaku","flame_dash","higashi","minami","steam_cut");
    public record Pose(Vec3 arm,Vec3 support,Vec3 body,Vec3 leg,Vec3 item,Vec3 handPosition,Vec3 handRotation,float weight){ }
    private record Key(float tick,Vec3 arm,Vec3 support,Vec3 body,Vec3 leg,Vec3 item,Vec3 handPosition,Vec3 handRotation){ }
    private static Vec3 v(double x,double y,double z){return new Vec3(x,y,z);}
    private static Key key(float time,Vec3 arm,Vec3 support,Vec3 body,Vec3 leg,Vec3 item,Vec3 hand,Vec3 handRot){return new Key(time,arm,support,body,leg,item,hand,handRot);}
    private static Key k(float t,double ax,double ay,double az,double bx,double by,double lx,double rx,double ry,double rz){
        return key(t,v(ax,ay,az),v(-.45,-.25,.15),v(bx,by,0),v(lx,0,.06),v(0,0,0),v(-.06,-.02,-.04),v(rx,ry,rz));
    }
    private static Key neutral(float t){return key(t,Vec3.ZERO,Vec3.ZERO,Vec3.ZERO,Vec3.ZERO,Vec3.ZERO,Vec3.ZERO,Vec3.ZERO);}
    private static float smooth(float t){t=Math.max(0,Math.min(1,t));return t*t*(3-2*t);}
    private static Vec3 mirror(Vec3 v,float sign){return new Vec3(v.x,v.y*sign,v.z*sign);}
    private static Pose interpolate(Key a,Key b,float f,float sign,float weight){
        return new Pose(mirror(a.arm.lerp(b.arm,f),sign),mirror(a.support.lerp(b.support,f),sign),mirror(a.body.lerp(b.body,f),sign),mirror(a.leg.lerp(b.leg,f),sign),mirror(a.item.lerp(b.item,f),sign),
                a.handPosition.lerp(b.handPosition,f).multiply(sign,1,1),mirror(a.handRotation.lerp(b.handRotation,f),sign),weight);
    }
    private static Pose curve(float age,float sign,Key... keys){
        if(!Float.isFinite(age) || age<=0 || age>=keys[keys.length-1].tick)return interpolate(neutral(0),neutral(0),0,sign,0);
        int i=1;while(i<keys.length-1 && age>keys[i].tick)i++;
        float f=smooth((age-keys[i-1].tick)/(keys[i].tick-keys[i-1].tick));
        float weight=smooth(age/2)*smooth((keys[keys.length-1].tick-age)/3);
        return interpolate(keys[i-1],keys[i],f,sign,weight);
    }
    /** End tick agrees with the server gesture; body translations are never entity translations. */
    public static Pose attack(String kind,float age,float sign){
        return switch(kind){
            case "sealed_cut"->curve(age,sign,neutral(0),k(4,-2.25,.2,-.3,-.04,-.2,-.12,-.6,.05,.1),k(6,-.55,-.25,.2,.12,.18,.22,.5,-.05,-.1),k(10,.05,-.35,.15,.08,.25,.15,.2,-.1,-.15),neutral(16));
            case "sealed_sweep"->curve(age,sign,neutral(0),k(4,-1.1,.9,-.15,0,-.5,-.18,-.1,-.7,.5),k(6,-1.1,-.7,.1,.05,.4,.2,.1,.65,-.55),k(10,-.7,-.9,.2,.04,.5,.18,.1,.8,-.35),neutral(16));
            case "flame_barrage"->curve(age,sign,neutral(0),k(4,-2.65,.25,-.25,-.08,-.18,-.2,-.9,0,.1),k(6,-.5,-.2,.15,.2,.22,.3,.6,0,-.15),k(10,.1,-.3,.2,.12,.25,.2,.3,0,-.2),neutral(16));
            case "flame_fan"->curve(age,sign,neutral(0),k(5,-1.25,1.05,-.15,0,-.5,-.2,-.1,-.85,.6),k(13,-1.25,0,-.2,.07,0,.2,.05,0,0),k(20,-.9,-1.05,.15,.06,.5,.25,.1,.85,-.6),neutral(22));
            case "flame_tornado"->curve(age,sign,neutral(0),k(3,-2,.65,-.4,0,-.4,-.2,-.35,-.5,.7),k(7,-2,-.65,.4,.04,.45,.2,-.2,.55,-.7),k(12,-1.1,.65,-.2,.02,-.35,-.18,.1,-.45,.5),neutral(18));
            case "jokaku"->curve(age,sign,neutral(0),k(5,-2.1,.2,-.25,-.05,-.15,-.1,-.65,.05,.2),k(13,-.85,-.1,-.15,.12,.12,.18,.25,-.05,-.25),k(17,-.7,-.15,-.1,.08,.15,.12,.2,-.1,-.2),neutral(20));
            case "flame_dash"->curve(age,sign,neutral(0),k(2,-1.45,.15,-.15,.3,-.1,-.65,-.2,.15,.05),k(8,-1.6,.1,-.1,.35,-.08,.65,-.15,.1,0),k(12,-1.45,.1,-.1,.28,-.06,-.65,-.1,.1,0),neutral(16));
            // Coiled at the outside hip; short whole-body rotation, not Rajada's overhead swing.
            case "higashi"->curve(age,sign,neutral(0),k(6,.25,.65,-.95,.07,-.40,.15,-.6,0,-.3),k(10,.30,.70,-1.0,.08,-.45,.18,-.65,0,-.35),k(16,-.9,-.15,.25,-.06,.50,-.18,.15,0,.2),neutral(22));
            case "minami"->curve(age,sign,neutral(0),k(7,-2.15,.2,-.3,-.05,-.1,-.18,-.65,0,.2),k(16,-.3,.15,-.15,.3,-.1,.4,.7,0,-.25),k(18,-.3,.15,-.15,.28,-.1,.35,.65,0,-.2),neutral(22));
            case "steam_cut"->curve(age,sign,neutral(0),k(9,-2.8,.2,-.2,-.08,-.2,-.2,-1.05,0,.1),k(16,-2.75,.25,-.25,-.1,-.25,-.25,-1.1,0,.15),k(18,-.5,-.2,.1,.22,.3,.4,.8,0,-.3),k(24,.05,-.4,.2,.15,.35,.25,.3,0,-.25),neutral(32));
            default->interpolate(neutral(0),neutral(0),0,sign,0);
        };
    }
    /** Execution shot: draw at the hip, strike on tick 12, hold the follow-through then recover. */
    public static Pose execution(float age,float sign){
        return curve(age,sign,neutral(0),
                k(6,.25,.65,-.95,.07,-.40,.15,-.6,0,-.3),
                k(10,.30,.70,-1.0,.08,-.45,.18,-.65,0,-.35),
                k(16,-.9,-.15,.25,-.06,.50,-.18,.15,0,.2),
                k(HigashiCinematicPath.IGNITE,-.9,-.15,.25,-.06,.50,-.18,.15,0,.2),
                k(HigashiCinematicPath.END-8,-1.25,-.30,.35,0,.2,-.03,0,0,0),neutral(HigashiCinematicPath.END));
    }
    /** No idle layer: outward pose exists only inside the 60-tick release and fades out by tick 60. */
    public static Pose release(float age,float sign){
        return curve(age,sign,neutral(0),
                key(10,v(-.65,-.25,-.75),v(-1.45,.65,.3),v(0,-.12,0),v(0,0,.04),Vec3.ZERO,v(-.18,-.04,-.1),v(-.25,0,1.1)),
                key(38,v(-.65,-.25,-.75),v(-1.45,-.25,.3),v(0,-.12,0),v(0,0,.04),Vec3.ZERO,v(-.18,-.04,-.1),v(-.25,0,1.1)),
                key(47,v(-.25,1.2,-.03),Vec3.ZERO,Vec3.ZERO,Vec3.ZERO,Vec3.ZERO,v(.12,-.10,-.08),v(-.1,.35,-.65)),
                key(53,v(-.25,1.2,-.03),Vec3.ZERO,Vec3.ZERO,Vec3.ZERO,Vec3.ZERO,v(.12,-.10,-.08),v(-.1,.35,-.65)),neutral(60));
    }
    private static Vec3 modelRotation(Vec3 vector,Vec3 angles){
        double x=vector.x,y=vector.y*Math.cos(angles.x)-vector.z*Math.sin(angles.x),z=vector.y*Math.sin(angles.x)+vector.z*Math.cos(angles.x);
        double xx=x*Math.cos(angles.y)+z*Math.sin(angles.y),zz=-x*Math.sin(angles.y)+z*Math.cos(angles.y);
        return v(xx*Math.cos(angles.z)-y*Math.sin(angles.z),xx*Math.sin(angles.z)+y*Math.cos(angles.z),zz);
    }
    /** Local coordinates: anatomical right, up, forward. Accounts for the authored katana's
     * -45° element / 55° third-person display and vanilla held-item rotations. */
    public static Vec3 bladeDirection(Pose pose){
        Vec3 r=modelRotation(v(0,Math.sin(Math.toRadians(10)),-Math.cos(Math.toRadians(10))),pose.arm);
        return v(-r.x,-r.y,-r.z).normalize();
    }
    public static Vec3 bladeHand(Pose pose,float sign){
        Vec3 r=modelRotation(v(0,.625,0),pose.arm);
        return v(.3125*sign-r.x,1.375-r.y,-r.z);
    }
}
