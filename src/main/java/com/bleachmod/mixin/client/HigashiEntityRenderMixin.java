package com.bleachmod.mixin.client;

import com.bleachmod.client.model.HigashiExecutionVisuals;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Unequivocally suppresses the original entity from being rendered by Minecraft's master entity pass,
 * including base model, living/geckolib layers, fire billboards, shadows, and nametags, while
 * Higashi's controlled execution representation is active.
 */
@Mixin(EntityRenderDispatcher.class)
public abstract class HigashiEntityRenderMixin {
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private <E extends Entity> void bleachmod$suppressOriginalExecutingEntity(
            E entity, double x, double y, double z, float rotationYaw, float partialTicks,
            PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, CallbackInfo ci) {
        if (HigashiExecutionVisuals.shouldSuppressEntity(entity.getId())) {
            ci.cancel();
        }
    }
}
