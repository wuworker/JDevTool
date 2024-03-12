package com.wxl.jdevtool.encrypt.component

import com.wxl.jdevtool.component.ComponentFactory
import com.wxl.jdevtool.encrypt.KeyShowStyle
import com.wxl.jdevtool.encrypt.extension.*
import com.wxl.jdevtool.extension.showCaretLocation
import com.wxl.jdevtool.listener.FlowResizeComponentListener
import com.wxl.jdevtool.toast.ToastType
import com.wxl.jdevtool.toast.Toasts
import com.wxl.jdevtool.util.ClipboardUtils
import com.wxl.jdevtool.validate.InputChecker
import com.wxl.jdevtool.validate.InputValidate
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea
import org.fife.ui.rtextarea.RTextScrollPane
import java.awt.BorderLayout
import java.awt.Component
import java.awt.FlowLayout
import java.awt.event.ActionListener
import java.awt.event.FocusEvent
import java.io.Serial
import javax.swing.ButtonGroup
import javax.swing.JPanel
import javax.swing.JRadioButton
import javax.swing.ScrollPaneConstants
import javax.swing.text.JTextComponent

/**
 * Create by wuxingle on 2024/02/06
 * 定义了如何展示字节内容界面
 */
class ByteAreaPanel : JPanel(), InputValidate {

    // 上次的展示方式
    private var lastShowStyle: KeyShowStyle

    val headPanel: JPanel

    val stringRadio: JRadioButton

    val base64Radio: JRadioButton

    val hexRadio: JRadioButton

    val radioGroup: ButtonGroup

    val textArea: RSyntaxTextArea

    val textAreaSp: RTextScrollPane

    val inputChecker: InputChecker

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
        textArea.showCaretLocation()
        textAreaSp = RTextScrollPane(textArea)
        textAreaSp.verticalScrollBarPolicy = ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED
        textAreaSp.horizontalScrollBarPolicy = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER
        inputChecker = object : InputChecker(textArea, textAreaSp) {
            override fun doCheck(component: JTextComponent): Boolean {
                return try {
                    data
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

        layout = BorderLayout()
        add(headPanel, BorderLayout.NORTH)
        add(textAreaSp)

        initListener()
    }

    private fun initListener() {
        headPanel.addComponentListener(FlowResizeComponentListener())

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
                inputChecker.showWarn()
                setShowStyle(lastShowStyle)
                return@ActionListener
            }
            inputChecker.showNormal()

            data = bytes

            lastShowStyle = showStyle
        }

        stringRadio.addActionListener(itemListener)
        base64Radio.addActionListener(itemListener)
        hexRadio.addActionListener(itemListener)
    }

    /**
     * 添加自定义组件
     */
    fun addCustom(c: Component) {
        headPanel.add(c)
    }

    /**
     * 增加复制按钮
     */
    fun addCopyBtn() {
        val copyBtn = ComponentFactory.createCopyBtn()
        copyBtn.addActionListener {
            val text = textArea.text
            if (text.isNotBlank()) {
                ClipboardUtils.setText(text)
                Toasts.show(ToastType.SUCCESS, "复制成功")
            }
        }
        headPanel.add(copyBtn)
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

    override val component = textArea

    override fun check(focus: Boolean) = inputChecker.check(focus)

    companion object {
        @Serial
        private const val serialVersionUID: Long = 5157414515518004264L
    }

}
