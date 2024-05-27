package com.wxl.jdevtool.note

/**
 * Create by wuxingle on 2024/05/13
 * 记事本常量
 */


enum class NoteType(
    val type: Int,
    val desc: String
) {
    TXT(1, "文本文件"),

    KV(2, "键值对"),
    ;

    override fun toString(): String {
        return desc
    }
}

object NoteAttrs {

    // 文件格式
    val STYLE = "style"

}
