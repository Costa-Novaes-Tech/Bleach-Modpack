package com.bleachmod.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Read-only renderer probe; writes only when the explicit disposable QA flags are enabled. */
@Mixin(ItemInHandRenderer.class)
public abstract class HeldWeaponQaMixin {
    @Inject(method="renderItem",at=@At("HEAD"))
    private void bleachmod$heldWeaponProbe(LivingEntity entity, ItemStack item, ItemDisplayContext context,
            boolean left, PoseStack stack, MultiBufferSource buffers, int light, CallbackInfo ci){
        com.bleachmod.client.cinematic.HeldWeaponQaProbe.capture(entity,item,context,left,stack);
    }
}
