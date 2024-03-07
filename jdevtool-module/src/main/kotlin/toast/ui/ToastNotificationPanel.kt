package com.wxl.jdevtool.toast.ui

import com.formdev.flatlaf.FlatClientProperties
import com.formdev.flatlaf.extras.FlatSVGIcon
import com.formdev.flatlaf.extras.FlatSVGIcon.ColorFilter
import com.wxl.jdevtool.toast.ToastIcons
import com.wxl.jdevtool.toast.ToastProperties
import com.wxl.jdevtool.toast.ToastType
import java.awt.Color
import java.awt.Cursor
import javax.swing.*

/**
 * Create by wuxingle on 2024/03/05
 */
open class ToastNotificationPanel : JPanel() {

    protected var window: JWindow? = null

    protected var labelIcon: JLabel

    protected var textPane: JTextPane

    protected var type: ToastType? = null

    init {
        labelIcon = JLabel()
        textPane = JTextPane()

        installDefault()
    }

    private fun installDefault() {
        with(textPane) {
            text = "Hello!\nToast Notification"
            isOpaque = false
            isFocusable = false
            cursor = Cursor.getDefaultCursor()
        }
        putClientProperty(ToastProperties.TOAST_ICON, labelIcon)
        putClientProperty(ToastProperties.TOAST_COMPONENT, textPane)
    }

    open fun setDialog(window: JWindow) {
        this.window = window
        removeDialogBackground()
    }

    override fun updateUI() {
        setUI(ToastPanelUI())
        removeDialogBackground()
    }

    private fun removeDialogBackground() {
        if (window != null) {
            val bg = background
            window!!.background = Color(bg.red, bg.green, bg.blue, 0)
            window!!.size = preferredSize
        }
    }

    open fun set(type: ToastType, message: String) {
        this.type = type
        labelIcon.icon = getIcon(type)
        textPane.text = message
        installPropertyStyle(type)
    }

    open fun getIcon(type: ToastType): Icon {
        val icon = UIManager.getIcon(ToastProperties.getIconKey(type))
        if (icon != null) {
            return icon
        }
        val svgIcon = FlatSVGIcon(ToastIcons.Path.getIconPath(type))
        val colorFilter = ColorFilter()
        colorFilter.add(Color(150, 150, 150), getColor(type))
        svgIcon.colorFilter = colorFilter
        return svgIcon
    }

    open fun getColor(type: ToastType): Color {
        return when (type) {
            ToastType.SUCCESS -> Color.decode("#67C23A")
            ToastType.INFO -> Color.decode("#909399")
            ToastType.WARNING -> Color.decode("#E6A23C")
            ToastType.ERROR -> Color.decode("#F56C6C")
        }
    }

    private fun installPropertyStyle(type: ToastType) {
        val outlineColor = toTextColor(getColor(type))
        val key = getKey(type)
        val outline = convertsKey(key, "outlineColor", outlineColor)
        putClientProperty(
            FlatClientProperties.STYLE, "" +
                    "background:" + convertsKey(key, "background", "\$Panel.background") + ";" +
                    "outlineColor:" + outline + ";" +
                    "effectColor:" + convertsKey(key, "effectColor", outline)
        )
    }

    private fun getKey(type: ToastType): String {
        return when (type) {
            ToastType.SUCCESS -> "success"
            ToastType.INFO -> "info"
            ToastType.WARNING -> "warning"
            ToastType.ERROR -> "error"
        }
    }

    private fun toTextColor(color: Color): String {
        return "rgb(" + color.red + "," + color.green + "," + color.blue + ")"
    }

    private fun convertsKey(key: String, value: String, defaultValue: String): String {
        return "if(\$Toast.$key.$value, \$Toast.$key.$value, if(\$Toast.$value, \$Toast.$value, $defaultValue))"
    }
}