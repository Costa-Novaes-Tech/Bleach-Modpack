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
    @FunctionalInterface private interface Pixel { void draw(int x, int y, int color); }
    private static void shield(float posture, boolean broken, Pixel pixels) {
        float ratio = Math.max(0, Math.min(1, posture / GuardPolicy.MAX));
        int color = broken ? 0xFFFF6655 : ratio <= .25F ? 0xFFFFAA55 : 0xFF8BDAFF;
        for (int y = 0; y < 18; y++) {
            int half = y < 10 ? 7 : Math.max(0, 7 - (y - 9));
            for (int x = -half; x <= half; x++) {
                boolean edge = Math.abs(x) == half || y == 0 || y == 17;
                int fill = edge ? 0xFFF1FAFF : y >= (int) (18 * (1 - ratio)) ? color : 0xCC263746;
                boolean crack = (ratio < .75F && x == (y / 3) % 3 - 1 && y > 2 && y < 15)
                        || (ratio < .4F && y > 6 && y < 12 && x == 10 - y);
                if (broken && Math.abs(x) < 2) continue;
                pixels.draw(x + (broken ? (x < 0 ? -2 : 2) : 0), y, crack ? 0xFF14202B : fill);
            }
        }
        for (int x = -8; x <= 8; x++) for (int y = 20; y <= 22; y++)
            pixels.draw(x, y, x + 8 < Math.round(17 * ratio) ? color : 0xCC263746);
    }
    public static void hud(GuiGraphics g, int width, int height, com.bleachmod.common.data.PlayerData data) {
        var guard = data.getGuard();
        if (!guard.shownActive() && !guard.shownBroken() && guard.shownPosture() >= GuardPolicy.MAX) return;
        var mc = Minecraft.getInstance();
        int x = width / 2, y = height / 2 + 29;
        shield(guard.shownPosture(), guard.shownBroken(), (px, py, color) -> g.fill(x + px, y + py, x + px + 1, y + py + 1, color));
        var text = guard.shownBroken() ? Component.translatable("hud.bleachmod.guard_broken")
                : Component.translatable("hud.bleachmod.posture", (int) Math.ceil(guard.shownPosture()));
        g.drawCenteredString(mc.font, text, x, y + 26, guard.shownBroken() ? 0xFFFF6655 : 0xFFB8E8FF);
    }
    @SubscribeEvent public static void abovePlayer(RenderPlayerEvent.Post event) {
        var p = event.getEntity(); var mc = Minecraft.getInstance();
        if (p.isInvisible() || p.isSpectator() || !p.isAlive() || mc.player == null
                || mc.player.distanceToSqr(p) > 32 * 32) return;
        PlayerCapability.get(p).ifPresent(data -> {
            var guard = data.getGuard();
            if (!guard.shownActive() && !guard.shownBroken()) return;
            var pose = event.getPoseStack(); pose.pushPose();
            pose.translate(0, p.getBbHeight() + 1.2, 0);
            pose.mulPose(mc.getEntityRenderDispatcher().cameraOrientation());
            pose.scale(.025F, -.025F, .025F);
            var matrix = pose.last().pose();
            var buffer = event.getMultiBufferSource().getBuffer(RenderType.debugQuads());
            shield(guard.shownPosture(), guard.shownBroken(), (x, y, color) -> {
                int a = color >>> 24, r = (color >> 16) & 255, green = (color >> 8) & 255, b = color & 255;
                buffer.vertex(matrix, x, y, 0).color(r, green, b, a).endVertex();
                buffer.vertex(matrix, x, y + 1, 0).color(r, green, b, a).endVertex();
                buffer.vertex(matrix, x + 1, y + 1, 0).color(r, green, b, a).endVertex();
                buffer.vertex(matrix, x + 1, y, 0).color(r, green, b, a).endVertex();
            });
            pose.popPose();
        });
    }
}
