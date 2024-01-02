package com.wxl.jdevtool.txt

import com.wxl.jdevtool.TabbedModule
import org.springframework.stereotype.Component
import javax.swing.Icon
import javax.swing.JLabel
import javax.swing.JPanel

/**
 * Create by wuxingle on 2024/01/02
 * 文本处理器
 */
@Component
class TxtTabbedModule : TabbedModule {

    /**
     * 获取panel
     */
    override fun getPanel(): JPanel {
        val panel = JPanel()
        val label = JLabel("测试标签")

        panel.add(label)
        return panel
    }

    /**
     * 模块标题
     */
    override val title: String = "文本处理器"

    /**
     * 模块图标
     */
    override val icon: Icon? = null

    /**
     * 模块提示
     */
    override val tip: String? = "文本处理"
}
