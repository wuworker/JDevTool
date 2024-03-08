package com.wxl.jdevtool.component

import com.formdev.flatlaf.FlatClientProperties
import com.wxl.jdevtool.Icons
import com.wxl.jdevtool.validate.InputChecker
import com.wxl.jdevtool.validate.InputValidate
import io.github.oshai.kotlinlogging.KotlinLogging
import java.awt.FlowLayout
import java.io.Serial
import javax.swing.*
import javax.swing.event.DocumentEvent
import javax.swing.text.JTextComponent


/**
 * Create by wuxingle on 2024/01/04
 * label和text输入框组合
 */
class LabelTextPanel(
    labelTxt: String = ""
) : JPanel(FlowLayout()), InputValidate {

    private val log = KotlinLogging.logger { }

    private var lastSelected = false

    private val lineSeparator: String = System.lineSeparator()

    private val inputChecker: InputChecker

    val label: JLabel

    val textField: JTextField

    val newLineBtn: JToggleButton

    val text: String
        get() {
            return if (newLineBtn.isSelected) {
                lineSeparator
            } else {
                textField.text
            }
        }

    init {
        label = JLabel(labelTxt)

        textField = JTextField(4)

        val toolbar = JToolBar()
        newLineBtn = JToggleButton(Icons.newLine)
        with(newLineBtn) {
            isContentAreaFilled = false
            rolloverIcon = Icons.newLineHover
            selectedIcon = Icons.newLineSelected
            toolTipText = "匹配换行${lineSeparator}启用后相当于输入换行符"
        }
        toolbar.add(newLineBtn)
        textField.putClientProperty(FlatClientProperties.TEXT_FIELD_TRAILING_COMPONENT, toolbar)

        inputChecker = object : InputChecker(textField) {

            override fun doCheck(component: JTextComponent): Boolean {
                return this@LabelTextPanel.text.isNotEmpty()
            }

            override fun documentUpdate(e: DocumentEvent) {
                showNormal()
            }
        }

        add(label)
        add(textField)

        addBtnListener()
    }

    private fun addBtnListener() {
        newLineBtn.addChangeListener {
            val btn = it.source as JToggleButton
            val selected = btn.isSelected
            if (selected == lastSelected) {
                return@addChangeListener
            }

            log.info { "change: $it" }
            inputChecker.showNormal()
            textField.requestFocus()
            if (selected) {
                textField.text = ""
                textField.isEditable = false
            } else {
                textField.isEditable = true
            }
            lastSelected = selected
        }
    }

    fun clearText() {
        textField.text = ""
        newLineBtn.isSelected = false
    }

    fun setNewLine() {
        newLineBtn.isSelected = true
    }

    override val component = textField

    override fun check(focus: Boolean): Boolean {
        return inputChecker.check(focus)
    }

    companion object {
        @Serial
        private const val serialVersionUID: Long = -8258905862664446421L
    }
}

