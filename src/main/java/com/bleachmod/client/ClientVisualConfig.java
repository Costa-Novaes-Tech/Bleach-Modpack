package com.bleachmod.client;

import net.minecraftforge.common.ForgeConfigSpec;

/** Local presentation only; disabling cinematics never skips server timings. */
public final class ClientVisualConfig {
    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.BooleanValue CINEMATIC_FOV;
    static {var b=new ForgeConfigSpec.Builder();CINEMATIC_FOV=b.comment("Subtle FOV contraction during Bankai release; no forced perspective or invulnerability.").define("cinematicReleaseFov",true);SPEC=b.build();}
    private ClientVisualConfig(){ }
}
