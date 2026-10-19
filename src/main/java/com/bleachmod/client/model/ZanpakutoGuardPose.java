package com.bleachmod.client.model;

import com.bleachmod.Reference;
import com.bleachmod.common.data.PlayerCapability;
import com.bleachmod.common.item.ZanpakutoItem;
import com.mojang.math.Axis;
import dev.kosmx.playerAnim.api.TransformType;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.core.util.Vec3f;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationFactory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import java.util.WeakHashMap;

/** Presentation of the existing authoritative guard; never starts or extends blocking. */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT)
public final class ZanpakutoGuardPose {
    private static final WeakHashMap<AbstractClientPlayer, Blend> BLENDS = new WeakHashMap<>();
    private static final class Blend { float amount, time = -1; InteractionHand hand; }
    private ZanpakutoGuardPose() { }
    public static void init() {
        // BC attack layer is 2000 in the pinned version. Guard controls only arms/items.
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(Reference.id("zanpakuto_guard"), 3000, Animation::new);
    }
    public static void clear() { BLENDS.clear(); }
    private static InteractionHand weaponHand(AbstractClientPlayer p) {
        if (p.getMainHandItem().getItem() instanceof ZanpakutoItem) return InteractionHand.MAIN_HAND;
        if (p.getOffhandItem().getItem() instanceof ZanpakutoItem) return InteractionHand.OFF_HAND;
        return null;
    }
    private static boolean available(AbstractClientPlayer p) {
        return p.isAlive() && !p.isSpectator() && !p.isSleeping() && !p.isPassenger()
                && !p.getAbilities().flying && !p.isFallFlying() && !p.isUsingItem() && !p.isFullyFrozen();
    }
    private static Blend sample(AbstractClientPlayer p, float partial) {
        var b = BLENDS.computeIfAbsent(p, ignored -> new Blend());
        var hand = weaponHand(p);
        boolean broken = PlayerCapability.get(p).map(d -> d.getGuard().shownBroken()).orElse(false);
        boolean held = PlayerCapability.get(p).map(d -> d.getGuard().shownActive()).orElse(false);
        float time = p.tickCount + partial;
        float elapsed = b.time < 0 ? 0 : Mth.clamp(time - b.time, 0, 1);
        b.time = Math.max(b.time, time);
        if (!available(p) || broken || hand == null || b.hand != hand) b.amount = 0;
        else b.amount = Mth.clamp(b.amount + (held ? 1 : -1) * elapsed / 4, 0, 1);
        b.hand = hand;
        return b;
    }
    public static boolean engaged(AbstractClientPlayer p, float partial) {
        return sample(p, partial).amount > 0;
    }
    private static float weight(Blend b) { return b.amount * b.amount * (3 - 2 * b.amount); }
    private static HumanoidArm arm(AbstractClientPlayer p, Blend b) {
        return b.hand == InteractionHand.OFF_HAND ? p.getMainArm().getOpposite() : p.getMainArm();
    }
    private static Vec3f mix(Vec3f from, float x, float y, float z, float weight) {
        return new Vec3f(Mth.lerp(weight, from.getX(), x), Mth.lerp(weight, from.getY(), y), Mth.lerp(weight, from.getZ(), z));
    }
    private static final class Animation implements IAnimation {
        private final AbstractClientPlayer player;
        private Blend blend;
        Animation(AbstractClientPlayer player) { this.player = player; }
        @Override public boolean isActive() {
            return available(player) && weaponHand(player) != null
                    && !PlayerCapability.get(player).map(d -> d.getGuard().shownBroken()).orElse(false)
                    && (engaged(player, 0) || PlayerCapability.get(player).map(d -> d.getGuard().shownActive()).orElse(false));
        }
        @Override public void setupAnim(float partial) { blend = sample(player, partial); }
        @Override public FirstPersonMode getFirstPersonMode(float partial) { return FirstPersonMode.VANILLA; }
        @Override public Vec3f get3DTransform(String part, TransformType type, float partial, Vec3f value) {
            // Querying isActive must not advance animation time backwards between render passes.
            blend = sample(player, partial);
            float w = weight(blend), sign = arm(player, blend) == HumanoidArm.RIGHT ? 1 : -1;
            boolean swordArm = part.equals(sign > 0 ? "rightArm" : "leftArm");
            boolean supportArm = part.equals(sign > 0 ? "leftArm" : "rightArm");
            if (type == TransformType.ROTATION) {
                if (swordArm) return mix(value, -1.25F, -.55F * sign, -.35F * sign, w);
                var other = blend.hand == InteractionHand.OFF_HAND ? player.getMainHandItem() : player.getOffhandItem();
                if (supportArm && other.isEmpty()) return mix(value, -.95F, .35F * sign, .15F * sign, w);
                if (part.equals("rightItem") || part.equals("leftItem")) return mix(value, 0, 0, 0, w);
            }
            if (type == TransformType.POSITION && (part.equals("rightItem") || part.equals("leftItem"))) return mix(value, 0, 0, 0, w);
            if (type == TransformType.BEND && (swordArm || supportArm)) return mix(value, 0, 0, 0, w);
            return value;
        }
    }
    @SubscribeEvent public static void firstPerson(RenderHandEvent event) {
        var p = Minecraft.getInstance().player;
        if (p == null) return;
        var b = sample(p, event.getPartialTick());
        if (b.hand != event.getHand() || b.amount <= 0) return;
        float w = weight(b), sign = arm(p, b) == HumanoidArm.RIGHT ? 1 : -1;
        var pose = event.getPoseStack();
        pose.translate(-sign * .42F * w, .16F * w, -.18F * w);
        pose.mulPose(Axis.YP.rotationDegrees(-sign * 30 * w));
        pose.mulPose(Axis.ZP.rotationDegrees(sign * 65 * w));
        pose.mulPose(Axis.XP.rotationDegrees(-20 * w));
    }
}
