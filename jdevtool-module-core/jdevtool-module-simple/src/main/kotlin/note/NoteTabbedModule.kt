package com.wxl.jdevtool.note

import com.formdev.flatlaf.FlatClientProperties
import com.formdev.flatlaf.extras.FlatSVGIcon
import com.formdev.flatlaf.icons.FlatSearchIcon
import com.wxl.jdevtool.AppContexts
import com.wxl.jdevtool.ComponentId
import com.wxl.jdevtool.TabbedModule
import com.wxl.jdevtool.component.ComponentFactory
import com.wxl.jdevtool.extension.setHint
import com.wxl.jdevtool.note.component.KVFilePanel
import com.wxl.jdevtool.note.component.TxtFilePanel
import com.wxl.jdevtool.note.db.NoteFileDO
import com.wxl.jdevtool.note.db.mapper.NoteFileMapper
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import java.awt.BorderLayout
import java.awt.FlowLayout
import java.awt.Font
import java.util.*
import javax.swing.*

/**
 * Create by wuxingle on 2024/05/13
 * 记事本
 */
@Order(50)
@Component
@ComponentId("noteTabbedModule")
class NoteTabbedModule : TabbedModule {

    private val log = KotlinLogging.logger { }

    // 全部文件列表
    val fileList = Collections.synchronizedList(arrayListOf<NoteFileDO>())

    final override val mainPanel: JPanel = JPanel(BorderLayout())

    @ComponentId("searchField")
    val searchField = JTextField()

    @ComponentId("fileJList")
    val fileJList = JList(DefaultListModel<NoteFileDO>())

    val rightPanel = JPanel(BorderLayout(0, 5))

    var currentShowPanel: JPanel? = null

    @ComponentId("currentFileLabel")
    val currentFileLabel = JLabel()

    @ComponentId("addBtn")
    val addBtn = ComponentFactory.createIconBtn(FlatSVGIcon("icons/note/addFile.svg", 1.5f), toolTip = "新增文件")

    @ComponentId("saveBtn")
    val saveBtn = ComponentFactory.createIconBtn(FlatSVGIcon("icons/note/saveFile.svg", 1.5f), toolTip = "保存文件")

    @ComponentId("delBtn")
    val delBtn = ComponentFactory.createIconBtn(FlatSVGIcon("icons/note/delFile.svg", 1.5f), toolTip = "删除文件")

    @ComponentId("renameBtn")
    val renameBtn = ComponentFactory.createIconBtn(FlatSVGIcon("icons/note/renameFile.svg"), toolTip = "重命名文件")

    override fun afterPropertiesSet() {
        // 初始化属性
        with(searchField) {
            columns = 15
            font = Font(font.name, font.style, 15)
            setHint("搜索")
            putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_ICON, FlatSearchIcon())
        }

        // 加载fileList
        fileList.addAll(loadFileList())

        with(fileJList) {
            selectionMode = ListSelectionModel.SINGLE_SELECTION
            (model as DefaultListModel).addAll(fileList)
            font = Font(font.name, font.style, 15)
            setCellRenderer(object : ListCellRenderer<NoteFileDO> {

                private val redner = DefaultListCellRenderer()

                override fun getListCellRendererComponent(
                    list: JList<out NoteFileDO>,
                    value: NoteFileDO,
                    index: Int,
                    isSelected: Boolean,
                    cellHasFocus: Boolean
                ): java.awt.Component {
                    return redner.getListCellRendererComponent(list, value.name, index, isSelected, cellHasFocus)
                }
            })
        }

        val leftPanel = JPanel(BorderLayout(0, 10))
        val sp = JScrollPane(fileJList)
        with(leftPanel) {
            add(searchField, BorderLayout.NORTH)
            add(sp, BorderLayout.CENTER)
        }

        // 控制按钮
        renameBtn.isVisible = false
        val controlPanel = JPanel(BorderLayout())
        val controlLeftPanel = JPanel(FlowLayout(FlowLayout.LEFT))
        with(controlLeftPanel) {
            add(addBtn)
            add(saveBtn)
        }
        val controlRightPanel = JPanel(FlowLayout(FlowLayout.RIGHT))
        with(controlRightPanel) {
            add(delBtn)
        }

        val controlBottomPanel = JPanel(BorderLayout())
        with(controlBottomPanel) {
            add(JSeparator(), BorderLayout.NORTH)

            val p = JPanel(FlowLayout(FlowLayout.LEFT))
            p.add(currentFileLabel)
            p.add(renameBtn)
            add(p)
        }
        with(controlPanel) {
            add(controlLeftPanel, BorderLayout.WEST)
            add(controlRightPanel, BorderLayout.EAST)
            add(controlBottomPanel, BorderLayout.SOUTH)
        }

        rightPanel.add(controlPanel, BorderLayout.NORTH)

        val splitPanel = JSplitPane(JSplitPane.HORIZONTAL_SPLIT)
        with(splitPanel) {
            leftComponent = leftPanel
            rightComponent = rightPanel
            border = BorderFactory.createEmptyBorder(5, 5, 0, 5)
        }
        mainPanel.add(splitPanel)
    }

    /**
     * 获取展示的file list
     */
    fun getListModel(): DefaultListModel<NoteFileDO> = fileJList.model as DefaultListModel<NoteFileDO>

    /**
     * 显示文件内容
     */
    fun showFilePanel(fid: Long) {
        val fileDO = AppContexts.executeSql(NoteFileMapper::class.java) {
            it.selectDetailById(fid)
        }
        if (currentShowPanel != null) {
            rightPanel.remove(currentShowPanel)
        }
        if (fileDO == null) {
            currentShowPanel = null
            return
        }
        currentFileLabel.text = fileDO.name
        renameBtn.isVisible = true

        if (fileDO.type == NoteType.TXT.type) {
            currentShowPanel = TxtFilePanel(fileDO)
        } else if (fileDO.type == NoteType.KV.type) {
            currentShowPanel = KVFilePanel(fileDO)
        }

        rightPanel.add(currentShowPanel)
        rightPanel.revalidate()
        rightPanel.repaint()
    }

    /**
     * 默认页面
     */
    fun showDefaultPanel() {
        if (currentShowPanel != null) {
            rightPanel.remove(currentShowPanel)
            rightPanel.revalidate()
            rightPanel.repaint()

            currentShowPanel = null
        }
        currentFileLabel.text = ""
        renameBtn.isVisible = false
    }

    /**
     * 加载所有文件
     */
    private fun loadFileList(): List<NoteFileDO> {
        return AppContexts.executeSql(NoteFileMapper::class.java) {
            it.selectList()
        }
    }


    override val title = "记事本"

    override val icon = FlatSVGIcon("icons/note_dark.svg")

    override val tip = "记事本"
}
