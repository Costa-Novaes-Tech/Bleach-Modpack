package com.bleachmod.common.visual;

import net.minecraft.world.phys.Vec3;
import java.util.Map;

/** Presentation-only timelines for the remaining existing kits; never moves an entity. */
public final class SignatureChoreography {
    public static final Map<String,Integer> LENGTHS=Map.ofEntries(
        Map.entry("petal_cut",6),Map.entry("five_cuts",54),Map.entry("petal_stream",24),Map.entry("petal_convergence",12),Map.entry("petal_guard",12),
        Map.entry("floral_garden",16),Map.entry("floral_bloom",16),Map.entry("senkei",20),Map.entry("ikka_senjinka",12),Map.entry("hakuteiken",26),
        Map.entry("nake",15),Map.entry("shield",12),Map.entry("shibari",20),Map.entry("hiasobi",16),Map.entry("dissection",12),Map.entry("reconstruction",30),Map.entry("restructure",18),Map.entry("path",6));
    private SignatureChoreography(){ }
    public static int duration(String kind){return LENGTHS.getOrDefault(kind,0);}
    private static RyujinChoreography.Pose empty(){return new RyujinChoreography.Pose(Vec3.ZERO,Vec3.ZERO,Vec3.ZERO,Vec3.ZERO,Vec3.ZERO,Vec3.ZERO,Vec3.ZERO,0);}
    private static Vec3 v(double x,double y,double z){return new Vec3(x,y,z);}
    private static double smooth(double t){t=Math.max(0,Math.min(1,t));return t*t*(3-2*t);}
    public static RyujinChoreography.Pose sample(String kind,float age,int duration,float sign){
        if(!Float.isFinite(age)||duration<=0||age<=0||age>=duration)return empty();
        double t=duration<=0?1:Math.max(0,Math.min(1,age/duration));
        float w=Float.isFinite(age) && age>0 && age<duration?(float)(smooth(age/1.5)*smooth((duration-age)/2)):0;
        double a=t*Math.PI,roll=Math.sin(a);Vec3 arm=v(-1.2,-.2,-.15),support=v(-.8,.25,.25),body=Vec3.ZERO,leg=Vec3.ZERO,hand=v(-.05,.04,-.08),handRot=Vec3.ZERO;
        switch(kind){
            case "petal_cut","five_cuts"->{double step=kind.equals("five_cuts")?age%12/6.0:t;double sweep=Math.max(0,Math.min(1,step));int cut=kind.equals("five_cuts")?(int)(age/12):0;
                double side=cut%2==0?1:-1;arm=v(-1.25+(cut==4?.65*sweep:0),side*(.9-1.8*sweep),cut==2?-.5:cut==3?.4:-.12);body=v(.04,side*(-.3+.6*sweep),0);leg=v(.16*roll,0,.04);handRot=v(.08,side*(-.6+1.2*sweep),side*(.4-.8*sweep));}
            case "petal_stream","nake"->{double cut=smooth(t/.4);arm=v(-2.1+1.1*cut,.1-.35*cut,-.18);body=v(.14*cut,.2*cut,0);handRot=v(-.55+1.0*cut,0,-.12);}
            case "petal_convergence","shibari"->{arm=v(-1.25,.5-.8*t,-.25);support=v(-1.15,-.45+.7*t,.3);body=v(.06,.2*roll,0);handRot=v(-.1,.3-.6*t,.2);}
            case "petal_guard","shield"->{arm=v(-1.35,-.45,-.35);support=v(-1.1,.25,.3);body=v(-.08,-.18,0);handRot=v(-.15,-.2,.8*roll);}
            case "floral_garden"->{arm=v(-1.9+1.5*smooth(t),.25,-.2);support=v(-1.2,.6,.35);body=v(.18*roll,-.2,0);leg=v(.2*roll,0,.08);handRot=v(-.5+1.0*t,0,-.2);}
            case "floral_bloom","ikka_senjinka","hiasobi"->{arm=v(-1.1+.7*t,.5-.8*t,-.15);support=v(-1.1+.8*t,-.5+.8*t,.2);body=v(.12*roll,.25*roll,0);handRot=v(.15,.4-.8*t,-.3*roll);}
            case "senkei"->{arm=v(-.65,.8,-.5);support=v(-1.15,.45,.6);body=v(0,-.3,0);handRot=v(0,-.4,.65*roll);}
            case "hakuteiken"->{double dash=smooth((age-14)/2);arm=v(-.75-.9*dash,.2,-.1);support=v(-.8,.3,.5);body=v(.05+.3*dash,-.12,0);leg=v(.4*Math.sin(age*.8)*dash,0,.05);handRot=v(-.25-.15*dash,.15,0);}
            case "dissection"->{double cut=smooth(t);arm=v(-.6-.65*cut,.85-1.5*cut,-.3);body=v(.08,-.35+.65*cut,0);handRot=v(.1,-.6+1.1*cut,.4-.8*cut);}
            case "reconstruction"->{arm=v(-1.1,.18,-.25);support=v(-1.3,-.2,.25);body=v(.08,0,0);handRot=v(-.15,.15,.15*Math.sin(age*Math.PI/5));}
            case "restructure"->{arm=v(-.8,.5,-.2);support=v(-1.2,-.4,.35);body=v(-.08*roll,.2*roll,0);leg=v(.12*roll,0,.06);handRot=v(-.1,.3,-.3*roll);}
            case "path"->{arm=v(-1.65,-.2,-.12);support=v(-1.1,.3,.25);body=v(.2,-.15,0);handRot=v(-.3,0,-.18);}
        }
        return pose(arm,support,body,leg,hand,handRot,w,sign);
    }
    private static RyujinChoreography.Pose pose(Vec3 a,Vec3 s,Vec3 b,Vec3 l,Vec3 h,Vec3 r,float w,float sign){
        return new RyujinChoreography.Pose(mirror(a,sign),mirror(s,sign),mirror(b,sign),mirror(l,sign),Vec3.ZERO,h.multiply(sign,1,1),mirror(r,sign),w);
    }
    private static Vec3 mirror(Vec3 v,float sign){return v.multiply(1,sign,sign);}
    public static RyujinChoreography.Pose release(String identity,float age,int duration,float sign){
        if(!Float.isFinite(age)||duration<=0||age<=0||age>=duration)return empty();
        float t=Math.max(0,Math.min(1,age/duration)),w=(float)(smooth(age/4)*smooth((duration-age)/6));
        if(!Float.isFinite(age) || age<=0 || age>=duration)w=0;
        double gather=smooth(t/.64),settle=smooth((t-.72)/.22);
        Vec3 arm,support,body;Vec3 handRot;
        switch(identity){
            case "senbonzakura"->{arm=v(-1.2+1.25*gather,.15,.08);support=v(-1.15,.55,.4);body=v(.10*gather,-.1,0);handRot=v(.2*gather,.2,-.5*gather);}
            case "benihime"->{arm=v(-.55-.65*gather,.65-.85*gather,-.3);support=v(-1.35,-.35+.7*gather,.35);body=v(.06,-.22+.3*gather,0);handRot=v(-.15,.3-.6*gather,.35);}
            default->{arm=v(-1.6+.35*gather,-.15,-.2);support=v(-1.1,.3,.15);body=v(.05,-.1,0);handRot=v(-.3,.1,-.15);}
        }
        return pose(arm.scale(1-settle),support.scale(1-settle),body.scale(1-settle),v(.12*Math.sin(t*Math.PI),0,.04),Vec3.ZERO,handRot.scale(1-settle),w,sign);
    }
}
