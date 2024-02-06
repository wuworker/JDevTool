package com.wxl.jdevtool.escape.listener

import com.wxl.jdevtool.escape.EscapeTabbedModule
import com.wxl.jdevtool.theme.AppTheme
import com.wxl.jdevtool.theme.AppThemeListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Create by wuxingle on 2024/01/09
 * 主题监听
 */
@Component
class EscapeTabbedModuleThemeListener(
    @Autowired val escapeTabbedModule: EscapeTabbedModule
) : AppThemeListener {

    /**
     * 主题修改
     */
    override fun themeChange(theme: AppTheme) {
        val textAreaTheme = theme.textAreaTheme
        textAreaTheme.apply(escapeTabbedModule.leftTextArea)
        textAreaTheme.apply(escapeTabbedModule.rightTextArea)
    }
}
