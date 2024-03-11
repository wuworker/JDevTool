package com.wxl.jdevtool.json.listener

import com.jayway.jsonpath.Configuration
import com.jayway.jsonpath.JsonPath
import com.jayway.jsonpath.Option
import com.wxl.jdevtool.ComponentListener
import com.wxl.jdevtool.json.JsonTabbedModule
import com.wxl.jdevtool.json.JsonUtils
import com.wxl.jdevtool.toast.ToastType
import com.wxl.jdevtool.toast.Toasts
import com.wxl.jdevtool.validate.InputValidateGroup
import org.springframework.beans.factory.annotation.Autowired
import java.awt.event.ActionEvent
import java.awt.event.ActionListener

/**
 * Create by wuxingle on 2024/01/23
 * json求值
 */
@ComponentListener("jsonTabbedModule.getValBtn")
class JsonValueGetActionListener(
    @Autowired val jsonTabbedModule: JsonTabbedModule
) : ActionListener {

    private val conf = Configuration.defaultConfiguration()
        .addOptions(Option.DEFAULT_PATH_LEAF_TO_NULL)
        .addOptions(Option.ALWAYS_RETURN_LIST)
        .addOptions(Option.SUPPRESS_EXCEPTIONS)

    override fun actionPerformed(e: ActionEvent?) {
        if (!InputValidateGroup(jsonTabbedModule.leftInChecker, jsonTabbedModule.jsonPathChecker).check(true)) {
            return
        }

        val path = jsonTabbedModule.jsonPathField.text
        val json = jsonTabbedModule.leftTextArea.text

        val dc = try {
            JsonPath.using(conf).parse(json)
        } catch (e: Exception) {
            jsonTabbedModule.leftInChecker.showWarn()
            Toasts.show(ToastType.ERROR, "Json格式错误:${e.message}")
            return
        }

        val resultText = try {
            val result = dc.read<List<String?>>(path)
            if (result.isEmpty() || (result.size == 1 && result[0] == null)) {
                "null"
            } else {
                // 格式化result
                JsonUtils.formatJson(result.toString())
            }
        } catch (e: Exception) {
            jsonTabbedModule.jsonPathChecker.showWarn()
            Toasts.show(ToastType.ERROR, "JsonPath格式错误:${e.message}")
            return
        }
        jsonTabbedModule.leftInChecker.showNormal()
        jsonTabbedModule.jsonPathChecker.showNormal()

        jsonTabbedModule.rightTextArea.text = resultText
        jsonTabbedModule.showRight()
    }
}