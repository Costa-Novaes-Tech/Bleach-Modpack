package com.bleachmod.common.data;

import com.bleachmod.Reference;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;

public class ResourcesData {
    private float stamina = com.bleachmod.common.StaminaPolicy.MAX;
    private int staminaRestTicks;
    private boolean staminaExhausted;
    public float getStamina() { return stamina; }
    public boolean isStaminaExhausted() { return staminaExhausted; }
    public void fillStamina() { stamina = com.bleachmod.common.StaminaPolicy.MAX; staminaRestTicks = 0; staminaExhausted = false; }
    /** Returns false when an attempted sprint cannot be sustained. No reiatsu or progression mutation. */
    public boolean tickStamina(boolean effort) {
        if (effort && !staminaExhausted) {
            stamina = Math.max(0, stamina - com.bleachmod.common.StaminaPolicy.SPRINT_PER_TICK);
            staminaRestTicks = com.bleachmod.common.StaminaPolicy.REST_TICKS;
            if (stamina == 0) staminaExhausted = true;
            return !staminaExhausted;
        }
        if (staminaRestTicks > 0) staminaRestTicks--;
        else stamina = Math.min(com.bleachmod.common.StaminaPolicy.MAX, stamina + com.bleachmod.common.StaminaPolicy.REGEN_PER_TICK);
        if (stamina >= com.bleachmod.common.StaminaPolicy.RESUME) staminaExhausted = false;
        return !effort || !staminaExhausted;
    }
    private float currentReiatsu = Reference.BASE_REIATSU;
    private float maxReiatsu = Reference.BASE_REIATSU;
    private int actionCharge;
    private float trainingPoints;
    // Operator test override; deliberately absent from saved/player sync data.
    private boolean costsDisabled;

    public boolean areCostsDisabled() { return costsDisabled; }
    public void setCostsDisabled(boolean disabled) { costsDisabled = disabled; }
    public boolean canAffordReiatsu(float amount) {
        return Float.isFinite(amount) && amount >= 0 && (costsDisabled || currentReiatsu >= amount);
    }

    public float getCurrentReiatsu() {
        return currentReiatsu;
    }

    public void setCurrentReiatsu(float value) {
        this.currentReiatsu = Mth.clamp(value, 0.0F, maxReiatsu);
    }

    public void addReiatsu(float amount) {
        if (costsDisabled && amount < 0) return;
        setCurrentReiatsu(currentReiatsu + amount);
    }

    public boolean consumeReiatsu(float amount) {
        if (!canAffordReiatsu(amount)) {
            return false;
        }
        if (!costsDisabled) setCurrentReiatsu(currentReiatsu - amount);
        return true;
    }

    public float getMaxReiatsu() {
        return maxReiatsu;
    }

    public boolean isReiatsuFull() {
        return currentReiatsu >= maxReiatsu;
    }

    public void fillReiatsu() {
        currentReiatsu = maxReiatsu;
    }

    public void setMaxReiatsu(float maxReiatsu) {
        this.maxReiatsu = Math.max(1.0F, maxReiatsu);
        setCurrentReiatsu(currentReiatsu);
    }

    public int getActionCharge() {
        return actionCharge;
    }

    public void setActionCharge(int actionCharge) {
        this.actionCharge = Mth.clamp(actionCharge, 0, 100);
    }

    public void addActionCharge(int amount) {
        setActionCharge(actionCharge + amount);
    }

    public float getTrainingPoints() {
        return trainingPoints;
    }

    public void addTrainingPoints(float amount) {
        if (!Float.isFinite(amount) || amount < 0) throw new IllegalArgumentException("Invalid points");
        this.trainingPoints = Math.min(1_000_000F, trainingPoints + amount);
    }

    public boolean consumeTrainingPoints(float amount) {
        if (!Float.isFinite(amount) || amount < 0 || trainingPoints < amount) {
            return false;
        }
        trainingPoints -= amount;
        return true;
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putFloat("stamina", stamina);
        tag.putInt("staminaRestTicks", staminaRestTicks);
        tag.putBoolean("staminaExhausted", staminaExhausted);
        tag.putFloat("currentReiatsu", currentReiatsu);
        tag.putFloat("maxReiatsu", maxReiatsu);
        tag.putInt("actionCharge", actionCharge);
        tag.putFloat("trainingPoints", trainingPoints);
        return tag;
    }

    public void load(CompoundTag tag) {
        costsDisabled = false;
        if (tag.contains("stamina")) {
            float value = tag.getFloat("stamina");
            stamina = Float.isFinite(value) ? Mth.clamp(value, 0, com.bleachmod.common.StaminaPolicy.MAX) : 0;
            staminaRestTicks = Mth.clamp(tag.getInt("staminaRestTicks"), 0, com.bleachmod.common.StaminaPolicy.REST_TICKS);
            staminaExhausted = stamina == 0 || (tag.getBoolean("staminaExhausted") && stamina < com.bleachmod.common.StaminaPolicy.RESUME);
        }
        if (tag.contains("currentReiatsu")) {
            currentReiatsu = tag.getFloat("currentReiatsu");
        }
        if (tag.contains("maxReiatsu")) {
            maxReiatsu = tag.getFloat("maxReiatsu");
        }
        if (tag.contains("actionCharge")) {
            actionCharge = tag.getInt("actionCharge");
        }
        if (tag.contains("trainingPoints")) {
            trainingPoints = tag.getFloat("trainingPoints");
        }
        maxReiatsu = Float.isFinite(maxReiatsu) ? Math.max(1, maxReiatsu) : 100;
        currentReiatsu = Float.isFinite(currentReiatsu) ? currentReiatsu : 0;
        trainingPoints = Float.isFinite(trainingPoints) ? Mth.clamp(trainingPoints, 0, 1_000_000) : 0;
        setActionCharge(actionCharge);
        setCurrentReiatsu(currentReiatsu);
    }
}
