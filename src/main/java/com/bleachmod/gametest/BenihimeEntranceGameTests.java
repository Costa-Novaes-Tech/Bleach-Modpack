package com.bleachmod.gametest;

import com.bleachmod.common.data.PlayerCapability;
import com.bleachmod.common.data.PlayerData;
import com.bleachmod.common.technique.BenihimeEntranceService;
import com.bleachmod.common.technique.ReleaseAnimationService;
import com.bleachmod.common.visual.BenihimeEntranceGeometry;
import com.bleachmod.common.visual.BenihimeFollowMotion;
import com.bleachmod.common.visual.BenihimePlayerPose;
import com.bleachmod.init.ModItems;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;
import java.util.List;
import java.util.UUID;

/** Server contract and numerical geometry checks; these do not approve client pixels. */
@GameTestHolder("bleachmod") @PrefixGameTestTemplate(false)
public final class BenihimeEntranceGameTests {
    private static PlayerData data(FakePlayer p) {
        return PlayerCapability.get(p).orElseThrow(() -> new IllegalStateException("Missing capability"));
    }
    private static FakePlayer player(GameTestHelper h) {
        var p = new FakePlayer(h.getLevel(), new GameProfile(UUID.randomUUID(), "EntranceQA"));
        var pos = h.absolutePos(new BlockPos(2, 2, 2));
        p.setPos(pos.getX() + .5, h.getLevel().getMaxBuildHeight() - 110, pos.getZ() + .5);
        p.setYRot(0); p.setNoGravity(true); p.setOnGround(true);
        h.getLevel().getChunkAt(p.blockPosition());
        var d = data(p); d.initializeShinigami(); d.getCharacter().bindZanpakuto("benihime");
        d.getSkills().get("zanpakuto").setLevel(2);
        for (String form : List.of("shikai", "bankai")) {
            d.getCharacter().unlockForm(form); d.getCharacter().setMastery("zanpakuto", form, 100);
        }
        p.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.BENIHIME.get()));
        return p;
    }
    private static void tickScene(FakePlayer p) {
        BenihimeEntranceService.tick(new TickEvent.PlayerTickEvent(TickEvent.Phase.END, p));
    }
    private static void clean(FakePlayer p) {
        ReleaseAnimationService.cancel(p); BenihimeEntranceService.cancel(p);
    }
    @GameTest(template="empty", batch="benihime_entrance_commit", timeoutTicks=90)
    public static void presentationOutlivesReleaseWithoutChangingCommitOrChargingTwice(GameTestHelper h) {
        var p = player(h); var d = data(p);
        ReleaseAnimationService.begin(p, d, "zanpakuto", "bankai", 7);
        h.assertTrue(ReleaseAnimationService.snapshot(p).getInt("duration") == 60
                && BenihimeEntranceService.snapshot(p).getInt("duration") == 560, "Visual and gameplay clocks conflated");
        // Forge subscriber discovery must not decide whether the scene survives commit tick 40.
        h.onEachTick(() -> { tickScene(p); ReleaseAnimationService.tick(p); });
        h.runAfterDelay(39, () -> h.assertTrue(d.getCharacter().getActiveForm().equals("sealed")
                && d.getResources().getCurrentReiatsu() == 100, "Bankai committed or charged before tick 40"));
        h.runAfterDelay(40, () -> {
            ReleaseAnimationService.tick(p);
            h.assertTrue(d.getCharacter().getActiveForm().equals("bankai")
                    && d.getResources().getCurrentReiatsu() == 93, "Tick-40 commit changed");
        });
        h.runAfterDelay(65, () -> {
            try {
                h.assertTrue(!ReleaseAnimationService.busy(p), "Visual scene prolonged gameplay action lock");
                h.assertTrue(!BenihimeEntranceService.snapshot(p).isEmpty(), "Entrance ended with short release");
                h.assertTrue(d.getResources().getCurrentReiatsu() == 93, "Presentation charged another cost");
            } finally { clean(p); }
            h.succeed();
        });
    }
    @GameTest(template="empty", batch="benihime_entrance_cleanup")
    public static void cancellationEquipmentMovementAndDimensionRemovePresentation(GameTestHelper h) {
        for (int scenario = 0; scenario < 5; scenario++) {
            var p = player(h); var d = data(p);
            try {
                ReleaseAnimationService.begin(p, d, "zanpakuto", "bankai", 7);
                h.assertTrue(!BenihimeEntranceService.snapshot(p).isEmpty(), "Entrance did not start");
                switch (scenario) {
                    case 0 -> ReleaseAnimationService.cancel(p);
                    case 1 -> { p.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY); ReleaseAnimationService.tick(p); }
                    case 2 -> p.setPos(p.position().add(2, 0, 0));
                    case 3 -> p.setServerLevel(p.getServer().getLevel(Level.NETHER));
                    case 4 -> { p.setHealth(0); ReleaseAnimationService.tick(p); }
                }
                tickScene(p);
                h.assertTrue(BenihimeEntranceService.snapshot(p).isEmpty(), "Presentation leaked after invalidation " + scenario);
                h.assertTrue(d.getResources().getCurrentReiatsu() == 100
                        && d.getCharacter().getActiveForm().equals("sealed"), "Visual cleanup mutated gameplay " + scenario);
            } finally { clean(p); }
        }
        h.succeed();
    }
    @GameTest(template="empty", batch="benihime_entrance_snapshot", timeoutTicks=40)
    public static void lateSnapshotKeepsOriginalAnchorAndYawWithoutPersistence(GameTestHelper h) {
        var p = player(h); var d = data(p); var anchor = p.position();
        ReleaseAnimationService.begin(p, d, "zanpakuto", "bankai", 7);
        p.setPos(anchor.add(.4, 0, .3)); p.setYRot(110);
        h.runAfterDelay(20, () -> {
            try {
                tickScene(p); var view = BenihimeEntranceService.snapshot(p);
                h.assertTrue(view.getInt("elapsed") >= 20 && view.getInt("elapsed") < 40, "Late observer lost scene age");
                h.assertTrue(new Vec3(view.getDouble("x"), view.getDouble("y"), view.getDouble("z")).equals(anchor)
                        && view.getFloat("yaw") == 0, "Mouse yaw or small movement dragged the scene");
                h.assertTrue(!d.save().contains("benihimeEntrance"), "Presentation leaked into character persistence");
            } finally { clean(p); }
            h.succeed();
        });
    }
    @GameTest(template="empty", batch="benihime_entrance_geometry", timeoutTicks=100)
    public static void curtainStaysClosedAndArticulatedGeometryRemainsFinite(GameTestHelper h) {
        for (int tick = 0; tick <= 560; tick++) {
            var rig = BenihimeEntranceGeometry.sample(tick);
            for (var arm : List.of(rig.left(), rig.right())) {
                h.assertTrue(Double.isFinite(arm.shoulder().lengthSqr()) && Double.isFinite(arm.elbow().lengthSqr())
                        && Double.isFinite(arm.wrist().lengthSqr()), "Invalid arm anchor at " + tick);
                h.assertTrue(Math.abs(arm.shoulder().distanceTo(arm.elbow()) - BenihimeEntranceGeometry.UPPER_ARM) < 1e-8
                        && Math.abs(arm.elbow().distanceTo(arm.wrist()) - BenihimeEntranceGeometry.FOREARM) < 1e-8,
                        "Arm changes length while reaching or rising at " + tick);
                if (tick >= 490) h.assertTrue(arm.elbow().y < arm.shoulder().y - BenihimeEntranceGeometry.UPPER_ARM*.7
                        && arm.wrist().y > arm.elbow().y + BenihimeEntranceGeometry.FOREARM*.2,
                        "Settled arms are horizontal instead of relaxed beside torso at " + tick);
            }
            if (tick <= 139) for (double height : new double[]{0, 4.6, 12})
                h.assertTrue(BenihimeEntranceGeometry.curtainGap(tick, height) <= 0, "Curtain revealed an early slit at " + tick);
            if (tick % 8 != 0 && tick != 139 && tick != 176 && tick != 236 && tick != 559) continue;
            for (var quad : BenihimeEntranceGeometry.build(new Vec3(3, 200, -7), new Vec3(.7, .2, .7), tick))
                for (var vertex : List.of(quad.a(), quad.b(), quad.c(), quad.d()))
                    h.assertTrue(Double.isFinite(vertex.lengthSqr()), "Non-finite render vertex at " + tick);
        }
        h.assertTrue(BenihimeEntranceGeometry.curtainGap(200, 4.6) > 0, "Grasp never opens curtain");
        h.assertTrue(BenihimeEntranceGeometry.sample(340).waist().z < BenihimeEntranceGeometry.sample(240).waist().z - 3,
                "Crawl failed to move body towards player");
        h.assertTrue(BenihimeEntranceGeometry.sample(450).head().y > BenihimeEntranceGeometry.sample(340).head().y + 2,
                "Rise failed to lift torso and head");
        var origin = new Vec3(3, 200, -7); var forward = new Vec3(0, 0, 1);
        h.assertTrue(BenihimeEntranceGeometry.build(origin, forward, 560)
                .equals(BenihimeEntranceGeometry.buildIdle(origin, forward, 0, 0)),
                "End of entrance changes silhouette abruptly on idle handoff");
        h.succeed();
    }
    @GameTest(template="empty", batch="benihime_follow_motion")
    public static void followingPreservesDirectionAcrossHalfTurnAndRenderRates(GameTestHelper h) {
        var start = new BenihimeFollowMotion.Pose(Vec3.ZERO, 170);
        var target = new Vec3(12, 2, -7);
        var oneFrame = BenihimeFollowMotion.advance(start, target, -170, 10);
        var manyFrames = start;
        for (int i = 0; i < 20; i++) manyFrames = BenihimeFollowMotion.advance(manyFrames, target, -170, .5);
        h.assertTrue(oneFrame.anchor().distanceTo(manyFrames.anchor()) < 1e-8
                && Math.abs(net.minecraft.util.Mth.wrapDegrees(oneFrame.yaw() - manyFrames.yaw())) < .001,
                "Following changes speed with render-frame count");
        h.assertTrue(oneFrame.anchor().distanceTo(target) < start.anchor().distanceTo(target)
                && oneFrame.anchor().distanceTo(target) > .01, "Ordinary following teleports or fails to move");
        var opposite = BenihimeFollowMotion.advance(new BenihimeFollowMotion.Pose(Vec3.ZERO, 0), Vec3.ZERO, 180, 4);
        h.assertTrue(Double.isFinite(opposite.forward().lengthSqr()) && opposite.forward().lengthSqr() > .999,
                "A half-turn collapses the visual facing vector");
        h.assertTrue(net.minecraft.util.Mth.wrapDegrees(oneFrame.yaw() - start.yaw()) > 0,
                "Yaw took the long path across the -180/180 boundary");
        var teleport = new Vec3(100, 20, -100);
        var reset = BenihimeFollowMotion.advance(oneFrame, teleport, 35, .1);
        h.assertTrue(reset.anchor().equals(teleport) && reset.yaw() == 35, "Teleport leaves manifestation interpolating across world");
        h.assertTrue(BenihimeFollowMotion.advance(oneFrame, target, -170, 0).equals(oneFrame),
                "Zero elapsed time moves manifestation");
        h.succeed();
    }
    @GameTest(template="empty", batch="benihime_player_pose")
    public static void standingPreservesHeightAndBlendsTwoHandPoseWithSmoothRecovery(GameTestHelper h) {
        h.assertTrue(BenihimePlayerPose.weight(0)==0 && BenihimePlayerPose.weight(550)==0
                && BenihimePlayerPose.weight(560)==0,"Player pose does not return to neutral");
        h.assertTrue(BenihimePlayerPose.weight(30)==1 && BenihimePlayerPose.weight(500)==1,
                "Standing grip is not maintained through the reveal");
        double previous=0;
        for(int step=0;step<=2240;step++){
            double age=step/4.0,w=BenihimePlayerPose.weight(age);
            h.assertTrue(BenihimePlayerPose.bodyOffset(age)==0 && BenihimePlayerPose.faceHeight(age)==1.62,
                    "Standing transformation lowers player or camera target at "+age);
            h.assertTrue(Double.isFinite(w)&&w>=0&&w<=1&&Math.abs(w-previous)<.02,
                    "Standing grip snaps or leaves blend range at "+age);previous=w;
        }
        for(double boundary:new double[]{20,520,550}){
            double before=BenihimePlayerPose.weight(boundary-.01),at=BenihimePlayerPose.weight(boundary),after=BenihimePlayerPose.weight(boundary+.01);
            h.assertTrue(Math.abs(before-at)<.001&&Math.abs(after-at)<.001
                    && Math.abs((at-before)/.01-(after-at)/.01)<.002,
                    "Standing grip has a velocity discontinuity at "+boundary);
        }
        h.succeed();
    }
    @GameTest(template="empty",batch="benihime_terrain_rig")
    public static void unevenSupportsKeepArmLengthsAndDollProportions(GameTestHelper h){
        h.assertTrue(BenihimeEntranceGeometry.UPPER_ARM+BenihimeEntranceGeometry.FOREARM<6.5,"Arms retain the old oversized span");
        for(var ground:java.util.List.<BenihimeEntranceGeometry.Ground>of((x,z)->0,(x,z)->x<0?1:-.5,(x,z)->1.5)){
            for(int tick=240;tick<=560;tick++){
                var rig=BenihimeEntranceGeometry.sample(tick,ground);
                for(var arm:java.util.List.of(rig.left(),rig.right())){
                    h.assertTrue(Math.abs(arm.shoulder().distanceTo(arm.elbow())-BenihimeEntranceGeometry.UPPER_ARM)<1e-7&&Math.abs(arm.elbow().distanceTo(arm.wrist())-BenihimeEntranceGeometry.FOREARM)<1e-7,"Terrain stretches a limb at "+tick);
                }
            }
        }
        var flat=BenihimeEntranceGeometry.sample(340);
        var uneven=BenihimeEntranceGeometry.sample(340,(x,z)->x<0?1:-.5);
        h.assertTrue(uneven.left().wrist().y>flat.left().wrist().y+.8&&uneven.right().wrist().y<flat.right().wrist().y-.3,"Palms ignore independent support heights");
        h.succeed();
    }

}
