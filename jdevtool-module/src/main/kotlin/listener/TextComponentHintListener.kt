package com.wxl.jdevtool.listener

import java.awt.Color
import java.awt.event.FocusEvent
import java.awt.event.FocusListener
import javax.swing.UIManager
import javax.swing.text.JTextComponent

/**
 * Create by wuxingle on 2024/01/29
 * 显示textComponent提示
 */
class TextComponentHintListener(
    val hint: String,
    val hintColor: Color = Color.GRAY
) : FocusListener {

    override fun focusGained(e: FocusEvent) {
        val textComponent = e.source as? JTextComponent ?: return

        if (textComponent.text == hint) {
            textComponent.text = ""
        }
        textComponent.foreground = UIManager.getColor("TextField.foreground")
    }

    override fun focusLost(e: FocusEvent) {
        val textComponent = e.source as? JTextComponent ?: return

        if (textComponent.text.isEmpty()) {
            textComponent.foreground = hintColor
            textComponent.text = hint
        }
    }
}
