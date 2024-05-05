package com.wxl.jdevtool.dubbo.listener

import com.wxl.jdevtool.dubbo.DubboTabbedModule
import com.wxl.jdevtool.theme.AppTheme
import com.wxl.jdevtool.theme.AppThemeListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Create by wuxingle on 2024/01/09
 * 主题监听
 */
@Component
class DubboTabbedModuleThemeListener(
    @Autowired val dubboTabbedModule: DubboTabbedModule
) : AppThemeListener {

    /**
     * 主题修改
     */
    override fun themeChange(theme: AppTheme) {
        val textAreaTheme = theme.textAreaTheme
        textAreaTheme.apply(dubboTabbedModule.appConfigPanel.textArea)
        textAreaTheme.apply(dubboTabbedModule.registerConfigPanel.textArea)
        textAreaTheme.apply(dubboTabbedModule.referenceConfigPanel.textArea)
        textAreaTheme.apply(dubboTabbedModule.attachmentConfigPanel.textArea)
        textAreaTheme.apply(dubboTabbedModule.resultArea)
    }
}
