package com.wxl.jdevtool.encrypt.utils

import java.security.Key
import java.security.KeyFactory
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
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
     * RSA私钥加密
     */
    fun doRSAPrivateKeyEncode(source: ByteArray, key: ByteArray): ByteArray {
        val keyFactory = KeyFactory.getInstance("RSA")
        val pkcs8EncodedKeySpec = PKCS8EncodedKeySpec(key)
        val privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec)
        return encodeFromCipher(source, privateKey, "RSA")
    }

    /**
     * RSA私钥解密
     */
    fun doRSAPrivateKeyDecode(source: ByteArray, key: ByteArray): ByteArray {
        val keyFactory = KeyFactory.getInstance("RSA")
        val pkcs8EncodedKeySpec = PKCS8EncodedKeySpec(key)
        val privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec)
        return decodeFromCipher(source, privateKey, "RSA")
    }

    /**
     * RSA公钥加密
     */
    fun doRSAPublicKeyEncode(source: ByteArray, key: ByteArray): ByteArray {
        val keyFactory = KeyFactory.getInstance("RSA")
        val x509EncodedKeySpec = X509EncodedKeySpec(key)
        val publicKey = keyFactory.generatePublic(x509EncodedKeySpec)
        return encodeFromCipher(source, publicKey, "RSA")
    }

    /**
     * RSA公钥解密
     */
    fun doRSAPublicKeyDecode(source: ByteArray, key: ByteArray): ByteArray {
        val keyFactory = KeyFactory.getInstance("RSA")
        val x509EncodedKeySpec = X509EncodedKeySpec(key)
        val publicKey = keyFactory.generatePublic(x509EncodedKeySpec)
        return decodeFromCipher(source, publicKey, "RSA")
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