package com.bleachmod.common.technique;

import com.bleachmod.Reference;
import com.bleachmod.common.data.CharacterData;
import com.bleachmod.common.data.PlayerCapability;
import com.bleachmod.common.data.PlayerData;
import com.bleachmod.common.network.SyncHelper;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Server-side authoritative presentation timeline for Senbonzakura Kageyoshi Bankai.
 * Total presentation duration: 290 ticks (14.5 seconds).
 *
 * Timeline (synchronous with audio & visual direction):
 *   t = 0..10 (0.0..0.5s):    Ritual framing, player stands serene holding inverted katana.
 *   t = 10..76 (0.5..3.8s):   Voice "Bankai" (t=10..76). Hand releases katana at t=10; katana falls
 *                             vertically, hitting spiritual floor at t=35 and progressively sinking into ripples.
 *   t = 40 (2.0s):            Authoritative gameplay commit: Bankai transformation is committed on server.
 *   t = 60 (3.0s):            Combat lock expires (ReleaseAnimationService ends). Player is free to act;
 *                             presentation continues smoothly unless canceled by dodge, guard, or signature move.
 *   t = 76..160 (3.8..8.0s):  The Rising Arsenal: 20-24 colossal monumental blades emerge along the corridor
 *                             with smooth ease-out acceleration and settling. Concentric ripples expand.
 *   t = 160..228 (8.0..11.4s): Voice "Senbonzakura Kageyoshi" (t=160..228). Player commands with right arm outstretched;
 *                             spiritual pink reishi energy resonates along the towering blades.
 *   t = 228..266 (11.4..13.3s): Progressive petal dispersion: Colossal blades dissolve into swirling sakura vortices.
 *   t = 266..290 (13.3..14.5s): Petal storm sweeps out; transformed Byakuya remains visible; camera smoothly returns.
 */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public final class SenbonzakuraCinematicService {
    public static final int DURATION = 290;
    public static final int COMMIT = 40;

    private record Scene(long start, Vec3 anchor, float yaw, net.minecraft.resources.ResourceKey<net.minecraft.world.level.Level> dimension) { }
    private static final Map<UUID, Scene> SCENES = new HashMap<>();

    private SenbonzakuraCinematicService() { }

    public static void begin(ServerPlayer player) {
        SCENES.put(player.getUUID(), new Scene(player.level().getGameTime(), player.position(), player.getYRot(), player.level().dimension()));
        SyncHelper.appearance(player);
    }

    public static boolean active(ServerPlayer player) {
        return SCENES.containsKey(player.getUUID());
    }

    public static CompoundTag snapshot(ServerPlayer player) {
        var tag = new CompoundTag();
        var scene = SCENES.get(player.getUUID());
        if (scene == null) return tag;
        long age = player.level().getGameTime() - scene.start;
        if (age < 0 || age >= DURATION) return tag;
        tag.putInt("elapsed", (int) age);
        tag.putInt("duration", DURATION);
        tag.putDouble("x", scene.anchor.x);
        tag.putDouble("y", scene.anchor.y);
        tag.putDouble("z", scene.anchor.z);
        tag.putFloat("yaw", scene.yaw);
        return tag;
    }

    public static void cancel(ServerPlayer player) {
        if (SCENES.remove(player.getUUID()) != null) {
            SyncHelper.appearance(player);
        }
    }

    @SubscribeEvent
    public static void tick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !(event.player instanceof ServerPlayer p)) return;
        var scene = SCENES.get(p.getUUID());
        if (scene == null) return;

        PlayerData d = PlayerCapability.get(p).orElse(null);
        long age = p.level().getGameTime() - scene.start;

        boolean valid = d != null && CharacterData.SENBONZAKURA.equals(d.getCharacter().getZanpakutoIdentity())
                && TechniqueProgression.equipped(p, d)
                && (ReleaseAnimationService.busy(p) || (age >= COMMIT && Reference.FORM_BANKAI.equals(d.getCharacter().getActiveForm())));

        if (age >= 60 && d != null && (!SignatureAnimationService.snapshot(p).isEmpty() || d.getGuard().shownActive() || d.getStatus().isDodging())) {
            valid = false;
        }

        if (!valid || !p.isAlive() || p.isSpectator() || !scene.dimension.equals(p.level().dimension()) || age >= DURATION || p.position().distanceToSqr(scene.anchor) > 256 * 256) {
            cancel(p);
            return;
        }

        // Sound and ambient particle cues
        ServerLevel level = p.serverLevel();
        if (age == 35) {
            // Katana hits the water-like ground: crystalline water ripple resonance
            level.playSound(null, scene.anchor.x, scene.anchor.y, scene.anchor.z,
                    SoundEvents.PLAYER_SPLASH, SoundSource.PLAYERS, 0.85F, 1.85F);
            level.playSound(null, scene.anchor.x, scene.anchor.y, scene.anchor.z,
                    SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.PLAYERS, 1.2F, 1.6F);
        } else if (age == 76) {
            // Colossal steel blades emerge: deep earth rumble, anvil resonance and metallic slide
            level.playSound(null, scene.anchor.x, scene.anchor.y, scene.anchor.z,
                    SoundEvents.IRON_GOLEM_ATTACK, SoundSource.PLAYERS, 1.5F, 0.55F);
            level.playSound(null, scene.anchor.x, scene.anchor.y, scene.anchor.z,
                    SoundEvents.ANVIL_PLACE, SoundSource.PLAYERS, 0.7F, 0.6F);
            level.playSound(null, scene.anchor.x, scene.anchor.y, scene.anchor.z,
                    SoundEvents.BELL_RESONATE, SoundSource.PLAYERS, 1.0F, 0.8F);
        } else if (age == 160) {
            // Name invocation begins: reishi resonance hum
            level.playSound(null, scene.anchor.x, scene.anchor.y, scene.anchor.z,
                    SoundEvents.BEACON_ACTIVATE, SoundSource.PLAYERS, 1.8F, 1.4F);
        } else if (age == 228) {
            // Blades shatter simultaneously into millions of petals: glass break & gale rush
            level.playSound(null, scene.anchor.x, scene.anchor.y + 6, scene.anchor.z,
                    SoundEvents.GLASS_BREAK, SoundSource.PLAYERS, 2.0F, 0.8F);
            level.playSound(null, scene.anchor.x, scene.anchor.y + 2, scene.anchor.z,
                    SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.PLAYERS, 1.8F, 0.75F);
        }

        // Atmospheric reishi motes around the player
        if (age >= 35 && age < 228 && age % 2 == 0) {
            double angle = (age * 0.25);
            double dist = 1.5 + ((age - 35.0) / 193.0) * 4.0;
            level.sendParticles(ParticleTypes.END_ROD,
                    scene.anchor.x + Math.cos(angle) * dist, scene.anchor.y + 0.2, scene.anchor.z + Math.sin(angle) * dist,
                    2, 0.1, 0.1, 0.1, 0.01);
        }
    }

    @SubscribeEvent
    public static void logout(PlayerEvent.PlayerLoggedOutEvent e) {
        if (e.getEntity() instanceof ServerPlayer p) cancel(p);
    }

    @SubscribeEvent
    public static void death(net.minecraftforge.event.entity.living.LivingDeathEvent e) {
        if (e.getEntity() instanceof ServerPlayer p) cancel(p);
    }

    @SubscribeEvent
    public static void dimension(PlayerEvent.PlayerChangedDimensionEvent e) {
        if (e.getEntity() instanceof ServerPlayer p) cancel(p);
    }

    @SubscribeEvent
    public static void stop(ServerStoppingEvent e) {
        SCENES.clear();
    }
}
