package com.bleachmod.common.network.s2c;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;

/** Starts (or stops, with duration 0) a release cinematic for one player, seen by them and everyone tracking them. */
public record ReleaseCinematicS2C(int entityId, String kind, int durationTicks) {
    public static void encode(ReleaseCinematicS2C msg, FriendlyByteBuf buf) {
        buf.writeVarInt(msg.entityId); buf.writeUtf(msg.kind, 64); buf.writeVarInt(msg.durationTicks);
    }
    public static ReleaseCinematicS2C decode(FriendlyByteBuf buf) {
        return new ReleaseCinematicS2C(buf.readVarInt(), buf.readUtf(64), buf.readVarInt());
    }
    public static void handle(ReleaseCinematicS2C msg, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
                () -> () -> com.bleachmod.client.cinematic.CinematicDirector.start(msg.entityId, msg.kind, msg.durationTicks)));
        context.get().setPacketHandled(true);
    }
}
