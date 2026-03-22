package net.sussyit.endermanbossmod.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.sussyit.endermanbossmod.EndermanBossMod;
import net.sussyit.endermanbossmod.entity.ModEntities;
import net.sussyit.endermanbossmod.entity.client.EndermanBossModel;
import net.sussyit.endermanbossmod.entity.client.EndermanEyeModel;
import net.sussyit.endermanbossmod.entity.client.EyeOfEndModel;
import net.sussyit.endermanbossmod.entity.client.SpikeModel;
import net.sussyit.endermanbossmod.entity.custom.EndermanBossEntity;
import net.sussyit.endermanbossmod.entity.custom.EndermanEyeEntity;
import net.sussyit.endermanbossmod.entity.custom.EyeOfEndEntity;
import net.sussyit.endermanbossmod.entity.custom.SpikeEntity;

@EventBusSubscriber(modid = EndermanBossMod.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(EndermanBossModel.LAYER_LOCATION, EndermanBossModel::createBodyLayer);
        event.registerLayerDefinition(EndermanEyeModel.LAYER_LOCATION, EndermanEyeModel::createBodyLayer);
        event.registerLayerDefinition(EyeOfEndModel.LAYER_LOCATION, EyeOfEndModel::createBodyLayer);
        event.registerLayerDefinition(SpikeModel.LAYER_LOCATION, SpikeModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.ENDERMANBOSS.get(), EndermanBossEntity.createAttributes().build());
        event.put(ModEntities.ENDERMANEYE.get(), EndermanEyeEntity.createAttributes().build());
        event.put(ModEntities.EYEOFEND.get(), EyeOfEndEntity.createAttributes().build());
        event.put(ModEntities.SPIKE.get(), SpikeEntity.createAttributes().build());
    }
}
