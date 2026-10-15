package com.bleachmod.common;

import com.bleachmod.common.data.*;
import com.bleachmod.init.ModItems;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;

/** Minimal public appearance: absent, sheathed, main hand, off hand. Never exposes inventory contents. */
public final class ZanpakutoCarry {
    private ZanpakutoCarry() { }
    public static Item item(String identity) {
        return switch (identity) {
            case CharacterData.RYUJIN -> ModItems.RYUJIN_JAKKA.get();
            case CharacterData.HYORINMARU -> ModItems.HYORINMARU.get();
            case CharacterData.SENBONZAKURA -> ModItems.SENBONZAKURA.get(); case CharacterData.BENIHIME -> ModItems.BENIHIME.get();
            default -> ModItems.ASAUCHI.get();
        };
    }
    public static int state(Player player, PlayerData data) {
        if (!player.isAlive() || player.isSpectator() || !data.getStatus().hasCreatedCharacter()) return 0;
        Item item = item(data.getCharacter().getZanpakutoIdentity());
        if (player.getMainHandItem().is(item)) return 2;
        if (player.getOffhandItem().is(item)) return 3;
        return player.getInventory().items.stream().anyMatch(stack -> stack.is(item)) ? 1 : 0;
    }
    public static boolean update(Player player, PlayerData data) {
        int next = state(player, data);
        boolean changed = next != data.getCharacter().getCarryState();
        data.getCharacter().setCarryState(next); return changed;
    }
}
