package com.wxl.jdevtool.toast.util

import com.wxl.jdevtool.toast.Notifications
import com.wxl.jdevtool.toast.ToastLocation

/**
 * Create by wuxingle on 2024/03/05
 */
class NotificationHolder {

    private val lists = arrayListOf<Notifications.NotificationAnimation>()

    fun getHold(location: ToastLocation): Notifications.NotificationAnimation? {
        synchronized(this) {
            for (i in lists.indices) {
                val n = lists[i]
                if (n.location == location) {
                    return n
                }
            }
            return null
        }
    }

    fun removeHold(notificationAnimation: Notifications.NotificationAnimation) {
        synchronized(this) { lists.remove(notificationAnimation) }
    }

    fun hold(notificationAnimation: Notifications.NotificationAnimation) {
        synchronized(this) { lists.add(notificationAnimation) }
    }

    fun clearHold() {
        synchronized(this) { lists.clear() }
    }

    fun clearHold(location: ToastLocation) {
        synchronized(this) {
            var i = 0
            while (i < lists.size) {
                val n = lists[i]
                if (n.location == location) {
                    lists.remove(n)
                    i--
                }
                i++
            }
        }
    }
}