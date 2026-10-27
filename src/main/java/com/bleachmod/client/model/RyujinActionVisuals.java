package com.bleachmod.client.model;

import com.bleachmod.Reference;
import com.bleachmod.common.visual.RyujinChoreography;
import com.bleachmod.common.data.PlayerCapability;
import com.bleachmod.init.ModItems;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.kosmx.playerAnim.api.TransformType;
import dev.kosmx.playerAnim.core.util.Vec3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.particles.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.*;
import net.minecraftforge.fml.common.Mod;
import java.util.*;

/** Native flame particles, not filled slash meshes. Render work never applies damage/motion. */
@Mod.EventBusSubscriber(modid=Reference.MOD_ID,value=Dist.CLIENT)
public final class RyujinActionVisuals {
    private static final Set<PoseStack> SPINS=Collections.newSetFromMap(new IdentityHashMap<>());
    private static final Map<PoseStack,Double> EMERGENCE=new IdentityHashMap<>();
    private record Trail(long cast, Vec3 position) { }
    private static final Map<Integer,Trail> TRAILS=new HashMap<>();
    private static long budgetTick=Long.MIN_VALUE;
    private static int budget;
    private static final Map<Integer,Long> RELEASE_TICKS=new HashMap<>();
    private RyujinActionVisuals(){ }
    public static boolean ashSoldier(int id){return ElementalActionVisuals.VIEWS.values().stream().flatMap(List::stream)
        .anyMatch(v->v.kind().equals("minami") && v.soldiers().contains(id));}
    public static void clear(){SPINS.clear();EMERGENCE.clear();RELEASE_TICKS.clear();TRAILS.clear();budgetTick=Long.MIN_VALUE;budget=0;}
    private static void budget(){var mc=Minecraft.getInstance();if(mc.level!=null && budgetTick!=mc.level.getGameTime()){budgetTick=mc.level.getGameTime();budget=640;}}
    private static Vec3f mix(Vec3f from,Vec3 to,float w){return new Vec3f(Mth.lerp(w,from.getX(),(float)to.x),Mth.lerp(w,from.getY(),(float)to.y),Mth.lerp(w,from.getZ(),(float)to.z));}
    public static Vec3f transform(AbstractClientPlayer p,RyujinChoreography.Pose pose,String part,TransformType type,Vec3f value){
        float sign=p.getMainArm()==HumanoidArm.RIGHT?1:-1,w=pose.weight();
        boolean arm=part.equals(sign>0?"rightArm":"leftArm"),support=part.equals(sign>0?"leftArm":"rightArm");
        boolean item=part.equals(sign>0?"rightItem":"leftItem");
        if(support && !p.getOffhandItem().isEmpty())return value;
        if(type==TransformType.ROTATION){
            if(arm)return mix(value,pose.arm(),w);
            if(support)return mix(value,pose.support(),w);
            if(part.equals("body"))return mix(value,pose.body(),w);
            if(part.equals(sign>0?"rightLeg":"leftLeg"))return mix(value,pose.leg(),w);
            if(part.equals(sign>0?"leftLeg":"rightLeg"))return mix(value,pose.leg().multiply(-1,1,-1),w);
            if(item){
                Vec3 r = pose.item().lengthSqr() > 1e-6 ? pose.item() : pose.handRotation();
                return mix(value, r, w);
            }
        }
        if(type==TransformType.BEND && (arm||support))return mix(value,new Vec3(0,arm && Math.abs(pose.arm().x)>2?.35:.1,0),w);
        if(type==TransformType.POSITION){
            if(item){
                Vec3 pos = pose.handPosition();
                return mix(value, pos, w);
            }
            if(part.equals("body"))return new Vec3f(value.getX(),value.getY()+Math.abs((float)pose.leg().x)*.8F*w,value.getZ());
        }
        return value;
    }
    public static Vec3f steamTransform(AbstractClientPlayer p,RyujinChoreography.Pose pose,String part,TransformType type,Vec3f value){
        // Keep straight wrists on the shared grip; no shoulder-position teleport or tilted attack plane.
        if(type==TransformType.BEND && (part.equals("rightArm") || part.equals("leftArm")))return mix(value,Vec3.ZERO,pose.weight());
        var result=transform(p,pose,part,type,value);
        // Ryujin's authored left-hand item display has a20-degree basis difference.
        // Compensate on this local item bone only, leaving all other attacks/models intact.
        if(p.getMainArm()==HumanoidArm.LEFT && part.equals("leftItem") && type==TransformType.ROTATION)
            return new Vec3f(result.getX()+(float)Math.toRadians(20)*pose.weight(),result.getY(),result.getZ());
        return result;
    }
    static void hand(PoseStack stack,RyujinChoreography.Pose pose){
        float w=pose.weight();Vec3 pos=pose.handPosition().scale(w),r=pose.handRotation().scale(w);
        stack.translate(pos.x,pos.y,pos.z);stack.mulPose(Axis.XP.rotation((float)r.x));stack.mulPose(Axis.YP.rotation((float)r.y));stack.mulPose(Axis.ZP.rotation((float)r.z));
    }
    private static final class Emitter {
        final Minecraft mc=Minecraft.getInstance();final int stride;int samples,remaining;
        Emitter(boolean far){stride=mc.options.particles().get()==net.minecraft.client.ParticleStatus.MINIMAL?4:mc.options.particles().get()==net.minecraft.client.ParticleStatus.DECREASED||far?2:1;remaining=128;}
        void particle(SimpleParticleType type,Vec3 at,Vec3 velocity){
            if(samples++%stride!=0 || remaining<=0 || budget<=0 || mc.level==null)return;
            remaining--;budget--;mc.level.addParticle(type,at.x,at.y,at.z,velocity.x,velocity.y,velocity.z);
        }
        void fire(Vec3 at,int i){particle(i%3==0?ParticleTypes.FLAME:ParticleTypes.SMALL_FLAME,at,new Vec3(0,.025+(i%4)*.007,0));}
        void smoke(Vec3 at){particle(ParticleTypes.SMOKE,at,new Vec3(0,.025,0));}
    }
    private static boolean equipped(AbstractClientPlayer p){return p.isAlive() && !p.isSpectator() && p.getMainHandItem().is(ModItems.RYUJIN_JAKKA.get()) && PlayerCapability.get(p).map(d->d.getCharacter().getZanpakutoIdentity().equals("ryujin_jakka")).orElse(false);}
    static Vec3 world(Vec3 origin,Vec3 forward,Vec3 local){Vec3 right=new Vec3(-forward.z,0,forward.x);return origin.add(right.scale(local.x)).add(0,local.y,0).add(forward.scale(local.z));}
    private static void blade(Emitter e,AbstractClientPlayer p,Vec3 forward,RyujinChoreography.Pose pose,int age,boolean hot){
        float sign=p.getMainArm()==HumanoidArm.RIGHT?1:-1;
        Vec3 hand=world(p.position(),forward,RyujinChoreography.bladeHand(pose,sign));Vec3 dir=world(Vec3.ZERO,forward,RyujinChoreography.bladeDirection(pose));
        for(int i=0;i<12;i++){var q=hand.add(dir.scale(.12+i*.11));e.fire(q,i);if(hot && i%3==0)e.particle(ParticleTypes.ELECTRIC_SPARK,q,new Vec3(0,.005,0));}
        if(age%3==0)e.smoke(hand.add(dir.scale(1.2)));
    }
    private static void particles(AbstractClientPlayer p,ElementalActionVisuals.View v,int age,Emitter e){
        Vec3 f=v.forward(),right=new Vec3(-f.z,0,f.x),origin=v.origin();
        var pose=RyujinChoreography.attack(v.kind(),age,p.getMainArm()==HumanoidArm.RIGHT?1:-1);
        if(!v.kind().startsWith("sealed_") && !v.kind().equals("steam_cut") && age<v.gesture() && pose.weight()>.1)blade(e,p,Vec3.directionFromRotation(0,p.getYRot()),pose,age,v.kind().equals("higashi"));
        switch(v.kind()){
            case "flame_barrage"->{if(age<6 || age>14)return;var head=origin.add(f.scale(age-6));
                for(int i=0;i<65;i++){var local=com.bleachmod.common.visual.RyujinFlamePresentation.crest(i,age);
                    var q=head.add(right.scale(local.x)).add(0,local.y,0).add(f.scale(local.z));
                    e.particle(i%4==0?ParticleTypes.FLAME:ParticleTypes.SMALL_FLAME,q,f.scale(.04).add(0,.025+(i%4)*.012,0));}
                if(age%2==0)for(int i=0;i<6;i++)e.particle(ParticleTypes.ASH,head.add(right.scale((i-2.5)*.28)).add(0,.3,0),new Vec3(0,.018,0));
                e.smoke(head.add(right.scale(1.1)).add(0,.4,0));e.smoke(head.add(right.scale(-1.1)).add(0,.4,0));
            }
            case "flame_fan"->{if(age<6 || age>20)return;double a=Math.toRadians(-70+(age-6)*10);
                for(int i=0;i<92;i++){double r=.8+(i%23)*.6;var dir=f.yRot((float)(a-(i/23)*.04));var q=origin.add(dir.scale(r)).add(0,.7+(i%4)*.17,0);e.particle(i%3==0?ParticleTypes.FLAME:ParticleTypes.SMALL_FLAME,q,dir.scale(.035).add(0,.025,0));}
            }
            case "jokaku"->{double grow=Mth.clamp(age/20.0,0,1);
                for(int i=0;i<104;i++){double a=(i%26)*Math.PI*2/26+Math.sin(age*.06)*.02,y=((i/26)*1.65+(age%4)*.3)*grow,r=6*Math.sqrt(Math.max(0,1-y*y/49));var q=origin.add(Math.cos(a)*r,y+.1,Math.sin(a)*r);e.fire(q,i);}
                for(int i=0;i<12;i++){double y=(6+i%3*.45)*grow,r=6*Math.sqrt(Math.max(0,1-y*y/49)),a=i*Math.PI/6;e.fire(origin.add(Math.cos(a)*r,y,Math.sin(a)*r),i);}
                if(age%4==0)for(int i=0;i<8;i++){double a=i*Math.PI/4;e.smoke(origin.add(Math.cos(a)*5.2,4.7*grow,Math.sin(a)*5.2));}
            }
            case "higashi"->{if(age<10 || age>16)return;
                float sign=p.getMainArm()==HumanoidArm.RIGHT?1:-1;
                // Sample the authored blade, not a second diagonal disconnected from the wrist.
                for(int trail=0;trail<3;trail++){
                    var stroke=RyujinChoreography.attack("higashi",age-trail*.25F,sign);
                    Vec3 hand=world(p.position(),f,RyujinChoreography.bladeHand(stroke,sign));
                    Vec3 direction=world(Vec3.ZERO,f,RyujinChoreography.bladeDirection(stroke));
                    for(int i=0;i<10;i++){
                        Vec3 q=hand.add(direction.scale(.15+i*.12));
                        if(i%8==0)e.particle(ParticleTypes.ELECTRIC_SPARK,q,new Vec3(0,.012,0));else e.fire(q,i);
                    }
                }
            }
            case "steam_cut"->{
                if(age<36){
                    // Embers converge on the actual weapon; avoid a flame wall at the camera.
                    float sign=p.getMainArm()==HumanoidArm.RIGHT?1:-1;
                    Vec3 hand=world(p.position(),f,RyujinChoreography.bladeHand(pose,sign));
                    Vec3 dir=world(Vec3.ZERO,f,RyujinChoreography.bladeDirection(pose));
                    if(age%2==0)for(int i=0;i<6;i++){
                        double a=i*Math.PI/3+age*.12;Vec3 q=hand.add(dir.scale(.3+i*.15));
                        Vec3 from=q.add(Math.cos(a)*.65,.15,Math.sin(a)*.65);
                        e.particle(i%3==0?ParticleTypes.SMALL_FLAME:ParticleTypes.ASH,from,q.subtract(from).scale(.15));
                    }
                }else if(age<=43){
                    for(int trail=0;trail<2;trail++){
                        var cut=RyujinChoreography.attack("steam_cut",age-trail*.4F,p.getMainArm()==HumanoidArm.RIGHT?1:-1);
                        Vec3 grip=world(p.position(),f,RyujinChoreography.bladeHand(cut,p.getMainArm()==HumanoidArm.RIGHT?1:-1));
                        Vec3 direction=world(Vec3.ZERO,f,RyujinChoreography.bladeDirection(cut));
                        for(int sample=0;sample<6;sample++)e.particle(sample%2==0?ParticleTypes.SMALL_FLAME:ParticleTypes.ELECTRIC_SPARK,grip.add(direction.scale(.18+sample*.22)),new Vec3(0,.015,0));
                    }
                    if(age==40){
                        Vec3 hand=world(p.position(),f,RyujinChoreography.bladeHand(pose,p.getMainArm()==HumanoidArm.RIGHT?1:-1));
                        Vec3 tip=hand.add(world(Vec3.ZERO,f,RyujinChoreography.bladeDirection(pose)).scale(1.35));
                        for(int i=0;i<16;i++){
                            double a=i*Math.PI/8;Vec3 d=new Vec3(Math.cos(a),0,Math.sin(a));
                            e.particle(ParticleTypes.SMALL_FLAME,tip.add(d.scale(.25)),d.scale(.13).add(0,.04,0));
                            if(i%4==0)e.smoke(tip.add(d.scale(.4)));
                        }
                    }
                }
            }
            case "minami"->{if(age<10 || age>46)return;
                for(int j=0;j<10;j++){var entity=j<v.soldiers().size()?e.mc.level.getEntity(v.soldiers().get(j)):null;
                    var at=entity!=null?entity.position():origin.subtract(f.scale(1.5+j/5*1.5)).add(right.scale((j%5-2)*1.25));
                    for(int i=0;i<9;i++){double q=i*Math.PI*2/9+age*.08;e.fire(at.add(Math.cos(q)*.45,.08,Math.sin(q)*.45),i);}
                    if(age%3==0)e.particle(ParticleTypes.ASH,at.add(0,.8,0),new Vec3(0,.035,0));
                }
            }
            case "flame_dash"->{if(age>=16)return;
                var previous=TRAILS.put(p.getId(),new Trail(v.start(),p.position()));
                if(previous==null || previous.cast()!=v.start())return;
                var samples=com.bleachmod.common.visual.RyujinFlamePresentation.trail(previous.position(),p.position());
                Vec3 delta=p.position().subtract(previous.position()),side=new Vec3(-delta.z,0,delta.x).normalize();
                for(int i=0;i<samples.size();i++)for(int j=0;j<3;j++){
                    var q=samples.get(i).add(side.scale((j-1)*.22)).add(0,.18+(i%3)*.12,0);e.fire(q,i+j);
                    if(j==1 && i%3==0)e.particle(ParticleTypes.ASH,q.add(0,.25,0),new Vec3(0,.025,0));
                }
            }
            // The authoritative executor supplies the long-range Vapor and the persistent Tornado.
        }
    }
    @SubscribeEvent public static void tick(TickEvent.ClientTickEvent event){
        if(event.phase!=TickEvent.Phase.END)return;var mc=Minecraft.getInstance();if(mc.level==null || mc.player==null){clear();return;}
        if(mc.isPaused())return;if(ElementalActionVisuals.world!=mc.level){ElementalActionVisuals.clear();return;}budget();
        TRAILS.keySet().removeIf(id->!(mc.level.getEntity(id) instanceof AbstractClientPlayer p)||!equipped(p)
                ||mc.player.distanceToSqr(p)>64*64||ElementalActionVisuals.VIEWS.getOrDefault(id,List.of()).stream()
                .noneMatch(v->v.kind().equals("flame_dash")&&ElementalActionVisuals.age(v,0)>=0&&ElementalActionVisuals.age(v,0)<16));
        var owners=new ArrayList<>(ElementalActionVisuals.VIEWS.entrySet());owners.sort(Comparator.comparingInt(a->a.getKey()==mc.player.getId()?0:1));
        for(var owner:owners){if(!(mc.level.getEntity(owner.getKey()) instanceof AbstractClientPlayer p) || !equipped(p) || mc.player.distanceToSqr(p)>64*64)continue;
            for(var v:owner.getValue()){int age=(int)ElementalActionVisuals.age(v,0);if(v.ice() || age<0 || age>=v.duration())continue;
                particles(p,v,age,new Emitter(mc.player.distanceToSqr(p)>32*32));}
        }
    }
    @SubscribeEvent(priority=EventPriority.LOW) public static void dashSpin(RenderLivingEvent.Pre<?,?> event){
        if(!(event.getEntity() instanceof AbstractClientPlayer p)||!equipped(p))return;
        var view=ElementalActionVisuals.VIEWS.getOrDefault(p.getId(),List.of()).stream().filter(v->v.kind().equals("flame_dash")&&ElementalActionVisuals.age(v,0)<16).findFirst().orElse(null);
        if(view==null)return;double age=ElementalActionVisuals.age(view,event.getPartialTick());Vec3 f=p.getDeltaMovement().lengthSqr()>.01?p.getDeltaMovement().normalize():view.forward();
        var stack=event.getPoseStack();stack.pushPose();SPINS.add(stack);
        stack.translate(0,.9,0);
        stack.mulPose(new org.joml.Quaternionf().rotationAxis((float)(age*Math.PI/3),(float)f.x,(float)f.y,(float)f.z));
        stack.mulPose(com.bleachmod.common.visual.RyujinFlamePresentation.dashTilt(f));
        stack.translate(0,-.9,0);
    }
    @SubscribeEvent(priority=EventPriority.LOWEST) public static void dashSpinEnd(RenderLivingEvent.Post<?,?> event){if(SPINS.remove(event.getPoseStack()))event.getPoseStack().popPose();}
    @SubscribeEvent(priority=EventPriority.LOWEST) public static void emergence(RenderLivingEvent.Pre<?,?> event){
        if(!(event.getEntity() instanceof WitherSkeleton) || ElementalActionVisuals.world!=Minecraft.getInstance().level)return;
        for(var list:ElementalActionVisuals.VIEWS.values())for(var v:list){if(v.ice() || !v.kind().equals("minami"))continue;int i=v.soldiers().indexOf(event.getEntity().getId());if(i<0)continue;
            double age=ElementalActionVisuals.age(v,event.getPartialTick()),rise=Mth.clamp((age-16-i*1.5)/8,0,1),offset=2.5*(1-rise);
            if(offset>0 && EMERGENCE.size()<16){event.getPoseStack().translate(0,-offset,0);EMERGENCE.put(event.getPoseStack(),offset);}return;
        }
    }
    @SubscribeEvent(priority=EventPriority.LOWEST) public static void emergenceEnd(RenderLivingEvent.Post<?,?> event){var offset=EMERGENCE.remove(event.getPoseStack());if(offset!=null)event.getPoseStack().translate(0,offset,0);}
    static void releaseParticles(AbstractClientPlayer p,float age){
        var mc=Minecraft.getInstance();if(mc.level==null || mc.isPaused())return;long now=mc.level.getGameTime();
        if(RELEASE_TICKS.getOrDefault(p.getId(),Long.MIN_VALUE)==now)return;
        RELEASE_TICKS.entrySet().removeIf(e->e.getValue()<now-65);if(RELEASE_TICKS.size()>=24 && !RELEASE_TICKS.containsKey(p.getId()))return;RELEASE_TICKS.put(p.getId(),now);budget();
        var e=new Emitter(mc.player!=null && mc.player.distanceToSqr(p)>32*32);float sign=p.getMainArm()==HumanoidArm.RIGHT?1:-1;
        Vec3 f=Vec3.directionFromRotation(0,p.getYRot());var pose=RyujinChoreography.release(age,sign);
        Vec3 hand=world(p.position(),f,RyujinChoreography.bladeHand(pose,sign)),dir=world(Vec3.ZERO,f,RyujinChoreography.bladeDirection(pose));
        if(age<40){double r=3.8*(1-age/40)+.25;
            for(int i=0;i<64;i++){double a=i*Math.PI*2/32+age*.13;Vec3 at=p.position().add(Math.cos(a)*r,.25+(i%8)*.24,Math.sin(a)*r),goal=hand.add(dir.scale((i%12)*.10));e.particle(i%3==0?ParticleTypes.FLAME:ParticleTypes.SMALL_FLAME,at,goal.subtract(at).scale(.18));}
            blade(e,p,f,pose,(int)age,false);
        }else if(age<44){
            for(int i=0;i<32;i++){double a=i*Math.PI/16;Vec3 velocity=new Vec3(Math.cos(a)*.18,.015,Math.sin(a)*.18);e.particle(ParticleTypes.SMALL_FLAME,p.position().add(0,.08,0).add(velocity),velocity);}
            blade(e,p,f,pose,(int)age,true);
        }else if(age<56){for(int i=0;i<8;i++){var at=hand.add(dir.scale(i*.16));e.smoke(at);if(i%3==0)e.particle(ParticleTypes.ASH,at,new Vec3(0,.018,0));}}
    }
}
