package com.wxl.jdevtool.encrypt.listener

import com.wxl.jdevtool.ComponentListener
import com.wxl.jdevtool.encrypt.EncryptTabbedModule
import com.wxl.jdevtool.encrypt.SymmetricAlgorithm
import org.springframework.beans.factory.annotation.Autowired
import java.awt.event.ItemEvent
import java.awt.event.ItemListener
import javax.swing.JComboBox

/**
 * Create by wuxingle on 2024/02/07
 * 对称加密算法变化监听
 */
@ComponentListener(
    "encryptTabbedModule.symmetricCipherPanel.algorithmComboBox"
)
class SymmetricAlgorithmItemListeners(
    @Autowired val encryptTabbedModule: EncryptTabbedModule
) : ItemListener {

    override fun itemStateChanged(e: ItemEvent) {
        val comboBox = e.source as? JComboBox<SymmetricAlgorithm> ?: return
        val algorithm = comboBox.selectedItem as SymmetricAlgorithm
        if (algorithm == SymmetricAlgorithm.AES) {
            encryptTabbedModule.symmetricCipherPanel.keyLenLabel.isVisible = true
            encryptTabbedModule.symmetricCipherPanel.keyLenComboBox.isVisible = true
        } else {
            encryptTabbedModule.symmetricCipherPanel.keyLenLabel.isVisible = false
            encryptTabbedModule.symmetricCipherPanel.keyLenComboBox.isVisible = false
        }
    }
}
