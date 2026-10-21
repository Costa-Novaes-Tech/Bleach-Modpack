package com.bleachmod.common.technique;

import com.bleachmod.Reference;
import com.bleachmod.common.network.NetworkHandler;
import com.bleachmod.common.network.s2c.HigashiExecutionS2C;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.*;
import net.minecraft.world.entity.LivingEntity;

/** Only authoritative, confirmed death can authorize an execution presentation. */
public final class HigashiExecutionService {
    public static final double THRESHOLD=.07;
    public static final int VISUAL_TICKS=32;
    private static final class DamageKey {static final ResourceKey<DamageType> VALUE=ResourceKey.create(Registries.DAMAGE_TYPE,Reference.id("higashi_execution"));}
    private HigashiExecutionService(){ }
    public static ResourceKey<DamageType> damageKey(){return DamageKey.VALUE;}
    public static boolean belowThreshold(float health,float max){return Float.isFinite(health)&&Float.isFinite(max)&&health>0&&max>0&&health<=max*THRESHOLD+1e-6;}
    public static boolean eligible(ServerPlayer owner,LivingEntity target){return TechniqueTargets.allowed(owner,target)&&!target.isInvulnerable()&&!(target instanceof ServerPlayer p&&p.getAbilities().invulnerable)&&belowThreshold(target.getHealth(),target.getMaxHealth());}
    private static void broadcast(ServerPlayer owner,HigashiExecutionS2C packet){for(var observer:owner.serverLevel().players())if(observer==owner||observer.position().distanceToSqr(packet.victim())<80*80)NetworkHandler.sendToPlayer(packet,observer);}
    /** Called only after authoritative damage; surviving totems/cancelled damage never incinerate. */
    public static void confirmedDeath(ServerPlayer owner,LivingEntity target,net.minecraft.world.phys.Vec3 position,float width,float height){
        if(target.isAlive())return;
        broadcast(owner,new HigashiExecutionS2C(owner.getId(),target.getId(),owner.position(),position,Math.min(16,width),Math.min(16,height)));
    }
    public static boolean execute(ServerPlayer owner,LivingEntity target){
        if(!eligible(owner,target))return false;
        var position=target.position();float width=target.getBbWidth(),height=target.getBbHeight();
        // Indirect attribution keeps the killer while avoiding the physical Resistência pipeline.
        var source=new DamageSource(owner.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageKey.VALUE),null,owner);
        boolean accepted=MasteryPracticeService.hurt(owner,target,source,Float.MAX_VALUE,2);
        if(accepted&&!target.isAlive()){
            confirmedDeath(owner,target,position,width,height);
        }
        return accepted&&!target.isAlive();
    }
}
