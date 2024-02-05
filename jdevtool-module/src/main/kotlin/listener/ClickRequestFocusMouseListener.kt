package com.wxl.jdevtool.listener

import java.awt.Component
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent

/**
 * Create by wuxingle on 2024/02/02
 * 组件被点击获取焦点
 */
class ClickRequestFocusMouseListener : MouseAdapter() {

    override fun mousePressed(e: MouseEvent) {
        val component = e.source as? Component ?: return
        component.requestFocus()
    }
}
