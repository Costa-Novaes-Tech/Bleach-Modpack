package com.bleachmod.common.network.c2s;

import com.bleachmod.common.data.PlayerCapability;
import com.bleachmod.common.quincy.QuincyTechniqueService;
import com.bleachmod.common.technique.TechniqueService;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record ExecuteTechniqueSlotC2S(int slot, int forward, int left) {
    public ExecuteTechniqueSlotC2S(int slot) {
        this(slot, 0, 0);
    }

    public static void encode(ExecuteTechniqueSlotC2S message, FriendlyByteBuf buffer) {
        buffer.writeByte(message.slot);
        buffer.writeByte(message.forward);
        buffer.writeByte(message.left);
    }

    public static ExecuteTechniqueSlotC2S decode(FriendlyByteBuf buffer) {
        int slot = buffer.readUnsignedByte();
        int forward = buffer.isReadable() ? buffer.readByte() : 0;
        int left = buffer.isReadable() ? buffer.readByte() : 0;
        return new ExecuteTechniqueSlotC2S(slot, forward, left);
    }

    public static void handle(ExecuteTechniqueSlotC2S message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player == null || message.slot < 1 || message.slot > 4) {
                return;
            }
            PlayerCapability.get(player).ifPresent(data -> {
                if (data.getStatus().allowAction(player.level().getGameTime())) {
                    if (QuincyTechniqueService.isQuincy(data)) {
                        QuincyTechniqueService.executeSlot(player, data, message.slot, message.forward, message.left);
                    } else {
                        TechniqueService.executeSlot(player, data, message.slot);
                    }
                }
            });
        });
        context.setPacketHandled(true);
    }
}
