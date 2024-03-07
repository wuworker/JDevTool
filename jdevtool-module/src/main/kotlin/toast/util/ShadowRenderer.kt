package com.wxl.jdevtool.toast.util

import java.awt.Color
import java.awt.Shape
import java.awt.image.BufferedImage
import java.awt.image.Raster

/**
 * Create by wuxingle on 2024/03/05
 */
class ShadowRenderer(
    var size: Int,
    var opacity: Float,
    var color: Color
) {

    constructor() : this(5, 0.5f, Color.BLACK)

    fun createShadow(shape: Shape): BufferedImage {
        val rec = shape.bounds
        val img = BufferedImage(rec.width, rec.height, BufferedImage.TYPE_INT_ARGB)
        val g2 = img.createGraphics()
        g2.color = Color.BLACK
        g2.translate(-rec.x, -rec.y)
        g2.fill(shape)
        g2.dispose()
        return createShadow(img)
    }

    fun createShadow(image: BufferedImage): BufferedImage {
        val shadowSize = size * 2
        val srcWidth = image.width
        val srcHeight = image.height
        val dstWidth = srcWidth + shadowSize
        val dstHeight = srcHeight + shadowSize
        val left = size
        val right = shadowSize - left
        val yStop = dstHeight - right
        val shadowRgb = color.rgb and 0x00FFFFFF
        val aHistory = IntArray(shadowSize)
        var historyIdx: Int
        var aSum: Int
        val dst = BufferedImage(
            dstWidth, dstHeight,
            BufferedImage.TYPE_INT_ARGB
        )
        val dstBuffer = IntArray(dstWidth * dstHeight)
        val srcBuffer = IntArray(srcWidth * srcHeight)
        getPixels(image, 0, 0, srcWidth, srcHeight, srcBuffer)
        val lastPixelOffset = right * dstWidth
        val hSumDivider = 1.0f / shadowSize
        val vSumDivider = opacity / shadowSize
        val hSumLookup = IntArray(256 * shadowSize)
        for (i in hSumLookup.indices) {
            hSumLookup[i] = (i * hSumDivider).toInt()
        }
        val vSumLookup = IntArray(256 * shadowSize)
        for (i in vSumLookup.indices) {
            vSumLookup[i] = (i * vSumDivider).toInt()
        }
        var srcOffset: Int
        var srcY = 0
        var dstOffset = left * dstWidth
        while (srcY < srcHeight) {
            historyIdx = 0
            while (historyIdx < shadowSize) {
                aHistory[historyIdx++] = 0
            }
            aSum = 0
            historyIdx = 0
            srcOffset = srcY * srcWidth
            for (srcX in 0 until srcWidth) {
                var a = hSumLookup[aSum]
                dstBuffer[dstOffset++] = a shl 24
                aSum -= aHistory[historyIdx]
                a = srcBuffer[srcOffset + srcX] ushr 24
                aHistory[historyIdx] = a
                aSum += a
                if (++historyIdx >= shadowSize) {
                    historyIdx -= shadowSize
                }
            }
            for (i in 0 until shadowSize) {
                val a = hSumLookup[aSum]
                dstBuffer[dstOffset++] = a shl 24
                aSum -= aHistory[historyIdx]
                if (++historyIdx >= shadowSize) {
                    historyIdx -= shadowSize
                }
            }
            srcY++
        }
        var x = 0
        var bufferOffset = 0
        while (x < dstWidth) {
            aSum = 0
            historyIdx = 0
            while (historyIdx < left) {
                aHistory[historyIdx++] = 0
            }
            run {
                var y = 0
                while (y < right) {
                    val a = dstBuffer[bufferOffset] ushr 24
                    aHistory[historyIdx++] = a
                    aSum += a
                    y++
                    bufferOffset += dstWidth
                }
            }
            bufferOffset = x
            historyIdx = 0
            run {
                var y = 0
                while (y < yStop) {
                    var a = vSumLookup[aSum]
                    dstBuffer[bufferOffset] = a shl 24 or shadowRgb
                    aSum -= aHistory[historyIdx]
                    a = dstBuffer[bufferOffset + lastPixelOffset] ushr 24
                    aHistory[historyIdx] = a
                    aSum += a
                    if (++historyIdx >= shadowSize) {
                        historyIdx -= shadowSize
                    }
                    y++
                    bufferOffset += dstWidth
                }
            }
            var y = yStop
            while (y < dstHeight) {
                val a = vSumLookup[aSum]
                dstBuffer[bufferOffset] = a shl 24 or shadowRgb
                aSum -= aHistory[historyIdx]
                if (++historyIdx >= shadowSize) {
                    historyIdx -= shadowSize
                }
                y++
                bufferOffset += dstWidth
            }
            x++
            bufferOffset = x
        }
        setPixels(dst, 0, 0, dstWidth, dstHeight, dstBuffer)
        return dst
    }

    private fun getPixels(img: BufferedImage, x: Int, y: Int, w: Int, h: Int, p: IntArray?): IntArray? {
        if (w == 0 || h == 0) {
            return IntArray(0)
        }
        var pixels = p
        if (pixels == null) {
            pixels = IntArray(w * h)
        } else require(pixels.size >= w * h) { "pixels array must have a length >= w*h" }

        val imageType = img.type
        if (imageType == BufferedImage.TYPE_INT_ARGB || imageType == BufferedImage.TYPE_INT_RGB) {
            val raster: Raster = img.raster
            return raster.getDataElements(x, y, w, h, pixels) as IntArray
        }
        return img.getRGB(x, y, w, h, pixels, 0, w)
    }

    private fun setPixels(img: BufferedImage, x: Int, y: Int, w: Int, h: Int, pixels: IntArray?) {
        if (pixels == null || w == 0 || h == 0) {
            return
        } else require(pixels.size >= w * h) { "pixels array must have a length >= w*h" }

        val imageType = img.type
        if (imageType == BufferedImage.TYPE_INT_ARGB || imageType == BufferedImage.TYPE_INT_RGB) {
            val raster = img.raster
            raster.setDataElements(x, y, w, h, pixels)
        } else {
            img.setRGB(x, y, w, h, pixels, 0, w)
        }
    }
}