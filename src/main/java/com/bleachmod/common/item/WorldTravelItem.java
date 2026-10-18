package com.bleachmod.common.item;

import com.bleachmod.server.world.HistoryWorld;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/** Temporary development item: teleports to the spawn of one History Mode map. Destination is resolved on the server. */
public class WorldTravelItem extends Item {
    private static final int COOLDOWN_TICKS = 20;

    private final String mapId;

    public WorldTravelItem(String mapId) {
        super(new Item.Properties().stacksTo(1));
        this.mapId = mapId;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (level.isClientSide() || !(player instanceof ServerPlayer serverPlayer)) {
            return InteractionResultHolder.success(stack);
        }
        if (serverPlayer.isPassenger() || serverPlayer.isVehicle() || serverPlayer.isSleeping()) {
            serverPlayer.displayClientMessage(Component.translatable("message.bleachmod.travel.invalid_state"), true);
            return InteractionResultHolder.fail(stack);
        }
        HistoryWorld.Destination destination = HistoryWorld.destination(mapId).orElse(null);
        ServerLevel target = destination == null ? null : serverPlayer.server.getLevel(destination.dimension());
        if (target == null) {
            serverPlayer.displayClientMessage(Component.translatable("message.bleachmod.travel.history_only"), true);
            return InteractionResultHolder.fail(stack);
        }
        serverPlayer.resetFallDistance();
        serverPlayer.teleportTo(target, destination.x(), destination.y(), destination.z(), destination.yaw(), 0.0F);
        serverPlayer.getCooldowns().addCooldown(this, COOLDOWN_TICKS);
        return InteractionResultHolder.consume(stack);
    }
}
