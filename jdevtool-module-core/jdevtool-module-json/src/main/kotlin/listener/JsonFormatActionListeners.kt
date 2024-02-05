package com.wxl.jdevtool.json.listener

import com.wxl.jdevtool.ComponentListener
import com.wxl.jdevtool.json.JsonTabbedModule
import com.wxl.jdevtool.json.JsonUtils
import com.wxl.jdevtool.message.MessageNotifier
import kotlinx.serialization.json.Json
import org.springframework.beans.factory.annotation.Autowired
import java.awt.event.ActionEvent
import java.awt.event.ActionListener

/**
 * Create by wuxingle on 2024/01/23
 * json格式化监听
 */

@ComponentListener("jsonTabbedModule.expandBtn")
class JsonExpandActionListener(
    @Autowired val jsonTabbedModule: JsonTabbedModule,
    @Autowired val messageNotifier: MessageNotifier
) : ActionListener {

    override fun actionPerformed(e: ActionEvent?) {
        val text = jsonTabbedModule.leftTextArea.text
        if (text.isBlank()) {
            return
        }

        // 去掉另一个框
        jsonTabbedModule.hiddenRight()

        try {
            jsonTabbedModule.leftTextArea.text = JsonUtils.formatJson(text)
        } catch (e: Exception) {
            messageNotifier.showMessage("输入json格式错误")
        }
    }
}

@ComponentListener("jsonTabbedModule.compressBtn")
class JsonCompressActionListener(
    @Autowired val jsonTabbedModule: JsonTabbedModule,
    @Autowired val messageNotifier: MessageNotifier
) : ActionListener {

    override fun actionPerformed(e: ActionEvent?) {
        val text = jsonTabbedModule.leftTextArea.text
        if (text.isBlank()) {
            return
        }

        // 去掉另一个框
        jsonTabbedModule.hiddenRight()

        try {
            jsonTabbedModule.leftTextArea.text = Json.parseToJsonElement(text).toString()
        } catch (e: Exception) {
            messageNotifier.showMessage("输入json格式错误")
        }
    }
}
