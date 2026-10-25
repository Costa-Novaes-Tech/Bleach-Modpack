package com.bleachmod.client.cinematic;

import com.bleachmod.common.visual.RyujinBankaiTimeline;
import com.bleachmod.init.ModSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;

import java.util.*;

/**
 * Audio coordinator for Ryūjin Jakka / Zanka no Tachi Bankai cinematic.
 * Handles cues for "Bankai" (tick 12) and "Zanka no Tachi" (tick 60),
 * flame ambiance with ducking during voice lines, and sizzle/smoke extinguish at tick 52.
 */
final class RyujinBankaiAudio {
    private static final class State {
        double lastAge;
        boolean firedBankai;
        boolean firedName;
        boolean firedExtinguish;
        final List<SoundInstance> playing = new ArrayList<>();
        State(double age) { lastAge = age - 1; }
    }

    private static final Map<Integer, State> STATES = new HashMap<>();

    private RyujinBankaiAudio() { }

    static void stop(int owner) {
        var state = STATES.remove(owner);
        if (state != null) stopSounds(state);
    }

    private static void stopSounds(State state) {
        var manager = Minecraft.getInstance().getSoundManager();
        for (var s : state.playing) manager.stop(s);
        state.playing.clear();
    }

    static void clear() {
        for (var state : STATES.values()) stopSounds(state);
        STATES.clear();
    }

    static void retain(Set<Integer> owners) {
        for (int id : List.copyOf(STATES.keySet())) {
            if (!owners.contains(id)) stop(id);
        }
    }

    static void update(int owner, double age, Vec3 position, boolean local, boolean allowed) {
        var mc = Minecraft.getInstance();
        var state = STATES.computeIfAbsent(owner, id -> new State(age));

        if (age < state.lastAge - 2) {
            stop(owner);
            state = new State(age);
            STATES.put(owner, state);
        }

        if (!allowed) {
            stopSounds(state);
            state.lastAge = age;
            return;
        }

        // Cue 1: "Bankai" at tick 12
        if (age >= RyujinBankaiTimeline.CUE_BANKAI && !state.firedBankai && age < 40) {
            state.firedBankai = true;
            var sound = new SimpleSoundInstance(
                    ModSounds.RYUJIN_VOICE_BANKAI.get().getLocation(),
                    SoundSource.VOICE,
                    local ? 1.0F : 1.6F, 1.0F, RandomSource.create(), false, 0,
                    local ? SoundInstance.Attenuation.NONE : SoundInstance.Attenuation.LINEAR,
                    local ? 0 : position.x, local ? 0 : position.y, local ? 0 : position.z, local
            );
            state.playing.add(sound);
            mc.getSoundManager().play(sound);
        }

        // Flame roar / sun ambiance (blaze shoot / fire ambient burst during swell and absorption)
        if (age >= 1 && age < 50 && (int) age % 16 == 0) {
            float duck = (age >= 12 && age <= 35) ? 0.35F : 0.85F;
            var roar = new SimpleSoundInstance(
                    SoundEvents.BLAZE_SHOOT.getLocation(),
                    SoundSource.PLAYERS,
                    duck * 0.7F, 0.65F, RandomSource.create(), false, 0,
                    local ? SoundInstance.Attenuation.NONE : SoundInstance.Attenuation.LINEAR,
                    local ? 0 : position.x, local ? 0 : position.y, local ? 0 : position.z, local
            );
            state.playing.add(roar);
            mc.getSoundManager().play(roar);
        }

        // Sizzle / extinguish at tick 52 when all fire has been absorbed into the blade
        if (age >= 52 && !state.firedExtinguish && age < 60) {
            state.firedExtinguish = true;
            var ext = new SimpleSoundInstance(
                    SoundEvents.FIRE_EXTINGUISH.getLocation(),
                    SoundSource.PLAYERS,
                    local ? 0.7F : 1.0F, 0.75F, RandomSource.create(), false, 0,
                    local ? SoundInstance.Attenuation.NONE : SoundInstance.Attenuation.LINEAR,
                    local ? 0 : position.x, local ? 0 : position.y, local ? 0 : position.z, local
            );
            state.playing.add(ext);
            mc.getSoundManager().play(ext);
        }

        // Cue 2: "Zanka no Tachi" at tick 60
        if (age >= RyujinBankaiTimeline.CUE_NAME && !state.firedName && age < 105) {
            state.firedName = true;
            var sound = new SimpleSoundInstance(
                    ModSounds.RYUJIN_VOICE_NAME.get().getLocation(),
                    SoundSource.VOICE,
                    local ? 1.0F : 1.6F, 1.0F, RandomSource.create(), false, 0,
                    local ? SoundInstance.Attenuation.NONE : SoundInstance.Attenuation.LINEAR,
                    local ? 0 : position.x, local ? 0 : position.y, local ? 0 : position.z, local
            );
            state.playing.add(sound);
            mc.getSoundManager().play(sound);
        }

        state.lastAge = age;
    }
}
