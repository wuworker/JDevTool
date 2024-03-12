package com.wxl.jdevtool.component

import com.formdev.flatlaf.FlatClientProperties
import com.wxl.jdevtool.TabbedModule
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.beans.factory.ObjectProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.awt.BorderLayout
import java.io.Serial
import javax.swing.*
import javax.swing.event.ChangeEvent
import javax.swing.event.ChangeListener

/**
 * Create by wuxingle on 2024/01/08
 * 主框架
 */
@Component
class JDevToolFrame(
    @Autowired tabbedProdiver: ObjectProvider<TabbedModule>,
    @Autowired messageNotifierPanel: MessageBarPanel
) : JFrame() {

    private val log = KotlinLogging.logger {}

    init {
        val tabbedPane = JTabbedPane(JTabbedPane.LEFT, JTabbedPane.WRAP_TAB_LAYOUT)
        tabbedPane.putClientProperty(
            FlatClientProperties.TABBED_PANE_TAB_ALIGNMENT,
            FlatClientProperties.TABBED_PANE_ALIGN_LEADING
        )

        // 需要加载的模块
        val tabbedModules = tabbedProdiver.orderedStream().toList()
        if (tabbedModules.isNotEmpty()) {
            for (module in tabbedModules) {
                log.info { "load module: ${module.title}" }
                module.mainPanel.border = BorderFactory.createEmptyBorder(5, 5, 0, 5)
                tabbedPane.addTab(module.title, module.icon, module.mainPanel, module.tip)
            }
            tabbedPane.addChangeListener(object : ChangeListener {
                private var lastIdx = 0

                override fun stateChanged(e: ChangeEvent) {
                    val tabbed = e.source as JTabbedPane
                    val selectIdx = tabbed.selectedIndex

                    if (selectIdx != lastIdx) {
                        tabbedModules[lastIdx].selectedChange(false)
                        tabbedModules[selectIdx].selectedChange(true)
                        lastIdx = selectIdx
                    }
                }
            })
            tabbedModules[0].selectedChange(true)
        }

        // 底部消息组件
        val bottomPanel = messageNotifierPanel.panel

        val mainPanel = JPanel(BorderLayout())
        mainPanel.add(tabbedPane)
        mainPanel.add(bottomPanel, BorderLayout.SOUTH)

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
