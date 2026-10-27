package com.bleachmod.client.cinematic;

import com.bleachmod.Reference;
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
import java.util.Locale;

/** Reproducible real Minecraft screenshots; never opens an existing save. */
@Mod.EventBusSubscriber(modid=Reference.MOD_ID, value=Dist.CLIENT)
public final class HollowQaBootstrap {
    private static boolean created;
    private static int ticks;
    private static String capture;
    private static File evidence;
    private HollowQaBootstrap() { }
    @SubscribeEvent public static void tick(TickEvent.ClientTickEvent event) {
        var mc = Minecraft.getInstance();
        if (event.phase != TickEvent.Phase.END || net.minecraftforge.fml.loading.FMLEnvironment.production
                || !Boolean.getBoolean("bleachmod.hollowQa")
                || !mc.gameDirectory.getAbsolutePath().replace('\\', '/').contains("/run/hollow-qa/")) return;
        if (!created && mc.screen instanceof TitleScreen) {
            created = true;
            String name = "Hollow-QA-" + System.currentTimeMillis();
            evidence = new File(mc.gameDirectory, "hollow-evidence/" + name);
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
                || !mc.getSingleplayerServer().getWorldData().getLevelName().startsWith("Hollow-QA-")
                || !com.bleachmod.common.data.PlayerCapability.get(mc.player).map(d -> d.getStatus().hasCreatedCharacter()).orElse(false)) return;
        ticks++;
        if (ticks == 30) {
            mc.setScreen(null);
            mc.options.pauseOnLostFocus = false;
            mc.options.hideGui = true;
            mc.options.renderDistance().set(6);
            mc.getTutorial().stop();
            mc.getToasts().clear();
            moveCamera(.5, 2.1, 13.5, .5, 1.8, .5);
        }
        if (Boolean.getBoolean("bleachmod.hollowGaitQa")) {
            gaitTick();
            return;
        }
        if (ticks == 100) capture = "01-front";
        if (ticks == 130) moveCamera(.5, 2.2, -12.5, .5, 1.8, .5);
        if (ticks == 190) capture = "02-rear";
        if (ticks == 210) moveCamera(1.7, 2.6, 5.5, 4.5, 1.6, .5);
        if (ticks == 250) capture = "03-flight-detail";
        if (ticks == 270) moveCamera(7.5, 2.2, -3.5, 4.5, 1.3, .5);
        if (ticks == 310) capture = "04-flight-rear";
        if (ticks == 330) moveCamera(1.6, 2.5, 6.8, -3.5, 1.7, 2.5);
        if (ticks == 360) startCombat(com.bleachmod.entity.HollowVariant.BRUTE, 0);
        if (ticks >= 380 && ticks <= 600 && ticks % 2 == 0) capture = "tank-" + ticks;
        if (ticks == 620) {
            moveCamera(4.5, 1.5, 2, .5, .75, 2.8);
            startCombat(com.bleachmod.entity.HollowVariant.SWIFT, 0);
        }
        if (ticks >= 640 && ticks <= 840 && ticks % 2 == 0) capture = "swift-" + ticks;
        if (ticks == 860) {
            moveCamera(16, 7.5, 17, 4.5, 2.5, 5.3);
            startCombat(com.bleachmod.entity.HollowVariant.FLYING, 0);
        }
        if (ticks >= 880 && ticks <= 1600 && ticks % 2 == 0) capture = "flight-" + ticks;
        if (ticks == 1620) com.mojang.logging.LogUtils.getLogger().info("Hollow aerial QA complete: {}", evidence);
        if (ticks == 1660 && Boolean.getBoolean("bleachmod.hollowQaExit")) mc.stop();
    }

    /** Side/front/rear walking passes in the same fresh, development-only QA world. */
    private static void gaitTick() {
        var mc = Minecraft.getInstance();
        if (ticks >= 40 && ticks <= 500) {
            int frame = ticks;
            var server = mc.getSingleplayerServer();
            var uuid = mc.player.getUUID();
            server.execute(() -> {
                var player = server.getPlayerList().getPlayer(uuid);
                if (player == null) return;
                var level = player.serverLevel();
                for (var entity : level.getAllEntities()) {
                    if (!(entity instanceof com.bleachmod.entity.HollowEntity mob)
                            || mob.getVariant() != com.bleachmod.entity.HollowVariant.BRUTE) continue;
                    mob.setTarget(null);
                    mob.setNoAi(false);
                    if (frame % 20 == 0)
                        mob.getNavigation().moveTo(-3.5, mob.getY(), mob.getZ() + 12, 1);
                    double dx = frame < 200 ? 7 : frame < 350 ? 5 : -5;
                    double dz = frame < 200 ? 0 : frame < 350 ? 6 : -6;
                    double x = mob.getX() + dx, z = mob.getZ() + dz;
                    float yaw = (float)Math.toDegrees(Math.atan2(dx, -dz));
                    float pitch = (float)Math.toDegrees(Math.atan2(.4, Math.hypot(dx, dz)));
                    player.teleportTo(level, x, mob.getY() + 2 - player.getEyeHeight(), z, yaw, pitch);
                    player.getAbilities().flying = true;
                    player.onUpdateAbilities();
                    break;
                }
            });
        }
        if (ticks >= 80 && ticks <= 490 && ticks % 2 == 0)
            capture = (ticks < 200 ? "gait-side-" : ticks < 350 ? "gait-front-" : "gait-rear-") + ticks;
        if (ticks == 510) com.mojang.logging.LogUtils.getLogger().info("Hollow gait QA complete: {}", evidence);
        if (ticks == 530 && Boolean.getBoolean("bleachmod.hollowQaExit")) mc.stop();
    }

    private static void startCombat(com.bleachmod.entity.HollowVariant variant, double elevation) {
        var mc = Minecraft.getInstance();
        var server = mc.getSingleplayerServer();
        var uuid = mc.player.getUUID();
        server.execute(() -> {
            var player = server.getPlayerList().getPlayer(uuid);
            if (player == null) return;
            var level = player.serverLevel();
            for (var old : level.getEntitiesOfClass(net.minecraft.world.entity.Mob.class,
                    player.getBoundingBox().inflate(40), mob -> mob.getTags().contains("hollow_qa_target"))) old.discard();
            int y = level.getHeight(net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING, 0, 0);
            for (var mob : level.getEntitiesOfClass(com.bleachmod.entity.HollowEntity.class, player.getBoundingBox().inflate(40))) {
                mob.setNoAi(true);
                mob.cancelMeleeStrike();
                mob.getFlightCombat().reset();
                mob.setTarget(null);
                mob.getNavigation().stop();
                mob.setPos(-3.5 + mob.getVariant().ordinal() * 4, y, .5);
                mob.setDeltaMovement(net.minecraft.world.phys.Vec3.ZERO);
                if (mob.getVariant() != variant) continue;
                net.minecraft.world.entity.Mob target = variant == com.bleachmod.entity.HollowVariant.BRUTE
                        ? net.minecraft.world.entity.EntityType.IRON_GOLEM.create(level)
                        : net.minecraft.world.entity.EntityType.HUSK.create(level);
                target.addTag("hollow_qa_target");
                target.setNoAi(true);
                target.setNoGravity(true);
                target.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH).setBaseValue(500);
                target.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.KNOCKBACK_RESISTANCE).setBaseValue(1);
                target.setHealth(500);
                target.setPos(mob.getX(), y + elevation, 5.3);
                target.setYRot(180);
                level.addFreshEntity(target);
                mob.setNoAi(false);
                mob.setTarget(target);
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
            Screenshot.grab(evidence, name, mc.getMainRenderTarget(), msg -> com.mojang.logging.LogUtils.getLogger().info("Hollow QA: {}", msg.getString()));
            String meta = String.format(Locale.ROOT,
                    "{\"file\":\"screenshots/%s\",\"tick\":%d,\"fpsObserved\":%d,\"renderer\":\"%s\",\"source\":\"Minecraft Screenshot.grab framebuffer\"}%n",
                    name, ticks, mc.getFps(), org.lwjgl.opengl.GL11.glGetString(org.lwjgl.opengl.GL11.GL_RENDERER));
            Files.writeString(evidence.toPath().resolve("frames.jsonl"), meta, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (Exception error) {
            com.mojang.logging.LogUtils.getLogger().error("Hollow QA capture failed", error);
        }
    }
}
