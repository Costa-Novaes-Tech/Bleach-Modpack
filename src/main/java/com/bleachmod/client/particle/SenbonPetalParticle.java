package com.bleachmod.client.particle;

import com.bleachmod.Reference;
import com.bleachmod.init.ModParticles;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/** Vanilla cherry sprites with a controlled short trail instead of natural leaf drift. */
public final class SenbonPetalParticle extends TextureSheetParticle {
    private SenbonPetalParticle(ClientLevel level, double x,double y,double z,double dx,double dy,double dz, SpriteSet sprites) {
        super(level,x,y,z,dx,dy,dz); xd=dx; yd=dy; zd=dz;
        gravity=0; friction=.94F; lifetime=18+random.nextInt(9); hasPhysics=false;
        quadSize=.08F+random.nextFloat()*.07F; roll=random.nextFloat()*6.283F; oRoll=roll;
        pickSprite(sprites);
    }
    @Override public void tick() { oRoll=roll; super.tick(); roll+=.12F; alpha=1-(float)age/lifetime; }
    @Override public int getLightColor(float partial) { return 0xF000F0; }
    @Override public ParticleRenderType getRenderType() { return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT; }
    @Mod.EventBusSubscriber(modid=Reference.MOD_ID, value=Dist.CLIENT, bus=Mod.EventBusSubscriber.Bus.MOD)
    public static final class Registration {
        @SubscribeEvent public static void register(RegisterParticleProvidersEvent e) {
            e.registerSpriteSet(ModParticles.SENBON_PETAL.get(), sprites ->
                    (SimpleParticleType type, ClientLevel level, double x,double y,double z,double dx,double dy,double dz) ->
                            new SenbonPetalParticle(level,x,y,z,dx,dy,dz,sprites));
        }
    }
}
