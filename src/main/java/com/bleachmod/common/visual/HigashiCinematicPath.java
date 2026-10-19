package com.bleachmod.common.visual;

import net.minecraft.world.phys.Vec3;

/** Authored shots in a stable attacker/target frame; no world or graphics state. */
public final class HigashiCinematicPath {
    public static final int IMPACT=12, END=44, PREVIEW_TIMEOUT=18;
    public record Shot(Vec3 eye,Vec3 focus,float fov){ }
    private HigashiCinematicPath(){ }
    private static double ease(double t){t=Math.max(0,Math.min(1,t));return t*t*(3-2*t);}
    public static Shot sample(Vec3 attacker,Vec3 victim,float height,float age){
        Vec3 f=victim.subtract(attacker).multiply(1,0,1).normalize();if(f.lengthSqr()<.1)f=new Vec3(0,0,1);
        Vec3 side=new Vec3(-f.z,0,f.x),middle=attacker.lerp(victim,.5);
        double span=Math.max(4.8,attacker.distanceTo(victim)*1.15),t;Vec3 eye,focus;float fov;
        if(age<10){t=ease(age/10.0);eye=middle.add(side.scale(span-.35*t)).subtract(f.scale(.7)).add(0,1.65+.2*t,0);focus=middle.add(0,1.05,0);fov=64;}
        else if(age<18){t=ease((age-10)/8.0);eye=middle.add(side.scale(span-.45)).add(f.scale(.9+t*.35)).add(0,1.35,0);focus=middle.add(0,1.05,0);fov=60;}
        else{t=ease((age-18)/26.0);eye=middle.add(side.scale(span+.8*t)).subtract(f.scale(.5+t*.6)).add(0,1.8+t*.9,0);focus=middle.lerp(victim,.25*t).add(0,Math.min(2,height*.55)+t*.45,0);fov=66;}
        return new Shot(eye,focus,fov);
    }
    public static float yaw(Vec3 eye,Vec3 focus){Vec3 v=focus.subtract(eye);return (float)Math.toDegrees(Math.atan2(-v.x,v.z));}
    public static float pitch(Vec3 eye,Vec3 focus){Vec3 v=focus.subtract(eye);return (float)-Math.toDegrees(Math.atan2(v.y,v.horizontalDistance()));}
}
