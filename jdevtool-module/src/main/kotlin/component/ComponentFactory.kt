package com.wxl.jdevtool.component

import com.formdev.flatlaf.FlatClientProperties
import com.wxl.jdevtool.Icons
import java.awt.Cursor
import javax.swing.JButton
import javax.swing.UIManager

/**
 * Create by wuxingle on 2024/02/05
 * 创建通用组件
 */
object ComponentFactory {

    /**
     * 复制按钮
     */
    fun createCopyBtn(): JButton {
        val btn = JButton()
        with(btn) {
            icon = Icons.copy
            rolloverIcon = Icons.copyHover
            pressedIcon = Icons.copyPress
            isContentAreaFilled = false
        }

        return btn
    }

    /**
     * 执行按钮
     */
    fun createExecuteBtn(): JButton {
        val btn = JButton(Icons.execute)
        with(btn) {
            icon = Icons.execute
            isBorderPainted = false
            background = UIManager.getColor("window")
        }
        return btn
    }

    /**
     * 清空按钮
     */
    fun createClearBtn(): JButton {
        val button = JButton()
        button.name = "TextField.clearButton"
        button.putClientProperty(FlatClientProperties.STYLE_CLASS, "clearButton")
        button.putClientProperty(FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_TOOLBAR_BUTTON)
        button.cursor = Cursor.getDefaultCursor()
        return button
    }
}