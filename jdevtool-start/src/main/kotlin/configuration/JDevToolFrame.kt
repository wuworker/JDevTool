package com.wxl.jdevtool.configuration

import com.wxl.jdevtool.TabbedModule
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.beans.factory.ObjectProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.awt.BorderLayout
import java.io.Serial
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.JTabbedPane
import javax.swing.WindowConstants

/**
 * Create by wuxingle on 2024/01/08
 * 主框架
 */
@Component
class JDevToolFrame(
    @Autowired tabbedProdiver: ObjectProvider<TabbedModule>
) : JFrame() {

    private val log = KotlinLogging.logger {}

    init {
        val tabbedPane = JTabbedPane(JTabbedPane.LEFT, JTabbedPane.WRAP_TAB_LAYOUT)

        // 需要加载的模块
        val tabbedModules = tabbedProdiver.orderedStream().toList()
        for (module in tabbedModules) {
            log.info { "load module: ${module.title}" }
            tabbedPane.addTab(module.title, module.icon, module.mainPanel, module.tip)
        }

        val mainPanel = JPanel(BorderLayout())
        mainPanel.add(tabbedPane)

        layout = BorderLayout()
        contentPane = mainPanel
        title = "JDevTool"
        defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
    }

    companion object {
        @Serial
        private const val serialVersionUID: Long = -8781370776147719216L
    }
}
