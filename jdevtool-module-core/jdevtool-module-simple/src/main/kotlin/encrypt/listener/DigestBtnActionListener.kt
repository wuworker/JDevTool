package com.wxl.jdevtool.encrypt.listener

import com.wxl.jdevtool.ComponentListener
import com.wxl.jdevtool.encrypt.DigestAlgorithm
import com.wxl.jdevtool.encrypt.EncryptTabbedModule
import com.wxl.jdevtool.encrypt.HMacAlgorithm
import com.wxl.jdevtool.encrypt.utils.DigestUtils
import com.wxl.jdevtool.toast.ToastType
import com.wxl.jdevtool.toast.Toasts
import org.springframework.beans.factory.annotation.Autowired
import java.awt.event.ActionEvent
import java.awt.event.ActionListener

/**
 * Create by wuxingle on 2024/02/27
 * 生成摘要
 */
@ComponentListener(
    "encryptTabbedModule.digestPanel.genBtn"
)
class DigestBtnActionListener(
    @Autowired val encryptTabbedModule: EncryptTabbedModule
) : ActionListener {

    override fun actionPerformed(e: ActionEvent) {
        val digestPanel = encryptTabbedModule.digestPanel
        if (!digestPanel.checkInput()) {
            return
        }
        val data = digestPanel.inByteArea.data

        val mdData = try {
            when (digestPanel.algorithmComboBox.selectedItem as DigestAlgorithm) {
                DigestAlgorithm.MD5 -> DigestUtils.doMD5(data)
                DigestAlgorithm.SHA_1 -> DigestUtils.doSHA1(data)
                DigestAlgorithm.SHA_256 -> DigestUtils.doSHA256(data)
                DigestAlgorithm.SHA_512 -> DigestUtils.doSHA512(data)
                DigestAlgorithm.HMAC -> {
                    if (!digestPanel.checkSecret()) {
                        return
                    }
                    val key = digestPanel.secretTextField.key
                    when (digestPanel.hmacAlgorithmComboBox.selectedItem as HMacAlgorithm) {
                        HMacAlgorithm.HMAC_MD5 -> DigestUtils.doMACWithMD5(data, key)
                        HMacAlgorithm.HMAC_SHA1 -> DigestUtils.doMACWithSHA1(data, key)
                        HMacAlgorithm.HMAC_SHA256 -> DigestUtils.doMACWithSHA256(data, key)
                        HMacAlgorithm.HMAC_SHA512 -> DigestUtils.doMACWithSHA512(data, key)
                    }
                }
            }
        } catch (e: Exception) {
            Toasts.show(ToastType.ERROR, "生成失败:${e.message}")
            return
        }

        digestPanel.outByteArea.data = mdData
    }
}
