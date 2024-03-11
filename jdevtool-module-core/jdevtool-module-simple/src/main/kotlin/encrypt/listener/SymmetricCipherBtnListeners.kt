package com.wxl.jdevtool.encrypt.listener

import com.wxl.jdevtool.ComponentListener
import com.wxl.jdevtool.encrypt.*
import com.wxl.jdevtool.encrypt.utils.CipherUtils
import com.wxl.jdevtool.encrypt.utils.KeyGeneratorUtils
import com.wxl.jdevtool.toast.ToastType
import com.wxl.jdevtool.toast.Toasts
import org.springframework.beans.factory.annotation.Autowired
import java.awt.event.ActionEvent
import java.awt.event.ActionListener

/**
 * Create by wuxingle on 2024/02/28
 * 密钥生成
 */
@ComponentListener("encryptTabbedModule.symmetricCipherPanel.keyGenBtn")
class SymmetricCipherKeyGenListener(
    @Autowired val encryptTabbedModule: EncryptTabbedModule
) : ActionListener {

    override fun actionPerformed(e: ActionEvent) {
        val symmetricCipherPanel = encryptTabbedModule.symmetricCipherPanel

        // 算法
        val algorithm = symmetricCipherPanel.algorithmComboBox.selectedItem as SymmetricAlgorithm

        try {
            val key: ByteArray = when (algorithm) {
                SymmetricAlgorithm.AES -> {
                    val len = symmetricCipherPanel.keyLenComboBox.selectedItem as Int
                    KeyGeneratorUtils.generateAESKey(len)
                }

                SymmetricAlgorithm.DES -> KeyGeneratorUtils.generateDESKey()
            }

            symmetricCipherPanel.keyPanel.data = key
        } catch (e: Exception) {
            Toasts.show(ToastType.ERROR, "密钥生成失败:${e.message}")
        }
    }
}

@ComponentListener("encryptTabbedModule.symmetricCipherPanel.enBtn")
class SymmetricCipherEncodeListener(
    @Autowired val encryptTabbedModule: EncryptTabbedModule
) : ActionListener {

    override fun actionPerformed(e: ActionEvent) {
        val symmetricCipherPanel = encryptTabbedModule.symmetricCipherPanel
        symmetricCipherPanel.dePanel.inputChecker.showNormal()
        if (!symmetricCipherPanel.checkEncode()) {
            return
        }
        val key = symmetricCipherPanel.keyPanel.data
        val source = symmetricCipherPanel.enPanel.data
        val algorithm = getSelectedAlgorithm(symmetricCipherPanel)

        try {
            val encodeBytes = if (algorithm.startsWith("AES")) {
                CipherUtils.doAESEncode(source, key, algorithm)
            } else {
                CipherUtils.doDESEncode(source, key, algorithm)
            }

            symmetricCipherPanel.dePanel.data = encodeBytes
        } catch (e: Exception) {
            Toasts.show(ToastType.ERROR, "加密失败:${e.message}")
        }
    }
}

@ComponentListener("encryptTabbedModule.symmetricCipherPanel.deBtn")
class SymmetricCipherDecodeListener(
    @Autowired val encryptTabbedModule: EncryptTabbedModule
) : ActionListener {

    override fun actionPerformed(e: ActionEvent) {
        val symmetricCipherPanel = encryptTabbedModule.symmetricCipherPanel
        symmetricCipherPanel.enPanel.inputChecker.showNormal()
        if (!symmetricCipherPanel.checkDecode()) {
            return
        }

        val key = symmetricCipherPanel.keyPanel.data
        val source = symmetricCipherPanel.dePanel.data
        val algorithm = getSelectedAlgorithm(symmetricCipherPanel)

        try {
            val decodeBytes = if (algorithm.startsWith("AES")) {
                CipherUtils.doAESDecode(source, key, algorithm)
            } else {
                CipherUtils.doDESDecode(source, key, algorithm)
            }

            symmetricCipherPanel.enPanel.data = decodeBytes
        } catch (e: Exception) {
            Toasts.show(ToastType.ERROR, "解密失败:${e.message}")
        }
    }
}


/**
 * 获取选中的算法
 */
private fun getSelectedAlgorithm(symmetricCipherPanel: SymmetricCipherPanel): String {
    val algorithm = symmetricCipherPanel.algorithmComboBox.selectedItem as SymmetricAlgorithm
    val mode = symmetricCipherPanel.modeAlgorithmComboBox.selectedItem as AlgorithmMode
    val padding = symmetricCipherPanel.paddingAlgorithmComboBox.selectedItem as AlgorithmPadding

    return "${algorithm}/${mode}/${padding}"
}





