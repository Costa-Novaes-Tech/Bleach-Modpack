package com.bleachmod.common.visual;

/** Shared entrance clock, in server ticks. No client classes: also safe on dedicated servers. */
public final class HollowBossEntrance {
    public static final int DURATION = 160, LAND = 120, ROAR = 132;
    public static final int RIGHT_HAND_DOWN = 74, LEFT_HAND_DOWN = 94;
    public static final float PORTAL_Z = -2.0F;
    private HollowBossEntrance() { }

    public static float ease(float age, float from, float to) {
        float t = Math.max(0, Math.min(1, (age - from) / (to - from)));
        return t * t * (3 - 2 * t);
    }
    public static float aperture(float age) { return ease(age, 0, 30) * (1 - ease(age, 136, DURATION)); }
    /** Peek, pause, pull against the first palm, transfer weight, then take the final step. */
    public static float retreat(float age) {
        return -5.1F + 1.4F * ease(age,30,54) + 2.15F * ease(age,64,84)
                + 1.1F * ease(age,92,108) + .45F * ease(age,108,LAND);
    }
    public static float weight(float age) { return ease(age, 20, 42) * (1 - ease(age, 136, 156)); }
    public static float landing(float age) { return ease(age, 112, LAND) * (1 - ease(age, LAND, 131)); }
    public static float jaw(float age) { return 24 * ease(age, 126, ROAR) * (1 - ease(age, 140, 157)); }

    private static float pulse(float age,float a,float b,float c,float d) {
        return ease(age,a,b)*(1-ease(age,c,d));
    }
    public static float settle(float age) { return 1-ease(age,140,156); }
    public static float bodyY(float age) { return -9.5F*pulse(age,38,66,108,133)-2.5F*landing(age); }
    public static float bodyX(float age) {
        return -2.5F*pulse(age,62,76,84,98)+1.8F*pulse(age,88,100,112,126);
    }
    public static float bodyPitch(float age) { return -7*pulse(age,38,62,100,128); }
    public static float bodyYaw(float age) { return 7*pulse(age,40,60,76,92)-5*pulse(age,82,96,108,130); }
    public static float bodyRoll(float age) { return -3.5F*pulse(age,60,74,82,98)+3*pulse(age,88,102,110,128); }
    public static float chestPitch(float age) {
        return -11*pulse(age,40,60,90,120)-3*landing(age)+(float)Math.sin(age*.12)*.65F*weight(age);
    }
    public static float chestYaw(float age) { return 15*pulse(age,38,58,72,88)-12*pulse(age,78,92,104,126); }
    public static float headYaw(float age) {
        return -bodyYaw(age)-chestYaw(age)-10*pulse(age,46,55,60,70)+7*pulse(age,70,80,86,100);
    }
    public static float headPitch(float age) {
        return -bodyPitch(age)-chestPitch(age)-7*pulse(age,40,54,94,122);
    }

    /** Contact targets use model units, in the final actor's frame; +Z is behind it. */
    public record Contact(float z,float y,float lift,boolean planted) { }
    public static Contact hand(float age,boolean right) {
        float start=right?60:78, down=right?RIGHT_HAND_DOWN:LEFT_HAND_DOWN;
        float liftAt=right?96:112, finalDown=right?116:128;
        float anchor=right?16.8F:-3.2F;
        float z,y=14,lift=0;
        if(age<down) {
            float t=ease(age,start,down);
            z=(-9-retreat(age)*16)*(1-t)+anchor*t;
            lift=arc(t);y+=20*lift;
        } else if(age<liftAt) z=anchor;
        else {
            float t=ease(age,liftAt,finalDown);
            z=anchor+(-9-anchor)*t;lift=arc(t);y+=12*lift;
        }
        return new Contact(z,y,lift,age>=down && age<=liftAt || age>=finalDown);
    }
    public static Contact foot(float age,boolean right) {
        float start=right?78:90, down=right?102:LAND;
        // Keep toes behind the plane until the arms have started pulling the torso out.
        float anchor=right?52.8F:49.6F;
        float tuck=ease(age,30,60),t=ease(age,start,down),lift=arc(t);
        float z=(1-retreat(age)*16)*(1-tuck)+anchor*tuck;
        return new Contact(z+(1-z)*t,3+10*lift,lift,age>=60 && age<=start || age>=down);
    }
    private static float arc(float t) { float s=(float)Math.sin(Math.PI*t);return s*s; }
}
