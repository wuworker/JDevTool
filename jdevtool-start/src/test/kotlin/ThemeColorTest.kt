package com.wxl.jdevtool

import com.formdev.flatlaf.themes.FlatMacDarkLaf
import org.junit.jupiter.api.Test
import javax.swing.UIManager

/**
 * Create by wuxingle on 2024/01/19
 */
class ThemeColorTest {

    @Test
    fun test(){
        FlatMacDarkLaf.setup()

        println(UIManager.getColor("Label.background"))
        println(UIManager.getColor("Label.foreground"))
        println(UIManager.getColor("Focus.color"))
        println(UIManager.getColor("Button.background"))
        println(UIManager.getColor("Button.foreground"))
        println(UIManager.getColor("Button.select"))
        println(UIManager.getColor("TextField.background"))
        println(UIManager.getColor("TextField.foreground"))
    }
}