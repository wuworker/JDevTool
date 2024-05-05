package com.wxl.jdevtool.dubbo.component

import com.formdev.flatlaf.FlatClientProperties
import com.google.gson.GsonBuilder
import com.wxl.jdevtool.component.ShrinkPanel
import com.wxl.jdevtool.extension.showCaretLocation
import com.wxl.jdevtool.util.ComponentUtils
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea
import org.fife.ui.rsyntaxtextarea.SyntaxConstants
import org.fife.ui.rtextarea.RTextScrollPane
import java.awt.BorderLayout
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.io.Serial
import java.lang.reflect.Type
import javax.swing.*

/**
 * Create by wuxingle on 2024/03/14
 * 配置panel
 */
abstract class ConfigPanel<T>(
    configTitle: String,
) : JPanel(BorderLayout()) {

    val tabbedPane: JTabbedPane

    val kvPanel: JPanel

    val jsonPanel: JPanel

    val textArea: RSyntaxTextArea

    val textAreaSp: RTextScrollPane

    abstract val config: T

    abstract val configType: Type

    init {
        // kv配置
        kvPanel = JPanel(GridBagLayout())

        // json配置
        textArea = RSyntaxTextArea()
        textAreaSp = RTextScrollPane(textArea)
        with(textArea) {
            syntaxEditingStyle = SyntaxConstants.SYNTAX_STYLE_JSON
            lineWrap = true
            rows = 3
            showCaretLocation()
        }
        with(textAreaSp) {
            verticalScrollBarPolicy = ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS
            horizontalScrollBarPolicy = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER
        }

        jsonPanel = JPanel()
        with(jsonPanel) {
            layout = BorderLayout()
            add(textAreaSp)
        }

        // tab
        tabbedPane = JTabbedPane(JTabbedPane.TOP, JTabbedPane.WRAP_TAB_LAYOUT)
        with(tabbedPane) {
            addTab("配置", null, kvPanel, "界面配置")
            addTab("JSON", null, jsonPanel, "Json格式配置")
            putClientProperty(
                FlatClientProperties.TABBED_PANE_TAB_TYPE,
                FlatClientProperties.TABBED_PANE_TAB_TYPE_CARD
            )
        }
        tabbedPane.addChangeListener {
            val tabbed = it.source as JTabbedPane
            val selectIdx = tabbed.selectedIndex
            if (selectIdx == 0) {
                val jsonConfig = try {
                    gson.fromJson<T>(textArea.text, configType)
                } catch (e: Exception) {
                    null
                }
                if (jsonConfig != null) {
                    merge(config, jsonConfig)
                }
                showKVView(config)
            } else {
                showJsonView(config)
                textArea.text = gson.toJson(config)
            }
        }

        add(ShrinkPanel(configTitle, tabbedPane))
    }

    /**
     * 合并配置
     * sub合并到main里
     */
    abstract fun merge(mainConfig: T, subConfig: T)

    /**
     * 展示kv界面
     * 展示config内容
     */
    abstract fun showKVView(config: T)

    /**
     * 展示json界面
     * 填充config
     */
    abstract fun showJsonView(config: T)

    /**
     * 检查并获取配置
     */
    abstract fun checkAndGetConfig(): T

    /**
     * 新增kv组件
     */
    protected fun addKV(title: String, component: JComponent) {
        kvPanel.add(
            JLabel(title), ComponentUtils.createConstraints(
                anchor = GridBagConstraints.WEST
            )
        )
        kvPanel.add(
            component, ComponentUtils.createConstraints(
                anchor = GridBagConstraints.WEST,
                fill = GridBagConstraints.HORIZONTAL,
                weightx = 0.6
            )
        )
        kvPanel.add(
            JPanel(), ComponentUtils.createConstraints(
                weightx = 0.4,
                gridwidth = GridBagConstraints.REMAINDER
            )
        )
    }

    companion object {

        @Serial
        private const val serialVersionUID: Long = -7551996115823580859L

        private val gson = GsonBuilder().setPrettyPrinting().create()
    }
}
