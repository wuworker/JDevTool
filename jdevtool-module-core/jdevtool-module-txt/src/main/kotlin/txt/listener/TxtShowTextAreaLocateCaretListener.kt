package com.wxl.jdevtool.txt.listener

import com.wxl.jdevtool.ComponentListener
import com.wxl.jdevtool.listener.TextAreaCaretLocationListener
import com.wxl.jdevtool.txt.TxtTabbedModule
import org.springframework.beans.factory.annotation.Autowired

/**
 * Create by wuxingle on 2024/01/08
 * 显示文本域光标定位信息
 */
@ComponentListener("txtTabbedModule.leftTextArea", "txtTabbedModule.rightTextArea")
class TxtShowTextAreaLocateCaretListener(
    @Autowired val txtTabbedModule: TxtTabbedModule
) : TextAreaCaretLocationListener {

    override fun caretLocation(line: Int, column: Int, selectedText: String?) {
        val label = txtTabbedModule.areaLocateLabel
        val sb = StringBuilder("$line:$column")
        if (!selectedText.isNullOrEmpty()) {
            sb.append("（${selectedText.length} chars")
            val lineBreaks = selectedText.split(System.lineSeparator()).size - 1
            sb.append(if (lineBreaks > 0) ", $lineBreaks line break）" else "）")
        }
        label.text = sb.toString()
    }
}


