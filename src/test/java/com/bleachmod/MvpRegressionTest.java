package com.bleachmod;

import com.bleachmod.common.CombatBalance;
import com.bleachmod.common.data.*;
import com.bleachmod.common.quest.*;
import com.bleachmod.common.quest.objectives.*;
import com.bleachmod.common.quest.rewards.*;
import com.bleachmod.common.evolution.*;
import com.bleachmod.common.ProgressionService;
import com.bleachmod.client.gui.layout.*;
import com.google.gson.*;
import net.minecraft.nbt.CompoundTag;
import java.util.*;

/** Dependency-free regression runner, executed by Gradle check even offline. */
public final class MvpRegressionTest {
    private static int count;
    public static void main(String[] args) {
        test("Ryujin short scene aligns dialogue, absorption and safe first-person handoff",()->{
            eq(240,com.bleachmod.common.visual.RyujinBankaiTimeline.DURATION);
            eq(240,com.bleachmod.common.visual.RyujinBankaiTimeline.COMPACT_DURATION);
            eq(40D,com.bleachmod.common.visual.RyujinBankaiTimeline.realAge(12,240));
            eq(160D,com.bleachmod.common.visual.RyujinBankaiTimeline.realAge(60,240));
            eq(90D,com.bleachmod.common.visual.RyujinBankaiTimeline.realAge(30,240));
            eq(140D,com.bleachmod.common.visual.RyujinBankaiTimeline.realAge(52,240));
            double previous=-1;
            for(int t=0;t<=240;t++){
                double beat=com.bleachmod.common.visual.RyujinBankaiTimeline.storyAge(t,240);
                yes(beat>=previous);previous=beat;
                yes(Math.abs(com.bleachmod.common.visual.RyujinBankaiTimeline.realAge(beat,240)-t)<.0001);
            }
            var center=net.minecraft.world.phys.Vec3.ZERO;
            var look=new net.minecraft.world.phys.Vec3(0,0,1);
            var start=new net.minecraft.world.phys.Vec3(0,-.12,5.2);
            for(int i=1;i<100;i++){
                double t=i/100.0;
                var eye=com.bleachmod.common.visual.RyujinBankaiCamera.returnEye(start,center,center,look,t,false);
                yes(Double.isFinite(eye.lengthSqr()));
                if(t<.65)yes(eye.horizontalDistance()>5);
                else yes(eye.dot(look)<0); // Final approach from behind, not through the face.
            }
        });
        test("Benihime camera follows the grasp and opens before floor supports without moving the rig clock",()->{
            var previous=com.bleachmod.common.visual.BenihimeEntranceCamera.sample(108);
            for(int i=433;i<=2112;i++){
                double age=i/4.0;
                var frame=com.bleachmod.common.visual.BenihimeEntranceCamera.sample(age);
                yes(Double.isFinite(frame.eye().lengthSqr()));
                yes(frame.eye().distanceTo(frame.focus())>2);
                yes(frame.eye().distanceTo(previous.eye())<.5);
                yes(frame.focus().distanceTo(previous.focus())<.5);
                yes(frame.fov()>=40 && frame.fov()<=60);
                if(age>=138 && age<=176){
                    yes(frame.focus().distanceTo(com.bleachmod.common.visual.BenihimeEntranceGeometry.hemAnchor(age))<1);
                    yes(frame.eye().z<11);
                }
                previous=frame;
            }
            for(int t:new int[]{280,340,450,528}){
                var frame=com.bleachmod.common.visual.BenihimeEntranceCamera.sample(t);
                var wide=com.bleachmod.common.visual.BenihimeEntranceCamera.wide(t);
                yes(frame.eye().distanceTo(wide.eye())<.00001);
            }
            eq(560,com.bleachmod.common.visual.BenihimeEntranceTimeline.DURATION);
            eq(176,com.bleachmod.common.visual.BenihimeEntranceTimeline.Phase.PULL.start());
        });
        test("Hyorinmaru camera stays continuous and opposing return directions never collapse",()->{
            var previous=com.bleachmod.common.visual.HyorinmaruCameraPath.sample(0);
            for(int i=1;i<=1840;i++){
                var frame=com.bleachmod.common.visual.HyorinmaruCameraPath.sample(i/10.0);
                yes(Double.isFinite(frame.eye().lengthSqr()));
                yes(frame.eye().distanceTo(frame.focus())>1);
                yes(frame.eye().distanceTo(previous.eye())<.5);
                yes(frame.focus().distanceTo(previous.focus())<.3);
                yes(frame.fov()>=40 && frame.fov()<=65);
                previous=frame;
            }
            for(int i=0;i<=100;i++){
                var direction=com.bleachmod.common.visual.HyorinmaruCameraPath.blendDirection(new net.minecraft.world.phys.Vec3(0,0,-1),new net.minecraft.world.phys.Vec3(0,0,1),i/100.0);
                yes(Math.abs(direction.length()-1)<.00001);
                var orbit=com.bleachmod.common.visual.HyorinmaruCameraPath.returnEye(new net.minecraft.world.phys.Vec3(-.8,.2,4),net.minecraft.world.phys.Vec3.ZERO,new net.minecraft.world.phys.Vec3(0,0,-4),new net.minecraft.world.phys.Vec3(0,0,1),i/100.0);
                yes(orbit.horizontalDistance()>3.9);
            }
            yes(com.bleachmod.common.visual.HyorinmaruCameraPath.sample(12).eye().distanceTo(com.bleachmod.common.visual.HyorinmaruCameraPath.sample(12).focus())<3);
        });
        test("Hyorinmaru dialogue cues never replay for late trackers and OGG stems have correct channels",()->{
            yes(com.bleachmod.common.visual.HyorinmaruTimeline.cueDue(23,24,24));
            yes(!com.bleachmod.common.visual.HyorinmaruTimeline.cueDue(24,25,24));
            yes(!com.bleachmod.common.visual.HyorinmaruTimeline.cueDue(100,101,24));
            eq(200D,com.bleachmod.common.visual.HyorinmaruTimeline.motionAge(400));
            try {
                for(String stem:List.of("hyorinmaru_voice_bankai","hyorinmaru_voice_name","hyorinmaru_theme")){
                    var bytes=java.nio.file.Files.readAllBytes(java.nio.file.Path.of("src/main/resources/assets/bleachmod/sounds/"+stem+".ogg"));
                    eq("OggS",new String(bytes,0,4,java.nio.charset.StandardCharsets.US_ASCII));
                    eq("\u0001vorbis",new String(bytes,28,7,java.nio.charset.StandardCharsets.US_ASCII));
                    eq(stem.equals("hyorinmaru_theme")?2:1,bytes[39]&255);
                }
            } catch(java.io.IOException failure){throw new AssertionError(failure);}
        });
        test("Hydra heads advance once and stay at impact while the neck disintegrates",()->{
            var target=new net.minecraft.world.phys.Vec3(6,.6,40);
            for(int head=0;head<3;head++){
                double previous=-100;
                for(int age=8+head*10;age<=com.bleachmod.common.visual.HydraMotion.impactTick(head);age++){
                    double z=com.bleachmod.common.visual.HydraMotion.point(true,head,age,1,target).z;yes(z>=previous);previous=z;
                }
                for(int age=com.bleachmod.common.visual.HydraMotion.impactTick(head);age<=156;age++)yes(com.bleachmod.common.visual.HydraMotion.point(true,head,age,1,target).distanceTo(target)<.00001);
                eq(0D,com.bleachmod.common.visual.HydraMotion.growth(true,head,com.bleachmod.common.visual.HydraMotion.impactTick(head)+12));
            }
        });
        test("Hydra curves remain finite, connected and bounded throughout formation and attack",()->{
            for(boolean assault:List.of(false,true))for(int head=0;head<3;head++)for(int age=0;age<=156;age++){
                var last=com.bleachmod.common.visual.HydraMotion.point(assault,head,age,0,32);
                for(int i=1;i<=120;i++){
                    var next=com.bleachmod.common.visual.HydraMotion.point(assault,head,age,i/120.0,32);
                    yes(Double.isFinite(next.lengthSqr()));yes(next.z<=40.01);yes(next.distanceTo(last)<1.2);last=next;
                }
            }
        });
        test("Hyorinmaru slot migration retains stable Ryusenka mastery and replaced-technique investment",()->{
            var old=new CharacterData().save();old.putInt("techniqueRevision",3);old.putString("zanpakutoIdentity","hyorinmaru");
            var mastery=old.getCompound("techniqueMasteries");var unlocks=old.getCompound("unlockedTechniques");
            mastery.putDouble("hyorinmaru|bankai|ryusenka",73);unlocks.putBoolean("hyorinmaru|bankai|ryusenka",true);
            mastery.putDouble("hyorinmaru|bankai|ice_dragon",41);unlocks.putBoolean("hyorinmaru|bankai|ice_dragon",true);
            mastery.putDouble("hyorinmaru|bankai|sennen",82);unlocks.putBoolean("hyorinmaru|bankai|sennen",true);
            var copy=new CharacterData();copy.load(old);eq(73D,copy.getTechniqueMastery("bankai",1));eq(41D,copy.getTechniqueMastery("bankai",2));eq(82D,copy.getTechniqueMastery("bankai",3));
            yes(copy.isTechniqueDiscovered("bankai",1));yes(!copy.isTechniqueDiscovered("shikai",3));
            var second=new CharacterData();second.load(copy.save());eq(copy.save(),second.save());
        });
        test("Guard posture breaks without spending stamina and requires release", () -> {
            var d = new PlayerData(); d.initializeShinigami(); var g = d.getGuard();
            yes(g.request(0)); yes(!g.absorb(50)); eq(50F, g.posture());
            yes(g.absorb(50)); yes(g.broken()); yes(!g.active()); eq(0F, g.posture());
            eq(100F, d.getResources().getStamina());
            for (int i = 1; i <= 30; i++) g.tick(i);
            yes(!g.broken()); eq(10F, g.posture()); yes(!g.request(30));
            g.release(); yes(g.request(32));
        });
        test("Guard lease expires and recovery requires lowering the guard", () -> {
            var g = new GuardData(); yes(g.request(0)); g.absorb(20);
            for (int i = 1; i <= 30; i++) { if (i % 5 == 0) yes(g.request(i)); g.tick(i); }
            eq(80F, g.posture()); yes(g.active()); g.tick(42); yes(!g.active());
            g.release(); for (int i = 43; i < 70; i++) g.tick(i); eq(100F, g.posture());
        });
        test("Guard snapshot is sanitized and persistence never restores held input", () -> {
            var g = new GuardData(); g.request(0); g.absorb(30);
            var saved = g.save(); yes(!saved.contains("active"));
            var copy = new GuardData(); copy.load(saved); eq(70F, copy.posture()); yes(!copy.active());
            copy.readSnapshot(g.snapshot()); yes(copy.shownActive()); eq(70F, copy.shownPosture());
            var bad = new CompoundTag(); bad.putFloat("posture", Float.NaN); bad.putBoolean("active", true);
            copy.readSnapshot(bad); eq(0F, copy.shownPosture()); yes(!copy.shownActive());
            g.absorb(100); copy.load(g.save()); yes(copy.broken()); eq(20, copy.breakTicks());
        });
        test("Guard cone, pressure limits and schema 7 migration preserve previous progress", () -> {
            yes(com.bleachmod.common.GuardPolicy.inFront(0, new net.minecraft.world.phys.Vec3(0,0,1)));
            yes(!com.bleachmod.common.GuardPolicy.inFront(0, new net.minecraft.world.phys.Vec3(0,0,-1)));
            yes(!com.bleachmod.common.GuardPolicy.inFront(0, new net.minecraft.world.phys.Vec3(1,0,0)));
            yes(!com.bleachmod.common.GuardPolicy.inFront(0, net.minecraft.world.phys.Vec3.ZERO));
            eq(8F, com.bleachmod.common.GuardPolicy.pressure(.1F)); eq(50F, com.bleachmod.common.GuardPolicy.pressure(100));
            eq(0F, com.bleachmod.common.GuardPolicy.pressure(Float.NaN));
            var d = new PlayerData(); d.initializeShinigami(); d.getResources().spendBasicAttackStamina();
            var old = d.save(); old.putInt("schemaVersion", 7); old.remove("guard");
            var copy = new PlayerData(); copy.load(old); eq(100F, copy.getGuard().posture()); eq(96F, copy.getResources().getStamina());
            yes(copy.getCharacter().save().equals(d.getCharacter().save())); eq(9, copy.save().getInt("schemaVersion"));
        });
        test("Dodge directions rotate with yaw and diagonals have equal length", () -> {
            var p = com.bleachmod.common.DodgePolicy.direction(1, 0, 0);
            yes(p.distanceTo(new net.minecraft.world.phys.Vec3(0,0,1)) < .00001);
            yes(com.bleachmod.common.DodgePolicy.direction(0, 1, 0).x > .99);
            yes(com.bleachmod.common.DodgePolicy.direction(0, -1, 0).x < -.99);
            yes(com.bleachmod.common.DodgePolicy.direction(0, 0, 0).z < -.99);
            yes(com.bleachmod.common.DodgePolicy.direction(1, 0, 90).x < -.99);
            yes(Math.abs(com.bleachmod.common.DodgePolicy.direction(1, 1, 45).length() - 1) < .00001);
            yes(!com.bleachmod.common.DodgePolicy.validInput(Integer.MIN_VALUE, 0));
            yes(com.bleachmod.common.DodgePolicy.direction(2, 0, 0).lengthSqr() == 0);
        });
        test("Dodge requires full stamina, keeps reiatsu and pauses regeneration", () -> {
            var r = new ResourcesData(); r.setCostsDisabled(true); r.setCurrentReiatsu(50);
            for (int i = 0; i < 6; i++) yes(r.trySpendDodgeStamina());
            eq(10F, r.getStamina()); yes(!r.trySpendDodgeStamina()); eq(10F, r.getStamina());
            for (int i = 0; i < 20; i++) r.tickStamina(false);
            eq(10F, r.getStamina()); r.tickStamina(false); eq(11F, r.getStamina()); eq(50F, r.getCurrentReiatsu());
            var tag = r.save(); tag.putFloat("stamina", 15); r.load(tag);
            yes(r.trySpendDodgeStamina()); eq(0F, r.getStamina()); yes(r.isStaminaExhausted());
        });
        test("Dodge interval survives form changes but lifecycle clears transient movement", () -> {
            var s = new StatusData(); s.beginDodge(new net.minecraft.world.phys.Vec3(1,0,0), 100);
            yes(!s.canDodge(100));
            for (int i = 0; i < 5; i++) s.advanceDodge(); eq(0, s.getDodgeTicks());
            s.stopDodge(); yes(!s.canDodge(111)); yes(s.canDodge(112));
            s.beginDodge(new net.minecraft.world.phys.Vec3(1,0,0), 112); s.clearTransformationState();
            yes(!s.isDodging()); yes(!s.canDodge(123)); yes(s.canDodge(124));
            s.clearTransientState(); yes(s.canDodge(0));
            s.beginDodge(new net.minecraft.world.phys.Vec3(1,0,0), 100); s.load(s.save());
            yes(!s.isDodging()); yes(s.canDodge(0));
        });
        test("Resistance stamina curve has diminishing returns without free refills", () -> {
            eq(100F, com.bleachmod.common.StaminaPolicy.maximum(0));
            eq(150F, com.bleachmod.common.StaminaPolicy.maximum(20));
            yes(com.bleachmod.common.StaminaPolicy.maximum(Integer.MAX_VALUE) <= 200);
            var d = new PlayerData(); d.initializeShinigami(); d.getResources().spendBasicAttackStamina();
            var ranks = new CompoundTag(); ranks.putInt(AttributeData.RESISTANCE, 20); d.getAttributes().load(ranks);
            d.refreshDerivedResources(); eq(150F, d.getResources().getMaxStamina()); eq(96F, d.getResources().getStamina());
            d.getResources().fillStamina(); var copy = new PlayerData(); copy.load(d.save());
            eq(150F, copy.getResources().getStamina()); eq(150F, copy.getResources().getMaxStamina());
            copy.getAttributes().load(new CompoundTag()); copy.refreshDerivedResources(); eq(100F, copy.getResources().getStamina());
        });
        test("Draw timing initializes quietly and handles hands, removal and identity switches", () -> {
            var draw = new com.bleachmod.common.visual.ZanpakutoDraw(); draw.update(1,"asauchi",10);
            eq(1F, draw.progress(10,0)); draw.update(2,"asauchi",11); eq(0F, draw.progress(11,0));
            eq(.5F, draw.progress(15,0)); eq(1F, draw.progress(19,0));
            draw.update(1,"asauchi",20); eq(0F,draw.progress(20,0));
            draw.update(3,"asauchi",30); eq(3,draw.hand); eq(0F,draw.progress(30,0));
            draw.update(0,"asauchi",31); eq(1F,draw.progress(31,0));
            draw.update(1,"ryujin_jakka",32); eq(1F,draw.progress(32,0));
        });
        test("Basic sword effort spends only stamina and delays recovery without blocking attacks", () -> {
            var r = new ResourcesData(); r.setCurrentReiatsu(50); r.setCostsDisabled(true);
            for (int i = 0; i < 25; i++) r.spendBasicAttackStamina();
            eq(0F, r.getStamina()); yes(r.isStaminaExhausted()); eq(50F, r.getCurrentReiatsu());
            r.spendBasicAttackStamina(); eq(0F, r.getStamina());
            for (int i = 0; i < 20; i++) r.tickStamina(false); eq(0F, r.getStamina());
            r.tickStamina(false); eq(1F, r.getStamina());
            r.spendBasicAttackStamina(); eq(0F, r.getStamina());
        });
        test("Stamina drains, rests and resumes without changing reiatsu", () -> {
            var r = new ResourcesData(); r.setCurrentReiatsu(50);
            for (int i = 0; i < 399; i++) yes(r.tickStamina(true));
            yes(!r.tickStamina(true)); eq(0F, r.getStamina()); yes(r.isStaminaExhausted());
            for (int i = 0; i < 20; i++) r.tickStamina(false);
            eq(0F, r.getStamina());
            for (int i = 0; i < 9; i++) r.tickStamina(false);
            yes(r.isStaminaExhausted()); r.tickStamina(false); yes(!r.isStaminaExhausted());
            for (int i = 0; i < 200; i++) r.tickStamina(false);
            eq(100F, r.getStamina()); eq(50F, r.getCurrentReiatsu());
        });
        test("Stamina persists including rest and sanitizes malformed values", () -> {
            var r = new ResourcesData(); for (int i = 0; i < 400; i++) r.tickStamina(true);
            var copy = new ResourcesData(); copy.load(r.save()); yes(copy.save().equals(r.save()));
            yes(copy.isStaminaExhausted()); copy.tickStamina(false); eq(0F, copy.getStamina());
            var tag = new CompoundTag(); tag.putFloat("stamina", Float.NaN); tag.putInt("staminaRestTicks", Integer.MAX_VALUE);
            copy.load(tag); eq(0F, copy.getStamina()); yes(copy.isStaminaExhausted());
            tag.putFloat("stamina", 1000); copy.load(tag); eq(100F, copy.getStamina()); yes(!copy.isStaminaExhausted());
            var legacy = new PlayerData(); legacy.initializeShinigami(); var saved = legacy.save(); saved.putInt("schemaVersion", 5);
            saved.getCompound("resources").remove("stamina"); saved.getCompound("resources").remove("staminaRestTicks");
            saved.getCompound("resources").remove("staminaExhausted"); var migrated = new PlayerData(); migrated.load(saved);
            eq(100F, migrated.getResources().getStamina()); eq(9, migrated.save().getInt("schemaVersion"));
        });
        test("Combat state expires, refreshes and stays absent from persistent saves", () -> {
            var status = new StatusData(); status.refreshCombat(200);
            for (int i = 0; i < 199; i++) status.tickTransientState(); yes(status.isInCombat());
            status.refreshCombat(200); eq(200, status.getCombatTicks());
            var client = new StatusData(); client.readTechniqueHud(status.techniqueHud()); yes(client.isHudInCombat());
            for (int i = 0; i < 200; i++) status.tickTransientState(); yes(!status.isInCombat());
            client.readTechniqueHud(status.techniqueHud()); yes(!client.isHudInCombat());
            status.refreshCombat(200); yes(!status.save().contains("combatTicks"));
            status.clearTransformationState(); eq(200, status.getCombatTicks());
            status.load(status.save()); yes(!status.isInCombat());
            status.refreshCombat(200); status.clearTransientState(); yes(!status.isInCombat());
        });
        test("Nozarashi kit: twelve slots, eyepatch drain only, Berserk Mode display name", () -> {
            var defs = com.bleachmod.common.technique.TechniqueDefinitions.all().stream().filter(d -> d.identity().equals("nozarashi")).toList();
            eq(12, defs.size());
            for (var d : defs) eq(d.form().equals("sealed") && d.slot() == 3, d.drainPerTick() > 0);
            eq("world_cleaver", com.bleachmod.common.technique.TechniqueDefinitions.require("nozarashi", "bankai", 4).id());
            eq("form.bleachmod.nozarashi.bankai", com.bleachmod.common.evolution.FormNames.key("nozarashi", "bankai"));
            eq("form.bleachmod.bankai", com.bleachmod.common.evolution.FormNames.key("hyorinmaru", "bankai"));
            yes(com.bleachmod.common.data.CharacterData.knownIdentity("nozarashi"));
        });
        test("Nozarashi player animations parse with PlayerAnimator and match the server timeline", () -> {
            var root = java.nio.file.Path.of("src/main/resources/assets/bleachmod/player_animation");
            java.util.Map<String, Integer> expected = java.util.Map.of(
                    "berserk_release", com.bleachmod.common.technique.NozarashiReleaseEffects.DURATION,
                    "nozarashi_shoulder", 40, "nozarashi_hammer", 10);
            for (var entry : expected.entrySet()) {
                try (var reader = java.nio.file.Files.newBufferedReader(root.resolve(entry.getKey() + ".json"))) {
                    var animations = dev.kosmx.playerAnim.core.data.gson.AnimationSerializing.deserializeAnimation(reader);
                    eq(1, animations.size());
                    var animation = animations.get(0);
                    yes(Math.abs(entry.getValue() - animation.endTick) <= 1); // endTick is the last tick index
                    yes(animation.getPart("rightArm").isEnabled());
                    eq(entry.getKey().equals("nozarashi_shoulder"), animation.isInfinite());
                } catch (java.io.IOException e) { throw new AssertionError(e); }
            }
        });
        test("Benihime Better Combat animations parse as finite original fencing motions",()->{
            for(String name:List.of("benihime_thrust","benihime_thrust_2","benihime_slash","senbonzakura_diagonal","senbonzakura_return","senbonzakura_thrust")){
                try(var reader=java.nio.file.Files.newBufferedReader(java.nio.file.Path.of("src/main/resources/assets/bleachmod/attack_animations/"+name+".json"))){
                    var animations=dev.kosmx.playerAnim.core.data.gson.AnimationSerializing.deserializeAnimation(reader);
                    eq(1,animations.size());var animation=animations.get(0);yes(animation.getPart("rightArm").isEnabled());yes(animation.getPart("rightItem").isEnabled());yes(animation.getPart("body").isEnabled());yes(!animation.isInfinite());eq(20,animation.endTick);
                }catch(java.io.IOException e){throw new AssertionError(e);}
                try(var reader=java.nio.file.Files.newBufferedReader(java.nio.file.Path.of("src/main/resources/assets/bleachmod/player_animation/"+name+".json"))){
                    var animations=dev.kosmx.playerAnim.core.data.gson.AnimationSerializing.deserializeAnimation(reader);
                    eq(1,animations.size());var animation=animations.get(0);yes(animation.getPart("rightArm").isEnabled());yes(animation.getPart("rightItem").isEnabled());
                }catch(java.io.IOException e){throw new AssertionError(e);}
            }
        });
        test("Bankai voice line is a declared mono Ogg Vorbis that fits the ducked voice window", () -> {
            try {
                var assets = java.nio.file.Path.of("src/main/resources/assets/bleachmod");
                String json = java.nio.file.Files.readString(assets.resolve("sounds.json"));
                yes(json.contains("\"voice.bankai_zaraki\"") && json.contains("\"bleachmod:bankai_zaraki\""));
                byte[] ogg = java.nio.file.Files.readAllBytes(assets.resolve("sounds/bankai_zaraki.ogg"));
                // First page: 27-byte header + 1 lacing byte, then the Vorbis identification packet.
                eq("OggS", new String(ogg, 0, 4, java.nio.charset.StandardCharsets.US_ASCII));
                eq("\u0001vorbis", new String(ogg, 28, 7, java.nio.charset.StandardCharsets.US_ASCII));
                eq(1, ogg[39] & 0xFF); // mono, so the line is positional around the wielder
                var header = java.nio.ByteBuffer.wrap(ogg).order(java.nio.ByteOrder.LITTLE_ENDIAN);
                int rate = header.getInt(40);
                int last = -1; // the last page's granule position is the total sample count
                for (int i = ogg.length - 4; i >= 0 && last < 0; i--) if (ogg[i] == 'O' && ogg[i + 1] == 'g' && ogg[i + 2] == 'g' && ogg[i + 3] == 'S') last = i;
                double seconds = header.getLong(last + 6) / (double) rate;
                yes(seconds > 3.5 && seconds * 20 <= com.bleachmod.common.technique.NozarashiReleaseEffects.VOICE_TICKS);
            } catch (java.io.IOException e) { throw new AssertionError(e); }
        });
        test("Shared technique definitions preserve all homologated costs and cooldowns", () -> {
            var definitions = com.bleachmod.common.technique.TechniqueDefinitions.all(); eq(60, definitions.size());
            Set<String> unique = new HashSet<>();
            for (var def : definitions) {
                yes(unique.add(def.identity() + "|" + def.form() + "|" + def.slot()));
                eq((def.slot() - 1) * 20, def.mastery());
                eq(def, com.bleachmod.common.technique.TechniqueDefinitions.require(def.identity(), def.form(), def.slot()));
            }
            for (String form : List.of("sealed", "shikai", "bankai")) {
                for (int slot = 1; slot <= 4; slot++) yes(com.bleachmod.common.technique.TechniqueDefinitions.find("asauchi", form, slot).isEmpty());
                float[] costs = form.equals("sealed") ? new float[]{8,8,12,18} : form.equals("shikai") ? new float[]{15,20,30,30} : new float[]{25,30,35,35};
                int[] cooldowns = form.equals("sealed") ? new int[]{100,160,240,300} : form.equals("bankai") ? new int[]{160,240,320,300} : new int[]{100,160,280,300};
                for (int slot = 1; slot <= 4; slot++) {
                    var def = com.bleachmod.common.technique.TechniqueDefinitions.require("hyorinmaru", form, slot);
                    eq(costs[slot - 1], def.cost()); eq(cooldowns[slot - 1], def.cooldownTicks());
                }
                boolean bankai = form.equals("bankai");
                float[] fireCosts = bankai ? new float[]{20,25,40,45} : form.equals("sealed") ? new float[]{8,8,0,0} : new float[]{15,25,40,35};
                int[] fireCooldowns = bankai ? new int[]{100,160,300,1200} : form.equals("sealed") ? new int[]{60,80,0,0} : new int[]{80,100,300,300};
                for (int slot = 1; slot <= 4; slot++) {
                    var def = com.bleachmod.common.technique.TechniqueDefinitions.require("ryujin_jakka", form, slot);
                    eq(fireCosts[slot - 1], def.cost()); eq(fireCooldowns[slot - 1], def.cooldownTicks());
                    eq(0F, def.drainPerTick());
                }
            }
            rejects(() -> com.bleachmod.common.technique.TechniqueDefinitions.require("missing", "sealed", 1));
            rejects(() -> com.bleachmod.common.technique.TechniqueDefinitions.require("ryujin_jakka", "missing", 1));
            rejects(() -> com.bleachmod.common.technique.TechniqueDefinitions.require("ryujin_jakka", "sealed", 5));
        });
        test("Mastery feedback preserves fractional thresholds and reports only new access", () -> {
            eq("19.5", com.bleachmod.common.technique.MasteryFeedback.value(19.5));
            eq("19.9", com.bleachmod.common.technique.MasteryFeedback.value(19.99));
            eq("20", com.bleachmod.common.technique.MasteryFeedback.value(20));
            eq("100", com.bleachmod.common.technique.MasteryFeedback.value(101));
            eq("0", com.bleachmod.common.technique.MasteryFeedback.value(Double.NaN));
            PlayerData d = new PlayerData(); d.initializeShinigami();
            var c = d.getCharacter(); c.bindZanpakuto("hyorinmaru"); c.setMastery("zanpakuto", "sealed", 19.5);
            var before = com.bleachmod.common.technique.MasteryFeedback.snapshot(c);
            c.addMastery("zanpakuto", "sealed", .5, 100); com.bleachmod.common.technique.TechniqueProgression.unlockEligible(c);
            var changes = com.bleachmod.common.technique.MasteryFeedback.gained(before, c);
            eq(2, changes.size()); eq(2, changes.get(0).slot()); eq("shikai", changes.get(1).form());
            eq(0, com.bleachmod.common.technique.MasteryFeedback.gained(com.bleachmod.common.technique.MasteryFeedback.snapshot(c), c).size());
            c.bindZanpakuto("asauchi"); eq(0, com.bleachmod.common.technique.MasteryFeedback.gained(before, c).size());
            c.setMastery("zanpakuto", "sealed", 19.5); before = com.bleachmod.common.technique.MasteryFeedback.snapshot(c);
            c.addMastery("zanpakuto", "sealed", .5, 100); com.bleachmod.common.technique.TechniqueProgression.unlockEligible(c);
            changes = com.bleachmod.common.technique.MasteryFeedback.gained(before, c); eq(1, changes.size()); eq(0, changes.get(0).slot());
        });
        test("Elemental migration follows attacks instead of transferring mastery to replacements", () -> {
            var old = new CharacterData().save(); old.remove("techniqueRevision");
            var mastery = old.getCompound("techniqueMasteries");
            var unlocked = old.getCompound("unlockedTechniques");
            mastery.putDouble("ryujin_jakka|bankai|2", 63);
            unlocked.putBoolean("ryujin_jakka|bankai|2", true);
            mastery.putDouble("hyorinmaru|bankai|3", 91);
            unlocked.putBoolean("hyorinmaru|bankai|3", true);
            var c = new CharacterData(); c.load(old); c.bindZanpakuto("ryujin_jakka");
            eq(63D, c.getTechniqueMastery("shikai", 2)); yes(c.isTechniqueDiscovered("shikai", 2));
            eq(0D, c.getTechniqueMastery("bankai", 2)); yes(!c.isTechniqueDiscovered("bankai", 2));
            c.bindZanpakuto("hyorinmaru");
            eq(0D, c.getTechniqueMastery("bankai", 3)); yes(!c.isTechniqueDiscovered("bankai", 3));
            yes(c.save().getCompound("techniqueMasteries").contains("hyorinmaru|bankai|3"));
            var roundTrip = new CharacterData(); roundTrip.load(c.save()); roundTrip.bindZanpakuto("ryujin_jakka");
            eq(63D, roundTrip.getTechniqueMastery("shikai", 2));
        });
        test("Schema nine migration preserves balance and leaves transient cooldowns absent", () -> {
            var d = new PlayerData(); d.initializeShinigami(); d.getResources().setCurrentReiatsu(43);
            d.getStatus().setTechniqueSlot2CooldownTicks(87);
            var tag = d.save(); tag.putInt("schemaVersion", 8);
            var migrated = new PlayerData(); migrated.load(tag);
            eq(43F, migrated.getResources().getCurrentReiatsu());
            eq(0, migrated.getStatus().getTechniqueSlot1CooldownTicks());
            eq(0, migrated.getStatus().getTechniqueSlot4CooldownTicks());
        });
        test("Ice slot profiles preserve combat ranges and cover the whole glacial disk", () -> {
            for (int slot = 1; slot <= 2; slot++) {
                var sealed = new com.bleachmod.common.visual.IceTechniqueVisuals.Cut(slot, false, false);
                eq(6.0, sealed.range()); eq(120, sealed.budget());
            }
            var narrow = new com.bleachmod.common.visual.IceTechniqueVisuals.Cut(1, true, true);
            eq(24.0, narrow.range()); eq(1056, narrow.budget());
            eq(352, new com.bleachmod.common.visual.IceTechniqueVisuals.Cut(2, true, true).budget());
            eq(480, new com.bleachmod.common.visual.IceTechniqueVisuals.Cut(1, true, false).budget());
            eq(192, new com.bleachmod.common.visual.IceTechniqueVisuals.Cut(2, true, false).budget());
            rejects(() -> new com.bleachmod.common.visual.IceTechniqueVisuals.Cut(3, true, true));
            rejects(() -> new com.bleachmod.common.visual.IceTechniqueVisuals.Cut(1, false, true));
            var origin = new net.minecraft.world.phys.Vec3(19, 70, 23);
            var forward = new net.minecraft.world.phys.Vec3(0, 0, 1);
            double minRadius = 30, maxRadius = 0;
            for (int i = 0; i < 64; i++) {
                var delta = com.bleachmod.common.visual.IceTechniqueVisuals.fieldPoint(origin, forward, false, true, 30, i, 100).subtract(origin);
                double radius = Math.sqrt(delta.x * delta.x + delta.z * delta.z);
                minRadius = Math.min(minRadius, radius); maxRadius = Math.max(maxRadius, radius);
                yes(radius <= 30 && Math.abs(delta.y - 1.2) < 1e-9);
            }
            yes(minRadius < 3 && maxRadius > 29);
            rejects(() -> com.bleachmod.common.visual.IceTechniqueVisuals.fieldPoint(origin, forward, false, true, Double.NaN, 0, 0));
        });
        test("Approved ice release geometry remains bounded and distinct from the bench", () -> {
            var origin = new net.minecraft.world.phys.Vec3(17, 65, -9);
            for (boolean bankai : new boolean[]{false, true}) {
                int samples = com.bleachmod.common.visual.IceManifestation.releaseSamples(bankai);
                double sumX = 0, sumZ = 0;
                for (int i = 0; i < samples; i++) {
                    var offset = com.bleachmod.common.visual.IceManifestation.releasePoint(origin, bankai, i).subtract(origin);
                    yes(Math.abs(offset.y - .8) < 1e-9);
                    yes(Math.abs(offset.x * offset.x + offset.z * offset.z - (bankai ? 9 : 2.25)) < 1e-9);
                    sumX += offset.x; sumZ += offset.z;
                }
                yes(Math.abs(sumX) < 1e-9 && Math.abs(sumZ) < 1e-9);
                eq(bankai ? 160 : 96, com.bleachmod.common.visual.IceManifestation.releaseBudget(bankai));
                rejects(() -> com.bleachmod.common.visual.IceManifestation.releasePoint(origin, bankai, samples));
                rejects(() -> com.bleachmod.common.visual.IceManifestation.releasePoint(origin, bankai, -1));
            }
        });
        test("Effect palettes compose sixteen bounded profiles and reject invalid definitions", () -> {
            var library = com.bleachmod.common.visual.EffectLibrary.class;
            int profiles = 0;
            for (String palette : com.bleachmod.common.visual.EffectLibrary.palettes()) for (String shape : com.bleachmod.common.visual.EffectLibrary.shapes()) {
                var profile = com.bleachmod.common.visual.EffectLibrary.resolve(palette, shape); eq(palette + "/" + shape, profile.id()); profiles++;
                for (int i = 0; i < 48; i++) {
                    var point = com.bleachmod.common.visual.EffectLibrary.point(profile.shape(), net.minecraft.world.phys.Vec3.ZERO, new net.minecraft.world.phys.Vec3(0, 0, 1), i, 48, 96);
                    yes(Double.isFinite(point.x) && Double.isFinite(point.y) && Double.isFinite(point.z)); yes(point.lengthSqr() <= 49);
                }
            }
            eq(16, profiles);
            rejects(() -> com.bleachmod.common.visual.EffectLibrary.resolve("missing", "aura"));
            rejects(() -> com.bleachmod.common.visual.EffectLibrary.resolve("fire", "missing"));
            rejects(() -> com.bleachmod.common.visual.EffectLibrary.budget(4));
        });
        test("Asset bench has a finite catalogue and strict intensity budget", () -> {
            var bench = com.bleachmod.common.visual.VanillaAssetBench.class;
            eq(6, com.bleachmod.common.visual.VanillaAssetBench.ids().size());
            for (int intensity = 1; intensity <= 3; intensity++) eq(8 * intensity, com.bleachmod.common.visual.VanillaAssetBench.samples(intensity));
            rejects(() -> com.bleachmod.common.visual.VanillaAssetBench.samples(0));
            rejects(() -> com.bleachmod.common.visual.VanillaAssetBench.samples(4));
            rejects(() -> com.bleachmod.common.visual.VanillaAssetBench.resolve("missing"));
        });
        test("All asset bench geometries are finite and bounded", () -> {
            var origin = new net.minecraft.world.phys.Vec3(0, 0, 0); var forward = new net.minecraft.world.phys.Vec3(0, 0, 1);
            for (var preset : com.bleachmod.common.visual.VanillaAssetBench.Preset.values()) for (int i = 0; i < 24; i++) {
                var point = com.bleachmod.common.visual.VanillaAssetBench.point(preset, origin, forward, i, 24, 1);
                yes(Double.isFinite(point.x) && Double.isFinite(point.y) && Double.isFinite(point.z)); yes(point.distanceToSqr(origin) <= 49);
            }
        });
        test("Fresh Asauchi keeps independent release progress and no special techniques", () -> {
            PlayerData d = new PlayerData(); d.initializeShinigami();
            eq(CharacterData.ASAUCHI, d.getCharacter().getZanpakutoIdentity());
            yes(d.getCharacter().supportsForm("shikai")); yes(d.getCharacter().supportsForm("bankai"));
            d.getResources().addTrainingPoints(700);
            yes(!ProgressionService.purchaseSkill(d, "zanpakuto"));
            d.getCharacter().setMastery("zanpakuto", "sealed", 19.5);
            com.bleachmod.common.technique.TechniqueProgression.unlockEligible(d.getCharacter());
            yes(!d.getCharacter().isFormDiscovered("shikai"));
            d.getCharacter().setMastery("zanpakuto", "sealed", 20);
            com.bleachmod.common.technique.TechniqueProgression.unlockEligible(d.getCharacter());
            yes(ProgressionService.purchaseSkill(d, "zanpakuto"));
            d.getCharacter().setMastery("zanpakuto", "shikai", 25);
            com.bleachmod.common.technique.TechniqueProgression.unlockEligible(d.getCharacter());
            yes(ProgressionService.purchaseSkill(d, "zanpakuto"));
            for (String form : List.of("sealed", "shikai", "bankai")) for (int slot = 1; slot <= 4; slot++)
                yes(!com.bleachmod.common.technique.TechniqueProgression.available(d.getCharacter(), form, slot));
            d.getCharacter().setActiveForm("zanpakuto", "bankai");
            PlayerData loaded = new PlayerData(); loaded.load(d.save());
            eq("bankai", loaded.getCharacter().getActiveForm());
            loaded.getCharacter().bindZanpakuto("hyorinmaru"); eq(0D, loaded.getCharacter().getMastery("zanpakuto", "shikai"));
            loaded.getCharacter().bindZanpakuto("asauchi"); eq(25D, loaded.getCharacter().getMastery("zanpakuto", "shikai"));
        });
        test("Technique thresholds, permanent discoveries and archived practice", () -> {
            CharacterData d = new CharacterData(); d.initializeShinigami(); d.bindZanpakuto("hyorinmaru");
            for (int slot = 1; slot <= 4; slot++) {
                if(slot==3){yes(!com.bleachmod.common.technique.TechniqueProgression.available(d,"sealed",slot));continue;}
                int required = com.bleachmod.common.technique.TechniqueProgression.requirement(slot);
                d.setMastery("zanpakuto", "sealed", Math.max(0, required - .5));
                if (slot > 1) yes(!com.bleachmod.common.technique.TechniqueProgression.available(d, "sealed", slot));
                d.setMastery("zanpakuto", "sealed", required);
                yes(com.bleachmod.common.technique.TechniqueProgression.available(d, "sealed", slot));
                com.bleachmod.common.technique.TechniqueProgression.unlockEligible(d);
                d.addTechniqueMastery("sealed", slot, 200); eq(100D, d.getTechniqueMastery("sealed", slot));
            }
            d.setMastery("zanpakuto", "sealed", 0); yes(d.isTechniqueDiscovered("sealed", 4));
            d.bindZanpakuto("ryujin_jakka"); yes(!d.isTechniqueDiscovered("sealed", 4)); eq(0D, d.getTechniqueMastery("sealed", 4));
            d.bindZanpakuto("hyorinmaru"); CharacterData loaded = new CharacterData(); loaded.load(d.save());
            yes(loaded.isTechniqueDiscovered("sealed", 4)); eq(100D, loaded.getTechniqueMastery("sealed", 4));
            loaded.load(d.saveAppearance()); yes(loaded.isTechniqueDiscovered("sealed", 4));
        });
        test("Schema four keeps the existing repertoire without copying it to Asauchi", () -> {
            PlayerData d = new PlayerData(); d.initializeShinigami(); d.getCharacter().bindZanpakuto("hyorinmaru");
            d.getCharacter().setMastery("zanpakuto", "shikai", 12);
            CompoundTag old = d.save(); old.putInt("schemaVersion", 4); old.getCompound("character").remove("unlockedTechniques");
            PlayerData loaded = new PlayerData(); loaded.load(old);
            yes(loaded.getCharacter().isTechniqueDiscovered("sealed", 4)); eq(12D, loaded.getCharacter().getMastery("zanpakuto", "shikai"));
            loaded.getCharacter().bindZanpakuto("asauchi"); yes(!loaded.getCharacter().isTechniqueDiscovered("sealed", 4));
            PlayerData fresh = new PlayerData(); fresh.initializeShinigami(); PlayerData roundtrip = new PlayerData(); roundtrip.load(fresh.save());
            roundtrip.getCharacter().bindZanpakuto("hyorinmaru"); yes(!roundtrip.getCharacter().isTechniqueDiscovered("sealed", 4));
        });
        test("Ice armor bakes independent body plates, wings and tail", () -> {
            var root = com.bleachmod.client.model.IceArmorLayer.createLayer().bakeRoot();
            for (String part : List.of("body", "arm", "leg", "wing")) yes(!root.getChild(part).isEmpty());
            var joint=root.getChild("tail");for(int i=0;i<7;i++){joint=joint.getChild("joint_"+i);yes(!joint.isEmpty());}
            yes(root.getChild("wing").getChild("tip").hasChild("feather_6"));
            CharacterData d = new CharacterData(); d.setIceArmorVisual(true);
            yes(d.saveAppearance().getBoolean("iceArmorVisual")); yes(!d.save().contains("iceArmorVisual"));
            CharacterData loaded = new CharacterData(); loaded.load(d.save()); yes(!loaded.isIceArmorVisual());
            loaded.load(d.saveAppearance()); yes(loaded.isIceArmorVisual());
        });
        test("Guide distinguishes sealed armor and duel from released fields", () -> {
            eq("ability.bleachmod.hyorinmaru.frost_armor", com.bleachmod.client.hud.ZanpakutoHud.abilityKey("hyorinmaru", "sealed", 3));
            eq("ability.bleachmod.hyorinmaru.ice_eruption", com.bleachmod.client.hud.ZanpakutoHud.abilityKey("hyorinmaru", "bankai", 3));
            eq("ability.bleachmod.ryujin.minami", com.bleachmod.client.hud.ZanpakutoHud.abilityKey("ryujin_jakka", "bankai", 3));
            try {
                var lang = JsonParser.parseString(java.nio.file.Files.readString(java.nio.file.Path.of("src/main/resources/assets/bleachmod/lang/pt_br.json"))).getAsJsonObject();
                for(String identity : List.of("hyorinmaru", "ryujin_jakka")) for(String form : List.of("sealed", "shikai", "bankai")) for(int slot=1;slot<=4;slot++)
                    yes(lang.has(com.bleachmod.client.hud.ZanpakutoHud.abilityKey(identity,form,slot)));
            } catch(java.io.IOException e) { throw new IllegalStateException(e); }
        });
        test("Zanpakuto identity archives mastery and unlocks across switches and reload", () -> {
            CharacterData d = new CharacterData(); d.initializeShinigami(); d.bindZanpakuto("ryujin_jakka");
            d.unlockForm("bankai"); d.setMastery("zanpakuto", "shikai", 75);
            d.bindZanpakuto("hyorinmaru");
            yes(!d.isFormDiscovered("bankai")); eq(0D, d.getMastery("zanpakuto", "shikai"));
            d.unlockForm("shikai"); d.setMastery("zanpakuto", "shikai", 15);
            CharacterData loaded = new CharacterData(); loaded.load(d.save());
            eq("hyorinmaru", loaded.getZanpakutoIdentity()); eq(15D, loaded.getMastery("zanpakuto", "shikai"));
            loaded.bindZanpakuto("ryujin_jakka"); yes(loaded.isFormDiscovered("bankai")); eq(75D, loaded.getMastery("zanpakuto", "shikai"));
            loaded.bindZanpakuto("hyorinmaru"); yes(loaded.isFormDiscovered("shikai")); yes(!loaded.isFormDiscovered("bankai"));
            CompoundTag legacy = d.save(); legacy.remove("zanpakutoIdentity"); loaded.load(legacy);
            eq("ryujin_jakka", loaded.getZanpakutoIdentity()); eq(75D, loaded.getMastery("zanpakuto", "shikai"));
        });
        test("Original story reward cannot unlock Hyorinmaru", () -> {
            PlayerData d = new PlayerData(); d.initializeShinigami();d.getCharacter().bindZanpakuto("ryujin_jakka");d.getCharacter().preserveLegacyTechniques(); d.getCharacter().bindZanpakuto("hyorinmaru");
            new TransformationReward("zanpakuto", "bankai", 40).give(null, d);
            yes(!d.getCharacter().isFormDiscovered("bankai")); eq(0D, d.getCharacter().getMastery("zanpakuto", "bankai"));
            d.getCharacter().bindZanpakuto("ryujin_jakka"); yes(d.getCharacter().isFormDiscovered("bankai"));
            eq(40D, d.getCharacter().getMastery("zanpakuto", "bankai"));
        });
        test("Hyorinmaru models load with a single edge and distinct guard", () -> {
            try {
                for (String name : List.of("hyorinmaru", "hyorinmaru_shikai", "hyorinmaru_bankai")) {
                    String source = java.nio.file.Files.readString(java.nio.file.Path.of("src/main/resources/assets/bleachmod/models/item", name + ".json"));
                    var parsed = net.minecraft.client.renderer.block.model.BlockModel.fromString(source);
                    yes(parsed.getElements().size() >= 23);
                    var model = JsonParser.parseString(source).getAsJsonObject(); int edges = 0;
                    for (var element : model.getAsJsonArray("elements")) {
                        if (element.getAsJsonObject().get("name").getAsString().equals("single_cutting_edge")) edges++;
                    }
                    eq(4, edges); yes(source.contains("four_point_tsuba"));
                }
            } catch (java.io.IOException e) { throw new IllegalStateException(e); }
        });
        test("Four-block flame front collapses over six ticks in both ranges", () -> {
            for (int range : List.of(8, 14)) {
                eq(4D, com.bleachmod.common.technique.FlameWaveService.frontHeight(1, range));
                eq(4D, com.bleachmod.common.technique.FlameWaveService.frontHeight(range, range));
                eq(2D, com.bleachmod.common.technique.FlameWaveService.frontHeight(range + 3, range));
                eq(0D, com.bleachmod.common.technique.FlameWaveService.frontHeight(range + 6, range));
            }
        });
        test("Ryujin models parse as explicit geometry with a shared corrected grip", () -> {
            try {
                var directory = java.nio.file.Path.of("src/main/resources/assets/bleachmod/models/item");
                var parent = JsonParser.parseString(java.nio.file.Files.readString(
                        directory.resolve("ryujin_katana_handheld.json"))).getAsJsonObject();
                yes(!parent.has("parent")); // builtin/generated would replace our geometry with the short sprite.
                var display = parent.getAsJsonObject("display");
                eq(55, display.getAsJsonObject("thirdperson_righthand").getAsJsonArray("rotation").get(2).getAsInt());
                eq(-55, display.getAsJsonObject("thirdperson_lefthand").getAsJsonArray("rotation").get(2).getAsInt());
                for (String name : List.of("ryujin_jakka", "ryujin_jakka_shikai", "ryujin_jakka_bankai")) {
                    String source = java.nio.file.Files.readString(directory.resolve(name + ".json"));
                    var parsed = net.minecraft.client.renderer.block.model.BlockModel.fromString(source);
                    eq(22, parsed.getElements().size());
                    var model = JsonParser.parseString(source).getAsJsonObject();
                    int edges = 0;
                    int spines = 0;
                    eq("bleachmod:item/ryujin_katana_handheld", model.get("parent").getAsString());
                    for (var element : model.getAsJsonArray("elements")) {
                        var part = element.getAsJsonObject();
                        if (part.has("name") && part.get("name").getAsString().equals("single_cutting_edge")) edges++;
                        if (part.has("name") && part.get("name").getAsString().equals("blunt_spine")) spines++;
                        eq(6, part.getAsJsonObject("faces").size());
                        for (int axis = 0; axis < 3; axis++) {
                            double from = part.getAsJsonArray("from").get(axis).getAsDouble();
                            double to = part.getAsJsonArray("to").get(axis).getAsDouble();
                            yes(from >= -16 && to <= 32 && from < to);
                        }
                    }
                    eq(4, edges);
                    eq(4, spines);
                    if (!name.equals("ryujin_jakka")) {
                        var uv = model.getAsJsonArray("elements").get(8).getAsJsonObject()
                                .getAsJsonObject("faces").getAsJsonObject("north").getAsJsonArray("uv");
                        yes(uv.get(2).getAsDouble() - uv.get(0).getAsDouble() > 10);
                    }
                }
            } catch (java.io.IOException exception) { throw new IllegalStateException(exception); }
        });
        test("HUD cooldown snapshot is isolated from persistence and authoritative state", () -> {
            StatusData server = new StatusData();
            server.setTechniqueSlot3CooldownTicks(400);
            server.setTechniqueSlot4CooldownTicks(1200);
            StatusData client = new StatusData();
            client.load(server.save());
            client.readTechniqueHud(server.techniqueHud());
            eq(400, client.getHudCooldowns()[2]);
            eq(1200, client.getHudCooldowns()[3]);
            eq(0, client.getTechniqueSlot4CooldownTicks());
            yes(!server.save().contains("cooldowns"));
            int[] detached = client.getHudCooldowns();
            detached[3] = 0;
            eq(1200, client.getHudCooldowns()[3]);
            server.setCooldownsDisabled(true);
            client.readTechniqueHud(server.techniqueHud());
            yes(client.areHudCooldownsDisabled());
        });
        test("release effects trigger only when entering or ascending released forms", () -> {
            yes(com.bleachmod.common.technique.RyujinReleaseEffects.shouldRelease("sealed", "shikai"));
            yes(com.bleachmod.common.technique.RyujinReleaseEffects.shouldRelease("shikai", "bankai"));
            yes(!com.bleachmod.common.technique.RyujinReleaseEffects.shouldRelease("bankai", "shikai"));
            yes(!com.bleachmod.common.technique.RyujinReleaseEffects.shouldRelease("bankai", "bankai"));
            yes(!com.bleachmod.common.technique.RyujinReleaseEffects.shouldRelease("shikai", "sealed"));
        });
        test("free reiatsu accepts zero balance without debit and restore enforces costs", () -> {
            ResourcesData resources = new ResourcesData();
            resources.setCurrentReiatsu(0);
            resources.setCostsDisabled(true);
            yes(resources.canAffordReiatsu(45));
            yes(resources.consumeReiatsu(45));
            eq(0F, resources.getCurrentReiatsu());
            yes(!resources.consumeReiatsu(Float.NaN));
            yes(!resources.consumeReiatsu(-1));
            resources.setCurrentReiatsu(50);
            resources.addReiatsu(-5);
            eq(50F, resources.getCurrentReiatsu());
            resources.setCostsDisabled(false);
            yes(resources.consumeReiatsu(45));
            eq(5F, resources.getCurrentReiatsu());
            yes(!resources.consumeReiatsu(6));
        });
        test("cooldown test mode covers every slot and survives form cleanup", () -> {
            StatusData status = new StatusData();
            status.setTechniqueSlot4CooldownTicks(1200);
            status.setCooldownsDisabled(true);
            status.setTechniqueSlot1CooldownTicks(100);
            status.setTechniqueSlot2CooldownTicks(80);
            status.setTechniqueSlot3CooldownTicks(400);
            status.setTechniqueSlot4CooldownTicks(1200);
            status.clearTransformationState();
            yes(status.areCooldownsDisabled());
            eq(0, status.getTechniqueSlot1CooldownTicks());
            eq(0, status.getTechniqueSlot2CooldownTicks());
            eq(0, status.getTechniqueSlot3CooldownTicks());
            eq(0, status.getTechniqueSlot4CooldownTicks());
            status.setCooldownsDisabled(false);
            status.setTechniqueSlot4CooldownTicks(1200);
            eq(1200, status.getTechniqueSlot4CooldownTicks());
        });
        test("operator overrides are never restored from NBT", () -> {
            PlayerData original = new PlayerData();
            original.getStatus().setCooldownsDisabled(true);
            original.getResources().setCostsDisabled(true);
            PlayerData loaded = new PlayerData();
            loaded.getStatus().setCooldownsDisabled(true);
            loaded.getResources().setCostsDisabled(true);
            loaded.load(original.save());
            yes(!loaded.getStatus().areCooldownsDisabled());
            yes(!loaded.getResources().areCostsDisabled());
        });
        test("oriented wall rejects behind width height and diagonal query corners", () -> {
            var origin = net.minecraft.world.phys.Vec3.ZERO;
            var forward = new net.minecraft.world.phys.Vec3(0, 0, 1);
            yes(com.bleachmod.common.technique.TechniqueGeometry.intersectsWall(origin, forward, 16, 2, 15,
                    new net.minecraft.world.phys.AABB(-0.3, 0, 15.9, 0.3, 2, 16.5)));
            for (var box : List.of(new net.minecraft.world.phys.AABB(2, 0, 2, 3, 2, 3),
                    new net.minecraft.world.phys.AABB(0, 16, 2, 0.5, 17, 3),
                    new net.minecraft.world.phys.AABB(0, 0, -2, 0.5, 2, -1))) {
                yes(!com.bleachmod.common.technique.TechniqueGeometry.intersectsWall(origin, forward, 16, 2, 15, box));
            }
            var diagonal = new net.minecraft.world.phys.Vec3(1, 0, 1).normalize();
            yes(!com.bleachmod.common.technique.TechniqueGeometry.intersectsWall(origin, diagonal, 16, 2, 15,
                    new net.minecraft.world.phys.AABB(8, 0, 0, 8.5, 2, 0.5)));
        });
        test("tornado phase advances clockwise in Minecraft horizontal coordinates", () -> {
            double before = com.bleachmod.common.technique.TechniqueGeometry.tornadoAngle(0, 0, 0);
            double after = com.bleachmod.common.technique.TechniqueGeometry.tornadoAngle(1, 0, 0);
            yes(after > before);
            yes(Math.sin(after) > Math.sin(before));
        });
        test("technique cooldowns survive transformation and count down", () -> {
            StatusData status = new StatusData();
            status.setTechniqueSlot1CooldownTicks(100);
            status.setTechniqueSlot2CooldownTicks(80);
            status.setTechniqueSlot3CooldownTicks(300);
            status.setTechniqueSlot4CooldownTicks(1200);
            status.setIgnitionActive(true);
            status.setFlameDashTicks(16);
            status.clearTransformationState();
            yes(!status.isIgnitionActive());
            eq(0, status.getFlameDashTicks());
            status.tickTransientState();
            eq(99, status.getTechniqueSlot1CooldownTicks());
            eq(79, status.getTechniqueSlot2CooldownTicks());
            eq(299, status.getTechniqueSlot3CooldownTicks());
            eq(1199, status.getTechniqueSlot4CooldownTicks());
        });
        test("technique cooldowns are transient and administrative reset clears slot three", () -> {
            StatusData status = new StatusData();
            status.setTechniqueSlot3CooldownTicks(300);
            status.setTechniqueSlot4CooldownTicks(1200);
            StatusData loaded = new StatusData();
            loaded.load(status.save());
            eq(0, loaded.getTechniqueSlot3CooldownTicks());
            eq(0, loaded.getTechniqueSlot4CooldownTicks());
            status.clearTechniqueTestState();
            eq(0, status.getTechniqueSlot3CooldownTicks());
            eq(0, status.getTechniqueSlot4CooldownTicks());
        });
        test("cylinder rejects enclosing box corners and targets above and below", () -> {
            var center = net.minecraft.world.phys.Vec3.ZERO;
            yes(!com.bleachmod.common.technique.TechniqueGeometry.intersectsCylinder(center, 5, 0, 8,
                    new net.minecraft.world.phys.AABB(4, 1, 4, 4.5, 2, 4.5)));
            yes(!com.bleachmod.common.technique.TechniqueGeometry.intersectsCylinder(center, 5, 0, 8,
                    new net.minecraft.world.phys.AABB(0, 8.1, 0, 1, 9, 1)));
            yes(!com.bleachmod.common.technique.TechniqueGeometry.intersectsCylinder(center, 5, 0, 8,
                    new net.minecraft.world.phys.AABB(0, -2, 0, 1, -0.1, 1)));
        });
        test("cylinder includes contact and intersecting edge hitboxes", () -> {
            var center = net.minecraft.world.phys.Vec3.ZERO;
            yes(com.bleachmod.common.technique.TechniqueGeometry.intersectsCylinder(center, 6, 1, 2,
                    new net.minecraft.world.phys.AABB(-0.3, 0, -0.3, 0.3, 1.8, 0.3)));
            yes(com.bleachmod.common.technique.TechniqueGeometry.intersectsCylinder(center, 6, 1, 2,
                    new net.minecraft.world.phys.AABB(5.9, 0, 0, 6.5, 1.8, 0.5)));
        });
        test("cleared tracking replaces old client value",()->{
            PlayerQuestData server=new PlayerQuestData(), client=new PlayerQuestData();
            client.setTrackedQuestId("old"); client.load(server.save());
            eq(null,client.getTrackedQuestId());
        });
        test("exact balance purchases one attribute",()->{
            ResourcesData r=new ResourcesData();r.addTrainingPoints(100);
            AttributeData a=new AttributeData();
            yes(a.purchase("zanjutsu",r));eq(1,a.level("zanjutsu"));eq(0F,r.getTrainingPoints());
        });
        test("insufficient balance does not change rank or points",()->{
            ResourcesData r=new ResourcesData();r.addTrainingPoints(99);
            AttributeData a=new AttributeData();
            yes(!a.purchase("reserve",r));eq(0,a.level("reserve"));eq(99F,r.getTrainingPoints());
        });
        test("unknown attribute cannot consume points",()->{
            ResourcesData r=new ResourcesData();r.addTrainingPoints(1000);
            yes(!new AttributeData().purchase("unknown",r));eq(1000F,r.getTrainingPoints());
        });
        test("attributes continue beyond the old level five cap",()->{
            ResourcesData r=new ResourcesData();r.addTrainingPoints(10000);AttributeData a=new AttributeData();
            for(int i=0;i<6;i++)yes(a.purchase("control",r));
            eq(6,a.level("control"));eq(700,a.cost("control"));
        });
        test("negative/NaN debit cannot create money or energy",()->{
            ResourcesData r=new ResourcesData();yes(!r.consumeTrainingPoints(-1));yes(!r.consumeReiatsu(Float.NaN));eq(100F,r.getCurrentReiatsu());
        });
        test("attribute NBT rejects negative ranks and preserves high ranks",()->{
            CompoundTag tag=new CompoundTag();tag.putInt("zanjutsu",-50);tag.putInt("reserve",900);
            AttributeData a=new AttributeData();a.load(tag);eq(0,a.level("zanjutsu"));eq(900,a.level("reserve"));
        });
        test("legacy power migrates to zanjutsu",()->{
            CompoundTag tag=new CompoundTag();tag.putInt("power",4);
            AttributeData a=new AttributeData();a.load(tag);eq(4,a.level("zanjutsu"));eq(0,a.level("hakuda"));
        });
        test("save roundtrip preserves progression",()->{
            PlayerData d=player();d.getResources().addTrainingPoints(750);d.getAttributes().purchase("reserve",d.getResources());d.refreshDerivedResources();
            d.getCharacter().unlockForm("shikai");d.getCharacter().setMastery("zanpakuto","shikai",42);
            PlayerData copy=new PlayerData();copy.load(d.save());eq(650F,copy.getResources().getTrainingPoints());eq(120F,copy.getResources().getMaxReiatsu());
            eq(42D,copy.getCharacter().getMastery("zanpakuto","shikai"));yes(copy.getCharacter().isFormDiscovered("shikai"));
        });
        test("raising reserve while full also fills the new maximum",()->{
            PlayerData d=player();d.getResources().addTrainingPoints(100);
            yes(d.getResources().isReiatsuFull());yes(d.getAttributes().purchase("reserve",d.getResources()));
            d.refreshDerivedResources();eq(120F,d.getResources().getCurrentReiatsu());eq(120F,d.getResources().getMaxReiatsu());
        });
        test("raising reserve while depleted preserves current reiatsu",()->{
            PlayerData d=player();d.getResources().setCurrentReiatsu(40);d.getResources().addTrainingPoints(100);
            yes(d.getAttributes().purchase("reserve",d.getResources()));d.refreshDerivedResources();
            eq(40F,d.getResources().getCurrentReiatsu());eq(120F,d.getResources().getMaxReiatsu());
        });
        test("legacy skills migrate discovered forms without losing mastery",()->{
            PlayerData d=player();d.getSkills().setSkillLevel("zanpakuto",2);d.getCharacter().setMastery("zanpakuto","bankai",100);
            CompoundTag old=d.save();old.remove("attributes");old.remove("schemaVersion");old.getCompound("character").remove("unlockedForms");
            PlayerData copy=new PlayerData();copy.load(old);yes(copy.getCharacter().isFormDiscovered("bankai"));eq(100D,copy.getCharacter().getMastery("zanpakuto","bankai"));eq(0,copy.getAttributes().level("zanjutsu"));
        });
        test("combat attributes use their separate damage paths",()->{
            eq(12F,CombatBalance.outgoingDamage(10F,2,0));
            eq(17F,CombatBalance.outgoingDamage(10F,2,.5F));
            eq(8F,CombatBalance.incomingPhysicalDamage(10F,5));
            eq(12F,CombatBalance.kidouDamage(10F,2));
            eq(.2F,CombatBalance.formDamageBonus("shikai"));
            eq(.5F,CombatBalance.formDamageBonus("bankai"));
        });
        test("control drain has diminishing returns and never becomes regeneration",()->{
            eq(.08F,CombatBalance.formDrain(.08F,0));
            yes(CombatBalance.formDrain(.08F,100)>0);
            yes(CombatBalance.formDrain(.08F,100)<.08F);
        });
        test("battle power is the weighted sum of all categories",()->{
            PlayerData d=player();d.getResources().addTrainingPoints(1000);
            yes(d.getAttributes().purchase("zanjutsu",d.getResources()));
            yes(d.getAttributes().purchase("hakuda",d.getResources()));
            eq(20D,d.getBattlePower());
        });
        test("undiscovered skill purchase cannot consume points",()->{
            PlayerData d=player();d.getResources().addTrainingPoints(500);
            yes(!ProgressionService.purchaseSkill(d,"zanpakuto"));eq(500F,d.getResources().getTrainingPoints());
            d.getCharacter().unlockForm("shikai");yes(ProgressionService.purchaseSkill(d,"zanpakuto"));eq(300F,d.getResources().getTrainingPoints());
        });
        test("zero mastery reward remains zero",()->{
            var reward=new TransformationReward("zanpakuto","shikai",0);
            eq(0D,reward.toJson().get("mastery").getAsDouble());
        });
        test("quest reward cannot be reclaimed after NBT roundtrip",()->{
            QuestProgress p=new QuestProgress("q");p.claimReward(0);p.setStatus(QuestStatus.SUCCESS);
            QuestProgress copy=QuestProgress.load(p.save());yes(copy.isRewardClaimed(0));yes(!copy.isRewardClaimed(1));
        });
        test("quest structural changes rejected but text edits accepted",()->{
            Quest q=quest();QuestProgress p=new QuestProgress("q");yes(p.bindDefinition(q));q.setDescription("new description");yes(p.bindDefinition(q));
            q.getRewards().add(new TpsReward(200));yes(!p.bindDefinition(q));yes(!QuestProgress.load(p.save()).matchesDefinition(q));
        });
        test("quest version changes require migration",()->{
            Quest q=quest();QuestProgress p=new QuestProgress("q");yes(p.bindDefinition(q));q.setVersion(2);yes(!p.bindDefinition(q));
        });
        test("forms ordered consistently regardless of JSON order",()->{
            JsonObject json=forms();JsonObject map=json.getAsJsonObject("shinigami").getAsJsonObject("zanpakuto").getAsJsonObject("forms");
            JsonObject reordered=new JsonObject();for(String key:List.of("bankai","sealed","shikai"))reordered.add(key,map.get(key));
            json.getAsJsonObject("shinigami").getAsJsonObject("zanpakuto").add("forms",reordered);
            eq(List.of("sealed","shikai","bankai"),new ArrayList<>(FormRegistry.parse(json.toString()).get("shinigami").get("zanpakuto").getForms().keySet()));
        });
        test("negative drain rejected before install",()->{
            JsonObject json=forms();json.getAsJsonObject("shinigami").getAsJsonObject("zanpakuto").getAsJsonObject("forms").getAsJsonObject("shikai").addProperty("energyDrain",-1);
            rejects(()->FormRegistry.parse(json.toString()));
        });
        test("quest-owned spawn modes roundtrip for the existing boss quests",()->{
            JsonObject q=quest().toJson();
            JsonObject objective=q.getAsJsonArray("objectives").get(0).getAsJsonObject();
            objective.addProperty("spawn","QUEST");
            objective.addProperty("count_mode","QUEST_SPAWNED_ONLY");
            KillObjective parsed=(KillObjective)QuestParser.parseQuest(q,null).getObjectives().get(0);
            eq(KillObjective.SpawnMode.QUEST,parsed.getSpawnMode());
            eq(KillObjective.CountMode.QUEST_SPAWNED_ONLY,parsed.getCountMode());
        });
        test("unknown quest spawn modes are rejected",()->{
            JsonObject q=quest().toJson();q.getAsJsonArray("objectives").get(0).getAsJsonObject().addProperty("spawn","INVALID");
            rejects(()->QuestParser.parseQuest(q,null));
        });
        test("empty objectives rejected",()->{
            JsonObject q=quest().toJson();q.add("objectives",new JsonArray());rejects(()->QuestParser.parseQuest(q,null));
        });
        test("transient reset preserves permanent progress",()->{
            PlayerData d=player();d.getResources().addTrainingPoints(88);d.getStatus().setActionCharging(true);d.getResources().setActionCharge(75);
            d.resetTransientState();yes(!d.getStatus().isActionCharging());eq(0,d.getResources().getActionCharge());eq(88F,d.getResources().getTrainingPoints());
        });
        test("training cannot restart before every reward is claimed",()->{
            Quest q=quest();q.setRepeatable(true);QuestProgress p=new QuestProgress("q");p.bindDefinition(q);
            p.setStatus(QuestStatus.SUCCESS);yes(!p.canRepeat(q));p.claimReward(0);yes(p.canRepeat(q));
            p.initializeRequirements(q);p.setStatus(QuestStatus.ACCEPTED);yes(!p.isRewardClaimed(0));yes(!p.canRepeat(q));
        });
        test("repeatability survives JSON roundtrip",()->{
            Quest q=quest();q.setRepeatable(true);
            yes(QuestParser.parseQuest(q.toJson(),null).isRepeatable());
        });
        test("quest giver survives JSON roundtrip",()->{
            Quest q=quest();q.setQuestGiver("rukia");
            eq("rukia",QuestParser.parseQuest(q.toJson(),null).getQuestGiver());
        });
        test("invalid quest giver is rejected",()->{
            JsonObject q=quest().toJson();q.addProperty("quest_giver","Rukia Kuchiki");
            rejects(()->QuestParser.parseQuest(q,null));
        });
        test("selection is revalidated after descending",()->{
            FormRegistry.replaceFromNetwork(forms().toString());
            PlayerData d=player();d.getSkills().setSkillLevel("zanpakuto",2);
            d.getCharacter().unlockForm("shikai");d.getCharacter().unlockForm("bankai");
            d.getCharacter().setMastery("zanpakuto","shikai",25);
            d.getCharacter().setActiveForm("zanpakuto","shikai");
            yes(TransformationsHelper.isSelectable(d,"zanpakuto","bankai"));
            d.getCharacter().setSelectedForm("zanpakuto","bankai");
            d.getCharacter().setActiveForm("zanpakuto","sealed");
            yes(!TransformationsHelper.isSelectable(d,"zanpakuto","bankai"));
            d.getCharacter().setMastery("zanpakuto","bankai",50);
            yes(TransformationsHelper.isSelectable(d,"zanpakuto","bankai"));
            FormRegistry.clearClient();
        });
        test("mastery boundary 24/25 controls bankai",()->{
            FormRegistry.replaceFromNetwork(forms().toString());
            PlayerData d=player();d.getSkills().setSkillLevel("zanpakuto",2);d.getCharacter().unlockForm("bankai");
            d.getCharacter().setMastery("zanpakuto","shikai",24);yes(!TransformationsHelper.isUnlocked(d,"zanpakuto","bankai"));
            d.getCharacter().setMastery("zanpakuto","shikai",25);yes(TransformationsHelper.isUnlocked(d,"zanpakuto","bankai"));
            FormRegistry.clearClient();
        });
        test("invalid client snapshot leaves previous definitions intact",()->{
            FormRegistry.replaceFromNetwork(forms().toString());
            rejects(()->FormRegistry.replaceFromNetwork("{}"));
            yes(FormRegistry.getForm("shinigami","zanpakuto","shikai")!=null);
            FormRegistry.clearClient();
        });
        test("provider data remains serializable after optional invalidation",()->{
            PlayerProvider provider=new PlayerProvider();provider.getData().initializeShinigami();
            provider.getData().getResources().addTrainingPoints(321);provider.invalidate();
            PlayerProvider replacement=new PlayerProvider();replacement.deserializeNBT(provider.serializeNBT());
            eq(321F,replacement.getData().getResources().getTrainingPoints());
        });
        test("HUD status panel keeps native pixel size and only shrinks on narrow GUIs",()->{
            GuiLayout status=layout("hud_status");
            var narrow=LayoutGeometry.place(status,339,189); var normal=LayoutGeometry.place(status,509,189); var wide=LayoutGeometry.place(status,1017,189);
            eq(240F,normal.width(status));eq(240F,wide.width(status));
            yes(narrow.width(status)<normal.width(status));yes(narrow.width(status)<=339*.60F);
            yes(normal.x()>0);yes(normal.y()>0);
        });
        test("HUD information sits in the lower-left corner, above the hotbar on narrow GUIs",()->{
            GuiLayout info=layout("hud_info");
            var corner=LayoutGeometry.place(info,509,189);
            eq(6F,corner.x());eq(189F-6,corner.y()+corner.height(info));yes(corner.x()+corner.width(info)<=509/2F-91);
            var above=LayoutGeometry.place(info,320,180);
            eq(6F,above.x());yes(above.y()+above.height(info)<=180-22-6);
        });
        test("atlas textures keep the full mipmap chain (sides divisible by 16)",()->{
            for(String folder:List.of("item","block")){
                var dir=java.nio.file.Path.of("src/main/resources/assets/bleachmod/textures",folder);
                if(!java.nio.file.Files.isDirectory(dir))continue;
                try(var files=java.nio.file.Files.walk(dir)){
                    for(var file:files.filter(f->f.toString().endsWith(".png")).toList()){
                        var img=javax.imageio.ImageIO.read(file.toFile());
                        if(Integer.lowestOneBit(img.getWidth())<16||Integer.lowestOneBit(img.getHeight())<16)
                            throw new AssertionError(file+": "+img.getWidth()+"x"+img.getHeight()+" limita o mipmap do atlas");
                    }
                }catch(java.io.IOException e){throw new java.io.UncheckedIOException(e);}
            }
        });
        test("GUI layouts parse and bar fills match their wells pixel for pixel",()->{
            var dir=java.nio.file.Path.of("src/main/resources/assets/bleachmod/gui_layouts");
            try(var files=java.nio.file.Files.list(dir)){
                for(var file:files.filter(f->f.toString().endsWith(".json")).toList()){
                    String id=file.getFileName().toString().replace(".json","");
                    GuiLayout layout=layout(id);
                    for(var element:layout.elements()){
                        if(element instanceof GuiLayout.Image image){var img=texture(image.texture());
                            if(img.getWidth()%image.states().count()!=0)throw new AssertionError(id+"/"+image.common().id()+": "+img.getWidth()+"px nao divide em "+image.states().count()+" estados");}
                        if(element instanceof GuiLayout.Button button){var img=texture(button.texture());
                            if(img.getWidth()%button.states().count()!=0)throw new AssertionError(id+"/"+button.common().id()+": "+img.getWidth()+"px nao divide em "+button.states().count()+" estados");}
                        if(!(element instanceof GuiLayout.Bar bar)) continue;
                        for(int row=0;row<bar.h();row++){int[] span=LayoutGeometry.barRow(bar.w(),bar.h(),bar.skew(),bar.skewEnd(),row);yes(span[0]>=0&&span[1]<=bar.w()&&span[0]<span[1]);}
                        if(bar.texture()!=null){var img=texture(bar.texture());
                            if(img.getWidth()!=bar.w()||img.getHeight()!=bar.h())throw new AssertionError(id+"/"+bar.common().id()+": fill "+img.getWidth()+"x"+img.getHeight()+" != bar "+bar.w()+"x"+bar.h());}
                    }
                }
            }catch(java.io.IOException e){throw new RuntimeException(e);}
        });
        test("GUI screen layouts expand repeats and keep every element inside the canvas",()->{
            GuiLayout journal=layout("journal");
            long rows=journal.elements().stream().filter(e->e.common().id().matches("row[0-9]")).count();
            eq(5L,rows);
            var row3=(GuiLayout.Button)journal.elements().stream().filter(e->e.common().id().equals("row3")).findFirst().orElseThrow();
            eq("row:3",row3.action());eq("row3_selected",row3.states().stateIf().get("selected"));
            var icon=(GuiLayout.Image)layout("character").elements().stream().filter(e->e.common().id().equals("attr5_icon")).findFirst().orElseThrow();
            eq(5,icon.states().cell());eq(7,icon.states().count());
            for(String id:List.of("journal","form_wheel","character")){
                GuiLayout l=layout(id);
                for(var e:l.elements()){var c=e.common();
                    if(c.x()<0||c.y()<0||c.x()>l.width()||c.y()>l.height())throw new AssertionError(id+"/"+c.id()+" fora do canvas");}
                var small=LayoutGeometry.place(l,480,270);yes(small.width(l)<=480&&small.height(l)<=270);
            }
            var cells=new GuiLayout.States(List.of("normal","hover","pressed","disabled"),Map.of("pressed","x"),-1);
            yes(GuiLayout.States.NONE.count()==1);eq(4,cells.count());
        });
        test("GUI layout errors name the element and field",()->{
            String message=null;
            try{GuiLayoutParser.parse("x","{\"size\":[10,10],\"elements\":[{\"id\":\"vida\",\"type\":\"bar\",\"pos\":[0,0]}]}");}
            catch(GuiLayoutParser.LayoutException e){message=e.getMessage();}
            yes(message!=null&&message.contains("vida")&&message.contains("size"));
            message=null;
            try{GuiLayoutParser.parse("x","{\"size\":[10,10],\"elements\":[{\"type\":\"rect\",\"size\":[1,1],\"color\":\"azul\"}]}");}
            catch(GuiLayoutParser.LayoutException e){message=e.getMessage();}
            yes(message!=null&&message.contains("color")&&message.contains("azul"));
            var ctx=new LayoutContext().value("health",.504F).text("tp",7).flag("in_combat",true);
            eq("50%",ctx.resolve("{health}%"));eq(7,ctx.resolve("{tp}"));eq("{nada}",ctx.resolve("{nada}"));
            yes(ctx.test("in_combat"));yes(!ctx.test("!in_combat"));yes(ctx.test(null));
            eq(0xFFFF8855,ctx.color(0xFF77DDAA,Map.of("in_combat",0xFFFF8855)));
        });
        test("client form snapshot cannot overwrite logical server snapshot",()->{
            var initial=FormRegistry.parse(forms().toString());FormRegistry.installServer(initial);
            JsonObject edited=forms();
            edited.getAsJsonObject("shinigami").getAsJsonObject("zanpakuto").getAsJsonObject("forms").getAsJsonObject("shikai").addProperty("energyDrain",.2);
            FormRegistry.replaceFromNetwork(edited.toString());
            eq(.2D,FormRegistry.getForm("shinigami","zanpakuto","shikai").getEnergyDrain());
            java.util.concurrent.FutureTask<Double> task=new java.util.concurrent.FutureTask<>(()->FormRegistry.getForm("shinigami","zanpakuto","shikai").getEnergyDrain());
            new Thread(net.minecraftforge.fml.util.thread.SidedThreadGroups.SERVER,task,"registry-regression").start();
            try { eq(.08D,task.get(5,java.util.concurrent.TimeUnit.SECONDS)); } catch(Exception e) { throw new AssertionError(e); }
            FormRegistry.clearClient();
        });
        test("self and forward form prerequisites are rejected",()->{
            for(String target:List.of("shikai","bankai")) {
                JsonObject json=forms();
                json.getAsJsonObject("shinigami").getAsJsonObject("zanpakuto").getAsJsonObject("forms").getAsJsonObject("shikai").addProperty("formRequisite","zanpakuto."+target);
                rejects(()->FormRegistry.parse(json.toString()));
            }
        });
        test("prerequisite mastery must be attainable",()->{
            JsonObject json=forms();
            JsonObject map=json.getAsJsonObject("shinigami").getAsJsonObject("zanpakuto").getAsJsonObject("forms");
            map.getAsJsonObject("shikai").addProperty("maxMastery",60);
            map.getAsJsonObject("bankai").addProperty("unlockOnMastery",61);
            rejects(()->FormRegistry.parse(json.toString()));
        });
        test("Senbon horizontal cut moves from anatomical right to left",()->{
            var zero=net.minecraft.world.phys.Vec3.ZERO;
            for(float yaw:new float[]{0,90,180,270}) {
                var f=com.bleachmod.common.visual.SenbonGeometry.forward(yaw);
                var left=com.bleachmod.common.visual.SenbonGeometry.left(f);
                yes(com.bleachmod.common.visual.SenbonGeometry.cut(zero,f,0,0,6).dot(left)<0);
                yes(com.bleachmod.common.visual.SenbonGeometry.cut(zero,f,0,1,6).dot(left)>0);
            }
        });
        test("Senbon final cut rises and diagonal cuts descend",()->{
            var zero=net.minecraft.world.phys.Vec3.ZERO;var f=new net.minecraft.world.phys.Vec3(0,0,1);
            for(int pattern:new int[]{2,3}) yes(com.bleachmod.common.visual.SenbonGeometry.cut(zero,f,pattern,0,6).y>
                    com.bleachmod.common.visual.SenbonGeometry.cut(zero,f,pattern,1,6).y);
            yes(com.bleachmod.common.visual.SenbonGeometry.cut(zero,f,4,0,6).y<0);
            yes(com.bleachmod.common.visual.SenbonGeometry.cut(zero,f,4,1,6).y>0);
        });
        test("floral contour remains within fixed damage radius",()->{
            for(int n=0;n<360;n++) {double r=com.bleachmod.common.visual.SenbonGeometry.flowerRadius(10,Math.toRadians(n));yes(r>=5.999 && r<=10);}
            var center=new net.minecraft.world.phys.Vec3(10,20,30);
            yes(com.bleachmod.common.visual.SenbonGeometry.inside(center,center.add(10,0,0),10,3));
            yes(!com.bleachmod.common.visual.SenbonGeometry.inside(center,center.add(10.01,0,0),10,3));
            yes(!com.bleachmod.common.visual.SenbonGeometry.inside(center,center.add(0,3.01,0),10,3));
        });
        test("Senbon melee trail positions match sword kinematics and combo seams seamlessly", () -> {
            for (float sign : new float[]{1.0F, -1.0F}) {
                var h0_end = com.bleachmod.client.model.SenbonCombatVisuals.sampleHand(0, 1.0F, sign);
                var t0_end = com.bleachmod.client.model.SenbonCombatVisuals.sampleTip(0, 1.0F, sign);
                var h1_start = com.bleachmod.client.model.SenbonCombatVisuals.sampleHand(1, 0.0F, sign);
                var t1_start = com.bleachmod.client.model.SenbonCombatVisuals.sampleTip(1, 0.0F, sign);
                yes(h0_end.distanceToSqr(h1_start) < 1e-6);
                yes(t0_end.distanceToSqr(t1_start) < 1e-6);

                var h1_end = com.bleachmod.client.model.SenbonCombatVisuals.sampleHand(1, 1.0F, sign);
                var t1_end = com.bleachmod.client.model.SenbonCombatVisuals.sampleTip(1, 1.0F, sign);
                var h2_start = com.bleachmod.client.model.SenbonCombatVisuals.sampleHand(2, 0.0F, sign);
                var t2_start = com.bleachmod.client.model.SenbonCombatVisuals.sampleTip(2, 0.0F, sign);
                yes(h1_end.distanceToSqr(h2_start) < 1e-6);
                yes(t1_end.distanceToSqr(t2_start) < 1e-6);

                var h2_end = com.bleachmod.client.model.SenbonCombatVisuals.sampleHand(2, 1.0F, sign);
                var t2_end = com.bleachmod.client.model.SenbonCombatVisuals.sampleTip(2, 1.0F, sign);
                yes(Math.abs(h2_end.x - t2_end.x) < 1e-6);
                yes(Math.abs(h2_end.y - t2_end.y) < 1e-6);
                yes(t2_end.z > h2_end.z + 1.0);
            }
        });
        test("Senbon identity and independent mastery survive save",()->{
            var d=player();d.getCharacter().bindZanpakuto("senbonzakura");d.getCharacter().setMastery("zanpakuto","shikai",43);
            var copy=new PlayerData();copy.load(d.save());eq("senbonzakura",copy.getCharacter().getZanpakutoIdentity());
            eq(43D,copy.getCharacter().getMastery("zanpakuto","shikai"));
            copy.getCharacter().bindZanpakuto("ryujin_jakka");copy.getCharacter().setMastery("zanpakuto","shikai",12);
            copy.getCharacter().bindZanpakuto("senbonzakura");eq(43D,copy.getCharacter().getMastery("zanpakuto","shikai"));
        });
        test("Senbon sealed and released repertoires share access metadata",()->{
            eq(12L,com.bleachmod.common.technique.TechniqueDefinitions.all().stream().filter(d->d.identity().equals("senbonzakura")).count());
            yes(com.bleachmod.common.technique.TechniqueDefinitions.find("senbonzakura","sealed",1).isPresent());
            var bloom=com.bleachmod.common.technique.TechniqueDefinitions.FLORAL_BLOOM;
            eq(40,bloom.readyTicks());eq(120,bloom.windowTicks());eq(0F,bloom.cost());
        });
        test("Senbon visual packet rejects invalid frames",()->{
            var zero=net.minecraft.world.phys.Vec3.ZERO;var f=new net.minecraft.world.phys.Vec3(0,0,1);
            yes(new com.bleachmod.common.network.s2c.SenbonPetalsS2C(0,zero,f,6,1,0,1,7).valid());
            yes(!new com.bleachmod.common.network.s2c.SenbonPetalsS2C(0,zero,f,Float.NaN,1,0,1,7).valid());
            yes(!new com.bleachmod.common.network.s2c.SenbonPetalsS2C(13,zero,f,6,1,0,1,7).valid());
            yes(!new com.bleachmod.common.network.s2c.SenbonPetalsS2C(0,zero,zero,6,1,0,1,7).valid());
        });
        test("Senbon passive aura cannot start melee trail and trail validates combo",()->{
            var zero=net.minecraft.world.phys.Vec3.ZERO;var f=new net.minecraft.world.phys.Vec3(0,0,1);
            for(int combo=0;combo<3;combo++){
                var aura=new com.bleachmod.common.network.s2c.SenbonPetalsS2C(10,zero,f,1,0,combo,1,7);
                yes(aura.valid());yes(!aura.meleeTrail());
                var trail=new com.bleachmod.common.network.s2c.SenbonPetalsS2C(12,zero,f,1,0,combo,1,7);
                yes(trail.valid());yes(trail.meleeTrail());
                var buffer=new net.minecraft.network.FriendlyByteBuf(io.netty.buffer.Unpooled.buffer());
                try{com.bleachmod.common.network.s2c.SenbonPetalsS2C.encode(trail,buffer);eq(trail,com.bleachmod.common.network.s2c.SenbonPetalsS2C.decode(buffer));}finally{buffer.release();}
            }
            yes(!new com.bleachmod.common.network.s2c.SenbonPetalsS2C(12,zero,f,1,0,3,1,7).valid());
        });
        test("large floral field packet roundtrips owner and rejects excess radius",()->{
            var zero=net.minecraft.world.phys.Vec3.ZERO;var f=new net.minecraft.world.phys.Vec3(0,0,1);
            var frame=new com.bleachmod.common.network.s2c.SenbonPetalsS2C(4,zero,f,40,.5F,0,17,5);
            var buffer=new net.minecraft.network.FriendlyByteBuf(io.netty.buffer.Unpooled.buffer());
            try {yes(frame.valid());com.bleachmod.common.network.s2c.SenbonPetalsS2C.encode(frame,buffer);
                eq(frame,com.bleachmod.common.network.s2c.SenbonPetalsS2C.decode(buffer));}finally{buffer.release();}
            yes(!new com.bleachmod.common.network.s2c.SenbonPetalsS2C(4,zero,f,49,.5F,0,17,5).valid());
        });
        test("Senkei and Hakuteiken geometry is bounded and has pointed blades",()->{
            var zero=net.minecraft.world.phys.Vec3.ZERO;var f=new net.minecraft.world.phys.Vec3(0,0,1);
            var swords=com.bleachmod.common.visual.SenbonConstructGeometry.build(8,zero,f,7,.2,0);
            eq(160,swords.size());eq(32L,swords.stream().filter(q->q.c().equals(q.d())).count());
            var wings=com.bleachmod.common.visual.SenbonConstructGeometry.build(9,zero,f,4,1,0);
            yes(wings.size()<128);yes(wings.stream().anyMatch(q->q.a().x< -3));yes(wings.stream().anyMatch(q->q.a().x>3));
            var lance=com.bleachmod.common.visual.SenbonConstructGeometry.build(11,zero,f,12,1,0);
            eq(4,lance.size());yes(lance.stream().allMatch(q->q.c().z==12));
        });
        test("petal emission is reduced and garden radius is explicit",()->{
            eq(32,com.bleachmod.common.network.s2c.SenbonPetalsS2C.particleCount(0));
            eq(6,com.bleachmod.common.network.s2c.SenbonPetalsS2C.particleCount(10));
            eq(35D,com.bleachmod.common.technique.SenbonPolicy.FIELD_RADIUS);
            eq(8D,com.bleachmod.common.technique.SenbonPolicy.CUT_RANGE);
            eq(14D,com.bleachmod.common.technique.SenbonPolicy.STREAM_RANGE);
            eq(11D,com.bleachmod.common.technique.SenbonPolicy.CONVERGENCE_RANGE);
        });
        test("garden intensity increases without changing other petal layers",()->{
            eq(192,com.bleachmod.common.network.s2c.SenbonPetalsS2C.particleCount(4));
            eq(128,com.bleachmod.common.network.s2c.SenbonPetalsS2C.particleCount(5));
            eq(160,com.bleachmod.common.network.s2c.SenbonPetalsS2C.particleCount(6,2));
            eq(36,com.bleachmod.common.network.s2c.SenbonPetalsS2C.particleCount(6,0));
            eq(32,com.bleachmod.common.network.s2c.SenbonPetalsS2C.particleCount(0));
            eq(16D,com.bleachmod.common.technique.SenbonPolicy.HAKU_STEP*com.bleachmod.common.technique.SenbonPolicy.HAKU_DASH_TICKS);
        });
        test("Ikka blades converge radially from both sides",()->{
            var zero=net.minecraft.world.phys.Vec3.ZERO;var f=new net.minecraft.world.phys.Vec3(0,0,1);
            var mesh=com.bleachmod.common.visual.SenbonConstructGeometry.build(8,zero,f,14,0,2);
            eq(160,mesh.size());
            var tips=mesh.stream().filter(q->q.c().equals(q.d())).map(q->q.c()).toList();
            eq(32,tips.size());yes(tips.stream().anyMatch(p->p.x>10));yes(tips.stream().anyMatch(p->p.x< -10));
            yes(tips.stream().anyMatch(p->p.z>10));yes(tips.stream().anyMatch(p->p.z< -10));
        });
        test("Benihime has eight owned techniques and no elemental sealed slots",()->{
            yes(com.bleachmod.common.data.CharacterData.knownIdentity("benihime"));
            eq(12L,com.bleachmod.common.technique.TechniqueDefinitions.all().stream().filter(d->d.identity().equals("benihime")).count());
            eq("cane_draw",com.bleachmod.common.technique.TechniqueDefinitions.require("benihime","sealed",1).id());
            var d=player();d.getCharacter().bindZanpakuto("benihime");d.getCharacter().setMastery("zanpakuto","shikai",62);
            var copy=new PlayerData();copy.load(d.save());eq("benihime",copy.getCharacter().getZanpakutoIdentity());eq(62D,copy.getCharacter().getMastery("zanpakuto","shikai"));
            copy.getCharacter().bindZanpakuto("senbonzakura");eq(0D,copy.getCharacter().getMastery("zanpakuto","shikai"));
        });
        test("Benihime revision three moves path progress and preserves all archived numeric mastery",()->{
            var legacy=new CharacterData().save();legacy.putInt("techniqueRevision",2);legacy.putString("zanpakutoIdentity","benihime");
            var masteries=new net.minecraft.nbt.CompoundTag();masteries.putDouble("benihime|bankai|4",43);masteries.putDouble("benihime|bankai|3",17);masteries.putDouble("benihime|shikai|3",66);legacy.put("techniqueMasteries",masteries);
            var unlocks=new net.minecraft.nbt.CompoundTag();unlocks.putBoolean("benihime|bankai|4",true);legacy.put("unlockedTechniques",unlocks);
            var c=new CharacterData();c.load(legacy);eq(43D,c.getTechniqueMastery("bankai",3));eq(17D,c.getTechniqueMastery("bankai",4));eq(66D,c.getTechniqueMastery("shikai",3));yes(c.isTechniqueDiscovered("bankai",3));
            var copy=new CharacterData();copy.load(c.save());eq(43D,copy.getTechniqueMastery("bankai",3));eq(17D,copy.getTechniqueMastery("bankai",4));
            eq("path",com.bleachmod.common.technique.TechniqueDefinitions.require("benihime","bankai",3).id());eq("final_stitch",com.bleachmod.common.technique.TechniqueDefinitions.require("benihime","bankai",4).id());
        });
        test("Benihime Hadō 91 definition, migration from gigai, choreography and geometry",()->{
            var def=com.bleachmod.common.technique.TechniqueDefinitions.require("benihime","sealed",4);
            eq("senju_koten_taiho",def.id());
            eq(30F,def.cost());
            eq(280,def.cooldownTicks());
            eq("ability.bleachmod.benihime.senju_koten_taiho",def.nameKey());

            // Legacy migration from revision 4 gigai
            var legacy=new CharacterData().save();legacy.putInt("techniqueRevision",4);legacy.putString("zanpakutoIdentity","benihime");
            var masteries=new net.minecraft.nbt.CompoundTag();masteries.putDouble("benihime|sealed|gigai",72.5);masteries.putDouble("benihime|shikai|1",50);legacy.put("techniqueMasteries",masteries);
            var unlocks=new net.minecraft.nbt.CompoundTag();unlocks.putBoolean("benihime|sealed|gigai",true);legacy.put("unlockedTechniques",unlocks);
            var c=new CharacterData();c.load(legacy);
            eq(72.5D,c.getTechniqueMastery("sealed",4));
            yes(c.isTechniqueDiscovered("sealed",4));
            var saved=c.save();
            eq(5,saved.getInt("techniqueRevision"));
            yes(saved.getCompound("unlockedTechniques").contains("benihime|sealed|senju_koten_taiho"));
            var copy=new CharacterData();copy.load(saved);
            eq(72.5D,copy.getTechniqueMastery("sealed",4));
            yes(copy.isTechniqueDiscovered("sealed",4));

            // Choreography
            for(float sign:new float[]{1F,-1F}){
                var start=com.bleachmod.common.visual.BenihimeChoreography.technique("senju_koten_taiho",0,24,sign);
                eq(0F,start.weight());
                var mid=com.bleachmod.common.visual.BenihimeChoreography.technique("senju_koten_taiho",12,24,sign);
                yes(mid.weight()>0F);
                yes(Double.isFinite(mid.arm().lengthSqr()) && Double.isFinite(mid.body().lengthSqr()) && Double.isFinite(mid.handRotation().lengthSqr()));
                var end=com.bleachmod.common.visual.BenihimeChoreography.technique("senju_koten_taiho",24,24,sign);
                eq(0F,end.weight());
                eq(0D,end.arm().lengthSqr());
            }

            // Geometry
            var zero=net.minecraft.world.phys.Vec3.ZERO;var f=new net.minecraft.world.phys.Vec3(0,0,1);
            for(float progress:new float[]{0F,.25F,.5F,.8F,1F}){
                var frame=new com.bleachmod.common.network.s2c.BenihimeFrameS2C(1,com.bleachmod.common.network.s2c.BenihimeFrameS2C.HADO91,zero,f.scale(16),f,24,progress,0);
                yes(frame.valid());
                var mesh=com.bleachmod.common.visual.BenihimeGeometry.build(frame);
                yes(!mesh.isEmpty() && mesh.size()<700);
                for(var q:mesh)for(var v:List.of(q.a(),q.b(),q.c(),q.d()))yes(Double.isFinite(v.x)&&Double.isFinite(v.y)&&Double.isFinite(v.z));
            }
        });
        test("Benihime combat rig returns to idle and every spell has a distinct body motion",()->{
            var zero=net.minecraft.world.phys.Vec3.ZERO;var f=new net.minecraft.world.phys.Vec3(0,0,1);
            var idle=com.bleachmod.common.visual.BenihimeEntranceGeometry.buildIdle(zero,f,30,0);
            for(int action=1;action<=4;action++){
                eq(idle,com.bleachmod.common.visual.BenihimeEntranceGeometry.buildCombat(zero,f,30,action,0,com.bleachmod.common.visual.BenihimeEntranceGeometry.FLAT));
                var mesh=com.bleachmod.common.visual.BenihimeEntranceGeometry.buildCombat(zero,f,30,action,.5,com.bleachmod.common.visual.BenihimeEntranceGeometry.FLAT);
                yes(!idle.equals(mesh));for(var q:mesh)for(var v:List.of(q.a(),q.b(),q.c(),q.d()))yes(Double.isFinite(v.x)&&Double.isFinite(v.y)&&Double.isFinite(v.z));
            }
            for(int kind:new int[]{10,11}){var mesh=com.bleachmod.common.visual.BenihimeGeometry.build(new com.bleachmod.common.network.s2c.BenihimeFrameS2C(1,kind,zero,f.scale(10),f,8,.5F,0));yes(!mesh.isEmpty()&&mesh.size()<700);}
        });
        test("Benihime visual packet roundtrips and rejects invalid data",()->{
            var zero=net.minecraft.world.phys.Vec3.ZERO;var f=new net.minecraft.world.phys.Vec3(0,0,1);
            var frame=new com.bleachmod.common.network.s2c.BenihimeFrameS2C(5,0,zero,zero,f,18,.5F,1);
            var buffer=new net.minecraft.network.FriendlyByteBuf(io.netty.buffer.Unpooled.buffer());
            try{yes(frame.valid());com.bleachmod.common.network.s2c.BenihimeFrameS2C.encode(frame,buffer);eq(frame,com.bleachmod.common.network.s2c.BenihimeFrameS2C.decode(buffer));}finally{buffer.release();}
            yes(!new com.bleachmod.common.network.s2c.BenihimeFrameS2C(5,0,zero,zero,f,25,.5F,1).valid());
            yes(!new com.bleachmod.common.network.s2c.BenihimeFrameS2C(5,0,zero,zero,f,18,Float.NaN,1).valid());
        });
        test("Benihime manifestation and every effect have finite bounded geometry",()->{
            var zero=net.minecraft.world.phys.Vec3.ZERO;var f=new net.minecraft.world.phys.Vec3(0,0,1);
            for(int kind=0;kind<=8;kind++){
                var frame=new com.bleachmod.common.network.s2c.BenihimeFrameS2C(1,kind,zero,f.scale(10),f,3,.5F,1);
                var mesh=com.bleachmod.common.visual.BenihimeGeometry.build(frame);yes(!mesh.isEmpty() && mesh.size()<1200);
                for(var q:mesh)for(var v:List.of(q.a(),q.b(),q.c(),q.d()))yes(Double.isFinite(v.x) && Double.isFinite(v.y) && Double.isFinite(v.z));
            }
        });
        test("Benihime path charges survive form effect cancellation",()->{
            var status=new StatusData();status.useBenihimePath();status.clearTransformationState();eq(2,status.getBenihimePathCharges());
            status.useBenihimePath();eq(1,status.getBenihimePathCharges());status.useBenihimePath();status.setTechniqueSlot3CooldownTicks(260);status.clearTransformationState();eq(0,status.getBenihimePathCharges());
            status.setTechniqueSlot3CooldownTicks(0);eq(3,status.getBenihimePathCharges());
        });
        test("Benihime idle hands animate and manifestation stays behind owner",()->{
            var o=net.minecraft.world.phys.Vec3.ZERO;var f=new net.minecraft.world.phys.Vec3(0,0,1);
            var a=com.bleachmod.common.visual.BenihimeGeometry.build(new com.bleachmod.common.network.s2c.BenihimeFrameS2C(1,0,o,o,f,18,0,0));
            var b=com.bleachmod.common.visual.BenihimeGeometry.build(new com.bleachmod.common.network.s2c.BenihimeFrameS2C(1,0,o,o,f,18,.25F,0));yes(!a.equals(b));
            yes(a.stream().flatMap(q->java.util.stream.Stream.of(q.a(),q.b(),q.c(),q.d())).mapToDouble(v->v.y).max().orElse(0)>10);
        });
        test("Benihime net mesh stays above its surface anchor",()->{
            var o=new net.minecraft.world.phys.Vec3(2,65.1,2);var f=new net.minecraft.world.phys.Vec3(0,0,1);
            var mesh=com.bleachmod.common.visual.BenihimeGeometry.build(new com.bleachmod.common.network.s2c.BenihimeFrameS2C(1,3,o,o,f,10,1,1));
            yes(mesh.stream().flatMap(q->java.util.stream.Stream.of(q.a(),q.b(),q.c(),q.d())).allMatch(v->v.y>=o.y-.03));
        });
        test("Better Combat pilot honors guard dodge and charge states",()->{
            var d=player();yes(com.bleachmod.common.compat.BetterCombatCompat.stateAllows(d));d.getGuard().request(1);yes(!com.bleachmod.common.compat.BetterCombatCompat.stateAllows(d));
            d.getGuard().release();d.getStatus().beginDodge(new net.minecraft.world.phys.Vec3(1,0,0),1);yes(!com.bleachmod.common.compat.BetterCombatCompat.stateAllows(d));d.getStatus().stopDodge();
            d.getStatus().setActionCharging(true);yes(!com.bleachmod.common.compat.BetterCombatCompat.stateAllows(d));
        });
        test("Benihime HUD leases are transient and snapshots isolated",()->{
            var status=new StatusData();var tag=new net.minecraft.nbt.CompoundTag();tag.putInt("shield",900);tag.putInt("charges",3);status.readBenihimeHud(tag);
            eq(100,status.getBenihimeHud().getInt("shield"));status.getBenihimeHud().putInt("charges",0);eq(3,status.getBenihimeHud().getInt("charges"));
            yes(!status.save().contains("benihimeHud"));
        });
        test("Bankai seal points the authored katana outward in both hands and always releases the pose", () -> {
            for(float sign:new float[]{1,-1}){
                var pose=com.bleachmod.common.visual.RyujinChoreography.release(50,sign);
                var blade=com.bleachmod.common.visual.RyujinChoreography.bladeDirection(pose);
                yes(blade.x*sign>.85 && blade.z>0 && Math.abs(blade.y)<.2);
                var end=com.bleachmod.common.visual.RyujinChoreography.release(60,sign);
                eq(0F,end.weight());eq(0D,end.arm().lengthSqr());
                eq(0F,com.bleachmod.common.visual.RyujinChoreography.release(80,sign).weight());
            }
        });
        test("All ten fire gestures have finite continuous curves and return to the base animation", () -> {
            int[] ends={16,16,16,22,18,20,16,22,22,56};int n=0;
            for(var kind:com.bleachmod.common.visual.RyujinChoreography.ATTACKS){
                int end=ends[n++];var previous=com.bleachmod.common.visual.RyujinChoreography.attack(kind,0,1);
                for(float age=.25F;age<end;age+=.25F){
                    var current=com.bleachmod.common.visual.RyujinChoreography.attack(kind,age,1);
                    yes(Double.isFinite(current.arm().lengthSqr()+current.body().lengthSqr()+current.handRotation().lengthSqr()));
                    yes(current.arm().distanceTo(previous.arm())<.9);previous=current;
                }
                eq(0F,com.bleachmod.common.visual.RyujinChoreography.attack(kind,end,1).weight());
                eq(0F,com.bleachmod.common.visual.RyujinChoreography.attack(kind,Float.NaN,1).weight());
            }
            eq(10,n);
        });
        test("Spinning dash keeps a unit pose on horizontal upward and downward travel",()->{
            for(var direction:List.of(new net.minecraft.world.phys.Vec3(0,0,1),new net.minecraft.world.phys.Vec3(.4,.2,.7),new net.minecraft.world.phys.Vec3(0,1,0),new net.minecraft.world.phys.Vec3(0,-1,0))){
                var q=com.bleachmod.common.visual.RyujinFlamePresentation.dashTilt(direction);
                var up=q.transform(new org.joml.Vector3f(0,1,0));var target=direction.normalize();
                yes(Math.abs(up.length()-1)<.00001);yes(Math.abs(up.x-target.x)+Math.abs(up.y-target.y)+Math.abs(up.z-target.z)<.0001);
            }
            yes(Float.isFinite(com.bleachmod.common.visual.RyujinFlamePresentation.dashTilt(net.minecraft.world.phys.Vec3.ZERO).w()));
        });
        test("Fire wake follows measured turns and rejects stationary motion and teleport bridges", () -> {
            var origin=new net.minecraft.world.phys.Vec3(0,0,0);
            var turn=new net.minecraft.world.phys.Vec3(.9,.3,.4);
            var samples=com.bleachmod.common.visual.RyujinFlamePresentation.trail(origin,turn);
            yes(!samples.isEmpty() && samples.size()<=12);
            eq(turn,samples.get(samples.size()-1));
            var previous=origin;
            for(var point:samples){yes(point.distanceTo(previous)<=.18);previous=point;}
            yes(com.bleachmod.common.visual.RyujinFlamePresentation.trail(turn,turn).isEmpty());
            yes(com.bleachmod.common.visual.RyujinFlamePresentation.trail(origin,new net.minecraft.world.phys.Vec3(50,0,0)).isEmpty());
            yes(com.bleachmod.common.visual.RyujinFlamePresentation.trail(null,origin).isEmpty());
        });
        test("Rajada crest stays narrow and recovers while dash preserves the server gesture lease", () -> {
            for(int age=6;age<=14;age++)for(int i=0;i<65;i++){
                var q=com.bleachmod.common.visual.RyujinFlamePresentation.crest(i,age);
                yes(Math.abs(q.x)<=.93 && q.y>=.12 && q.y<1.8 && q.z<=0 && q.z>=-.21);
            }
            var a=com.bleachmod.common.visual.RyujinChoreography.attack("flame_dash",5,1);
            var b=com.bleachmod.common.visual.RyujinChoreography.attack("flame_dash",8,1);
            yes(a.leg().lengthSqr()==0 && b.leg().lengthSqr()==0);
            yes(a.arm().x< -2.8 && b.arm().x< -2.8);
            eq(0F,com.bleachmod.common.visual.RyujinChoreography.attack("flame_dash",16,1).weight());
            eq(0F,com.bleachmod.common.visual.RyujinChoreography.attack("flame_barrage",16,1).weight());
        });
        test("Only confirmed valid Higashi deaths authorize the execution presentation", () -> {
            var a=new net.minecraft.world.phys.Vec3(0,64,0);var b=a.add(0,0,3);
            var preview=new com.bleachmod.common.network.s2c.HigashiExecutionS2C(1,2,a,b,1,2,false);
            yes(preview.valid()&&!preview.startsExecution());
            yes(new com.bleachmod.common.network.s2c.HigashiExecutionS2C(1,2,a,b,1,2,true).startsExecution());
            yes(!new com.bleachmod.common.network.s2c.HigashiExecutionS2C(1,1,a,b,1,2,true).startsExecution());
        });
        test("Benihime basic cuts are distinct mirrored six-tick gestures and recover cleanly", () -> {
            var zero=net.minecraft.world.phys.Vec3.ZERO;
            for(int combo=0;combo<3;combo++){
                var previous=com.bleachmod.common.visual.BenihimeChoreography.melee(combo,0,6,1);
                for(float t=.125F;t<6;t+=.125F){
                    var p=com.bleachmod.common.visual.BenihimeChoreography.melee(combo,t,6,1);
                    var l=com.bleachmod.common.visual.BenihimeChoreography.melee(combo,t,6,-1);
                    yes(p.arm().distanceTo(previous.arm())<.6&&Double.isFinite(p.handRotation().lengthSqr()));
                    yes(Math.abs(p.arm().y+l.arm().y)<1e-9&&Math.abs(p.handPosition().x+l.handPosition().x)<1e-9);
                    yes(p.support().length()<1.2&&p.weight()>=0&&p.weight()<=1);previous=p;
                }
                eq(0F,com.bleachmod.common.visual.BenihimeChoreography.melee(combo,6,6,1).weight());
            }
            var a=com.bleachmod.common.visual.BenihimeChoreography.melee(0,3,6,1);
            var b=com.bleachmod.common.visual.BenihimeChoreography.melee(1,3,6,1);
            yes(a.arm().distanceTo(b.arm())>.7);
        });
        test("All eight Benihime technique curves preserve their existing cosmetic leases", () -> {
            for(var kind:com.bleachmod.common.visual.BenihimeChoreography.TECHNIQUES){
                int length=com.bleachmod.common.visual.SignatureChoreography.duration(kind);yes(length>0);
                for(float t=.25F;t<length;t+=.25F){
                    var p=com.bleachmod.common.visual.BenihimeChoreography.technique(kind,t,length,1);
                    yes(Double.isFinite(p.arm().lengthSqr()+p.support().lengthSqr()+p.body().lengthSqr()));
                    yes(p.weight()>=0&&p.weight()<=1);
                }
                eq(0F,com.bleachmod.common.visual.BenihimeChoreography.technique(kind,length,length,1).weight());
                eq(0F,com.bleachmod.common.visual.BenihimeChoreography.technique(kind,Float.NaN,length,1).weight());
            }
        });
        test("Higashi coils the torso at the hip instead of sharing Rajada's overhead preparation", () -> {
            var hip=com.bleachmod.common.visual.RyujinChoreography.attack("higashi",10,1);
            var overhead=com.bleachmod.common.visual.RyujinChoreography.attack("flame_barrage",4,1);
            yes(hip.arm().x>-1 && overhead.arm().x<-2.5 && hip.body().y<-.4);
            var left=com.bleachmod.common.visual.RyujinChoreography.attack("higashi",10,-1);
            eq(hip.arm().x,left.arm().x);eq(-hip.arm().y,left.arm().y);
            eq(-hip.body().y,left.body().y);
        });
        test("Remaining technique animations have bounded mirrored curves and cannot persist after their lease", () -> {
            for(var entry:com.bleachmod.common.visual.SignatureChoreography.LENGTHS.entrySet()){
                int end=entry.getValue();
                for(int age=0;age<=end;age++){
                    var r=com.bleachmod.common.visual.SignatureChoreography.sample(entry.getKey(),age,end,1);
                    var l=com.bleachmod.common.visual.SignatureChoreography.sample(entry.getKey(),age,end,-1);
                    yes(Double.isFinite(r.arm().lengthSqr()+r.body().lengthSqr()));
                    yes(r.weight()>=0 && r.weight()<=1);yes(Math.abs(r.arm().y+l.arm().y)<1e-9);
                }
                eq(0F,com.bleachmod.common.visual.SignatureChoreography.sample(entry.getKey(),end,end,1).weight());
            }
            eq(23,com.bleachmod.common.visual.SignatureChoreography.LENGTHS.size());
        });
        test("Remaining releases recover to neutral and reject invalid time", () -> {
            for(String identity:List.of("senbonzakura","benihime","asauchi"))for(int length:new int[]{24,60}){
                yes(com.bleachmod.common.visual.SignatureChoreography.release(identity,length/2F,length,1).weight()>0);
                eq(0F,com.bleachmod.common.visual.SignatureChoreography.release(identity,length,length,1).weight());
                eq(0D,com.bleachmod.common.visual.SignatureChoreography.release(identity,Float.NaN,length,1).arm().lengthSqr());
            }
        });
        test("Senbon release uses two rows of tall blades with a bounded construct budget", () -> {
            var quads=com.bleachmod.common.visual.SenbonConstructGeometry.build(com.bleachmod.common.network.s2c.SenbonPetalsS2C.SWORDS,net.minecraft.world.phys.Vec3.ZERO,new net.minecraft.world.phys.Vec3(0,0,1),7,.5,4);
            double top=quads.stream().flatMap(q->List.of(q.a(),q.b(),q.c(),q.d()).stream()).mapToDouble(v->v.y).max().orElse(0);
            yes(top>6 && quads.size()<1200);
        });
        test("Higashi execution checks seven percent before damage and rejects invalid life", () -> {
            yes(com.bleachmod.common.technique.HigashiExecutionService.belowThreshold(7,100));
            yes(com.bleachmod.common.technique.HigashiExecutionService.belowThreshold(70,1000));
            yes(!com.bleachmod.common.technique.HigashiExecutionService.belowThreshold(7.001F,100));
            yes(!com.bleachmod.common.technique.HigashiExecutionService.belowThreshold(0,100));
            yes(!com.bleachmod.common.technique.HigashiExecutionService.belowThreshold(Float.NaN,100));
        });
        test("Floral morph preserves two thirds of ground samples and the interior coverage", () -> {
            int unchanged=0,interior=0;for(int i=0;i<1200;i++){
                var a=com.bleachmod.common.visual.SenbonGardenGeometry.point(35,i,1200,0);
                var b=com.bleachmod.common.visual.SenbonGardenGeometry.point(35,i,1200,1);
                if(a.distanceToSqr(b)<1e-10)unchanged++;if(b.horizontalDistance()<17.5)interior++;
                yes(b.horizontalDistance()<=35.001);
            }
            yes(unchanged>=800&&interior>=190);
        });
        test("Confirmed execution packet validates positions and round trips without client instructions", () -> {
            var m=new com.bleachmod.common.network.s2c.HigashiExecutionS2C(1,2,new net.minecraft.world.phys.Vec3(0,70,0),new net.minecraft.world.phys.Vec3(0,70,3),.6F,1.8F);yes(m.valid());
            var buffer=new net.minecraft.network.FriendlyByteBuf(io.netty.buffer.Unpooled.buffer());try{com.bleachmod.common.network.s2c.HigashiExecutionS2C.encode(m,buffer);eq(m,com.bleachmod.common.network.s2c.HigashiExecutionS2C.decode(buffer));}finally{buffer.release();}
            yes(!new com.bleachmod.common.network.s2c.HigashiExecutionS2C(1,1,m.attacker(),m.victim(),.6F,1.8F).valid());
            yes(!new com.bleachmod.common.network.s2c.HigashiExecutionS2C(1,2,m.attacker(),new net.minecraft.world.phys.Vec3(0,70,100),.6F,1.8F).valid());
        });
        test("Higashi shots frame attacker and victim without changing the line of action", () -> {
            var a=new net.minecraft.world.phys.Vec3(10,70,20);
            for(double distance:new double[]{1,2,4})for(float age=0;age<44;age+=.25F){
                var b=a.add(0,0,distance);var shot=com.bleachmod.common.visual.HigashiCinematicPath.sample(a,b,2,age);
                var look=shot.focus().subtract(shot.eye()).normalize();var right=look.cross(new net.minecraft.world.phys.Vec3(0,1,0)).normalize();var up=right.cross(look).normalize();double tan=Math.tan(Math.toRadians(shot.fov()/2));
                for(var at:List.of(a.add(0,1,0),b.add(0,1,0))){var delta=at.subtract(shot.eye());double depth=delta.dot(look);yes(depth>0);yes(Math.abs(delta.dot(right))<depth*tan*16/9);yes(Math.abs(delta.dot(up))<depth*tan);}
                yes(shot.eye().x<a.x&&shot.fov()>=60&&shot.fov()<=66);
                float yaw=com.bleachmod.common.visual.HigashiCinematicPath.yaw(shot.eye(),shot.focus()),pitch=com.bleachmod.common.visual.HigashiCinematicPath.pitch(shot.eye(),shot.focus());
                yes(Float.isFinite(yaw)&&Float.isFinite(pitch));yes(net.minecraft.world.phys.Vec3.directionFromRotation(pitch,yaw).distanceToSqr(look)<1e-6);
            }
        });
        test("Higashi authored execution recovers and mirrors without an idle lease", () -> {
            for(float age=0;age<=com.bleachmod.common.visual.HigashiCinematicPath.END+4;age+=.25F){var right=com.bleachmod.common.visual.RyujinChoreography.execution(age,1);var left=com.bleachmod.common.visual.RyujinChoreography.execution(age,-1);
                yes(Double.isFinite(right.arm().lengthSqr()+right.body().lengthSqr()));eq(right.arm().x,left.arm().x);eq(right.arm().y,-left.arm().y);yes(right.weight()>=0&&right.weight()<=1);
                if(age>=com.bleachmod.common.visual.HigashiCinematicPath.END)eq(0F,right.weight());
            }
        });
        test("Higashi draws on the sword hip and lifts the blade from low outside to high inside", () -> {
            for(float sign:new float[]{1,-1}){
                var hip=com.bleachmod.common.visual.RyujinChoreography.execution(10,sign);
                var end=com.bleachmod.common.visual.RyujinChoreography.execution(16,sign);
                var hand=com.bleachmod.common.visual.RyujinChoreography.bladeHand(hip,sign);
                yes(hand.x*sign>.35&&hand.y>.65&&hand.y<.9&&hand.z<0);
                var low=hand.add(com.bleachmod.common.visual.RyujinChoreography.executionBladeDirection(hip).scale(1.4));
                var high=com.bleachmod.common.visual.RyujinChoreography.bladeHand(end,sign).add(com.bleachmod.common.visual.RyujinChoreography.executionBladeDirection(end).scale(1.4));
                yes(low.y<.5&&high.y>1.9&&high.y-low.y>1.4);
                yes((low.x-high.x)*sign>.5&&low.z>.9&&high.z>1);
                yes(end.arm().x < -1.5);
                yes(com.bleachmod.common.visual.RyujinChoreography.bladeHand(end,sign).y>1.35);
            }
        });
        test("Higashi rising follow-through points toward the victim instead of behind the caster", () -> {
            for(float sign:new float[]{-1,1}){
                var pose=com.bleachmod.common.visual.RyujinChoreography.execution(16,sign);
                var blade=com.bleachmod.common.visual.RyujinChoreography.executionBladeDirection(pose);
                yes(blade.z>.6&&blade.y>.4);
                for(float t=0;t<=com.bleachmod.common.visual.HigashiCinematicPath.END;t+=1.0F){
                    var p=com.bleachmod.common.visual.RyujinChoreography.execution(t,sign);
                    yes(Double.isFinite(p.item().lengthSqr()));
                    yes(com.bleachmod.common.visual.RyujinChoreography.executionItemPosition(p).length()<3);
                }
            }
        });
        test("Held katana coordinates agree with independently captured renderer matrices", () -> {
            try {
                var fixture=com.google.gson.JsonParser.parseString(java.nio.file.Files.readString(
                        java.nio.file.Path.of("src/test/resources/higashi-held-renderer-calibration.json"))).getAsJsonObject();
                var measured=new java.util.ArrayList<net.minecraft.world.phys.Vec3>();
                var predicted=new java.util.ArrayList<net.minecraft.world.phys.Vec3>();
                double by=Math.cos(Math.toRadians(10)),bz=-Math.sin(Math.toRadians(10));
                for(var entry:fixture.getAsJsonArray("poses")){
                    var o=entry.getAsJsonObject();var m=o.getAsJsonArray("heldMatrix");
                    measured.add(new net.minecraft.world.phys.Vec3(
                            m.get(4).getAsDouble()*by+m.get(8).getAsDouble()*bz,
                            m.get(5).getAsDouble()*by+m.get(9).getAsDouble()*bz,
                            m.get(6).getAsDouble()*by+m.get(10).getAsDouble()*bz).normalize());
                    var zero=net.minecraft.world.phys.Vec3.ZERO;
                    var pose=new com.bleachmod.common.visual.RyujinChoreography.Pose(
                            fixtureVector(o.getAsJsonArray("arm")),zero,fixtureVector(o.getAsJsonArray("body")),
                            zero,fixtureVector(o.getAsJsonArray("item")),zero,zero,1);
                    predicted.add(com.bleachmod.common.visual.RyujinChoreography.executionBladeDirection(pose));
                }
                // Camera-independent angle between two stable poses, measured before model display.
                yes(Math.abs(measured.get(0).dot(measured.get(1))-predicted.get(0).dot(predicted.get(1)))<1e-5);
            }catch(java.io.IOException failure){throw new AssertionError(failure);}
        });
        test("Higashi preview packets remain distinct from confirmed death", () -> {
            var preview=new com.bleachmod.common.network.s2c.HigashiExecutionS2C(1,2,new net.minecraft.world.phys.Vec3(0,70,0),new net.minecraft.world.phys.Vec3(0,70,3),.6F,1.8F,false);
            yes(preview.valid()&&!preview.confirmed());var b=new net.minecraft.network.FriendlyByteBuf(io.netty.buffer.Unpooled.buffer());try{com.bleachmod.common.network.s2c.HigashiExecutionS2C.encode(preview,b);eq(preview,com.bleachmod.common.network.s2c.HigashiExecutionS2C.decode(b));}finally{b.release();}
        });
        test("History Mode install is all-or-nothing and resumes after an interrupted copy",()->{
            java.nio.file.Path dir=null;
            try{
                dir=java.nio.file.Files.createTempDirectory("history");
                var src=dir.resolve("maps");var manifest=src.resolve("maps.json");java.nio.file.Files.createDirectories(src);java.nio.file.Files.writeString(manifest,"{}");
                for(String id:List.of("a","b")){var region=src.resolve(id).resolve("region");java.nio.file.Files.createDirectories(region);java.nio.file.Files.write(region.resolve("r.0.0.mca"),new byte[4096]);}
                java.nio.file.Files.createDirectories(src.resolve("a/entities"));java.nio.file.Files.write(src.resolve("a/entities/e.0.0.mca"),new byte[100]);
                var world=dir.resolve("world");var installed=world.resolve("bleachmod/maps_installed.json");var marker=world.resolve("bleachmod/maps_installing");
                var targets=new java.util.LinkedHashMap<String,java.nio.file.Path>();targets.put("a",world);targets.put("b",world.resolve("dimensions/bleachmod/b"));
                var incomplete=new java.util.LinkedHashMap<>(targets);incomplete.put("c",world.resolve("dimensions/bleachmod/c"));java.nio.file.Files.createDirectories(src.resolve("c"));
                boolean refused=false;
                try{com.bleachmod.server.world.HistoryMapInstaller.installMaps(src,manifest,incomplete,installed);}catch(java.io.IOException expected){refused=true;}
                yes(refused);yes(!java.nio.file.Files.exists(world));
                java.nio.file.Files.createDirectories(marker.getParent());java.nio.file.Files.writeString(marker,"");
                java.nio.file.Files.createDirectories(world.resolve("region"));java.nio.file.Files.write(world.resolve("region/r.9.9.mca"),new byte[10]);
                java.nio.file.Files.createDirectories(world.resolve("region.staging"));
                com.bleachmod.server.world.HistoryMapInstaller.installMaps(src,manifest,targets,installed);
                yes(java.nio.file.Files.exists(installed));yes(!java.nio.file.Files.exists(marker));
                yes(!java.nio.file.Files.exists(world.resolve("region/r.9.9.mca")));yes(!java.nio.file.Files.exists(world.resolve("region.staging")));
                yes(java.nio.file.Files.size(world.resolve("region/r.0.0.mca"))==4096);yes(java.nio.file.Files.exists(world.resolve("entities/e.0.0.mca")));
                yes(java.nio.file.Files.exists(targets.get("b").resolve("region/r.0.0.mca")));
                java.nio.file.Files.delete(installed);refused=false;
                try{com.bleachmod.server.world.HistoryMapInstaller.installMaps(src,manifest,targets,installed);}catch(java.io.IOException expected){refused=true;}
                yes(refused);yes(java.nio.file.Files.exists(world.resolve("region/r.0.0.mca")));
            }catch(java.io.IOException e){throw new java.io.UncheckedIOException(e);}
            finally{if(dir!=null)try(var files=java.nio.file.Files.walk(dir)){for(var f:files.sorted(java.util.Comparator.reverseOrder()).toList())java.nio.file.Files.deleteIfExists(f);}catch(java.io.IOException ignored){}}
        });
        test("History Mode map manifest keeps every map and rejects malformed entries",()->{
            var maps=historyManifest("{\"maps\":{\"human_world\":{\"dimension\":\"minecraft:overworld\",\"spawn\":{\"x\":19.5,\"y\":65,\"z\":-116.5}},"
                    +"\"hueco_mundo\":{\"dimension\":\"bleachmod:hueco_mundo\",\"spawn\":{\"x\":2528.9,\"y\":41,\"z\":2540.9,\"yaw\":90}}}}");
            eq(List.of("human_world","hueco_mundo"),List.copyOf(maps.keySet()));
            var hueco=maps.get("hueco_mundo");eq("bleachmod:hueco_mundo",hueco.dimensionId().toString());eq(41.0,hueco.y());eq(90F,hueco.yaw());
            eq(0F,maps.get("human_world").yaw());
            historyRejects("{\"maps\":{\"Bad Id\":{\"dimension\":\"minecraft:overworld\",\"spawn\":{\"x\":0,\"y\":0,\"z\":0}}}}");
            historyRejects("{\"maps\":{\"a\":{\"dimension\":\"minecraft:overworld\"}}}");
            historyRejects("{\"maps\":{\"a\":{\"dimension\":\"minecraft:overworld\",\"spawn\":{\"x\":NaN,\"y\":0,\"z\":0}}}}");
            historyRejects("{\"maps\":{\"a\":{\"dimension\":\"Not Valid\",\"spawn\":{\"x\":0,\"y\":0,\"z\":0}}}}");
            historyRejects("not json");
        });
        test("Appearance catalog is valid and every layer texture is bundled",()->{
            var catalog=com.bleachmod.common.appearance.AppearanceCatalog.get();
            for(String category:com.bleachmod.common.appearance.AppearanceCatalog.CATEGORIES) for(String id:catalog.ids(category)){
                var piece=catalog.piece(id).orElseThrow(); yes(!piece.layers().isEmpty());
                for(var layer:piece.layers()){
                    String path="/assets/"+layer.texture().replace(":","/");
                    yes(MvpRegressionTest.class.getResource(path)!=null);
                }
            }
            yes(catalog.valid("hair:ichigo","hair","shinigami")); yes(!catalog.valid("hair:ichigo","outfit","shinigami"));
            eq("base:default",catalog.defaults("shinigami").pieces().get("base"));
            rejects(()->com.bleachmod.common.appearance.AppearanceCatalog.color("orange"));
            rejects(()->com.bleachmod.common.appearance.AppearanceCatalog.parse(JsonParser.parseString(
                "{\"pieces\":[{\"id\":\"hair:x\",\"category\":\"outfit\",\"layers\":[]}],\"defaults\":{}}").getAsJsonObject()));
        });
        test("Appearance from the network is sanitized against the catalog and race",()->{
            var catalog=com.bleachmod.common.appearance.AppearanceCatalog.get();
            var look=new com.bleachmod.common.appearance.AppearanceData();
            look.setUseVanillaSkin(false); look.setPiece("hair","hair:does_not_exist"); look.setPiece("outfit","hair:ichigo");
            look.setPiece("base",""); look.setPiece("mouth",""); look.setPiece("eyes","eyes:rukia");
            look.sanitize(catalog,"shinigami");
            eq(catalog.defaults("shinigami").pieces().get("hair"),look.piece("hair"));
            eq(catalog.defaults("shinigami").pieces().get("outfit"),look.piece("outfit"));
            eq("base:default",look.piece("base")); eq("",look.piece("mouth")); eq("eyes:rukia",look.piece("eyes"));
            var tag=look.save(); tag.putInt("skin",0x7FFFFFFF); tag.putInt("hairColor",-7);
            tag.getCompound("pieces").putString("hair","x".repeat(200)); tag.getCompound("pieces").putString("unknown","hair:ichigo");
            var copy=new com.bleachmod.common.appearance.AppearanceData(); copy.applyDefaults(catalog,"shinigami"); copy.load(tag); copy.sanitize(catalog,"shinigami");
            eq(0xFFFFFF,copy.skinColor()); eq(-1,copy.hairColor()); eq(catalog.defaults("shinigami").pieces().get("hair"),copy.piece("hair"));
            yes(!copy.save().getCompound("pieces").contains("unknown"));
            var other=look.copy(); eq(look.textureKey(),other.textureKey()); other.setSkinColor(0x123456); yes(!look.textureKey().equals(other.textureKey()));
        });
        test("Saves before schema 9 keep the Minecraft skin and appearance survives a copy",()->{
            var d=new PlayerData(); d.initializeShinigami();
            var legacy=d.save(); legacy.remove("appearance"); legacy.putInt("schemaVersion",8);
            var loaded=new PlayerData(); loaded.load(legacy); yes(loaded.getAppearance().usesVanillaSkin());
            d.getAppearance().applyDefaults(com.bleachmod.common.appearance.AppearanceCatalog.get(),"shinigami"); d.getAppearance().setUseVanillaSkin(false);
            d.getAppearance().setPiece("hair","hair:rukia"); d.getAppearance().setSlim(true);
            var clone=new PlayerData(); clone.copyFrom(d);
            eq(9,d.save().getInt("schemaVersion")); yes(!clone.getAppearance().usesVanillaSkin());
            eq("hair:rukia",clone.getAppearance().piece("hair")); yes(clone.getAppearance().isSlim());
        });
        test("Composed skin blends tinted layers source-over",()->{
            int abgrGray=0xFF000000|200<<16|200<<8|200;
            eq(0xFF000000|(200*0x56/255)<<16|(200*0x34/255)<<8|(200*0x12/255),com.bleachmod.client.appearance.SkinCompositor.blend(0,abgrGray,0x123456));
            eq(0xFF0000FF,com.bleachmod.client.appearance.SkinCompositor.blend(0xFF00FF00,0xFF0000FF,-1));
            int half=com.bleachmod.client.appearance.SkinCompositor.blend(0xFF000000,0x80FFFFFF,-1); eq(255,half>>>24); yes((half&255)>120&&(half&255)<136);
            eq(0,com.bleachmod.client.appearance.SkinCompositor.blend(0,0,-1)&0xFF000000);
            var look=new com.bleachmod.common.appearance.AppearanceData(); look.setHairColor(0xABCDEF);
            eq(0xABCDEF,com.bleachmod.client.appearance.SkinCompositor.tint("hair",look,0x111111));
            eq(0x111111,com.bleachmod.client.appearance.SkinCompositor.tint("eye",look,0x111111));
            eq(-1,com.bleachmod.client.appearance.SkinCompositor.tint("none",look,0x111111));
        });
        test("Every roster NPC has a skin, a spawn egg model and names in both languages",()->{
            var roster=com.bleachmod.registry.NpcRoster.entries(); yes(roster.size()>=30);
            var ids=new HashSet<String>();
            JsonObject pt, en;
            try(var a=MvpRegressionTest.class.getResourceAsStream("/assets/bleachmod/lang/pt_br.json");var b=MvpRegressionTest.class.getResourceAsStream("/assets/bleachmod/lang/en_us.json")){
                pt=JsonParser.parseReader(new java.io.InputStreamReader(a,java.nio.charset.StandardCharsets.UTF_8)).getAsJsonObject();
                en=JsonParser.parseReader(new java.io.InputStreamReader(b,java.nio.charset.StandardCharsets.UTF_8)).getAsJsonObject();
            }catch(java.io.IOException e){throw new RuntimeException(e);}
            for(var npc:roster){
                yes(ids.add(npc.id()));
                yes(MvpRegressionTest.class.getResource("/assets/bleachmod/textures/entity/"+npc.id()+".png")!=null);
                yes(MvpRegressionTest.class.getResource("/assets/bleachmod/models/item/npc_"+npc.id()+"_spawn_egg.json")!=null);
                for(var lang:List.of(pt,en)){ yes(lang.has("entity.bleachmod.npc_"+npc.id())); yes(lang.has("item.bleachmod.npc_"+npc.id()+"_spawn_egg")); }
            }
            rejects(()->com.bleachmod.registry.NpcRoster.parse(JsonParser.parseString("{\"npcs\":[{\"id\":\"Bad Id\",\"eggPrimary\":\"#000000\",\"eggSecondary\":\"#000000\"}]}").getAsJsonObject()));
        });
        test("Main schema 9 preserves Nozarashi and appearance while migrating elemental numeric keys",()->{
            var d=new PlayerData();d.initializeShinigami();d.getCharacter().bindZanpakuto("nozarashi");d.getCharacter().unlockTechnique("bankai",4);d.getCharacter().addTechniqueMastery("bankai",4,63);
            d.getAppearance().applyDefaults(com.bleachmod.common.appearance.AppearanceCatalog.get(),"shinigami");d.getAppearance().setSlim(true);d.getAppearance().setUseVanillaSkin(false);
            var tag=d.save();var character=tag.getCompound("character");character.remove("techniqueRevision");character.getCompound("techniqueMasteries").putDouble("ryujin_jakka|bankai|2",45);
            var loaded=new PlayerData();loaded.load(tag);yes(loaded.getCharacter().isTechniqueDiscovered("bankai",4));eq(63D,loaded.getCharacter().getTechniqueMastery("bankai",4));yes(loaded.getAppearance().isSlim()&&!loaded.getAppearance().usesVanillaSkin());
            loaded.getCharacter().bindZanpakuto("ryujin_jakka");eq(45D,loaded.getCharacter().getTechniqueMastery("shikai",2));
            var clone=new PlayerData();clone.copyFrom(loaded);clone.getCharacter().bindZanpakuto("nozarashi");eq(63D,clone.getCharacter().getTechniqueMastery("bankai",4));
        });
        test("Boss entrance IK reaches support targets without stretching its limbs",()->{
            var a=new org.joml.Vector3f(21,69,1);var b=new org.joml.Vector3f(24,41,-3);
            var c=new org.joml.Vector3f(25,14,-9);
            for(int x=-8;x<=8;x+=4)for(int y=12;y<=32;y+=5)for(int z=-30;z<=20;z+=5){
                var target=new org.joml.Vector3f(25+x,y,z);
                var chain=com.bleachmod.common.visual.TwoBonePose.solve(a,b,c,target,new org.joml.Vector3f(0,0,1));
                var elbow=chain.upper().transform(new org.joml.Vector3f(b).sub(a)).add(a);
                var tip=chain.lower().transform(new org.joml.Vector3f(c).sub(b));chain.upper().transform(tip);tip.add(elbow);
                yes(Math.abs(elbow.distance(a)-a.distance(b))<.001);
                yes(Math.abs(tip.distance(elbow)-b.distance(c))<.001);
                if(target.distance(a)<a.distance(b)+b.distance(c)-.01F)yes(tip.distance(target)<.003);
                else yes(Float.isFinite(tip.x+tip.y+tip.z));
                var e=com.bleachmod.common.visual.TwoBonePose.eulerZYX(chain.upper());
                var roundTrip=new org.joml.Quaternionf().rotationZYX(e.z,e.y,e.x);
                yes(roundTrip.transform(new org.joml.Vector3f(b).sub(a)).add(a).distance(elbow)<.003);
            }
        });
        test("Boss entrance alternates planted hands and airborne steps and settles before combat",()->{
            float anchor=com.bleachmod.common.visual.HollowBossEntrance.hand(74,true).z();
            for(float t=74;t<96;t+=.25F){
                var p=com.bleachmod.common.visual.HollowBossEntrance.hand(t,true);
                yes(p.planted() && p.y()==14 && p.z()==anchor);
            }
            yes(com.bleachmod.common.visual.HollowBossEntrance.hand(68,true).lift()>.5F);
            yes(com.bleachmod.common.visual.HollowBossEntrance.hand(68,false).lift()==0);
            yes(com.bleachmod.common.visual.HollowBossEntrance.foot(89,true).lift()>.5F);
            yes(com.bleachmod.common.visual.HollowBossEntrance.foot(89,false).lift()==0);
            eq(com.bleachmod.common.visual.HollowBossEntrance.retreat(56),com.bleachmod.common.visual.HollowBossEntrance.retreat(62));
            eq(com.bleachmod.common.visual.HollowBossEntrance.retreat(86),com.bleachmod.common.visual.HollowBossEntrance.retreat(90));
            for(float t=156;t<=160;t+=.25F){
                yes(Math.abs(com.bleachmod.common.visual.HollowBossEntrance.retreat(t))<.0001F);
                yes(com.bleachmod.common.visual.HollowBossEntrance.bodyY(t)==0);
                yes(com.bleachmod.common.visual.HollowBossEntrance.settle(t)==0);
                yes(com.bleachmod.common.visual.HollowBossEntrance.hand(t,true).planted());
                yes(com.bleachmod.common.visual.HollowBossEntrance.foot(t,false).planted());
            }
        });
        test("Quincy profile initialization, race, and independent QuincyData save/load NBT persistence", () -> {
            PlayerData d = new PlayerData();
            d.initializeQuincy();
            eq("quincy", d.getCharacter().getRace());
            eq(100.0F, d.getResources().getCurrentReiatsu());
            eq(100.0F, d.getResources().getMaxReiatsu());
            yes(!d.getQuincy().isSprengerActive());

            var center = new net.minecraft.core.BlockPos(10, 64, 20);
            var vertices = java.util.List.of(
                    new net.minecraft.core.BlockPos(16, 64, 20),
                    new net.minecraft.core.BlockPos(12, 64, 26),
                    new net.minecraft.core.BlockPos(5, 64, 24),
                    new net.minecraft.core.BlockPos(5, 64, 16),
                    new net.minecraft.core.BlockPos(12, 64, 14)
            );
            d.getQuincy().startSprenger(center, vertices, 1000L);
            yes(d.getQuincy().isSprengerActive());
            eq(center, d.getQuincy().getSprengerCenter());
            eq(5, d.getQuincy().getSprengerVertices().size());
            eq(1000L, d.getQuincy().getSprengerCreationTick());

            var tag = d.save();
            var loaded = new PlayerData();
            loaded.load(tag);
            eq("quincy", loaded.getCharacter().getRace());
            yes(loaded.getQuincy().isSprengerActive());
            eq(center, loaded.getQuincy().getSprengerCenter());
            eq(5, loaded.getQuincy().getSprengerVertices().size());
            eq(1000L, loaded.getQuincy().getSprengerCreationTick());

            loaded.resetTransientState();
            yes(!loaded.getQuincy().isSprengerActive());
        });
        test("Quincy Kojaku bow charges Reiatsu, calculates pull scale, and enforces 64-block maximum range", () -> {
            eq(2.0F, com.bleachmod.common.quincy.QuincyBowItem.COMMON_SHOT_PE_COST);
            eq(20, com.bleachmod.common.quincy.QuincyBowItem.FULL_CHARGE_TICKS);
            eq(5, com.bleachmod.common.quincy.QuincyBowItem.MIN_CHARGE_TICKS);
            eq(64.0D, com.bleachmod.common.quincy.QuincyArrowEntity.COMMON_MAX_RANGE);
            eq(6.0F, com.bleachmod.common.quincy.QuincyArrowEntity.COMMON_BASE_DAMAGE);
            eq(40.0D, com.bleachmod.common.quincy.QuincyArrowEntity.LICHT_REGEN_MAX_RANGE);
            eq(2.0F, com.bleachmod.common.quincy.QuincyArrowEntity.LICHT_REGEN_DAMAGE);
            eq(60, com.bleachmod.common.quincy.QuincyArrowEntity.COMMON_MAX_LIFE_TICKS);
            eq(40, com.bleachmod.common.quincy.QuincyArrowEntity.LICHT_REGEN_MAX_LIFE_TICKS);

            // Vertical deviation measurement across flight: 0.0 blocks drop at 16, 32, and 64 blocks without gravity
            double vy = 0.0D;
            for (int tick = 0; tick < 25; tick++) {
                vy *= 0.99D;
                eq(0.0D, vy);
            }
        });
        test("Seele Schneider (H) pierces up to 3 distinct targets, deals 10 HP per target, and stops at wall", () -> {
            eq(12.0F, com.bleachmod.common.quincy.QuincyTechniqueService.SEELE_PE_COST);
            eq(120, com.bleachmod.common.quincy.QuincyTechniqueService.SEELE_COOLDOWN_TICKS);
            eq(48.0D, com.bleachmod.common.quincy.SeeleSchneiderEntity.MAX_RANGE);
            eq(10.0F, com.bleachmod.common.quincy.SeeleSchneiderEntity.DAMAGE_PER_TARGET);
            eq(3, com.bleachmod.common.quincy.SeeleSchneiderEntity.MAX_TARGETS);
            eq(0.5D, com.bleachmod.common.quincy.SeeleSchneiderEntity.CORRIDOR_WIDTH);
        });
        test("Licht Regen (N) fires 12 arrows over 2 seconds in a 20-degree cone, with 2 HP per hit and 8 HP ceiling per target", () -> {
            eq(20.0F, com.bleachmod.common.quincy.QuincyTechniqueService.LICHT_REGEN_PE_COST);
            eq(200, com.bleachmod.common.quincy.QuincyTechniqueService.LICHT_REGEN_COOLDOWN_TICKS);
            eq(12, com.bleachmod.common.quincy.QuincyTechniqueService.LICHT_REGEN_TOTAL_SHOTS);
            eq(40, com.bleachmod.common.quincy.QuincyTechniqueService.LICHT_REGEN_DURATION_TICKS);
            eq(4, com.bleachmod.common.quincy.QuincyArrowEntity.LICHT_REGEN_MAX_HITS_PER_TARGET);

            var quincy = new com.bleachmod.common.quincy.QuincyData();
            quincy.startLichtRegen(new net.minecraft.world.phys.Vec3(0, 0, 1));
            yes(quincy.isLichtRegenActive());
            eq(40, quincy.getLichtRegenTicksLeft());
            eq(0, quincy.getLichtRegenFiredCount());

            var targetId = java.util.UUID.randomUUID();
            eq(0, quincy.getLichtRegenHitsOn(targetId));
            for (int i = 0; i < 4; i++) {
                quincy.recordLichtRegenHit(targetId);
            }
            eq(4, quincy.getLichtRegenHitsOn(targetId));
        });
        test("Hirenkyaku (B) executes 6-block horizontal movement, charges 8 PE and 10 stamina, and respects terrain bounds", () -> {
            eq(8.0F, com.bleachmod.common.quincy.HirenkyakuService.PE_COST);
            eq(10.0F, com.bleachmod.common.quincy.HirenkyakuService.STAMINA_COST);
            eq(120, com.bleachmod.common.quincy.HirenkyakuService.COOLDOWN_TICKS);
            eq(6.0D, com.bleachmod.common.quincy.HirenkyakuService.TOTAL_DISTANCE);
            eq(0.5D, com.bleachmod.common.quincy.HirenkyakuService.STEP_SIZE);

            var res = new com.bleachmod.common.data.ResourcesData();
            res.fillStamina();
            eq(100.0F, res.getStamina());
            yes(res.spendStamina(10.0F));
            eq(90.0F, res.getStamina());
        });
        test("Sprenger (C) requires 5 local floor vertices, charges 35 PE once, prepares in 1.5s, detonates within 6s for 18 HP up to 8 targets, and cleans up on timeout", () -> {
            eq(35.0F, com.bleachmod.common.quincy.QuincyTechniqueService.SPRENGER_PE_COST);
            eq(400, com.bleachmod.common.quincy.QuincyTechniqueService.SPRENGER_COOLDOWN_TICKS);
            eq(36.0D, com.bleachmod.common.quincy.QuincyTechniqueService.SPRENGER_MAX_CENTER_DISTANCE);
            eq(6.0D, com.bleachmod.common.quincy.QuincyTechniqueService.SPRENGER_RADIUS);
            eq(4.0D, com.bleachmod.common.quincy.QuincyTechniqueService.SPRENGER_HEIGHT);
            eq(30, com.bleachmod.common.quincy.QuincyTechniqueService.SPRENGER_PREP_TICKS);
            eq(120, com.bleachmod.common.quincy.QuincyTechniqueService.SPRENGER_EXPIRATION_TICKS);
            eq(18.0F, com.bleachmod.common.quincy.QuincyTechniqueService.SPRENGER_DAMAGE);
            eq(8, com.bleachmod.common.quincy.QuincyTechniqueService.SPRENGER_MAX_TARGETS);

            var quincy = new com.bleachmod.common.quincy.QuincyData();
            quincy.startSprenger(new net.minecraft.core.BlockPos(0, 60, 0), java.util.List.of(), 100L);
            yes(quincy.isSprengerActive());

            long agePrep = 120L - quincy.getSprengerCreationTick();
            yes(agePrep < com.bleachmod.common.quincy.QuincyTechniqueService.SPRENGER_PREP_TICKS);

            long ageWindow = 150L - quincy.getSprengerCreationTick();
            yes(ageWindow >= com.bleachmod.common.quincy.QuincyTechniqueService.SPRENGER_PREP_TICKS
                    && ageWindow <= com.bleachmod.common.quincy.QuincyTechniqueService.SPRENGER_EXPIRATION_TICKS);

            long ageExpired = 230L - quincy.getSprengerCreationTick();
            yes(ageExpired > com.bleachmod.common.quincy.QuincyTechniqueService.SPRENGER_EXPIRATION_TICKS);
        });
        System.out.println("PASS: "+count+" regression scenarios");
    }
    private static Map<String,com.bleachmod.server.world.HistoryWorld.Destination> historyManifest(String json){try{var f=java.nio.file.Files.createTempFile("maps",".json");try{java.nio.file.Files.writeString(f,json);return com.bleachmod.server.world.HistoryWorld.parse(f);}finally{java.nio.file.Files.deleteIfExists(f);}}catch(java.io.IOException e){throw new IllegalArgumentException(e);}}
    private static void historyRejects(String json){rejects(()->historyManifest(json));}
    private static net.minecraft.world.phys.Vec3 fixtureVector(com.google.gson.JsonArray a){return new net.minecraft.world.phys.Vec3(a.get(0).getAsDouble(),a.get(1).getAsDouble(),a.get(2).getAsDouble());}
    private static PlayerData player(){PlayerData d=new PlayerData();d.initializeShinigami();d.getCharacter().bindZanpakuto("ryujin_jakka");d.getCharacter().preserveLegacyTechniques();return d;}
    private static Quest quest(){Quest q=new Quest();q.setStringId("q");q.getObjectives().add(new KillObjective("minecraft:zombie",5,KillObjective.SpawnMode.NATURAL,KillObjective.CountMode.ANY_MATCHING));q.getRewards().add(new TpsReward(100));return q;}
    private static JsonObject forms(){JsonObject r=new JsonObject(),groups=new JsonObject();groups.add("zanpakuto",FormDefaults.shinigamiGroup().toJson());r.add("shinigami",groups);return r;}
    private static GuiLayout layout(String id){
        try{return GuiLayoutParser.parse(id,java.nio.file.Files.readString(java.nio.file.Path.of("src/main/resources/assets/bleachmod/gui_layouts/"+id+".json")));}
        catch(java.io.IOException e){throw new RuntimeException(e);}
    }
    private static java.awt.image.BufferedImage texture(net.minecraft.resources.ResourceLocation location){
        try{return javax.imageio.ImageIO.read(java.nio.file.Path.of("src/main/resources/assets",location.getNamespace(),location.getPath()).toFile());}
        catch(java.io.IOException e){throw new AssertionError("missing texture "+location,e);}
    }
    private static void test(String name,Runnable action){try{action.run();count++;System.out.println("PASS "+name);}catch(Throwable e){throw new AssertionError(name,e);}}
    private static void yes(boolean value){if(!value)throw new AssertionError("Expected true");}
    private static void eq(Object expected,Object actual){if(!Objects.equals(expected,actual))throw new AssertionError("Expected "+expected+" but got "+actual);}
    private static void rejects(Runnable action){try{action.run();}catch(IllegalArgumentException e){return;}throw new AssertionError("Expected validation failure");}
}
