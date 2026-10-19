package com.bleachmod.server.commands;

import com.bleachmod.common.appearance.AppearanceCatalog;
import com.bleachmod.common.appearance.AppearanceData;
import com.bleachmod.common.appearance.AppearanceService;
import com.bleachmod.common.data.PlayerData;
import com.bleachmod.common.network.SyncHelper;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;
import static net.minecraft.commands.Commands.*;

/** /bleachdev appearance: operator tool to inspect or change a character's look outside the NPC/item flow. */
public final class AppearanceCommands {
    private static final List<String> FIELDS = List.of("base", "outfit", "hair", "eyes", "mouth", "skin", "hairColor", "eyeColor", "slim", "vanilla");
    private AppearanceCommands() { }

    public static void attach(LiteralArgumentBuilder<CommandSourceStack> root) {
        root.then(literal("appearance")
                .then(literal("show").then(argument("player", EntityArgument.player()).executes(ctx -> show(ctx))))
                .then(literal("reset").then(argument("player", EntityArgument.player()).executes(ctx -> change(ctx, (data, race) -> {
                    data.getAppearance().applyDefaults(AppearanceCatalog.get(), race);
                    data.getAppearance().setUseVanillaSkin(false);
                }))))
                .then(literal("edit").then(argument("player", EntityArgument.player()).executes(ctx -> {
                    ServerPlayer player = target(ctx);
                    AppearanceService.openEditor(player);
                    ctx.getSource().sendSuccess(() -> Component.translatable("command.bleachmod.appearance.edit", player.getDisplayName()), true);
                    return 1;
                })))
                .then(literal("set").then(argument("player", EntityArgument.player())
                        .then(argument("field", StringArgumentType.word())
                                .suggests((ctx, b) -> SharedSuggestionProvider.suggest(FIELDS, b))
                                .then(argument("value", StringArgumentType.greedyString())
                                        .suggests((ctx, b) -> SharedSuggestionProvider.suggest(values(StringArgumentType.getString(ctx, "field")), b))
                                        .executes(ctx -> change(ctx, (data, race) -> set(data.getAppearance(), race,
                                                StringArgumentType.getString(ctx, "field"), StringArgumentType.getString(ctx, "value").trim())))))))));
    }

    private interface Change { void apply(PlayerData data, String race); }

    private static ServerPlayer target(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        ServerPlayer player = EntityArgument.getPlayer(ctx, "player");
        PlayerData data = SyncHelper.require(player);
        if (!data.getStatus().hasCreatedCharacter()) throw new SimpleCommandExceptionType(Component.literal("Confirm the character first")).create();
        return player;
    }

    private static int change(CommandContext<CommandSourceStack> ctx, Change change) throws CommandSyntaxException {
        ServerPlayer player = target(ctx);
        PlayerData data = SyncHelper.require(player);
        try { change.apply(data, data.getCharacter().getRace()); }
        catch (IllegalArgumentException e) { throw new SimpleCommandExceptionType(Component.literal(e.getMessage())).create(); }
        AppearanceService.apply(player, data, data.getAppearance().save());
        return show(ctx);
    }

    private static void set(AppearanceData look, String race, String field, String value) {
        AppearanceCatalog catalog = AppearanceCatalog.get();
        switch (field) {
            case "base", "outfit", "hair", "eyes", "mouth" -> {
                String id = "none".equals(value) ? "" : value;
                if (id.isEmpty() ? !AppearanceCatalog.OPTIONAL.contains(field) : !catalog.valid(id, field, race))
                    throw new IllegalArgumentException("Not a " + field + " for " + race + ": " + value);
                look.setPiece(field, id);
                look.setUseVanillaSkin(false);
            }
            case "skin" -> look.setSkinColor(AppearanceCatalog.color(value));
            case "hairColor" -> look.setHairColor("default".equals(value) ? AppearanceData.PIECE_DEFAULT : AppearanceCatalog.color(value));
            case "eyeColor" -> look.setEyeColor("default".equals(value) ? AppearanceData.PIECE_DEFAULT : AppearanceCatalog.color(value));
            case "slim" -> look.setSlim(bool(value));
            case "vanilla" -> look.setUseVanillaSkin(bool(value));
            default -> throw new IllegalArgumentException("Unknown field " + field + "; use " + String.join(", ", FIELDS));
        }
    }

    private static boolean bool(String value) {
        if (!"true".equals(value) && !"false".equals(value)) throw new IllegalArgumentException("Use true or false");
        return Boolean.parseBoolean(value);
    }

    private static List<String> values(String field) {
        List<String> out = new ArrayList<>();
        switch (field) {
            case "base", "outfit", "hair", "eyes", "mouth" -> {
                if (AppearanceCatalog.OPTIONAL.contains(field)) out.add("none");
                out.addAll(AppearanceCatalog.get().ids(field));
            }
            case "skin" -> AppearanceCatalog.get().palette("skinColors").forEach(c -> out.add(String.format("#%06X", c)));
            case "hairColor", "eyeColor" -> {
                out.add("default");
                AppearanceCatalog.get().palette("hairColor".equals(field) ? "hairColors" : "eyeColors").forEach(c -> out.add(String.format("#%06X", c)));
            }
            case "slim", "vanilla" -> out.addAll(List.of("true", "false"));
            default -> { }
        }
        return out;
    }

    private static int show(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        ServerPlayer player = target(ctx);
        AppearanceData look = SyncHelper.require(player).getAppearance();
        StringBuilder text = new StringBuilder("Bleach: " + player.getScoreboardName() + " appearance");
        text.append(look.usesVanillaSkin() ? " [Minecraft skin]" : "").append(" | model=").append(look.isSlim() ? "slim" : "classic");
        for (String category : AppearanceCatalog.CATEGORIES) text.append(" | ").append(category).append('=').append(look.piece(category).isEmpty() ? "none" : look.piece(category));
        text.append(String.format(" | skin=#%06X", look.skinColor()))
                .append(" | hairColor=").append(look.hairColor() < 0 ? "default" : String.format("#%06X", look.hairColor()))
                .append(" | eyeColor=").append(look.eyeColor() < 0 ? "default" : String.format("#%06X", look.eyeColor()));
        ctx.getSource().sendSuccess(() -> Component.literal(text.toString()), false);
        return 1;
    }
}
