package com.wxl.jdevtool.component

import com.wxl.jdevtool.Icons
import javax.swing.JButton

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
            pressedIcon = Icons.copySelected
            disabledIcon = Icons.copyDisable
            isContentAreaFilled = false
        }

        return btn
    }


}