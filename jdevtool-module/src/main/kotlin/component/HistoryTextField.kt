package com.wxl.jdevtool.component

import com.wxl.jdevtool.extension.RelativeLocation
import com.wxl.jdevtool.extension.setLocationRelativeTo
import com.wxl.jdevtool.extension.showClear
import io.github.oshai.kotlinlogging.KotlinLogging
import java.awt.BorderLayout
import java.awt.event.*
import java.io.Serial
import java.util.*
import javax.swing.JFrame
import javax.swing.JList
import javax.swing.JTextField
import javax.swing.UIManager
import javax.swing.event.AncestorEvent
import javax.swing.event.AncestorListener
import javax.swing.text.Document

/**
 * Create by wuxingle on 2024/04/25
 * 保存历史输入的TextField
 */
class HistoryTextField : JTextField {

    constructor() : super() {
        history = Collections.synchronizedList(arrayListOf())
    }

    constructor(text: String) : super(text) {
        history = Collections.synchronizedList(arrayListOf())
    }

    constructor(columns: Int) : super(columns) {
        history = Collections.synchronizedList(arrayListOf())
    }

    constructor(text: String, columns: Int) : super(text, columns) {
        history = Collections.synchronizedList(arrayListOf())
    }

    constructor(doc: Document, text: String, columns: Int) : super(doc, text, columns) {
        history = Collections.synchronizedList(arrayListOf())
    }

    constructor(history: MutableList<String>) : super() {
        this.history = Collections.synchronizedList(history)
    }

    private val log = KotlinLogging.logger { }

    private val history: MutableList<String>

    // 最多缓存条数
    var cacheSize = 5

    private val jList by lazy {
        val l = JList<String>()
        val mouseListener = object : MouseAdapter() {

            override fun mouseClicked(e: MouseEvent) {
                val value = l.selectedValue
                if (!value.isNullOrBlank()) {
                    this@HistoryTextField.text = value
                    // 选中的移到第一个位置
                    addHistory(value)
                    hiddenWindow()
                }
            }

            override fun mouseMoved(e: MouseEvent) {
                val idx = l.locationToIndex(e.point)
                if (idx >= 0 && idx < l.model.size) {
                    l.selectedIndex = idx
                }
            }
        }
        val color = UIManager.getColor(PROP_HISTORY_BACKGROUND)
        if (color != null) {
            l.background = color
        }

        l.addMouseListener(mouseListener)
        l.addMouseMotionListener(mouseListener)
        l
    }

    private val window by lazy {
        val window = JFrame()
        with(window) {
            layout = BorderLayout()
            defaultCloseOperation = JFrame.DISPOSE_ON_CLOSE
            isUndecorated = true
            focusableWindowState = false
            isAlwaysOnTop = true

            contentPane.add(jList)
        }
        window
    }

    init {
        addFocusListener(object : FocusListener {
            override fun focusGained(e: FocusEvent) {
                showWindow()
            }

            override fun focusLost(e: FocusEvent) {
                val t = text
                if (t.isNotBlank()) {
                    addHistory(t)
                }
                hiddenWindow()
            }
        })

        addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent?) {
                showWindow()
            }
        })

        addComponentListener(object : ComponentAdapter() {

            override fun componentResized(e: ComponentEvent) {
                hiddenWindow()
            }

            override fun componentMoved(e: ComponentEvent) {
                hiddenWindow()
            }
        })

        addAncestorListener(object : AncestorListener {
            override fun ancestorAdded(event: AncestorEvent?) {
                hiddenWindow()
            }

            override fun ancestorRemoved(event: AncestorEvent?) {
                hiddenWindow()
            }

            override fun ancestorMoved(event: AncestorEvent?) {
                hiddenWindow()
            }
        })

        showClear()
    }

    /**
     * 加载历史记录，总共不超过cacheSize
     */
    fun loadHistory(list: List<String>): Int {
        if (history.size >= cacheSize) {
            return 0
        }
        var i = 0
        for (s in list) {
            history.add(s)
            i++
            if (history.size >= cacheSize) {
                break
            }
        }
        return i
    }

    /**
     * 获取历史记录
     */
    fun getHistory() = history.toList()

    /**
     * 加入历史选项，最近的放前面
     */
    private fun addHistory(s: String) {
        history.remove(s)
        history.add(0, s)
        if (history.size > cacheSize) {
            history.removeAt(history.lastIndex)
        }
    }

    /**
     * 显示历史选项
     */
    private fun showWindow() {
        if (history.isEmpty() || window.isVisible) {
            return
        }

        val historyList =
            if (history.size > cacheSize) history.subList(0, cacheSize).toTypedArray() else history.toTypedArray()
        jList.setListData(historyList)

        window.setSize(this.width, jList.preferredSize.height)
        window.setLocationRelativeTo(this, location = RelativeLocation.BOTTOM)
        window.isVisible = true
    }

    /**
     * 关闭历史选项窗口
     */
    private fun hiddenWindow() {
        if (!window.isVisible) {
            return
        }
        window.isVisible = false
    }

    companion object {

        @Serial
        private const val serialVersionUID: Long = 1850142860154831889L

        val PROP_HISTORY_BACKGROUND = "DevTool.HistoryTextField.history.background"
    }

}
