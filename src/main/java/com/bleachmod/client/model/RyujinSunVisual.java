package com.bleachmod.client.model;

import com.bleachmod.common.visual.RyujinBankaiTimeline;
import com.bleachmod.common.visual.RyujinBankaiPresentation;
import com.bleachmod.common.visual.RyujinFireLayout;
import net.minecraft.client.particle.Particle;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

/**
 * Animated vanilla flame sprites in a bounded 3D fire volume.
 * Provides blade coating, grounded fire, a sustained rear curtain and solar corona,
 * then directed absorption onto the katana. Own particles never outlive their scene.
 *
 * Strict particle and card budget: emission is driven by client game ticks, never by render FPS.
 */
public final class RyujinSunVisual {
    public static final int MAX_FIRE_CARDS = RyujinFireLayout.CARD_LIMIT, MAX_PARTICLES_PER_SCENE_TICK = 12;

    private record Tracked(Particle particle,boolean flame,long born) { }
    private static final Map<Integer,List<Tracked>> PARTICLES=new HashMap<>();
    private static final Map<BlockPos,Double> GROUND_HEIGHTS=new HashMap<>();
    private static long groundTick=Long.MIN_VALUE;
    private static int renderedCards;
    private RyujinSunVisual() { }
    public static void remove(int owner){var list=PARTICLES.remove(owner);if(list!=null)list.forEach(p->p.particle.remove());}
    public static void clear(){
        for(int id:List.copyOf(PARTICLES.keySet()))remove(id);
        GROUND_HEIGHTS.clear();
        groundTick=Long.MIN_VALUE;
    }
    public static void retain(java.util.Set<Integer> owners){for(int id:List.copyOf(PARTICLES.keySet()))if(!owners.contains(id))remove(id);}
    public static int particleCount(){return PARTICLES.values().stream().mapToInt(List::size).sum();}
    public static int flameCount(){return (int)PARTICLES.values().stream().flatMap(List::stream).filter(p->p.flame&&p.particle.isAlive()).count();}
    public static int cardCount(){return renderedCards+RyujinEyeFlameLayer.cardCount();}
    public static void beginFrame(){renderedCards=0;}

    public static void render(PoseStack pose, Vec3 camera, Vec3 anchor, Vec3 forward, double age,
                              double realTick,RyujinBankaiPresentation.Blade blade) {
        var mc=Minecraft.getInstance();
        if(mc.level==null||mc.player==null||camera.distanceToSqr(anchor)>128*128)return;
        var flames=RyujinFireLayout.sample(anchor,forward,age,realTick,blade,com.bleachmod.client.ClientVisualConfig.RYUJIN_FIRE_DETAIL.get());
        renderedCards+=flames.size();
        flames.sort(Comparator.comparingDouble((RyujinFireLayout.Card f)->f.at().distanceToSqr(camera)).reversed());
        var type=com.bleachmod.common.visual.RyujinSealFlow.enabled()?RyujinFlowRenderer.fireType():RenderType.entityTranslucentEmissive(InventoryMenu.BLOCK_ATLAS);
        var buffers=mc.renderBuffers().bufferSource();
        var rv=new org.joml.Vector3f(1,0,0).rotate(mc.gameRenderer.getMainCamera().rotation());
        Vec3 right=new Vec3(rv.x,0,rv.z).normalize(), up=new Vec3(0,1,0);
        var smokeType=RyujinFlowRenderer.smokeType();
        VertexConsumer smoke=null;
        for(var fire:flames){
            if(fire.layer()!=RyujinFireLayout.Layer.SMOKE)continue;
            if(smoke==null)smoke=buffers.getBuffer(smokeType);
            backdrop(smoke,pose.last().pose(),camera,fire,right,up,realTick*.55,true);
        }
        if(smoke!=null)buffers.endBatch(smokeType);
        var farType=RyujinFlowRenderer.farFireType();
        VertexConsumer far=null;
        for(var fire:flames){
            if(fire.layer()!=RyujinFireLayout.Layer.BACKDROP)continue;
            if(far==null)far=buffers.getBuffer(farType);
            backdrop(far,pose.last().pose(),camera,fire,right,up,realTick,false);
        }
        if(far!=null)buffers.endBatch(farType);
        var out=buffers.getBuffer(type);
        for(var fire:flames){
            if(fire.layer()==RyujinFireLayout.Layer.BACKDROP||fire.layer()==RyujinFireLayout.Layer.SMOKE)continue;
            Vec3 at=fire.at();
            if(fire.layer()==RyujinFireLayout.Layer.LATERAL){
                double floor=groundHeight(mc,at.subtract(0,fire.height()*.5+.04,0));
                if(!Double.isFinite(floor))continue;
                at=new Vec3(at.x,floor+.04+fire.height()*.5,at.z);
            }
            sprite(out,pose.last().pose(),camera,fire,at,right,up,
                    fire.alternate()?ModelBakery.FIRE_1.sprite():ModelBakery.FIRE_0.sprite(),realTick);
        }
        buffers.endBatch(type);
        if(com.bleachmod.common.visual.RyujinSealFlow.enabled())
            renderedCards+=RyujinFlowRenderer.render(pose,camera,anchor,forward,age,realTick,blade);
    }

    private static double groundHeight(Minecraft mc,Vec3 position){
        long tick=mc.level.getGameTime();
        if(tick!=groundTick){GROUND_HEIGHTS.clear();groundTick=tick;}
        return GROUND_HEIGHTS.computeIfAbsent(BlockPos.containing(position),block->{
            Vec3 base=Vec3.atBottomCenterOf(block);
            var hit=mc.level.clip(new ClipContext(base.add(0,1.9,0),base.subtract(0,2.1,0),
                    ClipContext.Block.COLLIDER,ClipContext.Fluid.NONE,mc.player));
            return hit.getType()==HitResult.Type.BLOCK?hit.getLocation().y:Double.NaN;
        });
    }

    private static void backdrop(VertexConsumer out, Matrix4f matrix, Vec3 camera,
                                 RyujinFireLayout.Card fire, Vec3 right, Vec3 up, double tick, boolean smoke) {
        Vec3 c=fire.at().subtract(camera), r=right.scale(fire.width()*.5), u=up.scale(fire.height()*.5);
        int variant=Math.floorMod(fire.seed()*7+fire.seed()/4,4);
        int frame=Math.floorMod((int)Math.floor(tick/3)+fire.seed(),8);
        float u0=variant/4F, u1=(variant+1)/4F, v0=frame/8F, v1=(frame+1)/8F;
        int green=smoke?255:210, blue=smoke?255:112;
        vertex(out,matrix,c.subtract(r).subtract(u),u0,v1,fire.alpha(),green,blue);
        vertex(out,matrix,c.add(r).subtract(u),u1,v1,fire.alpha(),green,blue);
        vertex(out,matrix,c.add(r).add(u),u1,v0,fire.alpha(),green,blue);
        vertex(out,matrix,c.subtract(r).add(u),u0,v0,fire.alpha(),green,blue);
    }

    private static void sprite(VertexConsumer out, Matrix4f matrix, Vec3 cam, RyujinFireLayout.Card f, Vec3 at, Vec3 right, Vec3 up, TextureAtlasSprite tex,double tick) {
        Vec3 c = at.subtract(cam), r = right.scale(f.width() * 0.5), u = up.scale(f.height() * 0.5);
        float u0=tex.getU0(),u1=tex.getU1(),v0=tex.getV0(),v1=tex.getV1();
        if(com.bleachmod.common.visual.RyujinSealFlow.enabled()){
            int frame=Math.floorMod((int)Math.floor(tick*.5)+f.seed()*3,8);
            u0=f.alternate()?0:.5F;u1=u0+.5F;v0=frame/8F;v1=(frame+1)/8F;
        }
        vertex(out, matrix, c.subtract(r).subtract(u), u0, v1, f.alpha(), false);
        vertex(out, matrix, c.add(r).subtract(u), u1, v1, f.alpha(), false);
        vertex(out, matrix, c.add(r).add(u), u1, v0, f.alpha(), true);
        vertex(out, matrix, c.subtract(r).add(u), u0, v0, f.alpha(), true);
    }

    private static void vertex(VertexConsumer out, Matrix4f matrix, Vec3 p, float u, float v, int alpha, boolean tip) {
        vertex(out,matrix,p,u,v,alpha,tip?210:156,tip?112:48);
    }
    private static void vertex(VertexConsumer out,Matrix4f matrix,Vec3 p,float u,float v,int alpha,int green,int blue){
        out.vertex(matrix, (float) p.x, (float) p.y, (float) p.z).color(255,green,blue,alpha).uv(u, v)
                .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT).normal(0, 1, 0).endVertex();
    }

    /** Owned, tick-driven particles; phases never control the random clock. */
    public static int emit(int owner,Vec3 anchor,Vec3 forward,double age,long realTick,
                           RyujinBankaiPresentation.Blade blade,int allowance){
        var mc=Minecraft.getInstance();
        if(mc.level==null||mc.isPaused()||mc.player==null||mc.player.distanceToSqr(anchor)>96*96)return 0;
        var owned=PARTICLES.computeIfAbsent(owner,ignored->new ArrayList<>());
        owned.removeIf(p->!p.particle.isAlive());
        if(age>=52)owned.removeIf(p->{if(p.flame){p.particle.remove();return true;}return false;});
        else if(com.bleachmod.common.visual.RyujinSealFlow.enabled()&&age>=32)
            owned.removeIf(p->{if(p.flame&&realTick-p.born>=5){p.particle.remove();return true;}return false;});
        int limit=Math.min(MAX_PARTICLES_PER_SCENE_TICK,allowance);
        if(age<12)limit=Math.min(3,limit);else if(age<20)limit=Math.min(7,limit);
        if(mc.options.particles().get()==net.minecraft.client.ParticleStatus.MINIMAL)limit=Math.min(3,limit);
        else if(mc.options.particles().get()==net.minecraft.client.ParticleStatus.DECREASED)limit=Math.min(6,limit);
        if(age>=112||limit<=0)return 0;
        boolean sealedPlume=com.bleachmod.common.visual.RyujinSealFlow.enabled()&&age>=52;
        if(sealedPlume){
            if(blade==null||age>=108||realTick%2!=0)return 0;
            int plumeDensity=age<62?3:age<96?2:1;
            limit=Math.min(plumeDensity,limit);
        }
        Random rng=new Random(Double.doubleToLongBits(anchor.x+anchor.z*31)^realTick*0x9E3779B97F4A7C15L^owner);
        Vec3 side=new Vec3(forward.z,0,-forward.x),centre=RyujinBankaiPresentation.sunCenter(anchor,forward);
        double radius=RyujinBankaiTimeline.sunRadius(age);
        int emitted=0;
        for(int i=0;i<limit;i++){
            Vec3 p,velocity;
            net.minecraft.core.particles.SimpleParticleType type;
            if(age<52){
                if(com.bleachmod.common.visual.RyujinSealFlow.enabled()&&age>=32){
                    if(blade==null||i>=3)continue;
                    p=blade.hand().lerp(blade.tip(),.25+.6*rng.nextDouble());
                    velocity=new Vec3(0,.004,0);
                }else if(blade!=null&&i%3==0){
                    p=blade.hand().lerp(blade.tip(),.15+.8*rng.nextDouble());
                    velocity=new Vec3(0,.025+.025*rng.nextDouble(),0);
                }else if(age>=32){
                    if(blade==null)continue;
                    double contract=RyujinBankaiTimeline.progress(age,32,52);
                    p=centre.add(side.scale((rng.nextDouble()-.5)*6)).add(0,(rng.nextDouble()-.5)*4,0);
                    p=p.lerp(blade.hand().lerp(blade.tip(),rng.nextDouble()),contract);
                    velocity=blade.hand().lerp(blade.tip(),.6).subtract(p).normalize().scale(.16);
                }else{
                    p=anchor.subtract(forward.scale(2.8+rng.nextDouble()*3.5))
                            .add(side.scale((rng.nextDouble()-.5)*(age<20?5:12)))
                            .add(0,.1+rng.nextDouble()*(age<20?1.6:4),0);
                    if(age>=24&&i%4==1){
                        double a=rng.nextDouble()*Math.PI*2;
                        p=centre.add(side.scale(Math.cos(a)*radius*.85)).add(0,Math.sin(a)*radius*.8,0);
                    }
                    velocity=new Vec3((rng.nextDouble()-.5)*.025,.025+rng.nextDouble()*.035,0);
                }
                type=i%5==4?ParticleTypes.SMOKE:ParticleTypes.FLAME;
            }else if(sealedPlume){
                p=blade.hand().lerp(blade.tip(),.23+.70*rng.nextDouble()).add(0,.04,0);
                velocity=side.scale((rng.nextDouble()-.38)*.022)
                        .add(forward.scale((rng.nextDouble()-.5)*.016)).add(0,.026+.021*rng.nextDouble(),0);
                type=(i==0&&(age<96||realTick%4==0))?ParticleTypes.SMOKE:ParticleTypes.ASH;
            }else if(age<60&&blade!=null){
                if(i>=3)break;
                p=blade.hand().lerp(blade.tip(),rng.nextDouble());
                velocity=new Vec3(0,.018,0);type=i==0?ParticleTypes.SMOKE:ParticleTypes.ASH;
            }else{
                if(i>=2)break;
                p=anchor.add(side.scale((rng.nextDouble()-.5)*3)).add(0,.3+rng.nextDouble()*1.5,0);
                velocity=new Vec3(0,-.004,0);type=ParticleTypes.ASH;
            }
            Particle particle=mc.particleEngine.createParticle(type,p.x,p.y,p.z,velocity.x,velocity.y,velocity.z);
            if(particle!=null){
                if(sealedPlume){
                    particle.setParticleSpeed(velocity.x,velocity.y,velocity.z);
                    particle.setLifetime(type==ParticleTypes.SMOKE?24:21);
                }
                if(owned.size()>=192)owned.remove(0).particle.remove();
                owned.add(new Tracked(particle,type==ParticleTypes.FLAME,realTick));emitted++;
            }
        }
        return emitted;
    }
}
