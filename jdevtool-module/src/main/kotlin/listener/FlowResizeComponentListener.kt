package com.wxl.jdevtool.listener

import io.github.oshai.kotlinlogging.KotlinLogging
import java.awt.Container
import java.awt.Dimension
import java.awt.Point
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent

/**
 * Create by wuxingle on 2024/01/05
 * 用于panel
 * 动态调整flow布局的panel大小
 */
open class FlowResizeComponentListener : ComponentAdapter() {

    private val log = KotlinLogging.logger { }

    private val lastPointMap = hashMapOf<Container, Point>()

    override fun componentResized(e: ComponentEvent) {
        val panel = e.source as? Container ?: return
        val count = panel.componentCount
        if (count > 0) {
            val lastComponent = panel.getComponent(count - 1)
            val nowPoint = Point(
                lastComponent.x + lastComponent.size.width + 10,
                lastComponent.y + lastComponent.size.height + 10
            )

            if (lastPointMap[panel] != nowPoint) {
                log.info { "resize : $nowPoint" }
                lastPointMap[panel] = nowPoint

                panel.preferredSize = Dimension(nowPoint.x, nowPoint.y)
            }
        }
    }
}