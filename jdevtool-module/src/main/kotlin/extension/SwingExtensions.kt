package com.wxl.jdevtool.extension

import com.wxl.jdevtool.listener.ClickRequestFocusMouseListener
import java.awt.Component
import java.awt.Window

/**
 * Create by wuxingle on 2024/01/18
 * swing扩展函数
 */

/**
 * Window相对位置
 */
object RelativeLocation {
    const val LEFT = 1
    const val RIGHT = 2
    const val TOP = 3
    const val BOTTOM = 4

    // 居左，或居上
    const val ALIGN_LEFT = 1
    const val ALIGN_CENTER = 2

    // 居右，或居下
    const val ALIGN_RIGHT = 3
}

/**
 * 设置相当于parent组件的对齐方式
 * 需要先设置该窗口的size
 */
fun Window.setLocationRelativeTo(parent: Component, location: Int, align: Int = RelativeLocation.ALIGN_CENTER) {
    val parentPoint = parent.locationOnScreen
    val parentSize = parent.size

    var x = 0
    var y = 0
    if (location == RelativeLocation.TOP || location == RelativeLocation.BOTTOM) {
        y = when (location) {
            RelativeLocation.TOP -> parentPoint.y - size.height
            else -> parentPoint.y + parentSize.height
        }
        x = when (align) {
            RelativeLocation.ALIGN_LEFT -> parentPoint.x
            RelativeLocation.ALIGN_CENTER -> (parentPoint.x + parentSize.width / 2) - size.width / 2
            else -> parentPoint.x + parentSize.width - size.width
        }
    } else if (location == RelativeLocation.LEFT || location == RelativeLocation.RIGHT) {
        x = when (location) {
            RelativeLocation.LEFT -> parentPoint.x - size.width
            else -> parentPoint.x + parentSize.width
        }
        y = when (align) {
            RelativeLocation.ALIGN_LEFT -> parentPoint.y
            RelativeLocation.ALIGN_CENTER -> (parentPoint.y + parentSize.height / 2) - size.height / 2
            else -> parentPoint.y + parentSize.height - size.height
        }
    }

    setLocation(x, y)
}

/**
 * Component设置被点击自动获取焦点
 */
fun Component.enableClickRequestFocus() {
    addMouseListener(ClickRequestFocusMouseListener())
}

