package com.wxl.jdevtool.component.history

import com.wxl.jdevtool.extension.RelativeLocation
import com.wxl.jdevtool.extension.setLocationRelativeTo
import com.wxl.jdevtool.extension.showClear
import io.github.oshai.kotlinlogging.KotlinLogging
import java.awt.BorderLayout
import java.awt.event.*
import java.io.Serial
import javax.swing.JFrame
import javax.swing.JList
import javax.swing.JTextField
import javax.swing.UIManager
import javax.swing.event.AncestorEvent
import javax.swing.event.AncestorListener

/**
 * Create by wuxingle on 2024/04/25
 * 保存历史输入的TextField
 */
class HistoryTextField(
    val historyList: HistoryList
) : JTextField() {

    private val log = KotlinLogging.logger { }

    private val jList by lazy {
        val l = JList<String>()
        val mouseListener = object : MouseAdapter() {

            override fun mouseClicked(e: MouseEvent) {
                val value = l.selectedValue
                if (!value.isNullOrBlank()) {
                    this@HistoryTextField.text = value
                    // 选中的移到第一个位置
                    historyList.addHistory(value)
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
                log.debug { "focusGained:${historyList.id}" }
            }

            override fun focusLost(e: FocusEvent) {
                val t = text
                if (t.isNotBlank()) {
                    historyList.addHistory(t)
                }
                hiddenWindow()
            }
        })

        addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent) {
                log.debug { "mouseClicked:${historyList.id}" }

                if (window.isVisible) {
                    hiddenWindow()
                } else {
                    showWindow()
                }
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
     * 显示历史选项
     */
    private fun showWindow() {
        if (window.isVisible) {
            return
        }
        val history = historyList.getShowHistory().toTypedArray()
        if (history.isEmpty()) {
            return
        }

        jList.setListData(history)
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

        const val PROP_HISTORY_BACKGROUND = "DevTool.HistoryTextField.history.background"
    }

}
