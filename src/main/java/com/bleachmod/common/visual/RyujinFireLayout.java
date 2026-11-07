package com.bleachmod.common.visual;

import net.minecraft.world.phys.Vec3;
import java.util.ArrayList;
import java.util.List;

/** Pure, bounded staging geometry. Phase controls lifecycle; real ticks drive turbulence. */
public final class RyujinFireLayout {
    public static final int CARD_LIMIT=288;
    public enum Detail { LOW(3,1.65), MEDIUM(2,1.3), HIGH(1,1);
        final int stride;final double width;
        Detail(int stride,double width){this.stride=stride;this.width=width;}
    }
    public enum Layer { BLADE, BASE, CURTAIN, CORONA, ABSORPTION, BACKDROP, LATERAL, SMOKE }
    public record Card(Layer layer,Vec3 at,double width,double height,int alpha,boolean alternate,int seed) { }
    private RyujinFireLayout() { }
    private static double noise(int i){double v=Math.sin(i*127.1+311.7)*43758.5453;return v-Math.floor(v);}
    private static void add(List<Card> cards,Layer layer,Vec3 at,double w,double h,double alpha,int i){
        if(cards.size()<CARD_LIMIT && w>.005 && h>.005 && alpha>3)
            cards.add(new Card(layer,at,w,h,(int)Math.min(255,alpha),i%2==0,i+layer.ordinal()*233));
    }
    public static List<Card> sample(Vec3 anchor,Vec3 forward,double phase,double realTick,RyujinBankaiPresentation.Blade blade,Detail detail){
        var full=sample(anchor,forward,phase,realTick,blade);
        if(detail==Detail.HIGH)return full;
        var reduced=new ArrayList<Card>();int index=0;
        for(var c:full)if(index++%detail.stride==0)
            reduced.add(new Card(c.layer,c.at,c.width*(c.layer==Layer.SMOKE?1:detail.width),c.height,c.alpha,reduced.size()%2==0,c.seed));
        return reduced;
    }
    public static List<Card> sample(Vec3 anchor,Vec3 forward,double phase,double realTick,RyujinBankaiPresentation.Blade blade){
        return sample(anchor,forward,phase,realTick,blade,RyujinSealFlow.enabled(),
                !Boolean.getBoolean("bleachmod.ryujinQaNoBackdropSmoke"));
    }

    public static List<Card> sample(Vec3 anchor,Vec3 forward,double phase,double realTick,
                                    RyujinBankaiPresentation.Blade blade,boolean pilot){
        return sample(anchor,forward,phase,realTick,blade,pilot,true);
    }

    public static List<Card> sample(Vec3 anchor,Vec3 forward,double phase,double realTick,
                                    RyujinBankaiPresentation.Blade blade,boolean pilot,boolean smoke){
        var cards=new ArrayList<Card>(CARD_LIMIT);
        if(!Double.isFinite(phase)||!Double.isFinite(realTick)||phase<0||phase>=RyujinBankaiTimeline.ABSORPTION_END)return cards;
        Vec3 side=new Vec3(forward.z,0,-forward.x);
        double ignition=RyujinBankaiPresentation.ignition(phase);
        double drain=1-RyujinBankaiTimeline.progress(phase,32,52);
        double wall=RyujinBankaiTimeline.progress(phase,10,20)*drain;
        double burst=RyujinBankaiTimeline.progress(phase,20,23)*(1-RyujinBankaiTimeline.progress(phase,26,30));
        double contract=RyujinBankaiTimeline.progress(phase,32,52);
        Vec3 target=blade==null?anchor.add(0,1,0):blade.hand().lerp(blade.tip(),.55);

        // Two interleaved tongues coat the blade; keep the guard/hand readable.
        if(blade!=null)for(int i=0;i<36;i++){
            double p=.12+.86*(i/2.0)/18;
            double grip=Math.min(1,p/.34);
            double h=ignition*(.38+.48*noise(i+23))*(.86+.14*Math.sin(realTick*.22+i*1.6))*grip;
            Vec3 at=blade.hand().lerp(blade.tip(),p).add(side.scale((i%2==0?1:-1)*.045)).add(0,h*.46,0);
            add(cards,Layer.BLADE,at,.16+.09*noise(i+43),h,230*ignition,i);
        }

        // Grounded base. The sprite's bottom, rather than its centre, touches the floor.
        for(int i=0;i<40;i++){
            if(pilot && i%4==1)continue;
            double theta=i*Math.PI*2/40, front=Math.cos(theta), r=1.1+2.0*noise(i+81);
            boolean nearFront=front>0;
            double h=ignition*(nearFront?.3+.5*noise(i+92):.8+1.3*noise(i+105));
            h*=.82+.18*Math.sin(realTick*.17+i*1.3);
            Vec3 at=anchor.add(side.scale(Math.sin(theta)*r)).add(forward.scale(front*r)).add(0,.04+h*.5,0);
            add(cards,Layer.BASE,at,(nearFront?.3:.65)*ignition,h,(nearFront?140:210)*ignition,i);
        }
        if(pilot && phase>20)addLateralFire(cards,anchor,forward,side,phase,realTick,drain,burst);

        // 18 columns, three depths and two overlapping tiers: a sustained rear mass.
        int curtainCount=pilot?54:108;
        for(int i=0;i<curtainCount;i++){
            if(pilot && smoke && i%9==4)continue;
            int column=i%18, depth=(i/18)%3, tier=i/54;
            double x=(column/17.0*2-1)*6.8+.9*(noise(i+77)-.5)+.16*Math.sin(realTick*.11+i);
            double edge=1-.6*Math.pow(Math.min(1,Math.abs(x)/7),2.5);
            double d=3.0+depth*1.45+.8*(1-Math.min(1,Math.abs(x)/7))+.5*noise(i+14);
            double h=(2.6+2.2*noise(i+55))*(.86+.14*Math.sin(realTick*.13+i*1.73))*(1+.35*burst)*edge;
            if(pilot){
                // Stable groups breathe at different rates instead of identical columns.
                double group=(i%18)/3;
                h*=.80+.20*Math.sin(realTick*.083+group*1.91+depth*.7);
                if(i%9==0)h*=1.22;
            }
            double floor=tier==0?.02:(1.8+1.0*noise(i+113))*edge;
            Vec3 at=anchor.add(side.scale(x)).subtract(forward.scale(d)).add(0,(floor+h*.5)*wall,0);
            at=at.lerp(target,contract*.85);
            add(cards,Layer.CURTAIN,at,(.65+.55*noise(i+31))*wall,h*wall,(125+65*noise(i+9))*wall,i);
        }
        if(pilot && phase>20) addBackdrop(cards,anchor,forward,side,phase,realTick,drain,contract);
        if(pilot && smoke && phase>21)addSmoke(cards,anchor,forward,side,phase,realTick,drain);

        // Corona follows the model, with real-time motion even during the scenic hold.
        Vec3 centre=(pilot||RyujinSolarPose.pilot())?RyujinSolarPose.center(anchor,forward,phase,blade):RyujinBankaiPresentation.sunCenter(anchor,forward);
        double radius=RyujinBankaiTimeline.sunRadius(phase);
        if(radius>.08)for(int i=0;i<48;i++){
            double a=i*Math.PI*2/48+realTick*.003;
            double r=radius*(.82+.06*Math.sin(realTick*.07+i));
            Vec3 at=centre.add(side.scale(Math.cos(a)*r)).add(0,Math.sin(a)*r*.85,0)
                    .add(forward.scale(Math.sin(i*1.9)*radius*.13));
            add(cards,Layer.CORONA,at,radius*.16,radius*(.25+.10*noise(i+4)),190,i);
        }

        // Directed streams replace the old tall body-centred vortex during absorption.
        double absorption=blade==null?0:RyujinBankaiTimeline.absorption(phase);
        if(!pilot&&absorption>.01)for(int i=0;i<32;i++){
            int stream=i/8;
            double t=((i%8)+((realTick*.18+stream*.23)%1))/8;
            double angle=stream*Math.PI*.5;
            Vec3 from=centre.add(side.scale(Math.cos(angle)*Math.max(.3,radius*.7)))
                    .add(0,Math.sin(angle)*Math.max(.3,radius*.7),0);
            Vec3 end=blade.hand().lerp(blade.tip(),.3+.18*stream);
            Vec3 at=from.lerp(end,t).add(side.scale(Math.sin(t*10+realTick*.1)*.15*(1-t)));
            add(cards,Layer.ABSORPTION,at,.34*absorption,.9*absorption,220*absorption,i);
        }
        return cards;
    }

    private static void addLateralFire(List<Card> cards,Vec3 anchor,Vec3 forward,Vec3 side,
                                       double phase,double tick,double drain,double burst){
        for(int i=0;i<10;i++){
            int group=i%5;
            double sign=i<5?-1:1;
            double strength=RyujinBankaiTimeline.progress(phase,20+.3*group,23+.3*group)*drain;
            double flicker=.82+.18*Math.sin(tick*.22+i*1.37);
            double h=(.60+.38*noise(i+31)+.48*burst)*strength*flicker;
            Vec3 at=anchor.add(side.scale(sign*(2.7+.59*group+.34*noise(i+8))))
                    .subtract(forward.scale(-.25+.70*group+.37*noise(i+14))).add(0,.04+h*.5,0);
            add(cards,Layer.LATERAL,at,(.82+.42*noise(i+71))*strength,h,(210+37*burst)*strength,i);
        }
    }

    private static void addSmoke(List<Card> cards,Vec3 anchor,Vec3 forward,Vec3 side,
                                 double phase,double tick,double drain){
        for(int i=0;i<6;i++){
            double x=(i<3?-1:1)*(11+(i%3)*5.8+.8*Math.sin(tick*.037+i));
            double opacity=RyujinBankaiTimeline.progress(phase,21+i*.26,27+i*.26)*drain;
            double height=4.4+2*noise(i+64)+.35*Math.sin(tick*.058+i);
            Vec3 at=anchor.add(side.scale(x)).subtract(forward.scale(16+noise(i+10)*2.6))
                    .add(0,8.3+height*.34+noise(i+5)*1.4,0);
            add(cards,Layer.SMOKE,at,6+3*noise(i+82),height,(100+48*noise(i+18))*opacity,i);
        }
    }

    private static void addBackdrop(List<Card> cards,Vec3 anchor,Vec3 forward,Vec3 side,
                                    double phase,double realTick,double drain,double contract){
        int[] counts={24,18,12};
        double[] spans={25,22,19};
        for(int layer=0;layer<counts.length;layer++){
            int count=counts[layer];
            for(int column=0;column<count;column++){
                int seed=layer*31+column;
                double x=((2.0*column/(count-1)-1)*spans[layer]+(noise(seed+7)-.5)*2)
                        *(1-.20*contract);
                double start=20+1.7*noise(seed+19);
                double intensity=RyujinBankaiTimeline.progress(phase,start,start+7)*drain;
                double breath=.88+.12*Math.sin(realTick*.11+noise(seed+27)*6);
                double height=(4.1+layer*2.15+noise(seed+15)*(3.5+layer))*breath*intensity;
                double depth=12.4+(2-layer)*1.55+noise(seed+39)*1.25-1.05*contract;
                double width=(5.8-layer*.85+noise(seed+46)*1.65)*(.72+.28*intensity);
                double alpha=(97+layer*14+noise(seed+55)*68)*intensity;
                Vec3 at=anchor.add(side.scale(x)).subtract(forward.scale(depth)).add(0,.02+height*.5,0);
                add(cards,Layer.BACKDROP,at,width,height,alpha,seed);
            }
        }
    }
}
