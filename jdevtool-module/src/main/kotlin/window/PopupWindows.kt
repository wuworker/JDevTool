package com.wxl.jdevtool.window

import java.util.concurrent.atomic.AtomicLong
import javax.swing.JFrame

/**
 * Create by wuxingle on 2024/01/12
 * popupWindow管理
 */
object PopupWindows {

    private var textId = AtomicLong(0)

    /**
     * 创建无边框窗口
     */
    fun createBorderlessWindow(): JFrame {
        return BorderlessPopupWindow()
    }

}