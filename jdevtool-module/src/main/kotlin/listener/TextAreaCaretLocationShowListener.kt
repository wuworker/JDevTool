package com.wxl.jdevtool.listener

import com.wxl.jdevtool.message.MessageBar
import javax.swing.JTextArea
import javax.swing.event.CaretEvent
import javax.swing.event.CaretListener

/**
 * Create by wuxingle on 2024/01/12
 * 显示文本域光标定位信息
 */
open class TextAreaCaretLocationShowListener : CaretListener {

    override fun caretUpdate(e: CaretEvent) {
        val area = e.source as? JTextArea ?: return

        val caretpos = area.caretPosition
        val lineNum = area.getLineOfOffset(caretpos)
        val columNum = caretpos - area.getLineStartOffset(lineNum)
        val selectedText = area.selectedText

        showCaretLocation(lineNum + 1, columNum + 1, selectedText)
    }

    private fun showCaretLocation(line: Int, column: Int, selectedText: String?) {
        val sb = StringBuilder("$line:$column")
        if (!selectedText.isNullOrEmpty()) {
            sb.append("（${selectedText.length} chars")
            val lineBreaks = selectedText.split(System.lineSeparator()).size - 1
            sb.append(if (lineBreaks > 0) ", $lineBreaks line break）" else "）")
        }
        MessageBar.showMouseCaret(sb.toString())
    }
}

