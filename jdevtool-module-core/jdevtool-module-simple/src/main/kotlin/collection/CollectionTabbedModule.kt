package  com.wxl.jdevtool.collection

import com.formdev.flatlaf.extras.FlatSVGIcon
import com.wxl.jdevtool.ComponentId
import com.wxl.jdevtool.TabbedModule
import com.wxl.jdevtool.component.ComponentFactory
import com.wxl.jdevtool.component.history.HistoryTextField
import com.wxl.jdevtool.component.history.StorageHistoryList
import com.wxl.jdevtool.extension.setHint
import com.wxl.jdevtool.validate.InputChecker
import com.wxl.jdevtool.validate.InputValidateGroup
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

    override val mainPanel = JPanel()

    @ComponentId("headPanel")
    val headPanel = JPanel()

    val splitTextField = HistoryTextField(StorageHistoryList("collection.split"))

    @ComponentId("deduplicateRadio")
    val deduplicateRadio = JRadioButton("去重")

    @ComponentId("repeatRadio")
    val repeatRadio = JRadioButton("重复元素")

    @ComponentId("unionRadio")
    val unionRadio = JRadioButton("并集")

    @ComponentId("intersectionRadio")
    val intersectionRadio = JRadioButton("交集")

    @ComponentId("diff1Radio")
    val diff1Radio = JRadioButton("差集(1-2)")

    @ComponentId("diff2Radio")
    val diff2Radio = JRadioButton("差集(2-1)")

    val radioGroup = ButtonGroup()

    @ComponentId("executeBtn")
    val executeBtn = JButton("执行")

    val leftPanel = JSplitPane(JSplitPane.VERTICAL_SPLIT)

    val leftInputPanel1 = JPanel(BorderLayout())

    @ComponentId("leftTextArea1")
    val leftTextArea1 = ComponentFactory.createTextArea()

    val leftTextAreaSp1 = RTextScrollPane(leftTextArea1)

    val leftChecker1 = object : InputChecker(leftTextArea1, leftTextAreaSp1) {
        override fun doCheck(component: JTextComponent): Boolean {
            return !component.text.isNullOrBlank()
        }

        override fun documentUpdate(e: DocumentEvent) {
            showNormal()
        }
    }

    val leftInputPanel2 = JPanel(BorderLayout())

    @ComponentId("leftTextArea2")
    val leftTextArea2 = ComponentFactory.createTextArea()

    val leftTextAreaSp2 = RTextScrollPane(leftTextArea2)

    val leftChecker2 = object : InputChecker(leftTextArea2, leftTextAreaSp2) {
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

        splitTextField.setHint("\\n")
        with(headPanel) {
            layout = FlowLayout(FlowLayout.LEFT, 10, 10)
            border = BorderFactory.createTitledBorder("执行选项")

            add(JLabel("分隔符:"))
            add(splitTextField)
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
            resizeWeight = 0.5
            leftComponent = leftInputPanel1
        }
        // 默认选中第一个选项
        deduplicateRadio.isSelected = true
        leftInputPanel1.border = BorderFactory.createTitledBorder("集合")

        // 下方输出
        val rightPanel = JPanel(BorderLayout())
        with(rightPanel) {
            border = BorderFactory.createTitledBorder("输出")

            val sp = RTextScrollPane(rightTextArea)
            add(sp)
        }

        val downPanel = JSplitPane(JSplitPane.HORIZONTAL_SPLIT)
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
        val validate = InputValidateGroup(leftChecker1)
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
