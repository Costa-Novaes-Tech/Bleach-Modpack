package com.bleachmod.client.model;

import com.bleachmod.Reference;
import com.bleachmod.common.data.PlayerCapability;
import com.bleachmod.common.visual.SignatureChoreography;
import com.bleachmod.common.visual.RyujinChoreography;
import com.bleachmod.init.*;
import dev.kosmx.playerAnim.api.TransformType;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.core.util.Vec3f;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationFactory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.particles.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import java.util.*;

/** Cosmetic overlays for accepted existing techniques and vanilla swings; no new attack handling. */
@Mod.EventBusSubscriber(modid=Reference.MOD_ID,value=Dist.CLIENT)
public final class SignatureActionVisuals {
    private record View(String identity,String form,String kind,int duration,long start){ }
    private static final Map<Integer,View> VIEWS=new LinkedHashMap<>();
    private static final Map<Integer,View> MELEE=new LinkedHashMap<>();
    private static final Map<Integer,Integer> COMBOS=new HashMap<>(),LAST_SWING=new HashMap<>();
    private static net.minecraft.client.multiplayer.ClientLevel world;
    private static final ParticleOptions RED=new DustParticleOptions(new org.joml.Vector3f(.85F,.035F,.15F),.85F);
    private static long lastTick=Long.MIN_VALUE;
    private SignatureActionVisuals(){ }
    public static void init(){PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(Reference.id("signature_action"),3500,Animation::new);}
    public static void clear(){VIEWS.clear();MELEE.clear();COMBOS.clear();LAST_SWING.clear();world=null;lastTick=Long.MIN_VALUE;}
    private static boolean supported(String id){return id.equals("senbonzakura")||id.equals("benihime");}
    public static void accept(int owner,CompoundTag tag){var level=Minecraft.getInstance().level;if(level==null)return;if(world!=level){clear();world=level;}
        String id=tag.getString("identity"),kind=tag.getString("kind"),form=tag.getString("form");int age=tag.getInt("age"),duration=tag.getInt("duration");
        if(!supported(id)||!List.of("sealed","shikai","bankai").contains(form)||SignatureChoreography.duration(kind)==0||duration<1||duration>60||age<0||age>=duration){VIEWS.remove(owner);return;}
        if(VIEWS.size()>=24&&!VIEWS.containsKey(owner))VIEWS.remove(VIEWS.keySet().iterator().next());VIEWS.put(owner,new View(id,form,kind,duration,level.getGameTime()-age));
    }
    private static String identity(AbstractClientPlayer p){return p.getMainHandItem().is(ModItems.SENBONZAKURA.get())?"senbonzakura":p.getMainHandItem().is(ModItems.BENIHIME.get())?"benihime":"";}
    private static boolean available(AbstractClientPlayer p){return world==p.level()&&p.isAlive()&&!p.isSpectator()&&!p.isUsingItem()&&!p.isSleeping()&&!p.isFallFlying() && PlayerCapability.get(p).map(d->!d.getGuard().shownActive()&&!d.getGuard().shownBroken()&&!d.getStatus().isDodging()).orElse(false);}
    private static View view(AbstractClientPlayer p){if(!available(p))return null;var v=VIEWS.get(p.getId());if(v==null)v=MELEE.get(p.getId());
        final var chosen=v;return chosen!=null&&chosen.identity.equals(identity(p))&&p.level().getGameTime()-chosen.start<chosen.duration&&PlayerCapability.get(p).map(d->d.getCharacter().getZanpakutoIdentity().equals(chosen.identity)&&d.getCharacter().getActiveForm().equals(chosen.form)).orElse(false)?chosen:null;
    }
    public static boolean active(AbstractClientPlayer p){return view(p)!=null;}
    private static RyujinChoreography.Pose pose(AbstractClientPlayer p,View v,float partial){
        float age=(float)(p.level().getGameTime()-v.start)+partial,sign=p.getMainArm()==HumanoidArm.RIGHT?1:-1;
        if(v.identity.equals("benihime")){
            if(v.kind.startsWith("melee_"))return com.bleachmod.common.visual.BenihimeChoreography.melee(v.kind.charAt(6)-'0',age,v.duration,sign);
            return com.bleachmod.common.visual.BenihimeChoreography.technique(v.kind,age,v.duration,sign);
        }
        return SignatureChoreography.sample(v.kind,age,v.duration,sign);
    }
    private static final class Animation implements IAnimation {
        final AbstractClientPlayer p;Animation(AbstractClientPlayer p){this.p=p;}
        @Override public boolean isActive(){return view(p)!=null&&!ZanpakutoReleaseAnimation.active(p);}
        @Override public void setupAnim(float partial){ }
        @Override public FirstPersonMode getFirstPersonMode(float partial){return FirstPersonMode.VANILLA;}
        @Override public Vec3f get3DTransform(String part,TransformType type,float partial,Vec3f value){var v=view(p);return v==null?value:RyujinActionVisuals.transform(p,pose(p,v,partial),part,type,value);}
    }
    @SubscribeEvent public static void hand(RenderHandEvent e){var p=Minecraft.getInstance().player;if(p==null||e.getHand()!=InteractionHand.MAIN_HAND||ZanpakutoReleaseAnimation.active(p))return;var v=view(p);if(v!=null)RyujinActionVisuals.hand(e.getPoseStack(),pose(p,v,e.getPartialTick()));}
    @SubscribeEvent public static void tick(TickEvent.ClientTickEvent e){
        if(e.phase!=TickEvent.Phase.END)return;var mc=Minecraft.getInstance();if(mc.level==null||mc.player==null){clear();return;}if(world!=mc.level){clear();world=mc.level;}if(mc.isPaused()||lastTick==mc.level.getGameTime())return;lastTick=mc.level.getGameTime();
        VIEWS.values().removeIf(v->lastTick-v.start>=v.duration);MELEE.values().removeIf(v->lastTick-v.start>=v.duration);COMBOS.keySet().removeIf(id->mc.level.getEntity(id)==null);LAST_SWING.keySet().removeIf(id->mc.level.getEntity(id)==null);
        int budget=192,scale=mc.options.particles().get()==net.minecraft.client.ParticleStatus.MINIMAL?4:mc.options.particles().get()==net.minecraft.client.ParticleStatus.DECREASED?2:1;
        for(var p:mc.level.players()){
            String id=identity(p);if(!supported(id)||mc.player.distanceToSqr(p)>64*64)continue;
            if(LAST_SWING.size()>=24&&!LAST_SWING.containsKey(p.getId()))continue;var previous=LAST_SWING.put(p.getId(),p.swinging?p.swingTime:-1);
            // Vanilla swing replication supplies remote observers too; no BC hitbox/attributes are changed.
            if(!id.equals("benihime")&&p.swinging&&p.swingTime<=1&&(previous==null||previous<0||p.swingTime<previous)&&available(p)&&!VIEWS.containsKey(p.getId())&&MELEE.size()<24&&COMBOS.size()<24){int combo=COMBOS.merge(p.getId(),1,Integer::sum);String kind=id.equals("senbonzakura")?(combo%2==0?"petal_cut":"petal_stream"):"melee_"+Math.floorMod(combo-1,3);String form=PlayerCapability.get(p).map(d->d.getCharacter().getActiveForm()).orElse("sealed");MELEE.put(p.getId(),new View(id,form,kind,6,lastTick));}
            var v=view(p);if(v==null||ZanpakutoReleaseAnimation.active(p)||budget<=0)continue;
            if(v.kind.startsWith("melee_")){
                // A small blade-bound red trace; no technique ring or hitbox on a basic attack.
                var gesture=pose(p,v,0);var forward=Vec3.directionFromRotation(0,p.getYRot());
                var right=new Vec3(-forward.z,0,forward.x);float sign=p.getMainArm()==HumanoidArm.RIGHT?1:-1;
                var local=com.bleachmod.common.visual.RyujinChoreography.bladeHand(gesture,sign);
                var direction=com.bleachmod.common.visual.RyujinChoreography.bladeDirection(gesture);
                var hand=p.position().add(right.scale(local.x)).add(0,local.y,0).add(forward.scale(local.z));
                var blade=right.scale(direction.x).add(0,direction.y,0).add(forward.scale(direction.z));
                for(int i=0;i<6/scale&&budget>0;i++){
                    var at=hand.add(blade.scale(.25+i*.16));mc.level.addParticle(RED,at.x,at.y,at.z,0,.008,0);budget--;
                }
                continue;
            }
            boolean petal=id.equals("senbonzakura");int age=(int)(lastTick-v.start);var f=Vec3.directionFromRotation(0,p.getYRot());var side=new Vec3(-f.z,0,f.x);
            for(int i=0;i<12/scale && budget>0;i++){
                double a=i*Math.PI*2/(12/scale)+age*.18;Vec3 at;
                if(v.kind.equals("restructure")||v.kind.equals("reconstruction"))at=p.position().add(Math.cos(a)*.55,.3+(i%5)*.33,Math.sin(a)*.55);
                else at=p.position().add(0,1.1,0).add(f.scale(.4+i*.08)).add(side.scale(Math.sin(a)*.35));
                ParticleOptions particle=petal?ModParticles.SENBON_PETAL.get():v.kind.equals("reconstruction")&&i%4==0?ParticleTypes.END_ROD:RED;
                mc.level.addParticle(particle,at.x,at.y,at.z,petal?side.x*.018:0,.015,petal?side.z*.018:0);budget--;
            }
        }
    }
    /** Additional release motes; existing petal constructs and articulated Queen stay authoritative. */
    static void releaseParticles(AbstractClientPlayer p,String id,float progress,boolean bankai){
        var mc=Minecraft.getInstance();if(mc.level==null||mc.isPaused())return;int count=mc.options.particles().get()==net.minecraft.client.ParticleStatus.MINIMAL?4:16;
        var f=Vec3.directionFromRotation(0,p.getYRot());var side=new Vec3(-f.z,0,f.x);
        if(id.equals("senbonzakura")&&bankai&&progress>.2&&progress<.75)
            com.bleachmod.client.particle.SenbonConstructRenderer.accept(new com.bleachmod.common.network.s2c.SenbonPetalsS2C(com.bleachmod.common.network.s2c.SenbonPetalsS2C.SWORDS,p.position(),f,7,progress,4,0,p.getId()));
        for(int i=0;i<count;i++){double a=i*Math.PI*2/count+progress*7;Vec3 at;ParticleOptions particle;
            if(id.equals("senbonzakura")){double r=progress<.66?(.5+progress*3):(.5+(progress-.66)*10);at=p.position().add(Math.cos(a)*r,.15+(i%5)*.45,Math.sin(a)*r);particle=ModParticles.SENBON_PETAL.get();}
            else if(id.equals("benihime")){double r=.6+Math.sin(progress*Math.PI)*1.1;at=p.position().add(side.scale(Math.cos(a)*r)).add(f.scale(Math.sin(a)*r)).add(0,.15+(i%6)*.35,0);particle=i%5==0?ParticleTypes.END_ROD:RED;}
            else {double length=.2+1.3*(i/(double)count);at=p.position().add(side.scale(.3*(p.getMainArm()==HumanoidArm.RIGHT?1:-1))).add(f.scale(.4)).add(0,.65+length,0);particle=i%4==0?ParticleTypes.END_ROD:new DustParticleOptions(bankai?new org.joml.Vector3f(.8F,.12F,.18F):new org.joml.Vector3f(.25F,.6F,1),.7F);}
            mc.level.addParticle(particle,at.x,at.y,at.z,Math.cos(a)*.018,.025,Math.sin(a)*.018);
        }
    }
}
