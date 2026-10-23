package com.bleachmod.common.visual;

import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

/** Camera coordinates relative to the caster: X lateral, Y up, Z forward.
 * Uses the shared half-speed motion clock; gameplay/audio retain their real tick clock. */
public final class HyorinmaruCameraPath {
    public static final double RETURN_START = 180;
    public record Frame(Vec3 eye, Vec3 focus, float fov) { }
    private record Key(double tick, Vec3 eye, Vec3 focus, float fov) { }
    private static Key key(double t, double x, double y, double z, double fx, double fy, double fz, float fov) {
        return new Key(t, new Vec3(x,y,z), new Vec3(fx,fy,fz), fov);
    }
    private static final Key[] KEYS = {
            // Keep the face readable through the entire first voice; no premature wide shot.
            key(0, .25,1.75,3.1, 0,1.55,0, 46),
            key(25, 0,1.68,1.9, 0,1.55,0, 42),
            // Pull out before emergence (real tick 88), with room above all three heads.
            key(44, 11,5.5,24, 0,5,-3, 60),
            key(82, 14,8,26, 0,8,-4, 62),
            key(108, -16,7,23, 0,7,-3, 62),
            // Side view keeps caster and breath in frame instead of burying the camera in snow.
            key(144, -27,9,18, 0,5,8, 64),
            key(160, -10,3.4,9, 0,2,0, 54),
            // Return to the transformed caster for the full-name voice at real tick 330.
            key(176, -1.5,1.95,4.3, 0,1.55,0, 46),
            key(RETURN_START, -.8,1.85,4, 0,1.55,0, 46)
    };
    private HyorinmaruCameraPath() { }
    public static double ease(double t) {
        t=Mth.clamp(t,0,1);
        return t*t*t*(t*(t*6-15)+10);
    }
    public static Frame sample(double age) {
        for (int i=1;i<KEYS.length;i++) {
            Key a=KEYS[i-1], b=KEYS[i];
            if(age<=b.tick) {
                double t=ease((age-a.tick)/(b.tick-a.tick));
                return new Frame(a.eye.lerp(b.eye,t), a.focus.lerp(b.focus,t), (float)Mth.lerp(t,a.fov,b.fov));
            }
        }
        Key end=KEYS[KEYS.length-1];
        return new Frame(end.eye,end.focus,end.fov);
    }
    /** Interpolating opposing focus points can collapse the look vector. Blend angles instead. */
    public static Vec3 blendDirection(Vec3 from, Vec3 to, double t) {
        from=from.normalize(); to=to.normalize();
        double yaw=Math.toDegrees(Math.atan2(-from.x,from.z));
        double targetYaw=Math.toDegrees(Math.atan2(-to.x,to.z));
        double pitch=-Math.toDegrees(Math.asin(Mth.clamp(from.y,-1,1)));
        double targetPitch=-Math.toDegrees(Math.asin(Mth.clamp(to.y,-1,1)));
        return Vec3.directionFromRotation((float)Mth.lerp(t,pitch,targetPitch),
                (float)(yaw+Mth.wrapDegrees(targetYaw-yaw)*t));
    }
    /** Orbit around the actor, rather than taking a straight chord through their face/body. */
    public static Vec3 returnEye(Vec3 start, Vec3 center, Vec3 target, Vec3 look, double t) {
        t=Mth.clamp(t,0,1);
        if(t==0)return start;
        if(t==1)return target;
        Vec3 a=start.subtract(center), b=target.subtract(center);
        if(b.horizontalDistance()<.01)return BenihimeStage.returnEye(start,target,look,t);
        double angle=Math.atan2(a.z,a.x), end=Math.atan2(b.z,b.x);
        double turn=Math.atan2(Math.sin(end-angle),Math.cos(end-angle));
        double radius=Mth.lerp(t,a.horizontalDistance(),b.horizontalDistance());
        return center.add(Math.cos(angle+turn*t)*radius,Mth.lerp(t,a.y,b.y),Math.sin(angle+turn*t)*radius);
    }
}
