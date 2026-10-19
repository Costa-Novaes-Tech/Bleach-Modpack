package com.bleachmod.common.visual;

import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

/** Render-time following with shortest-arc yaw and a teleport reset. No gameplay entity is moved. */
public final class BenihimeFollowMotion {
    public record Pose(Vec3 anchor,float yaw){public Vec3 forward(){return Vec3.directionFromRotation(0,yaw);}}
    private BenihimeFollowMotion(){ }
    public static Pose advance(Pose previous,Vec3 target,float yaw,double elapsedTicks){
        if(previous==null||previous.anchor.distanceToSqr(target)>32*32)return new Pose(target,yaw);
        double alpha=1-Math.exp(-Math.max(0,Math.min(20,elapsedTicks))*.18);
        return new Pose(previous.anchor.lerp(target,alpha),(float)(previous.yaw+Mth.wrapDegrees(yaw-previous.yaw)*alpha));
    }
}
