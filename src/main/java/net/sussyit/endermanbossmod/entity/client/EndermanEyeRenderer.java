package net.sussyit.endermanbossmod.entity.client;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.sussyit.endermanbossmod.EndermanBossMod;
import net.sussyit.endermanbossmod.entity.custom.EndermanBossEntity;
import net.sussyit.endermanbossmod.entity.custom.EndermanEyeEntity;

public class EndermanEyeRenderer extends MobRenderer<EndermanEyeEntity, EndermanEyeModel<EndermanEyeEntity>> {

    private static final ResourceLocation NORMAL_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(EndermanBossMod.MOD_ID, "textures/entity/endermaneye/endermaneye.png");

    public EndermanEyeRenderer(EntityRendererProvider.Context context) {
        super(context, new EndermanEyeModel<>(context.bakeLayer(EndermanEyeModel.LAYER_LOCATION)), 0.0f);
    }


    @Override
    public ResourceLocation getTextureLocation(EndermanEyeEntity entity) {
        return NORMAL_TEXTURE;
    }
}
