package com.wxl.jdevtool.encrypt.extension

import java.util.*

/**
 * Create by wuxingle on 2024/02/07
 * String和Byte扩展
 */

/**
 * byte转16进制字符串
 */
fun ByteArray.toHexString(): String {
    return joinToString("") { "%02x".format(it) }
}

/**
 * 十六进制字符串转byte
 */
fun String.fromHexString(): ByteArray {
    check(length % 2 == 0) { "illegal hex string" }

    return ByteArray(length / 2) {
        Integer.parseInt(this, it * 2, (it + 1) * 2, 16).toByte()
    }
}

/**
 * byte转base64
 */
fun ByteArray.toBase64(): String {
    return Base64.getEncoder().encodeToString(this)
}


/**
 * base64转byte
 */
fun String.fromBase64(): ByteArray {
    return Base64.getDecoder().decode(this)
}

/**
 * 除了可见ascii，其他转为16进制
 */
fun ByteArray.toDisplayString(): String {
    return this.joinToString("") {
        if (it in 0x20..0x7e) {
            it.toInt().toChar().toString()
        } else {
            "\\x${String.format("%02x", it)}"
        }
    }
}

/**
 * 先转义再转byte
 */
fun String.fromDisplayString(): ByteArray {
    val byteList = arrayListOf<Byte>()
    val bytes = toByteArray()
    var i = 0
    while (i < bytes.size) {
        // \x开头
        if (bytes[i] == '\\'.code.toByte() && i + 1 < bytes.size && bytes[i + 1] == 'x'.code.toByte()) {
            if (i + 3 >= bytes.size) {
                throw NumberFormatException(
                    "Less than 2 hex digits in hex value: '${
                        String(bytes, i, bytes.size - i)
                    }' due to end of CharSequence"
                )
            }
            val b = Integer.parseInt(String(bytes, i + 2, 2), 16).toByte()
            byteList.add(b)
            i += 4
        } else {
            byteList.add(bytes[i])
            i++
        }
    }

    return byteList.toByteArray()
}
