package com.bleachmod.common.quincy;



import com.bleachmod.common.data.PlayerCapability;

import com.bleachmod.common.network.SyncHelper;

import com.bleachmod.common.technique.ElementalTechniqueService;

import com.bleachmod.common.technique.ReleaseAnimationService;

import net.minecraft.network.chat.Component;

import net.minecraft.server.level.ServerPlayer;

import net.minecraft.sounds.SoundEvents;

import net.minecraft.sounds.SoundSource;

import net.minecraft.world.InteractionHand;

import net.minecraft.world.InteractionResultHolder;

import net.minecraft.world.entity.LivingEntity;

import net.minecraft.world.entity.player.Player;

import net.minecraft.world.item.BowItem;

import net.minecraft.world.item.ItemStack;

import net.minecraft.world.item.UseAnim;

import net.minecraft.world.level.Level;

import net.minecraft.world.phys.Vec3;



/**

 * Uryu's Kojaku (Lone Sparrow) spirit bow.

 * Manifests pure Reishi arrows, requiring spiritual energy (PE) instead of vanilla ammunition.

 * Strictly bound to characters of the Quincy race.

 */

public class QuincyBowItem extends BowItem {

    public static final float COMMON_SHOT_PE_COST = 2.0F;

    public static final int FULL_CHARGE_TICKS = 20; // 1.0s

    public static int chargeTicks(LivingEntity player) { return !(player instanceof Player p)?FULL_CHARGE_TICKS:PlayerCapability.get(p).map(d->d.getQuincy().isVollstandigActive()?QuincyVollstandigService.CHARGE:FULL_CHARGE_TICKS).orElse(FULL_CHARGE_TICKS); }
    public static float shotCost(ServerPlayer p) { return QuincyVollstandigService.active(p)?QuincyVollstandigService.VOLLEY_COST:COMMON_SHOT_PE_COST; }
    public static final int MIN_CHARGE_TICKS = 5;   // 0.25s



    public QuincyBowItem() {

        super(new Properties().stacksTo(1));

    }



    @Override

    public int getMaxDamage(ItemStack stack) {

        return 0;

    }



    @Override

    public boolean isDamageable(ItemStack stack) {

        return false;

    }



    @Override

    public boolean isBarVisible(ItemStack stack) {

        return false;

    }



    @Override

    public UseAnim getUseAnimation(ItemStack stack) {

        return UseAnim.BOW;

    }



    @Override

    public int getUseDuration(ItemStack stack) {

        return 72000;

    }



    @Override

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

        ItemStack stack = player.getItemInHand(hand);

        if (!player.isAlive() || player.isSpectator()) {

            return InteractionResultHolder.fail(stack);

        }



        if (player instanceof ServerPlayer serverPlayer) {

            var data = PlayerCapability.get(serverPlayer).resolve().orElse(null);

            if (data == null || !QuincyTechniqueService.isQuincy(data)) {

                serverPlayer.displayClientMessage(Component.translatable("message.bleachmod.quincy.race_required"), true);

                return InteractionResultHolder.fail(stack);

            }

            if (data.getStatus().isDodging() || data.getGuard().active() || data.getGuard().broken()

                    || data.getStatus().isActionCharging() || ReleaseAnimationService.busy(serverPlayer)

                    || ElementalTechniqueService.busy(serverPlayer) || QuincyVollstandigService.preparing(serverPlayer)) {

                return InteractionResultHolder.fail(stack);

            }

            if (!data.getResources().canAffordReiatsu(shotCost(serverPlayer))) {

                serverPlayer.displayClientMessage(Component.translatable("message.bleachmod.quincy.low_reiatsu"), true);

                return InteractionResultHolder.fail(stack);

            }

        }

        player.startUsingItem(hand);

        return InteractionResultHolder.consume(stack);

    }



    @Override

    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {

        if (!(entity instanceof Player player) || !player.isAlive() || player.isSpectator()) {

            return;

        }



        int useTicks = getUseDuration(stack) - timeLeft;

        if (useTicks < MIN_CHARGE_TICKS) {

            // Draw cancelled or too weak to loose

            return;

        }



        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {

            PlayerCapability.get(serverPlayer).ifPresent(data -> {

                if (!QuincyTechniqueService.isQuincy(data)) {

                    return;

                }

                if (data.getStatus().isDodging() || data.getGuard().active() || data.getGuard().broken()

                        || data.getStatus().isActionCharging() || ReleaseAnimationService.busy(serverPlayer)

                        || ElementalTechniqueService.busy(serverPlayer) || QuincyVollstandigService.preparing(serverPlayer)) {

                    return;

                }

                if (!data.getResources().consumeReiatsu(shotCost(serverPlayer))) {

                    serverPlayer.displayClientMessage(Component.translatable("message.bleachmod.quincy.low_reiatsu"), true);

                    return;

                }



                float power = Math.min(1.0F, (float) useTicks / (float) chargeTicks(serverPlayer));

                com.bleachmod.common.technique.MasteryPracticeService.noteIntent(serverPlayer);
                QuincyArrowEntity arrow = new QuincyArrowEntity(level, serverPlayer);

                if(QuincyVollstandigService.active(serverPlayer)) arrow.configureVollstandig(power); else arrow.configureCommon(power);



                Vec3 look = serverPlayer.getLookAngle();

                // Velocity 3.0F travels ~64 blocks in straight flight without gravity, stable precision 0.0F divergence
                arrow.shoot(look.x, look.y, look.z, (QuincyVollstandigService.active(serverPlayer)?3.6F:3.0F) * power, 0.0F);

                if(level.addFreshEntity(arrow) && QuincyVollstandigService.active(serverPlayer)){QuincyVollstandigService.track(serverPlayer,arrow);QuincyVollstandigService.satellites(serverPlayer);}



                level.playSound(null, serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(),

                        SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.3F / (power * 0.4F + 0.8F));



                SyncHelper.resources(serverPlayer);

            });

        }

    }

}

