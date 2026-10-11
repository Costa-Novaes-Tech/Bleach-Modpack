package com.bleachmod.common.visual;

import com.bleachmod.Reference;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import java.util.*;

/** Owner-only vanilla effect previews. No entities, blocks, combat, progression or persistent data. */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public final class VanillaAssetBench {
    public static final int DURATION = 100, INTERVAL = 4, SESSION_LIMIT = 8;
    public enum Preset {
        FROST("frost"), REIATSU("reiatsu"), SOUL("soul"), SLASH("slash"), BEAM("beam"), CRYSTAL("crystal");
        public final String id;
        Preset(String id) { this.id = id; }
    }
    public static List<String> ids() { return Arrays.stream(Preset.values()).map(p -> p.id).toList(); }
    public static Preset resolve(String id) {
        return Arrays.stream(Preset.values()).filter(p -> p.id.equals(id)).findFirst().orElseThrow(() -> new IllegalArgumentException("Unknown preview"));
    }
    public static int samples(int intensity) {
        if (intensity < 1 || intensity > 3) throw new IllegalArgumentException("Intensity must be 1..3");
        return 8 * intensity;
    }
    private static final class Session {
        final ServerPlayer owner;
        final ServerLevel level;
        final Preset preset;
        final int intensity;
        final Vec3 origin, forward;
        final long started;
        long lastFrame = Long.MIN_VALUE;
        int particles;
        Session(ServerPlayer owner, Preset preset, int intensity) {
            this.owner = owner; this.level = owner.serverLevel(); this.preset = preset; this.intensity = intensity;
            this.forward = Vec3.directionFromRotation(0, owner.getYRot());
            this.origin = owner.position().add(forward.scale(4)); this.started = level.getGameTime();
        }
    }
    private static final Map<UUID, Session> SESSIONS = new HashMap<>();
    private VanillaAssetBench() { }
    public static boolean start(ServerPlayer owner, Preset preset, int intensity) {
        samples(intensity); Objects.requireNonNull(preset);
        if (!owner.isAlive() || owner.isSpectator()) return false;
        if (!SESSIONS.containsKey(owner.getUUID()) && SESSIONS.size() >= SESSION_LIMIT) return false;
        Session session = new Session(owner, preset, intensity); SESSIONS.put(owner.getUUID(), session);
        owner.playNotifySound(switch (preset) {
            case FROST -> SoundEvents.GLASS_BREAK;
            case REIATSU -> SoundEvents.BEACON_ACTIVATE;
            case SOUL -> SoundEvents.SOUL_ESCAPE;
            case SLASH -> SoundEvents.PLAYER_ATTACK_SWEEP;
            case BEAM -> SoundEvents.GUARDIAN_ATTACK;
            case CRYSTAL -> SoundEvents.AMETHYST_BLOCK_CHIME;
        }, SoundSource.PLAYERS, .35F, 1);
        tick(owner); return true;
    }
    public static void stop(ServerPlayer owner) { SESSIONS.remove(owner.getUUID()); }
    public static void clear() { SESSIONS.clear(); }
    public static int emitted(ServerPlayer owner) { Session s = SESSIONS.get(owner.getUUID()); return s == null ? 0 : s.particles; }
    public static int remaining(ServerPlayer owner) { Session s = SESSIONS.get(owner.getUUID()); return s == null ? 0 : (int) Math.max(0, DURATION - (s.level.getGameTime() - s.started)); }
    public static String active(ServerPlayer owner) { Session s = SESSIONS.get(owner.getUUID()); return s == null ? "none" : s.preset.id; }
    public static int activeCount() { return SESSIONS.size(); }
    public static Vec3 point(Preset preset, Vec3 origin, Vec3 forward, int index, int count, double phase) {
        double t = index / (double) Math.max(1, count - 1), angle = Math.PI * 2 * t + phase;
        Vec3 right = new Vec3(forward.z, 0, -forward.x);
        return switch (preset) {
            case FROST -> origin.add(Math.cos(angle) * 2, .4 + Math.sin(angle * 2) * .2, Math.sin(angle) * 2);
            case REIATSU, SOUL -> origin.add(Math.cos(angle) * (1 - .4 * t), .2 + t * 3, Math.sin(angle) * (1 - .4 * t));
            case SLASH -> origin.add(right.scale(Math.cos(Math.PI * t) * 2)).add(0, .3 + Math.sin(Math.PI * t) * 2, 0);
            case BEAM -> origin.add(forward.scale(t * 6)).add(0, 1 + Math.sin(angle) * .12, 0);
            case CRYSTAL -> origin.add(Math.cos(angle) * .8, .3 + t * 2.5, Math.sin(angle) * .8);
        };
    }
    private static ParticleOptions primary(Preset preset) {
        return switch (preset) {
            case FROST -> ParticleTypes.SNOWFLAKE;
            case REIATSU -> ParticleTypes.END_ROD;
            case SOUL -> ParticleTypes.SOUL;
            case SLASH -> ParticleTypes.SWEEP_ATTACK;
            case BEAM -> ParticleTypes.ELECTRIC_SPARK;
            case CRYSTAL -> new DustParticleOptions(new org.joml.Vector3f(.7F, .35F, 1), 1);
        };
    }
    private static ParticleOptions secondary(Preset preset) {
        return switch (preset) {
            case FROST -> new DustParticleOptions(new org.joml.Vector3f(.4F, .8F, 1), 1);
            case REIATSU -> ParticleTypes.ENCHANT;
            case SOUL -> ParticleTypes.SOUL_FIRE_FLAME;
            case SLASH -> ParticleTypes.CRIT;
            case BEAM -> ParticleTypes.END_ROD;
            case CRYSTAL -> ParticleTypes.REVERSE_PORTAL;
        };
    }
    public static void tick(ServerPlayer owner) {
        Session session = SESSIONS.get(owner.getUUID()); if (session == null) return;
        long now = session.level.getGameTime(), age = now - session.started;
        if (session.owner != owner || owner.serverLevel() != session.level || !owner.isAlive() || owner.isSpectator()
                || age >= DURATION || age < 0 || owner.position().distanceToSqr(session.origin) > 32 * 32) { stop(owner); return; }
        if (age % INTERVAL != 0 || session.lastFrame == now) return;
        session.lastFrame = now;
        int count = samples(session.intensity);
        ParticleOptions primary = primary(session.preset), secondary = secondary(session.preset);
        for (int i = 0; i < count; i++) {
            Vec3 point = point(session.preset, session.origin, session.forward, i, count, age * .09);
            if (!session.level.hasChunkAt(BlockPos.containing(point))) continue;
            // Direct recipient: previews never broadcast to other players or load chunks.
            session.level.sendParticles(owner, primary, false, point.x, point.y, point.z, 1, .03, .03, .03, .01);
            session.level.sendParticles(owner, secondary, false, point.x, point.y, point.z, 1, .08, .08, .08, .01);
            session.particles += 2;
        }
    }
    @SubscribeEvent public static void tick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) for (Session s : new ArrayList<>(SESSIONS.values())) tick(s.owner);
    }
    @SubscribeEvent public static void logout(PlayerEvent.PlayerLoggedOutEvent event) { if (event.getEntity() instanceof ServerPlayer p) stop(p); }
    @SubscribeEvent public static void dimension(PlayerEvent.PlayerChangedDimensionEvent event) { if (event.getEntity() instanceof ServerPlayer p) stop(p); }
    @SubscribeEvent public static void death(LivingDeathEvent event) { if (event.getEntity() instanceof ServerPlayer p) stop(p); }
    @SubscribeEvent public static void unload(LevelEvent.Unload event) {
        if (!event.getLevel().isClientSide()) SESSIONS.values().removeIf(s -> s.level == event.getLevel());
    }
    @SubscribeEvent public static void stopped(ServerStoppedEvent event) { clear(); }
}
