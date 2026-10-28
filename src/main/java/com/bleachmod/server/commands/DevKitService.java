package com.bleachmod.server.commands;

import com.bleachmod.Reference;
import com.bleachmod.common.data.*;
import com.bleachmod.common.evolution.FormRegistry;
import com.bleachmod.common.network.NetworkHandler;
import com.bleachmod.common.network.SyncHelper;
import com.bleachmod.common.network.s2c.AppearanceSyncS2C;
import com.bleachmod.common.quincy.SprengerNodeEntity;
import com.bleachmod.common.technique.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;

/** Operator laboratory only. Never called by normal acquisition or equipment handlers. */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public final class DevKitService {
    public static final List<String> IDENTITIES = List.of("asauchi", "ryujin_jakka", "hyorinmaru", "senbonzakura", "benihime", "nozarashi");
    public static final int TEST_POINTS = 10_000;
    // Session state, deliberately not written into a character save or copied on death.
    private static final Set<UUID> ALL = new HashSet<>();
    private DevKitService() { }

    public static boolean allEnabled(ServerPlayer player) { return ALL.contains(player.getUUID()); }

    public static void requireCharacter(PlayerData data) {
        if (!data.getStatus().hasCreatedCharacter()) throw new IllegalArgumentException("Confirme a criação do personagem antes de preparar um kit de teste.");
    }

    private static void requireResettable(ServerPlayer player) {
        // Preserve the approved, non-interruptible Zaraki implementation.
        if (NozarashiReleaseEffects.inCinematic(player)) throw new IllegalArgumentException("Aguarde a transformação da Nozarashi terminar antes de trocar ou limpar o kit.");
    }

    public static void resetCombat(ServerPlayer player, PlayerData data, boolean clearCooldowns) {
        requireResettable(player);
        ReleaseAnimationService.cancel(player);
        BenihimeEntranceService.cancel(player);
        HyorinmaruCinematicService.cancel(player);
        SenbonzakuraCinematicService.cancel(player);
        RyujinBankaiEntranceService.cancel(player);
        RyujinTechniqueService.cancel(player); // Cancels owned signature, elemental, Beni, Senbon, ice, Nozarashi and flame-wave actions.
        MasteryPracticeService.cancelIntent(player);
        // Only already-loaded, owned formation nodes. No world-wide AABB scan or chunk loading.
        for (var level : player.getServer().getAllLevels())
            for (var entity : level.getAllEntities())
                if (entity instanceof SprengerNodeEntity node && node.getOwnerUUID().filter(player.getUUID()::equals).isPresent()) node.discard();
        data.getQuincy().resetTransientState();
        data.getGuard().release();
        data.getStatus().clearTransformationState();
        data.getStatus().clearCombat();
        if (clearCooldowns) data.getStatus().clearTechniqueTestState();
        data.getResources().setActionCharge(0);
        player.stopUsingItem();
        player.setSprinting(false);
        player.setDeltaMovement(Vec3.ZERO);
        player.hurtMarked = true;
        var reset = new CompoundTag(); reset.putBoolean("devCombatReset", true);
        NetworkHandler.sendToTrackingAndSelf(new AppearanceSyncS2C(player.getId(), reset), player);
    }

    /** Equip without deleting an unrelated hand item, accumulating duplicate weapons or touching the offhand. */
    public static void equip(ServerPlayer player, Item weapon) {
        var inventory = player.getInventory();
        ItemStack hand = player.getMainHandItem();
        if (hand.is(weapon)) return;
        for (int slot = 0; slot < 36; slot++) {
            ItemStack existing = inventory.getItem(slot);
            if (existing.is(weapon)) {
                inventory.setItem(slot, hand);
                player.setItemInHand(net.minecraft.world.InteractionHand.MAIN_HAND, existing);
                inventory.setChanged();
                return;
            }
        }
        if (!hand.isEmpty()) {
            int empty = inventory.getFreeSlot();
            if (empty < 0) throw new IllegalArgumentException("Inventário cheio: libere um espaço para preservar o item da mão antes de trocar o kit.");
            inventory.setItem(empty, hand);
        }
        player.setItemInHand(net.minecraft.world.InteractionHand.MAIN_HAND, new ItemStack(weapon));
        inventory.setChanged();
    }

    private static Item weapon(String identity) {
        if (!IDENTITIES.contains(identity)) throw new IllegalArgumentException("Zanpakutō desconhecida. Use o autocomplete de /bleachdev kit shinigami.");
        Item result = net.minecraftforge.registries.ForgeRegistries.ITEMS.getValue(Reference.id(identity));
        if (result == null) throw new IllegalArgumentException("O item do kit não está registrado: " + identity);
        return result;
    }

    private static void validateInventory(ServerPlayer player, Item weapon) {
        if (player.getMainHandItem().isEmpty() || player.getMainHandItem().is(weapon) || player.getInventory().getFreeSlot() >= 0) return;
        for (int slot = 0; slot < 36; slot++) if (player.getInventory().getItem(slot).is(weapon)) return;
        throw new IllegalArgumentException("Inventário cheio: libere um espaço antes de trocar o kit; nenhum item foi removido.");
    }

    public static void bindLegacy(ServerPlayer player, PlayerData data, String identity) {
        requireCharacter(data); requireResettable(player);
        if (!Reference.RACE_SHINIGAMI.equals(data.getCharacter().getRace())) throw new IllegalArgumentException("Use /bleachdev kit shinigami <zanpakuto> para voltar de Quincy.");
        Item item=weapon(identity); validateInventory(player,item);
        resetCombat(player,data,false);
        data.getCharacter().bindZanpakuto(identity);
        equip(player,item);
    }

    public static void shinigami(ServerPlayer player, PlayerData data, String identity) {
        requireCharacter(data); requireResettable(player);
        Item item = weapon(identity);
        validateInventory(player, item);
        // Validate required registry data BEFORE mutating the old profile.
        if (!CharacterData.ASAUCHI.equals(identity)) for (String form : List.of("shikai", "bankai"))
            if (FormRegistry.getForm("shinigami", "zanpakuto", form) == null) throw new IllegalArgumentException("As formas ainda não foram carregadas.");
        resetCombat(player, data, true);
        data.getCharacter().setRace(Reference.RACE_SHINIGAMI);
        data.updateTransformationSkillLimits(Reference.RACE_SHINIGAMI);
        data.getCharacter().bindZanpakuto(identity);
        equip(player, item);
        prepareForms(data);
        if (allEnabled(player)) maximize(data);
        finish(player, data);
    }

    public static void quincy(ServerPlayer player, PlayerData data) {
        requireCharacter(data); requireResettable(player);
        Item item = com.bleachmod.init.ModItems.QUINCY_BOW.get();
        validateInventory(player, item);
        resetCombat(player, data, true);
        data.getCharacter().setRace(Reference.RACE_QUINCY);
        // Archive zanpakuto identity/progress, but never expose its form as an active Quincy transformation.
        data.getCharacter().setActiveForm("", "");
        data.getCharacter().setSelectedForm("", "");
        equip(player, item);
        if (allEnabled(player)) maximize(data);
        finish(player, data);
    }

    private static void prepareForms(PlayerData data) {
        var character = data.getCharacter();
        character.unlockForm("sealed");
        if (CharacterData.ASAUCHI.equals(character.getZanpakutoIdentity())) return;
        data.getSkills().get("zanpakuto").setLevel(2);
        for (String form : List.of("shikai", "bankai")) {
            var def = FormRegistry.getForm("shinigami", "zanpakuto", form);
            character.unlockForm(form);
            character.setMastery("zanpakuto", form, Math.max(character.getMastery("zanpakuto", form),
                    Math.min(def.getMaxMastery(), def.getAllowFreeTransformOnMastery())));
            // unlockOnMastery belongs to the PREVIOUS form named by formRequisite, not this form.
            if (def.getFormRequisite()!=null) for (String token : def.getFormRequisite().split(",")) {
                String[] prerequisite=token.trim().split("\\.",2);
                if(prerequisite.length!=2)continue;
                var previous=FormRegistry.getForm("shinigami",prerequisite[0],prerequisite[1]);
                if(previous!=null)character.setMastery(prerequisite[0],prerequisite[1],
                        Math.max(character.getMastery(prerequisite[0],prerequisite[1]),Math.min(previous.getMaxMastery(),def.getUnlockOnMastery())));
            }
        }
        character.setSelectedForm("zanpakuto", "bankai");
    }

    /** Permanent administrative grants scoped to the selected kit; no attributes or stamina changes. */
    public static void maximize(PlayerData data) {
        requireCharacter(data);
        if (Reference.RACE_SHINIGAMI.equals(data.getCharacter().getRace())) {
            prepareForms(data);
            var character = data.getCharacter();
            for (String form : List.of("sealed", "shikai", "bankai")) {
                if (CharacterData.ASAUCHI.equals(character.getZanpakutoIdentity()) && !form.equals("sealed")) continue;
                var def = FormRegistry.getForm("shinigami", "zanpakuto", form);
                if (def == null) continue;
                character.unlockForm(form);
                character.setMastery("zanpakuto", form, def.getMaxMastery());
                for (int slot = 1; slot <= 4; slot++) {
                    var technique = TechniqueDefinitions.find(character.getZanpakutoIdentity(), form, slot);
                    if (technique.isPresent() && !technique.get().id().equals("unavailable")) {
                        character.unlockTechnique(form, slot);
                        character.addTechniqueMastery(form, slot, 100);
                    }
                }
            }
        } else if (!Reference.RACE_QUINCY.equals(data.getCharacter().getRace())) {
            throw new IllegalArgumentException("Selecione um kit Shinigami ou Quincy primeiro.");
        }
        if(Reference.RACE_QUINCY.equals(data.getCharacter().getRace()))data.getQuincy().maximizeMastery();
        // Each kit keeps its own progress; no Shikai/Bankai granted to Quincy.
        float points = data.getResources().getTrainingPoints();
        if (points < TEST_POINTS) data.getResources().addTrainingPoints(TEST_POINTS - points);
    }

    public static void cheatsAll(ServerPlayer player, PlayerData data) {
        requireCharacter(data); requireResettable(player);
        maximize(data);
        resetCombat(player, data, true);
        ALL.add(player.getUUID());
        data.getStatus().setCooldownsDisabled(true);
        data.getResources().setCostsDisabled(true);
        data.getResources().fillReiatsu();
        finish(player, data);
    }

    public static void cheatsOff(ServerPlayer player, PlayerData data) {
        ALL.remove(player.getUUID());
        data.getStatus().setCooldownsDisabled(false);
        data.getResources().setCostsDisabled(false);
        finish(player, data);
    }

    public static void setCooldownCheat(ServerPlayer player, PlayerData data, boolean on) {
        ALL.remove(player.getUUID());
        data.getStatus().setCooldownsDisabled(on);
    }

    public static void setReiatsuCheat(ServerPlayer player, PlayerData data, boolean on) {
        ALL.remove(player.getUUID());
        data.getResources().setCostsDisabled(on);
    }

    public static void finish(ServerPlayer player, PlayerData data) {
        data.refreshDerivedResources();
        com.bleachmod.common.ProgressionService.applyVitality(player, data);
        SyncHelper.full(player);
        SyncHelper.appearance(player);
        player.inventoryMenu.broadcastChanges();
    }

    @SubscribeEvent public static void logout(PlayerEvent.PlayerLoggedOutEvent event) {
        ALL.remove(event.getEntity().getUUID());
        PlayerCapability.get(event.getEntity()).ifPresent(data -> {
            data.getStatus().setCooldownsDisabled(false); data.getResources().setCostsDisabled(false);
        });
    }
    @SubscribeEvent public static void clonePlayer(PlayerEvent.Clone event) { ALL.remove(event.getEntity().getUUID()); }
    @SubscribeEvent public static void stopped(ServerStoppedEvent event) { ALL.clear(); }
}
