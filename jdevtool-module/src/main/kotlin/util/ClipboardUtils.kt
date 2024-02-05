package com.wxl.jdevtool.util

import java.awt.Toolkit
import java.awt.datatransfer.StringSelection

/**
 * Create by wuxingle on 2024/01/31
 * 剪切板工具
 */
object ClipboardUtils {

    /**
     * 设置剪切板内容
     */
    fun setText(text: String) {
        val clipboard = Toolkit.getDefaultToolkit().systemClipboard
        val selection = StringSelection(text)
        clipboard.setContents(selection, null)
    }

}