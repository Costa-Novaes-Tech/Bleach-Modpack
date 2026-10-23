package com.bleachmod.common.technique;

import com.bleachmod.Reference;
import com.bleachmod.common.data.*;
import com.bleachmod.common.events.BleachEvents;
import com.bleachmod.common.visual.IceManifestation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid=Reference.MOD_ID)
public final class HyorinmaruReleaseEffects {
    @SubscribeEvent public static void release(BleachEvents.FormChangeEvent event) {
        ServerPlayer p=event.getPlayer();
        if (!Reference.GROUP_ZANPAKUTO.equals(event.getNewGroup()) || !RyujinReleaseEffects.shouldRelease(event.getOldForm(),event.getNewForm())) return;
        PlayerCapability.get(p).ifPresent(d->{
            if (!CharacterData.HYORINMARU.equals(d.getCharacter().getZanpakutoIdentity())) return;
            boolean bankai=Reference.FORM_BANKAI.equals(event.getNewForm());
            IceManifestation.release(p, bankai);
            p.serverLevel().playSound(null,p.blockPosition(),SoundEvents.GLASS_BREAK,p.getSoundSource(),1,bankai?0.6F:1);
            if(bankai) {
                p.serverLevel().playSound(null,p.blockPosition(),SoundEvents.ENDER_DRAGON_GROWL,p.getSoundSource(),0.3F,1.3F);
            }
        });
    }
    @SubscribeEvent public static void aura(TickEvent.PlayerTickEvent event) {
        if(event.player instanceof ServerPlayer p && ReleaseAnimationService.busy(p)) return;
        if(event.phase!=TickEvent.Phase.END || !(event.player instanceof ServerPlayer p) || p.tickCount%8!=0 || !p.isAlive() || p.isSpectator()) return;
        PlayerCapability.get(p).ifPresent(d->{
            if(!d.getStatus().hasCreatedCharacter() || !CharacterData.HYORINMARU.equals(d.getCharacter().getZanpakutoIdentity())) return;
            String form=d.getCharacter().getActiveForm();
            boolean bankai=Reference.FORM_BANKAI.equals(form);
            if(!bankai && !Reference.FORM_SHIKAI.equals(form) && !IceArmorService.active(p)) return;
            IceManifestation.aura(p, bankai);
        });
    }
}
