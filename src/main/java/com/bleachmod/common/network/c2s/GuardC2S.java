package com.bleachmod.common.network.c2s;

import com.bleachmod.common.GuardService;
import com.bleachmod.common.data.PlayerCapability;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;

public record GuardC2S(boolean held) {
    public static void encode(GuardC2S message, FriendlyByteBuf buffer) { buffer.writeBoolean(message.held); }
    public static GuardC2S decode(FriendlyByteBuf buffer) { return new GuardC2S(buffer.readBoolean()); }
    public static void handle(GuardC2S message, Supplier<NetworkEvent.Context> supplier) {
        var context = supplier.get();
        context.enqueueWork(() -> {
            var p = context.getSender();
            if (p != null) PlayerCapability.get(p).ifPresent(d -> GuardService.request(p, d, message.held));
        });
        context.setPacketHandled(true);
    }
}
