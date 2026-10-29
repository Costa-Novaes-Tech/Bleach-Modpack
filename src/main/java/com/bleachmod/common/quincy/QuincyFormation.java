package com.bleachmod.common.quincy;

import net.minecraft.world.phys.Vec3;

/** Shared geometry for the four orbiting Reishi emitters and their actual launch positions. */
public final class QuincyFormation {
    private QuincyFormation() { }
    public static Vec3 star(Vec3 origin,float yaw,int index,double time) {
        Vec3 forward=Vec3.directionFromRotation(0,yaw),right=new Vec3(-forward.z,0,forward.x);
        double side=index<2?-1:1,outer=index%2;
        return origin.subtract(forward.scale(.7+outer*.3)).add(right.scale(side*(1.1+outer*.9)))
                .add(0,1.8+outer*.8+Math.sin(time*.07+index)*.08,0);
    }
}
