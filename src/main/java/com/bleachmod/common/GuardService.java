package com.bleachmod.common;

import com.bleachmod.Reference;
import com.bleachmod.common.data.*;
import com.bleachmod.common.network.SyncHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.damagesource.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.SwordItem;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.*;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public final class GuardService {
    private GuardService() { }
    public static boolean eligible(ServerPlayer p, PlayerData d) {
        if (com.bleachmod.common.technique.ReleaseAnimationService.busy(p) || com.bleachmod.common.technique.ElementalTechniqueService.busy(p)) return false;
        return d.getStatus().hasCreatedCharacter() && p.isAlive() && !p.isSpectator()
                && !p.isSleeping() && !p.isPassenger() && !p.getAbilities().flying && !p.isFallFlying()
                && !p.isUsingItem() && !p.isFullyFrozen() && !d.getStatus().isActionCharging()
                && !d.getStatus().isDodging() && d.getStatus().getFlameDashTicks() == 0
                && !com.bleachmod.common.technique.SenbonzakuraTechniqueService.busy(p)
                && !com.bleachmod.common.technique.SenbonzakuraTechniqueService.ward(p)
                && !com.bleachmod.common.technique.BenihimeTechniqueService.busy(p)
                && !com.bleachmod.common.technique.BenihimeTechniqueService.ward(p);
    }
    public static void request(ServerPlayer p, PlayerData d, boolean held) {
        if (!held) d.getGuard().release();
        else if (eligible(p, d)) d.getGuard().request(p.level().getGameTime());
        else d.getGuard().interrupt();
        publish(p, d);
    }
    public static void tick(ServerPlayer p, PlayerData d) {
        var guard = d.getGuard();
        if (!eligible(p, d)) guard.interrupt();
        guard.tick(p.level().getGameTime());
        if (guard.active()) p.setSprinting(false);
        publish(p, d);
    }
    public static void publish(ServerPlayer p, PlayerData d) {
        if (d.getGuard().needsPublish()) SyncHelper.appearance(p);
    }
    public static boolean blockable(DamageSource source) {
        if (!(source.getEntity() instanceof LivingEntity) || source.getDirectEntity() != source.getEntity()
                || source.is(DamageTypeTags.BYPASSES_INVULNERABILITY) || source.is(DamageTypeTags.BYPASSES_SHIELD)
                || source.is(DamageTypeTags.IS_PROJECTILE) || source.is(DamageTypeTags.IS_EXPLOSION)) return false;
        return source.is(DamageTypes.PLAYER_ATTACK) || source.is(DamageTypes.MOB_ATTACK)
                || source.is(DamageTypes.MOB_ATTACK_NO_AGGRO) || source.is(DamageTypes.STING);
    }
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void hurt(LivingHurtEvent event) {
        if (event.isCanceled() || !(event.getEntity() instanceof ServerPlayer p)
                || !Float.isFinite(event.getAmount()) || event.getAmount() <= 0 || !blockable(event.getSource())) return;
        var attacker = event.getSource().getEntity();
        if (attacker == p || !GuardPolicy.inFront(p.getYRot(), attacker.position().subtract(p.position()))) return;
        PlayerCapability.get(p).ifPresent(d -> {
            if (!eligible(p, d) || !d.getGuard().active()) return;
            float amount = event.getAmount();
            boolean armed = p.getMainHandItem().getItem() instanceof SwordItem || p.getMainHandItem().is(ItemTags.SWORDS);
            boolean broken = d.getGuard().absorb(GuardPolicy.pressure(amount));
            event.setAmount(amount * (1 - (armed ? GuardPolicy.ARMED_REDUCTION : GuardPolicy.UNARMED_REDUCTION)));
            p.serverLevel().playSound(null, p.blockPosition(), broken ? SoundEvents.SHIELD_BREAK : SoundEvents.SHIELD_BLOCK,
                    p.getSoundSource(), .7F, broken ? .85F : 1.2F);
            if (broken) p.serverLevel().sendParticles(net.minecraft.core.particles.ParticleTypes.CRIT,
                    p.getX(), p.getY() + p.getBbHeight() + .3, p.getZ(), 8, .2, .15, .2, .06);
            publish(p, d); SyncHelper.resources(p);
        });
    }
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void attack(AttackEntityEvent event) {
        if (event.getEntity() instanceof ServerPlayer releasing && (com.bleachmod.common.technique.ReleaseAnimationService.busy(releasing) || com.bleachmod.common.technique.ElementalTechniqueService.busy(releasing))) { event.setCanceled(true); return; }
        if (event.getEntity() instanceof ServerPlayer p)
            PlayerCapability.get(p).ifPresent(d -> { if (d.getGuard().active()
                    || com.bleachmod.common.technique.SenbonzakuraTechniqueService.busy(p) || com.bleachmod.common.technique.BenihimeTechniqueService.busy(p)
                    || !com.bleachmod.common.technique.SenbonzakuraTechniqueService.allowMelee(p,event.getTarget())) event.setCanceled(true); });
    }
}
