package com.bleachmod.client.hud;

import com.bleachmod.Reference;
import com.bleachmod.common.data.PlayerCapability;
import com.bleachmod.common.network.SyncHelper;
import com.bleachmod.common.quest.*;
import com.bleachmod.entity.QuestNpcEntity;
import com.bleachmod.registry.ModEntities;
import com.bleachmod.server.events.QuestNavigationEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.tutorial.TutorialSteps;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.*;
import net.minecraft.world.level.levelgen.WorldOptions;
import net.minecraft.world.level.levelgen.presets.WorldPresets;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.UUID;
import java.util.function.Consumer;

/** Opt-in, disposable real-client navigation scenario; never opens an existing save. */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT)
public final class QuestNavigationQaBootstrap {
    private static boolean created;
    private static int ticks;
    private static String capture;
    private static File evidence;
    private static UUID rukia;
    private static volatile boolean serverBusy;
    private static volatile RuntimeException serverFailure;
    private static int waitingForSync;

    private static boolean enabled() {
        return !net.minecraftforge.fml.loading.FMLEnvironment.production
                && Boolean.getBoolean("bleachmod.questNavigationQa")
                && Minecraft.getInstance().gameDirectory.toPath().toAbsolutePath().normalize()
                .toString().replace('\\', '/').endsWith("/run/quest-navigation-qa");
    }

    private static void server(Consumer<ServerPlayer> action) {
        var mc = Minecraft.getInstance();
        UUID playerId = mc.player.getUUID();
        serverBusy = true;
        mc.getSingleplayerServer().execute(() -> {
            try {
                var player = mc.getSingleplayerServer().getPlayerList().getPlayer(playerId);
                action.accept(player);
                QuestNavigationEvents.refresh(player);
            } catch (RuntimeException failure) { serverFailure = failure; }
            finally { serverBusy = false; }
        });
    }

    private static QuestNpcEntity npc(ServerPlayer player, EntityType<QuestNpcEntity> type, double x, double z) {
        var npc = type.create(player.serverLevel());
        npc.moveTo(x, -60, z, 180, 0);
        player.serverLevel().addFreshEntity(npc);
        return npc;
    }

    @SubscribeEvent public static void tick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !enabled()) return;
        var mc = Minecraft.getInstance();
        if (!created && mc.screen instanceof TitleScreen) {
            created = true;
            String name = "Quest-Navigation-QA-" + System.currentTimeMillis();
            evidence = new File(mc.gameDirectory, "evidence/" + name);
            var rules = new GameRules();
            rules.getRule(GameRules.RULE_DOMOBSPAWNING).set(false, null);
            rules.getRule(GameRules.RULE_DAYLIGHT).set(false, null);
            var settings = new LevelSettings(name, GameType.CREATIVE, false, net.minecraft.world.Difficulty.PEACEFUL,
                    true, rules, WorldDataConfiguration.DEFAULT);
            mc.getTutorial().setStep(TutorialSteps.NONE);
            mc.options.languageCode = "pt_br";
            mc.getLanguageManager().setSelected("pt_br");
            mc.reloadResourcePacks();
            mc.createWorldOpenFlows().createFreshLevel(name, settings, new WorldOptions(102926, false, false),
                    registry -> registry.registryOrThrow(net.minecraft.core.registries.Registries.WORLD_PRESET)
                            .getHolderOrThrow(WorldPresets.FLAT).value().createWorldDimensions());
        }
        if (mc.player == null || !mc.hasSingleplayerServer()
                || !mc.getSingleplayerServer().getWorldData().getLevelName().startsWith("Quest-Navigation-QA-")) return;
        if (serverFailure != null) throw serverFailure;
        if (serverBusy) return;
        ticks++;
        if (ticks == 285 || ticks == 325 || ticks == 365) {
            var quests = PlayerCapability.get(mc.player).orElseThrow(() -> new IllegalStateException("Missing QA capability")).getPlayerQuestData();
            var target = quests.getWaypoint(quests.getTrackedQuestId());
            boolean ready = ticks == 285 ? target != null && target.dimension().equals(Level.NETHER.location())
                    : ticks == 325 ? "ichigo_npc_test".equals(quests.getTrackedQuestId()) && target == null
                    : "orihime_npc_test".equals(quests.getTrackedQuestId()) && target != null;
            if (!ready) {
                if (++waitingForSync > 200) throw new IllegalStateException("QA synchronization timeout at " + ticks);
                ticks--; return;
            }
            waitingForSync = 0;
        }
        if (ticks == 20) {
            mc.options.pauseOnLostFocus = false;
            mc.options.guiScale().set(2); mc.resizeDisplay(); mc.setScreen(null);
            server(player -> PlayerCapability.get(player).ifPresent(data -> {
                data.initializeShinigami();
                player.teleportTo(.5, -60, 78.5);
                var giver = npc(player, ModEntities.NPC_RUKIA.get(), .5, 80.5);
                rukia = giver.getUUID();
                QuestService.startQuest(player, "rukia_npc_test", giver);
                // Exercise real acceptance, then remove only the QA-spawned fight from this visual fixture.
                for (var entity : player.serverLevel().getAllEntities())
                    if ("rukia_npc_test".equals(entity.getPersistentData().getString(Reference.TAG_QUEST_KEY))) entity.discard();
                npc(player, ModEntities.NPC_RUKIA.get(), 4.5, .5);
                npc(player, ModEntities.NPC_ORIHIME.get(), 40.5, 40.5);
                var quests = data.getPlayerQuestData();
                for (String key : java.util.List.of("orihime_npc_test", "ichigo_npc_test"))
                    quests.acceptQuest(key, QuestRegistry.getQuest(key));
                if (QuestRegistry.getQuest("rukia_basic_training") != null || QuestRegistry.getQuest("rukia_npc_test") == null)
                    throw new IllegalStateException("Rukia catalog retirement failed");
                player.connection.teleport(.5, -60, .5, 0, 0);
                QuestNavigationEvents.refresh(player);
                if (!rukia.equals(quests.getWaypoint("rukia_npc_test").entityId())) throw new IllegalStateException("Giver switched to duplicate");
                SyncHelper.full(player);
            }));
        }
        if (ticks > 20 && ticks < 50) mc.setScreen(null);
        if (ticks == 55) com.bleachmod.client.gui.StoryToastManager.clear();
        if (ticks == 60) capture = "01-rukia-ahead-80m";
        if (ticks == 75) server(p -> p.connection.teleport(.5, -60, .5, 90, 0));
        if (ticks == 95) capture = "02-turn-left";
        if (ticks == 110) server(p -> p.connection.teleport(.5, -60, .5, 180, 0));
        if (ticks == 130) capture = "03-target-behind";
        if (ticks == 145) server(p -> p.connection.teleport(.5, -60, 40.5, 0, 0));
        if (ticks == 170) capture = "04-distance-40m";
        if (ticks == 185) { mc.options.guiScale().set(3); mc.resizeDisplay(); }
        if (ticks == 205) capture = "05-compact-scale3";
        if (ticks == 220) { mc.options.guiScale().set(1); mc.resizeDisplay(); }
        if (ticks == 240) capture = "06-large-scale1";
        if (ticks == 255) {
            mc.options.guiScale().set(2); mc.resizeDisplay();
            server(p -> {
                var giver = (QuestNpcEntity) p.serverLevel().getEntity(rukia);
                var nether = p.server.getLevel(Level.NETHER);
                var movedGiver = giver.changeDimension(nether, new net.minecraftforge.common.util.ITeleporter() {
                    @Override public net.minecraft.world.entity.Entity placeEntity(net.minecraft.world.entity.Entity entity,
                            net.minecraft.server.level.ServerLevel current, net.minecraft.server.level.ServerLevel destination,
                            float yaw, java.util.function.Function<Boolean, net.minecraft.world.entity.Entity> reposition) {
                        var moved = reposition.apply(false); moved.moveTo(.5, 70, 80.5); return moved;
                    }
                });
                if (!(movedGiver instanceof QuestNpcEntity moved)) throw new IllegalStateException("QA NPC dimension transfer failed");
                QuestNpcLocations.get(p.server).update(moved);
            });
        }
        if (ticks == 285) capture = "07-other-dimension";
        if (ticks == 300) server(p -> PlayerCapability.get(p).ifPresent(data -> {
            var quests = data.getPlayerQuestData(); quests.completeQuest("rukia_npc_test");
            quests.setTrackedQuestId("ichigo_npc_test"); SyncHelper.progression(p);
        }));
        if (ticks == 325) capture = "08-unknown-location";
        if (ticks == 340) server(p -> PlayerCapability.get(p).ifPresent(data -> {
            data.getPlayerQuestData().setTrackedQuestId("orihime_npc_test"); SyncHelper.progression(p);
        }));
        if (ticks == 365) capture = "09-fallback-orihime";
        if (ticks == 380) mc.options.hideGui = true;
        if (ticks == 395) capture = "10-hidden-hud";
        if (ticks == 410) { mc.options.hideGui = false; com.mojang.logging.LogUtils.getLogger().info("Quest navigation QA complete: {}", evidence); mc.stop(); }
    }

    @SubscribeEvent public static void render(TickEvent.RenderTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !enabled() || capture == null || evidence == null) return;
        var mc = Minecraft.getInstance();
        String name = capture + ".png"; capture = null;
        try {
            Files.createDirectories(evidence.toPath());
            Screenshot.grab(evidence, name, mc.getMainRenderTarget(), message -> {});
            var quests = PlayerCapability.get(mc.player).orElseThrow(() -> new IllegalStateException("Missing QA capability")).getPlayerQuestData();
            var waypoint = quests.getWaypoint(quests.getTrackedQuestId());
            Files.writeString(evidence.toPath().resolve("frames.jsonl"),
                    "{\"file\":\"screenshots/" + name + "\",\"tick\":" + ticks
                            + ",\"guiWidth\":" + mc.getWindow().getGuiScaledWidth()
                            + ",\"guiHeight\":" + mc.getWindow().getGuiScaledHeight()
                            + ",\"yaw\":" + mc.player.getYRot() + ",\"tracked\":\"" + quests.getTrackedQuestId()
                            + "\",\"dimension\":\"" + (waypoint == null ? "unknown" : waypoint.dimension())
                            + "\",\"distance\":" + (waypoint == null || !waypoint.dimension().equals(mc.player.level().dimension().location()) ? -1 : waypoint.distanceMetres(mc.player.getX(), mc.player.getY(), mc.player.getZ()))
                            + ",\"source\":\"Minecraft Screenshot.grab framebuffer\"}\n",
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (Exception error) { throw new IllegalStateException("Quest navigation QA capture", error); }
    }
}
