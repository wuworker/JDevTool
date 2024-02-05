package com.wxl.jdevtool.collection.listener

import com.wxl.jdevtool.ComponentListener
import com.wxl.jdevtool.listener.TextAreaCaretLocationShowListener
import com.wxl.jdevtool.message.MessageNotifier
import org.springframework.beans.factory.annotation.Autowired

/**
 * Create by wuxingle on 2024/01/08
 * 显示文本域光标定位信息
 */
@ComponentListener(
    "collectionTabbedModule.leftTextArea1",
    "collectionTabbedModule.leftTextArea2",
    "collectionTabbedModule.rightTextArea"
)
class CollectionTextAreaCaretShowListener(
    @Autowired private val messageNotifier: MessageNotifier
) : TextAreaCaretLocationShowListener(messageNotifier)


