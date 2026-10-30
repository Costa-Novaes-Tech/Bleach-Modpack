package com.bleachmod.common.data;

import com.bleachmod.Reference;
import com.bleachmod.common.quest.PlayerQuestData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

public class PlayerData {
    private final CharacterData character = new CharacterData();
    private final AcademyData academy = new AcademyData();
    private final OriginData origin = new OriginData();
    public OriginData getOrigin() { return origin; }
    public AcademyData getAcademy() { return academy; }
    private final ResourcesData resources = new ResourcesData();
    private final SkillsData skills = new SkillsData();
    private final StatusData status = new StatusData();
    private final GuardData guard = new GuardData();
    public GuardData getGuard() { return guard; }
    private final com.bleachmod.common.appearance.AppearanceData appearance = new com.bleachmod.common.appearance.AppearanceData();
    public com.bleachmod.common.appearance.AppearanceData getAppearance() { return appearance; }
    private final PlayerQuestData playerQuestData = new PlayerQuestData();
    private boolean dataLoaded;
    private final AttributeData attributes = new AttributeData();

    public AttributeData getAttributes() { return attributes; }

    public void refreshDerivedResources() {
        resources.setMaxStamina(com.bleachmod.common.StaminaPolicy.maximum(attributes.level(AttributeData.RESISTANCE)));
        boolean wasFull = resources.isReiatsuFull();
        double derived = Reference.BASE_REIATSU + 20.0D * attributes.level(AttributeData.RESERVE);
        resources.setMaxReiatsu((float)Math.min(Float.MAX_VALUE, derived));
        if (wasFull) resources.fillReiatsu();
    }

    public double getBattlePower() {
        long totalRanks = 0;
        for (String id : AttributeData.IDS) totalRanks += attributes.level(id);
        return totalRanks * (resources.getMaxReiatsu() / 10.0D);
    }

    public CharacterData getCharacter() {
        return character;
    }

    public ResourcesData getResources() {
        return resources;
    }

    public SkillsData getSkills() {
        return skills;
    }

    public StatusData getStatus() {
        return status;
    }

    public PlayerQuestData getPlayerQuestData() {
        return playerQuestData;
    }

    public boolean isDataLoaded() {
        return dataLoaded;
    }

    public void setDataLoaded(boolean dataLoaded) {
        this.dataLoaded = dataLoaded;
    }

    public void updateTransformationSkillLimits(String race) {
        if (Reference.RACE_SHINIGAMI.equals(race) || race == null || race.isBlank()) {
            skills.registerOrUpdate(Reference.SKILL_ZANPAKUTO, Reference.ZANPAKUTO_MAX_LEVEL);
        }
    }

    public void initializeShinigami() {
        guard.reset();
        character.initializeShinigami();
        academy.startJourney();
        status.setHasCreatedCharacter(true);
        updateTransformationSkillLimits(Reference.RACE_SHINIGAMI);
        resources.setMaxReiatsu(Reference.BASE_REIATSU);
        resources.setCurrentReiatsu(Reference.BASE_REIATSU);
        resources.setActionCharge(0);
        status.clearTransientState();
        resources.fillStamina();
    }

    public void resetTransientState() {
        guard.release();
        resources.setActionCharge(0);
        status.clearTransientState();
    }

    /** Leaving a level ends combat and drops what was bound to it, but cooldowns travel with the player. */
    public void resetLevelBoundState() {
        guard.release();
        resources.setActionCharge(0);
        status.clearTransformationState();
        status.clearCombat();
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("schemaVersion", 9);
        tag.put("guard", guard.save());
        tag.put("attributes", attributes.save());
        tag.put("character", character.save());
        tag.put("academy", academy.save());
        tag.put("origin", origin.save());
        tag.put("appearance", appearance.save());
        tag.put("resources", resources.save());
        tag.put("skills", skills.save());
        tag.put("status", status.save());
        tag.put("playerQuestData", playerQuestData.save());
        return tag;
    }

    public void load(CompoundTag tag) {
        if (tag.contains("guard")) guard.load(tag.getCompound("guard"));
        else if (tag.contains("schemaVersion")) guard.reset(); // Legacy saves start with full posture.
        if (tag.contains("character")) {
            character.load(tag.getCompound("character"));
        }
        // Saves before schema 9 have no appearance and keep the player's own Minecraft skin.
        if (tag.contains("appearance")) appearance.load(tag.getCompound("appearance"));
        if (tag.contains("resources")) {
            resources.load(tag.getCompound("resources"));
        }
        if (tag.contains("skills")) {
            skills.load(tag.getCompound("skills"));
        }
        if (tag.contains("status")) {
            status.load(tag.getCompound("status"));
        }
        if (tag.contains("academy", net.minecraft.nbt.Tag.TAG_COMPOUND)) {
            academy.load(tag.getCompound("academy"));
        } else if (tag.contains("schemaVersion") || (tag.contains("character") && tag.contains("status"))) {
            academy.loadLegacy(status.hasCreatedCharacter()
                    && Reference.RACE_SHINIGAMI.equals(character.getRace()));
        }
        if (tag.contains("origin", net.minecraft.nbt.Tag.TAG_COMPOUND)) origin.load(tag.getCompound("origin"));
        else if (tag.contains("schemaVersion")) origin.load(new CompoundTag());
        if (tag.contains("playerQuestData")) {
            playerQuestData.load(tag.getCompound("playerQuestData"));
        }
        if (tag.contains("attributes")) attributes.load(tag.getCompound("attributes"));
        if (tag.contains("attributes") || tag.contains("schemaVersion"))
            resources.setMaxStamina(com.bleachmod.common.StaminaPolicy.maximum(attributes.level(AttributeData.RESISTANCE)));
        updateTransformationSkillLimits(character.getRace());
        if (tag.contains("character") && !tag.getCompound("character").contains("unlockedForms")
                && CharacterData.RYUJIN.equals(character.getZanpakutoIdentity())) {
            character.unlockForm("sealed");
            if (skills.getLevel("zanpakuto") >= 1 || character.getMastery("zanpakuto", "shikai") > 0) character.unlockForm("shikai");
            if (skills.getLevel("zanpakuto") >= 2 || character.getMastery("zanpakuto", "bankai") > 0) character.unlockForm("bankai");
        }
        if(tag.contains("schemaVersion") && tag.getInt("schemaVersion")<9 && tag.contains("status")) {
            status.setIgnitionActive(false); // Retired sealed toggle must not survive the repertory migration.
        }
        dataLoaded = true;
        if (tag.getInt("schemaVersion") < 5 && tag.contains("character") && status.hasCreatedCharacter()) character.preserveLegacyTechniques();
    }

    public void copyFrom(PlayerData other) {
        load(other.save());
    }

    public static Optional<PlayerData> get(ServerPlayer player) {
        return player.getCapability(PlayerCapability.INSTANCE).resolve();
    }
}
