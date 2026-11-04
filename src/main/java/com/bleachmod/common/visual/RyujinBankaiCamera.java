package com.bleachmod.common.visual;

import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

/** Frontal dolly on the caster's sagittal axis. No fixed rear-camera endpoint. */
public final class RyujinBankaiCamera {
    public static final int RETURN_START = RyujinBankaiTimeline.RETURN_START, RETURN_SETTLE = 140;
    public record Frame(Vec3 eye, Vec3 focus, float fov) { }
    private RyujinBankaiCamera() { }
    private static Frame frame(Vec3 anchor, Vec3 forward, double z, double y, double focusY, double focusZ, float fov) {
        return new Frame(anchor.add(forward.scale(z)).add(0,y,0),
                anchor.add(forward.scale(focusZ)).add(0,focusY,0), fov);
    }
    private static Frame blend(Frame a, Frame b, double age, double start, double end) {
        double t=RyujinBankaiTimeline.progress(age,start,end);
        return new Frame(a.eye.lerp(b.eye,t),a.focus.lerp(b.focus,t),(float)Mth.lerp(t,a.fov,b.fov));
    }
    public static Frame sample(Vec3 anchor, Vec3 forward, double age) {
        return sample(anchor,forward,age,1);
    }
    public static Frame sample(Vec3 anchor, Vec3 forward, double age, float sign) {
        age=RyujinBankaiTimeline.clamp(age);
        // All shots stay on the caster's front axis. The high wide eye sees over the
        // shoulders while leaving the full rear sun and flame corona in frame.
        Frame opening=frame(anchor,forward,7.8,2.5,1.45,0,58);
        Frame speech=frame(anchor,forward,4.3,2.65,1.65,0,48);
        Frame wide=frame(anchor,forward,12.5,8.2,5.0,-2.2,76);
        Frame seal=frame(anchor,forward,7.2,3.4,2.2,0,60);
        // Close again only as the sun finishes sealing into the outward blade.
        Frame detail=frame(anchor,forward,3.8,2.35,1.25,0,48);
        Frame name=frame(anchor,forward,5.2,1.5,1.15,0,54);
        if(age<10)return blend(opening,speech,age,0,10);
        if(age<16)return speech;
        if(age<26)return blend(speech,wide,age,16,26);
        if(age<=34)return wide;
        if(age<52)return blend(wide,seal,age,34,52);
        if(age<64)return blend(seal,detail,age,52,64);
        if(age<=72)return detail;
        if(age<88)return blend(detail,name,age,72,88);
        return name;
    }
    /** Rear return uses an orbit; frontal return is already on the correct side. */
    public static Vec3 returnEye(Vec3 start, Vec3 center, Vec3 target, Vec3 look, double t, boolean frontal) {
        t=Mth.clamp(t,0,1);
        if(t==0)return start;
        if(t==1)return target;
        if(frontal)return start.lerp(target,t);
        Vec3 a=start.subtract(center), b=target.subtract(center);
        // First-person endpoint is the eye itself: hold a safe radius while rotating behind
        // the actor, then approach from behind. Never interpolate through the face/chest.
        if(b.horizontalDistance()<.01){
            double angle=Math.atan2(a.z,a.x),end=Math.atan2(-look.z,-look.x);
            double turn=Math.atan2(Math.sin(end-angle),Math.cos(end-angle));
            double orbit=RyujinBankaiTimeline.smooth(Math.min(1,t/.65));
            double settle=RyujinBankaiTimeline.smooth(Math.max(0,(t-.65)/.35));
            double radius=a.horizontalDistance()*(1-settle);
            return center.add(Math.cos(angle+turn*orbit)*radius,a.y*(1-settle),Math.sin(angle+turn*orbit)*radius);
        }
        double angle=Math.atan2(a.z,a.x), end=Math.atan2(b.z,b.x);
        double turn=Math.atan2(Math.sin(end-angle),Math.cos(end-angle));
        double r=Mth.lerp(t,a.horizontalDistance(),b.horizontalDistance());
        return center.add(Math.cos(angle+turn*t)*r,Mth.lerp(t,a.y,b.y),Math.sin(angle+turn*t)*r);
    }
    public static Vec3 blendDirection(Vec3 from, Vec3 to, double t) {
        from=from.normalize();to=to.normalize();
        double yaw=Math.toDegrees(Math.atan2(-from.x,from.z)), end=Math.toDegrees(Math.atan2(-to.x,to.z));
        double pitch=-Math.toDegrees(Math.asin(Mth.clamp(from.y,-1,1))), target=-Math.toDegrees(Math.asin(Mth.clamp(to.y,-1,1)));
        return Vec3.directionFromRotation((float)Mth.lerp(t,pitch,target),(float)(yaw+Mth.wrapDegrees(end-yaw)*t));
    }
}
