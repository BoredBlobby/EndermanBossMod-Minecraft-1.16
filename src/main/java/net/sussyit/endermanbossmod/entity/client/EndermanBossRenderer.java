package net.sussyit.endermanbossmod.entity.client;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.sussyit.endermanbossmod.EndermanBossMod;
import net.sussyit.endermanbossmod.entity.custom.EndermanBossEntity;

public class EndermanBossRenderer extends MobRenderer<EndermanBossEntity, EndermanBossModel<EndermanBossEntity>> {

    private static final ResourceLocation NORMAL_TEXTURE =
        ResourceLocation.fromNamespaceAndPath(EndermanBossMod.MOD_ID, "textures/entity/endermanboss/endermanboss.png");

    public EndermanBossRenderer(EntityRendererProvider.Context context) {
        super(context, new EndermanBossModel<>(context.bakeLayer(EndermanBossModel.LAYER_LOCATION)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(EndermanBossEntity entity) {
        return NORMAL_TEXTURE;
    }
}
