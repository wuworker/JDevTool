package com.wxl.jdevtool.toast.ui

import com.formdev.flatlaf.FlatClientProperties
import com.formdev.flatlaf.ui.FlatStylingSupport
import com.formdev.flatlaf.ui.FlatStylingSupport.Styleable
import com.formdev.flatlaf.ui.FlatStylingSupport.StyleableUI
import com.formdev.flatlaf.ui.FlatUIUtils
import com.formdev.flatlaf.util.LoggingFacade
import com.formdev.flatlaf.util.UIScale
import com.wxl.jdevtool.toast.ToastIcons
import com.wxl.jdevtool.toast.ToastProperties
import com.wxl.jdevtool.toast.util.UIUtils
import java.awt.*
import java.awt.event.ActionEvent
import java.beans.PropertyChangeEvent
import java.beans.PropertyChangeListener
import java.util.function.Consumer
import javax.swing.*
import javax.swing.border.Border
import javax.swing.plaf.basic.BasicPanelUI
import kotlin.math.max
import kotlin.math.min

/**
 * Create by wuxingle on 2024/03/05
 */
open class ToastPanelUI : BasicPanelUI(), StyleableUI, PropertyChangeListener {

    protected var iconComponent: JComponent? = null

    protected var component: JComponent? = null

    protected var closeButton: JComponent? = null

    @Styleable
    protected var iconTextGap = 0

    @Styleable
    protected var closeButtonGap = 0

    @Styleable
    protected var minimumWidth = 0

    @Styleable
    protected var maximumWidth = 0

    @Styleable
    protected var arc = 0

    @Styleable
    protected var outlineWidth = 0

    @Styleable
    protected var outlineColor: Color? = null

    @Styleable
    protected var showCloseButton = false

    @Styleable
    protected var closeIconColor: Color? = null

    @Styleable
    protected var margin: Insets? = null

    @Styleable
    protected var closeButtonIcon: Icon? = null

    @Styleable
    protected var useEffect = false

    @Styleable
    protected var effectColor: Color? = null

    @Styleable
    protected var effectWidth = 0f

    @Styleable
    protected var effectOpacity = 0f

    @Styleable
    protected var effectAlignment: String? = null

    private var layout: PanelNotificationLayout? = null

    private var oldStyleValues: Map<String, Any>? = null

    override fun installUI(c: JComponent) {
        super.installUI(c)
        c.addPropertyChangeListener(this)
        installIconComponent(c)
        installComponent(c)
        installCloseButton(c)
        installStyle(c as JPanel)
    }

    override fun uninstallUI(c: JComponent) {
        super.uninstallUI(c)
        c.removePropertyChangeListener(this)
        uninstallIconComponent(c)
        uninstallComponent(c)
        uninstallCloseButton(c)
    }

    override fun installDefaults(p: JPanel) {
        super.installDefaults(p)
        iconTextGap = FlatUIUtils.getUIInt(ToastProperties.TOAST_ICON_TEXT_GAP, 5)
        closeButtonGap = FlatUIUtils.getUIInt(ToastProperties.TOAST_CLOSE_BUTTON_GAP, 5)
        minimumWidth = FlatUIUtils.getUIInt(ToastProperties.TOAST_MINIMUM_WIDTH, 50)
        maximumWidth = FlatUIUtils.getUIInt(ToastProperties.TOAST_MAXIMUM_WIDTH, -1)
        arc = FlatUIUtils.getUIInt(ToastProperties.TOAST_ARC, 20)
        outlineWidth = FlatUIUtils.getUIInt(ToastProperties.TOAST_OUTLINE_WIDTH, 0)
        outlineColor = FlatUIUtils.getUIColor(ToastProperties.TOAST_OUTLINE_COLOR, "Component.focusColor")
        margin = UIUtils.getInsets(ToastProperties.TOAST_MARGIN, Insets(8, 8, 8, 8))
        showCloseButton = FlatUIUtils.getUIBoolean(ToastProperties.TOAST_SHOW_CLOSE_BUTTON, true)
        closeIconColor = FlatUIUtils.getUIColor(ToastProperties.TOAST_CLOSE_ICON_COLOR, Color(150, 150, 150))
        closeButtonIcon = UIUtils.getIcon(
            ToastProperties.TOAST_CLOSE_ICON,
            UIUtils.createIcon(ToastIcons.Path.CLOSE_ICON, closeIconColor, 0.75f)
        )
        useEffect = FlatUIUtils.getUIBoolean(ToastProperties.TOAST_USE_EFFECT, true)
        effectColor = FlatUIUtils.getUIColor(ToastProperties.TOAST_EFFECT_COLOR, "Component.focusColor")
        effectWidth = FlatUIUtils.getUIFloat(ToastProperties.TOAST_EFFECT_WIDTH, 0.5f)
        effectOpacity = FlatUIUtils.getUIFloat(ToastProperties.TOAST_EFFECT_OPACITY, 0.2f)
        effectAlignment = UIUtils.getString(ToastProperties.TOAST_EFFECT_ALIGNMENT, "left")
        p.background = FlatUIUtils.getUIColor(ToastProperties.TOAST_BACKGROUND, "Panel.background")
        p.border = createDefaultBorder()
        LookAndFeel.installProperty(p, "opaque", false)
    }

    protected fun createDefaultBorder(): Border {
        val color = FlatUIUtils.getUIColor(ToastProperties.TOAST_SHADOW_COLOR, Color(0, 0, 0))
        val insets = UIUtils.getInsets(ToastProperties.TOAST_SHADOW_INSETS, Insets(0, 0, 6, 6))
        val shadowOpacity = FlatUIUtils.getUIFloat(ToastProperties.TOAST_SHADOW_OPACITY, 0.1f)
        return DropShadowBorder(color, insets, shadowOpacity)
    }

    override fun uninstallDefaults(p: JPanel?) {
        super.uninstallDefaults(p)
        oldStyleValues = null
    }

    override fun propertyChange(e: PropertyChangeEvent) {
        when (e.propertyName) {
            ToastProperties.TOAST_ICON -> {
                val c = e.source as JPanel
                uninstallIconComponent(c)
                installIconComponent(c)
                c.revalidate()
                c.repaint()
            }

            ToastProperties.TOAST_COMPONENT -> {
                val c = e.source as JPanel
                uninstallComponent(c)
                installComponent(c)
                c.revalidate()
                c.repaint()
            }

            ToastProperties.TOAST_SHOW_CLOSE_BUTTON -> {
                val c = e.source as JPanel
                uninstallCloseButton(c)
                installCloseButton(c)
                c.revalidate()
                c.repaint()
            }

            FlatClientProperties.STYLE, FlatClientProperties.STYLE_CLASS -> {
                val c = e.source as JPanel
                installStyle(c)
                c.revalidate()
                c.repaint()
            }
        }
    }

    private fun installIconComponent(c: JComponent) {
        iconComponent = FlatClientProperties.clientProperty(c, ToastProperties.TOAST_ICON, null, JComponent::class.java)
        if (iconComponent != null) {
            installLayout(c)
            c.add(iconComponent)
        }
    }

    private fun uninstallIconComponent(c: JComponent) {
        if (iconComponent != null) {
            c.remove(iconComponent)
            iconComponent = null
        }
    }

    private fun installComponent(c: JComponent) {
        component =
            FlatClientProperties.clientProperty(c, ToastProperties.TOAST_COMPONENT, null, JComponent::class.java)
        if (component != null) {
            installLayout(c)
            c.add(component)
        }
    }

    private fun uninstallComponent(c: JComponent) {
        if (component != null) {
            c.remove(component)
            component = null
        }
    }

    private fun installCloseButton(c: JComponent) {
        if (FlatClientProperties.clientPropertyBoolean(c, ToastProperties.TOAST_SHOW_CLOSE_BUTTON, showCloseButton)) {
            closeButton = createCloseButton(c)
            installLayout(c)
            c.add(closeButton)
        }
    }

    private fun uninstallCloseButton(c: JComponent) {
        if (closeButton != null) {
            c.remove(closeButton)
            closeButton = null
        }
    }

    protected fun createCloseButton(c: JComponent): JComponent {
        val button = JButton()
        button.isFocusable = false
        button.name = ToastProperties.TOAST_CLOSE_BUTTON
        button.putClientProperty(FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_TOOLBAR_BUTTON)
        button.putClientProperty(
            FlatClientProperties.STYLE, "arc:999"
        )
        button.icon = closeButtonIcon
        button.addActionListener { e: ActionEvent? -> closeButtonClicked(c) }
        return button
    }

    protected fun closeButtonClicked(c: JComponent) {
        val callback = c.getClientProperty(ToastProperties.TOAST_CLOSE_CALLBACK)
        if (callback is Runnable) {
            callback.run()
        } else if (callback is Consumer<*>) {
            (callback as Consumer<Any>).accept(c)
        }
    }

    protected fun installLayout(c: JComponent) {
        if (layout == null) {
            layout = PanelNotificationLayout()
        }
        c.layout = layout
    }


    protected fun installStyle(c: JPanel) {
        try {
            applyStyle(c, FlatStylingSupport.getResolvedStyle(c, "ToastPanel"))
        } catch (ex: RuntimeException) {
            LoggingFacade.INSTANCE.logSevere(null, ex)
        }
    }

    protected fun applyStyle(c: JPanel, style: Any?) {
        val oldShowCloseButton = showCloseButton
        oldStyleValues = FlatStylingSupport.parseAndApply(
            oldStyleValues, style
        ) { key: String, value: Any? ->
            applyStyleProperty(
                c,
                key,
                value
            )
        }

        if (oldShowCloseButton != showCloseButton) {
            uninstallCloseButton(c)
            installCloseButton(c)
        }
    }

    protected fun applyStyleProperty(c: JPanel?, key: String, value: Any?): Any? {
        return FlatStylingSupport.applyToAnnotatedObjectOrComponent(this, c, key, value)
    }

    override fun getStyleableInfos(c: JComponent?): Map<String?, Class<*>?>? {
        return FlatStylingSupport.getAnnotatedStyleableInfos(this)
    }

    override fun getStyleableValue(c: JComponent?, key: String): Any? {
        return FlatStylingSupport.getAnnotatedStyleableValue(this, key)
    }

    inner class PanelNotificationLayout : LayoutManager {
        override fun addLayoutComponent(name: String, comp: Component) {}

        override fun removeLayoutComponent(comp: Component) {}

        override fun preferredLayoutSize(parent: Container): Dimension? {
            synchronized(parent.treeLock) {
                val insets = FlatUIUtils.addInsets(parent.insets, UIScale.scale(margin))
                var width = insets.left + insets.right
                var height = 0
                var gap = 0
                var closeGap = 0
                if (iconComponent != null) {
                    width += iconComponent!!.preferredSize.width
                    height = max(height, iconComponent!!.preferredSize.height)
                    gap = UIScale.scale(iconTextGap)
                }
                if (component != null) {
                    width += gap
                    width += component!!.preferredSize.width
                    height = max(height, component!!.preferredSize.height)
                    closeGap = UIScale.scale(closeButtonGap)
                }
                if (closeButton != null) {
                    width += closeGap
                    width += closeButton!!.preferredSize.width
                    height = max(height, closeButton!!.preferredSize.height)
                }
                height += insets.top + insets.bottom
                width = max(minimumWidth, if (maximumWidth == -1) width else min(maximumWidth, width))
                return Dimension(width, height)
            }
        }

        override fun minimumLayoutSize(parent: Container): Dimension {
            synchronized(parent.treeLock) { return Dimension(0, 0) }
        }

        private fun getMaxWidth(insets: Int): Int {
            var width = max(maximumWidth, minimumWidth) - insets
            if (iconComponent != null) {
                width -= iconComponent!!.preferredSize.width + UIScale.scale(iconTextGap)
            }
            if (closeButton != null) {
                width -= UIScale.scale(closeButtonGap) + closeButton!!.preferredSize.width
            }
            return width
        }

        override fun layoutContainer(parent: Container) {
            synchronized(parent.treeLock) {
                val insets = FlatUIUtils.addInsets(parent.insets, UIScale.scale(margin))
                var x = insets.left
                val y = insets.top
                var height = 0
                if (iconComponent != null) {
                    val iconW = iconComponent!!.preferredSize.width
                    val iconH = iconComponent!!.preferredSize.height
                    iconComponent!!.setBounds(x, y, iconW, iconH)
                    x += iconW
                    height = iconH
                }
                if (component != null) {
                    val cW = if (maximumWidth == -1) component!!.preferredSize.width else min(
                        component!!.preferredSize.width,
                        getMaxWidth(insets.left + insets.right)
                    )
                    val cH = component!!.preferredSize.height
                    x += UIScale.scale(iconTextGap)
                    component!!.setBounds(x, y, cW, cH)
                    height = max(height, cH)
                }
                if (closeButton != null) {
                    val cW = closeButton!!.preferredSize.width
                    val cH = closeButton!!.preferredSize.height
                    val cX = parent.width - insets.right - cW
                    val cy = y + (height - cH) / 2
                    closeButton!!.setBounds(cX, cy, cW, cH)
                }
            }
        }
    }
}