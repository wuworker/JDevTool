package com.wxl.jdevtool.encrypt

import com.formdev.flatlaf.themes.FlatMacDarkLaf
import com.wxl.jdevtool.encrypt.component.SecretTextFieldPanel
import org.junit.jupiter.api.Test
import java.awt.BorderLayout
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.WindowConstants
import kotlin.random.Random

/**
 * Create by wuxingle on 2024/02/07
 */
class SecretTextFieldPanelTest {

    @Test
    fun test() {
        FlatMacDarkLaf.setup()
        val frame = JFrame()

        val panel = JPanel(BorderLayout())
        panel.add(SecretTextFieldPanel { Random.nextBytes(6) })

        frame.contentPane = panel
        frame.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        frame.setSize(300, 300)
        frame.isVisible = true

        Thread.sleep(100000)
    }

}