package com.wxl.jdevtool.txt

import com.formdev.flatlaf.extras.FlatSVGIcon
import com.wxl.jdevtool.ComponentId
import com.wxl.jdevtool.TabbedModule
import com.wxl.jdevtool.component.ComponentFactory
import com.wxl.jdevtool.component.history.HistoryTextField
import com.wxl.jdevtool.component.history.StorageHistoryList
import com.wxl.jdevtool.extension.setHint
import com.wxl.jdevtool.validate.InputChecker
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

    override val mainPanel = JPanel(BorderLayout())

    @ComponentId("originHeadPanel")
    val originHeadPanel = JPanel(FlowLayout(FlowLayout.LEFT))

    @ComponentId("targetHeadPanel")
    val targetHeadPanel = JPanel(FlowLayout(FlowLayout.LEFT))

    val originSplitText = HistoryTextField(StorageHistoryList("text.split.origin"))

    val originItemPreText = HistoryTextField(StorageHistoryList("text.item.pre.origin"))

    val originItemPostText = HistoryTextField(StorageHistoryList("text.item.post.origin"))

    val originPreText = HistoryTextField(StorageHistoryList("text.pre.origin"))

    val originPostText = HistoryTextField(StorageHistoryList("text.post.origin"))

    @ComponentId("originClearBtn")
    val originClearBtn = JButton("清空")

    val targetSplitText = HistoryTextField(StorageHistoryList("text.split.target"))

    val targetItemPreText = HistoryTextField(StorageHistoryList("text.item.pre.target"))

    val targetItemPostText = HistoryTextField(StorageHistoryList("text.item.post.target"))

    val targetPreText = HistoryTextField(StorageHistoryList("text.pre.target"))

    val targetPostText = HistoryTextField(StorageHistoryList("text.post.target"))

    @ComponentId("targetClearBtn")
    val targetClearBtn = JButton("清空")

    @ComponentId("executeBtn")
    val executeBtn = JButton("生成")

    @ComponentId("leftTextArea")
    val leftTextArea = ComponentFactory.createTextArea()

    val leftSp = RTextScrollPane(leftTextArea)

    val leftChecker = object : InputChecker(leftTextArea, leftSp) {
        override fun doCheck(component: JTextComponent): Boolean {
            return !component.text.isNullOrBlank()
        }

        override fun documentUpdate(e: DocumentEvent) {
            showNormal()
        }
    }

    @ComponentId("rightTextArea")
    val rightTextArea = ComponentFactory.createTextArea()

    override fun afterPropertiesSet() {
        // 源字符串处理
        originSplitText.setHint("\\n")

        // 上方输入布局
        with(originHeadPanel) {
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
        val headPanel = JPanel()
        with(headPanel) {
            layout = GridLayout(1, 2)
            add(originHeadPanel)
            add(targetHeadPanel)
        }

        // 加入下方面板
        val downPanel = JSplitPane(JSplitPane.HORIZONTAL_SPLIT)
        val rightSp = RTextScrollPane(rightTextArea)
        with(downPanel) {
            resizeWeight = 0.5
            leftComponent = leftSp
            rightComponent = rightSp
        }

        // 加入主面板
        with(mainPanel) {
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
