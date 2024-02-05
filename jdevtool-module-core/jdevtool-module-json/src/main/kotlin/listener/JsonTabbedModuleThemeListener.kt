package com.wxl.jdevtool.json.listener

import com.wxl.jdevtool.json.JsonTabbedModule
import com.wxl.jdevtool.theme.AppTheme
import com.wxl.jdevtool.theme.AppThemeListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Create by wuxingle on 2024/01/12
 * 主题监听
 */
@Component
class JsonTabbedModuleThemeListener(
    @Autowired val jsonTabbedModule: JsonTabbedModule
) : AppThemeListener {
    /**
     * 主题修改
     */
    override fun themeChange(theme: AppTheme) {
        val textAreaTheme = theme.textAreaTheme
        textAreaTheme.apply(jsonTabbedModule.leftTextArea)
        textAreaTheme.apply(jsonTabbedModule.rightTextArea)
    }
}
