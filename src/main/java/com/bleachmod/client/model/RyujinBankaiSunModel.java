package com.bleachmod.client.model;

import com.bleachmod.Reference;
import com.bleachmod.common.visual.RyujinBankaiTimeline;
import com.bleachmod.common.visual.RyujinSolarPose;
import com.bleachmod.common.visual.RyujinBankaiPresentation;
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
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.Color;
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
    private static final ResourceLocation ANIM = Reference.id("animations/ryujin_bankai_sun_v10.animation.json");
    private static final ResourceLocation FINE_ATLAS = Reference.id("ryujin_solar_atlas_v16.json");
    private static final Map<Integer, Sun> SCENES = new HashMap<>();
    private static final Renderer RENDERER = new Renderer();
    private static final class SolarTypes extends RenderType {
        private SolarTypes(){super("ryujin_solar_types",com.mojang.blaze3d.vertex.DefaultVertexFormat.NEW_ENTITY,
                com.mojang.blaze3d.vertex.VertexFormat.Mode.QUADS,256,false,true,()->{},()->{});}
        static final RenderType BODY=create("ryujin_solar_volume",com.mojang.blaze3d.vertex.DefaultVertexFormat.NEW_ENTITY,
                com.mojang.blaze3d.vertex.VertexFormat.Mode.QUADS,1536,false,true,
                // Unlit with alpha discard; vanilla translucent-emissive still mixes normals.
                CompositeState.builder().setShaderState(new ShaderStateShard(RyujinSolarShader::get))
                        .setTextureState(new TextureStateShard(TEX,false,false))
                        .setTransparencyState(TRANSLUCENT_TRANSPARENCY).setCullState(NO_CULL)
                        .setLightmapState(LIGHTMAP).setOverlayState(OVERLAY)
                        .setWriteMaskState(COLOR_DEPTH_WRITE).createCompositeState(false));
    }

    private RyujinBankaiSunModel() { }

    public static void remove(int id) { SCENES.remove(id); }
    public static void clear() { SCENES.clear(); }

    public static void render(PoseStack stack, Vec3 camera, Vec3 anchor, Vec3 forward, int id, double realTick,
                              RyujinBankaiPresentation.Blade blade) {
        if (Minecraft.getInstance().level == null || anchor.distanceToSqr(camera) > 128 * 128
                || realTick >= RyujinBankaiTimeline.realAge(RyujinBankaiTimeline.ABSORPTION_END,RyujinBankaiTimeline.DURATION)) return;
        Sun sun = SCENES.computeIfAbsent(id, ignored -> new Sun());
        sun.tick = realTick;
        Vec3 offset=RyujinSolarPose.pilot()?RyujinSolarPose.center(anchor,forward,
                RyujinBankaiTimeline.storyAge(realTick,RyujinBankaiTimeline.DURATION),blade)
                .subtract(RyujinSolarPose.heldSourceCenter(anchor,forward)):Vec3.ZERO;
        Vec3 rel = anchor.add(offset).subtract(camera);
        stack.pushPose();
        stack.translate(rel.x, rel.y + RyujinSolarPose.MODEL_BASE_Y, rel.z);
        float yaw = (float)Math.toDegrees(Math.atan2(-forward.x, forward.z));
        // GeckoLib's exported -Z is the scene front; Minecraft yaw zero faces +Z.
        // Half-turn the asset so its +Z solar depth is behind the actual caster.
        stack.mulPose(Axis.YP.rotationDegrees(180 - yaw));
        // The source is modeled in Minecraft pixels with the rear on local +Z.
        stack.scale(RyujinSolarPose.MODEL_SCALE,RyujinSolarPose.MODEL_SCALE,RyujinSolarPose.MODEL_SCALE);
        // GeoObjectRenderer adds this block/item origin in preRender. A world VFX
        // must cancel it before rendering or its centre drifts away from the corona.
        if(RyujinSolarPose.pilot())stack.translate(-.5,-.51,-.5);
        MultiBufferSource.BufferSource buffers = Minecraft.getInstance().renderBuffers().bufferSource();
        RenderType type = RENDERER.getGeoModel().getRenderType(sun,TEX);
        VertexConsumer out = buffers.getBuffer(type);
        RENDERER.continuous=RyujinSolarPose.pilot() && Boolean.getBoolean("bleachmod.ryujinContinuousSolar")
                && Minecraft.getInstance().getResourceManager().getResource(FINE_ATLAS).isPresent();
        RENDERER.render(stack, sun, buffers, type, out, LightTexture.FULL_BRIGHT);
        buffers.endBatch(type);
        stack.popPose();
    }

    private static final class Model extends GeoModel<Sun> {
        @Override public ResourceLocation getModelResource(Sun animatable) { return GEO; }
        @Override public ResourceLocation getTextureResource(Sun animatable) { return TEX; }
        @Override public ResourceLocation getAnimationResource(Sun animatable) { return ANIM; }
        @Override public RenderType getRenderType(Sun animatable, ResourceLocation texture) {
            if(RyujinSolarShader.usePackMaterial())return RenderType.entityTranslucentEmissive(texture);
            return RyujinSolarPose.pilot()?SolarTypes.BODY:RenderType.entityTranslucentEmissive(texture);
        }
        @Override public void setCustomAnimations(Sun animatable, long instanceId, AnimationState<Sun> state) {
            super.setCustomAnimations(animatable, instanceId, state);
            // GeckoLib may begin its clip a few frames after the scene packet arrives.
            // The silhouette must obey the shared presentation clock, especially at sealing.
            float scale = (float)(RyujinBankaiTimeline.sunRadius(
                    RyujinBankaiTimeline.storyAge(animatable.tick, RyujinBankaiTimeline.DURATION)) / 9.0);
            final float visualScale = Math.max(.001F, scale);
            var bone = getAnimationProcessor().getBone("sun_root");
            if (bone != null) {
                if(RyujinSolarPose.pilot()){
                    bone.setPosX(0);bone.setPosY(18);bone.setPosZ(18);
                }
                bone.setScaleX(visualScale);
                bone.setScaleY(visualScale);
                bone.setScaleZ(visualScale);
            }
            // The exported clip staggers the core and rings. Anchor those scales
            // to the same real cue so first-load rendering cannot leave a hollow sun.
            setLayerScale("sun_core", animatable.tick, 81, 100);
            if(RyujinSolarPose.pilot()){
                var core=getAnimationProcessor().getBone("sun_core");
                if(core!=null)core.setRotZ((float)(animatable.tick*.004));
            }
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
            controllers.add(new AnimationController<Sun>(this, "bankai_sun", 0,
                    state -> state.setAndContinue(RawAnimation.begin().thenPlay("animation.ryujin.bankai_sun"))){
                @Override protected double adjustTick(double requestedTick){
                    super.adjustTick(requestedTick); // Preserve controller transition/reset bookkeeping.
                    // Rebinding baked bones on reload must not restart this one-shot clip.
                    return Sun.this.tick;
                }
            });
        }
        @Override public AnimatableInstanceCache getAnimatableInstanceCache() { return cache; }
        @Override public double getTick(Object object) { return tick; }
    }

    private static final class Renderer extends GeoObjectRenderer<Sun> {
        private static final Color SHADER_PACK_SOLAR = Color.ofRGB(194,180,166);
        private boolean continuous;

        private Renderer() {
            super(new Model());
            // The base type is already emissive; avoid an extra offset/glow pass in the pilot.
            if(!RyujinSolarPose.pilot())addRenderLayer(new AutoGlowingGeoLayer<>(this));
        }

        @Override
        public Color getRenderColor(Sun sun,float partialTick,int light){
            return RyujinSolarShader.usePackMaterial()?SHADER_PACK_SOLAR:super.getRenderColor(sun,partialTick,light);
        }

        @Override
        public void renderCubesOfBone(PoseStack pose,GeoBone bone,VertexConsumer vertices,int light,int overlay,
                                      float red,float green,float blue,float alpha){
            if(continuous && !bone.isHidden()
                    && RyujinSolarBands.draw(bone.getName(),pose.last(),vertices,light,overlay,red,green,blue,alpha))return;
            super.renderCubesOfBone(pose,bone,vertices,light,overlay,red,green,blue,alpha);
        }
    }
}
