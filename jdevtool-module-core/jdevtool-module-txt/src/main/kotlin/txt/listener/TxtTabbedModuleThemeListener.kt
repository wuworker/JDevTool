package com.wxl.jdevtool.txt.listener

import com.wxl.jdevtool.theme.AppTheme
import com.wxl.jdevtool.theme.AppThemeListener
import com.wxl.jdevtool.txt.TxtTabbedModule
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Create by wuxingle on 2024/01/08
 * 主题监听
 */
@Component
class TxtTabbedModuleThemeListener(
    @Autowired val txtTabbedModule: TxtTabbedModule
) : AppThemeListener {

    /**
     * 主题修改
     */
    override fun themeChange(theme: AppTheme) {
        theme.textAreaTheme.apply(txtTabbedModule.leftTextArea)
        theme.textAreaTheme.apply(txtTabbedModule.rightTextArea)
    }
}
