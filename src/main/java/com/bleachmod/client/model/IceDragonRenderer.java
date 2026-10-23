package com.bleachmod.client.model;

import com.bleachmod.Reference;
import com.bleachmod.common.visual.HydraMotion;
import com.bleachmod.entity.IceDragonEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public final class IceDragonRenderer extends GeoEntityRenderer<IceDragonEntity> {
    public IceDragonRenderer(EntityRendererProvider.Context context) { super(context, new Model()); }

    @Override public void render(IceDragonEntity dragon, float yaw, float partial, com.mojang.blaze3d.vertex.PoseStack pose,
                                 net.minecraft.client.renderer.MultiBufferSource buffers, int light) {
        pose.pushPose();
        float scale = dragon.isManifestation() ? 1.0F : 0.42F;
        pose.scale(scale, scale, scale);
        super.render(dragon, yaw, partial, pose, buffers, light);
        pose.popPose();
    }

    private static final class Model extends GeoModel<IceDragonEntity> {
        private static Vec3 serpentPoint(double t, double u) {
            double tailDist = (1.0 - u) * 9.5;
            double wavePhase = t * 0.45 - (1.0 - u) * 3.8;
            double x = Math.sin(wavePhase) * (0.35 + (1.0 - u) * 0.45);
            double y = Math.cos(wavePhase * 0.7) * (0.20 + (1.0 - u) * 0.25);
            double z = -tailDist;
            return new Vec3(x, y, z);
        }

        @Override public void setCustomAnimations(IceDragonEntity d, long instance, software.bernie.geckolib.core.animation.AnimationState<IceDragonEntity> state) {
            super.setCustomAnimations(d, instance, state);
            if (!d.isManifestation()) {
                // Projectile mode: Single-headed serpentine ice dragon trailing behind the head
                for (int h = 1; h < 3; h++) {
                    String side = HydraMotion.NECKS[h];
                    var root = getAnimationProcessor().getBone("root_" + side);
                    if (root != null) root.setHidden(true);
                    var head = getAnimationProcessor().getBone("head_" + side);
                    if (head != null) head.setHidden(true);
                    var jaw = getAnimationProcessor().getBone("jaw_" + side);
                    if (jaw != null) jaw.setHidden(true);
                    for (int link = 1; link <= HydraMotion.LINKS; link++) {
                        var b = getAnimationProcessor().getBone("neck_" + side + "_" + link);
                        if (b != null) b.setHidden(true);
                    }
                }

                var rootCenter = getAnimationProcessor().getBone("root_center");
                if (rootCenter != null) rootCenter.setHidden(false);

                double t = (double) d.tickCount + net.minecraft.client.Minecraft.getInstance().getFrameTime();

                // 12 neck segments undulating behind the head
                for (int link = 0; link < HydraMotion.LINKS; link++) {
                    var bone = getAnimationProcessor().getBone("neck_center_" + (link + 1));
                    if (bone == null) continue;
                    double a = (double) link / HydraMotion.LINKS;
                    double b = (double) (link + 1) / HydraMotion.LINKS;
                    var from = serpentPoint(t, a);
                    var to = serpentPoint(t, b);
                    var delta = to.subtract(from);
                    double length = Math.max(0.001, delta.length());
                    bone.setHidden(false);
                    bone.setPosX((float) (-from.x * 16));
                    bone.setPosY((float) (from.y * 16));
                    bone.setPosZ((float) (-from.z * 16));
                    bone.setRotX((float) Math.asin(-delta.z / length));
                    bone.setRotZ((float) Math.atan2(-delta.x, delta.y));
                    bone.setRotY(0);
                    bone.setScaleY((float) (length * 1.02));
                    float taper = (float) (0.62 + 0.38 * b);
                    bone.setScaleX(taper);
                    bone.setScaleZ(taper);
                }

                var headBone = getAnimationProcessor().getBone("head_center");
                if (headBone != null) {
                    var tip = serpentPoint(t, 1.0);
                    headBone.setHidden(false);
                    headBone.setPosX((float) (-tip.x * 16));
                    headBone.setPosY((float) (tip.y * 16));
                    headBone.setPosZ((float) (-tip.z * 16));
                    headBone.setScaleX(1.0F);
                    headBone.setScaleY(1.0F);
                    headBone.setScaleZ(1.0F);
                    headBone.setRotX((float) (Math.sin(t * 0.35) * 0.08));
                    headBone.setRotY((float) (Math.cos(t * 0.35) * 0.10));
                    headBone.setRotZ(0);
                }

                var jawBone = getAnimationProcessor().getBone("jaw_center");
                if (jawBone != null) {
                    jawBone.setHidden(false);
                    jawBone.setRotX((float) Math.toRadians(25.0 + 5.5 * Math.sin(t * 0.45)));
                }
                return;
            }

            int mode = d.isAssault() ? HydraMotion.MODE_ASSAULT : d.isRoar() ? HydraMotion.MODE_ROAR : HydraMotion.MODE_TRANSFORMATION;
            double age = d.sceneAge(net.minecraft.client.Minecraft.getInstance().getFrameTime());
            if (mode == HydraMotion.MODE_TRANSFORMATION) {
                age = com.bleachmod.common.visual.HyorinmaruTimeline.motionAge(age);
            }

            for (int head = 0; head < 3; head++) {
                String name = HydraMotion.NECKS[head];
                var root = getAnimationProcessor().getBone("root_" + name);
                if (root != null) {
                    root.setHidden(mode == HydraMotion.MODE_ASSAULT && age >= HydraMotion.impactTick(head) + 12);
                }
                double growth = HydraMotion.growth(mode, head, age);
                for (int link = 0; link < HydraMotion.LINKS; link++) {
                    var bone = getAnimationProcessor().getBone("neck_" + name + "_" + (link + 1));
                    if (bone == null) continue;
                    double a = (double) link / HydraMotion.LINKS;
                    double b = (double) (link + 1) / HydraMotion.LINKS;
                    bone.setHidden(a >= growth);
                    if (a >= growth) continue;
                    var from = d.curvePoint(head, age, a);
                    var to = d.curvePoint(head, age, Math.min(b, growth));
                    var delta = to.subtract(from);
                    double length = Math.max(0.001, delta.length());
                    bone.setPosX((float) (-from.x * 16));
                    bone.setPosY((float) (from.y * 16));
                    bone.setPosZ((float) (-from.z * 16));
                    bone.setRotX((float) Math.asin(-delta.z / length));
                    bone.setRotZ((float) Math.atan2(-delta.x, delta.y));
                    bone.setRotY(0);
                    bone.setScaleY((float) (length * 1.02));
                    float neckThick = (mode == HydraMotion.MODE_ROAR) ? 1.35F : 1.0F;
                    bone.setScaleX(neckThick);
                    bone.setScaleZ(neckThick);
                }
                var bone = getAnimationProcessor().getBone("head_" + name);
                if (bone == null) continue;
                var tip = d.curvePoint(head, age, mode == HydraMotion.MODE_ASSAULT ? 1 : growth);
                bone.setPosX((float) (-tip.x * 16));
                bone.setPosY((float) (tip.y * 16));
                bone.setPosZ((float) (-tip.z * 16));
                float scale = (float) HydraMotion.smooth((growth - 0.65) / 0.35) * (head == 0 ? 1.25F : 1.15F);
                bone.setScaleX(scale);
                bone.setScaleY(scale);
                bone.setScaleZ(scale);
                bone.setHidden(scale < 0.01F || (mode == HydraMotion.MODE_ASSAULT && ((d.impactMask() & (1 << head)) != 0 || age >= HydraMotion.impactTick(head))));
                bone.setRotX(HydraMotion.headRotX(mode, head, age));
                if (mode == HydraMotion.MODE_ROAR) {
                    bone.setRotY((float) Math.toRadians(head == 1 ? -22 : head == 2 ? 22 : 0));
                    bone.setRotZ((float) Math.toRadians(head == 1 ? -8 : head == 2 ? 8 : 0));
                } else {
                    bone.setRotY((float) Math.toRadians(head == 1 ? -16 : head == 2 ? 16 : 0));
                    bone.setRotZ(0);
                }
                var jaw = getAnimationProcessor().getBone("jaw_" + name);
                if (jaw != null) {
                    jaw.setHidden(false);
                    jaw.setScaleX(1.0F);
                    jaw.setScaleY(1.0F);
                    jaw.setScaleZ(1.0F);
                    jaw.setPosX(0);
                    jaw.setPosY(0);
                    jaw.setPosZ(0);
                    jaw.setRotX(HydraMotion.jaw(mode, head, age));
                    jaw.setRotY(0);
                    jaw.setRotZ(0);
                }
            }
        }

        private static final ResourceLocation HYDRA_GEO = Reference.id("geo/ice_hydra.geo.json");
        private static final ResourceLocation HYDRA_TEX = Reference.id("textures/entity/ice_hydra.png");
        private static final ResourceLocation HYDRA_ANIM = Reference.id("animations/ice_hydra.animation.json");

        @Override public ResourceLocation getModelResource(IceDragonEntity d) {
            return HYDRA_GEO;
        }

        @Override public ResourceLocation getTextureResource(IceDragonEntity d) {
            return HYDRA_TEX;
        }

        @Override public ResourceLocation getAnimationResource(IceDragonEntity d) {
            return HYDRA_ANIM;
        }

        @Override public net.minecraft.client.renderer.RenderType getRenderType(IceDragonEntity d, ResourceLocation texture) {
            return net.minecraft.client.renderer.RenderType.entityTranslucent(texture);
        }
    }
}
