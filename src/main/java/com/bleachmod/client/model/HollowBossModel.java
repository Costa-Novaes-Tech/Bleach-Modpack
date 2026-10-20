package com.bleachmod.client.model;

import com.bleachmod.Reference;
import com.bleachmod.entity.HollowBossEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

/** Authored at 16 model units/block, matching the 5.4-block collision height. */
public final class HollowBossModel extends GeoModel<HollowBossEntity> {
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
