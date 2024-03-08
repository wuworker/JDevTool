package  com.wxl.jdevtool.collection

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
import javax.swing.*
import javax.swing.event.DocumentEvent
import javax.swing.text.JTextComponent

/**
 * Create by wuxingle on 2024/01/08
 * 集合处理
 */
@Order(200)
@Component
@ComponentId("collectionTabbedModule")
class CollectionTabbedModule : TabbedModule {

    final override val mainPanel: JPanel

    @ComponentId("headPanel")
    final val headPanel: JPanel

    final val splitLabel: LabelTextPanel

    @ComponentId("deduplicateRadio")
    final val deduplicateRadio: JRadioButton

    @ComponentId("repeatRadio")
    final val repeatRadio: JRadioButton

    @ComponentId("unionRadio")
    final val unionRadio: JRadioButton

    @ComponentId("intersectionRadio")
    final val intersectionRadio: JRadioButton

    @ComponentId("diff1Radio")
    final val diff1Radio: JRadioButton

    @ComponentId("diff2Radio")
    final val diff2Radio: JRadioButton

    final val radioGroup: ButtonGroup

    @ComponentId("executeBtn")
    final val executeBtn: JButton

    final val downPanel: JSplitPane

    final val leftPanel: JSplitPane

    final val leftInputPanel1: JPanel

    @ComponentId("leftTextArea1")
    final val leftTextArea1: RSyntaxTextArea

    final val leftTextAreaSp1: RTextScrollPane

    final val leftChecker1: InputChecker

    final val leftInputPanel2: JPanel

    @ComponentId("leftTextArea2")
    final val leftTextArea2: RSyntaxTextArea

    final val leftTextAreaSp2: RTextScrollPane

    final val leftChecker2: InputChecker

    final val rightPanel: JPanel

    @ComponentId("rightTextArea")
    final val rightTextArea: RSyntaxTextArea

    init {
        mainPanel = JPanel()

        // 上方控制布局
        headPanel = JPanel()
        splitLabel = LabelTextPanel("分隔符")
        deduplicateRadio = JRadioButton("去重")
        repeatRadio = JRadioButton("重复元素")
        unionRadio = JRadioButton("并集")
        intersectionRadio = JRadioButton("交集")
        diff1Radio = JRadioButton("差集(1-2)")
        diff2Radio = JRadioButton("差集(2-1)")
        executeBtn = JButton("执行")
        radioGroup = ButtonGroup()

        // 下方布局
        downPanel = JSplitPane(JSplitPane.HORIZONTAL_SPLIT)

        // 输入
        leftPanel = JSplitPane(JSplitPane.VERTICAL_SPLIT)
        leftInputPanel1 = JPanel(BorderLayout())
        leftTextArea1 = RSyntaxTextArea()
        leftTextAreaSp1 = RTextScrollPane(leftTextArea1)
        leftChecker1 = object : InputChecker(leftTextArea1, leftTextAreaSp1) {
            override fun doCheck(component: JTextComponent): Boolean {
                return !component.text.isNullOrBlank()
            }

            override fun documentUpdate(e: DocumentEvent) {
                showNormal()
            }
        }

        leftInputPanel2 = JPanel(BorderLayout())
        leftTextArea2 = RSyntaxTextArea()
        leftTextAreaSp2 = RTextScrollPane(leftTextArea2)
        leftChecker2 = object : InputChecker(leftTextArea2, leftTextAreaSp2) {
            override fun doCheck(component: JTextComponent): Boolean {
                return !component.text.isNullOrBlank()
            }

            override fun documentUpdate(e: DocumentEvent) {
                showNormal()
            }
        }

        // 输出
        rightPanel = JPanel(BorderLayout())
        rightTextArea = RSyntaxTextArea()

        // 布局初始化
        initUI()
    }

    /**
     * 布局初始化
     */
    private fun initUI() {
        // 上方控制布局
        deduplicateRadio.actionCommand = CollectionOps.DEDUPLICATE.name
        repeatRadio.actionCommand = CollectionOps.REPEAT.name
        unionRadio.actionCommand = CollectionOps.UNION.name
        intersectionRadio.actionCommand = CollectionOps.INTERSECTION.name
        diff1Radio.actionCommand = CollectionOps.DIFF12.name
        diff2Radio.actionCommand = CollectionOps.DIFF21.name
        with(radioGroup) {
            add(deduplicateRadio)
            add(repeatRadio)
            add(unionRadio)
            add(intersectionRadio)
            add(diff1Radio)
            add(diff2Radio)
        }

        splitLabel.setNewLine()
        with(headPanel) {
            layout = FlowLayout(FlowLayout.LEFT, 10, 10)
            border = BorderFactory.createTitledBorder("执行选项")

            add(splitLabel)
            add(deduplicateRadio)
            add(repeatRadio)
            add(unionRadio)
            add(intersectionRadio)
            add(diff1Radio)
            add(diff2Radio)
            add(executeBtn)
        }

        // 下方输入
        leftInputPanel1.add(leftTextAreaSp1)
        leftInputPanel2.add(leftTextAreaSp2)
        with(leftPanel) {
            leftTextArea1.syntaxEditingStyle = SyntaxConstants.SYNTAX_STYLE_NONE
            leftTextArea1.isCodeFoldingEnabled = true
            leftTextArea2.syntaxEditingStyle = SyntaxConstants.SYNTAX_STYLE_NONE
            leftTextArea2.isCodeFoldingEnabled = true
            leftTextAreaSp1.verticalScrollBarPolicy = ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED
            leftTextAreaSp1.horizontalScrollBarPolicy = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED
            leftTextAreaSp2.verticalScrollBarPolicy = ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED
            leftTextAreaSp2.horizontalScrollBarPolicy = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED

            resizeWeight = 0.5
            leftComponent = leftInputPanel1
        }
        // 默认选中第一个选项
        deduplicateRadio.isSelected = true
        leftInputPanel1.border = BorderFactory.createTitledBorder("集合")

        // 下方输出
        with(rightPanel) {
            border = BorderFactory.createTitledBorder("输出")

            rightTextArea.syntaxEditingStyle = SyntaxConstants.SYNTAX_STYLE_NONE
            rightTextArea.isCodeFoldingEnabled = true
            val sp = RTextScrollPane(rightTextArea)
            sp.verticalScrollBarPolicy = ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED
            sp.horizontalScrollBarPolicy = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED
            add(sp)
        }

        with(downPanel) {
            resizeWeight = 0.6
            leftComponent = leftPanel
            rightComponent = rightPanel
        }

        with(mainPanel) {
            layout = BorderLayout()
            add(headPanel, BorderLayout.NORTH)
            add(downPanel)
        }
    }

    fun getSelectedOp(): CollectionOps {
        return CollectionOps.valueOf(radioGroup.selection.actionCommand)
    }

    fun check(): Boolean {
        val validate = InputValidateGroup(splitLabel, leftChecker1)
        val selectedOp = getSelectedOp()
        if (CollectionOps.DEDUPLICATE != selectedOp && CollectionOps.REPEAT != selectedOp) {
            validate.add(leftChecker2)
        }
        return validate.check(true)
    }

    override val title = "集合处理"

    override val icon = FlatSVGIcon("icons/collection_dark.svg")

    override val tip = "集合处理"
}
