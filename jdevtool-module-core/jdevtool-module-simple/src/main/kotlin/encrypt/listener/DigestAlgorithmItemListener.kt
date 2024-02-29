package com.wxl.jdevtool.encrypt.listener

import com.wxl.jdevtool.ComponentListener
import com.wxl.jdevtool.encrypt.DigestAlgorithm
import com.wxl.jdevtool.encrypt.EncryptTabbedModule
import org.springframework.beans.factory.annotation.Autowired
import java.awt.event.ItemEvent
import java.awt.event.ItemListener
import javax.swing.JComboBox

/**
 * Create by wuxingle on 2024/02/07
 * 消息摘要选项变化监听
 */
@ComponentListener(
    "encryptTabbedModule.digestPanel.algorithmComboBox"
)
class DigestAlgorithmItemListener(
    @Autowired val encryptTabbedModule: EncryptTabbedModule
) : ItemListener {

    override fun itemStateChanged(e: ItemEvent) {
        val comboBox = e.source as? JComboBox<DigestAlgorithm> ?: return
        val algorithm = comboBox.selectedItem as DigestAlgorithm
        if (algorithm == DigestAlgorithm.HMAC) {
            encryptTabbedModule.digestPanel.hmacAlgorithmComboBox.isVisible = true
            encryptTabbedModule.digestPanel.secretTextField.isVisible = true
        } else {
            encryptTabbedModule.digestPanel.hmacAlgorithmComboBox.isVisible = false
            encryptTabbedModule.digestPanel.secretTextField.isVisible = false
        }
    }
}
