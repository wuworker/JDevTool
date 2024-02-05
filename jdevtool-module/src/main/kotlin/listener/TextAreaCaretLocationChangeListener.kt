package com.wxl.jdevtool.listener

import javax.swing.JTextArea
import javax.swing.event.CaretEvent
import javax.swing.event.CaretListener

/**
 * Create by wuxingle on 2024/01/08
 * 用于textarea
 * 监听光标定位的行，列(从1开始)，选中字符
 */
interface TextAreaCaretLocationChangeListener : CaretListener {

    override fun caretUpdate(e: CaretEvent) {
        val area = e.source as? JTextArea ?: return

        val caretpos = area.caretPosition
        val lineNum = area.getLineOfOffset(caretpos)
        val columNum = caretpos - area.getLineStartOffset(lineNum)
        val selectedText = area.selectedText

        caretLocationUpdate(lineNum + 1, columNum + 1, selectedText)
    }

    fun caretLocationUpdate(line: Int, column: Int, selectedText: String?)
}
