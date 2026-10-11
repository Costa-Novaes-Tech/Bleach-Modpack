package com.bleachmod.common.visual;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

/** A shared budget for all layers of one owner-only frame. No world or gameplay mutations. */
public final class VisualFrame {
    public static final int LIMIT = 48;
    public static int budget(int intensity) {
        if (intensity < 1 || intensity > 3) throw new IllegalArgumentException("Intensity must be 1..3");
        return LIMIT * intensity / 3;
    }
    private final ServerPlayer viewer;
    private final ServerLevel level;
    private int requested;
    public VisualFrame(ServerPlayer viewer, ServerLevel level) { this.viewer = viewer; this.level = level; }
    public int requested() { return requested; }
    public boolean emit(ParticleOptions particle, Vec3 point, double spread, double speed) {
        if (requested >= LIMIT || particle == null || point == null || !Double.isFinite(point.x) || !Double.isFinite(point.y)
                || !Double.isFinite(point.z) || !Double.isFinite(spread) || !Double.isFinite(speed) || spread < 0 || spread > 1 || speed < 0 || speed > 1
                || viewer.serverLevel() != level || !viewer.isAlive() || viewer.isSpectator() || viewer.position().distanceToSqr(point) > 32 * 32
                || !level.hasChunkAt(BlockPos.containing(point))) return false;
        level.sendParticles(viewer, particle, false, point.x, point.y, point.z, 1, spread, spread, spread, speed);
        requested++; return true;
    }
}
