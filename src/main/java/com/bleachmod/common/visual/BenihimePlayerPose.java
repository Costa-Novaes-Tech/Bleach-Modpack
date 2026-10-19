package com.bleachmod.common.visual;

/** Standing two-hand entrance. Item offsets are pixels; whole-body translation is blocks. */
public final class BenihimePlayerPose {
    public record Limb(net.minecraft.world.phys.Vec3 rotation,double bend){ }
    private BenihimePlayerPose(){ }
    public static double weight(double age){
        return BenihimeEntranceTimeline.progress(age,0,20)*(1-BenihimeEntranceTimeline.progress(age,520,550));
    }
    public static float bodyOffset(double age){return 0;}
    public static double faceHeight(double age){return 1.62+bodyOffset(age);}
    // Native straight-arm solution: no optional BendyLib is required or enabled globally.
    public static Limb mainArm(boolean left){return new Limb(new net.minecraft.world.phys.Vec3(-.986781598,left?.586010111:-.586010111,left?.547101250:-.547101250),0);}
    public static Limb supportArm(boolean leftMain){return new Limb(new net.minecraft.world.phys.Vec3(-.821124155,leftMain?-.384789654:.384789654,leftMain?-.404645935:.404645935),0);}
    public static net.minecraft.world.phys.Vec3 itemRotation(boolean left){return left?new net.minecraft.world.phys.Vec3(-.152397469,.768508097,2.448441417):new net.minecraft.world.phys.Vec3(-2.640129334,.768508097,.693151237);}
    public static net.minecraft.world.phys.Vec3 itemPosition(boolean left){return left?new net.minecraft.world.phys.Vec3(.856478328,-.182147874,-.801859753):new net.minecraft.world.phys.Vec3(.832721013,.229897075,.814515246);}
}
