package com.bleachmod.mixin.client;

import com.bleachmod.client.cinematic.RyujinBankaiEntranceClient;
import net.minecraft.client.sounds.MusicManager;
import net.minecraft.sounds.Music;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MusicManager.class)
public abstract class MusicManagerMixin {
    @Inject(method = "startPlaying", at = @At("HEAD"), cancellable = true)
    private void bleachmod$keepVanillaMusicOutOfRyujinScore(Music music, CallbackInfo ci) {
        if ("minecraft".equals(music.getEvent().value().getLocation().getNamespace())
                && RyujinBankaiEntranceClient.suppressesVanillaMusic()) {
            ci.cancel();
        }
    }
}
