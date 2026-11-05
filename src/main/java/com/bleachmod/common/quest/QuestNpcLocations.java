package com.bleachmod.common.quest;

import com.bleachmod.entity.QuestNpcEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.*;

/** Persistent world-wide NPC index; reads never load or generate chunks. */
public final class QuestNpcLocations extends SavedData {
    private final Map<UUID, QuestWaypoint> locations = new LinkedHashMap<>();

    public static QuestNpcLocations get(MinecraftServer server) {
        return server.overworld().getDataStorage().computeIfAbsent(QuestNpcLocations::load,
                QuestNpcLocations::new, "bleachmod_quest_npcs");
    }

    public void update(QuestNpcEntity npc) {
        put(new QuestWaypoint(npc.getUUID(), npc.getNpcId(), npc.level().dimension().location(), npc.blockPosition(), npc.getQuestContext()));
    }

    public void put(QuestWaypoint location) {
        if (!location.equals(locations.put(location.entityId(), location))) setDirty();
    }

    public void remove(UUID id, ResourceLocation dimension) {
        QuestWaypoint previous = locations.get(id);
        // An old dimension's leave event must not erase a newly registered destination.
        if (previous != null && previous.dimension().equals(dimension)) { locations.remove(id); setDirty(); }
    }

    public QuestWaypoint resolve(String npcId, UUID boundId, ResourceLocation dimension, double x, double y, double z) {
        if (boundId != null) {
            QuestWaypoint bound = locations.get(boundId);
            return bound != null && bound.npcId().equals(npcId) ? bound : null;
        }
        return locations.values().stream().filter(p -> p.npcId().equals(npcId))
                .min(Comparator.<QuestWaypoint>comparingInt(p -> p.dimension().equals(dimension) ? 0 : 1)
                        .thenComparingDouble(p -> p.pos().distToCenterSqr(x, y, z))
                        .thenComparing(p -> p.entityId().toString())).orElse(null);
    }

    @Override public CompoundTag save(CompoundTag tag) {
        ListTag list = new ListTag();
        locations.values().forEach(p -> list.add(p.save()));
        tag.put("npcs", list);
        return tag;
    }

    public QuestWaypoint resolveContext(String npcId, String context, ResourceLocation dimension, UUID boundId,
                                        double x, double y, double z) {
        if (boundId != null) {
            QuestWaypoint target = locations.get(boundId);
            return target != null && target.npcId().equals(npcId) && target.context().equals(context)
                    && target.dimension().equals(dimension) ? target : null;
        }
        return locations.values().stream().filter(p -> p.npcId().equals(npcId) && p.context().equals(context)
                        && p.dimension().equals(dimension))
                .min(Comparator.<QuestWaypoint>comparingDouble(p -> p.pos().distToCenterSqr(x, y, z))
                        .thenComparing(p -> p.entityId().toString())).orElse(null);
    }

    public static QuestNpcLocations load(CompoundTag tag) {
        QuestNpcLocations data = new QuestNpcLocations();
        ListTag list = tag.getList("npcs", Tag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            QuestWaypoint location = QuestWaypoint.load(list.getCompound(i));
            if (location != null) data.locations.put(location.entityId(), location);
        }
        return data;
    }
}
