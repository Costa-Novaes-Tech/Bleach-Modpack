package com.bleachmod.client.gui;

import com.bleachmod.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
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
import java.util.List;

/** Opt-in GUI exercise in a fresh development world, with real framebuffer captures. */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT)
public final class ClassSelectionQaBootstrap {
    private static boolean created;
    private static int ticks;
    private static String capture;
    private static File evidence;
    private static QaScreen screen;

    private ClassSelectionQaBootstrap() { }

    private static boolean enabled() {
        return !net.minecraftforge.fml.loading.FMLEnvironment.production
                && Boolean.getBoolean("bleachmod.classSelectionQa")
                && Minecraft.getInstance().gameDirectory.toPath().toAbsolutePath().normalize()
                .toString().replace('\\', '/').endsWith("/run/class-selection-qa");
    }

    @SubscribeEvent
    public static void tick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !enabled()) return;
        var mc = Minecraft.getInstance();
        if (!created && mc.screen instanceof TitleScreen) {
            created = true;
            String name = "Class-Selection-QA-" + System.currentTimeMillis();
            evidence = new File(mc.gameDirectory, "evidence/" + name);
            var rules = new GameRules();
            rules.getRule(GameRules.RULE_DOMOBSPAWNING).set(false, null);
            var settings = new LevelSettings(name, GameType.CREATIVE, false, net.minecraft.world.Difficulty.PEACEFUL,
                    true, rules, WorldDataConfiguration.DEFAULT);
            mc.createWorldOpenFlows().createFreshLevel(name, settings, new WorldOptions(102826, false, false),
                    registry -> registry.registryOrThrow(net.minecraft.core.registries.Registries.WORLD_PRESET)
                            .getHolderOrThrow(WorldPresets.FLAT).value().createWorldDimensions());
        }
        if (mc.player == null || !mc.hasSingleplayerServer()
                || !mc.getSingleplayerServer().getWorldData().getLevelName().startsWith("Class-Selection-QA-")) return;
        if (ticks == 0 && !(mc.screen instanceof ConfirmRaceScreen)) return;
        ticks++;
        mc.getToasts().clear();
        if (ticks == 10) {
            mc.options.pauseOnLostFocus = false;
            mc.getTutorial().stop();
            mc.getToasts().clear();
            mc.options.guiScale().set(2);
            mc.resizeDisplay();
            screen = new QaScreen();
            mc.setScreen(screen);
        }
        if (ticks == 35) capture = "01-idle-scale2";
        if (ticks == 45) screen.pointer = 0;
        if (ticks == 65) capture = "02-shinigami-hover";
        if (ticks == 75) screen.pointer = 1;
        if (ticks == 95) {
            var widgets = cards();
            require(widgets.size() == 4 && widgets.get(0).active, "four cards; Shinigami enabled");
            for (int i = 1; i < widgets.size(); i++) {
                var card = widgets.get(i);
                require(!card.active, "locked class " + i);
                screen.mouseClicked(card.getX() + card.getWidth() / 2., card.getY() + 10, 0);
                require(mc.screen == screen, "locked click does not navigate");
            }
            capture = "03-locked-hover";
        }
        if (ticks == 105) {
            var card = cards().get(0);
            screen.mouseClicked(card.getX() + 10, card.getY() + 10, 0);
            require(mc.screen instanceof CharacterAppearanceScreen, "Shinigami opens appearance");
        }
        if (ticks == 120) capture = "04-appearance";
        if (ticks == 125) {
            var appearance = mc.screen;
            var back = (Button) appearance.children().get(appearance.children().size() - 2);
            back.onPress();
            require(mc.screen instanceof ConfirmRaceScreen, "back returns to origins");
            require(!com.bleachmod.common.data.PlayerCapability.get(mc.player)
                    .map(d -> d.getStatus().hasCreatedCharacter()).orElse(true), "preview does not create character");
            screen = new QaScreen();
            mc.setScreen(screen);
        }
        if (ticks == 145) { mc.options.guiScale().set(3); mc.resizeDisplay(); }
        if (ticks == 135) {
            screen.keyPressed(org.lwjgl.glfw.GLFW.GLFW_KEY_TAB, 0, 0);
            require(cards().get(0).isFocused(), "keyboard focuses available class");
        }
        if (ticks == 140) capture = "08-keyboard-focus";
        if (ticks == 165) capture = "05-compact-scale3";
        if (ticks == 175) { mc.options.guiScale().set(1); mc.resizeDisplay(); }
        if (ticks == 195) capture = "06-large-scale1";
        if (ticks == 205) { mc.options.guiScale().set(2); mc.resizeDisplay(); }
        if (ticks == 225) capture = "07-final";
        if (ticks == 235) {
            var server = mc.getSingleplayerServer();
            var uuid = mc.player.getUUID();
            server.execute(() -> {
                var player = server.getPlayerList().getPlayer(uuid);
                com.bleachmod.common.data.PlayerCapability.get(player).ifPresent(data -> {
                    data.initializeShinigami();
                    com.bleachmod.common.network.SyncHelper.full(player);
                });
            });
        }
        if (ticks == 255) {
            require(com.bleachmod.common.data.PlayerCapability.get(mc.player).map(d ->
                    d.getAcademy().stage() == com.bleachmod.common.data.AcademyData.Stage.AWAITING_GUIDANCE).orElse(false),
                    "formation synced from server");
            mc.setScreen(new JournalScreen());
        }
        if (ticks == 265) {
            var button = mc.screen.children().stream().filter(c -> c instanceof Button).map(c -> (Button)c)
                    .filter(b -> b.getMessage().getString().equals(net.minecraft.network.chat.Component.translatable("item.bleachmod.story_journal").getString()))
                    .findFirst().orElseThrow();
            button.onPress();
            require(mc.screen instanceof StoryBookScreen, "journal opens live story book");
        }
        if (ticks == 280) capture = "09-academy-record";
        if (ticks == 285) { mc.options.guiScale().set(3); mc.resizeDisplay(); }
        if (ticks == 300) capture = "10-academy-compact";
        if (ticks == 305) {
            mc.screen.onClose();
            require(mc.screen instanceof JournalScreen, "formation returns to journal");
        }
        if (ticks == 320) {
            com.mojang.logging.LogUtils.getLogger().info("Class selection QA complete: {}", evidence);
            mc.stop();
        }
    }

    private static List<AbstractWidget> cards() {
        return screen.children().stream().filter(c -> c instanceof AbstractWidget)
                .map(c -> (AbstractWidget) c).toList();
    }

    private static void require(boolean condition, String label) {
        if (!condition) throw new IllegalStateException("Class selection QA failed: " + label);
        com.mojang.logging.LogUtils.getLogger().info("Class selection QA PASS: {}", label);
    }

    private static final class QaScreen extends ConfirmRaceScreen {
        private int pointer = -1;
        @Override public void render(GuiGraphics graphics, int x, int y, float partialTick) {
            if (pointer >= 0) {
                var card = cards().get(pointer);
                x = card.getX() + card.getWidth() / 2;
                y = card.getY() + card.getHeight() / 3;
            } else { x = -100; y = -100; }
            super.render(graphics, x, y, partialTick);
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
            Screenshot.grab(evidence, name, mc.getMainRenderTarget(), message -> { });
            Files.writeString(evidence.toPath().resolve("frames.jsonl"),
                    "{\"file\":\"screenshots/" + name + "\",\"tick\":" + ticks
                            + ",\"guiWidth\":" + mc.getWindow().getGuiScaledWidth()
                            + ",\"guiHeight\":" + mc.getWindow().getGuiScaledHeight()
                            + ",\"source\":\"Minecraft Screenshot.grab framebuffer\"}\n",
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (Exception error) { throw new IllegalStateException("Class selection QA capture", error); }
    }
}
