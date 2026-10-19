package com.bleachmod.common.appearance;

import com.bleachmod.common.data.PlayerData;
import com.bleachmod.common.network.NetworkHandler;
import com.bleachmod.common.network.SyncHelper;
import com.bleachmod.common.network.s2c.OpenAppearanceEditorS2C;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/** Server rules for changing a character's look: validation and the editing window. */
public final class AppearanceService {
    /** How long an opened editor may still submit (the NPC or item that opens it is still to be defined). */
    public static final int EDIT_WINDOW_TICKS = 20 * 60 * 10;
    private static final Map<UUID, Long> EDIT_UNTIL = new HashMap<>();

    private AppearanceService() { }

    /** Drops pieces that no longer exist in the catalog or that the race may not wear. */
    public static void normalize(PlayerData data) {
        if (data.getStatus().hasCreatedCharacter() && !data.getCharacter().getRace().isEmpty())
            data.getAppearance().sanitize(AppearanceCatalog.get(), data.getCharacter().getRace());
    }

    /** Validates a look sent by the client and stores it; nothing from the packet is trusted as is. */
    public static void apply(ServerPlayer player, PlayerData data, CompoundTag requested) {
        AppearanceData incoming = new AppearanceData();
        incoming.applyDefaults(AppearanceCatalog.get(), data.getCharacter().getRace());
        if (requested != null) incoming.load(requested);
        incoming.sanitize(AppearanceCatalog.get(), data.getCharacter().getRace());
        data.getAppearance().load(incoming.save());
        SyncHelper.appearance(player);
    }

    public static void openEditor(ServerPlayer player) {
        EDIT_UNTIL.put(player.getUUID(), player.serverLevel().getGameTime() + EDIT_WINDOW_TICKS);
        NetworkHandler.sendToPlayer(new OpenAppearanceEditorS2C(), player);
    }

    /** True once per opened editor; the window closes when used or when it expires. */
    public static boolean consumeEdit(ServerPlayer player) {
        Long until = EDIT_UNTIL.remove(player.getUUID());
        return until != null && player.serverLevel().getGameTime() <= until;
    }

    public static void forget(ServerPlayer player) { EDIT_UNTIL.remove(player.getUUID()); }
}
