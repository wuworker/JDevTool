package com.wxl.jdevtool.extension

import java.io.Serial
import java.util.function.Predicate
import javax.swing.text.AttributeSet
import javax.swing.text.PlainDocument

/**
 * Create by wuxingle on 2024/01/31
 * 校验通过的才能插入
 */
class FilterPlainDocument(
    private val filter: Predicate<String>
) : PlainDocument() {

    override fun insertString(offs: Int, str: String?, a: AttributeSet?) {
        if (str.isNullOrEmpty()) {
            return
        }
        if (filter.test(str)) {
            super.insertString(offs, str, a)
        }
    }

    companion object {
        @Serial
        private const val serialVersionUID: Long = 8183548083459315953L
    }

}

/**
 * 输入的字符都在必须在candidate里
 */
class CandidatePlainDocument(
    candidate: String
) : PlainDocument() {

    private val candidateChars = candidate.toCharArray().toSet()

    override fun insertString(offs: Int, str: String?, a: AttributeSet?) {
        if (str.isNullOrEmpty()) {
            return
        }
        for (c in str.toCharArray()) {
            if (!candidateChars.contains(c)) {
                return
            }
        }

        super.insertString(offs, str, a)
    }

    companion object {
        @Serial
        private const val serialVersionUID: Long = -7619330559269027412L
    }
}
