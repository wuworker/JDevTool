package com.wxl.jdevtool.component

import com.wxl.jdevtool.AppContexts
import com.wxl.jdevtool.Icons
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea
import javax.swing.Icon
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
     * 图片按钮
     */
    fun createIconBtn(icon: Icon, text: String = "", toolTip: String = ""): JButton {
        val btn = JButton(icon)
        with(btn) {
            this.text = text
            isBorderPainted = false
            background = UIManager.getColor("Panel.background")
            toolTipText = toolTip
        }
        return btn
    }

    /**
     * 创建textarea
     */
    fun createTextArea(): RSyntaxTextArea {
        val textArea = RSyntaxTextArea()
        AppContexts.theme.textAreaTheme.apply(textArea)
        return textArea
    }


}