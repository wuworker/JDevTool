package com.wxl.jdevtool

import com.formdev.flatlaf.themes.FlatMacDarkLaf
import org.junit.jupiter.api.Test
import java.awt.Color
import javax.swing.JList
import javax.swing.UIManager

/**
 * Create by wuxingle on 2024/01/19
 */
class ThemeColorTest {

    @Test
    fun test() {
        FlatMacDarkLaf.setup()

        val list = UIManager.getLookAndFeelDefaults().entries
            .filter { it.value is Color }
            .map { it.key as String }
            .sorted()

        for (s in list) {
            println("${s}: ${UIManager.getColor(s)}")
        }

        println()
        val l = JList<String>()
        println(l.background)
        println(l.selectionBackground)
    }
}