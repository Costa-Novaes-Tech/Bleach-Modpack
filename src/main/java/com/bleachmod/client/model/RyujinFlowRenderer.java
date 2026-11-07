package com.bleachmod.client.model;

import com.bleachmod.Reference;
import com.bleachmod.common.visual.RyujinBankaiPresentation;
import com.bleachmod.common.visual.RyujinSealFlow;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import java.util.Comparator;

/** Camera-facing cross sections keep each strip's length aligned with its actual path. */
public final class RyujinFlowRenderer {
    private static final ResourceLocation FLOW_TEXTURE=texture("ryujin_seal_flow"), EYE_TEXTURE=texture("ryujin_eye_flame"),
            FIRE_TEXTURE=texture("ryujin_fire_sheet"), EYE_CROWN_TEXTURE=texture("ryujin_eye_crown"),
            FAR_FIRE_TEXTURE=texture("ryujin_far_flame_sheet"), SMOKE_TEXTURE=texture("ryujin_backdrop_smoke");
    private static final class Types extends RenderType {
        private Types(){super("ryujin_flow_types",DefaultVertexFormat.NEW_ENTITY,VertexFormat.Mode.QUADS,256,false,true,()->{},()->{});}
        static final RenderType FLOW=create("ryujin_seal_flow",DefaultVertexFormat.NEW_ENTITY,VertexFormat.Mode.QUADS,1536,false,true,
                CompositeState.builder().setShaderState(new ShaderStateShard(RyujinSolarShader::get))
                        .setTextureState(new TextureStateShard(FLOW_TEXTURE,false,false))
                        .setTransparencyState(TRANSLUCENT_TRANSPARENCY).setCullState(NO_CULL)
                        .setLightmapState(LIGHTMAP).setOverlayState(OVERLAY).setWriteMaskState(COLOR_WRITE).createCompositeState(false));
        static final RenderType EYE=create("ryujin_eye_flame",DefaultVertexFormat.NEW_ENTITY,VertexFormat.Mode.QUADS,256,false,true,
                CompositeState.builder().setShaderState(new ShaderStateShard(RyujinSolarShader::get))
                        .setTextureState(new TextureStateShard(EYE_TEXTURE,false,false))
                        .setTransparencyState(TRANSLUCENT_TRANSPARENCY).setCullState(NO_CULL)
                        .setLightmapState(LIGHTMAP).setOverlayState(OVERLAY).setWriteMaskState(COLOR_WRITE).createCompositeState(false));
        static final RenderType FIRE=create("ryujin_authored_fire",DefaultVertexFormat.NEW_ENTITY,VertexFormat.Mode.QUADS,4096,false,true,
                CompositeState.builder().setShaderState(new ShaderStateShard(RyujinSolarShader::get))
                        .setTextureState(new TextureStateShard(FIRE_TEXTURE,false,false))
                        .setTransparencyState(TRANSLUCENT_TRANSPARENCY).setCullState(NO_CULL)
                        .setLightmapState(LIGHTMAP).setOverlayState(OVERLAY).setWriteMaskState(COLOR_WRITE).createCompositeState(false));
        static final RenderType EYE_CROWN=flameType("ryujin_eye_crown",256);
        static final RenderType FAR_FIRE=flameType("ryujin_far_flame_sheet",4096);
        static final RenderType SMOKE=create("ryujin_backdrop_smoke",DefaultVertexFormat.NEW_ENTITY,VertexFormat.Mode.QUADS,512,false,true,
                CompositeState.builder().setShaderState(new ShaderStateShard(GameRenderer::getRendertypeEntityTranslucentShader))
                        .setTextureState(new TextureStateShard(SMOKE_TEXTURE,false,false))
                        .setTransparencyState(TRANSLUCENT_TRANSPARENCY).setCullState(NO_CULL)
                        .setLightmapState(LIGHTMAP).setOverlayState(OVERLAY).setWriteMaskState(COLOR_WRITE).createCompositeState(false));

        private static RenderType flameType(String name,int size){
            return create(name,DefaultVertexFormat.NEW_ENTITY,VertexFormat.Mode.QUADS,size,false,true,
                    CompositeState.builder().setShaderState(new ShaderStateShard(RyujinSolarShader::get))
                            .setTextureState(new TextureStateShard(texture(name),false,false))
                            .setTransparencyState(TRANSLUCENT_TRANSPARENCY).setCullState(NO_CULL)
                            .setLightmapState(LIGHTMAP).setOverlayState(OVERLAY).setWriteMaskState(COLOR_WRITE).createCompositeState(false));
        }
    }
    private RyujinFlowRenderer(){ }
    private static ResourceLocation texture(String name){return Reference.id("textures/effect/"+name+".png");}
    private static RenderType compatible(RenderType nativeType,ResourceLocation texture,boolean pack){
        return pack?RenderType.entityTranslucentEmissive(texture):nativeType;
    }
    public static RenderType eyeType(){return compatible(Types.EYE,EYE_TEXTURE,RyujinSolarShader.shaderPackActive());}
    public static RenderType eyeTrailType(){return compatible(Types.FLOW,FLOW_TEXTURE,RyujinSolarShader.shaderPackActive());}
    public static RenderType flowType(){return compatible(Types.FLOW,FLOW_TEXTURE,RyujinSolarShader.usePackMaterial());}
    public static RenderType fireType(){return compatible(Types.FIRE,FIRE_TEXTURE,RyujinSolarShader.usePackMaterial());}
    public static RenderType eyeCrownType(){return compatible(Types.EYE_CROWN,EYE_CROWN_TEXTURE,RyujinSolarShader.shaderPackActive());}
    public static RenderType farFireType(){return compatible(Types.FAR_FIRE,FAR_FIRE_TEXTURE,RyujinSolarShader.usePackMaterial());}
    public static RenderType smokeType(){return RyujinSolarShader.usePackMaterial()?RenderType.entityTranslucent(SMOKE_TEXTURE):Types.SMOKE;}
    public static int render(PoseStack pose,Vec3 camera,Vec3 anchor,Vec3 forward,double phase,double tick,RyujinBankaiPresentation.Blade blade){
        var strips=RyujinSealFlow.sample(anchor,forward,phase,tick,blade);
        if(strips.isEmpty())return 0;
        strips.sort(Comparator.comparingDouble((RyujinSealFlow.Strip s)->s.start().lerp(s.end(),.5).distanceToSqr(camera)).reversed());
        var buffers=Minecraft.getInstance().renderBuffers().bufferSource();
        var type=flowType();
        var out=buffers.getBuffer(type);
        for(var strip:strips){
            Vec3 a=strip.start(),b=strip.end();
            Vec3 wa=width(strip.startTangent(),camera.subtract(a)).scale(strip.startWidth()*.5);
            Vec3 wb=width(strip.endTangent(),camera.subtract(b)).scale(strip.endWidth()*.5);
            vertex(out,pose,camera,a.subtract(wa),0,0,strip);vertex(out,pose,camera,a.add(wa),1,0,strip);
            vertex(out,pose,camera,b.add(wb),1,1,strip);vertex(out,pose,camera,b.subtract(wb),0,1,strip);
        }
        buffers.endBatch(type);
        return strips.size();
    }
    private static Vec3 width(Vec3 tangent,Vec3 view){
        Vec3 width=tangent.cross(view).normalize();
        if(width.lengthSqr()<.01)width=tangent.cross(new Vec3(0,1,0)).normalize();
        return width.lengthSqr()<.01?new Vec3(1,0,0):width;
    }
    private static void vertex(VertexConsumer out,PoseStack pose,Vec3 camera,Vec3 point,float u,float v,RyujinSealFlow.Strip strip){
        Vec3 p=point.subtract(camera);
        out.vertex(pose.last().pose(),(float)p.x,(float)p.y,(float)p.z)
                .color(255,(int)(175+65*strip.pulse()),(int)(70+85*strip.pulse()),strip.alpha()).uv(u,v)
                .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT).normal(0,1,0).endVertex();
    }
}
