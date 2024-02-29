package com.wxl.jdevtool.encrypt.utils

import java.security.MessageDigest
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

/**
 * Create by wuxingle on 2024/02/27
 * 消息摘要
 */
object DigestUtils {

    /**
     * MD5
     * 128位
     */
    fun doMD5(source: ByteArray): ByteArray {
        return encodeFromMessageDigest(source, "MD5")
    }

    /**
     * SHA-1
     * 160位
     */
    fun doSHA1(source: ByteArray): ByteArray {
        return encodeFromMessageDigest(source, "SHA-1")
    }

    /**
     * SHA-256
     * 256位
     */
    fun doSHA256(source: ByteArray): ByteArray {
        return encodeFromMessageDigest(source, "SHA-256")
    }

    /**
     * SHA-512
     * 512位
     */
    fun doSHA512(source: ByteArray): ByteArray {
        return encodeFromMessageDigest(source, "SHA-512")
    }

    /**
     * HmacMD5加密
     *
     * @param key 密钥
     */
    fun doMACWithMD5(source: ByteArray, key: ByteArray): ByteArray {
        return encodeFromMAC(source, key, "HmacMD5")
    }


    /**
     * HmacSHA1加密
     *
     * @param key 密钥
     */
    fun doMACWithSHA1(source: ByteArray, key: ByteArray): ByteArray {
        return encodeFromMAC(source, key, "HmacSHA1")
    }


    /**
     * HmacSHA256加密
     *
     * @param key 密钥
     */
    fun doMACWithSHA256(source: ByteArray, key: ByteArray): ByteArray {
        return encodeFromMAC(source, key, "HmacSHA256")
    }


    /**
     * HmacSHA512加密
     *
     * @param key 密钥
     */
    fun doMACWithSHA512(source: ByteArray, key: ByteArray): ByteArray {
        return encodeFromMAC(source, key, "HmacSHA512")
    }

    /**
     * 消息摘要加密
     *
     * @param source    原文
     * @param algorithm 算法
     */
    private fun encodeFromMessageDigest(source: ByteArray, algorithm: String): ByteArray {
        val md = MessageDigest.getInstance(algorithm)
        return md.digest(source)
    }

    /**
     * MAC加密
     * 含有密钥的消息摘要算法(MAC或HMAC)
     * HmacMD5:128位
     * HmacSHA1:160位...
     */
    private fun encodeFromMAC(source: ByteArray, key: ByteArray, algorithm: String): ByteArray {
        //根据数据生成密钥
        val receiveSecretKey = SecretKeySpec(key, algorithm)
        val mac = Mac.getInstance(receiveSecretKey.algorithm)
        mac.init(receiveSecretKey)
        return mac.doFinal(source)
    }

}