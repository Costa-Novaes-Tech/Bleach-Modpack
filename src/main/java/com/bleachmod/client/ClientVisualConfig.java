package com.bleachmod.client;

import net.minecraftforge.common.ForgeConfigSpec;

/** Local presentation only; disabling cinematics never skips server timings. */
public final class ClientVisualConfig {
    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.BooleanValue CINEMATIC_FOV,EXECUTION_CAMERA,PHOTON_PILOT;
    static {var b=new ForgeConfigSpec.Builder();CINEMATIC_FOV=b.comment("Subtle FOV contraction during Bankai release; no forced perspective or invulnerability.").define("cinematicReleaseFov",true);EXECUTION_CAMERA=b.comment("Directed Higashi preparation/impact/ash shots; movement, F5 or a menu cancels it.").define("cinematicExecutionCamera",true);PHOTON_PILOT=b.comment("Experimental Higashi embers and ash; requires Photon 1.1.17 + LDLib 1.0.42 Forge. No gameplay changes.").define("experimentalPhoton",false);SPEC=b.build();}
    private ClientVisualConfig(){ }
}
