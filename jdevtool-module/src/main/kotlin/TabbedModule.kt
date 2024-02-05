package com.wxl.jdevtool

import javax.swing.Icon
import javax.swing.JPanel

/**
 * Create by wuxingle on 2024/01/02
 * tab模块
 */
interface TabbedModule {

    /**
     * 初始化
     */
    fun init() {}

    /**
     * 选中状态变化
     */
    fun selectedChange(select: Boolean) {}

    /**
     * 获取主panel
     */
    val mainPanel: JPanel

    /**
     * 模块标题
     */
    val title: String

    /**
     * 模块图标
     */
    val icon: Icon?

    /**
     * 模块提示
     */
    val tip: String?
}
