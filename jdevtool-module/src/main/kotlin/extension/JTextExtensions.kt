package com.wxl.jdevtool.extension

import com.formdev.flatlaf.FlatClientProperties
import com.wxl.jdevtool.component.ComponentFactory
import com.wxl.jdevtool.listener.TextAreaCaretLocationShowListener
import com.wxl.jdevtool.toast.ToastType
import com.wxl.jdevtool.toast.Toasts
import com.wxl.jdevtool.util.ClipboardUtils
import org.apache.commons.text.StringEscapeUtils
import java.awt.Dimension
import javax.swing.JTextArea
import javax.swing.JTextField
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener
import javax.swing.text.JTextComponent

/**
 * Create by wuxingle on 2024/05/11
 * JTexComponent相关扩展
 */

/**
 * 获取转义后的text
 */
fun JTextField.getUnescapeText(): String {
    return StringEscapeUtils.unescapeJava(text)
}

/**
 * JTextComponent 设置提示
 */
fun JTextField.setHint(hint: String) {
    putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, hint)
}

/**
 * 显示清空按钮
 */
fun JTextField.showClear() {
    putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true)
}

/**
 * 显示复制按钮
 */
fun JTextField.showCopy() {
    val copyBtn = ComponentFactory.createCopyBtn()
    copyBtn.addActionListener {
        if (text.isNotBlank()) {
            ClipboardUtils.setText(text)
            Toasts.show(ToastType.SUCCESS, "复制成功")
        }
    }
    putClientProperty(FlatClientProperties.TEXT_FIELD_TRAILING_COMPONENT, copyBtn)
}

/**
 * 随输入变化长度
 */
fun JTextField.resizeable() {
    document.addDocumentListener(object : DocumentListener {
        override fun insertUpdate(e: DocumentEvent) {
            resize(e.length)
        }

        override fun removeUpdate(e: DocumentEvent) {
            resize(e.length)
        }

        override fun changedUpdate(e: DocumentEvent) {
            resize(e.length)
        }

        private fun resize(len: Int) {
            val width = getFontMetrics(font).stringWidth(text)
            preferredSize = Dimension(width + 7, preferredSize.height)
            revalidate()
        }
    })
}

/**
 * 显示光标定位信息
 */
fun JTextArea.showCaretLocation() {
    addCaretListener(TextAreaCaretLocationShowListener())
}

/**
 * JTextComponent 设置输入类型
 */
enum class TextInputType {
    // 仅数字
    NUMBER,

    // 日期，时间
    DATE, TIME, DATETIME
}

/**
 * JTextComponent 设置输入类型
 * 加在原document的listener会不生效
 */
fun JTextComponent.setInputType(type: TextInputType) {
    document = when (type) {
        TextInputType.NUMBER -> CandidatePlainDocument("0123456789")
        TextInputType.DATE -> CandidatePlainDocument("0123456789-")
        TextInputType.TIME -> CandidatePlainDocument("0123456789:")
        TextInputType.DATETIME -> CandidatePlainDocument("0123456789-: ")
    }
}
