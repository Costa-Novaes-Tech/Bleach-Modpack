package com.bleachmod.client.cinematic;

import com.bleachmod.Reference;
import com.bleachmod.common.visual.HollowBossEntrance;
import com.bleachmod.entity.HollowBossEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/** Viewer-local accents. No camera-mode/input changes or persistent state to restore. */
@Mod.EventBusSubscriber(modid=Reference.MOD_ID,value=Dist.CLIENT)
public final class HollowBossEntranceEffects {
    private HollowBossEntranceEffects() { }
    private static HollowBossEntity visibleBoss() {
        var mc=Minecraft.getInstance();
        if (mc.player==null || mc.level==null || CinematicDirector.localActive()) return null;
        HollowBossEntity nearest=null;
        double distance=28*28;
        Vec3 eye=mc.gameRenderer.getMainCamera().getPosition();
        Vec3 look=Vec3.directionFromRotation(mc.gameRenderer.getMainCamera().getXRot(),mc.gameRenderer.getMainCamera().getYRot());
        for (var entity:mc.level.entitiesForRendering()) if (entity instanceof HollowBossEntity boss && boss.isEntering()) {
            Vec3 delta=boss.position().add(0,3,0).subtract(eye);
            double d=delta.lengthSqr();
            if (d<distance && delta.normalize().dot(look)>.4 && mc.player.hasLineOfSight(boss)) {nearest=boss;distance=d;}
        }
        return nearest;
    }
    @SubscribeEvent public static void shake(ViewportEvent.ComputeCameraAngles event) {
        var boss=visibleBoss();if(boss==null)return;
        float t=boss.getEntranceAge((float)event.getPartialTick());
        float impact=Math.max(0,1-Math.abs(t-HollowBossEntrance.LAND)/8);
        float roar=Math.max(0,1-Math.abs(t-HollowBossEntrance.ROAR)/12)*.4F;
        float attenuation=1-Mth.clamp((float)(boss.distanceTo(Minecraft.getInstance().player)/28),0,1);
        float amplitude=(impact+roar)*attenuation;
        event.setPitch(event.getPitch()+(float)Math.sin(t*2.1)*amplitude*.7F);
        event.setRoll(event.getRoll()+(float)Math.sin(t*1.7)*amplitude*.45F);
    }
    @SubscribeEvent public static void hideHud(RenderGuiOverlayEvent.Pre event) {
        if (visibleBoss()!=null) event.setCanceled(true);
    }
    @SubscribeEvent public static void overlay(RenderGuiEvent.Post event) {
        var mc=Minecraft.getInstance();if(mc.options.hideGui)return;
        var boss=visibleBoss();if(boss==null)return;
        float t=boss.getEntranceAge(event.getPartialTick());
        var g=event.getGuiGraphics();int w=g.guiWidth(),h=g.guiHeight();
        float fade=HollowBossEntrance.ease(t,4,20)*(1-HollowBossEntrance.ease(t,144,160));
        int bar=(int)(h*.065F*fade);
        g.fill(0,0,w,bar,0xFF050407);g.fill(0,h-bar,w,h,0xFF050407);
        float title=HollowBossEntrance.ease(t,123,132)*(1-HollowBossEntrance.ease(t,146,158));
        if(title>.05F) {
            g.pose().pushPose();g.pose().translate(w/2F,h*.78F,0);g.pose().scale(1.6F,1.6F,1.6F);
            g.drawCenteredString(mc.font,boss.getDisplayName(),0,0,((int)(title*255)<<24)|0xE3DBD8);
            g.pose().popPose();
        }
    }
}
