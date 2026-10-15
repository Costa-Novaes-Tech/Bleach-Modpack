package com.bleachmod.common.technique;

import com.bleachmod.common.data.CharacterData;
import java.util.*;

/** Immutable gameplay metadata shared by access rules, kit executors and the HUD. */
public final class TechniqueDefinitions {
    public record Definition(String identity, String form, int slot, String id, String nameKey,
                             int mastery, float cost, int cooldownTicks, float drainPerTick) {
        public Definition {
            if (!List.of(CharacterData.RYUJIN, CharacterData.HYORINMARU, CharacterData.SENBONZAKURA).contains(identity)
                    || !List.of("sealed", "shikai", "bankai").contains(form) || slot < 1 || slot > 4
                    || id == null || id.isBlank() || nameKey == null || nameKey.isBlank() || mastery < 0 || mastery > 100
                    || !Float.isFinite(cost) || cost < 0 || cooldownTicks < 0 || !Float.isFinite(drainPerTick) || drainPerTick < 0)
                throw new IllegalArgumentException("Invalid technique definition");
        }
    }
    private record Key(String identity, String form, int slot) { }
    public record Continuation(String id, int slot, String nameKey, int readyTicks, int windowTicks, float cost) {
        public Continuation {
            if (id == null || id.isBlank() || slot < 1 || slot > 4 || nameKey == null || nameKey.isBlank()
                    || readyTicks < 0 || windowTicks <= readyTicks || !Float.isFinite(cost) || cost < 0)
                throw new IllegalArgumentException("Invalid continuation");
        }
    }
    public static final Continuation FLORAL_BLOOM = new Continuation("floral_bloom", 2, "ability.bleachmod.senbonzakura.floral_bloom", 100, 200, 0);
    public static final Continuation IKKA = new Continuation("ikka_senjinka", 3, "ability.bleachmod.senbonzakura.ikka_senjinka", 20, 160, 0);
    public static List<Continuation> continuations() { return List.of(FLORAL_BLOOM, IKKA); }
    private static final Map<Key, Definition> DEFINITIONS = build();
    private TechniqueDefinitions() { }
    public static int slotRequirement(int slot) { return slot >= 1 && slot <= 4 ? (slot - 1) * 20 : Integer.MAX_VALUE; }
    private static Map<Key, Definition> build() {
        Map<Key, Definition> entries = new LinkedHashMap<>();
        for (String form : List.of("sealed", "shikai", "bankai")) {
            boolean bankai = form.equals("bankai"), sealed = form.equals("sealed");
            add(entries, CharacterData.RYUJIN, form, 1, bankai ? "flame_dash" : "ignition", "ability.bleachmod.ryujin." + (bankai ? "flame_dash" : "ignition"), bankai ? 20 : 0, bankai ? 100 : 0, bankai ? 0 : .05F);
            add(entries, CharacterData.RYUJIN, form, 2, bankai ? "flame_fan" : "flame_barrage", "ability.bleachmod.ryujin." + (bankai ? "flame_fan" : "flame_barrage"), bankai ? 25 : 15, 80, 0);
            add(entries, CharacterData.RYUJIN, form, 3, bankai ? "flame_tornado" : "flame_wall", "ability.bleachmod.ryujin." + (bankai ? "flame_tornado" : "flame_wall"), 40, bankai ? 300 : 400, 0);
            add(entries, CharacterData.RYUJIN, form, 4, bankai ? "steam_cut" : "flame_bats", "ability.bleachmod.ryujin." + (bankai ? "steam_cut" : "flame_bats"), bankai ? 45 : 25, bankai ? 1200 : 200, 0);
            String[] iceIds = sealed ? new String[]{"front_cut", "wide_cut", "ice_armor", "duel_control"}
                    : new String[]{"ice_dragon", "wide_cut", "ice_barrier", "glacial_zone"};
            float[] costs = sealed ? new float[]{8, 8, 12, 18} : bankai ? new float[]{20, 25, 30, 35} : new float[]{15, 20, 25, 30};
            int[] cooldowns = {100, 160, 240, 300};
            for (int slot = 1; slot <= 4; slot++) add(entries, CharacterData.HYORINMARU, form, slot, iceIds[slot - 1],
                    "ability.bleachmod.hyorinmaru." + (sealed ? "sealed_" : "slot_") + slot, costs[slot - 1], cooldowns[slot - 1], 0);
        }
        for (String form : List.of("sealed", "shikai", "bankai")) {
            boolean bankai = form.equals("bankai");
            String[] ids = bankai ? new String[]{"five_cuts", "floral_garden", "senkei", "hakuteiken"}
                    : new String[]{"petal_cut", "petal_stream", "petal_convergence", "petal_guard"};
            float[] costs = bankai ? new float[]{25,35,25,40} : new float[]{10,15,20,18};
            int[] cooldowns = bankai ? new int[]{160,400,240,300} : new int[]{60,100,140,180};
            for (int slot = 1; slot <= 4; slot++) add(entries, CharacterData.SENBONZAKURA, form, slot,
                    ids[slot-1], "ability.bleachmod.senbonzakura." + ids[slot-1], costs[slot-1], cooldowns[slot-1], 0);
        }
        return Collections.unmodifiableMap(entries);
    }
    private static void add(Map<Key, Definition> entries, String identity, String form, int slot, String id, String nameKey, float cost, int cooldown, float drain) {
        var key = new Key(identity, form, slot);
        var definition = new Definition(identity, form, slot, id, nameKey, slotRequirement(slot), cost, cooldown, drain);
        if (entries.putIfAbsent(key, definition) != null) throw new IllegalStateException("Duplicate technique: " + key);
    }
    public static Optional<Definition> find(String identity, String form, int slot) { return Optional.ofNullable(DEFINITIONS.get(new Key(identity, form, slot))); }
    public static Definition require(String identity, String form, int slot) {
        return find(identity, form, slot).orElseThrow(() -> new IllegalArgumentException("Unknown technique definition"));
    }
    public static List<Definition> all() { return List.copyOf(DEFINITIONS.values()); }
}
