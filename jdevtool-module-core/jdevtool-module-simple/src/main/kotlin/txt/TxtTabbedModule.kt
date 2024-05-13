package com.wxl.jdevtool.txt

import com.formdev.flatlaf.extras.FlatSVGIcon
import com.wxl.jdevtool.ComponentId
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
import java.awt.GridLayout
import javax.swing.*
import javax.swing.event.DocumentEvent
import javax.swing.text.JTextComponent

/**
 * Create by wuxingle on 2024/01/02
 * 文本处理器
 */
@Order(100)
@Component
@ComponentId("txtTabbedModule")
class TxtTabbedModule : TabbedModule {

    final override val mainPanel: JPanel

    final val headPanel: JPanel

    @ComponentId("originHeadPanel")
    final val originHeadPanel: JPanel

    @ComponentId("targetHeadPanel")
    final val targetHeadPanel: JPanel

    final val downPanel: JSplitPane

    final val originSplitText: JTextField

    final val originItemPreText: JTextField

    final val originItemPostText: JTextField

    final val originPreText: JTextField

    final val originPostText: JTextField

    @ComponentId("originClearBtn")
    final val originClearBtn: JButton

    final val targetSplitText: JTextField

    final val targetItemPreText: JTextField

    final val targetItemPostText: JTextField

    final val targetPreText: JTextField

    final val targetPostText: JTextField

    @ComponentId("targetClearBtn")
    final val targetClearBtn: JButton

    @ComponentId("executeBtn")
    final val executeBtn: JButton

    @ComponentId("leftTextArea")
    final val leftTextArea: RSyntaxTextArea

    final val leftSp: RTextScrollPane

    final val leftChecker: InputChecker

    @ComponentId("rightTextArea")
    final val rightTextArea: RSyntaxTextArea

    final val rightSp: RTextScrollPane

    init {
        mainPanel = JPanel()

        // 源字符串处理
        originHeadPanel = JPanel()
        originSplitText = HistoryTextField(StorageHistoryList("text.split.origin"))
        originSplitText.setHint("\\n")
        originItemPreText = HistoryTextField(StorageHistoryList("text.item.pre.origin"))
        originItemPostText = HistoryTextField(StorageHistoryList("text.item.post.origin"))
        originPreText = HistoryTextField(StorageHistoryList("text.pre.origin"))
        originPostText = HistoryTextField(StorageHistoryList("text.post.origin"))
        originClearBtn = JButton("清空")

        // 目标字符串处理
        targetHeadPanel = JPanel()
        targetSplitText = HistoryTextField(StorageHistoryList("text.split.target"))
        targetItemPreText = HistoryTextField(StorageHistoryList("text.item.pre.target"))
        targetItemPostText = HistoryTextField(StorageHistoryList("text.item.post.target"))
        targetPreText = HistoryTextField(StorageHistoryList("text.pre.target"))
        targetPostText = HistoryTextField(StorageHistoryList("text.post.target"))
        targetClearBtn = JButton("清空")
        executeBtn = JButton("生成")

        // 上方控制面板
        headPanel = JPanel()

        // 下方
        downPanel = JSplitPane(JSplitPane.HORIZONTAL_SPLIT)

        // 左边输入
        leftTextArea = RSyntaxTextArea()
        leftSp = RTextScrollPane(leftTextArea)
        leftChecker = object : InputChecker(leftTextArea, leftSp) {
            override fun doCheck(component: JTextComponent): Boolean {
                return !component.text.isNullOrBlank()
            }

            override fun documentUpdate(e: DocumentEvent) {
                showNormal()
            }
        }

        // 右边输出
        rightTextArea = RSyntaxTextArea()
        rightSp = RTextScrollPane(rightTextArea)

        // 布局初始化
        initUI()
    }

    /**
     * 布局初始化
     */
    private fun initUI() {
        // 上方输入布局
        with(originHeadPanel) {
            layout = FlowLayout(FlowLayout.LEFT)
            border = BorderFactory.createTitledBorder("输入设置")
            add(wrapperLabelInput("分隔符：", originSplitText))
            add(wrapperLabelInput("每项前缀：", originItemPreText))
            add(wrapperLabelInput("每项后缀：", originItemPostText))
            add(wrapperLabelInput("前缀：", originPreText))
            add(wrapperLabelInput("后缀：", originPostText))
            add(originClearBtn)
        }

        // 上方输出布局
        with(targetHeadPanel) {
            layout = FlowLayout(FlowLayout.LEFT)
            border = BorderFactory.createTitledBorder("输出设置")
            add(wrapperLabelInput("分隔符：", targetSplitText))
            add(wrapperLabelInput("每项前缀：", targetItemPreText))
            add(wrapperLabelInput("每项后缀：", targetItemPostText))
            add(wrapperLabelInput("前缀：", targetPreText))
            add(wrapperLabelInput("后缀：", targetPostText))
            add(targetClearBtn)
            add(executeBtn)
        }

        // 加入上方控制面板
        with(headPanel) {
            layout = GridLayout(1, 2)
            add(originHeadPanel)
            add(targetHeadPanel)
        }

        // 下方左边输入
        initTextArea(leftTextArea)
        initScrollPane(leftSp)

        // 下方右边输出
        initTextArea(rightTextArea)
        initScrollPane(rightSp)

        // 加入下方面板
        with(downPanel) {
            resizeWeight = 0.5
            leftComponent = leftSp
            rightComponent = rightSp
        }

        // 加入主面板
        with(mainPanel) {
            layout = BorderLayout()
            add(headPanel, BorderLayout.NORTH)
            add(downPanel)
        }
    }

    /**
     * 输入校验
     */
    fun check(): Boolean {
        return leftChecker.check(true)
    }

    private fun initTextArea(textArea: RSyntaxTextArea) {
        with(textArea) {
            syntaxEditingStyle = SyntaxConstants.SYNTAX_STYLE_NONE
            isCodeFoldingEnabled = true
            showCaretLocation()
        }
    }

    private fun initScrollPane(scrollPane: RTextScrollPane) {
        scrollPane.verticalScrollBarPolicy = ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED
        scrollPane.horizontalScrollBarPolicy = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED
    }

    private fun wrapperLabelInput(name: String, textField: JTextField): JPanel {
        val p = JPanel(FlowLayout(FlowLayout.LEFT))
        p.add(JLabel(name))
        p.add(textField)
        return p
    }

    /**
     * 模块标题
     */
    override val title = "文本处理"

    /**
     * 模块图标
     */
    override val icon = FlatSVGIcon("icons/txt_dark.svg")

    /**
     * 模块提示
     */
    override val tip = "文本处理"
}
