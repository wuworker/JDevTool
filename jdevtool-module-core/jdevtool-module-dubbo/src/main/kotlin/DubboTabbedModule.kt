package com.wxl.jdevtool.dubbo

import com.formdev.flatlaf.extras.FlatSVGIcon
import com.wxl.jdevtool.ComponentId
import com.wxl.jdevtool.Icons
import com.wxl.jdevtool.TabbedModule
import com.wxl.jdevtool.component.ComponentFactory
import com.wxl.jdevtool.component.CopiedPanel
import com.wxl.jdevtool.component.history.HistoryTextField
import com.wxl.jdevtool.component.history.StorageHistoryList
import com.wxl.jdevtool.dubbo.component.ConfigPanel
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

    override val mainPanel = JPanel(GridBagLayout())

    val versionCombox = JComboBox(arrayOf("2.7.5"))

    // 应用配置
    val appConfigPanel = AppConfigPanel()

    // 注册中心配置
    val registerConfigPanel = RegisterConfigPanel()

    // 消费者配置
    val referenceConfigPanel = ReferenceConfigPanel()

    // attachment
    val attachmentConfigPanel = AttachmentConfigPanel()

    val invokePanel = JSplitPane(JSplitPane.HORIZONTAL_SPLIT)

    val reqPanel = JPanel(GridBagLayout())

    val methodField = HistoryTextField(StorageHistoryList("dubbo:invoke:method"))

    @ComponentId("methodExeBtn")
    val methodExeBtn = ComponentFactory.createIconBtn(Icons.execute, toolTip = "执行调用")

    val paramTypeField: CopiedPanel<HistoryTextField>

    val paramTextArea: CopiedPanel<RSyntaxTextArea>

    val resultArea = ComponentFactory.createTextArea {
        syntaxEditingStyle = SyntaxConstants.SYNTAX_STYLE_JSON
    }

    init {
        val paramTypeHistory = StorageHistoryList("dubbo:invoke:param:type")
        paramTypeField = CopiedPanel({
            val tf = HistoryTextField(paramTypeHistory)
            tf.columns = 10
            tf
        })
        paramTextArea = CopiedPanel({
            val area = ComponentFactory.createTextArea {
                rows = 5
                syntaxEditingStyle = SyntaxConstants.SYNTAX_STYLE_JSON
            }
            return@CopiedPanel area
        }, DEFAULT_IDENT)
    }

    override fun afterPropertiesSet() {
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

        val resultAreaSp = RTextScrollPane(resultArea)
        with(resultAreaSp) {
            border = BorderFactory.createTitledBorder("响应")
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
        configPanel.initUI()
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