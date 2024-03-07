package com.wxl.jdevtool.toast

import com.formdev.flatlaf.ui.FlatUIUtils
import com.formdev.flatlaf.util.Animator
import com.formdev.flatlaf.util.Animator.TimingTarget
import com.formdev.flatlaf.util.UIScale
import com.wxl.jdevtool.toast.ui.ToastNotificationPanel
import com.wxl.jdevtool.toast.util.NotificationHolder
import com.wxl.jdevtool.toast.util.UIUtils
import java.awt.*
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
import java.awt.event.ComponentListener
import java.util.function.Consumer
import javax.swing.JComponent
import javax.swing.JFrame
import javax.swing.JWindow
import javax.swing.Timer

/**
 * Create by wuxingle on 2024/03/05
 * toast实现
 */
class Notifications {

    private var frame: JFrame? = null

    private val lists = hashMapOf<ToastLocation, MutableList<NotificationAnimation>>()

    private val notificationHolder = NotificationHolder()

    private var windowEvent: ComponentListener = object : ComponentAdapter() {
        override fun componentMoved(e: ComponentEvent) {
            move(frame!!.bounds)
        }

        override fun componentResized(e: ComponentEvent) {
            move(frame!!.bounds)
        }
    }

    fun setFrame(frame: JFrame?) = installEvent(frame)

    /**
     * 监听窗口变化
     */
    private fun installEvent(frame: JFrame?) {
        this.frame?.removeComponentListener(windowEvent)
        this.frame = frame
        frame?.addComponentListener(windowEvent)
    }

    /**
     * toast位置调整
     */
    @Synchronized
    private fun move(rectangle: Rectangle) {
        for ((_, value) in lists) {
            value.forEach { it.move(rectangle) }
        }
    }

    /**
     * 显示toast
     */
    fun show(
        type: ToastType,
        message: String,
        location: ToastLocation = ToastLocation.TOP_CENTER,
        duration: Long = FlatUIUtils.getUIInt(ToastProperties.TOAST_DURATION, 2500).toLong()
    ) {
        initStart(NotificationAnimation(type, location, duration, message), duration)
    }

    fun show(
        component: JComponent,
        location: ToastLocation = ToastLocation.TOP_CENTER,
        duration: Long = FlatUIUtils.getUIInt(ToastProperties.TOAST_DURATION, 2500).toLong()
    ) {
        initStart(NotificationAnimation(location, duration, component), duration)
    }

    /**
     * 启动toast
     */
    @Synchronized
    private fun initStart(notificationAnimation: NotificationAnimation, duration: Long): Boolean {
        val limit = FlatUIUtils.getUIInt(ToastProperties.TOAST_LIMIT, -1)
        // 小于限制个数才启动
        return if (limit == -1 || getCurrentShowCount(notificationAnimation.location) < limit) {
            notificationAnimation.start()
            true
        } else {
            notificationHolder.hold(notificationAnimation)
            false
        }
    }

    /**
     * 当前位置已展示个数
     */
    private fun getCurrentShowCount(location: ToastLocation) = lists[location]?.size ?: 0

    /**
     * 关闭toast
     */
    @Synchronized
    private fun notificationClose(notificationAnimation: NotificationAnimation) {
        val hold = notificationHolder.getHold(notificationAnimation.location)
        if (hold != null) {
            if (initStart(hold, hold.duration)) {
                notificationHolder.removeHold(hold)
            }
        }
    }

    /**
     * 关闭所有
     */
    fun clearAll() {
        notificationHolder.clearHold()
        for ((_, value) in lists) {
            value.forEach { it.close() }
        }
    }

    /**
     * 关闭该位置所有
     */
    fun clear(location: ToastLocation) {
        notificationHolder.clearHold(location)
        val list = lists[location]
        list?.forEach { it.close() }
    }

    /**
     * 创建toast panel
     */
    protected fun createNotification(type: ToastType, message: String): ToastNotificationPanel {
        val toastNotificationPanel = ToastNotificationPanel()
        toastNotificationPanel.set(type, message)
        return toastNotificationPanel
    }

    @Synchronized
    private fun updateList(key: ToastLocation, values: NotificationAnimation, add: Boolean) {
        if (add) {
            if (lists.containsKey(key)) {
                lists[key]!!.add(values)
            } else {
                val list = arrayListOf<NotificationAnimation>()
                list.add(values)
                lists[key] = list
            }
        } else {
            if (lists.containsKey(key)) {
                lists[key]!!.remove(values)
                if (lists[key]!!.isEmpty()) {
                    lists.remove(key)
                }
            }
        }
    }

    inner class NotificationAnimation {
        private val window: JWindow
        private var animator: Animator? = null
        private var show = true
        private var animate = 0f
        private var x = 0
        private var y = 0
        val location: ToastLocation
        val duration: Long
        private lateinit var frameInsets: Insets
        private var horizontalSpace = 0
        private var animationMove = 0
        private var top = false
        private var close = false

        constructor (type: ToastType, location: ToastLocation, duration: Long, message: String) {
            installDefault()
            this.location = location
            this.duration = duration
            this.window = JWindow(frame)

            val toastNotificationPanel = createNotification(type, message)
            toastNotificationPanel.putClientProperty(ToastProperties.TOAST_CLOSE_CALLBACK,
                Consumer { o: Any? -> close() })

            with(window) {
                contentPane = toastNotificationPanel
                focusableWindowState = false
                pack()
            }
            toastNotificationPanel.setDialog(window)
        }

        constructor (location: ToastLocation, duration: Long, component: JComponent) {
            installDefault()
            this.location = location
            this.duration = duration
            this.window = JWindow(frame)
            with(window) {
                background = Color(0, 0, 0, 0)
                contentPane = component
                focusableWindowState = false
                size = component.preferredSize
            }
        }

        private fun installDefault() {
            frameInsets = UIUtils.getInsets(ToastProperties.TOAST_FRAME_INSETS, Insets(10, 10, 10, 10))
            horizontalSpace = FlatUIUtils.getUIInt(ToastProperties.TOAST_HORIZONTAL_GAP, 10)
            animationMove = FlatUIUtils.getUIInt(ToastProperties.TOAST_ANIMATION_MOVE, 10)
        }

        fun start() {
            val animation = FlatUIUtils.getUIInt(ToastProperties.TOAST_ANIMATION, 200)
            val resolution = FlatUIUtils.getUIInt(ToastProperties.TOAST_ANIMATION_RESOLUTION, 5)
            val animator = Animator(animation, object : TimingTarget {
                override fun begin() {
                    if (show) {
                        updateList(location, this@NotificationAnimation, true)
                        installLocation()
                    }
                }

                override fun timingEvent(f: Float) {
                    animate = if (show) f else 1f - f
                    updateLocation(true)
                }

                override fun end() {
                    if (show && !close) {
                        Timer(duration.toInt()) {
                            if (!close) {
                                show = false
                                animator!!.start()
                            }
                        }.start()
                    } else {
                        updateList(location, this@NotificationAnimation, false)
                        window.dispose()
                        notificationClose(this@NotificationAnimation)
                    }
                }
            })
            animator.resolution = resolution
            animator.start()
            this.animator = animator
        }

        private fun installLocation() {
            val insets: Insets
            val rec: Rectangle
            val frame = this@Notifications.frame
            if (frame == null) {
                insets = UIScale.scale(frameInsets)
                rec = Rectangle(Point(0, 0), Toolkit.getDefaultToolkit().screenSize)
            } else {
                insets = UIScale.scale(FlatUIUtils.addInsets(frameInsets, frame.insets))
                rec = frame.bounds
            }
            setupLocation(rec, insets)
            window.opacity = 0f
            window.isVisible = true
        }

        fun move(rec: Rectangle) {
            val insets = UIScale.scale(FlatUIUtils.addInsets(frameInsets, frame!!.insets))
            setupLocation(rec, insets)
        }

        private fun setupLocation(rec: Rectangle, insets: Insets) {
            when (location) {
                ToastLocation.TOP_LEFT -> {
                    x = rec.x + insets.left
                    y = rec.y + insets.top
                    top = true
                }

                ToastLocation.TOP_CENTER -> {
                    x = rec.x + (rec.width - window.width) / 2
                    y = rec.y + insets.top
                    top = true
                }

                ToastLocation.TOP_RIGHT -> {
                    x = rec.x + rec.width - (window.width + insets.right)
                    y = rec.y + insets.top
                    top = true
                }

                ToastLocation.BOTTOM_LEFT -> {
                    x = rec.x + insets.left
                    y = rec.y + rec.height - (window.height + insets.bottom)
                    top = false
                }

                ToastLocation.BOTTOM_CENTER -> {
                    x = rec.x + (rec.width - window.width) / 2
                    y = rec.y + rec.height - (window.height + insets.bottom)
                    top = false
                }

                ToastLocation.BOTTOM_RIGHT -> {
                    x = rec.x + rec.width - (window.width + insets.right)
                    y = rec.y + rec.height - (window.height + insets.bottom)
                    top = false
                }
            }
            val am = UIScale.scale(if (top) animationMove else -animationMove)
            val ly: Int = (getLocation(this@NotificationAnimation) + y + animate * am).toInt()
            window.setLocation(x, ly)
        }

        private fun updateLocation(loop: Boolean) {
            val am = UIScale.scale(if (top) animationMove else -animationMove)
            val ly = (getLocation(this@NotificationAnimation) + y + animate * am).toInt()
            window.setLocation(x, ly)
            window.opacity = animate
            if (loop) {
                update(this)
            }
        }

        private fun getLocation(notification: NotificationAnimation): Int {
            var height = 0
            val list: List<NotificationAnimation>? = lists[location]
            if (list.isNullOrEmpty()) {
                return height
            }

            for (n in list) {
                if (notification === n) {
                    return height
                }
                val v = n.animate * (n.window.height + UIScale.scale(horizontalSpace))
                height += (if (top) v else -v).toInt()
            }
            return height
        }

        private fun update(except: NotificationAnimation) {
            val list: List<NotificationAnimation?>? = lists[location]
            list?.forEach {
                if (it !== except) {
                    it?.updateLocation(false)
                }
            }
        }

        fun close() {
            close = true
            show = false
            val animator = this.animator
            if (animator != null) {
                if (animator.isRunning) {
                    animator.stop()
                }
                animator.start()
            }
        }
    }

}