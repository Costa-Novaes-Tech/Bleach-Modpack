package com.bleachmod.mixin.client;

import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Only hide the vanilla death puff and death tilt for a confirmed incineration held on this client. */
@Mixin(LivingEntity.class)
public abstract class HigashiDeathParticleMixin {
    @Inject(method="handleEntityEvent",at=@At("HEAD"),cancellable=true)
    private void bleachmod$delayDeathPuff(byte event,CallbackInfo ci){
        int id = ((LivingEntity)(Object)this).getId();
        if((event==60 || event==3) && com.bleachmod.client.model.HigashiExecutionVisuals.isExecuting(id))ci.cancel();
    }
}
