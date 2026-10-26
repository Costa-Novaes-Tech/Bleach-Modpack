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
        net.minecraft.client.renderer.entity.LivingEntityRenderer<net.minecraft.world.entity.monster.AbstractSkeleton, net.minecraft.client.model.SkeletonModel<net.minecraft.world.entity.monster.AbstractSkeleton>> ashRenderer = event.getRenderer(net.minecraft.world.entity.EntityType.WITHER_SKELETON);
        if(ashRenderer!=null)ashRenderer.addLayer(new com.bleachmod.client.model.AshSoldierLayer(ashRenderer));
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
            super(context, new PlayerModel<>(context.bakeLayer(ModelLayers.PLAYER), false), 0.5F);
        }

        @Override
        public ResourceLocation getTextureLocation(QuestNpcEntity entity) {
            return Reference.id("textures/entity/" + entity.getNpcId() + ".png");
        }
    }
}
