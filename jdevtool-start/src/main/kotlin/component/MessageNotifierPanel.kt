package com.wxl.jdevtool.component

import com.wxl.jdevtool.message.MessageNotifier
import org.springframework.stereotype.Component
import java.awt.Font
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.Insets
import javax.swing.BorderFactory
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.SwingConstants

/**
 * Create by wuxingle on 2024/01/24
 * 消息栏
 */
@Component
class MessageNotifierPanel : MessageNotifier {

    final val panel: JPanel

    private val msgLabel: JLabel

    private val caretLabel: JLabel

    init {
        val gridBagLayout = GridBagLayout()
        gridBagLayout.columnWeights = doubleArrayOf(1.0, 0.0)

        panel = JPanel(gridBagLayout)
        panel.border = BorderFactory.createLoweredBevelBorder()

        // 消息区
        msgLabel = JLabel("-")
        with(msgLabel) {
            horizontalAlignment = SwingConstants.CENTER
            font = Font(font.name, font.style, (font.size / 1.1).toInt())
        }
        var c = getGridBagConstraints()
        with(c) {
            fill = GridBagConstraints.BOTH
            insets = Insets(2, 100, 2, 50)
        }
        gridBagLayout.setConstraints(msgLabel, c)

        // 光标定位区
        caretLabel = JLabel()
        with(caretLabel) {
            horizontalAlignment = SwingConstants.CENTER
            font = Font(font.name, font.style, (font.size / 1.1).toInt())
        }
        c = getGridBagConstraints()
        with(c) {
            fill = GridBagConstraints.BOTH
            insets = Insets(2, 10, 2, 10)
        }
        gridBagLayout.setConstraints(caretLabel, c)

        panel.add(msgLabel)
        panel.add(caretLabel)
    }

    private fun getGridBagConstraints(): GridBagConstraints {
        return GridBagConstraints()
    }

    /**
     * 显示光标信息
     */
    override fun showMouseCaret(text: String) {
        caretLabel.text = text
    }

    /**
     * 显示底部消息
     */
    override fun showMessage(text: String) {
        msgLabel.text = text
    }
}
