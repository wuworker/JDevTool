package com.wxl.jdevtool.validate

import javax.swing.text.JTextComponent

/**
 * Create by wuxingle on 2024/03/07
 * 输入校验
 */
interface InputValidate {

    /**
     * 校验的组件
     */
    val component: JTextComponent

    /**
     * 检查
     * @param focus 校验失败是否聚焦
     */
    fun check(focus: Boolean = true): Boolean

}
