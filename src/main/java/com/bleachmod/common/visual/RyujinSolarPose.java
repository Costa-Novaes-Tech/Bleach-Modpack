package com.bleachmod.common.visual;

import net.minecraft.world.phys.Vec3;

/** One presentation-space centre for the model, corona and optional light. No gameplay. */
public final class RyujinSolarPose {
    public static final float MODEL_SCALE=1.35F;
    public static final double MODEL_BASE_Y=-2.5;
    private RyujinSolarPose(){ }
    /** Isolated development pilot until the new model/material passes artistic review. */
    public static boolean pilot(){return RyujinSealFlow.enabled()||Boolean.getBoolean("bleachmod.ryujinSolarPosePilot");}
    /** Original source pivot (0,80,50) plus its held translation (0,18,18). */
    public static Vec3 heldSourceCenter(Vec3 anchor,Vec3 forward){
        return anchor.subtract(forward.scale(68*MODEL_SCALE/16)).add(0,98*MODEL_SCALE/16+MODEL_BASE_Y,0);
    }
    public static Vec3 center(Vec3 anchor,Vec3 forward,double phase,RyujinBankaiPresentation.Blade blade){
        Vec3 target=blade==null?anchor.add(0,1,0):blade.hand().lerp(blade.tip(),.55);
        return RyujinBankaiPresentation.sunCenter(anchor,forward).lerp(target,RyujinBankaiTimeline.progress(phase,32,52));
    }
}
