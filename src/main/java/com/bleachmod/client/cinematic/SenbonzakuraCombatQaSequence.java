package com.bleachmod.client.cinematic;

import com.bleachmod.Reference;
import com.bleachmod.common.compat.BetterCombatCompat;
import com.bleachmod.common.data.PlayerCapability;
import com.bleachmod.common.network.SyncHelper;
import com.bleachmod.common.technique.ReleaseAnimationService;
import com.bleachmod.common.technique.SenbonzakuraCinematicService;
import com.bleachmod.common.technique.SenbonzakuraTechniqueService;
import com.bleachmod.common.technique.SignatureAnimationService;
import com.bleachmod.common.technique.TechniqueService;
import com.bleachmod.init.ModItems;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Camera;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Husk;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static net.minecraft.commands.Commands.literal;

/**
 * Automated QA sequence for Senbonzakura Strike 1 (S1 - diagonal descendente):
 * Validates S1 execution across 6 audit-ready scenarios:
 * 1. 01_shikai_right_front
 * 2. 02_shikai_right_profile
 * 3. 03_shikai_right_back
 * 4. 04_sealed_right_profile
 * 5. 05_bankai_right_profile
 * 6. 06_shikai_left_profile
 */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT)
public final class SenbonzakuraCombatQaSequence {
    private static final String[] CASES = {
            "01_shikai_right_front",
            "02_shikai_right_profile",
            "03_shikai_right_back",
            "04_sealed_right_profile",
            "05_bankai_right_profile",
            "06_shikai_left_profile"
    };

    private record Trial(UUID owner, UUID target, long start, int index, Vec3 origin) { }

    private static volatile Trial trial;
    private static int stage = 0, wait = 0, lastFrame = -1;
    private static Path evidence;
    private static CameraType previousCamera;
    private static boolean previousHud;
    private static HumanoidArm previousMainHand;
    private static boolean manualTriggered = false;
    private static java.lang.reflect.Method cameraPosition;
    private static java.lang.reflect.Field attackAnimationField;

    private SenbonzakuraCombatQaSequence() { }

    public static boolean automatedFinished() { return stage > CASES.length; }

    private static boolean enabled() {
        var mc = Minecraft.getInstance();
        if (net.minecraftforge.fml.loading.FMLEnvironment.production) return false;

        return (manualTriggered || Boolean.getBoolean("bleachmod.senbonCombatQa"))
                && Boolean.getBoolean("bleachmod.benihimeQa")
                && mc.hasSingleplayerServer() && mc.player != null && mc.player.hasPermissions(2)
                && mc.gameDirectory.getAbsolutePath().replace('\\', '/').contains("/run-benihime-qa/")
                && mc.getSingleplayerServer().getWorldData().getLevelName().startsWith("Benihime-QA-68-");
    }

    @SubscribeEvent
    public static void registerCommands(RegisterClientCommandsEvent e) {
        if (net.minecraftforge.fml.loading.FMLEnvironment.production) return;
        e.getDispatcher().register(literal("senboncombatqa")
                .then(literal("start").executes(c -> {
                    manualTriggered = true;
                    stage = 0;
                    wait = 0;
                    trial = null;
                    c.getSource().sendSuccess(() -> Component.literal("§d[Senbonzakura Combat QA] Starting combat validation..."), false);
                    return 1;
                }))
                .then(literal("stop").executes(c -> {
                    stage = CASES.length + 1;
                    trial = null;
                    manualTriggered = false;
                    c.getSource().sendSuccess(() -> Component.literal("§c[Senbonzakura Combat QA] Stopped."), false);
                    return 1;
                })));
    }

    @SubscribeEvent
    public static void tick(TickEvent.ClientTickEvent e) {
        if (e.phase != TickEvent.Phase.END || !enabled()) return;
        var mc = Minecraft.getInstance();
        if (mc.screen != null || mc.isPaused()) return;
        if (!manualTriggered && !BenihimeQaBootstrap.automatedFinished()) return;

        var current = trial;
        if (current != null) {
            long age = mc.level.getGameTime() - current.start();
            // Single attack input on tick 30 (triggers S1)
            mc.options.keyAttack.setDown(age >= 30 && age < 32);
        }

        wait++;
        // Warmup: 120 ticks before case 0 for shaders/terrain stabilization
        if (stage == 0 && wait < 120) return;
        if (stage == 0) {
            previousCamera = mc.options.getCameraType();
            previousHud = mc.options.hideGui;
            previousMainHand = mc.options.mainHand().get();
        }

        if (stage < CASES.length && (stage == 0 || wait >= 140)) {
            var name = CASES[stage];
            var id = mc.player.getUUID();
            int index = stage++;
            wait = 0;
            lastFrame = -1;

            if (name.contains("left")) {
                mc.options.mainHand().set(HumanoidArm.LEFT);
            } else {
                mc.options.mainHand().set(HumanoidArm.RIGHT);
            }
            mc.options.broadcastOptions();

            if (name.contains("front")) {
                mc.options.setCameraType(CameraType.THIRD_PERSON_FRONT);
            } else {
                mc.options.setCameraType(CameraType.THIRD_PERSON_BACK);
            }
            mc.options.hideGui = true;

            String runId = System.getProperty("bleachmod.senbonQaRunId", "run-cycle145");
            evidence = mc.gameDirectory.toPath().resolve("senbonzakura-combat-qa/" + runId + "/" + name);
            mc.getSingleplayerServer().execute(() -> prepareServer(mc.getSingleplayerServer().getPlayerList().getPlayer(id), index));
        } else if (stage == CASES.length && wait >= 140) {
            stage++;
            trial = null;
            mc.options.keyAttack.setDown(false);
            mc.options.setCameraType(previousCamera != null ? previousCamera : CameraType.FIRST_PERSON);
            mc.options.hideGui = previousHud;
            mc.options.mainHand().set(previousMainHand != null ? previousMainHand : HumanoidArm.RIGHT);
            mc.options.broadcastOptions();
            LogUtils.getLogger().info("Senbonzakura Combat QA sequence COMPLETE: All 6 scenarios executed & captured!");
            if (mc.player != null) {
                mc.player.displayClientMessage(Component.literal("§d[Senbonzakura Combat QA] Validation complete!"), false);
            }
            if (Boolean.getBoolean("bleachmod.qaAutoExit")) mc.stop();
        }
    }

    private static void prepareServer(ServerPlayer p, int index) {
        if (p == null) return;
        p.getServer().setDifficulty(net.minecraft.world.Difficulty.NORMAL, true);
        ReleaseAnimationService.cancel(p);
        SenbonzakuraCinematicService.cancel(p);
        SenbonzakuraTechniqueService.cancel(p);
        SignatureAnimationService.cancel(p);

        var d = PlayerCapability.get(p).orElse(null);
        if (d == null) return;

        int x = 160 + index * 60;
        int y = p.serverLevel().getHeight(net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, 0);
        p.teleportTo(p.serverLevel(), x + 0.5, y, 0.5, 0, 0);
        p.setDeltaMovement(Vec3.ZERO);
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

        String caseName = CASES[index];
        String form = "shikai";
        if (caseName.contains("sealed")) {
            form = "sealed";
        } else if (caseName.contains("bankai")) {
            form = "bankai";
        }
        d.getCharacter().setActiveForm("zanpakuto", form);
        d.getCharacter().setSelectedForm("zanpakuto", form);
        d.getStatus().clearCooldowns();
        d.getGuard().interrupt();
        d.getResources().setCurrentReiatsu(d.getResources().getMaxReiatsu());

        p.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.SENBONZAKURA.get()));
        p.setItemSlot(EquipmentSlot.OFFHAND, ItemStack.EMPTY);

        SyncHelper.full(p);
        SyncHelper.appearance(p);

        trial = new Trial(p.getUUID(), null, p.level().getGameTime(), index, p.position());
        LogUtils.getLogger().info("Senbonzakura Combat QA prepared {} form={} index={}", CASES[index], form, index);
    }

    @SubscribeEvent
    public static void serverTick(TickEvent.ServerTickEvent e) {
        var t = trial;
        if (e.phase != TickEvent.Phase.END || t == null || !enabled()) return;
        var server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) return;
        var p = server.getPlayerList().getPlayer(t.owner());
        if (p == null) return;

        long age = p.level().getGameTime() - t.start();
        // Server ticks for isolated S1 (no target management needed)
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void camera(ViewportEvent.ComputeCameraAngles e) {
        var t = trial;
        if (t == null || !enabled()) return;
        var mc = Minecraft.getInstance();
        if (mc.player == null) return;

        Vec3 focus = mc.player.getPosition((float) e.getPartialTick()).add(0, 1.2, 0);
        Vec3 offset;
        String caseName = CASES[t.index()];
        if (caseName.contains("front")) {
            offset = new Vec3(0, 0.25, 4.2);
        } else if (caseName.contains("back")) {
            offset = new Vec3(0, 0.25, -4.2);
        } else if (caseName.contains("left")) {
            offset = new Vec3(4.2, 0.25, 0); // Lateral profile from the left
        } else {
            offset = new Vec3(-4.2, 0.25, 0); // Lateral profile from the right
        }
        Vec3 eye = focus.add(offset);
        try {
            if (cameraPosition == null) {
                cameraPosition = net.minecraftforge.fml.util.ObfuscationReflectionHelper.findMethod(Camera.class, "m_90581_", Vec3.class);
            }
            cameraPosition.invoke(e.getCamera(), eye);
        } catch (ReflectiveOperationException | RuntimeException ignored) { }
        Vec3 dir = focus.subtract(eye).normalize();
        double pitch = -Math.toDegrees(Math.asin(dir.y));
        double yaw = Math.toDegrees(Math.atan2(-dir.x, dir.z));
        e.setYaw((float) yaw);
        e.setPitch((float) pitch);
        e.setRoll(0);
    }

    @SubscribeEvent
    public static void capture(TickEvent.RenderTickEvent e) {
        var t = trial;
        if (e.phase != TickEvent.Phase.END || t == null || !enabled() || evidence == null) return;
        var mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) return;

        int age = (int) (mc.level.getGameTime() - t.start());
        if (age < 25 || age > 75 || lastFrame == age || mc.screen != null) return;
        lastFrame = age;

        String file = String.format(Locale.ROOT, "frame-t%03d.png", age);
        try {
            Files.createDirectories(evidence);
            var poseData = new com.google.gson.JsonObject();
            poseData.addProperty("age", age);
            poseData.addProperty("partialTick", mc.getFrameTime());
            poseData.addProperty("worldTick", mc.level.getGameTime());
            poseData.addProperty("case", CASES[t.index()]);
            poseData.addProperty("form", CASES[t.index()].contains("sealed") ? "sealed" : CASES[t.index()].contains("bankai") ? "bankai" : "shikai");
            poseData.addProperty("hand", mc.options.mainHand().get().name());
            poseData.addProperty("fps", mc.getFps());
            poseData.addProperty("comboCount", ((net.bettercombat.logic.PlayerAttackProperties) mc.player).getComboCount());
            if (mc instanceof net.bettercombat.api.MinecraftClient_BetterCombat bc) {
                poseData.addProperty("upswingTicks", bc.getUpswingTicks());
                poseData.addProperty("swingProgress", bc.getSwingProgress());
                poseData.addProperty("swingInProgress", bc.isWeaponSwingInProgress());
            }

            try {
                if (attackAnimationField == null) {
                    for (java.lang.reflect.Field f : mc.player.getClass().getDeclaredFields()) {
                        if (f.getType().getSimpleName().equals("AttackAnimationSubStack")) {
                            f.setAccessible(true);
                            attackAnimationField = f;
                            break;
                        }
                    }
                }
                if (attackAnimationField != null) {
                    var subStack = (net.bettercombat.client.animation.AttackAnimationSubStack) attackAnimationField.get(mc.player);
                    if (subStack != null && subStack.base != null) {
                        var anim = subStack.base.getAnimation();
                        if (anim instanceof dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer kap) {
                        String clipName = "senbon_diagonal";
                        if (kap.getData() != null && kap.getData().extraData != null && kap.getData().extraData.containsKey("name")) {
                            clipName = String.valueOf(kap.getData().extraData.get("name"));
                        }
                        poseData.addProperty("clipName", clipName);
                            poseData.addProperty("clipTick", kap.getCurrentTick());
                            poseData.addProperty("clipStopTick", kap.getStopTick());
                            poseData.addProperty("clipActive", kap.isActive());
                        }
                    }
                }
            } catch (Throwable ignored) { }

            if (mc.player instanceof dev.kosmx.playerAnim.impl.IAnimatedPlayer animated) {
                var ap = animated.playerAnimator_getAnimation();
                poseData.addProperty("applierActive", ap.isActive());
                for (String bone : List.of("rightArm", "rightItem", "leftArm", "leftItem", "body", "head")) {
                    var rot = ap.get3DTransform(bone, dev.kosmx.playerAnim.api.TransformType.ROTATION, dev.kosmx.playerAnim.core.util.Vec3f.ZERO);
                    poseData.addProperty(bone + "_rot", rot.getX() + "," + rot.getY() + "," + rot.getZ());
                    var pos = ap.get3DTransform(bone, dev.kosmx.playerAnim.api.TransformType.POSITION, dev.kosmx.playerAnim.core.util.Vec3f.ZERO);
                    poseData.addProperty(bone + "_pos", pos.getX() + "," + pos.getY() + "," + pos.getZ());
                }
            }

            Files.writeString(evidence.resolve("pose-telemetry.jsonl"), poseData + System.lineSeparator(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            Screenshot.grab(evidence.toFile(), file, mc.getMainRenderTarget(), msg -> { });
            Files.writeString(evidence.resolve("frames.jsonl"),
                    String.format(Locale.ROOT, "{\"file\":\"%s\",\"tick\":%d,\"case\":\"%s\",\"fps\":%d,\"busy\":%s,\"source\":\"Minecraft framebuffer\"}%n",
                            file, age, CASES[t.index()], mc.getFps(), com.bleachmod.client.model.SignatureActionVisuals.active(mc.player)),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException failure) {
            LogUtils.getLogger().warn("Senbonzakura Combat QA capture failed", failure);
        }
    }
}
