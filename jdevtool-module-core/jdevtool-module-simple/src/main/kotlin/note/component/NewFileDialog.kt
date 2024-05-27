package com.wxl.jdevtool.note.component

import com.wxl.jdevtool.AppContexts
import com.wxl.jdevtool.extension.setHint
import com.wxl.jdevtool.note.NoteType
import com.wxl.jdevtool.note.db.NoteFileDO
import com.wxl.jdevtool.toast.ToastType
import com.wxl.jdevtool.toast.Toasts
import com.wxl.jdevtool.util.ComponentUtils
import com.wxl.jdevtool.validate.InputChecker
import java.awt.*
import java.io.Serial
import java.util.*
import javax.swing.*
import javax.swing.event.DocumentEvent
import javax.swing.text.JTextComponent


/**
 * Create by wuxingle on 2024/05/14
 * 新增文件弹窗
 */
class NewFileDialog(
    title: String,
    defaultFileName: String = ""
) : JDialog(AppContexts.mainFrame, title) {

    private val filetypeCombobox = JComboBox(NoteType.values())

    private val filenameField = JTextField(20)

    private var confirmAdd = false

    init {
        isModal = true
        defaultCloseOperation = DISPOSE_ON_CLOSE

        val centerPanel = JPanel(GridBagLayout())

        filenameField.setHint(defaultFileName)
        with(centerPanel) {
            border = BorderFactory.createEmptyBorder(0, 0, 10, 0)
            add(
                JLabel("文件类型："), ComponentUtils.createConstraints(
                    insets = Insets(5, 5, 5, 5)
                )
            )
            add(
                filetypeCombobox, ComponentUtils.createConstraints(
                    insets = Insets(5, 0, 5, 5),
                    gridwidth = GridBagConstraints.REMAINDER,
                    weightx = 1.0,
                    fill = GridBagConstraints.HORIZONTAL
                )
            )
            add(
                JLabel("文件名："), ComponentUtils.createConstraints(
                    insets = Insets(5, 5, 5, 5)
                )
            )
            add(
                filenameField, ComponentUtils.createConstraints(
                    insets = Insets(5, 0, 5, 5),
                    gridwidth = GridBagConstraints.REMAINDER,
                    weightx = 1.0,
                    fill = GridBagConstraints.HORIZONTAL
                )
            )
        }

        // 按钮
        val buttonArea = JPanel(FlowLayout(FlowLayout.RIGHT, 10, 0))
        val cancelBtn = JButton("取消")
        cancelBtn.addActionListener { dispose() }

        val okBtn = JButton("确定")
        val okChecker = object : InputChecker(filenameField) {
            override fun doCheck(component: JTextComponent): Boolean {
                if (component.text.isNotEmpty() && component.text.isBlank()) {
                    return false
                }
                if (component.text.length > 20) {
                    return false
                }
                return !component.text.isNullOrBlank() || defaultFileName.isNotBlank()
            }


            override fun documentUpdate(e: DocumentEvent) {
                showNormal()
            }
        }

        okBtn.addActionListener {
            if (!okChecker.check()) {
                if (filenameField.text.length > 20) {
                    Toasts.show(ToastType.WARNING, "文件名必须小于20字符")
                }

                return@addActionListener
            }
            confirmAdd = true
            dispose()
        }


        with(buttonArea) {
            add(cancelBtn)
            add(okBtn)
        }

        val panel = JPanel(BorderLayout())
        with(panel) {
            border = BorderFactory.createEmptyBorder(20, 20, 20, 20)
            add(centerPanel)
            add(buttonArea, BorderLayout.SOUTH)
        }

        with(contentPane) {
            layout = BorderLayout()
            add(panel)
        }

        pack()
        setLocationRelativeTo(owner)
    }


    companion object {
        @Serial
        private const val serialVersionUID: Long = 1550660608183156905L

        fun showDialog(title: String, defaultFileName: String = ""): NoteFileDO? {
            val dialog = NewFileDialog(title, defaultFileName)
            dialog.isVisible = true
            dialog.dispose()
            if (!dialog.confirmAdd) {
                return null
            }

            var filename = dialog.filenameField.text
            if (filename.isBlank()) {
                filename = defaultFileName
            }
            val filetype = dialog.filetypeCombobox.selectedItem as NoteType
            val date = Date()

            val content = ""
            return NoteFileDO(filename, filetype.type, content, date, date)
        }
    }


}