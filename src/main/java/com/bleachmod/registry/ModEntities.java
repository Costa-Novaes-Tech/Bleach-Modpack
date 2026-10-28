package com.bleachmod.registry;

import com.bleachmod.Reference;
import com.bleachmod.entity.HollowEntity;
import com.bleachmod.entity.QuestNpcEntity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import com.bleachmod.entity.HollowEntity;
import com.bleachmod.entity.HollowBossEntity;
import com.bleachmod.entity.QuestNpcEntity;

import java.util.LinkedHashMap;
import java.util.Map;

public class ModEntities {

    private static final Map<String, RegistryObject<EntityType<QuestNpcEntity>>> NPCS = new LinkedHashMap<>();

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(
                    ForgeRegistries.ENTITY_TYPES,
                    Reference.MOD_ID
            );

    public static final RegistryObject<EntityType<com.bleachmod.common.quincy.QuincyArrowEntity>> QUINCY_ARROW =
            ENTITY_TYPES.register("quincy_arrow",
                    () -> EntityType.Builder.<com.bleachmod.common.quincy.QuincyArrowEntity>of(com.bleachmod.common.quincy.QuincyArrowEntity::new, MobCategory.MISC)
                            .sized(0.5F, 0.5F).clientTrackingRange(8).updateInterval(1).build("quincy_arrow"));

    public static final RegistryObject<EntityType<com.bleachmod.common.quincy.SeeleSchneiderEntity>> SEELE_SCHNEIDER_PROJECTILE =
            ENTITY_TYPES.register("seele_schneider_projectile",
                    () -> EntityType.Builder.<com.bleachmod.common.quincy.SeeleSchneiderEntity>of(com.bleachmod.common.quincy.SeeleSchneiderEntity::new, MobCategory.MISC)
                            .sized(0.5F, 0.5F).clientTrackingRange(8).updateInterval(1).build("seele_schneider_projectile"));

    public static final RegistryObject<EntityType<com.bleachmod.common.quincy.SprengerNodeEntity>> SPRENGER_NODE =
            ENTITY_TYPES.register("sprenger_node",
                    () -> EntityType.Builder.<com.bleachmod.common.quincy.SprengerNodeEntity>of(com.bleachmod.common.quincy.SprengerNodeEntity::new, MobCategory.MISC)
                            .sized(0.5F, 1.2F).clientTrackingRange(8).updateInterval(2).build("sprenger_node"));

    public static final RegistryObject<EntityType<com.bleachmod.entity.IceDragonEntity>> ICE_DRAGON = ENTITY_TYPES.register("ice_dragon",
            () -> EntityType.Builder.of(com.bleachmod.entity.IceDragonEntity::new, MobCategory.MISC)
                    .sized(2, 0.8F).clientTrackingRange(8).updateInterval(1).noSave().noSummon().build("ice_dragon"));

    public static final RegistryObject<EntityType<HollowEntity>> HOLLOW =
            ENTITY_TYPES.register(
                    "hollow",
                    () -> EntityType.Builder
                            .of(HollowEntity::new, MobCategory.MONSTER)
                            .sized(0.8F, 2.0F)
                            .build("hollow")
            );

    public static final RegistryObject<EntityType<HollowBossEntity>> HOLLOW_BOSS =
            ENTITY_TYPES.register(
                    "hollow_boss",
                    () -> EntityType.Builder
                            .of(HollowBossEntity::new, MobCategory.MONSTER)
                            .sized(2.8F, 5.4F)
                            .clientTrackingRange(12)
                            .updateInterval(1)
                            .build("hollow_boss")
            );

    public static final RegistryObject<EntityType<QuestNpcEntity>> NPC_SADO_YASUTORA = registerNpc("sado_yasutora");
    public static final RegistryObject<EntityType<QuestNpcEntity>> NPC_ICHIGO = registerNpc("ichigo");
    public static final RegistryObject<EntityType<QuestNpcEntity>> NPC_ORIHIME = registerNpc("orihime");
    public static final RegistryObject<EntityType<QuestNpcEntity>> NPC_URYUU = registerNpc("uryuu");
    public static final RegistryObject<EntityType<QuestNpcEntity>> NPC_RUKIA = registerNpc("rukia");
    public static final RegistryObject<EntityType<QuestNpcEntity>> NPC_BYAKUYA = registerNpc("byakuya");
    public static final RegistryObject<EntityType<QuestNpcEntity>> NPC_URAHARA = registerNpc("urahara");
    public static final RegistryObject<EntityType<QuestNpcEntity>> NPC_ULQUIORRA = registerNpc("ulquiorra");

    static {
        // Skin-only NPCs from the roster; the quest givers above keep their fields for direct references.
        for (NpcRoster.Entry npc : NpcRoster.entries()) if (!NPCS.containsKey(npc.id())) registerNpc(npc.id());
    }

    private static RegistryObject<EntityType<QuestNpcEntity>> registerNpc(String npcId) {
        RegistryObject<EntityType<QuestNpcEntity>> result = ENTITY_TYPES.register(
                "npc_" + npcId,
                () -> EntityType.Builder.of(QuestNpcEntity::new, MobCategory.CREATURE)
                        .sized(0.6F, 1.8F)
                        .clientTrackingRange(10)
                        .build("npc_" + npcId)
        );
        NPCS.put(npcId, result);
        return result;
    }

    /** Every NPC (quest givers and skin-only ones), in registration order. */
    public static Map<String, RegistryObject<EntityType<QuestNpcEntity>>> questNpcs() {
        return java.util.Collections.unmodifiableMap(NPCS);
    }

    public static String getNpcId(EntityType<?> type) {
        return NPCS.entrySet().stream()
                .filter(entry -> entry.getValue().isPresent() && entry.getValue().get() == type)
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse("");
    }
}
