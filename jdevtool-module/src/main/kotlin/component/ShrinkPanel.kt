package com.wxl.jdevtool.component

import com.wxl.jdevtool.Icons
import java.awt.BorderLayout
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.Insets
import java.io.Serial
import javax.swing.*

/**
 * Create by wuxingle on 2024/04/02
 * 可收缩panel
 */
class ShrinkPanel(
    title: String,
    val shrinkContent: JComponent
) : JPanel(BorderLayout()) {

    private val headPanel: JPanel

    private val shrinkPanel: JPanel

    val btn: JToggleButton

    val label: JLabel

    var title: String
        get() = label.text
        set(value) {
            label.text = value
        }

    init {
        headPanel = JPanel(GridBagLayout())
        shrinkPanel = JPanel(BorderLayout())

        btn = JToggleButton(Icons.chevronRight)
        with(btn) {
            isContentAreaFilled = false
            selectedIcon = Icons.chevronDown
        }
        label = JLabel(title)

        var c = GridBagConstraints()
        c.insets = Insets(0, 0, 5, 0)
        headPanel.add(btn, c)
        headPanel.add(label, c)

        c = GridBagConstraints()
        c.fill = GridBagConstraints.HORIZONTAL
        c.weightx = 1.0
        c.gridwidth = GridBagConstraints.REMAINDER
        c.insets = Insets(0, 5, 5, 0)
        headPanel.add(JSeparator(), c)

        shrinkPanel.add(shrinkContent)
        shrinkPanel.border = BorderFactory.createEmptyBorder(0, 15, 0, 15)
        shrinkPanel.isVisible = false

        initListener()

        add(headPanel, BorderLayout.NORTH)
        add(shrinkPanel)
    }

    private fun initListener() {
        btn.addChangeListener {
            val btn = it.source as JToggleButton
            shrinkPanel.isVisible = btn.isSelected
        }
    }

    companion object {
        @Serial
        private const val serialVersionUID: Long = 2535644651161127827L
    }

}