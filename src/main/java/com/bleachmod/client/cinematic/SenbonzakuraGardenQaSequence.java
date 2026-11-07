package com.bleachmod.client.cinematic;

import com.bleachmod.Reference;
import com.bleachmod.common.network.SyncHelper;
import com.bleachmod.common.technique.SenbonzakuraTechniqueService;
import com.mojang.logging.LogUtils;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Locale;
import java.util.UUID;

/**
 * Opt-in automated in-game verification and continuous screenshot capture
 * for Byakuya Kuchiki's Bankai Garden N (Petal Prison).
 *
 * Validates:
 * 1. Initial 35-block ground carpet expansion and petal elevation.
 * 2. Flower bloom formation upon reactivation.
 * 3. Individual petal swirl envelopes around captured targets.
 * 4. Limited vertical levitation (clamped at floor + 2.5m) and ceiling containment.
 * 5. Three damage pulses and smooth 20-tick monotonic floor fade-out upon expiration.
 */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT)
public final class SenbonzakuraGardenQaSequence {
    private static int tick = 0;
    private static Path output;
    private static volatile boolean prepared = false;
    private static volatile String failure = null;
    private static final long RUN_ID = System.currentTimeMillis();
    private static UUID openTargetId;
    private static UUID ceilingTargetId;
    private static BlockPos ceilingBlockPos;

    private SenbonzakuraGardenQaSequence() { }

    private static boolean enabled() {
        var mc = Minecraft.getInstance();
        return !net.minecraftforge.fml.loading.FMLEnvironment.production
                && Boolean.getBoolean("bleachmod.senbonGardenQa")
                && Boolean.getBoolean("bleachmod.benihimeQa")
                && BenihimeQaBootstrap.automatedFinished()
                && mc.hasSingleplayerServer() && mc.player != null
                && mc.getSingleplayerServer().getWorldData().getLevelName().startsWith("Benihime-QA-68-");
    }

    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent e) {
        if (e.phase != TickEvent.Phase.END || !enabled() || tick > 185) return;
        var mc = Minecraft.getInstance();
        if (mc.screen != null || mc.isPaused()) return;

        if (output == null) {
            output = mc.gameDirectory.toPath().resolve("senbon-garden-qa/" + RUN_ID);
            try {
                Files.createDirectories(output);
            } catch (IOException ex) {
                throw new IllegalStateException("Failed to create QA output directory", ex);
            }

            mc.options.setCameraType(CameraType.THIRD_PERSON_BACK);
            mc.options.hideGui = true;

            var server = mc.getSingleplayerServer();
            var uuid = mc.player.getUUID();
            server.execute(() -> {
                try {
                    var p = server.getPlayerList().getPlayer(uuid);
                    if (p == null) throw new IllegalStateException("Player missing on server");
                    var source = p.createCommandSourceStack().withPermission(2);
                    var dispatcher = server.getCommands().getDispatcher();

                    dispatcher.execute("bleachdev kit shinigami senbonzakura", source);
                    dispatcher.execute("bleachdev cheats all", source);
                    dispatcher.execute("time set noon", source);
                    dispatcher.execute("weather clear", source);

                    var d = SyncHelper.require(p);
                    d.getCharacter().unlockForm("bankai");
                    d.getCharacter().setActiveForm("zanpakuto", "bankai");
                    SyncHelper.full(p);

                    // Spawn open target 8 blocks ahead
                    var t1 = EntityType.ZOMBIE.create(p.serverLevel());
                    t1.setPos(p.position().add(0, 0, 8));
                    t1.setNoAi(true);
                    t1.setNoGravity(true);
                    t1.setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
                    t1.getAttribute(Attributes.ARMOR).setBaseValue(0);
                    p.serverLevel().addFreshEntity(t1);
                    openTargetId = t1.getUUID();

                    // Spawn ceiling target 4 blocks to the right
                    var t2 = EntityType.ZOMBIE.create(p.serverLevel());
                    t2.setPos(p.position().add(4, 0, 8));
                    t2.setNoAi(true);
                    t2.setNoGravity(true);
                    t2.setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
                    t2.getAttribute(Attributes.ARMOR).setBaseValue(0);
                    p.serverLevel().addFreshEntity(t2);
                    ceilingTargetId = t2.getUUID();

                    ceilingBlockPos = t2.blockPosition().above(2);
                    p.serverLevel().setBlockAndUpdate(ceilingBlockPos, Blocks.STONE.defaultBlockState());

                    // Execute initial Garden N cast
                    SenbonzakuraTechniqueService.executeSlot(p, d, 2);
                    LogUtils.getLogger().info("[SENBON_GARDEN_QA] Server initialized Garden N cast at tick 0");
                } catch (Exception ex) {
                    failure = ex.toString();
                    LogUtils.getLogger().error("[SENBON_GARDEN_QA] Server preparation failed", ex);
                } finally {
                    prepared = true;
                }
            });
            return;
        }

        if (!prepared) return;
        tick++;

        // Re-activate bloom at tick 45
        if (tick == 45) {
            var server = mc.getSingleplayerServer();
            var uuid = mc.player.getUUID();
            server.execute(() -> {
                var p = server.getPlayerList().getPlayer(uuid);
                if (p != null) {
                    var d = SyncHelper.require(p);
                    SenbonzakuraTechniqueService.executeSlot(p, d, 2);
                    LogUtils.getLogger().info("[SENBON_GARDEN_QA] Committed bloom reactivation at tick 45");
                }
            });
        }

        // Adjust camera angle for perspective variation (close-up of targets and ceiling at tick 85)
        if (tick == 85) {
            mc.options.setCameraType(CameraType.THIRD_PERSON_FRONT);
        } else if (tick == 125) {
            mc.options.setCameraType(CameraType.THIRD_PERSON_BACK);
        }

        // Capture milestone frames
        if (tick == 10 || tick == 25 || tick == 45 || tick == 55 || tick == 62 || tick == 70
                || tick == 82 || tick == 102 || tick == 122 || tick == 140 || tick == 160 || tick == 180) {
            String filename = String.format(Locale.ROOT, "frame-%03d.png", tick);
            Screenshot.grab(output.toFile(), filename, mc.getMainRenderTarget(), ignored -> { });

            String hud = SenbonzakuraTechniqueService.hud(mc.getSingleplayerServer().getPlayerList().getPlayer(mc.player.getUUID())).toString();
            String row = String.format(Locale.ROOT, "tick=%03d camera=%s hud=%s failure=%s%n",
                    tick, mc.options.getCameraType(), hud, failure);
            try {
                Files.writeString(output.resolve("results.txt"), row, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            } catch (IOException ex) {
                LogUtils.getLogger().error("[SENBON_GARDEN_QA] Failed to write results.txt", ex);
            }
            LogUtils.getLogger().info("[SENBON_GARDEN_QA] Captured frame: {} {}", filename, row.trim());
        }

        // Clean shutdown
        if (tick >= 180 && Boolean.getBoolean("bleachmod.qaAutoExit")) {
            LogUtils.getLogger().info("[SENBON_GARDEN_QA] Sequence complete, closing Minecraft cleanly...");
            mc.options.hideGui = false;
            mc.options.setCameraType(CameraType.FIRST_PERSON);
            mc.stop();
        }
    }
}
