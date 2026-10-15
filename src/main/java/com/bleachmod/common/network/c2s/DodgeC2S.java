package com.bleachmod.common.network.c2s;

import com.bleachmod.common.DodgeService;
import com.bleachmod.common.data.PlayerCapability;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;

public record DodgeC2S(int forward, int left) {
    public static void encode(DodgeC2S message, FriendlyByteBuf buffer) {
        buffer.writeByte(message.forward);
        buffer.writeByte(message.left);
    }
    public static DodgeC2S decode(FriendlyByteBuf buffer) {
        return new DodgeC2S(buffer.readByte(), buffer.readByte());
    }
    public static void handle(DodgeC2S message, Supplier<NetworkEvent.Context> supplier) {
        var context = supplier.get();
        context.enqueueWork(() -> {
            var player = context.getSender();
            if (player != null) PlayerCapability.get(player).ifPresent(data -> {
                if (data.getStatus().allowAction(player.level().getGameTime()))
                    DodgeService.request(player, data, message.forward, message.left);
            });
        });
        context.setPacketHandled(true);
    }
}
