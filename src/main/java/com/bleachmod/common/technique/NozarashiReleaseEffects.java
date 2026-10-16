package com.bleachmod.common.technique;

import com.bleachmod.Reference;
import com.bleachmod.common.data.*;
import com.bleachmod.common.events.BleachEvents;
import com.bleachmod.common.network.NetworkHandler;
import com.bleachmod.common.network.s2c.ReleaseCinematicS2C;
import com.bleachmod.common.visual.BerserkVisuals;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
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
 * Release presentation for Nozarashi. Berserk Mode always plays the full 7 s cinematic of pure rage: the
 * wielder trembles, collapses and pounds the ground three times, crimson reiatsu erupts as a skull pillar
 * with a layered roar, he roars again, rises transformed, rips the blade overhead and hammers the ground.
 * The server owns the timeline (sounds, eruption, knockback, invulnerability, shakes); clients play the
 * camera and body animation.
 */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public final class NozarashiReleaseEffects {
    public static final String BERSERK_RELEASE = "berserk_release", IMPACT = "impact";
    public static final int DURATION = 140, ERUPTION = 45, REVEAL = 47, SECOND_ROAR = 68, GRAB = 112, SLAM = 124;
    public static final int[] POUNDS = {24, 32, 40};
    private record Cinematic(long start, Vec3 anchor, Vec3 forward) { }
    private static final Map<UUID, Cinematic> ACTIVE = new HashMap<>();
    private NozarashiReleaseEffects() { }

    public static boolean inCinematic(ServerPlayer player) { return ACTIVE.containsKey(player.getUUID()); }

    /** Screen shake for the source and everyone tracking them; strength 1..10 maps to amplitude and length. */
    public static void shake(ServerPlayer source, int strength) {
        NetworkHandler.sendToTrackingAndSelf(new ReleaseCinematicS2C(source.getId(), IMPACT, Math.max(1, Math.min(10, strength))), source);
    }
    /** Rage roar layered from vanilla beasts, pitched down. */
    public static void roar(ServerPlayer p, float volume) {
        var level = p.serverLevel(); var pos = p.blockPosition(); var src = p.getSoundSource();
        level.playSound(null, pos, SoundEvents.WARDEN_ROAR, src, volume, .7F);
        level.playSound(null, pos, SoundEvents.RAVAGER_ROAR, src, volume, .45F);
        level.playSound(null, pos, SoundEvents.ENDER_DRAGON_GROWL, src, volume * .6F, .55F);
        level.playSound(null, pos, SoundEvents.HOGLIN_ANGRY, src, volume, .5F);
    }
    private static void play(ServerPlayer p, SoundEvent sound, float volume, float pitch) {
        p.serverLevel().playSound(null, p.blockPosition(), sound, p.getSoundSource(), volume, pitch);
    }

    @SubscribeEvent public static void release(BleachEvents.FormChangeEvent event) {
        ServerPlayer p = event.getPlayer();
        if (ACTIVE.containsKey(p.getUUID()) && !Reference.FORM_BANKAI.equals(event.getNewForm())) stop(p);
        if (!Reference.GROUP_ZANPAKUTO.equals(event.getNewGroup()) || !RyujinReleaseEffects.shouldRelease(event.getOldForm(), event.getNewForm())) return;
        PlayerCapability.get(p).ifPresent(d -> {
            if (!CharacterData.NOZARASHI.equals(d.getCharacter().getZanpakutoIdentity())) return;
            Vec3 forward = Vec3.directionFromRotation(0, p.getYRot());
            if (Reference.FORM_BANKAI.equals(event.getNewForm())) {
                ACTIVE.put(p.getUUID(), new Cinematic(p.level().getGameTime(), p.position(), forward));
                NetworkHandler.sendToTrackingAndSelf(new ReleaseCinematicS2C(p.getId(), BERSERK_RELEASE, DURATION), p);
                play(p, SoundEvents.WARDEN_HEARTBEAT, 3, .5F);
            } else {
                // Shikai: "Drink, Nozarashi" — a violent crimson burst, no cinematic.
                BerserkVisuals.ring(p, p.position(), 2, 36, true);
                BerserkVisuals.ring(p, p.position(), 4, 48, true);
                BerserkVisuals.crescent(p, forward, 2, 4, true);
                BerserkVisuals.impact(p, p.position(), 2);
                play(p, SoundEvents.ANVIL_LAND, 1.2F, .5F);
                play(p, SoundEvents.RAVAGER_ROAR, 1.5F, .6F);
                shake(p, 4);
            }
        });
    }

    private static void timeline(ServerPlayer p, Cinematic c, int t) {
        var level = p.serverLevel();
        // Freeze in place; the client also suppresses input while its camera runs.
        p.setDeltaMovement(0, Math.min(0, p.getDeltaMovement().y), 0); p.hurtMarked = true;
        // Heartbeat accelerating with the rage.
        if (t < ERUPTION && (t % Math.max(4, 14 - t / 4) == 0)) play(p, SoundEvents.WARDEN_HEARTBEAT, 3, .5F + t * .012F);
        if (t == 6) play(p, SoundEvents.WARDEN_ANGRY, 2, .6F);
        if (t == 16) play(p, SoundEvents.ZOGLIN_ANGRY, 2, .5F);
        if (t >= 8 && t < ERUPTION && t % 4 == 0) BerserkVisuals.ring(p, p.position(), .5 + (t - 8) * .09, 20, false);
        for (int pound : POUNDS) if (t == pound) { // fists hammering the ground
            Vec3 fist = p.position().add(c.forward.scale(.9));
            BerserkVisuals.impact(p, fist, 1.5F + (t - POUNDS[0]) * .08F);
            BerserkTerrain.crater(p, fist, 1.3, 1);
            play(p, SoundEvents.ANVIL_LAND, 1.4F, .4F); play(p, SoundEvents.GENERIC_EXPLODE, .8F, .7F);
            shake(p, 3 + (t - POUNDS[0]) / 6);
        }
        if (t == ERUPTION) {
            BerserkVisuals.pillar(p, c.forward, 9);
            BerserkTerrain.crater(p, p.position(), 5.5, 3, 1.3); // the wielder stays on a pillar of ground
            BerserkVisuals.ring(p, p.position(), 3, 48, true);
            BerserkVisuals.ring(p, p.position(), 6, 64, true);
            play(p, SoundEvents.GENERIC_EXPLODE, 3, .45F); play(p, SoundEvents.WARDEN_SONIC_BOOM, 2, .5F);
            roar(p, 3);
            shake(p, 10);
            for (LivingEntity e : level.getEntitiesOfClass(LivingEntity.class, p.getBoundingBox().inflate(12), e -> TechniqueTargets.allowed(p, e))) {
                Vec3 away = e.position().subtract(p.position()).normalize();
                e.push(away.x * 2.2, .9, away.z * 2.2); e.hurtMarked = true;
            }
        }
        if (t > ERUPTION && t < ERUPTION + 36 && t % 6 == 0) BerserkVisuals.ring(p, p.position(), 3 + (t - ERUPTION) * .35, 40, true);
        if (t == SECOND_ROAR) { roar(p, 3); shake(p, 7); BerserkVisuals.ring(p, p.position(), 4, 64, true); }
        if (t == GRAB) { play(p, SoundEvents.ANVIL_LAND, 1.4F, .5F); play(p, SoundEvents.CHAIN_BREAK, 2, .5F); }
        if (t == SLAM) { // the blade comes down on the ground like a hammer
            Vec3 ground = p.position().add(c.forward.scale(3));
            BerserkVisuals.crescent(p, c.forward, 3, 5, true);
            BerserkVisuals.fissure(p, p.position(), c.forward, 18, true);
            BerserkVisuals.impact(p, ground, 2.5F);
            BerserkTerrain.trench(p, p.position().add(c.forward.scale(1.5)), c.forward, 18, 1.2, 3);
            BerserkTerrain.crater(p, ground, 3, 2);
            BerserkVisuals.ring(p, ground, 4, 56, true);
            play(p, SoundEvents.GENERIC_EXPLODE, 2.5F, .5F); play(p, SoundEvents.WARDEN_SONIC_BOOM, 1.5F, .6F);
            roar(p, 2);
            shake(p, 9);
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
        // Berserk Mode's spiral aura is drawn client-side every frame; Shikai keeps a server aura.
        if (p.tickCount % 6 != 0 || !p.isAlive() || p.isSpectator()) return;
        PlayerCapability.get(p).ifPresent(d -> {
            if (d.getStatus().hasCreatedCharacter() && CharacterData.NOZARASHI.equals(d.getCharacter().getZanpakutoIdentity())
                    && Reference.GROUP_ZANPAKUTO.equals(d.getCharacter().getActiveFormGroup())
                    && Reference.FORM_SHIKAI.equals(d.getCharacter().getActiveForm())) BerserkVisuals.aura(p, 2);
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
