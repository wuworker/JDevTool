package com.wxl.jdevtool.configuration

import com.wxl.jdevtool.AppContexts
import com.wxl.jdevtool.component.JDevToolMainPanel
import com.wxl.jdevtool.util.ComponentUtils
import org.springframework.boot.ConfigurableBootstrapContext
import org.springframework.boot.SpringApplicationRunListener
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.core.env.ConfigurableEnvironment
import java.awt.BorderLayout
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.time.Duration
import javax.swing.BorderFactory
import javax.swing.JPanel
import javax.swing.JProgressBar
import javax.swing.WindowConstants

/**
 * Create by wuxingle on 2024/04/28
 * springboot事件
 */
class JDevToolApplicationRunListener : SpringApplicationRunListener {

    private val progress = JProgressBar(0, 100)

    override fun starting(bootstrapContext: ConfigurableBootstrapContext) {
        progress.isStringPainted = true
        progress.string = "初始化: 10%"
        progress.value = 10

        val panel = JPanel(GridBagLayout())
        panel.border = BorderFactory.createEmptyBorder(50, 50, 50, 50)
        panel.add(
            progress, ComponentUtils.createConstraints(
                anchor = GridBagConstraints.CENTER,
                weightx = 1.0,
                weighty = 1.0,
                fill = GridBagConstraints.HORIZONTAL
            )
        )

        val frame = AppContexts.mainFrame
        with(frame) {
            layout = BorderLayout()
            contentPane = panel
            title = "JDevTool"
            defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
            setSize(800, 1600)
            setLocationRelativeTo(null)
            isVisible = true
        }
    }

    override fun environmentPrepared(
        bootstrapContext: ConfigurableBootstrapContext,
        environment: ConfigurableEnvironment
    ) {
        AppContexts.environment = environment

        progress.string = "环境准备完成: 20%"
        progress.value = 20
    }

    override fun contextPrepared(context: ConfigurableApplicationContext) {
        progress.string = "上下文准备完成: 40%"
        progress.value = 40
    }

    override fun contextLoaded(context: ConfigurableApplicationContext) {
        AppContexts.context = context

        progress.string = "上下文加载完成，准备启动: 60%"
        progress.value = 60
    }

    /**
     * 启动，已经刷新完毕
     */
    override fun started(context: ConfigurableApplicationContext, timeTaken: Duration) {
        progress.string = "已就绪: 90%"
        progress.value = 90

        context.getBean(JDevToolInitializer::class.java).init(context)
    }

    /**
     * 运行
     */
    override fun ready(context: ConfigurableApplicationContext, timeTaken: Duration) {
        progress.string = "完成: 100%"
        progress.value = 100

        val mainPanel = context.getBean(JDevToolMainPanel::class.java)
        val frame = AppContexts.mainFrame
        frame.contentPane = mainPanel
        frame.repaint()
    }

    override fun failed(context: ConfigurableApplicationContext, exception: Throwable) {

    }
}
