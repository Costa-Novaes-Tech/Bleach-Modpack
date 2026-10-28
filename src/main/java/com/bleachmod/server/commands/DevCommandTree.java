package com.bleachmod.server.commands;

import com.bleachmod.common.data.*;
import com.bleachmod.common.evolution.FormRegistry;
import com.bleachmod.common.network.SyncHelper;
import com.mojang.brigadier.arguments.*;
import com.mojang.brigadier.builder.*;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.*;
import net.minecraft.commands.*;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import static net.minecraft.commands.Commands.*;

/** Small canonical command surface; legacy commands forward to the same services. */
public final class DevCommandTree {
    private DevCommandTree() { }
    @FunctionalInterface private interface Action { void apply(CommandContext<CommandSourceStack> ctx, ServerPlayer player, PlayerData data) throws CommandSyntaxException; }

    public static void attach(LiteralArgumentBuilder<CommandSourceStack> root) {
        root.executes(DevCommandTree::help);
        root.then(literal("help").executes(DevCommandTree::help)
                .then(literal("kits").executes(ctx -> helpPage(ctx,"kits")))
                .then(literal("cheats").executes(ctx -> helpPage(ctx,"cheats")))
                .then(literal("progress").executes(ctx -> helpPage(ctx,"progress")))
                .then(literal("legacy").executes(ctx -> helpPage(ctx,"legacy"))));
        root.then(targets(literal("status"), (ctx, p, d) -> { }));
        root.then(literal("kit").executes(DevCommandTree::help)
                .then(literal("shinigami").executes(DevCommandTree::help)
                    .then(identityNode()))
                .then(targets(literal("quincy"), (ctx, p, d) -> DevKitService.quincy(p, d))));
        root.then(literal("cheats").executes(DevCommandTree::help)
                .then(targets(literal("all"), (ctx, p, d) -> DevKitService.cheatsAll(p, d)))
                .then(targets(literal("off"), (ctx, p, d) -> DevKitService.cheatsOff(p, d)))
                .then(toggle("cooldowns", DevKitService::setCooldownCheat))
                .then(toggle("reiatsu", DevKitService::setReiatsuCheat)));
        root.then(literal("progress").executes(DevCommandTree::help)
                .then(targets(literal("max"), (ctx, p, d) -> DevKitService.maximize(d)))
                .then(literal("points").then(targets(argument("amount", IntegerArgumentType.integer(1, 1_000_000)),
                    (ctx, p, d) -> d.getResources().addTrainingPoints(IntegerArgumentType.getInteger(ctx, "amount")))))
                .then(literal("mastery").then(argument("form", StringArgumentType.word())
                    .suggests((ctx, b) -> SharedSuggestionProvider.suggest(java.util.List.of("sealed", "shikai", "bankai"), b))
                    .then(targets(argument("value", DoubleArgumentType.doubleArg(0, 100)), (ctx, p, d) -> {
                        if (!"shinigami".equals(d.getCharacter().getRace())) throw new IllegalArgumentException("Este ajuste de forma exige kit Shinigami; Quincy não possui Shikai/Bankai.");
                        String form = StringArgumentType.getString(ctx, "form");
                        var def = FormRegistry.getForm("shinigami", "zanpakuto", form);
                        if (def == null || DoubleArgumentType.getDouble(ctx, "value") > def.getMaxMastery()) throw new IllegalArgumentException("Forma ou maestria inválida para a configuração atual.");
                        d.getCharacter().setMastery("zanpakuto", form, DoubleArgumentType.getDouble(ctx, "value"));
                        for (int slot = 1; slot <= 4; slot++) if (com.bleachmod.common.technique.TechniqueProgression.available(d.getCharacter(), form, slot)) d.getCharacter().unlockTechnique(form, slot);
                    })))));
        root.then(literal("reset").executes(DevCommandTree::help)
                .then(targets(literal("combat"), (ctx, p, d) -> DevKitService.resetCombat(p, d, true))));
    }

    private static RequiredArgumentBuilder<CommandSourceStack, String> identityNode() {
        return targets(argument("identity", StringArgumentType.word())
                .suggests((ctx, b) -> SharedSuggestionProvider.suggest(DevKitService.IDENTITIES, b)),
                (ctx, p, d) -> DevKitService.shinigami(p, d, StringArgumentType.getString(ctx, "identity")));
    }

    @FunctionalInterface private interface Toggle { void apply(ServerPlayer p, PlayerData d, boolean on); }
    private static LiteralArgumentBuilder<CommandSourceStack> toggle(String name, Toggle action) {
        return literal(name).executes(DevCommandTree::help)
                .then(targets(literal("on"), (ctx, p, d) -> action.apply(p, d, true)))
                .then(targets(literal("off"), (ctx, p, d) -> action.apply(p, d, false)));
    }

    private static <T extends ArgumentBuilder<CommandSourceStack, T>> T targets(T builder, Action action) {
        builder.executes(ctx -> run(ctx, false, action));
        builder.then(argument("player", EntityArgument.player()).executes(ctx -> run(ctx, true, action)));
        return builder;
    }

    private static int run(CommandContext<CommandSourceStack> ctx, boolean explicit, Action action) throws CommandSyntaxException {
        // root requires permission 2; recheck for direct programmatic invocation.
        if (!ctx.getSource().hasPermission(2)) throw error("É necessário ser operador para usar /bleachdev.");
        var player = explicit ? EntityArgument.getPlayer(ctx, "player") : ctx.getSource().getPlayerOrException();
        var data = SyncHelper.require(player);
        try {
            boolean readOnly = ctx.getNodes().stream().anyMatch(n -> n.getNode().getName().equals("status"));
            if (!readOnly) DevKitService.requireCharacter(data);
            action.apply(ctx, player, data);
            if (!readOnly) DevKitService.finish(player, data);
        } catch (IllegalArgumentException ex) { throw error(ex.getMessage()); }
        boolean diagnostic = ctx.getNodes().stream().anyMatch(n -> n.getNode().getName().equals("status"));
        ctx.getSource().sendSuccess(() -> Component.literal(diagnostic ? status(player,data) : summary(data)), false);
        if (ctx.getNodes().stream().anyMatch(n -> n.getNode().getName().equals("all")))
            ctx.getSource().sendSuccess(() -> Component.literal("Preset all: maestria máxima do kit e mínimo de 10000 pontos. Stamina normal. Progressão fica salva; cheats off restaura custos. Detalhes: /bleachdev help cheats"), false);
        return 1;
    }

    private static CommandSyntaxException error(String text) { return new SimpleCommandExceptionType(Component.literal(text)).create(); }

    public static String status(ServerPlayer player, PlayerData data) {
        var c = data.getCharacter();
        boolean shinigami = "shinigami".equals(c.getRace());
        return player.getScoreboardName() + " | raça=" + c.getRace()
                + (shinigami ? " | kit=" + c.getZanpakutoIdentity() + " | forma=" + c.getActiveForm() + " -> " + c.getSelectedForm() : " | kit=Quincy (identidade zanpakutō arquivada=" + c.getZanpakutoIdentity() + ")")
                + " | Reiatsu=" + data.getResources().getCurrentReiatsu() + "/" + data.getResources().getMaxReiatsu()
                + " | cheats: cooldowns=" + (data.getStatus().areCooldownsDisabled() ? "on (sem espera)" : "off")
                + ", reiatsu=" + (data.getResources().areCostsDisabled() ? "on (sem custo)" : "off")
                + " | all=" + DevKitService.allEnabled(player) + " | pontos=" + data.getResources().getTrainingPoints()
                + (shinigami ? " | maestria selada/Shikai/Bankai=" + c.getMastery("zanpakuto", "sealed") + "/" + c.getMastery("zanpakuto", "shikai") + "/" + c.getMastery("zanpakuto", "bankai") : " | domínio Quincy="+data.getQuincy().getMastery()+"/100");
    }

    private static String summary(PlayerData data) {
        String kit="shinigami".equals(data.getCharacter().getRace())?data.getCharacter().getZanpakutoIdentity():data.getCharacter().getRace();
        return "Kit: "+kit+" | cheats CD: "+(data.getStatus().areCooldownsDisabled()?"on":"off")
                +" | Reiatsu: "+(data.getResources().areCostsDisabled()?"livre":"normal")+" | pontos: "+Math.round(data.getResources().getTrainingPoints());
    }

    public static int help(CommandContext<CommandSourceStack> ctx) { return helpPage(ctx,"main"); }
    private static int helpPage(CommandContext<CommandSourceStack> ctx,String page) {
        String text=switch(page) {
            case "kits" -> "Kits — alvo opcional ao final; padrão: você.\n"
                    +"/bleachdev kit shinigami <zanpakuto> [jogador]\n"
                    +"/bleachdev kit quincy [jogador]\n"
                    +"Troca segura, preservando itens e progresso.\n"
                    +"Shinigami: selada, próxima Bankai; Asauchi sem Bankai.\n"
                    +"Zanpakutō: "+String.join(", ",DevKitService.IDENTITIES);
            case "cheats" -> "Cheats — on ATIVA a ausência de custo/espera.\n"
                    +"/bleachdev cheats all|off [jogador]\n"
                    +"/bleachdev cheats cooldowns|reiatsu on|off [jogador]\n"
                    +"all: Reiatsu cheio, maestrias do kit máximas, mínimo 10000 pontos.\n"
                    +"Stamina normal. off não desfaz progresso concedido.\n"
                    +"Overrides terminam ao sair/morrer. Domínio e técnicas Quincy têm maestrias próprias; aquisição narrativa ainda é protótipo.";
            case "progress" -> "Progressão administrativa — alterações ficam salvas.\n"
                    +"/bleachdev progress max [jogador]\n"
                    +"/bleachdev progress points <quantidade> [jogador] — adicionar\n"
                    +"/bleachdev progress mastery <sealed|shikai|bankai> <valor> [jogador]\n"
                    +"max não liga cheats. Maestria segue limite configurado.";
            case "legacy" -> "Atalhos antigos temporariamente compatíveis.\n"
                    +"bankai/quincy/zanpakuto bind: prefira kit.\n"
                    +"reiatsu/cooldowns: prefira cheats; clear: reset combat.\n"
                    +"points/mastery/skill: ajustes de progressão antigos.\n"
                    +"inspect: status. asauchi give: kit shinigami asauchi.\n"
                    +"appearance e assets continuam ferramentas especializadas.";
            default -> "Bleach — laboratório. Alvo opcional ao final; padrão: você.\n"
                    +"/bleachdev kit shinigami <id> | kit quincy\n"
                    +"/bleachdev cheats all | cheats off\n"
                    +"/bleachdev status | reset combat\n"
                    +"/bleachdev progress max | progress points <quantidade>\n"
                    +"Detalhes: /bleachdev help kits|cheats|progress|legacy";
        };
        ctx.getSource().sendSuccess(() -> Component.literal(text),false);
        return 1;
    }
}
