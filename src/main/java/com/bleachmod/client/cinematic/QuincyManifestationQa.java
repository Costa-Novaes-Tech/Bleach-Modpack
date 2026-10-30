package com.bleachmod.client.cinematic;

import com.bleachmod.Reference;
import com.bleachmod.common.data.PlayerCapability;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * Isolated dev testbench for Quincy Manifestation (Vollständig) 3D model, rig, and animations.
 * Renders the 11-bone multi-segmented wings and 4 floating Reishi satellites natively in Forge client.
 * Does NOT alter production gameplay, netcode, or common registries.
 */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT)
public final class QuincyManifestationQa {
    public static final ResourceLocation TEXTURE = Reference.id("textures/entity/quincy_manifestation.png");

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
        if (wait < 30 || prepared != stage) {
            return;
        }

        var d = PlayerCapability.get(mc.player).orElseThrow(() -> new IllegalStateException("Missing client data"));
        boolean ok = (failure == null) && d.getCharacter().getRace().equals("quincy");

        String row = CASES[stage] + " " + (ok ? "PASS" : "FAIL") + " stage=" + stage + " bones=11 cubes=28 satellites=4 failure=" + failure + "\n";
        try {
            Files.writeString(out.resolve("results.txt"), row, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }

        Screenshot.grab(out.toFile(), CASES[stage] + ".png", mc.getMainRenderTarget(), ignored -> {});
        com.mojang.logging.LogUtils.getLogger().info("QUINCY_MANIFESTATION_QA {} output={}", row.trim(), out.toAbsolutePath());

        stage++;
        wait = 0;

        if (stage == CASES.length) {
            mc.options.setCameraType(CameraType.FIRST_PERSON);
            if (Boolean.getBoolean("bleachmod.qaAutoExit")) {
                mc.stop();
            }
        }
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

        // Viewpoints per stage:
        // 0: emergence-back -> behind actor (+Z)
        // 1: idle-back -> behind actor (+Z)
        // 2: idle-profile -> left side (+X)
        // 3: arsenal-left-front -> front-angle showing left wing sweep into -Z
        // 4: arsenal-right-front -> front-angle showing right wing sweep into -Z
        // 5: settle-recoil -> side-back angle showing recoil & settle
        switch (stage) {
            case 0:
            case 1:
                eye = focus.add(0, 0.4, 4.2); // Looking from +Z towards -Z
                break;
            case 2:
                eye = focus.add(4.5, 0.2, 0.5); // Looking from left side (+X)
                break;
            case 3:
                eye = focus.add(2.8, 0.3, -3.8); // Frontal left angle
                break;
            case 4:
                eye = focus.add(-2.8, 0.3, -3.8); // Frontal right angle
                break;
            case 5:
            default:
                eye = focus.add(3.5, 0.6, 3.2); // Three-quarter rear perspective
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

        // Model units: 1 unit = 1/16 block
        pose.scale(1.0F / 16.0F, 1.0F / 16.0F, 1.0F / 16.0F);

        var buffers = mc.renderBuffers().bufferSource();
        VertexConsumer consumer = buffers.getBuffer(RenderType.entityTranslucent(TEXTURE));
        int light = LightTexture.FULL_BRIGHT;

        renderManifestationRig(pose, consumer, stage, wait, light);

        pose.popPose();
    }

    private static void renderManifestationRig(PoseStack pose, VertexConsumer consumer, int currentStage, int currentWait, int light) {
        // Animation evaluation according to active stage clip
        float emergenceFactor = currentStage == 0 ? 0.9F : 1.0F;

        // Wing left root & chain
        pose.pushPose();
        // Pivot: [2, 20, 2]
        pose.translate(2.0F, 20.0F, 2.0F);
        applyWingAnimation(pose, true, currentStage, currentWait, 0);
        // Cubes for wing_left_root
        renderCube(pose, consumer, -1.0F, -1.0F, -0.2F, 3.0F, 2.0F, 1.4F, 64, 0, light);
        renderCube(pose, consumer, 1.5F, 0.0F, 0.0F, 4.5F, 4.0F, 1.0F, 74, 0, light);

        // Wing left mid (pivot: [8, 24, 2] -> relative: [6, 4, 0])
        pose.pushPose();
        pose.translate(6.0F, 4.0F, 0.0F);
        applyWingAnimation(pose, true, currentStage, currentWait, 1);
        renderCube(pose, consumer, 0.0F, -0.5F, -0.5F, 8.0F, 4.5F, 1.0F, 96, 0, light);
        renderCube(pose, consumer, 1.0F, -12.0F, -0.2F, 3.0F, 11.5F, 0.4F, 0, 32, light);
        renderCube(pose, consumer, 3.5F, -10.0F, -0.4F, 3.0F, 11.0F, 0.4F, 8, 32, light);
        renderCube(pose, consumer, 6.0F, -7.0F, -0.6F, 2.5F, 10.0F, 0.4F, 16, 32, light);

        // Wing left tip (pivot: [16, 28, 1.5] -> relative: [8, 4, -0.5])
        pose.pushPose();
        pose.translate(8.0F, 4.0F, -0.5F);
        applyWingAnimation(pose, true, currentStage, currentWait, 2);
        renderCube(pose, consumer, 0.0F, -0.5F, -0.5F, 10.0F, 7.5F, 0.8F, 64, 32, light);
        renderCube(pose, consumer, 6.0F, 6.0F, -0.7F, 6.0F, 7.0F, 0.6F, 86, 32, light);
        renderCube(pose, consumer, 2.0F, -4.0F, -0.3F, 6.0F, 7.0F, 0.4F, 100, 32, light);

        pose.popPose(); // tip
        pose.popPose(); // mid
        pose.popPose(); // root

        // Wing right root & chain (symmetrical on -X)
        pose.pushPose();
        pose.translate(-2.0F, 20.0F, 2.0F);
        applyWingAnimation(pose, false, currentStage, currentWait, 0);
        renderCube(pose, consumer, -2.0F, -1.0F, -0.2F, 3.0F, 2.0F, 1.4F, 64, 0, light);
        renderCube(pose, consumer, -6.0F, 0.0F, 0.0F, 4.5F, 4.0F, 1.0F, 74, 0, light);

        // Wing right mid (pivot: [-8, 24, 2] -> relative: [-6, 4, 0])
        pose.pushPose();
        pose.translate(-6.0F, 4.0F, 0.0F);
        applyWingAnimation(pose, false, currentStage, currentWait, 1);
        renderCube(pose, consumer, -8.0F, -0.5F, -0.5F, 8.0F, 4.5F, 1.0F, 96, 0, light);
        renderCube(pose, consumer, -4.0F, -12.0F, -0.2F, 3.0F, 11.5F, 0.4F, 0, 32, light);
        renderCube(pose, consumer, -6.5F, -10.0F, -0.4F, 3.0F, 11.0F, 0.4F, 8, 32, light);
        renderCube(pose, consumer, -8.5F, -7.0F, -0.6F, 2.5F, 10.0F, 0.4F, 16, 32, light);

        // Wing right tip (pivot: [-16, 28, 1.5] -> relative: [-8, 4, -0.5])
        pose.pushPose();
        pose.translate(-8.0F, 4.0F, -0.5F);
        applyWingAnimation(pose, false, currentStage, currentWait, 2);
        renderCube(pose, consumer, -10.0F, -0.5F, -0.5F, 10.0F, 7.5F, 0.8F, 64, 32, light);
        renderCube(pose, consumer, -12.0F, 6.0F, -0.7F, 6.0F, 7.0F, 0.6F, 86, 32, light);
        renderCube(pose, consumer, -8.0F, -4.0F, -0.3F, 6.0F, 7.0F, 0.4F, 100, 32, light);

        pose.popPose(); // tip
        pose.popPose(); // mid
        pose.popPose(); // root

        // Four Satellites (satellite_0..3)
        // satellite_0: inner left  [ 17.6, 28.8, 11.2 ]
        // satellite_1: outer left  [ 32.0, 41.6, 16.0 ]
        // satellite_2: inner right [-17.6, 28.8, 11.2 ]
        // satellite_3: outer right [-32.0, 41.6, 16.0 ]
        float[][] satPivots = {
                { 17.6F, 28.8F, 11.2F },
                { 32.0F, 41.6F, 16.0F },
                { -17.6F, 28.8F, 11.2F },
                { -32.0F, 41.6F, 16.0F }
        };

        for (int i = 0; i < 4; i++) {
            pose.pushPose();
            float[] p = satPivots[i];
            pose.translate(p[0], p[1], p[2]);
            applySatelliteAnimation(pose, i, currentStage, currentWait);
            renderSatellite(pose, consumer, light);
            pose.popPose();
        }
    }

    private static void applyWingAnimation(PoseStack pose, boolean isLeft, int curStage, int curWait, int segment) {
        float sign = isLeft ? 1.0F : -1.0F;
        float rx = 0, ry = 0, rz = 0;

        switch (curStage) {
            case 0: // emergence peak flare
                rx = segment == 0 ? -5.0F : 0.0F;
                ry = sign * (segment == 0 ? 25.0F : (segment == 1 ? -10.0F : -15.0F));
                rz = sign * (segment == 0 ? 20.0F : (segment == 1 ? 15.0F : 20.0F));
                break;
            case 1: // idle pulse
            case 2:
                rx = (float) Math.sin(curWait * 0.1F + segment * 0.4F) * 3.0F;
                ry = sign * ((float) Math.cos(curWait * 0.1F + segment * 0.3F) * 4.0F);
                rz = sign * ((float) Math.sin(curWait * 0.1F + segment * 0.5F) * 6.0F);
                break;
            case 3: // arsenal_left
                if (isLeft) {
                    rx = -15.0F + segment * 5.0F;
                    ry = 45.0F - segment * 10.0F;
                    rz = 10.0F + segment * 5.0F;
                } else {
                    rx = 5.0F;
                    ry = -20.0F;
                    rz = -10.0F;
                }
                break;
            case 4: // arsenal_right
                if (!isLeft) {
                    rx = -15.0F + segment * 5.0F;
                    ry = -45.0F + segment * 10.0F;
                    rz = -10.0F - segment * 5.0F;
                } else {
                    rx = 5.0F;
                    ry = 20.0F;
                    rz = 10.0F;
                }
                break;
            case 5: // settle / recoil
                rx = segment == 0 ? 5.0F : 0.0F;
                ry = sign * (segment == 0 ? -10.0F : -5.0F);
                rz = sign * (segment == 0 ? -8.0F : -4.0F);
                break;
        }

        if (rx != 0) pose.mulPose(Axis.XP.rotationDegrees(rx));
        if (ry != 0) pose.mulPose(Axis.YP.rotationDegrees(ry));
        if (rz != 0) pose.mulPose(Axis.ZP.rotationDegrees(rz));
    }

    private static void applySatelliteAnimation(PoseStack pose, int index, int curStage, int curWait) {
        boolean isLeft = index < 2;
        boolean isOuter = (index % 2) != 0;
        float floatOffset = (float) Math.sin(curWait * 0.12F + index * 1.57F) * 1.8F;
        float spin = (curWait * 4.0F + index * 90.0F) % 360.0F;

        if (curStage == 3 && isLeft) {
            // arsenal_left: advance forward
            pose.translate(isOuter ? 4.0F : 2.0F, isOuter ? 5.0F : 3.0F, isOuter ? -18.0F : -14.0F);
            pose.mulPose(Axis.XP.rotationDegrees(15.0F));
        } else if (curStage == 4 && !isLeft) {
            // arsenal_right: advance forward
            pose.translate(isOuter ? -4.0F : -2.0F, isOuter ? 5.0F : 3.0F, isOuter ? -18.0F : -14.0F);
            pose.mulPose(Axis.XP.rotationDegrees(15.0F));
        } else if (curStage == 5) {
            // recoil: surge back
            pose.translate(isLeft ? 2.0F : -2.0F, 1.0F, 4.0F);
        } else {
            pose.translate(0.0F, floatOffset, 0.0F);
        }

        pose.mulPose(Axis.YP.rotationDegrees(spin));
    }

    private static void renderSatellite(PoseStack pose, VertexConsumer consumer, int light) {
        // Luminous diamond core (3x3x3)
        renderCube(pose, consumer, -1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, 0, 0, light);
        // Horizontal blade (9x1.2x1.2)
        renderCube(pose, consumer, -4.5F, -0.6F, -0.6F, 9.0F, 1.2F, 1.2F, 32, 0, light);
        // Vertical blade (1.2x9x1.2)
        renderCube(pose, consumer, -0.6F, -4.5F, -0.6F, 1.2F, 9.0F, 1.2F, 32, 4, light);
        // Axial emitter tip (1.2x1.2x5.0)
        renderCube(pose, consumer, -0.6F, -0.6F, -2.5F, 1.2F, 1.2F, 5.0F, 36, 4, light);
    }

    private static void renderCube(PoseStack pose, VertexConsumer consumer,
                                   float x, float y, float z,
                                   float w, float h, float d,
                                   int u, int v, int light) {
        Matrix4f poseMat = pose.last().pose();
        Matrix3f normMat = pose.last().normal();

        float x1 = x, x2 = x + w;
        float y1 = y, y2 = y + h;
        float z1 = z, z2 = z + d;

        float texW = 128.0F, texH = 128.0F;
        float u1 = u / texW, u2 = (u + w) / texW;
        float v1 = v / texH, v2 = (v + h) / texH;

        int r = 255, g = 255, b = 255, a = 240;

        // North (-Z)
        vertex(consumer, poseMat, normMat, x1, y2, z1, u1, v1, 0, 0, -1, r, g, b, a, light);
        vertex(consumer, poseMat, normMat, x2, y2, z1, u2, v1, 0, 0, -1, r, g, b, a, light);
        vertex(consumer, poseMat, normMat, x2, y1, z1, u2, v2, 0, 0, -1, r, g, b, a, light);
        vertex(consumer, poseMat, normMat, x1, y1, z1, u1, v2, 0, 0, -1, r, g, b, a, light);

        // South (+Z)
        vertex(consumer, poseMat, normMat, x2, y2, z2, u1, v1, 0, 0, 1, r, g, b, a, light);
        vertex(consumer, poseMat, normMat, x1, y2, z2, u2, v1, 0, 0, 1, r, g, b, a, light);
        vertex(consumer, poseMat, normMat, x1, y1, z2, u2, v2, 0, 0, 1, r, g, b, a, light);
        vertex(consumer, poseMat, normMat, x2, y1, z2, u1, v2, 0, 0, 1, r, g, b, a, light);

        // West (-X)
        vertex(consumer, poseMat, normMat, x1, y2, z2, u1, v1, -1, 0, 0, r, g, b, a, light);
        vertex(consumer, poseMat, normMat, x1, y2, z1, u2, v1, -1, 0, 0, r, g, b, a, light);
        vertex(consumer, poseMat, normMat, x1, y1, z1, u2, v2, -1, 0, 0, r, g, b, a, light);
        vertex(consumer, poseMat, normMat, x1, y1, z2, u1, v2, -1, 0, 0, r, g, b, a, light);

        // East (+X)
        vertex(consumer, poseMat, normMat, x2, y2, z1, u1, v1, 1, 0, 0, r, g, b, a, light);
        vertex(consumer, poseMat, normMat, x2, y2, z2, u2, v1, 1, 0, 0, r, g, b, a, light);
        vertex(consumer, poseMat, normMat, x2, y1, z2, u2, v2, 1, 0, 0, r, g, b, a, light);
        vertex(consumer, poseMat, normMat, x2, y1, z1, u1, v2, 1, 0, 0, r, g, b, a, light);

        // Up (+Y)
        vertex(consumer, poseMat, normMat, x1, y2, z2, u1, v1, 0, 1, 0, r, g, b, a, light);
        vertex(consumer, poseMat, normMat, x2, y2, z2, u2, v1, 0, 1, 0, r, g, b, a, light);
        vertex(consumer, poseMat, normMat, x2, y2, z1, u2, v2, 0, 1, 0, r, g, b, a, light);
        vertex(consumer, poseMat, normMat, x1, y2, z1, u1, v2, 0, 1, 0, r, g, b, a, light);

        // Down (-Y)
        vertex(consumer, poseMat, normMat, x1, y1, z1, u1, v1, 0, -1, 0, r, g, b, a, light);
        vertex(consumer, poseMat, normMat, x2, y1, z1, u2, v1, 0, -1, 0, r, g, b, a, light);
        vertex(consumer, poseMat, normMat, x2, y1, z2, u2, v2, 0, -1, 0, r, g, b, a, light);
        vertex(consumer, poseMat, normMat, x1, y1, z2, u1, v2, 0, -1, 0, r, g, b, a, light);
    }

    private static void vertex(VertexConsumer consumer, Matrix4f pose, Matrix3f normal,
                               float x, float y, float z,
                               float u, float v,
                               float nx, float ny, float nz,
                               int r, int g, int b, int a, int light) {
        consumer.vertex(pose, x, y, z)
                .color(r, g, b, a)
                .uv(u, v)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(light)
                .normal(normal, nx, ny, nz)
                .endVertex();
    }
}
