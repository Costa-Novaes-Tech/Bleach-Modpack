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

/** Supplies a distinct persistent pair when a map has not authored the Orihime occurrence. */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public final class OrihimeQuestEvents {
    private OrihimeQuestEvents() {}
    @SubscribeEvent public static void tick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !(event.player instanceof ServerPlayer player)
                || player instanceof net.minecraftforge.common.util.FakePlayer || player.tickCount % 40 != 0
                || player.level().dimension() != Level.OVERWORLD) return;
        var data = PlayerCapability.get(player).orElse(null);
        if (data == null || !data.getStatus().hasCreatedCharacter() || !"shinigami".equals(data.getCharacter().getRace())
                || !data.getPlayerQuestData().isCompleted(RukiaOpeningQuest.KEY)
                || data.getPlayerQuestData().isCompleted(RukiaOpeningQuest.ORIHIME_KEY)) return;
        provision(player);
    }
    public static void provision(ServerPlayer player) {
        var level = player.serverLevel();
        var index = QuestNpcLocations.get(player.server);
        var known = index.resolveContext("rukia", "karakura_orihime", Level.OVERWORLD.location(), null,
                player.getX(), player.getY(), player.getZ());
        if (known != null) {
            if (level.getEntity(known.entityId()) instanceof QuestNpcEntity rukia) companion(player, rukia);
            return; // Preserve authored or unloaded anchors; never force chunks or duplicate them.
        }
        for (int radius = 20; radius <= 36; radius += 4) for (int angle = 0; angle < 360; angle += 45) {
            var horizontal = player.blockPosition().offset((int)Math.round(Math.cos(Math.toRadians(angle))*radius), 0,
                    (int)Math.round(Math.sin(Math.toRadians(angle))*radius));
            if (!level.hasChunkAt(horizontal)) continue;
            var pos = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, horizontal);
            if (Math.abs(pos.getY()-player.getY()) > 12) continue;
            var rukia = ModEntities.NPC_RUKIA.get().create(level);
            if (!place(player, rukia, pos)) { rukia.discard(); continue; }
            var orihime = newCompanion(player, rukia);
            if (orihime == null) { rukia.discard(); continue; }
            rukia.setQuestContext("karakura_orihime");
            if (!level.addFreshEntity(rukia)) { rukia.discard(); orihime.discard(); continue; }
            if (!level.addFreshEntity(orihime)) { rukia.discard(); orihime.discard(); continue; }
            index.update(rukia); index.update(orihime); QuestNavigationEvents.refresh(player); return;
        }
    }
    private static void companion(ServerPlayer player, QuestNpcEntity rukia) {
        var nearby = player.serverLevel().getEntitiesOfClass(QuestNpcEntity.class, rukia.getBoundingBox().inflate(8),
                npc -> "orihime".equals(npc.getNpcId()) && "karakura_orihime".equals(npc.getQuestContext()));
        if (!nearby.isEmpty()) return;
        var orihime = newCompanion(player, rukia);
        if (orihime != null && player.serverLevel().addFreshEntity(orihime)) QuestNpcLocations.get(player.server).update(orihime);
    }
    private static QuestNpcEntity newCompanion(ServerPlayer player, QuestNpcEntity rukia) {
        var npc = ModEntities.NPC_ORIHIME.get().create(player.serverLevel());
        for (int dx : new int[]{2,-2,0}) for (int dz : new int[]{0,2,-2}) {
            if (dx == 0 && dz == 0) continue;
            for (int dy : new int[]{0,1,-1}) if (place(player, npc, rukia.blockPosition().offset(dx,dy,dz))) {
                npc.setQuestContext("karakura_orihime"); return npc;
            }
        }
        npc.discard(); return null;
    }
    private static boolean place(ServerPlayer player, QuestNpcEntity npc, BlockPos pos) {
        var level = player.serverLevel();
        if (!level.hasChunkAt(pos) || !level.getBlockState(pos.below()).isFaceSturdy(level,pos.below(),net.minecraft.core.Direction.UP)) return false;
        npc.moveTo(pos.getX()+.5,pos.getY(),pos.getZ()+.5);
        return level.getWorldBorder().isWithinBounds(npc.getBoundingBox()) && level.noCollision(npc)
                && !level.containsAnyLiquid(npc.getBoundingBox());
    }
}
