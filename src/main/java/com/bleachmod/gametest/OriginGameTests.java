package com.bleachmod.gametest;

import com.bleachmod.common.data.*;
import com.bleachmod.common.visual.OriginTimeline;
import com.bleachmod.registry.ModEntities;
import net.minecraft.gametest.framework.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.gametest.*;
import java.util.UUID;

@GameTestHolder("bleachmod") @PrefixGameTestTemplate(false)
public final class OriginGameTests {
    private record Rig(net.minecraftforge.common.util.FakePlayer player,java.util.Map<net.minecraft.core.BlockPos,net.minecraft.world.level.block.state.BlockState> blocks) {
        void clear() {
            var level=player.serverLevel();
            com.bleachmod.common.story.ShinigamiOriginService.cancel(player);
            level.getEntitiesOfClass(net.minecraft.world.entity.Entity.class,player.getBoundingBox().inflate(22),e->e.getPersistentData().getBoolean(com.bleachmod.common.story.ShinigamiOriginService.TEMP)
                    ||e.getPersistentData().hasUUID(com.bleachmod.common.story.ShinigamiOriginService.OWNER)
                    &&e.getPersistentData().getUUID(com.bleachmod.common.story.ShinigamiOriginService.OWNER).equals(player.getUUID()))
                    .forEach(net.minecraft.world.entity.Entity::discard);
            blocks.forEach(level::setBlockAndUpdate);blocks.clear();
        }
    }
    private static final java.util.List<Rig> RIGS=new java.util.ArrayList<>();
    @AfterBatch(batch="origin_flow") public static void clearFlow(net.minecraft.server.level.ServerLevel level) { RIGS.forEach(Rig::clear);RIGS.clear(); }
    @AfterBatch(batch="origin_cancel") public static void clearCancel(net.minecraft.server.level.ServerLevel level) { RIGS.forEach(Rig::clear);RIGS.clear(); }
    private static Rig rig(GameTestHelper h) {
        var id=UUID.randomUUID();var p=new net.minecraftforge.common.util.FakePlayer(h.getLevel(),new com.mojang.authlib.GameProfile(id,"Origin"+id.toString().substring(0,8)));
        var at=h.absoluteVec(new net.minecraft.world.phys.Vec3(2.5,0,2.5));int y=h.getLevel().getMaxBuildHeight()-28;
        p.setPos(at.x,y,at.z);p.setYRot(0);p.setXRot(0);p.setOnGround(true);
        var originals=new java.util.LinkedHashMap<net.minecraft.core.BlockPos,net.minecraft.world.level.block.state.BlockState>();
        for(int x=-17;x<=3;x++)for(int z=-3;z<=5;z++) {
            var pos=p.blockPosition().offset(x,-1,z);originals.put(pos,h.getLevel().getBlockState(pos));
            h.getLevel().setBlockAndUpdate(pos,net.minecraft.world.level.block.Blocks.STONE.defaultBlockState());
        }
        var d=PlayerCapability.get(p).orElseThrow(IllegalStateException::new);d.initializeShinigami();
        com.bleachmod.common.story.ShinigamiOriginService.request(p,d);
        var rig=new Rig(p,originals);RIGS.add(rig);return rig;
    }
    @GameTest(template="empty",batch="origin_cancel",timeoutTicks=75)
    public static void interruptedTransferRestoresActionsAndRemovesTemporaryActors(GameTestHelper h) {
        var r=rig(h);var p=r.player;var d=PlayerCapability.get(p).orElseThrow(IllegalStateException::new);
        h.assertTrue(com.bleachmod.common.story.ShinigamiOriginService.start(p,true),"Scene did not stage on clear ground");
        h.assertTrue(!d.getStatus().allowAction(h.getLevel().getGameTime()),"Action accepted during transfer");
        h.assertTrue(!com.bleachmod.common.story.ShinigamiOriginService.start(p,true),"Duplicate origin actor accepted");
        h.runAfterDelay(20,()->{
            com.bleachmod.common.story.ShinigamiOriginService.cancel(p);
            h.assertTrue(!com.bleachmod.common.story.ShinigamiOriginService.locked(p)&&d.getOrigin().stage()==OriginData.Stage.PENDING,"Cancellation left owner locked");
            h.assertTrue(d.getStatus().allowAction(h.getLevel().getGameTime()),"Actions stayed blocked after cancellation");
            h.assertTrue(h.getLevel().getEntitiesOfClass(com.bleachmod.entity.HollowBossEntity.class,p.getBoundingBox().inflate(22)).isEmpty(),"Temporary boss remained");
            r.clear();h.succeed();
        });
    }
    @GameTest(template="empty",batch="origin_flow",timeoutTicks=300)
    public static void authoritativeSceneTransfersThenReleasesOneWoundedBoss(GameTestHelper h) {
        var r=rig(h);var p=r.player;var d=PlayerCapability.get(p).orElseThrow(IllegalStateException::new);
        h.assertTrue(com.bleachmod.common.story.ShinigamiOriginService.start(p,true),"Scene did not start");
        h.onEachTick(()->{p.tickCount++;com.bleachmod.common.story.ShinigamiOriginService.tick(new net.minecraftforge.event.TickEvent.PlayerTickEvent(net.minecraftforge.event.TickEvent.Phase.END,p));});
        h.runAfterDelay(100,()->h.assertTrue(com.bleachmod.common.story.ShinigamiOriginService.locked(p)&&!d.getPlayerQuestData().isAccepted(com.bleachmod.common.story.ShinigamiOriginService.QUEST),"Boss quest unlocked before transfer"));
        h.runAfterDelay(265,()->{
            var boss=h.getLevel().getEntity(d.getOrigin().boss());
            h.assertTrue(d.getOrigin().stage()==OriginData.Stage.FIGHT&&!com.bleachmod.common.story.ShinigamiOriginService.locked(p),"Scene did not return control");
            h.assertTrue(boss instanceof com.bleachmod.entity.HollowBossEntity b&&b.isOriginArmLost()&&!b.isNoAi(),"Combat boss missing or arm restored");
            h.assertTrue(p.getMainHandItem().is(com.bleachmod.init.ModItems.ASAUCHI.get()),"Starter blade not equipped");
            h.assertTrue(d.getPlayerQuestData().isAccepted(com.bleachmod.common.story.ShinigamiOriginService.QUEST),"Opening quest not accepted");r.clear();h.succeed();
        });
    }
    @GameTest(template="empty") public static void checkpointsSurviveCloneAndPartialSyncWithoutReplayingLegacy(GameTestHelper h) {
        var d=new PlayerData();d.initializeShinigami();
        h.assertTrue(d.getOrigin().stage()==OriginData.Stage.NONE,"Legacy initialization unexpectedly replays opening");
        d.getOrigin().request();d.getOrigin().scene();var id=UUID.randomUUID();d.getOrigin().fight(id);
        var copy=new PlayerData();copy.copyFrom(d);copy.resetTransientState();copy.load(new CompoundTag());
        h.assertTrue(copy.getOrigin().stage()==OriginData.Stage.FIGHT&&id.equals(copy.getOrigin().boss()),"Persistent fight lost on lifecycle/partial sync");
        copy.getOrigin().complete();var saved=copy.save();var restored=new PlayerData();restored.load(saved);
        h.assertTrue(restored.getOrigin().stage()==OriginData.Stage.COMPLETE,"Completed intro replayed");
        saved.remove("origin");restored.load(saved);
        h.assertTrue(restored.getOrigin().stage()==OriginData.Stage.NONE,"Legacy save starts an unsolicited boss");
        var invalid=new CompoundTag();invalid.putInt("version",1);invalid.putString("stage","FIGHT");restored.getOrigin().load(invalid);
        h.assertTrue(restored.getOrigin().stage()==OriginData.Stage.RETRY,"Missing boss UUID leaves stuck fight");h.succeed();
    }
    @GameTest(template="empty") public static void missingArmPersistsWithoutChangingNormalBosses(GameTestHelper h) {
        var b=ModEntities.HOLLOW_BOSS.get().create(h.getLevel());
        h.assertTrue(!b.isOriginArmLost(),"Normal boss spawned with missing arm");
        b.setOriginArmLost(true);var tag=new CompoundTag();b.addAdditionalSaveData(tag);
        var copy=ModEntities.HOLLOW_BOSS.get().create(h.getLevel());copy.readAdditionalSaveData(tag);
        h.assertTrue(copy.isOriginArmLost(),"Arm restored after save/reload");
        var target=net.minecraft.world.entity.EntityType.IRON_GOLEM.create(h.getLevel());
        b.getPersistentData().putUUID(com.bleachmod.common.story.ShinigamiOriginService.OWNER,UUID.randomUUID());
        h.assertTrue(!b.canAttack(target),"Personal boss can attack an unrelated NPC");h.succeed();
    }
    @GameTest(template="empty") public static void transferPrecedesArmLossAndEveryCameraIsFinite(GameTestHelper h) {
        h.assertTrue(OriginTimeline.THRUST<OriginTimeline.BURST&&OriginTimeline.BURST<OriginTimeline.CUT&&OriginTimeline.CUT<OriginTimeline.END,"Story beats reordered");
        for(int t=0;t<=OriginTimeline.END;t++) {
            var s=OriginTimeline.shot(t);
            h.assertTrue(Double.isFinite(s.eye().lengthSqr())&&s.eye().distanceToSqr(s.target())>.01&&s.fov()>=30&&s.fov()<=80,"Invalid directed camera at "+t);
        }
        h.assertTrue(OriginTimeline.bossDistance(OriginTimeline.CUT)<OriginTimeline.bossDistance(OriginTimeline.BURST),"Hollow does not advance toward scene");h.succeed();
    }
    @GameTest(template="empty") public static void approachKeepsRukiaSeatedAndThreatOnPlayersRight(GameTestHelper h) {
        h.assertTrue(OriginTimeline.rukiaDistance(0)==OriginTimeline.rukiaDistance(OriginTimeline.END),"Rukia walks into the player");
        h.assertTrue(OriginTimeline.playerAdvance(120)==0&&OriginTimeline.playerAdvance(OriginTimeline.THRUST)>1,"Player does not approach the blade");
        for(int t=0;t<=OriginTimeline.END;t++) {
            var boss=OriginTimeline.bossOffset(t);
            h.assertTrue(boss.x<0&&Math.abs(boss.z-.8)<.001,"Fishbone enters behind Rukia instead of from her left");
        }
        h.assertTrue(OriginTimeline.playerAdvance(OriginTimeline.THRUST)==OriginTimeline.playerAdvance(OriginTimeline.END),"Player drifts after contacting blade");h.succeed();
    }
}
