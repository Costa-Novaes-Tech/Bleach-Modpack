package com.bleachmod.common;

import com.bleachmod.Reference;
import com.bleachmod.common.data.PlayerCapability;
import com.bleachmod.common.network.SyncHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.common.Mod;

/** Transient server-owned combat state. No regeneration, skill, inventory or camera changes. */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public final class CombatStateService {
    /** Prototype timeout, deliberately not a final design/balance decision. */
    public static final int QUIET_TICKS = com.bleachmod.common.data.StatusData.COMBAT_IDLE_TICKS;
    private CombatStateService() { }
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void damage(LivingDamageEvent event) {
        LivingEntity target = event.getEntity();
        if (event.isCanceled() || !Float.isFinite(event.getAmount()) || event.getAmount() <= 0
                || !(event.getSource().getEntity() instanceof LivingEntity attacker) || attacker == target
                || attacker.level() != target.level() || target instanceof ArmorStand || attacker instanceof ArmorStand
                || attacker.isAlliedTo(target) || target.isAlliedTo(attacker)) return;
        if (attacker instanceof ServerPlayer player) enter(player);
        if (target instanceof ServerPlayer player) enter(player);
    }
    private static void enter(ServerPlayer player) {
        if (!player.isAlive() || player.isSpectator()) return;
        PlayerCapability.get(player).ifPresent(data -> {
            if (!data.getStatus().hasCreatedCharacter()) return;
            data.getStatus().refreshCombat(QUIET_TICKS);
            SyncHelper.resources(player);
        });
    }
    @SubscribeEvent public static void death(LivingDeathEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) PlayerCapability.get(player).ifPresent(data -> {
            data.getStatus().clearCombat(); SyncHelper.resources(player);
        });
    }
}
