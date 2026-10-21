package com.bleachmod.common.technique;

import com.bleachmod.Reference;
import com.bleachmod.common.data.CharacterData;
import com.bleachmod.common.data.PlayerCapability;
import com.bleachmod.common.network.SyncHelper;
import com.bleachmod.entity.IceDragonEntity;
import com.bleachmod.init.ModSounds;
import com.bleachmod.registry.ModEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Server-authoritative lifecycle controller for Daiguren Hyorinmaru's Bankai Transformation Cinematic.
 * Total presentation duration: 70 ticks (3.5 seconds).
 */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public final class HyorinmaruCinematicService {
    public static final int DURATION = 70;

    private record Scene(long start, Vec3 anchor, float yaw, net.minecraft.resources.ResourceKey<net.minecraft.world.level.Level> dimension) { }

    private static final Map<UUID, Scene> SCENES = new HashMap<>();
    private static final Map<UUID, UUID> MANIFESTATIONS = new HashMap<>();

    private HyorinmaruCinematicService() { }

    public static void begin(ServerPlayer player) {
        Vec3 anchor = player.position();
        SCENES.put(player.getUUID(), new Scene(player.level().getGameTime(), anchor, player.getYRot(), player.level().dimension()));
        SyncHelper.appearance(player);
    }

    public static boolean active(ServerPlayer player) {
        Scene s = SCENES.get(player.getUUID());
        if (s == null) return false;
        long age = player.level().getGameTime() - s.start;
        return age >= 0 && age < DURATION;
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
        UUID hydraId = MANIFESTATIONS.remove(player.getUUID());
        if (hydraId != null && player.level() instanceof ServerLevel sl) {
            var entity = sl.getEntity(hydraId);
            if (entity != null) entity.discard();
        }
        if (SCENES.remove(player.getUUID()) != null) {
            SyncHelper.appearance(player);
        }
    }

    @SubscribeEvent
    public static void tick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !(event.player instanceof ServerPlayer p)) return;
        var s = SCENES.get(p.getUUID());
        if (s == null) return;

        long age = p.level().getGameTime() - s.start;
        var data = PlayerCapability.get(p).orElse(null);

        boolean valid = data != null
                && CharacterData.HYORINMARU.equals(data.getCharacter().getZanpakutoIdentity())
                && p.isAlive()
                && !p.isSpectator()
                && s.dimension.equals(p.level().dimension())
                && age < DURATION
                && p.position().distanceToSqr(s.anchor) < 256.0;

        if (age >= 50 && data != null && (data.getGuard().shownActive() || data.getStatus().isDodging())) {
            valid = false;
        }

        if (!valid) {
            cancel(p);
            return;
        }

        ServerLevel level = p.serverLevel();
        Vec3 pos = s.anchor;

        // Stage 1 (Ticks 0-20): Subzero Frost Vortex & "Bankai" Voice
        if (age == 12) {
            level.playSound(null, pos.x, pos.y, pos.z, ModSounds.HYORINMARU_VOICE_BANKAI.get(), SoundSource.PLAYERS, 1.2F, 1.0F);
        }
        if (age < 25) {
            if (age % 3 == 0) {
                level.sendParticles(ParticleTypes.SNOWFLAKE, pos.x, pos.y + 0.1, pos.z, 20, 1.2, 0.2, 1.2, 0.02);
            }
        }
        // Stage 2 (Tick 25): Ring of Ice Eruption and Hydra Emergence
        else if (age == 25) {
            level.playSound(null, pos.x, pos.y, pos.z, SoundEvents.GLASS_BREAK, SoundSource.PLAYERS, 1.0F, 0.8F);
            level.sendParticles(ParticleTypes.ITEM_SNOWBALL, pos.x, pos.y + 0.3, pos.z, 60, 2.5, 0.5, 2.5, 0.1);

            // Spawn the stationary 3-headed Hydra entity behind the player
            IceDragonEntity hydra = ModEntities.ICE_DRAGON.get().create(level);
            if (hydra != null) {
                hydra.configureStationary(p, pos, s.yaw, DURATION - 20);
                if (level.addFreshEntity(hydra)) {
                    MANIFESTATIONS.put(p.getUUID(), hydra.getUUID());
                }
            }
        }
        // Stage 3 (Tick 40): Rising Hydra, Name Callout
        else if (age == 40) {
            level.playSound(null, pos.x, pos.y, pos.z, ModSounds.HYORINMARU_VOICE_NAME.get(), SoundSource.PLAYERS, 1.3F, 1.0F);
            level.playSound(null, pos.x, pos.y, pos.z, SoundEvents.ENDER_DRAGON_GROWL, SoundSource.PLAYERS, 0.6F, 1.3F);
            level.sendParticles(ParticleTypes.SNOWFLAKE, pos.x, pos.y + 1.2, pos.z, 40, 2.0, 0.8, 2.0, 0.05);
        }
        // Stage 4 (Tick 55): Hydra Roar & Frost Shockwave
        else if (age == 55) {
            level.playSound(null, pos.x, pos.y, pos.z, ModSounds.HYORINMARU_ROAR.get(), SoundSource.PLAYERS, 1.4F, 0.9F);
            level.playSound(null, pos.x, pos.y, pos.z, SoundEvents.GLASS_BREAK, SoundSource.PLAYERS, 1.2F, 0.5F);
            level.sendParticles(ParticleTypes.SNOWFLAKE, pos.x, pos.y + 1.5, pos.z, 120, 3.5, 1.0, 3.5, 0.2);
        }
    }

    @SubscribeEvent
    public static void logout(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity() instanceof ServerPlayer p) cancel(p);
    }

    @SubscribeEvent
    public static void hurt(LivingHurtEvent event) {
        if (event.getEntity() instanceof ServerPlayer p) {
            cancel(p);
        }
    }

    @SubscribeEvent
    public static void stop(ServerStoppingEvent event) {
        SCENES.clear();
        MANIFESTATIONS.clear();
    }
}
