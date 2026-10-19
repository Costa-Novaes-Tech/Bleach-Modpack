package com.bleachmod.client.appearance;

import com.bleachmod.common.appearance.AppearanceData;
import com.bleachmod.common.data.PlayerCapability;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;

import java.util.UUID;

/** Which look a client player is drawn with: the editor preview first, then the synced appearance. */
public final class AppearanceClient {
    private static UUID previewOwner;
    private static AppearanceData preview;

    private AppearanceClient() { }

    /** Shows {@code look} on that player only, until {@link #endPreview}; nothing is sent to the server. */
    public static void preview(UUID player, AppearanceData look) { previewOwner = player; preview = look; }

    public static void endPreview() { previewOwner = null; preview = null; }

    private static AppearanceData look(AbstractClientPlayer player) {
        if (preview != null && player.getUUID().equals(previewOwner)) return preview;
        return PlayerCapability.get(player).resolve().filter(d -> d.getStatus().hasCreatedCharacter())
                .map(d -> d.getAppearance()).orElse(null);
    }

    /** Composed skin, or null to keep the player's Minecraft skin. */
    public static ResourceLocation skin(AbstractClientPlayer player) {
        AppearanceData look = look(player);
        if (look == null || look.usesVanillaSkin() || !RenderSystem.isOnRenderThread()) return null;
        return SkinCompositor.texture(look);
    }

    /** "slim" or "default" for a custom look, or null to keep the Minecraft skin model. */
    public static String model(AbstractClientPlayer player) {
        AppearanceData look = look(player);
        if (look == null || look.usesVanillaSkin()) return null;
        return look.isSlim() ? "slim" : "default";
    }
}
