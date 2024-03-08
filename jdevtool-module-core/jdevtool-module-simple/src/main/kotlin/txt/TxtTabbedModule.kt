package com.wxl.jdevtool.txt

import com.formdev.flatlaf.extras.FlatSVGIcon
import com.wxl.jdevtool.ComponentId
import com.wxl.jdevtool.TabbedModule
import com.wxl.jdevtool.component.LabelTextPanel
import com.wxl.jdevtool.validate.InputChecker
import com.wxl.jdevtool.validate.InputValidateGroup
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

    final val originSplitText: LabelTextPanel

    final val originItemPreText: LabelTextPanel

    final val originItemPostText: LabelTextPanel

    final val originPreText: LabelTextPanel

    final val originPostText: LabelTextPanel

    @ComponentId("originClearBtn")
    final val originClearBtn: JButton

    final val targetSplitText: LabelTextPanel

    final val targetItemPreText: LabelTextPanel

    final val targetItemPostText: LabelTextPanel

    final val targetPreText: LabelTextPanel

    final val targetPostText: LabelTextPanel

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

    init {
        mainPanel = JPanel()

        // 源字符串处理
        originHeadPanel = JPanel()
        originSplitText = LabelTextPanel("分隔符:")
        originItemPreText = LabelTextPanel("每项前缀:")
        originItemPostText = LabelTextPanel("每项后缀:")
        originPreText = LabelTextPanel("前缀:")
        originPostText = LabelTextPanel("后缀:")
        originClearBtn = JButton("清空")

        // 目标字符串处理
        targetHeadPanel = JPanel()
        targetSplitText = LabelTextPanel("分隔符:")
        targetItemPreText = LabelTextPanel("每项前缀:")
        targetItemPostText = LabelTextPanel("每项后缀:")
        targetPreText = LabelTextPanel("前缀:")
        targetPostText = LabelTextPanel("后缀:")
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
            add(originSplitText)
            add(originSplitText)
            add(originItemPreText)
            add(originItemPostText)
            add(originPreText)
            add(originPostText)
            add(originClearBtn)
        }

        // 上方输出布局
        with(targetHeadPanel) {
            layout = FlowLayout(FlowLayout.LEFT)
            border = BorderFactory.createTitledBorder("输出设置")
            add(targetSplitText)
            add(targetSplitText)
            add(targetItemPreText)
            add(targetItemPostText)
            add(targetPreText)
            add(targetPostText)
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
        with(leftTextArea) {
            syntaxEditingStyle = SyntaxConstants.SYNTAX_STYLE_NONE
            isCodeFoldingEnabled = true
        }
        leftSp.verticalScrollBarPolicy = ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED
        leftSp.horizontalScrollBarPolicy = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED

        // 下方右边输出
        with(rightTextArea) {
            syntaxEditingStyle = SyntaxConstants.SYNTAX_STYLE_NONE
            isCodeFoldingEnabled = true
        }
        val rightSp = RTextScrollPane(rightTextArea)
        rightSp.verticalScrollBarPolicy = ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED
        rightSp.horizontalScrollBarPolicy = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED

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
        return InputValidateGroup(originSplitText, leftChecker).check(true)
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
