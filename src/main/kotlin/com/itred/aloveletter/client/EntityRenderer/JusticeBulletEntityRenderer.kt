package com.itred.aloveletter.client.EntityRenderer

import com.itred.aloveletter.ALoveLetter
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import com.mojang.math.Axis
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.Mth
import net.minecraft.world.entity.Entity
import org.joml.Matrix3f
import org.joml.Matrix4f

class JusticeBulletEntityRenderer(context: EntityRendererProvider.Context) : EntityRenderer<Entity>(context) {

    val TEXTURE_LOCATION = ALoveLetter.modLoc("textures/item/scp_207.png")

    override fun getTextureLocation(pEntity: Entity?): ResourceLocation? {
        return TEXTURE_LOCATION
    }


    // Copied from ArrowRenderer, as I want basically the same result but the ArrowRenderer only accepts extensions of Arrows
    // Will likely translate from Mojang gobbeldegook at some point
    override fun render(
        pEntity: Entity,
        pEntityYaw: Float,
        pPartialTicks: Float,
        pPoseStack: PoseStack,
        pBuffer: MultiBufferSource,
        pPackedLight: Int
    ) {
        pPoseStack.pushPose()
        pPoseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(pPartialTicks, pEntity.yRotO, pEntity.getYRot()) - 90.0f))
        pPoseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(pPartialTicks, pEntity.xRotO, pEntity.getXRot())))
        val i = 0
        val f = 0.0f
        val f1 = 0.5f
        val f2 = 0.0f
        val f3 = 0.15625f
        val f4 = 0.0f
        val f5 = 0.15625f
        val f6 = 0.15625f
        val f7 = 0.3125f
        val f8 = 0.05625f


        pPoseStack.mulPose(Axis.XP.rotationDegrees(45.0f))
        pPoseStack.scale(0.05625f, 0.05625f, 0.05625f)
        pPoseStack.translate(-4.0f, 0.0f, 0.0f)
        val vertexconsumer = pBuffer.getBuffer(RenderType.entityCutout(this.getTextureLocation(pEntity)))
        val `posestack$pose` = pPoseStack.last()
        val matrix4f = `posestack$pose`.pose()
        val matrix3f = `posestack$pose`.normal()
        this.vertex(matrix4f, matrix3f, vertexconsumer, -7, -2, -2, 0.0f, 0.15625f, -1, 0, 0, pPackedLight)
        this.vertex(matrix4f, matrix3f, vertexconsumer, -7, -2, 2, 0.15625f, 0.15625f, -1, 0, 0, pPackedLight)
        this.vertex(matrix4f, matrix3f, vertexconsumer, -7, 2, 2, 0.15625f, 0.3125f, -1, 0, 0, pPackedLight)
        this.vertex(matrix4f, matrix3f, vertexconsumer, -7, 2, -2, 0.0f, 0.3125f, -1, 0, 0, pPackedLight)
        this.vertex(matrix4f, matrix3f, vertexconsumer, -7, 2, -2, 0.0f, 0.15625f, 1, 0, 0, pPackedLight)
        this.vertex(matrix4f, matrix3f, vertexconsumer, -7, 2, 2, 0.15625f, 0.15625f, 1, 0, 0, pPackedLight)
        this.vertex(matrix4f, matrix3f, vertexconsumer, -7, -2, 2, 0.15625f, 0.3125f, 1, 0, 0, pPackedLight)
        this.vertex(matrix4f, matrix3f, vertexconsumer, -7, -2, -2, 0.0f, 0.3125f, 1, 0, 0, pPackedLight)

        for (j in 0..3) {
            pPoseStack.mulPose(Axis.XP.rotationDegrees(90.0f))
            this.vertex(matrix4f, matrix3f, vertexconsumer, -8, -2, 0, 0.0f, 0.0f, 0, 1, 0, pPackedLight)
            this.vertex(matrix4f, matrix3f, vertexconsumer, 8, -2, 0, 0.5f, 0.0f, 0, 1, 0, pPackedLight)
            this.vertex(matrix4f, matrix3f, vertexconsumer, 8, 2, 0, 0.5f, 0.15625f, 0, 1, 0, pPackedLight)
            this.vertex(matrix4f, matrix3f, vertexconsumer, -8, 2, 0, 0.0f, 0.15625f, 0, 1, 0, pPackedLight)
        }

        pPoseStack.popPose()
        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight)
    }

    fun vertex(
        pMatrix: Matrix4f,
        pNormal: Matrix3f,
        pConsumer: VertexConsumer,
        pX: Int,
        pY: Int,
        pZ: Int,
        pU: Float,
        pV: Float,
        pNormalX: Int,
        pNormalZ: Int,
        pNormalY: Int,
        pPackedLight: Int
    ) {
        pConsumer.vertex(pMatrix, pX.toFloat(), pY.toFloat(), pZ.toFloat()).color(255, 255, 255, 255).uv(pU, pV)
            .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(pPackedLight)
            .normal(pNormal, pNormalX.toFloat(), pNormalY.toFloat(), pNormalZ.toFloat()).endVertex()
    }

}