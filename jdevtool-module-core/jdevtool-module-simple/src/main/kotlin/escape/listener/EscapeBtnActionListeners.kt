package com.wxl.jdevtool.escape.listener

import com.wxl.jdevtool.ComponentListener
import com.wxl.jdevtool.escape.EscapeTabbedModule
import com.wxl.jdevtool.escape.EscapeType
import com.wxl.jdevtool.util.EscapeUtils
import org.springframework.beans.factory.annotation.Autowired
import java.awt.event.ActionEvent
import java.awt.event.ActionListener

/**
 * Create by wuxingle on 2024/02/06
 * 转义按钮处理
 */


@ComponentListener("escapeTabbedModule.escapeBtn")
class EscapeBtnActionListener(
    @Autowired val escapeTabbedModule: EscapeTabbedModule
) : ActionListener {

    override fun actionPerformed(e: ActionEvent) {
        val text = escapeTabbedModule.leftTextArea.text
        if (!escapeTabbedModule.leftChecker.check()) {
            return
        }

        val escapeType = EscapeType.valueOf(escapeTabbedModule.radioGroup.selection.actionCommand)
        val escapeText = when (escapeType) {
            EscapeType.STRING -> EscapeUtils.escapeString(text)
            EscapeType.JSON -> EscapeUtils.escapeJson(text)
            EscapeType.XML -> EscapeUtils.escapeXml(text)
            EscapeType.HTML -> EscapeUtils.escapeHtml(text)
            EscapeType.URL -> EscapeUtils.escapeURL(text)
            EscapeType.UNICODE -> EscapeUtils.escapeUnicode(text)
        }

        escapeTabbedModule.rightTextArea.text = escapeText
    }
}

@ComponentListener("escapeTabbedModule.unescapeBtn")
class UnescapeBtnActionListener(
    @Autowired val escapeTabbedModule: EscapeTabbedModule
) : ActionListener {

    override fun actionPerformed(e: ActionEvent) {
        val text = escapeTabbedModule.leftTextArea.text
        if (!escapeTabbedModule.leftChecker.check()) {
            return
        }
        val escapeType = EscapeType.valueOf(escapeTabbedModule.radioGroup.selection.actionCommand)
        val escapeText = when (escapeType) {
            EscapeType.STRING -> EscapeUtils.unescapeString(text)
            EscapeType.JSON -> EscapeUtils.unescapeJson(text)
            EscapeType.XML -> EscapeUtils.unescapeXml(text)
            EscapeType.HTML -> EscapeUtils.unescapeHtml(text)
            EscapeType.URL -> EscapeUtils.unescapeURL(text)
            EscapeType.UNICODE -> EscapeUtils.unescapeUnicode(text)
        }

        escapeTabbedModule.rightTextArea.text = escapeText
    }
}
