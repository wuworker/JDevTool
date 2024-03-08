package com.wxl.jdevtool

import com.formdev.flatlaf.themes.FlatMacDarkLaf
import com.wxl.jdevtool.component.JDevToolFrame
import com.wxl.jdevtool.configuration.DefaultJDevToolStartupProcess
import com.wxl.jdevtool.theme.AppTheme
import com.wxl.jdevtool.theme.AppThemeManager
import com.wxl.jdevtool.toast.ToastType
import com.wxl.jdevtool.toast.Toasts
import org.fife.ui.rsyntaxtextarea.Theme
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import javax.swing.JTextField

/**
 * Create by wuxingle on 2024/01/02
 * 启动类
 */
@SpringBootApplication
class JDevToolApplication

fun main(args: Array<String>) {
    // 主题设置
    FlatMacDarkLaf.setup()
    val textAreaTheme =
        Theme.load(DefaultJDevToolStartupProcess::class.java.getResourceAsStream("/themes/rsyntax_dark.xml"))
    val appTheme = AppTheme(textAreaTheme)
    AppThemeManager.theme = appTheme

    val context = SpringApplicationBuilder(JDevToolApplication::class.java).headless(false)
        .run(*args)

    val jdevToolFrame = context.getBean(JDevToolFrame::class.java)
    Toasts.setFrame(jdevToolFrame)

    jdevToolFrame.setSize(800, 800)
    jdevToolFrame.isVisible = true
}
