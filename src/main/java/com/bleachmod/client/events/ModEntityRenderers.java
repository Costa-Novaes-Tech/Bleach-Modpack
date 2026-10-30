package com.bleachmod.client.events;

import com.bleachmod.Reference;
import com.bleachmod.entity.QuestNpcEntity;
import com.bleachmod.registry.ModEntities;

import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import com.bleachmod.client.model.HollowBossRenderer;
import com.bleachmod.entity.HollowBossEntity;

@Mod.EventBusSubscriber(
        modid = Reference.MOD_ID,
        bus = Mod.EventBusSubscriber.Bus.MOD,
        value = Dist.CLIENT
)
public class ModEntityRenderers {

    @SubscribeEvent
    public static void registerRenderers(
            EntityRenderersEvent.RegisterRenderers event
    ) {
        event.registerEntityRenderer(ModEntities.ICE_DRAGON.get(), com.bleachmod.client.model.IceDragonRenderer::new);
        event.registerEntityRenderer(
                ModEntities.HOLLOW.get(),
                com.bleachmod.client.model.HollowRenderer::new
        );

        event.registerEntityRenderer(
                ModEntities.HOLLOW_BOSS.get(),
                HollowBossRenderer::new
        );

        ModEntities.questNpcs().values().forEach(type ->
                event.registerEntityRenderer(type.get(), QuestNpcRenderer::new)
        );
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(
            EntityRenderersEvent.RegisterLayerDefinitions event
    ) {
        event.registerLayerDefinition(com.bleachmod.client.model.IceArmorLayer.LAYER, com.bleachmod.client.model.IceArmorLayer::createLayer);
        event.registerLayerDefinition(com.bleachmod.client.model.ZanpakutoSheathLayer.LAYER, com.bleachmod.client.model.ZanpakutoSheathLayer::createLayer);
    }

    @SubscribeEvent public static void playerLayers(EntityRenderersEvent.AddLayers event) {
        for (String skin : event.getSkins()) {
            var renderer = event.getSkin(skin);
            if (renderer instanceof net.minecraft.client.renderer.entity.player.PlayerRenderer playerRenderer) {
                playerRenderer.addLayer(new com.bleachmod.client.model.IceArmorLayer(playerRenderer,
                    event.getEntityModels().bakeLayer(com.bleachmod.client.model.IceArmorLayer.LAYER)));
                playerRenderer.addLayer(new com.bleachmod.client.berserk.BerserkSkinLayer(playerRenderer));
                playerRenderer.addLayer(new com.bleachmod.client.model.ZanpakutoSheathLayer(playerRenderer,
                    event.getEntityModels().bakeLayer(com.bleachmod.client.model.ZanpakutoSheathLayer.LAYER)));
            }
        }
    }
    public static class QuestNpcRenderer extends MobRenderer<QuestNpcEntity, PlayerModel<QuestNpcEntity>> {
        public QuestNpcRenderer(EntityRendererProvider.Context context) {
            super(context, new PlayerModel<QuestNpcEntity>(context.bakeLayer(ModelLayers.PLAYER), false) {
                @Override public void setupAnim(QuestNpcEntity npc,float walk,float amount,float age,float yaw,float pitch) {
                    headParts().forEach(net.minecraft.client.model.geom.ModelPart::resetPose);
                    bodyParts().forEach(net.minecraft.client.model.geom.ModelPart::resetPose);
                    super.setupAnim(npc,walk,amount,age,yaw,pitch);
                    com.bleachmod.client.cinematic.OriginCinematicClient.poseNpc(npc,this,age-npc.tickCount);
                }
            }, 0.5F);
            addLayer(new net.minecraft.client.renderer.entity.layers.ItemInHandLayer<>(this,context.getItemInHandRenderer()) {
                @Override protected void renderArmWithItem(net.minecraft.world.entity.LivingEntity npc,net.minecraft.world.item.ItemStack item,
                        net.minecraft.world.item.ItemDisplayContext display,net.minecraft.world.entity.HumanoidArm arm,
                        com.mojang.blaze3d.vertex.PoseStack pose,net.minecraft.client.renderer.MultiBufferSource buffers,int light) {
                    if(!com.bleachmod.client.cinematic.OriginCinematicClient.isRukia(npc)) { super.renderArmWithItem(npc,item,display,arm,pose,buffers,light);return; }
                    if(item.isEmpty())return;
                    pose.pushPose();pose.translate(0,.19,0);getParentModel().translateToHand(arm,pose);
                    pose.mulPose(com.mojang.math.Axis.XP.rotationDegrees(-90));pose.mulPose(com.mojang.math.Axis.YP.rotationDegrees(180));
                    boolean left=arm==net.minecraft.world.entity.HumanoidArm.LEFT;
                    pose.translate((left?-1:1)/16F,.125,-.625);
                    pose.mulPose(com.mojang.math.Axis.XP.rotationDegrees(-90));
                    context.getItemInHandRenderer().renderItem(npc,item,display,left,pose,buffers,light);
                    pose.popPose();
                }
            });
        }

        @Override
        public ResourceLocation getTextureLocation(QuestNpcEntity entity) {
            return Reference.id("textures/entity/" + entity.getNpcId() + ".png");
        }
        @Override protected boolean shouldShowName(QuestNpcEntity npc) {
            return !com.bleachmod.client.cinematic.OriginCinematicClient.isRukia(npc)&&super.shouldShowName(npc);
        }
    }
}
