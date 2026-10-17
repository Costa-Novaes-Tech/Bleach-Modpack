package com.bleachmod.client.model;

import com.bleachmod.Reference;
import com.bleachmod.common.data.PlayerCapability;
import com.mojang.math.Axis;
import dev.kosmx.playerAnim.api.TransformType;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.core.util.Vec3f;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationFactory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import java.util.*;

/** Custom player gesture and converging fire/condensing ice, driven by server snapshots. */
@Mod.EventBusSubscriber(modid=Reference.MOD_ID,value=Dist.CLIENT)
public final class ZanpakutoReleaseAnimation {
    private record View(String identity, boolean bankai, int duration, long start) { }
    private static final Map<Integer,View> VIEWS=new HashMap<>();
    private static net.minecraft.client.multiplayer.ClientLevel world;
    private ZanpakutoReleaseAnimation() { }
    public static void init() {
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(Reference.id("release"),4000,Animation::new);
    }
    public static void clear() {VIEWS.clear();world=null;}
    public static void accept(int id,CompoundTag tag) {
        var level=Minecraft.getInstance().level;if(level==null)return;
        if(world!=level){clear();world=level;}
        String identity=tag.getString("identity"),form=tag.getString("form");
        int duration=tag.getInt("duration"),elapsed=tag.getInt("elapsed");
        if(!List.of("ryujin_jakka","hyorinmaru").contains(identity) || !List.of("shikai","bankai").contains(form)
                || duration!=(form.equals("bankai")?60:24) || elapsed<0 || elapsed>=duration){VIEWS.remove(id);return;}
        VIEWS.put(id,new View(identity,form.equals("bankai"),duration,level.getGameTime()-elapsed));
    }
    private static View view(AbstractClientPlayer p) {
        var v=VIEWS.get(p.getId());
        if(v==null || !p.isAlive() || p.isSpectator() || world!=p.level() || p.level().getGameTime()-v.start>=v.duration
                || !PlayerCapability.get(p).map(d->d.getCharacter().getZanpakutoIdentity().equals(v.identity)).orElse(false))return null;
        return v;
    }
    public static boolean active(AbstractClientPlayer p){return view(p)!=null;}
    public static float iceGrowth(AbstractClientPlayer p,float partial){var v=view(p);return v==null?1:Mth.clamp((progress(p,v,partial)-.66F)/.3F,0,1);}
    private static float progress(AbstractClientPlayer p,View v,float partial){return Mth.clamp((p.level().getGameTime()-v.start+partial)/v.duration,0,1);}
    private static float weight(float t){return Math.min(1,t*8)*Math.min(1,(1-t)*6);}
    private static Vec3f mix(Vec3f a,float x,float y,float z,float w){return new Vec3f(Mth.lerp(w,a.getX(),x),Mth.lerp(w,a.getY(),y),Mth.lerp(w,a.getZ(),z));}
    private static final class Animation implements IAnimation {
        private final AbstractClientPlayer player;
        Animation(AbstractClientPlayer p){player=p;}
        @Override public boolean isActive(){return active(player);}
        @Override public void setupAnim(float partial){ }
        @Override public FirstPersonMode getFirstPersonMode(float partial){return FirstPersonMode.VANILLA;}
        @Override public Vec3f get3DTransform(String part,TransformType type,float partial,Vec3f value){
            var v=view(player);if(v==null)return value;
            float t=progress(player,v,partial),w=weight(t),sign=player.getMainArm()==HumanoidArm.RIGHT?1:-1;
            if(v.bankai && v.identity.equals("ryujin_jakka"))return RyujinActionVisuals.transform(player,com.bleachmod.common.visual.RyujinChoreography.release(t*v.duration,sign),part,type,value);
            boolean sword=part.equals(sign>0?"rightArm":"leftArm"),seal=part.equals(sign>0?"leftArm":"rightArm");
            boolean fire=v.identity.equals("ryujin_jakka");
            if(type==TransformType.ROTATION){
                if(sword){
                    return mix(value,-1.35F,-.25F*sign,-.2F*sign,w);
                }
                if(seal && player.getOffhandItem().isEmpty())return mix(value,-1.05F,.4F*sign,.35F*sign,w);
                if(part.equals("body"))return mix(value,0,-.12F*sign,0,w);
                if(part.equals("rightItem") || part.equals("leftItem"))return mix(value,0,0,0,w);
            }
            if((type==TransformType.POSITION && (part.equals("rightItem") || part.equals("leftItem")))
                    || type==TransformType.BEND && (sword||seal))return mix(value,0,0,0,w);
            return value;
        }
    }
    @SubscribeEvent public static void fov(net.minecraftforge.client.event.ComputeFovModifierEvent e){
        if(!com.bleachmod.client.ClientVisualConfig.CINEMATIC_FOV.get() || !(e.getPlayer() instanceof AbstractClientPlayer p))return;
        var v=view(p);if(v==null || !v.bankai)return;
        float t=progress(p,v,Minecraft.getInstance().getFrameTime());
        e.setNewFovModifier(e.getNewFovModifier()*(1-.08F*(float)Math.sin(t*Math.PI)));
    }
    @SubscribeEvent public static void hand(RenderHandEvent e){
        var p=Minecraft.getInstance().player;if(p==null || e.getHand()!=InteractionHand.MAIN_HAND)return;
        var v=view(p);if(v==null)return;
        if(v.bankai && v.identity.equals("ryujin_jakka")){RyujinActionVisuals.hand(e.getPoseStack(),com.bleachmod.common.visual.RyujinChoreography.release(progress(p,v,e.getPartialTick())*v.duration,p.getMainArm()==HumanoidArm.RIGHT?1:-1));return;}
        float w=weight(progress(p,v,e.getPartialTick())),sign=p.getMainArm()==HumanoidArm.RIGHT?1:-1;
        var pose=e.getPoseStack();
        pose.translate(-sign*.2*w,-.05*w,-.1*w);
        pose.mulPose(Axis.ZP.rotationDegrees(sign*(v.bankai && v.identity.equals("ryujin_jakka")?75:20)*w));
        pose.mulPose(Axis.XP.rotationDegrees(-15*w));
    }
    @SubscribeEvent public static void tick(TickEvent.ClientTickEvent e){
        if(e.phase!=TickEvent.Phase.END)return;
        var mc=Minecraft.getInstance();if(mc.level==null){clear();return;}
        VIEWS.entrySet().removeIf(entry->mc.level.getGameTime()-entry.getValue().start>=entry.getValue().duration);
        for(var entry:VIEWS.entrySet()){
            if(!(mc.level.getEntity(entry.getKey()) instanceof AbstractClientPlayer p) || view(p)==null || mc.player==null || mc.player.distanceToSqr(p)>64*64)continue;
            var v=entry.getValue();float t=progress(p,v,0);
            if(v.bankai && v.identity.equals("ryujin_jakka")){RyujinActionVisuals.releaseParticles(p,t*v.duration);if(p==mc.player && mc instanceof net.bettercombat.api.MinecraftClient_BetterCombat bc)bc.cancelUpswing();continue;}
            boolean fire=v.identity.equals("ryujin_jakka");
            var forward=Vec3.directionFromRotation(0,p.getYRot());var side=new Vec3(-forward.z,0,forward.x);
            float sign=p.getMainArm()==HumanoidArm.RIGHT?1:-1;
            Vec3 blade=p.position().add(forward.scale(.7)).add(side.scale(sign*.3)).add(0,1,0);
            int samples=mc.options.particles().get()==net.minecraft.client.ParticleStatus.MINIMAL?3:12;
            for(int i=0;i<samples;i++){
                double a=i*Math.PI*2/samples+t*6;
                if(fire && v.bankai && t<.67){
                    double r=3.8*(1-t/.67)+.25;
                    Vec3 point=p.position().add(Math.cos(a)*r,.4+(i%4)*.45,Math.sin(a)*r);
                    Vec3 velocity=blade.subtract(point).scale(.22);
                    mc.level.addParticle(ParticleTypes.FLAME,point.x,point.y,point.z,velocity.x,velocity.y,velocity.z);
                }else{
                    double r=fire?.2:(.8+Math.max(0,t-.66)*6);
                    Vec3 point=fire?blade.add(side.scale(Math.sin(a)*.7)):p.position().add(Math.cos(a)*r,.4+(i%4)*.4,Math.sin(a)*r);
                    mc.level.addParticle(fire?(v.bankai?ParticleTypes.SMOKE:ParticleTypes.FLAME):ParticleTypes.SNOWFLAKE,
                            point.x,point.y,point.z,fire?0:Math.cos(a)*.06,.025,fire?0:Math.sin(a)*.06);
                }
            }
            if(p==mc.player && mc instanceof net.bettercombat.api.MinecraftClient_BetterCombat bc)bc.cancelUpswing();
        }
    }
}
