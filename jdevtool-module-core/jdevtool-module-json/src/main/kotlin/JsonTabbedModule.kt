package com.wxl.jdevtool.json

import com.formdev.flatlaf.FlatClientProperties
import com.formdev.flatlaf.extras.FlatSVGIcon
import com.wxl.jdevtool.ComponentId
import com.wxl.jdevtool.Icons
import com.wxl.jdevtool.TabbedModule
import com.wxl.jdevtool.component.history.HistoryTextField
import com.wxl.jdevtool.component.history.StorageHistoryList
import com.wxl.jdevtool.extension.setHint
import com.wxl.jdevtool.extension.showCaretLocation
import com.wxl.jdevtool.validate.InputChecker
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea
import org.fife.ui.rsyntaxtextarea.SyntaxConstants
import org.fife.ui.rtextarea.RTextScrollPane
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import java.awt.BorderLayout
import java.awt.FlowLayout
import javax.swing.*
import javax.swing.text.JTextComponent

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

    final val jsonPathChecker: InputChecker

    @ComponentId("headPanel")
    final val headPanel: JPanel

    final val textPanel: JSplitPane

    @ComponentId("leftTextArea")
    final val leftTextArea: RSyntaxTextArea

    final val leftSp: RTextScrollPane

    final val leftInChecker: InputChecker

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
        jsonPathField = HistoryTextField(StorageHistoryList("json.path"))
        jsonPathField.columns = 15
        jsonPathChecker = object : InputChecker(jsonPathField) {
            override fun doCheck(component: JTextComponent): Boolean {
                return !component.text.isNullOrBlank()
            }
        }

        jsonPathDescBtn = JButton(Icons.help)

        // 下方文本域
        textPanel = JSplitPane(JSplitPane.HORIZONTAL_SPLIT)
        leftTextArea = RSyntaxTextArea()
        leftTextArea.syntaxEditingStyle = SyntaxConstants.SYNTAX_STYLE_JSON
        leftSp = RTextScrollPane(leftTextArea)
        leftInChecker = object : InputChecker(leftTextArea, leftSp) {
            override fun doCheck(component: JTextComponent): Boolean {
                return !component.text.isNullOrBlank()
            }
        }
        rightTextArea = RSyntaxTextArea()
        rightSp = RTextScrollPane(rightTextArea)

        initUI()
    }

    private fun initUI() {
        // 上方输入布局
        with(jsonPathDescBtn) {
            toolTipText = "JsonPath说明"
        }
        with(jsonPathField) {
            setHint("输入JsonPath")
            putClientProperty(FlatClientProperties.TEXT_FIELD_TRAILING_COMPONENT, jsonPathDescBtn)
        }

        with(headPanel) {
            layout = FlowLayout(FlowLayout.LEFT, 10, 10)
            border = BorderFactory.createTitledBorder("操作")
            add(JLabel("JsonPath:"))
            add(jsonPathField)
            add(getValBtn)
            add(expandBtn)
            add(compressBtn)
        }

        // 下方文本域
        with(leftTextArea) {
            isCodeFoldingEnabled = true
            showCaretLocation()
        }
        with(leftSp) {
            verticalScrollBarPolicy = ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED
            horizontalScrollBarPolicy = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED
        }
        with(rightTextArea) {
            syntaxEditingStyle = SyntaxConstants.SYNTAX_STYLE_JSON
            isCodeFoldingEnabled = true
            showCaretLocation()
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
