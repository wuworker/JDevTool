package com.wxl.jdevtool.validate

import com.formdev.flatlaf.FlatClientProperties
import java.awt.event.FocusAdapter
import java.awt.event.FocusEvent
import javax.swing.JComponent
import javax.swing.UIManager
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener
import javax.swing.text.JTextComponent

/**
 * Create by wuxingle on 2024/03/07
 * 输入校验
 */
abstract class InputChecker(
    override final val component: JTextComponent,
    val realChangeBorderComponent: JComponent? = null
) : InputValidate {

    private val focusColor = UIManager.getColor("Component.focusColor")

    private val borderColor = UIManager.getColor("Component.borderColor")

    private val errorBorderColor = UIManager.getColor("Component.error.borderColor")

    private val errorFocusedBorderColor = UIManager.getColor("Component.error.focusedBorderColor")

    private var isWarn = false

    init {
        component.addFocusListener(object : FocusAdapter() {
            override fun focusGained(e: FocusEvent) {
                this@InputChecker.focusGained(e)
            }

            override fun focusLost(e: FocusEvent) {
                this@InputChecker.focusLost(e)
            }
        })

        component.document.addDocumentListener(object : DocumentListener {
            override fun insertUpdate(e: DocumentEvent) {
                this@InputChecker.insertUpdate(e)
            }

            override fun removeUpdate(e: DocumentEvent) {
                this@InputChecker.removeUpdate(e)
            }

            override fun changedUpdate(e: DocumentEvent) {
                this@InputChecker.changedUpdate(e)
            }
        })
    }

    fun isWarn(): Boolean = isWarn

    /**
     * 检查输入，失败显示红框
     */
    override fun check(focus: Boolean): Boolean {
        val res = doCheck(component)
        if (res) {
            showNormal()
        } else {
            showWarn(focus)
        }
        return res
    }

    /**
     * 正常边框
     */
    fun showNormal() {
        if (!isWarn) {
            return
        }
        if (realChangeBorderComponent == null) {
            component.putClientProperty(FlatClientProperties.OUTLINE, arrayOf(focusColor, borderColor))
        } else {
            realChangeBorderComponent.putClientProperty(FlatClientProperties.OUTLINE, arrayOf(focusColor, borderColor))
        }
        isWarn = false
    }

    /**
     * 告警边框
     */
    fun showWarn(focus: Boolean = true) {
        if (focus) {
            component.requestFocus()
        }
        if (isWarn) {
            return
        }

        if (realChangeBorderComponent == null) {
            component.putClientProperty(
                FlatClientProperties.OUTLINE,
                arrayOf(errorFocusedBorderColor, errorBorderColor)
            )
        } else {
            realChangeBorderComponent.putClientProperty(
                FlatClientProperties.OUTLINE, arrayOf(errorFocusedBorderColor, errorBorderColor)
            )
        }
        isWarn = true
    }

    protected open fun focusGained(e: FocusEvent) {

    }

    protected open fun focusLost(e: FocusEvent) {

    }

    protected open fun insertUpdate(e: DocumentEvent) {
        documentUpdate(e)
    }

    protected open fun removeUpdate(e: DocumentEvent) {
        documentUpdate(e)
    }

    protected open fun changedUpdate(e: DocumentEvent) {
        documentUpdate(e)
    }

    protected open fun documentUpdate(e: DocumentEvent) {

    }

    protected abstract fun doCheck(component: JTextComponent): Boolean

}
