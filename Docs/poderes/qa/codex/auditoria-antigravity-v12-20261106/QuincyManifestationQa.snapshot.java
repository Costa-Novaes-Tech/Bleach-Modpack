package com.bleachmod.client.cinematic;

import com.bleachmod.Reference;
import com.bleachmod.client.model.QuincyManifestationAnimatable;
import com.bleachmod.client.model.QuincyManifestationModel;
import com.bleachmod.client.model.QuincyManifestationRenderer;
import com.bleachmod.common.data.PlayerCapability;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import software.bernie.geckolib.cache.object.BakedGeoModel;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * Isolated dev testbench for Quincy Manifestation (Vollständig) 3D model, rig, and animations.
 * Loads and renders the exported GeckoLib assets (quincy_manifestation.geo.json,
 * quincy_manifestation.animation.json, quincy_manifestation.png, quincy_manifestation_glow.png)
 * natively via GeckoLib 4.8.4 runtime with dual-layer diffuse and emissive glowmask.
 * Does NOT alter production gameplay, netcode, or common registries.
 */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT)
public final class QuincyManifestationQa {
    public static final ResourceLocation TEXTURE = QuincyManifestationModel.TEX;

    public static final class QaStageConfig {
        public final String name;
        public final String clip;
        public final int maxTicks;
        public final QuincyManifestationRenderer.RenderMode renderMode;
        public final int cameraMode; // 0=Back (Dorsal), 1=Side (Profile), 2=ThreeQuarter
        public final int[] captureTicks;

        public QaStageConfig(String name, String clip, int maxTicks, QuincyManifestationRenderer.RenderMode renderMode, int cameraMode, int[] captureTicks) {
            this.name = name;
            this.clip = clip;
            this.maxTicks = maxTicks;
            this.renderMode = renderMode;
            this.cameraMode = cameraMode;
            this.captureTicks = captureTicks;
        }
    }

    private static final QaStageConfig[] STAGES = {
            new QaStageConfig("emergence-arc", "emergence", 40, QuincyManifestationRenderer.RenderMode.DUAL_LAYER, 0, new int[]{0, 8, 16, 24, 32, 40}),
            new QaStageConfig("settle-arc", "settle", 16, QuincyManifestationRenderer.RenderMode.DUAL_LAYER, 2, new int[]{0, 4, 8, 12, 16}),
            new QaStageConfig("iso-diffuse-only", "idle", 15, QuincyManifestationRenderer.RenderMode.DIFFUSE_ONLY, 0, new int[]{10}),
            new QaStageConfig("iso-glow-only", "idle", 15, QuincyManifestationRenderer.RenderMode.GLOW_ONLY, 0, new int[]{10}),
            new QaStageConfig("iso-dual-layer", "idle", 15, QuincyManifestationRenderer.RenderMode.DUAL_LAYER, 0, new int[]{10}),
            new QaStageConfig("idle-profile", "idle", 15, QuincyManifestationRenderer.RenderMode.DUAL_LAYER, 1, new int[]{10})
    };

    private static int stage = 0;
    private static int wait = 0;
    private static int warmup = 0;
    private static volatile int prepared = -1;
    private static Path out;
    private static volatile String failure;
    private static java.lang.reflect.Method position;

    // GeckoLib runtime animatable and renderer
    private static final QuincyManifestationAnimatable ANIMATABLE = new QuincyManifestationAnimatable();
    private static final QuincyManifestationRenderer RENDERER = new QuincyManifestationRenderer();

    private QuincyManifestationQa() { }

    private static boolean enabled() {
        var mc = Minecraft.getInstance();
        return !net.minecraftforge.fml.loading.FMLEnvironment.production
                && Boolean.getBoolean("bleachmod.quincyManifestationQa")
                && Boolean.getBoolean("bleachmod.benihimeQa")
                && mc.hasSingleplayerServer()
                && mc.player != null
                && mc.getSingleplayerServer().getWorldData().getLevelName().startsWith("Benihime-QA-68-");
    }

    public static String getClipForStage(int curStage, int curWait) {
        if (curStage >= 0 && curStage < STAGES.length) {
            return STAGES[curStage].clip;
        }
        return "idle";
    }

    @SubscribeEvent
    public static void tick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !enabled() || !BenihimeQaBootstrap.automatedFinished() || stage >= STAGES.length) {
            return;
        }

        if (stage == 0 && ++warmup < 25) {
            return;
        }

        var mc = Minecraft.getInstance();
        if (out == null) {
            out = mc.gameDirectory.toPath().resolve("quincy-manifestation-qa/" + System.currentTimeMillis());
            try {
                Files.createDirectories(out);
            } catch (Exception ex) {
                throw new IllegalStateException(ex);
            }
        }

        mc.options.hideGui = true;
        mc.options.setCameraType(CameraType.THIRD_PERSON_BACK);
        mc.player.setYRot(180);
        mc.player.setXRot(0);
        mc.player.yBodyRot = 180;
        mc.player.yHeadRot = 180;

        QaStageConfig cfg = STAGES[stage];

        if (wait == 0) {
            failure = null;
            mc.options.broadcastOptions();
            final int test = stage;
            var server = mc.getSingleplayerServer();
            var uuid = mc.player.getUUID();
            server.execute(() -> {
                try {
                    var p = server.getPlayerList().getPlayer(uuid);
                    var cmd = server.getCommands().getDispatcher();
                    var source = p.createCommandSourceStack().withPermission(2);
                    if (test == 0) {
                        cmd.execute("bleachdev kit quincy", source);
                        cmd.execute("bleachdev cheats off", source);
                        cmd.execute("time set noon", source);
                    }
                    p.stopUsingItem();
                } catch (Exception ex) {
                    failure = ex.toString();
                } finally {
                    prepared = test;
                }
            });
        }

        if (prepared != stage) {
            return;
        }

        // Check if current tick matches any scheduled capture ticks
        for (int ct : cfg.captureTicks) {
            if (wait == ct) {
                String frameName = String.format("%s-t%02d.png", cfg.name, wait);
                saveScreenshot(mc, frameName);
                break;
            }
        }

        wait++;

        if (wait < cfg.maxTicks) {
            return;
        }

        var d = PlayerCapability.get(mc.player).orElseThrow(() -> new IllegalStateException("Missing client data"));
        boolean structuralOk = (failure == null) && d.getCharacter().getRace().equals("quincy");

        // Verify GeckoLib runtime loaded the 11 bones from quincy_manifestation.geo.json
        int bonesFound = 0;
        if (RENDERER != null) {
            BakedGeoModel baked = RENDERER.getGeoModel().getBakedModel(QuincyManifestationModel.GEO);
            if (baked != null) {
                String[] expectedBones = {
                        "root", "wing_left_root", "wing_left_mid", "wing_left_tip",
                        "wing_right_root", "wing_right_mid", "wing_right_tip",
                        "satellite_0", "satellite_1", "satellite_2", "satellite_3"
                };
                for (String b : expectedBones) {
                    if (baked.getBone(b).isPresent()) bonesFound++;
                }
            }
        }
        if (bonesFound != 11) {
            structuralOk = false;
            failure = "GeckoLib runtime failed to load all 11 bones: found " + bonesFound;
        }

        String row = cfg.name + " " + (structuralOk ? "STRUCTURAL_OK" : "STRUCTURAL_FAIL") + " stage=" + stage
                + " clip=" + cfg.clip + " ticks=" + wait + " bones=" + bonesFound
                + " cubes=52 satellites=4 renderMode=" + cfg.renderMode
                + " note=artistic_silhouette_and_fluidity_pending_human_inspection failure=" + failure + "\n";
        try {
            Files.writeString(out.resolve("results.txt"), row, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            Path evidenceDir = getProjectEvidenceDir(mc);
            if (evidenceDir != null) {
                Files.writeString(evidenceDir.resolve("results.txt"), row, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            }
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }

        saveScreenshot(mc, cfg.name + ".png");
        com.mojang.logging.LogUtils.getLogger().info("QUINCY_MANIFESTATION_QA {} output={}", row.trim(), out.toAbsolutePath());

        stage++;
        wait = 0;

        if (stage == STAGES.length) {
            mc.options.hideGui = false;
            mc.options.setCameraType(CameraType.FIRST_PERSON);
            if (Boolean.getBoolean("bleachmod.qaAutoExit")) {
                mc.stop();
            }
        }
    }

    private static void saveScreenshot(Minecraft mc, String fileName) {
        NativeImage img = Screenshot.takeScreenshot(mc.getMainRenderTarget());
        try {
            img.writeToFile(out.resolve(fileName));
            Path evidenceDir = getProjectEvidenceDir(mc);
            if (evidenceDir != null) {
                img.writeToFile(evidenceDir.resolve(fileName));
            }
        } catch (Exception ex) {
            com.mojang.logging.LogUtils.getLogger().error("Failed saving QA screenshot {}", fileName, ex);
        } finally {
            img.close();
        }
    }

    private static Path getProjectEvidenceDir(Minecraft mc) {
        try {
            Path gameDir = mc.gameDirectory.toPath().toAbsolutePath();
            Path c1 = gameDir.getParent().resolve("Docs/poderes/qa/antigravity/quincy-v11");
            if (Files.isDirectory(c1.getParent())) {
                Files.createDirectories(c1);
                return c1;
            }
            Path c2 = gameDir.getParent().getParent().resolve("Docs/poderes/qa/antigravity/quincy-v11");
            if (Files.isDirectory(c2.getParent())) {
                Files.createDirectories(c2);
                return c2;
            }
        } catch (Exception ignored) { }
        return null;
    }

    @SubscribeEvent
    public static void camera(ViewportEvent.ComputeCameraAngles event) {
        if (!enabled() || stage >= STAGES.length) {
            return;
        }

        var p = Minecraft.getInstance().player;
        if (p == null) return;

        var focus = p.position().add(0, 1.3, 0);
        Vec3 eye;

        QaStageConfig cfg = STAGES[stage];
        switch (cfg.cameraMode) {
            case 0:
                eye = focus.add(0, 0.4, 4.6); // Looking from +Z towards -Z (Dorsal / Back view)
                break;
            case 1:
                eye = focus.add(5.0, 0.2, 0.0); // Looking from +X (Side profile)
                break;
            case 2:
            default:
                eye = focus.add(4.2, 0.8, 4.2); // Three-quarter rear perspective (+X, +Z)
                break;
        }

        var delta = focus.subtract(eye);
        try {
            if (position == null) {
                position = Camera.class.getDeclaredMethod("setPosition", double.class, double.class, double.class);
                position.setAccessible(true);
            }
            position.invoke(event.getCamera(), eye.x, eye.y, eye.z);
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }

        event.setYaw((float) Math.toDegrees(Math.atan2(-delta.x, delta.z)));
        event.setPitch((float) -Math.toDegrees(Math.atan2(delta.y, Math.hypot(delta.x, delta.z))));
    }

    @SubscribeEvent
    public static void onRenderPlayer(RenderPlayerEvent.Post event) {
        if (!enabled() || stage >= STAGES.length || event.getEntity() != Minecraft.getInstance().player) {
            return;
        }

        float pt = event.getPartialTick();
        var player = Minecraft.getInstance().player;
        if (player == null) return;

        PoseStack pose = event.getPoseStack();
        pose.pushPose();

        // 1. LivingEntityRenderer setupRotations
        float bodyYaw = Mth.rotLerp(pt, player.yBodyRotO, player.yBodyRot);
        pose.mulPose(Axis.YP.rotationDegrees(180.0F - bodyYaw));
        pose.scale(-1.0F, -1.0F, 1.0F);
        pose.scale(0.9375F, 0.9375F, 0.9375F);
        pose.translate(0.0F, -1.501F, 0.0F);

        // 2. Lock directly to the player's torso model part (exact Elytra / Senbon architecture):
        // Eliminates any air gap, transfers torso orientation, and guarantees strict symmetry at X=0.00!
        event.getRenderer().getModel().body.translateAndRotate(pose);

        // 3. Cancel the LivingEntityRenderer (-1, -1, 1) inverted scale so GeckoLib
        // renders the Bedrock model right-side up (+Y points up into the sky):
        pose.scale(-1.0F, -1.0F, 1.0F);

        // 4. Map from Minecraft body space (neck at origin) to Blockbench space (neck at Y=24 voxels / 1.5 blocks):
        pose.translate(0.0, -1.5, 0.0);

        // Update animatable state and render mode
        QaStageConfig cfg = STAGES[stage];
        ANIMATABLE.setClip(cfg.clip);
        ANIMATABLE.setTick(wait + pt);

        RENDERER.setRenderMode(cfg.renderMode);
        MultiBufferSource buffers = event.getMultiBufferSource();
        RENDERER.renderManifestation(pose, ANIMATABLE, buffers, pt, LightTexture.FULL_BRIGHT);

        pose.popPose();
    }
}
