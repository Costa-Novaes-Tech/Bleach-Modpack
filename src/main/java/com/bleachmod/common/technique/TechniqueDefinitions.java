package com.bleachmod.common.technique;

import com.bleachmod.common.data.CharacterData;
import java.util.*;

/** Immutable gameplay metadata shared by access rules, kit executors and the HUD. */
public final class TechniqueDefinitions {
    public record Definition(String identity, String form, int slot, String id, String nameKey,
                             int mastery, float cost, int cooldownTicks, float drainPerTick) {
        public Definition {
            if (!List.of(CharacterData.RYUJIN, CharacterData.HYORINMARU, CharacterData.SENBONZAKURA, CharacterData.BENIHIME, CharacterData.NOZARASHI).contains(identity)
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
    public static final Continuation FLORAL_BLOOM = new Continuation("floral_bloom", 2, "ability.bleachmod.senbonzakura.floral_bloom", 40, 120, 0);
    public static final Continuation IKKA = new Continuation("ikka_senjinka", 3, "ability.bleachmod.senbonzakura.ikka_senjinka", 20, 160, 0);
    public static List<Continuation> continuations() { return List.of(FLORAL_BLOOM, IKKA); }
    private static final Map<Key, Definition> DEFINITIONS = build();
    private TechniqueDefinitions() { }
    public static int slotRequirement(int slot) { return slot >= 1 && slot <= 4 ? (slot - 1) * 20 : Integer.MAX_VALUE; }
    private static Map<Key, Definition> build() {
        Map<Key, Definition> entries = new LinkedHashMap<>();
        for (String form : List.of("sealed", "shikai", "bankai")) {
            boolean bankai = form.equals("bankai"), sealed = form.equals("sealed");
            String[] fireIds=sealed?new String[]{"sealed_cut","sealed_sweep","unavailable","unavailable"}
                :bankai?new String[]{"flame_dash","higashi","minami","steam_cut"}:new String[]{"flame_barrage","flame_fan","flame_tornado","jokaku"};
            float[] fireCosts=sealed?new float[]{8,8,0,0}:bankai?new float[]{20,25,40,45}:new float[]{15,25,40,35};
            int[] fireCds=sealed?new int[]{60,80,0,0}:bankai?new int[]{100,160,300,1200}:new int[]{80,100,300,300};
            for(int slot=1;slot<=4;slot++)add(entries,CharacterData.RYUJIN,form,slot,fireIds[slot-1],"ability.bleachmod.ryujin."+fireIds[slot-1],fireCosts[slot-1],fireCds[slot-1],0);
            String[] iceIds=sealed?new String[]{"front_cut","wide_cut","unavailable","duel_control"}
                :bankai?new String[]{"ice_dragon","ryusenka","sennen","glacial_zone"}:new String[]{"ice_dragon","crescent_chain","duel_control","glacial_zone"};
            float[] costs=sealed?new float[]{8,8,0,18}:bankai?new float[]{20,25,30,35}:new float[]{15,20,18,30};
            int[] cooldowns=sealed?new int[]{100,160,0,300}:new int[]{100,160,240,300};
            for(int slot=1;slot<=4;slot++)add(entries,CharacterData.HYORINMARU,form,slot,iceIds[slot-1],
                "ability.bleachmod.hyorinmaru."+iceIds[slot-1],costs[slot-1],cooldowns[slot-1],0);
            // Zaraki Kenpachi: brute force when sealed, the Nozarashi cleaver in Shikai, Berserk Mode in Bankai.
            String[] brute = sealed ? new String[]{"brutal_cut", "spiritual_pressure", "eyepatch", "kendo"}
                    : bankai ? new String[]{"berserk_charge", "fury", "roar", "world_cleaver"}
                    : new String[]{"nozarashi_cut", "crushing_leap", "battle_thirst", "drink_nozarashi"};
            float[] bruteCosts = sealed ? new float[]{10, 15, 0, 20} : bankai ? new float[]{25, 30, 35, 60} : new float[]{20, 25, 25, 40};
            int[] bruteCooldowns = sealed ? new int[]{60, 200, 100, 300} : bankai ? new int[]{100, 160, 300, 1200} : new int[]{120, 160, 400, 400};
            for (int slot = 1; slot <= 4; slot++) add(entries, CharacterData.NOZARASHI, form, slot, brute[slot - 1],
                    "ability.bleachmod.nozarashi." + brute[slot - 1], bruteCosts[slot - 1], bruteCooldowns[slot - 1],
                    sealed && slot == 3 ? .1F : 0);
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
        for(String form:List.of("shikai","bankai")) {
            boolean bankai=form.equals("bankai");
            String[] ids=bankai?new String[]{"dissection","reconstruction","restructure","path"}:new String[]{"nake","shield","shibari","hiasobi"};
            float[] costs=bankai?new float[]{25,28,22,30}:new float[]{12,18,20,25};
            int[] cooldowns=bankai?new int[]{140,240,200,260}:new int[]{60,140,160,200};
            for(int slot=1;slot<=4;slot++)add(entries,CharacterData.BENIHIME,form,slot,ids[slot-1],"ability.bleachmod.benihime."+ids[slot-1],costs[slot-1],cooldowns[slot-1],0);
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
