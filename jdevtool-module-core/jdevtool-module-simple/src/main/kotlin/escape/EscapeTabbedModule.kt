package com.wxl.jdevtool.escape

import com.formdev.flatlaf.extras.FlatSVGIcon
import com.wxl.jdevtool.ComponentId
import com.wxl.jdevtool.TabbedModule
import com.wxl.jdevtool.component.ComponentFactory
import com.wxl.jdevtool.validate.InputChecker
import org.fife.ui.rtextarea.RTextScrollPane
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import java.awt.BorderLayout
import java.awt.FlowLayout
import javax.swing.*
import javax.swing.event.DocumentEvent
import javax.swing.text.JTextComponent

/**
 * Create by wuxingle on 2024/02/05
 * 字符转义处理
 */
@Order(400)
@Component
@ComponentId("escapeTabbedModule")
class EscapeTabbedModule : TabbedModule {

    override val mainPanel = JPanel(BorderLayout())

    @ComponentId("headPanel")
    val headPanel = JPanel(FlowLayout(FlowLayout.LEFT, 10, 10))

    @ComponentId("stringRadio")
    val stringRadio = JRadioButton("字符串")

    @ComponentId("jsonRadio")
    val jsonRadio = JRadioButton("JSON")

    @ComponentId("xmlRadio")
    val xmlRadio = JRadioButton("XML")

    @ComponentId("htmlRadio")
    val htmlRadio = JRadioButton("HTML")

    @ComponentId("urlRadio")
    val urlRadio = JRadioButton("URL")

    @ComponentId("unicodeRadio")
    val unicodeRadio = JRadioButton("UNICODE")

    val radioGroup = ButtonGroup()

    @ComponentId("escapeBtn")
    val escapeBtn = JButton("转义")

    @ComponentId("unescapeBtn")
    val unescapeBtn = JButton("反转义")

    @ComponentId("leftTextArea")
    val leftTextArea = ComponentFactory.createTextArea {
        lineWrap = true
    }

    val leftTextAreaSp = RTextScrollPane(leftTextArea)

    val leftChecker = object : InputChecker(leftTextArea, leftTextAreaSp) {
        override fun doCheck(component: JTextComponent): Boolean {
            return !leftTextArea.text.isNullOrBlank()
        }

        override fun documentUpdate(e: DocumentEvent) {
            showNormal()
        }
    }

    @ComponentId("rightTextArea")
    val rightTextArea = ComponentFactory.createTextArea {
        lineWrap = true
    }

    override fun afterPropertiesSet() {
        // 上方控制
        stringRadio.actionCommand = EscapeType.STRING.name
        stringRadio.isSelected = true
        jsonRadio.actionCommand = EscapeType.JSON.name
        xmlRadio.actionCommand = EscapeType.XML.name
        htmlRadio.actionCommand = EscapeType.HTML.name
        urlRadio.actionCommand = EscapeType.URL.name
        unicodeRadio.actionCommand = EscapeType.UNICODE.name
        with(radioGroup) {
            add(stringRadio)
            add(jsonRadio)
            add(xmlRadio)
            add(htmlRadio)
            add(urlRadio)
            add(unicodeRadio)
        }

        with(headPanel) {
            border = BorderFactory.createTitledBorder("转义类型")
            add(stringRadio)
            add(jsonRadio)
            add(xmlRadio)
            add(htmlRadio)
            add(urlRadio)
            add(unicodeRadio)
            add(escapeBtn)
            add(unescapeBtn)
        }

        // 下方文本域
        val leftPanel = JPanel(BorderLayout())
        leftPanel.border = BorderFactory.createTitledBorder("输入：")
        leftPanel.add(leftTextAreaSp)

        val rightPanel = JPanel(BorderLayout())
        rightPanel.border = BorderFactory.createTitledBorder("结果：")
        val rightTextAreaSp = RTextScrollPane(rightTextArea)
        rightPanel.add(rightTextAreaSp)

        leftTextAreaSp.horizontalScrollBarPolicy = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER
        rightTextAreaSp.horizontalScrollBarPolicy = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER

        val downPanel = JSplitPane(JSplitPane.VERTICAL_SPLIT)
        with(downPanel) {
            resizeWeight = 0.5
            leftComponent = leftPanel
            rightComponent = rightPanel
        }

        with(mainPanel) {
            add(headPanel, BorderLayout.NORTH)
            add(downPanel)
        }
    }

    override val title = "字符转义"

    override val icon = FlatSVGIcon("icons/escape_dark.svg")

    override val tip = "字符转义"
}
