package com.bleachmod.common.technique;

import com.bleachmod.Reference;
import com.bleachmod.common.data.*;
import com.bleachmod.common.events.BleachEvents;
import com.bleachmod.common.network.NetworkHandler;
import com.bleachmod.common.network.s2c.ReleaseCinematicS2C;
import com.bleachmod.common.visual.BerserkVisuals;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import java.util.*;

/**
 * Release presentation for Nozarashi. Berserk Mode always plays the full cinematic: the wielder collapses,
 * crimson reiatsu erupts as a skull pillar, they rise transformed and seize the blade. The server owns the
 * timeline (sounds, eruption, knockback, invulnerability); clients play camera and body animation.
 */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public final class NozarashiReleaseEffects {
    public static final String BERSERK_RELEASE = "berserk_release";
    public static final int DURATION = 100, ERUPTION = 30, REVEAL = 32, GRAB = 82;
    private record Cinematic(long start, Vec3 anchor, Vec3 forward) { }
    private static final Map<UUID, Cinematic> ACTIVE = new HashMap<>();
    private NozarashiReleaseEffects() { }

    public static boolean inCinematic(ServerPlayer player) { return ACTIVE.containsKey(player.getUUID()); }

    @SubscribeEvent public static void release(BleachEvents.FormChangeEvent event) {
        ServerPlayer p = event.getPlayer();
        boolean wasActive = ACTIVE.containsKey(p.getUUID());
        if (wasActive && !Reference.FORM_BANKAI.equals(event.getNewForm())) stop(p);
        if (!Reference.GROUP_ZANPAKUTO.equals(event.getNewGroup()) || !RyujinReleaseEffects.shouldRelease(event.getOldForm(), event.getNewForm())) return;
        PlayerCapability.get(p).ifPresent(d -> {
            if (!CharacterData.NOZARASHI.equals(d.getCharacter().getZanpakutoIdentity())) return;
            Vec3 forward = Vec3.directionFromRotation(0, p.getYRot());
            if (Reference.FORM_BANKAI.equals(event.getNewForm())) {
                ACTIVE.put(p.getUUID(), new Cinematic(p.level().getGameTime(), p.position(), forward));
                NetworkHandler.sendToTrackingAndSelf(new ReleaseCinematicS2C(p.getId(), BERSERK_RELEASE, DURATION), p);
                p.serverLevel().playSound(null, p.blockPosition(), SoundEvents.WARDEN_HEARTBEAT, p.getSoundSource(), 3, .6F);
            } else {
                // Shikai: "Drink, Nozarashi" — a short crimson burst, no cinematic.
                BerserkVisuals.ring(p, p.position(), 2, 28, true);
                BerserkVisuals.crescent(p, forward, 2, 3, false);
                p.serverLevel().playSound(null, p.blockPosition(), SoundEvents.ANVIL_LAND, p.getSoundSource(), 1, .5F);
            }
        });
    }

    private static void timeline(ServerPlayer p, Cinematic c, int t) {
        var level = p.serverLevel();
        // Freeze in place; the client also suppresses input while its camera runs.
        p.setDeltaMovement(0, Math.min(0, p.getDeltaMovement().y), 0); p.hurtMarked = true;
        if (t < ERUPTION && t % 10 == 0) level.playSound(null, p.blockPosition(), SoundEvents.WARDEN_HEARTBEAT, p.getSoundSource(), 3, .6F + t * .01F);
        if (t >= 10 && t < ERUPTION && t % 3 == 0) BerserkVisuals.ring(p, p.position(), .6 + (t - 10) * .12, 16, false);
        if (t == ERUPTION) {
            BerserkVisuals.pillar(p, c.forward, 7);
            BerserkVisuals.ring(p, p.position(), 3, 40, true);
            level.playSound(null, p.blockPosition(), SoundEvents.GENERIC_EXPLODE, p.getSoundSource(), 2, .5F);
            level.playSound(null, p.blockPosition(), SoundEvents.WARDEN_ROAR, p.getSoundSource(), 2, .8F);
            for (LivingEntity e : level.getEntitiesOfClass(LivingEntity.class, p.getBoundingBox().inflate(8), e -> TechniqueTargets.allowed(p, e))) {
                Vec3 away = e.position().subtract(p.position()).normalize();
                e.push(away.x * 1.4, .6, away.z * 1.4); e.hurtMarked = true;
            }
        }
        if (t > ERUPTION && t < ERUPTION + 25 && t % 4 == 0) BerserkVisuals.ring(p, p.position(), 3 + (t - ERUPTION) * .3, 32, true);
        if (t > ERUPTION && t % 5 == 0) BerserkVisuals.aura(p, 3);
        if (t == 60) level.playSound(null, p.blockPosition(), SoundEvents.RAVAGER_ROAR, p.getSoundSource(), 2, .5F);
        if (t == GRAB) {
            level.playSound(null, p.blockPosition(), SoundEvents.ANVIL_LAND, p.getSoundSource(), 1.2F, .5F);
            BerserkVisuals.crescent(p, c.forward, 3, 4, true);
        }
    }

    private static void stop(ServerPlayer p) {
        if (ACTIVE.remove(p.getUUID()) != null) NetworkHandler.sendToTrackingAndSelf(new ReleaseCinematicS2C(p.getId(), BERSERK_RELEASE, 0), p);
    }

    @SubscribeEvent public static void tick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !(event.player instanceof ServerPlayer p)) return;
        Cinematic c = ACTIVE.get(p.getUUID());
        if (c != null) {
            int t = (int) (p.level().getGameTime() - c.start);
            if (t >= DURATION || !p.isAlive() || p.isSpectator()) ACTIVE.remove(p.getUUID());
            else timeline(p, c, t);
            return;
        }
        if (p.tickCount % 8 != 0 || !p.isAlive() || p.isSpectator()) return;
        PlayerCapability.get(p).ifPresent(d -> {
            if (!d.getStatus().hasCreatedCharacter() || !CharacterData.NOZARASHI.equals(d.getCharacter().getZanpakutoIdentity())
                    || !Reference.GROUP_ZANPAKUTO.equals(d.getCharacter().getActiveFormGroup())) return;
            String form = d.getCharacter().getActiveForm();
            if (Reference.FORM_BANKAI.equals(form)) BerserkVisuals.aura(p, 3);
            else if (Reference.FORM_SHIKAI.equals(form)) BerserkVisuals.aura(p, 2);
        });
    }
    /** The transformation itself cannot be interrupted by damage. */
    @SubscribeEvent public static void invulnerable(LivingAttackEvent event) {
        if (event.getEntity() instanceof ServerPlayer p && ACTIVE.containsKey(p.getUUID())
                && !event.getSource().is(net.minecraft.tags.DamageTypeTags.BYPASSES_INVULNERABILITY)) event.setCanceled(true);
    }
    @SubscribeEvent public static void death(LivingDeathEvent event) { if (event.getEntity() instanceof ServerPlayer p) stop(p); }
    @SubscribeEvent public static void logout(PlayerEvent.PlayerLoggedOutEvent event) { ACTIVE.remove(event.getEntity().getUUID()); }
    @SubscribeEvent public static void dimension(PlayerEvent.PlayerChangedDimensionEvent event) { if (event.getEntity() instanceof ServerPlayer p) stop(p); }
    @SubscribeEvent public static void stopping(ServerStoppingEvent event) { ACTIVE.clear(); }
}
