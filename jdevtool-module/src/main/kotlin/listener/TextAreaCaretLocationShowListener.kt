package com.wxl.jdevtool.listener

import com.wxl.jdevtool.message.MessageNotifier

/**
 * Create by wuxingle on 2024/01/12
 * 显示文本域光标定位信息
 */
open class TextAreaCaretLocationShowListener(
    private val messageNotifier: MessageNotifier
) : TextAreaCaretLocationChangeListener {

    override fun caretLocationUpdate(line: Int, column: Int, selectedText: String?) {
        val sb = StringBuilder("$line:$column")
        if (!selectedText.isNullOrEmpty()) {
            sb.append("（${selectedText.length} chars")
            val lineBreaks = selectedText.split(System.lineSeparator()).size - 1
            sb.append(if (lineBreaks > 0) ", $lineBreaks line break）" else "）")
        }
        messageNotifier.showMouseCaret(sb.toString())
    }
}

