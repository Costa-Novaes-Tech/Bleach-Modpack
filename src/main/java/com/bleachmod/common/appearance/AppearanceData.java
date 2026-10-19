package com.bleachmod.common.appearance;

import net.minecraft.nbt.CompoundTag;

import java.util.HashMap;
import java.util.Map;

/**
 * The character's look: which catalog pieces are combined and the colors used for tintable layers.
 * Server-authoritative; every write from the network goes through {@link #sanitize}.
 */
public final class AppearanceData {
    /** Color value meaning "use the piece's own default color". */
    public static final int PIECE_DEFAULT = -1;

    private boolean useVanillaSkin = true;
    private boolean slim;
    private int skinColor = 0xF3D2B5;
    private int hairColor = PIECE_DEFAULT;
    private int eyeColor = PIECE_DEFAULT;
    private final Map<String, String> pieces = new HashMap<>();

    public boolean usesVanillaSkin() { return useVanillaSkin; }
    public void setUseVanillaSkin(boolean value) { useVanillaSkin = value; }
    public boolean isSlim() { return slim; }
    public void setSlim(boolean value) { slim = value; }
    public int skinColor() { return skinColor; }
    public void setSkinColor(int rgb) { skinColor = rgb & 0xFFFFFF; }
    public int hairColor() { return hairColor; }
    public void setHairColor(int rgb) { hairColor = rgb < 0 ? PIECE_DEFAULT : rgb & 0xFFFFFF; }
    public int eyeColor() { return eyeColor; }
    public void setEyeColor(int rgb) { eyeColor = rgb < 0 ? PIECE_DEFAULT : rgb & 0xFFFFFF; }
    public String piece(String category) { return pieces.getOrDefault(category, ""); }
    public void setPiece(String category, String id) { pieces.put(category, id == null ? "" : id); }

    /** Resets to the race defaults from the catalog, keeping the vanilla-skin choice. */
    public void applyDefaults(AppearanceCatalog catalog, String race) {
        AppearanceCatalog.RaceDefaults d = catalog.defaults(race);
        pieces.clear();
        pieces.putAll(d.pieces());
        skinColor = d.skinColor();
        hairColor = PIECE_DEFAULT;
        eyeColor = PIECE_DEFAULT;
        slim = false;
    }

    /** Replaces every piece the race may not wear (or that is unknown or locked) with the race default. */
    public void sanitize(AppearanceCatalog catalog, String race) {
        AppearanceCatalog.RaceDefaults d = catalog.defaults(race);
        for (String category : AppearanceCatalog.CATEGORIES) {
            String id = piece(category);
            boolean ok = id.isEmpty() ? AppearanceCatalog.OPTIONAL.contains(category) : catalog.valid(id, category, race);
            if (!ok) pieces.put(category, d.pieces().getOrDefault(category, ""));
        }
        pieces.keySet().retainAll(AppearanceCatalog.CATEGORIES);
    }

    /** Stable key of everything that changes the composed texture. */
    public String textureKey() {
        StringBuilder key = new StringBuilder();
        for (String category : AppearanceCatalog.CATEGORIES) key.append(piece(category)).append('|');
        return key.append(Integer.toHexString(skinColor)).append('|').append(hairColor).append('|').append(eyeColor).toString();
    }

    public AppearanceData copy() {
        AppearanceData copy = new AppearanceData();
        copy.load(save());
        return copy;
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("vanilla", useVanillaSkin);
        tag.putBoolean("slim", slim);
        tag.putInt("skin", skinColor);
        tag.putInt("hairColor", hairColor);
        tag.putInt("eyeColor", eyeColor);
        CompoundTag p = new CompoundTag();
        for (String category : AppearanceCatalog.CATEGORIES) p.putString(category, piece(category));
        tag.put("pieces", p);
        return tag;
    }

    /** Reads only known keys with bounded values; ids are checked separately by {@link #sanitize}. */
    public void load(CompoundTag tag) {
        if (tag.contains("vanilla")) useVanillaSkin = tag.getBoolean("vanilla");
        if (tag.contains("slim")) slim = tag.getBoolean("slim");
        if (tag.contains("skin")) setSkinColor(tag.getInt("skin"));
        if (tag.contains("hairColor")) setHairColor(tag.getInt("hairColor"));
        if (tag.contains("eyeColor")) setEyeColor(tag.getInt("eyeColor"));
        if (tag.contains("pieces")) {
            CompoundTag p = tag.getCompound("pieces");
            for (String category : AppearanceCatalog.CATEGORIES) {
                String id = p.getString(category);
                if (id.length() <= 64) pieces.put(category, id); // oversized ids keep the current piece
            }
        }
    }
}
