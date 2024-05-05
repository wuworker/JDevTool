package com.wxl.jdevtool.dubbo

import com.google.gson.reflect.TypeToken
import com.wxl.jdevtool.component.CopiedPanel
import com.wxl.jdevtool.component.HistoryTextField
import com.wxl.jdevtool.dubbo.component.ConfigPanel
import com.wxl.jdevtool.util.ComponentUtils
import java.awt.GridBagConstraints
import java.awt.GridLayout
import java.io.Serial
import java.lang.reflect.Type
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField

/**
 * Create by wuxingle on 2024/04/24
 * 附加请求参数
 */
class AttachmentConfigPanel : ConfigPanel<MutableMap<String, String>>("附加参数") {

    override val config = linkedMapOf<String, String>()

    override val configType: Type = object : TypeToken<LinkedHashMap<String, String>>() {}.type

    val entryPanel: CopiedPanel<JPanel>

    val entryFields = arrayListOf<Pair<JTextField, JTextField>>()

    init {
        val keyHistory = arrayListOf<String>()
        val valHistory = arrayListOf<String>()
        entryPanel = CopiedPanel({
            val p = JPanel(GridLayout(1, 2))
            val k = HistoryTextField(keyHistory)
            val v = HistoryTextField(valHistory)
            entryFields.add(Pair(k, v))

            p.add(k)
            p.add(v)

            p
        })

        with(kvPanel) {
            add(
                JLabel("Key"), ComponentUtils.createConstraints(
                    anchor = GridBagConstraints.WEST,
                    weightx = 0.5
                )
            )

            add(
                JLabel("Value"), ComponentUtils.createConstraints(
                    anchor = GridBagConstraints.WEST,
                    weightx = 0.5,
                    gridwidth = GridBagConstraints.REMAINDER
                )
            )

            add(
                entryPanel, ComponentUtils.createConstraints(
                    weightx = 1.0,
                    fill = GridBagConstraints.HORIZONTAL,
                    gridwidth = 2
                )
            )
        }
    }

    override fun merge(mainConfig: MutableMap<String, String>, subConfig: MutableMap<String, String>) {
        mainConfig.putAll(subConfig)
    }

    override fun showKVView(config: MutableMap<String, String>) {
        val kvSize = entryPanel.copiedComponents.size
        val jsonSize = config.size

        if (kvSize < jsonSize) {
            entryPanel.addCopied(jsonSize - kvSize)
        } else if (kvSize > jsonSize) {
            entryPanel.delCopied(kvSize - jsonSize)
        }
        if (jsonSize == 0) {
            entryFields[0].first.text = ""
            entryFields[0].second.text = ""
            return
        }

        for ((i, entry) in config.entries.withIndex()) {
            entryFields[i].first.text = entry.key
            entryFields[i].second.text = entry.value
        }
    }

    override fun showJsonView(config: MutableMap<String, String>) {
        for (entryField in entryFields) {
            config[entryField.first.text] = entryField.second.text
        }
    }

    override fun checkAndGetConfig(): MutableMap<String, String> {
        return config
    }

    companion object {
        @Serial
        private const val serialVersionUID: Long = -882290391965051873L
    }
}