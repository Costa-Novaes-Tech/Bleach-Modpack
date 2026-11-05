package com.bleachmod.common.network.s2c;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;

/** Confirmed death presentation. Legacy unconfirmed packets are decoded but cannot start a scene. */
public record HigashiExecutionS2C(int owner,int target,Vec3 attacker,Vec3 victim,float width,float height,boolean confirmed){
    public HigashiExecutionS2C(int owner,int target,Vec3 attacker,Vec3 victim,float width,float height){this(owner,target,attacker,victim,width,height,true);}
    public boolean valid(){return owner>=0&&target>=0&&owner!=target&&finite(attacker)&&finite(victim)&&attacker.distanceToSqr(victim)<=12*12&&Float.isFinite(width)&&Float.isFinite(height)&&width>0&&height>0&&width<=16&&height<=16;}
    public boolean startsExecution(){return confirmed&&valid();}
    public boolean isCancel(){return !confirmed&&owner==target&&target>=0;}
    private static boolean finite(Vec3 v){return Double.isFinite(v.x+v.y+v.z)&&Math.abs(v.x)<3e7&&Math.abs(v.z)<3e7&&Math.abs(v.y)<4096;}
    public static void encode(HigashiExecutionS2C m,FriendlyByteBuf b){b.writeVarInt(m.owner);b.writeVarInt(m.target);for(var p:new Vec3[]{m.attacker,m.victim}){b.writeDouble(p.x);b.writeDouble(p.y);b.writeDouble(p.z);}b.writeFloat(m.width);b.writeFloat(m.height);b.writeBoolean(m.confirmed);}
    public static HigashiExecutionS2C decode(FriendlyByteBuf b){return new HigashiExecutionS2C(b.readVarInt(),b.readVarInt(),new Vec3(b.readDouble(),b.readDouble(),b.readDouble()),new Vec3(b.readDouble(),b.readDouble(),b.readDouble()),b.readFloat(),b.readFloat(),b.readBoolean());}
    public static void handle(HigashiExecutionS2C m,Supplier<NetworkEvent.Context> supplier){
        var c=supplier.get();
        if(m.isCancel()){
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT,()->()->com.bleachmod.client.model.HigashiExecutionVisuals.removePendingTarget(m.target()));
        } else if(m.startsExecution()){
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT,()->()->com.bleachmod.client.model.HigashiExecutionVisuals.notePendingTarget(m.target(),true));
        } else if(!m.confirmed()&&m.valid()){
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT,()->()->com.bleachmod.client.model.HigashiExecutionVisuals.notePendingTarget(m.target(),false));
        }
        c.enqueueWork(()->{
            if(m.isCancel())DistExecutor.unsafeRunWhenOn(Dist.CLIENT,()->()->com.bleachmod.client.model.HigashiExecutionVisuals.removePendingTarget(m.target()));
            else if(m.valid())DistExecutor.unsafeRunWhenOn(Dist.CLIENT,()->()->com.bleachmod.client.model.HigashiExecutionVisuals.accept(m));
        });
        c.setPacketHandled(true);
    }
}
