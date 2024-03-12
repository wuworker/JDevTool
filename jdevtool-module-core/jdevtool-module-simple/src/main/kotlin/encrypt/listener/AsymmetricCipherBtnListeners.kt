package com.wxl.jdevtool.encrypt.listener

import com.wxl.jdevtool.ComponentListener
import com.wxl.jdevtool.encrypt.EncryptTabbedModule
import com.wxl.jdevtool.encrypt.component.ByteAreaPanel
import com.wxl.jdevtool.encrypt.component.ByteAreaPanelChecker
import com.wxl.jdevtool.encrypt.utils.CipherUtils
import com.wxl.jdevtool.encrypt.utils.KeyGeneratorUtils
import com.wxl.jdevtool.toast.ToastType
import com.wxl.jdevtool.toast.Toasts
import com.wxl.jdevtool.validate.InputValidateGroup
import org.springframework.beans.factory.annotation.Autowired
import java.awt.event.ActionEvent
import java.awt.event.ActionListener

/**
 * Create by wuxingle on 2024/03/12
 * 非对称加密按钮监听
 */

/**
 * 生成密钥对
 */
@ComponentListener("encryptTabbedModule.asymmetricCipherPanel.genKeyBtn")
class AsymmetricKeyGenBtnListener(
    @Autowired val encryptTabbedModule: EncryptTabbedModule
) : ActionListener {

    override fun actionPerformed(e: ActionEvent) {
        val panel = encryptTabbedModule.asymmetricCipherPanel
        panel.privateKeyPanel.inputChecker.showNormal()
        panel.publicKeyPanel.inputChecker.showNormal()

        val keyLen = panel.keyLenComboBox.selectedItem as Int

        try {
            val keyPair = KeyGeneratorUtils.generateRSAKey(keyLen)
            panel.privateKeyPanel.data = keyPair.private.encoded
            panel.publicKeyPanel.data = keyPair.public.encoded
        } catch (e: Exception) {
            Toasts.show(ToastType.ERROR, "密钥生成失败:${e.message}")
        }
    }
}

/**
 * 私钥加密
 */
@ComponentListener("encryptTabbedModule.asymmetricCipherPanel.priEnBtn")
class AsymmetricPriKeyEnBtnListener(
    @Autowired val encryptTabbedModule: EncryptTabbedModule
) : ActionListener {

    override fun actionPerformed(e: ActionEvent) {
        val panel = encryptTabbedModule.asymmetricCipherPanel
        panel.publicKeyPanel.inputChecker.showNormal()
        panel.dePanel.inputChecker.showNormal()

        if (!check(panel.privateKeyPanel, panel.enPanel)) {
            return
        }
        val source = panel.enPanel.data
        val priKey = panel.privateKeyPanel.data

        try {
            val data = CipherUtils.doRSAPrivateKeyEncode(source, priKey)
            panel.dePanel.data = data
        } catch (e: Exception) {
            Toasts.show(ToastType.ERROR, "私钥加密失败:${e.message}")
        }
    }
}

/**
 * 公钥加密
 */
@ComponentListener("encryptTabbedModule.asymmetricCipherPanel.pubEnBtn")
class AsymmetricPubKeyEnBtnListener(
    @Autowired val encryptTabbedModule: EncryptTabbedModule
) : ActionListener {

    override fun actionPerformed(e: ActionEvent) {
        val panel = encryptTabbedModule.asymmetricCipherPanel
        panel.privateKeyPanel.inputChecker.showNormal()
        panel.dePanel.inputChecker.showNormal()

        if (!check(panel.publicKeyPanel, panel.enPanel)) {
            return
        }
        val source = panel.enPanel.data
        val priKey = panel.publicKeyPanel.data

        try {
            val data = CipherUtils.doRSAPublicKeyEncode(source, priKey)
            panel.dePanel.data = data
        } catch (e: Exception) {
            Toasts.show(ToastType.ERROR, "公钥加密失败:${e.message}")
        }
    }
}


/**
 * 私钥解密
 */
@ComponentListener("encryptTabbedModule.asymmetricCipherPanel.priDeBtn")
class AsymmetricPriKeyDeBtnListener(
    @Autowired val encryptTabbedModule: EncryptTabbedModule
) : ActionListener {

    override fun actionPerformed(e: ActionEvent) {
        val panel = encryptTabbedModule.asymmetricCipherPanel
        panel.publicKeyPanel.inputChecker.showNormal()
        panel.enPanel.inputChecker.showNormal()

        if (!check(panel.privateKeyPanel, panel.dePanel)) {
            return
        }
        val source = panel.dePanel.data
        val priKey = panel.privateKeyPanel.data

        try {
            val data = CipherUtils.doRSAPrivateKeyDecode(source, priKey)
            panel.enPanel.data = data
        } catch (e: Exception) {
            Toasts.show(ToastType.ERROR, "私钥解密失败:${e.message}")
        }
    }
}

/**
 * 公钥解密
 */
@ComponentListener("encryptTabbedModule.asymmetricCipherPanel.pubDeBtn")
class AsymmetricPubKeyDeBtnListener(
    @Autowired val encryptTabbedModule: EncryptTabbedModule
) : ActionListener {

    override fun actionPerformed(e: ActionEvent) {
        val panel = encryptTabbedModule.asymmetricCipherPanel
        panel.privateKeyPanel.inputChecker.showNormal()
        panel.enPanel.inputChecker.showNormal()

        if (!check(panel.publicKeyPanel, panel.dePanel)) {
            return
        }
        val source = panel.dePanel.data
        val priKey = panel.publicKeyPanel.data

        try {
            val data = CipherUtils.doRSAPublicKeyDecode(source, priKey)
            panel.enPanel.data = data
        } catch (e: Exception) {
            Toasts.show(ToastType.ERROR, "公钥解密失败:${e.message}")
        }
    }
}

private fun check(key: ByteAreaPanel, input: ByteAreaPanel): Boolean {
    val checker =
        InputValidateGroup(ByteAreaPanelChecker(key), ByteAreaPanelChecker(input))
    return checker.check(true)
}

