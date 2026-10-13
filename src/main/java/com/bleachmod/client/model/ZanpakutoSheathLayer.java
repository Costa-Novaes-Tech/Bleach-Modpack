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
        // Overlapping, tapered segments follow a shallow katana curve instead of a single beam.
        var scabbard = root.addOrReplaceChild("scabbard", CubeListBuilder.create().texOffs(0,0)
                .addBox(-.65F,-.5F,0,1.3F,1,6), PartPose.ZERO);
        var middle = scabbard.addOrReplaceChild("middle", CubeListBuilder.create().texOffs(0,0)
                .addBox(-.58F,-.48F,0,1.16F,.96F,5.2F), PartPose.offsetAndRotation(0,0,5.9F,.05236F,0,0));
        middle.addOrReplaceChild("tip", CubeListBuilder.create().texOffs(0,0)
                .addBox(-.5F,-.43F,0,1,.86F,4.3F), PartPose.offsetAndRotation(0,0,5.1F,.06981F,0,0));
        var fittings = root.addOrReplaceChild("fittings", CubeListBuilder.create().texOffs(0,0)
                .addBox(-.73F,-.57F,0,1.46F,1.14F,.55F), PartPose.ZERO);
        fittings.addOrReplaceChild("tip_cap", CubeListBuilder.create().texOffs(0,0)
                .addBox(-.54F,-.47F,0,1.08F,.94F,.45F), PartPose.offsetAndRotation(0,-.77F,15.06F,.12217F,0,0));
        root.addOrReplaceChild("grip", CubeListBuilder.create().texOffs(0,0).addBox(-.48F,-.48F,-4.6F,.96F,.96F,4.6F), PartPose.ZERO);
        root.addOrReplaceChild("wrap", CubeListBuilder.create().texOffs(0,0).addBox(-.53F,-.53F,-3.9F,1.06F,1.06F,.4F)
                .addBox(-.53F,-.53F,-2.7F,1.06F,1.06F,.4F).addBox(-.53F,-.53F,-1.5F,1.06F,1.06F,.4F), PartPose.ZERO);
        root.addOrReplaceChild("round_guard", CubeListBuilder.create().texOffs(0,0).addBox(-1.25F,-1.25F,-.35F,2.5F,2.5F,.35F), PartPose.ZERO);
        root.addOrReplaceChild("cross_guard", CubeListBuilder.create().texOffs(0,0).addBox(-1.4F,-.35F,-.35F,2.8F,.7F,.35F)
                .addBox(-.35F,-1.4F,-.35F,.7F,2.8F,.35F), PartPose.ZERO);
        return LayerDefinition.create(mesh,16,16);
    }
    @Override public void render(PoseStack pose, MultiBufferSource buffers, int light, AbstractClientPlayer player,
                                 float swing, float amount, float partial, float age, float yaw, float pitch) {
        if (player.isInvisible() || player.isSpectator() || !player.isAlive()) return;
        var motion = ZanpakutoMotion.motion(player); if (motion.previous == 0 || motion.previous < 0) return;
        PlayerCapability.get(player).ifPresent(data -> {
            String id = data.getCharacter().getZanpakutoIdentity(); boolean ice = CharacterData.HYORINMARU.equals(id), fire = CharacterData.RYUJIN.equals(id);
            pose.pushPose(); getParentModel().body.translateAndRotate(pose);
            // Player-model +X is the character's left: the previous negative sign put it on the right.
            boolean left = player.getMainArm() == net.minecraft.world.entity.HumanoidArm.RIGHT;
            pose.translate(left ? .32 : -.32, .68, -.04);
            pose.mulPose(Axis.YP.rotationDegrees(left ? 25 : -25)); pose.mulPose(Axis.XP.rotationDegrees(-26));
            // Increase the approved curved profile by 15%; negative pitch lowers the positive-Z tip.
            pose.scale(.9775F,.9775F,.9775F);
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
