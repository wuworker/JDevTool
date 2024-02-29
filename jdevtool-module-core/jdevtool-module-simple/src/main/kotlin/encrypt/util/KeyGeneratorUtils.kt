package com.wxl.jdevtool.encrypt.utils

import javax.crypto.KeyGenerator

/**
 * Create by wuxingle on 2024/02/27
 * 密钥生成
 */
object KeyGeneratorUtils {

    /**
     * HmacMD5密钥
     */
    fun generateMACWithMD5Key(len: Int? = null): ByteArray {
        return generateKey("HmacMD5", len)
    }

    /**
     * HmacSHA1密钥
     */
    fun generateMACWithSHA1Key(len: Int? = null): ByteArray {
        return generateKey("HmacSHA1", len)
    }

    /**
     * HmacSHA256密钥
     */
    fun generateMACWithSHA256Key(len: Int? = null): ByteArray {
        return generateKey("HmacSHA256", len)
    }

    /**
     * HmacSHA512密钥
     */
    fun generateMACWithSHA512Key(len: Int? = null): ByteArray {
        return generateKey("HmacSHA512", len)
    }

    /**
     * AES密钥
     * 128,192,256
     */
    fun generateAESKey(len: Int? = null): ByteArray {
        return generateKey("AES", len)
    }

    /**
     * DES密钥（固定长度）
     */
    fun generateDESKey(): ByteArray {
        return generateKey("DES")
    }

    /**
     * 产生key
     */
    private fun generateKey(algorithm: String, len: Int? = null): ByteArray {
        val keyGenerator = KeyGenerator.getInstance(algorithm)
        if (len != null) {
            keyGenerator.init(len)
        }
        val secretKey = keyGenerator.generateKey()
        return secretKey.encoded
    }
}
