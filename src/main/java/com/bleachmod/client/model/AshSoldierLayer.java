package com.bleachmod.client.model;

import com.bleachmod.Reference;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.SkeletonModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.monster.AbstractSkeleton;

/** Scoped by synchronized Minami IDs. Vanilla skeletons keep their own renderer/skin. */
public final class AshSoldierLayer extends RenderLayer<AbstractSkeleton,SkeletonModel<AbstractSkeleton>> {
    public AshSoldierLayer(RenderLayerParent<AbstractSkeleton,SkeletonModel<AbstractSkeleton>> parent){super(parent);}
    @Override public void render(PoseStack pose,MultiBufferSource buffers,int light,AbstractSkeleton mob,
            float swing,float amount,float partial,float age,float yaw,float pitch){
        if(!RyujinActionVisuals.ashSoldier(mob.getId()) || mob.isInvisible())return;
        pose.pushPose();pose.scale(1.003F,1.003F,1.003F);
        getParentModel().renderToBuffer(pose,buffers.getBuffer(RenderType.entityCutoutNoCull(Reference.id("textures/entity/ash_soldier.png"))),light,OverlayTexture.NO_OVERLAY,1,1,1,1);
        getParentModel().renderToBuffer(pose,buffers.getBuffer(RenderType.eyes(Reference.id("textures/entity/ash_soldier_embers.png"))),LightTexture.FULL_BRIGHT,OverlayTexture.NO_OVERLAY,1,1,1,.8F);
        pose.popPose();
    }
}
