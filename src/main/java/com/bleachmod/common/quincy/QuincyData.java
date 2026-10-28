package com.bleachmod.common.quincy;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.world.phys.Vec3;

import java.util.*;

/**
 * Substate for Quincy archetype: Kojaku spirit bow, Licht Regen, Hirenkyaku and Sprenger.
 */
public class QuincyData {
    private double mastery;
    private final double[] techniqueMasteries=new double[4];
    public double getMastery(){return mastery;}
    public double getTechniqueMastery(int slot){QuincyTechniques.slot(slot);return techniqueMasteries[slot-1];}
    public void addPractice(int slot,double profileGain,double techniqueGain){
        if(slot<0||slot>4||!Double.isFinite(profileGain)||!Double.isFinite(techniqueGain)||profileGain<0||techniqueGain<0)return;
        mastery=clamp(mastery+profileGain);
        if(slot>0)techniqueMasteries[slot-1]=clamp(techniqueMasteries[slot-1]+techniqueGain);
    }
    public void maximizeMastery(){mastery=100;java.util.Arrays.fill(techniqueMasteries,100);}
    private static double clamp(double value){return Double.isFinite(value)?Math.max(0,Math.min(100,value)):0;}

    // Sprenger state
    private boolean sprengerActive;
    private BlockPos sprengerCenter = BlockPos.ZERO;
    private final List<BlockPos> sprengerVertices = new ArrayList<>();
    private long sprengerCreationTick = Long.MIN_VALUE;
    private boolean sprengerDetonated;

    // Licht Regen active barrage
    private boolean lichtRegenActive;
    private int lichtRegenTicksLeft;
    private int lichtRegenFiredCount;
    private Vec3 lichtRegenLook = Vec3.ZERO;
    private final Map<UUID, Integer> lichtRegenHits = new HashMap<>();

    public boolean isSprengerActive() {
        return sprengerActive;
    }

    public BlockPos getSprengerCenter() {
        return sprengerCenter;
    }

    public List<BlockPos> getSprengerVertices() {
        return Collections.unmodifiableList(sprengerVertices);
    }

    public long getSprengerCreationTick() {
        return sprengerCreationTick;
    }

    public boolean isSprengerDetonated() {
        return sprengerDetonated;
    }

    public void startSprenger(BlockPos center, List<BlockPos> vertices, long gameTick) {
        this.sprengerActive = true;
        this.sprengerCenter = center;
        this.sprengerVertices.clear();
        this.sprengerVertices.addAll(vertices);
        this.sprengerCreationTick = gameTick;
        this.sprengerDetonated = false;
    }

    public void detonateSprenger() {
        this.sprengerDetonated = true;
        clearSprenger();
    }

    public void clearSprenger() {
        this.sprengerActive = false;
        this.sprengerCenter = BlockPos.ZERO;
        this.sprengerVertices.clear();
        this.sprengerCreationTick = Long.MIN_VALUE;
    }

    // Licht Regen
    public boolean isLichtRegenActive() {
        return lichtRegenActive;
    }

    public int getLichtRegenTicksLeft() {
        return lichtRegenTicksLeft;
    }

    public int getLichtRegenFiredCount() {
        return lichtRegenFiredCount;
    }

    public Vec3 getLichtRegenLook() {
        return lichtRegenLook;
    }

    public void startLichtRegen(Vec3 lookVector) {
        this.lichtRegenActive = true;
        this.lichtRegenTicksLeft = 40; // 2.0s duration
        this.lichtRegenFiredCount = 0;
        this.lichtRegenLook = lookVector;
        this.lichtRegenHits.clear();
    }

    public void advanceLichtRegen() {
        if (lichtRegenTicksLeft > 0) {
            lichtRegenTicksLeft--;
        }
        if (lichtRegenTicksLeft == 0) {
            stopLichtRegen();
        }
    }

    public void recordLichtRegenShot() {
        this.lichtRegenFiredCount++;
    }

    public int getLichtRegenHitsOn(UUID targetId) {
        return lichtRegenHits.getOrDefault(targetId, 0);
    }

    public void recordLichtRegenHit(UUID targetId) {
        lichtRegenHits.put(targetId, getLichtRegenHitsOn(targetId) + 1);
    }

    public void stopLichtRegen() {
        this.lichtRegenActive = false;
        this.lichtRegenTicksLeft = 0;
        this.lichtRegenFiredCount = 0;
        this.lichtRegenLook = Vec3.ZERO;
        this.lichtRegenHits.clear();
    }

    public void resetTransientState() {
        clearSprenger();
        stopLichtRegen();
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putDouble("profileMastery",mastery);
        var progress=new CompoundTag();
        for(var def:QuincyTechniques.ALL)progress.putDouble(def.id(),techniqueMasteries[def.slot()-1]);
        tag.put("masteries",progress);
        if (sprengerActive) {
            tag.putBoolean("sprengerActive", true);
            tag.put("sprengerCenter", NbtUtils.writeBlockPos(sprengerCenter));
            tag.putLong("sprengerCreationTick", sprengerCreationTick);
            ListTag list = new ListTag();
            for (BlockPos pos : sprengerVertices) {
                list.add(NbtUtils.writeBlockPos(pos));
            }
            tag.put("sprengerVertices", list);
        }
        return tag;
    }

    public void load(CompoundTag tag) {
        resetTransientState();
        mastery=clamp(tag.getDouble("profileMastery"));
        var progress=tag.getCompound("masteries");
        for(var def:QuincyTechniques.ALL)techniqueMasteries[def.slot()-1]=clamp(progress.getDouble(def.id()));
        if (tag.contains("sprengerActive") && tag.getBoolean("sprengerActive")) {
            this.sprengerActive = true;
            this.sprengerCenter = NbtUtils.readBlockPos(tag.getCompound("sprengerCenter"));
            this.sprengerCreationTick = tag.getLong("sprengerCreationTick");
            this.sprengerVertices.clear();
            ListTag list = tag.getList("sprengerVertices", Tag.TAG_COMPOUND);
            for (int i = 0; i < list.size(); i++) {
                sprengerVertices.add(NbtUtils.readBlockPos(list.getCompound(i)));
            }
        }
    }
}
