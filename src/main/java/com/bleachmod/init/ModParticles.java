package com.bleachmod.init;

import com.bleachmod.Reference;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.*;

public final class ModParticles {
    public static final DeferredRegister<ParticleType<?>> TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Reference.MOD_ID);
    public static final RegistryObject<SimpleParticleType> SENBON_PETAL = TYPES.register("senbon_petal", () -> new SimpleParticleType(false));
    private ModParticles() { }
}
