package com.wxl.jdevtool

/**
 * Create by wuxingle on 2024/01/05
 * 同一分组组件id全局唯一
 * 注释在字段时，该字段id = 类的组件id + 当前设置的id
 */
@MustBeDocumented
@Target(AnnotationTarget.FIELD, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class ComponentId(
    /**
     * id
     */
    val value: String
)
