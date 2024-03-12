package com.wxl.jdevtool.message

import java.util.*


/**
 * Create by wuxingle on 2024/03/12
 * 消息栏
 */
interface MessageBarListener : EventListener {

    /**
     * 显示光标信息
     */
    fun showMouseCaret(text: String)

    /**
     * 显示底部消息
     */
    fun showMessage(text: String)
}
