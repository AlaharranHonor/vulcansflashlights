package com.alaharranhonor.vfl.client.model;

import com.alaharranhonor.vfl.ModRef;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.LivingEntity;

public class HelmetMinersModel<T extends LivingEntity> extends HumanoidModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ModRef.res("helmet_miners"), "main");

    public HelmetMinersModel(ModelPart root) {
        super(root, RenderType::entityCutout);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = createMesh(CubeDeformation.NONE, 0);
        PartDefinition part = mesh.getRoot();

        PartDefinition head = part.addOrReplaceChild("head", CubeListBuilder.create()
            .texOffs(0, 0).addBox(-5.0F, -1.0F, -5.0F, 10.0F, 10.0F, 10.0F, new CubeDeformation(0.0F))
            .texOffs(0, 20).addBox(-5.0F, 1.0F, -7.0F, 10.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
            .texOffs(24, 20).addBox(-1.5F, -2.0F, -6.0F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 16.0F, 0.0F));

        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        super.setupAnim(pEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
        this.head.y = 20;
    }
}