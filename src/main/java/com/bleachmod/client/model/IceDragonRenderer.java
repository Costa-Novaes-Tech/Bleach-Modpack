package com.bleachmod.client.model;

import com.bleachmod.Reference;
import com.bleachmod.entity.IceDragonEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public final class IceDragonRenderer extends GeoEntityRenderer<IceDragonEntity> {
    public IceDragonRenderer(EntityRendererProvider.Context context) { super(context, new Model()); }
    @Override public void render(IceDragonEntity dragon, float yaw, float partial, com.mojang.blaze3d.vertex.PoseStack pose,
                                 net.minecraft.client.renderer.MultiBufferSource buffers, int light) {
        pose.pushPose(); float scale = dragon.getPhantomSize() == IceDragonEntity.BANKAI_SIZE ? IceDragonEntity.BANKAI_VISUAL_SCALE : 1.35F;
        pose.scale(scale,scale,scale);
        super.render(dragon,yaw,partial,pose,buffers,light);pose.popPose();
    }
    private static final class Model extends GeoModel<IceDragonEntity>{
        private static final ResourceLocation HYDRA_GEO = Reference.id("geo/ice_hydra.geo.json");
        private static final ResourceLocation HYDRA_TEX = Reference.id("textures/entity/ice_hydra.png");
        private static final ResourceLocation HYDRA_ANIM = Reference.id("animations/ice_hydra.animation.json");
        private static final ResourceLocation DRAGON_GEO = Reference.id("geo/ice_dragon.geo.json");
        private static final ResourceLocation DRAGON_TEX = new ResourceLocation("minecraft", "textures/block/packed_ice.png");
        private static final ResourceLocation DRAGON_ANIM = Reference.id("animations/ice_dragon.animation.json");

        @Override public ResourceLocation getModelResource(IceDragonEntity d){
            return d.getPhantomSize() == IceDragonEntity.BANKAI_SIZE ? HYDRA_GEO : DRAGON_GEO;
        }
        @Override public ResourceLocation getTextureResource(IceDragonEntity d){
            return d.getPhantomSize() == IceDragonEntity.BANKAI_SIZE ? HYDRA_TEX : DRAGON_TEX;
        }
        @Override public ResourceLocation getAnimationResource(IceDragonEntity d){
            return d.getPhantomSize() == IceDragonEntity.BANKAI_SIZE ? HYDRA_ANIM : DRAGON_ANIM;
        }
        @Override public net.minecraft.client.renderer.RenderType getRenderType(IceDragonEntity d,ResourceLocation texture){
            return net.minecraft.client.renderer.RenderType.entityTranslucent(texture);
        }
    }
}
