package com.bleachmod.client.gui;

import com.bleachmod.client.appearance.AppearanceClient;
import com.bleachmod.common.appearance.AppearanceCatalog;
import com.bleachmod.common.appearance.AppearanceData;
import com.bleachmod.common.data.PlayerCapability;
import com.bleachmod.common.network.NetworkHandler;
import com.bleachmod.common.network.c2s.ConfirmCharacterC2S;
import com.bleachmod.common.network.c2s.UpdateAppearanceC2S;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

/**
 * Character look editor. Opened after the race choice (creating) or by a server-granted editing window.
 * The preview is local only: nothing reaches the server until Confirm, which the server validates.
 */
public class CharacterAppearanceScreen extends Screen {
    private static final int ROW = 22;
    private final String race;
    private final boolean creating;
    private final AppearanceData look;
    private final AppearanceCatalog catalog = AppearanceCatalog.get();
    private final List<Row> rows = new ArrayList<>();
    private boolean pending;
    /** Preview turntable: the model faces the viewer and turns only while the player drags it. */
    private float previewYaw;
    private boolean draggingPreview;

    private record Row(Component label, Supplier<Component> value, int top) { }

    public CharacterAppearanceScreen(String race, boolean creating) {
        super(Component.translatable("screen.bleachmod.appearance"));
        this.race = race;
        this.creating = creating;
        AppearanceData current = null;
        var mc = net.minecraft.client.Minecraft.getInstance();
        if (!creating && mc.player != null)
            current = PlayerCapability.get(mc.player).resolve().map(d -> d.getAppearance().copy()).orElse(null);
        look = current != null ? current : new AppearanceData();
        if (current == null) {
            look.applyDefaults(catalog, race);
            look.setUseVanillaSkin(false);
        } else if (current.piece("base").isEmpty()) {
            boolean vanilla = current.usesVanillaSkin();
            look.applyDefaults(catalog, race);
            look.setUseVanillaSkin(vanilla);
        }
    }

    @Override
    protected void init() {
        rows.clear();
        int left = 20, top = 40;
        addCycle(left, top, Component.translatable("screen.bleachmod.appearance.model"),
                () -> Component.translatable(look.isSlim() ? "screen.bleachmod.appearance.model.slim" : "screen.bleachmod.appearance.model.classic"),
                step -> look.setSlim(!look.isSlim()));
        top += ROW;
        for (String category : AppearanceCatalog.CATEGORIES) {
            List<String> options = options(category);
            if (options.size() < 2) continue;
            addCycle(left, top, Component.translatable("screen.bleachmod.appearance." + category),
                    () -> pieceName(look.piece(category)), step -> look.setPiece(category, cycle(options, look.piece(category), step)));
            top += ROW;
        }
        top += 6;
        addCycle(left, top, Component.translatable("screen.bleachmod.appearance.skin_color"),
                () -> swatch(look.skinColor()), step -> look.setSkinColor(cycleColor(colors("skinColors", false), look.skinColor(), step)));
        top += ROW;
        addCycle(left, top, Component.translatable("screen.bleachmod.appearance.hair_color"),
                () -> swatch(look.hairColor()), step -> look.setHairColor(cycleColor(colors("hairColors", true), look.hairColor(), step)));
        top += ROW;
        addCycle(left, top, Component.translatable("screen.bleachmod.appearance.eye_color"),
                () -> swatch(look.eyeColor()), step -> look.setEyeColor(cycleColor(colors("eyeColors", true), look.eyeColor(), step)));
        top += ROW + 6;

        addRenderableWidget(Button.builder(vanillaLabel(), b -> {
            look.setUseVanillaSkin(!look.usesVanillaSkin());
            b.setMessage(vanillaLabel());
        }).bounds(left, top, 200, 20).build());
        top += ROW;
        addRenderableWidget(Button.builder(Component.translatable("screen.bleachmod.appearance.random"), b -> randomize())
                .bounds(left, top, 98, 20).build());
        addRenderableWidget(Button.builder(Component.translatable("screen.bleachmod.appearance.reset"), b -> {
            boolean vanilla = look.usesVanillaSkin();
            look.applyDefaults(catalog, race);
            look.setUseVanillaSkin(vanilla);
        }).bounds(left + 102, top, 98, 20).build());

        int bottom = height - 28;
        if (creating) addRenderableWidget(Button.builder(Component.translatable("screen.bleachmod.appearance.back"),
                b -> minecraft.setScreen(new ConfirmRaceScreen())).bounds(width / 2 - 154, bottom, 150, 20).build());
        else addRenderableWidget(Button.builder(Component.translatable("gui.cancel"), b -> onClose())
                .bounds(width / 2 - 154, bottom, 150, 20).build());
        addRenderableWidget(Button.builder(Component.translatable("screen.bleachmod.appearance.confirm"), this::confirm)
                .bounds(width / 2 + 4, bottom, 150, 20).build());
    }

    private interface Step { void apply(int direction); }

    private void addCycle(int left, int top, Component label, Supplier<Component> value, Step step) {
        rows.add(new Row(label, value, top));
        addRenderableWidget(Button.builder(Component.literal("<"), b -> step.apply(-1)).bounds(left + 90, top, 20, 20).build());
        addRenderableWidget(Button.builder(Component.literal(">"), b -> step.apply(1)).bounds(left + 180, top, 20, 20).build());
    }

    private List<String> options(String category) {
        List<String> out = new ArrayList<>();
        if (AppearanceCatalog.OPTIONAL.contains(category)) out.add("");
        catalog.pieces(category, race).forEach(p -> out.add(p.id()));
        return out;
    }

    private static String cycle(List<String> options, String current, int step) {
        int i = Math.max(0, options.indexOf(current));
        return options.get(Math.floorMod(i + step, options.size()));
    }

    private List<Integer> colors(String palette, boolean withDefault) {
        List<Integer> out = new ArrayList<>();
        if (withDefault) out.add(AppearanceData.PIECE_DEFAULT);
        out.addAll(catalog.palette(palette));
        return out;
    }

    private static int cycleColor(List<Integer> options, int current, int step) {
        int i = options.indexOf(current);
        return options.get(Math.floorMod((i < 0 ? 0 : i) + step, options.size()));
    }

    private Component pieceName(String id) {
        if (id.isEmpty()) return Component.translatable("screen.bleachmod.appearance.none");
        String who = catalog.piece(id).map(AppearanceCatalog.Piece::inspiredBy).orElse(id);
        return Component.translatableWithFallback("appearance.bleachmod.inspired." + who, prettyName(who));
    }

    /** "kenpachi_zaraki" -> "Kenpachi Zaraki", for pieces without a translation yet. */
    private static String prettyName(String id) {
        StringBuilder out = new StringBuilder();
        for (String word : id.split("_")) {
            if (word.isEmpty()) continue;
            if (out.length() > 0) out.append(' ');
            out.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1));
        }
        return out.toString();
    }

    private static Component swatch(int rgb) {
        if (rgb < 0) return Component.translatable("screen.bleachmod.appearance.default_color");
        return Component.literal("██ ").withStyle(s -> s.withColor(rgb)).append(Component.literal(String.format("#%06X", rgb)));
    }

    private Component vanillaLabel() {
        return Component.translatable("screen.bleachmod.appearance.vanilla",
                Component.translatable(look.usesVanillaSkin() ? "options.on" : "options.off"));
    }

    private void randomize() {
        Random random = new Random();
        for (String category : AppearanceCatalog.CATEGORIES) {
            List<String> options = options(category);
            List<String> filled = options.stream().filter(s -> !s.isEmpty()).toList();
            if (!filled.isEmpty()) look.setPiece(category, filled.get(random.nextInt(filled.size())));
        }
        List<Integer> skin = catalog.palette("skinColors"), hair = catalog.palette("hairColors"), eyes = catalog.palette("eyeColors");
        if (!skin.isEmpty()) look.setSkinColor(skin.get(random.nextInt(skin.size())));
        if (!hair.isEmpty()) look.setHairColor(random.nextBoolean() ? AppearanceData.PIECE_DEFAULT : hair.get(random.nextInt(hair.size())));
        if (!eyes.isEmpty()) look.setEyeColor(random.nextBoolean() ? AppearanceData.PIECE_DEFAULT : eyes.get(random.nextInt(eyes.size())));
        look.setUseVanillaSkin(false);
        rebuildWidgets(); // refreshes the Minecraft-skin toggle label
    }

    private void confirm(Button button) {
        if (creating) {
            NetworkHandler.sendToServer(new ConfirmCharacterC2S(race, look.save()));
            pending = true;
            button.active = false;
        } else {
            NetworkHandler.sendToServer(new UpdateAppearanceC2S(look.save()));
            onClose();
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics);
        graphics.drawCenteredString(font, title, width / 2, 14, 0xFFE8C547);
        for (Row row : rows) {
            graphics.drawString(font, row.label(), 20, row.top() + 6, 0xFFF2E9C8);
            graphics.drawCenteredString(font, row.value().get(), 165, row.top() + 6, 0xFFFFFFFF);
        }
        if (minecraft.player != null) {
            AppearanceClient.preview(minecraft.player.getUUID(), look);
            EntityPreview.render(graphics, previewX(), previewY(), 70, previewYaw, minecraft.player);
            graphics.drawCenteredString(font, Component.translatable("screen.bleachmod.appearance.rotate_hint"), previewX(), previewY() + 14, 0xFF9A927C);
        }
        super.render(graphics, mouseX, mouseY, partialTick);
    }

    private int previewX() { return Math.max(300, width * 2 / 3); }

    private int previewY() { return height / 2 + 70; }

    private boolean overPreview(double mouseX, double mouseY) {
        return Math.abs(mouseX - previewX()) <= 60 && mouseY >= previewY() - 160 && mouseY <= previewY() + 20;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (super.mouseClicked(mouseX, mouseY, button)) return true;
        draggingPreview = button == 0 && overPreview(mouseX, mouseY);
        return draggingPreview;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (draggingPreview && button == 0) {
            previewYaw = (previewYaw - (float) dragX * 2.0F) % 360.0F;
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0) draggingPreview = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public void tick() {
        if (pending && minecraft.player != null)
            PlayerCapability.get(minecraft.player).ifPresent(data -> { if (data.getStatus().hasCreatedCharacter()) onClose(); });
    }

    @Override
    public void removed() {
        AppearanceClient.endPreview();
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return !creating;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
