package com.wxl.jdevtool.note.component

import com.formdev.flatlaf.extras.FlatSVGIcon
import com.formdev.flatlaf.ui.FlatButtonUI
import com.google.gson.reflect.TypeToken
import com.wxl.jdevtool.AppContexts
import com.wxl.jdevtool.component.ComponentFactory
import com.wxl.jdevtool.extension.showClear
import com.wxl.jdevtool.extension.showCopy
import com.wxl.jdevtool.note.db.NoteFileDO
import com.wxl.jdevtool.note.db.mapper.NoteFileMapper
import com.wxl.jdevtool.toast.ToastType
import com.wxl.jdevtool.toast.Toasts
import com.wxl.jdevtool.util.ComponentUtils
import io.github.oshai.kotlinlogging.KotlinLogging
import java.awt.BorderLayout
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.event.FocusAdapter
import java.awt.event.FocusEvent
import java.io.Serial
import java.util.*
import javax.swing.*

/**
 * Create by wuxingle on 2024/05/22
 * 键值对文件
 */
class KVFilePanel(
    val fileDO: NoteFileDO
) : JPanel(BorderLayout()) {

    private data class FileContent(
        val key: String,
        val value: String
    )

    private data class KVEntry(
        val addBtn: JButton,
        val delBtn: JButton,
        val keyTextField: JTextField,
        val valTextField: JTextField
    )

    private val log = KotlinLogging.logger { }

    private val kvPanel = JPanel(GridBagLayout())

    private val kvEntryList = arrayListOf<KVEntry>()

    init {
        val contents = AppContexts.gson.fromJson<List<FileContent>>(
            fileDO.content,
            object : TypeToken<List<FileContent>>() {}.type
        )
        if (contents == null || contents.isEmpty()) {
            addEntry(needUpdateUI = false)
        } else {
            for (content in contents) {
                addEntry(content.key, content.value, false)
            }
        }
        refreshKVPaint()

        add(kvPanel)
    }

    /**
     * 配置项
     */
    private fun createEntry(): KVEntry {
        val addBtn = ComponentFactory.createIconBtn(FlatSVGIcon("icons/note/newKVEntry.svg"))
        val delBtn = ComponentFactory.createIconBtn(FlatSVGIcon("icons/note/delKVEntry.svg"), toolTip = "删除")
        delBtn.setUI(object : FlatButtonUI(false) {
            override fun installDefaults(b: AbstractButton?) {
                super.installDefaults(b)
                disabledBackground = UIManager.getColor("Panel.background")
            }
        })
        delBtn.background = UIManager.getColor("Panel.background")

        val kTextField = JTextField()
        val vTextField = JTextField()

        val entry = KVEntry(addBtn, delBtn, kTextField, vTextField)

        with(kTextField) {
            showClear()
            showCopy()
            addFocusListener(object : FocusAdapter() {
                override fun focusLost(e: FocusEvent) {
                    saveFileContent()
                }
            })
        }
        with(vTextField) {
            showClear()
            showCopy()
            addFocusListener(object : FocusAdapter() {
                override fun focusLost(e: FocusEvent) {
                    saveFileContent()
                }
            })
        }
        addBtn.addActionListener {
            addEntry(before = entry)
            saveFileContent(false)
        }
        delBtn.addActionListener {
            deleteEntry(entry)
            saveFileContent(false)
        }

        return entry
    }

    private fun addEntry(
        key: String = "",
        value: String = "",
        needUpdateUI: Boolean = true,
        before: KVEntry? = null
    ) {
        val p = createEntry()
        p.keyTextField.text = key
        p.valTextField.text = value

        if (before != null) {
            val idx = kvEntryList.indexOf(before)
            if (idx >= 0) {
                kvEntryList.add(idx + 1, p)
            }
        } else {
            kvEntryList.add(p)
        }

        kvEntryList[0].delBtn.isEnabled = kvEntryList.size > 1
        if (needUpdateUI) {
            refreshKVPaint()
        }
    }

    private fun deleteEntry(entry: KVEntry) {
        val res = kvEntryList.remove(entry)
        log.info { "deleteEntry: ${res}, size:${kvEntryList.size}" }

        if (kvEntryList.size == 1) {
            kvEntryList[0].delBtn.isEnabled = false
        }

        refreshKVPaint()
    }

    private fun refreshKVPaint() {
        // 重新布局
        kvPanel.removeAll()
        for ((idx, panel) in kvEntryList.withIndex()) {
            val weighty = if (idx >= kvEntryList.size - 1) 1.0 else 0.0
            kvPanel.add(
                panel.keyTextField, ComponentUtils.createConstraints(
                    anchor = GridBagConstraints.NORTH,
                    weightx = 0.5,
                    weighty = weighty,
                    fill = GridBagConstraints.HORIZONTAL
                )
            )
            kvPanel.add(
                panel.valTextField, ComponentUtils.createConstraints(
                    anchor = GridBagConstraints.NORTH,
                    weightx = 0.5,
                    weighty = weighty,
                    fill = GridBagConstraints.HORIZONTAL
                )
            )
            kvPanel.add(
                panel.addBtn, ComponentUtils.createConstraints(
                    anchor = GridBagConstraints.NORTH,
                    weighty = weighty,
                    fill = GridBagConstraints.HORIZONTAL
                )
            )
            kvPanel.add(
                panel.delBtn, ComponentUtils.createConstraints(
                    anchor = GridBagConstraints.NORTH,
                    gridwidth = GridBagConstraints.REMAINDER,
                    weighty = weighty,
                    fill = GridBagConstraints.HORIZONTAL
                )
            )
        }

        kvPanel.invalidate()
        kvPanel.repaint()
        if (kvEntryList.isNotEmpty()) {
            kvPanel.requestFocus()
        }
    }

    /**
     * 保存文件内容
     */
    fun saveFileContent(toast: Boolean = false) {
        val dataList = kvEntryList.map { FileContent(it.keyTextField.text, it.valTextField.text) }.toList()
        val content = AppContexts.gson.toJson(dataList)
        if (fileDO.content != content) {
            fileDO.content = content
            AppContexts.executeSqlAsync(NoteFileMapper::class.java) {
                it.updateContentById(fileDO.id!!, fileDO.content!!, Date())
                if (toast) {
                    SwingUtilities.invokeLater { Toasts.show(ToastType.SUCCESS, "保存成功") }
                }
            }
        } else if (toast) {
            Toasts.show(ToastType.SUCCESS, "保存成功")
        }
    }

    companion object {
        @Serial
        private const val serialVersionUID: Long = 1742428229608131311L
    }

}

