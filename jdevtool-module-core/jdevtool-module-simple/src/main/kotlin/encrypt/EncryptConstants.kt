package com.wxl.jdevtool.encrypt

/**
 * Create by wuxingle on 2024/02/07
 * 加解密常量
 */

/**
 * 字节展示方式
 */
enum class KeyShowStyle {
    BASE64,
    HEX,
    STRING,
    ;
}

/**
 * 消息摘要算法支持
 */
enum class DigestAlgorithm(
    val algorithm: String
) {

    MD5("MD5"),
    SHA_1("SHA-1"),
    SHA_256("SHA-256"),
    SHA_512("SHA-512"),
    HMAC("HMAC"),
    ;

    override fun toString(): String {
        return algorithm
    }
}

/**
 * HMAC算法支持
 */
enum class HMacAlgorithm(
    val algorithm: String
) {
    HMAC_MD5("HmacMD5"),
    HMAC_SHA1("HmacSHA1"),
    HMAC_SHA256("HmacSHA256"),
    HMAC_SHA512("HmacSHA512"),
    ;

    override fun toString(): String {
        return algorithm
    }
}

/**
 * 对称加密算法
 */
enum class SymmetricAlgorithm {
    AES, DES
}

/**
 * 模式
 */
enum class AlgorithmMode {
    ECB, CBC, CTR, CFB
}

/**
 * 填充
 */
enum class AlgorithmPadding(
    val padding: String
) {
    PKCS5_PADDING("PKCS5Padding"),
    NO_PADDING("NoPadding"),
    ;

    override fun toString(): String {
        return padding
    }
}

/**
 * 非对称加密算法
 */
enum class ASymmetricAlgorithm {
    RSA
}