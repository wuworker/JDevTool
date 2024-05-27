package com.wxl.jdevtool.dubbo

import com.formdev.flatlaf.extras.FlatSVGIcon
import com.wxl.jdevtool.AppContexts
import com.wxl.jdevtool.ComponentId
import com.wxl.jdevtool.Icons
import com.wxl.jdevtool.TabbedModule
import com.wxl.jdevtool.component.ComponentFactory
import com.wxl.jdevtool.component.CopiedPanel
import com.wxl.jdevtool.component.history.HistoryTextField
import com.wxl.jdevtool.component.history.StorageHistoryList
import com.wxl.jdevtool.dubbo.component.ConfigPanel
import com.wxl.jdevtool.extension.showCaretLocation
import com.wxl.jdevtool.util.ComponentUtils
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea
import org.fife.ui.rsyntaxtextarea.SyntaxConstants
import org.fife.ui.rtextarea.RTextScrollPane
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.Insets
import javax.swing.*

/**
 * Create by wuxingle on 2024/03/12
 * dubbo模块
 */
@Order(1100)
@Component
@ComponentId("dubboTabbedModule")
class DubboTabbedModule : TabbedModule {

    final override val mainPanel: JPanel

    final val versionCombox: JComboBox<String>

    final val configPanel: JPanel

    final val scrollPane: JScrollPane

    // 应用配置
    final val appConfigPanel: AppConfigPanel

    // 注册中心配置
    final val registerConfigPanel: RegisterConfigPanel

    // 消费者配置
    final val referenceConfigPanel: ReferenceConfigPanel

    // attachment
    final val attachmentConfigPanel: AttachmentConfigPanel

    final val invokePanel: JSplitPane

    final val reqPanel: JPanel

    final val methodField: HistoryTextField

    @ComponentId("methodExeBtn")
    final val methodExeBtn: JButton

    final val paramTypeField: CopiedPanel<HistoryTextField>

    final val paramTextArea: CopiedPanel<RSyntaxTextArea>

    final val resultArea: RSyntaxTextArea

    final val resultAreaSp: RTextScrollPane

    init {
        mainPanel = JPanel(GridBagLayout())
        versionCombox = JComboBox(arrayOf("2.7.5"))

        configPanel = JPanel()
        scrollPane = JScrollPane(configPanel)

        appConfigPanel = AppConfigPanel()
        registerConfigPanel = RegisterConfigPanel()
        referenceConfigPanel = ReferenceConfigPanel()
        attachmentConfigPanel = AttachmentConfigPanel()

        invokePanel = JSplitPane(JSplitPane.HORIZONTAL_SPLIT)
        reqPanel = JPanel(GridBagLayout())
        methodField = HistoryTextField(StorageHistoryList("dubbo:invoke:method"))
        methodExeBtn = ComponentFactory.createIconBtn(Icons.execute, toolTip = "执行调用")

        val paramTypeHistory = StorageHistoryList("dubbo:invoke:param:type")
        paramTypeField = CopiedPanel({
            val tf = HistoryTextField(paramTypeHistory)
            tf.columns = 10
            tf
        })
        paramTextArea = CopiedPanel({
            val area = RSyntaxTextArea()
            with(area) {
                rows = 5
                syntaxEditingStyle = SyntaxConstants.SYNTAX_STYLE_JSON
                showCaretLocation()
            }
            AppContexts.theme.textAreaTheme.apply(area)
            return@CopiedPanel area
        }, DEFAULT_IDENT)

        resultArea = RSyntaxTextArea()
        resultAreaSp = RTextScrollPane(resultArea)

        initUI()
    }

    private fun initUI() {
        addRow(JLabel("dubbo版本："), versionCombox)

        addConfigPanel(appConfigPanel)
        addConfigPanel(registerConfigPanel)
        addConfigPanel(referenceConfigPanel)
        addConfigPanel(attachmentConfigPanel)

        addInvokePanel()
    }

    private fun addInvokePanel() {
        with(reqPanel) {
            border = BorderFactory.createTitledBorder("请求")
            add(
                JLabel("方法名："), ComponentUtils.createConstraints(
                    anchor = GridBagConstraints.WEST,
                    insets = Insets(DEFAULT_IDENT, DEFAULT_IDENT, DEFAULT_IDENT, DEFAULT_IDENT)
                )
            )
            add(
                methodField, ComponentUtils.createConstraints(
                    anchor = GridBagConstraints.WEST,
                    insets = Insets(DEFAULT_IDENT, 0, DEFAULT_IDENT, DEFAULT_IDENT),
                    weightx = 1.0,
                    fill = GridBagConstraints.HORIZONTAL
                )
            )
            add(
                methodExeBtn, ComponentUtils.createConstraints(
                    anchor = GridBagConstraints.WEST,
                    insets = Insets(DEFAULT_IDENT, 0, DEFAULT_IDENT, DEFAULT_IDENT),
                    gridwidth = GridBagConstraints.REMAINDER
                )
            )

            add(
                JLabel("参数类型："), ComponentUtils.createConstraints(
                    anchor = GridBagConstraints.NORTHWEST,
                    insets = Insets(DEFAULT_IDENT, DEFAULT_IDENT, DEFAULT_IDENT, DEFAULT_IDENT)
                )
            )
            add(
                paramTypeField, ComponentUtils.createConstraints(
                    anchor = GridBagConstraints.NORTHWEST,
                    insets = Insets(DEFAULT_IDENT, 0, DEFAULT_IDENT, DEFAULT_IDENT),
                    gridwidth = GridBagConstraints.REMAINDER,
                    weightx = 1.0,
                    fill = GridBagConstraints.HORIZONTAL
                )
            )
            add(
                JLabel("参数值："), ComponentUtils.createConstraints(
                    anchor = GridBagConstraints.NORTHWEST,
                    insets = Insets(DEFAULT_IDENT, DEFAULT_IDENT, DEFAULT_IDENT, DEFAULT_IDENT)
                )
            )
            add(
                paramTextArea, ComponentUtils.createConstraints(
                    insets = Insets(DEFAULT_IDENT, 0, DEFAULT_IDENT, DEFAULT_IDENT),
                    gridwidth = GridBagConstraints.REMAINDER,
                    weightx = 1.0,
                    weighty = 1.0,
                    fill = GridBagConstraints.BOTH
                )
            )
        }
        val sp = JScrollPane(reqPanel)

        // 响应
        with(resultArea) {
            syntaxEditingStyle = SyntaxConstants.SYNTAX_STYLE_JSON
            isCodeFoldingEnabled = true
            showCaretLocation()
        }

        with(resultAreaSp) {
            border = BorderFactory.createTitledBorder("响应")
            verticalScrollBarPolicy = ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED
            horizontalScrollBarPolicy = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED
        }

        invokePanel.leftComponent = sp
        invokePanel.rightComponent = resultAreaSp
        invokePanel.resizeWeight = 0.5

        mainPanel.add(
            invokePanel, ComponentUtils.createConstraints(
                gridwidth = GridBagConstraints.REMAINDER,
                weightx = 1.0,
                weighty = 1.0,
                fill = GridBagConstraints.BOTH
            )
        )
    }

    /**
     * 添加一行
     */
    private fun addRow(vararg components: JComponent) {
        for (i in components.indices) {
            val c = ComponentUtils.createConstraints(
                anchor = GridBagConstraints.WEST,
            )
            // 第一个
            c.insets = if (i == 0) {
                Insets(DEFAULT_IDENT, DEFAULT_IDENT, DEFAULT_IDENT, DEFAULT_IDENT)
            } else {
                Insets(DEFAULT_IDENT, 0, DEFAULT_IDENT, DEFAULT_IDENT)
            }
            // 最后一个
            if (i == components.size - 1) {
                c.gridwidth = GridBagConstraints.REMAINDER
            }
            mainPanel.add(components[i], c)
        }
    }

    private fun <T> addConfigPanel(configPanel: ConfigPanel<T>) {
        mainPanel.add(
            configPanel, ComponentUtils.createConstraints(
                gridwidth = GridBagConstraints.REMAINDER,
                gridheight = 50,
                weightx = 1.0,
                fill = GridBagConstraints.HORIZONTAL,
                insets = Insets(DEFAULT_IDENT, DEFAULT_IDENT, 0, DEFAULT_IDENT)
            )
        )
    }

    override val title = "Dubbo调用"

    override val icon = FlatSVGIcon("icons/dubbo_dark.svg")

    override val tip = "Dubbo调用"


    companion object {
        const val DEFAULT_IDENT = 5

        init {
            System.setProperty("dubbo.application.logger", "slf4j")
        }
    }
}