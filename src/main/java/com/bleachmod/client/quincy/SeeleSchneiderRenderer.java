package com.bleachmod.client.quincy;

import com.bleachmod.Reference;
import com.bleachmod.common.quincy.SeeleSchneiderEntity;
import com.bleachmod.init.ModItems;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class SeeleSchneiderRenderer extends EntityRenderer<SeeleSchneiderEntity> {
    private final ItemRenderer itemRenderer;

    public SeeleSchneiderRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.itemRenderer = Minecraft.getInstance().getItemRenderer();
    }

    @Override
    public void render(SeeleSchneiderEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();

        Vec3 motion = entity.getDeltaMovement();
        if (motion.lengthSqr() > 1e-4) {
            float yRot = (float) (Mth.atan2(motion.x, motion.z) * (180F / (float) Math.PI));
            float xRot = (float) (Mth.atan2(motion.y, motion.horizontalDistance()) * (180F / (float) Math.PI));
            poseStack.mulPose(Axis.YP.rotationDegrees(yRot - 90.0F));
            poseStack.mulPose(Axis.ZP.rotationDegrees(xRot));
        }

        poseStack.scale(0.85F, 0.85F, 0.85F);
        ItemStack stack = new ItemStack(ModItems.SEELE_SCHNEIDER.get());
        itemRenderer.renderStatic(stack, ItemDisplayContext.GROUND, packedLight, OverlayTexture.NO_OVERLAY, poseStack, buffer, entity.level(), entity.getId());

        poseStack.popPose();
        super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(SeeleSchneiderEntity entity) {
        return Reference.id("textures/item/seele_schneider.png");
    }
}
