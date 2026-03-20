package net.sussyit.endermanbossmod.entity.client;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.sussyit.endermanbossmod.EndermanBossMod;
import net.sussyit.endermanbossmod.entity.custom.EndermanBossEntity;
import net.sussyit.endermanbossmod.entity.custom.EyeOfEndEntity;

public class EyeOfEndRenderer extends MobRenderer<EyeOfEndEntity, EyeOfEndModel<EyeOfEndEntity>> {

    private static final ResourceLocation CLOSED_EYES =
            ResourceLocation.fromNamespaceAndPath(EndermanBossMod.MOD_ID, "textures/entity/eyeofender/eyesofenderclosed.png");
    private static final ResourceLocation WARNING_EYES =
            ResourceLocation.fromNamespaceAndPath(EndermanBossMod.MOD_ID, "textures/entity/eyeofender/eyesofenderwarning.png");
    private static final ResourceLocation OPEN_EYES =
            ResourceLocation.fromNamespaceAndPath(EndermanBossMod.MOD_ID, "textures/entity/eyeofender/eyesofenderopen.png");
    private static final ResourceLocation HIDDEN =
            ResourceLocation.fromNamespaceAndPath(EndermanBossMod.MOD_ID, "textures/entity/eyeofender/eyesofenderhidden.png");

    public EyeOfEndRenderer(EntityRendererProvider.Context context) {
        super(context, new EyeOfEndModel<>(context.bakeLayer(EyeOfEndModel.LAYER_LOCATION)), 0.0f);
    }

    @Override
    public ResourceLocation getTextureLocation(EyeOfEndEntity entity) {

        int stage = entity.getAttackStage();

        if(stage == EndermanBossEntity.HIDDEN) {
            return HIDDEN;
        }
        if(stage == EndermanBossEntity.CLOSED) {
            return CLOSED_EYES;
        }
        if(stage == EndermanBossEntity.WARNING) {
            return WARNING_EYES;
        }
        if(stage == EndermanBossEntity.OPEN) {
            return OPEN_EYES;
        }

        return CLOSED_EYES;
    }
}
