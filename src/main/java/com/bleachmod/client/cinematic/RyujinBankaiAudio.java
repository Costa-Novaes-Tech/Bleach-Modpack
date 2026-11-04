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
        boolean bankai,name,extinguished,musicStarted,fireStarted,preheatStarted,voiceBurst,exploded,roared,absorbed,sealed;
        Score score,fire,preheat;
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
    private static void stopSounds(State s){var m=Minecraft.getInstance().getSoundManager();s.playing.forEach(m::stop);if(s.score!=null)m.stop(s.score);if(s.fire!=null)m.stop(s.fire);if(s.preheat!=null)m.stop(s.preheat);s.playing.clear();s.score=null;s.fire=null;s.preheat=null;}
    static void clear(){STATES.values().forEach(RyujinBankaiAudio::stopSounds);STATES.clear();}
    static void retain(Set<Integer> owners){for(int id:List.copyOf(STATES.keySet()))if(!owners.contains(id))stop(id);}
    private static void play(State s,SoundEvent event,SoundSource source,float volume,float pitch,Vec3 p,boolean local){
        var sound=new SimpleSoundInstance(event.getLocation(),source,volume,pitch,RandomSource.create(),false,0,
                local?SoundInstance.Attenuation.NONE:SoundInstance.Attenuation.LINEAR,local?0:p.x,local?0:p.y,local?0:p.z,local);
        s.playing.add(sound);Minecraft.getInstance().getSoundManager().play(sound);
    }
    private static boolean localAudioAvailable(String effect) {
        return Minecraft.getInstance().getResourceManager()
                .getResource(Reference.id("sounds/ryujin_v7_" + effect + ".ogg")).isPresent();
    }
    private static void playLocalAudio(State s, String effect, float volume, Vec3 p, boolean local) {
        if (localAudioAvailable(effect))
            play(s, SoundEvent.createVariableRangeEvent(Reference.id("effect.ryujin_v7_" + effect)),
                    SoundSource.PLAYERS, volume, 1, p, local);
    }
    static void update(int owner,double raw,int duration,Vec3 p,boolean local,boolean allowed){
        var mc=Minecraft.getInstance();var s=STATES.computeIfAbsent(owner,id->new State());
        if(raw<s.lastAge-2){stop(owner);s=new State();STATES.put(owner,s);}
        double age=RyujinBankaiTimeline.storyAge(raw,duration);
        if(!allowed || raw>=duration){stopSounds(s);s.lastAge=raw;return;}
        if(raw==s.lastAge)return;
        s.playing.removeIf(sound->!mc.getSoundManager().isActive(sound));
        if(local && !s.musicStarted && raw<10 && ClientVisualConfig.RYUJIN_MUSIC.get()){
            s.musicStarted=true;s.score=new Score("music.ryujin_entrance",SoundSource.MUSIC);s.playing.add(s.score);mc.getSoundManager().play(s.score);
        }
        if(local && !s.fireStarted && raw<10){
            s.fireStarted=true;s.fire=new Score("effect.ryujin_solar_fire",SoundSource.PLAYERS);s.playing.add(s.fire);mc.getSoundManager().play(s.fire);
        }
        if(!s.preheatStarted && raw<8){
            s.preheatStarted=true;
            if(local && localAudioAvailable("preheat")) {
                s.preheat=new Score("effect.ryujin_v7_preheat",SoundSource.PLAYERS);
                s.playing.add(s.preheat);
                mc.getSoundManager().play(s.preheat);
            } else if(!local) playLocalAudio(s,"preheat",.35F,p,false);
        }
        boolean speaking=(raw>=RyujinBankaiTimeline.realAge(12,duration)-3 && raw<RyujinBankaiTimeline.realAge(12,duration)+32)
                || (raw>=RyujinBankaiTimeline.realAge(60,duration)-3 && raw<RyujinBankaiTimeline.realAge(60,duration)+50);
        double fade=Math.min(1,raw/8)*Math.min(1,(duration-raw)/20.0);
        if(s.score!=null){
            s.score.gain=ClientVisualConfig.RYUJIN_MUSIC.get()?(float)(.55*fade*(speaking?.18:1)):0;
        }
        if(s.fire!=null)s.fire.gain=(float)((raw<70?.10:raw<142?.45:.14)*fade*(speaking?.3:1));
        if(s.preheat!=null)s.preheat.gain=(float)(.30*fade*(speaking?.18:1));
        // Skip expired lines for late observers; never replay a voice after a menu interruption.
        if(!s.bankai && age>=12){s.bankai=true;if(raw<RyujinBankaiTimeline.realAge(12,duration)+8)play(s,ModSounds.RYUJIN_VOICE_BANKAI.get(),SoundSource.VOICE,local?1:1.6F,1,p,local);}
        if(!s.voiceBurst && raw>=40){
            s.voiceBurst=true;
            if(raw<48){
                // A restrained impact under the word; the full explosion remains at tick 70.
                play(s,SoundEvents.GENERIC_EXPLODE,SoundSource.PLAYERS,local?.16F:.27F,1.28F,p,local);
                play(s,SoundEvents.FIRECHARGE_USE,SoundSource.PLAYERS,local?.24F:.38F,.9F,p,local);
            }
        }
        if(!s.name && age>=60){s.name=true;if(raw<RyujinBankaiTimeline.realAge(60,duration)+8)play(s,ModSounds.RYUJIN_VOICE_NAME.get(),SoundSource.VOICE,local?1:1.6F,1,p,local);}
        if(!s.exploded && raw>=70){
            s.exploded=true;
            if(raw<78){
                playLocalAudio(s,"flame_explosion",local?.75F:1.15F,p,local);
                play(s,SoundEvents.GENERIC_EXPLODE,SoundSource.PLAYERS,local?.42F:.65F,.68F,p,local);
                play(s,SoundEvents.FIRECHARGE_USE,SoundSource.PLAYERS,local?.65F:.85F,.70F,p,local);
            }
        }
        if(!s.roared && raw>=72){s.roared=true;if(raw<80)playLocalAudio(s,"solar_roar_loop",local?.46F:.70F,p,local);}
        if(!s.absorbed && raw>=116){s.absorbed=true;if(raw<124)playLocalAudio(s,"flame_absorption",local?.65F:.9F,p,local);}
        if(!s.sealed && raw>=142){
            s.sealed=true;
            if(raw<150){
                playLocalAudio(s,"blade_seal",local?.72F:.9F,p,local);
                play(s,SoundEvents.FIRE_EXTINGUISH,SoundSource.PLAYERS,.40F,.76F,p,local);
            }
        }
        if(!local && age<52 && (int)raw/35!=(int)Math.max(0,s.lastAge)/35)
            play(s,SoundEvents.FIRE_AMBIENT,SoundSource.PLAYERS,speaking?.06F:.20F,.65F,p,local);
        if(!s.extinguished && age>=52){s.extinguished=true;}
        s.lastAge=raw;
    }
}
