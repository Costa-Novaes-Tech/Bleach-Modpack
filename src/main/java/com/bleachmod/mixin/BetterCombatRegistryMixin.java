package com.bleachmod.mixin;

import com.bleachmod.common.compat.BetterCombatCompat;
import net.bettercombat.api.WeaponAttributes;
import net.bettercombat.logic.WeaponRegistry;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** BC's automatic weapon fallback must not silently enable all homologated kits. */
@Mixin(value=WeaponRegistry.class,remap=false)
public abstract class BetterCombatRegistryMixin {
    @Inject(method="getAttributes(Lnet/minecraft/world/item/ItemStack;)Lnet/bettercombat/api/WeaponAttributes;",at=@At("HEAD"),cancellable=true)
    private static void bleachmod$pilotOnly(ItemStack stack,CallbackInfoReturnable<WeaponAttributes> cir){
        if(stack!=null && BetterCombatCompat.excluded(stack))cir.setReturnValue(null);
    }
}
