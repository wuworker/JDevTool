package com.wxl.jdevtool.collection.listener

import com.wxl.jdevtool.collection.CollectionTabbedModule
import com.wxl.jdevtool.theme.AppTheme
import com.wxl.jdevtool.theme.AppThemeListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Create by wuxingle on 2024/01/09
 * 主题监听
 */
@Component
class CollectionTabbedModuleThemeListener(
    @Autowired val collectionTabbedModule: CollectionTabbedModule
) : AppThemeListener {
    /**
     * 主题修改
     */
    override fun themeChange(theme: AppTheme) {
        val textAreaTheme = theme.textAreaTheme
        textAreaTheme.apply(collectionTabbedModule.leftTextArea1)
        textAreaTheme.apply(collectionTabbedModule.leftTextArea2)
        textAreaTheme.apply(collectionTabbedModule.rightTextArea)
    }
}