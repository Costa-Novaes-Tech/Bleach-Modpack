package com.bleachmod.client.cinematic;

import com.bleachmod.Reference;
import com.bleachmod.entity.HollowBossEntity;
import com.bleachmod.entity.HollowBossEntity.Attack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.world.level.*;
import net.minecraft.world.level.levelgen.WorldOptions;
import net.minecraft.world.level.levelgen.presets.WorldPresets;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

/** Reproducible framebuffer captures from the real Forge client, in new disposable saves. */
@Mod.EventBusSubscriber(modid=Reference.MOD_ID, value=Dist.CLIENT)
public final class BossQaBootstrap {
    private static boolean created;
    private static int ticks;
    private static String capture;
    private static File evidence;
    private BossQaBootstrap() { }

    @SubscribeEvent public static void tick(TickEvent.ClientTickEvent event) {
        var mc = Minecraft.getInstance();
        if (event.phase != TickEvent.Phase.END || net.minecraftforge.fml.loading.FMLEnvironment.production
                || !Boolean.getBoolean("bleachmod.bossQa")
                || !mc.gameDirectory.getAbsolutePath().replace('\\', '/').contains("/run/boss-qa/")) return;
        if (!created && mc.screen instanceof TitleScreen) {
            created = true;
            String name = "Boss-QA-" + System.currentTimeMillis();
            evidence = new File(mc.gameDirectory, "boss-evidence/" + name);
            var rules = new GameRules();
            rules.getRule(GameRules.RULE_DOMOBSPAWNING).set(false, null);
            rules.getRule(GameRules.RULE_DAYLIGHT).set(false, null);
            rules.getRule(GameRules.RULE_WEATHER_CYCLE).set(false, null);
            var settings = new LevelSettings(name, GameType.CREATIVE, false, net.minecraft.world.Difficulty.NORMAL,
                    true, rules, WorldDataConfiguration.DEFAULT);
            mc.createWorldOpenFlows().createFreshLevel(name, settings, new WorldOptions(102026, false, false),
                    registry -> registry.registryOrThrow(net.minecraft.core.registries.Registries.WORLD_PRESET)
                            .getHolderOrThrow(WorldPresets.FLAT).value().createWorldDimensions());
        }
        if (mc.player == null || mc.level == null || !mc.hasSingleplayerServer()
                || !mc.getSingleplayerServer().getWorldData().getLevelName().startsWith("Boss-QA-")
                || !com.bleachmod.common.data.PlayerCapability.get(mc.player).map(d -> d.getStatus().hasCreatedCharacter()).orElse(false)) return;
        ticks++;
        if (ticks == 30) {
            mc.setScreen(null);
            mc.options.pauseOnLostFocus = false;
            mc.options.hideGui = true;
            mc.options.renderDistance().set(6);
            mc.getTutorial().setStep(net.minecraft.client.tutorial.TutorialSteps.NONE);
            mc.getTutorial().stop();
            mc.getToasts().clear();
            moveCamera(1.6, 3.2, 10.5, 1.6, 2.7, .5);
        }
        if (ticks == 45) startEntrance();
        if (ticks >= 45 && ticks <= 215 && ticks % 2 == 0) capture = "entrance-" + ticks;
        if (ticks == 235) {
            mc.options.hideGui=false;
            moveCamera(7,3.8,10.5,.5,2.7,-.5);
        }
        if (ticks == 245) startEntrance();
        if (ticks >= 245 && ticks <= 415 && ticks % 2 == 0) capture = "entrance-side-" + ticks;
        if (ticks == 430) { mc.options.hideGui=true;moveCamera(1.6,3.2,10.5,1.6,2.7,.5); }
        if (ticks < 450) return;
        int sceneTick = ticks - 400;
        if (sceneTick == 100) capture = "01-front-ichigo-scale";
        if (sceneTick == 130) moveCamera(10.5, 3.3, 4.5, .5, 2.7, .5);
        if (sceneTick == 190) capture = "02-side-hunch-arms";
        if (sceneTick == 220) moveCamera(.5, 3.5, -10.5, .5, 2.7, .5);
        if (sceneTick == 280) capture = "03-rear-dorsal-fin";
        if (sceneTick == 300) moveCamera(4, 4.7, 7, .5, 4.4, .5);
        if (sceneTick == 350) capture = "04-mask-detail";
        if (sceneTick == 365) { resetActor(); moveCamera(12, 6.3, 13, .5, 2.4, 2); }
        if (sceneTick == 390) startAttack(Attack.MELEE, 2.7);
        if (sceneTick >= 390 && sceneTick <= 450 && sceneTick % 2 == 0) capture = "melee-" + sceneTick;
        if (sceneTick == 475) { resetActor(); moveCamera(13, 6.5, 13, .5, 2.5, 5); }
        if (sceneTick == 490) startAttack(Attack.CHARGE, 8);
        if (sceneTick >= 490 && sceneTick <= 570 && sceneTick % 2 == 0) capture = "charge-" + sceneTick;
        if (sceneTick == 605) { resetActor(); moveCamera(9, 4.2, 7.5, .5, 2.1, 2); }
        if (sceneTick == 630) startAttack(Attack.SLAM, 2.8);
        if (sceneTick >= 630 && sceneTick <= 710) capture = "slam-" + sceneTick;
        if (sceneTick == 745) {
            moveCamera(12, 4.8, 10, .5, 2.5, 2.5);
            startWalk();
        }
        if (sceneTick >= 760 && sceneTick <= 850 && sceneTick % 3 == 0) capture = "walk-" + sceneTick;
        if (sceneTick == 870) com.mojang.logging.LogUtils.getLogger().info("Boss QA complete: {}", evidence);
        if (sceneTick == 910 && Boolean.getBoolean("bleachmod.bossQaExit")) mc.stop();
    }

    /** A fresh actor exercises the same entry path as a quest spawn, without replay hooks. */
    private static void startEntrance() {
        var mc=Minecraft.getInstance();var uuid=mc.player.getUUID();
        mc.getSingleplayerServer().execute(() -> {
            var player=mc.getSingleplayerServer().getPlayerList().getPlayer(uuid);
            if(player==null)return;
            var level=player.serverLevel();
            level.getEntitiesOfClass(HollowBossEntity.class,player.getBoundingBox().inflate(60),
                    b->b.getTags().contains("boss_qa_actor")).forEach(net.minecraft.world.entity.Entity::discard);
            var boss=com.bleachmod.registry.ModEntities.HOLLOW_BOSS.get().create(level);
            int y=level.getHeight(net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING,0,0);
            boss.setPos(.5,y,.5);boss.setYRot(0);boss.setYBodyRot(0);boss.setYHeadRot(0);
            boss.setNoAi(true);boss.setPersistenceRequired();boss.addTag("boss_qa_actor");
            boss.goalSelector.removeAllGoals(goal->true);boss.targetSelector.removeAllGoals(goal->true);
            level.addFreshEntity(boss);
        });
    }

    private static void resetActor() {
        var mc = Minecraft.getInstance();
        var server = mc.getSingleplayerServer();
        var uuid = mc.player.getUUID();
        server.execute(() -> {
            var player = server.getPlayerList().getPlayer(uuid);
            if (player == null) return;
            int y = player.serverLevel().getHeight(net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING, 0, 0);
            for (var boss : player.serverLevel().getEntitiesOfClass(HollowBossEntity.class,
                    player.getBoundingBox().inflate(60), mob -> mob.getTags().contains("boss_qa_actor"))) {
                boss.setNoAi(true); boss.setTarget(null); boss.getNavigation().stop();
                boss.setPos(.5, y, .5);
                boss.setYRot(0); boss.setYBodyRot(0); boss.setYHeadRot(0);
                boss.setDeltaMovement(net.minecraft.world.phys.Vec3.ZERO);
            }
        });
    }

    private static void startAttack(Attack attack, double distance) {
        var mc = Minecraft.getInstance();
        var server = mc.getSingleplayerServer();
        var uuid = mc.player.getUUID();
        server.execute(() -> {
            var player = server.getPlayerList().getPlayer(uuid);
            if (player == null) return;
            var level = player.serverLevel();
            for (var old : level.getEntitiesOfClass(net.minecraft.world.entity.Mob.class,
                    player.getBoundingBox().inflate(60), mob -> mob.getTags().contains("boss_qa_target"))) old.discard();
            int y = level.getHeight(net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING, 0, 0);
            for (var boss : level.getEntitiesOfClass(HollowBossEntity.class, player.getBoundingBox().inflate(60),
                    mob -> mob.getTags().contains("boss_qa_actor"))) {
                boss.setNoAi(true);
                boss.cancelBossAttack();
                boss.getNavigation().stop();
                boss.setTarget(null);
                boss.setPos(.5, y, .5);
                boss.setYRot(0); boss.setYBodyRot(0); boss.setYHeadRot(0);
                boss.setDeltaMovement(net.minecraft.world.phys.Vec3.ZERO);
                boss.setOnGround(true);
                var target = net.minecraft.world.entity.EntityType.IRON_GOLEM.create(level);
                target.addTag("boss_qa_target");
                target.setNoAi(true);
                target.setNoGravity(true);
                target.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH).setBaseValue(500);
                target.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.KNOCKBACK_RESISTANCE).setBaseValue(1);
                target.setHealth(500);
                target.setPos(.5, y, .5 + distance);
                level.addFreshEntity(target);
                boss.setNoAi(false);
                boss.setTarget(target);
                boolean started = boss.beginAttack(attack, target);
                com.mojang.logging.LogUtils.getLogger().info("Boss QA start {} accepted={} tick={}", attack, started, boss.tickCount);
            }
        });
    }

    private static void startWalk() {
        var mc = Minecraft.getInstance();
        var uuid = mc.player.getUUID();
        mc.getSingleplayerServer().execute(() -> {
            var player = mc.getSingleplayerServer().getPlayerList().getPlayer(uuid);
            if (player == null) return;
            for (var boss : player.serverLevel().getEntitiesOfClass(HollowBossEntity.class, player.getBoundingBox().inflate(60),
                    mob -> mob.getTags().contains("boss_qa_actor"))) {
                boss.cancelBossAttack(); boss.setTarget(null); boss.setNoAi(false);
                boss.setPos(.5, boss.getY(), .5);
                boss.getNavigation().moveTo(.5, boss.getY(), 10.5, 1);
            }
        });
    }

    private static void moveCamera(double x, double eyeY, double z, double tx, double ty, double tz) {
        var mc = Minecraft.getInstance();
        var server = mc.getSingleplayerServer();
        var uuid = mc.player.getUUID();
        server.execute(() -> {
            var player = server.getPlayerList().getPlayer(uuid);
            if (player == null) return;
            int y = player.serverLevel().getHeight(net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING, 0, 0);
            double dx = tx - x, dz = tz - z;
            float yaw = (float)Math.toDegrees(Math.atan2(-dx, dz));
            float pitch = (float)-Math.toDegrees(Math.atan2(ty - eyeY, Math.hypot(dx, dz)));
            player.teleportTo(player.serverLevel(), x, y + eyeY - player.getEyeHeight(), z, yaw, pitch);
            player.getAbilities().flying = true;
            player.onUpdateAbilities();
        });
    }

    @SubscribeEvent public static void render(TickEvent.RenderTickEvent event) {
        if (event.phase != TickEvent.Phase.END || capture == null || evidence == null) return;
        var mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null || mc.screen != null) return;
        String name = capture + ".png";
        capture = null;
        try {
            Files.createDirectories(evidence.toPath());
            Screenshot.grab(evidence, name, mc.getMainRenderTarget(), msg ->
                    com.mojang.logging.LogUtils.getLogger().info("Boss QA: {}", msg.getString()));
            var meta = new com.google.gson.JsonObject();
            meta.addProperty("file", "screenshots/" + name);
            meta.addProperty("clientTick", ticks);
            meta.addProperty("gameTick", mc.level.getGameTime());
            meta.addProperty("fpsObserved", mc.getFps());
            meta.addProperty("renderer", org.lwjgl.opengl.GL11.glGetString(org.lwjgl.opengl.GL11.GL_RENDERER));
            meta.addProperty("source", "Minecraft Screenshot.grab framebuffer");
            meta.addProperty("scaleReference", "npc_ichigo: 1.8-block entity, same as standing player");
            for (var entity : mc.level.entitiesForRendering()) if (entity instanceof HollowBossEntity boss) {
                meta.addProperty("attack", boss.getAttack().name());
                meta.addProperty("attackTick", boss.getAttackTick());
                meta.addProperty("entranceTick", boss.getEntranceTick());
                meta.addProperty("entranceAge", boss.getEntranceAge(mc.getFrameTime()));
                meta.addProperty("bossHeight", boss.getBbHeight());
                meta.addProperty("bossY", boss.getY());
                var renderer = mc.getEntityRenderDispatcher().getRenderer(boss);
                if (renderer instanceof com.bleachmod.client.model.HollowBossRenderer geo) {
                    // GeckoLib's tracked world position removes the outer pre-render translation.
                    float retreat=boss.isEntering()?com.bleachmod.common.visual.HollowBossEntrance.retreat(boss.getEntranceAge(mc.getFrameTime())):0;
                    var entryOffset=net.minecraft.world.phys.Vec3.directionFromRotation(0,boss.getEntranceYaw()).scale(retreat);
                    for(String side:new String[]{"left","right"}) for(String part:new String[]{"hand","foot"}) {
                        geo.getGeoModel().getBone(side+"_"+part).ifPresent(bone->{
                            var point=bone.getWorldPosition();String key=side+(part.equals("hand")?"Wrist":"Ankle");
                            meta.addProperty(key+"X",point.x+entryOffset.x);meta.addProperty(key+"Y",point.y);meta.addProperty(key+"Z",point.z+entryOffset.z);
                        });
                    }
                    for (String side : new String[]{"left", "right"}) geo.getGeoModel().getBone(side + "_fist_contact").ifPresent(bone -> {
                        var point = bone.getWorldPosition();
                        meta.addProperty(side + "ContactX", point.x+entryOffset.x);
                        meta.addProperty(side + "ContactY", point.y);
                        meta.addProperty(side + "ContactZ", point.z+entryOffset.z);
                    });
                }
            }
            Files.writeString(evidence.toPath().resolve("frames.jsonl"), meta + System.lineSeparator(),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (Exception error) {
            com.mojang.logging.LogUtils.getLogger().error("Boss QA capture failed", error);
        }
    }
}
