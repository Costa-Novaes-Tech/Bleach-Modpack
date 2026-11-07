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
        return mix(a,b,t);
    }
    private static Frame mix(Frame a,Frame b,double t) {
        return new Frame(a.eye.lerp(b.eye,t),a.focus.lerp(b.focus,t),(float)Mth.lerp(t,a.fov,b.fov));
    }
    /** Shorter reveal keeps the speech hold without increasing the peak dolly speed.
     * Integrated trapezoid velocity: continuous at ends and bounded to 1.25 times average. */
    private static Frame reveal(Frame from,Frame to,double age) {
        double u=Mth.clamp((age-20)/10,0,1);
        double t=u<.2?u*u/.32:u>.8?1-(1-u)*(1-u)/.32:1.25*u-.125;
        return mix(from,to,t);
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
        Frame wide=frame(anchor,forward,14.2,9.0,6.8,-2.2,80);
        Frame seal=frame(anchor,forward,7.2,3.4,2.2,0,60);
        // Close again only as the sun finishes sealing into the outward blade.
        Frame detail=frame(anchor,forward,3.8,2.35,1.25,0,48);
        Frame name=frame(anchor,forward,5.2,1.5,1.15,0,54);
        if(age<10)return blend(opening,speech,age,0,10);
        if(age<20)return speech;
        if(age<30)return reveal(speech,wide,age);
        if(age<=34)return wide;
        if(age<52)return blend(wide,seal,age,34,52);
        if(age<64)return blend(seal,detail,age,52,64);
        if(age<=72)return detail;
        if(age<88)return blend(detail,name,age,72,88);
        return name;
    }
    /** Frontal weapon-side closeup; the same blade anchor drives the seal streams. */
    public static Frame sample(Vec3 anchor,Vec3 forward,double age,float sign,RyujinBankaiPresentation.Blade blade){
        if(blade==null)return sample(anchor,forward,age,sign);
        Frame face=frame(anchor,forward,2.15,1.9,1.62,0,38);
        Frame reveal=frame(anchor,forward,7.0,2.1,3.5,-3,66);
        if(age<10)return blend(sample(anchor,forward,0,sign),face,age,0,10);
        if(age<14)return face;
        if(age<20)return blend(face,sample(anchor,forward,20-1e-5,sign),age,14,20);
        if(age<30)return reveal(sample(anchor,forward,20,sign),reveal,age);
        if(age<=32)return reveal;
        Vec3 right=new Vec3(-forward.z,0,forward.x);
        Vec3 focus=blade.hand().lerp(blade.tip(),.53).add(0,.31,0);
        Frame detail=new Frame(anchor.add(right.scale(sign*.95)).add(forward.scale(3.2)).add(0,1.55,0),focus,46);
        // The client hands off from this same shot when the return phase begins.
        if(age<50)return blend(reveal,detail,age,32,50);
        return detail;
    }
    /** Fit the caster's feet and visible sun above terrain inside the letterboxed viewport.
     * Only used after a terrain hit; keep the frontal axis and the collision-safe eye. */
    public static Frame confined(Frame planned,Vec3 safeEye,Vec3 anchor,Vec3 forward,double age,double aspect){
        double radius=RyujinBankaiTimeline.sunRadius(age);
        if(age<20||age>=52||radius<.1)return new Frame(safeEye,planned.focus,planned.fov);
        Vec3 sun=RyujinBankaiPresentation.sunCenter(anchor,forward);
        double actorDistance=Math.max(.1,safeEye.subtract(anchor).horizontalDistance());
        double sunDistance=Math.max(.1,safeEye.subtract(sun).horizontalDistance());
        double bottom=Math.atan2(anchor.y+.04-safeEye.y,actorDistance);
        double top=Math.atan2(sun.y+radius*1.35-safeEye.y,sunDistance);
        double middle=(bottom+top)*.5;
        double half=Math.max(Math.abs(top-bottom)*.5+Math.toRadians(3),
                Math.atan(radius*1.12/sunDistance/Math.max(.5,aspect)));
        // Letterbox masks ten percent of the screen at either edge.
        float fitFov=(float)Mth.clamp(Math.toDegrees(2*Math.atan(Math.tan(half)/.8)),planned.fov,110);
        Vec3 fitFocus=safeEye.subtract(forward.scale(10)).add(0,Math.tan(middle)*10,0);
        double severity=RyujinBankaiTimeline.smooth(Mth.clamp((planned.eye.distanceTo(anchor)-safeEye.distanceTo(anchor))/5,0,1));
        float fov=(float)Mth.lerp(severity,planned.fov,fitFov);
        Vec3 focus=planned.focus.lerp(fitFocus,severity);
        if(severity>0){
            double reach=focus.subtract(safeEye).horizontalDistance();
            double maxPitch=bottom+Math.atan(Math.tan(Math.toRadians(fov*.5))*.72);
            double ceiling=safeEye.y+Math.tan(maxPitch)*reach;
            focus=new Vec3(focus.x,Math.min(focus.y,ceiling),focus.z);
        }
        return new Frame(safeEye,focus,fov);
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
