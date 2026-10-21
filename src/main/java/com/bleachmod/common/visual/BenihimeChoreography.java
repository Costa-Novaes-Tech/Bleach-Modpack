package com.bleachmod.common.visual;

import net.minecraft.world.phys.Vec3;
import java.util.List;

/** Sword gestures only: no attack scheduling, reach, damage, movement or release changes. */
public final class BenihimeChoreography {
    public static final List<String> TECHNIQUES=List.of("cane_draw","byakurai","six_rods","gigai","nake","shield","shibari","hiasobi","dissection","reconstruction","final_stitch","path");
    private record Key(float phase,Vec3 arm,Vec3 support,Vec3 body,Vec3 leg,Vec3 hand,Vec3 rotation) { }
    private BenihimeChoreography() { }
    private static Vec3 v(double x,double y,double z){return new Vec3(x,y,z);}
    private static Key neutral(float t){return new Key(t,Vec3.ZERO,Vec3.ZERO,Vec3.ZERO,Vec3.ZERO,Vec3.ZERO,Vec3.ZERO);}
    private static Key k(float t,double ax,double ay,double az,double torso,Vec3 support,Vec3 hand,Vec3 rotation){
        return new Key(t,v(ax,ay,az),support,v(.04,torso,0),v(.06,0,.025),hand,rotation);
    }
    private static double smooth(double t){t=Math.max(0,Math.min(1,t));return t*t*(3-2*t);}
    private static Vec3 mirror(Vec3 p,float sign){return p.multiply(1,sign,sign);}
    private static RyujinChoreography.Pose curve(float age,int duration,float sign,Key... keys){
        if(!Float.isFinite(age)||duration<1||age<=0||age>=duration)return new RyujinChoreography.Pose(Vec3.ZERO,Vec3.ZERO,Vec3.ZERO,Vec3.ZERO,Vec3.ZERO,Vec3.ZERO,Vec3.ZERO,0);
        float t=age/duration;int i=1;while(i<keys.length-1&&t>keys[i].phase)i++;
        var a=keys[i-1];var b=keys[i];double f=smooth((t-a.phase)/(b.phase-a.phase));
        float w=(float)(smooth(age/1.25)*smooth((duration-age)/1.75));
        return new RyujinChoreography.Pose(mirror(a.arm.lerp(b.arm,f),sign),mirror(a.support.lerp(b.support,f),sign),
                mirror(a.body.lerp(b.body,f),sign),mirror(a.leg.lerp(b.leg,f),sign),Vec3.ZERO,
                a.hand.lerp(b.hand,f).multiply(sign,1,1),mirror(a.rotation.lerp(b.rotation,f),sign),w);
    }
    /** Three short cuts replace ability gestures reused on vanilla swings. */
    public static RyujinChoreography.Pose melee(int combo,float age,int duration,float sign){
        Vec3 support=v(-.45,.12,.10),grip=v(-.035,.015,-.07);
        return switch(Math.floorMod(combo,3)){
            case 0->curve(age,duration,sign,neutral(0),
                    k(.18F,-.85,.65,-.42,-.18,support,grip,v(-.12,-.35,-.28)),
                    k(.48F,-.25,-.45,.28,.18,support,grip,v(.16,.32,.20)),
                    k(.72F,-.35,-.55,.20,.20,support,grip,v(.10,.28,.12)),neutral(1));
            case 1->curve(age,duration,sign,neutral(0),
                    k(.18F,-.35,-.65,.35,.18,support,grip,v(.08,.35,.24)),
                    k(.48F,-.65,.50,-.30,-.18,support,grip,v(-.14,-.30,-.20)),
                    k(.72F,-.65,.55,-.20,-.20,support,grip,v(-.08,-.26,-.10)),neutral(1));
            default->curve(age,duration,sign,neutral(0),
                    k(.18F,-.75,.12,-.12,-.10,v(-.65,.12,.18),v(-.05,-.03,.03),v(-.15,.08,0)),
                    k(.48F,-.28,-.10,-.05,.14,v(-.55,.12,.16),v(-.02,-.015,-.22),v(.12,0,-.03)),
                    k(.70F,-.35,-.12,-.06,.12,support,v(-.025,-.01,-.15),v(.08,0,-.02)),neutral(1));
        };
    }
    /** Eight existing techniques, same server leases; redirection belongs to presentation only. */
    public static RyujinChoreography.Pose technique(String kind,float age,int duration,float sign){
        Vec3 support=v(-.6,.18,.16),grip=v(-.04,.02,-.07);
        return switch(kind){
            case "cane_draw","nake","dissection"->curve(age,duration,sign,neutral(0),
                    k(.22F,kind.equals("nake")?-1.8:-.65,.65,-.30,-.24,support,grip,v(-.35,-.4,-.25)),
                    k(.48F,-.35,-.40,.20,.20,support,v(-.02,-.04,-.18),v(.25,.3,.12)),
                    k(.76F,-.45,-.48,.14,.16,support,grip,v(.12,.22,.08)),neutral(1));
            case "shield"->curve(age,duration,sign,neutral(0),
                    k(.25F,-1.15,-.30,-.30,-.12,v(-1.0,.30,.24),v(-.12,.04,-.1),v(-.10,-.15,.4)),
                    k(.72F,-1.12,-.30,-.28,-.10,v(-1.0,.30,.24),v(-.12,.04,-.1),v(-.08,-.15,.36)),neutral(1));
            case "six_rods","shibari"->curve(age,duration,sign,neutral(0),
                    k(.22F,-1.05,.65,-.35,-.15,v(-1.05,-.55,.30),grip,v(-.12,.3,.20)),
                    k(.55F,-1.05,-.25,-.10,.15,v(-1.05,.25,.12),v(-.06,.02,-.17),v(-.08,-.22,.05)),
                    k(.8F,-.75,-.10,-.08,.08,v(-.75,.1,.10),grip,v(.05,-.1,0)),neutral(1));
            case "hiasobi"->curve(age,duration,sign,neutral(0),
                    k(.24F,-1.0,.25,-.20,-.1,v(-1.0,-.25,.20),grip,v(-.12,.2,.12)),
                    k(.5F,-.42,-.12,-.08,.12,v(-.55,.1,.12),v(-.02,-.06,-.14),v(.20,-.08,-.12)),
                    k(.78F,-.5,-.1,-.08,.08,support,grip,v(.08,-.06,-.06)),neutral(1));
            case "reconstruction"->curve(age,duration,sign,neutral(0),
                    k(.22F,-.85,.35,-.20,-.1,v(-1.1,-.25,.25),grip,v(-.12,.22,-.1)),
                    k(.5F,-.75,.12,-.10,0,v(-.95,-.10,.15),v(-.07,.04,-.1),v(-.08,.06,-.16)),
                    k(.78F,-.8,.22,-.16,.08,v(-1.0,-.2,.20),grip,v(-.1,.12,-.08)),neutral(1));
            case "final_stitch"->curve(age,duration,sign,neutral(0),
                    k(.20F,-1.8,.55,-.45,-.14,v(-1.8,-.55,.45),grip,v(-.25,.3,-.2)),
                    k(.48F,-2.4,.3,-.3,0,v(-2.4,-.3,.3),v(-.05,.08,-.1),v(-.4,.1,0)),
                    k(.80F,-.5,-.3,.12,.18,v(-.5,.3,-.12),v(-.03,-.04,-.18),v(.3,-.2,.1)),
                    k(.92F,-.55,-.2,.1,.1,support,grip,v(.1,-.1,0)),neutral(1));
            case "byakurai","gigai","path"->curve(age,duration,sign,neutral(0),
                    k(.2F,-.80,.10,-.12,-.1,support,v(-.04,-.03,.04),v(-.2,.1,0)),
                    k(.5F,-.35,-.1,-.06,.16,support,v(-.03,-.02,-.22),v(.08,0,-.08)),
                    k(.75F,-.50,-.1,-.06,.1,support,grip,v(.04,0,-.04)),neutral(1));
            default->curve(age,duration,sign,neutral(0),neutral(1));
        };
    }
}
