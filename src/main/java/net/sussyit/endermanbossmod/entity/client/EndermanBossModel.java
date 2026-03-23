package net.sussyit.endermanbossmod.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.AnimationState;
import net.sussyit.endermanbossmod.EndermanBossMod;
import net.sussyit.endermanbossmod.entity.custom.EndermanBossEntity;

public class EndermanBossModel<T extends EndermanBossEntity> extends HierarchicalModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(EndermanBossMod.MOD_ID, "endermanboss"), "main");
    private final ModelPart root;
    private final ModelPart rotate;
    private final ModelPart legs;
    private final ModelPart left_leg;
    private final ModelPart right_leg;
    private final ModelPart body;
    private final ModelPart jacket;
    private final ModelPart pants;
    private final ModelPart right_arm;
    private final ModelPart right_sleeve;
    private final ModelPart left_arm;
    private final ModelPart left_sleeve;
    private final ModelPart headwear;
    private final ModelPart head;
    private final ModelPart hat;
    private final ModelPart right_eye;
    private final ModelPart right_pupil;
    private final ModelPart left_eye;
    private final ModelPart left_pupil;

    public EndermanBossModel(ModelPart root) {
        this.root = root.getChild("root");
        this.rotate = this.root.getChild("rotate");
        this.body = this.rotate.getChild("body");
        this.jacket = this.body.getChild("jacket");
        this.pants = this.jacket.getChild("pants");
        this.right_arm = this.body.getChild("right_arm");
        this.right_sleeve = this.right_arm.getChild("right_sleeve");
        this.left_arm = this.body.getChild("left_arm");
        this.left_sleeve = this.left_arm.getChild("left_sleeve");
        this.headwear = this.body.getChild("headwear");
        this.head = this.headwear.getChild("head");
        this.hat = this.head.getChild("hat");
        this.right_eye = this.head.getChild("right_eye");
        this.right_pupil = this.right_eye.getChild("right_pupil");
        this.left_eye = this.head.getChild("left_eye");
        this.left_pupil = this.left_eye.getChild("left_pupil");
        this.legs = this.rotate.getChild("legs");
        this.left_leg = this.legs.getChild("left_leg");
        this.right_leg = this.legs.getChild("right_leg");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, -12.0F, 0.0F));

        PartDefinition rotate = root.addOrReplaceChild("rotate", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 36.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

        PartDefinition body = rotate.addOrReplaceChild("body", CubeListBuilder.create().texOffs(48, 64).addBox(-4.0F, -5.5F, -2.0F, 8.0F, 11.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -16.5F, 0.0F));

        PartDefinition jacket = body.addOrReplaceChild("jacket", CubeListBuilder.create().texOffs(64, 24).addBox(-4.0F, -22.0F, -3.0F, 8.0F, 11.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 16.5F, 0.0F));

        PartDefinition pants = jacket.addOrReplaceChild("pants", CubeListBuilder.create(), PartPose.offset(0.0F, -11.5F, 0.0F));

        PartDefinition left_pants_r1 = pants.addOrReplaceChild("left_pants_r1", CubeListBuilder.create().texOffs(1, 72).addBox(-4.0F, 0.0F, -2.5F, 4.0F, 11.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.5F, 0.0F, 0.0F, 0.0F, 0.1745F));

        PartDefinition right_pants_r1 = pants.addOrReplaceChild("right_pants_r1", CubeListBuilder.create().texOffs(21, 72).addBox(0.0F, 0.0F, -2.5F, 4.0F, 11.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.5F, 0.0F, 0.0F, 0.0F, -0.1745F));

        PartDefinition right_arm = body.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(80, 41).addBox(-0.5F, -1.5F, -1.0F, 2.0F, 11.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, -4.0F, 0.0F));

        PartDefinition right_sleeve = right_arm.addOrReplaceChild("right_sleeve", CubeListBuilder.create().texOffs(40, 79).addBox(4.0F, -22.0F, -2.0F, 3.0F, 11.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, 20.5F, 0.0F));

        PartDefinition left_arm = body.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(86, 77).addBox(-1.5F, -1.5F, -1.0F, 2.0F, 11.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, -4.0F, 0.0F));

        PartDefinition left_sleeve = left_arm.addOrReplaceChild("left_sleeve", CubeListBuilder.create().texOffs(72, 77).addBox(-3.0F, -1.5F, -2.0F, 3.0F, 11.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, 0.0F, 0.0F));

        PartDefinition headwear = body.addOrReplaceChild("headwear", CubeListBuilder.create().texOffs(0, 107).addBox(-5.0F, -11.0F, -5.0F, 10.0F, 11.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -5.5F, 0.0F));

        PartDefinition head = headwear.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 47).addBox(-6.0F, -10.0F, -1.0F, 12.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, -5.0F));

        PartDefinition hat = head.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(0, 0).addBox(-11.0F, 0.0F, -11.0F, 22.0F, 2.0F, 22.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -11.0F, 5.0F, 0.0F, 0.0F, -0.0436F));

        PartDefinition hat5_r1 = hat.addOrReplaceChild("hat5_r1", CubeListBuilder.create().texOffs(40, 71).addBox(-1.0F, -1.5F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -13.5F, -10.0F, 2.2253F, 0.0F, 0.0F));

        PartDefinition hat4_r1 = hat.addOrReplaceChild("hat4_r1", CubeListBuilder.create().texOffs(72, 64).addBox(-2.0F, -5.5F, -2.0F, 4.0F, 9.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -12.5F, -4.0F, 1.0908F, 0.0F, 0.0F));

        PartDefinition hat3_r1 = hat.addOrReplaceChild("hat3_r1", CubeListBuilder.create().texOffs(48, 47).addBox(-4.0F, -5.5F, -4.0F, 8.0F, 9.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -7.5F, -1.0F, 0.48F, 0.0F, 0.0F));

        PartDefinition hat2_r1 = hat.addOrReplaceChild("hat2_r1", CubeListBuilder.create().texOffs(0, 24).addBox(-8.0F, -6.0F, -8.0F, 16.0F, 7.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0873F, 0.0F, 0.0F));

        PartDefinition right_eye = head.addOrReplaceChild("right_eye", CubeListBuilder.create().texOffs(64, 41).addBox(-2.5F, -2.5F, 0.0F, 5.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(3.5F, -2.5F, 11.05F));

        PartDefinition right_pupil = right_eye.addOrReplaceChild("right_pupil", CubeListBuilder.create().texOffs(74, 44).addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, 1.0F, 0.05F));

        PartDefinition left_eye = head.addOrReplaceChild("left_eye", CubeListBuilder.create().texOffs(80, 54).addBox(-2.5F, -2.5F, 0.0F, 5.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.5F, -2.5F, 11.05F));

        PartDefinition left_pupil = left_eye.addOrReplaceChild("left_pupil", CubeListBuilder.create().texOffs(74, 41).addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, 1.0F, 0.05F));

        PartDefinition legs = rotate.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(0.0F, -11.5F, 0.0F));

        PartDefinition left_leg = legs.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(54, 79).addBox(-1.0F, -0.5F, -1.0F, 2.0F, 11.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 1.0F, 0.0F));

        PartDefinition right_leg = legs.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(62, 79).addBox(-1.0F, -0.5F, -1.0F, 2.0F, 11.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 1.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(EndermanBossEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);

        this.animate(entity.chargeAttackAnimationState, EndermanBossAnimations.ATTACK_CHARGE_RADIAL, ageInTicks, 1f);
        this.animate(entity.summonAttackAnimationState, EndermanBossAnimations.ATTACK_SUMMON, ageInTicks, 1f);
        this.animate(entity.eyeOfEndAttackAnimationState, EndermanBossAnimations.ATTACK_EYE_OF_END, ageInTicks, 1f);
        this.animate(entity.knockBackAnimationState, EndermanBossAnimations.ATTACK_KNOCKBACK, ageInTicks, 1f);
        this.animate(entity.spikeAnimationState, EndermanBossAnimations.ATTACK_ICE, ageInTicks, 1f);

        this.animate(entity.teleportOneAnimationState, EndermanBossAnimations.TELEPORT_ONE, ageInTicks, 1f);
        this.animate(entity.teleportTwoAnimationState, EndermanBossAnimations.TELEPORT_TWO, ageInTicks, 1f);
        this.animate(entity.teleportThreeAnimationState, EndermanBossAnimations.TELEPORT_THREE, ageInTicks, 1f);
        this.animate(entity.teleportFourAnimationState, EndermanBossAnimations.TELEPORT_FOUR, ageInTicks, 1f);
    }


    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        poseStack.scale(0.7f, 0.7f, 0.5f);
        root.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

}
