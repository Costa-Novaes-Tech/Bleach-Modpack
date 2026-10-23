package com.bleachmod.client.model;

import com.bleachmod.common.visual.HollowBossEntrance;
import com.bleachmod.entity.HollowBossEntity;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.culling.Frustum;
import org.joml.Vector3f;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

/** The portal and clipped actor share the same origin and authoritative entrance clock. */
public final class HollowBossRenderer extends GeoEntityRenderer<HollowBossEntity> {
    private static final class Types extends RenderType {
        private Types() { super("unused",DefaultVertexFormat.POSITION_COLOR,VertexFormat.Mode.QUADS,256,false,false,()->{},()->{}); }
        static final RenderType GARGANTA = create("hollow_garganta",DefaultVertexFormat.POSITION_COLOR,
                VertexFormat.Mode.QUADS,4096,false,false,CompositeState.builder()
                .setShaderState(RENDERTYPE_LIGHTNING_SHADER).setTransparencyState(NO_TRANSPARENCY)
                .setCullState(NO_CULL).setWriteMaskState(COLOR_DEPTH_WRITE).createCompositeState(false));
    }
    public HollowBossRenderer(EntityRendererProvider.Context context) {
        super(context,new HollowBossModel()); shadowRadius=1.4F;
    }
    @Override public boolean shouldRender(HollowBossEntity boss, Frustum frustum, double x,double y,double z) {
        return boss.isEntering() ? frustum.isVisible(boss.getBoundingBox().inflate(7)) : super.shouldRender(boss,frustum,x,y,z);
    }
    @Override public void render(HollowBossEntity boss,float yaw,float partial,PoseStack pose,MultiBufferSource buffers,int light) {
        if (!net.minecraftforge.fml.loading.FMLEnvironment.production && Boolean.getBoolean("bleachmod.bossQa")) {
            for (String side : new String[]{"right","left"})
                for(String part:new String[]{"_fist_contact","_hand","_foot"})
                    getGeoModel().getBone(side+part).ifPresent(bone -> bone.setTrackingMatrices(true));
        }
        if (!boss.isEntering()) {
            shadowRadius=1.4F;
            super.render(boss,yaw,partial,pose,buffers,light);
            return;
        }
        shadowRadius=0; // No full-body shadow or fire/name layer before the body exists here.
        float age=boss.getEntranceAge(partial);
        portal(pose,buffers,age,boss.getEntranceYaw());
        double angle=Math.toRadians(boss.getEntranceYaw());
        Vector3f forward=new Vector3f((float)-Math.sin(angle),0,(float)Math.cos(angle));
        Vector3f point=new Vector3f(forward).mul(HollowBossEntrance.PORTAL_Z);
        pose.last().pose().transformPosition(point);
        Vector3f planeNormal=pose.last().pose().transformDirection(new Vector3f(forward)).normalize();
        MultiBufferSource clipped=type -> new PortalClippedBuffer(buffers.getBuffer(type),point,planeNormal);
        pose.pushPose();
        float retreat=HollowBossEntrance.retreat(age);
        pose.translate(forward.x*retreat,0,forward.z*retreat);
        super.render(boss,boss.getEntranceYaw(),partial,pose,clipped,light);
        pose.popPose();
    }
    private void portal(PoseStack pose,MultiBufferSource buffers,float age,float yaw) {
        float open=HollowBossEntrance.aperture(age);
        if (open<=0) return;
        pose.pushPose();pose.mulPose(Axis.YP.rotationDegrees(-yaw));
        pose.translate(0,3.12+1.5*(1-open),HollowBossEntrance.PORTAL_Z);
        var m=pose.last().pose();var out=buffers.getBuffer(Types.GARGANTA);
        // Two torn lips meet at pointed ends: an asymmetric wound, rather than a circular gate.
        int count=48;
        float[] xs=new float[count],ys=new float[count];
        for (int i=0;i<count;i++) {
            boolean upper=i<count/2;
            float u=upper ? -1+2F*i/(count/2-1) : 1-2F*(i-count/2)/(count/2-1);
            float envelope=(float)Math.pow(Math.max(0,1-Math.pow(Math.abs(u),1.45)),.72);
            float tear=(i%2==0?.92F:1.03F)+(float)Math.sin(i*7.13)*.065F;
            float tremor=(float)Math.sin(i*2.31+age*.14)*.012F*open;
            xs[i]=u*3.65F*HollowBossEntrance.ease(age,0,18)*(1-HollowBossEntrance.ease(age,138,160));
            ys[i]=(upper?1:-1)*envelope*3.08F*open*(tear+tremor);
        }
        for (int i=0;i<count;i++) {
            int j=(i+1)%count;
            quad(out,m,0,0,xs[i],ys[i],xs[j],ys[j],xs[j],ys[j],0,3,2,7);
            // Fractured outer lip and alternating pale inner teeth.
            quad(out,m,xs[i]*1.055F,ys[i]*1.055F,xs[j]*1.055F,ys[j]*1.055F,
                    xs[j],ys[j],xs[i],ys[i],-.012F,37,29,44);
            float inner=i%2==0?.974F:.99F;
            quad(out,m,xs[i],ys[i],xs[j],ys[j],xs[j]*inner,ys[j]*inner,
                    xs[i]*inner,ys[i]*inner,.014F,147,140,156);
        }
        pose.popPose();
    }
    private static void quad(VertexConsumer b,org.joml.Matrix4f m,float ax,float ay,float bx,float by,
            float cx,float cy,float dx,float dy,float z,int r,int g,int blue) {
        b.vertex(m,ax,ay,z).color(r,g,blue,255).endVertex();
        b.vertex(m,bx,by,z).color(r,g,blue,255).endVertex();
        b.vertex(m,cx,cy,z).color(r,g,blue,255).endVertex();
        b.vertex(m,dx,dy,z).color(r,g,blue,255).endVertex();
    }
}
