package com.wxl.jdevtool.dubbo

import com.google.gson.reflect.TypeToken
import com.wxl.jdevtool.component.CopiedActionListener
import com.wxl.jdevtool.component.CopiedPanel
import com.wxl.jdevtool.component.history.HistoryTextField
import com.wxl.jdevtool.component.history.StorageHistoryList
import com.wxl.jdevtool.dubbo.component.ConfigPanel
import com.wxl.jdevtool.extension.getUnescapeText
import com.wxl.jdevtool.util.ComponentUtils
import java.awt.GridBagConstraints
import java.awt.GridLayout
import java.io.Serial
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField

/**
 * Create by wuxingle on 2024/04/24
 * 附加请求参数
 */
class AttachmentConfigPanel : ConfigPanel<Map<String, String>>(
    "附加参数",
    object : TypeToken<LinkedHashMap<String, String>>() {}.type
) {

    val entryPanel: CopiedPanel<JPanel>

    val entryFields = arrayListOf<Pair<JTextField, JTextField>>()

    init {
        val historyKeyList = StorageHistoryList("dubbo:attachment.key")
        val historyValList = StorageHistoryList("dubbo:attachment.value")
        entryPanel = CopiedPanel({
            val p = JPanel(GridLayout(1, 2))
            val k = HistoryTextField(historyKeyList)
            val v = HistoryTextField(historyValList)
            entryFields.add(Pair(k, v))

            p.add(k)
            p.add(v)

            p
        })
        entryPanel.addCopiedActionListener(object : CopiedActionListener {
            override fun componentRemove(size: Int) {
                entryFields.removeAt(entryFields.size - 1)
            }
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

    override fun merge(mainConfig: MutableMap<String, Any?>, subConfig: Map<String, Any?>) {
        mainConfig.clear()
        mainConfig.putAll(subConfig)
    }

    override fun showKVView(config: Map<String, Any?>) {
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
            entryFields[i].second.text = entry.value?.toString() ?: ""
        }
    }

    override fun getJsonFromKV(): Map<String, Any?> {
        val config = linkedMapOf<String, Any?>()
        for (entryField in entryFields) {
            val k = entryField.first.getUnescapeText()
            val v = entryField.second.getUnescapeText()
            if (k.isEmpty() && v.isEmpty()) {
                continue
            }
            config[entryField.first.text] = entryField.second.text
        }
        return config
    }

    companion object {
        @Serial
        private const val serialVersionUID: Long = -882290391965051873L
    }
}