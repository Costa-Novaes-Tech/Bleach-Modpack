package com.bleachmod.client.model;

import com.bleachmod.Reference;
import com.bleachmod.entity.HollowEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

/** All variants use authored geometry at world scale, matching the entity's collision height. */
public final class HollowRenderer extends GeoEntityRenderer<HollowEntity> {
    public HollowRenderer(EntityRendererProvider.Context context) {
        super(context, new Model());
        shadowRadius = .6F;
    }

    @Override public void render(HollowEntity hollow, float yaw, float partial,
            com.mojang.blaze3d.vertex.PoseStack pose,
            net.minecraft.client.renderer.MultiBufferSource buffers, int light) {
        shadowRadius = hollow.getVariant().width * .5F;
        super.render(hollow, yaw, partial, pose, buffers, light);
    }

    private static final class Model extends GeoModel<HollowEntity> {
        @Override public ResourceLocation getModelResource(HollowEntity hollow) {
            return Reference.id("geo/hollow_" + hollow.getVariant().id + ".geo.json");
        }
        @Override public ResourceLocation getTextureResource(HollowEntity hollow) {
            return Reference.id("textures/entity/hollow_" + hollow.getVariant().id + ".png");
        }
        @Override public ResourceLocation getAnimationResource(HollowEntity hollow) {
            return Reference.id("animations/hollow.animation.json");
        }
    }
}
