package com.bleachmod.client.cinematic;

import com.bleachmod.Reference;
import com.bleachmod.common.quincy.QuincyFormation;
import com.bleachmod.common.quincy.QuincyVollstandigService;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import java.util.*;

/** Initial geometric Reishi manifestation; the four satellites share server launch coordinates. */
@Mod.EventBusSubscriber(modid=Reference.MOD_ID,value=Dist.CLIENT)
public final class QuincyVollstandigVisuals {
    private record View(long start,Vec3 anchor,float yaw,boolean active,int steps,long lastShot) { }
    private static final Map<Integer,View> VIEWS=new HashMap<>();
    private static net.minecraft.client.multiplayer.ClientLevel world;
    private static CameraType previous;
    private static java.lang.reflect.Method setPosition;
    private QuincyVollstandigVisuals() { }
    public static void init(){dev.kosmx.playerAnim.minecraftApi.PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(Reference.id("quincy_entrance"),4100,Pose::new);}
    private static final class Pose implements dev.kosmx.playerAnim.api.layered.IAnimation {
        private final net.minecraft.client.player.AbstractClientPlayer player;
        Pose(net.minecraft.client.player.AbstractClientPlayer player){this.player=player;}
        @Override public void setupAnim(float partial) { }
        @Override public boolean isActive(){var v=VIEWS.get(player.getId());return world==player.level()&&player.isAlive()&&v!=null&&age(v,0)<180;}
        @Override public dev.kosmx.playerAnim.core.util.Vec3f get3DTransform(String part,dev.kosmx.playerAnim.api.TransformType type,float partial,dev.kosmx.playerAnim.core.util.Vec3f value){
            var v=VIEWS.get(player.getId());if(v==null||!isActive()||type!=dev.kosmx.playerAnim.api.TransformType.ROTATION)return value;
            double t=age(v,partial);float weight=(float)(smooth(t/20)*(1-smooth((t-155)/25)));
            boolean left=player.getMainArm()==net.minecraft.world.entity.HumanoidArm.LEFT;
            boolean bow=part.equals(left?"leftArm":"rightArm"),string=part.equals(left?"rightArm":"leftArm");
            if(!bow&&!string)return value;
            float x=bow?-1.45F:-1.65F,y=(left?-1:1)*(bow?-.35F:.75F),z=0;
            return new dev.kosmx.playerAnim.core.util.Vec3f(Mth.lerp(weight,value.getX(),x),Mth.lerp(weight,value.getY(),y),Mth.lerp(weight,value.getZ(),z));
        }
    }
    public static void accept(int id,CompoundTag tag){
        var mc=Minecraft.getInstance();if(mc.level==null)return;
        if(world!=mc.level){clear();world=mc.level;}
        if(!tag.contains("age")){VIEWS.remove(id);if(mc.player!=null&&id==mc.player.getId())restore();return;}
        VIEWS.put(id,new View(world.getGameTime()-tag.getInt("age"),new Vec3(tag.getDouble("x"),tag.getDouble("y"),tag.getDouble("z")),tag.getFloat("yaw"),tag.getBoolean("active"),tag.getInt("steps"),world.getGameTime()-tag.getInt("shotAge")));
    }
    public static int steps(int id){var v=VIEWS.get(id);return v==null?0:v.steps;}
    private static double age(View v,double partial){return world.getGameTime()-v.start+partial;}
    private static View local(){var mc=Minecraft.getInstance();return mc.player==null?null:VIEWS.get(mc.player.getId());}
    private static void restore(){if(previous!=null){Minecraft.getInstance().options.setCameraType(previous);previous=null;}}
    private static void clear(){restore();VIEWS.clear();world=null;}
    @SubscribeEvent public static void tick(TickEvent.ClientTickEvent e){
        if(e.phase!=TickEvent.Phase.END)return;var mc=Minecraft.getInstance();
        if(mc.level==null||world!=mc.level){clear();return;}
        VIEWS.entrySet().removeIf(v->world.getEntity(v.getKey())==null);
        var v=local();if(v==null||age(v,0)>=QuincyVollstandigService.ENTRANCE||!mc.player.isAlive())restore();
        else if(previous==null){previous=mc.options.getCameraType();mc.options.setCameraType(CameraType.THIRD_PERSON_BACK);}
    }
    @SubscribeEvent public static void camera(ViewportEvent.ComputeCameraAngles e){
        var mc=Minecraft.getInstance();var v=local();if(previous==null||v==null||world!=mc.level)return;
        double t=age(v,e.getPartialTick());if(t>=180)return;
        Vec3 forward=Vec3.directionFromRotation(0,v.yaw),focus=v.anchor.add(0,1.4,0);
        double opening=smooth(t/40),wide=smooth((t-55)/90),exit=smooth((t-160)/20);
        Vec3 eye=focus.add(forward.scale(Mth.lerp(wide,Mth.lerp(opening,4,2.6),7))).add(0,Mth.lerp(wide,.2,1),0);
        Vec3 normal=mc.player.getEyePosition();
        if(previous!=CameraType.FIRST_PERSON)normal=normal.add(mc.player.getLookAngle().scale(previous==CameraType.THIRD_PERSON_FRONT?4:-4));
        eye=eye.lerp(normal,exit);focus=focus.lerp(normal.add(mc.player.getLookAngle().scale(previous==CameraType.THIRD_PERSON_FRONT?-8:8)),exit);
        var hit=world.clip(new net.minecraft.world.level.ClipContext(mc.player.getEyePosition(),eye,net.minecraft.world.level.ClipContext.Block.COLLIDER,net.minecraft.world.level.ClipContext.Fluid.NONE,mc.player));
        if(hit.getType()!=net.minecraft.world.phys.HitResult.Type.MISS)eye=hit.getLocation().lerp(mc.player.getEyePosition(),.1);
        try{if(setPosition==null)setPosition=net.minecraftforge.fml.util.ObfuscationReflectionHelper.findMethod(Camera.class,"m_90581_",Vec3.class);setPosition.invoke(e.getCamera(),eye);}
        catch(ReflectiveOperationException|RuntimeException failure){restore();return;}
        Vec3 delta=focus.subtract(eye);e.setYaw((float)Math.toDegrees(Math.atan2(-delta.x,delta.z)));e.setPitch((float)-Math.toDegrees(Math.atan2(delta.y,delta.horizontalDistance())));e.setRoll(0);
    }
    private static double smooth(double t){t=Mth.clamp(t,0,1);return t*t*(3-2*t);}
    @SubscribeEvent public static void overlay(RenderGuiOverlayEvent.Pre e){var v=local();if(previous!=null&&v!=null&&world==Minecraft.getInstance().level&&age(v,0)<180)e.setCanceled(true);}
    @SubscribeEvent public static void gui(RenderGuiEvent.Post e){
        var v=local();if(previous==null||v==null)return;var mc=Minecraft.getInstance();double age=age(v,mc.getFrameTime());
        if(age>=180)return;var g=e.getGuiGraphics();int w=mc.getWindow().getGuiScaledWidth(),h=mc.getWindow().getGuiScaledHeight();
        int band=(int)(h*.07*Math.min(smooth(age/12),1-smooth((age-160)/20)));
        g.fill(0,0,w,band,0xFF081322);g.fill(0,h-band,w,h,0xFF081322);
        if(age>55&&age<155)g.drawCenteredString(mc.font,"Vollständig",w/2,h-band-20,0xCCF7FF);
    }
    @SubscribeEvent public static void render(RenderLevelStageEvent e){
        var mc=Minecraft.getInstance();if(e.getStage()!=RenderLevelStageEvent.Stage.AFTER_ENTITIES||world!=mc.level||mc.player==null)return;
        var type=RenderType.lightning();var buffers=mc.renderBuffers().bufferSource();var out=buffers.getBuffer(type);var matrix=e.getPoseStack().last().pose();Vec3 camera=e.getCamera().getPosition();int count=0;
        for(var entry:VIEWS.entrySet()){
            if(++count>16)break;var entity=world.getEntity(entry.getKey());if(entity==null||!entity.isAlive()||entity.distanceToSqr(mc.player)>64*64)continue;
            var v=entry.getValue();double age=age(v,e.getPartialTick()),growth=smooth((age-45)/90);if(!v.active&&age>=180)continue;
            Vec3 pos=entity.getPosition(e.getPartialTick()),forward=Vec3.directionFromRotation(0,entity.getYRot()),side=new Vec3(forward.z,0,-forward.x);
            double starGrowth=growth*smooth((world.getGameTime()+e.getPartialTick()-v.lastShot-2)/10);
            for(int i=0;i<4;i++){
                Vec3 star=QuincyFormation.star(pos,entity.getYRot(),i,world.getGameTime()+e.getPartialTick());
                diamond(out,matrix,star.subtract(camera),new Vec3(0,.42*starGrowth,0),side.scale(.18*starGrowth),forward.scale(.13*starGrowth),220);
                diamond(out,matrix,star.subtract(camera),side.scale(.34*starGrowth),new Vec3(0,.07*starGrowth,0),forward.scale(.07*starGrowth),160);
            }
            for(int sign:new int[]{-1,1})for(int k=0;k<4;k++){
                Vec3 base=pos.subtract(forward.scale(.45)).add(0,1.3,0).add(side.scale(sign*.25));
                Vec3 tip=base.add(side.scale(sign*(1.7+k*.2)*growth)).subtract(forward.scale((.3+k*.15)*growth)).add(0,(1.8-k*.45)*growth,0);
                diamond(out,matrix,base.lerp(tip,.5).subtract(camera),tip.subtract(base).scale(.5),new Vec3(0,.11,0),forward.scale(.08),95);
            }
        }
        buffers.endBatch(type);
    }
    private static void diamond(VertexConsumer out,org.joml.Matrix4f m,Vec3 c,Vec3 axis,Vec3 a,Vec3 b,int alpha){
        Vec3[] ring={c.add(a),c.add(b),c.subtract(a),c.subtract(b)};
        for(int sign:new int[]{-1,1})for(int i=0;i<4;i++){
            Vec3 tip=c.add(axis.scale(sign));for(Vec3 p:new Vec3[]{tip,ring[i],ring[(i+1)%4],tip})out.vertex(m,(float)p.x,(float)p.y,(float)p.z).color(110,215,255,alpha).endVertex();
        }
    }
}
