package com.bleachmod.client.model;

import com.bleachmod.Reference;
import com.bleachmod.common.visual.HollowBossEntrance;
import com.bleachmod.common.visual.TwoBonePose;
import com.bleachmod.entity.HollowBossEntity;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import java.util.LinkedHashMap;
import java.util.Map;

/** Authored at 16 model units/block, matching the 5.4-block collision height. */
public final class HollowBossModel extends GeoModel<HollowBossEntity> {
    private record Base(float x,float y,float z,float rx,float ry,float rz) {
        Base(GeoBone b) { this(b.getPosX(),b.getPosY(),b.getPosZ(),b.getRotX(),b.getRotY(),b.getRotZ()); }
        void restore(GeoBone b) {
            b.setPosX(x);b.setPosY(y);b.setPosZ(z);b.setRotX(rx);b.setRotY(ry);b.setRotZ(rz);
        }
    }
    private final Map<GeoBone,Base> entranceBase=new LinkedHashMap<>();

    @Override public void handleAnimations(HollowBossEntity boss,long id,AnimationState<HollowBossEntity> state) {
        // Undo our previous overlay BEFORE GeckoLib evaluates the next entity/frame,
        // rather than clearing a newly evaluated melee/charge/slam pose afterwards.
        entranceBase.forEach((bone,base)->base.restore(bone));entranceBase.clear();
        super.handleAnimations(boss,id,state);
    }
    private GeoBone edit(String name) {
        var b=getBone(name).orElseThrow();entranceBase.computeIfAbsent(b,Base::new);return b;
    }
    private GeoBone rotate(String name,float x,float y,float z) {
        var b=edit(name);b.setRotX(rad(x));b.setRotY(rad(y));b.setRotZ(rad(z));return b;
    }
    private static float rad(float degrees) { return (float)Math.toRadians(degrees); }

    @Override public void setCustomAnimations(HollowBossEntity boss,long id,AnimationState<HollowBossEntity> state) {
        super.setCustomAnimations(boss,id,state);
        getBone("right_arm").ifPresent(arm->{arm.setHidden(boss.isOriginArmLost());arm.setChildrenHidden(boss.isOriginArmLost());});
        if(!boss.isEntering()) return;
        float t=boss.getEntranceAge(state.getPartialTick()),fade=HollowBossEntrance.settle(t);
        var body=rotate("body",HollowBossEntrance.bodyPitch(t),HollowBossEntrance.bodyYaw(t),HollowBossEntrance.bodyRoll(t));
        body.setPosX(HollowBossEntrance.bodyX(t));body.setPosY(HollowBossEntrance.bodyY(t));
        rotate("chest",HollowBossEntrance.chestPitch(t),HollowBossEntrance.chestYaw(t),-HollowBossEntrance.bodyRoll(t)*.3F);
        rotate("head",HollowBossEntrance.headPitch(t),HollowBossEntrance.headYaw(t),-HollowBossEntrance.bodyRoll(t)*.5F);
        rotate("jaw",HollowBossEntrance.jaw(t),0,0);
        rotate("dorsal_fin",0,(float)Math.sin((t-8)*.14)*2*HollowBossEntrance.weight(t),0);
        for(String side:new String[]{"right","left"}) {
            boolean right=side.equals("right");var hand=HollowBossEntrance.hand(t,right);
            reach(side+"_arm",side+"_arm_lower",side+"_hand",hand,t,true,fade);
            reach(side+"_leg",side+"_leg_lower",side+"_foot",HollowBossEntrance.foot(t,right),t,false,fade);
            for(int i=0;i<4;i++) rotate(side+"_finger_"+i,(20+i*4)*hand.lift()*fade,0,0);
            rotate(side+"_thumb",0,-12*hand.lift()*fade,0);
        }
    }

    private void reach(String upperName,String lowerName,String endName,HollowBossEntrance.Contact contact,
                       float age,boolean arm,float fade) {
        var upper=rotate(upperName,0,0,0);var lower=rotate(lowerName,0,0,0);var end=rotate(endName,0,0,0);
        // Fixed targets are in the final actor frame. Compensate root advance to keep
        // supported palms/feet on the same patch of ground while the torso moves.
        var target=new Vector3f(end.getPivotX(),contact.y(),contact.z()+HollowBossEntrance.retreat(age)*16);
        new Matrix4f(matrix(upper.getParent())).invert().transformPosition(target);
        var solved=TwoBonePose.solve(pivot(upper),pivot(lower),pivot(end),target,new Vector3f(0,0,arm?1:-1));
        rotation(upper,solved.upper(),fade);rotation(lower,solved.lower(),fade);
        var orientation=matrix(lower).getNormalizedRotation(new Quaternionf()).invert();
        orientation.rotateX(rad((arm?-8:18)*contact.lift()));
        rotation(end,orientation,fade);
    }
    private static void rotation(GeoBone bone,Quaternionf q,float fade) {
        var e=TwoBonePose.eulerZYX(q);bone.setRotX(e.x*fade);bone.setRotY(e.y*fade);bone.setRotZ(e.z*fade);
    }
    private static Vector3f pivot(GeoBone b) { return new Vector3f(b.getPivotX(),b.getPivotY(),b.getPivotZ()); }
    private static Matrix4f matrix(GeoBone b) {
        if(b==null)return new Matrix4f();
        return matrix(b.getParent()).translate(-b.getPosX(),b.getPosY(),b.getPosZ())
                .translate(b.getPivotX(),b.getPivotY(),b.getPivotZ())
                .rotateZ(b.getRotZ()).rotateY(b.getRotY()).rotateX(b.getRotX())
                .translate(-b.getPivotX(),-b.getPivotY(),-b.getPivotZ());
    }
    @Override public ResourceLocation getModelResource(HollowBossEntity boss) {
        return Reference.id("geo/hollow_boss.geo.json");
    }
    @Override public ResourceLocation getTextureResource(HollowBossEntity boss) {
        return Reference.id("textures/entity/hollow_boss.png");
    }
    @Override public ResourceLocation getAnimationResource(HollowBossEntity boss) {
        return Reference.id("animations/hollow_boss.animation.json");
    }
}
