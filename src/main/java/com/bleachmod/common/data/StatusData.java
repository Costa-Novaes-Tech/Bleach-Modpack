package com.bleachmod.common.data;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class StatusData {
    private int benihimePathUses;
    public int getBenihimePathCharges(){if(benihimePathUses>=3 && techniqueSlot4CooldownTicks==0)benihimePathUses=0;return 3-benihimePathUses;}
    public void useBenihimePath(){if(!cooldownsDisabled)benihimePathUses=Math.min(3,benihimePathUses+1);}
    private CompoundTag benihimeHud=new CompoundTag();
    public CompoundTag getBenihimeHud(){return benihimeHud.copy();}
    public void readBenihimeHud(CompoundTag tag){benihimeHud=new CompoundTag();for(var key:java.util.List.of("shield","net","charges","path","pathCharges"))if(tag.contains(key))benihimeHud.putInt(key,Math.max(0,Math.min(100,tag.getInt(key))));}
    private CompoundTag senbonHud = new CompoundTag();
    public CompoundTag getSenbonHud() { return senbonHud.copy(); }
    public void readSenbonHud(CompoundTag tag) {
        senbonHud = new CompoundTag();
        for (String key : java.util.List.of("fieldReady","fieldRemaining","sceneReady","sceneRemaining","ward","prison"))
            if (tag.contains(key)) senbonHud.putInt(key, Math.max(0, Math.min(200,tag.getInt(key))));
        senbonHud.putBoolean("bloom",tag.getBoolean("bloom"));
    }
    private net.minecraft.world.phys.Vec3 dodgeDirection = net.minecraft.world.phys.Vec3.ZERO;
    private int dodgeTicks;
    private boolean dodging;
    private long nextDodgeTick = Long.MIN_VALUE;
    public boolean canDodge(long tick) { return !dodging && tick >= nextDodgeTick; }
    public boolean isDodging() { return dodging; }
    public int getDodgeTicks() { return dodgeTicks; }
    public net.minecraft.world.phys.Vec3 getDodgeDirection() { return dodgeDirection; }
    public void beginDodge(net.minecraft.world.phys.Vec3 direction, long tick) {
        dodgeDirection = direction;
        dodgeTicks = com.bleachmod.common.DodgePolicy.DURATION;
        dodging = true;
        nextDodgeTick = tick + com.bleachmod.common.DodgePolicy.INTERVAL;
    }
    public void advanceDodge() { if (dodgeTicks > 0) dodgeTicks--; }
    public void stopDodge() { dodging = false; dodgeTicks = 0; dodgeDirection = net.minecraft.world.phys.Vec3.ZERO; }
    private net.minecraft.world.phys.Vec3 staminaPosition;
    public boolean sampleStaminaMovement(net.minecraft.world.phys.Vec3 position) {
        var previous = staminaPosition; staminaPosition = position;
        if (previous == null) return false;
        double dx = position.x - previous.x, dz = position.z - previous.z;
        return dx * dx + dz * dz > .0001;
    }
    public static final int COMBAT_IDLE_TICKS = 200;
    private int combatTicks, hudCombatTicks;
    public int getCombatTicks() { return combatTicks; }
    public boolean isInCombat() { return combatTicks > 0; }
    public boolean isHudInCombat() { return hudCombatTicks > 0; }
    public void refreshCombat(int ticks) { combatTicks = Math.max(0, Math.min(COMBAT_IDLE_TICKS, ticks)); }
    public void clearCombat() { combatTicks = 0; hudCombatTicks = 0; }
    private long lastActionTick = Long.MIN_VALUE;
    private long lastUiActionTick = Long.MIN_VALUE;
    private boolean cooldownsDisabled;
    private int[] hudCooldowns = new int[4];
    private boolean hudCooldownsDisabled;

    /** Separate owner-only network snapshot, never written by save(). */
    public CompoundTag techniqueHud() {
        CompoundTag tag = new CompoundTag();
        tag.putIntArray("cooldowns", new int[]{techniqueSlot1CooldownTicks, techniqueSlot2CooldownTicks,
                techniqueSlot3CooldownTicks, techniqueSlot4CooldownTicks});
        tag.putBoolean("disabled", cooldownsDisabled);
        tag.putInt("combatTicks", combatTicks);
        return tag;
    }
    public void readTechniqueHud(CompoundTag tag) {
        int[] values = tag.getIntArray("cooldowns");
        if (values.length != 4) return;
        hudCooldowns = java.util.Arrays.stream(values).map(value -> Math.max(0, value)).toArray();
        hudCooldownsDisabled = tag.getBoolean("disabled");
        hudCombatTicks = Math.max(0, Math.min(COMBAT_IDLE_TICKS, tag.getInt("combatTicks")));
    }
    public int[] getHudCooldowns() { return hudCooldowns.clone(); }
    public boolean areHudCooldownsDisabled() { return hudCooldownsDisabled; }

    public boolean areCooldownsDisabled() { return cooldownsDisabled; }
    public void setCooldownsDisabled(boolean disabled) {
        cooldownsDisabled = disabled;
        clearCooldowns();
    }
    public void clearCooldowns() {
        techniqueSlot1CooldownTicks = 0;
        techniqueSlot2CooldownTicks = 0;
        techniqueSlot3CooldownTicks = 0;
        techniqueSlot4CooldownTicks = 0; benihimePathUses=0;
    }
    private int techniqueSlot1CooldownTicks;
    private int techniqueSlot2CooldownTicks;
    private int techniqueSlot3CooldownTicks;
    private int techniqueSlot4CooldownTicks;
    private int flameDashTicks;
    private boolean ignitionActive;
    private final Set<UUID> flameDashHitEntities = new HashSet<>();
    private final Set<BlockPos> flameBarrageGroundPositions = new HashSet<>();
    private int flameBarrageGroundTicks;
    private final Set<BlockPos> flameFanGroundPositions = new HashSet<>();
    private int flameFanGroundTicks;

    public boolean allowAction(long tick) {
        if (lastActionTick != Long.MIN_VALUE && tick >= lastActionTick && tick - lastActionTick < 4) return false;
        lastActionTick = tick;
        return true;
    }

    /** Menu requests (forms, quests, attributes, skills) have their own window so combat never swallows them. */
    public boolean allowUiAction(long tick) {
        if (lastUiActionTick != Long.MIN_VALUE && tick >= lastUiActionTick && tick - lastUiActionTick < 4) return false;
        lastUiActionTick = tick;
        return true;
    }

    private boolean hasCreatedCharacter;
    private boolean actionCharging;

    public boolean hasCreatedCharacter() {
        return hasCreatedCharacter;
    }

    public void setHasCreatedCharacter(boolean hasCreatedCharacter) {
        this.hasCreatedCharacter = hasCreatedCharacter;
    }

    public boolean isActionCharging() {
        return actionCharging;
    }

    public void setActionCharging(boolean actionCharging) {
        this.actionCharging = actionCharging;
    }

    public boolean isIgnitionActive() {
        return ignitionActive;
    }

    public void setIgnitionActive(boolean active) {
        ignitionActive = active;
    }

    public int getTechniqueSlot1CooldownTicks() {
        return techniqueSlot1CooldownTicks;
    }

    public void setTechniqueSlot1CooldownTicks(int ticks) {
        techniqueSlot1CooldownTicks = cooldownsDisabled ? 0 : Math.max(0, ticks);
    }

    public int getTechniqueSlot2CooldownTicks() {
        return techniqueSlot2CooldownTicks;
    }

    public void setTechniqueSlot2CooldownTicks(int ticks) {
        techniqueSlot2CooldownTicks = cooldownsDisabled ? 0 : Math.max(0, ticks);
    }

    public int getTechniqueSlot4CooldownTicks() {
        return techniqueSlot4CooldownTicks;
    }

    public int getTechniqueSlot3CooldownTicks() {
        return techniqueSlot3CooldownTicks;
    }

    public void setTechniqueSlot3CooldownTicks(int ticks) {
        techniqueSlot3CooldownTicks = cooldownsDisabled ? 0 : Math.max(0, ticks);
    }

    /** Changing form cancels active effects but must not allow a cooldown bypass. */
    public void clearTransformationState() {
        int combat = combatTicks, hudCombat = hudCombatTicks;
        long dodgeCooldown = nextDodgeTick;
        int slot1 = techniqueSlot1CooldownTicks;
        int slot2 = techniqueSlot2CooldownTicks;
        int slot3 = techniqueSlot3CooldownTicks;
        int slot4 = techniqueSlot4CooldownTicks, pathUses=benihimePathUses;
        clearTransientState();
        nextDodgeTick = dodgeCooldown;
        combatTicks = combat; hudCombatTicks = hudCombat;
        techniqueSlot1CooldownTicks = slot1;
        techniqueSlot2CooldownTicks = slot2;
        techniqueSlot3CooldownTicks = slot3;
        techniqueSlot4CooldownTicks = slot4; benihimePathUses=pathUses;
    }

    public void setTechniqueSlot4CooldownTicks(int ticks) {
        techniqueSlot4CooldownTicks = cooldownsDisabled ? 0 : Math.max(0, ticks);
    }

    public int getFlameBarrageGroundTicks() {
        return flameBarrageGroundTicks;
    }

    public void setFlameBarrageGroundTicks(int ticks) {
        flameBarrageGroundTicks = Math.max(0, ticks);
        if (flameBarrageGroundTicks == 0) {
            flameBarrageGroundPositions.clear();
        }
    }

    public void setFlameBarrageGroundPositions(Set<BlockPos> positions, int ticks) {
        flameBarrageGroundPositions.clear();
        flameBarrageGroundPositions.addAll(positions);
        flameBarrageGroundTicks = Math.max(0, ticks);
    }

    public Set<BlockPos> getFlameBarrageGroundPositions() {
        return Set.copyOf(flameBarrageGroundPositions);
    }

    public int getFlameFanGroundTicks() {
        return flameFanGroundTicks;
    }

    public void setFlameFanGroundTicks(int ticks) {
        flameFanGroundTicks = Math.max(0, ticks);
        if (flameFanGroundTicks == 0) {
            flameFanGroundPositions.clear();
        }
    }

    public void setFlameFanGroundPositions(Set<BlockPos> positions, int ticks) {
        flameFanGroundPositions.clear();
        flameFanGroundPositions.addAll(positions);
        flameFanGroundTicks = Math.max(0, ticks);
    }

    public Set<BlockPos> getFlameFanGroundPositions() {
        return Set.copyOf(flameFanGroundPositions);
    }

    public int getFlameDashTicks() {
        return flameDashTicks;
    }

    public void setFlameDashTicks(int ticks) {
        flameDashTicks = Math.max(0, ticks);
    }

    public void beginFlameDash() {
        flameDashHitEntities.clear();
    }

    public boolean markFlameDashHit(UUID entityId) {
        return flameDashHitEntities.add(entityId);
    }

    public void clearFlameDashHits() {
        flameDashHitEntities.clear();
    }

    /** Clears technique test state without changing form, reiatsu, ignition or progression. */
    public void clearTechniqueTestState() {
        lastActionTick = Long.MIN_VALUE;
        lastUiActionTick = Long.MIN_VALUE;
        techniqueSlot1CooldownTicks = 0;
        techniqueSlot2CooldownTicks = 0;
        techniqueSlot3CooldownTicks = 0;
        techniqueSlot4CooldownTicks = 0; benihimePathUses=0;
        flameDashTicks = 0;
        flameDashHitEntities.clear();
        flameBarrageGroundTicks = 0;
        flameBarrageGroundPositions.clear();
        flameFanGroundTicks = 0;
        flameFanGroundPositions.clear();
    }

    public void tickTransientState() {
        if (combatTicks > 0) combatTicks--;
        if (techniqueSlot3CooldownTicks > 0) {
            techniqueSlot3CooldownTicks--;
        }

        if (techniqueSlot1CooldownTicks > 0) {
            techniqueSlot1CooldownTicks--;
        }
        if (techniqueSlot2CooldownTicks > 0) {
            techniqueSlot2CooldownTicks--;
        }
        if (techniqueSlot4CooldownTicks > 0) {
            techniqueSlot4CooldownTicks--;
        }
        if (flameBarrageGroundTicks > 0) {
            flameBarrageGroundTicks--;
            if (flameBarrageGroundTicks == 0) {
                flameBarrageGroundPositions.clear();
            }
        }
        if (flameFanGroundTicks > 0) {
            flameFanGroundTicks--;
            if (flameFanGroundTicks == 0) {
                flameFanGroundPositions.clear();
            }
        }
    }

    public void clearTransientState() {
        stopDodge();
        nextDodgeTick = Long.MIN_VALUE;
        staminaPosition = null;
        clearCombat();
        techniqueSlot1CooldownTicks = 0;
        techniqueSlot2CooldownTicks = 0;
        techniqueSlot3CooldownTicks = 0;
        techniqueSlot4CooldownTicks = 0; benihimePathUses=0;
        flameBarrageGroundTicks = 0;
        flameBarrageGroundPositions.clear();
        flameFanGroundTicks = 0;
        flameFanGroundPositions.clear();
        flameDashTicks = 0;
        ignitionActive = false;
        actionCharging = false;
        flameDashHitEntities.clear();
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("hasCreatedCharacter", hasCreatedCharacter);
        tag.putBoolean("actionCharging", actionCharging);
        return tag;
    }

    public void load(CompoundTag tag) {
        stopDodge();
        nextDodgeTick = Long.MIN_VALUE;
        staminaPosition = null;
        clearCombat();
        cooldownsDisabled = false;
        if (tag.contains("hasCreatedCharacter")) {
            hasCreatedCharacter = tag.getBoolean("hasCreatedCharacter");
        }
        if (tag.contains("actionCharging")) {
            actionCharging = tag.getBoolean("actionCharging");
        }
        // Cooldowns, contact targets and temporary flame visuals are intentionally transient and are not loaded from NBT.
        techniqueSlot1CooldownTicks = 0;
        techniqueSlot2CooldownTicks = 0;
        techniqueSlot3CooldownTicks = 0;
        techniqueSlot4CooldownTicks = 0; benihimePathUses=0;
        flameBarrageGroundTicks = 0;
        flameBarrageGroundPositions.clear();
        flameFanGroundTicks = 0;
        flameFanGroundPositions.clear();
        flameDashTicks = 0;
        flameDashHitEntities.clear();
    }
}
