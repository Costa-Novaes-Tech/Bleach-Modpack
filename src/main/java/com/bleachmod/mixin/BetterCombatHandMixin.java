package com.bleachmod.mixin;

import com.bleachmod.common.compat.BetterCombatCompat;
import net.bettercombat.logic.PlayerAttackHelper;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value=PlayerAttackHelper.class,remap=false)
public abstract class BetterCombatHandMixin {
    @Inject(method="isDualWielding",at=@At("HEAD"),cancellable=true)
    private static void bleachmod$singleBoundWeapon(Player p,CallbackInfoReturnable<Boolean> cir){
        if(BetterCombatCompat.managed(p.getMainHandItem()) || BetterCombatCompat.managed(p.getOffhandItem()))cir.setReturnValue(false);
    }
}
