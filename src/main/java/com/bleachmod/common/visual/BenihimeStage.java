package com.bleachmod.common.visual;

import net.minecraft.world.phys.Vec3;
import java.util.ArrayList;
import java.util.List;
import java.util.function.ToDoubleFunction;
import static com.bleachmod.common.visual.BenihimeEntranceTimeline.progress;

/** Presentation only: no blocks, collision, time of day, status effects or player movement. */
public final class BenihimeStage {
    private BenihimeStage() { }
    public static double floorWeight(double age) {
        return progress(age,72,108)*(1-progress(age,520,560));
    }
    public static double darkness(double age) {
        return .66*progress(age,72,120)*(1-progress(age,376,466));
    }
    /** Bounded candidates stay in the rear hemisphere; identical scores retain the original axis. */
    public static float chooseYaw(float yaw,ToDoubleFunction<Vec3> obstruction) {
        float best=yaw;double score=Double.POSITIVE_INFINITY;
        for(int offset:new int[]{0,-25,25,-50,50,-75,75}) {
            float candidate=yaw+offset;double value=obstruction.applyAsDouble(Vec3.directionFromRotation(0,candidate));
            if(!Double.isFinite(value))continue;
            value+=Math.abs(offset)*.035;
            if(value<score){score=value;best=candidate;}
        }
        return best;
    }
    /** Final first-person approach happens after orbiting to the rear, away from the face. */
    public static Vec3 returnEye(Vec3 start,Vec3 target,Vec3 look,double blend) {
        double t=Math.max(0,Math.min(1,blend));
        if(t==0)return start;if(t==1)return target;
        Vec3 delta=start.subtract(target);double radius=Math.sqrt(delta.x*delta.x+delta.z*delta.z);
        if(radius<.001)return start.lerp(target,t);
        double a=Math.atan2(delta.z,delta.x),b=Math.atan2(-look.z,-look.x);
        double turn=Math.atan2(Math.sin(b-a),Math.cos(b-a));
        double orbit=progress(t*560,0,392),approach=progress(t*560,392,560);
        double r=(radius*(1-orbit)+Math.min(radius,2.2)*orbit)*(1-approach);
        return target.add(Math.cos(a+turn*orbit)*r,delta.y*(1-t),Math.sin(a+turn*orbit)*r);
    }
    public static double mask(double x,double y,double centerX,double centerY,double radiusX,double radiusY) {
        double dx=(x-centerX)/Math.max(1,radiusX),dy=(y-centerY)/Math.max(1,radiusY);
        double r=Math.sqrt(dx*dx+dy*dy);
        double t=Math.max(0,Math.min(1,(r-.7)/.8));
        return t*t*(3-2*t);
    }
    public static List<BenihimeGeometry.Quad> floor(Vec3 origin,Vec3 forward,double age) {
        double weight=floorWeight(age);if(weight<=0)return List.of();
        Vec3 f=new Vec3(forward.x,0,forward.z).normalize(),left=SenbonGeometry.left(f);
        var out=new ArrayList<BenihimeGeometry.Quad>(468);
        for(int x=-9;x<9;x++)for(int z=-4;z<22;z++) {
            double spot=Math.exp(-((x+.5)*(x+.5)+(z+.5)*(z+.5))/2.8);
            double edge=Math.min(Math.min(x+10,9-x),Math.min(z+5,22-z))/2.;
            int alpha=(int)(255*weight*Math.min(1,edge));
            int r=14+(int)(spot*88),g=12+(int)(spot*78),b=20+(int)(spot*60);
            int color=r<<24|g<<16|b<<8|alpha;
            Vec3 a=origin.add(left.scale(x)).subtract(f.scale(z)).add(0,.018,0);
            Vec3 right=left,back=f.scale(-1);
            out.add(new BenihimeGeometry.Quad(a,a.add(right),a.add(right).add(back),a.add(back),color));
        }
        return List.copyOf(out);
    }
}
