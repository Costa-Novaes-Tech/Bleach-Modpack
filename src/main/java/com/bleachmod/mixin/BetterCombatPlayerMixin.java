package com.bleachmod.mixin;

import com.bleachmod.common.compat.BetterCombatCompat;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value=Player.class,priority=900)
public abstract class BetterCombatPlayerMixin {
    @Dynamic("Method supplied by Better Combat 1.9.0 PlayerEntityMixin")
    @Inject(method="setComboCount",at=@At("TAIL"),remap=false)
    private void bleachmod$comboScope(int combo,CallbackInfo ci){BetterCombatCompat.comboChanged((Player)(Object)this,combo);}
}
