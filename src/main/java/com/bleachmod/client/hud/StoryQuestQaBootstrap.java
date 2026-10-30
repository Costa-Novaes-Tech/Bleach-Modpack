package com.bleachmod.client.hud;

import com.bleachmod.Reference;
import com.bleachmod.client.gui.NpcQuestScreen;
import com.bleachmod.common.data.PlayerCapability;
import com.bleachmod.common.network.SyncHelper;
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
        var data = PlayerCapability.get(mc.player).orElse(null);
        boolean ready = ticks == 50 ? data != null && data.getStatus().hasCreatedCharacter() && mc.level.getEntity(openingId) != null
                : ticks == 110 ? data != null && data.getPlayerQuestData().isAccepted("shinigami_substitute:1")
                : ticks == 170 ? data != null && data.getPlayerQuestData().isCompleted("shinigami_substitute:1")
                : ticks == 220 ? data != null && data.getPlayerQuestData().isAccepted("shinigami_substitute:2")
                : ticks == 280 ? data != null && "hollow".equals(data.getCharacter().getRace()) : true;
        if (!ready) { if (++waiting > 200) throw new IllegalStateException("Story QA sync timeout " + ticks); ticks--; return; }
        waiting = 0;
        if (ticks == 20) {
            mc.options.pauseOnLostFocus = false; mc.options.guiScale().set(2); mc.resizeDisplay(); mc.setScreen(null);
            server(p -> {
                PlayerCapability.get(p).ifPresent(d -> d.initializeShinigami());
                var a = npc(p, "rukia", "karakura_inicio", .5, 10.5); opening = a.getUUID(); openingId = a.getId();
                var b = npc(p, "rukia", "karakura_orihime", 16.5, 10.5); next = b.getUUID(); nextId = b.getId();
                var c = npc(p, "rukia", "karakura_cemiterio", 5.5, .5); wrong = c.getUUID(); wrongId = c.getId();
                var k = npc(p, "kon", "karakura_escola", 24.5, 10.5); kon = k.getUUID(); konId = k.getId();
                p.connection.teleport(.5, -60, 8.5, 0, 0);
            });
        }
        if (ticks > 20 && ticks < 45) mc.setScreen(null);
        if (ticks == 50) { com.bleachmod.client.gui.StoryToastManager.clear(); mc.setScreen(new NpcQuestScreen("rukia", openingId)); }
        if (ticks == 65) capture = "01-rukia-opening-offer";
        if (ticks == 80) {
            mc.setScreen(null);
            server(p -> PlayerCapability.get(p).ifPresent(d -> {
                var giver = (QuestNpcEntity) p.serverLevel().getEntity(opening);
                QuestService.startQuest(p, "shinigami_substitute:1", giver);
                require(d.getPlayerQuestData().getProgress("shinigami_substitute:1").isEncounterStarted(), "Opening encounter did not spawn");
                var legacy = QuestRegistry.getQuest("soul_society:1"); d.getPlayerQuestData().acceptQuest(legacy.getQuestKey(), legacy);
                d.getPlayerQuestData().setTrackedQuestId(legacy.getQuestKey());
                require("shinigami_substitute:1".equals(d.getPlayerQuestData().getTrackedQuestId()), "Story priority lost");
                p.connection.teleport(.5, -60, -5.5, 0, 0);
            }));
        }
        if (ticks == 110) com.bleachmod.client.gui.StoryToastManager.clear();
        if (ticks == 125) capture = "02-story-priority-hud";
        if (ticks == 140) server(p -> {
            net.minecraft.world.entity.LivingEntity actor = null;
            for (var entity : p.serverLevel().getAllEntities()) if ("shinigami_substitute:1".equals(entity.getPersistentData().getString(Reference.TAG_QUEST_KEY))
                    && p.getUUID().equals(entity.getPersistentData().getUUID(Reference.TAG_QUEST_OWNER))) actor = (net.minecraft.world.entity.LivingEntity) entity;
            require(actor != null, "Opening actor missing"); actor.hurt(p.damageSources().playerAttack(p), 100000);
            require(PlayerCapability.get(p).orElseThrow(() -> new IllegalStateException("No data")).getPlayerQuestData().isCompleted("shinigami_substitute:1"), "Kill did not complete opening");
            p.connection.teleport(16.5, -60, 8.5, 0, 0);
        });
        if (ticks == 170) { com.bleachmod.client.gui.StoryToastManager.clear(); mc.setScreen(new NpcQuestScreen("rukia", nextId)); }
        if (ticks == 185) capture = "03-next-mission-unlocked";
        if (ticks == 200) {
            mc.setScreen(null); server(p -> {
                QuestService.startQuest(p, "shinigami_substitute:2", (QuestNpcEntity) p.serverLevel().getEntity(next));
                var progress = PlayerCapability.get(p).orElseThrow(() -> new IllegalStateException("No data")).getPlayerQuestData().getProgress("shinigami_substitute:2");
                require(!progress.isEncounterStarted(), "Missing boss started");
                p.connection.teleport(16.5, -60, -5.5, 0, 0);
            });
        }
        if (ticks == 220) com.bleachmod.client.gui.StoryToastManager.clear();
        if (ticks == 235) capture = "04-pending-boss-hud";
        if (ticks == 245) server(p -> p.connection.teleport(16.5, -60, 8.5, 0, 0));
        if (ticks == 250) mc.setScreen(new NpcQuestScreen("rukia", nextId));
        if (ticks == 260) capture = "05-pending-boss-details";
        if (ticks == 270) server(p -> PlayerCapability.get(p).ifPresent(d -> d.getCharacter().setRace("hollow")));
        if (ticks == 295) capture = "06-other-race-no-story";
        if (ticks == 305) {
            mc.setScreen(null); server(p -> {
                PlayerCapability.get(p).ifPresent(d -> d.getCharacter().setRace("shinigami"));
                p.connection.teleport(5.5, -60, -1.5, 0, 0);
            });
        }
        if (ticks == 320) mc.setScreen(new NpcQuestScreen("rukia", wrongId));
        if (ticks == 335) capture = "07-wrong-context-no-story";
        if (ticks == 345) { mc.setScreen(null); mc.options.guiScale().set(3); mc.resizeDisplay(); }
        if (ticks == 360) capture = "08-compact-hud";
        if (ticks == 375) {
            mc.options.guiScale().set(2); mc.resizeDisplay(); mc.options.hideGui = true;
            server(p -> p.connection.teleport(24.5, -60, 8.5, 0, 0));
        }
        if (ticks == 390) capture = "09-new-npc-placeholder";
        if (ticks == 410) {
            mc.options.hideGui = false;
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
