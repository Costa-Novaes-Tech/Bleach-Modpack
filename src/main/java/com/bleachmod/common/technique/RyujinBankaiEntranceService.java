package com.bleachmod.common.technique;

import com.bleachmod.Reference;
import com.bleachmod.common.data.PlayerCapability;
import com.bleachmod.common.network.SyncHelper;
import com.bleachmod.common.visual.RyujinBankaiTimeline;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
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
 * Authoritative presentation service for Ryūjin Jakka / Zanka no Tachi Bankai cinematic.
 * Tracks the 240-tick visual presentation separately from the 60/40 gameplay release contract.
 */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public final class RyujinBankaiEntranceService {
    public static final int DURATION = RyujinBankaiTimeline.DURATION;

    private record Scene(long start, Vec3 anchor, float yaw, ResourceKey<Level> dimension) { }
    private static final Map<UUID, Scene> SCENES = new HashMap<>();

    private RyujinBankaiEntranceService() { }

    public static void begin(ServerPlayer player) {
        Vec3 anchor = player.position();
        SCENES.put(player.getUUID(), new Scene(player.level().getGameTime(), anchor, player.getYRot(), player.level().dimension()));
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

    public static boolean active(ServerPlayer player) {
        return SCENES.containsKey(player.getUUID());
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

        var d = PlayerCapability.get(p).orElse(null);
        long age = p.level().getGameTime() - s.start;

        boolean valid = d != null && d.getCharacter().getZanpakutoIdentity().equals("ryujin_jakka")
                && TechniqueProgression.equipped(p, d)
                && (ReleaseAnimationService.busy(p) || (age >= 40 && d.getCharacter().getActiveForm().equals("bankai")));

        if (age >= 60 && d != null && (!SignatureAnimationService.snapshot(p).isEmpty()
                || d.getGuard().shownActive()
                || d.getStatus().isDodging()
                || ElementalTechniqueService.busy(p)
                || d.getStatus().getFlameDashTicks() > 0)) {
            valid = false;
        }

        if (!valid || !p.isAlive() || p.isSpectator() || s.dimension != p.level().dimension()
                || age >= DURATION || p.position().distanceToSqr(s.anchor) > 512 * 512) {
            cancel(p);
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
