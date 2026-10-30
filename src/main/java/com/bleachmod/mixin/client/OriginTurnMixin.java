package com.bleachmod.mixin.client;

import com.bleachmod.client.cinematic.OriginCinematicClient;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Mouse deltas never alter the owner's underlying yaw while the directed camera is active. */
@Mixin(Entity.class)
public abstract class OriginTurnMixin {
    @Inject(method="turn",at=@At("HEAD"),cancellable=true)
    private void bleachmod$originTurn(double yaw,double pitch,CallbackInfo ci) {
        if((Object)this==Minecraft.getInstance().player&&OriginCinematicClient.active())ci.cancel();
    }
}
