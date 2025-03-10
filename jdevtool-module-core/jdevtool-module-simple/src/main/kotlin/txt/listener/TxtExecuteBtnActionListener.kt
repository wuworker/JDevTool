package com.wxl.jdevtool.txt.listener

import com.wxl.jdevtool.ComponentListener
import com.wxl.jdevtool.extension.getUnescapeText
import com.wxl.jdevtool.txt.TxtTabbedModule
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import java.awt.event.ActionEvent
import java.awt.event.ActionListener

/**
 * Create by wuxingle on 2024/01/08
 * 生成内容
 */
@ComponentListener("txtTabbedModule.executeBtn")
class TxtExecuteBtnActionListener(
    @Autowired val txtTabbedModule: TxtTabbedModule,
) : ActionListener {

    private val log = KotlinLogging.logger { }

    override fun actionPerformed(e: ActionEvent?) {
        // 获取输入内容
        var input = txtTabbedModule.leftTextArea.text
        if (!txtTabbedModule.check()) {
            return
        }

        // 去掉前缀，后缀
        input = trimPrePost(
            input,
            txtTabbedModule.originPreText.getUnescapeText(),
            txtTabbedModule.originPostText.getUnescapeText()
        )

        // 按分隔符分割，去掉每项前后缀
        var splitTxt = txtTabbedModule.originSplitText.getUnescapeText()
        if (splitTxt.isEmpty()) {
            splitTxt = "\n"
        }
        val list = input.split(splitTxt)
            .filter { it.isNotBlank() }
            .map {
                trimPrePost(
                    it,
                    txtTabbedModule.originItemPreText.getUnescapeText(),
                    txtTabbedModule.originItemPostText.getUnescapeText()
                )
            }
        log.debug { "input to list :$list" }

        // 按输出组装
        val output = list.joinToString(
            txtTabbedModule.targetSplitText.getUnescapeText(),
            txtTabbedModule.targetPreText.getUnescapeText(),
            txtTabbedModule.targetPostText.getUnescapeText()
        ) {
            "${txtTabbedModule.targetItemPreText.getUnescapeText()}$it${txtTabbedModule.targetItemPostText.getUnescapeText()}"
        }

        log.debug { "output is :$output" }
        txtTabbedModule.rightTextArea.text = output
    }


    private fun trimPrePost(input: String, prefix: String?, postfix: String?): String {
        if (input.isBlank()) {
            return input
        }
        var result = input
        if (!prefix.isNullOrEmpty() && result.startsWith(prefix)) {
            result = result.substring(prefix.length)
        }
        if (!postfix.isNullOrEmpty() && result.endsWith(postfix)) {
            result = result.substring(0, result.length - postfix.length)
        }

        return result
    }
}
