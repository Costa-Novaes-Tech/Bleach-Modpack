package com.bleachmod.client.cinematic;

import com.bleachmod.Reference;
import com.bleachmod.init.ModSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;

import java.util.*;

/**
 * Client-side audio manager for Senbonzakura Kageyoshi Bankai transformation.
 * Coordinates orchestral theme music ducking, Bankai voice invocation (tick 10),
 * and Senbonzakura Kageyoshi name invocation (tick 160).
 */
final class SenbonzakuraEntranceAudio {
    public static final int CUE_BANKAI = 10;
    public static final int CUE_NAME = 160;

    private static final class State {
        double previous;
        final List<SoundInstance> sounds = new ArrayList<>();
        final Set<Integer> fired = new HashSet<>();

        State(double age) {
            previous = age - 1;
        }
    }

    private static final Map<Integer, State> STATES = new HashMap<>();

    private SenbonzakuraEntranceAudio() { }

    static void stop(int id) {
        var s = STATES.remove(id);
        if (s != null) {
            var sm = Minecraft.getInstance().getSoundManager();
            s.sounds.forEach(sm::stop);
        }
    }

    static void clear() {
        for (int id : List.copyOf(STATES.keySet())) {
            stop(id);
        }
    }

    static void update(int id, double age, Vec3 anchor, boolean local) {
        var mc = Minecraft.getInstance();
        if (!STATES.containsKey(id)) {
            var s = new State(age);
            STATES.put(id, s);
            if (local && age <= 4) {
                mc.getMusicManager().stopPlaying();
                var music = SimpleSoundInstance.forMusic(ModSounds.SENBONZAKURA_THEME.get());
                s.sounds.add(music);
                mc.getSoundManager().play(music);
            }
        }

        var s = STATES.get(id);
        for (int cue : new int[]{CUE_BANKAI, CUE_NAME}) {
            if (s.previous < cue && age >= cue && s.fired.add(cue)) {
                boolean isBankai = cue == CUE_BANKAI;
                var sound = new SimpleSoundInstance(
                        Reference.id(isBankai ? "voice.senbonzakura_bankai" : "voice.senbonzakura_name"),
                        SoundSource.VOICE,
                        1.0F,
                        1.0F,
                        RandomSource.create(),
                        false,
                        0,
                        local ? SoundInstance.Attenuation.NONE : SoundInstance.Attenuation.LINEAR,
                        local ? 0 : anchor.x,
                        local ? 0 : anchor.y,
                        local ? 0 : anchor.z,
                        local
                );
                s.sounds.add(sound);
                mc.getSoundManager().play(sound);
                if (!net.minecraftforge.fml.loading.FMLEnvironment.production) {
                    com.mojang.logging.LogUtils.getLogger().info("Senbonzakura audio cue {} at tick {}", cue, age);
                }
            }
        }
        s.previous = age;
    }
}
