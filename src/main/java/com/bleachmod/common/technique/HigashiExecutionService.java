package com.bleachmod.common.technique;

import com.bleachmod.Reference;
import com.bleachmod.common.network.NetworkHandler;
import com.bleachmod.common.network.s2c.HigashiExecutionS2C;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/** Only authoritative, confirmed death can authorize an execution presentation. */
public final class HigashiExecutionService {
    public static final double THRESHOLD=.07;
    public static final int VISUAL_TICKS=32;
    private static final class DamageKey {static final ResourceKey<DamageType> VALUE=ResourceKey.create(Registries.DAMAGE_TYPE,Reference.id("higashi_execution"));}
    private static final AtomicInteger CONFIRMED_COUNT=new AtomicInteger(0);
    private static final ConcurrentHashMap<UUID,Integer> CONFIRMED_BY_TARGET=new ConcurrentHashMap<>();

    private HigashiExecutionService(){ }
    public static ResourceKey<DamageType> damageKey(){return DamageKey.VALUE;}
    public static boolean belowThreshold(float health,float max){return Float.isFinite(health)&&Float.isFinite(max)&&health>0&&max>0&&health<=max*THRESHOLD+1e-6;}
    public static boolean eligible(ServerPlayer owner,LivingEntity target){return TechniqueTargets.allowed(owner,target)&&!target.isInvulnerable()&&!(target instanceof ServerPlayer p&&p.getAbilities().invulnerable)&&belowThreshold(target.getHealth(),target.getMaxHealth());}
    private static void broadcast(ServerPlayer owner,HigashiExecutionS2C packet){for(var observer:owner.serverLevel().players())if(observer==owner||observer.position().distanceToSqr(packet.victim())<80*80)NetworkHandler.sendToPlayer(packet,observer);}

    public static int totalConfirmedCount(){return CONFIRMED_COUNT.get();}
    public static int confirmedCountFor(UUID targetId){return CONFIRMED_BY_TARGET.getOrDefault(targetId,0);}
    public static void resetTestCounters(){CONFIRMED_COUNT.set(0);CONFIRMED_BY_TARGET.clear();}

    /** Pre-strike visual hint: informs observers of imminent lethal Higashi cut to suppress vanilla hurt flash without starting execution. */
    public static void sendHint(ServerPlayer owner,LivingEntity target,Vec3 position,float width,float height){
        broadcast(owner,new HigashiExecutionS2C(owner.getId(),target.getId(),owner.position(),position,Math.min(16,width),Math.min(16,height),false));
    }

    /** Rollback: target survived, totem activated, or death was cancelled. Immediately unsuppresses client. */
    public static void cancelHint(ServerPlayer owner,LivingEntity target){
        target.getPersistentData().remove("bleachmod_higashi_confirmed");
        target.getPersistentData().remove("bleachmod_death_canceled_transient");
        broadcast(owner,new HigashiExecutionS2C(target.getId(),target.getId(),Vec3.ZERO,Vec3.ZERO,0.01F,0.01F,false));
    }

    /** Called only after authoritative damage; surviving totems/cancelled damage never incinerate. */
    public static void confirmedDeath(ServerPlayer owner,LivingEntity target,Vec3 position,float width,float height){
        if(target.isAlive())return;
        if(target.getPersistentData().getBoolean("bleachmod_death_canceled_transient"))return;
        if(target.getPersistentData().getBoolean("bleachmod_higashi_confirmed"))return;
        target.getPersistentData().putBoolean("bleachmod_higashi_confirmed",true);
        CONFIRMED_COUNT.incrementAndGet();
        CONFIRMED_BY_TARGET.merge(target.getUUID(),1,Integer::sum);
        broadcast(owner,new HigashiExecutionS2C(owner.getId(),target.getId(),owner.position(),position,Math.min(16,width),Math.min(16,height),true));
    }

    public static boolean execute(ServerPlayer owner,LivingEntity target){
        if(!eligible(owner,target))return false;
        var position=target.position();float width=target.getBbWidth(),height=target.getBbHeight();
        sendHint(owner,target,position,width,height);
        // Indirect attribution keeps the killer while avoiding the physical Resistência pipeline.
        var source=new DamageSource(owner.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageKey.VALUE),null,owner);
        boolean accepted=MasteryPracticeService.hurt(owner,target,source,Float.MAX_VALUE,2);
        boolean finalDead=accepted&&!target.isAlive()&&!target.getPersistentData().getBoolean("bleachmod_death_canceled_transient");
        if(finalDead){
            confirmedDeath(owner,target,position,width,height);
        }else{
            cancelHint(owner,target);
        }
        return finalDead;
    }
}
