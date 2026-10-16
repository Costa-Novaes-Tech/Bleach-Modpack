package com.bleachmod.client.compat;

import com.bleachmod.Reference;
import com.bleachmod.client.input.ModKeybinds;
import com.bleachmod.common.compat.BetterCombatCompat;
import net.bettercombat.api.MinecraftClient_BetterCombat;
import net.bettercombat.api.client.BetterCombatClientEvents;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid=Reference.MOD_ID,value=Dist.CLIENT)
public final class BetterCombatClientCompat {
    private BetterCombatClientCompat(){ }
    public static void init(){BetterCombatClientEvents.ATTACK_START.register((p,hand)->cancelIfBlocked());}
    private static void cancelIfBlocked(){var mc=Minecraft.getInstance();var p=mc.player;
        if(p!=null && BetterCombatCompat.pilot(p.getMainHandItem()) && (!BetterCombatCompat.canAttack(p) || ModKeybinds.GUARD.isDown() || com.bleachmod.common.data.PlayerCapability.get(p).map(d->d.getGuard().shownActive() || d.getGuard().shownBroken()).orElse(false)))
            ((MinecraftClient_BetterCombat)mc).cancelUpswing();
    }
    @SubscribeEvent public static void tick(TickEvent.ClientTickEvent event){if(event.phase==TickEvent.Phase.START)cancelIfBlocked();}
}
