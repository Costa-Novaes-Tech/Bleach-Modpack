package com.bleachmod.server.commands;
import com.bleachmod.Reference;
import com.bleachmod.common.ProgressionService;
import com.bleachmod.common.RegistryReload;
import com.bleachmod.common.data.*;
import com.bleachmod.common.network.SyncHelper;
import com.bleachmod.common.evolution.FormRegistry;
import com.bleachmod.init.ModItems;
import com.mojang.brigadier.arguments.*;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.*;
import net.minecraft.commands.*;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import static net.minecraft.commands.Commands.*;

@Mod.EventBusSubscriber(modid=Reference.MOD_ID)
public final class BleachCommands {
    @SubscribeEvent
    public static void register(RegisterCommandsEvent event) {
        event.getDispatcher().register(literal("bleachreload").requires(s->s.hasPermission(2))
            .then(literal("quests").executes(ctx->{
                try {
                    RegistryReload.reload(ctx.getSource().getServer());
                    for(ServerPlayer player:ctx.getSource().getServer().getPlayerList().getPlayers()) {
                        PlayerCapability.get(player).ifPresent(data->ProgressionService.sync(player,data));
                        SyncHelper.questRegistry(player);
                    }
                    ctx.getSource().sendSuccess(()->Component.translatable("command.bleachmod.reload.quests"),true);
                    return 1;
                } catch(Exception e) {
                    com.bleachmod.BleachMod.LOGGER.error("Bleach reload rejected",e);
                    ctx.getSource().sendFailure(Component.literal("Bleach: "+e.getMessage()));
                    return 0;
                }
            })));
        var root=literal("bleachdev").requires(s->s.hasPermission(2));
        DevCommandTree.attach(root);
        AssetBenchCommands.attach(root);
        AppearanceCommands.attach(root);
        root.then(literal("quincy").executes(DevCommandTree::help).then(argument("player", EntityArgument.player()).executes(ctx -> mutate(ctx, (p, d) -> {
            DevKitService.quincy(p,d);
        }))));
        root.then(literal("zanpakuto").executes(DevCommandTree::help).then(literal("bind").then(argument("player",EntityArgument.player())
            .then(argument("identity",StringArgumentType.word())
                .suggests((ctx,b)->SharedSuggestionProvider.suggest(IDENTITIES,b))
                .executes(ctx->mutate(ctx,(p,d)->bind(p,d,StringArgumentType.getString(ctx,"identity"))))))));
        // One-step Bankai for testing: binds the blade (optional), buys the skill, discovers the forms,
        // meets the mastery requirements of the world's form JSON, hands over the blade and preselects Bankai.
        root.then(literal("bankai").executes(DevCommandTree::help).then(argument("player",EntityArgument.player())
            .executes(ctx->mutate(ctx,(p,d)->grantBankai(p,d,null)))
            .then(argument("identity",StringArgumentType.word())
                .suggests((ctx,b)->SharedSuggestionProvider.suggest(IDENTITIES.subList(1,IDENTITIES.size()),b))
                .executes(ctx->mutate(ctx,(p,d)->grantBankai(p,d,StringArgumentType.getString(ctx,"identity")))))));
        root.then(literal("points").executes(DevCommandTree::help).then(literal("add").then(argument("player",EntityArgument.player())
            .then(argument("amount",IntegerArgumentType.integer(1,1000000)).executes(ctx->mutate(ctx,(p,d)->d.getResources().addTrainingPoints(IntegerArgumentType.getInteger(ctx,"amount"))))))));
        root.then(literal("skill").executes(DevCommandTree::help).then(literal("set").then(argument("player",EntityArgument.player())
            .then(literal("zanpakuto").then(argument("level",IntegerArgumentType.integer(0,2)).executes(ctx->mutate(ctx,(p,d)->{
                int level=IntegerArgumentType.getInteger(ctx,"level");
                d.getSkills().get("zanpakuto").setLevel(level);
                if(level>=1)d.getCharacter().unlockForm("shikai");
                if(level>=2)d.getCharacter().unlockForm("bankai");
            })))))));
        root.then(literal("mastery").executes(DevCommandTree::help).then(literal("set").then(argument("player",EntityArgument.player())
            .then(literal("zanpakuto").then(argument("form",StringArgumentType.word())
                .suggests((ctx,b)->SharedSuggestionProvider.suggest(java.util.List.of("sealed","shikai","bankai"),b))
                .then(argument("value",DoubleArgumentType.doubleArg(0,100)).executes(ctx->mutate(ctx,(p,d)->{
                    String form=StringArgumentType.getString(ctx,"form");
                    var definition=FormRegistry.getForm("shinigami","zanpakuto",form);
                    if(definition==null) throw new IllegalArgumentException("Unknown form");
                    double value=DoubleArgumentType.getDouble(ctx,"value");
                    if(value>definition.getMaxMastery()) throw new IllegalArgumentException("Mastery exceeds configured maximum");
                    d.getCharacter().setMastery("zanpakuto",form,value);
                    com.bleachmod.common.technique.TechniqueProgression.unlockEligible(d.getCharacter());
                }))))))));
        root.then(literal("reiatsu").executes(DevCommandTree::help).then(literal("fill").then(argument("player",EntityArgument.player())
            .executes(ctx->mutate(ctx,(p,d)->d.getResources().setCurrentReiatsu(d.getResources().getMaxReiatsu()))))));
        root.then(literal("reiatsu").executes(DevCommandTree::help).then(literal("free").then(argument("player",EntityArgument.player())
            .executes(ctx->mutateTest(ctx,(p,d)->DevKitService.setReiatsuCheat(p,d,true))))));
        root.then(literal("reiatsu").executes(DevCommandTree::help).then(literal("restore").then(argument("player",EntityArgument.player())
            .executes(ctx->mutateTest(ctx,(p,d)->DevKitService.setReiatsuCheat(p,d,false))))));
        root.then(literal("cooldowns").executes(DevCommandTree::help).then(literal("disable").then(argument("player",EntityArgument.player())
            .executes(ctx->mutateTest(ctx,(p,d)->{
                resetTechniqueEffects(p,d);
                DevKitService.setCooldownCheat(p,d,true);
            })))));
        root.then(literal("cooldowns").executes(DevCommandTree::help).then(literal("restore").then(argument("player",EntityArgument.player())
            .executes(ctx->mutateTest(ctx,(p,d)->DevKitService.setCooldownCheat(p,d,false))))));
        root.then(literal("cooldowns").executes(DevCommandTree::help).then(literal("clear").then(argument("player",EntityArgument.player())
            .executes(ctx->mutateTest(ctx,BleachCommands::resetTechniqueEffects)))));

        root.then(literal("asauchi").executes(DevCommandTree::help).then(literal("give").then(argument("player",EntityArgument.player())
            .executes(ctx->mutate(ctx,(p,d)->{
                ItemStack stack=new ItemStack(ModItems.ASAUCHI.get());
                if(!p.getInventory().add(stack)) p.drop(stack,false);
            })))));
        root.then(literal("inspect").executes(DevCommandTree::help).then(argument("player",EntityArgument.player()).executes(ctx->{
            ServerPlayer p=EntityArgument.getPlayer(ctx,"player");
            ctx.getSource().sendSuccess(()->Component.literal("Atalho antigo: use /bleachdev status [jogador].\n"+DevCommandTree.status(p,SyncHelper.require(p))),false);
            return 1;
        })));
        event.getDispatcher().register(root);
    }
    private static final java.util.List<String> IDENTITIES=DevKitService.IDENTITIES;
    private interface Mutation { void apply(ServerPlayer player,PlayerData data); }
    private static void bind(ServerPlayer p,PlayerData d,String identity) {
        DevKitService.bindLegacy(p,d,identity);
    }
    private static void grantBankai(ServerPlayer p,PlayerData d,String identity) {
        String target=identity==null?d.getCharacter().getZanpakutoIdentity():identity;
        if(CharacterData.ASAUCHI.equals(target))throw new IllegalArgumentException("Asauchi não possui Bankai; use /bleachdev kit shinigami <zanpakuto>.");
        DevKitService.shinigami(p,d,target);
    }
    private static void resetTechniqueEffects(ServerPlayer player,PlayerData data) {
        DevKitService.resetCombat(player,data,true);
    }
    private static int mutate(CommandContext<CommandSourceStack> ctx,Mutation action) throws CommandSyntaxException {
        return performLegacy(ctx,action);
    }
    private static int mutateTest(CommandContext<CommandSourceStack> ctx,Mutation action) throws CommandSyntaxException {
        return performLegacy(ctx,action);
    }
    private static int performLegacy(CommandContext<CommandSourceStack> ctx,Mutation action) throws CommandSyntaxException {
        ServerPlayer player=EntityArgument.getPlayer(ctx,"player");
        PlayerData data=SyncHelper.require(player);
        if(!data.getStatus().hasCreatedCharacter()) throw new SimpleCommandExceptionType(Component.literal("Confirme a criação do personagem primeiro.")).create();
        try { action.apply(player,data); }
        catch(IllegalArgumentException e) { throw new SimpleCommandExceptionType(Component.literal(e.getMessage())).create(); }
        DevKitService.finish(player,data);
        ctx.getSource().sendSuccess(()->Component.literal("Atalho antigo: consulte /bleachdev help para kit, cheats e progress."),false);
        ctx.getSource().sendSuccess(()->Component.literal("Bleach: "+player.getScoreboardName()+" updated"
                +" | cooldowns="+(data.getStatus().areCooldownsDisabled()?"disabled":"normal")
                +" | reiatsuCosts="+(data.getResources().areCostsDisabled()?"free":"normal")),true);
        return 1;
    }
}
