package com.itred.aloveletter.client.EntityRenderer

import com.itred.aloveletter.client.EntityRenderer.RendererHelper.drawSquare
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.Entity

class WhirlpoolEnchantmentAnchorEntityRenderer(context: EntityRendererProvider.Context) : EntityRenderer<Entity>(context)  {


    override fun getTextureLocation(pEntity: Entity?): ResourceLocation? {
        return ResourceLocation.tryParse("minecraft:textures/item/heart_of_the_sea.png")
    }

    override fun render(
        pEntity: Entity,
        pEntityYaw: Float,
        pPartialTicks: Float,
        pPoseStack: PoseStack,
        pBuffer: MultiBufferSource,
        pPackedLight: Int
    ) {
        pPoseStack.pushPose()




        pPoseStack.translate(0.0, 0.5, 0.0)
        pPoseStack.mulPose(Axis.YP.rotationDegrees( 360f - pEntityYaw))
        pPoseStack.mulPose(Axis.XP.rotationDegrees(pEntity.xRot))


        val vertexconsumer = pBuffer.getBuffer(RenderType.entityCutout(this.getTextureLocation(pEntity)))

        val `posestack$pose` = pPoseStack.last()


        val poseMatrix = `posestack$pose`.pose()
        val normalMatrix = `posestack$pose`.normal()



        pPoseStack.scale(0.5f, 0.5f, 0.5f)
        drawSquare(
            poseMatrix, normalMatrix, vertexconsumer,
            1, -1,
            1, -1,
            0, 0,
            0.0f, 1.0f,
            0.0f, 1.0f,
            0, 0, 0,
            pPackedLight
        )





        pPoseStack.popPose()
        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight)
    }


}