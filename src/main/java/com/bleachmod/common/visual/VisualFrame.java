package com.bleachmod.common.visual;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

/** A shared particle budget for one frame. No world or gameplay mutations. */
public final class VisualFrame {
    public static final int LIMIT = 48;
    public static final int PRODUCTION_LIMIT = 160;
    public static final int TECHNIQUE_LIMIT = 1056;
    public enum Audience { OWNER, NEARBY }
    public static int budget(int intensity) {
        if (intensity < 1 || intensity > 3) throw new IllegalArgumentException("Intensity must be 1..3");
        return LIMIT * intensity / 3;
    }
    private final ServerPlayer viewer;
    private final ServerLevel level;
    private final Audience audience;
    private final int limit;
    private final Vec3 anchor;
    private final double maxSpread;
    private int requested;
    public VisualFrame(ServerPlayer viewer, ServerLevel level) { this(viewer, level, Audience.OWNER, LIMIT); }
    private VisualFrame(ServerPlayer viewer, ServerLevel level, Audience audience, int limit) {
        this(viewer, level, audience, limit, null, 1);
    }
    private VisualFrame(ServerPlayer viewer, ServerLevel level, Audience audience, int limit, Vec3 anchor, double maxSpread) {
        this.viewer = java.util.Objects.requireNonNull(viewer);
        this.level = java.util.Objects.requireNonNull(level);
        this.audience = audience; this.limit = limit;
        this.anchor = anchor; this.maxSpread = maxSpread;
    }
    public static VisualFrame nearby(ServerPlayer owner, int limit) {
        if (limit < 1 || limit > PRODUCTION_LIMIT) throw new IllegalArgumentException("Production budget must be 1..160");
        return new VisualFrame(owner, owner.serverLevel(), Audience.NEARBY, limit);
    }
    public Audience audience() { return audience; }
    /** One shared frame per technique update, centered on its fixed field or moving visual entity. */
    public static VisualFrame technique(ServerPlayer owner, Vec3 anchor, int limit) {
        if (!finite(anchor) || limit < 1 || limit > TECHNIQUE_LIMIT) throw new IllegalArgumentException("Invalid technique frame");
        return new VisualFrame(owner, owner.serverLevel(), Audience.NEARBY, limit, anchor, 6);
    }
    /** Vanilla normal particle range is measured from each emission, in its own dimension. */
    public boolean includes(ServerPlayer receiver, Vec3 point) {
        return receiver != null && finite(point) && receiver.serverLevel() == level
                && (audience == Audience.NEARBY || receiver == viewer)
                && receiver.position().distanceToSqr(point) <= 32 * 32;
    }
    private static boolean finite(Vec3 point) {
        return point != null && Double.isFinite(point.x) && Double.isFinite(point.y) && Double.isFinite(point.z);
    }
    private boolean spread(double value) { return Double.isFinite(value) && value >= 0 && value <= maxSpread; }
    public int requested() { return requested; }
    public boolean emit(ParticleOptions particle, Vec3 point, double spread, double speed) {
        return emit(particle, point, 1, spread, spread, spread, speed);
    }
    public boolean emit(ParticleOptions particle, Vec3 point, int count, double dx, double dy, double dz, double speed) {
        // Reject an entire batch rather than silently reducing the approved visual density.
        if (count < 1 || count > limit - requested || particle == null || !finite(point)
                || !spread(dx) || !spread(dy) || !spread(dz) || !Double.isFinite(speed) || speed < 0 || speed > 1
                || viewer.serverLevel() != level || !viewer.isAlive() || viewer.isSpectator() || (anchor == null ? viewer.position() : anchor).distanceToSqr(point) > 32 * 32
                || !level.hasChunkAt(BlockPos.containing(point))) return false;
        if (audience == Audience.OWNER) {
            level.sendParticles(viewer, particle, false, point.x, point.y, point.z, count, dx, dy, dz, speed);
        } else {
            for (ServerPlayer receiver : level.players()) if (includes(receiver, point))
                level.sendParticles(receiver, particle, false, point.x, point.y, point.z, count, dx, dy, dz, speed);
        }
        requested += count; return true;
    }
}
