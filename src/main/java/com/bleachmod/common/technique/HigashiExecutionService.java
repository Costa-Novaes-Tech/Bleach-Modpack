package com.bleachmod.common.technique;

import com.bleachmod.Reference;
import com.bleachmod.common.network.NetworkHandler;
import com.bleachmod.common.network.s2c.HigashiExecutionS2C;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.*;
import net.minecraft.world.entity.LivingEntity;

/** One decisive server hit; the subsequent cinematic cannot deal another hit. */
public final class HigashiExecutionService {
    public static final double THRESHOLD=.07;
    public static final int VISUAL_TICKS=32;
    private static final class DamageKey {static final ResourceKey<DamageType> VALUE=ResourceKey.create(Registries.DAMAGE_TYPE,Reference.id("higashi_execution"));}
    private HigashiExecutionService(){ }
    public static ResourceKey<DamageType> damageKey(){return DamageKey.VALUE;}
    public static boolean belowThreshold(float health,float max){return Float.isFinite(health)&&Float.isFinite(max)&&health>0&&max>0&&health<=max*THRESHOLD+1e-6;}
    public static boolean eligible(ServerPlayer owner,LivingEntity target){return TechniqueTargets.allowed(owner,target)&&!target.isInvulnerable()&&!(target instanceof ServerPlayer p&&p.getAbilities().invulnerable)&&belowThreshold(target.getHealth(),target.getMaxHealth());}
    public static boolean execute(ServerPlayer owner,LivingEntity target){
        if(!eligible(owner,target))return false;
        var position=target.position();float width=target.getBbWidth(),height=target.getBbHeight();
        // Indirect attribution keeps the killer while avoiding the physical Resistência pipeline.
        var source=new DamageSource(owner.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageKey.VALUE),null,owner);
        boolean accepted=MasteryPracticeService.hurt(owner,target,source,Float.MAX_VALUE,2);
        if(accepted&&!target.isAlive()){
            owner.serverLevel().playSound(null,target.blockPosition(),net.minecraft.sounds.SoundEvents.BLAZE_DEATH,owner.getSoundSource(),.8F,.65F);
            var packet=new HigashiExecutionS2C(owner.getId(),target.getId(),owner.position(),position,Math.min(16,width),Math.min(16,height));
            for(var observer:owner.serverLevel().players())if(observer==owner||observer.position().distanceToSqr(position)<80*80)NetworkHandler.sendToPlayer(packet,observer);
        }
        return accepted&&!target.isAlive();
    }
}
