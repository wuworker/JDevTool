package com.wxl.jdevtool.encrypt.utils

import java.security.Key
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

/**
 * Create by wuxingle on 2024/02/28
 * 加解密
 */
object CipherUtils {

    /**
     * AES加密
     */
    fun doAESEncode(source: ByteArray, key: ByteArray, algorithm: String = "AES/ECB/PKCS5Padding"): ByteArray {
        val secretKey: SecretKey = SecretKeySpec(key, "AES")
        return encodeFromCipher(source, secretKey, algorithm)
    }

    /**
     * AES解密
     */
    fun doAESDecode(source: ByteArray, key: ByteArray, algorithm: String = "AES/ECB/PKCS5Padding"): ByteArray {
        val secretKey: SecretKey = SecretKeySpec(key, "AES")
        return decodeFromCipher(source, secretKey, algorithm)
    }

    /**
     * DES加密
     */
    fun doDESEncode(source: ByteArray, key: ByteArray, algorithm: String = "DES/ECB/PKCS5Padding"): ByteArray {
        val secretKey: SecretKey = SecretKeySpec(key, "DES")
        return encodeFromCipher(source, secretKey, algorithm)
    }

    /**
     * DES解密
     */
    fun doDESDecode(source: ByteArray, key: ByteArray, algorithm: String = "DES/ECB/PKCS5Padding"): ByteArray {
        val secretKey: SecretKey = SecretKeySpec(key, "DES")
        return decodeFromCipher(source, secretKey, algorithm)
    }

    /**
     * Cipher加密
     */
    private fun encodeFromCipher(source: ByteArray, key: Key, algorithm: String): ByteArray {
        val cipher = Cipher.getInstance(algorithm)
        //加密
        cipher.init(Cipher.ENCRYPT_MODE, key)
        return cipher.doFinal(source)
    }

    /**
     * Cipher解密
     */
    private fun decodeFromCipher(source: ByteArray, key: Key, algorithm: String): ByteArray {
        val cipher = Cipher.getInstance(algorithm)
        //解密
        cipher.init(Cipher.DECRYPT_MODE, key)
        return cipher.doFinal(source)
    }


}