package com.wxl.jdevtool.json.listener

import com.wxl.jdevtool.ComponentListener
import com.wxl.jdevtool.Icons
import com.wxl.jdevtool.json.JsonPathDescPanel
import java.awt.event.*
import javax.swing.JButton
import javax.swing.JDialog

/**
 * Create by wuxingle on 2024/01/30
 * jsonPath说明button
 */
@ComponentListener("jsonTabbedModule.jsonPathDescBtn")
class JsonPathDescBtnMouseListener : MouseAdapter() {

    override fun mouseEntered(e: MouseEvent) {
        val btn = e.source as? JButton ?: return
        btn.icon = Icons.helpHover
    }

    override fun mouseExited(e: MouseEvent) {
        val btn = e.source as? JButton ?: return
        btn.icon = Icons.help
    }
}

@ComponentListener("jsonTabbedModule.jsonPathDescBtn")
class JsonPathDescBtnActionListener : ActionListener {

    override fun actionPerformed(e: ActionEvent) {
        val btn = e.source as? JButton ?: return

        // 说明窗口
        val dialog = JDialog()
        with(dialog) {
            setSize(600, 500)
            title = "JsonPath说明"
            defaultCloseOperation = JDialog.DISPOSE_ON_CLOSE
            addWindowListener(object : WindowAdapter() {
                override fun windowClosed(e: WindowEvent?) {
                    btn.isEnabled = true
                }
            })
            contentPane = JsonPathDescPanel()
            setLocationRelativeTo(null)

            isVisible = true
        }

        btn.isEnabled = false
    }


}
