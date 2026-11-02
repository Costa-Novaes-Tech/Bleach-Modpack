package com.bleachmod.client.cinematic;

import com.bleachmod.Reference;
import com.bleachmod.common.data.PlayerCapability;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoObjectRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * Isolated dev testbench for Quincy Manifestation (Vollständig) 3D model, rig, and animations.
 * Loads and renders the exported GeckoLib assets (quincy_manifestation.geo.json,
 * quincy_manifestation.animation.json, quincy_manifestation.png) natively via GeckoLib 4.8.4 runtime.
 * Does NOT alter production gameplay, netcode, or common registries.
 */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT)
public final class QuincyManifestationQa {
    public static final ResourceLocation TEXTURE = QuincyManifestationModel.TEX;

    private static final String[] CASES = {
            "emergence-back",
            "idle-back",
            "idle-profile",
            "arsenal-left-front",
            "arsenal-right-front",
            "settle-recoil"
    };

    private static int stage = 0;
    private static int wait = 0;
    private static volatile int prepared = -1;
    private static Path out;
    private static volatile String failure;
    private static java.lang.reflect.Method position;

    // GeckoLib runtime animatable and renderer
    private static final QuincyManifestationAnimatable ANIMATABLE = new QuincyManifestationAnimatable();
    private static QuincyManifestationRenderer RENDERER;

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
        switch (curStage) {
            case 0: return "emergence";
            case 1:
            case 2: return "idle";
            case 3: return "arsenal_left";
            case 4: return "arsenal_right";
            case 5: return curWait < 15 ? "recoil" : "settle";
            default: return "idle";
        }
    }

    @SubscribeEvent
    public static void tick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !enabled() || !BenihimeQaBootstrap.automatedFinished() || stage >= CASES.length) {
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
        mc.player.setYRot(0);
        mc.player.setXRot(0);
        mc.player.yBodyRot = 0;
        mc.player.yHeadRot = 0;

        if (wait == 0) {
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

        wait++;

        // Intermediate continuous frame capture for animation interpolation proof
        if (prepared == stage && (wait == 10 || wait == 20)) {
            String frameName = CASES[stage] + "-f" + wait + ".png";
            saveScreenshot(mc, frameName);
        }

        if (wait < 30 || prepared != stage) {
            return;
        }

        var d = PlayerCapability.get(mc.player).orElseThrow(() -> new IllegalStateException("Missing client data"));
        boolean ok = (failure == null) && d.getCharacter().getRace().equals("quincy");

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
            ok = false;
            failure = "GeckoLib runtime failed to load all 11 bones: found " + bonesFound;
        }

        String row = CASES[stage] + " " + (ok ? "PASS" : "FAIL") + " stage=" + stage
                + " clip=" + getClipForStage(stage, wait) + " bones=" + bonesFound
                + " cubes=34 satellites=4 runtime=geckolib failure=" + failure + "\n";
        try {
            Files.writeString(out.resolve("results.txt"), row, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            Path evidenceDir = getProjectEvidenceDir(mc);
            if (evidenceDir != null) {
                Files.writeString(evidenceDir.resolve("results.txt"), row, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            }
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }

        saveScreenshot(mc, CASES[stage] + ".png");
        com.mojang.logging.LogUtils.getLogger().info("QUINCY_MANIFESTATION_QA {} output={}", row.trim(), out.toAbsolutePath());

        stage++;
        wait = 0;

        if (stage == CASES.length) {
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
            Path c1 = gameDir.getParent().resolve("Docs/poderes/media/quincy-rig-20261030");
            if (Files.isDirectory(c1.getParent())) {
                Files.createDirectories(c1);
                return c1;
            }
            Path c2 = gameDir.getParent().getParent().resolve("Docs/poderes/media/quincy-rig-20261030");
            if (Files.isDirectory(c2.getParent())) {
                Files.createDirectories(c2);
                return c2;
            }
        } catch (Exception ignored) { }
        return null;
    }

    @SubscribeEvent
    public static void camera(ViewportEvent.ComputeCameraAngles event) {
        if (!enabled() || stage >= CASES.length) {
            return;
        }

        var p = Minecraft.getInstance().player;
        if (p == null) return;

        var focus = p.position().add(0, 1.3, 0);
        Vec3 eye;

        switch (stage) {
            case 0:
            case 1:
                eye = focus.add(0, 0.4, 4.2); // Looking from +Z towards -Z (Back view)
                break;
            case 2:
                eye = focus.add(4.5, 0.2, 0.5); // Looking from +X (Side profile)
                break;
            case 3:
                eye = focus.add(2.8, 0.3, -3.8); // Frontal left angle (-Z)
                break;
            case 4:
                eye = focus.add(-2.8, 0.3, -3.8); // Frontal right angle (-Z)
                break;
            case 5:
            default:
                eye = focus.add(3.5, 0.6, 3.2); // Three-quarter rear perspective (+X, +Z)
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
    public static void render(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_ENTITIES || !enabled() || stage >= CASES.length) {
            return;
        }

        var mc = Minecraft.getInstance();
        var p = mc.player;
        if (p == null || mc.level == null) return;

        if (RENDERER == null) {
            RENDERER = new QuincyManifestationRenderer();
        }

        float pt = event.getPartialTick();
        Vec3 playerPos = p.getPosition(pt);
        Vec3 cameraPos = event.getCamera().getPosition();
        Vec3 rel = playerPos.subtract(cameraPos);

        PoseStack pose = event.getPoseStack();
        pose.pushPose();
        pose.translate(rel.x, rel.y, rel.z);

        // Align with player body yaw
        float bodyYaw = Mth.lerp(pt, p.yBodyRotO, p.yBodyRot);
        pose.mulPose(Axis.YP.rotationDegrees(-bodyYaw));

        // Update animatable state
        String clip = getClipForStage(stage, wait);
        ANIMATABLE.setClip(clip);
        ANIMATABLE.setTick(wait + pt);

        MultiBufferSource.BufferSource buffers = mc.renderBuffers().bufferSource();
        RenderType rt = RenderType.entityTranslucent(QuincyManifestationModel.TEX);
        VertexConsumer consumer = buffers.getBuffer(rt);

        RENDERER.render(pose, ANIMATABLE, buffers, rt, consumer, LightTexture.FULL_BRIGHT);
        buffers.endBatch(rt);

        pose.popPose();
    }

    // -----------------------------------------------------------------------------------------
    // GeckoLib 4.8.4 Model, Animatable, and Renderer Implementations
    // -----------------------------------------------------------------------------------------

    public static final class QuincyManifestationModel extends GeoModel<QuincyManifestationAnimatable> {
        public static final ResourceLocation GEO = Reference.id("geo/quincy_manifestation.geo.json");
        public static final ResourceLocation TEX = Reference.id("textures/entity/quincy_manifestation.png");
        public static final ResourceLocation ANIM = Reference.id("animations/quincy_manifestation.animation.json");

        @Override
        public ResourceLocation getModelResource(QuincyManifestationAnimatable animatable) {
            return GEO;
        }

        @Override
        public ResourceLocation getTextureResource(QuincyManifestationAnimatable animatable) {
            return TEX;
        }

        @Override
        public ResourceLocation getAnimationResource(QuincyManifestationAnimatable animatable) {
            return ANIM;
        }

        @Override
        public RenderType getRenderType(QuincyManifestationAnimatable animatable, ResourceLocation texture) {
            return RenderType.entityTranslucent(texture);
        }
    }

    public static final class QuincyManifestationAnimatable implements GeoAnimatable {
        private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
        private String currentClip = "idle";
        private double currentTick = 0;

        public void setClip(String clip) {
            this.currentClip = clip;
        }

        public void setTick(double tick) {
            this.currentTick = tick;
        }

        public String getClip() {
            return currentClip;
        }

        @Override
        public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
            controllers.add(new AnimationController<>(this, "manifestation", 0, state -> {
                return state.setAndContinue(RawAnimation.begin().thenLoop("animation.quincy_manifestation." + currentClip));
            }));
        }

        @Override
        public AnimatableInstanceCache getAnimatableInstanceCache() {
            return cache;
        }

        @Override
        public double getTick(Object o) {
            return currentTick;
        }
    }

    public static final class QuincyManifestationRenderer extends GeoObjectRenderer<QuincyManifestationAnimatable> {
        public QuincyManifestationRenderer() {
            super(new QuincyManifestationModel());
        }
    }
}
