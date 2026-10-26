package com.bleachmod.client.cinematic;

import com.bleachmod.Reference;
import com.bleachmod.client.ClientVisualConfig;
import com.bleachmod.common.visual.RyujinBankaiTimeline;
import com.bleachmod.init.ModSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.*;
import net.minecraft.sounds.*;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import java.util.*;

/** Real-time score and authored-beat voice cues. No repeated bursts on a paused tick. */
final class RyujinBankaiAudio {
    private static final class State {
        double lastAge=-1;
        boolean bankai,name,extinguished,musicStarted,fireStarted;
        Score score,fire;
        final List<SoundInstance> playing=new ArrayList<>();
    }
    private static final Map<Integer,State> STATES=new HashMap<>();
    private RyujinBankaiAudio() { }
    private static final class Score extends AbstractTickableSoundInstance {
        float gain;
        Score(String event, SoundSource source) {
            super(SoundEvent.createVariableRangeEvent(Reference.id(event)),source,RandomSource.create());
            relative=true;attenuation=Attenuation.NONE;volume=0;looping=false;
        }
        @Override public boolean canStartSilent(){return true;}
        @Override public void tick(){volume=gain;}
    }
    static void stop(int owner){var s=STATES.remove(owner);if(s!=null)stopSounds(s);}
    private static void stopSounds(State s){var m=Minecraft.getInstance().getSoundManager();s.playing.forEach(m::stop);if(s.score!=null)m.stop(s.score);if(s.fire!=null)m.stop(s.fire);s.playing.clear();s.score=null;s.fire=null;}
    static void clear(){STATES.values().forEach(RyujinBankaiAudio::stopSounds);STATES.clear();}
    static void retain(Set<Integer> owners){for(int id:List.copyOf(STATES.keySet()))if(!owners.contains(id))stop(id);}
    private static void play(State s,SoundEvent event,SoundSource source,float volume,float pitch,Vec3 p,boolean local){
        var sound=new SimpleSoundInstance(event.getLocation(),source,volume,pitch,RandomSource.create(),false,0,
                local?SoundInstance.Attenuation.NONE:SoundInstance.Attenuation.LINEAR,local?0:p.x,local?0:p.y,local?0:p.z,local);
        s.playing.add(sound);Minecraft.getInstance().getSoundManager().play(sound);
    }
    static void update(int owner,double raw,int duration,Vec3 p,boolean local,boolean allowed){
        var mc=Minecraft.getInstance();var s=STATES.computeIfAbsent(owner,id->new State());
        if(raw<s.lastAge-2){stop(owner);s=new State();STATES.put(owner,s);}
        double age=RyujinBankaiTimeline.storyAge(raw,duration),previous=RyujinBankaiTimeline.storyAge(Math.max(0,s.lastAge),duration);
        if(!allowed || raw>=duration){stopSounds(s);s.lastAge=raw;return;}
        if(raw==s.lastAge)return;
        s.playing.removeIf(sound->!mc.getSoundManager().isActive(sound));
        if(local && !s.musicStarted && raw<10 && ClientVisualConfig.RYUJIN_MUSIC.get()){
            s.musicStarted=true;s.score=new Score("music.ryujin_entrance",SoundSource.MUSIC);s.playing.add(s.score);mc.getSoundManager().play(s.score);
        }
        if(local && !s.fireStarted && raw<10){
            s.fireStarted=true;s.fire=new Score("effect.ryujin_solar_fire",SoundSource.PLAYERS);s.playing.add(s.fire);mc.getSoundManager().play(s.fire);
        }
        boolean speaking=(raw>=RyujinBankaiTimeline.realAge(12,duration)-3 && raw<RyujinBankaiTimeline.realAge(12,duration)+32)
                || (raw>=RyujinBankaiTimeline.realAge(60,duration)-3 && raw<RyujinBankaiTimeline.realAge(60,duration)+50);
        double fade=Math.min(1,raw/8)*Math.min(1,(duration-raw)/20.0);
        if(s.score!=null){
            s.score.gain=ClientVisualConfig.RYUJIN_MUSIC.get()?(float)(.75*fade*(speaking?.16:1)):0;
        }
        if(s.fire!=null)s.fire.gain=(float)(.65*fade*(speaking?.12:1));
        // Skip expired lines for late observers; never replay a voice after a menu interruption.
        if(!s.bankai && age>=12){s.bankai=true;if(raw<RyujinBankaiTimeline.realAge(12,duration)+8)play(s,ModSounds.RYUJIN_VOICE_BANKAI.get(),SoundSource.VOICE,local?1:1.6F,1,p,local);}
        if(!s.name && age>=60){s.name=true;if(raw<RyujinBankaiTimeline.realAge(60,duration)+8)play(s,ModSounds.RYUJIN_VOICE_NAME.get(),SoundSource.VOICE,local?1:1.6F,1,p,local);}
        if(!local && age<52 && (int)raw/35!=(int)Math.max(0,s.lastAge)/35)
            play(s,SoundEvents.FIRE_AMBIENT,SoundSource.PLAYERS,speaking?.06F:.20F,.65F,p,local);
        if(!s.extinguished && age>=52){s.extinguished=true;if(previous<52)play(s,SoundEvents.FIRE_EXTINGUISH,SoundSource.PLAYERS,.55F,.7F,p,local);}
        s.lastAge=raw;
    }
}
