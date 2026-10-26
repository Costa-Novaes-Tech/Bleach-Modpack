package com.bleachmod.client;

import net.minecraftforge.common.ForgeConfigSpec;

/** Local presentation only; disabling cinematics never skips server timings. */
public final class ClientVisualConfig {
    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.BooleanValue BENIHIME_CAMERA,BENIHIME_MUSIC,BENIHIME_PENUMBRA,BENIHIME_VOICE;
    public static final ForgeConfigSpec.BooleanValue CINEMATIC_FOV,EXECUTION_CAMERA,PHOTON_PILOT,RYUJIN_LONG,RYUJIN_MUSIC;
    static {var b=new ForgeConfigSpec.Builder();BENIHIME_CAMERA=b.comment("Directed Benihime entrance; movement, combat or F5 restores the player's view. No gameplay timing changes.").define("benihimeEntranceCamera",true);BENIHIME_MUSIC=b.comment("Original local music during Benihime's entrance; follows the Music volume slider.").define("benihimeEntranceMusic",true);BENIHIME_VOICE=b.comment("Provisional offline Japanese speech for Benihime; uses the Voice/Speech volume slider. No actor impersonation.").define("benihimeEntranceVoice",true);BENIHIME_PENUMBRA=b.comment("Subtle local dimming when looking at a nearby Benihime entrance. No status effects or world lighting changes.").define("benihimeEntrancePenumbra",true);CINEMATIC_FOV=b.comment("Subtle FOV contraction during Bankai release; no forced perspective or invulnerability.").define("cinematicReleaseFov",true);EXECUTION_CAMERA=b.comment("Directed Higashi preparation/impact/ash shots; movement, F5 or a menu cancels it.").define("cinematicExecutionCamera",true);PHOTON_PILOT=b.comment("Experimental Higashi embers and ash; requires Photon 1.1.17 + LDLib 1.0.42 Forge. No gameplay changes.").define("experimentalPhoton",false);RYUJIN_LONG=b.comment("Legacy preference retained for compatibility: both values select the 12-second Ryujin presentation. Gameplay release remains 60/40 ticks.").define("ryujinLongEntrance",true);RYUJIN_MUSIC=b.comment("Original 12-second Ryujin score; Music slider, ducked under voices. Fire effects use Players volume.").define("ryujinEntranceMusic",true);SPEC=b.build();}
    private ClientVisualConfig(){ }
}
