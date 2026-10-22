package com.bleachmod.client.model;

import com.bleachmod.Reference;
import com.bleachmod.entity.IceDragonEntity;
import com.bleachmod.common.visual.HydraMotion;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/** Visual ice only: synchronized impact centers, no terrain replacement or collision. */
@Mod.EventBusSubscriber(modid=Reference.MOD_ID,value=Dist.CLIENT)
public final class HydraImpactVisuals {
    private HydraImpactVisuals() { }
    private static final java.util.Map<IceDragonEntity,Vec3[][]> SURFACES=new java.util.WeakHashMap<>();
    private static net.minecraft.client.multiplayer.ClientLevel world;
    private static Vec3[][] surface(IceDragonEntity hydra){
        if(SURFACES.size()>=24 && !SURFACES.containsKey(hydra))SURFACES.clear();
        return SURFACES.computeIfAbsent(hydra,key->{
            var mc=Minecraft.getInstance();Vec3[][] points=new Vec3[3][24];
            for(int head=0;head<3;head++)for(int i=0;i<24;i++){
                Vec3 center=hydra.impactPosition(head);double angle=i*Math.PI/12,r=i%3==0?2.6:5.5;
                Vec3 sample=center.add(Math.cos(angle)*r,0,Math.sin(angle)*r);
                if(!mc.level.hasChunkAt(net.minecraft.core.BlockPos.containing(sample)))continue;
                var clip=new net.minecraft.world.level.ClipContext(center.add(0,.6,0),sample.add(0,.8,0),net.minecraft.world.level.ClipContext.Block.COLLIDER,net.minecraft.world.level.ClipContext.Fluid.NONE,mc.player);
                if(mc.level.clip(clip).getType()!=net.minecraft.world.phys.HitResult.Type.MISS)continue;
                var hit=mc.level.clip(new net.minecraft.world.level.ClipContext(sample.add(0,2,0),sample.add(0,-6,0),net.minecraft.world.level.ClipContext.Block.COLLIDER,net.minecraft.world.level.ClipContext.Fluid.NONE,mc.player));
                if(hit.getType()!=net.minecraft.world.phys.HitResult.Type.MISS)points[head][i]=hit.getLocation().add(0,.03,0);
            }
            return points;
        });
    }
    @SubscribeEvent public static void cleanup(net.minecraftforge.event.TickEvent.ClientTickEvent e){
        if(e.phase==net.minecraftforge.event.TickEvent.Phase.END && world!=Minecraft.getInstance().level){SURFACES.clear();world=null;}
    }
    private static final class Types extends RenderType {
        private Types(){super("unused_hydra",DefaultVertexFormat.POSITION_COLOR,VertexFormat.Mode.QUADS,4096,false,true,()->{},()->{});}
        static final RenderType ICE=create("hydra_impact_ice",DefaultVertexFormat.POSITION_COLOR,VertexFormat.Mode.QUADS,4096,false,true,
                CompositeState.builder().setShaderState(RENDERTYPE_LIGHTNING_SHADER).setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                        .setCullState(NO_CULL).setWriteMaskState(COLOR_WRITE).setOutputState(PARTICLES_TARGET).createCompositeState(false));
    }
    @SubscribeEvent public static void render(RenderLevelStageEvent e){
        var mc=Minecraft.getInstance();if(e.getStage()!=RenderLevelStageEvent.Stage.AFTER_PARTICLES || mc.level==null || mc.player==null)return;
        if(world!=mc.level){SURFACES.clear();world=mc.level;}
        var buffers=mc.renderBuffers().bufferSource();var out=buffers.getBuffer(Types.ICE);var matrix=e.getPoseStack().last().pose();int count=0;
        for(var entity:mc.level.entitiesForRendering()){
            if(!(entity instanceof IceDragonEntity hydra) || !hydra.isAssault() || hydra.distanceToSqr(mc.player)>96*96)continue;
            if(++count>6)break;
            double age=hydra.sceneAge(e.getPartialTick());
            for(int head=0;head<3;head++){
                double t=age-HydraMotion.impactTick(head);if(t<0 || t>100 || (hydra.impactMask()&(1<<head))==0)continue;
                Vec3 worldCenter=hydra.impactPosition(head),center=worldCenter.subtract(e.getCamera().getPosition());
                var anchors=surface(hydra)[head];
                double grow=HydraMotion.smooth(t/8),fade=1-HydraMotion.smooth((t-65)/35);
                for(int i=0;i<24;i++){
                    double angle=i*Math.PI/12,r=(i%3==0?2.6:5.5)*grow;
                    if(anchors[i]==null)continue;
                    Vec3 base=worldCenter.lerp(anchors[i],grow).subtract(e.getCamera().getPosition());
                    Vec3 tip=base.add(Math.cos(angle)*.35,(.7+(i%5)*.28)*grow,Math.sin(angle)*.35);
                    double w=.3+(i%3)*.13;
                    Vec3[] ring={base.add(-w,0,-w),base.add(w,0,-w),base.add(w,0,w),base.add(-w,0,w)};
                    for(int face=0;face<4;face++)quad(out,matrix,ring[face],ring[(face+1)%4],tip,tip,(int)(210*fade),face);
                    if(anchors[(i+1)%24]!=null){
                        Vec3 next=worldCenter.lerp(anchors[(i+1)%24],grow).subtract(e.getCamera().getPosition());
                        quad(out,matrix,center,base,next,next,(int)(65*fade),2);
                    }
                }
            }
        }
        buffers.endBatch(Types.ICE);
    }
    private static void quad(VertexConsumer out,org.joml.Matrix4f m,Vec3 a,Vec3 b,Vec3 c,Vec3 d,int alpha,int face){
        for(var p:new Vec3[]{a,b,c,d})out.vertex(m,(float)p.x,(float)p.y,(float)p.z).color(face%2==0?130:205,225,255,Mth.clamp(alpha,0,255)).endVertex();
    }
}
