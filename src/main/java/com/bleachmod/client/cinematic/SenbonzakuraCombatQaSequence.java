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
 * Automated QA sequence for Senbonzakura:
 * - Controlled fencing Better Combat combo (diagonal cut, horizontal return with petal ribbon, precise thrust)
 * - Shūkei: Hakuteiken 5 moments (preparation, complete form, aerodynamic launch, impact pulse, dissipation)
 * Validated across Front, Profile, and Back camera views.
 */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT)
public final class SenbonzakuraCombatQaSequence {
    private static final String[] CASES = {
            "01_combo_front",
            "02_combo_profile",
            "03_combo_back",
            "04_hakuteiken_front",
            "05_hakuteiken_profile",
            "06_hakuteiken_back"
    };

    private record Trial(UUID owner, UUID target, long start, int index, Vec3 origin) { }

    private static volatile Trial trial;
    private static int stage = 0, wait = 0, lastFrame = -1;
    private static Path evidence;
    private static CameraType previousCamera;
    private static boolean previousHud;
    private static boolean manualTriggered = false;
    private static java.lang.reflect.Method cameraPosition;

    private SenbonzakuraCombatQaSequence() { }

    public static boolean automatedFinished() { return stage > CASES.length; }

    private static boolean enabled() {
        var mc = Minecraft.getInstance();
        if (net.minecraftforge.fml.loading.FMLEnvironment.production) return false;
        if (manualTriggered && mc.player != null) return true;
        return Boolean.getBoolean("bleachmod.senbonCombatQa")
                && Boolean.getBoolean("bleachmod.benihimeQa")
                && mc.hasSingleplayerServer() && mc.player != null && mc.player.hasPermissions(2)
                && mc.gameDirectory.getAbsolutePath().replace('\\', '/').contains("/run-benihime-qa/")
                && mc.getSingleplayerServer().getWorldData().getLevelName().startsWith("Benihime-QA-68-");
    }

    @SubscribeEvent
    public static void registerCommands(RegisterClientCommandsEvent e) {
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
            // Combo attacks input simulation (swings on ticks 30, 44, 58)
            if (current.index() < 3) {
                mc.options.keyAttack.setDown(age >= 30 && age < 85);
            }
        }

        wait++;
        if (stage == 0 && wait < 40) return;
        if (stage == 0) {
            previousCamera = mc.options.getCameraType();
            previousHud = mc.options.hideGui;
        }

        if (stage < CASES.length && (stage == 0 || wait >= 140)) {
            var name = CASES[stage];
            var id = mc.player.getUUID();
            int index = stage++;
            wait = 0;
            lastFrame = -1;

            if (name.contains("front")) {
                mc.options.setCameraType(CameraType.THIRD_PERSON_FRONT);
            } else if (name.contains("back")) {
                mc.options.setCameraType(CameraType.THIRD_PERSON_BACK);
            } else {
                mc.options.setCameraType(CameraType.THIRD_PERSON_BACK); // Profile camera managed via ComputeCameraAngles
            }
            mc.options.hideGui = true;

            evidence = mc.gameDirectory.toPath().resolve("senbonzakura-combat-qa/" + System.currentTimeMillis() + "-" + name);
            mc.getSingleplayerServer().execute(() -> prepareServer(mc.getSingleplayerServer().getPlayerList().getPlayer(id), index));
        } else if (stage == CASES.length && wait >= 140) {
            stage++;
            trial = null;
            mc.options.keyAttack.setDown(false);
            mc.options.setCameraType(previousCamera != null ? previousCamera : CameraType.FIRST_PERSON);
            mc.options.hideGui = previousHud;
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
        String form = index >= 3 ? "bankai" : "shikai";
        d.getCharacter().setActiveForm("zanpakuto", form);
        d.getCharacter().setSelectedForm("zanpakuto", form);
        d.getStatus().clearCooldowns();
        d.getGuard().interrupt();
        d.getResources().setCurrentReiatsu(d.getResources().getMaxReiatsu());

        p.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.SENBONZAKURA.get()));
        p.setItemSlot(EquipmentSlot.OFFHAND, ItemStack.EMPTY);

        Husk h = EntityType.HUSK.create(p.serverLevel());
        if (h != null) {
            h.setPos(p.position().add(0, 0, index < 3 ? 2.2 : 10.0));
            h.setNoAi(true);
            h.setNoGravity(true);
            h.setPersistenceRequired();
            h.getAttribute(Attributes.MAX_HEALTH).setBaseValue(1000);
            h.setHealth(1000);
            h.setYRot(180);
            p.serverLevel().addFreshEntity(h);
        }

        SyncHelper.full(p);
        SyncHelper.appearance(p);

        trial = new Trial(p.getUUID(), h != null ? h.getUUID() : null, p.level().getGameTime(), index, p.position());
        LogUtils.getLogger().info("Senbonzakura Combat QA prepared {} target={} index={}", CASES[index], h != null ? h.getId() : -1, index);
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
        var target = t.target() != null ? p.serverLevel().getEntity(t.target()) : null;

        // Keep combo target in reach
        if (t.index() < 3 && (age == 30 || age == 44 || age == 58) && target != null) {
            target.setPos(p.position().add(0, 0, 2.2));
            target.setDeltaMovement(Vec3.ZERO);
            LogUtils.getLogger().info("Senbonzakura Combat QA swing {} age={}", CASES[t.index()], age);
        }

        // Hakuteiken cast at tick 40
        if (t.index() >= 3 && age == 40) {
            PlayerCapability.get(p).ifPresent(d -> {
                TechniqueService.executeSlot(p, d, 4);
                LogUtils.getLogger().info("Senbonzakura Combat QA cast Hakuteiken: busy={} blade={}", SenbonzakuraTechniqueService.busy(p), d.getCharacter().getSenbonBlade());
            });
        }

        if (age == 130 && target != null) {
            target.discard();
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void camera(ViewportEvent.ComputeCameraAngles e) {
        var t = trial;
        if (t == null || !enabled()) return;
        var mc = Minecraft.getInstance();
        if (mc.player == null) return;

        // Custom camera framing for profile view
        if (CASES[t.index()].contains("profile")) {
            Vec3 focus = mc.player.getPosition((float) e.getPartialTick()).add(0, 1.2, 0);
            Vec3 eye = focus.add(-4.5, 0.4, 0.5); // Lateral profile view from the side
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
    }

    @SubscribeEvent
    public static void capture(TickEvent.RenderTickEvent e) {
        var t = trial;
        if (e.phase != TickEvent.Phase.END || t == null || !enabled() || evidence == null) return;
        var mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) return;

        int age = (int) (mc.level.getGameTime() - t.start());
        if (age < 25 || age > 120 || lastFrame == age || mc.screen != null) return;

        // Capture key moments
        boolean captureThisTick = false;
        if (t.index() < 3) {
            // Combo: Strike 1 windup & cut (32, 38), Strike 2 return + petal ribbon (46, 52), Strike 3 thrust (60, 68), Recovery (76)
            captureThisTick = (age == 32 || age == 38 || age == 46 || age == 52 || age == 60 || age == 68 || age == 76);
        } else {
            // Hakuteiken: Preparation (44, 50), Full wings/zenith (56), Launch/dash (60, 64), Impact/contact (68), Recovery/dissipation (78, 88)
            captureThisTick = (age == 44 || age == 50 || age == 56 || age == 60 || age == 64 || age == 68 || age == 78 || age == 88);
        }

        if (!captureThisTick) return;
        lastFrame = age;

        String file = String.format(Locale.ROOT, "frame-t%03d.png", age);
        try {
            Files.createDirectories(evidence);
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
