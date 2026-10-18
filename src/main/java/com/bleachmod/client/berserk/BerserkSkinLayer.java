package com.bleachmod.client.berserk;

import com.bleachmod.Reference;
import com.bleachmod.client.cinematic.CinematicDirector;
import com.bleachmod.common.data.CharacterData;
import com.bleachmod.common.data.PlayerCapability;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

/** Berserk Mode body: crimson skin (clothes untouched) and glowing yellow eyes over the standard face. */
public final class BerserkSkinLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
    private static final ResourceLocation WHITE = new ResourceLocation("minecraft", "textures/misc/white.png");
    private final ModelPart eyes;
    public BerserkSkinLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> parent) {
        super(parent);
        MeshDefinition mesh = new MeshDefinition();
        // Standard skins draw the eyes on face row 4: pixels 1-2 and 5-6 from the left edge of the face.
        mesh.getRoot().addOrReplaceChild("eyes", CubeListBuilder.create().texOffs(0, 0)
                .addBox(-3, -4, -4.06F, 2, 1, 0).addBox(1, -4, -4.06F, 2, 1, 0), PartPose.ZERO);
        eyes = LayerDefinition.create(mesh, 16, 16).bakeRoot().getChild("eyes");
    }
    public static boolean active(AbstractClientPlayer player) {
        return !player.isInvisible() && !player.isSpectator() && PlayerCapability.get(player).map(d ->
                CharacterData.NOZARASHI.equals(d.getCharacter().getZanpakutoIdentity())
                        && Reference.GROUP_ZANPAKUTO.equals(d.getCharacter().getActiveFormGroup())
                        && Reference.FORM_BANKAI.equals(d.getCharacter().getActiveForm())).orElse(false)
                && CinematicDirector.transformationVisible(player);
    }
    @Override public void render(PoseStack pose, MultiBufferSource buffers, int light, AbstractClientPlayer player,
                                 float limbSwing, float limbAmount, float partialTick, float age, float yaw, float pitch) {
        if (!active(player)) return;
        // Same geometry and depth as the base pass, so the crimson copy replaces the original pixels.
        ResourceLocation skin = BerserkSkins.of(player.getSkinTextureLocation());
        getParentModel().renderToBuffer(pose, buffers.getBuffer(RenderType.entityTranslucent(skin)), light,
                LivingEntityRenderer.getOverlayCoords(player, 0), 1, 1, 1, 1);
        // Pulsing emissive eyes; drawn at full brightness regardless of world light.
        float pulse = .8F + .2F * (float) Math.sin(age * .25F);
        pose.pushPose(); getParentModel().head.translateAndRotate(pose);
        eyes.render(pose, buffers.getBuffer(RenderType.eyes(WHITE)), LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, pulse, pulse * .82F, .08F, 1);
        pose.popPose();
    }
}
