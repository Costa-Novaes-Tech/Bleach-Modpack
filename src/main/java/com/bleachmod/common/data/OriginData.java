package com.bleachmod.common.data;
import net.minecraft.nbt.CompoundTag;
import java.util.UUID;
/** Personal checkpoint; transient actors never replace a saved conquest. */
public final class OriginData {
    public enum Stage { NONE, PENDING, CINEMATIC, FIGHT, RETRY, COMPLETE }
    private Stage stage = Stage.NONE;
    private UUID boss;
    private net.minecraft.core.BlockPos bossPosition;
    public Stage stage() { return stage; }
    public UUID boss() { return boss; }
    public net.minecraft.core.BlockPos bossPosition() { return bossPosition; }
    public void rememberBoss(net.minecraft.core.BlockPos position) { bossPosition=position.immutable(); }
    public void request() { stage = Stage.PENDING; boss = null;bossPosition=null; }
    public void scene() { stage = Stage.CINEMATIC; boss = null;bossPosition=null; }
    public void fight(UUID id) { stage = Stage.FIGHT; boss = id;bossPosition=null; }
    public void retry() { stage = Stage.RETRY; boss = null;bossPosition=null; }
    public void complete() { stage = Stage.COMPLETE; boss = null;bossPosition=null; }
    public CompoundTag save() {
        var tag = new CompoundTag(); tag.putInt("version", 1); tag.putString("stage", stage.name());
        if (boss != null) tag.putUUID("boss", boss);
        if(bossPosition!=null)tag.putLong("bossPosition",bossPosition.asLong());
        return tag;
    }
    public void load(CompoundTag tag) {
        stage = Stage.NONE; boss = null;bossPosition=null;
        if (tag.getInt("version") != 1) return;
        try { stage = Stage.valueOf(tag.getString("stage")); } catch (IllegalArgumentException ignored) { }
        if (stage==Stage.FIGHT&&tag.hasUUID("boss")) boss = tag.getUUID("boss");
        if(stage==Stage.FIGHT&&tag.contains("bossPosition"))bossPosition=net.minecraft.core.BlockPos.of(tag.getLong("bossPosition"));
        if (stage == Stage.FIGHT && boss == null) stage = Stage.RETRY;
    }
}
