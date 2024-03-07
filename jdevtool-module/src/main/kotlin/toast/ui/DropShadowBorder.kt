package com.wxl.jdevtool.toast.ui

import com.formdev.flatlaf.FlatPropertiesLaf
import com.formdev.flatlaf.ui.FlatStylingSupport.Styleable
import com.formdev.flatlaf.ui.FlatUIUtils
import com.formdev.flatlaf.util.UIScale
import com.wxl.jdevtool.toast.util.ShadowRenderer
import java.awt.*
import java.awt.image.BufferedImage
import javax.swing.JComponent
import javax.swing.border.EmptyBorder
import kotlin.math.max

/**
 * Create by wuxingle on 2024/02/29
 * 边框阴影
 */
class DropShadowBorder(
    @Styleable
    private var shadowColor: Color,
    @Styleable
    private var shadowInsets: Insets,
    @Styleable
    private var shadowOpacity: Float
) : EmptyBorder(nonNegativeInsets(shadowInsets)) {

    private var shadowImage: Image? = null
    private var shadowSize: Int
    private var lastShadowColor: Color? = null
    private var lastShadowOpacity = 0f
    private var lastShadowSize = 0
    private var lastArc = 0
    private var lastWidth = 0
    private var lastHeight = 0

    init {
        shadowSize = maxInset(shadowInsets)
    }

    constructor() : this(Color(0, 0, 0), Insets(0, 0, 6, 6), 0.1f)


    override fun getBorderInsets(): Insets = UIScale.scale(super.getBorderInsets())

    override fun paintBorder(c: Component, g: Graphics, x: Int, y: Int, width: Int, height: Int) {
        val com = c as JComponent
        val arc = FlatPropertiesLaf.getStyleableValue<Int>(com, "arc")
        val useEffect = FlatPropertiesLaf.getStyleableValue<Boolean>(com, "useEffect")
        if (shadowImage == null || shadowColor != lastShadowColor || width != lastWidth || height != lastHeight || shadowSize != lastShadowSize || shadowOpacity != lastShadowOpacity || arc != lastArc) {
            shadowImage = createShadowImage(width, height, arc)

            lastShadowColor = shadowColor
            lastWidth = width
            lastHeight = height
            lastShadowSize = shadowSize
            lastShadowOpacity = shadowOpacity
            lastArc = arc
        }
        g.drawImage(shadowImage, 0, 0, null)
        val insets = borderInsets
        val lx = insets.left
        val ly = insets.top
        val lw = width - (insets.left + insets.right)
        val lh = height - (insets.top + insets.bottom)
        val g2 = g.create() as Graphics2D
        if (arc > 0) {
            FlatUIUtils.setRenderingHints(g2)
            g2.color = c.getBackground()
            FlatUIUtils.paintComponentBackground(g2, lx, ly, lw, lh, 0f, UIScale.scale(arc).toFloat())
        } else {
            g2.color = c.getBackground()
            g2.fillRect(lx, ly, lw, lh)
        }
        if (useEffect) {
            createEffect(com, g2, lx, ly, lw, lh, arc)
        }
        val outlineWidth = FlatPropertiesLaf.getStyleableValue<Int>(com, "outlineWidth")
        if (outlineWidth > 0) {
            val outlineColor = FlatPropertiesLaf.getStyleableValue<Color>(com, "outlineColor")
            g2.color = outlineColor
            FlatUIUtils.paintOutline(
                g2,
                lx.toFloat(),
                ly.toFloat(),
                lw.toFloat(),
                lh.toFloat(),
                UIScale.scale(outlineWidth).toFloat(),
                UIScale.scale(arc).toFloat()
            )
        }
        g2.dispose()
    }

    private fun createEffect(c: JComponent, g2: Graphics2D, x: Int, y: Int, width: Int, height: Int, arc: Int) {
        val effectColor = FlatPropertiesLaf.getStyleableValue<Color>(c, "effectColor")
        val effectWidth = FlatPropertiesLaf.getStyleableValue<Float>(c, "effectWidth")
        val effectOpacity = FlatPropertiesLaf.getStyleableValue<Float>(c, "effectOpacity")
        val effectRight = FlatPropertiesLaf.getStyleableValue<Any>(c, "effectAlignment") == "right"
        if (!effectRight) {
            g2.paint = GradientPaint(x.toFloat(), 0f, effectColor, x + width * effectWidth, 0f, c.background)
        } else {
            g2.paint =
                GradientPaint((x + width).toFloat(), 0f, effectColor, x + width - width * effectWidth, 0f, c.background)
        }
        g2.composite = AlphaComposite.SrcOver.derive(effectOpacity)
        if (arc > 0) {
            FlatUIUtils.paintComponentBackground(g2, x, y, width, height, 0f, UIScale.scale(arc).toFloat())
        } else {
            g2.fillRect(x, y, width, height)
        }
        g2.composite = AlphaComposite.SrcOver
    }

    private fun createShadowImage(width: Int, height: Int, arc: Int): BufferedImage {
        val size = UIScale.scale(shadowSize)
        val round = UIScale.scale(arc * 0.7f)
        val shadowWidth = width - size * 2
        val shadowHeight = height - size * 2
        val shape = FlatUIUtils.createRoundRectanglePath(
            0f,
            0f,
            shadowWidth.toFloat(),
            shadowHeight.toFloat(),
            round,
            round,
            round,
            round
        )
        return ShadowRenderer(size, shadowOpacity, shadowColor).createShadow(shape)
    }

    companion object {

        private fun nonNegativeInsets(shadowInsets: Insets): Insets {
            return Insets(
                max(shadowInsets.top, 0),
                max(shadowInsets.left, 0),
                max(shadowInsets.bottom, 0),
                max(shadowInsets.right, 0)
            )
        }

        private fun maxInset(shadowInsets: Insets): Int {
            return max(
                max(shadowInsets.left, shadowInsets.right),
                max(shadowInsets.top, shadowInsets.bottom)
            )
        }
    }

}