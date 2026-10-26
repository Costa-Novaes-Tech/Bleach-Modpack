package com.bleachmod.common.network;

import com.bleachmod.common.data.PlayerCapability;
import com.bleachmod.common.data.PlayerData;
import com.bleachmod.common.network.s2c.AppearanceSyncS2C;
import com.bleachmod.common.network.s2c.PlayerSyncS2C;
import com.bleachmod.common.network.s2c.ProgressionSyncS2C;
import com.bleachmod.common.network.s2c.ResourceSyncS2C;
import com.bleachmod.common.network.s2c.SyncQuestRegistryS2C;
import com.bleachmod.common.quest.QuestRegistry;
import com.bleachmod.common.evolution.FormRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;

public final class SyncHelper {
    private SyncHelper() {
    }

    public static void full(ServerPlayer player) {
        PlayerCapability.get(player).ifPresent(data -> {
            CompoundTag tag = data.save();
            tag.put("senbonHud", com.bleachmod.common.technique.SenbonzakuraTechniqueService.hud(player));
            tag.put("benihimeHud",com.bleachmod.common.technique.BenihimeTechniqueService.hud(player));
            tag.put("guardView", data.getGuard().snapshot());
            tag.put("techniqueHud", data.getStatus().techniqueHud());
            NetworkHandler.sendToPlayer(new PlayerSyncS2C(player.getId(), tag), player);
        });
    }

    public static void resources(ServerPlayer player) {
        PlayerCapability.get(player).ifPresent(data -> {
            CompoundTag tag = new CompoundTag();
            tag.put("resources", data.getResources().save());
            tag.put("senbonHud", com.bleachmod.common.technique.SenbonzakuraTechniqueService.hud(player));
            tag.put("benihimeHud",com.bleachmod.common.technique.BenihimeTechniqueService.hud(player));
            tag.put("guardView", data.getGuard().snapshot());
            tag.put("status", data.getStatus().save());
            tag.put("techniqueHud", data.getStatus().techniqueHud());
            NetworkHandler.sendToPlayer(new ResourceSyncS2C(player.getId(), tag), player);
        });
    }

    public static void progression(ServerPlayer player) {
        PlayerCapability.get(player).ifPresent(data -> {
            CompoundTag tag = new CompoundTag();
            tag.put("skills", data.getSkills().save());
            tag.put("playerQuestData", data.getPlayerQuestData().save());
            tag.put("status", data.getStatus().save());
            NetworkHandler.sendToPlayer(new ProgressionSyncS2C(player.getId(), tag), player);
        });
    }

    public static void appearance(ServerPlayer player) {
        PlayerCapability.get(player).ifPresent(data -> {
            com.bleachmod.common.ZanpakutoCarry.update(player, data);
            CompoundTag tag = new CompoundTag();
            tag.put("character", data.getCharacter().saveAppearance());
            tag.put("releaseView", com.bleachmod.common.technique.ReleaseAnimationService.snapshot(player));
            tag.put("benihimeEntrance", com.bleachmod.common.technique.BenihimeEntranceService.snapshot(player));
            tag.put("hyorinmaruCinematic", com.bleachmod.common.technique.HyorinmaruCinematicService.snapshot(player));
            tag.put("senbonzakuraCinematic", com.bleachmod.common.technique.SenbonzakuraCinematicService.snapshot(player));
            tag.put("ryujinBankaiEntrance", com.bleachmod.common.technique.RyujinBankaiEntranceService.snapshot(player));
            tag.put("signatureView", com.bleachmod.common.technique.SignatureAnimationService.snapshot(player));
            tag.put("elementalView", com.bleachmod.common.technique.ElementalTechniqueService.snapshot(player));
            tag.put("appearance", data.getAppearance().save());
            tag.put("guardView", data.getGuard().snapshot());
            NetworkHandler.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player), new AppearanceSyncS2C(player.getId(), tag));
        });
    }

    public static void questRegistry(ServerPlayer player) {
        NetworkHandler.sendToPlayer(new SyncQuestRegistryS2C(QuestRegistry.serializeSagas(), QuestRegistry.serializeQuests(), FormRegistry.serialize()), player);
    }

    public static void appearanceTo(ServerPlayer subject, ServerPlayer observer) {
        PlayerCapability.get(subject).ifPresent(data -> {
            com.bleachmod.common.ZanpakutoCarry.update(subject, data);
            CompoundTag tag = new CompoundTag();
            tag.put("character", data.getCharacter().saveAppearance());
            tag.put("releaseView", com.bleachmod.common.technique.ReleaseAnimationService.snapshot(subject));
            tag.put("benihimeEntrance", com.bleachmod.common.technique.BenihimeEntranceService.snapshot(subject));
            tag.put("hyorinmaruCinematic", com.bleachmod.common.technique.HyorinmaruCinematicService.snapshot(subject));
            tag.put("senbonzakuraCinematic", com.bleachmod.common.technique.SenbonzakuraCinematicService.snapshot(subject));
            tag.put("ryujinBankaiEntrance", com.bleachmod.common.technique.RyujinBankaiEntranceService.snapshot(subject));
            tag.put("signatureView", com.bleachmod.common.technique.SignatureAnimationService.snapshot(subject));
            tag.put("elementalView", com.bleachmod.common.technique.ElementalTechniqueService.snapshot(subject));
            tag.put("appearance", data.getAppearance().save());
            tag.put("guardView", data.getGuard().snapshot());
            NetworkHandler.sendToPlayer(new AppearanceSyncS2C(subject.getId(), tag), observer);
        });
    }

    public static PlayerData require(ServerPlayer player) {
        return PlayerCapability.get(player).orElseThrow(() -> new IllegalStateException("Missing PlayerData for " + player.getGameProfile().getName()));
    }
}
