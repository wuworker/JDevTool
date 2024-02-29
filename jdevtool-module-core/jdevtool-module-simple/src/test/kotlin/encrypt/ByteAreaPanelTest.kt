package com.wxl.jdevtool.encrypt

import com.formdev.flatlaf.themes.FlatMacDarkLaf
import com.wxl.jdevtool.encrypt.component.ByteAreaPanel
import org.junit.jupiter.api.Test
import java.awt.BorderLayout
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.WindowConstants

/**
 * Create by wuxingle on 2024/02/07
 */
class ByteAreaPanelTest {

    @Test
    fun test() {
        FlatMacDarkLaf.setup()
        val frame = JFrame()

        val panel = JPanel(BorderLayout())
        panel.add(ByteAreaPanel())

        frame.contentPane = panel
        frame.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        frame.setSize(300, 300)
        frame.isVisible = true

        Thread.sleep(100000)
    }

}