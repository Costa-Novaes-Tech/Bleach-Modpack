package com.bleachmod.client.model;

import com.bleachmod.Reference;
import com.bleachmod.common.ZanpakutoCarry;
import com.bleachmod.common.data.PlayerCapability;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import java.util.WeakHashMap;

/** Cosmetic transitions only: switching slots never delays or grants an attack. */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT)
public final class ZanpakutoMotion {
    private static final WeakHashMap<AbstractClientPlayer, Motion> MOTIONS = new WeakHashMap<>();
    public static final class Motion extends com.bleachmod.common.visual.ZanpakutoDraw { }
    public static Motion motion(AbstractClientPlayer player) {
        var motion = MOTIONS.computeIfAbsent(player, ignored -> new Motion());
        PlayerCapability.get(player).ifPresent(data -> {
            int state = player == Minecraft.getInstance().player ? ZanpakutoCarry.state(player, data) : data.getCharacter().getCarryState();
            motion.update(state, data.getCharacter().getZanpakutoIdentity(), player.tickCount);
        });
        return motion;
    }
    public static void clear() { MOTIONS.clear(); ZanpakutoGuardPose.clear(); }
    private static final HumanoidModel.ArmPose DRAW = HumanoidModel.ArmPose.create("BLEACH_ZANPAKUTO_DRAW", false, (model, entity, arm) -> {
        if (!(entity instanceof AbstractClientPlayer player)) return;
        var motion = motion(player); float p = motion.progress(player.tickCount, 0);
        var part = arm == HumanoidArm.RIGHT ? model.rightArm : model.leftArm;
        if (motion.previous >= 2) { part.xRot = part.xRot * .5F - .314F + .9F * (1 - p); part.yRot += (arm == HumanoidArm.RIGHT ? .8F : -.8F) * (1 - p); }
        else { float reach = (float) Math.sin(p * Math.PI); part.xRot += .7F * reach; part.yRot += (arm == HumanoidArm.RIGHT ? .8F : -.8F) * reach; }
    });
    @SubscribeEvent public static void beforePlayer(RenderLivingEvent.Pre<?, ?> event) {
        if (!(event.getEntity() instanceof AbstractClientPlayer player) || !(event.getRenderer().getModel() instanceof PlayerModel<?> model)) return;
        if (ZanpakutoGuardPose.engaged(player, event.getPartialTick())) return;
        var motion = motion(player);
        if (motion.progress(player.tickCount, event.getPartialTick()) >= 1 || player.isInvisible() || player.isSpectator()) return;
        HumanoidArm arm = motion.hand == 2 ? player.getMainArm() : player.getMainArm().getOpposite();
        if (arm == HumanoidArm.RIGHT) model.rightArmPose = DRAW; else model.leftArmPose = DRAW;
    }
    @SubscribeEvent public static void firstPerson(RenderHandEvent event) {
        var player = Minecraft.getInstance().player; if (player == null) return;
        if (ZanpakutoGuardPose.engaged(player, event.getPartialTick())) return;
        var motion = motion(player); float p = motion.progress(player.tickCount, event.getPartialTick());
        if (p >= 1 || motion.previous == 0 || (motion.hand == 2) != (event.getHand() == net.minecraft.world.InteractionHand.MAIN_HAND)) return;
        float reach = motion.previous >= 2 ? 1 - p : (float) Math.sin(p * Math.PI);
        event.getPoseStack().translate(0, -.35 * reach, .25 * reach);
        HumanoidArm arm = motion.hand == 2 ? player.getMainArm() : player.getMainArm().getOpposite();
        event.getPoseStack().mulPose(Axis.ZP.rotationDegrees((arm == HumanoidArm.RIGHT ? -35 : 35) * reach));
    }
}
