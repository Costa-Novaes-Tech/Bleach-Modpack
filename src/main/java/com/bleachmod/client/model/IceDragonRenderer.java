package com.bleachmod.client.model;

import com.bleachmod.Reference;
import com.bleachmod.entity.IceDragonEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public final class IceDragonRenderer extends GeoEntityRenderer<IceDragonEntity> {
    public IceDragonRenderer(EntityRendererProvider.Context context){super(context,new Model());shadowRadius=0;}
    @Override public void render(IceDragonEntity dragon,float yaw,float partial,com.mojang.blaze3d.vertex.PoseStack pose,net.minecraft.client.renderer.MultiBufferSource buffers,int light){
        pose.pushPose();float scale=dragon.getPhantomSize()==IceDragonEntity.BANKAI_SIZE?IceDragonEntity.BANKAI_VISUAL_SCALE:.85F;pose.scale(scale,scale,scale);
        super.render(dragon,yaw,partial,pose,buffers,light);pose.popPose();
    }
    private static final class Model extends GeoModel<IceDragonEntity>{
        @Override public ResourceLocation getModelResource(IceDragonEntity d){return Reference.id("geo/ice_dragon.geo.json");}
        @Override public ResourceLocation getTextureResource(IceDragonEntity d){return new ResourceLocation("minecraft","textures/block/packed_ice.png");}
        @Override public ResourceLocation getAnimationResource(IceDragonEntity d){return Reference.id("animations/ice_dragon.animation.json");}
        @Override public net.minecraft.client.renderer.RenderType getRenderType(IceDragonEntity d,ResourceLocation texture){return net.minecraft.client.renderer.RenderType.entityTranslucent(texture);}
    }
}
