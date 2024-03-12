package com.wxl.jdevtool.message

import io.github.oshai.kotlinlogging.KotlinLogging
import javax.swing.event.EventListenerList

/**
 * Create by wuxingle on 2024/01/24
 * 底部消息通知
 */
object MessageBar {

    private val listeners = EventListenerList()

    private val log = KotlinLogging.logger { }

    /**
     * 增加监听
     */
    fun addListener(listener: MessageBarListener) {
        listeners.add(MessageBarListener::class.java, listener)
    }

    /**
     * 显示光标信息
     */
    fun showMouseCaret(text: String) {
        val list = listeners.getListeners(MessageBarListener::class.java)
        for (listener in list) {
            try {
                listener.showMouseCaret(text)
            } catch (e: Exception) {
                log.error(e) { "showMouseCaret error by ${listener::class.java.name}, text:$text" }
            }
        }
    }

    /**
     * 显示底部消息
     */
    fun showMessage(text: String) {
        val list = listeners.getListeners(MessageBarListener::class.java)
        for (listener in list) {
            try {
                listener.showMessage(text)
            } catch (e: Exception) {
                log.error(e) { "showMessage error by ${listener::class.java.name}, text:$text" }
            }
        }
    }
}
