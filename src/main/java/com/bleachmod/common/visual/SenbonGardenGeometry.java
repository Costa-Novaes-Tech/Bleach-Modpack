package com.bleachmod.common.visual;

import net.minecraft.world.phys.Vec3;

/** Fixed floor samples: only one third forms the outline; the interior never drains away. */
public final class SenbonGardenGeometry {
    private SenbonGardenGeometry(){ }
    public static Vec3 point(double radius,int index,int count,double bloom){
        double angle=index*2.399963229728653,r=radius*Math.sqrt((index+.5)/count);
        double target=SenbonGeometry.flowerRadius(radius,angle);
        if(index%3==0)r+=(target-r)*Math.max(0,Math.min(1,bloom));
        return new Vec3(Math.cos(angle)*r,0,Math.sin(angle)*r);
    }
}
