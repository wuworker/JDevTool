package com.wxl.jdevtool.component

import com.wxl.jdevtool.Icons
import com.wxl.jdevtool.toast.ToastType
import com.wxl.jdevtool.toast.Toasts
import com.wxl.jdevtool.util.ComponentUtils
import java.awt.BorderLayout
import java.awt.Component
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.io.Serial
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList
import java.util.function.Supplier
import javax.swing.Box
import javax.swing.JButton
import javax.swing.JPanel
import javax.swing.SwingUtilities

/**
 * Create by wuxingle on 2024/04/02
 * 复制组件panel
 */
class CopiedPanel<T : Component>(
    val newComponent: Supplier<T>,
    val insetSize: Int = 0
) : JPanel(GridBagLayout()) {

    /**
     * 复制出来的panel，包括原始的
     */
    val copiedComponents = CopyOnWriteArrayList<T>()

    private val leftBox: Box

    private val addBtn: JButton

    private val subBtn: JButton

    init {
        val leftPanel = JPanel(BorderLayout())
        leftBox = Box.createVerticalBox()

        addBtn = JButton()
        with(addBtn) {
            icon = Icons.add
            rolloverIcon = Icons.addHover
            pressedIcon = Icons.addPress
            isContentAreaFilled = false
        }
        subBtn = JButton()
        with(subBtn) {
            icon = Icons.remove
            rolloverIcon = Icons.removeHover
            pressedIcon = Icons.removePress
            isContentAreaFilled = false
        }

        val firstComponent = newComponent.get()
        copiedComponents.add(firstComponent)
        leftBox.add(firstComponent)

        leftPanel.add(leftBox)
        add(
            leftPanel,
            ComponentUtils.createConstraints(
                anchor = GridBagConstraints.NORTH,
                weightx = 1.0,
                weighty = 1.0,
                fill = GridBagConstraints.HORIZONTAL
            )
        )
        add(addBtn, ComponentUtils.createConstraints(anchor = GridBagConstraints.NORTHWEST))
        add(
            subBtn,
            ComponentUtils.createConstraints(
                anchor = GridBagConstraints.NORTHWEST,
                gridwidth = GridBagConstraints.REMAINDER
            )
        )

        initListener()
    }

    /**
     * 新增复制组件
     */
    fun addCopied(num: Int = 1) {
        for (i in 0 until num) {
            val component = newComponent.get()
            copiedComponents.add(component)

            leftBox.add(Box.createVerticalStrut(insetSize))
            leftBox.add(component)

            // 新增组件监听
            triggerAdd()
        }
        SwingUtilities.updateComponentTreeUI(this)
    }

    /**
     * 删除复制的组件
     */
    fun delCopied(num: Int = 1): Int {
        if (num < 1) {
            return 0
        }

        var i = 0
        while (i < num && copiedComponents.size > 1) {
            // 删除间距
            leftBox.remove((copiedComponents.size - 1) * 2 - 1)

            val old = copiedComponents.removeAt(copiedComponents.size - 1)
            leftBox.remove(old)
            i++

            // 删除组件监听
            triggerRemove()
        }
        if (i > 0) {
            SwingUtilities.updateComponentTreeUI(this)
        }
        return i
    }

    /**
     * 组件变化监听
     */
    fun addCopiedActionListener(listener: CopiedActionListener) {
        listenerList.add(CopiedActionListener::class.java, listener)
    }

    private fun initListener() {
        addBtn.addActionListener {
            addCopied()
        }

        subBtn.addActionListener {
            if (delCopied() == 0) {
                Toasts.show(ToastType.WARNING, "至少保留一个组件")
            }
        }
    }

    private fun triggerAdd() {
        val listeners = listenerList.getListeners(CopiedActionListener::class.java)
        val size = copiedComponents.size
        for (listener in listeners) {
            listener.componentAdd(size)
        }
    }

    private fun triggerRemove() {
        val listeners = listenerList.getListeners(CopiedActionListener::class.java)
        val size = copiedComponents.size
        for (listener in listeners) {
            listener.componentRemove(size)
        }
    }

    companion object {
        @Serial
        private const val serialVersionUID: Long = -6494962017065440991L
    }

}

interface CopiedActionListener : EventListener {

    /**
     * 新增触发
     * @param size 变化后的size
     */
    fun componentAdd(size: Int) {}

    /**
     * 删除触发
     * @param size 变化后的size
     */
    fun componentRemove(size: Int) {}
}
