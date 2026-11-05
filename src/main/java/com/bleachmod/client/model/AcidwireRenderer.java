package com.bleachmod.client.model;

import com.bleachmod.Reference;
import com.bleachmod.entity.AcidwireEntity;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

/** Supplied model at its authored scale; the long tail remains visible past torso collision. */
public final class AcidwireRenderer extends GeoEntityRenderer<AcidwireEntity> {
    public AcidwireRenderer(EntityRendererProvider.Context context) { super(context, new Model()); shadowRadius = .85F; }
    @Override public boolean shouldRender(AcidwireEntity entity, Frustum frustum, double x, double y, double z) {
        return frustum.isVisible(entity.getBoundingBox().inflate(4));
    }
    private static final class Model extends GeoModel<AcidwireEntity> {
        @Override public ResourceLocation getModelResource(AcidwireEntity entity) { return Reference.id("geo/acidwire.geo.json"); }
        @Override public ResourceLocation getTextureResource(AcidwireEntity entity) { return Reference.id("textures/entity/acidwire.png"); }
        @Override public ResourceLocation getAnimationResource(AcidwireEntity entity) { return Reference.id("animations/acidwire.animation.json"); }
    }
}
