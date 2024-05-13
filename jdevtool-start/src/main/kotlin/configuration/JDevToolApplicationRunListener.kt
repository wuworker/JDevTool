package com.wxl.jdevtool.configuration

import com.wxl.jdevtool.AppContexts
import com.wxl.jdevtool.component.JDevToolMainPanel
import org.springframework.boot.ConfigurableBootstrapContext
import org.springframework.boot.SpringApplicationRunListener
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.core.env.ConfigurableEnvironment
import java.awt.BorderLayout
import java.time.Duration
import javax.swing.WindowConstants

/**
 * Create by wuxingle on 2024/04/28
 * springboot事件
 */
class JDevToolApplicationRunListener : SpringApplicationRunListener {

    override fun starting(bootstrapContext: ConfigurableBootstrapContext) {

    }

    override fun environmentPrepared(
        bootstrapContext: ConfigurableBootstrapContext,
        environment: ConfigurableEnvironment
    ) {
        AppContexts.environment = environment
    }

    override fun contextPrepared(context: ConfigurableApplicationContext) {

    }

    override fun contextLoaded(context: ConfigurableApplicationContext) {

    }

    /**
     * 启动，已经刷新完毕
     */
    override fun started(context: ConfigurableApplicationContext, timeTaken: Duration) {
        AppContexts.context = context

        context.getBean(JDevToolInitializer::class.java).init(context)
    }

    /**
     * 运行
     */
    override fun ready(context: ConfigurableApplicationContext, timeTaken: Duration) {
        val mainPanel = context.getBean(JDevToolMainPanel::class.java)
        val frame = AppContexts.mainFrame
        with(frame) {
            layout = BorderLayout()
            contentPane = mainPanel
            title = "JDevTool"
            defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
            setSize(800, 800)
            isVisible = true
        }
    }

    override fun failed(context: ConfigurableApplicationContext, exception: Throwable) {

    }
}
