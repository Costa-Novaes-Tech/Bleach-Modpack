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
            case "flame_barrage"->curve(age,sign,neutral(0),
                    key(4,v(-2.7,.22,-.22),v(-1.75,-.22,.18),v(-.1,-.22,0),v(-.18,0,.06),Vec3.ZERO,v(-.10,.06,-.12),v(-.95,0,.12)),
                    key(6,v(-.55,-.18,.12),v(-.95,-.2,.12),v(.22,.24,0),v(.30,0,.06),Vec3.ZERO,v(.02,-.10,-.22),v(.65,0,-.16)),
                    key(9,v(-.28,-.22,.14),v(-.8,-.16,.1),v(.16,.19,0),v(.20,0,.05),Vec3.ZERO,v(.02,-.07,-.15),v(.38,0,-.12)),
                    k(13,-.40,-.10,.10,.06,.10,.08,.10,0,-.06),neutral(16));
            case "flame_fan"->curve(age,sign,neutral(0),k(5,-1.25,1.05,-.15,0,-.5,-.2,-.1,-.85,.6),k(13,-1.25,0,-.2,.07,0,.2,.05,0,0),k(20,-.9,-1.05,.15,.06,.5,.25,.1,.85,-.6),neutral(22));
            case "flame_tornado"->curve(age,sign,neutral(0),k(3,-2,.65,-.4,0,-.4,-.2,-.35,-.5,.7),k(7,-2,-.65,.4,.04,.45,.2,-.2,.55,-.7),k(12,-1.1,.65,-.2,.02,-.35,-.18,.1,-.45,.5),neutral(18));
            case "jokaku"->curve(age,sign,neutral(0),k(5,-2.1,.2,-.25,-.05,-.15,-.1,-.65,.05,.2),k(13,-.85,-.1,-.15,.12,.12,.18,.25,-.05,-.25),k(17,-.7,-.15,-.1,.08,.15,.12,.2,-.1,-.2),neutral(20));
            case "flame_dash"->curve(age,sign,neutral(0),
                    key(2,v(-2.95,.08,-.08),v(-2.85,-.08,.08),Vec3.ZERO,Vec3.ZERO,Vec3.ZERO,v(-.04,-.04,-.16),v(-.15,0,0)),
                    key(13,v(-2.95,.08,-.08),v(-2.85,-.08,.08),Vec3.ZERO,Vec3.ZERO,Vec3.ZERO,v(-.04,-.04,-.16),v(-.15,0,0)),neutral(16));
            // Coiled at the outside hip; short whole-body rotation, not Rajada's overhead swing.
            case "higashi"->curve(age,sign,neutral(0),higashiHip(10),higashiCross(12),higashiEnd(16),neutral(22));
            case "minami"->curve(age,sign,neutral(0),k(7,-2.15,.2,-.3,-.05,-.1,-.18,-.65,0,.2),k(16,-.3,.15,-.15,.3,-.1,.4,.7,0,-.25),k(18,-.3,.15,-.15,.28,-.1,.35,.65,0,-.2),neutral(22));
            case "steam_cut"->curve(age,sign,neutral(0),
                steamReady(10),steamReady(27),steamLift(34),steamLift(35),
                steamGround(40),steamGround(46),
                key(51,v(-1.72,-.53,0),v(-1.5,.53,0),Vec3.ZERO,v(.08,0,.04),v(.2,0,0),Vec3.ZERO,Vec3.ZERO),neutral(56));
            default->interpolate(neutral(0),neutral(0),0,sign,0);
        };
    }
    /** Wrists meet in front of the sternum; yaw solves the grip without moving shoulder pivots. */
    private static Key steamReady(float time){return key(time,v(-1.745,-.533,0),v(-1.50,.526,0),
            v(.015,0,0),v(.14,0,.04),Vec3.ZERO,v(-.12,.06,-.16),v(-.15,0,0));}
    private static Key steamLift(float time){return key(time,v(-2.65,-.53,.32),v(-2.45,.53,-.24),
            v(-.04,0,0),v(-.12,0,.04),v(-.17,0,0),v(-.10,.16,-.16),v(-.6,0,0));}
    /** Blade points down; authored length1.35 reaches ground at contact, not a diagonal swipe. */
    private static Key steamGround(float time){return key(time,v(-1.55,-.524,0),v(-1.65,.526,0),
            Vec3.ZERO,v(.2,0,.04),v(2.947,0,0),v(-.08,-.20,-.25),v(1.4,0,0));}
    private static Key dashKey(float age,double stride,double lean){
        return key(age,v(-.48,.10,-.10),v(-.72,-.35,.38),v(lean,-.08,0),v(stride,0,.04),
                Vec3.ZERO,v(-.04,-.06,-.18),v(-.18,.08,-.02));
    }
    // Positive roll keeps the sword hand on its own hip. The former negative roll crossed the torso.
    private static Key higashiHip(float tick){
        return key(tick,v(.18,.04,.24),v(-.5,.18,-.12),v(.06,-.45,0),v(.12,0,.06),Vec3.ZERO,
                v(.10,-.20,.04),v(.16,-.10,-.18));
    }
    private static Key higashiCross(float tick){
        return key(tick,v(-.32,-.14,.06),v(-.65,.10,.08),v(.02,-.12,0),v(.04,0,.05),Vec3.ZERO,
                v(.03,-.06,-.12),v(-.18,.08,.12));
    }
    private static Key higashiEnd(float tick){
        return key(tick,v(-.98,-.45,-.16),v(-.8,.18,.14),v(-.06,.50,0),v(-.14,0,.05),Vec3.ZERO,
                v(-.10,.12,-.12),v(-.55,.22,.38));
    }
    /** Same draw and rising blade as the normal strike, followed by the confirmed dramatic hold. */
    public static Pose execution(float age,float sign){
        return curve(age,sign,neutral(0),higashiHip(10),higashiCross(12),
                higashiEnd(16),higashiEnd(HigashiCinematicPath.IGNITE),
                k(HigashiCinematicPath.END-8,-.65,-.15,.06,0,.16,-.03,0,0,0),neutral(HigashiCinematicPath.END));
    }
    /** No idle layer: outward pose exists only inside the 60-tick release and fades out by tick 60. */
    public static Pose release(float age,float sign){
        return curve(age,sign,neutral(0),
                key(10,v(-.65,-.25,-.75),v(-1.45,.65,.3),v(0,-.12,0),v(0,0,.04),Vec3.ZERO,v(-.18,-.04,-.1),v(-.25,0,1.1)),
                key(38,v(-.65,-.25,-.75),v(-1.45,-.25,.3),v(0,-.12,0),v(0,0,.04),Vec3.ZERO,v(-.18,-.04,-.1),v(-.25,0,1.1)),
                key(47,v(-.25,1.2,-.03),Vec3.ZERO,Vec3.ZERO,Vec3.ZERO,Vec3.ZERO,v(.12,-.10,-.08),v(-.1,.35,-.65)),
                key(53,v(-.25,1.2,-.03),Vec3.ZERO,Vec3.ZERO,Vec3.ZERO,Vec3.ZERO,v(.12,-.10,-.08),v(-.1,.35,-.65)),neutral(60));
    }

    /**
     * Authored 140-tick transformation stance for Ryūjin Bankai / Zanka no Tachi.
     * Planted stance, erect chest, katana held low pointing outward on the wielding side.
     * Fades weight smoothly to 0 between ticks 120 and 140.
     */
    public static Pose bankaiEntrance(float age, float sign) {
        float breathe = (float) Math.sin(age * 0.15) * 0.015F;
        return curve(age, sign, neutral(0),
                key(8, v(-.25, 1.2, -.03), v(-.2, -.1, .05), v(0, 0, 0), v(.05, 0, .04), Vec3.ZERO, v(.12, -.10, -.08), v(-.1, .35, -.65)),
                key(20, v(-.38, .95, -.12), v(-.65, .10, .12), v(.02, -.08, 0), v(.08, 0, .04), Vec3.ZERO, v(.08, -.10, -.10), v(-.12, .25, -.50)),
                key(32, v(-.48, .80, -.18), v(-.9, .15, .18), v(.03, -.12, 0), v(.10, 0, .04), Vec3.ZERO, v(.06, -.06, -.12), v(-.18, .20, -.40)),
                key(44, v(-.35, 1.05, -.08), v(-.65, .10, .12), v(.02, -.06, 0), v(.08, 0, .04), Vec3.ZERO, v(.10, -.08, -.10), v(-.12, .3, -.55)),
                key(52, v(-.25, 1.2, -.03), v(-.2, -.1, .05), v(0, 0, 0), v(.05, 0, .04), Vec3.ZERO, v(.12, -.10, -.08), v(-.1, .35, -.65)),
                key(60, v(-.25, 1.22, -.03), v(-.18, -.1, .05), v(0, 0, 0), v(.05, 0, .04), Vec3.ZERO, v(.12, -.10, -.08), v(-.08, .35, -.65)),
                key(120, v(-.25, 1.22, -.03), v(-.18, -.1, .05), v(breathe, 0, 0), v(.05, 0, .04), Vec3.ZERO, v(.12, -.10, -.08), v(-.08, .35, -.65)),
                neutral(140));
    }

    private static Vec3 modelRotation(Vec3 vector,Vec3 angles){
        double x=vector.x,y=vector.y*Math.cos(angles.x)-vector.z*Math.sin(angles.x),z=vector.y*Math.sin(angles.x)+vector.z*Math.cos(angles.x);
        double xx=x*Math.cos(angles.y)+z*Math.sin(angles.y),zz=-x*Math.sin(angles.y)+z*Math.cos(angles.y);
        return v(xx*Math.cos(angles.z)-y*Math.sin(angles.z),xx*Math.sin(angles.z)+y*Math.cos(angles.z),zz);
    }
    /** Local coordinates: anatomical right, up, forward. Accounts for the authored katana's
     * -45° element / 55° third-person display and vanilla held-item rotations. */
    public static Vec3 bladeDirection(Pose pose){
        Vec3 r=modelRotation(modelRotation(v(0,Math.sin(Math.toRadians(10)),-Math.cos(Math.toRadians(10))),pose.item),pose.arm);
        return v(-r.x,-r.y,-r.z).normalize();
    }
    public static Vec3 supportHand(Pose pose,float sign){
        Vec3 r=modelRotation(v(0,.625,0),pose.support);
        return v(-.3125*sign-r.x,1.375-r.y,-r.z);
    }
    public static Vec3 bladeHand(Pose pose,float sign){
        Vec3 r=modelRotation(v(0,.625,0),pose.arm);
        return v(.3125*sign-r.x,1.375-r.y,-r.z);
    }
}
