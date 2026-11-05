package com.bleachmod.client.model;

import com.bleachmod.Reference;
import com.bleachmod.common.visual.RyujinBankaiTimeline;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoObjectRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.HashMap;
import java.util.Map;

/** One editable GeckoLib sun clip per caster, rendered in the actor's local back (+Z). */
public final class RyujinBankaiSunModel {
    private static final ResourceLocation GEO = Reference.id("geo/ryujin_bankai_sun.geo.json");
    private static final ResourceLocation TEX = Reference.id("textures/entity/ryujin_bankai_sun.png");
    private static final ResourceLocation ANIM = Reference.id("animations/ryujin_bankai_sun.animation.json");
    private static final Map<Integer, Sun> SCENES = new HashMap<>();
    private static final Renderer RENDERER = new Renderer();

    private RyujinBankaiSunModel() { }

    public static void remove(int id) { SCENES.remove(id); }
    public static void clear() { SCENES.clear(); }

    public static void render(PoseStack stack, Vec3 camera, Vec3 anchor, Vec3 forward, int id, double realTick) {
        if (Minecraft.getInstance().level == null || anchor.distanceToSqr(camera) > 128 * 128
                || realTick >= 142) return;
        Sun sun = SCENES.computeIfAbsent(id, ignored -> new Sun());
        sun.tick = realTick;
        Vec3 rel = anchor.subtract(camera);
        stack.pushPose();
        stack.translate(rel.x, rel.y - 2.5, rel.z);
        float yaw = (float)Math.toDegrees(Math.atan2(-forward.x, forward.z));
        // GeckoLib's exported -Z is the scene front; Minecraft yaw zero faces +Z.
        // Half-turn the asset so its +Z solar depth is behind the actual caster.
        stack.mulPose(Axis.YP.rotationDegrees(180 - yaw));
        // The source is modeled in Minecraft pixels with the rear on local +Z.
        stack.scale(1.35F, 1.35F, 1.35F);
        MultiBufferSource.BufferSource buffers = Minecraft.getInstance().renderBuffers().bufferSource();
        RenderType type = RenderType.entityTranslucentEmissive(TEX);
        VertexConsumer out = buffers.getBuffer(type);
        RENDERER.render(stack, sun, buffers, type, out, LightTexture.FULL_BRIGHT);
        buffers.endBatch(type);
        stack.popPose();
    }

    private static final class Model extends GeoModel<Sun> {
        @Override public ResourceLocation getModelResource(Sun animatable) { return GEO; }
        @Override public ResourceLocation getTextureResource(Sun animatable) { return TEX; }
        @Override public ResourceLocation getAnimationResource(Sun animatable) { return ANIM; }
        @Override public RenderType getRenderType(Sun animatable, ResourceLocation texture) {
            return RenderType.entityTranslucentEmissive(texture);
        }
        @Override public void setCustomAnimations(Sun animatable, long instanceId, AnimationState<Sun> state) {
            super.setCustomAnimations(animatable, instanceId, state);
            // GeckoLib may begin its clip a few frames after the scene packet arrives.
            // The silhouette must obey the shared 0..240 clock, especially at sealing.
            float scale = (float)(RyujinBankaiTimeline.sunRadius(
                    RyujinBankaiTimeline.storyAge(animatable.tick, RyujinBankaiTimeline.DURATION)) / 9.0);
            final float visualScale = Math.max(.001F, scale);
            var bone = getAnimationProcessor().getBone("sun_root");
            if (bone != null) {
                bone.setScaleX(visualScale);
                bone.setScaleY(visualScale);
                bone.setScaleZ(visualScale);
            }
            // The exported clip staggers the core and rings. Anchor those scales
            // to the same real cue so first-load rendering cannot leave a hollow sun.
            setLayerScale("sun_core", animatable.tick, 81, 100);
            setLayerScale("inner_swirl", animatable.tick, 84, 109);
            setLayerScale("mid_swirl", animatable.tick, 87, 110);
            setLayerScale("outer_swirl", animatable.tick, 88, 111);
            setLayerScale("broken_rim", animatable.tick, 90, 112);
        }
        private void setLayerScale(String name, double tick, double start, double end) {
            var bone = getAnimationProcessor().getBone(name);
            if (bone == null) return;
            float scale = (float)(.03 + .97 * RyujinBankaiTimeline.smooth((tick - start) / (end - start)));
            bone.setScaleX(scale);
            bone.setScaleY(scale);
            bone.setScaleZ(scale);
        }
    }

    private static final class Sun implements GeoAnimatable {
        private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
        private double tick;
        @Override public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
            controllers.add(new AnimationController<>(this, "bankai_sun", 0,
                    state -> state.setAndContinue(RawAnimation.begin().thenPlay("animation.ryujin.bankai_sun"))));
        }
        @Override public AnimatableInstanceCache getAnimatableInstanceCache() { return cache; }
        @Override public double getTick(Object object) { return tick; }
    }

    private static final class Renderer extends GeoObjectRenderer<Sun> {
        private Renderer() {
            super(new Model());
            addRenderLayer(new AutoGlowingGeoLayer<>(this));
        }
    }
}
