package com.wxl.jdevtool.util

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Create by wuxingle on 2024/01/24
 * json工具
 */
object JsonUtils {

    private val json = Json {
        prettyPrint = true
    }

    /**
     * json展开
     */
    fun formatJson(text: String): String {
        val element = Json.parseToJsonElement(text)
        return json.encodeToString(element)
    }

    /**
     * json压缩
     */
    fun compressJson(text: String): String {
        return Json.parseToJsonElement(text).toString()
    }
}