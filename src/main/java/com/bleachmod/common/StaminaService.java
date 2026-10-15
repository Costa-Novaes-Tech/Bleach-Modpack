package com.bleachmod.common;

import com.bleachmod.common.data.PlayerData;
import net.minecraft.server.level.ServerPlayer;

public final class StaminaService {
    private StaminaService() { }
    public static void tick(ServerPlayer player, PlayerData data) {
        if (!player.isAlive() || player.isSpectator() || !data.getStatus().hasCreatedCharacter()) return;
        boolean moving = data.getStatus().sampleStaminaMovement(player.position());
        var resources = data.getResources();
        if (data.getStatus().isDodging()) {
            player.setSprinting(false);
            resources.markStaminaEffort();
        }
        boolean eligible = !player.isCreative() && !player.getAbilities().flying && !player.isPassenger();
        if (eligible && resources.isStaminaExhausted()) player.setSprinting(false);
        boolean effort = eligible && player.isSprinting() && moving;
        if (!resources.tickStamina(effort)) player.setSprinting(false);
    }
}
