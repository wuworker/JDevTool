package com.wxl.jdevtool.note.listener

import com.wxl.jdevtool.ComponentListener
import com.wxl.jdevtool.note.NoteTabbedModule
import javax.swing.event.ListSelectionEvent
import javax.swing.event.ListSelectionListener

/**
 * Create by wuxingle on 2024/05/13
 * 文件列表选择项
 */
@ComponentListener("noteTabbedModule.fileJList")
class NoteFileListSelectionListener(
    private val module: NoteTabbedModule
) : ListSelectionListener {

    override fun valueChanged(e: ListSelectionEvent) {
        // 鼠标释放
        if (!e.valueIsAdjusting) {
            val fileDO = module.fileJList.selectedValue
            if (fileDO != null) {
                module.showFilePanel(fileDO.id!!)
            } else {
                module.showDefaultPanel()
            }
        }
    }

}