package com.bleachmod.client.quincy;

import com.bleachmod.Reference;
import com.bleachmod.common.quincy.SprengerNodeEntity;
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
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class SprengerNodeRenderer extends EntityRenderer<SprengerNodeEntity> {
    private final ItemRenderer itemRenderer;

    public SprengerNodeRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.itemRenderer = Minecraft.getInstance().getItemRenderer();
    }

    @Override
    public void render(SprengerNodeEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();

        // Planted vertically in the ground point down
        poseStack.translate(0, 0.4D, 0);
        poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
        poseStack.mulPose(Axis.YP.rotationDegrees(entity.tickCount * 2.0F)); // slow mystical rotation
        poseStack.scale(1.2F, 1.2F, 1.2F);

        ItemStack stack = new ItemStack(ModItems.SEELE_SCHNEIDER.get());
        itemRenderer.renderStatic(stack, ItemDisplayContext.GROUND, packedLight, OverlayTexture.NO_OVERLAY, poseStack, buffer, entity.level(), entity.getId());

        poseStack.popPose();
        super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(SprengerNodeEntity entity) {
        return Reference.id("textures/item/seele_schneider.png");
    }
}
