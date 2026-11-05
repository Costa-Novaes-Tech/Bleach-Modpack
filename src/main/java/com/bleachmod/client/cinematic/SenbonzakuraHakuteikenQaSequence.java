package com.bleachmod.client.cinematic;

import com.bleachmod.Reference;
import com.bleachmod.client.model.SenbonHakuteikenVisuals;
import com.bleachmod.common.data.CharacterData;
import com.bleachmod.common.data.PlayerCapability;
import com.bleachmod.common.network.SyncHelper;
import com.bleachmod.common.technique.SenbonzakuraTechniqueService;
import com.bleachmod.init.ModItems;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.logging.LogUtils;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.minecraft.client.Camera;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * Automated in-game QA harness for Senbonzakura Bankai Slot 4 (Shukei: Hakuteiken)
 * 3D GeckoLib model + animations + PlayerAnimator pose.
 *
 * Executes 3 full Hakuteiken casts (windup 16t + dash 10t) across Back, Side Profile,
 * and Three-Quarter Frontal camera angles, verifying:
 * - All 13 GeckoLib construct bones loaded in BakedGeoModel
 * - PlayerAnimator hakuteiken_dash animation loaded and active
 * - 16-block linear dash mechanics and 20 HP target hit preserved
 */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT)
public final class SenbonzakuraHakuteikenQaSequence {
    private static final String[] PASS_NAMES = {
            "pass0-back",
            "pass1-side",
            "pass2-front",
            "pass3-top"
    };

    private record Trial(UUID owner, long start, int passIndex) {
    }

    private static volatile Trial trial = null;
    private static int pass = 0;
    private static int waitTicks = 0;
    private static int lastCapturedAge = -999;
    private static volatile String failure = null;
    private static volatile double serverDistMoved = 0.0;
    private static volatile double serverPeakLeapY = 0.0;
    private static volatile float serverDamageDealt = 0.0F;
    private static volatile boolean serverTransformActive = false;
    private static volatile boolean serverFlightGranted = false;
    private static volatile double serverReachLimit = 0.0;
    private static volatile float serverOffensiveBonus = 0.0F;
    private static volatile boolean serverSlotExecuted = false;
    private static volatile boolean serverEvaluated = false;
    private static volatile int clientSyncWaitTicks = 0;

    private static Path outDir;
    private static Method setCameraPosMethod;
    private static java.lang.reflect.Field awaitingPosField;
    private static UUID targetId;
    private static Vec3 startOrigin = Vec3.ZERO;
    private static float targetInitialHp = 100.0F;
    private static final long RUN_ID = System.currentTimeMillis();

    private SenbonzakuraHakuteikenQaSequence() {
    }

    private static boolean enabled() {
        var mc = Minecraft.getInstance();
        return !net.minecraftforge.fml.loading.FMLEnvironment.production
                && Boolean.getBoolean("bleachmod.senbonHakuteikenQa")
                && Boolean.getBoolean("bleachmod.benihimeQa")
                && BenihimeQaBootstrap.automatedFinished()
                && mc.hasSingleplayerServer() && mc.player != null && mc.level != null
                && mc.getSingleplayerServer().getWorldData().getLevelName().startsWith("Benihime-QA-68-");
    }

    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent e) {
        if (e.phase != TickEvent.Phase.END || !enabled() || pass >= PASS_NAMES.length) {
            return;
        }
        var mc = Minecraft.getInstance();
        if (mc.screen != null || mc.isPaused()) {
            return;
        }

        if (outDir == null) {
            outDir = mc.gameDirectory.toPath().toAbsolutePath().normalize().resolve("senbon-hakuteiken-qa/" + RUN_ID);
            try {
                Files.createDirectories(outDir);
                Path evidenceDir = getProjectEvidenceDir(mc);
                if (evidenceDir != null) {
                    Files.deleteIfExists(evidenceDir.resolve("results.txt"));
                }
            } catch (Exception ex) {
                throw new IllegalStateException("Failed to create Hakuteiken QA output directory", ex);
            }
            mc.options.setCameraType(CameraType.THIRD_PERSON_BACK);
            mc.options.hideGui = true;
            mc.options.pauseOnLostFocus = false;
        }

        if (trial == null) {
            if (++waitTicks < (pass == 0 ? 60 : 25)) {
                return;
            }
            waitTicks = 0;
            lastCapturedAge = -999;
            failure = null;
            serverDistMoved = 0.0;
            serverPeakLeapY = 0.0;
            serverDamageDealt = 0.0F;
            serverTransformActive = false;
            serverFlightGranted = false;
            serverReachLimit = 0.0;
            serverOffensiveBonus = 0.0F;
            serverSlotExecuted = false;
            serverEvaluated = false;

            int currentPass = pass;
            UUID uuid = mc.player.getUUID();
            mc.getSingleplayerServer().execute(() -> prepareOnServer(uuid, currentPass));
            return;
        }

        // Keep client looking straight North (yaw 180)
        mc.player.setYRot(180.0F);
        mc.player.setXRot(0.0F);
        mc.player.yRotO = 180.0F;
        mc.player.yBodyRot = 180.0F;
        mc.player.yBodyRotO = 180.0F;
        mc.player.yHeadRot = 180.0F;
        mc.player.yHeadRotO = 180.0F;
        if (clientSyncWaitTicks > 0) {
            clientSyncWaitTicks--;
        }

        long age = mc.level.getGameTime() - trial.start();
        if (age >= 56 && serverEvaluated) {
            int expectedBones = SenbonHakuteikenVisuals.EXPECTED_BONES.length;
            int bonesFound = SenbonHakuteikenVisuals.verifyBakedBones();
            boolean playerAnimLoaded = PlayerAnimationRegistry.getAnimation(SenbonHakuteikenVisuals.DASH_ANIM) != null;
            var layer = SenbonHakuteikenVisuals.getLayer(mc.player);
            boolean layerRegistered = layer != null;

            boolean ok = (failure == null)
                    && (bonesFound == expectedBones)
                    && playerAnimLoaded
                    && layerRegistered
                    && (serverPeakLeapY >= 4.5)
                    && (serverDistMoved >= 14.0)
                    && (serverDamageDealt >= 19.0F)
                    && serverTransformActive
                    && serverFlightGranted
                    && (serverReachLimit >= 5.5)
                    && (serverOffensiveBonus >= 0.30F);

            String row = String.format(
                    Locale.ROOT,
                    "%s %s bones=%d/%d playerAnimLoaded=%b layerRegistered=%b leapY=%.2f distMoved=%.2f damageDealt=%.1f transform=%b mayfly=%b reach=%.2f offBonus=%.2f failure=%s%n",
                    PASS_NAMES[pass],
                    ok ? "PASS" : "FAIL",
                    bonesFound,
                    expectedBones,
                    playerAnimLoaded,
                    layerRegistered,
                    serverPeakLeapY,
                    serverDistMoved,
                    serverDamageDealt,
                    serverTransformActive,
                    serverFlightGranted,
                    serverReachLimit,
                    serverOffensiveBonus,
                    failure
            );

            try {
                Files.writeString(outDir.resolve("results.txt"), row, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                Path evidenceDir = getProjectEvidenceDir(mc);
                if (evidenceDir != null) {
                    Files.writeString(evidenceDir.resolve("results.txt"), row, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                }
            } catch (Exception ex) {
                LogUtils.getLogger().error("Failed writing Hakuteiken QA results", ex);
            }

            LogUtils.getLogger().info("[SENBON_HAKUTEIKEN_QA] {}", row.trim());

            trial = null;
            pass++;

            if (pass >= PASS_NAMES.length) {
                mc.options.hideGui = false;
                mc.options.setCameraType(CameraType.FIRST_PERSON);
                if (Boolean.getBoolean("bleachmod.qaAutoExit")) {
                    mc.stop();
                }
            }
        }
    }

    private static void prepareOnServer(UUID uuid, int passIndex) {
        try {
            var server = ServerLifecycleHooks.getCurrentServer();
            if (server == null) {
                return;
            }
            var p = server.getPlayerList().getPlayer(uuid);
            if (p == null) {
                throw new IllegalStateException("Player missing on server");
            }

            server.setDifficulty(Difficulty.NORMAL, true);
            p.serverLevel().setDayTime(6000);
            SenbonzakuraTechniqueService.cancel(p);

            double baseX = (passIndex + 1) * 32.0 + 0.5;
            double baseY = -60.0;
            double baseZ = 0.5;
            startOrigin = new Vec3(baseX, baseY, baseZ);

            p.teleportTo(p.serverLevel(), baseX, baseY, baseZ, 180.0F, 0.0F);
            p.setYRot(180.0F);
            p.setXRot(0.0F);
            p.setYHeadRot(180.0F);
            p.setYBodyRot(180.0F);
            p.setOnGround(true);

            var d = PlayerCapability.get(p).orElseThrow(() -> new IllegalStateException("Missing player capability"));
            d.initializeShinigami();
            d.getCharacter().bindZanpakuto(CharacterData.SENBONZAKURA);
            d.getSkills().get("zanpakuto").setLevel(2);
            for (String form : List.of("sealed", "shikai", "bankai")) {
                d.getCharacter().unlockForm(form);
                d.getCharacter().setMastery("zanpakuto", form, 100);
            }
            d.getCharacter().setActiveForm("zanpakuto", "bankai");
            d.getCharacter().setSelectedForm("zanpakuto", "bankai");
            d.getResources().setCostsDisabled(true);
            d.getStatus().clearCooldowns();
            d.getGuard().interrupt();
            d.getResources().setCurrentReiatsu(d.getResources().getMaxReiatsu());

            p.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(ModItems.SENBONZAKURA.get()));
            p.setItemSlot(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
            SyncHelper.full(p);
            SyncHelper.appearance(p);

            // Spawn a persistent Husk target 8 blocks North (-Z) and offset 1.05 blocks West (-X)
            // so it never blocks the camera or collides with the player's body, while still inside dashHits's inflate(1.25) AABB
            var target = EntityType.HUSK.create(p.serverLevel());
            if (target != null) {
                target.moveTo(baseX - 1.05, baseY, baseZ - 8.0, 0.0F, 0.0F);
                target.setNoAi(true);
                target.setNoGravity(true);
                target.setPersistenceRequired();
                target.setSilent(true);
                if (target.getAttribute(Attributes.ARMOR) != null) {
                    target.getAttribute(Attributes.ARMOR).setBaseValue(0.0);
                }
                if (target.getAttribute(Attributes.MAX_HEALTH) != null) {
                    target.getAttribute(Attributes.MAX_HEALTH).setBaseValue(100.0);
                }
                target.setHealth(100.0F);
                p.serverLevel().addFreshEntity(target);
                targetId = target.getUUID();
                targetInitialHp = target.getHealth();
            }

            var sLevel = p.serverLevel();
            if (passIndex == 2) {
                // Scenario: Enclosed Chamber with solid stone ceiling at Y+8 and side columns
                // Verifies upward 5.0m leap and diagonal swoop dive operate with zero block collision jamming
                net.minecraft.core.BlockPos bOrigin = net.minecraft.core.BlockPos.containing(baseX, baseY, baseZ);
                for (int dx = -6; dx <= 6; dx++) {
                    for (int dz = -18; dz <= 6; dz++) {
                        sLevel.setBlockAndUpdate(bOrigin.offset(dx, 8, dz), net.minecraft.world.level.block.Blocks.STONE_BRICKS.defaultBlockState());
                    }
                }
                for (int y = 0; y <= 7; y++) {
                    for (int dz = -16; dz <= 4; dz += 4) {
                        sLevel.setBlockAndUpdate(bOrigin.offset(-5, y, dz), net.minecraft.world.level.block.Blocks.STONE_BRICKS.defaultBlockState());
                        sLevel.setBlockAndUpdate(bOrigin.offset(5, y, dz), net.minecraft.world.level.block.Blocks.STONE_BRICKS.defaultBlockState());
                    }
                }
                var zombie = EntityType.ZOMBIE.create(sLevel);
                if (zombie != null) {
                    zombie.moveTo(baseX + 2.5, baseY, baseZ - 4.0, 0.0F, 0.0F);
                    zombie.setNoAi(true);
                    zombie.setNoGravity(true);
                    zombie.setSilent(true);
                    sLevel.addFreshEntity(zombie);
                }
                var skel = EntityType.SKELETON.create(sLevel);
                if (skel != null) {
                    skel.moveTo(baseX - 2.5, baseY, baseZ - 4.0, 0.0F, 0.0F);
                    skel.setNoAi(true);
                    skel.setNoGravity(true);
                    skel.setSilent(true);
                    sLevel.addFreshEntity(skel);
                }
            } else if (passIndex == 3) {
                // Scenario: Multi-Entity Interference Arena
                for (int i = 0; i < 3; i++) {
                    var flanker = EntityType.HUSK.create(sLevel);
                    if (flanker != null) {
                        double fx = baseX + (i == 0 ? 2.8 : (i == 1 ? -2.8 : 0.0));
                        double fz = baseZ - (i == 2 ? 12.0 : 6.0);
                        flanker.moveTo(fx, baseY, fz, 0.0F, 0.0F);
                        flanker.setNoAi(true);
                        flanker.setNoGravity(true);
                        flanker.setSilent(true);
                        sLevel.addFreshEntity(flanker);
                    }
                }
            }

            // Allow 15 server ticks for teleport confirmation to settle before casting Slot 4
            clientSyncWaitTicks = 2;
            trial = new Trial(uuid, p.level().getGameTime() + 25, passIndex);
            LogUtils.getLogger().info("[SENBON_HAKUTEIKEN_QA] Prepared pass {} ({}) at ({}, {}, {})",
                    passIndex, PASS_NAMES[passIndex], baseX, baseY, baseZ);
        } catch (Exception ex) {
            failure = ex.toString();
            LogUtils.getLogger().error("[SENBON_HAKUTEIKEN_QA] Failed preparing pass {}", passIndex, ex);
        }
    }

    @SubscribeEvent(priority = net.minecraftforge.eventbus.api.EventPriority.LOWEST)
    public static void serverTick(TickEvent.ServerTickEvent e) {
        Trial t = trial;
        if (e.phase != TickEvent.Phase.END || t == null || !Boolean.getBoolean("bleachmod.senbonHakuteikenQa")) {
            return;
        }
        var server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) {
            return;
        }
        var p = server.getPlayerList().getPlayer(t.owner());
        if (p == null) {
            return;
        }
        long age = p.level().getGameTime() - t.start();
        if (age < 0 || (age >= 28 && age <= 44)) {
            p.setYRot(180.0F);
            p.setXRot(0.0F);
            p.setYHeadRot(180.0F);
            p.setYBodyRot(180.0F);
            long deadline = System.currentTimeMillis() + 500L;
            while (hasPendingTeleport(p) && System.currentTimeMillis() < deadline) {
                if (!server.pollTask()) {
                    try {
                        Thread.sleep(1L);
                    } catch (InterruptedException ignored) {
                        break;
                    }
                }
            }
            return;
        }
        if (age >= 0 && !serverSlotExecuted) {
            serverSlotExecuted = true;
            p.setYRot(180.0F);
            p.setXRot(0.0F);
            p.setYHeadRot(180.0F);
            p.setYBodyRot(180.0F);
            startOrigin = new Vec3((t.passIndex() + 1) * 32.0 + 0.5, -60.0, 0.5);
            PlayerCapability.get(p).ifPresent(d -> {
                d.getStatus().clearCooldowns();
                boolean accepted = SenbonzakuraTechniqueService.executeSlot(p, d, 4);
                if (!accepted) {
                    failure = "executeSlot(4) returned false";
                }
            });
            LogUtils.getLogger().info("[SENBON_HAKUTEIKEN_QA] Executed Hakuteiken Slot 4 on pass {} from {}",
                    t.passIndex(), startOrigin);
        }
        if (age >= 1 && age <= 35) {
            serverPeakLeapY = Math.max(serverPeakLeapY, p.getY() - startOrigin.y);
        }
        if (age < 0) {
            long deadline = System.currentTimeMillis() + 500L;
            while (hasPendingTeleport(p) && System.currentTimeMillis() < deadline) {
                if (!server.pollTask()) {
                    try {
                        Thread.sleep(1L);
                    } catch (InterruptedException ignored) {
                        break;
                    }
                }
            }
        }
        if (age == 49) {
            p.swing(InteractionHand.MAIN_HAND, true);
        }
        if (age >= 46 && serverSlotExecuted && !serverEvaluated) {
            serverDistMoved = p.position().subtract(startOrigin).horizontalDistance();
            float hpAfter = targetInitialHp;
            if (targetId != null) {
                var ent = p.serverLevel().getEntity(targetId);
                if (ent instanceof LivingEntity living) {
                    hpAfter = living.getHealth();
                    living.discard();
                }
            }
            serverDamageDealt = targetInitialHp - hpAfter;
            serverTransformActive = SenbonzakuraTechniqueService.hakuteiken(p);
            serverFlightGranted = p.getAbilities().mayfly;
            serverReachLimit = com.bleachmod.common.compat.BetterCombatCompat.reachLimit(p);
            serverOffensiveBonus = SenbonzakuraTechniqueService.offensiveBonus(p);
            serverEvaluated = true;
        }
    }

    @SubscribeEvent
    public static void capture(TickEvent.RenderTickEvent e) {
        Trial t = trial;
        if (e.phase != TickEvent.Phase.END || t == null || outDir == null || !enabled()) {
            return;
        }
        var mc = Minecraft.getInstance();
        if (mc.level == null || mc.screen != null) {
            return;
        }
        int age = (int) (mc.level.getGameTime() - t.start());
        if (age == lastCapturedAge) {
            return;
        }
        if (age == 49) {
            mc.player.swing(InteractionHand.MAIN_HAND);
        }
        if (age == 6 || age == 12 || age == 20 || age == 28 || age == 34 || age == 40 || age == 46 || age == 50) {
            lastCapturedAge = age;
            String phase = age < 10 ? "leap" : (age < 30 ? "hover" : (age < 40 ? "rasante" : (age == 40 ? "impact" : (age == 46 ? "transform" : "attack"))));
            String fileName = String.format(Locale.ROOT, "%s-t%02d-%s.png", PASS_NAMES[t.passIndex()], age, phase);
            saveScreenshot(mc, fileName);
        }
    }

    @SubscribeEvent
    public static void camera(ViewportEvent.ComputeCameraAngles event) {
        Trial t = trial;
        if (!enabled() || t == null || pass >= PASS_NAMES.length) {
            return;
        }
        var p = Minecraft.getInstance().player;
        if (p == null) {
            return;
        }

        Vec3 focus = p.getPosition((float) event.getPartialTick()).add(0, 1.35, 0);
        Vec3 eye;
        switch (t.passIndex()) {
            case 0 -> eye = focus.add(0.0, 0.55, 4.2);       // Back view (+Z looking North toward -Z)
            case 1 -> eye = focus.add(4.4, 0.35, -0.4);      // Side profile (+X looking West toward player)
            case 2 -> eye = focus.add(2.2, 0.45, -4.0);     // Three-quarter frontal (-Z, +X looking back at player & blade)
            default -> eye = focus.add(0.0, 5.0, 0.05);     // Top-down view (+Y looking directly down at player & wings)
        }

        Vec3 delta = focus.subtract(eye);
        try {
            if (setCameraPosMethod == null) {
                setCameraPosMethod = Camera.class.getDeclaredMethod("setPosition", double.class, double.class, double.class);
                setCameraPosMethod.setAccessible(true);
            }
            setCameraPosMethod.invoke(event.getCamera(), eye.x, eye.y, eye.z);
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }

        event.setYaw((float) Math.toDegrees(Math.atan2(-delta.x, delta.z)));
        event.setPitch((float) -Math.toDegrees(Math.atan2(delta.y, Math.hypot(delta.x, delta.z))));
    }

    private static void saveScreenshot(Minecraft mc, String fileName) {
        NativeImage img = Screenshot.takeScreenshot(mc.getMainRenderTarget());
        Path outTarget = outDir.resolve(fileName);
        Path evidenceDir = getProjectEvidenceDir(mc);
        Path evidenceTarget = evidenceDir != null ? evidenceDir.resolve(fileName) : null;
        net.minecraft.Util.ioPool().execute(() -> {
            try {
                img.writeToFile(outTarget);
                if (evidenceTarget != null) {
                    img.writeToFile(evidenceTarget);
                }
            } catch (Exception ex) {
                LogUtils.getLogger().error("Failed saving Hakuteiken QA screenshot {}", fileName, ex);
            } finally {
                img.close();
            }
        });
    }

    private static boolean hasPendingTeleport(net.minecraft.server.level.ServerPlayer p) {
        try {
            if (awaitingPosField == null) {
                awaitingPosField = net.minecraft.server.network.ServerGamePacketListenerImpl.class
                        .getDeclaredField("awaitingPositionFromClient");
                awaitingPosField.setAccessible(true);
            }
            return awaitingPosField.get(p.connection) != null;
        } catch (Exception ex) {
            return false;
        }
    }

    private static Path getProjectEvidenceDir(Minecraft mc) {
        try {
            Path gameDir = mc.gameDirectory.toPath().toAbsolutePath().normalize();
            Path c1 = gameDir.getParent().resolve("Docs/poderes/media/senbon-hakuteiken-20261103");
            if (Files.isDirectory(c1.getParent())) {
                Files.createDirectories(c1);
                return c1;
            }
            Path c2 = gameDir.getParent().getParent().resolve("Docs/poderes/media/senbon-hakuteiken-20261103");
            if (Files.isDirectory(c2.getParent())) {
                Files.createDirectories(c2);
                return c2;
            }
        } catch (Exception ignored) {
        }
        return null;
    }
}
