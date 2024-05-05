package com.wxl.jdevtool

import com.formdev.flatlaf.themes.FlatMacDarkLaf
import com.wxl.jdevtool.component.HistoryTextField
import org.junit.Test
import java.awt.BorderLayout
import java.awt.FlowLayout
import javax.swing.JComboBox
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.WindowConstants

/**
 * Create by wuxingle on 2024/04/25
 */
class HistoryTextFieldTest {

    @Test
    fun test() {
        FlatMacDarkLaf.setup()
        val frame = JFrame()

        val panel = JPanel(BorderLayout())
        panel.add(HistoryTextField(10))

        frame.contentPane = panel
        frame.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        frame.setSize(300, 300)
        frame.isVisible = true

        Thread.sleep(100000)
    }

    @Test
    fun test2() {
        FlatMacDarkLaf.setup()
        val frame = JFrame()

        val panel = JPanel(FlowLayout())

        val cb = JComboBox<String>(arrayOf("1", "2", "3"))

        panel.add(cb)

        frame.contentPane = panel
        frame.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        frame.setSize(300, 300)
        frame.isVisible = true

        Thread.sleep(100000)
    }
}