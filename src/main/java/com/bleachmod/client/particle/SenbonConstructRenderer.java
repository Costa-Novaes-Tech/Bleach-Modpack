package com.bleachmod.client.particle;

import com.bleachmod.Reference;
import com.bleachmod.common.network.s2c.SenbonPetalsS2C;
import com.bleachmod.common.visual.SenbonConstructGeometry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import java.util.*;

@Mod.EventBusSubscriber(modid=Reference.MOD_ID,value=Dist.CLIENT)
public final class SenbonConstructRenderer {
    /** Two-sided unlit ribbons, depth tested against terrain, on the particles composite target. */
    private static final class Types extends RenderType {
        private Types() { super("senbon_unused",DefaultVertexFormat.POSITION_COLOR,VertexFormat.Mode.QUADS,4096,false,true,()->{},()->{}); }
        static final RenderType GLOW=create("senbon_construct",DefaultVertexFormat.POSITION_COLOR,VertexFormat.Mode.QUADS,4096,false,true,
                CompositeState.builder().setShaderState(RENDERTYPE_LIGHTNING_SHADER).setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                        .setCullState(NO_CULL).setWriteMaskState(COLOR_WRITE).setOutputState(PARTICLES_TARGET).createCompositeState(false));
    }
    private record Key(int owner,int shape) { }
    private record Frame(SenbonPetalsS2C packet,long until,long born) { }
    private static final Map<Key,Frame> FRAMES=new LinkedHashMap<>();
    private SenbonConstructRenderer() { }
    public static void accept(SenbonPetalsS2C m) {
        var level=Minecraft.getInstance().level;if(level==null)return;
        var key=new Key(m.ownerId(),m.shape());
        if(!FRAMES.containsKey(key) && FRAMES.size()>=32)FRAMES.remove(FRAMES.keySet().iterator().next());
        var previous=FRAMES.get(key);long born=previous==null || previous.packet.pattern()!=m.pattern()?level.getGameTime():previous.born;
        FRAMES.put(key,new Frame(m,level.getGameTime()+7,born));
    }
    @SubscribeEvent public static void render(RenderLevelStageEvent event) {
        if(event.getStage()!=RenderLevelStageEvent.Stage.AFTER_PARTICLES)return;
        var mc=Minecraft.getInstance();if(mc.level==null || mc.player==null){FRAMES.clear();return;}
        FRAMES.values().removeIf(f->f.until()<mc.level.getGameTime());
        if(FRAMES.isEmpty())return;
        var pose=event.getPoseStack();var camera=event.getCamera().getPosition();
        pose.pushPose();
        var buffers=mc.renderBuffers().bufferSource();var vertices=buffers.getBuffer(Types.GLOW);
        var matrix=pose.last().pose();
        for(var frame:FRAMES.values()) {
            var m=frame.packet();if(mc.player.distanceToSqr(m.origin())>96*96)continue;
            boolean swords=m.shape()==com.bleachmod.common.network.s2c.SenbonPetalsS2C.SWORDS;
            float grow=swords && m.pattern()==4?net.minecraft.util.Mth.clamp((m.progress()-.2F)/.25F,0,1):swords && m.pattern()==0?net.minecraft.util.Mth.clamp((mc.level.getGameTime()+event.getPartialTick()-frame.born)/10F,0,1):1;
            float fade=swords && m.pattern()==4?1-net.minecraft.util.Mth.clamp((m.progress()-.55F)/.2F,0,1):1;
            for(var q:SenbonConstructGeometry.build(m.shape(),m.origin().subtract(camera),m.forward(),m.radius(),m.progress(),m.pattern()))
                for(var v:List.of(q.a(),q.b(),q.c(),q.d()))
                    vertices.vertex(matrix,(float)v.x,(float)(m.origin().y-camera.y+(v.y-m.origin().y+camera.y)*grow),(float)v.z).color(q.red(),q.green(),q.blue(),(int)(q.alpha()*grow*fade)).endVertex();
        }
        buffers.endBatch(Types.GLOW);pose.popPose();
    }
    @SubscribeEvent public static void logout(ClientPlayerNetworkEvent.LoggingOut e) { FRAMES.clear();SenbonPetalEffects.reset(); }
    @SubscribeEvent public static void unload(LevelEvent.Unload e) { if(e.getLevel().isClientSide()) { FRAMES.clear();SenbonPetalEffects.reset(); } }
}
