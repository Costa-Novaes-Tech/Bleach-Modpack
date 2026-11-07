package com.bleachmod.common.visual;

import net.minecraft.world.phys.Vec3;
import java.util.ArrayList;
import java.util.List;

/** Anatomical RIGHT eye in vanilla head-model coordinates (negative model X). */
public final class RyujinEyeFlame {
    private static final double PIXEL = 1.0 / 16;
    // Face UV (8, 8). The vanilla right eye spans U9–10 on V12.
    private static final Vec3 EYE = new Vec3(-2 * PIXEL, -3.5 * PIXEL, -4.6 * PIXEL);
    private static final int FLAME_COUNT = 6, TRAIL_SEGMENTS = 6;
    private static final double SEAL = RyujinBankaiTimeline.realAge(
            RyujinBankaiTimeline.ABSORPTION_END, RyujinBankaiTimeline.DURATION);
    public record Flame(Vec3 at,double width,double height,int alpha) { }
    public record Trail(Vec3 start, Vec3 end, double startWidth, double endWidth, int startAlpha, int endAlpha) { }
    private RyujinEyeFlame(){ }
    public static double envelope(double realTick){
        if(!Double.isFinite(realTick)||realTick<20||realTick>=SEAL)return 0;
        double held=1-.19*RyujinBankaiTimeline.smooth((realTick-54)/22);
        double sealFade=1-RyujinBankaiTimeline.smooth((realTick-(SEAL-8))/8);
        return RyujinBankaiTimeline.smooth((realTick-20)/12)*held*sealFade;
    }
    public static List<Flame> sample(double realTick){
        var flames=new ArrayList<Flame>(FLAME_COUNT);double power=envelope(realTick);
        if(power<.005)return flames;
        for(int i=0;i<FLAME_COUNT;i++){
            double t=.24+.11*i;
            double life=.5+.5*Math.sin(realTick*.62-i*1.7);
            double size=(.022+.023*life)*(1-.62*t)*power;
            Vec3 point=tail(t,realTick).add(0,-.015-.027*life,-.008-i*.001);
            flames.add(new Flame(point,size,size*(1.5+.7*life),(int)(248*power*(1-.70*t))));
        }
        return flames;
    }
    public static Flame core(double realTick){
        double power=envelope(realTick);
        double height=.29+.023*Math.sin(realTick*.26);
        // Match the atlas's v=.73 coverage point to the anatomical eye.
        return new Flame(EYE.add(0,-height*.23,0),.26,height,(int)(255*power));
    }
    private static Vec3 tail(double t,double tick){
        return EYE.add(-.085-.37*t, -.024-.085*t-.15*t*t+.032*Math.sin(t*5-tick*.31)*t,
                .006+.20*t*t);
    }
    public static List<Trail> trail(double realTick){
        var strips=new ArrayList<Trail>(TRAIL_SEGMENTS);double power=envelope(realTick);
        if(power<.005)return strips;
        for(int i=0;i<TRAIL_SEGMENTS;i++){
            double t=(double)i/TRAIL_SEGMENTS, end=(double)(i+1)/TRAIL_SEGMENTS;
            strips.add(new Trail(tail(t,realTick),tail(end,realTick),
                    width(t,power),width(end,power),alpha(t,power),alpha(end,power)));
        }
        return strips;
    }
    private static double width(double t,double power){return (.005+.071*Math.pow(1-t,1.35))*power;}
    private static int alpha(double t,double power){return (int)(255*power*Math.pow(1-t,.66));}
}
