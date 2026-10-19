package com.bleachmod.server.commands;

import com.bleachmod.Reference;
import com.bleachmod.common.data.PlayerCapability;
import com.bleachmod.common.network.SyncHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/** Prepares only newly created, named disposable QA worlds from the opt-in development launch. */
@Mod.EventBusSubscriber(modid=Reference.MOD_ID)
public final class BenihimeQaWorld {
    private BenihimeQaWorld(){ }
    @SubscribeEvent public static void joined(PlayerEvent.PlayerLoggedInEvent e){
        if(net.minecraftforge.fml.loading.FMLEnvironment.production||!Boolean.getBoolean("bleachmod.benihimeQa")||!(e.getEntity() instanceof ServerPlayer p))return;
        if(!p.getServer().getWorldData().getLevelName().startsWith("Benihime-QA-68-"))return;
        PlayerCapability.get(p).ifPresent(d->{
            d.initializeShinigami();d.getCharacter().bindZanpakuto("benihime");d.getSkills().get("zanpakuto").setLevel(2);
            for(String form:java.util.List.of("sealed","shikai","bankai")){d.getCharacter().unlockForm(form);d.getCharacter().setMastery("zanpakuto",form,100);}
            d.getCharacter().setSelectedForm("zanpakuto","bankai");
            p.setItemSlot(net.minecraft.world.entity.EquipmentSlot.MAINHAND,new net.minecraft.world.item.ItemStack(com.bleachmod.init.ModItems.BENIHIME.get()));
            int height=p.serverLevel().getHeight(net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING,0,0);
            p.teleportTo(p.serverLevel(),.5,height+.05,.5,0,0);p.setGameMode(net.minecraft.world.level.GameType.CREATIVE);
            SyncHelper.full(p);SyncHelper.appearance(p);
        });
    }
}
