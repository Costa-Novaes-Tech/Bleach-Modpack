package com.bleachmod.common.visual;

import net.minecraft.world.phys.Vec3;

/** Authored shots in a stable attacker/target frame; no world or graphics state. */
public final class HigashiCinematicPath {
    public static final int IMPACT=12, SLASH_END=16, IGNITE=SLASH_END+40, END=IGNITE+32, PREVIEW_TIMEOUT=22;
    public record Shot(Vec3 eye,Vec3 focus,float fov){ }
    private HigashiCinematicPath(){ }
    private static double ease(double t){t=Math.max(0,Math.min(1,t));return t*t*(3-2*t);}
    public static Shot sample(Vec3 attacker,Vec3 victim,float height,float age){
        Vec3 f=victim.subtract(attacker).multiply(1,0,1).normalize();if(f.lengthSqr()<.1)f=new Vec3(0,0,1);
        Vec3 side=new Vec3(-f.z,0,f.x),middle=attacker.lerp(victim,.5);
        double span=Math.max(4.8,attacker.distanceTo(victim)*1.15),t;Vec3 eye,focus;float fov;
        // Stable side-on composition throughout: both actors remain in frame.
        t=ease(Math.max(0,age-IGNITE)/32.0);
        eye=middle.add(side.scale(span+.6*t)).subtract(f.scale(.25)).add(0,1.55+.4*t,0);
        focus=middle.add(0,Math.max(1.05,Math.min(2,height*.5)),0);fov=64;
        return new Shot(eye,focus,fov);
    }
    public static float yaw(Vec3 eye,Vec3 focus){Vec3 v=focus.subtract(eye);return (float)Math.toDegrees(Math.atan2(-v.x,v.z));}
    public static float pitch(Vec3 eye,Vec3 focus){Vec3 v=focus.subtract(eye);return (float)-Math.toDegrees(Math.atan2(v.y,v.horizontalDistance()));}
}
