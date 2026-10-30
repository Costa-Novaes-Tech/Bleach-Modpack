package com.bleachmod.client.hud;

import com.bleachmod.Reference;
import com.bleachmod.client.gui.NpcQuestScreen;
import com.bleachmod.common.data.PlayerCapability;
import com.bleachmod.common.network.SyncHelper;
import com.bleachmod.common.network.NetworkHandler;
import com.bleachmod.common.quest.*;
import com.bleachmod.entity.QuestNpcEntity;
import com.bleachmod.registry.ModEntities;
import net.minecraft.client.*;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.tutorial.TutorialSteps;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.*;
import net.minecraft.world.level.levelgen.WorldOptions;
import net.minecraft.world.level.levelgen.presets.WorldPresets;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import java.io.File;
import java.nio.file.*;
import java.util.UUID;
import java.util.function.Consumer;

/** Opt-in real Forge client scenario. Only creates new disposable worlds in its own run directory. */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT)
public final class StoryQuestQaBootstrap {
    private static boolean created;
    private static volatile boolean busy;
    private static volatile RuntimeException failure;
    private static int ticks, waiting;
    private static File evidence;
    private static String capture;
    private static UUID opening, next, wrong, kon;
    private static int openingId, nextId, wrongId, konId;
    private static boolean enabled() {
        return !net.minecraftforge.fml.loading.FMLEnvironment.production && Boolean.getBoolean("bleachmod.storyQuestQa")
                && Minecraft.getInstance().gameDirectory.toPath().toAbsolutePath().normalize().toString().replace('\\','/')
                .endsWith("/run/story-quest-qa");
    }
    private static void server(Consumer<ServerPlayer> action) {
        var mc = Minecraft.getInstance(); var id = mc.player.getUUID(); busy = true;
        mc.getSingleplayerServer().execute(() -> {
            try {
                var player = mc.getSingleplayerServer().getPlayerList().getPlayer(id); action.accept(player);
                com.bleachmod.server.events.QuestNavigationEvents.refresh(player); SyncHelper.full(player);
            } catch (RuntimeException error) { failure = error; }
            finally { busy = false; }
        });
    }
    private static QuestNpcEntity npc(ServerPlayer p, String id, String context, double x, double z) {
        var npc = ModEntities.questNpcs().get(id).get().create(p.serverLevel());
        npc.moveTo(x, -60, z, 180, 0); npc.setQuestContext(context); p.serverLevel().addFreshEntity(npc); return npc;
    }
    private static void require(boolean result, String detail) { if (!result) throw new IllegalStateException(detail); }
    @SubscribeEvent public static void tick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !enabled()) return;
        var mc = Minecraft.getInstance();
        if (!created && mc.screen instanceof TitleScreen) {
            created = true; String name = "Story-Quest-QA-" + System.currentTimeMillis();
            evidence = new File(mc.gameDirectory, "evidence/" + name);
            var rules = new GameRules(); rules.getRule(GameRules.RULE_DOMOBSPAWNING).set(false, null);
            rules.getRule(GameRules.RULE_DAYLIGHT).set(false, null);
            var settings = new LevelSettings(name, GameType.CREATIVE, false, net.minecraft.world.Difficulty.NORMAL, true, rules, WorldDataConfiguration.DEFAULT);
            mc.getTutorial().setStep(TutorialSteps.NONE); mc.options.languageCode = "pt_br";
            mc.getLanguageManager().setSelected("pt_br"); mc.reloadResourcePacks();
            mc.createWorldOpenFlows().createFreshLevel(name, settings, new WorldOptions(102926, false, false),
                    registry -> registry.registryOrThrow(net.minecraft.core.registries.Registries.WORLD_PRESET)
                            .getHolderOrThrow(WorldPresets.FLAT).value().createWorldDimensions());
        }
        if (mc.player == null || !mc.hasSingleplayerServer() || !mc.getSingleplayerServer().getWorldData().getLevelName().startsWith("Story-Quest-QA-")) return;
        if (failure != null) throw failure;
        if (busy) return;
        ticks++;
        if (ticks >= 30 && openingId == 0) {
            server(p -> {
                var target = QuestNpcLocations.get(p.server).resolveContext("rukia", "karakura_inicio",
                        Level.OVERWORLD.location(), null, p.getX(), p.getY(), p.getZ());
                if (target != null && p.serverLevel().getEntity(target.entityId()) instanceof QuestNpcEntity npc) {
                    opening = npc.getUUID(); openingId = npc.getId();
                }
            });
            if (++waiting > 200) throw new IllegalStateException("Opening Rukia was not provisioned");
            ticks--; return;
        }
        var data = PlayerCapability.get(mc.player).orElse(null);
        boolean ready = ticks == 50 ? data != null && data.getStatus().hasCreatedCharacter() && mc.level.getEntity(openingId) != null
                : ticks == 110 ? data != null && data.getPlayerQuestData().isAccepted("shinigami_substitute:1")
                : ticks == 170 ? mc.screen instanceof NpcQuestScreen
                : ticks == 220 ? data != null && RukiaOpeningQuest.stage(data.getPlayerQuestData().getProgress(RukiaOpeningQuest.KEY)) == 1
                : ticks == 265 ? mc.player.isAlive() && data != null && RukiaOpeningQuest.isRetryPending(data.getPlayerQuestData().getProgress(RukiaOpeningQuest.KEY))
                : true;
        if (!ready) { if (++waiting > 200) throw new IllegalStateException("Story QA sync timeout " + ticks); ticks--; return; }
        waiting = 0;
        if (ticks == 20) {
            mc.options.pauseOnLostFocus = false; mc.options.guiScale().set(2); mc.resizeDisplay(); mc.setScreen(null);
            server(p -> {
                PlayerCapability.get(p).ifPresent(d -> d.initializeShinigami());
                var b = npc(p, "rukia", "karakura_orihime", 16.5, 10.5); next = b.getUUID(); nextId = b.getId();
                var c = npc(p, "rukia", "karakura_cemiterio", 5.5, .5); wrong = c.getUUID(); wrongId = c.getId();
                var k = npc(p, "kon", "karakura_escola", 24.5, 10.5); kon = k.getUUID(); konId = k.getId();
                p.connection.teleport(.5, -60, 8.5, 0, 0);
            });
        }
        if (ticks > 20 && ticks < 45) mc.setScreen(null);
        if (ticks == 50) { com.bleachmod.client.gui.StoryToastManager.clear(); mc.setScreen(new com.bleachmod.client.gui.JournalScreen()); }
        if (ticks == 65) capture = "01-journal-only-opening";
        if (ticks == 80) {
            mc.setScreen(null);
            NetworkHandler.sendToServer(new com.bleachmod.common.network.c2s.QuestActionC2S(
                    com.bleachmod.common.network.c2s.QuestActionC2S.Action.START, RukiaOpeningQuest.KEY, -1));
        }
        if (ticks == 110) {
            server(p -> PlayerCapability.get(p).ifPresent(d -> {
                require(!d.getPlayerQuestData().getProgress("shinigami_substitute:1").isEncounterStarted(), "Acceptance spawned boss");
                require(QuestRegistry.allQuests().stream().filter(q -> QuestAvailabilityChecker.isVisible(d, q)).count() == 1, "Future quests visible at start");
                require(QuestRegistry.allQuests().size() == 28 && QuestRegistry.getQuest("soul_society:1") == null,
                        "Obsolete quests remain active");
                d.getPlayerQuestData().setTrackedQuestId("orihime_npc_test");
                require("shinigami_substitute:1".equals(d.getPlayerQuestData().getTrackedQuestId()), "Story priority lost");
                p.connection.teleport(.5, -60, -5.5, 0, 0);
            }));
        }
        if (ticks == 110) com.bleachmod.client.gui.StoryToastManager.clear();
        if (ticks == 125) capture = "02-compass-meet-rukia";
        if (ticks == 140) server(p -> {
            var npc = (QuestNpcEntity) p.serverLevel().getEntity(opening);
            p.connection.teleport(npc.getX(), npc.getY(), npc.getZ() - 2, 0, 0);
            npc.mobInteract(p, net.minecraft.world.InteractionHand.MAIN_HAND);
        });
        if (ticks == 180) capture = "03-introduction-dialogue";
        if (ticks == 190) {
            mc.setScreen(null);
            NetworkHandler.sendToServer(new com.bleachmod.common.network.c2s.QuestActionC2S(
                    com.bleachmod.common.network.c2s.QuestActionC2S.Action.BEGIN, RukiaOpeningQuest.KEY, openingId));
        }
        if (ticks == 220) capture = "04-boss-stage";
        if (ticks == 230) server(p -> p.kill());
        if (ticks == 250) mc.player.respawn();
        if (ticks == 265) server(p -> PlayerCapability.get(p).ifPresent(d -> {
            var progress = d.getPlayerQuestData().getProgress(RukiaOpeningQuest.KEY);
            require(progress.getObjectiveProgress(0) == 1 && opening.equals(progress.getStoryTargetUuid()), "Death lost introduction or Rukia binding");
            var npc = (QuestNpcEntity) p.serverLevel().getEntity(opening);
            p.connection.teleport(npc.getX(), npc.getY(), npc.getZ() - 2, 0, 0);
        }));
        if (ticks == 280) mc.setScreen(new com.bleachmod.client.gui.JournalScreen());
        if (ticks == 300) capture = "04a-respawn-combat-checkpoint";
        if (ticks == 315) {
            mc.setScreen(null);
            NetworkHandler.sendToServer(new com.bleachmod.common.network.c2s.QuestActionC2S(
                    com.bleachmod.common.network.c2s.QuestActionC2S.Action.BEGIN, RukiaOpeningQuest.KEY, -1));
        }
        if (ticks == 350) server(p -> PlayerCapability.get(p).ifPresent(d -> {
            var progress = d.getPlayerQuestData().getProgress(RukiaOpeningQuest.KEY);
            require(progress.isEncounterStarted() && progress.getObjectiveProgress(0) == 1, "Retry failed to retain introduction");
        }));
        if (ticks == 440) server(p -> {
            net.minecraft.world.entity.LivingEntity actor = null;
            for (var entity : p.serverLevel().getAllEntities()) if ("shinigami_substitute:1".equals(entity.getPersistentData().getString(Reference.TAG_QUEST_KEY))
                    && p.getUUID().equals(entity.getPersistentData().getUUID(Reference.TAG_QUEST_OWNER))) actor = (net.minecraft.world.entity.LivingEntity) entity;
            require(actor instanceof com.bleachmod.entity.HollowBossEntity, "Opening boss missing");
            require(p.getInventory().countItem(com.bleachmod.init.ModItems.ASAUCHI.get()) == 0, "Asauchi given before return");
            ((com.bleachmod.entity.HollowBossEntity) actor).finishEntrance();
            actor.hurt(p.damageSources().playerAttack(p), 100000);
            require(!PlayerCapability.get(p).orElseThrow(() -> new IllegalStateException("No data")).getPlayerQuestData().isCompleted("shinigami_substitute:1"), "Kill completed without return");
            p.connection.teleport(.5, -60, -5.5, 0, 0);
        });
        if (ticks == 460) capture = "05-return-stage";
        if (ticks == 475) server(p -> {
            var npc = (QuestNpcEntity) p.serverLevel().getEntity(opening);
            p.connection.teleport(npc.getX(), npc.getY(), npc.getZ() - 2, 0, 0);
            npc.mobInteract(p, net.minecraft.world.InteractionHand.MAIN_HAND);
        });
        if (ticks == 500) capture = "06-final-dialogue";
        if (ticks == 515) {
            mc.setScreen(null);
            NetworkHandler.sendToServer(new com.bleachmod.common.network.c2s.QuestActionC2S(
                    com.bleachmod.common.network.c2s.QuestActionC2S.Action.BEGIN, RukiaOpeningQuest.KEY, openingId));
        }
        if (ticks == 545) server(p -> PlayerCapability.get(p).ifPresent(d -> {
            require(d.getPlayerQuestData().isCompleted(RukiaOpeningQuest.KEY), "Final dialogue did not complete");
            require(p.getInventory().countItem(com.bleachmod.init.ModItems.ASAUCHI.get()) == 1, "Final sword missing");
        }));
        if (ticks == 555) mc.setScreen(new com.bleachmod.client.gui.JournalScreen());
        if (ticks == 575) capture = "07-completed-next-unlocked";
        if (ticks == 595) {
            com.mojang.logging.LogUtils.getLogger().info("Story quest QA complete: {}", evidence); mc.stop();
        }
    }
    @SubscribeEvent public static void render(TickEvent.RenderTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !enabled() || capture == null) return;
        var mc = Minecraft.getInstance(); String name = capture + ".png"; capture = null;
        try {
            Files.createDirectories(evidence.toPath()); Screenshot.grab(evidence, name, mc.getMainRenderTarget(), message -> {});
            Files.writeString(evidence.toPath().resolve("frames.jsonl"), "{\"file\":\"screenshots/" + name + "\",\"tick\":" + ticks
                    + ",\"guiScale\":" + mc.options.guiScale().get() + ",\"source\":\"Minecraft Screenshot.grab framebuffer\"}\n",
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (Exception error) { throw new IllegalStateException("Story QA capture", error); }
    }
}
