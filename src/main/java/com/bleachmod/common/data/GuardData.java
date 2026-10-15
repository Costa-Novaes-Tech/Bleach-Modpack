package com.bleachmod.common.data;

import com.bleachmod.common.GuardPolicy;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;

/** Pool/recovery persist; input leases and observer snapshots are transient. */
public final class GuardData {
    private float posture = GuardPolicy.MAX, shownPosture = GuardPolicy.MAX;
    private int restTicks, breakTicks, shownBreak;
    private boolean active, releaseRequired, shownActive;
    private long leaseUntil, lastRequest = Long.MIN_VALUE;
    private int published = Integer.MIN_VALUE;
    public float posture() { return posture; }
    public boolean active() { return active; }
    public boolean broken() { return breakTicks > 0; }
    public int breakTicks() { return breakTicks; }
    public float shownPosture() { return shownPosture; }
    public boolean shownActive() { return shownActive; }
    public boolean shownBroken() { return shownBreak > 0; }
    public boolean request(long tick) {
        if (lastRequest != Long.MIN_VALUE && tick - lastRequest < 2) return false;
        lastRequest = tick;
        if (broken() || releaseRequired || (!active && posture < GuardPolicy.RESUME)) return false;
        active = true; leaseUntil = tick + GuardPolicy.INPUT_LEASE;
        return true;
    }
    public void release() { active = false; releaseRequired = false; }
    public void interrupt() { if (active) releaseRequired = true; active = false; }
    public boolean absorb(float amount) {
        if (!active || !Float.isFinite(amount) || amount <= 0) return false;
        posture = Math.max(0, posture - amount); restTicks = GuardPolicy.REST;
        if (posture > 0) return false;
        active = false; releaseRequired = true; breakTicks = GuardPolicy.BREAK_TICKS;
        return true;
    }
    public void tick(long tick) {
        if (active && tick >= leaseUntil) interrupt();
        if (breakTicks > 0) breakTicks--;
        if (restTicks > 0) restTicks--;
        else if (!active && !broken()) posture = Math.min(GuardPolicy.MAX, posture + GuardPolicy.REGEN);
    }
    public void reset() {
        posture = GuardPolicy.MAX; restTicks = 0; breakTicks = 0;
        release(); lastRequest = Long.MIN_VALUE;
    }
    public boolean needsPublish() {
        int state = (int) Math.ceil(posture / 5) * 100 + (active ? 1 : 0) + (broken() ? 2 : 0);
        if (state == published) return false;
        published = state; return true;
    }
    public CompoundTag snapshot() {
        var tag = new CompoundTag(); tag.putFloat("posture", posture);
        tag.putBoolean("active", active); tag.putInt("breakTicks", breakTicks); return tag;
    }
    public void readSnapshot(CompoundTag tag) {
        float value = tag.getFloat("posture");
        shownPosture = Float.isFinite(value) ? Mth.clamp(value, 0, GuardPolicy.MAX) : 0;
        shownBreak = Mth.clamp(tag.getInt("breakTicks"), 0, GuardPolicy.BREAK_TICKS);
        shownActive = tag.getBoolean("active") && shownBreak == 0 && shownPosture > 0;
    }
    public CompoundTag save() {
        var tag = new CompoundTag(); tag.putFloat("posture", posture);
        tag.putInt("restTicks", restTicks); tag.putInt("breakTicks", breakTicks); return tag;
    }
    public void load(CompoundTag tag) {
        release(); lastRequest = Long.MIN_VALUE;
        float value = tag.getFloat("posture");
        posture = Float.isFinite(value) ? Mth.clamp(value, 0, GuardPolicy.MAX) : 0;
        restTicks = Mth.clamp(tag.getInt("restTicks"), 0, GuardPolicy.REST);
        breakTicks = Mth.clamp(tag.getInt("breakTicks"), 0, GuardPolicy.BREAK_TICKS);
        if (broken()) posture = 0;
    }
}
