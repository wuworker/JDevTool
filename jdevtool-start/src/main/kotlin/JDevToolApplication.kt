package com.wxl.jdevtool

import com.formdev.flatlaf.themes.FlatMacDarkLaf
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import java.awt.BorderLayout
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.JTabbedPane
import javax.swing.WindowConstants

/**
 * Create by wuxingle on 2024/01/02
 * 启动类
 */
@SpringBootApplication
class JDevToolApplication

private val log = KotlinLogging.logger {}

fun main(args: Array<String>) {

    FlatMacDarkLaf.setup()
    val context = SpringApplicationBuilder(JDevToolApplication::class.java).headless(false)
        .run(*args)

    // 需要加载的模块
    val tabbedModules = context.getBeanProvider(TabbedModule::class.java).orderedStream().toList()

    val tabbedPane = JTabbedPane(JTabbedPane.LEFT, JTabbedPane.WRAP_TAB_LAYOUT)
    for (module in tabbedModules) {
        log.info { "load module: ${module.title}" }
        tabbedPane.addTab(module.title, module.icon, module.getPanel(), module.tip)
    }

    val mainPanel = JPanel(BorderLayout())
    mainPanel.add(tabbedPane)

    val frame = JFrame()
    with(frame) {
        layout = BorderLayout()
        add(mainPanel)
        title = "JDevTool"
        defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        setSize(600, 600)
        isVisible = true
    }
}



