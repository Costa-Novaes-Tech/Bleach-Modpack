package com.bleachmod.common.visual;

/** Presentation cues in server-scene ticks, independent of camera perspective and sound sliders. */
public final class BenihimeAudioTimeline {
    public enum Cue {
        BANKAI(28,"voice.benihime_bankai",true,1F,1F),
        CURTAIN(72,"benihime.curtain",false,.7F,1F),
        GRIP(176,"benihime.curtain",false,.35F,1.2F),
        REVEAL(236,"benihime.reveal",false,.8F,1F),
        THREADS(450,"benihime.threads",false,.6F,1F),
        NAME(460,"voice.benihime_name",true,1F,1F);
        public final int tick;public final String event;public final boolean voice;public final float volume,pitch;
        Cue(int tick,String event,boolean voice,float volume,float pitch){this.tick=tick;this.event=event;this.voice=voice;this.volume=volume;this.pitch=pitch;}
    }
    private BenihimeAudioTimeline(){ }
    /** Never replay past dialogue for a late observer or repeat it on an unchanged tick. */
    public static boolean due(double previous,double age,Cue cue){return Double.isFinite(previous)&&Double.isFinite(age)&&previous<cue.tick&&age>=cue.tick&&age-cue.tick<=8;}
}
