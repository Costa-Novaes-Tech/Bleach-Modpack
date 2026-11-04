package com.bleachmod.client.model;

import com.bleachmod.Reference;
import com.bleachmod.common.data.CharacterData;
import com.bleachmod.common.data.PlayerCapability;
import com.bleachmod.common.network.s2c.SenbonPetalsS2C;
import com.bleachmod.init.ModParticles;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import dev.kosmx.playerAnim.api.TransformType;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.core.util.Vec3f;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationFactory;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.Animation;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoObjectRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Client-side 3D GeckoLib + PlayerAnimator visuals for Senbonzakura Bankai Slot 4
 * (Shukei: Hakuteiken — Imperial White Sword).
 *
 * Replaces only the flat procedural quad WINGS construct on the client while preserving
 * all server-side mechanics, hitboxes, damage, and SenbonConstructGeometry regression contracts.
 */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT)
public final class SenbonHakuteikenVisuals {
    public static final ResourceLocation POSE_LAYER = Reference.id("hakuteiken_pose");
    public static final ResourceLocation POSE_DRIVER = Reference.id("hakuteiken_pose_driver");
    public static final ResourceLocation DASH_ANIM = Reference.id("hakuteiken_dash");

    public static final String[] EXPECTED_BONES = {
            "root", "wing_nexus",
            "wing_left_root", "wing_left_mid", "wing_left_tip", "wing_left_feathers",
            "wing_right_root", "wing_right_mid", "wing_right_tip", "wing_right_feathers",
            "petal_streams"
    };

    private static final class ActiveState {
        final int ownerId;
        Vec3 origin;
        Vec3 forward;
        float progress;
        int pattern;
        long castStartTick;
        long phaseBornTick;
        long untilTick;
        final HakuteikenAnimatable animatable = new HakuteikenAnimatable();
        boolean poseTriggered;

        ActiveState(int ownerId, Vec3 origin, Vec3 forward, float progress, int pattern, long now, long until) {
            this.ownerId = ownerId;
            this.origin = origin;
            this.forward = forward;
            this.progress = progress;
            this.pattern = pattern;
            this.castStartTick = now;
            this.phaseBornTick = now;
            this.untilTick = until;
        }
    }

    private static final Map<Integer, ActiveState> STATES = new LinkedHashMap<>();
    private static HakuteikenRenderer RENDERER;

    private SenbonHakuteikenVisuals() {
    }

    public static void init() {
        PlayerAnimationAccess.REGISTER_ANIMATION_EVENT.register((player, stack) -> {
            var layer = new ModifierLayer<IAnimation>();
            stack.addAnimLayer(3800, layer);
            PlayerAnimationAccess.getPlayerAssociatedData(player).set(POSE_LAYER, layer);
        });
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(POSE_DRIVER, 3950, DirectPoseAnimation::new);
    }

    @SuppressWarnings("unchecked")
    public static ModifierLayer<IAnimation> getLayer(AbstractClientPlayer player) {
        return PlayerAnimationAccess.getPlayerAssociatedData(player).get(POSE_LAYER) instanceof ModifierLayer<?> layer
                ? (ModifierLayer<IAnimation>) layer
                : null;
    }

    public static void accept(SenbonPetalsS2C m) {
        if (m.shape() != SenbonPetalsS2C.WINGS) {
            return;
        }
        var mc = Minecraft.getInstance();
        if (mc.level == null) {
            return;
        }
        long now = mc.level.getGameTime();
        int ownerId = m.ownerId();
        if (!STATES.containsKey(ownerId) && STATES.size() >= 24) {
            STATES.remove(STATES.keySet().iterator().next());
        }
        ActiveState existing = STATES.get(ownerId);
        Vec3 fwd = m.forward().lengthSqr() > 1.0E-4 ? m.forward().normalize() : new Vec3(0, 0, -1);
        boolean newlyEnteredTransform = false;
        if (existing == null) {
            existing = new ActiveState(ownerId, m.origin(), fwd, m.progress(), m.pattern(), now, now + 8);
            STATES.put(ownerId, existing);
        } else {
            if (existing.pattern != m.pattern()) {
                if (m.pattern() == 2) {
                    newlyEnteredTransform = true;
                }
                existing.phaseBornTick = now;
            }
            existing.origin = m.origin();
            existing.forward = fwd;
            existing.progress = m.progress();
            existing.pattern = m.pattern();
            existing.untilTick = now + 8;
        }

        if (newlyEnteredTransform) {
            spawnImpactPetalSea(mc.level, m.origin());
            if (mc.level.getEntity(ownerId) instanceof AbstractClientPlayer clientPlayer) {
                var layer = getLayer(clientPlayer);
                if (layer != null) {
                    layer.setAnimation(null);
                }
            }
        } else if (existing.pattern != 2 && !existing.poseTriggered && mc.level.getEntity(ownerId) instanceof AbstractClientPlayer clientPlayer) {
            triggerPlayerPose(clientPlayer);
            existing.poseTriggered = true;
        }
    }

    private static void spawnImpactPetalSea(net.minecraft.client.multiplayer.ClientLevel level, Vec3 impact) {
        for (int i = 0; i < 48; i++) {
            double angle = (i / 48.0) * Math.PI * 2.0 + (level.random.nextDouble() - 0.5) * 0.15;
            double dist = 0.4 + level.random.nextDouble() * 4.2;
            double px = impact.x + Math.cos(angle) * dist;
            double py = impact.y + 0.08 + level.random.nextDouble() * 0.85;
            double pz = impact.z + Math.sin(angle) * dist;
            double speed = 0.06 + level.random.nextDouble() * 0.14;
            double vx = Math.cos(angle) * speed;
            double vy = 0.04 + level.random.nextDouble() * 0.12;
            double vz = Math.sin(angle) * speed;
            level.addParticle(ModParticles.SENBON_PETAL.get(), px, py, pz, vx, vy, vz);
        }
    }

    public static void triggerPlayerPose(AbstractClientPlayer player) {
        var layer = getLayer(player);
        if (layer == null) {
            return;
        }
        var anim = PlayerAnimationRegistry.getAnimation(DASH_ANIM);
        if (anim != null) {
            layer.setAnimation(new KeyframeAnimationPlayer(anim));
        }
    }

    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        var mc = Minecraft.getInstance();
        if (mc.level == null) {
            STATES.clear();
            return;
        }
        long now = mc.level.getGameTime();
        STATES.values().removeIf(s -> s.untilTick < now);

        for (AbstractClientPlayer player : mc.level.players()) {
            boolean hakuActive = PlayerCapability.get(player).map(d ->
                    CharacterData.SENBONZAKURA.equals(d.getCharacter().getZanpakutoIdentity())
                            && Reference.FORM_BANKAI.equals(d.getCharacter().getActiveForm())
                            && d.getCharacter().getSenbonBlade() == 2
            ).orElse(false);
            if (hakuActive) {
                ActiveState state = STATES.get(player.getId());
                if (state != null) {
                    state.untilTick = Math.max(state.untilTick, now + 3);
                    if (state.pattern != 2 && !state.poseTriggered) {
                        triggerPlayerPose(player);
                        state.poseTriggered = true;
                    }
                    spawnAngelWingPetals(mc.level, player, state, now);
                }
            }
        }
    }

    private static void spawnAngelWingPetals(net.minecraft.client.multiplayer.ClientLevel level, AbstractClientPlayer player, ActiveState state, long now) {
        Vec3 pos = player.position();
        float yawRad = (float) Math.toRadians(player.getYRot());
        // Player right vector (+X local) and back vector (+Z local) in world space
        double rx = -Math.cos(yawRad);
        double rz = -Math.sin(yawRad);
        double bx = Math.sin(yawRad);
        double bz = -Math.cos(yawRad);

        int countPerWing = state.pattern == 1 ? 5 : 3;
        for (int side = -1; side <= 1; side += 2) {
            for (int i = 0; i < countPerWing; i++) {
                double u = (i + 0.5) / countPerWing;
                // Follow the angelic S-curve arch from shoulder (0.3m) out to wingtip (2.1m)
                double span = (0.35 + 1.80 * u) * side;
                double archY = 1.25 + 0.68 * Math.sin(u * Math.PI * 0.72) - (i % 2 == 0 ? 0.15 : 0.45 * u);
                double depth = 0.25 + 0.15 * u;
                double jitterX = (level.random.nextDouble() - 0.5) * 0.18;
                double jitterY = (level.random.nextDouble() - 0.5) * 0.18;
                double jitterZ = (level.random.nextDouble() - 0.5) * 0.18;

                double px = pos.x + rx * span + bx * depth + jitterX;
                double py = pos.y + archY + jitterY;
                double pz = pos.z + rz * span + bz * depth + jitterZ;

                double vx = rx * (0.025 * side) + bx * (state.pattern == 1 ? 0.08 : 0.02) + (level.random.nextDouble() - 0.5) * 0.02;
                double vy = 0.015 - 0.03 * u + (level.random.nextDouble() - 0.5) * 0.015;
                double vz = rz * (0.025 * side) + bz * (state.pattern == 1 ? 0.08 : 0.02) + (level.random.nextDouble() - 0.5) * 0.02;

                level.addParticle(ModParticles.SENBON_PETAL.get(), px, py, pz, vx, vy, vz);
            }
        }
    }

    @SubscribeEvent
    public static void render(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_ENTITIES) {
            return;
        }
        var mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null || STATES.isEmpty()) {
            return;
        }
        long now = mc.level.getGameTime();
        STATES.values().removeIf(s -> s.untilTick < now);
        if (STATES.isEmpty()) {
            return;
        }

        if (RENDERER == null) {
            RENDERER = new HakuteikenRenderer();
        }

        float pt = event.getPartialTick();
        Vec3 cameraPos = event.getCamera().getPosition();
        PoseStack pose = event.getPoseStack();
        MultiBufferSource.BufferSource buffers = mc.renderBuffers().bufferSource();
        RenderType rt = RenderType.entityTranslucent(HakuteikenModel.TEX);
        BakedGeoModel baked = RENDERER.getGeoModel().getBakedModel(HakuteikenModel.GEO);

        for (ActiveState state : STATES.values()) {
            Entity owner = mc.level.getEntity(state.ownerId);
            Vec3 worldPos = owner != null ? owner.getPosition(pt) : state.origin;
            if (mc.player.distanceToSqr(worldPos) > 96 * 96) {
                continue;
            }

            Vec3 fwd = state.forward;
            if (owner != null && state.pattern == 0) {
                float viewYaw = owner.getViewYRot(pt);
                Vec3 liveFwd = Vec3.directionFromRotation(0.0F, viewYaw);
                if (liveFwd.lengthSqr() > 1.0E-4) {
                    fwd = liveFwd.normalize();
                }
            } else if (owner instanceof LivingEntity living && state.pattern == 2) {
                float bodyYaw = Mth.rotLerp(pt, living.yBodyRotO, living.yBodyRot);
                fwd = Vec3.directionFromRotation(0.0F, bodyYaw);
            }
            float yawDeg = (float) Math.toDegrees(Math.atan2(-fwd.x, -fwd.z));

            String clip = state.pattern == 1 ? "dash" : (state.pattern == 0 && state.progress >= 0.0F ? "windup" : "idle");
            double animTick = Math.max(0.0, (now - state.phaseBornTick) + pt);
            state.animatable.setClip(clip);
            state.animatable.setTick(animTick);

            Vec3 rel = worldPos.subtract(cameraPos);
            pose.pushPose();
            pose.translate(rel.x, rel.y, rel.z);

            // Align model local forward (-Z) with player body orientation
            pose.mulPose(Axis.YP.rotationDegrees(yawDeg));

            // Match vanilla PlayerRenderer 0.9375F scale so Blockbench pixel heights [0..24] match the player torso
            float scale = (state.pattern == 0
                    ? Mth.clamp(0.78F + 0.22F * state.progress, 0.78F, 1.0F)
                    : 1.0F) * 0.9375F;
            pose.scale(scale, scale, scale);

            // Couple the wing construct directly to the player's torso waist pivot (y = 0.75m) so when the torso
            // leans forward during the diagonal rasante or crouching, the wings tilt in 1:1 physical union with the back.
            Vec3f torsoRot = computeTorsoRotation(owner, state, now, pt);
            if (owner != null && owner.isCrouching()) {
                pose.translate(0.0, -0.125, 0.0);
            }
            pose.translate(0.0, 1.35, 0.0);
            pose.mulPose(Axis.XP.rotation(-torsoRot.getX()));
            pose.mulPose(Axis.YP.rotation(-torsoRot.getY()));
            pose.translate(0.0, -1.35, 0.0);

            VertexConsumer consumer = buffers.getBuffer(rt);
            RENDERER.render(pose, state.animatable, buffers, rt, consumer, LightTexture.FULL_BRIGHT);
            pose.popPose();
        }

        buffers.endBatch(rt);
    }

    private static Vec3f computeTorsoRotation(Entity owner, ActiveState state, long now, float pt) {
        if (state.pattern == 2) {
            float crouchPitch = (owner != null && owner.isCrouching()) ? 0.5F : 0.0F;
            return new Vec3f(crouchPitch, 0.0F, 0.0F);
        }
        float sign = (owner instanceof LivingEntity living && living.getMainArm() == HumanoidArm.LEFT) ? -1.0F : 1.0F;
        boolean isDash = state.pattern == 1;
        float phaseAge = (float) (now - state.phaseBornTick) + pt;
        float coil = isDash ? 1.0F : smooth(phaseAge / 11.0F);
        float lock = isDash ? 1.0F : smooth((phaseAge - 11.0F) / 4.5F);
        float dash = isDash ? smooth(phaseAge / 2.5F) : 0.0F;
        float w = isDash ? Mth.clamp(1.0F - smooth((phaseAge - 9.0F) / 3.0F), 0.0F, 1.0F) : smooth(phaseAge / 2.0F);
        float rx = Mth.lerp(dash, Mth.lerp(lock, -0.17F * coil, 0.28F), 0.38F) * w;
        float ry = sign * Mth.lerp(dash, Mth.lerp(lock, 0.26F * coil, -0.12F), -0.16F) * w;
        return new Vec3f(rx, ry, 0.0F);
    }

    public static HakuteikenRenderer getOrCreateRenderer() {
        if (RENDERER == null) {
            RENDERER = new HakuteikenRenderer();
        }
        return RENDERER;
    }

    public static int verifyBakedBones() {
        HakuteikenRenderer renderer = getOrCreateRenderer();
        BakedGeoModel baked = renderer.getGeoModel().getBakedModel(HakuteikenModel.GEO);
        if (baked == null) {
            return 0;
        }
        int found = 0;
        for (String bone : EXPECTED_BONES) {
            if (baked.getBone(bone).isPresent()) {
                found++;
            }
        }
        return found;
    }

    public static boolean isActiveFor(int ownerId) {
        return STATES.containsKey(ownerId);
    }

    public static String getActiveClip(int ownerId) {
        ActiveState s = STATES.get(ownerId);
        return s != null ? s.animatable.getClip() : "none";
    }

    public static void clear() {
        STATES.clear();
    }

    @SubscribeEvent
    public static void logout(ClientPlayerNetworkEvent.LoggingOut e) {
        clear();
    }

    @SubscribeEvent
    public static void unload(LevelEvent.Unload e) {
        if (e.getLevel().isClientSide()) {
            clear();
        }
    }

    // -----------------------------------------------------------------------------------------
    // High-Priority PlayerAnimator Driver matching Blockbench Windup + Lunge Keyframes
    // -----------------------------------------------------------------------------------------

    private static float smooth(float t) {
        t = Mth.clamp(t, 0.0F, 1.0F);
        return t * t * (3.0F - 2.0F * t);
    }

    private static Vec3f mix(Vec3f from, float x, float y, float z, float w) {
        return new Vec3f(
                Mth.lerp(w, from.getX(), x),
                Mth.lerp(w, from.getY(), y),
                Mth.lerp(w, from.getZ(), z)
        );
    }

    private static final class DirectPoseAnimation implements IAnimation {
        private final AbstractClientPlayer player;

        DirectPoseAnimation(AbstractClientPlayer player) {
            this.player = player;
        }

        @Override
        public boolean isActive() {
            ActiveState s = STATES.get(player.getId());
            return s != null && s.pattern != 2 && !ZanpakutoReleaseAnimation.active(player);
        }

        @Override
        public void setupAnim(float partialTick) {
        }

        @Override
        public FirstPersonMode getFirstPersonMode(float partialTick) {
            return FirstPersonMode.VANILLA;
        }

        @Override
        public Vec3f get3DTransform(String part, TransformType type, float partialTick, Vec3f value) {
            ActiveState state = STATES.get(player.getId());
            if (state == null || state.pattern == 2 || player.level() == null) {
                return value;
            }
            float sign = player.getMainArm() == HumanoidArm.RIGHT ? 1.0F : -1.0F;
            boolean mainArm = part.equals(sign > 0 ? "rightArm" : "leftArm");
            boolean supportArm = part.equals(sign > 0 ? "leftArm" : "rightArm");
            boolean mainLeg = part.equals(sign > 0 ? "rightLeg" : "leftLeg");
            boolean backLeg = part.equals(sign > 0 ? "leftLeg" : "rightLeg");
            boolean mainItem = part.equals(sign > 0 ? "rightItem" : "leftItem");

            boolean isDash = state.pattern == 1;
            float phaseAge = (float) (player.level().getGameTime() - state.phaseBornTick) + partialTick;
            float coil = isDash ? 1.0F : smooth(phaseAge / 11.0F);
            float lock = isDash ? 1.0F : smooth((phaseAge - 11.0F) / 4.5F);
            float dash = isDash ? smooth(phaseAge / 2.5F) : 0.0F;
            float w = isDash ? Mth.clamp(1.0F - smooth((phaseAge - 9.0F) / 3.0F), 0.0F, 1.0F) : smooth(phaseAge / 2.0F);

            if (type == TransformType.ROTATION) {
                if (part.equals("body")) {
                    float rx = Mth.lerp(dash, Mth.lerp(lock, -0.17F * coil, 0.28F), 0.38F);
                    float ry = sign * Mth.lerp(dash, Mth.lerp(lock, 0.26F * coil, -0.12F), -0.16F);
                    return mix(value, rx, ry, 0.0F, w);
                }
                if (part.equals("head")) {
                    float rx = Mth.lerp(dash, Mth.lerp(lock, -0.18F * coil, 0.14F), -0.16F);
                    float ry = sign * Mth.lerp(dash, Mth.lerp(lock, -0.22F * coil, 0.12F), 0.16F);
                    return mix(value, rx, ry, 0.0F, w);
                }
                if (mainArm) {
                    // Upward leap: raises katana high overhead (-2.25 rad), then snaps to diagonal downward rasante thrust (-1.36 rad)
                    float rx = Mth.lerp(dash, Mth.lerp(lock, -2.25F * coil - 0.50F * (1.0F - coil), -1.28F), -1.36F);
                    float ry = sign * Mth.lerp(dash, Mth.lerp(lock, -0.28F * coil, 0.18F), 0.22F);
                    float rz = sign * Mth.lerp(dash, Mth.lerp(lock, 0.24F * coil, -0.12F), -0.14F);
                    return mix(value, rx, ry, rz, w);
                }
                if (supportArm) {
                    float rx = Mth.lerp(dash, Mth.lerp(lock, -0.85F * coil, 0.45F), 0.72F);
                    float ry = sign * Mth.lerp(dash, Mth.lerp(lock, 0.38F * coil, 0.28F), 0.36F);
                    float rz = sign * Mth.lerp(dash, Mth.lerp(lock, -0.31F * coil, -0.38F), -0.46F);
                    return mix(value, rx, ry, rz, w);
                }
                if (mainItem) {
                    // Pitch the held white Hakuteiken katana along the diagonal rasante dive vector (-1.32 rad = -76 deg)
                    float rx = Mth.lerp(dash, Mth.lerp(lock, 0.18F * coil, -1.18F), -1.32F);
                    return mix(value, rx, 0.0F, 0.0F, w);
                }
                if (mainLeg) {
                    float rx = Mth.lerp(dash, 0.36F * coil, -0.46F);
                    float rz = sign * Mth.lerp(dash, 0.10F * coil, 0.09F);
                    return mix(value, rx, 0.0F, rz, w);
                }
                if (backLeg) {
                    float rx = Mth.lerp(dash, -0.38F * coil, 0.56F);
                    float rz = sign * Mth.lerp(dash, -0.10F * coil, -0.10F);
                    return mix(value, rx, 0.0F, rz, w);
                }
            }
            return value;
        }
    }

    // -----------------------------------------------------------------------------------------
    // GeckoLib 4.8.4 Model, Animatable, and Renderer
    // -----------------------------------------------------------------------------------------

    public static final class HakuteikenModel extends GeoModel<HakuteikenAnimatable> {
        public static final ResourceLocation GEO = Reference.id("geo/senbon_hakuteiken.geo.json");
        public static final ResourceLocation TEX = Reference.id("textures/entity/senbon_hakuteiken.png");
        public static final ResourceLocation ANIM = Reference.id("animations/senbon_hakuteiken.animation.json");

        @Override
        public ResourceLocation getModelResource(HakuteikenAnimatable animatable) {
            return GEO;
        }

        @Override
        public ResourceLocation getTextureResource(HakuteikenAnimatable animatable) {
            return TEX;
        }

        @Override
        public ResourceLocation getAnimationResource(HakuteikenAnimatable animatable) {
            return ANIM;
        }

        @Override
        public RenderType getRenderType(HakuteikenAnimatable animatable, ResourceLocation texture) {
            return RenderType.entityTranslucent(texture);
        }
    }

    public static final class HakuteikenAnimatable implements GeoAnimatable {
        private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
        private String currentClip = "windup";
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
            controllers.add(new AnimationController<>(this, "hakuteiken_controller", 2, state -> {
                String animName = "animation.hakuteiken." + currentClip;
                if ("idle".equals(currentClip)) {
                    return state.setAndContinue(RawAnimation.begin().thenLoop(animName));
                }
                return state.setAndContinue(RawAnimation.begin().then(animName, Animation.LoopType.HOLD_ON_LAST_FRAME));
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

    public static final class HakuteikenRenderer extends GeoObjectRenderer<HakuteikenAnimatable> {
        public HakuteikenRenderer() {
            super(new HakuteikenModel());
        }
    }
}
