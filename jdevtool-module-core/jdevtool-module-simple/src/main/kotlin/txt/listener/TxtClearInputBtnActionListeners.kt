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
            originSplitText.text = ""
            originItemPreText.text = ""
            originItemPostText.text = ""
            originPreText.text = ""
            originPostText.text = ""
        }
    }
}

@ComponentListener("txtTabbedModule.targetClearBtn")
class TxtTargetClearBtnActionListener(
    @Autowired val txtTabbedModule: TxtTabbedModule
) : ActionListener {

    override fun actionPerformed(e: ActionEvent?) {
        with(txtTabbedModule) {
            targetSplitText.text = ""
            targetItemPreText.text = ""
            targetItemPostText.text = ""
            targetPreText.text = ""
            targetPostText.text = ""
        }
    }
}