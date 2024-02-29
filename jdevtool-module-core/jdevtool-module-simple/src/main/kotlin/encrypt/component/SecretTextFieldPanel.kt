package com.wxl.jdevtool.encrypt.component

import com.formdev.flatlaf.extras.FlatSVGIcon
import com.wxl.jdevtool.Icons
import com.wxl.jdevtool.encrypt.KeyShowStyle
import com.wxl.jdevtool.encrypt.extension.*
import com.wxl.jdevtool.extension.setHint
import com.wxl.jdevtool.util.ClipboardUtils
import java.awt.Color
import java.awt.FlowLayout
import java.awt.event.FocusAdapter
import java.awt.event.FocusEvent
import java.io.Serial
import java.util.function.Function
import javax.swing.*
import javax.swing.border.Border
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener

/**
 * Create by wuxingle on 2024/02/07
 * 密钥输入框
 */
class SecretTextFieldPanel(
    private val keyGen: Function<KeyShowStyle, ByteArray>
) : JPanel(FlowLayout(FlowLayout.LEFT)) {

    private val keyFormatIconBase64Icon = FlatSVGIcon("icons/encrypt/keyformat_base64.svg")
    private val keyFormatIconHexIcon = FlatSVGIcon("icons/encrypt/keyformat_hex.svg")
    private val keyFormatIconStringIcon = FlatSVGIcon("icons/encrypt/keyformat_string.svg")

    // 当前展示方式
    private var currentFormatIndex = 0

    private val normalBorder: Border

    private val warnBorder: Border

    val textField: JTextField

    val keyFormatBtn: JButton

    val keyGenBtn: JButton

    val keyCopyBtn: JButton

    val box: Box

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
            if (textField.text.isBlank() || textField.text == DEFAULT_HINT) {
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
        textField = JTextField(15)
        textField.setHint(DEFAULT_HINT)
        keyFormatBtn = JButton()
        with(keyFormatBtn) {
            icon = keyFormatIconBase64Icon
            background = textField.background
            foreground = textField.foreground
            border = BorderFactory.createEmptyBorder()
            toolTipText = "密钥BASE64方式展示"
        }

        keyGenBtn = JButton()
        with(keyGenBtn) {
            icon = FlatSVGIcon("icons/encrypt/keygen.svg")
            rolloverIcon = FlatSVGIcon("icons/encrypt/keygenHover.svg")
            pressedIcon = FlatSVGIcon("icons/encrypt/keygenSelected.svg")
            background = textField.background
            foreground = textField.foreground
            border = BorderFactory.createEmptyBorder()
            toolTipText = "随机生成密钥"
        }

        keyCopyBtn = JButton()
        with(keyCopyBtn) {
            icon = Icons.copy
            rolloverIcon = Icons.copyHover
            pressedIcon = Icons.copySelected
            disabledIcon = Icons.copyDisable

            background = textField.background
            foreground = textField.foreground
            border = BorderFactory.createEmptyBorder()
            toolTipText = "复制到剪切板"
        }

        normalBorder = textField.border
        warnBorder = BorderFactory.createLineBorder(Color.RED)

        box = Box.createHorizontalBox()
        box.border = normalBorder
        box.add(textField)
        box.add(keyFormatBtn)
        box.add(keyGenBtn)
        box.add(keyCopyBtn)

        textField.border = BorderFactory.createEmptyBorder()

        add(box)

        initListener()
    }

    private fun initListener() {
        // 文本清空时，取消红框
        textField.document.addDocumentListener(object : DocumentListener {
            override fun insertUpdate(e: DocumentEvent) {
                changedUpdate(e)
            }

            override fun removeUpdate(e: DocumentEvent) {
                changedUpdate(e)
            }

            override fun changedUpdate(e: DocumentEvent) {
                if (textField.text.isBlank()) {
                    clearWarn()
                }
            }
        })
        // 失去焦点时，校验key
        textField.addFocusListener(object : FocusAdapter() {
            override fun focusLost(e: FocusEvent?) {
                if (isKeyLegal()) {
                    clearWarn()
                } else {
                    showWarn()
                }
            }
        })

        keyFormatBtn.addActionListener {
            // 获取key的字节
            val keyBytes = try {
                key
            } catch (e: Exception) {
                showWarn()
                return@addActionListener
            }
            clearWarn()

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
            }
        }
    }

    /**
     * 输入校验失败，显示告警
     */
    private fun showWarn() {
        textField.requestFocus()
        box.border = warnBorder
    }

    /**
     * 取消告警
     */
    private fun clearWarn() {
        box.border = normalBorder
    }

    /**
     * 判断key格式是否合法
     */
    fun isKeyLegal(): Boolean {
        return try {
            key
            true
        } catch (e: Exception) {
            false
        }
    }

    companion object {
        @Serial
        private const val serialVersionUID: Long = 8540804453102355409L

        private const val DEFAULT_HINT = "输入密钥"
    }
}
