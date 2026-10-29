package com.bleachmod.client.quincy;

import com.bleachmod.Reference;
import com.bleachmod.common.quincy.QuincyArrowEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

/**
 * Dedicated renderer for Quincy Reishi spiritual arrows.
 * Replaces vanilla wooden/feathered arrow with a multi-planar fullbright Reishi beam:
 * brilliant white core, glowing cyan halo, sleek tapered tail, and emissive lighting.
 */
public class QuincyArrowRenderer extends ArrowRenderer<QuincyArrowEntity> {
    public static final ResourceLocation TEXTURE = Reference.id("textures/entity/projectiles/quincy_arrow.png");

    public QuincyArrowRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(QuincyArrowEntity entity) {
        return TEXTURE;
    }

    @Override
    public void render(QuincyArrowEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();

        float yRot = Mth.lerp(partialTick, entity.yRotO, entity.getYRot());
        float xRot = Mth.lerp(partialTick, entity.xRotO, entity.getXRot());

        // Align local coordinates along projectile flight vector
        poseStack.mulPose(Axis.YP.rotationDegrees(yRot - 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(xRot));

        // Spiritual arrow is fully emissive (FULL_BRIGHT) day and night
        int fullBright = LightTexture.FULL_BRIGHT;
        VertexConsumer consumer = buffer.getBuffer(RenderType.entityTranslucent(getTextureLocation(entity)));

        // Multi-planar crossed quads (4 planes at 0, 45, 90, 135 deg) for 3D visibility from all camera angles
        for (int p = 0; p < 4; p++) {
            poseStack.pushPose();
            poseStack.mulPose(Axis.XP.rotationDegrees(p * 45.0F));
            PoseStack.Pose cur = poseStack.last();
            Matrix4f curPose = cur.pose();
            Matrix3f curNormal = cur.normal();

            // 1. Outer luminous cyan halo (wider translucent aura)
            renderQuad(consumer, curPose, curNormal,
                    -0.45F, 0.45F, -0.075F, 0.075F, 0.0F,
                    100, 220, 255, 190, fullBright);

            // 2. Inner brilliant white core (dense Reishi beam)
            renderQuad(consumer, curPose, curNormal,
                    -0.42F, 0.48F, -0.03F, 0.03F, 0.0F,
                    255, 255, 255, 255, fullBright);

            // 3. Short trailing wake (rastro curto estreitando para tras: -0.45F a -0.75F)
            renderTaperQuad(consumer, curPose, curNormal,
                    -0.75F, -0.45F, 0.0F, 0.06F, 0.0F,
                    90, 210, 255, 140, 0, fullBright);

            poseStack.popPose();
        }

        poseStack.popPose();
        // Do NOT call super.render() to prevent rendering the vanilla wooden/feathered arrow
    }

    private static void renderQuad(VertexConsumer consumer, Matrix4f pose, Matrix3f normal,
                                  float minX, float maxX, float minY, float maxY, float z,
                                  int r, int g, int b, int a, int light) {
        // Front face
        vertex(consumer, pose, normal, minX, minY, z, 0.0F, 0.0F, 0, 0, 1, r, g, b, a, light);
        vertex(consumer, pose, normal, maxX, minY, z, 1.0F, 0.0F, 0, 0, 1, r, g, b, a, light);
        vertex(consumer, pose, normal, maxX, maxY, z, 1.0F, 1.0F, 0, 0, 1, r, g, b, a, light);
        vertex(consumer, pose, normal, minX, maxY, z, 0.0F, 1.0F, 0, 0, 1, r, g, b, a, light);

        // Back face (two-sided visibility)
        vertex(consumer, pose, normal, minX, maxY, z, 0.0F, 1.0F, 0, 0, -1, r, g, b, a, light);
        vertex(consumer, pose, normal, maxX, maxY, z, 1.0F, 1.0F, 0, 0, -1, r, g, b, a, light);
        vertex(consumer, pose, normal, maxX, minY, z, 1.0F, 0.0F, 0, 0, -1, r, g, b, a, light);
        vertex(consumer, pose, normal, minX, minY, z, 0.0F, 0.0F, 0, 0, -1, r, g, b, a, light);
    }

    private static void renderTaperQuad(VertexConsumer consumer, Matrix4f pose, Matrix3f normal,
                                       float tailX, float headX, float tailHalfW, float headHalfW, float z,
                                       int r, int g, int b, int headA, int tailA, int light) {
        // Tapered wake behind the arrow
        vertex(consumer, pose, normal, tailX, -tailHalfW, z, 0.0F, 0.0F, 0, 0, 1, r, g, b, tailA, light);
        vertex(consumer, pose, normal, headX, -headHalfW, z, 1.0F, 0.0F, 0, 0, 1, r, g, b, headA, light);
        vertex(consumer, pose, normal, headX,  headHalfW, z, 1.0F, 1.0F, 0, 0, 1, r, g, b, headA, light);
        vertex(consumer, pose, normal, tailX,  tailHalfW, z, 0.0F, 1.0F, 0, 0, 1, r, g, b, tailA, light);

        vertex(consumer, pose, normal, tailX,  tailHalfW, z, 0.0F, 1.0F, 0, 0, -1, r, g, b, tailA, light);
        vertex(consumer, pose, normal, headX,  headHalfW, z, 1.0F, 1.0F, 0, 0, -1, r, g, b, headA, light);
        vertex(consumer, pose, normal, headX, -headHalfW, z, 1.0F, 0.0F, 0, 0, -1, r, g, b, headA, light);
        vertex(consumer, pose, normal, tailX, -tailHalfW, z, 0.0F, 0.0F, 0, 0, -1, r, g, b, tailA, light);
    }

    private static void vertex(VertexConsumer consumer, Matrix4f pose, Matrix3f normal,
                               float x, float y, float z, float u, float v,
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
