package com.wxl.jdevtool.window

import java.awt.BorderLayout
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.io.Serial
import javax.swing.JFrame

/**
 * Create by wuxingle on 2024/01/18
 * 无边框窗口
 */
class BorderlessPopupWindow internal constructor(
) : JFrame() {

    init {
        contentPane.layout = BorderLayout()

        defaultCloseOperation = DISPOSE_ON_CLOSE
        isUndecorated = true
        addWindowFocusListener(object : WindowAdapter() {
            override fun windowLostFocus(e: WindowEvent?) {
                dispose()
            }
        })
    }

    companion object {
        @Serial
        private const val serialVersionUID: Long = 7927197143088515807L
    }
}
