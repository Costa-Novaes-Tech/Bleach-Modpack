package com.bleachmod.client.model;

import com.bleachmod.Reference;
import com.bleachmod.client.cinematic.RyujinBankaiEntranceClient;
import com.bleachmod.common.visual.RyujinEyeFlame;
import com.bleachmod.common.visual.RyujinSealFlow;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/** Small authored flamelets, attached to the actual animated head, never a screen overlay. */
public final class RyujinEyeFlameLayer extends RenderLayer<AbstractClientPlayer,PlayerModel<AbstractClientPlayer>> {
    private static int cards;
    public static void beginFrame(){cards=0;}
    public static int cardCount(){return cards;}
    public RyujinEyeFlameLayer(RenderLayerParent<AbstractClientPlayer,PlayerModel<AbstractClientPlayer>> parent){super(parent);}
    @Override public void render(PoseStack pose,MultiBufferSource buffers,int light,AbstractClientPlayer player,
                                 float limbSwing,float limbAmount,float partial,float age,float headYaw,float headPitch){
        if(!RyujinSealFlow.enabled()||player.isInvisible()||!player.isAlive())return;
        double tick=RyujinBankaiEntranceClient.visualTick(player.getId(),partial);
        var flames=RyujinEyeFlame.sample(tick);
        if(flames.isEmpty())return;
        pose.pushPose();getParentModel().head.translateAndRotate(pose);
        var out=buffers.getBuffer(RyujinFlowRenderer.eyeType());
        for(var flame:flames)drawFlame(out,pose,flame,0,1);
        var trail=RyujinEyeFlame.trail(tick);
        out=buffers.getBuffer(RyujinFlowRenderer.eyeTrailType());
        for(int i=0;i<trail.size();i++){
            var t=trail.get(i);
            float x=(float)t.start().x,y=(float)t.start().y,z=(float)t.start().z;
            float ex=(float)t.end().x,ey=(float)t.end().y,ez=(float)t.end().z;
            float w=(float)t.startWidth()/2, endWidth=(float)t.endWidth()/2;
            float v=(float)i/trail.size(), endV=(float)(i+1)/trail.size();
            vertex(out,pose,x,y+w,z,0,v,t.startAlpha());vertex(out,pose,x,y-w,z,1,v,t.startAlpha());
            vertex(out,pose,ex,ey-endWidth,ez,1,endV,t.endAlpha());vertex(out,pose,ex,ey+endWidth,ez,0,endV,t.endAlpha());
        }
        int frame=Math.floorMod((int)Math.floor(tick/3),8);
        out=buffers.getBuffer(RyujinFlowRenderer.eyeCrownType());
        var core=RyujinEyeFlame.core(tick);
        float top=frame/8F,bottom=(frame+1)/8F;
        drawProfileFlame(out,pose,core,top,bottom);
        drawFlame(out,pose,core,top,bottom);
        cards+=2+flames.size()+trail.size();pose.popPose();
    }
    private static void drawFlame(VertexConsumer out,PoseStack pose,RyujinEyeFlame.Flame f,float top,float bottom){
        float x=(float)f.at().x,y=(float)f.at().y,z=(float)f.at().z;
        float w=(float)f.width()/2,h=(float)f.height()/2;
        vertex(out,pose,x-w,y+h,z,0,bottom,f.alpha());vertex(out,pose,x+w,y+h,z,1,bottom,f.alpha());
        vertex(out,pose,x+w,y-h,z,1,top,f.alpha());vertex(out,pose,x-w,y-h,z,0,top,f.alpha());
    }
    /** A shorter cross-section keeps a flame silhouette when the front card is seen edge-on. */
    private static void drawProfileFlame(VertexConsumer out,PoseStack pose,RyujinEyeFlame.Flame f,float top,float bottom){
        float x=(float)f.at().x,y=(float)f.at().y,z=(float)f.at().z-.03F;
        float h=(float)f.height()*.43F,w=.078F;
        int alpha=(int)(f.alpha()*.78);
        vertex(out,pose,x,y+h,z-w,0,bottom,alpha,true);vertex(out,pose,x,y+h,z+w,1,bottom,alpha,true);
        vertex(out,pose,x,y-h,z+w,1,top,alpha,true);vertex(out,pose,x,y-h,z-w,0,top,alpha,true);
    }
    private static void vertex(VertexConsumer out,PoseStack pose,float x,float y,float z,float u,float v,int alpha){
        vertex(out,pose,x,y,z,u,v,alpha,false);
    }
    private static void vertex(VertexConsumer out,PoseStack pose,float x,float y,float z,float u,float v,int alpha,boolean profile){
        out.vertex(pose.last().pose(),x,y,z).color(255,225,135,alpha).uv(u,v)
                .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT).normal(profile?-1:0,0,profile?0:-1).endVertex();
    }
    @Mod.EventBusSubscriber(modid=Reference.MOD_ID,value=Dist.CLIENT,bus=Mod.EventBusSubscriber.Bus.MOD)
    public static final class Registration {
        @SubscribeEvent public static void layers(EntityRenderersEvent.AddLayers event){
            for(String skin:event.getSkins()){
                var renderer=event.getSkin(skin);
                if(renderer instanceof net.minecraft.client.renderer.entity.player.PlayerRenderer player)
                    player.addLayer(new RyujinEyeFlameLayer(player));
            }
        }
    }
}
