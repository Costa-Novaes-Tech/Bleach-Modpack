package com.bleachmod.common.technique;

import com.bleachmod.Reference;
import com.bleachmod.common.data.*;
import com.bleachmod.common.events.BleachEvents;
import com.bleachmod.common.network.SyncHelper;
import com.bleachmod.common.visual.BerserkVisuals;
import com.bleachmod.init.ModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import java.util.*;

/**
 * Zaraki Kenpachi kit. Sealed: brute swordsmanship and the eyepatch limiter. Shikai: the Nozarashi cleaver.
 * Bankai (Berserk Mode): charges, combos and a world-splitting cut that wounds its wielder.
 * Runtime state is transient and cleared on form change, death, logout, dimension change and server stop.
 */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public final class NozarashiTechniqueService {
    public static final float EYEPATCH_BONUS = .30F, THIRST_STACK = .05F, THIRST_MAX = .40F, THIRST_LIFESTEAL = .15F;
    public static final int THIRST_TICKS = 200, CHARGE_TICKS = 10, CLEAVER_WINDUP = 20, LEAP_TIMEOUT = 50;
    public static final double CHARGE_SPEED = 1.4, CLEAVER_RANGE = 30;
    public static final float CLEAVER_DAMAGE = 45, CLEAVER_RECOIL = .15F;

    private static final class Runtime {
        boolean eyepatch;
        long thirstUntil; float thirst;
        int leapTicks = -1;
        int chargeTicks; Vec3 chargeDirection = Vec3.ZERO; final Set<UUID> chargeHits = new HashSet<>();
        int furyHits, furyDelay;
        int cleaverTicks = -1; Vec3 cleaverDirection = Vec3.ZERO;
        boolean idle() { return !eyepatch && thirst == 0 && thirstUntil == 0 && leapTicks < 0 && chargeTicks <= 0 && furyHits <= 0 && cleaverTicks < 0; }
    }
    private static final Map<UUID, Runtime> ACTIVE = new HashMap<>();
    /** Landing grace after Crushing Leap, kept apart from the runtime so the slam can end it. */
    private static final Map<UUID, Long> FALL_GRACE = new HashMap<>();
    private NozarashiTechniqueService() { }
    private static Runtime runtime(ServerPlayer player) { return ACTIVE.computeIfAbsent(player.getUUID(), id -> new Runtime()); }

    public static boolean equipped(ServerPlayer p, PlayerData d) {
        return CharacterData.NOZARASHI.equals(d.getCharacter().getZanpakutoIdentity()) && p.getMainHandItem().is(ModItems.NOZARASHI.get());
    }
    public static boolean eyepatchActive(ServerPlayer player) { var r = ACTIVE.get(player.getUUID()); return r != null && r.eyepatch; }
    public static boolean charging(ServerPlayer player) { var r = ACTIVE.get(player.getUUID()); return r != null && (r.chargeTicks > 0 || r.cleaverTicks >= 0); }

    /** Extra zanjutsu multiplier from the eyepatch limiter and Battle Thirst stacks. */
    public static float damageBonus(ServerPlayer player, PlayerData data) {
        var r = ACTIVE.get(player.getUUID());
        if (r == null || !equipped(player, data)) return 0;
        return (r.eyepatch ? EYEPATCH_BONUS : 0) + (player.level().getGameTime() < r.thirstUntil ? r.thirst : 0);
    }

    public static void executeSlot(ServerPlayer player, PlayerData data, int slot) {
        if (NozarashiReleaseEffects.inCinematic(player)) return;
        if (!equipped(player, data) || !Reference.GROUP_ZANPAKUTO.equals(data.getCharacter().getActiveFormGroup())) {
            player.displayClientMessage(Component.translatable("message.bleachmod.technique.requires_nozarashi"), true); return;
        }
        String form = data.getCharacter().getActiveForm();
        var definition = TechniqueDefinitions.require(CharacterData.NOZARASHI, form, slot);
        boolean sealed = Reference.FORM_SEALED.equals(form), bankai = Reference.FORM_BANKAI.equals(form);
        Runtime r = runtime(player);
        // The eyepatch is a toggle: removing it costs nothing up front, putting it back is always allowed.
        if (sealed && slot == 3 && r.eyepatch) { setEyepatch(player, r, false); return; }
        if (charging(player)) return;
        StatusData status = data.getStatus();
        int cooldown = switch (slot) {
            case 1 -> status.getTechniqueSlot1CooldownTicks(); case 2 -> status.getTechniqueSlot2CooldownTicks();
            case 3 -> status.getTechniqueSlot3CooldownTicks(); default -> status.getTechniqueSlot4CooldownTicks();
        };
        if (cooldown > 0) {
            player.displayClientMessage(Component.translatable("message.bleachmod.technique.cooldown", (cooldown + 19) / 20), true); return;
        }
        if (sealed && slot == 4 && data.getResources().isStaminaExhausted()) {
            player.displayClientMessage(Component.translatable("message.bleachmod.nozarashi.kendo_exhausted"), true); return;
        }
        if (!data.getResources().consumeReiatsu(definition.cost())) {
            player.displayClientMessage(Component.translatable("message.bleachmod.technique.no_reiatsu", (int) definition.cost()), true); return;
        }
        switch (slot) {
            case 1 -> status.setTechniqueSlot1CooldownTicks(definition.cooldownTicks());
            case 2 -> status.setTechniqueSlot2CooldownTicks(definition.cooldownTicks());
            case 3 -> status.setTechniqueSlot3CooldownTicks(definition.cooldownTicks());
            default -> status.setTechniqueSlot4CooldownTicks(definition.cooldownTicks());
        }
        Vec3 forward = Vec3.directionFromRotation(0, player.getYRot());
        if (sealed) switch (slot) {
            case 1 -> brutalCut(player, forward);
            case 2 -> spiritualPressure(player, 6, 60, 1, false);
            case 3 -> setEyepatch(player, r, true);
            default -> kendo(player, data, forward);
        } else if (!bankai) switch (slot) {
            case 1 -> nozarashiCut(player, forward);
            case 2 -> crushingLeap(player, r, forward);
            case 3 -> battleThirst(player, r);
            default -> drinkNozarashi(player, forward);
        } else switch (slot) {
            case 1 -> { r.chargeTicks = CHARGE_TICKS; r.chargeDirection = forward; r.chargeHits.clear(); BerserkVisuals.trail(player);
                NozarashiReleaseEffects.roar(player, 1.2F); NozarashiReleaseEffects.shake(player, 3); }
            case 2 -> { r.furyHits = 3; r.furyDelay = 0; }
            case 3 -> spiritualPressure(player, 10, 100, 2, true);
            default -> { r.cleaverTicks = CLEAVER_WINDUP; r.cleaverDirection = forward;
                player.serverLevel().playSound(null, player.blockPosition(), SoundEvents.WARDEN_SONIC_CHARGE, player.getSoundSource(), 1.2F, .7F); }
        }
        player.displayClientMessage(Component.translatable(definition.nameKey()), true);
        SyncHelper.resources(player);
    }

    private static boolean inCone(ServerPlayer p, LivingEntity target, Vec3 origin, Vec3 forward, double range, double halfWidth, double widen) {
        Vec3 delta = target.position().subtract(origin);
        double ahead = delta.dot(forward), lateral = Math.abs(delta.x * forward.z - delta.z * forward.x);
        return ahead >= -.5 && ahead <= range && lateral <= halfWidth + Math.max(0, ahead) * widen
                && Math.abs(delta.y) <= 3.5 && p.hasLineOfSight(target);
    }
    private static List<LivingEntity> targets(ServerPlayer p, Vec3 center, double radius) {
        return p.serverLevel().getEntitiesOfClass(LivingEntity.class, p.getBoundingBox().move(center.subtract(p.position())).inflate(radius),
                e -> TechniqueTargets.allowed(p, e));
    }
    /** Set while a technique deals damage, so melee hammer impacts do not repeat on technique hits. */
    private static final ThreadLocal<Boolean> IN_TECHNIQUE = ThreadLocal.withInitial(() -> false);
    private static void hit(ServerPlayer p, LivingEntity target, float damage, int slot, Vec3 push, double lift) {
        IN_TECHNIQUE.set(true);
        try {
            if (MasteryPracticeService.hurt(p, target, p.damageSources().playerAttack(p), damage, slot)) {
                target.push(push.x, lift, push.z); target.hurtMarked = true;
            }
        } finally { IN_TECHNIQUE.set(false); }
    }
    /** Every plain blow with Nozarashi lands like a hammer: crater, rubble and a dull metallic crash. */
    public static void meleeHammer(ServerPlayer p, PlayerData d, LivingEntity target) {
        if (IN_TECHNIQUE.get() || !equipped(p, d) || !Reference.GROUP_ZANPAKUTO.equals(d.getCharacter().getActiveFormGroup())) return;
        String form = d.getCharacter().getActiveForm();
        float strength = Reference.FORM_BANKAI.equals(form) ? 2.2F : Reference.FORM_SHIKAI.equals(form) ? 1.6F : 1F;
        BerserkVisuals.impact(p, target.position(), strength);
        BerserkTerrain.crater(p, target.position(), BerserkTerrain.hammerRadius(form), BerserkTerrain.hammerDepth(form));
        p.serverLevel().playSound(null, target.blockPosition(), SoundEvents.ANVIL_LAND, p.getSoundSource(), .5F + strength * .25F, .5F);
        if (strength > 2) { p.serverLevel().playSound(null, target.blockPosition(), SoundEvents.GENERIC_EXPLODE, p.getSoundSource(), .6F, .8F); NozarashiReleaseEffects.shake(p, 2); }
    }

    // ---- Sealed ----
    private static void brutalCut(ServerPlayer p, Vec3 forward) {
        for (LivingEntity t : targets(p, p.position(), 5)) if (inCone(p, t, p.position(), forward, 4.5, .8, Math.tan(Math.toRadians(45))))
            hit(p, t, 6, 1, forward.scale(.6), .2);
        p.swing(net.minecraft.world.InteractionHand.MAIN_HAND, true);
        BerserkVisuals.crescent(p, forward, 3, 3, false);
        BerserkVisuals.impact(p, p.position().add(forward.scale(3)), 1.2F);
        BerserkTerrain.crater(p, p.position().add(forward.scale(3)), 1.5, 1);
        p.serverLevel().playSound(null, p.blockPosition(), SoundEvents.ANVIL_LAND, p.getSoundSource(), 1, .55F);
    }
    private static void spiritualPressure(ServerPlayer p, double radius, int ticks, int amplifier, boolean roar) {
        for (LivingEntity t : targets(p, p.position(), radius)) {
            if (t.distanceToSqr(p) > radius * radius) continue;
            t.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, ticks, amplifier), p);
            t.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, ticks, amplifier - 1), p);
            Vec3 away = t.position().subtract(p.position()).normalize();
            if (roar) hit(p, t, 4, 3, away.scale(1.1), .35);
            else { t.push(away.x * .5, .1, away.z * .5); t.hurtMarked = true; MasteryPracticeService.control(p, t, 2); }
        }
        for (int wave = 1; wave <= (roar ? 4 : 2); wave++) BerserkVisuals.ring(p, p.position(), radius * wave / (roar ? 4 : 2), roar ? 64 : 36, true);
        if (roar) { NozarashiReleaseEffects.roar(p, 2.5F); NozarashiReleaseEffects.shake(p, 7); BerserkVisuals.pillar(p, Vec3.directionFromRotation(0, p.getYRot()), 4); }
        else { p.serverLevel().playSound(null, p.blockPosition(), SoundEvents.WARDEN_HEARTBEAT, p.getSoundSource(), 2, .7F); NozarashiReleaseEffects.shake(p, 2); }
    }
    private static void setEyepatch(ServerPlayer p, Runtime r, boolean active) {
        r.eyepatch = active;
        if (active) {
            BerserkVisuals.ring(p, p.position(), 2.5, 40, true);
            BerserkVisuals.ring(p, p.position(), 5, 56, true);
            p.serverLevel().playSound(null, p.blockPosition(), SoundEvents.WARDEN_HEARTBEAT, p.getSoundSource(), 2.5F, .6F);
            p.serverLevel().playSound(null, p.blockPosition(), SoundEvents.WARDEN_ANGRY, p.getSoundSource(), 1.5F, .7F);
            NozarashiReleaseEffects.shake(p, 3);
        }
        p.displayClientMessage(Component.translatable("message.bleachmod.nozarashi.eyepatch." + (active ? "off" : "on")), true);
    }
    private static void kendo(ServerPlayer p, PlayerData d, Vec3 forward) {
        float spent = d.getResources().spendAllStamina();
        float damage = 6 + spent * .12F;
        LivingEntity target = targets(p, p.position(), 5).stream().filter(t -> inCone(p, t, p.position(), forward, 4.5, .6, Math.tan(Math.toRadians(20))))
                .min(Comparator.comparingDouble(p::distanceToSqr)).orElse(null);
        if (target != null) hit(p, target, damage, 4, forward.scale(1.2), .4);
        p.swing(net.minecraft.world.InteractionHand.MAIN_HAND, true);
        BerserkVisuals.crescent(p, forward, 3, 4, true);
        BerserkVisuals.impact(p, p.position().add(forward.scale(3)), 1.5F + spent / 100F);
        BerserkTerrain.crater(p, p.position().add(forward.scale(3)), 1.5 + spent / 100, 2);
        p.serverLevel().playSound(null, p.blockPosition(), SoundEvents.ANVIL_LAND, p.getSoundSource(), 1.2F, .45F);
        p.serverLevel().playSound(null, p.blockPosition(), SoundEvents.GENERIC_EXPLODE, p.getSoundSource(), .8F, .7F);
        NozarashiReleaseEffects.shake(p, 4);
        p.setSprinting(false);
    }

    // ---- Shikai: Nozarashi ----
    private static void nozarashiCut(ServerPlayer p, Vec3 forward) {
        for (LivingEntity t : targets(p, p.position().add(forward.scale(6)), 7)) if (inCone(p, t, p.position(), forward, 12, 1.2, 0))
            hit(p, t, 10, 1, forward.scale(.3), .45);
        p.swing(net.minecraft.world.InteractionHand.MAIN_HAND, true);
        BerserkVisuals.crescent(p, forward, 2.5, 5, true);
        BerserkVisuals.fissure(p, p.position(), forward, 12, false);
        BerserkVisuals.impact(p, p.position().add(forward.scale(2.5)), 1.8F);
        BerserkTerrain.trench(p, p.position().add(forward), forward, 12, .9, 2);
        BerserkTerrain.crater(p, p.position().add(forward.scale(2.5)), 2, 2);
        p.serverLevel().playSound(null, p.blockPosition(), SoundEvents.GENERIC_EXPLODE, p.getSoundSource(), 1.2F, .6F);
        NozarashiReleaseEffects.shake(p, 5);
    }
    private static void crushingLeap(ServerPlayer p, Runtime r, Vec3 forward) {
        p.setDeltaMovement(forward.x * 1.1, 1.05, forward.z * 1.1); p.hurtMarked = true;
        r.leapTicks = 0;
        p.serverLevel().playSound(null, p.blockPosition(), SoundEvents.RAVAGER_STEP, p.getSoundSource(), 1.5F, .6F);
    }
    private static void slam(ServerPlayer p, boolean bankai) {
        double radius = bankai ? 6 : 4.5;
        for (LivingEntity t : targets(p, p.position(), radius)) {
            if (t.distanceToSqr(p) > radius * radius) continue;
            hit(p, t, bankai ? 12 : 9, 2, t.position().subtract(p.position()).normalize().scale(.6), .6);
        }
        p.swing(net.minecraft.world.InteractionHand.MAIN_HAND, true);
        BerserkVisuals.impact(p, p.position(), 2.5F);
        BerserkTerrain.crater(p, p.position(), radius * .7, 2);
        BerserkVisuals.ring(p, p.position(), radius * .5, 40, true);
        BerserkVisuals.ring(p, p.position(), radius, 56, true);
        p.serverLevel().playSound(null, p.blockPosition(), SoundEvents.GENERIC_EXPLODE, p.getSoundSource(), 1.6F, .5F);
        NozarashiReleaseEffects.shake(p, 6);
    }
    private static void battleThirst(ServerPlayer p, Runtime r) {
        r.thirstUntil = p.level().getGameTime() + THIRST_TICKS; r.thirst = 0;
        BerserkVisuals.ring(p, p.position(), 2, 40, true);
        NozarashiReleaseEffects.roar(p, 1.2F);
        p.serverLevel().playSound(null, p.blockPosition(), SoundEvents.WARDEN_HEARTBEAT, p.getSoundSource(), 2, 1);
    }
    private static void drinkNozarashi(ServerPlayer p, Vec3 forward) {
        for (LivingEntity t : targets(p, p.position().add(forward.scale(5)), 6)) if (inCone(p, t, p.position(), forward, 10, 1.4, Math.tan(Math.toRadians(8))))
            hit(p, t, 22, 4, forward.scale(.8), .7);
        p.swing(net.minecraft.world.InteractionHand.MAIN_HAND, true);
        BerserkVisuals.crescent(p, forward, 6, 7, true);
        BerserkVisuals.fissure(p, p.position(), forward, 14, true);
        BerserkVisuals.impact(p, p.position().add(forward.scale(4)), 2.5F);
        p.serverLevel().playSound(null, p.blockPosition(), SoundEvents.WARDEN_SONIC_BOOM, p.getSoundSource(), 1.6F, .7F);
        BerserkTerrain.trench(p, p.position().add(forward.scale(1.5)), forward, 14, 1.3, 3);
        BerserkTerrain.crater(p, p.position().add(forward.scale(4)), 2.5, 3);
        p.serverLevel().playSound(null, p.blockPosition(), SoundEvents.GENERIC_EXPLODE, p.getSoundSource(), 1.4F, .55F);
        NozarashiReleaseEffects.shake(p, 7);
    }

    // ---- Bankai: Berserk Mode ----
    private static void furyStrike(ServerPlayer p, int index) {
        Vec3 forward = Vec3.directionFromRotation(0, p.getYRot());
        for (LivingEntity t : targets(p, p.position(), 6)) if (inCone(p, t, p.position(), forward, 5, .8, Math.tan(Math.toRadians(30))))
            hit(p, t, index == 3 ? 12 : 8, 2, forward.scale(index == 3 ? 1 : .25), index == 3 ? .5 : .1);
        BerserkVisuals.crescent(p, forward, 4, index == 3 ? 5 : 3.5, index == 3);
        BerserkVisuals.impact(p, p.position().add(forward.scale(3.5)), index == 3 ? 2.5F : 1.4F);
        BerserkTerrain.crater(p, p.position().add(forward.scale(3.5)), index == 3 ? 2.5 : 1.5, index == 3 ? 2 : 1);
        p.serverLevel().playSound(null, p.blockPosition(), SoundEvents.ANVIL_LAND, p.getSoundSource(), 1.2F, .4F + index * .1F);
        if (index == 3) { p.serverLevel().playSound(null, p.blockPosition(), SoundEvents.GENERIC_EXPLODE, p.getSoundSource(), 1.3F, .6F); NozarashiReleaseEffects.roar(p, 1); }
        NozarashiReleaseEffects.shake(p, index == 3 ? 6 : 3);
        p.swing(net.minecraft.world.InteractionHand.MAIN_HAND, true);
    }
    private static void worldCleaver(ServerPlayer p, Vec3 forward) {
        for (LivingEntity t : targets(p, p.position().add(forward.scale(CLEAVER_RANGE / 2)), CLEAVER_RANGE / 2 + 2))
            if (inCone(p, t, p.position(), forward, CLEAVER_RANGE, 2.5, 0)) hit(p, t, CLEAVER_DAMAGE, 4, forward.scale(1.2), .9);
        p.swing(net.minecraft.world.InteractionHand.MAIN_HAND, true);
        BerserkVisuals.crescent(p, forward, 8, 8, true);
        BerserkVisuals.fissure(p, p.position(), forward, CLEAVER_RANGE, true);
        BerserkVisuals.impact(p, p.position().add(forward.scale(4)), 2.5F);
        BerserkVisuals.ring(p, p.position(), 3, 48, true);
        BerserkTerrain.trench(p, p.position().add(forward.scale(1.5)), forward, CLEAVER_RANGE, 1.6, 4);
        BerserkTerrain.crater(p, p.position().add(forward.scale(4)), 3.5, 3);
        BerserkVisuals.ring(p, p.position(), 7, 64, true);
        NozarashiReleaseEffects.roar(p, 3);
        NozarashiReleaseEffects.shake(p, 10);
        p.serverLevel().playSound(null, p.blockPosition(), SoundEvents.WARDEN_SONIC_BOOM, p.getSoundSource(), 2, .5F);
        p.serverLevel().playSound(null, p.blockPosition(), SoundEvents.GENERIC_EXPLODE, p.getSoundSource(), 1.5F, .5F);
        // Canon cost: the wielder's arm breaks under the strain. Never lethal on its own.
        float recoil = Math.min(p.getMaxHealth() * CLEAVER_RECOIL, Math.max(0, p.getHealth() - 1));
        if (recoil > 0) p.hurt(p.damageSources().magic(), recoil);
        p.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 200, 1));
        p.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 200, 1));
        p.displayClientMessage(Component.translatable("message.bleachmod.nozarashi.recoil"), true);
    }

    public static void tick(ServerPlayer p, PlayerData d) {
        Runtime r = ACTIVE.get(p.getUUID());
        if (r == null) return;
        if (!p.isAlive() || p.isSpectator() || !equipped(p, d)) {
            if (r.eyepatch) setEyepatch(p, r, false);
            ACTIVE.remove(p.getUUID()); return;
        }
        long time = p.level().getGameTime();
        if (r.eyepatch) {
            if (!d.getResources().consumeReiatsu(TechniqueDefinitions.require(CharacterData.NOZARASHI, Reference.FORM_SEALED, 3).drainPerTick())) {
                setEyepatch(p, r, false);
            } else if (p.tickCount % 6 == 0) BerserkVisuals.aura(p, 1);
        }
        if (r.thirstUntil != 0 && time >= r.thirstUntil) { r.thirstUntil = 0; r.thirst = 0; }
        if (r.leapTicks >= 0) {
            r.leapTicks++;
            if (r.leapTicks > 3 && p.onGround()) { r.leapTicks = -1; FALL_GRACE.put(p.getUUID(), time + 10); slam(p, false); }
            else if (r.leapTicks > LEAP_TIMEOUT) r.leapTicks = -1;
        }
        if (r.chargeTicks > 0) {
            r.chargeTicks--;
            BerserkTerrain.bulldoze(p, r.chargeDirection);
            p.setDeltaMovement(r.chargeDirection.x * CHARGE_SPEED, Math.min(p.getDeltaMovement().y, .1), r.chargeDirection.z * CHARGE_SPEED);
            p.hurtMarked = true;
            for (LivingEntity t : targets(p, p.position(), 1.6)) if (r.chargeHits.add(t.getUUID()))
                hit(p, t, 10, 1, r.chargeDirection.scale(1.3), .5);
            if (p.tickCount % 2 == 0) BerserkVisuals.trail(p);
            if (r.chargeTicks == 0) p.setDeltaMovement(p.getDeltaMovement().multiply(.2, 1, .2));
        }
        if (r.furyHits > 0 && --r.furyDelay <= 0) { furyStrike(p, 4 - r.furyHits); r.furyHits--; r.furyDelay = 6; }
        if (r.cleaverTicks >= 0) {
            p.setDeltaMovement(0, Math.min(p.getDeltaMovement().y, 0), 0); p.hurtMarked = true;
            if (r.cleaverTicks % 2 == 0) BerserkVisuals.ring(p, p.position(), .8 + r.cleaverTicks * .2, 32, true);
            if (r.cleaverTicks % 5 == 0) BerserkVisuals.aura(p, 3);
            if (r.cleaverTicks-- == 0) { r.cleaverTicks = -1; worldCleaver(p, r.cleaverDirection); }
        }
        if (r.idle()) ACTIVE.remove(p.getUUID());
    }

    /** Battle Thirst: every blow received sharpens the next ones. */
    public static void onHurt(ServerPlayer p) {
        Runtime r = ACTIVE.get(p.getUUID());
        if (r != null && p.level().getGameTime() < r.thirstUntil) r.thirst = Math.min(THIRST_MAX, r.thirst + THIRST_STACK);
    }
    public static void onDamageDealt(ServerPlayer p, float amount) {
        Runtime r = ACTIVE.get(p.getUUID());
        if (r != null && p.level().getGameTime() < r.thirstUntil && amount > 0) p.heal(amount * THIRST_LIFESTEAL);
    }

    public static void cancel(ServerPlayer player) { ACTIVE.remove(player.getUUID()); FALL_GRACE.remove(player.getUUID()); }
    @SubscribeEvent public static void fall(LivingFallEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer p)) return;
        var r = ACTIVE.get(p.getUUID()); Long grace = FALL_GRACE.remove(p.getUUID());
        if ((r != null && r.leapTicks >= 0) || (grace != null && p.level().getGameTime() <= grace)) event.setDamageMultiplier(0);
    }
    @SubscribeEvent public static void form(BleachEvents.FormChangeEvent event) { cancel(event.getPlayer()); }
    @SubscribeEvent public static void logout(PlayerEvent.PlayerLoggedOutEvent event) { if (event.getEntity() instanceof ServerPlayer p) cancel(p); }
    @SubscribeEvent public static void dimension(PlayerEvent.PlayerChangedDimensionEvent event) { if (event.getEntity() instanceof ServerPlayer p) cancel(p); }
    @SubscribeEvent public static void death(LivingDeathEvent event) { if (event.getEntity() instanceof ServerPlayer p) cancel(p); }
    @SubscribeEvent public static void stopping(ServerStoppingEvent event) { ACTIVE.clear(); FALL_GRACE.clear(); }
}
