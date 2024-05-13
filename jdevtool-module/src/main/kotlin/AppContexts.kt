package com.wxl.jdevtool

import com.wxl.jdevtool.theme.AppTheme
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.context.ApplicationContext
import org.springframework.core.env.Environment
import org.springframework.scheduling.concurrent.CustomizableThreadFactory
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import javax.swing.JFrame

/**
 * Create by wuxingle on 2024/04/28
 * 应用上下文
 */
object AppContexts {

    private val log = KotlinLogging.logger { }

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

    /**
     * 处理数据落库线程池
     */
    private val dbPool = ThreadPoolExecutor(
        1, 3,
        5, TimeUnit.MINUTES,
        ArrayBlockingQueue(100),
        CustomizableThreadFactory("jdevtool-db-")
    )

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

    fun <T> getMapper(clazz: Class<T>): T {
        return context.getBean(clazz)
    }

    /**
     * 异步执行sql
     */
    fun executeSql(runnable: Runnable) {
        dbPool.execute {
            try {
                runnable.run()
            } catch (e: Exception) {
                log.error(e) { "sql execute error" }
            }
        }
    }

    fun <T> executeSql(clazz: Class<T>, runnable: (T) -> Unit) {
        executeSql {
            runnable(getMapper(clazz))
        }
    }

}

