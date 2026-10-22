package com.bleachmod.common.visual;

import org.joml.Quaternionf;
import org.joml.Quaternionfc;
import org.joml.Vector3f;
import org.joml.Vector3fc;

/** Analytic two-joint reach, preserving segment lengths. Coordinates are in the parent's frame. */
public final class TwoBonePose {
    public record Chain(Quaternionf upper,Quaternionf lower) { }
    private TwoBonePose() { }

    /** GeckoLib applies Rz * Ry * Rx. JOML 1.10.5's ZYX getter has a wrong X denominator. */
    public static Vector3f eulerZYX(Quaternionfc q) {
        float x=q.x(),y=q.y(),z=q.z(),w=q.w();
        float norm=x*x+y*y+z*z+w*w;
        return new Vector3f((float)Math.atan2(2*(w*x+y*z),norm-2*(x*x+y*y)),
                (float)Math.asin(Math.max(-1,Math.min(1,2*(w*y-z*x)/norm))),
                (float)Math.atan2(2*(w*z+x*y),norm-2*(y*y+z*z)));
    }

    public static Chain solve(Vector3fc shoulder,Vector3fc elbow,Vector3fc wrist,
                              Vector3fc target,Vector3fc bendDirection) {
        var first=new Vector3f(elbow).sub(shoulder);
        var second=new Vector3f(wrist).sub(elbow);
        float a=first.length(),b=second.length();
        var axis=new Vector3f(target).sub(shoulder);
        float length=axis.length();
        if(a<.0001F || b<.0001F || length<.0001F) return new Chain(new Quaternionf(),new Quaternionf());
        axis.div(length);
        float distance=Math.max(Math.abs(a-b)+.0001F,Math.min(a+b-.0001F,length));
        float along=(a*a-b*b+distance*distance)/(2*distance);
        float height=(float)Math.sqrt(Math.max(0,a*a-along*along));
        var bend=new Vector3f(bendDirection).fma(-axis.dot(bendDirection),axis);
        if(bend.lengthSquared()<.0001F) {
            bend.set(1,0,0).fma(-axis.x,axis);
            if(bend.lengthSquared()<.0001F) bend.set(0,1,0).fma(-axis.y,axis);
        }
        bend.normalize();
        var firstAim=new Vector3f(axis).mul(along).fma(height,bend);
        var secondAim=new Vector3f(axis).mul(distance).sub(firstAim);
        var upper=new Quaternionf().rotationTo(first,firstAim);
        upper.transformInverse(secondAim);
        return new Chain(upper,new Quaternionf().rotationTo(second,secondAim));
    }
}
