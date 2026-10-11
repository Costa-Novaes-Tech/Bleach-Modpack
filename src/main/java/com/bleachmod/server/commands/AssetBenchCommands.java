package com.bleachmod.server.commands;

import com.bleachmod.common.visual.VanillaAssetBench;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import static net.minecraft.commands.Commands.*;
import com.mojang.brigadier.arguments.*;

public final class AssetBenchCommands {
    private AssetBenchCommands() { }
    public static void attach(LiteralArgumentBuilder<CommandSourceStack> root) {
        root.then(literal("assets").requires(s -> s.hasPermission(2))
                .then(literal("list").executes(ctx -> {
                    ctx.getSource().sendSuccess(() -> Component.translatable("command.bleachmod.assets.list", String.join(", ", VanillaAssetBench.ids())), false); return 1;
                }))
                .then(literal("preview").then(argument("effect", StringArgumentType.word())
                        .suggests((ctx, builder) -> SharedSuggestionProvider.suggest(VanillaAssetBench.ids(), builder))
                        .executes(ctx -> preview(ctx.getSource(), StringArgumentType.getString(ctx, "effect"), 1))
                        .then(argument("intensity", IntegerArgumentType.integer(1, 3))
                                .executes(ctx -> preview(ctx.getSource(), StringArgumentType.getString(ctx, "effect"), IntegerArgumentType.getInteger(ctx, "intensity"))))))
                .then(literal("stop").executes(ctx -> {
                    VanillaAssetBench.stop(ctx.getSource().getPlayerOrException());
                    ctx.getSource().sendSuccess(() -> Component.translatable("command.bleachmod.assets.stopped"), false); return 1;
                }))
                .then(literal("status").executes(ctx -> {
                    var player = ctx.getSource().getPlayerOrException();
                    ctx.getSource().sendSuccess(() -> Component.translatable("command.bleachmod.assets.status", VanillaAssetBench.active(player),
                            VanillaAssetBench.remaining(player), VanillaAssetBench.emitted(player)), false); return 1;
                })));
    }
    private static int preview(CommandSourceStack source, String effect, int intensity) throws com.mojang.brigadier.exceptions.CommandSyntaxException {
        var player = source.getPlayerOrException();
        final VanillaAssetBench.Preset preset;
        try { preset = VanillaAssetBench.resolve(effect); }
        catch (IllegalArgumentException e) { source.sendFailure(Component.translatable("command.bleachmod.assets.unknown")); return 0; }
        if (!VanillaAssetBench.start(player, preset, intensity)) { source.sendFailure(Component.translatable("command.bleachmod.assets.unavailable")); return 0; }
        source.sendSuccess(() -> Component.translatable("command.bleachmod.assets.started", effect, intensity), false); return 1;
    }
}
