package com.bleachmod.client.cinematic;

import com.bleachmod.Reference;
import com.bleachmod.client.ClientVisualConfig;
import com.bleachmod.common.visual.RyujinBankaiTimeline;
import com.bleachmod.common.visual.RyujinAudioEnvelope;
import com.bleachmod.common.visual.RyujinSealFlow;
import com.bleachmod.init.ModSounds;
import com.bleachmod.mixin.client.MusicManagerAccess;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.*;
import net.minecraft.sounds.*;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import java.util.*;

/** Real-time score and authored-beat voice cues. No repeated bursts on a paused tick. */
final class RyujinBankaiAudio {
    private static final int BANKAI_V15_LINE_TICKS = 37, NAME_V15_LINE_TICKS = 38;
    private static final class State {
        final boolean newMix = resourceAvailable("ryujin_audio_15s.json");
        double lastAge=-1;
        double bankaiStarted=Double.NaN, nameStarted=Double.NaN;
        boolean bankai,name,extinguished,musicStarted,fireStarted,preheatStarted,voiceBurst,exploded,roared,absorbed,sealed;
        Score score,fire,preheat,roar;
        Score impact,spread,crackle,ash;
        final List<SoundInstance> playing=new ArrayList<>();
    }
    private static final Map<Integer,State> STATES=new HashMap<>();
    private RyujinBankaiAudio() { }
    private static final class Score extends AbstractTickableSoundInstance {
        float gain;
        Score(String event, SoundSource source) {
            this(event,source,Vec3.ZERO,true,false);
        }
        Score(String event,SoundSource source,Vec3 position,boolean local,boolean loop) {
            super(SoundEvent.createVariableRangeEvent(Reference.id(event)),source,RandomSource.create());
            relative=local;attenuation=local?Attenuation.NONE:Attenuation.LINEAR;volume=0;looping=loop;
            follow(position);
        }
        void follow(Vec3 position){if(!relative){x=position.x;y=position.y;z=position.z;}}
        @Override public boolean canStartSilent(){return true;}
        @Override public void tick(){volume=gain;}
    }
    static void stop(int owner){var s=STATES.remove(owner);if(s!=null)stopSounds(s);}
    static boolean suppressesVanillaMusic(int owner) {
        var s = STATES.get(owner);
        var mc = Minecraft.getInstance();
        return s != null && s.score != null && ClientVisualConfig.RYUJIN_MUSIC.get()
                && mc.options.getSoundSourceVolume(SoundSource.MUSIC) > 0
                && mc.getSoundManager().isActive(s.score);
    }
    private static void stopSounds(State s){
        var manager=Minecraft.getInstance().getSoundManager();
        s.playing.forEach(manager::stop);
        for (Score sound : new Score[]{s.score,s.fire,s.preheat,s.roar,s.impact,s.spread,s.crackle,s.ash}) {
            if (sound!=null) manager.stop(sound);
        }
        s.playing.clear();
        s.score=s.fire=s.preheat=s.roar=null;
        s.impact=s.spread=s.crackle=s.ash=null;
    }
    static void clear(){STATES.values().forEach(RyujinBankaiAudio::stopSounds);STATES.clear();}
    static void retain(Set<Integer> owners){for(int id:List.copyOf(STATES.keySet()))if(!owners.contains(id))stop(id);}
    private static void play(State s,SoundEvent event,SoundSource source,float volume,float pitch,Vec3 p,boolean local){
        var sound=new SimpleSoundInstance(event.getLocation(),source,volume,pitch,RandomSource.create(),false,0,
                local?SoundInstance.Attenuation.NONE:SoundInstance.Attenuation.LINEAR,local?0:p.x,local?0:p.y,local?0:p.z,local);
        s.playing.add(sound);Minecraft.getInstance().getSoundManager().play(sound);
        trace(event.getLocation().toString());
    }
    private static boolean resourceAvailable(String path) {
        return Minecraft.getInstance().getResourceManager().getResource(Reference.id(path)).isPresent();
    }
    private static boolean localAudioAvailable(String effect) {
        return resourceAvailable("sounds/ryujin_v7_" + effect + ".ogg");
    }
    private static Score playScore(State s, String event, SoundSource source, Vec3 position,
                                   boolean local, boolean loop, float gain) {
        Score sound=new Score(event,source,position,local,loop);
        sound.gain=gain;
        s.playing.add(sound);
        Minecraft.getInstance().getSoundManager().play(sound);
        trace(Reference.id(event).toString());
        return sound;
    }
    private static Score playNewEffect(State s,String effect,Vec3 p,boolean local,float gain) {
        String id="ryujin_v15_"+effect;
        return resourceAvailable("sounds/"+id+".ogg")
                ?playScore(s,"effect."+id,SoundSource.PLAYERS,p,local,false,gain):null;
    }
    private static void trace(String event) {
        if (Boolean.getBoolean("bleachmod.ryujinBankaiQa")) {
            com.mojang.logging.LogUtils.getLogger().info("Ryujin audio cue {}",event);
        }
    }
    private static float voiceRelease(double raw,double cue,double playedAt,boolean visited,int lineTicks) {
        if (visited && !Double.isFinite(playedAt)) return 1;
        return RyujinAudioEnvelope.speechRelease(raw,Double.isFinite(playedAt)?playedAt:cue,lineTicks);
    }
    private static void setGain(Score sound,Vec3 p,float gain) {
        if(sound!=null){sound.follow(p);sound.gain=gain;}
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
        if(local && !s.musicStarted && raw<10 && ClientVisualConfig.RYUJIN_MUSIC.get()
                && mc.options.getSoundSourceVolume(SoundSource.MUSIC)>0
                && resourceAvailable("sounds/ryujin_entrance_score.ogg")){
            s.musicStarted=true;
            var music = mc.getMusicManager();
            var background = ((MusicManagerAccess) music).bleachmod$currentMusic();
            if (background != null && "minecraft".equals(background.getLocation().getNamespace())) {
                music.stopPlaying();
                trace("vanilla music interrupted");
            }
            s.score=playScore(s,"music.ryujin_entrance",SoundSource.MUSIC,p,true,false,0);
            if(Boolean.getBoolean("bleachmod.ryujinBankaiQa")){
                mc.getResourceManager().getResource(Reference.id("sounds/ryujin_entrance_score.ogg"))
                        .ifPresent(resource->com.mojang.logging.LogUtils.getLogger()
                                .info("Ryujin score resource pack {}",resource.sourcePackId()));
            }
        }
        if(!s.newMix && local && !s.fireStarted && raw<10){
            s.fireStarted=true;s.fire=new Score("effect.ryujin_solar_fire",SoundSource.PLAYERS);s.playing.add(s.fire);mc.getSoundManager().play(s.fire);
        }
        if(!s.preheatStarted && raw<8){
            s.preheatStarted=true;
            if((local || RyujinSealFlow.enabled() || s.newMix) && localAudioAvailable("preheat")) {
                s.preheat=playScore(s,"effect.ryujin_v7_preheat",SoundSource.PLAYERS,p,local,false,0);
            } else if(!local) playLocalAudio(s,"preheat",.35F,p,false);
        }
        double bankaiAt=RyujinBankaiTimeline.realAge(RyujinBankaiTimeline.CUE_BANKAI,duration);
        double nameAt=RyujinBankaiTimeline.realAge(RyujinBankaiTimeline.CUE_NAME,duration);
        // Skip expired lines for late observers, including after an interrupted view.
        if(!s.bankai && age>=RyujinBankaiTimeline.CUE_BANKAI){
            s.bankai=true;
            if(raw<bankaiAt+8){
                s.bankaiStarted=raw;
                play(s,ModSounds.RYUJIN_VOICE_BANKAI.get(),SoundSource.VOICE,local?1:1.6F,1,p,local);
            }
        }
        if(!s.name && age>=RyujinBankaiTimeline.CUE_NAME){
            s.name=true;
            if(raw<nameAt+8){
                s.nameStarted=raw;
                play(s,ModSounds.RYUJIN_VOICE_NAME.get(),SoundSource.VOICE,local?1:1.6F,1,p,local);
            }
        }
        boolean speaking=(raw>=bankaiAt-3 && raw<bankaiAt+32) || (raw>=nameAt-3 && raw<nameAt+50);
        float speech=speaking?0:1;
        if(s.newMix){
            speech=Math.min(voiceRelease(raw,bankaiAt,s.bankaiStarted,s.bankai,BANKAI_V15_LINE_TICKS),
                    voiceRelease(raw,nameAt,s.nameStarted,s.name,NAME_V15_LINE_TICKS));
        }
        double fade=Math.min(1,raw/8)*Math.min(1,(duration-raw)/20.0);
        if(s.score!=null){
            s.score.gain=ClientVisualConfig.RYUJIN_MUSIC.get()?(float)((s.newMix?.42:.55)*fade*(.18+.82*speech)):0;
        }
        if(s.fire!=null)s.fire.gain=(float)((age<RyujinBankaiTimeline.FLAME_BURST?.10:age<RyujinBankaiTimeline.ABSORPTION_END?.45:.14)*fade*(speaking?.3:1));
        if(s.preheat!=null)s.preheat.gain=(float)(.30*fade*(speaking?.18:1));
        if(RyujinSealFlow.enabled() || s.newMix){
            var gains=RyujinAudioEnvelope.sample(raw,duration);
            if(s.fire!=null){s.fire.gain=gains.fire()*(speaking?.3F:1);if(age>=52){mc.getSoundManager().stop(s.fire);s.fire=null;}}
            if(s.preheat!=null){setGain(s.preheat,p,gains.preheat()*(.18F+.82F*speech));if(age>=20){mc.getSoundManager().stop(s.preheat);s.preheat=null;}}
            if(s.roar!=null){
                setGain(s.roar,p,gains.roar()*(s.newMix?.72F:1)*(local?1:1.52F)*(.3F+.7F*speech));
                if(age>=52){mc.getSoundManager().stop(s.roar);s.roar=null;}
            }
        }
        if(!s.voiceBurst && raw>=40){
            s.voiceBurst=true;
            if(raw<48){
                // A restrained impact under the word; the full explosion remains at tick 70.
                if(!s.newMix)play(s,SoundEvents.GENERIC_EXPLODE,SoundSource.PLAYERS,local?.16F:.27F,1.28F,p,local);
                play(s,SoundEvents.FIRECHARGE_USE,SoundSource.PLAYERS,s.newMix?.11F:local?.24F:.38F,.9F,p,local);
            }
        }
        if(!s.exploded && raw>=70){
            s.exploded=true;
            if(raw<78){
                if(s.newMix && localAudioAvailable("flame_explosion")){
                    s.impact=playScore(s,"effect.ryujin_v7_flame_explosion",SoundSource.PLAYERS,p,local,false,.27F+.51F*speech);
                }else{
                    playLocalAudio(s,"flame_explosion",local?.75F:1.15F,p,local);
                    play(s,SoundEvents.GENERIC_EXPLODE,SoundSource.PLAYERS,local?.42F:.65F,.68F,p,local);
                    play(s,SoundEvents.FIRECHARGE_USE,SoundSource.PLAYERS,local?.65F:.85F,.70F,p,local);
                }
            }
        }
        if(!s.roared && raw>=72){
            s.roared=true;
            if(raw<80){
                if((RyujinSealFlow.enabled() || s.newMix) && localAudioAvailable("solar_roar_loop")){
                    s.roar=playScore(s,"effect.ryujin_v7_solar_roar_loop",SoundSource.PLAYERS,p,local,true,0);
                } else playLocalAudio(s,"solar_roar_loop",local?.46F:.70F,p,local);
                if(s.newMix){
                    s.spread=playNewEffect(s,"flame_spread",p,local,0);
                    s.crackle=playNewEffect(s,"ember_crackle",p,local,0);
                }
            }
        }
        double absorptionAt=RyujinBankaiTimeline.realAge(RyujinBankaiTimeline.ABSORPTION_START,duration);
        double sealAt=RyujinBankaiTimeline.realAge(RyujinBankaiTimeline.ABSORPTION_END,duration);
        if(!s.absorbed && raw>=absorptionAt){s.absorbed=true;if(raw<absorptionAt+8)playLocalAudio(s,"flame_absorption",local?.65F:.9F,p,local);}
        if(!s.sealed && raw>=sealAt){
            s.sealed=true;
            if(raw<sealAt+8){
                playLocalAudio(s,"blade_seal",local?.72F:.9F,p,local);
                if(s.newMix)s.ash=playNewEffect(s,"seal_ash",p,local,.56F);
                if(!s.newMix || !localAudioAvailable("blade_seal"))
                    play(s,SoundEvents.FIRE_EXTINGUISH,SoundSource.PLAYERS,.40F,.76F,p,local);
            }
        }
        if(s.newMix){
            float fireBed=(float)(1-RyujinBankaiTimeline.smooth((raw-(absorptionAt-12))/12))*(.28F+.72F*speech);
            setGain(s.impact,p,.27F+.51F*speech);
            setGain(s.spread,p,.32F*fireBed);
            setGain(s.crackle,p,.48F*fireBed);
            setGain(s.ash,p,(float)(.56*fade*(.35+.65*speech)));
        }
        if(!s.newMix && !local && age<52 && (int)raw/35!=(int)Math.max(0,s.lastAge)/35)
            play(s,SoundEvents.FIRE_AMBIENT,SoundSource.PLAYERS,speaking?.06F:.20F,.65F,p,local);
        if(!s.extinguished && age>=52){s.extinguished=true;}
        s.lastAge=raw;
    }
}
