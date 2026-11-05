package com.bleachmod.server.commands;

import com.bleachmod.Reference;
import com.bleachmod.common.quest.*;
import com.bleachmod.entity.QuestNpcEntity;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import static net.minecraft.commands.Commands.*;

/** Map-author tool. Structure entity NBT may set the same QuestContext directly. */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public final class QuestNpcContextCommands {
    private QuestNpcContextCommands() {}
    @SubscribeEvent public static void register(RegisterCommandsEvent event) {
        event.getDispatcher().register(literal("bleachnpc").requires(source -> source.hasPermission(2))
                .then(literal("context").then(argument("npc", EntityArgument.entity())
                        .then(argument("context", StringArgumentType.word())
                                .suggests((ctx, builder) -> SharedSuggestionProvider.suggest(
                                        QuestRegistry.allQuests().stream().filter(Quest::isStory)
                                                .flatMap(q -> java.util.stream.Stream.of(q.getStory().offerContext(), q.getStory().targetContext()))
                                                .distinct(), builder))
                                .executes(ctx -> {
                                    var entity = EntityArgument.getEntity(ctx, "npc");
                                    String value = StringArgumentType.getString(ctx, "context");
                                    if (!(entity instanceof QuestNpcEntity npc) || !isValid(npc, value)) {
                                        ctx.getSource().sendFailure(Component.translatable("command.bleachmod.npc.invalid")); return 0;
                                    }
                                    npc.setQuestContext(value);
                                    ctx.getSource().sendSuccess(() -> Component.translatable("command.bleachmod.npc.context", value), true);
                                    return 1;
                                })))));
    }
    public static boolean isValid(QuestNpcEntity npc, String context) {
        return StoryQuestInfo.validContext(context) && QuestRegistry.allQuests().stream().filter(Quest::isStory)
                .anyMatch(q -> q.getStory().dimension().equals(npc.level().dimension().location())
                        && ((q.getQuestGiver().equals(npc.getNpcId()) && q.getStory().offerContext().equals(context))
                        || (q.getStory().targetNpc().equals(npc.getNpcId()) && q.getStory().targetContext().equals(context))));
    }
}
