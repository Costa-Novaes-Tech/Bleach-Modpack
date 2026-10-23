package com.bleachmod.client.model;

import com.bleachmod.Reference;
import com.bleachmod.entity.IceDragonEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public final class IceDragonRenderer extends GeoEntityRenderer<IceDragonEntity> {
    public IceDragonRenderer(EntityRendererProvider.Context context) { super(context, new Model()); }
    @Override public void render(IceDragonEntity dragon, float yaw, float partial, com.mojang.blaze3d.vertex.PoseStack pose,
                                 net.minecraft.client.renderer.MultiBufferSource buffers, int light) {
        pose.pushPose(); float scale = dragon.getPhantomSize() >= IceDragonEntity.BANKAI_SIZE ? IceDragonEntity.BANKAI_VISUAL_SCALE : 1.35F;
        if (dragon.isManifestation()) scale=1;
        pose.scale(scale,scale,scale);
        super.render(dragon,yaw,partial,pose,buffers,light);pose.popPose();
    }
    private static final class Model extends GeoModel<IceDragonEntity>{
        @Override public void setCustomAnimations(IceDragonEntity d,long instance,software.bernie.geckolib.core.animation.AnimationState<IceDragonEntity> state){
            super.setCustomAnimations(d,instance,state);
            if(!d.isManifestation())return;
            double age=d.sceneAge(net.minecraft.client.Minecraft.getInstance().getFrameTime());
            if(!d.isAssault())age=com.bleachmod.common.visual.HyorinmaruTimeline.motionAge(age);
            for(int head=0;head<3;head++){
                String name=com.bleachmod.common.visual.HydraMotion.NECKS[head];
                var root=getAnimationProcessor().getBone("root_"+name);if(root!=null)root.setHidden(d.isAssault() && age>=com.bleachmod.common.visual.HydraMotion.impactTick(head)+12);
                double growth=com.bleachmod.common.visual.HydraMotion.growth(d.isAssault(),head,age);
                for(int link=0;link<com.bleachmod.common.visual.HydraMotion.LINKS;link++){
                    var bone=getAnimationProcessor().getBone("neck_"+name+"_"+(link+1));if(bone==null)continue;
                    double a=(double)link/com.bleachmod.common.visual.HydraMotion.LINKS,b=(double)(link+1)/com.bleachmod.common.visual.HydraMotion.LINKS;
                    bone.setHidden(a>=growth);if(a>=growth)continue;
                    var from=d.curvePoint(head,age,a);
                    var to=d.curvePoint(head,age,Math.min(b,growth));
                    var delta=to.subtract(from);double length=Math.max(.001,delta.length());
                    bone.setPosX((float)(-from.x*16));bone.setPosY((float)(from.y*16));bone.setPosZ((float)(-from.z*16));
                    bone.setRotX((float)Math.asin(-delta.z/length));bone.setRotZ((float)Math.atan2(-delta.x,delta.y));bone.setRotY(0);
                    bone.setScaleY((float)(length*1.02));bone.setScaleX(1);bone.setScaleZ(1);
                }
                var bone=getAnimationProcessor().getBone("head_"+name);if(bone==null)continue;
                var tip=d.curvePoint(head,age,d.isAssault()?1:growth);
                bone.setPosX((float)(-tip.x*16));bone.setPosY((float)(tip.y*16));bone.setPosZ((float)(-tip.z*16));
                float scale=(float)com.bleachmod.common.visual.HydraMotion.smooth((growth-.65)/.35)*(head==0?1.1F:1);
                bone.setScaleX(scale);bone.setScaleY(scale);bone.setScaleZ(scale);bone.setHidden(scale<.01 || d.isAssault() && ((d.impactMask() & (1<<head))!=0 || age>=com.bleachmod.common.visual.HydraMotion.impactTick(head)));
                bone.setRotX(com.bleachmod.common.visual.HydraMotion.headRotX(d.isAssault(),head,age));bone.setRotY((float)Math.toRadians(head==1?-16:head==2?16:0));
                var jaw=getAnimationProcessor().getBone("jaw_"+name);if(jaw!=null)jaw.setRotX(com.bleachmod.common.visual.HydraMotion.jaw(d.isAssault(),head,age));
            }
        }
        private static final ResourceLocation HYDRA_GEO = Reference.id("geo/ice_hydra.geo.json");
        private static final ResourceLocation HYDRA_TEX = Reference.id("textures/entity/ice_hydra.png");
        private static final ResourceLocation HYDRA_ANIM = Reference.id("animations/ice_hydra.animation.json");
        private static final ResourceLocation DRAGON_GEO = Reference.id("geo/ice_dragon.geo.json");
        private static final ResourceLocation DRAGON_TEX = new ResourceLocation("minecraft", "textures/block/packed_ice.png");
        private static final ResourceLocation DRAGON_ANIM = Reference.id("animations/ice_dragon.animation.json");

        @Override public ResourceLocation getModelResource(IceDragonEntity d){
            return d.isManifestation() ? HYDRA_GEO : DRAGON_GEO;
        }
        @Override public ResourceLocation getTextureResource(IceDragonEntity d){
            return d.isManifestation() ? HYDRA_TEX : DRAGON_TEX;
        }
        @Override public ResourceLocation getAnimationResource(IceDragonEntity d){
            return d.isManifestation() ? HYDRA_ANIM : DRAGON_ANIM;
        }
        @Override public net.minecraft.client.renderer.RenderType getRenderType(IceDragonEntity d,ResourceLocation texture){
            return net.minecraft.client.renderer.RenderType.entityTranslucent(texture);
        }
    }
}
