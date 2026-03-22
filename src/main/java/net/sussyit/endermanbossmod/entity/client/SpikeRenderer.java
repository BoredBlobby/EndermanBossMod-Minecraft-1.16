package net.sussyit.endermanbossmod.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.sussyit.endermanbossmod.EndermanBossMod;
import net.sussyit.endermanbossmod.entity.custom.SpikeEntity;

public class SpikeRenderer extends MobRenderer<SpikeEntity, SpikeModel<SpikeEntity>> {
    public static final ResourceLocation NORMAL_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(EndermanBossMod.MOD_ID, "textures/entity/spike/spike.png");

    public SpikeRenderer(EntityRendererProvider.Context context) {
        super(context, new SpikeModel<>(context.bakeLayer(SpikeModel.LAYER_LOCATION)), 0.0f);
    }

    @Override
    public void render(SpikeEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();

        // 1. Rotate the spike based on the random angle we stored
        poseStack.mulPose(Axis.YP.rotationDegrees(entity.getSpikeRotation()));

        // 2. Scale the spike based on the random size we stored
        float s = entity.getScale();
        poseStack.scale(s, s, s);

        // 3. Call the super render to actually draw the model with these changes
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);

        poseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(SpikeEntity entity) {
        return NORMAL_TEXTURE;
    }
}
