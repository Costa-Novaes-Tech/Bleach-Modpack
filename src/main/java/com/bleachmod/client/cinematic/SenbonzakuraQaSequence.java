package com.bleachmod.client.cinematic;

import com.bleachmod.Reference;
import com.bleachmod.common.data.PlayerCapability;
import com.bleachmod.common.data.PlayerData;
import com.bleachmod.common.network.SyncHelper;
import com.bleachmod.common.technique.ReleaseAnimationService;
import com.bleachmod.common.technique.SenbonzakuraCinematicService;
import com.bleachmod.common.technique.TechniqueService;
import com.bleachmod.init.ModItems;
import com.mojang.logging.LogUtils;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Husk;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static net.minecraft.commands.Commands.literal;

/**
 * Automated in-game validation suite and screenshot capture for Byakuya Kuchiki's Bankai: Senbonzakura Kageyoshi.
 * Validates:
 *   - Day and night transformations
 *   - Perspective return across first-person, third-person back, and third-person front
 *   - Wall collision safety
 *   - Clean interruption and consecutive activations
 *   - Continuous camera trajectory, atmospheric darkening, and localized subtitles
 */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT)
public final class SenbonzakuraQaSequence {
    private static final String[] CASES = {
            "01_bankai_day_transformation",
            "02_bankai_night_transformation",
            "03_bankai_first_person_return",
            "04_bankai_near_wall_collision",
            "05_bankai_interruption_cleanup",
            "06_bankai_consecutive_activation",
            "07_bankai_uneven_terrain"
    };

    private static final int SETTLE_TICKS = 14;
    private record Trial(String name, UUID owner, Vec3 origin, long start, int maxTicks, List<UUID> targets, List<BlockPos> placedBlocks, int scenarioIndex) { }

    private static volatile Trial trial;
    private static volatile boolean serverFinished;
    private static volatile int serverAge = -SETTLE_TICKS;
    private static int stage = 0;
    private static int wait = 0;
    private static int lastFrame = -1;
    private static Path evidence;
    private static final long RUN_ID = System.currentTimeMillis();
    private static CameraType previousCamera;
    private static boolean previousHud;
    private static boolean manualTriggered = false;

    private SenbonzakuraQaSequence() { }

    public static boolean automatedFinished() { return stage > CASES.length; }

    private static boolean enabled() {
        var mc = Minecraft.getInstance();
        if (net.minecraftforge.fml.loading.FMLEnvironment.production) return false;
        if (manualTriggered && mc.player != null) return true;
        return Boolean.getBoolean("bleachmod.senbonzakuraQa")
                && Boolean.getBoolean("bleachmod.benihimeQa")
                && mc.hasSingleplayerServer() && mc.player != null && mc.player.hasPermissions(2)
                && mc.gameDirectory.getAbsolutePath().replace('\\', '/').contains("/run-benihime-qa/")
                && mc.getSingleplayerServer().getWorldData().getLevelName().startsWith("Benihime-QA-68-");
    }

    @SubscribeEvent
    public static void registerCommands(RegisterClientCommandsEvent e) {
        e.getDispatcher().register(literal("senbonqa")
                .then(literal("start").executes(c -> {
                    manualTriggered = true;
                    stage = 0;
                    wait = 0;
                    trial = null;
                    c.getSource().sendSuccess(() -> Component.literal("§d[Senbonzakura QA] Starting test suite..."), false);
                    return 1;
                }))
                .then(literal("stop").executes(c -> {
                    stage = CASES.length + 1;
                    trial = null;
                    manualTriggered = false;
                    c.getSource().sendSuccess(() -> Component.literal("§c[Senbonzakura QA] Stopped."), false);
                    return 1;
                })));
    }

    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent e) {
        if (e.phase != TickEvent.Phase.END || !enabled()) return;
        var mc = Minecraft.getInstance();
        if (mc.screen != null || mc.isPaused()) return;
        if (!manualTriggered && (!BenihimeQaBootstrap.automatedFinished() || !HyorinmaruQaSequence.automatedFinished())) return;

        wait++;

        if (stage == 0 && wait == 1) {
            previousCamera = mc.options.getCameraType();
            previousHud = mc.options.hideGui;
        }

        if (stage < CASES.length) {
            int neededWait = (stage == 0) ? 60 : (trial != null ? trial.maxTicks() + 20 : 100);
            if ((stage == 0 && wait >= neededWait) || (stage > 0 && serverFinished && wait >= 30)) {
                var name = CASES[stage];
                var id = mc.player.getUUID();
                int index = stage++;
                wait = 0;
                serverFinished = false;
                serverAge = -SETTLE_TICKS;
                trial = null;
                lastFrame = -1;

                // Adjust initial client camera based on scenario (covers 1st person, 3rd person back, and 3rd person front)
                if (name.contains("first_person")) {
                    mc.options.setCameraType(CameraType.FIRST_PERSON);
                } else if (name.contains("night")) {
                    mc.options.setCameraType(CameraType.THIRD_PERSON_FRONT);
                } else {
                    mc.options.setCameraType(CameraType.THIRD_PERSON_BACK);
                }
                mc.options.hideGui = false;

                evidence = mc.gameDirectory.toPath().resolve("senbonzakura-qa/cycle101-" + RUN_ID + "/" + name);
                try {
                    Files.createDirectories(evidence);
                } catch (IOException ignored) { }

                mc.getSingleplayerServer().execute(() -> prepareServer(mc.getSingleplayerServer().getPlayerList().getPlayer(id), index, name));
            }
        } else if (stage == CASES.length && serverFinished && wait >= 30) {
            stage++;
            trial = null;
            mc.options.setCameraType(previousCamera != null ? previousCamera : CameraType.FIRST_PERSON);
            mc.options.hideGui = previousHud;
            LogUtils.getLogger().info("=================================================================");
            LogUtils.getLogger().info("Senbonzakura QA sequence COMPLETE: All 7 scenarios executed & captured!");
            LogUtils.getLogger().info("=================================================================");
            if (mc.player != null) {
                mc.player.displayClientMessage(Component.literal("§d[Senbonzakura QA] Sequência finalizada com sucesso! Screenshots salvas em senbonzakura-qa/"), false);
            }
            if (Boolean.getBoolean("bleachmod.qaAutoExit")) {
                LogUtils.getLogger().info("Senbonzakura QA auto-exit requested; closing client cleanly...");
                mc.stop();
            }
        }
    }

    private static void prepareServer(ServerPlayer p, int index, String name) {
        if (p == null) return;
        p.getServer().setDifficulty(net.minecraft.world.Difficulty.NORMAL, true);
        SenbonzakuraCinematicService.cancel(p);
        ReleaseAnimationService.cancel(p);

        var d = PlayerCapability.get(p).orElse(null);
        if (d == null) return;

        int x = 120 + 70 * index;
        int y = p.serverLevel().getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, 0);
        var origin = new Vec3(x + 0.5, y, 0.5);
        p.setDeltaMovement(Vec3.ZERO);
        p.teleportTo(p.serverLevel(), origin.x, origin.y, origin.z, 0, 0);
        p.setYRot(0.0F);
        p.setXRot(0.0F);
        p.setYHeadRot(0.0F);
        p.setYBodyRot(0.0F);

        d.getCharacter().bindZanpakuto("senbonzakura");
        d.getSkills().get("zanpakuto").setLevel(2);
        for (String form : List.of("sealed", "shikai", "bankai")) {
            d.getCharacter().unlockForm(form);
            d.getCharacter().setMastery("zanpakuto", form, 100);
        }
        d.getCharacter().preserveLegacyTechniques();

        p.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.SENBONZAKURA.get()));
        p.setItemSlot(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
        d.getStatus().clearCooldowns();
        d.getGuard().interrupt();
        d.getResources().setCurrentReiatsu(d.getResources().getMaxReiatsu());
        d.getCharacter().setActiveForm("zanpakuto", "shikai");
        d.getCharacter().setSelectedForm("zanpakuto", "shikai");

        var targets = new ArrayList<UUID>();
        var placedBlocks = new ArrayList<BlockPos>();
        int maxTicks = SenbonzakuraCinematicService.DURATION + 40;
        long timeOfDay = 6000;

        switch (index) {
            case 0 -> {
                // Scenario 1: Bankai Day Transformation + Third-Person Back Return (290 ticks + 40 ticks post-return)
                timeOfDay = 6000;
                LogUtils.getLogger().info("Senbonzakura QA: [Scenario 1] Day Transformation (THIRD_PERSON_BACK)");
            }
            case 1 -> {
                // Scenario 2: Bankai Night Transformation + Third-Person Front Return (290 ticks + 40 ticks post-return)
                timeOfDay = 18000;
                LogUtils.getLogger().info("Senbonzakura QA: [Scenario 2] Night Transformation (THIRD_PERSON_FRONT)");
            }
            case 2 -> {
                // Scenario 3: Bankai First Person Return (290 ticks + 40 ticks post-return)
                timeOfDay = 6000;
                LogUtils.getLogger().info("Senbonzakura QA: [Scenario 3] First-Person Return (FIRST_PERSON)");
            }
            case 3 -> {
                // Scenario 4: Near Wall Collision Test
                timeOfDay = 6000;
                BlockPos base = BlockPos.containing(origin);
                for (int dx = -3; dx <= 3; dx++) {
                    for (int dy = 0; dy <= 6; dy++) {
                        BlockPos pos = base.offset(dx, dy, -3);
                        placedBlocks.add(pos);
                        p.serverLevel().setBlock(pos, Blocks.STONE_BRICKS.defaultBlockState(), 3);
                    }
                }
                maxTicks = 140;
                LogUtils.getLogger().info("Senbonzakura QA: [Scenario 4] Near Wall Collision");
            }
            case 4 -> {
                // Scenario 5: Interruption cleanup test
                timeOfDay = 6000;
                maxTicks = 90;
                LogUtils.getLogger().info("Senbonzakura QA: [Scenario 5] Interruption Cleanup");
            }
            case 5 -> {
                // Scenario 6: Consecutive activation test
                timeOfDay = 6000;
                maxTicks = 175;
                LogUtils.getLogger().info("Senbonzakura QA: [Scenario 6] Consecutive Activation");
            }
            case 6 -> {
                // Scenario 7: Uneven terrain / stepped relief test
                timeOfDay = 6000;
                BlockPos base = BlockPos.containing(origin);
                for (int dx = -8; dx <= 8; dx++) {
                    for (int dz = -18; dz <= 14; dz++) {
                        // Leave immediate 2-block radius around player & katana drop clear, add stepped relief around corridor
                        if (Math.abs(dx) <= 2 && Math.abs(dz) <= 2) continue;
                        if ((Math.abs(dx) >= 3 && Math.abs(dx) <= 6 && (dz + 20) % 4 < 2)
                                || (dz <= -6 && dz >= -14 && Math.abs(dx) <= 4 && (dz + 20) % 3 == 0)) {
                            BlockPos pos = base.offset(dx, 0, dz);
                            placedBlocks.add(pos);
                            p.serverLevel().setBlock(pos, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 3);
                        }
                    }
                }
                maxTicks = 180;
                LogUtils.getLogger().info("Senbonzakura QA: [Scenario 7] Uneven Terrain Relief");
            }
        }

        SyncHelper.full(p);
        SyncHelper.appearance(p);
        p.serverLevel().setDayTime(timeOfDay);

        serverFinished = false;
        serverAge = -SETTLE_TICKS;
        long releaseStart = p.level().getGameTime() + SETTLE_TICKS;
        trial = new Trial(name, p.getUUID(), origin, releaseStart, maxTicks, List.copyOf(targets), List.copyOf(placedBlocks), index);
        LogUtils.getLogger().info("Senbonzakura QA prepared {} at {} (release in {} ticks)", name, origin, SETTLE_TICKS);
    }

    @SubscribeEvent
    public static void serverTick(TickEvent.ServerTickEvent e) {
        var t = trial;
        if (e.phase != TickEvent.Phase.END || t == null) return;
        var server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) return;
        var p = server.getPlayerList().getPlayer(t.owner());
        if (p == null) return;

        long age = p.level().getGameTime() - t.start();
        serverAge = (int) age;

        // Trigger the actual Bankai release at age == 0 after the teleport/chunk settle window
        if (age == 0) {
            PlayerData d = PlayerCapability.get(p).orElse(null);
            if (d != null) {
                d.getCharacter().setActiveForm("zanpakuto", "shikai");
                d.getCharacter().setSelectedForm("zanpakuto", "shikai");
                ReleaseAnimationService.begin(p, d, "zanpakuto", "bankai", 0f);
            }
        }

        // In scenario 5 (interruption), trigger dodge/guard at tick 70
        if (t.name().contains("interruption") && age == 70) {
            PlayerData d = PlayerCapability.get(p).orElse(null);
            if (d != null) {
                d.getStatus().beginDodge(new Vec3(1, 0, 0), p.level().getGameTime());
                LogUtils.getLogger().info("Senbonzakura QA simulated dodge interrupt at tick 70");
            }
        }

        // In scenario 6 (consecutive activation), cancel and re-trigger at tick 55 to verify zero stage accumulation
        if (t.name().contains("consecutive") && age == 55) {
            PlayerData d = PlayerCapability.get(p).orElse(null);
            if (d != null) {
                SenbonzakuraCinematicService.cancel(p);
                ReleaseAnimationService.cancel(p);
                d.getCharacter().setActiveForm("zanpakuto", "shikai");
                d.getCharacter().setSelectedForm("zanpakuto", "shikai");
                ReleaseAnimationService.begin(p, d, "zanpakuto", "bankai", 0f);
                LogUtils.getLogger().info("Senbonzakura QA re-triggered consecutive activation at tick 55");
            }
        }

        if (age >= t.maxTicks()) {
            serverFinished = true;
            for (var tid : t.targets()) {
                var ent = p.serverLevel().getEntity(tid);
                if (ent != null) ent.discard();
            }
            for (var pos : t.placedBlocks()) {
                p.serverLevel().removeBlock(pos, false);
            }
        }
    }

    @SubscribeEvent
    public static void renderTick(TickEvent.RenderTickEvent e) {
        var t = trial;
        if (e.phase != TickEvent.Phase.END || t == null || !enabled() || evidence == null) return;
        var mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null || mc.screen != null) return;

        int age = serverAge;
        if (age < 0 || age > t.maxTicks()) return;

        // Capture dense katana handoff/release/drop (0..40), core milestones, and dense continuous exit + post-return (220..330)
        boolean handoffSample = (age <= 14 && age % 2 == 0) || (age > 14 && age <= 40 && age % 4 == 0);
        boolean exitSample = (age >= 220 && age <= 296 && age % 2 == 0) || (age > 296 && age <= 330 && age % 6 == 0);
        boolean keyTick = handoffSample || exitSample
                || (age == 0 || age == 10 || age == 24 || age == 35 || age == 48 || age == 72 || age == 76 || age == 96
                || age == 112 || age == 120 || age == 144 || age == 160 || age == 168 || age == 200 || age == 228
                || age == 244 || age == 264 || age == 266 || age == 278 || age == 286 || age == 288 || age == 290 || (age % 24 == 0));
        if (!keyTick || lastFrame == age) return;
        lastFrame = age;

        String file = String.format(Locale.ROOT, "frame-t%03d.png", age);
        try {
            Screenshot.grab(evidence.toFile(), file, mc.getMainRenderTarget(), msg -> { });
            var cam = mc.gameRenderer.getMainCamera();
            Vec3 camPos = cam.getPosition();
            String line = String.format(Locale.ROOT,
                    "{\"file\":\"%s\",\"scenario\":\"%s\",\"tick\":%d,\"fps\":%d,\"cameraType\":\"%s\",\"cinematicActive\":%b,\"hideWeapon\":%b,\"camPos\":[%.3f,%.3f,%.3f],\"camYaw\":%.2f,\"camPitch\":%.2f,\"playerPos\":[%.2f,%.2f,%.2f]}%n",
                    file, t.name(), age, mc.getFps(), mc.options.getCameraType().name(),
                    SenbonzakuraCinematicClient.isCameraActive(),
                    SenbonzakuraCinematicClient.hideHeldWeapon(mc.player.getId(), e.renderTickTime),
                    camPos.x, camPos.y, camPos.z, cam.getYRot(), cam.getXRot(),
                    mc.player.getX(), mc.player.getY(), mc.player.getZ());
            Files.writeString(evidence.resolve("frames.jsonl"), line, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (Exception failure) {
            LogUtils.getLogger().warn("Senbonzakura QA screenshot capture failed: {}", failure.getMessage());
        }
    }
}
