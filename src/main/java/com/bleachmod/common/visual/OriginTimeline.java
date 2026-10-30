package com.bleachmod.common.visual;
import net.minecraft.world.phys.Vec3;
import net.minecraft.util.Mth;
/** Shared simulation ticks; reference shots are adapted to a thirteen-second opening. */
public final class OriginTimeline {
    public static final int THRUST = 174, BURST = 184, CUT = 222, END = 260;
    public record Shot(Vec3 eye, Vec3 target, float fov) { }
    private OriginTimeline() { }
    public static float ease(float p) { p=Mth.clamp(p,0,1);return p*p*(3-2*p); }
    public static double ease(double t, double from, double to) {
        double p = Mth.clamp((t - from) / (to - from), 0, 1); return p * p * (3 - 2 * p);
    }
    public static double rukiaDistance(double t) { return 3.0; }
    public static double playerAdvance(double t) { return 1.1 * ease(t,128,THRUST); }
    /** Local +X is the player's LEFT. Fishbone enters on -X: Rukia's left/player's right. */
    public static double bossDistance(double t) { return 14 - 10.2 * ease(t,180,CUT); }
    public static Vec3 bossOffset(double t) { return new Vec3(-bossDistance(t),0,.8); }
    public static float playerTurn(double t) {
        return (float)(Math.toDegrees(Math.atan2(3.8,.8-playerAdvance(END)))*ease(t,206,CUT));
    }
    public static Shot shot(float t) {
        if (t < 38) return new Shot(new Vec3(1.05,.9,1.35), new Vec3(.3,.85,2.3), 40);
        if (t < 106) return new Shot(new Vec3(-.8,1.03,1.45), new Vec3(0,1.06,3), 43);
        if (t < 144) return new Shot(new Vec3(.2,1.6,1.2), new Vec3(0,1.55,playerAdvance(t)), 42);
        if (t < 184) return new Shot(new Vec3(4.8,1.5,1.5), new Vec3(0,1,1.8), 52);
        if (t < 218) return new Shot(new Vec3(5.6,3.7,-6.5), new Vec3(-2,1.7,1.6), 68);
        if (t < 240) return new Shot(new Vec3(4.5,6.4,-5.8), new Vec3(-1.8,2,1.4), 64);
        double p = ease(t, 240, END);
        return new Shot(new Vec3(1.5*(1-p),2.6-p,-2.2+3.3*p), new Vec3(-3.8,2,.8), 64);
    }
}
