package com.bleachmod.common.visual;

/** Real-time heat bed fades with the seal, independently of speech ducking. */
public final class RyujinAudioEnvelope {
    public record Gains(float fire,float preheat,float roar) { }
    private RyujinAudioEnvelope(){ }
    /** Keeps the backing low until the actual voice playback has had time to finish. */
    public static float speechRelease(double tick, double start, int lineTicks) {
        double attack = RyujinBankaiTimeline.smooth((tick - start + 3) / 3);
        double release = RyujinBankaiTimeline.smooth((tick - start - lineTicks) / 5);
        return (float) (1 - attack * (1 - release));
    }
    public static Gains sample(double raw,int duration){
        if(!Double.isFinite(raw)||duration<=0||raw<0||raw>=duration)return new Gains(0,0,0);
        double t=raw*RyujinBankaiTimeline.DURATION/duration;
        double rise=RyujinBankaiTimeline.smooth(t/8);
        double heat=1-RyujinBankaiTimeline.smooth((t-176)/26);
        float fire=(float)((t<70?.10:.45)*rise*heat);
        float preheat=(float)(.30*rise*(1-RyujinBankaiTimeline.smooth((t-52)/18)));
        float roar=(float)(.46*RyujinBankaiTimeline.smooth((t-72)/6)*heat);
        return new Gains(fire,preheat,roar);
    }
}
