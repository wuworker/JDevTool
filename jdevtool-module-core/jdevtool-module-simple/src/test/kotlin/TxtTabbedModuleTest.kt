package com.wxl.jdevtool

import com.wxl.jdevtool.component.LabelTextPanel
import io.github.oshai.kotlinlogging.KotlinLogging
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea
import org.fife.ui.rsyntaxtextarea.SyntaxConstants
import org.fife.ui.rtextarea.RTextScrollPane
import org.springframework.stereotype.Component
import java.awt.*
import javax.swing.BorderFactory
import javax.swing.Icon
import javax.swing.JButton
import javax.swing.JPanel

/**
 * Create by wuxingle on 2024/01/02
 * 文本处理器
 */
@Component
class TxtTabbedModuleTest : TabbedModule {

    private val log = KotlinLogging.logger {}

    final override val mainPanel: JPanel

    final val headPanel: JPanel

    final val splitText: LabelTextPanel

    final val itemPreText: LabelTextPanel

    final val itemPostText: LabelTextPanel

    final val preText: LabelTextPanel

    final val postText: LabelTextPanel

    final val convertBtn: JButton

    final val leftPanel: JPanel

    final val leftTextArea: RSyntaxTextArea

    final val rightPanel: JPanel

    final val rightTextArea: RSyntaxTextArea

    init {
        val gridBagLayout = GridBagLayout()
        gridBagLayout.columnWeights = doubleArrayOf(0.5, 0.5)
        gridBagLayout.rowWeights = doubleArrayOf(0.0, 1.0)

        mainPanel = JPanel(gridBagLayout)
        mainPanel.border = BorderFactory.createLineBorder(Color.BLUE)

        // 上方控制面板
        headPanel = JPanel(FlowLayout(FlowLayout.LEFT))
        splitText = LabelTextPanel("分隔符:")
        itemPreText = LabelTextPanel("每项前缀:")
        itemPostText = LabelTextPanel("每项后缀:")
        preText = LabelTextPanel("前缀:")
        postText = LabelTextPanel("后缀:")
        convertBtn = JButton("生成")

        // 左边输入
        leftPanel = JPanel(BorderLayout())
        leftTextArea = RSyntaxTextArea()

        // 右边输出
        rightPanel = JPanel(BorderLayout())
        rightTextArea = RSyntaxTextArea()

        // 布局初始化
        initUI(gridBagLayout)

        mainPanel.add(headPanel)
        mainPanel.add(leftPanel)
        mainPanel.add(rightPanel)
    }

    private fun initUI(gridBagLayout: GridBagLayout) {
        initHead(gridBagLayout)
        initLeft(gridBagLayout)
        initRight(gridBagLayout)
    }

    private fun initHead(gridBagLayout: GridBagLayout) {
        val c = getGridBagConstraints()
        with(c) {
            fill = GridBagConstraints.HORIZONTAL
            gridwidth = 2
            anchor = GridBagConstraints.CENTER
        }
        headPanel.border = BorderFactory.createLineBorder(Color.RED)
        headPanel.preferredSize = Dimension(0, 100)

        headPanel.add(splitText)
        headPanel.add(itemPreText)
        headPanel.add(itemPostText)
        headPanel.add(preText)
        headPanel.add(postText)
        headPanel.add(convertBtn)

        gridBagLayout.setConstraints(headPanel, c)
    }

    private fun initLeft(gridBagLayout: GridBagLayout) {
        val c = getGridBagConstraints()
        with(c) {
            gridx = 0
            gridy = 1
            fill = GridBagConstraints.BOTH
            gridwidth = 1
        }

        leftPanel.border = BorderFactory.createLineBorder(Color.RED)

        leftTextArea.syntaxEditingStyle = SyntaxConstants.SYNTAX_STYLE_NONE
        leftTextArea.isCodeFoldingEnabled = true
        val sp = RTextScrollPane(leftTextArea)

        leftPanel.add(sp)
        gridBagLayout.setConstraints(leftPanel, c)
    }

    private fun initRight(gridBagLayout: GridBagLayout) {
        val c = getGridBagConstraints()
        with(c) {
            gridx = 1
            gridy = 1
            fill = GridBagConstraints.BOTH
            gridwidth = 1
        }

        rightPanel.border = BorderFactory.createLineBorder(Color.RED)

        rightTextArea.syntaxEditingStyle = SyntaxConstants.SYNTAX_STYLE_NONE
        rightTextArea.isCodeFoldingEnabled = true
        val sp = RTextScrollPane(rightTextArea)

        rightPanel.add(sp)
        gridBagLayout.setConstraints(rightPanel, c)
    }

    private fun getGridBagConstraints(): GridBagConstraints {
        return GridBagConstraints()
    }

    /**
     * 模块标题
     */
    override val title: String = "文本处理器"

    /**
     * 模块图标
     */
    override val icon: Icon? = null

    /**
     * 模块提示
     */
    override val tip: String? = "文本处理"
}
