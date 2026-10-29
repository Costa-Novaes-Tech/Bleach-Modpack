package com.bleachmod.client.model;

import com.bleachmod.common.visual.HigashiCinematicPath;
import dev.kosmx.playerAnim.api.TransformType;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.api.layered.modifier.MirrorModifier;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.core.util.Vec3f;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;

import java.util.Map;
import java.util.WeakHashMap;

/** Samples the installed Better Combat clip; never starts an attack or changes server damage. */
public final class HigashiBetterCombatMotion {
    public static final String CLIP = "bettercombat:one_handed_uppercut_right";
    private static final Map<AbstractClientPlayer, Sampler> SAMPLES = new WeakHashMap<>();
    private HigashiBetterCombatMotion() { }

    public static boolean available() {
        return net.bettercombat.client.animation.AnimationRegistry.animations.get(CLIP) != null;
    }

    private static float clipTime(float age, boolean execution, float duration) {
        if (age <= 8) return Math.max(0, age);
        if (age <= HigashiCinematicPath.SLASH_END) return 8 + (age - 8) * 1.5F;
        float holdEnd = execution ? HigashiCinematicPath.IGNITE : HigashiCinematicPath.SLASH_END;
        if (age <= holdEnd) return 20;
        return 20 + 10 * Mth.clamp((age - holdEnd) / Math.max(1, duration - holdEnd), 0, 1);
    }

    public static Vec3f transform(AbstractClientPlayer player, float age, boolean execution, float duration,
                                  String part, TransformType type, Vec3f value) {
        var data = net.bettercombat.client.animation.AnimationRegistry.animations.get(CLIP);
        if (data == null || age < 0 || age >= duration) return value;
        float time = clipTime(age, execution, duration);
        int tick = Mth.floor(time);
        boolean left = player.getMainArm() == HumanoidArm.LEFT;
        var sampler = SAMPLES.computeIfAbsent(player, ignored -> new Sampler());
        if (sampler.data != data || sampler.tick != tick || sampler.left != left) {
            sampler.data = data;
            sampler.tick = tick;
            sampler.left = left;
            // The pinned PlayerAnimator constructor does not link modifier delegates.
            // Use the mutators so the mirror forwards to the actual keyframe player.
            sampler.layer = new ModifierLayer<>();
            sampler.layer.addModifierLast(new MirrorModifier(left));
            sampler.layer.setAnimation(new KeyframeAnimationPlayer(data, tick));
        }
        sampler.layer.setupAnim(time - tick);
        Vec3f result = sampler.layer.get3DTransform(part, type, time - tick, value);
        float weight = Math.min(1, age / 3) * Math.min(1, (duration - age) / 6);
        return new Vec3f(Mth.lerp(weight, value.getX(), result.getX()),
                Mth.lerp(weight, value.getY(), result.getY()), Mth.lerp(weight, value.getZ(), result.getZ()));
    }

    private static final class Sampler {
        KeyframeAnimation data;
        int tick;
        boolean left;
        ModifierLayer<KeyframeAnimationPlayer> layer;
    }
}
