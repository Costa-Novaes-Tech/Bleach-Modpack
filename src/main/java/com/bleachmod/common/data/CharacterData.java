package com.bleachmod.common.data;

import com.bleachmod.Reference;
import net.minecraft.nbt.CompoundTag;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CharacterData {
    private final java.util.Set<String> unlockedForms = new java.util.HashSet<>();
    public static final String RYUJIN = "ryujin_jakka";
    public static final String HYORINMARU = "hyorinmaru";
    public static final String SENBONZAKURA = "senbonzakura";
    public static final String BENIHIME = "benihime";
    public static final String NOZARASHI = "nozarashi";
    public static final String ASAUCHI = "asauchi";
    private String zanpakutoIdentity = ASAUCHI;
    private final java.util.Set<String> unlockedTechniques = new java.util.HashSet<>();
    private final Map<String, Double> techniqueMasteries = new HashMap<>();
    public static boolean knownIdentity(String identity) { return ASAUCHI.equals(identity) || RYUJIN.equals(identity) || HYORINMARU.equals(identity) || SENBONZAKURA.equals(identity) || BENIHIME.equals(identity) || NOZARASHI.equals(identity); }
    public boolean supportsForm(String form) { return knownIdentity(zanpakutoIdentity) && java.util.Set.of("sealed", "shikai", "bankai").contains(form); }
    private String techniqueKey(String form, int slot) {
        String suffix=(RYUJIN.equals(zanpakutoIdentity) || HYORINMARU.equals(zanpakutoIdentity) || BENIHIME.equals(zanpakutoIdentity))
                ? com.bleachmod.common.technique.TechniqueDefinitions.find(zanpakutoIdentity,form,slot).map(d->d.id()).orElse("unavailable") : Integer.toString(slot);
        return zanpakutoIdentity + "|" + form + "|" + suffix;
    }
    public boolean isTechniqueDiscovered(String form, int slot) { return unlockedTechniques.contains(techniqueKey(form, slot)); }
    public void unlockTechnique(String form, int slot) { if (supportsForm(form) && !ASAUCHI.equals(zanpakutoIdentity) && slot >= 1 && slot <= 4) unlockedTechniques.add(techniqueKey(form, slot)); }
    public double getTechniqueMastery(String form, int slot) { return techniqueMasteries.getOrDefault(techniqueKey(form, slot), 0D); }
    public void addTechniqueMastery(String form, int slot, double amount) {
        if (Double.isFinite(amount) && amount > 0 && slot >= 1 && slot <= 4 && !ASAUCHI.equals(zanpakutoIdentity))
            techniqueMasteries.put(techniqueKey(form, slot), Math.min(100, getTechniqueMastery(form, slot) + amount));
    }
    public void preserveLegacyTechniques() {
        String active = zanpakutoIdentity;
        for (String id : java.util.List.of(RYUJIN, HYORINMARU)) {
            boolean owned = id.equals(active) || (RYUJIN.equals(id) ? unlockedForms.stream().anyMatch(f -> !f.contains("|"))
                    || formMasteries.keySet().stream().anyMatch(k -> k.startsWith("zanpakuto:"))
                    : unlockedForms.stream().anyMatch(f -> f.startsWith(id + "|")) || formMasteries.keySet().stream().anyMatch(k -> k.startsWith(id + "|")));
            if (owned) for (var definition:com.bleachmod.common.technique.TechniqueDefinitions.all())
                if(definition.identity().equals(id) && !definition.id().equals("unavailable")) unlockedTechniques.add(id+"|"+definition.form()+"|"+definition.id());
        }
    }
    private boolean iceArmorVisual;
    private int senbonBlade;
    public int getSenbonBlade() { return senbonBlade; }
    public void setSenbonBlade(int mode) { senbonBlade = Math.max(0, Math.min(2, mode)); }
    private int carryState;
    public int getCarryState() { return carryState; }
    public void setCarryState(int state) { carryState = Math.max(0, Math.min(3, state)); }
    public boolean isIceArmorVisual() { return iceArmorVisual; }
    public void setIceArmorVisual(boolean active) { iceArmorVisual = active; }
    public String getZanpakutoIdentity() { return zanpakutoIdentity; }
    /** Operator prototype switch; each identity retains its own archived progress. */
    public void bindZanpakuto(String identity) {
        if (!knownIdentity(identity)) throw new IllegalArgumentException("Unknown zanpakuto");
        zanpakutoIdentity = identity;
        setSelectedForm(Reference.GROUP_ZANPAKUTO, Reference.FORM_SEALED);
        setActiveForm(Reference.GROUP_ZANPAKUTO, Reference.FORM_SEALED);
    }
    private String identityKey(String key) { return RYUJIN.equals(zanpakutoIdentity) ? key : zanpakutoIdentity + "|" + key; }
    public void unlockZanpakutoForm(String identity, String form) {
        unlockedForms.add(RYUJIN.equals(identity) ? form : identity + "|" + form);
    }
    public double getZanpakutoMastery(String identity, String form) {
        String key = Reference.GROUP_ZANPAKUTO + ":" + form;
        return formMasteries.getOrDefault(RYUJIN.equals(identity) ? key : identity + "|" + key, 0.0D);
    }
    public void setZanpakutoMastery(String identity, String form, double value) {
        String key = Reference.GROUP_ZANPAKUTO + ":" + form;
        formMasteries.put(RYUJIN.equals(identity) ? key : identity + "|" + key,
                Double.isFinite(value) ? Math.max(0, Math.min(100, value)) : 0);
    }
    public void unlockForm(String form) { unlockedForms.add(identityKey(form)); }
    public boolean isFormDiscovered(String form) { return supportsForm(form) && ("sealed".equals(form) || unlockedForms.contains(identityKey(form))); }
    private String race = "";
    private String selectedFormGroup = "";
    private String selectedForm = "";
    private String activeFormGroup = "";
    private String activeForm = "";
    private final Map<String, Double> formMasteries = new HashMap<>();

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race == null ? "" : race.toLowerCase(Locale.ROOT);
    }

    public String getSelectedFormGroup() {
        return selectedFormGroup;
    }

    public String getSelectedForm() {
        return selectedForm;
    }

    public void setSelectedForm(String group, String form) {
        this.selectedFormGroup = group == null ? "" : group.toLowerCase(Locale.ROOT);
        this.selectedForm = form == null ? "" : form.toLowerCase(Locale.ROOT);
    }

    public String getActiveFormGroup() {
        return activeFormGroup;
    }

    public String getActiveForm() {
        return activeForm;
    }

    public void setActiveForm(String group, String form) {
        this.activeFormGroup = group == null ? "" : group.toLowerCase(Locale.ROOT);
        this.activeForm = form == null ? "" : form.toLowerCase(Locale.ROOT);
    }

    public boolean hasActiveForm() {
        return !activeForm.isEmpty();
    }

    public double getMastery(String group, String form) {
        return formMasteries.getOrDefault(masteryKey(group, form), 0.0D);
    }

    public void setMastery(String group, String form, double value) {
        formMasteries.put(masteryKey(group, form), Double.isFinite(value) ? Math.max(0.0D, Math.min(100.0D, value)) : 0.0D);
    }

    public void addMastery(String group, String form, double amount, double max) {
        double next = Math.min(max, getMastery(group, form) + amount);
        setMastery(group, form, next);
    }

    public void initializeShinigami() {
        zanpakutoIdentity = ASAUCHI;
        unlockForm("sealed");
        setRace(Reference.RACE_SHINIGAMI);
        setSelectedForm(Reference.GROUP_ZANPAKUTO, Reference.FORM_SEALED);
        setActiveForm(Reference.GROUP_ZANPAKUTO, Reference.FORM_SEALED);
    }

    public CompoundTag saveAppearance() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("senbonBlade", senbonBlade);
        tag.putInt("carryState", carryState);
        tag.putBoolean("iceArmorVisual", iceArmorVisual);
        tag.putString("race", race);
        tag.putString("zanpakutoIdentity", zanpakutoIdentity);
        tag.putString("activeFormGroup", activeFormGroup);
        tag.putString("activeForm", activeForm);
        return tag;
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("techniqueRevision",3);
        tag.putString("race", race);
        tag.putString("zanpakutoIdentity", zanpakutoIdentity);
        tag.putString("selectedFormGroup", selectedFormGroup);
        tag.putString("selectedForm", selectedForm);
        tag.putString("activeFormGroup", activeFormGroup);
        tag.putString("activeForm", activeForm);
        CompoundTag masteries = new CompoundTag();
        formMasteries.forEach(masteries::putDouble);
        tag.put("formMasteries", masteries);
        CompoundTag unlocks = new CompoundTag();
        unlockedForms.forEach(form -> unlocks.putBoolean(form, true));
        tag.put("unlockedForms", unlocks);
        CompoundTag techniques = new CompoundTag(); unlockedTechniques.forEach(key -> techniques.putBoolean(key, true));
        tag.put("unlockedTechniques", techniques);
        CompoundTag mastery = new CompoundTag(); techniqueMasteries.forEach(mastery::putDouble); tag.put("techniqueMasteries", mastery);
        return tag;
    }

    public void load(CompoundTag tag) {
        if (tag.contains("senbonBlade")) setSenbonBlade(tag.getInt("senbonBlade"));
        if (tag.contains("carryState")) setCarryState(tag.getInt("carryState"));
        if (tag.contains("iceArmorVisual")) iceArmorVisual = tag.getBoolean("iceArmorVisual");
        // Legacy characters belong to Ryujin; never copy their mastery into Hyorinmaru.
        String identity = tag.getString("zanpakutoIdentity");
        zanpakutoIdentity = !tag.contains("zanpakutoIdentity") ? RYUJIN : knownIdentity(identity) ? identity : ASAUCHI;
        if (tag.contains("unlockedTechniques")) { unlockedTechniques.clear(); unlockedTechniques.addAll(tag.getCompound("unlockedTechniques").getAllKeys()); }
        if (tag.contains("techniqueMasteries")) {
            techniqueMasteries.clear(); CompoundTag values = tag.getCompound("techniqueMasteries");
            for (String key : values.getAllKeys()) { double value = values.getDouble(key); techniqueMasteries.put(key, Double.isFinite(value) ? Math.max(0, Math.min(100, value)) : 0); }
        }
        if(tag.getInt("techniqueRevision")<2 && (tag.contains("techniqueMasteries") || tag.contains("unlockedTechniques")))migrateElementalTechniques();
        if(tag.getInt("techniqueRevision")<3){
            // Legacy Benihime used numeric slots. Resolve the OLD layout before adopting stable IDs.
            for(String form:java.util.List.of("shikai","bankai")){
                String[] ids=form.equals("bankai")?new String[]{"dissection","reconstruction","final_stitch","path"}:new String[]{"nake","shield","shibari","hiasobi"};
                for(int i=0;i<4;i++){
                    String old=BENIHIME+"|"+form+"|"+(i+1),replacement=BENIHIME+"|"+form+"|"+ids[i];
                    if(unlockedTechniques.contains(old))unlockedTechniques.add(replacement);
                    if(techniqueMasteries.containsKey(old))techniqueMasteries.merge(replacement,techniqueMasteries.get(old),Math::max);
                }
            }
        }
        if (tag.contains("unlockedForms")) { unlockedForms.clear(); unlockedForms.addAll(tag.getCompound("unlockedForms").getAllKeys()); }
        if (tag.contains("race")) {
            race = tag.getString("race");
        }
        if (tag.contains("selectedFormGroup")) {
            selectedFormGroup = tag.getString("selectedFormGroup");
        }
        if (tag.contains("selectedForm")) {
            selectedForm = tag.getString("selectedForm");
        }
        if (tag.contains("activeFormGroup")) {
            activeFormGroup = tag.getString("activeFormGroup");
        }
        if (tag.contains("activeForm")) {
            activeForm = tag.getString("activeForm");
        }
        if (tag.contains("formMasteries")) {
            formMasteries.clear();
            CompoundTag masteries = tag.getCompound("formMasteries");
            for (String key : masteries.getAllKeys()) {
                double value = masteries.getDouble(key); formMasteries.put(key, Double.isFinite(value) ? Math.max(0, Math.min(100, value)) : 0);
            }
        }
    }

    private String masteryKey(String group, String form) {
        String key = (group == null ? "" : group.toLowerCase(Locale.ROOT)) + ":"
                + (form == null ? "" : form.toLowerCase(Locale.ROOT));
        return Reference.GROUP_ZANPAKUTO.equalsIgnoreCase(group) ? identityKey(key) : key;
    }
    /** Preserve semantic attacks across slot moves; retired numeric keys remain archived. */
    private void migrateElementalTechniques(){
        for(String id:java.util.List.of(RYUJIN,HYORINMARU))for(String form:java.util.List.of("sealed","shikai","bankai"))for(int slot=1;slot<=4;slot++){
            String old=id+"|"+form+"|"+slot,next=null;
            if(id.equals(RYUJIN)){
                if(!form.equals("bankai") && slot==2)next=id+"|shikai|flame_barrage";
                if(form.equals("bankai"))next=switch(slot){case 1->id+"|bankai|flame_dash";case 2->id+"|shikai|flame_fan";case 3->id+"|shikai|flame_tornado";default->id+"|bankai|steam_cut";};
            }else{
                if(form.equals("sealed"))next=switch(slot){case 1->id+"|sealed|front_cut";case 2->id+"|sealed|wide_cut";case 4->id+"|sealed|duel_control";default->null;};
                else if(slot==1)next=id+"|"+form+"|ice_dragon";
                else if(slot==4)next=id+"|"+form+"|glacial_zone";
            }
            if(next==null)continue;
            if(unlockedTechniques.contains(old))unlockedTechniques.add(next);
            if(techniqueMasteries.containsKey(old))techniqueMasteries.merge(next,techniqueMasteries.get(old),Math::max);
        }
    }
}
