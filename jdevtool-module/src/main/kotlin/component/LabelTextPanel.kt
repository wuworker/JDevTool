package com.wxl.jdevtool.component

import com.wxl.jdevtool.Icons
import java.awt.FlowLayout
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.io.Serial
import javax.swing.*


/**
 * Create by wuxingle on 2024/01/04
 * label和text输入框组合
 */
class LabelTextPanel(
    labelTxt: String = ""
) : JPanel(FlowLayout()) {

    private var newLine = false

    private val lineSeparator: String = System.lineSeparator()

    val label: JLabel

    val textField: JTextField

    val newLineBtn: JButton

    val text: String
        get() {
            return if (newLine) {
                lineSeparator
            } else {
                textField.text
            }
        }

    init {
        label = JLabel(labelTxt)

        textField = JTextField(3)
        newLineBtn = JButton(Icons.newLine)
        with(newLineBtn) {
            background = textField.background
            foreground = textField.foreground
            border = BorderFactory.createEmptyBorder()
            toolTipText = "匹配换行${lineSeparator}启用后相当于输入换行符"
        }

        val box = Box.createHorizontalBox()
        box.border = textField.border
        box.add(textField)
        box.add(newLineBtn)

        textField.border = BorderFactory.createEmptyBorder()

        add(label)
        add(box)

        addBtnListener()
    }

    private fun addBtnListener() {
        newLineBtn.addMouseListener(object : MouseAdapter() {
            override fun mouseEntered(e: MouseEvent?) {
                if (!newLine) {
                    newLineBtn.icon = Icons.newLineHover
                }
            }

            override fun mouseExited(e: MouseEvent?) {
                if (!newLine) {
                    newLineBtn.icon = Icons.newLine
                }
            }
        })
        newLineBtn.addActionListener {
            newLine = !newLine
            if (newLine) {
                newLineBtn.icon = Icons.newLineSelected
                textField.text = ""
                textField.isEditable = false
            } else {
                textField.isEditable = true
                newLineBtn.icon = Icons.newLineHover
            }
        }
    }

    fun clearText() {
        textField.text = ""
        newLine = false
        newLineBtn.icon = Icons.newLine
    }


    companion object {
        @Serial
        private const val serialVersionUID: Long = -8258905862664446421L
    }
}

