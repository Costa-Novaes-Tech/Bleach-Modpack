package com.bleachmod.client.compat;

import com.bleachmod.Reference;
import com.bleachmod.client.ClientVisualConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

/** No Photon signatures here: the five-jar baseline never loads the optional backend. */
@Mod.EventBusSubscriber(modid=Reference.MOD_ID,value=Dist.CLIENT)
public final class PhotonPilotBridge {
    private static boolean failed;
    private PhotonPilotBridge(){ }
    public static boolean available(){return !failed&&ModList.get().isLoaded("photon")&&ModList.get().isLoaded("ldlib");}
    public static boolean incinerate(Vec3 at,float width,float height){
        if(!available()||!ClientVisualConfig.PHOTON_PILOT.get())return false;
        try{return PhotonPilotBackend.start(at,width,height);}catch(RuntimeException|LinkageError ex){failed=true;com.mojang.logging.LogUtils.getLogger().warn("Photon pilot disabled after VFX failure; native flames remain",ex);return false;}
    }
    @SubscribeEvent public static void tick(TickEvent.ClientTickEvent e){if(e.phase==TickEvent.Phase.END&&available())PhotonPilotBackend.tick();}
    @SubscribeEvent public static void logout(ClientPlayerNetworkEvent.LoggingOut e){if(available())PhotonPilotBackend.clear();failed=false;}
    @SubscribeEvent public static void commands(RegisterClientCommandsEvent e){e.getDispatcher().register(Commands.literal("bleachfx")
        .then(Commands.literal("photon")
            .then(Commands.literal("status").executes(c->{c.getSource().sendSuccess(()->Component.literal("Photon: "+available()+"; experimentalPhoton="+ClientVisualConfig.PHOTON_PILOT.get()),false);return 1;}))
            .then(Commands.literal("test").executes(c->{var mc=Minecraft.getInstance();boolean ok=false;if(mc.player!=null){ClientVisualConfig.PHOTON_PILOT.set(true);ok=incinerate(mc.player.position().add(mc.player.getLookAngle().multiply(1,0,1).normalize().scale(3)),.7F,1.8F);}boolean result=ok;c.getSource().sendSuccess(()->Component.translatable(result?"message.bleachmod.photon.test":"message.bleachmod.photon.unavailable"),false);return ok?1:0;}))
            .then(Commands.literal("stop").executes(c->{ClientVisualConfig.PHOTON_PILOT.set(false);if(available())PhotonPilotBackend.clear();return 1;}))
            .then(Commands.literal("export").executes(c->{if(!available()){c.getSource().sendFailure(Component.translatable("message.bleachmod.photon.unavailable"));return 0;}try{var path=PhotonPilotBackend.export();c.getSource().sendSuccess(()->Component.literal(path.toString()),false);return 1;}catch(java.io.IOException ex){c.getSource().sendFailure(Component.literal(ex.getMessage()));return 0;}}))));}
}
