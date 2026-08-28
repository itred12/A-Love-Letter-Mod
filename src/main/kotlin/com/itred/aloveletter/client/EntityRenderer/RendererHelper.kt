package com.itred.aloveletter.client.EntityRenderer

import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.client.renderer.texture.OverlayTexture
import org.joml.Matrix3f
import org.joml.Matrix4f

object RendererHelper {

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