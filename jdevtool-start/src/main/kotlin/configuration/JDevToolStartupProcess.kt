package com.wxl.jdevtool.configuration

import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent

/**
 * Create by wuxingle on 2024/01/08
 * JDevlTool启动流程
 * 容器refresh完毕时执行
 */
interface JDevToolStartupProcess : ApplicationListener<ContextRefreshedEvent> {

    override fun onApplicationEvent(event: ContextRefreshedEvent) {
        if (event.applicationContext.parent == null) {

            initComponentRegistry(event.applicationContext)

            initComponentBind(event.applicationContext)

            initTheme(event.applicationContext)
        }
    }

    /**
     * 初始化组件注册中心
     */
    fun initComponentRegistry(context: ApplicationContext)

    /**
     * 初始化组件绑定
     */
    fun initComponentBind(context: ApplicationContext)

    /**
     * 初始化主题
     */
    fun initTheme(context: ApplicationContext)
}

