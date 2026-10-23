package com.bleachmod.common.network.c2s;

import com.bleachmod.common.data.CharacterData;
import com.bleachmod.common.data.PlayerCapability;
import com.bleachmod.common.data.PlayerData;
import com.bleachmod.common.evolution.TransformationsHelper;
import com.bleachmod.common.network.SyncHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record SelectFormC2S(String group, String form) {
    public static void encode(SelectFormC2S msg, FriendlyByteBuf buf) {
        buf.writeUtf(msg.group);
        buf.writeUtf(msg.form);
    }

    public static SelectFormC2S decode(FriendlyByteBuf buf) {
        return new SelectFormC2S(buf.readUtf(32), buf.readUtf(32));
    }

    public static void handle(SelectFormC2S msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) {
                return;
            }
            PlayerCapability.get(player).ifPresent(data -> apply(player, data, msg.group, msg.form));
        });
        ctx.get().setPacketHandled(true);
    }

    /**
     * Picks the release target. Only the charge built toward the previous target is dropped:
     * cooldowns, guard and active effects belong to combat, not to the selection.
     */
    public static boolean apply(ServerPlayer player, PlayerData data, String group, String form) {
        if (!data.getStatus().hasCreatedCharacter() || !player.isAlive()) return false;
        if (!TransformationsHelper.isSelectable(data, group, form)) return false;
        CharacterData character = data.getCharacter();
        if (group.equalsIgnoreCase(character.getSelectedFormGroup()) && form.equalsIgnoreCase(character.getSelectedForm())) return false;
        if (!data.getStatus().allowUiAction(player.level().getGameTime())) return false;
        data.getResources().setActionCharge(0);
        data.getStatus().setActionCharging(false);
        character.setSelectedForm(group, form);
        SyncHelper.full(player);
        return true;
    }
}
