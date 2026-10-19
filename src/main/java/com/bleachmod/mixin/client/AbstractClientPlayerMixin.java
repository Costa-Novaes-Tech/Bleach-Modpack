package com.bleachmod.mixin.client;

import com.bleachmod.client.appearance.AppearanceClient;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Swaps in the composed character skin and arm model everywhere the game asks for a player's skin. */
@Mixin(AbstractClientPlayer.class)
public abstract class AbstractClientPlayerMixin {
    @Inject(method = "getSkinTextureLocation", at = @At("HEAD"), cancellable = true)
    private void bleachmod$skin(CallbackInfoReturnable<ResourceLocation> cir) {
        ResourceLocation skin = AppearanceClient.skin((AbstractClientPlayer) (Object) this);
        if (skin != null) cir.setReturnValue(skin);
    }

    @Inject(method = "getModelName", at = @At("HEAD"), cancellable = true)
    private void bleachmod$model(CallbackInfoReturnable<String> cir) {
        String model = AppearanceClient.model((AbstractClientPlayer) (Object) this);
        if (model != null) cir.setReturnValue(model);
    }
}
