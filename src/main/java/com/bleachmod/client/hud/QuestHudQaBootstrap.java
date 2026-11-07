package com.bleachmod.client.hud;

import com.bleachmod.Reference;
import com.bleachmod.common.data.PlayerCapability;
import com.bleachmod.common.network.SyncHelper;
import com.bleachmod.common.quest.QuestRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import net.minecraft.client.gui.screens.TitleScreen;
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

/** Opt-in real-client HUD exercise. Only creates new development saves. */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT)
public final class QuestHudQaBootstrap {
    private static boolean created;
    private static int ticks;
    private static String capture;
    private static File evidence;

    private QuestHudQaBootstrap() {}

    private static boolean enabled() {
        return !net.minecraftforge.fml.loading.FMLEnvironment.production
                && Boolean.getBoolean("bleachmod.questHudQa")
                && Minecraft.getInstance().gameDirectory.toPath().toAbsolutePath().normalize()
                .toString().replace('\\', '/').endsWith("/run/quest-hud-qa");
    }

    @SubscribeEvent
    public static void tick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !enabled()) return;
        var mc = Minecraft.getInstance();
        if (!created && mc.screen instanceof TitleScreen) {
            created = true;
            String name = "Quest-Hud-QA-" + System.currentTimeMillis();
            evidence = new File(mc.gameDirectory, "evidence/" + name);
            var rules = new GameRules();
            rules.getRule(GameRules.RULE_DOMOBSPAWNING).set(false, null);
            var settings = new LevelSettings(name, GameType.CREATIVE, false, net.minecraft.world.Difficulty.PEACEFUL,
                    true, rules, WorldDataConfiguration.DEFAULT);
            mc.createWorldOpenFlows().createFreshLevel(name, settings, new WorldOptions(102926, false, false),
                    registry -> registry.registryOrThrow(net.minecraft.core.registries.Registries.WORLD_PRESET)
                            .getHolderOrThrow(WorldPresets.FLAT).value().createWorldDimensions());
        }
        if (mc.player == null || !mc.hasSingleplayerServer()
                || !mc.getSingleplayerServer().getWorldData().getLevelName().startsWith("Quest-Hud-QA-")) return;
        ticks++;
        if (ticks == 20) {
            mc.options.pauseOnLostFocus = false;
            mc.options.guiScale().set(2);
            mc.resizeDisplay();
            mc.getTutorial().stop();
            mc.setScreen(null);
            mc.getSingleplayerServer().execute(() -> {
                var server = mc.getSingleplayerServer();
                var player = server.getPlayerList().getPlayer(mc.player.getUUID());
                PlayerCapability.get(player).ifPresent(data -> {
                    data.initializeShinigami();
                    var quests = data.getPlayerQuestData();
                    for (String key : java.util.List.of("shinigami_substitute:3", "shinigami_substitute:4", "shinigami_substitute:1", "shinigami_substitute:6")) {
                        var quest = QuestRegistry.getQuest(key);
                        if (quest == null) throw new IllegalStateException("Missing QA quest " + key);
                        quests.acceptQuest(key, quest);
                    }
                    quests.getProgress("shinigami_substitute:1").setObjectiveProgress(0, 2);
                    quests.setTrackedQuestId("shinigami_substitute:4");
                    if (!"shinigami_substitute:1".equals(quests.getTrackedQuestId())) throw new IllegalStateException("Story priority");
                    SyncHelper.full(player);
                });
            });
        }
        if (ticks > 20 && ticks < 50) mc.setScreen(null);
        if (ticks == 60) capture = "01-story-first-scale2";
        if (ticks == 75) { mc.options.guiScale().set(3); mc.resizeDisplay(); }
        if (ticks == 95) capture = "02-compact-scale3";
        if (ticks == 110) { mc.options.guiScale().set(1); mc.resizeDisplay(); }
        if (ticks == 130) capture = "03-large-scale1";
        if (ticks == 145) {
            mc.options.guiScale().set(2); mc.resizeDisplay();
            mc.getSingleplayerServer().execute(() -> {
                var player = mc.getSingleplayerServer().getPlayerList().getPlayer(mc.player.getUUID());
                PlayerCapability.get(player).ifPresent(data -> {
                    data.getPlayerQuestData().completeQuest("shinigami_substitute:1");
                    if (!"shinigami_substitute:4".equals(data.getPlayerQuestData().getTrackedQuestId()))
                        throw new IllegalStateException("Tracking fallback");
                    SyncHelper.full(player);
                });
            });
        }
        if (ticks == 170) capture = "04-fallback-next-story";
        if (ticks == 185) mc.options.hideGui = true;
        if (ticks == 195) capture = "05-hidden-hud";
        if (ticks == 205) mc.options.hideGui = false;
        if (ticks == 220) {
            com.mojang.logging.LogUtils.getLogger().info("Quest HUD QA complete: {}", evidence);
            mc.stop();
        }
    }

    @SubscribeEvent
    public static void render(TickEvent.RenderTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !enabled() || capture == null || evidence == null) return;
        var mc = Minecraft.getInstance();
        String name = capture + ".png";
        capture = null;
        try {
            Files.createDirectories(evidence.toPath());
            Screenshot.grab(evidence, name, mc.getMainRenderTarget(), message -> {});
            Files.writeString(evidence.toPath().resolve("frames.jsonl"),
                    "{\"file\":\"screenshots/" + name + "\",\"tick\":" + ticks
                            + ",\"guiWidth\":" + mc.getWindow().getGuiScaledWidth()
                            + ",\"guiHeight\":" + mc.getWindow().getGuiScaledHeight()
                            + ",\"source\":\"Minecraft Screenshot.grab framebuffer\"}\n",
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (Exception error) { throw new IllegalStateException("Quest HUD QA capture", error); }
    }
}
