package com.wxl.jdevtool.json.listener

import com.wxl.jdevtool.ComponentListener
import com.wxl.jdevtool.listener.TextAreaCaretLocationShowListener
import com.wxl.jdevtool.message.MessageNotifier
import org.springframework.beans.factory.annotation.Autowired

/**
 * Create by wuxingle on 2024/01/24
 * 显示光标定位信息
 */
@ComponentListener(
    "jsonTabbedModule.leftTextArea",
    "jsonTabbedModule.rightTextArea"
)
class JsonTextAreaCaretShowListener(
    @Autowired private val messageNotifier: MessageNotifier
) : TextAreaCaretLocationShowListener(messageNotifier)

