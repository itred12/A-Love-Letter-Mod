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

    override fun getTextureLocation(pEntity: Entity?): ResourceLocation {
        return ALoveLetter.modLoc("textures/item/bullet_pointer_testsprite.png")
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

        // Flip the model to line up with the bullet's horizontal facing direction
        pPoseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(pPartialTicks, pEntity.yRotO, pEntity.yRot) - 90.0f))
        // Flip the model to line up with the bullet's vertical facing direction
        pPoseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(pPartialTicks, pEntity.xRotO, pEntity.xRot)))

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

        val scale = 0.05625f
        val offsetUp = 1.5f

        val tailfinX = -7

        // Rotate 45 degrees (cross-shaped)
        pPoseStack.mulPose(Axis.XP.rotationDegrees(45.0f))

        // Shrink it a wee bit (normally model is ENORMOUS)
        pPoseStack.scale(scale, scale, scale)

        // Move it back a bit so its front only barely peeks out the front of the hitbox, and up a bit to line up with the center of its hitbox
        pPoseStack.translate(-4.0f, offsetUp, -offsetUp)

        val vertexconsumer = pBuffer.getBuffer(RenderType.entityCutout(this.getTextureLocation(pEntity)))

        val `posestack$pose` = pPoseStack.last()

        val poseMatrix = `posestack$pose`.pose()
        val normalMatrix = `posestack$pose`.normal()

        // The back square, both sides have to be drawn separately, by flipping the start and end vertices

        // uv:
        //      0.15625 == 5/32
        // Because arrows are 32x32!
        // From the top-left corner:
            // Arrow side:
                // x0 -> x15, 16 pixels, 0u -> 0.5u
                // y0 -> y4, 5 pixels, 0v -> 5/32v
            // Arrow back:
                // x0 -> x4, 5 pixels, 0u -> 5/32u
                // y5 -> y9, 5 pixels, 5/32v -> 10/32v
        // Uv is (inclusive) -> (exclusive), so though the back starts on the same v that the side ends on, they dont clash
        // y = 9 is 10 pixels down, so 10/32 (especially since its *to the end of* that pixel)

        drawSquare(
            poseMatrix, normalMatrix, vertexconsumer,
            -7, -7,
            -2, 2,
            -2, 2,
            0.0f, 0.15625f,
            0.15625f, 0.3125f,
            1, 0, 0,
            pPackedLight
        )

        drawSquare(
            poseMatrix, normalMatrix, vertexconsumer,
            -7, -7,
            2, -2,
            -2, 2,
            0.0f, 0.15625f,
            0.15625f, 0.3125f,
            -1, 0, 0,
            pPackedLight
        )

        /*
        this.vertex(poseMatrix, normalMatrix, vertexconsumer, tailfinX, -2, -2, 0.0f, 0.15625f, -1, 0, 0, pPackedLight)
        this.vertex(poseMatrix, normalMatrix, vertexconsumer, tailfinX, -2, 2, 0.15625f, 0.15625f, -1, 0, 0, pPackedLight)
        this.vertex(poseMatrix, normalMatrix, vertexconsumer, tailfinX, 2, 2, 0.15625f, 0.3125f, -1, 0, 0, pPackedLight)
        this.vertex(poseMatrix, normalMatrix, vertexconsumer, tailfinX, 2, -2, 0.0f, 0.3125f, -1, 0, 0, pPackedLight)

        this.vertex(poseMatrix, normalMatrix, vertexconsumer, tailfinX, 2, -2, 0.0f, 0.15625f, 1, 0, 0, pPackedLight)
        this.vertex(poseMatrix, normalMatrix, vertexconsumer, tailfinX, 2, 2, 0.15625f, 0.15625f, 1, 0, 0, pPackedLight)
        this.vertex(poseMatrix, normalMatrix, vertexconsumer, tailfinX, -2, 2, 0.15625f, 0.3125f, 1, 0, 0, pPackedLight)
        this.vertex(poseMatrix, normalMatrix, vertexconsumer, tailfinX, -2, -2, 0.0f, 0.3125f, 1, 0, 0, pPackedLight)
        */

        for (j in 0..3) {
            pPoseStack.mulPose(Axis.XP.rotationDegrees(90.0f))
            this.vertex(poseMatrix, normalMatrix, vertexconsumer, -8, -2, 0, 0.0f, 0.0f, 0, 1, 0, pPackedLight)
            this.vertex(poseMatrix, normalMatrix, vertexconsumer, 8, -2, 0, 0.5f, 0.0f, 0, 1, 0, pPackedLight)
            this.vertex(poseMatrix, normalMatrix, vertexconsumer, 8, 2, 0, 0.5f, 0.15625f, 0, 1, 0, pPackedLight)
            this.vertex(poseMatrix, normalMatrix, vertexconsumer, -8, 2, 0, 0.0f, 0.15625f, 0, 1, 0, pPackedLight)
        }

        pPoseStack.popPose()
        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight)
    }

    fun drawSquare(
        poseMatrix: Matrix4f, normalMatrix: Matrix3f, vertexConsumer: VertexConsumer,
        startX: Int, endX: Int,
        startY: Int, endY: Int,
        startZ: Int, endZ: Int,
        uvStartX: Float, uvEndX: Float,
        uvStartY: Float, uvEndY: Float,
        normalX: Int, normalZ: Int, normalY: Int,
        packedLight: Int
    ) {





        vertex(
            poseMatrix, normalMatrix, vertexConsumer,
            startX, startY, startZ, uvStartX, uvStartY,
            normalX, normalY, normalZ,
            packedLight
        )

        vertex(
            poseMatrix, normalMatrix, vertexConsumer,
            endX, startY, endZ, uvEndX, uvStartY,
            normalX, normalY, normalZ,
            packedLight
        )

        vertex(
            poseMatrix, normalMatrix, vertexConsumer,
            endX, endY, endZ, uvEndX, uvEndY,
            normalX, normalY, normalZ,
            packedLight
        )

        vertex(
            poseMatrix, normalMatrix, vertexConsumer,
            startX, endY, startZ, uvStartX, uvEndY,
            normalX, normalY, normalZ,
            packedLight
        )

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