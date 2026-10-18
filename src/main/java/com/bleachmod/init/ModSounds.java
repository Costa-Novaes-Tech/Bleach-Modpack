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

    private ModSounds() {
    }
}
