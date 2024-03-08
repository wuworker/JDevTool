package com.wxl.jdevtool.validate

/**
 * Create by wuxingle on 2024/03/07
 * 校验组
 */
class InputValidateGroup(
    vararg checkers: InputValidate
) {

    private val checkers = checkers.toMutableList()

    fun check(focus: Boolean): Boolean {
        var res = true
        var fistFailIdx = -1
        for (i in checkers.indices) {
            res = res and checkers[i].check(false)
            if (!res && fistFailIdx == -1) {
                fistFailIdx = i
            }
        }
        if (focus && fistFailIdx >= 0) {
            checkers[fistFailIdx].component.requestFocus()
        }

        return res
    }

    fun add(checker: InputValidate) {
        checkers.add(checker)
    }
}
