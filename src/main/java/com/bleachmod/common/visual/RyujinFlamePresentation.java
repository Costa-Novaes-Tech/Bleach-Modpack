package com.bleachmod.common.visual;

import net.minecraft.world.phys.Vec3;
import java.util.List;
import java.util.ArrayList;

/** Cosmetic geometry only. Never moves an actor or determines an attack hit. */
public final class RyujinFlamePresentation {
    private RyujinFlamePresentation() { }

    /** Align the actor's upright axis with travel, including vertical and antiparallel directions. */
    public static org.joml.Quaternionf dashTilt(Vec3 direction){
        Vec3 f=direction!=null&&finite(direction)&&direction.lengthSqr()>.0001?direction.normalize():new Vec3(0,0,1);
        return new org.joml.Quaternionf().rotationTo(0,1,0,(float)f.x,(float)f.y,(float)f.z);
    }

    /** A narrow, irregular crest inside Rajada's existing one-block half-width. */
    public static Vec3 crest(int sample, int age) {
        double x=(sample%13-6)/6.0;
        double layer=(sample/13)/4.0;
        double height=(1.65-.85*Math.abs(x))*(.9+.1*Math.sin(sample*2.4+age*1.7));
        return new Vec3(x*.92,.12+layer*height,-.10*(sample%3));
    }

    /** Samples actual displacement, including turns/vertical motion. A teleport cannot draw a bridge. */
    public static List<Vec3> trail(Vec3 previous, Vec3 current) {
        if(previous==null || !finite(previous) || !finite(current))return List.of();
        double distance=previous.distanceTo(current);
        if(distance<.025 || distance>3)return List.of();
        int count=Math.min(12,Math.max(1,(int)Math.ceil(distance/.18)));
        var result=new ArrayList<Vec3>(count);
        for(int i=1;i<=count;i++)result.add(previous.lerp(current,i/(double)count));
        return List.copyOf(result);
    }
    private static boolean finite(Vec3 p){return Double.isFinite(p.x)&&Double.isFinite(p.y)&&Double.isFinite(p.z);}
}
