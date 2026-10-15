package com.bleachmod.common.network.s2c;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;

/** Parametric visual frame: no entity inventories, client destinations or damage data. */
public record SenbonPetalsS2C(int shape, Vec3 origin, Vec3 forward, float radius, float progress, int pattern, int seed, int ownerId) {
    public static final int ARC=0, STREAM=1, CONVERGE=2, GUARD=3, FIELD=4, FLOWER=5, RISE=6, RADIAL=7, SWORDS=8, WINGS=9, AURA=10, LANCE=11;
    public static int particleCount(int shape) { return switch(shape) { case AURA->6;case ARC->32;case FIELD,FLOWER->64;case SWORDS,WINGS->12;case LANCE->20;default->36; }; }
    public boolean valid() {
        return shape >= 0 && shape <= LANCE && finite(origin) && finite(forward) && forward.lengthSqr() > .1
                && forward.lengthSqr() < 1.01 && Float.isFinite(radius) && radius > 0 && radius <= 48
                && Float.isFinite(progress) && progress >= 0 && progress <= 1 && pattern >= 0 && pattern <= 4 && ownerId>=0;
    }
    private static boolean finite(Vec3 p) { return Double.isFinite(p.x) && Double.isFinite(p.y) && Double.isFinite(p.z); }
    public static void encode(SenbonPetalsS2C m, FriendlyByteBuf b) {
        b.writeByte(m.shape); b.writeDouble(m.origin.x); b.writeDouble(m.origin.y); b.writeDouble(m.origin.z);
        b.writeFloat((float)m.forward.x); b.writeFloat((float)m.forward.y); b.writeFloat((float)m.forward.z);
        b.writeFloat(m.radius); b.writeFloat(m.progress); b.writeByte(m.pattern); b.writeInt(m.seed);b.writeVarInt(m.ownerId);
    }
    public static SenbonPetalsS2C decode(FriendlyByteBuf b) {
        return new SenbonPetalsS2C(b.readUnsignedByte(), new Vec3(b.readDouble(),b.readDouble(),b.readDouble()),
                new Vec3(b.readFloat(),b.readFloat(),b.readFloat()), b.readFloat(),b.readFloat(),b.readUnsignedByte(),b.readInt(),b.readVarInt());
    }
    public static void handle(SenbonPetalsS2C m, Supplier<NetworkEvent.Context> s) {
        var c=s.get(); c.enqueueWork(() -> { if (m.valid()) DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
                () -> () -> com.bleachmod.client.particle.SenbonPetalEffects.draw(m)); }); c.setPacketHandled(true);
    }
}
