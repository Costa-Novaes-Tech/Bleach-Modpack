package com.bleachmod.common.quest;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import java.util.UUID;

/** Server supplied location of an actual quest giver, never a guessed spawn position. */
public record QuestWaypoint(UUID entityId, String npcId, ResourceLocation dimension, BlockPos pos, String context) {
    public QuestWaypoint(UUID entityId, String npcId, ResourceLocation dimension, BlockPos pos) {
        this(entityId, npcId, dimension, pos, "");
    }
    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putUUID("entity", entityId);
        tag.putString("npc", npcId);
        tag.putString("dimension", dimension.toString());
        tag.putLong("pos", pos.asLong());
        if (!context.isEmpty()) tag.putString("context", context);
        return tag;
    }

    public static QuestWaypoint load(CompoundTag tag) {
        ResourceLocation dimension = ResourceLocation.tryParse(tag.getString("dimension"));
        String npc = tag.getString("npc");
        if (!tag.hasUUID("entity") || !tag.contains("pos", net.minecraft.nbt.Tag.TAG_LONG)
                || dimension == null || !npc.matches("[a-z0-9_]{1,64}")) return null;
        String context = tag.getString("context");
        if (!context.isEmpty() && !StoryQuestInfo.validContext(context)) return null;
        return new QuestWaypoint(tag.getUUID("entity"), npc, dimension, BlockPos.of(tag.getLong("pos")), context);
    }

    /** Minecraft blocks are displayed as metres; include vertical separation. */
    public long distanceMetres(double x, double y, double z) {
        return Math.round(Math.sqrt(pos.distToCenterSqr(x, y, z)));
    }

    /** Clockwise bearing on the screen: zero ahead, +90 right, 180 behind. */
    public double relativeBearing(double x, double z, float yaw) {
        double targetYaw = Math.toDegrees(Math.atan2(-(pos.getX() + .5 - x), pos.getZ() + .5 - z));
        return Mth.wrapDegrees(targetYaw - yaw);
    }
}
