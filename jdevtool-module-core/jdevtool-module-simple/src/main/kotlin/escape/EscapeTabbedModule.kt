package com.wxl.jdevtool.escape

import com.formdev.flatlaf.extras.FlatSVGIcon
import com.wxl.jdevtool.ComponentId
import com.wxl.jdevtool.TabbedModule
import com.wxl.jdevtool.validate.InputChecker
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea
import org.fife.ui.rsyntaxtextarea.SyntaxConstants
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

    final override val mainPanel: JPanel

    @ComponentId("headPanel")
    final val headPanel: JPanel

    @ComponentId("stringRadio")
    final val stringRadio: JRadioButton

    @ComponentId("jsonRadio")
    final val jsonRadio: JRadioButton

    @ComponentId("xmlRadio")
    final val xmlRadio: JRadioButton

    @ComponentId("htmlRadio")
    final val htmlRadio: JRadioButton

    @ComponentId("urlRadio")
    final val urlRadio: JRadioButton

    @ComponentId("unicodeRadio")
    final val unicodeRadio: JRadioButton

    final val radioGroup: ButtonGroup

    final val downPanel: JSplitPane

    @ComponentId("escapeBtn")
    final val escapeBtn: JButton

    @ComponentId("unescapeBtn")
    final val unescapeBtn: JButton

    @ComponentId("leftTextArea")
    final val leftTextArea: RSyntaxTextArea

    final val leftTextAreaSp: RTextScrollPane

    final val leftChecker: InputChecker

    @ComponentId("rightTextArea")
    final val rightTextArea: RSyntaxTextArea

    final val rightTextAreaSp: RTextScrollPane

    init {
        mainPanel = JPanel()

        headPanel = JPanel()
        stringRadio = JRadioButton("字符串")
        jsonRadio = JRadioButton("JSON")
        xmlRadio = JRadioButton("XML")
        htmlRadio = JRadioButton("HTML")
        urlRadio = JRadioButton("URL")
        unicodeRadio = JRadioButton("UNICODE")
        escapeBtn = JButton("转义")
        unescapeBtn = JButton("反转义")
        radioGroup = ButtonGroup()

        downPanel = JSplitPane(JSplitPane.VERTICAL_SPLIT)
        leftTextArea = RSyntaxTextArea()
        leftTextAreaSp = RTextScrollPane(leftTextArea)
        leftChecker = object : InputChecker(leftTextArea, leftTextAreaSp) {
            override fun doCheck(component: JTextComponent): Boolean {
                return !leftTextArea.text.isNullOrBlank()
            }

            override fun documentUpdate(e: DocumentEvent) {
                showNormal()
            }
        }

        rightTextArea = RSyntaxTextArea()
        rightTextAreaSp = RTextScrollPane(rightTextArea)

        initUI()
    }

    /**
     * 布局初始化
     */
    private fun initUI() {
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
            layout = FlowLayout(FlowLayout.LEFT, 10, 10)
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
        rightPanel.add(rightTextAreaSp)

        with(downPanel) {
            leftTextArea.lineWrap = true
            leftTextArea.syntaxEditingStyle = SyntaxConstants.SYNTAX_STYLE_NONE
            leftTextArea.isCodeFoldingEnabled = true
            leftTextAreaSp.verticalScrollBarPolicy = ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED
            leftTextAreaSp.horizontalScrollBarPolicy = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER

            rightTextArea.lineWrap = true
            rightTextArea.syntaxEditingStyle = SyntaxConstants.SYNTAX_STYLE_NONE
            rightTextArea.isCodeFoldingEnabled = true
            rightTextAreaSp.verticalScrollBarPolicy = ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED
            rightTextAreaSp.horizontalScrollBarPolicy = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER

            resizeWeight = 0.5
            leftComponent = leftPanel
            rightComponent = rightPanel
        }

        with(mainPanel) {
            layout = BorderLayout()
            add(headPanel, BorderLayout.NORTH)
            add(downPanel)
        }
    }

    override val title = "字符转义"

    override val icon = FlatSVGIcon("icons/escape_dark.svg")

    override val tip = "字符转义"
}
