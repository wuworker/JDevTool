package com.wxl.jdevtool.note.component

import com.wxl.jdevtool.AppContexts
import com.wxl.jdevtool.component.ComponentFactory
import com.wxl.jdevtool.extension.SyntaxEnum
import com.wxl.jdevtool.extension.showCaretLocation
import com.wxl.jdevtool.note.NoteAttrs
import com.wxl.jdevtool.note.db.NoteFileDO
import com.wxl.jdevtool.note.db.mapper.NoteFileMapper
import com.wxl.jdevtool.toast.ToastType
import com.wxl.jdevtool.toast.Toasts
import io.github.oshai.kotlinlogging.KotlinLogging
import org.fife.ui.rsyntaxtextarea.SyntaxConstants
import org.fife.ui.rtextarea.RTextScrollPane
import java.awt.BorderLayout
import java.awt.FlowLayout
import java.awt.event.FocusAdapter
import java.awt.event.FocusEvent
import java.io.Serial
import java.util.*
import javax.swing.*

/**
 * Create by wuxingle on 2024/05/16
 * 文本文件展示
 */
class TxtFilePanel(
    val fileDO: NoteFileDO
) : JPanel(BorderLayout(0, 5)) {

    private val log = KotlinLogging.logger { }

    // 文件格式选项
    val textStyleComboBox = JComboBox(SyntaxEnum.values().map { it.style }.toTypedArray())

    val textArea = ComponentFactory.createTextArea()

    init {
        val txtControlPanel = JPanel(FlowLayout(FlowLayout.LEFT))
        with(txtControlPanel) {
            add(JLabel("文件格式："))
            add(textStyleComboBox)
        }
        val style = fileDO.getAttrMap()[NoteAttrs.STYLE]
        if (style != null) {
            textStyleComboBox.selectedItem = style
        }

        with(textArea) {
            syntaxEditingStyle = SyntaxConstants.SYNTAX_STYLE_NONE
            isCodeFoldingEnabled = true
            showCaretLocation()
            text = fileDO.content
        }
        val txtSp = RTextScrollPane(textArea)
        with(txtSp) {
            verticalScrollBarPolicy = ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED
            horizontalScrollBarPolicy = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED
        }

        initListener()

        add(txtControlPanel, BorderLayout.NORTH)
        add(txtSp)
    }


    private fun initListener() {
        // 文件格式选项变化，保存
        textStyleComboBox.addActionListener {
            val style = textStyleComboBox.selectedItem as String
            textArea.syntaxEditingStyle = style

            val currentStyle = fileDO.getAttrMap()[NoteAttrs.STYLE]
            if (currentStyle != style) {
                AppContexts.executeSqlAsync(NoteFileMapper::class.java) {
                    val attrs = fileDO.getAttrMap()
                    val newAttrs = linkedMapOf<String, String?>()
                    newAttrs.putAll(attrs)
                    newAttrs[NoteAttrs.STYLE] = style

                    it.updateAttrsById(fileDO.id!!, AppContexts.gson.toJson(newAttrs), Date())
                }
            }
        }

        // 文件内容自动保存
        textArea.addFocusListener(object : FocusAdapter() {
            override fun focusLost(e: FocusEvent) {
                saveFileContent()
            }
        })
    }

    /**
     * 保存文件内容
     */
    fun saveFileContent(toast: Boolean = false) {
        val content = textArea.text
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
        private const val serialVersionUID: Long = 5642340010668589112L
    }

}
