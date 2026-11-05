package com.bleachmod.init;

import com.bleachmod.Reference;
import com.bleachmod.registry.ModEntities;
import net.minecraft.world.item.Item;
import com.bleachmod.common.item.WorldTravelItem;
import com.bleachmod.common.item.ZanpakutoItem;
import com.bleachmod.server.world.HistoryWorld;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.LinkedHashMap;
import java.util.Map;

public final class ModItems {
    private static final Map<String, RegistryObject<Item>> NPC_SPAWN_EGGS = new LinkedHashMap<>();
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Reference.MOD_ID);

    public static final RegistryObject<Item> ASAUCHI = ITEMS.register("asauchi",
            ZanpakutoItem::new);

    /** Indestructible identity weapon; kit access is checked by the progression services. */
    public static final RegistryObject<Item> RYUJIN_JAKKA = ITEMS.register("ryujin_jakka",
            ZanpakutoItem::new);

    public static final RegistryObject<Item> HYORINMARU = ITEMS.register("hyorinmaru",
            ZanpakutoItem::new);
    public static final RegistryObject<Item> SENBONZAKURA = ITEMS.register("senbonzakura", ZanpakutoItem::new);
    public static final RegistryObject<Item> BENIHIME = ITEMS.register("benihime", ZanpakutoItem::new);

    /** Zaraki Kenpachi's notched blade; its Shikai becomes the oversized Nozarashi cleaver. */
    public static final RegistryObject<Item> NOZARASHI = ITEMS.register("nozarashi",
            ZanpakutoItem::new);

    public static final RegistryObject<Item> QUINCY_BOW = ITEMS.register("quincy_bow",
            com.bleachmod.common.quincy.QuincyBowItem::new);
    public static final RegistryObject<Item> QUINCY_ARROW = ITEMS.register("quincy_arrow",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SEELE_SCHNEIDER = ITEMS.register("seele_schneider",
            () -> new Item(new Item.Properties()));

    /** Temporary History Mode travel items; replaced later by Senkaimon/Garganta. */
    public static final RegistryObject<Item> TRAVEL_HUMAN_WORLD = ITEMS.register("travel_human_world",
            () -> new WorldTravelItem(HistoryWorld.HUMAN_WORLD));
    public static final RegistryObject<Item> TRAVEL_SOUL_SOCIETY = ITEMS.register("travel_soul_society",
            () -> new WorldTravelItem(HistoryWorld.SOUL_SOCIETY));
    public static final RegistryObject<Item> TRAVEL_HUECO_MUNDO = ITEMS.register("travel_hueco_mundo",
            () -> new WorldTravelItem(HistoryWorld.HUECO_MUNDO));

    public static final RegistryObject<Item> HOLLOW_SPAWN_EGG =
            ITEMS.register(
                    "hollow_spawn_egg",
                    () -> new ForgeSpawnEggItem(
                            ModEntities.HOLLOW,
                            0x1A1A1A,
                            0xE6E6E6,
                            new Item.Properties()
                    )
            );

    public static final RegistryObject<Item> NPC_SADO_YASUTORA_SPAWN_EGG = npcEgg("sado_yasutora", ModEntities.NPC_SADO_YASUTORA, 0x29231F, 0xD2A17C);
    public static final RegistryObject<Item> NPC_ICHIGO_SPAWN_EGG = npcEgg("ichigo", ModEntities.NPC_ICHIGO, 0xE87822, 0x171717);
    public static final RegistryObject<Item> NPC_ORIHIME_SPAWN_EGG = npcEgg("orihime", ModEntities.NPC_ORIHIME, 0xB05B35, 0xE8D4C2);
    public static final RegistryObject<Item> NPC_URYUU_SPAWN_EGG = npcEgg("uryuu", ModEntities.NPC_URYUU, 0xE8E8E8, 0x37638A);
    public static final RegistryObject<Item> NPC_RUKIA_SPAWN_EGG = npcEgg("rukia", ModEntities.NPC_RUKIA, 0x181818, 0xE2D6C8);
    public static final RegistryObject<Item> NPC_BYAKUYA_SPAWN_EGG = npcEgg("byakuya", ModEntities.NPC_BYAKUYA, 0x241E2D, 0xF1E7DD);
    public static final RegistryObject<Item> NPC_URAHARA_SPAWN_EGG = npcEgg("urahara", ModEntities.NPC_URAHARA, 0x506644, 0xD4A17C);
    public static final RegistryObject<Item> NPC_ULQUIORRA_SPAWN_EGG = npcEgg("ulquiorra", ModEntities.NPC_ULQUIORRA, 0xE8E5DB, 0x315B4D);

    static {
        for (com.bleachmod.registry.NpcRoster.Entry npc : com.bleachmod.registry.NpcRoster.entries())
            if (!NPC_SPAWN_EGGS.containsKey(npc.id()))
                npcEgg(npc.id(), ModEntities.questNpcs().get(npc.id()), npc.eggPrimary(), npc.eggSecondary());
    }

    private static RegistryObject<Item> npcEgg(String npcId,
                                                RegistryObject<? extends net.minecraft.world.entity.EntityType<? extends net.minecraft.world.entity.Mob>> entityType,
                                                int primary, int secondary) {
        RegistryObject<Item> result = ITEMS.register("npc_" + npcId + "_spawn_egg",
                () -> new ForgeSpawnEggItem(entityType, primary, secondary, new Item.Properties()));
        NPC_SPAWN_EGGS.put(npcId, result);
        return result;
    }

    public static Map<String, RegistryObject<Item>> npcSpawnEggs() {
        return java.util.Collections.unmodifiableMap(NPC_SPAWN_EGGS);
    }

    private ModItems() {
    }
}
