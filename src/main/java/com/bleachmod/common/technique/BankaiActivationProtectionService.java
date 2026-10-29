package com.bleachmod.common.technique;

import com.bleachmod.Reference;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Server-authoritative temporary invulnerability during Bankai activation and presentation.
 * Protects players from mob contact, projectiles, fire, environmental hazards and knockback,
 * while allowing administrative kills/removals (bypasses_invulnerability).
 * Terminates immediately upon voluntary combat action (attack, dodge, guard, technique)
 * after gameplay release has ended, preventing free combat under protection.
 */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public final class BankaiActivationProtectionService {

    private BankaiActivationProtectionService() { }

    /**
     * Authoritative check: is the player currently in an active Bankai activation or presentation?
     * Covers:
     * - Nozarashi berserk presentation (NozarashiReleaseEffects.inCinematic, 140 ticks)
     * - Ryūjin Jakka presentation (RyujinBankaiEntranceService.active, up to 240 ticks)
     * - Senbonzakura presentation (SenbonzakuraCinematicService.active, up to 290 ticks)
     * - Daiguren Hyōrinmaru presentation (HyorinmaruCinematicService.active, up to 400 ticks)
     * - Kannonbiraki Benihime presentation (BenihimeEntranceService.active, up to 560 ticks)
     * - Any active Bankai release animation (ReleaseAnimationService.isBankaiRelease, 60 ticks)
     */
    public static boolean isProtected(ServerPlayer player) {
        if (player == null || !player.isAlive() || player.isSpectator()) return false;

        if (NozarashiReleaseEffects.inCinematic(player)) return true;
        if (RyujinBankaiEntranceService.active(player)) return true;
        if (SenbonzakuraCinematicService.active(player)) return true;
        if (HyorinmaruCinematicService.active(player)) return true;
        if (BenihimeEntranceService.active(player)) return true;

        return ReleaseAnimationService.isBankaiRelease(player);
    }

    /**
     * Intercepts damage at the start of Forge damage pipeline before health, armor,
     * stamina, totem, knockback or hurtTime are modified.
     */
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onLivingAttack(LivingAttackEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            DamageSource source = event.getSource();
            if (isProtected(player)) {
                // Administrative / kill / void commands must never be blocked
                if (source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
                    return;
                }
                event.setCanceled(true);
            }
        }
    }

    /**
     * Prevents mobs or environmental impacts from knocking the player away during the scene.
     */
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onLivingKnockBack(LivingKnockBackEvent event) {
        if (event.getEntity() instanceof ServerPlayer player && isProtected(player)) {
            event.setCanceled(true);
        }
    }

    /**
     * When the player executes a voluntary attack after release duration has ended,
     * any remaining scenic presentation must immediately terminate so the player cannot fight while invulnerable.
     */
    @SubscribeEvent
    public static void onPlayerAttack(AttackEntityEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            cancelPostReleasePresentations(player);
        }
    }

    public static void cancelPostReleasePresentations(ServerPlayer player) {
        if (player == null) return;
        // If initial gameplay release (busy lock, 0..60) has ended, manual combat cancels remaining scenic presentations
        if (!ReleaseAnimationService.busy(player)) {
            if (RyujinBankaiEntranceService.active(player)) RyujinBankaiEntranceService.cancel(player);
            if (SenbonzakuraCinematicService.active(player)) SenbonzakuraCinematicService.cancel(player);
            if (HyorinmaruCinematicService.active(player)) HyorinmaruCinematicService.cancel(player);
            if (BenihimeEntranceService.active(player)) BenihimeEntranceService.cancel(player);
        }
    }
}
