package com.bleachmod.client.particle;

import com.bleachmod.common.network.s2c.SenbonPetalsS2C;
import com.bleachmod.common.visual.SenbonGeometry;
import com.bleachmod.init.ModParticles;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.*;
import java.util.Random;
import static com.bleachmod.common.network.s2c.SenbonPetalsS2C.*;

public final class SenbonPetalEffects {
    private static net.minecraft.client.multiplayer.ClientLevel previous;
    private static long tick;
    private static int emitted;
    private SenbonPetalEffects() { }
    public static void draw(SenbonPetalsS2C m) {
        var mc=Minecraft.getInstance(); var level=mc.level;
        if (level==null || mc.player==null || !m.valid() || mc.player.distanceToSqr(m.origin())>64*64) return;
        if (previous!=level || tick!=level.getGameTime()) { previous=level; tick=level.getGameTime(); emitted=0; }
        var random=new Random(m.seed()); Vec3 o=m.origin(), f=m.forward().normalize(), left=SenbonGeometry.left(f);
        int count=m.shape()==AURA ? 16 : m.shape()==ARC ? 84 : 96;
        for (int i=0;i<count && emitted<256;i++) {
            double a=2*Math.PI*i/count, r=m.radius(); Vec3 p, v=Vec3.ZERO;
            switch(m.shape()) {
                case ARC -> {
                    double t=Math.max(0,m.progress()-1.0/SenbonGeometry.CUT_TICKS)+random.nextDouble()/SenbonGeometry.CUT_TICKS;
                    t=Math.min(m.progress(),t); double distance=r*(.25+.75*random.nextDouble());
                    p=SenbonGeometry.cut(o,f,m.pattern(),t,distance);
                    v=SenbonGeometry.cut(o,f,m.pattern(),Math.min(1,t+.02),distance).subtract(p).scale(.18);
                }
                case STREAM -> { double d=r*random.nextDouble(); p=o.add(f.scale(d)).add(left.scale((random.nextDouble()-.5)*.5)); v=f.scale(.12); }
                case CONVERGE -> { double rr=r*(1-m.progress()); p=o.add(Math.cos(a)*rr,random.nextDouble()*2,Math.sin(a)*rr); v=o.add(0,1,0).subtract(p).normalize().scale(.12); }
                case GUARD, AURA -> { double rr=m.shape()==AURA ? 1 : 1.5; p=o.add(Math.cos(a+m.progress()*6)*rr,random.nextDouble()*2,Math.sin(a+m.progress()*6)*rr); v=new Vec3(-Math.sin(a)*.025,.015,Math.cos(a)*.025); }
                case FIELD, FLOWER -> {
                    double rr=m.shape()==FIELD ? r*Math.sqrt(random.nextDouble()) : SenbonGeometry.flowerRadius(r,a);
                    p=o.add(Math.cos(a)*rr,0,Math.sin(a)*rr);
                    if (!level.hasChunkAt(BlockPos.containing(p))) continue;
                    var hit=level.clip(new ClipContext(p.add(0,3,0),p.add(0,-3,0),ClipContext.Block.COLLIDER,ClipContext.Fluid.NONE,mc.player));
                    if (hit.getType()==HitResult.Type.MISS) continue;
                    p=hit.getLocation().add(0,.08,0);
                }
                case RISE -> { double rr=r*Math.sqrt(random.nextDouble()); p=o.add(Math.cos(a)*rr,random.nextDouble()*.4,Math.sin(a)*rr); v=new Vec3(Math.cos(a)*.02,.18+random.nextDouble()*.16,Math.sin(a)*.02); }
                case RADIAL -> { double rr=r*m.progress(); p=o.add(Math.cos(a)*rr,.3+random.nextDouble()*1.3,Math.sin(a)*rr); v=new Vec3(Math.cos(a)*.12,.015,Math.sin(a)*.12); }
                case SWORDS -> {
                    double angle=2*Math.PI*(i/6)/16+m.progress()*.4;
                    p=o.add(Math.cos(angle)*r,.3+(i%6)*.3,Math.sin(angle)*r);
                    v=m.progress()>.5 ? o.add(0,1,0).subtract(p).normalize().scale(.18) : Vec3.ZERO;
                }
                case WINGS -> { double side=i%2==0 ? -1 : 1, span=random.nextDouble()*r; p=o.add(f.scale(-.6-span*.15)).add(left.scale(side*span)).add(0,.8+Math.sin(span/r*Math.PI)*2,0); v=new Vec3(0,.025,0); }
                default -> { continue; }
            }
            if (!level.hasChunkAt(BlockPos.containing(p))) continue;
            if (m.shape()==WINGS && i%3!=0) level.addParticle(ParticleTypes.END_ROD,p.x,p.y,p.z,v.x,v.y,v.z);
            else level.addParticle(ModParticles.SENBON_PETAL.get(),p.x,p.y,p.z,v.x,v.y,v.z);
            emitted++;
        }
    }
}
