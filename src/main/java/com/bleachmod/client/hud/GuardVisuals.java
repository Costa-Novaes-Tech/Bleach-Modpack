package com.bleachmod.client.hud;

import com.bleachmod.Reference;
import com.bleachmod.common.GuardPolicy;
import com.bleachmod.common.data.PlayerCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/** Code-drawn shield shared by the first-person HUD and the observer billboard. */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT)
public final class GuardVisuals {
    private GuardVisuals() { }
    public static void hud(GuiGraphics g, int width, int height, com.bleachmod.common.data.PlayerData data) {
        var guard = data.getGuard();
        if (!guard.shownActive() && !guard.shownBroken() && guard.shownPosture() >= GuardPolicy.MAX) return;
        var mc = Minecraft.getInstance();
        int x = width / 2, y = height / 2 + 22;
        com.bleachmod.common.visual.GuardShield.draw(guard.shownPosture() / GuardPolicy.MAX, guard.shownBroken(),
                (px, py, w, h, color) -> g.fill(x + px, y + py, x + px + w, y + py + h, color));
        var text = guard.shownBroken() ? Component.translatable("hud.bleachmod.guard_broken")
                : Component.translatable("hud.bleachmod.posture", (int) Math.ceil(guard.shownPosture()));
        g.drawCenteredString(mc.font, text, x, y + 36, guard.shownBroken() ? 0xFFFF6655 : 0xFFB8E8FF);
    }
    @SubscribeEvent public static void abovePlayer(RenderPlayerEvent.Post event) {
        var p = event.getEntity(); var mc = Minecraft.getInstance();
        if (p.isInvisible() || p.isSpectator() || !p.isAlive() || mc.player == null
                || mc.player.distanceToSqr(p) > 32 * 32) return;
        PlayerCapability.get(p).ifPresent(data -> {
            var guard = data.getGuard();
            if (!guard.shownActive() && !guard.shownBroken()) return;
            var pose = event.getPoseStack(); pose.pushPose();
            pose.translate(0, p.getBbHeight() + 1.4, 0);
            pose.mulPose(mc.getEntityRenderDispatcher().cameraOrientation());
            pose.scale(.021F, -.021F, .021F);
            var matrix = pose.last().pose();
            var buffer = event.getMultiBufferSource().getBuffer(RenderType.debugQuads());
            com.bleachmod.common.visual.GuardShield.draw(guard.shownPosture() / GuardPolicy.MAX, guard.shownBroken(), (x, y, w, h, color) -> {
                int a = color >>> 24, r = (color >> 16) & 255, green = (color >> 8) & 255, b = color & 255;
                buffer.vertex(matrix, x, y, 0).color(r, green, b, a).endVertex();
                buffer.vertex(matrix, x, y + h, 0).color(r, green, b, a).endVertex();
                buffer.vertex(matrix, x + w, y + h, 0).color(r, green, b, a).endVertex();
                buffer.vertex(matrix, x + w, y, 0).color(r, green, b, a).endVertex();
            });
            pose.popPose();
        });
    }
}
