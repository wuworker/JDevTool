package com.wxl.jdevtool.json.listener

import com.jayway.jsonpath.Configuration
import com.jayway.jsonpath.JsonPath
import com.jayway.jsonpath.Option
import com.wxl.jdevtool.ComponentListener
import com.wxl.jdevtool.json.JsonTabbedModule
import com.wxl.jdevtool.json.JsonUtils
import com.wxl.jdevtool.message.MessageNotifier
import org.springframework.beans.factory.annotation.Autowired
import java.awt.event.ActionEvent
import java.awt.event.ActionListener

/**
 * Create by wuxingle on 2024/01/23
 * json求值
 */
@ComponentListener("jsonTabbedModule.getValBtn")
class JsonValueGetActionListener(
    @Autowired val jsonTabbedModule: JsonTabbedModule,
    @Autowired val messageNotifier: MessageNotifier
) : ActionListener {

    private val conf = Configuration.defaultConfiguration()
        .addOptions(Option.DEFAULT_PATH_LEAF_TO_NULL)
        .addOptions(Option.ALWAYS_RETURN_LIST)
        .addOptions(Option.SUPPRESS_EXCEPTIONS)

    override fun actionPerformed(e: ActionEvent?) {
        val path = jsonTabbedModule.jsonPathField.text
        val json = jsonTabbedModule.leftTextArea.text

        if (path.isBlank() || json.isBlank()) {
            return
        }

        val resultText = try {
            val result = JsonPath.using(conf).parse(json).read<List<String?>>(path)
            if (result.isEmpty() || (result.size == 1 && result[0] == null)) {
                "null"
            } else {
                // 格式化result
                JsonUtils.formatJson(result.toString())
            }
        } catch (e: Exception) {
            messageNotifier.showMessage("输入内容格式错误")
            ""
        }

        jsonTabbedModule.rightTextArea.text = resultText
        jsonTabbedModule.showRight()
    }
}