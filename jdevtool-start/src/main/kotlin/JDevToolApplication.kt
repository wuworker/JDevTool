package com.wxl.jdevtool

import com.formdev.flatlaf.themes.FlatMacDarkLaf
import com.wxl.jdevtool.configuration.JDevToolFrame
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder

/**
 * Create by wuxingle on 2024/01/02
 * 启动类
 */
@SpringBootApplication
class JDevToolApplication

fun main(args: Array<String>) {
    FlatMacDarkLaf.setup()

    val context = SpringApplicationBuilder(JDevToolApplication::class.java).headless(false)
        .run(*args)

    val jdevToolFrame = context.getBean(JDevToolFrame::class.java)
    jdevToolFrame.setSize(800, 800)
    jdevToolFrame.isVisible = true
}
