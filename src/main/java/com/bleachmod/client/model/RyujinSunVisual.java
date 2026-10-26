package com.bleachmod.client.model;

import com.bleachmod.common.visual.RyujinBankaiTimeline;
import com.bleachmod.common.visual.RyujinBankaiPresentation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

/** Animated vanilla flame sprites in a bounded 3D fire volume. No untextured sun discs.
 * Particle emission is driven by client ticks, never by render FPS. */
public final class RyujinSunVisual {
    public static final int MAX_FIRE_CARDS = 272, MAX_PARTICLES_PER_SCENE_TICK = 12;
    private record Flame(Vec3 at, double width, double height, int alpha, boolean alternate) { }
    private RyujinSunVisual() { }
    private static double hash(int i) { return (Math.sin(i*127.1+311.7)*43758.5453)%1*.5+.5; }

    public static void render(PoseStack pose, Vec3 camera, Vec3 anchor, Vec3 forward, double age,
                              RyujinBankaiPresentation.Blade blade) {
        var mc=Minecraft.getInstance();
        if(mc.level==null || mc.player==null || camera.distanceToSqr(anchor)>128*128)return;
        Vec3 side=new Vec3(forward.z,0,-forward.x),center=RyujinBankaiPresentation.sunCenter(anchor,forward);
        double radius=RyujinBankaiTimeline.sunRadius(age), absorption=RyujinBankaiTimeline.absorption(age);
        var flames=new ArrayList<Flame>(MAX_FIRE_CARDS);
        if(radius>.08) {
            // Golden-angle distribution with depth: overlapping textured fire, not a flat polygon fan.
            for(int i=0;i<144;i++) {
                double a=i*2.3999632297, r=Math.sqrt((i+.5)/144.0)*radius*.88;
                double drift=Math.sin(age*.19+i*1.7)*radius*.025;
                Vec3 at=center.add(side.scale(Math.cos(a)*r+drift))
                        .add(0,Math.sin(a)*r*.85+Math.sin(age*.23+i)*radius*.035,0)
                        .add(forward.scale((hash(i)-.5)*radius*.65));
                flames.add(new Flame(at,radius*(.22+hash(i+3)*.10),radius*(.34+hash(i+9)*.14),225,i%2==0));
            }
            // Sparse rising tongues around the corona, all animated on the block texture atlas.
            for(int i=0;i<24;i++) {
                double a=i*Math.PI*2/24, r=radius*(.82+.06*Math.sin(age*.21+i));
                Vec3 at=center.add(side.scale(Math.cos(a)*r)).add(0,Math.sin(a)*r*.85,0)
                        .add(forward.scale(Math.sin(i*1.9)*radius*.18));
                flames.add(new Flame(at,radius*.20,radius*(.38+.12*hash(i)),205,i%2==0));
            }
        }
        if(absorption>.01) {
            // Separate textured flame packets travel around the actor and end on the posed katana.
            for(int stream=0;stream<6;stream++) {
                double a=stream*Math.PI*2/6;
                Vec3 from=center.add(side.scale(Math.cos(a)*Math.max(.2,radius*.85)))
                        .add(0,Math.sin(a)*Math.max(.2,radius*.85),0);
                Vec3 end=blade.hand().lerp(blade.tip(),.25+.12*stream);
                for(int bead=0;bead<16;bead++) {
                    double t=(bead+((age*.23+stream*.17)%1))/16;
                    Vec3 at=from.lerp(end,t).add(side.scale(Math.sin(t*Math.PI)*Math.cos(a)*1.7))
                            .add(0,Math.sin(t*Math.PI)*1.8,0);
                    double size=(.22+.80*(1-t))*absorption;
                    flames.add(new Flame(at,size,size*2.8,(int)(230*absorption),bead%2==0));
                }
            }
        }
        // Back-to-front sorting avoids opaque-looking intersecting sprite planes.
        flames.sort(Comparator.comparingDouble((Flame f)->f.at.distanceToSqr(camera)).reversed());
        var type=RenderType.entityTranslucentEmissive(InventoryMenu.BLOCK_ATLAS);
        var buffers=mc.renderBuffers().bufferSource();var out=buffers.getBuffer(type);
        var rotation=mc.gameRenderer.getMainCamera().rotation();
        var rv=new org.joml.Vector3f(1,0,0).rotate(rotation);var uv=new org.joml.Vector3f(0,1,0).rotate(rotation);
        Vec3 right=new Vec3(rv.x,rv.y,rv.z),up=new Vec3(uv.x,uv.y,uv.z);
        for(var fire:flames) sprite(out,pose.last().pose(),camera,fire,right,up,
                fire.alternate?ModelBakery.FIRE_1.sprite():ModelBakery.FIRE_0.sprite());
        buffers.endBatch(type);
    }
    private static void sprite(VertexConsumer out, Matrix4f matrix, Vec3 cam, Flame f, Vec3 right, Vec3 up, TextureAtlasSprite tex) {
        Vec3 c=f.at.subtract(cam),r=right.scale(f.width*.5),u=up.scale(f.height*.5);
        vertex(out,matrix,c.subtract(r).subtract(u),tex.getU0(),tex.getV1(),f.alpha);
        vertex(out,matrix,c.add(r).subtract(u),tex.getU1(),tex.getV1(),f.alpha);
        vertex(out,matrix,c.add(r).add(u),tex.getU1(),tex.getV0(),f.alpha);
        vertex(out,matrix,c.subtract(r).add(u),tex.getU0(),tex.getV0(),f.alpha);
    }
    private static void vertex(VertexConsumer out,Matrix4f matrix,Vec3 p,float u,float v,int alpha) {
        out.vertex(matrix,(float)p.x,(float)p.y,(float)p.z).color(255,255,255,alpha).uv(u,v)
                .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT).normal(0,1,0).endVertex();
    }
    /** Returns emitted count so the caller can enforce a global multi-caster budget. */
    public static int emit(Vec3 anchor,Vec3 forward,double age,RyujinBankaiPresentation.Blade blade,int allowance) {
        var mc=Minecraft.getInstance();
        if(mc.level==null || mc.isPaused() || mc.player==null || mc.player.distanceToSqr(anchor)>96*96)return 0;
        int limit=Math.min(MAX_PARTICLES_PER_SCENE_TICK,allowance);
        if(mc.options.particles().get()==net.minecraft.client.ParticleStatus.MINIMAL)limit=Math.min(3,limit);
        else if(mc.options.particles().get()==net.minecraft.client.ParticleStatus.DECREASED)limit=Math.min(6,limit);
        Random rng=new Random(Double.doubleToLongBits(anchor.x+anchor.z*31)+(long)age*73);
        double radius=RyujinBankaiTimeline.sunRadius(age),abs=RyujinBankaiTimeline.absorption(age);
        Vec3 side=new Vec3(forward.z,0,-forward.x),center=RyujinBankaiPresentation.sunCenter(anchor,forward);
        int n=0;
        for(int i=0;i<limit;i++) {
            Vec3 p,velocity;net.minecraft.core.particles.SimpleParticleType particle;
            if(radius>1 && i<limit/2) {
                double a=rng.nextDouble()*Math.PI*2;
                p=center.add(side.scale(Math.cos(a)*radius*.8)).add(0,Math.sin(a)*radius*.8,0);
                particle=i%3==0?ParticleTypes.LARGE_SMOKE:ParticleTypes.FLAME;
                velocity=new Vec3(0,.08+rng.nextDouble()*.06,0);
            } else if(age<52) {
                p=anchor.add(side.scale((rng.nextDouble()-.5)*6)).add(forward.scale((rng.nextDouble()-.5)*3)).add(0,.3+rng.nextDouble()*2.5,0);
                particle=i%3==0?ParticleTypes.SMOKE:ParticleTypes.ASH;
                velocity=new Vec3(0,.025+rng.nextDouble()*.04,0);
            } else if(age<124 && i<3) {
                p=blade.hand().lerp(blade.tip(),rng.nextDouble());
                particle=i==0?ParticleTypes.SMOKE:ParticleTypes.ASH;
                velocity=new Vec3(0,.018+rng.nextDouble()*.015,0);
            } else continue;
            mc.level.addParticle(particle,p.x,p.y,p.z,velocity.x,velocity.y,velocity.z);n++;
            if(abs>.1 && i==limit-1 && n<limit)break;
        }
        return n;
    }
}
