package com.wxl.jdevtool.json.listener

import com.wxl.jdevtool.ComponentListener
import com.wxl.jdevtool.json.JsonTabbedModule
import com.wxl.jdevtool.json.JsonUtils
import com.wxl.jdevtool.toast.ToastType
import com.wxl.jdevtool.toast.Toasts
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
    @Autowired val jsonTabbedModule: JsonTabbedModule
) : ActionListener {

    override fun actionPerformed(e: ActionEvent?) {
        jsonTabbedModule.jsonPathChecker.showNormal()
        if (!jsonTabbedModule.leftInChecker.check()) {
            return
        }

        // 去掉另一个框
        jsonTabbedModule.hiddenRight()

        try {
            val text = jsonTabbedModule.leftTextArea.text
            jsonTabbedModule.leftTextArea.text = JsonUtils.formatJson(text)
            jsonTabbedModule.jsonPathChecker.showNormal()
            jsonTabbedModule.leftInChecker.showNormal()
        } catch (e: Exception) {
            jsonTabbedModule.leftInChecker.showWarn()
            Toasts.show(ToastType.ERROR, "Json格式错误")
        }
    }
}

@ComponentListener("jsonTabbedModule.compressBtn")
class JsonCompressActionListener(
    @Autowired val jsonTabbedModule: JsonTabbedModule
) : ActionListener {

    override fun actionPerformed(e: ActionEvent?) {
        jsonTabbedModule.jsonPathChecker.showNormal()
        if (!jsonTabbedModule.leftInChecker.check()) {
            return
        }
        // 去掉另一个框
        jsonTabbedModule.hiddenRight()

        try {
            val text = jsonTabbedModule.leftTextArea.text
            jsonTabbedModule.leftTextArea.text = Json.parseToJsonElement(text).toString()
            jsonTabbedModule.jsonPathChecker.showNormal()
            jsonTabbedModule.leftInChecker.showNormal()
        } catch (e: Exception) {
            jsonTabbedModule.leftInChecker.showWarn()
            Toasts.show(ToastType.ERROR, "Json格式错误")
        }
    }
}
