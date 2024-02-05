package com.wxl.jdevtool.json

import org.springframework.core.io.ClassPathResource
import java.awt.BorderLayout
import java.io.Serial
import java.nio.charset.StandardCharsets
import javax.swing.BorderFactory
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JScrollPane

/**
 * Create by wuxingle on 2024/01/30
 * jsonPath说明panel
 */
class JsonPathDescPanel : JPanel(BorderLayout()) {

    init {
        border = BorderFactory.createEmptyBorder(10, 10, 10, 10)
        val label = JLabel(getIntroduceText())
        label.border = BorderFactory.createEmptyBorder()
        val scrollPane = JScrollPane(label)
        scrollPane.border = BorderFactory.createEmptyBorder()

        add(scrollPane)
    }

    private fun getIntroduceText(): String {
        val resource = ClassPathResource("jsonPath_introduce.html")
        return resource.getContentAsString(StandardCharsets.UTF_8)
    }

    companion object {
        @Serial
        private const val serialVersionUID: Long = -8255051074394404891L

    }
}