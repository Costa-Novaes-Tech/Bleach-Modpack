package com.bleachmod.client.model;

import com.bleachmod.Reference;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import dev.kosmx.playerAnim.api.TransformType;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.core.util.Vec3f;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationFactory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.nbt.*;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import java.util.*;

/** Server-driven gestures; fire uses native particles, ice keeps its existing constructs. */
@Mod.EventBusSubscriber(modid=Reference.MOD_ID,value=Dist.CLIENT)
public final class ElementalActionVisuals {
    public record View(String kind,Vec3 origin,Vec3 forward,double radius,int duration,int gesture,long start,boolean ice,List<Integer> soldiers,List<Integer> targets,List<Vec3> crystals){ }
    static final Map<Integer,List<View>> VIEWS=new LinkedHashMap<>();
    static net.minecraft.client.multiplayer.ClientLevel world;
    private ElementalActionVisuals(){ }
    public static void init(){PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(Reference.id("elemental_action"),3500,Animation::new);}
    public static void clear(){VIEWS.clear();world=null;RyujinActionVisuals.clear();}
    public static void accept(int owner,CompoundTag tag){var level=Minecraft.getInstance().level;if(level==null)return;if(world!=level){clear();world=level;}
        var result=new ArrayList<View>();var entries=tag.getList("casts",Tag.TAG_COMPOUND);
        for(int i=0;i<Math.min(4,entries.size());i++){var t=entries.getCompound(i);String kind=t.getString("kind");int duration=t.getInt("duration"),gesture=t.getInt("gesture"),age=t.getInt("age");
            var origin=new Vec3(t.getDouble("x"),t.getDouble("y"),t.getDouble("z"));var forward=new Vec3(t.getDouble("dx"),0,t.getDouble("dz"));double radius=t.getDouble("radius");
            if(!List.of("front_cut","wide_cut","sealed_cut","sealed_sweep","higashi","flame_barrage","flame_fan","flame_tornado","jokaku","minami","steam_cut","crescent_chain","ryusenka","sennen","duel_control","glacial_zone","ice_dragon","flame_dash","hydra_assault","ice_eruption","frost_armor").contains(kind)
                    || duration<1 || duration>200 || age<0 || age>=duration || gesture<0 || gesture>duration || !Double.isFinite(radius) || radius<0 || radius>100
                    || !Double.isFinite(origin.x+origin.y+origin.z) || !Double.isFinite(forward.x+forward.z) || Math.abs(origin.x)>3e7 || Math.abs(origin.z)>3e7 || Math.abs(origin.y)>4096 || forward.lengthSqr()<.9 || forward.lengthSqr()>1.1)continue;
            var crystals=new ArrayList<Vec3>();var points=t.getList("crystals",Tag.TAG_COMPOUND);
            for(int j=0;j<Math.min(40,points.size());j++){var q=points.getCompound(j);var at=new Vec3(q.getDouble("x"),q.getDouble("y"),q.getDouble("z"));if(Double.isFinite(at.lengthSqr()) && at.distanceToSqr(origin)<40*40)crystals.add(at);}
            result.add(new View(kind,origin,forward.normalize(),radius,duration,gesture,level.getGameTime()-age,t.getBoolean("ice"),Arrays.stream(t.getIntArray("soldiers")).limit(10).filter(id->id>0).distinct().boxed().toList(),Arrays.stream(t.getIntArray("targets")).limit(16).filter(id->id>0).distinct().boxed().toList(),crystals));
        }
        if(result.isEmpty())VIEWS.remove(owner);else{if(VIEWS.size()>=24 && !VIEWS.containsKey(owner))VIEWS.remove(VIEWS.keySet().iterator().next());VIEWS.put(owner,result);}
    }
    static float age(View v,float partial){return world==null?999:world.getGameTime()-v.start+partial;}
    public static View gesture(AbstractClientPlayer p){if(world!=p.level() || !p.isAlive() || HigashiExecutionVisuals.isPerformingExecution(p.getId()))return null;for(var v:VIEWS.getOrDefault(p.getId(),List.of()))if(age(v,0)<v.gesture)return v;return null;}
    public static boolean busy(AbstractClientPlayer p){return gesture(p)!=null;}
    private static Vec3f mix(Vec3f a,float x,float y,float z,float w){return new Vec3f(Mth.lerp(w,a.getX(),x),Mth.lerp(w,a.getY(),y),Mth.lerp(w,a.getZ(),z));}
    private static final class Animation implements IAnimation {
        private final AbstractClientPlayer p;Animation(AbstractClientPlayer p){this.p=p;}
        @Override public boolean isActive(){return busy(p);}
        @Override public void setupAnim(float partial){ }
        @Override public FirstPersonMode getFirstPersonMode(float partial){return FirstPersonMode.VANILLA;}
        @Override public Vec3f get3DTransform(String part,TransformType type,float partial,Vec3f value){var v=gesture(p);if(v==null)return value;
            if(!v.ice && v.kind.equals("higashi") && HigashiBetterCombatMotion.available())return HigashiBetterCombatMotion.transform(p,age(v,partial),false,v.gesture,part,type,value);
            if(!v.ice && v.kind.equals("steam_cut"))return RyujinActionVisuals.steamTransform(p,com.bleachmod.common.visual.RyujinChoreography.attack(v.kind,age(v,partial),p.getMainArm()==HumanoidArm.RIGHT?1:-1),part,type,value);
            if(!v.ice)return RyujinActionVisuals.transform(p,com.bleachmod.common.visual.RyujinChoreography.attack(v.kind,age(v,partial),p.getMainArm()==HumanoidArm.RIGHT?1:-1),part,type,value);
            float age = age(v, partial);
            float t=Mth.clamp(age/v.gesture,0,1),w=Math.min(1,t*6)*Math.min(1,(1-t)*6),sign=p.getMainArm()==HumanoidArm.RIGHT?1:-1;
            boolean sword=part.equals(sign>0?"rightArm":"leftArm");boolean support=part.equals(sign>0?"leftArm":"rightArm");

            if (v.kind.equals("ryusenka")) {
                // Dedicated full-body Ryusenka animation: attack stance with left knee flexed forward,
                // two-handed katana grip aiming at target, followed by explosive dash thrust.
                float wR = age < 2.0F ? age / 2.0F : age <= 20.0F ? 1.0F : 1.0F - Mth.clamp((age - 20.0F) / 4.0F, 0, 1);

                if (age < 6.0F) {
                    // Windup / Stance Phase: Low combat stance, left knee bent forward, 2-handed grip
                    if (type == TransformType.ROTATION) {
                        if (part.equals("leftLeg")) return mix(value, -0.68F, 0.04F, -0.06F, wR);
                        if (part.equals("rightLeg")) return mix(value, 0.48F, -0.08F, 0.05F, wR);
                        if (part.equals("body")) return mix(value, 0.32F, -0.15F * sign, 0.0F, wR);
                        if (part.equals("head")) return mix(value, -0.32F, 0.0F, 0.0F, wR);
                        if (sword) return mix(value, -1.48F, -0.18F * sign, 0.10F * sign, wR);
                        if (support && p.getOffhandItem().isEmpty()) return mix(value, -1.42F, 0.48F * sign, -0.22F * sign, wR);
                        if (part.equals("rightItem")) return mix(value, -1.42F, 0.0F, 0.0F, wR);
                        if (part.equals("leftItem")) return mix(value, 0.0F, 0.0F, 0.0F, wR);
                    }
                    if (type == TransformType.BEND) {
                        if (part.equals("leftLeg")) return mix(value, 0.95F, 0.0F, 0.0F, wR);
                        if (part.equals("rightLeg")) return mix(value, 0.15F, 0.0F, 0.0F, wR);
                        if (sword || support) return mix(value, 0.30F, 0.0F, 0.0F, wR);
                    }
                    if (type == TransformType.POSITION) {
                        if (part.equals("body")) return mix(value, 0.0F, -4.2F, 2.0F, wR);
                        if (part.equals("leftLeg")) return mix(value, -0.5F, -1.0F, 2.4F, wR);
                        if (part.equals("rightLeg")) return mix(value, 0.5F, 0.0F, -2.2F, wR);
                        if (part.equals("rightItem") || part.equals("leftItem")) return mix(value, 0.0F, 0.0F, 0.0F, wR);
                    }
                } else if (age <= 22.0F) {
                    // Thrust Phase: Explosive forward lunge, blade locked straight, support arm swept back
                    float thrust = Mth.clamp((age - 6.0F) / 2.5F, 0, 1);
                    if (type == TransformType.ROTATION) {
                        if (part.equals("leftLeg")) return mix(value, -0.75F, 0.0F, 0.0F, wR);
                        if (part.equals("rightLeg")) return mix(value, 0.60F, 0.0F, 0.0F, wR);
                        if (part.equals("body")) return mix(value, 0.52F, 0.0F, 0.0F, wR);
                        if (part.equals("head")) return mix(value, -0.50F, 0.0F, 0.0F, wR);
                        if (sword) return mix(value, -1.68F, -0.05F * sign, 0.0F, wR);
                        if (support && p.getOffhandItem().isEmpty()) return mix(value, 0.55F, 0.25F * sign, 0.40F * sign, wR);
                        if (part.equals("rightItem")) return mix(value, -1.48F, 0.0F, 0.0F, wR);
                        if (part.equals("leftItem")) return mix(value, 0.0F, 0.0F, 0.0F, wR);
                    }
                    if (type == TransformType.BEND) {
                        if (part.equals("leftLeg")) return mix(value, 0.65F, 0.0F, 0.0F, wR);
                        if (part.equals("rightLeg")) return mix(value, 0.05F, 0.0F, 0.0F, wR);
                        if (sword || support) return mix(value, 0.10F, 0.0F, 0.0F, wR);
                    }
                    if (type == TransformType.POSITION) {
                        if (part.equals("body")) return mix(value, 0.0F, -2.8F, 5.0F * thrust, wR);
                        if (part.equals("leftLeg")) return mix(value, -0.5F, -0.5F, 3.2F, wR);
                        if (part.equals("rightLeg")) return mix(value, 0.5F, 0.0F, -3.0F, wR);
                        if (part.equals("rightItem") || part.equals("leftItem")) return mix(value, 0.0F, 0.0F, 0.0F, wR);
                    }
                }
                return value;
            }

            if(type==TransformType.ROTATION){
                if(sword){
                    if(v.kind.equals("flame_fan") || v.kind.endsWith("sweep") || v.kind.equals("wide_cut"))return mix(value,-1.2F,(.9F-1.8F*t)*sign,-.15F*sign,w);
                    if(v.kind.equals("flame_barrage") || v.kind.equals("higashi") || v.kind.equals("steam_cut"))return mix(value,-2.6F+2.3F*t,-.25F*sign,-.2F*sign,w);
                    if(List.of("minami","sennen","jokaku","glacial_zone","ice_eruption","hydra_assault").contains(v.kind))return mix(value,-1.7F+1.4F*t,-.2F*sign,.15F*sign,w);
                    return mix(value,-1.55F+.5F*t,-.2F*sign,-.15F*sign,w);
                }
                if(support && p.getOffhandItem().isEmpty())return mix(value,-1.1F,.25F*sign,.2F*sign,w);
                if(part.equals("rightItem") || part.equals("leftItem"))return mix(value,0,0,0,w);
            }
            if(type==TransformType.BEND && (sword||support) || type==TransformType.POSITION && (part.equals("rightItem")||part.equals("leftItem")))return mix(value,0,0,0,w);
            return value;
        }
    }
    @SubscribeEvent public static void hand(RenderHandEvent e){var p=Minecraft.getInstance().player;if(p==null || e.getHand()!=InteractionHand.MAIN_HAND)return;var v=gesture(p);if(v==null)return;
        if(!v.ice){RyujinActionVisuals.hand(e.getPoseStack(),com.bleachmod.common.visual.RyujinChoreography.attack(v.kind,age(v,e.getPartialTick()),p.getMainArm()==HumanoidArm.RIGHT?1:-1));return;}
        float t=Mth.clamp(age(v,e.getPartialTick())/v.gesture,0,1),w=(float)Math.sin(t*Math.PI),sign=p.getMainArm()==HumanoidArm.RIGHT?1:-1;
        e.getPoseStack().translate(-sign*.18*w,.12*w,-.16*w);e.getPoseStack().mulPose(Axis.ZP.rotationDegrees(sign*(v.kind.equals("flame_fan")?70:-30)*w));
    }
    private static final class Types extends RenderType {
        private Types(){super("elemental_unused",DefaultVertexFormat.POSITION_COLOR,VertexFormat.Mode.QUADS,4096,false,true,()->{},()->{});}
        static final RenderType MAGIC=create("elemental_construct",DefaultVertexFormat.POSITION_COLOR,VertexFormat.Mode.QUADS,4096,false,true,CompositeState.builder().setShaderState(RENDERTYPE_LIGHTNING_SHADER).setTransparencyState(TRANSLUCENT_TRANSPARENCY).setCullState(NO_CULL).setWriteMaskState(COLOR_WRITE).setOutputState(PARTICLES_TARGET).createCompositeState(false));
    }
    private static void quad(VertexConsumer b,org.joml.Matrix4f m,Vec3 a,Vec3 c,Vec3 d,Vec3 e,int color){for(var p:List.of(a,c,d,e))b.vertex(m,(float)p.x,(float)p.y,(float)p.z).color((color>>16)&255,(color>>8)&255,color&255,color>>>24).endVertex();}
    private static void column(VertexConsumer b,org.joml.Matrix4f m,Vec3 p,double width,double height,int color){
        double x=p.x,z=p.z,y=p.y,w=width/2;
        quad(b,m,new Vec3(x-w,y,z-w),new Vec3(x+w,y,z-w),new Vec3(x+w,y+height,z-w),new Vec3(x-w,y+height,z-w),color);
        quad(b,m,new Vec3(x+w,y,z+w),new Vec3(x-w,y,z+w),new Vec3(x-w,y+height,z+w),new Vec3(x+w,y+height,z+w),color);
        quad(b,m,new Vec3(x-w,y,z+w),new Vec3(x-w,y,z-w),new Vec3(x-w,y+height,z-w),new Vec3(x-w,y+height,z+w),color);
        quad(b,m,new Vec3(x+w,y,z-w),new Vec3(x+w,y,z+w),new Vec3(x+w,y+height,z+w),new Vec3(x+w,y+height,z-w),color);
        quad(b,m,new Vec3(x-w,y+height,z-w),new Vec3(x+w,y+height,z-w),new Vec3(x+w,y+height,z+w),new Vec3(x-w,y+height,z+w),color);
    }
    private static void spike(VertexConsumer b,org.joml.Matrix4f m,Vec3 base,Vec3 direction,double width,double height,double alpha){
        if(height<.01 || alpha<.01)return;
        Vec3 tip=base.add(direction.scale(height*.15)).add(0,height,0);
        Vec3[] ring={base.add(-width,0,-width),base.add(width,0,-width),base.add(width,0,width),base.add(-width,0,width)};
        int[] colors={0x5EBFE9,0xBDEFFF,0x83D2F3,0xDDF9FF};
        for(int i=0;i<4;i++)quad(b,m,ring[i],ring[(i+1)%4],tip,tip,((int)(220*alpha)<<24)|colors[i]);
    }
    @SubscribeEvent public static void render(RenderLevelStageEvent e){if(e.getStage()!=RenderLevelStageEvent.Stage.AFTER_PARTICLES)return;var mc=Minecraft.getInstance();if(mc.level==null || mc.player==null){clear();return;}
        if(world!=mc.level){clear();return;}VIEWS.values().forEach(list->list.removeIf(v->age(v,0)>=v.duration));VIEWS.values().removeIf(List::isEmpty);
        if(VIEWS.isEmpty())return;var buffers=mc.renderBuffers().bufferSource();var b=buffers.getBuffer(Types.MAGIC);var matrix=e.getPoseStack().last().pose();var camera=e.getCamera().getPosition();
        for(var list:VIEWS.values())for(var v:list){if(!v.ice || mc.player.distanceToSqr(v.origin)>72*72)continue;float age=age(v,e.getPartialTick());var origin=v.origin.subtract(camera);
            if(v.kind.equals("sennen")){
                double grow=Mth.clamp(age/35,0,1),r=v.radius-(v.radius*.25)*Mth.clamp((age-35)/25,0,1);
                for(int i=0;i<18;i++){double a=i*Math.PI/9;var p=origin.add(Math.cos(a)*r,0,Math.sin(a)*r);spike(b,matrix,p,new Vec3(-Math.cos(a),0,-Math.sin(a)),1.2,grow*(12+(i%3)),1);}
            }else if(v.kind.equals("ice_eruption")){
                int index=0;for(var point:v.crystals){double distance=point.subtract(v.origin).dot(v.forward);double grow=Mth.clamp((age-12-distance*30/32)/8,0,1),fade=1-Mth.clamp((age-65)/25,0,1);
                    spike(b,matrix,point.subtract(camera),v.forward,1.1+distance*.035,(8+(index++%4)*2)*grow,fade);}
            }else if(v.kind.equals("ryusenka") && age>=18 && age<64){
                for(int id:v.targets){var target=mc.level.getEntity(id);if(target==null)continue;var center=target.position().subtract(camera);
                    double dissolve=1;
                    spike(b,matrix,center,new Vec3(0,0,1),1.0,3.5,dissolve);
                    for(int i=0;i<4;i++){double a=i*Math.PI/2;spike(b,matrix,center.add(Math.cos(a)*.9,0,Math.sin(a)*.9),new Vec3(-Math.cos(a),0,-Math.sin(a)),.35,2,dissolve);}
                }
}
        }
        buffers.endBatch(Types.MAGIC);
    }
}
