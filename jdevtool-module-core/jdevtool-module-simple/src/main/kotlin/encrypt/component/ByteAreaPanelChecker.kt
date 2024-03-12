package com.wxl.jdevtool.encrypt.component

import com.wxl.jdevtool.validate.InputValidate

/**
 * 增加校验内容不为空
 */
class ByteAreaPanelChecker(val panel: ByteAreaPanel) : InputValidate {

    override val component = panel.component

    override fun check(focus: Boolean): Boolean {
        if (!panel.check(focus)) {
            return false
        }
        if (panel.data.isEmpty()) {
            panel.inputChecker.showWarn(focus)
            return false
        }
        return true
    }
}
