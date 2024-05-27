package com.wxl.jdevtool.dubbo.component

import com.formdev.flatlaf.FlatClientProperties
import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.wxl.jdevtool.component.ShrinkPanel
import com.wxl.jdevtool.extension.showCaretLocation
import com.wxl.jdevtool.util.ComponentUtils
import org.apache.dubbo.config.bootstrap.DubboBootstrap
import org.apache.dubbo.rpc.model.ServiceMetadata
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea
import org.fife.ui.rsyntaxtextarea.SyntaxConstants
import org.fife.ui.rtextarea.RTextScrollPane
import java.awt.BorderLayout
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.io.Serial
import java.lang.reflect.Type
import java.util.concurrent.atomic.AtomicBoolean
import javax.swing.*

/**
 * Create by wuxingle on 2024/03/14
 * 配置panel
 */
abstract class ConfigPanel<T>(
    configTitle: String,
    val configClass: Type
) : JPanel(BorderLayout()) {

    val tabbedPane: JTabbedPane

    val kvPanel: JPanel

    val jsonPanel: JPanel

    val textArea: RSyntaxTextArea

    val textAreaSp: RTextScrollPane

    private val configMap = linkedMapOf<String, Any?>()

    init {
        // kv配置
        kvPanel = JPanel(GridBagLayout())
        kvPanel.border = BorderFactory.createEmptyBorder(5, 0, 0, 0)

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
                updateConfigFromJson()
                showKVView(configMap)
            } else {
                updateConfigFromKV()
                textArea.text = gson.toJson(configMap)
            }
        }

        add(ShrinkPanel(configTitle, tabbedPane))

        initConfig(configMap)
    }

    private fun updateConfigFromKV() {
        val jsonConfig = getJsonFromKV()
        merge(configMap, jsonConfig)
    }

    private fun updateConfigFromJson() {
        val jsonConfig = try {
            gson.fromJson<Map<String, Any?>>(textArea.text, object : TypeToken<Map<String, Any?>>() {}.type)
        } catch (e: Exception) {
            null
        }
        if (jsonConfig != null) {
            merge(configMap, jsonConfig)
        }
    }

    protected open fun initConfig(config: MutableMap<String, Any?>) {

    }

    /**
     * 合并配置
     * sub合并到main里
     */
    protected open fun merge(mainConfig: MutableMap<String, Any?>, subConfig: Map<String, Any?>) {
        mainConfig.putAll(subConfig)
    }

    /**
     * 据config内容，展示kv界面
     */
    abstract fun showKVView(config: Map<String, Any?>)

    /**
     * kv界面内容转为json
     */
    abstract fun getJsonFromKV(): Map<String, Any?>

    /**
     * 检查并获取配置
     */
    open fun checkAndGetConfig(): T {
        // 更新输入内容
        if (tabbedPane.selectedIndex == 0) {
            updateConfigFromKV()
        } else {
            updateConfigFromJson()
        }
        val str = gson.toJson(configMap)
        return gson.fromJson(str, configClass)
    }


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

        private val gson = GsonBuilder()
            .setExclusionStrategies(object : ExclusionStrategy {
                override fun shouldSkipField(f: FieldAttributes): Boolean {
                    if (f.name == "id" || f.name == "prefix") {
                        return true
                    }
                    return false
                }

                override fun shouldSkipClass(c: Class<*>): Boolean {
                    return c == DubboBootstrap::class.java
                            || c == Object::class.java
                            || c == ServiceMetadata::class.java
                            || c == AtomicBoolean::class.java
                }
            })
            .setPrettyPrinting()
            .create()
    }
}

fun <K, V> Map<K, V>.getString(key: K): String {
    val value = get(key)
    return value?.toString() ?: ""
}
