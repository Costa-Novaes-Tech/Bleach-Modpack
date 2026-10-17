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

/** Bounded timeline-driven player gestures and translucent constructs; never deals damage. */
@Mod.EventBusSubscriber(modid=Reference.MOD_ID,value=Dist.CLIENT)
public final class ElementalActionVisuals {
    private record View(String kind,Vec3 origin,Vec3 forward,double radius,int duration,int gesture,long start,boolean ice){ }
    private static final Map<Integer,List<View>> VIEWS=new LinkedHashMap<>();
    private static net.minecraft.client.multiplayer.ClientLevel world;
    private ElementalActionVisuals(){ }
    public static void init(){PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(Reference.id("elemental_action"),3500,Animation::new);}
    public static void clear(){VIEWS.clear();world=null;}
    public static void accept(int owner,CompoundTag tag){var level=Minecraft.getInstance().level;if(level==null)return;if(world!=level){clear();world=level;}
        var result=new ArrayList<View>();var entries=tag.getList("casts",Tag.TAG_COMPOUND);
        for(int i=0;i<Math.min(4,entries.size());i++){var t=entries.getCompound(i);String kind=t.getString("kind");int duration=t.getInt("duration"),gesture=t.getInt("gesture"),age=t.getInt("age");
            var origin=new Vec3(t.getDouble("x"),t.getDouble("y"),t.getDouble("z"));var forward=new Vec3(t.getDouble("dx"),0,t.getDouble("dz"));double radius=t.getDouble("radius");
            if(!List.of("front_cut","wide_cut","sealed_cut","sealed_sweep","higashi","flame_barrage","flame_fan","flame_tornado","jokaku","minami","steam_cut","crescent_chain","ryusenka","sennen","duel_control","bloom","glacial_zone","ice_dragon","flame_dash").contains(kind)
                    || duration<1 || duration>200 || age<0 || age>=duration || gesture<0 || gesture>duration || !Double.isFinite(radius) || radius<0 || radius>100
                    || !Double.isFinite(origin.x+origin.y+origin.z) || !Double.isFinite(forward.x+forward.z) || Math.abs(origin.x)>3e7 || Math.abs(origin.z)>3e7 || Math.abs(origin.y)>4096 || forward.lengthSqr()<.9 || forward.lengthSqr()>1.1)continue;
            result.add(new View(kind,origin,forward.normalize(),radius,duration,gesture,level.getGameTime()-age,t.getBoolean("ice")));
        }
        if(result.isEmpty())VIEWS.remove(owner);else{if(VIEWS.size()>=24 && !VIEWS.containsKey(owner))VIEWS.remove(VIEWS.keySet().iterator().next());VIEWS.put(owner,result);}
    }
    private static float age(View v,float partial){return world==null?999:world.getGameTime()-v.start+partial;}
    private static View gesture(AbstractClientPlayer p){if(world!=p.level() || !p.isAlive())return null;for(var v:VIEWS.getOrDefault(p.getId(),List.of()))if(age(v,0)<v.gesture)return v;return null;}
    public static boolean busy(AbstractClientPlayer p){return gesture(p)!=null;}
    public static boolean bloomReady(AbstractClientPlayer p){return world==p.level() && VIEWS.getOrDefault(p.getId(),List.of()).stream().anyMatch(v->v.kind.equals("glacial_zone") && v.radius==30 && age(v,0)>=60 && age(v,0)<120);}
    private static Vec3f mix(Vec3f a,float x,float y,float z,float w){return new Vec3f(Mth.lerp(w,a.getX(),x),Mth.lerp(w,a.getY(),y),Mth.lerp(w,a.getZ(),z));}
    private static final class Animation implements IAnimation {
        private final AbstractClientPlayer p;Animation(AbstractClientPlayer p){this.p=p;}
        @Override public boolean isActive(){return busy(p);}
        @Override public void setupAnim(float partial){ }
        @Override public FirstPersonMode getFirstPersonMode(float partial){return FirstPersonMode.VANILLA;}
        @Override public Vec3f get3DTransform(String part,TransformType type,float partial,Vec3f value){var v=gesture(p);if(v==null)return value;
            float t=Mth.clamp(age(v,partial)/v.gesture,0,1),w=Math.min(1,t*6)*Math.min(1,(1-t)*6),sign=p.getMainArm()==HumanoidArm.RIGHT?1:-1;
            boolean sword=part.equals(sign>0?"rightArm":"leftArm");boolean support=part.equals(sign>0?"leftArm":"rightArm");
            if(type==TransformType.ROTATION){
                if(sword){
                    if(v.kind.equals("flame_fan") || v.kind.endsWith("sweep") || v.kind.equals("wide_cut"))return mix(value,-1.2F,(.9F-1.8F*t)*sign,-.15F*sign,w);
                    if(v.kind.equals("flame_barrage") || v.kind.equals("higashi") || v.kind.equals("steam_cut"))return mix(value,-2.6F+2.3F*t,-.25F*sign,-.2F*sign,w);
                    if(List.of("minami","sennen","jokaku","bloom","glacial_zone").contains(v.kind))return mix(value,-1.7F+1.4F*t,-.2F*sign,.15F*sign,w);
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
    @SubscribeEvent public static void render(RenderLevelStageEvent e){if(e.getStage()!=RenderLevelStageEvent.Stage.AFTER_PARTICLES)return;var mc=Minecraft.getInstance();if(mc.level==null || mc.player==null){clear();return;}
        if(world!=mc.level){clear();return;}VIEWS.values().forEach(list->list.removeIf(v->age(v,0)>=v.duration));VIEWS.values().removeIf(List::isEmpty);
        if(VIEWS.isEmpty())return;var buffers=mc.renderBuffers().bufferSource();var b=buffers.getBuffer(Types.MAGIC);var matrix=e.getPoseStack().last().pose();var camera=e.getCamera().getPosition();
        for(var list:VIEWS.values())for(var v:list){if(mc.player.distanceToSqr(v.origin)>72*72)continue;float age=age(v,e.getPartialTick());var origin=v.origin.subtract(camera);
            if(v.kind.equals("sennen")){
                double grow=Mth.clamp(age/35,0,1),r=5-2*Mth.clamp((age-35)/25,0,1);
                for(int i=0;i<12;i++){double a=i*Math.PI/6;var p=origin.add(Math.cos(a)*r,0,Math.sin(a)*r);column(b,matrix,p,.8,grow*(5+(i%3)*.7),0x997BDFFF);column(b,matrix,p.add(0,.1,0),.18,grow*6,0xBBDDFFFF);}
            }else if(v.kind.equals("jokaku")){
                double grow=Mth.clamp(age/20,0,1);
                for(int i=0;i<32;i++){double a=i*Math.PI/16,c=(i+1)*Math.PI/16;for(int layer=0;layer<4;layer++){double y=layer*1.4*grow,r=6*Math.sqrt(Math.max(0,1-(y/7)*(y/7)));double y2=(layer+1)*1.4*grow,r2=6*Math.sqrt(Math.max(0,1-(y2/7)*(y2/7)));
                    quad(b,matrix,origin.add(Math.cos(a)*r,y,Math.sin(a)*r),origin.add(Math.cos(c)*r,y,Math.sin(c)*r),origin.add(Math.cos(c)*r2,y2,Math.sin(c)*r2),origin.add(Math.cos(a)*r2,y2,Math.sin(a)*r2),0x55FF802A);}}
            }else if(v.kind.equals("flame_barrage") && age>=6 && age<=16){var p=origin.add(v.forward.scale(Math.min(8,age-6)));var side=new Vec3(-v.forward.z,0,v.forward.x);
                quad(b,matrix,p.add(side.scale(-1)),p.add(side),p.add(side).add(0,2.8,0),p.add(side.scale(-1)).add(0,2.8,0),0xD9FFAA25);
            }else if(v.kind.equals("flame_fan") && age>=6 && age<=22){double head=Math.toRadians(-70+Math.min(14,age-6)*10);
                for(int i=0;i<8;i++){var a=v.forward.yRot((float)(head-i*.035));var c=v.forward.yRot((float)(head-(i+1)*.035));quad(b,matrix,origin.add(a.scale(1)).add(0,1,0),origin.add(a.scale(14)).add(0,1,0),origin.add(c.scale(14)).add(0,1.3,0),origin.add(c.scale(1)).add(0,1.3,0),((180-i*18)<<24)|0xFFAA35);}
            }else if(v.kind.equals("bloom")){
                for(int i=0;i<12;i++){double a=i*Math.PI/6;var center=origin.add(Math.cos(a)*v.radius*.65,.3,Math.sin(a)*v.radius*.65);double size=Mth.clamp(age/20,0,1)*2;
                    for(int petal=0;petal<6;petal++){double q=petal*Math.PI/3;var tip=center.add(Math.cos(q)*size,size*.6,Math.sin(q)*size);var side=new Vec3(-Math.sin(q)*.3,0,Math.cos(q)*.3);quad(b,matrix,center,tip.add(side),tip.add(0,size*.5,0),tip.subtract(side),0xAABCEEFF);}}
            }
        }
        buffers.endBatch(Types.MAGIC);
    }
}
