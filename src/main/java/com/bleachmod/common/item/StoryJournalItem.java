package com.bleachmod.common.item;

import com.bleachmod.common.data.*;
import com.bleachmod.common.story.ShinigamiOriginService;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

/** Contains no copied quest NBT: each reader sees their own live, server-synced progression. */
public final class StoryJournalItem extends Item {
    public StoryJournalItem() { super(new Item.Properties().stacksTo(1)); }
    @Override public InteractionResultHolder<ItemStack> use(Level level,Player player,InteractionHand hand) {
        if(level.isClientSide)DistExecutor.unsafeRunWhenOn(Dist.CLIENT,()->()->
                com.bleachmod.client.gui.StoryBookScreen.open());
        else if(player instanceof net.minecraft.server.level.ServerPlayer server) PlayerCapability.get(player).ifPresent(d->{
            if(d.getOrigin().stage()==OriginData.Stage.RETRY)ShinigamiOriginService.start(server,false);
        });
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand),level.isClientSide);
    }
}
