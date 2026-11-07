package com.bleachmod.client.model;

import com.bleachmod.Reference;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.PostPass;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.fml.ModList;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

/** Optional pre-HUD pass on the base framebuffer. The scene remains valid without it. */
public final class RyujinSolarPost {
    private static final boolean BLOOM=Boolean.getBoolean("bleachmod.ryujinSolarBloom");
    private static final boolean HEAT=Boolean.getBoolean("bleachmod.ryujinHeatRefraction");
    private static PostChain chain;
    private static PostPass material, copy;
    private static RenderTarget main, swap;
    private static boolean failed, logged;

    private RyujinSolarPost(){}

    public static boolean enabled(){
        return (BLOOM||HEAT)&&!failed&&!ModList.get().isLoaded("oculus");
    }

    public static void reset(){
        if(chain!=null)chain.close();
        chain=null;
        material=copy=null;
        main=swap=null;
        failed=false;
    }

    public static void render(RenderLevelStageEvent event,Vec3 center,double radius,double tick){
        if(!enabled()||radius<.35)return;
        var camera=event.getCamera();
        // Forge's AFTER_LEVEL event receives the projection stack, not the level's rotated view stack.
        var viewProjection=new Matrix4f(event.getProjectionMatrix())
                .rotateX((float)Math.toRadians(camera.getXRot()))
                .rotateY((float)Math.toRadians(camera.getYRot()+180));
        var origin=project(viewProjection,camera.getPosition(),center);
        if(origin==null)return;
        var right=new Vector3f(1,0,0).rotate(camera.rotation());
        var up=new Vector3f(0,1,0).rotate(camera.rotation());
        var edgeX=project(viewProjection,camera.getPosition(),center.add(right.x*radius,right.y*radius,right.z*radius));
        var edgeY=project(viewProjection,camera.getPosition(),center.add(up.x*radius,up.y*radius,up.z*radius));
        var near=project(viewProjection,camera.getPosition(),center.add(camera.getPosition().subtract(center).normalize().scale(radius*.72)));
        if(edgeX==null||edgeY==null||near==null)return;
        float rx=Math.abs(edgeX.x-origin.x),ry=Math.abs(edgeY.y-origin.y);
        if(rx<.006F||ry<.006F||rx>.8F||ry>1F||origin.x<-rx||origin.x>1+rx||origin.y<-ry||origin.y>1+ry)return;

        var mc=Minecraft.getInstance();
        var target=mc.getMainRenderTarget();
        if(!target.useDepth)return;
        try{
            if(chain==null||target!=main)initialize(mc,target);
            if(swap.width!=main.width||swap.height!=main.height)chain.resize(main.width,main.height);
            var shader=material.getEffect();
            shader.safeGetUniform("SolarMask").set(origin.x,origin.y,rx,ry);
            float strength=(float)Math.min(1,radius/7);
            shader.safeGetUniform("SolarTiming").set((float)tick,HEAT?strength:0,BLOOM?strength:0,near.z);
            material.process(event.getPartialTick());
            swap.copyDepthFrom(main);
            copy.process(event.getPartialTick());
            main.copyDepthFrom(swap);
            if(!logged){
                logged=true;
                LogUtils.getLogger().info("Ryujin solar post applied bloom={} refraction={}",BLOOM,HEAT);
            }
        }catch(java.io.IOException|RuntimeException error){
            reset();
            failed=true;
            LogUtils.getLogger().warn("Ryujin solar post unavailable; keeping base rendering",error);
        }finally{
            target.bindWrite(false);
        }
    }

    private static void initialize(Minecraft mc,RenderTarget target) throws java.io.IOException{
        reset();
        main=target;
        chain=new PostChain(mc.getTextureManager(),mc.getResourceManager(),main,Reference.id("shaders/post/ryujin_solar_post.json"));
        swap=chain.getTempTarget("ryujin_swap");
        material=chain.addPass("bleachmod:ryujin_solar_post",main,swap);
        material.addAuxAsset("SceneDepth",main::getDepthTextureId,main.width,main.height);
        copy=chain.addPass("blit",swap,main);
        chain.resize(main.width,main.height);
    }

    private static Vector3f project(Matrix4f viewProjection,Vec3 eye,Vec3 point){
        Vec3 p=point.subtract(eye);
        var clip=new Vector4f((float)p.x,(float)p.y,(float)p.z,1);
        viewProjection.transform(clip);
        if(clip.w<=.1F)return null;
        return new Vector3f(.5F+.5F*clip.x/clip.w,.5F+.5F*clip.y/clip.w,.5F+.5F*clip.z/clip.w);
    }
}
