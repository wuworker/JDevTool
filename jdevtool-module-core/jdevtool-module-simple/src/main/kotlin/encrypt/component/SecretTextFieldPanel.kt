package com.wxl.jdevtool.encrypt.component

import com.formdev.flatlaf.FlatClientProperties
import com.formdev.flatlaf.extras.FlatSVGIcon
import com.wxl.jdevtool.component.ComponentFactory
import com.wxl.jdevtool.encrypt.KeyShowStyle
import com.wxl.jdevtool.encrypt.extension.*
import com.wxl.jdevtool.extension.setHint
import com.wxl.jdevtool.toast.ToastType
import com.wxl.jdevtool.toast.Toasts
import com.wxl.jdevtool.util.ClipboardUtils
import com.wxl.jdevtool.validate.InputChecker
import com.wxl.jdevtool.validate.InputValidate
import java.awt.FlowLayout
import java.awt.event.FocusEvent
import java.io.Serial
import java.util.function.Function
import javax.swing.JButton
import javax.swing.JPanel
import javax.swing.JTextField
import javax.swing.JToolBar
import javax.swing.text.JTextComponent

/**
 * Create by wuxingle on 2024/02/07
 * 密钥输入框
 */
class SecretTextFieldPanel(
    private val keyGen: Function<KeyShowStyle, ByteArray>
) : JPanel(FlowLayout(FlowLayout.LEFT)), InputValidate {

    private val keyFormatIconBase64Icon = FlatSVGIcon("icons/encrypt/keyformat_base64.svg")
    private val keyFormatIconHexIcon = FlatSVGIcon("icons/encrypt/keyformat_hex.svg")
    private val keyFormatIconStringIcon = FlatSVGIcon("icons/encrypt/keyformat_string.svg")

    // 当前展示方式
    private var currentFormatIndex = 0

    val textField = JTextField(15)

    val keyFormatBtn = JButton()

    val keyGenBtn = JButton()

    val keyCopyBtn = ComponentFactory.createCopyBtn()

    val inputChecker: InputChecker

    // 密钥展示方式
    val keyShowStyle: KeyShowStyle
        get() = when (currentFormatIndex % 3) {
            0 -> KeyShowStyle.BASE64
            1 -> KeyShowStyle.HEX
            2 -> KeyShowStyle.STRING
            else -> KeyShowStyle.BASE64
        }

    // 密钥
    var key: ByteArray
        get() {
            if (textField.text.isBlank()) {
                return byteArrayOf()
            }
            return when (keyShowStyle) {
                KeyShowStyle.BASE64 -> textField.text.fromBase64()
                KeyShowStyle.HEX -> textField.text.fromHexString()
                KeyShowStyle.STRING -> textField.text.fromDisplayString()
            }
        }
        set(value) {
            when (keyShowStyle) {
                KeyShowStyle.BASE64 -> textField.text = value.toBase64()
                KeyShowStyle.HEX -> textField.text = value.toHexString()
                KeyShowStyle.STRING -> textField.text = value.toDisplayString()
            }
        }

    init {
        textField.setHint("输入密钥")

        with(keyFormatBtn) {
            icon = keyFormatIconBase64Icon
            isContentAreaFilled = false
            toolTipText = "密钥BASE64方式展示"
        }

        with(keyGenBtn) {
            icon = FlatSVGIcon("icons/encrypt/keygen.svg")
            rolloverIcon = FlatSVGIcon("icons/encrypt/keygenHover.svg")
            pressedIcon = FlatSVGIcon("icons/encrypt/keygenSelected.svg")
            isContentAreaFilled = false
            toolTipText = "随机生成密钥"
        }

        with(keyCopyBtn) {
            toolTipText = "复制到剪切板"
        }

        val bar = JToolBar()
        bar.add(keyFormatBtn)
        bar.add(keyGenBtn)
        bar.add(keyCopyBtn)
        textField.putClientProperty(FlatClientProperties.TEXT_FIELD_TRAILING_COMPONENT, bar)

        inputChecker = object : InputChecker(textField) {
            override fun doCheck(component: JTextComponent): Boolean {
                return try {
                    key
                    true
                } catch (e: Exception) {
                    false
                }
            }

            override fun focusLost(e: FocusEvent) {
                if (component.text.isNotBlank()) {
                    check(false)
                }
            }
        }


        add(textField)

        initListener()
    }

    private fun initListener() {
        keyFormatBtn.addActionListener {
            // 获取key的字节
            val keyBytes = try {
                key
            } catch (e: Exception) {
                inputChecker.showWarn()
                return@addActionListener
            }
            inputChecker.showNormal()

            currentFormatIndex++
            when (keyShowStyle) {
                KeyShowStyle.BASE64 -> {
                    keyFormatBtn.icon = keyFormatIconBase64Icon
                    keyFormatBtn.toolTipText = "密钥BASE64方式展示"
                }

                KeyShowStyle.HEX -> {
                    keyFormatBtn.icon = keyFormatIconHexIcon
                    keyFormatBtn.toolTipText = "密钥十六进制方式展示"
                }

                KeyShowStyle.STRING -> {
                    keyFormatBtn.icon = keyFormatIconStringIcon
                    keyFormatBtn.toolTipText = "密钥字符串方式展示"
                }
            }

            if (keyBytes.isNotEmpty()) {
                key = keyBytes
            }
        }

        keyGenBtn.addActionListener {
            textField.requestFocus()
            key = keyGen.apply(keyShowStyle)
        }

        keyCopyBtn.addActionListener {
            val text = textField.text
            if (text.isNotBlank()) {
                ClipboardUtils.setText(text)
                Toasts.show(ToastType.SUCCESS, "复制成功")
            }
        }
    }

    override val component = textField

    override fun check(focus: Boolean) = inputChecker.check(focus)

    companion object {
        @Serial
        private const val serialVersionUID: Long = 8540804453102355409L

    }
}
