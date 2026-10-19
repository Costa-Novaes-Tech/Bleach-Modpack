package com.bleachmod.init;

import com.bleachmod.Reference;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/** Custom sounds; files live in assets/bleachmod/sounds and are declared in sounds.json. */
public final class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Reference.MOD_ID);

    /** Zaraki shouting "Bankai" (4.16 s, mono): plays at the start of the Berserk Mode cinematic. */
    public static final RegistryObject<SoundEvent> BANKAI_ZARAKI = SOUND_EVENTS.register("voice.bankai_zaraki",
            () -> SoundEvent.createVariableRangeEvent(Reference.id("voice.bankai_zaraki")));
    public static final RegistryObject<SoundEvent> BENIHIME_ENTRANCE = SOUND_EVENTS.register("benihime.entrance",
            () -> SoundEvent.createVariableRangeEvent(Reference.id("benihime.entrance")));

    public static final RegistryObject<SoundEvent> BENIHIME_VOICE_BANKAI = SOUND_EVENTS.register("voice.benihime_bankai",
            () -> SoundEvent.createVariableRangeEvent(Reference.id("voice.benihime_bankai")));
    public static final RegistryObject<SoundEvent> BENIHIME_VOICE_NAME = SOUND_EVENTS.register("voice.benihime_name",
            () -> SoundEvent.createVariableRangeEvent(Reference.id("voice.benihime_name")));
    public static final RegistryObject<SoundEvent> BENIHIME_CURTAIN = SOUND_EVENTS.register("benihime.curtain",
            () -> SoundEvent.createVariableRangeEvent(Reference.id("benihime.curtain")));
    public static final RegistryObject<SoundEvent> BENIHIME_REVEAL = SOUND_EVENTS.register("benihime.reveal",
            () -> SoundEvent.createVariableRangeEvent(Reference.id("benihime.reveal")));
    public static final RegistryObject<SoundEvent> BENIHIME_THREADS = SOUND_EVENTS.register("benihime.threads",
            () -> SoundEvent.createVariableRangeEvent(Reference.id("benihime.threads")));

    private ModSounds() {
    }
}
