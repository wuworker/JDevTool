package com.wxl.jdevtool.component

import com.wxl.jdevtool.AppContexts
import com.wxl.jdevtool.Icons
import com.wxl.jdevtool.extension.showCaretLocation
import com.wxl.jdevtool.util.JsonUtils
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea
import org.fife.ui.rsyntaxtextarea.SyntaxConstants
import org.fife.ui.rtextarea.RTextArea
import org.fife.ui.rtextarea.RecordableTextAction
import java.awt.event.ActionEvent
import javax.swing.Icon
import javax.swing.JButton
import javax.swing.JPopupMenu
import javax.swing.UIManager
import javax.swing.event.CaretEvent

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
            pressedIcon = Icons.copyPress
            isContentAreaFilled = false
        }

        return btn
    }

    /**
     * 图片按钮
     */
    fun createIconBtn(icon: Icon, text: String = "", toolTip: String = ""): JButton {
        val btn = JButton(icon)
        with(btn) {
            this.text = text
            isBorderPainted = false
            background = UIManager.getColor("Panel.background")
            toolTipText = toolTip
        }
        return btn
    }

    /**
     * 创建textarea
     */
    private val textAreaFormatAction = object : RecordableTextAction("格式化") {
        override fun actionPerformedImpl(e: ActionEvent, textArea: RTextArea) {
            if (textArea is RSyntaxTextArea) {
                if (textArea.syntaxEditingStyle == SyntaxConstants.SYNTAX_STYLE_JSON) {
                    try {
                        textArea.text = JsonUtils.formatJson(textArea.text)
                    } catch (e: Exception) {
                        //ignore
                    }
                }
            }
        }

        override fun getMacroID() = "format-code"
    }

    private val textAreaCompressAction = object : RecordableTextAction("压缩") {
        override fun actionPerformedImpl(e: ActionEvent, textArea: RTextArea) {
            if (textArea is RSyntaxTextArea) {
                if (textArea.syntaxEditingStyle == SyntaxConstants.SYNTAX_STYLE_JSON) {
                    try {
                        textArea.text = JsonUtils.compressJson(textArea.text)
                    } catch (e: Exception) {
                        //ignore
                    }
                }
            }
        }

        override fun getMacroID() = "compress-code"
    }

    fun createTextArea(): RSyntaxTextArea {
        return createTextArea { }
    }

    fun <R> createTextArea(custom: RSyntaxTextArea.() -> R): RSyntaxTextArea {
        val textArea = object : RSyntaxTextArea() {

            private val formatAction = createPopupMenuItem(textAreaFormatAction)

            private val compressAction = createPopupMenuItem(textAreaCompressAction)

            override fun createPopupMenu(): JPopupMenu {
                val menu = super.createPopupMenu()

                menu.addSeparator()
                menu.add(formatAction)
                menu.add(compressAction)
                return menu
            }

            override fun configurePopupMenu(popupMenu: JPopupMenu?) {
                super.configurePopupMenu(popupMenu)

                formatAction.isEnabled = syntaxEditingStyle == SyntaxConstants.SYNTAX_STYLE_JSON && text.isNotBlank()
                compressAction.isEnabled = formatAction.isEnabled
            }

            override fun fireCaretUpdate(e: CaretEvent) {
                super.fireCaretUpdate(e)

                formatAction.isEnabled = syntaxEditingStyle == SyntaxConstants.SYNTAX_STYLE_JSON && text.isNotBlank()
                compressAction.isEnabled = formatAction.isEnabled
            }
        }

        with(textArea) {
            syntaxEditingStyle = SyntaxConstants.SYNTAX_STYLE_NONE
            isCodeFoldingEnabled = true
            showCaretLocation()
        }
        textArea.custom()

        AppContexts.theme.textAreaTheme.apply(textArea)
        return textArea
    }
}