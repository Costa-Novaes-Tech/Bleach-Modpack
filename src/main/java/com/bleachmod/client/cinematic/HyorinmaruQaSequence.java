package com.bleachmod.client.cinematic;

import com.bleachmod.Reference;
import com.bleachmod.common.data.PlayerCapability;
import com.bleachmod.common.data.PlayerData;
import com.bleachmod.common.network.SyncHelper;
import com.bleachmod.common.technique.HyorinmaruCinematicService;
import com.bleachmod.common.technique.HyorinmaruTechniqueService;
import com.bleachmod.common.technique.ReleaseAnimationService;
import com.bleachmod.common.technique.TechniqueService;
import com.bleachmod.init.ModItems;
import com.mojang.logging.LogUtils;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import net.minecraft.core.BlockPos;
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
 * Automated in-game validation sequence and screenshot capture for Daiguren Hyorinmaru.
 * Tests Bankai Transformation, 3-headed Hydra appearance, Wings/Lotus layer, and Bankai techniques.
 */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT)
public final class HyorinmaruQaSequence {
    private static final String[] CASES = {
            "01_bankai_transformation",
            "02_bankai_wings_lotus",
            "03_slot1_ryusenka",
            "04_slot2_hydra_assault",
            "05_slot3_ice_eruption",
            "06_slot4_blizzard_zone",
            "07_shikai_slot3_sennen",
            "08_sealed_armor",
            "09_slot2_hydra_wall",
            "10_slot2_hydra_uneven"
    };

    private record Trial(String name, UUID owner, Vec3 origin, long start, int maxTicks, List<UUID> targets) { }

    private static volatile Trial trial;
    private static volatile boolean serverFinished;
    private static volatile int serverAge;
    private static int stage = 0;
    private static int wait = 0;
    private static int lastFrame = -1;
    private static Path evidence;
    private static final long RUN_ID=System.currentTimeMillis();
    private static CameraType previousCamera;
    private static boolean previousHud;
    private static boolean manualTriggered = false;
    private static java.lang.reflect.Method cameraPosition;

    private HyorinmaruQaSequence() { }

    /** Wide laboratory view only; never changes the camera of a normal game session. */
    @SubscribeEvent(priority=net.minecraftforge.eventbus.api.EventPriority.LOWEST)
    public static void wideAttackView(net.minecraftforge.client.event.ViewportEvent.ComputeCameraAngles e) {
        var t=trial;
        if(t==null || !enabled())return;
        var mc=Minecraft.getInstance();if(mc.level==null)return;
        Vec3 eye = null;
        Vec3 focus = null;
        if (t.name().contains("slot1")) {
            eye = t.origin().add(3.8, 1.4, 4.8);
            focus = t.origin().add(0, 1.0, 1.0);
        } else if (t.name().contains("slot4")) {
            eye = t.origin().add(10.0, 3.5, 14.0);
            focus = t.origin().add(0, 9.5, 1.0);
        } else if (t.name().contains("slot2") || t.name().contains("slot3")) {
            eye = t.origin().add(22, 14, 28);
            focus = t.origin().add(0, 5, 8);
        }
        if (eye == null || focus == null) return;
        try {
            if(cameraPosition==null)cameraPosition=net.minecraftforge.fml.util.ObfuscationReflectionHelper.findMethod(net.minecraft.client.Camera.class,"m_90581_",Vec3.class);
            cameraPosition.invoke(e.getCamera(),eye);
            var d=focus.subtract(eye);
            e.setYaw((float)Math.toDegrees(Math.atan2(-d.x,d.z)));
            e.setPitch((float)-Math.toDegrees(Math.atan2(d.y,d.horizontalDistance())));e.setRoll(0);
        }catch(ReflectiveOperationException failure){LogUtils.getLogger().warn("QA wide view unavailable",failure);}
    }
    public static boolean automatedFinished() { return stage > CASES.length; }

    private static boolean enabled() {
        var mc = Minecraft.getInstance();
        if (net.minecraftforge.fml.loading.FMLEnvironment.production) return false;
        if (manualTriggered && mc.player != null) return true;
        return Boolean.getBoolean("bleachmod.hyorinmaruQa")
                && Boolean.getBoolean("bleachmod.benihimeQa")
                && mc.hasSingleplayerServer() && mc.player != null && mc.player.hasPermissions(2)
                && mc.gameDirectory.getAbsolutePath().replace('\\', '/').contains("/run-benihime-qa/")
                && mc.getSingleplayerServer().getWorldData().getLevelName().startsWith("Benihime-QA-68-");
    }

    @SubscribeEvent
    public static void registerCommands(RegisterClientCommandsEvent e) {
        e.getDispatcher().register(literal("hyorinmaruqa")
                .then(literal("start").executes(c -> {
                    manualTriggered = true;
                    stage = 0;
                    wait = 0;
                    trial = null;
                    c.getSource().sendSuccess(() -> net.minecraft.network.chat.Component.literal("§b[Hyorinmaru QA] Starting test suite..."), false);
                    return 1;
                }))
                .then(literal("stop").executes(c -> {
                    stage = CASES.length + 1;
                    trial = null;
                    manualTriggered = false;
                    c.getSource().sendSuccess(() -> net.minecraft.network.chat.Component.literal("§c[Hyorinmaru QA] Stopped."), false);
                    return 1;
                })));
    }

    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent e) {
        if (e.phase != TickEvent.Phase.END || !enabled()) return;
        var mc = Minecraft.getInstance();
        if (mc.screen != null || mc.isPaused() || !BenihimeQaBootstrap.automatedFinished()) return;

        wait++;

        if (stage == 0 && wait == 1) {
            previousCamera = mc.options.getCameraType();
            previousHud = mc.options.hideGui;
        }

        if (stage < CASES.length) {
            int neededWait = (stage == 0) ? 40 : trial != null ? trial.maxTicks()+20 : 100;
            if ((stage==0 && wait>=neededWait) || (stage>0 && serverFinished && wait>=40)) {
                var name = CASES[stage];
                var id = mc.player.getUUID();
                int index = stage++;
                wait = 0;
                serverFinished=false;serverAge=0;trial=null;
                lastFrame = -1;

                mc.options.setCameraType(index == 1 ? CameraType.THIRD_PERSON_FRONT : CameraType.THIRD_PERSON_BACK);
                mc.options.hideGui = true;

                evidence = mc.gameDirectory.toPath().resolve("hyorinmaru-qa/cycle93-" + RUN_ID + "/" + name);
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
            LogUtils.getLogger().info("Hyorinmaru QA sequence COMPLETE: All 10 scenarios executed & captured!");
            LogUtils.getLogger().info("=================================================================");
            if (mc.player != null) {
                mc.player.displayClientMessage(net.minecraft.network.chat.Component.literal("§a[Hyorinmaru QA] Sequência finalizada com sucesso! Screenshots salvas em hyorinmaru-qa/"), false);
            }
            if (Boolean.getBoolean("bleachmod.qaAutoExit") && !Boolean.getBoolean("bleachmod.benihimeCombatQa") && !Boolean.getBoolean("bleachmod.senbonzakuraQa")) {
                LogUtils.getLogger().info("Hyorinmaru QA auto-exit requested; closing client cleanly...");
                mc.stop();
            }
        }
    }

    private static void prepareServer(ServerPlayer p, int index, String name) {
        if (p == null) return;
        p.getServer().setDifficulty(net.minecraft.world.Difficulty.NORMAL, true);
        HyorinmaruCinematicService.cancel(p);
        HyorinmaruTechniqueService.cancel(p);
        ReleaseAnimationService.cancel(p);

        var d = PlayerCapability.get(p).orElse(null);
        if (d == null) return;

        int x = 60 * index;
        int y = p.serverLevel().getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, 0);
        var origin = new Vec3(x + 0.5, y, 0.5);
        p.teleportTo(p.serverLevel(), origin.x, origin.y, origin.z, 0, 0);

        d.getCharacter().bindZanpakuto("hyorinmaru");
        d.getSkills().get("zanpakuto").setLevel(2);
        for (String form : List.of("sealed", "shikai", "bankai")) {
            d.getCharacter().unlockForm(form);
            d.getCharacter().setMastery("zanpakuto", form, 100);
        }
        d.getCharacter().preserveLegacyTechniques();

        p.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.HYORINMARU.get()));
        p.setItemSlot(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
        d.getStatus().clearCooldowns();
        d.getGuard().interrupt();
        d.getResources().setCurrentReiatsu(d.getResources().getMaxReiatsu());

        var targets = new ArrayList<UUID>();
        int maxTicks = 80;

        switch (index) {
            case 0 -> {
                // Bankai Transformation
                d.getCharacter().setActiveForm("zanpakuto", "shikai");
                d.getCharacter().setSelectedForm("zanpakuto", "shikai");
                ReleaseAnimationService.begin(p, d, "zanpakuto", "bankai", 0f);
                maxTicks = HyorinmaruCinematicService.DURATION + 12;
                LogUtils.getLogger().info("Hyorinmaru QA: Started Bankai Transformation");
            }
            case 1 -> {
                // Bankai Wings & Lotus Idle Showcase
                d.getCharacter().setActiveForm("zanpakuto", "bankai");
                d.getCharacter().setSelectedForm("zanpakuto", "bankai");
                maxTicks = 40;
                LogUtils.getLogger().info("Hyorinmaru QA: Bankai Wings & Lotus Showcase");
            }
            case 7 -> {d.getCharacter().setActiveForm("zanpakuto","sealed");d.getCharacter().setSelectedForm("zanpakuto","sealed");maxTicks=140;}
            case 2, 3, 4, 5, 6, 8, 9 -> {
                // Approved Bankai slots and enlarged Shikai Sennen
                d.getCharacter().setActiveForm("zanpakuto", "bankai");
                d.getCharacter().setSelectedForm("zanpakuto", "bankai");

                if(index==6){d.getCharacter().setActiveForm("zanpakuto","shikai");d.getCharacter().setSelectedForm("zanpakuto","shikai");}
                if(index==8)for(int bx=-14;bx<=14;bx++)for(int by=0;by<6;by++)p.serverLevel().setBlock(net.minecraft.core.BlockPos.containing(origin).offset(bx,by,10),net.minecraft.world.level.block.Blocks.STONE.defaultBlockState(),3);
                if(index==9)for(int bx=-14;bx<=14;bx++)for(int bz=24;bz<=38;bz++)for(int by=0;by<Math.floorMod(bx+bz,3);by++)p.serverLevel().setBlock(net.minecraft.core.BlockPos.containing(origin).offset(bx,by,bz),net.minecraft.world.level.block.Blocks.STONE.defaultBlockState(),3);
                Husk target = EntityType.HUSK.create(p.serverLevel());
                if (target != null) {
                    target.setPos(origin.add(0, 0, index == 2 ? 10 : index == 3 ? 30 : index==4?24:index==6?16:index==9?30:6));
                    if(index==9)target.setPos(target.getX(),p.serverLevel().getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,target.blockPosition().getX(),target.blockPosition().getZ()),target.getZ());
                    target.setNoAi(true);
                    target.setPersistenceRequired();
                    target.getAttribute(Attributes.MAX_HEALTH).setBaseValue(500);
                    target.setHealth(500);
                    target.setYRot(180);
                    p.serverLevel().addFreshEntity(target);
                    targets.add(target.getUUID());
                }
                maxTicks = 110;
            }
        }

        SyncHelper.full(p);
        SyncHelper.appearance(p);
        p.serverLevel().setDayTime(6000);

        serverFinished=false;serverAge=0;
        trial = new Trial(name, p.getUUID(), origin, p.level().getGameTime(), maxTicks, List.copyOf(targets));
        LogUtils.getLogger().info("Hyorinmaru QA prepared {} at {} targets={}", name, origin, targets.size());
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
        serverAge=(int)age;

        // Cast technique for slots 1-4 at tick 10
        if (age == 10) {
            PlayerData d = PlayerCapability.get(p).orElse(null);
            if (d != null) {
                if(t.name().contains("sealed_armor")){TechniqueService.executeSlot(p,d,3);}
                else if (t.name().contains("slot1")) {
                    TechniqueService.executeSlot(p, d, 1);
                    LogUtils.getLogger().info("Hyorinmaru QA executed Slot 1 (Ryusenka)");
                } else if (t.name().contains("slot2")) {
                    TechniqueService.executeSlot(p, d, 2);
                    LogUtils.getLogger().info("Hyorinmaru QA executed Slot 2 (Hydra Assault)");
                } else if (t.name().contains("slot3")) {
                    TechniqueService.executeSlot(p, d, 3);
                    LogUtils.getLogger().info("Hyorinmaru QA executed Slot 3 (Eruption / Shikai Sennen)");
                } else if (t.name().contains("slot4")) {
                    TechniqueService.executeSlot(p, d, 4);
                    LogUtils.getLogger().info("Hyorinmaru QA executed Slot 4 (Blizzard Zone)");
                }
            }
        }

        if(age==30 || age==65 || age==100)for(var id:t.targets()){var target=p.serverLevel().getEntity(id);if(target instanceof net.minecraft.world.entity.LivingEntity living)LogUtils.getLogger().info("Hyorinmaru QA target {} tick={} health={} frozen={}",t.name(),age,living.getHealth(),living.getTicksFrozen());}
        // Cleanup targets at end of trial
        if (age >= t.maxTicks()) {
            serverFinished=true;
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

        // Capture every 4 ticks (or key ticks 0, 15, 25, 40, 70)
        int stride = 4;
        if (age % stride != 0 || lastFrame == age) return;
        lastFrame = age;

        String file = String.format(Locale.ROOT, "frame-t%03d.png", age);
        try {
            Screenshot.grab(evidence.toFile(), file, mc.getMainRenderTarget(), msg -> { });
            String line = String.format(Locale.ROOT,
                    "{\"file\":\"%s\",\"scenario\":\"%s\",\"tick\":%d,\"fps\":%d,\"playerPos\":[%.2f,%.2f,%.2f]}%n",
                    file, t.name(), age, mc.getFps(), mc.player.getX(), mc.player.getY(), mc.player.getZ());
            Files.writeString(evidence.resolve("frames.jsonl"), line, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (Exception failure) {
            LogUtils.getLogger().warn("Hyorinmaru QA screenshot capture failed: {}", failure.getMessage());
        }
    }
}
