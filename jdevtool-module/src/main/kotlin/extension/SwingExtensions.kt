package com.wxl.jdevtool.extension

import com.formdev.flatlaf.FlatClientProperties
import com.wxl.jdevtool.component.ComponentFactory
import com.wxl.jdevtool.listener.ClickRequestFocusMouseListener
import com.wxl.jdevtool.listener.TextAreaCaretLocationShowListener
import com.wxl.jdevtool.toast.ToastType
import com.wxl.jdevtool.toast.Toasts
import com.wxl.jdevtool.util.ClipboardUtils
import java.awt.Component
import java.awt.Window
import javax.swing.JTextArea
import javax.swing.JTextField
import javax.swing.text.JTextComponent

/**
 * Create by wuxingle on 2024/01/18
 * swing扩展函数
 */

/**
 * Window相对位置
 */
object RelativeLocation {
    const val LEFT = 1
    const val RIGHT = 2
    const val TOP = 3
    const val BOTTOM = 4

    // 居左，或居上
    const val ALIGN_LEFT = 1
    const val ALIGN_CENTER = 2

    // 居右，或居下
    const val ALIGN_RIGHT = 3
}

/**
 * 设置相当于parent组件的对齐方式
 * 需要先设置该窗口的size
 */
fun Window.setLocationRelativeTo(parent: Component, location: Int, align: Int = RelativeLocation.ALIGN_CENTER) {
    val parentPoint = parent.locationOnScreen
    val parentSize = parent.size

    var x = 0
    var y = 0
    if (location == RelativeLocation.TOP || location == RelativeLocation.BOTTOM) {
        y = when (location) {
            RelativeLocation.TOP -> parentPoint.y - size.height
            else -> parentPoint.y + parentSize.height
        }
        x = when (align) {
            RelativeLocation.ALIGN_LEFT -> parentPoint.x
            RelativeLocation.ALIGN_CENTER -> (parentPoint.x + parentSize.width / 2) - size.width / 2
            else -> parentPoint.x + parentSize.width - size.width
        }
    } else if (location == RelativeLocation.LEFT || location == RelativeLocation.RIGHT) {
        x = when (location) {
            RelativeLocation.LEFT -> parentPoint.x - size.width
            else -> parentPoint.x + parentSize.width
        }
        y = when (align) {
            RelativeLocation.ALIGN_LEFT -> parentPoint.y
            RelativeLocation.ALIGN_CENTER -> (parentPoint.y + parentSize.height / 2) - size.height / 2
            else -> parentPoint.y + parentSize.height - size.height
        }
    }

    setLocation(x, y)
}

/**
 * Component设置被点击自动获取焦点
 */
fun Component.enableClickRequestFocus() {
    addMouseListener(ClickRequestFocusMouseListener())
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
