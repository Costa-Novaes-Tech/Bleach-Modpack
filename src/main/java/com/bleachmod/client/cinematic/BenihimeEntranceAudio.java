package com.bleachmod.client.cinematic;

import com.bleachmod.Reference;
import com.bleachmod.client.ClientVisualConfig;
import com.bleachmod.common.visual.BenihimeAudioTimeline;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.*;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import java.util.*;

/** Local soundtrack; positional Japanese speech and effects for observers. No gameplay logic. */
final class BenihimeEntranceAudio {
    private static final class State {
        double lastAge;boolean musicAttempted,combined;final List<SoundInstance> playing=new ArrayList<>();
        final EnumSet<BenihimeAudioTimeline.Cue> fired=EnumSet.noneOf(BenihimeAudioTimeline.Cue.class);
        State(double age){lastAge=age-1;}
    }
    private static final Map<Integer,State> STATES=new HashMap<>();
    private BenihimeEntranceAudio(){ }
    static void stop(int owner){var state=STATES.remove(owner);if(state!=null)stopSounds(state);}
    private static void stopSounds(State state){var manager=Minecraft.getInstance().getSoundManager();state.playing.forEach(manager::stop);state.playing.clear();}
    static void clear(){for(var state:STATES.values())stopSounds(state);STATES.clear();}
    static void retain(Set<Integer> owners){for(int id:List.copyOf(STATES.keySet()))if(!owners.contains(id))stop(id);}
    static void update(int owner,double age,Vec3 position,Vec3 stage,boolean local,boolean allowed){
        var mc=Minecraft.getInstance();var state=STATES.computeIfAbsent(owner,id->new State(age));
        if(age<state.lastAge-2){stop(owner);state=new State(age);STATES.put(owner,state);}
        if(!allowed){stopSounds(state);state.musicAttempted=true;state.lastAge=age;return;}
        if(local&&!state.musicAttempted){
            state.musicAttempted=true;
            if(age<=10&&ClientVisualConfig.BENIHIME_MUSIC.get()){
                mc.getMusicManager().stopPlaying();
                // User ordered track contains its own dialogue: never play the clean stems twice.
                state.combined=ClientVisualConfig.BENIHIME_VOICE.get()&&mc.options.getSoundSourceVolume(SoundSource.VOICE)>0&&mc.options.getSoundSourceVolume(SoundSource.MUSIC)>0;
                var music=SimpleSoundInstance.forMusic(state.combined?com.bleachmod.init.ModSounds.BENIHIME_INVOCATION_MIX.get():com.bleachmod.init.ModSounds.BENIHIME_ENTRANCE.get());
                state.playing.add(music);mc.getSoundManager().play(music);
            }
        }
        for(var cue:BenihimeAudioTimeline.Cue.values()){
            if(!BenihimeAudioTimeline.due(state.lastAge,age,cue)||!state.fired.add(cue))continue;
            if(cue.voice&&(!ClientVisualConfig.BENIHIME_VOICE.get()||(local&&state.combined)))continue;
            Vec3 source=cue.voice?position:stage;
            var sound=new SimpleSoundInstance(Reference.id(cue.event),cue.voice?SoundSource.VOICE:SoundSource.PLAYERS,
                    cue.volume*(cue.voice&&!local?1.8F:1F),cue.pitch,RandomSource.create(),false,0,
                    local?SoundInstance.Attenuation.NONE:SoundInstance.Attenuation.LINEAR,
                    local?0:source.x,local?0:source.y,local?0:source.z,local);
            state.playing.add(sound);mc.getSoundManager().play(sound);
            if(local&&!net.minecraftforge.fml.loading.FMLEnvironment.production&&Boolean.getBoolean("bleachmod.benihimeQa"))
                com.mojang.logging.LogUtils.getLogger().info("Benihime audio cue {} at scene tick {} on {}",cue,age,cue.voice?"VOICE":"PLAYERS");
        }
        state.lastAge=age;
    }
}
