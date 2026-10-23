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
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Husk;
import net.minecraft.world.item.ItemStack;
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
 * Automated in-game validation sequence and screenshot capture for Byakuya Kuchiki's Bankai: Senbonzakura Kageyoshi.
 * Captures the 100-tick cinematic sequence:
 *   - The vertical drop of the katana into water ripples (t=0..20)
 *   - The 12 colossal rising blades emerging symmetrically (t=20..55)
 *   - The stoic stance and "Chire" command gesture with resonant tremor (t=55..75)
 *   - The dual cyclone vortex of shattering petals and combat recovery (t=75..100)
 */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT)
public final class SenbonzakuraQaSequence {
    private static final String[] CASES = {
            "01_bankai_transformation",
            "02_bankai_combat_chire"
    };

    private record Trial(String name, UUID owner, Vec3 origin, long start, int maxTicks, List<UUID> targets) { }

    private static volatile Trial trial;
    private static volatile boolean serverFinished;
    private static volatile int serverAge;
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
                    c.getSource().sendSuccess(() -> net.minecraft.network.chat.Component.literal("§d[Senbonzakura QA] Starting test suite..."), false);
                    return 1;
                }))
                .then(literal("stop").executes(c -> {
                    stage = CASES.length + 1;
                    trial = null;
                    manualTriggered = false;
                    c.getSource().sendSuccess(() -> net.minecraft.network.chat.Component.literal("§c[Senbonzakura QA] Stopped."), false);
                    return 1;
                })));
    }

    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent e) {
        if (e.phase != TickEvent.Phase.END || !enabled()) return;
        var mc = Minecraft.getInstance();
        if (mc.screen != null || mc.isPaused()) return;
        // Wait until previous sequences finish if automated
        if (!manualTriggered && (!BenihimeQaBootstrap.automatedFinished() || !HyorinmaruQaSequence.automatedFinished())) return;

        wait++;

        if (stage == 0 && wait == 1) {
            previousCamera = mc.options.getCameraType();
            previousHud = mc.options.hideGui;
        }

        if (stage < CASES.length) {
            int neededWait = (stage == 0) ? 40 : (trial != null ? trial.maxTicks() + 20 : 100);
            if ((stage == 0 && wait >= neededWait) || (stage > 0 && serverFinished && wait >= 40)) {
                var name = CASES[stage];
                var id = mc.player.getUUID();
                int index = stage++;
                wait = 0;
                serverFinished = false;
                serverAge = 0;
                trial = null;
                lastFrame = -1;

                mc.options.setCameraType(CameraType.THIRD_PERSON_BACK);
                mc.options.hideGui = true;

                evidence = mc.gameDirectory.toPath().resolve("senbonzakura-qa/cycle101-" + RUN_ID + "/" + name);
                try {
                    Files.createDirectories(evidence);
                } catch (IOException ignored) { }

                mc.getSingleplayerServer().execute(() -> prepareServer(mc.getSingleplayerServer().getPlayerList().getPlayer(id), index, name));
            }
        } else if (stage == CASES.length && serverFinished && wait >= 40) {
            stage++;
            trial = null;
            mc.options.setCameraType(previousCamera != null ? previousCamera : CameraType.FIRST_PERSON);
            mc.options.hideGui = previousHud;
            LogUtils.getLogger().info("=================================================================");
            LogUtils.getLogger().info("Senbonzakura QA sequence COMPLETE: All scenarios executed & captured!");
            LogUtils.getLogger().info("=================================================================");
            if (mc.player != null) {
                mc.player.displayClientMessage(net.minecraft.network.chat.Component.literal("§d[Senbonzakura QA] Sequência finalizada com sucesso! Screenshots salvas em senbonzakura-qa/"), false);
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

        int x = 120 + 60 * index;
        int y = p.serverLevel().getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, 0);
        var origin = new Vec3(x + 0.5, y, 0.5);
        p.teleportTo(p.serverLevel(), origin.x, origin.y, origin.z, 0, 0);

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

        var targets = new ArrayList<UUID>();
        int maxTicks = 115;

        switch (index) {
            case 0 -> {
                // Bankai Transformation: 100 ticks
                d.getCharacter().setActiveForm("zanpakuto", "shikai");
                d.getCharacter().setSelectedForm("zanpakuto", "shikai");
                ReleaseAnimationService.begin(p, d, "zanpakuto", "bankai", 0f);
                maxTicks = SenbonzakuraCinematicService.DURATION + 15;
                LogUtils.getLogger().info("Senbonzakura QA: Started Bankai Transformation");
            }
            case 1 -> {
                // Bankai Combat / Petal Attack
                d.getCharacter().setActiveForm("zanpakuto", "bankai");
                d.getCharacter().setSelectedForm("zanpakuto", "bankai");
                Husk target = EntityType.HUSK.create(p.serverLevel());
                if (target != null) {
                    target.setPos(origin.add(0, 0, 12));
                    target.setNoAi(true);
                    target.setPersistenceRequired();
                    target.getAttribute(Attributes.MAX_HEALTH).setBaseValue(500);
                    target.setHealth(500);
                    target.setYRot(180);
                    p.serverLevel().addFreshEntity(target);
                    targets.add(target.getUUID());
                }
                maxTicks = 60;
                LogUtils.getLogger().info("Senbonzakura QA: Bankai Combat Showcase");
            }
        }

        SyncHelper.full(p);
        SyncHelper.appearance(p);
        p.serverLevel().setDayTime(6000);

        serverFinished = false;
        serverAge = 0;
        trial = new Trial(name, p.getUUID(), origin, p.level().getGameTime(), maxTicks, List.copyOf(targets));
        LogUtils.getLogger().info("Senbonzakura QA prepared {} at {} targets={}", name, origin, targets.size());
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

        // Cast technique for slot 1 at tick 10 in combat trial
        if (age == 10 && t.name().contains("combat")) {
            PlayerData d = PlayerCapability.get(p).orElse(null);
            if (d != null) {
                TechniqueService.executeSlot(p, d, 1);
                LogUtils.getLogger().info("Senbonzakura QA executed Slot 1 (Petal Cut)");
            }
        }

        if (age >= t.maxTicks()) {
            serverFinished = true;
            for (var tid : t.targets()) {
                var ent = p.serverLevel().getEntity(tid);
                if (ent != null) ent.discard();
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

        // Capture key dramatic ticks and periodic milestones
        boolean keyTick = (age == 0 || age == 10 || age == 35 || age == 76 || age == 120 || age == 160 || age == 200 || age == 228 || age == 266 || age == 290 || (age % 16 == 0));
        if (!keyTick || lastFrame == age) return;
        lastFrame = age;

        String file = String.format(Locale.ROOT, "frame-t%03d.png", age);
        try {
            Screenshot.grab(evidence.toFile(), file, mc.getMainRenderTarget(), msg -> { });
            String line = String.format(Locale.ROOT,
                    "{\"file\":\"%s\",\"scenario\":\"%s\",\"tick\":%d,\"fps\":%d,\"playerPos\":[%.2f,%.2f,%.2f]}%n",
                    file, t.name(), age, mc.getFps(), mc.player.getX(), mc.player.getY(), mc.player.getZ());
            Files.writeString(evidence.resolve("frames.jsonl"), line, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (Exception failure) {
            LogUtils.getLogger().warn("Senbonzakura QA screenshot capture failed: {}", failure.getMessage());
        }
    }
}
