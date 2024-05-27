package com.wxl.jdevtool.note.listener

import com.wxl.jdevtool.ComponentListener
import com.wxl.jdevtool.note.NoteTabbedModule
import io.github.oshai.kotlinlogging.KotlinLogging
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener

/**
 * Create by wuxingle on 2024/05/13
 * 搜索文件
 */
@ComponentListener("noteTabbedModule.searchField")
class NoteSearchFieldListener(
    private val module: NoteTabbedModule
) : DocumentListener {

    private val log = KotlinLogging.logger { }

    override fun insertUpdate(e: DocumentEvent) {
        searchFile(e.document.getText(0, e.document.length))
    }

    override fun removeUpdate(e: DocumentEvent) {
        searchFile(e.document.getText(0, e.document.length))
    }

    override fun changedUpdate(e: DocumentEvent) {
        searchFile(e.document.getText(0, e.document.length))
    }

    /**
     * 展示过滤后文本
     */
    private fun searchFile(text: String) {
        val listModel = module.getListModel()
        listModel.clear()
        if (text.isEmpty()) {
            listModel.addAll(module.fileList)
        } else {
            listModel.addAll(module.fileList.filter { it.name!!.contains(text) })
        }
    }

}
