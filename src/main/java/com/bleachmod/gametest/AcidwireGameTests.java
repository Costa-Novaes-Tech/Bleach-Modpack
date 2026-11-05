package com.bleachmod.gametest;

import com.bleachmod.Reference;
import com.bleachmod.common.data.PlayerCapability;
import com.bleachmod.common.data.PlayerData;
import com.bleachmod.common.quest.*;
import com.bleachmod.entity.AcidwireEntity;
import com.bleachmod.entity.QuestNpcEntity;
import com.bleachmod.registry.ModEntities;
import com.bleachmod.server.events.QuestEvents;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;
import java.util.UUID;

@GameTestHolder("bleachmod")
@PrefixGameTestTemplate(false)
public final class AcidwireGameTests {
    private static final String KEY = RukiaOpeningQuest.ORIHIME_KEY;
    private static PlayerData data(FakePlayer p) { return PlayerCapability.get(p).orElseThrow(() -> new IllegalStateException("Missing capability")); }
    private static FakePlayer player(GameTestHelper h) {
        var p = new FakePlayer(h.getLevel(), new GameProfile(UUID.randomUUID(), "AcidwireQA"));
        data(p).initializeShinigami(); return p;
    }
    private static void stripAi(AcidwireEntity boss) {
        boss.goalSelector.removeAllGoals(g -> true); boss.targetSelector.removeAllGoals(g -> true);
        boss.setNoGravity(true);
    }
    private static AcidwireEntity boss(GameTestHelper h, FakePlayer p) {
        for (var e : h.getLevel().getAllEntities()) if (e instanceof AcidwireEntity a && a.isAlive() && !a.isRemoved()
                && KEY.equals(e.getPersistentData().getString(Reference.TAG_QUEST_KEY))
                && e.getPersistentData().hasUUID(Reference.TAG_QUEST_OWNER)
                && p.getUUID().equals(e.getPersistentData().getUUID(Reference.TAG_QUEST_OWNER))) return a;
        throw new IllegalStateException("Quest did not spawn real Acidwire");
    }

    @GameTest(template = "empty", timeoutTicks = 80)
    public static void authoredClawHitsOnlyAtWindupAndCannotRepeatDuringCooldown(GameTestHelper h) {
        var pos = h.absolutePos(new BlockPos(2, 4, 2));
        var a = ModEntities.ACIDWIRE.get().create(h.getLevel());
        a.setPos(pos.getX(), pos.getY(), pos.getZ()); stripAi(a); h.getLevel().addFreshEntity(a);
        var victim = EntityType.HUSK.create(h.getLevel()); victim.setNoAi(true); victim.setNoGravity(true);
        victim.setPos(a.getX(), a.getY(), a.getZ() + 2); h.getLevel().addFreshEntity(victim);
        a.setTarget(victim); float before = victim.getHealth();
        a.beginAttack(victim);
        h.assertTrue(a.isAttacking(), "Claw did not start");
        h.runAfterDelay(AcidwireEntity.WINDUP_TICKS - 1, () -> h.assertTrue(victim.getHealth() == before, "Claw damaged before authored impact"));
        h.runAfterDelay(AcidwireEntity.WINDUP_TICKS + 1, () -> h.assertTrue(victim.getHealth() < before, "Claw failed to damage at impact"));
        h.runAfterDelay(AcidwireEntity.ATTACK_DURATION + 1, () -> {
            h.assertTrue(!a.isAttacking() && a.getAttackCooldownTicks() > 0, "Claw did not finish into cooldown");
            a.beginAttack(victim); h.assertTrue(!a.isAttacking(), "Cooldown allowed another claw");
            a.discard(); victim.discard(); h.succeed();
        });
    }

    @GameTest(template = "empty", timeoutTicks = 80)
    public static void clawCanBeDodgedAndCancelRestoresIdle(GameTestHelper h) {
        var pos = h.absolutePos(new BlockPos(2, 4, 2));
        var a = ModEntities.ACIDWIRE.get().create(h.getLevel()); a.setPos(pos.getX(),pos.getY(),pos.getZ());
        stripAi(a); h.getLevel().addFreshEntity(a);
        var victim = EntityType.HUSK.create(h.getLevel()); victim.setNoAi(true); victim.setNoGravity(true);
        victim.setPos(a.getX(), a.getY(), a.getZ()+2); h.getLevel().addFreshEntity(victim);
        a.setTarget(victim); float before = victim.getHealth(); a.beginAttack(victim);
        victim.setPos(a.getX()+12,a.getY(),a.getZ());
        h.runAfterDelay(AcidwireEntity.WINDUP_TICKS+1, () -> {
            h.assertTrue(victim.getHealth() == before, "Dodged claw still damaged distant victim");
            a.cancelAttack(); h.assertTrue(!a.isAttacking(), "Cancel left attack active");
            a.discard(); victim.discard(); h.succeed();
        });
    }

    @GameTest(template = "empty")
    public static void realAcidwireQuestRetriesNaturalDeathAndConcludesWithOneReward(GameTestHelper h) {
        var p = player(h); var npc = ModEntities.NPC_RUKIA.get().create(h.getLevel());
        var pos = h.absolutePos(new BlockPos(1,3,1)); npc.setPos(pos.getX()+.5,pos.getY(),pos.getZ()+.5);
        npc.setQuestContext("karakura_orihime"); h.getLevel().addFreshEntity(npc); p.setPos(npc.position());
        for (int x=4;x<=14;x++) for (int z=-5;z<=7;z++) h.setBlock(new BlockPos(x,2,z),Blocks.STONE);
        data(p).getPlayerQuestData().completeQuest(RukiaOpeningQuest.KEY);
        h.assertTrue(StoryQuestService.pendingReason(QuestRegistry.getQuest(KEY)) == null, "Real Acidwire still marked pending");
        QuestService.startQuest(p, KEY); RukiaOpeningQuest.dialogue(p,npc,KEY);
        var progress=data(p).getPlayerQuestData().getProgress(KEY); var first=boss(h,p);
        var other=player(h);other.setPos(first.position());
        h.assertTrue(!first.beginAttack(other), "Quest Acidwire attacked a different player");
        UUID oldAttempt=progress.getAttemptId();
        QuestEvents.onEntityKill(new LivingDeathEvent(p,h.getLevel().damageSources().generic()));
        h.assertTrue(first.isRemoved() && RukiaOpeningQuest.isRetryPending(progress)
                && progress.getObjectiveProgress(0)==1, "Player death erased intro or retained old Acidwire");
        RukiaOpeningQuest.retry(p,KEY); var second=boss(h,p);
        QuestEvents.onEntityKill(new LivingDeathEvent(first,h.getLevel().damageSources().playerAttack(p)));
        h.assertTrue(progress.getObjectiveProgress(1)==0 && !oldAttempt.equals(progress.getAttemptId()), "Stale Acidwire credited retry");
        second.hurt(h.getLevel().damageSources().generic(),100000);
        RukiaOpeningQuest.recoverMissingEncounter(p);
        h.assertTrue(RukiaOpeningQuest.isRetryPending(progress) && progress.getObjectiveProgress(1)==0
                && progress.getObjectiveProgress(0)==1, "Environmental death stranded or completed battle");
        RukiaOpeningQuest.retry(p,KEY); var third=boss(h,p);
        third.hurt(h.getLevel().damageSources().playerAttack(p),100000);
        h.assertTrue(RukiaOpeningQuest.stage(progress)==2 && progress.getStatus()==QuestStatus.ACCEPTED, "Real boss kill did not unlock return stage");
        h.assertTrue(!QuestAvailabilityChecker.isVisible(data(p),QuestRegistry.getQuest("shinigami_substitute:3")), "Boss kill unlocked Chad before return");
        QuestEvents.onEntityKill(new LivingDeathEvent(p,h.getLevel().damageSources().generic()));
        h.assertTrue(RukiaOpeningQuest.stage(progress)==2, "Death after victory erased victory");
        int before=p.totalExperience; RukiaOpeningQuest.dialogue(p,npc,KEY); RukiaOpeningQuest.dialogue(p,npc,KEY);
        QuestService.claimReward(p,KEY,-1);
        h.assertTrue(data(p).getPlayerQuestData().isCompleted(KEY) && p.totalExperience==before+100, "Final dialogue lost or duplicated test XP");
        h.assertTrue(QuestAvailabilityChecker.isVisible(data(p),QuestRegistry.getQuest("shinigami_substitute:3")), "Final dialogue did not unlock Chad");
        npc.discard(); h.succeed();
    }
}
