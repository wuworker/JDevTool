package com.wxl.jdevtool.util

import org.apache.commons.text.StringEscapeUtils
import org.apache.commons.text.translate.*
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

/**
 * Create by wuxingle on 2024/02/06
 * 转义处理
 */
object EscapeUtils {

    private val ESCAPE_JAVA: CharSequenceTranslator

    private val ESCAPE_JSON: CharSequenceTranslator

    private val ESCAPE_UNICODE: CharSequenceTranslator

    private val UNESCAPE_UNICODE: CharSequenceTranslator

    init {
        val escapeJavaMap = hashMapOf<CharSequence, CharSequence>()
        escapeJavaMap["\""] = "\\\""
        escapeJavaMap["\\"] = "\\\\"
        ESCAPE_JAVA = AggregateTranslator(
            LookupTranslator(escapeJavaMap),
            LookupTranslator(EntityArrays.JAVA_CTRL_CHARS_ESCAPE)
        )

        val escapeJsonMap = hashMapOf<CharSequence, CharSequence>()
        escapeJsonMap["\""] = "\\\""
        escapeJsonMap["\\"] = "\\\\"
        escapeJsonMap["/"] = "\\/"
        ESCAPE_JSON = AggregateTranslator(
            LookupTranslator(escapeJsonMap),
            LookupTranslator(EntityArrays.JAVA_CTRL_CHARS_ESCAPE)
        )

        ESCAPE_UNICODE = UnicodeEscaper()
        UNESCAPE_UNICODE = UnicodeUnescaper()
    }

    /**
     * 转义java字符串
     */
    fun escapeString(str: String) = ESCAPE_JAVA.translate(str)

    fun unescapeString(str: String) = StringEscapeUtils.unescapeJava(str)

    /**
     * 转义json字符串
     */
    fun escapeJson(str: String) = ESCAPE_JSON.translate(str)

    fun unescapeJson(str: String) = StringEscapeUtils.unescapeJson(str)

    /**
     * 转义xml
     */
    fun escapeXml(str: String) = StringEscapeUtils.escapeXml11(str)

    fun unescapeXml(str: String) = StringEscapeUtils.unescapeXml(str)

    /**
     * 转义html
     */
    fun escapeHtml(str: String) = StringEscapeUtils.escapeHtml4(str)

    fun unescapeHtml(str: String) = StringEscapeUtils.unescapeHtml4(str)

    /**
     * 转义url
     */
    fun escapeURL(str: String): String = URLEncoder.encode(str, StandardCharsets.UTF_8)

    fun unescapeURL(str: String): String = URLDecoder.decode(str, StandardCharsets.UTF_8)

    /**
     * 转义unicode
     */
    fun escapeUnicode(str: String) = ESCAPE_UNICODE.translate(str)

    fun unescapeUnicode(str: String) = UNESCAPE_UNICODE.translate(str)


}