package com.bleachmod.client.berserk;

import com.bleachmod.Reference;
import com.bleachmod.init.ModItems;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import java.util.HashMap;
import java.util.Map;

/**
 * Zaraki's way of carrying Nozarashi: blade resting over the shoulder, and every swing (plain attack or
 * technique) comes down like a hammer. Third-person body animation only; first person keeps vanilla hands.
 * Layers sit below the release cinematic (priority 1000), which overrides them while it plays.
 */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT)
public final class NozarashiStance {
    public static final ResourceLocation STANCE_LAYER = Reference.id("nozarashi_stance"), HAMMER_LAYER = Reference.id("nozarashi_hammer");
    private static final ResourceLocation STANCE = Reference.id("nozarashi_shoulder"), HAMMER = Reference.id("nozarashi_hammer");
    private record Swing(boolean swinging, int time) { }
    private static final Map<Integer, Swing> LAST = new HashMap<>();
    private NozarashiStance() { }

    public static void init() {
        PlayerAnimationAccess.REGISTER_ANIMATION_EVENT.register((player, stack) -> {
            var stance = new ModifierLayer<IAnimation>(); var hammer = new ModifierLayer<IAnimation>();
            stack.addAnimLayer(500, stance); stack.addAnimLayer(600, hammer);
            var data = PlayerAnimationAccess.getPlayerAssociatedData(player);
            data.set(STANCE_LAYER, stance); data.set(HAMMER_LAYER, hammer);
        });
    }
    @SuppressWarnings("unchecked")
    private static ModifierLayer<IAnimation> layer(AbstractClientPlayer player, ResourceLocation id) {
        return PlayerAnimationAccess.getPlayerAssociatedData(player).get(id) instanceof ModifierLayer<?> layer ? (ModifierLayer<IAnimation>) layer : null;
    }
    public static boolean shouldered(AbstractClientPlayer player) {
        return player.getMainHandItem().is(ModItems.NOZARASHI.get()) && !player.isSleeping() && !player.isSwimming()
                && !player.isFallFlying() && !player.isPassenger() && !player.isSpectator() && !player.isUsingItem();
    }

    @SubscribeEvent public static void tick(TickEvent.ClientTickEvent event) {
        var mc = Minecraft.getInstance();
        if (event.phase != TickEvent.Phase.END) return;
        if (mc.level == null) { LAST.clear(); return; }
        for (AbstractClientPlayer player : mc.level.players()) {
            var stance = layer(player, STANCE_LAYER); var hammer = layer(player, HAMMER_LAYER);
            if (stance == null || hammer == null) continue;
            boolean shouldered = shouldered(player);
            if (shouldered && stance.getAnimation() == null) play(stance, STANCE);
            else if (!shouldered && stance.getAnimation() != null) { stance.setAnimation(null); hammer.setAnimation(null); }
            Swing last = LAST.getOrDefault(player.getId(), new Swing(false, 0));
            boolean started = player.swinging && (!last.swinging || player.swingTime < last.time);
            if (shouldered && started) play(hammer, HAMMER);
            LAST.put(player.getId(), new Swing(player.swinging, player.swingTime));
        }
        LAST.keySet().removeIf(id -> mc.level.getEntity(id) == null);
    }
    private static void play(ModifierLayer<IAnimation> layer, ResourceLocation id) {
        var animation = PlayerAnimationRegistry.getAnimation(id);
        if (animation != null) layer.setAnimation(new KeyframeAnimationPlayer(animation));
    }
}
