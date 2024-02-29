package com.wxl.jdevtool.encrypt.component

import com.wxl.jdevtool.encrypt.KeyShowStyle
import com.wxl.jdevtool.encrypt.extension.*
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea
import org.fife.ui.rtextarea.RTextScrollPane
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Component
import java.awt.FlowLayout
import java.awt.event.ActionListener
import java.awt.event.FocusAdapter
import java.awt.event.FocusEvent
import java.io.Serial
import javax.swing.*
import javax.swing.border.Border
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener

/**
 * Create by wuxingle on 2024/02/06
 * 定义了如何展示字节内容界面
 */
class ByteAreaPanel : JPanel() {

    // 上次的展示方式
    private var lastShowStyle: KeyShowStyle

    val headPanel: JPanel

    val stringRadio: JRadioButton

    val base64Radio: JRadioButton

    val hexRadio: JRadioButton

    val radioGroup: ButtonGroup

    val textArea: RSyntaxTextArea

    val textAreaSp: RTextScrollPane

    private val normalBorder: Border

    private val warnBorder: Border

    // 当前展示方式
    val showStyle: KeyShowStyle
        get() = KeyShowStyle.valueOf(radioGroup.selection.actionCommand)

    // 字节内容
    var data: ByteArray
        get() = getData(showStyle)
        set(value) {
            textArea.text = when (showStyle) {
                KeyShowStyle.STRING -> value.toDisplayString()
                KeyShowStyle.BASE64 -> value.toBase64()
                KeyShowStyle.HEX -> value.toHexString()
            }
        }

    init {
        headPanel = JPanel(FlowLayout(FlowLayout.LEFT))

        stringRadio = JRadioButton("字符串")
        stringRadio.actionCommand = KeyShowStyle.STRING.name
        base64Radio = JRadioButton("BASE64")
        base64Radio.actionCommand = KeyShowStyle.BASE64.name
        hexRadio = JRadioButton("十六进制")
        hexRadio.actionCommand = KeyShowStyle.HEX.name
        radioGroup = ButtonGroup()
        with(headPanel) {
            add(stringRadio)
            add(base64Radio)
            add(hexRadio)
        }
        with(radioGroup) {
            add(stringRadio)
            add(base64Radio)
            add(hexRadio)
        }
        stringRadio.isSelected = true
        lastShowStyle = KeyShowStyle.STRING

        textArea = RSyntaxTextArea()
        textArea.lineWrap = true
        textAreaSp = RTextScrollPane(textArea)
        textAreaSp.verticalScrollBarPolicy = ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED
        textAreaSp.horizontalScrollBarPolicy = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER

        layout = BorderLayout()
        add(headPanel, BorderLayout.NORTH)
        add(textAreaSp)
        normalBorder = textAreaSp.border
        warnBorder = BorderFactory.createLineBorder(Color.RED)

        initListener()
    }

    private fun initListener() {
        val itemListener = ActionListener {
            // 当前选中的
            val showStyle = KeyShowStyle.valueOf(radioGroup.selection.actionCommand)
            if (showStyle == lastShowStyle) {
                return@ActionListener
            }

            // 获取字节内容
            val bytes = try {
                getData(lastShowStyle)
            } catch (e: Exception) {
                showWarn()
                setShowStyle(lastShowStyle)
                return@ActionListener
            }
            clearWarn()

            data = bytes

            lastShowStyle = showStyle
        }

        stringRadio.addActionListener(itemListener)
        base64Radio.addActionListener(itemListener)
        hexRadio.addActionListener(itemListener)

        // 文本清空时，取消红框
        textArea.document.addDocumentListener(object : DocumentListener {
            override fun insertUpdate(e: DocumentEvent) {
                changedUpdate(e)
            }

            override fun removeUpdate(e: DocumentEvent) {
                changedUpdate(e)
            }

            override fun changedUpdate(e: DocumentEvent) {
                if (textArea.text.isBlank()) {
                    clearWarn()
                }
            }
        })

        // 失去焦点时，校验key
        textArea.addFocusListener(object : FocusAdapter() {
            override fun focusLost(e: FocusEvent?) {
                if (isDataLegal()) {
                    clearWarn()
                } else {
                    showWarn()
                }
            }
        })
    }

    /**
     * 添加自定义组件
     */
    fun addCustom(c: Component) {
        headPanel.add(c)
    }

    /**
     * 设置展示方式
     */
    fun setShowStyle(style: KeyShowStyle) {
        when (style) {
            KeyShowStyle.STRING -> stringRadio.isSelected = true
            KeyShowStyle.BASE64 -> base64Radio.isSelected = true
            KeyShowStyle.HEX -> hexRadio.isSelected = true
        }
        lastShowStyle = style
    }

    /**
     * 内容是否合法
     */
    fun isDataLegal(): Boolean {
        return try {
            data
            true
        } catch (e: Exception) {
            false
        }
    }

    /**
     * 根据style获取字节内容
     */
    private fun getData(style: KeyShowStyle): ByteArray {
        val text = textArea.text
        if (text.isBlank()) {
            return byteArrayOf()
        }
        return when (style) {
            KeyShowStyle.STRING -> text.fromDisplayString()
            KeyShowStyle.BASE64 -> text.fromBase64()
            KeyShowStyle.HEX -> text.fromHexString()
        }
    }

    /**
     * 输入校验失败，显示告警
     */
    private fun showWarn() {
        textArea.requestFocus()
        textAreaSp.border = warnBorder
    }

    /**
     * 清除告警
     */
    private fun clearWarn() {
        textAreaSp.border = normalBorder
    }

    companion object {
        @Serial
        private const val serialVersionUID: Long = 5157414515518004264L
    }

}
