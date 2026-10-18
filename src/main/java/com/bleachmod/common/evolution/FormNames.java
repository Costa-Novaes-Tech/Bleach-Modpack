package com.bleachmod.common.evolution;

import com.bleachmod.common.data.CharacterData;
import net.minecraft.network.chat.Component;
import java.util.Set;

/** Display names for Zanpakuto forms; an identity may rename a form (Nozarashi Bankai = Berserk Mode). */
public final class FormNames {
    private static final Set<String> RENAMED = Set.of(CharacterData.NOZARASHI + ".bankai");
    private FormNames() { }
    public static String key(String identity, String form) {
        return RENAMED.contains(identity + "." + form) ? "form.bleachmod." + identity + "." + form : "form.bleachmod." + form;
    }
    public static Component of(String identity, String form) { return Component.translatable(key(identity, form)); }
    public static Component of(CharacterData character, String form) { return of(character.getZanpakutoIdentity(), form); }
}
