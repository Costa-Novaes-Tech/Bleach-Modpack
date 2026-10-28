package com.bleachmod.client.quincy;

import com.bleachmod.Reference;
import com.bleachmod.common.quincy.QuincyArrowEntity;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class QuincyArrowRenderer extends ArrowRenderer<QuincyArrowEntity> {
    public static final ResourceLocation TEXTURE = Reference.id("textures/entity/projectiles/quincy_arrow.png");

    public QuincyArrowRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(QuincyArrowEntity entity) {
        return TEXTURE;
    }
}
