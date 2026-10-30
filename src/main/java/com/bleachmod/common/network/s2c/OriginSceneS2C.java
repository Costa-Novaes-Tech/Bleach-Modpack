package com.bleachmod.common.network.s2c;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.api.distmarker.Dist;
import java.util.function.Supplier;
public record OriginSceneS2C(int playerId, int rukiaId, int bossId, long start, Vec3 anchor, Vec3 forward, boolean stop) {
    public static void encode(OriginSceneS2C m, FriendlyByteBuf b) {
        b.writeInt(m.playerId); b.writeInt(m.rukiaId); b.writeInt(m.bossId); b.writeLong(m.start);
        b.writeDouble(m.anchor.x); b.writeDouble(m.anchor.y); b.writeDouble(m.anchor.z);
        b.writeDouble(m.forward.x); b.writeDouble(m.forward.y); b.writeDouble(m.forward.z); b.writeBoolean(m.stop);
    }
    public static OriginSceneS2C decode(FriendlyByteBuf b) {
        return new OriginSceneS2C(b.readInt(),b.readInt(),b.readInt(),b.readLong(),
                new Vec3(b.readDouble(),b.readDouble(),b.readDouble()),new Vec3(b.readDouble(),b.readDouble(),b.readDouble()),b.readBoolean());
    }
    public static void handle(OriginSceneS2C m, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
                com.bleachmod.client.cinematic.OriginCinematicClient.receive(m)));
        ctx.get().setPacketHandled(true);
    }
}
