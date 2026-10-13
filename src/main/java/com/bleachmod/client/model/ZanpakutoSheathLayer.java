package com.bleachmod.client.model;

import com.bleachmod.Reference;
import com.bleachmod.common.data.*;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.*;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

/** One scabbard at the waist for the bound identity, with an empty mouth while held. */
public final class ZanpakutoSheathLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
    public static final ModelLayerLocation LAYER = new ModelLayerLocation(Reference.id("zanpakuto_sheath"), "main");
    private static final ResourceLocation MATERIAL = new ResourceLocation("minecraft", "textures/block/white_concrete.png");
    private final ModelPart root;
    public ZanpakutoSheathLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> parent, ModelPart root) { super(parent); this.root = root; }
    public static LayerDefinition createLayer() {
        var mesh = new MeshDefinition(); var root = mesh.getRoot();
        root.addOrReplaceChild("scabbard", CubeListBuilder.create().texOffs(0,0).addBox(-1, -1, 0, 2, 2, 23), PartPose.ZERO);
        root.addOrReplaceChild("fittings", CubeListBuilder.create().texOffs(0,0).addBox(-1.15F,-1.15F,0,2.3F,2.3F,1)
                .addBox(-1.1F,-1.1F,21.8F,2.2F,2.2F,1.3F), PartPose.ZERO);
        root.addOrReplaceChild("grip", CubeListBuilder.create().texOffs(0,0).addBox(-.7F,-.7F,-7,1.4F,1.4F,7), PartPose.ZERO);
        root.addOrReplaceChild("wrap", CubeListBuilder.create().texOffs(0,0).addBox(-.8F,-.8F,-6,1.6F,1.6F,.6F)
                .addBox(-.8F,-.8F,-4,1.6F,1.6F,.6F).addBox(-.8F,-.8F,-2,1.6F,1.6F,.6F), PartPose.ZERO);
        root.addOrReplaceChild("round_guard", CubeListBuilder.create().texOffs(0,0).addBox(-1.8F,-1.8F,-.5F,3.6F,3.6F,.5F), PartPose.ZERO);
        root.addOrReplaceChild("cross_guard", CubeListBuilder.create().texOffs(0,0).addBox(-2,-.5F,-.5F,4,1,.5F)
                .addBox(-.5F,-2,-.5F,1,4,.5F), PartPose.ZERO);
        return LayerDefinition.create(mesh,16,16);
    }
    @Override public void render(PoseStack pose, MultiBufferSource buffers, int light, AbstractClientPlayer player,
                                 float swing, float amount, float partial, float age, float yaw, float pitch) {
        if (player.isInvisible() || player.isSpectator() || !player.isAlive()) return;
        var motion = ZanpakutoMotion.motion(player); if (motion.previous == 0 || motion.previous < 0) return;
        PlayerCapability.get(player).ifPresent(data -> {
            String id = data.getCharacter().getZanpakutoIdentity(); boolean ice = CharacterData.HYORINMARU.equals(id), fire = CharacterData.RYUJIN.equals(id);
            pose.pushPose(); getParentModel().body.translateAndRotate(pose);
            boolean left = player.getMainArm() == net.minecraft.world.entity.HumanoidArm.RIGHT;
            pose.translate(left ? -.32 : .32, .68, .04);
            pose.mulPose(Axis.YP.rotationDegrees(left ? -35 : 35)); pose.mulPose(Axis.XP.rotationDegrees(-12));
            draw(pose,buffers,light,"scabbard",ice ? .12F : fire ? .13F : .2F,ice ? .32F : fire ? .08F : .2F,ice ? .38F : fire ? .07F : .22F);
            draw(pose,buffers,light,"fittings",.65F,.53F,.22F);
            if (motion.previous == 1) {
                float p = motion.progress(player.tickCount,partial); pose.pushPose(); pose.translate(0,0,-.18 * (1-p));
                draw(pose,buffers,light,"grip",ice ? .15F : .18F,ice ? .65F : .16F,ice ? .75F : .16F);
                draw(pose,buffers,light,"wrap",ice ? .88F : .38F,ice ? .94F : .3F,ice ? 1 : .26F);
                draw(pose,buffers,light,ice ? "cross_guard" : "round_guard",.65F,.53F,.22F); pose.popPose();
            }
            pose.popPose();
        });
    }
    private void draw(PoseStack pose, MultiBufferSource buffers, int light, String part, float r, float g, float b) {
        root.getChild(part).render(pose,buffers.getBuffer(RenderType.entityCutoutNoCull(MATERIAL)),light,OverlayTexture.NO_OVERLAY,r,g,b,1);
    }
}
