package com.bleachmod.client.cinematic;

import com.bleachmod.Reference;
import com.bleachmod.common.visual.HyorinmaruTimeline;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import java.util.*;

/** Separate music/voice channels; no replay of past cues for late tracking clients. */
final class HyorinmaruEntranceAudio {
    private static final class State {
        double previous;
        final List<SoundInstance> sounds=new ArrayList<>();
        final Set<Integer> fired=new HashSet<>();
        State(double age){previous=age-1;}
    }
    private static final Map<Integer,State> STATES=new HashMap<>();
    private HyorinmaruEntranceAudio() { }
    static void stop(int id){var s=STATES.remove(id);if(s!=null)s.sounds.forEach(Minecraft.getInstance().getSoundManager()::stop);}
    static void clear(){for(int id:List.copyOf(STATES.keySet()))stop(id);}
    static void update(int id,double age,Vec3 anchor,boolean local){
        var mc=Minecraft.getInstance();
        if(!STATES.containsKey(id)){
            var s=new State(age);STATES.put(id,s);
            if(local && age<=4){
                mc.getMusicManager().stopPlaying();
                var music=SimpleSoundInstance.forMusic(com.bleachmod.init.ModSounds.HYORINMARU_THEME.get());
                s.sounds.add(music);mc.getSoundManager().play(music);
            }
        }
        var s=STATES.get(id);
        for(int cue:new int[]{HyorinmaruTimeline.BANKAI_VOICE,HyorinmaruTimeline.NAME_VOICE,HyorinmaruTimeline.HYDRA_ROAR}){
            if(!HyorinmaruTimeline.cueDue(s.previous,age,cue) || !s.fired.add(cue))continue;
            boolean roar=cue==HyorinmaruTimeline.HYDRA_ROAR;
            var sound=new SimpleSoundInstance(Reference.id(roar?"hyorinmaru.roar":cue==HyorinmaruTimeline.BANKAI_VOICE?"voice.hyorinmaru_bankai":"voice.hyorinmaru_name"),
                    roar?SoundSource.PLAYERS:SoundSource.VOICE,1F,roar?.9F:1F,RandomSource.create(),false,0,
                    local?SoundInstance.Attenuation.NONE:SoundInstance.Attenuation.LINEAR,
                    local?0:anchor.x,local?0:anchor.y,local?0:anchor.z,local);
            s.sounds.add(sound);mc.getSoundManager().play(sound);
            if(!net.minecraftforge.fml.loading.FMLEnvironment.production)
                com.mojang.logging.LogUtils.getLogger().info("Hyorinmaru audio cue {} at tick {}",cue,age);
        }
        s.previous=age;
    }
}
