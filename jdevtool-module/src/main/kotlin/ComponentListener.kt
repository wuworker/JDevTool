package com.wxl.jdevtool

import org.springframework.stereotype.Component

/**
 * Create by wuxingle on 2024/01/05
 * 组件监听器，用于自动绑定组件id
 * 注解类必须实现[java.util.EventListener]
 */
@Component
@MustBeDocumented
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class ComponentListener(
    /**
     * 要绑定的组件id列表
     */
    vararg val ids: String
)
