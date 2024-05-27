package com.wxl.jdevtool.note.listener

import com.wxl.jdevtool.AppContexts
import com.wxl.jdevtool.ComponentListener
import com.wxl.jdevtool.note.NoteTabbedModule
import com.wxl.jdevtool.note.component.KVFilePanel
import com.wxl.jdevtool.note.component.NewFileDialog
import com.wxl.jdevtool.note.component.TxtFilePanel
import com.wxl.jdevtool.note.db.mapper.NoteFileMapper
import com.wxl.jdevtool.toast.ToastType
import com.wxl.jdevtool.toast.Toasts
import io.github.oshai.kotlinlogging.KotlinLogging
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.text.SimpleDateFormat
import java.util.*
import javax.swing.JOptionPane

/**
 * Create by wuxingle on 2024/05/13
 * 记事本按钮监听
 */

/**
 * 新增文件
 */
@ComponentListener("noteTabbedModule.addBtn")
class NoteAddFileActionListener(
    private val module: NoteTabbedModule
) : ActionListener {

    private val log = KotlinLogging.logger { }

    override fun actionPerformed(e: ActionEvent) {
        val filename = "未命名_${SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(Date())}"

        val noteFileDO = NewFileDialog.showDialog("新文件", filename) ?: return
        AppContexts.executeSql(NoteFileMapper::class.java) { it.insert(noteFileDO) }

        log.info { "newfile: $noteFileDO" }

        module.fileList.add(0, noteFileDO)
        module.getListModel().add(0, noteFileDO)
        module.fileJList.selectedIndex = 0
        module.fileJList.requestFocus()
    }
}

/**
 * 保存文件
 */
@ComponentListener("noteTabbedModule.saveBtn")
class NoteSaveFileActionListener(
    private val module: NoteTabbedModule
) : ActionListener {

    override fun actionPerformed(e: ActionEvent) {
        val panel = module.currentShowPanel
        if (panel is TxtFilePanel) {
            panel.saveFileContent(true)
        } else if (panel is KVFilePanel) {
            panel.saveFileContent(true)
        }
    }
}

/**
 * 删除文件
 */
@ComponentListener("noteTabbedModule.delBtn")
class NoteDelFileActionListener(
    private val module: NoteTabbedModule
) : ActionListener {

    override fun actionPerformed(e: ActionEvent) {
        val fileDO = module.fileJList.selectedValue
        if (fileDO == null) {
            Toasts.show(ToastType.WARNING, "请选择删除文件")
            return
        }

        val r = JOptionPane.showConfirmDialog(
            AppContexts.mainFrame,
            "确定永久删除:\n${fileDO.name}",
            "删除文件",
            JOptionPane.YES_NO_OPTION
        )
        if (r == JOptionPane.OK_OPTION) {

            module.fileList.removeIf { it.id == fileDO.id }
            module.getListModel().removeElement(fileDO)
            module.fileJList.requestFocus()

            AppContexts.executeSqlAsync(NoteFileMapper::class.java) {
                it.deleteById(fileDO.id!!)
            }
        }
    }
}

/**
 * 重命名文件
 */
@ComponentListener("noteTabbedModule.renameBtn")
class NoteRenameFileActionListener(
    private val module: NoteTabbedModule
) : ActionListener {

    override fun actionPerformed(e: ActionEvent) {
        val fileDO = module.fileJList.selectedValue
        if (fileDO == null) {
            Toasts.show(ToastType.WARNING, "请选择文件")
            return
        }

        val newName = JOptionPane.showInputDialog(
            AppContexts.mainFrame,
            "文件名：",
            "文件重命名",
            JOptionPane.INFORMATION_MESSAGE,
            null, null, fileDO.name
        ) as String?

        // 更新文件名
        if (!newName.isNullOrBlank()) {
            fileDO.name = newName
            module.currentFileLabel.text = newName
            module.fileJList.requestFocus()

            AppContexts.executeSqlAsync(NoteFileMapper::class.java) {
                it.updateNameById(fileDO.id!!, newName, Date())
            }
        }
    }
}