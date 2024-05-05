package com.wxl.jdevtool

import com.wxl.jdevtool.theme.AppTheme
import org.springframework.context.ApplicationContext
import org.springframework.core.env.Environment
import javax.swing.JFrame

/**
 * Create by wuxingle on 2024/04/28
 * 应用上下文
 */
object JDevlToolContexts {

    /**
     * 主题
     */
    lateinit var theme: AppTheme

    /**
     * 主窗口
     */
    lateinit var mainFrame: JFrame

    /**
     * spring context
     */
    lateinit var context: ApplicationContext

    /**
     * 环境
     */
    lateinit var environment: Environment


    fun getProperty(key: String, def: String? = ""): String? {
        val value = environment.getProperty(key)
        return value ?: def
    }

    fun <T> getProperty(key: String, targetClass: Class<T>, def: T? = null): T? {
        val value = environment.getProperty(key, targetClass)
        return value ?: def
    }

    fun getRequireProperty(key: String): String {
        return environment.getRequiredProperty(key)
    }

    fun <T> getRequireProperty(key: String, targetClass: Class<T>): T {
        return environment.getRequiredProperty(key, targetClass)
    }

}

