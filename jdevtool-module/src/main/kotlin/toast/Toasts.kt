package com.wxl.jdevtool.toast

import com.formdev.flatlaf.ui.FlatUIUtils
import javax.swing.JComponent
import javax.swing.JFrame

/**
 * Create by wuxingle on 2024/03/07
 * toast调用入口
 */
object Toasts {

    private val notifications = Notifications()

    fun setMainFrame(frame: JFrame) {
        notifications.setFrame(frame)
    }

    /**
     * 显示toast
     */
    fun show(
        type: ToastType,
        message: String,
        location: ToastLocation = ToastLocation.TOP_RIGHT,
        duration: Long = FlatUIUtils.getUIInt(ToastProperties.TOAST_DURATION, 1500).toLong()
    ) {
        notifications.show(type, message, location, duration)
    }

    /**
     * 显示toast
     */
    fun show(
        component: JComponent,
        location: ToastLocation = ToastLocation.TOP_RIGHT,
        duration: Long = FlatUIUtils.getUIInt(ToastProperties.TOAST_DURATION, 1500).toLong()
    ) {
        notifications.show(component, location, duration)
    }

    /**
     * 清空弹框
     */
    fun clear(location: ToastLocation? = null) {
        if (location == null) {
            notifications.clearAll()
        } else {
            notifications.clear(location)
        }
    }
}
