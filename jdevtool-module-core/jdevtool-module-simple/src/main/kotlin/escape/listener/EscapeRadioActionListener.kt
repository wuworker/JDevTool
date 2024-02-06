package com.wxl.jdevtool.escape.listener

import com.wxl.jdevtool.ComponentListener
import com.wxl.jdevtool.escape.EscapeTabbedModule
import com.wxl.jdevtool.escape.EscapeType
import org.fife.ui.rsyntaxtextarea.SyntaxConstants
import org.springframework.beans.factory.annotation.Autowired
import java.awt.event.ActionEvent
import java.awt.event.ActionListener

/**
 * Create by wuxingle on 2024/02/05
 * 选项选择
 */
@ComponentListener(
    "escapeTabbedModule.stringRadio",
    "escapeTabbedModule.jsonRadio",
    "escapeTabbedModule.xmlRadio",
    "escapeTabbedModule.htmlRadio",
    "escapeTabbedModule.urlRadio",
    "escapeTabbedModule.unicodeRadio"
)
class EscapeRadioActionListener(
    @Autowired val escapeTabbedModule: EscapeTabbedModule
) : ActionListener {

    override fun actionPerformed(e: ActionEvent) {
        val escapeType = EscapeType.valueOf(escapeTabbedModule.radioGroup.selection.actionCommand)

        when (escapeType) {
            EscapeType.JSON -> {
                escapeTabbedModule.leftTextArea.syntaxEditingStyle = SyntaxConstants.SYNTAX_STYLE_JSON
                escapeTabbedModule.rightTextArea.syntaxEditingStyle = SyntaxConstants.SYNTAX_STYLE_JSON
            }

            EscapeType.XML -> {
                escapeTabbedModule.leftTextArea.syntaxEditingStyle = SyntaxConstants.SYNTAX_STYLE_XML
                escapeTabbedModule.rightTextArea.syntaxEditingStyle = SyntaxConstants.SYNTAX_STYLE_XML
            }

            EscapeType.HTML -> {
                escapeTabbedModule.leftTextArea.syntaxEditingStyle = SyntaxConstants.SYNTAX_STYLE_HTML
                escapeTabbedModule.rightTextArea.syntaxEditingStyle = SyntaxConstants.SYNTAX_STYLE_HTML
            }

            else -> {
                escapeTabbedModule.leftTextArea.syntaxEditingStyle = SyntaxConstants.SYNTAX_STYLE_NONE
                escapeTabbedModule.rightTextArea.syntaxEditingStyle = SyntaxConstants.SYNTAX_STYLE_NONE
            }
        }

    }
}