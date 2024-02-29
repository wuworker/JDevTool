package com.wxl.jdevtool.encrypt.listener

import com.wxl.jdevtool.encrypt.EncryptTabbedModule
import com.wxl.jdevtool.theme.AppTheme
import com.wxl.jdevtool.theme.AppThemeListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Create by wuxingle on 2024/01/09
 * 主题监听
 */
@Component
class EncryptTabbedModuleThemeListener(
    @Autowired val encryptTabbedModule: EncryptTabbedModule
) : AppThemeListener {

    /**
     * 主题修改
     */
    override fun themeChange(theme: AppTheme) {
        val textAreaTheme = theme.textAreaTheme
        textAreaTheme.apply(encryptTabbedModule.digestPanel.inByteArea.textArea)
        textAreaTheme.apply(encryptTabbedModule.digestPanel.outByteArea.textArea)
        textAreaTheme.apply(encryptTabbedModule.symmetricCipherPanel.keyPanel.textArea)
        textAreaTheme.apply(encryptTabbedModule.symmetricCipherPanel.enPanel.textArea)
        textAreaTheme.apply(encryptTabbedModule.symmetricCipherPanel.dePanel.textArea)
    }
}
