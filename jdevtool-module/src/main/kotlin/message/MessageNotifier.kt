package com.wxl.jdevtool.message

/**
 * Create by wuxingle on 2024/01/24
 * 底部消息通知
 */
interface MessageNotifier {

    /**
     * 显示光标信息
     */
    fun showMouseCaret(text: String)

    /**
     * 显示底部消息
     */
    fun showMessage(text: String)
}
