package com.bleachmod.common.visual;

import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

/** Presentation coordinates match the figure rig: X across, Y up, +Z behind the caster.
 * The close-up occurs on the fully raised stage, so the rig's flat reference matches its render pose. */
public final class BenihimeEntranceCamera {
    public record Frame(Vec3 eye, Vec3 focus, float fov) { }
    private BenihimeEntranceCamera() { }

    private static double ease(double age, double from, double to) {
        double t=Mth.clamp((age-from)/(to-from),0,1);
        return t*t*t*(t*(t*6-15)+10);
    }
    private static Frame blend(Frame a, Frame b, double t) {
        return new Frame(a.eye.lerp(b.eye,t),a.focus.lerp(b.focus,t),(float)Mth.lerp(t,a.fov,b.fov));
    }
    /** Keep the established broad composition and its late lateral movement unchanged. */
    public static Frame wide(double age) {
        double t=BenihimeEntranceTimeline.progress(age,72,112);
        double lateral=2*t*(1-BenihimeEntranceTimeline.progress(age,340,450));
        return new Frame(new Vec3(-lateral,1.5+1.6*t,-3-5*t),new Vec3(0,1+2.5*t,8*t),58);
    }
    private static Vec3 graspFocus(double age) {
        var rig=BenihimeEntranceGeometry.sample(age);
        // Include the palm and the moving hem instead of aiming at a hardcoded curtain centre.
        return BenihimeEntranceGeometry.hemAnchor(age).add(.2,.08,.08).lerp(rig.left().wrist(),.22);
    }
    private static Frame grasp(double age) {
        Vec3 focus=graspFocus(age);
        return new Frame(focus.add(-3.4,.65,-4.4),focus,42);
    }
    private static Frame reveal(double age) {
        Vec3 focus=graspFocus(age).lerp(BenihimeEntranceGeometry.sample(age).head(),.65);
        return new Frame(new Vec3(-4.6,5.8,2),focus,50);
    }
    public static Frame sample(double age) {
        age=BenihimeEntranceTimeline.clamp(age);
        if(age<108)return wide(age);
        if(age<138)return blend(wide(108),grasp(age),ease(age,108,138));
        if(age<176)return grasp(age);
        if(age<236)return blend(grasp(age),reveal(age),ease(age,176,236));
        Frame supports=new Frame(new Vec3(-3,4,-4),new Vec3(0,4.2,10),56);
        if(age<260)return blend(reveal(age),supports,ease(age,236,260));
        if(age<280)return blend(supports,wide(age),ease(age,260,280));
        return wide(age);
    }
}
