package com.wxl.jdevtool.listener

import io.github.oshai.kotlinlogging.KotlinLogging
import java.awt.Container
import java.awt.Dimension
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent

/**
 * Create by wuxingle on 2024/01/05
 * 用于panel
 * 动态调整flow布局的panel大小
 */
open class FlowResizeComponentListener : ComponentAdapter() {

    private val log = KotlinLogging.logger { }

    private var lastW: Int = 0

    private var lastH: Int = 0

    override fun componentResized(e: ComponentEvent) {
        val panel = e.source as? Container ?: return

        val count = panel.componentCount
        if (count > 0) {
            val lastComponent = panel.getComponent(count - 1)

            val reW = lastComponent.x + lastComponent.size.width + 10
            val reH = lastComponent.y + lastComponent.size.height + 10

            if (reW != lastW || reH != lastH) {
                log.debug { "resize w: $reW, h: $reH" }

                lastW = reW
                lastH = reH

                panel.preferredSize = Dimension(reW, reH)
            }
        }
    }
}