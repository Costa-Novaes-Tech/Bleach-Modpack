package com.bleachmod.common.visual;

import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

/** Shared local-space curve. Forward is +Z, units are blocks, no client clock. */
public final class HydraMotion {
    public static final int LINKS=12, ASSAULT_TICKS=156;
    public static final String[] NECKS={"center","left","right"};
    private HydraMotion() { }
    public static double smooth(double t){t=Mth.clamp(t,0,1);return t*t*(3-2*t);}
    public static int impactTick(int head){return 24+head*10;}
    public static double growth(boolean assault,int head,double age){return smooth((age-head*3)/(assault?10:42))*(assault?1-smooth((age-impactTick(head))/12):1);}
    public static double strike(int head,double age){
        return smooth((age-(8+head*10))/16);
    }
    public static Vec3 point(boolean assault,int head,double age,double u,double reach){
        return point(assault,head,age,u,new Vec3(head==0?0:head==1?6:-6,.6,8+reach));
    }
    public static Vec3 point(boolean assault,int head,double age,double u,Vec3 impact){
        double side=head==0?0:head==1?1:-1;
        Vec3 base=new Vec3(side*2.6,.15,0);
        double sway=Math.sin(age*.065-head*.8)*.45;
        Vec3 end=new Vec3(side*5+sway,head==0?16.6:13.7,3.5+Math.sin(age*.05+head)*.6);
        Vec3 c1=new Vec3(-side*3,6.2,-4);
        Vec3 c2=new Vec3(side*12,15.3,-5);
        if(assault){
            double hit=strike(head,age);
            end=end.lerp(impact,hit);
            c1=c1.lerp(new Vec3(side*5,head==0?4:9,(impact.z-8)*.18),hit);
            c2=c2.lerp(new Vec3(side*8,head==2?18:head==1?13:5,8+(impact.z-8)*.8),hit);
        }else if(age>48 && age<106){
            double breath=smooth((age-48)/24)*(1-smooth((age-86)/20));
            end=end.add(0,-breath*7.5,breath*2);
        }
        double v=1-u;
        return base.scale(v*v*v).add(c1.scale(3*v*v*u)).add(c2.scale(3*v*u*u)).add(end.scale(u*u*u));
    }
    public static float jaw(boolean assault,int head,double age){return (float)Math.toRadians(assault?8+30*strike(head,age):8+28*smooth((age-48)/24)*(1-smooth((age-90)/20)));}
}
