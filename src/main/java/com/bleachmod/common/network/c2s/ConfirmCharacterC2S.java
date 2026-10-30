package com.bleachmod.common.network.c2s;

import com.bleachmod.Reference;
import com.bleachmod.common.data.PlayerCapability;
import com.bleachmod.init.ModItems;
import com.bleachmod.common.network.SyncHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/** Creates the character: race and appearance are confirmed together, in one packet. */
public record ConfirmCharacterC2S(String race, net.minecraft.nbt.CompoundTag appearance) {
    public static void encode(ConfirmCharacterC2S msg, FriendlyByteBuf buf) {
        buf.writeUtf(msg.race);
        buf.writeNbt(msg.appearance);
    }

    public static ConfirmCharacterC2S decode(FriendlyByteBuf buf) {
        return new ConfirmCharacterC2S(buf.readUtf(32), buf.readNbt());
    }

    public static void handle(ConfirmCharacterC2S msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) {
                return;
            }
            PlayerCapability.get(player).ifPresent(data -> {
                if (data.getStatus().hasCreatedCharacter()) {
                    return;
                }
                if (!Reference.RACE_SHINIGAMI.equalsIgnoreCase(msg.race)) {
                    return;
                }
                data.initializeShinigami();
                com.bleachmod.common.appearance.AppearanceService.apply(player, data, msg.appearance);
                com.bleachmod.common.story.ShinigamiOriginService.request(player,data);
            });
        });
        ctx.get().setPacketHandled(true);
    }
}
