package com.bleachmod.server.commands;

import com.bleachmod.Reference;
import com.bleachmod.common.data.PlayerCapability;
import com.bleachmod.common.network.SyncHelper;
import com.bleachmod.entity.HollowBossEntity;
import com.bleachmod.registry.ModEntities;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/** Fresh, explicitly opted-in development worlds only; never touches a user's existing save. */
@Mod.EventBusSubscriber(modid=Reference.MOD_ID)
public final class BossQaWorld {
    private BossQaWorld() { }

    @SubscribeEvent public static void joined(PlayerEvent.PlayerLoggedInEvent event) {
        if (net.minecraftforge.fml.loading.FMLEnvironment.production || !Boolean.getBoolean("bleachmod.bossQa")
                || !(event.getEntity() instanceof ServerPlayer player)
                || !player.getServer().getWorldData().getLevelName().startsWith("Boss-QA-")) return;
        var level = player.serverLevel();
        int y = level.getHeight(net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING, 0, 0);
        PlayerCapability.get(player).ifPresent(data -> {
            data.initializeShinigami();
            SyncHelper.full(player);
            SyncHelper.appearance(player);
        });
        player.setGameMode(net.minecraft.world.level.GameType.CREATIVE);
        player.teleportTo(level, .5, y + 2, 16.5, 180, 0);
        player.getAbilities().flying = true;
        player.onUpdateAbilities();
        level.setDayTime(6000);
        var boss = ModEntities.HOLLOW_BOSS.get().create(level);
        boss.setPos(.5, y, .5);
        boss.setYRot(0); boss.setYBodyRot(0); boss.setYHeadRot(0);
        boss.setNoAi(true);
        boss.setPersistenceRequired();
        boss.addTag("boss_qa_actor");
        // Remove autonomous decision-making only from this disposable preview actor.
        boss.goalSelector.removeAllGoals(goal -> true);
        boss.targetSelector.removeAllGoals(goal -> true);
        level.addFreshEntity(boss);
        var ichigo = ModEntities.NPC_ICHIGO.get().create(level);
        ichigo.setPos(4, y, .5);
        ichigo.setYRot(0); ichigo.setYBodyRot(0); ichigo.setYHeadRot(0);
        ichigo.setNoAi(true);
        ichigo.setPersistenceRequired();
        ichigo.setInvulnerable(true);
        ichigo.addTag("boss_qa_scale_reference");
        level.addFreshEntity(ichigo);
    }

    /** Authoritative tick/health trace, stored separately from client screenshots. */
    @SubscribeEvent public static void trace(net.minecraftforge.event.TickEvent.LevelTickEvent event) {
        if (event.phase != net.minecraftforge.event.TickEvent.Phase.END
                || net.minecraftforge.fml.loading.FMLEnvironment.production || !Boolean.getBoolean("bleachmod.bossQa")
                || !(event.level instanceof net.minecraft.server.level.ServerLevel level)
                || level.dimension() != net.minecraft.world.level.Level.OVERWORLD || level.getGameTime() % 2 != 0) return;
        String world = level.getServer().getWorldData().getLevelName();
        if (!world.startsWith("Boss-QA-")) return;
        var destination = level.getServer().getServerDirectory().toPath().resolve("boss-evidence")
                .resolve(world.replaceAll("[^A-Za-z0-9_-]", "_")).resolve("combat.jsonl");
        for (var entity : level.getAllEntities()) {
            if (!(entity instanceof HollowBossEntity boss) || !boss.getTags().contains("boss_qa_actor")) continue;
            var row = new com.google.gson.JsonObject();
            row.addProperty("gameTick", level.getGameTime());
            row.addProperty("entityTick", boss.tickCount);
            row.addProperty("attack", boss.getAttack().name());
            row.addProperty("attackTick", boss.getAttackTick());
            row.addProperty("chargeCooldown", boss.getChargeCooldownTicks());
            row.addProperty("slamCooldown", boss.getSlamCooldownTicks());
            row.addProperty("meleeCooldown", boss.getMeleeCooldownTicks());
            row.addProperty("x", boss.getX()); row.addProperty("y", boss.getY()); row.addProperty("z", boss.getZ());
            row.addProperty("height", boss.getBbHeight());
            row.addProperty("width", boss.getBbWidth());
            if (boss.getTarget() != null) {
                row.addProperty("targetHealth", boss.getTarget().getHealth());
                row.addProperty("targetDistance", boss.distanceTo(boss.getTarget()));
            }
            try {
                java.nio.file.Files.createDirectories(destination.getParent());
                java.nio.file.Files.writeString(destination, row + System.lineSeparator(),
                        java.nio.file.StandardOpenOption.CREATE, java.nio.file.StandardOpenOption.APPEND);
            } catch (java.io.IOException error) {
                com.mojang.logging.LogUtils.getLogger().error("Boss QA server trace failed", error);
            }
        }
    }
}
