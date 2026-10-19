package com.bleachmod.common.network.c2s;

import com.bleachmod.common.appearance.AppearanceService;
import com.bleachmod.common.data.PlayerCapability;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/** New look for an existing character; accepted only inside an editing window opened by the server. */
public record UpdateAppearanceC2S(CompoundTag appearance) {
    public static void encode(UpdateAppearanceC2S msg, FriendlyByteBuf buf) { buf.writeNbt(msg.appearance); }

    public static UpdateAppearanceC2S decode(FriendlyByteBuf buf) { return new UpdateAppearanceC2S(buf.readNbt()); }

    public static void handle(UpdateAppearanceC2S msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null || !AppearanceService.consumeEdit(player)) return;
            PlayerCapability.get(player).ifPresent(data -> {
                if (data.getStatus().hasCreatedCharacter()) AppearanceService.apply(player, data, msg.appearance);
            });
        });
        ctx.get().setPacketHandled(true);
    }
}
