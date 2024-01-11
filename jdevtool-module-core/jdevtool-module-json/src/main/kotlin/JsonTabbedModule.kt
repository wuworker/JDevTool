package com.wxl.jdevtool.json

import com.formdev.flatlaf.extras.FlatSVGIcon
import com.wxl.jdevtool.ComponentId
import com.wxl.jdevtool.TabbedModule
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import javax.swing.JPanel

/**
 * Create by wuxingle on 2024/1/11
 * json处理
 */
@Order(300)
@Component
@ComponentId("jsonTabbedModule")
class JsonTabbedModule : TabbedModule {

    private val log = KotlinLogging.logger { }

    final override val mainPanel: JPanel

    init {
        mainPanel = JPanel()
    }

    override val title = "JSON处理"

    override val icon = FlatSVGIcon("icons/json_dark.svg")

    override val tip = "JSON处理"
}
