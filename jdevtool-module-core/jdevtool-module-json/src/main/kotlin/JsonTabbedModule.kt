package com.wxl.jdevtool.json

import com.formdev.flatlaf.FlatClientProperties
import com.formdev.flatlaf.extras.FlatSVGIcon
import com.wxl.jdevtool.ComponentId
import com.wxl.jdevtool.Icons
import com.wxl.jdevtool.TabbedModule
import com.wxl.jdevtool.component.ComponentFactory
import com.wxl.jdevtool.component.history.HistoryTextField
import com.wxl.jdevtool.component.history.StorageHistoryList
import com.wxl.jdevtool.extension.setHint
import com.wxl.jdevtool.extension.showCaretLocation
import com.wxl.jdevtool.validate.InputChecker
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

    override val mainPanel = JPanel(BorderLayout())

    @ComponentId("compressBtn")
    val compressBtn = JButton("压缩")

    @ComponentId("expandBtn")
    val expandBtn = JButton("展开")

    @ComponentId("getValBtn")
    val getValBtn = JButton("求值")

    @ComponentId("jsonPathDescBtn")
    val jsonPathDescBtn = JButton(Icons.help)

    val jsonPathField = HistoryTextField(StorageHistoryList("json.path"))

    val jsonPathChecker = object : InputChecker(jsonPathField) {
        override fun doCheck(component: JTextComponent): Boolean {
            return !component.text.isNullOrBlank()
        }
    }

    @ComponentId("headPanel")
    val headPanel = JPanel(FlowLayout(FlowLayout.LEFT, 10, 10))

    val textPanel = JSplitPane(JSplitPane.HORIZONTAL_SPLIT)

    @ComponentId("leftTextArea")
    val leftTextArea = ComponentFactory.createTextArea {
        syntaxEditingStyle = SyntaxConstants.SYNTAX_STYLE_JSON
    }

    val leftSp = RTextScrollPane(leftTextArea)

    val leftInChecker = object : InputChecker(leftTextArea, leftSp) {
        override fun doCheck(component: JTextComponent): Boolean {
            return !component.text.isNullOrBlank()
        }
    }

    @ComponentId("rightTextArea")
    val rightTextArea = ComponentFactory.createTextArea()

    val rightSp = RTextScrollPane(rightTextArea)

    override fun afterPropertiesSet() {
        // 上方输入布局
        with(jsonPathDescBtn) {
            toolTipText = "JsonPath说明"
        }
        with(jsonPathField) {
            columns = 15
            setHint("输入JsonPath")
            putClientProperty(FlatClientProperties.TEXT_FIELD_TRAILING_COMPONENT, jsonPathDescBtn)
        }

        with(headPanel) {
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
