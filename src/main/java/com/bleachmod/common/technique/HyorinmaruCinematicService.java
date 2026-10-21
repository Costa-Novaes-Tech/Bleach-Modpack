package com.bleachmod.common.technique;

import com.bleachmod.Reference;
import com.bleachmod.common.data.CharacterData;
import com.bleachmod.common.data.PlayerCapability;
import com.bleachmod.common.network.SyncHelper;
import com.bleachmod.init.ModSounds;
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
 * Total presentation duration: 140 ticks (7.0 seconds).
 */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public final class HyorinmaruCinematicService {
    public static final int DURATION = 140;

    private record Scene(long start, Vec3 anchor, float yaw, net.minecraft.resources.ResourceKey<net.minecraft.world.level.Level> dimension) { }

    private static final Map<UUID, Scene> SCENES = new HashMap<>();

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

        if (!valid) {
            cancel(p);
            return;
        }

        ServerLevel level = p.serverLevel();
        Vec3 pos = s.anchor;

        // Stage 1 (Ticks 0-25): Subzero Frost Vortex Gathering & "Bankai" Voice
        if (age == 15) {
            level.playSound(null, pos.x, pos.y, pos.z, ModSounds.HYORINMARU_VOICE_BANKAI.get(), SoundSource.PLAYERS, 1.2F, 1.0F);
        }
        if (age < 25) {
            if (age % 4 == 0) {
                level.sendParticles(ParticleTypes.SNOWFLAKE, pos.x, pos.y + 0.1, pos.z, 18, 1.2, 0.2, 1.2, 0.02);
            }
        }
        // Stage 2 (Ticks 25-50): Ring of Ice Eruption on Ground
        else if (age == 25) {
            level.playSound(null, pos.x, pos.y, pos.z, SoundEvents.GLASS_BREAK, SoundSource.PLAYERS, 1.0F, 0.8F);
            level.sendParticles(ParticleTypes.ITEM_SNOWBALL, pos.x, pos.y + 0.3, pos.z, 50, 2.5, 0.5, 2.5, 0.1);
        }
        // Stage 3 (Ticks 50-95): Rising Ice Hydra, Frost Gale & "...Daiguren Hyorinmaru!" Voice
        else if (age == 50) {
            level.playSound(null, pos.x, pos.y, pos.z, ModSounds.HYORINMARU_VOICE_NAME.get(), SoundSource.PLAYERS, 1.3F, 1.0F);
            level.playSound(null, pos.x, pos.y, pos.z, SoundEvents.ENDER_DRAGON_GROWL, SoundSource.PLAYERS, 0.5F, 1.4F);
        }
        // Stage 5 (Ticks 120): Triple Hydra Roar & Shockwave
        else if (age == 120) {
            level.playSound(null, pos.x, pos.y, pos.z, ModSounds.HYORINMARU_ROAR.get(), SoundSource.PLAYERS, 1.4F, 0.9F);
            level.playSound(null, pos.x, pos.y, pos.z, SoundEvents.GLASS_BREAK, SoundSource.PLAYERS, 1.2F, 0.5F);
            level.sendParticles(ParticleTypes.SNOWFLAKE, pos.x, pos.y + 1.5, pos.z, 140, 4.0, 1.0, 4.0, 0.25);
        }
    }

    @SubscribeEvent
    public static void logout(PlayerEvent.PlayerLoggedOutEvent event) {
        SCENES.remove(event.getEntity().getUUID());
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
    }
}
