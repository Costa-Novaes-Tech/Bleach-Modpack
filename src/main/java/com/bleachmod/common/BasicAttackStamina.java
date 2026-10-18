package com.bleachmod.common;

import com.bleachmod.Reference;
import com.bleachmod.common.data.PlayerCapability;
import com.bleachmod.common.network.SyncHelper;
import com.bleachmod.init.ModItems;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/** Charge the manual attack entry point, not damage events shared by elemental techniques. */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public final class BasicAttackStamina {
    private BasicAttackStamina() { }
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void attack(AttackEntityEvent event) {
        if (event.isCanceled() || !(event.getEntity() instanceof ServerPlayer player)
                || !player.isAlive() || player.isCreative() || player.isSpectator()
                || !(event.getTarget() instanceof LivingEntity target) || !target.isAlive()
                || target == player || target.level() != player.level() || player.isAlliedTo(target)) return;
        var weapon = player.getMainHandItem();
        if (!weapon.is(ModItems.ASAUCHI.get()) && !weapon.is(ModItems.RYUJIN_JAKKA.get())
                && !weapon.is(ModItems.HYORINMARU.get()) && !weapon.is(ModItems.SENBONZAKURA.get()) && !weapon.is(ModItems.BENIHIME.get())
                && !weapon.is(ModItems.NOZARASHI.get())) return;
        PlayerCapability.get(player).ifPresent(data -> {
            if (!data.getStatus().hasCreatedCharacter()) return;
            data.getResources().spendBasicAttackStamina();
            if (data.getResources().isStaminaExhausted()) player.setSprinting(false);
            SyncHelper.resources(player);
        });
    }
}
