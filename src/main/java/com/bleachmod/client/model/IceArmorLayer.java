package com.bleachmod.client.model;

import com.bleachmod.Reference;
import com.bleachmod.common.data.*;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.*;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;

/**
 * Daiguren Hyorinmaru's ice manifestation on the player:
 * Crystal ice wings, segmented ice tail, arm/leg plates and three floating ice lotus flowers in Bankai.
 */
public final class IceArmorLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
    public static final ModelLayerLocation LAYER = new ModelLayerLocation(Reference.id("ice_armor"), "main");
    private static final ResourceLocation TEXTURE = Reference.id("textures/entity/ice_hydra.png");

    private final ModelPart root;

    public IceArmorLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> parent, ModelPart root) {
        super(parent);
        this.root = root;
    }

    public static LayerDefinition createLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // Body crystal crest
        root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 32)
                .addBox(-4.6F, 1, -2.7F, 3, 5, 1.2F)
                .addBox(1.5F, 7, -2.8F, 3, 4, 1.3F)
                .addBox(-4, 0, -2.8F, 8, 1.2F, 5.6F), PartPose.ZERO);

        // Arm crystal gauntlet
        root.addOrReplaceChild("arm", CubeListBuilder.create().texOffs(0, 64)
                .addBox(-2.6F, -2.5F, -2.6F, 5.2F, 4, 5.2F)
                .addBox(-2.5F, 5, -2.5F, 5, 3, 5), PartPose.ZERO);

        // Leg greaves
        root.addOrReplaceChild("leg", CubeListBuilder.create().texOffs(0, 84)
                .addBox(-2.4F, 5, -2.7F, 4.8F, 6, 1.2F), PartPose.ZERO);

        // Majestically curved, pointed crystal wings of Daiguren
        root.addOrReplaceChild("wing", CubeListBuilder.create().texOffs(32, 0)
                .addBox(2, -8, 3.5F, 22, 3, 1.2F)
                .addBox(5, -6, 3.5F, 18, 16, 1.0F)
                .addBox(21, -10, 3, 4, 24, 1.8F)
                .addBox(14, 8, 3, 3, 8, 1.4F)
                .addBox(8, 7, 3, 3, 7, 1.4F), PartPose.ZERO);

        // Segmented ice tail
        root.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(64, 40)
                .addBox(-1.8F, 9, 3, 3.6F, 6, 4.5F)
                .addBox(-1.2F, 14, 6.5F, 2.4F, 6, 4.5F)
                .addBox(-0.8F, 19, 10, 1.6F, 6, 3.5F)
                .addBox(-0.4F, 24, 13, 0.8F, 4, 2.5F), PartPose.ZERO);

        // 4-petal floating crystal ice flower
        root.addOrReplaceChild("flower", CubeListBuilder.create().texOffs(16, 0)
                .addBox(-1, -4, 0, 2, 3, 0.8F)
                .addBox(-1, 1, 0, 2, 3, 0.8F)
                .addBox(-4, -1, 0, 3, 2, 0.8F)
                .addBox(1, -1, 0, 3, 2, 0.8F)
                .addBox(-1, -1, -0.4F, 2, 2, 1.6F), PartPose.ZERO);

        return LayerDefinition.create(mesh, 128, 128);
    }

    @Override
    public void render(PoseStack pose, MultiBufferSource buffers, int light, AbstractClientPlayer player,
                       float limbSwing, float limbAmount, float partialTick, float age, float yaw, float pitch) {
        if (player.isInvisible() || player.isSpectator()) return;

        PlayerCapability.get(player).ifPresent(d -> {
            if (!CharacterData.HYORINMARU.equals(d.getCharacter().getZanpakutoIdentity())) return;
            String form = d.getCharacter().getActiveForm();
            int stage = "bankai".equals(form) ? 3 : "shikai".equals(form) ? 2 : d.getCharacter().isIceArmorVisual() ? 1 : 0;
            if (stage == 0) return;

            draw(pose, buffers, light, getParentModel().body, root.getChild("body"), 1);
            draw(pose, buffers, light, getParentModel().leftArm, root.getChild("arm"), 1);

            if (stage >= 2) {
                draw(pose, buffers, light, getParentModel().rightArm, root.getChild("arm"), 1);
                draw(pose, buffers, light, getParentModel().leftLeg, root.getChild("leg"), 1);
                draw(pose, buffers, light, getParentModel().rightLeg, root.getChild("leg"), 1);
            }

            if (stage == 3) {
                // Tail
                draw(pose, buffers, light, getParentModel().body, root.getChild("tail"), 1);

                // Wings with dynamic growth and subtle sway
                ModelPart wing = root.getChild("wing");
                float growth = net.minecraft.util.Mth.lerp(ZanpakutoReleaseAnimation.iceGrowth(player, partialTick), -1.4F, -0.15F);
                wing.zRot = growth + (float) Math.sin(age * 0.08) * 0.06F;
                wing.yRot = (float) Math.cos(age * 0.08) * 0.04F;

                draw(pose, buffers, light, getParentModel().body, wing, 1);
                draw(pose, buffers, light, getParentModel().body, wing, -1);

                // Three Floating Ice Lotus Flowers in an arch behind the player's head/shoulders
                ModelPart flower = root.getChild("flower");
                float rot1 = age * 0.04F;
                float rot2 = -age * 0.035F;

                // Center Flower (Top)
                drawFloating(pose, buffers, light, getParentModel().body, flower, 0, -11.0F, 5.0F, rot1, 1.0F);
                // Left Flower
                drawFloating(pose, buffers, light, getParentModel().body, flower, -7.5F, -7.0F, 5.5F, rot2, 0.85F);
                // Right Flower
                drawFloating(pose, buffers, light, getParentModel().body, flower, 7.5F, -7.0F, 5.5F, rot1 + 1.2F, 0.85F);
            }
        });
    }

    private static void draw(PoseStack pose, MultiBufferSource buffers, int light, ModelPart anchor, ModelPart part, float mirror) {
        pose.pushPose();
        anchor.translateAndRotate(pose);
        pose.scale(mirror, 1, 1);
        part.render(pose, buffers.getBuffer(RenderType.entityTranslucent(TEXTURE)), light, OverlayTexture.NO_OVERLAY, 0.8F, 0.95F, 1.0F, 0.9F);
        pose.popPose();
    }

    private static void drawFloating(PoseStack pose, MultiBufferSource buffers, int light, ModelPart anchor, ModelPart flower,
                                     float offsetX, float offsetY, float offsetZ, float rotationZ, float scale) {
        pose.pushPose();
        anchor.translateAndRotate(pose);
        // Translate in pixel units (divide by 16 for Minecraft block scale)
        pose.translate(offsetX / 16.0F, offsetY / 16.0F, offsetZ / 16.0F);
        pose.mulPose(com.mojang.math.Axis.ZP.rotation(rotationZ));
        pose.scale(scale, scale, scale);
        flower.render(pose, buffers.getBuffer(RenderType.entityTranslucent(TEXTURE)), light, OverlayTexture.NO_OVERLAY, 0.85F, 0.95F, 1.0F, 0.92F);
        pose.popPose();
    }
}
