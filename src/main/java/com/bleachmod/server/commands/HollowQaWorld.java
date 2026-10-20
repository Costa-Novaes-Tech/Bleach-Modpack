package com.bleachmod.server.commands;

import com.bleachmod.Reference;
import com.bleachmod.common.data.PlayerCapability;
import com.bleachmod.common.network.SyncHelper;
import com.bleachmod.entity.HollowVariant;
import com.bleachmod.registry.ModEntities;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/** Only prepares fresh disposable saves from the explicitly opted-in development client. */
@Mod.EventBusSubscriber(modid=Reference.MOD_ID)
public final class HollowQaWorld {
    private HollowQaWorld() { }
    @SubscribeEvent public static void joined(PlayerEvent.PlayerLoggedInEvent event) {
        if (net.minecraftforge.fml.loading.FMLEnvironment.production || !Boolean.getBoolean("bleachmod.hollowQa")
                || !(event.getEntity() instanceof ServerPlayer player)
                || !player.getServer().getWorldData().getLevelName().startsWith("Hollow-QA-")) return;
        var level = player.serverLevel();
        int y = level.getHeight(net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING, 0, 0);
        PlayerCapability.get(player).ifPresent(data -> {
            data.initializeShinigami();
            SyncHelper.full(player);
            SyncHelper.appearance(player);
        });
        player.setGameMode(net.minecraft.world.level.GameType.CREATIVE);
        player.teleportTo(level, .5, y + .05, 12.5, 180, 0);
        player.getAbilities().flying = true;
        player.onUpdateAbilities();
        level.setDayTime(6000);
        for (var variant : HollowVariant.values()) {
            var hollow = ModEntities.HOLLOW.get().create(level);
            hollow.setVariant(variant);
            hollow.setHealth(hollow.getMaxHealth());
            hollow.setPos(-4 + variant.ordinal() * 4 + .5, y, .5);
            hollow.setYRot(0);
            hollow.setYBodyRot(0);
            hollow.setYHeadRot(0);
            hollow.setNoAi(true);
            hollow.setPersistenceRequired();
            level.addFreshEntity(hollow);
        }
    }

    /** Server-side trace, independent of screenshot/render timing. Only fresh opt-in QA saves. */
    @SubscribeEvent public static void trace(net.minecraftforge.event.TickEvent.LevelTickEvent event) {
        if (event.phase != net.minecraftforge.event.TickEvent.Phase.END
                || net.minecraftforge.fml.loading.FMLEnvironment.production || !Boolean.getBoolean("bleachmod.hollowQa")
                || !(event.level instanceof net.minecraft.server.level.ServerLevel level)
                || level.dimension() != net.minecraft.world.level.Level.OVERWORLD || level.getGameTime() % 2 != 0) return;
        String world = level.getServer().getWorldData().getLevelName();
        if (!world.startsWith("Hollow-QA-")) return;
        var destination = level.getServer().getServerDirectory().toPath().resolve("hollow-evidence")
                .resolve(world.replaceAll("[^A-Za-z0-9_-]", "_")).resolve("combat.jsonl");
        for (var entity : level.getAllEntities()) {
            if (!(entity instanceof com.bleachmod.entity.HollowEntity mob) || mob.isNoAi() || !mob.usesVariants()) continue;
            var row = new com.google.gson.JsonObject();
            row.addProperty("gameTick", level.getGameTime());
            row.addProperty("entityTick", mob.tickCount);
            row.addProperty("variant", mob.getVariant().id);
            row.addProperty("health", mob.getHealth());
            row.addProperty("maxHealth", mob.getMaxHealth());
            row.addProperty("lastStart", mob.getLastMeleeStartTick());
            row.addProperty("hits", mob.getSuccessfulMeleeHits());
            row.addProperty("cooldown", mob.getMeleeCooldownTicks());
            row.addProperty("phase", mob.getFlightPhase().name());
            row.addProperty("phaseAge", mob.getFlightCombat().getPhaseAge());
            row.addProperty("passes", mob.getFlightCombat().getCompletedPasses());
            row.addProperty("budget", mob.getFlightCombat().getStrikeBudget());
            row.addProperty("attempts", mob.getFlightCombat().getStartedStrikes());
            row.addProperty("lastBurstAttempts", mob.getFlightCombat().getLastBurstAttempts());
            row.addProperty("x", mob.getX()); row.addProperty("y", mob.getY()); row.addProperty("z", mob.getZ());
            if (mob.getTarget() != null) {
                row.addProperty("targetHealth", mob.getTarget().getHealth());
                row.addProperty("targetY", mob.getTarget().getY());
                row.addProperty("distance", mob.distanceTo(mob.getTarget()));
            }
            try {
                java.nio.file.Files.createDirectories(destination.getParent());
                java.nio.file.Files.writeString(destination, row + System.lineSeparator(),
                        java.nio.file.StandardOpenOption.CREATE, java.nio.file.StandardOpenOption.APPEND);
            } catch (java.io.IOException error) {
                com.mojang.logging.LogUtils.getLogger().error("Hollow QA server trace failed", error);
            }
        }
    }
}
