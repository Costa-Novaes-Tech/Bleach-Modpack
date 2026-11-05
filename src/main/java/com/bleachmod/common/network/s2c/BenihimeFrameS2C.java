package com.bleachmod.common.network.s2c;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;

public record BenihimeFrameS2C(int owner,int kind,Vec3 origin,Vec3 end,Vec3 forward,float radius,float progress,int variant) {
    public static final int QUEEN=0,SLASH=1,SHIELD=2,NET=3,BEADS=4,INCISION=5,HEAL=6,BUFF=7,CORRIDOR=8,CLEAR=9,THREAD_LASH=10,SURGERY=11,KIDO=12,RODS=13,DECOY=14,HADO91=15;
    public boolean valid() {
        return owner>=0 && kind>=0 && kind<=HADO91 && finite(origin) && finite(end) && finite(forward)
                && origin.distanceToSqr(end)<=64*64 && forward.lengthSqr()>.5 && forward.lengthSqr()<1.01
                && Float.isFinite(radius) && radius>=0 && radius<=24 && Float.isFinite(progress) && progress>=0 && progress<=1 && variant>=0 && variant<=8;
    }
    private static boolean finite(Vec3 p) { return Double.isFinite(p.x) && Double.isFinite(p.y) && Double.isFinite(p.z); }
    public static void encode(BenihimeFrameS2C m,FriendlyByteBuf b) {
        b.writeVarInt(m.owner);b.writeByte(m.kind);write(b,m.origin);write(b,m.end);write(b,m.forward);b.writeFloat(m.radius);b.writeFloat(m.progress);b.writeByte(m.variant);
    }
    private static void write(FriendlyByteBuf b,Vec3 v) { b.writeDouble(v.x);b.writeDouble(v.y);b.writeDouble(v.z); }
    private static Vec3 read(FriendlyByteBuf b) { return new Vec3(b.readDouble(),b.readDouble(),b.readDouble()); }
    public static BenihimeFrameS2C decode(FriendlyByteBuf b) { return new BenihimeFrameS2C(b.readVarInt(),b.readUnsignedByte(),read(b),read(b),read(b),b.readFloat(),b.readFloat(),b.readUnsignedByte()); }
    public static void handle(BenihimeFrameS2C m,Supplier<NetworkEvent.Context> s) {
        var c=s.get();c.enqueueWork(()->{if(m.valid())DistExecutor.unsafeRunWhenOn(Dist.CLIENT,()->()->com.bleachmod.client.particle.BenihimeRenderer.accept(m));});c.setPacketHandled(true);
    }
}
