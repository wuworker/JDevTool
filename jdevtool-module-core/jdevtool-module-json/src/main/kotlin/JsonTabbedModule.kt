package com.wxl.jdevtool.json

import com.formdev.flatlaf.extras.FlatSVGIcon
import com.wxl.jdevtool.ComponentId
import com.wxl.jdevtool.Icons
import com.wxl.jdevtool.TabbedModule
import com.wxl.jdevtool.extension.setHint
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea
import org.fife.ui.rsyntaxtextarea.SyntaxConstants
import org.fife.ui.rtextarea.RTextScrollPane
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import java.awt.BorderLayout
import java.awt.FlowLayout
import javax.swing.*

/**
 * Create by wuxingle on 2024/1/11
 * json处理
 */
@Order(1000)
@Component
@ComponentId("jsonTabbedModule")
class JsonTabbedModule : TabbedModule {

    final override val mainPanel: JPanel

    @ComponentId("compressBtn")
    final val compressBtn: JButton

    @ComponentId("expandBtn")
    final val expandBtn: JButton

    @ComponentId("getValBtn")
    final val getValBtn: JButton

    @ComponentId("jsonPathDescBtn")
    final val jsonPathDescBtn: JButton

    final val jsonPathField: JTextField

    @ComponentId("headPanel")
    final val headPanel: JPanel

    final val textPanel: JSplitPane

    @ComponentId("leftTextArea")
    final val leftTextArea: RSyntaxTextArea

    final val leftSp: RTextScrollPane

    @ComponentId("rightTextArea")
    final val rightTextArea: RSyntaxTextArea

    final val rightSp: RTextScrollPane

    init {
        mainPanel = JPanel()

        // 上方控制面板
        headPanel = JPanel()
        expandBtn = JButton("展开")
        compressBtn = JButton("压缩")
        getValBtn = JButton("求值")
        jsonPathField = JTextField(15)
        jsonPathDescBtn = JButton(Icons.help)

        // 下方文本域
        textPanel = JSplitPane(JSplitPane.HORIZONTAL_SPLIT)
        leftTextArea = RSyntaxTextArea()
        leftSp = RTextScrollPane(leftTextArea)
        rightTextArea = RSyntaxTextArea()
        rightSp = RTextScrollPane(rightTextArea)

        initUI()
    }

    private fun initUI() {
        // 上方输入布局
        with(jsonPathDescBtn) {
            background = jsonPathField.background
            foreground = jsonPathField.foreground
            border = BorderFactory.createEmptyBorder()
            toolTipText = "JsonPath说明"
        }
        val box = Box.createHorizontalBox()
        with(box) {
            border = jsonPathField.border
            add(jsonPathField)
            add(jsonPathDescBtn)
        }
        with(jsonPathField) {
            setHint("输入JsonPath")
            border = BorderFactory.createEmptyBorder()
        }

        with(headPanel) {
            layout = FlowLayout(FlowLayout.LEFT, 10, 10)
            border = BorderFactory.createTitledBorder("操作")
            add(JLabel("JsonPath:"))
            add(box)
            add(getValBtn)
            add(expandBtn)
            add(compressBtn)
        }

        // 下方文本域
        with(leftTextArea) {
            syntaxEditingStyle = SyntaxConstants.SYNTAX_STYLE_JSON
            isCodeFoldingEnabled = true
        }
        with(leftSp) {
            verticalScrollBarPolicy = ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED
            horizontalScrollBarPolicy = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED
        }
        with(rightTextArea) {
            syntaxEditingStyle = SyntaxConstants.SYNTAX_STYLE_JSON
            isCodeFoldingEnabled = true
        }
        with(rightSp) {
            verticalScrollBarPolicy = ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED
            horizontalScrollBarPolicy = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED
        }

        with(textPanel) {
            resizeWeight = 1.0
            leftComponent = leftSp
            rightComponent = null
        }

        with(mainPanel) {
            layout = BorderLayout()
            add(headPanel, BorderLayout.NORTH)
            add(textPanel)
        }
    }

    fun showRight() {
        textPanel.rightComponent = rightSp
        textPanel.resizeWeight = 0.5
    }

    fun hiddenRight() {
        textPanel.rightComponent = null
        textPanel.resizeWeight = 1.0
    }

    override val title = "JSON处理"

    override val icon = FlatSVGIcon("icons/json_dark.svg")

    override val tip = "JSON处理"
}
