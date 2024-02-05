package com.wxl.jdevtool.txt.listener

import com.wxl.jdevtool.ComponentListener
import com.wxl.jdevtool.txt.TxtTabbedModule
import org.springframework.beans.factory.annotation.Autowired
import java.awt.event.ActionEvent
import java.awt.event.ActionListener

/**
 * Create by wuxingle on 2024/01/08
 * 清空输入内容
 */
@ComponentListener("txtTabbedModule.originClearBtn")
class TxtOriginClearBtnActionListener(
    @Autowired val txtTabbedModule: TxtTabbedModule
) : ActionListener {

    override fun actionPerformed(e: ActionEvent?) {
        with(txtTabbedModule) {
            originSplitText.clearText()
            originItemPreText.clearText()
            originItemPostText.clearText()
            originPreText.clearText()
            originPostText.clearText()
        }
    }
}

@ComponentListener("txtTabbedModule.targetClearBtn")
class TxtTargetClearBtnActionListener(
    @Autowired val txtTabbedModule: TxtTabbedModule
) : ActionListener {

    override fun actionPerformed(e: ActionEvent?) {
        with(txtTabbedModule) {
            targetSplitText.clearText()
            targetItemPreText.clearText()
            targetItemPostText.clearText()
            targetPreText.clearText()
            targetPostText.clearText()
        }
    }
}