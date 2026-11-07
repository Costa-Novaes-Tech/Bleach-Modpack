package com.bleachmod.common.visual;

import net.minecraft.world.phys.Vec3;
import java.util.ArrayList;
import java.util.List;

/** Four continuous, tapered solar streams. Presentation only; no gameplay or particle spawning. */
public final class RyujinSealFlow {
    public static final int STREAMS=4, SEGMENTS=8;
    public record Strip(Vec3 start,Vec3 end,Vec3 startTangent,Vec3 endTangent,double startWidth,double endWidth,int alpha,double pulse) { }
    private RyujinSealFlow(){ }
    public static boolean enabled(){return Boolean.getBoolean("bleachmod.ryujinV14Pilot");}
    public static Vec3 point(Vec3 start,Vec3 control,Vec3 end,double t){
        double u=1-t;
        return start.scale(u*u).add(control.scale(2*u*t)).add(end.scale(t*t));
    }
    private static double width(double t,double envelope,int stream){
        return (.36+.12*stream)*envelope*(.55+.45*Math.sin(Math.PI*t))*(1-.72*t);
    }
    private static Vec3 tangent(Vec3 start,Vec3 control,Vec3 end,double t){
        return control.subtract(start).scale(2*(1-t)).add(end.subtract(control).scale(2*t)).normalize();
    }
    public static List<Strip> sample(Vec3 anchor,Vec3 forward,double phase,double realTick,
                                     RyujinBankaiPresentation.Blade blade){
        var strips=new ArrayList<Strip>(STREAMS*SEGMENTS);
        if(blade==null||!Double.isFinite(phase)||!Double.isFinite(realTick)||phase<=32||phase>=52)return strips;
        double envelope=RyujinBankaiTimeline.progress(phase,32,35)*(1-RyujinBankaiTimeline.progress(phase,50,52));
        if(envelope<=.001)return strips;
        Vec3 side=new Vec3(-forward.z,0,forward.x);
        Vec3 centre=RyujinSolarPose.center(anchor,forward,phase,blade);
        double radius=Math.max(.15,RyujinBankaiTimeline.sunRadius(phase)*.70);
        for(int stream=0;stream<STREAMS;stream++){
            double angle=stream*Math.PI*.5+realTick*.045;
            Vec3 start=centre.add(side.scale(Math.cos(angle)*radius)).add(0,Math.sin(angle)*radius,0);
            Vec3 end=blade.hand().lerp(blade.tip(),.25+.18*stream);
            Vec3 control=start.lerp(end,.48).add(side.scale(Math.sin(angle+.8)*radius*.65))
                    .add(0,.55+radius*.18,0).add(forward.scale(.45));
            for(int i=0;i<SEGMENTS;i++){
                double t0=i/(double)SEGMENTS,t1=(i+1)/(double)SEGMENTS;
                double pulse=.72+.28*Math.sin(((t0+t1)*.5-realTick*.045)*Math.PI*4-stream);
                strips.add(new Strip(point(start,control,end,t0),point(start,control,end,t1),
                        tangent(start,control,end,t0),tangent(start,control,end,t1),
                        width(t0,envelope,stream),width(t1,envelope,stream),(int)(225*envelope),pulse));
            }
        }
        return strips;
    }
}
