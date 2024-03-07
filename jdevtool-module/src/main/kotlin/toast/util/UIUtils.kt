package com.wxl.jdevtool.toast.util

import com.formdev.flatlaf.extras.FlatSVGIcon
import com.formdev.flatlaf.extras.FlatSVGIcon.ColorFilter
import java.awt.Color
import java.awt.Insets
import javax.swing.Icon
import javax.swing.UIManager

/**
 * Create by wuxingle on 2024/03/05
 */
object UIUtils {

    fun getIcon(key: String, defaultValue: Icon): Icon {
        return UIManager.getIcon(key) ?: return defaultValue
    }

    fun getInsets(key: String, defaultValue: Insets): Insets {
        return UIManager.getInsets(key) ?: return defaultValue
    }

    fun getString(key: String, defaultValue: String): String {
        return UIManager.getString(key) ?: return defaultValue
    }

    fun createIcon(path: String, color: Color?, scale: Float): Icon {
        val icon = FlatSVGIcon(path, scale)
        if (color != null) {
            val colorFilter = ColorFilter()
            colorFilter.add(Color(150, 150, 150), color)
            icon.colorFilter = colorFilter
        }
        return icon
    }

}