package com.bleachmod.common.network.s2c;

import com.bleachmod.client.network.ClientPacketHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/** Server granted an editing window: open the appearance editor for an existing character. */
public record OpenAppearanceEditorS2C() {
    public static void encode(OpenAppearanceEditorS2C msg, FriendlyByteBuf buf) { }

    public static OpenAppearanceEditorS2C decode(FriendlyByteBuf buf) { return new OpenAppearanceEditorS2C(); }

    public static void handle(OpenAppearanceEditorS2C msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> ClientPacketHandler::handleOpenAppearanceEditor));
        ctx.get().setPacketHandled(true);
    }
}
