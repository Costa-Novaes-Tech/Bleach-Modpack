package com.bleachmod.server.events;

import com.bleachmod.Reference;
import com.bleachmod.common.data.PlayerCapability;
import com.bleachmod.common.quest.*;
import com.bleachmod.entity.QuestNpcEntity;
import com.bleachmod.registry.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/** Supplies one persistent opening Rukia if a map has not authored her yet. */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public final class RukiaOpeningEvents {
    private RukiaOpeningEvents() {}
    @SubscribeEvent public static void tick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !(event.player instanceof ServerPlayer player)
                || player instanceof net.minecraftforge.common.util.FakePlayer
                || player.tickCount % 40 != 0 || player.level().dimension() != Level.OVERWORLD) return;
        var data = PlayerCapability.get(player).orElse(null);
        if (data == null || !data.getStatus().hasCreatedCharacter() || !"shinigami".equals(data.getCharacter().getRace())
                || data.getPlayerQuestData().isCompleted(RukiaOpeningQuest.KEY)) return;
        var index = QuestNpcLocations.get(player.server);
        if (index.resolveContext("rukia", "karakura_inicio", Level.OVERWORLD.location(), null, player.getX(), player.getY(), player.getZ()) != null) return;
        var level = player.serverLevel();
        var existing = level.getEntitiesOfClass(QuestNpcEntity.class, player.getBoundingBox().inflate(96),
                npc -> "rukia".equals(npc.getNpcId()) && npc.getQuestContext().isEmpty()).stream()
                .min(java.util.Comparator.comparingDouble(player::distanceToSqr)).orElse(null);
        if (existing != null) { existing.setQuestContext("karakura_inicio"); QuestNavigationEvents.refresh(player); return; }
        for (int radius = 12; radius <= 24; radius += 4) for (int angle = 0; angle < 360; angle += 45) {
            BlockPos horizontal = player.blockPosition().offset((int) Math.round(Math.cos(Math.toRadians(angle)) * radius), 0,
                    (int) Math.round(Math.sin(Math.toRadians(angle)) * radius));
            if (!level.hasChunkAt(horizontal)) continue;
            BlockPos pos = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, horizontal);
            if (!level.getBlockState(pos.below()).isFaceSturdy(level, pos.below(), net.minecraft.core.Direction.UP)
                    || Math.abs(pos.getY() - player.getY()) > 12) continue;
            var npc = ModEntities.NPC_RUKIA.get().create(level);
            npc.moveTo(pos.getX() + .5, pos.getY(), pos.getZ() + .5);
            if (!level.getWorldBorder().isWithinBounds(npc.getBoundingBox()) || !level.noCollision(npc)
                    || level.containsAnyLiquid(npc.getBoundingBox())) { npc.discard(); continue; }
            npc.setQuestContext("karakura_inicio");
            if (level.addFreshEntity(npc)) { QuestNavigationEvents.refresh(player); return; }
        }
    }
}
