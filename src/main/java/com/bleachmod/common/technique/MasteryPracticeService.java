package com.bleachmod.common.technique;

import com.bleachmod.Reference;
import com.bleachmod.common.data.*;
import com.bleachmod.common.evolution.TransformationsHelper;
import com.bleachmod.common.network.SyncHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import java.util.*;

/** Server-authoritative practice: no passive XP, limited repetition, and no XP in test mode. */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public final class MasteryPracticeService {
    public static final double FORM_GAIN = 0.5, TECHNIQUE_GAIN = 1;
    public static final int GLOBAL_INTERVAL = 20, TARGET_INTERVAL = 100, INTENT_DURATION = 100;
    private record Context(ServerPlayer owner, int slot) { }
    private static final ThreadLocal<Context> CONTEXT = new ThreadLocal<>();
    private static final Map<UUID, Practice> PRACTICE = new HashMap<>();
    private static final class Practice {
        final ServerPlayer owner;
        long intentUntil = Long.MIN_VALUE, next = Long.MIN_VALUE;
        final Map<UUID, Long> targets = new HashMap<>();
        Practice(ServerPlayer owner) { this.owner = owner; }
    }
    private MasteryPracticeService() { }
    private static Practice state(ServerPlayer player) {
        Practice state = PRACTICE.get(player.getUUID());
        if (state == null || state.owner != player) { state = new Practice(player); PRACTICE.put(player.getUUID(), state); }
        return state;
    }
    public static void noteIntent(ServerPlayer player) { state(player).intentUntil = player.getServer().getTickCount() + INTENT_DURATION; }
    public static void cancelIntent(ServerPlayer player) { Practice state = PRACTICE.get(player.getUUID()); if (state != null) state.intentUntil = Long.MIN_VALUE; }
    public static boolean hurt(ServerPlayer owner, LivingEntity target, DamageSource source, float damage, int slot) {
        Context previous = CONTEXT.get(); CONTEXT.set(new Context(owner, slot));
        try { return target.hurt(source, damage); }
        finally { if (previous == null) CONTEXT.remove(); else CONTEXT.set(previous); }
    }
    public static void damage(ServerPlayer owner, LivingEntity target) {
        Context context = CONTEXT.get();
        if (context != null && (context.owner != owner || context.slot == 0)) return;
        if (context == null) noteIntent(owner);
        int slot = context == null ? 0 : context.slot;
        if (context == null && TechniqueService.isRyujinJakkaEquipped(owner)
                && PlayerCapability.get(owner).map(TechniqueService::isIgnitionActive).orElse(false)) slot = 1;
        credit(owner, target, slot);
    }
    public static void control(ServerPlayer owner, LivingEntity target, int slot) { credit(owner, target, slot); }
    public static boolean isTechniqueDamage() { return CONTEXT.get()!=null; }
    public static void healing(ServerPlayer owner,LivingEntity target,int slot,float restored) {
        if(restored>0 && Float.isFinite(restored))credit(owner,target,slot,true);
    }
    public static void defense(ServerPlayer owner, LivingEntity attacker) {
        if (IceArmorService.active(owner)) credit(owner, attacker, 3);
    }
    public static void defense(ServerPlayer owner, LivingEntity attacker, DamageSource source) {
        if (IceArmorService.active(owner)) defense(owner,attacker);
        else if (SenbonzakuraTechniqueService.protection(owner,source)>0) credit(owner,attacker,4);
    }
    private static void credit(ServerPlayer owner, LivingEntity target, int slot) {
        credit(owner,target,slot,false);
    }
    private static void credit(ServerPlayer owner,LivingEntity target,int slot,boolean healing) {
        if (!owner.isAlive() || owner.isSpectator() || target.isInvulnerable())return;
        if(healing ? !BenihimeTechniqueService.healable(owner,target)
                : !(target instanceof Enemy || target instanceof ServerPlayer) || !TechniqueTargets.allowed(owner,target))return;
        PlayerCapability.get(owner).ifPresent(data -> {
            var character = data.getCharacter();
            if (!data.getStatus().hasCreatedCharacter() || data.getStatus().areCooldownsDisabled() || data.getResources().areCostsDisabled()
                    || !"shinigami".equals(character.getRace()) || !"zanpakuto".equals(character.getActiveFormGroup())
                    || !TechniqueProgression.equipped(owner, data) || !TransformationsHelper.isUnlocked(data, "zanpakuto", character.getActiveForm())
                    || (slot > 0 && !TechniqueProgression.available(character, character.getActiveForm(), slot))) return;
            Practice practice = state(owner); long now = owner.getServer().getTickCount();
            if (now > practice.intentUntil || now < practice.next || now < practice.targets.getOrDefault(target.getUUID(), Long.MIN_VALUE)) return;
            practice.targets.values().removeIf(until -> until <= now);
            if (practice.targets.size() >= 128) return;
            practice.next = now + GLOBAL_INTERVAL; practice.targets.put(target.getUUID(), now + TARGET_INTERVAL);
            var before = MasteryFeedback.snapshot(character);
            character.addMastery("zanpakuto", character.getActiveForm(), FORM_GAIN, 100);
            if (slot > 0) character.addTechniqueMastery(character.getActiveForm(), slot, TECHNIQUE_GAIN);
            TechniqueProgression.unlockEligible(character);
            for (var unlock : MasteryFeedback.gained(before, character)) {
                var formName = com.bleachmod.common.evolution.FormNames.of(character, unlock.form());
                owner.sendSystemMessage(unlock.slot() == 0
                        ? net.minecraft.network.chat.Component.translatable("message.bleachmod.mastery.form_discovered", formName)
                        : net.minecraft.network.chat.Component.translatable("message.bleachmod.mastery.slot_unlocked", unlock.slot(), formName));
            }
            SyncHelper.full(owner);
        });
    }
    @SubscribeEvent public static void logout(PlayerEvent.PlayerLoggedOutEvent event) { PRACTICE.remove(event.getEntity().getUUID()); }
    @SubscribeEvent public static void dimension(PlayerEvent.PlayerChangedDimensionEvent event) { if (event.getEntity() instanceof ServerPlayer player) cancelIntent(player); }
    @SubscribeEvent public static void death(net.minecraftforge.event.entity.living.LivingDeathEvent event) { if (event.getEntity() instanceof ServerPlayer player) cancelIntent(player); }
    @SubscribeEvent public static void form(com.bleachmod.common.events.BleachEvents.FormChangeEvent event) { cancelIntent(event.getPlayer()); }
    @SubscribeEvent public static void stopped(ServerStoppedEvent event) { PRACTICE.clear(); CONTEXT.remove(); }
}
