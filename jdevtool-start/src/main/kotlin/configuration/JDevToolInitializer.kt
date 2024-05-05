package com.wxl.jdevtool.configuration

import org.springframework.context.ApplicationContext

/**
 * Create by wuxingle on 2024/01/08
 * JDevlTool启动流程
 * 容器refresh完毕后执行
 */
interface JDevToolInitializer {

    /**
     * 初始化
     */
    fun init(context: ApplicationContext) {
        initComponentRegistry(context)
        initComponentBind(context)
        initTheme(context)
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

