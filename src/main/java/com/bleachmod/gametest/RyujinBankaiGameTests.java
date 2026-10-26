package com.bleachmod.gametest;

import com.bleachmod.common.data.PlayerCapability;
import com.bleachmod.common.data.PlayerData;
import com.bleachmod.common.technique.ReleaseAnimationService;
import com.bleachmod.common.technique.RyujinBankaiEntranceService;
import com.bleachmod.common.visual.RyujinBankaiCamera;
import com.bleachmod.common.visual.RyujinBankaiTimeline;
import com.bleachmod.common.visual.RyujinChoreography;
import com.bleachmod.init.ModItems;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

import java.util.List;
import java.util.UUID;

/**
 * Server-authoritative contract and numerical validation for Ryūjin Jakka / Zanka no Tachi Bankai transformation.
 * Verifies the 60/40 gameplay contract, 240-tick presentation lifecycle, Reiatsu expenditure,
 * movement decoupling, pre- and post-commit cancellation semantics, and camera/timeline curves.
 */
@GameTestHolder("bleachmod")
@PrefixGameTestTemplate(false)
public final class RyujinBankaiGameTests {

    private static PlayerData data(FakePlayer p) {
        return PlayerCapability.get(p).orElseThrow(() -> new IllegalStateException("Missing capability"));
    }

    private static FakePlayer player(GameTestHelper h) {
        var p = new FakePlayer(h.getLevel(), new GameProfile(UUID.randomUUID(), "RyujinQA"));
        var pos = h.absolutePos(new BlockPos(2, 2, 2));
        p.setPos(pos.getX() + 0.5, h.getLevel().getMaxBuildHeight() - 110, pos.getZ() + 0.5);
        p.setYRot(0);
        p.setNoGravity(true);
        p.setOnGround(true);
        h.getLevel().getChunkAt(p.blockPosition());
        var d = data(p);
        d.initializeShinigami();
        d.getCharacter().bindZanpakuto("ryujin_jakka");
        d.getSkills().get("zanpakuto").setLevel(2);
        for (String form : List.of("shikai", "bankai")) {
            d.getCharacter().unlockForm(form);
            d.getCharacter().setMastery("zanpakuto", form, 100);
        }
        p.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.RYUJIN_JAKKA.get()));
        return p;
    }

    private static void tickScene(FakePlayer p) {
        RyujinBankaiEntranceService.tick(new TickEvent.PlayerTickEvent(TickEvent.Phase.END, p));
    }

    private static void clean(FakePlayer p) {
        ReleaseAnimationService.cancel(p);
        RyujinBankaiEntranceService.cancel(p);
    }

    @GameTest(template = "empty", batch = "ryujin_bankai_commit", timeoutTicks = 90)
    public static void presentationOutlivesReleaseWithoutChangingCommitOrChargingTwice(GameTestHelper h) {
        var p = player(h);
        var d = data(p);
        ReleaseAnimationService.begin(p, d, "zanpakuto", "bankai", 12.0F);

        h.assertTrue(ReleaseAnimationService.snapshot(p).getInt("duration") == 60
                && RyujinBankaiEntranceService.snapshot(p).getInt("duration") == RyujinBankaiTimeline.DURATION,
                "Gameplay release (60 ticks) and presentation (240 ticks) clocks conflated");

        h.onEachTick(() -> {
            tickScene(p);
            ReleaseAnimationService.tick(p);
        });

        // Tick 39: Still pre-commit, sealed, Reiatsu full (100)
        h.runAfterDelay(39, () -> h.assertTrue(d.getCharacter().getActiveForm().equals("sealed")
                && d.getResources().getCurrentReiatsu() == 100, "Bankai committed or charged before tick 40"));

        // Tick 40: Commit executes
        h.runAfterDelay(40, () -> {
            ReleaseAnimationService.tick(p);
            h.assertTrue(d.getCharacter().getActiveForm().equals("bankai")
                    && d.getResources().getCurrentReiatsu() == 88, "Tick-40 commit failed or incorrect Reiatsu cost");
        });

        // Tick 65: Gameplay lock ended (busy == false), but 240-tick presentation is still active!
        h.runAfterDelay(65, () -> {
            try {
                h.assertTrue(!ReleaseAnimationService.busy(p), "Presentation prolonged gameplay busy state beyond tick 60");
                var snap = RyujinBankaiEntranceService.snapshot(p);
                h.assertTrue(!snap.isEmpty() && snap.getInt("duration") == RyujinBankaiTimeline.DURATION, "Entrance ended prematurely with short release");
                h.assertTrue(d.getResources().getCurrentReiatsu() == 88, "Presentation charged additional Reiatsu");
                h.assertTrue(d.getCharacter().getActiveForm().equals("bankai"), "Active form lost after gameplay release");
            } finally {
                clean(p);
            }
            h.succeed();
        });
    }

    @GameTest(template = "empty", batch = "ryujin_bankai_walk", timeoutTicks = 90)
    public static void walkingAfterCommitPreservesBankaiFormAndPresentation(GameTestHelper h) {
        var p = player(h);
        var d = data(p);
        var anchor = p.position();
        ReleaseAnimationService.begin(p, d, "zanpakuto", "bankai", 12.0F);

        h.onEachTick(() -> {
            ReleaseAnimationService.tick(p);
            tickScene(p);
        });

        h.runAfterDelay(70, () -> {
            try {
                p.setPos(anchor.add(5, 0, 0));
                tickScene(p);
                var snap = RyujinBankaiEntranceService.snapshot(p);
                h.assertTrue(snap.getInt("duration") == RyujinBankaiTimeline.DURATION && snap.getInt("elapsed") >= 70, "Walking cancelled presentation");
                h.assertTrue(snap.getDouble("x") == anchor.x, "Walking moved scenic sun anchor point");
                h.assertTrue(d.getCharacter().getActiveForm().equals("bankai"), "Walking reverted Bankai form");
                h.assertTrue(d.getResources().getCurrentReiatsu() == 88, "Walking double charged Reiatsu");
            } finally {
                clean(p);
            }
            h.succeed();
        });
    }

    @GameTest(template = "empty", batch = "ryujin_bankai_cleanup")
    public static void preCommitCancellationCleansPresentationAndPreservesSealedState(GameTestHelper h) {
        for (int scenario = 0; scenario < 5; scenario++) {
            var p = player(h);
            var d = data(p);
            try {
                ReleaseAnimationService.begin(p, d, "zanpakuto", "bankai", 12.0F);
                h.assertTrue(!RyujinBankaiEntranceService.snapshot(p).isEmpty(), "Entrance did not start in scenario " + scenario);
                switch (scenario) {
                    case 0 -> ReleaseAnimationService.cancel(p);
                    case 1 -> {
                        p.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                        ReleaseAnimationService.tick(p);
                    }
                    case 2 -> p.setPos(p.position().add(600, 0, 0));
                    case 3 -> p.setServerLevel(p.getServer().getLevel(Level.NETHER));
                    case 4 -> {
                        p.setHealth(0);
                        ReleaseAnimationService.tick(p);
                    }
                }
                tickScene(p);
                h.assertTrue(RyujinBankaiEntranceService.snapshot(p).isEmpty(), "Presentation leaked after invalidation scenario " + scenario);
                h.assertTrue(d.getResources().getCurrentReiatsu() == 100
                        && d.getCharacter().getActiveForm().equals("sealed"), "Gameplay state corrupted after scenario " + scenario);
            } finally {
                clean(p);
            }
        }
        h.succeed();
    }

    @GameTest(template = "empty", batch = "ryujin_bankai_damage", timeoutTicks = 85)
    public static void postCommitDamageCancelsPresentationWithoutRevertingBankai(GameTestHelper h) {
        var p = player(h);
        var d = data(p);
        ReleaseAnimationService.begin(p, d, "zanpakuto", "bankai", 12.0F);

        h.onEachTick(() -> {
            ReleaseAnimationService.tick(p);
            tickScene(p);
        });

        // Advance past commit (tick 40) and release (tick 60) to tick 70
        h.runAfterDelay(70, () -> {
            try {
                h.assertTrue(d.getCharacter().getActiveForm().equals("bankai"), "Form not Bankai at tick 70");
                h.assertTrue(!RyujinBankaiEntranceService.snapshot(p).isEmpty(), "Presentation inactive at tick 70");

                // Simulate hurt event
                RyujinBankaiEntranceService.hurt(new LivingHurtEvent(p, p.damageSources().generic(), 5.0F));
                tickScene(p);

                h.assertTrue(RyujinBankaiEntranceService.snapshot(p).isEmpty(), "Presentation was not cancelled by damage");
                h.assertTrue(d.getCharacter().getActiveForm().equals("bankai"), "Bankai was reverted by damage after commit");
            } finally {
                clean(p);
            }
            h.succeed();
        });
    }

    @GameTest(template = "empty", batch = "ryujin_bankai_curves")
    public static void timelineAndCameraCurvesComplyWithContract(GameTestHelper h) {
        // Phase progression
        h.assertTrue(RyujinBankaiTimeline.phase(0) == RyujinBankaiTimeline.Phase.GROWTH, "Phase 0 should be GROWTH");
        h.assertTrue(RyujinBankaiTimeline.phase(20) == RyujinBankaiTimeline.Phase.SUSTAINED_SUN, "Phase 20 should be SUSTAINED_SUN");
        h.assertTrue(RyujinBankaiTimeline.phase(40) == RyujinBankaiTimeline.Phase.ABSORPTION, "Phase 40 should be ABSORPTION");
        h.assertTrue(RyujinBankaiTimeline.phase(55) == RyujinBankaiTimeline.Phase.CARBONIZATION_HOLD, "Phase 55 should be CARBONIZATION_HOLD");
        h.assertTrue(RyujinBankaiTimeline.phase(80) == RyujinBankaiTimeline.Phase.NAME_PRONUNCIATION, "Phase 80 should be NAME_PRONUNCIATION");
        h.assertTrue(RyujinBankaiTimeline.phase(130) == RyujinBankaiTimeline.Phase.RETURN, "Phase 130 should be RETURN");

        // Sun radius curve
        h.assertTrue(RyujinBankaiTimeline.sunRadius(0) > 0.0 && RyujinBankaiTimeline.sunRadius(0) < 2.0, "Sun radius at tick 0 should start initial ignition");
        h.assertTrue(RyujinBankaiTimeline.sunRadius(15) > 8.0, "Sun radius at tick 15 should reach monumental scale (> 8 blocks)");
        h.assertTrue(RyujinBankaiTimeline.sunRadius(30) > 8.0, "Sun radius at tick 30 must remain sustained");
        h.assertTrue(RyujinBankaiTimeline.sunRadius(52) == 0.0, "Sun radius at tick 52 must be 0 (fully absorbed)");
        h.assertTrue(RyujinBankaiTimeline.sunRadius(70) == 0.0, "Sun radius after tick 52 must remain 0");

        // Carbonization intensity
        h.assertTrue(RyujinBankaiTimeline.carbonization(0) == 0.0F, "Carbonization at tick 0 must be 0");
        h.assertTrue(RyujinBankaiTimeline.carbonization(30) == 0.0F, "Carbonization at tick 30 must be 0");
        h.assertTrue(RyujinBankaiTimeline.carbonization(54) == 1.0F, "Carbonization at tick 54 must reach 1.0");
        h.assertTrue(RyujinBankaiTimeline.carbonization(120) == 1.0F, "Carbonization must remain sustained until return");

        // Subtitle timing (cue 60, signal starts ~65, ends ~105)
        h.assertTrue(RyujinBankaiTimeline.nameSubtitleAlpha(60) == 0.0F, "Subtitle at tick 60 must be 0");
        h.assertTrue(RyujinBankaiTimeline.nameSubtitleAlpha(80) == 1.0F, "Subtitle at tick 80 must be fully visible");
        h.assertTrue(RyujinBankaiTimeline.nameSubtitleAlpha(108) == 0.0F, "Subtitle after tick 106 must be 0");

        // Camera shots
        Vec3 anchor = new Vec3(10, 64, 10);
        Vec3 forward = new Vec3(0, 0, 1);
        RyujinBankaiCamera.Frame frameWide = RyujinBankaiCamera.sample(anchor, forward, 10);
        h.assertTrue(frameWide.fov() == 72.0F, "Wide shot FOV must include the complete sun inside letterbox bars");
        h.assertTrue(frameWide.eye().distanceTo(anchor) > 10.0, "Wide shot distance must be > 10 blocks for full body silhouette");

        RyujinBankaiCamera.Frame frameDetail = RyujinBankaiCamera.sample(anchor, forward, 68);
        h.assertTrue(frameDetail.fov() == 48.0F, "Detail shot FOV must be 48");
        h.assertTrue(frameDetail.eye().distanceTo(anchor) > 3.5 && frameDetail.eye().distanceTo(anchor) < 4.2, "Frontal detail must include face and blade without an extreme close-up");

        RyujinBankaiCamera.Frame frameMed = RyujinBankaiCamera.sample(anchor, forward, 100);
        h.assertTrue(frameMed.fov() == 54.0F, "Medium shot FOV must be 54");
        h.assertTrue(frameMed.eye().distanceTo(anchor) < 6.0 && frameMed.eye().distanceTo(anchor) > 4.0, "Medium shot distance must be ~4.8 blocks");

        // Left-handed mirroring
        RyujinBankaiCamera.Frame frameDetailLeft = RyujinBankaiCamera.sample(anchor, forward, 68, -1.0F);
        h.assertTrue(frameDetailLeft.fov() == 48.0F, "Left-handed detail FOV must be 48");
        h.assertTrue(frameDetailLeft.eye().distanceTo(anchor) > 3.5 && frameDetailLeft.eye().distanceTo(anchor) < 4.2, "Left-handed detail must retain the same frontal framing");

        // Return transition at tick 140
        Vec3 nativeEye=anchor.add(0,1.62,-4);
        Vec3 returned=RyujinBankaiCamera.returnEye(frameMed.eye(),anchor.add(0,1.62,0),nativeEye,forward,1,false);
        h.assertTrue(returned.distanceTo(nativeEye)<1e-9,"Return must reach actual perspective endpoint");

        // Yamamoto bankai entrance pose
        var pose0 = RyujinChoreography.bankaiEntrance(0, 1.0F);
        h.assertTrue(pose0.weight() == 0.0F, "Pose weight at tick 0 must be 0");

        var poseHold = RyujinChoreography.bankaiEntrance(80, 1.0F);
        h.assertTrue(poseHold.weight() > 0.9F, "Pose weight at tick 80 must be fully active");

        var poseEnd = RyujinChoreography.bankaiEntrance(140, 1.0F);
        h.assertTrue(poseEnd.weight() == 0.0F, "Pose weight at tick 140 must return to 0");

        h.succeed();
    }
    @GameTest(template="empty",batch="ryujin_frontal_presentation",timeoutTicks=20)
    public static void frontalPresentationTracksPoseAndReturnsWithoutCrossingActor(GameTestHelper h) {
        Vec3 origin=new Vec3(4,70,-8);
        for(float yaw:new float[]{0,73,180}) {
            Vec3 f=Vec3.directionFromRotation(0,yaw),right=new Vec3(-f.z,0,f.x);
            var prev=RyujinBankaiCamera.sample(origin,f,0);
            for(double age=.25;age<=140;age+=.25) {
                var frame=RyujinBankaiCamera.sample(origin,f,age);
                h.assertTrue(Math.abs(frame.eye().subtract(origin).dot(right))<1e-6,"Frontal camera drifted laterally");
                h.assertTrue(frame.eye().subtract(origin).dot(f)>3,"Camera crossed the caster before return");
                h.assertTrue(frame.eye().distanceTo(prev.eye())<.3 && frame.focus().distanceTo(prev.focus())<.3,"Discontinuous dolly boundary");
                prev=frame;
            }
            for(float sign:new float[]{-1,1}) {
                var blade=com.bleachmod.common.visual.RyujinBankaiPresentation.blade(origin,f,68,sign);
                h.assertTrue(blade.tip().subtract(blade.hand()).dot(right)*sign>.8,"Blade must point outwards in either hand");
                h.assertTrue(Math.abs(blade.tip().distanceTo(blade.hand())-1.35)<1e-6,"VFX blade anchor length changed");
            }
            Vec3 center=origin.add(0,1.62,0),start=RyujinBankaiCamera.sample(origin,f,112).eye();
            for(int mode=0;mode<3;mode++) {
                Vec3 target=mode==0?center:center.add(f.scale(mode==1?-4:4));
                h.assertTrue(RyujinBankaiCamera.returnEye(start,center,target,f,0,mode==2).equals(start),"Return changed initial eye");
                h.assertTrue(RyujinBankaiCamera.returnEye(start,center,target,f,1,mode==2).equals(target),"Return missed native eye");
                if(mode==1)for(double t=0;t<1;t+=.05)
                    h.assertTrue(RyujinBankaiCamera.returnEye(start,center,target,f,t,false).distanceTo(center)>3,"Rear return crossed actor");
            }
        }
        h.succeed();
    }

    @GameTest(template="empty",batch="ryujin_long_story")
    public static void realClocksPreserveOrderedBeatsInBothPresentationModes(GameTestHelper h){
        for(int duration:new int[]{RyujinBankaiTimeline.DURATION,RyujinBankaiTimeline.COMPACT_DURATION}){
            double previous=-1;
            for(int tick=0;tick<=duration;tick++){
                double age=RyujinBankaiTimeline.storyAge(tick,duration);
                h.assertTrue(age>=previous && Double.isFinite(age),"Story clock reversed or became nonfinite");previous=age;
            }
            for(double beat:new double[]{0,4,12,30,52,60,112,138,140}){
                double actual=RyujinBankaiTimeline.realAge(beat,duration);
                h.assertTrue(Math.abs(RyujinBankaiTimeline.storyAge(actual,duration)-beat)<.0001,"Cue drift in presentation clock");
            }
            h.assertTrue(RyujinBankaiTimeline.storyAge(duration,duration)==140,"Presentation failed to settle");
            h.assertTrue(duration-RyujinBankaiTimeline.realAge(60,duration)>=50,"Name voice would be cut at the end");
        }
        h.assertTrue(RyujinBankaiTimeline.DURATION==240 && RyujinBankaiTimeline.COMPACT_DURATION==240,"Legacy preference must not restore the rejected long scene");
        h.succeed();
    }

    @GameTest(template="empty",batch="ryujin_vertical_supreme")
    public static void supremeGripMeetsAndBladeContactsGroundOnImpactForBothHands(GameTestHelper h){
        for(float sign:new float[]{1,-1}){
            for(float tick=10;tick<=46;tick+=.25F){
                var pose=RyujinChoreography.attack("steam_cut",tick,sign);
                var main=RyujinChoreography.bladeHand(pose,sign);var support=RyujinChoreography.supportHand(pose,sign);
                h.assertTrue(main.distanceTo(support)<.22,"Hands separate from the common grip at"+tick+" gap="+main.distanceTo(support));
            }
            var contact=RyujinChoreography.attack("steam_cut",40,sign);
            var hand=RyujinChoreography.bladeHand(contact,sign);var dir=RyujinChoreography.bladeDirection(contact);
            var tip=hand.add(dir.scale(1.35));
            h.assertTrue(Math.abs(hand.x)<.02 && dir.y<-.99 && Math.abs(tip.y)<.04,"Supreme is not centered/vertical/on ground");
            h.assertTrue(RyujinChoreography.attack("steam_cut",56,sign).weight()==0,"Supreme did not release pose");
        }
        h.succeed();
    }

}
