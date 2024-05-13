package com.wxl.jdevtool

import com.formdev.flatlaf.FlatLaf
import com.formdev.flatlaf.themes.FlatMacDarkLaf
import com.wxl.jdevtool.theme.AppTheme
import com.wxl.jdevtool.toast.Toasts
import org.fife.ui.rsyntaxtextarea.Theme
import org.mybatis.spring.annotation.MapperScan
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import javax.swing.JFrame

/**
 * Create by wuxingle on 2024/01/02
 * 启动类
 */
@MapperScan("com.wxl.jdevtool.db.mapper")
@SpringBootApplication
class JDevToolApplication

fun main(args: Array<String>) {
    // 主题设置
    // 加载flat
    FlatLaf.registerCustomDefaultsSource("themes")
    val lookAndFeel = FlatMacDarkLaf()
    FlatLaf.setup(lookAndFeel)

    // 加载rsyntax text area
    val textAreaTheme =
        Theme.load(JDevToolApplication::class.java.getResourceAsStream("/themes/rsyntax_dark.xml"))
    val appTheme = AppTheme(lookAndFeel, textAreaTheme)

    AppContexts.theme = appTheme

    // 主frame
    val frame = JFrame()
    AppContexts.mainFrame = frame

    // init组件
    Toasts.setMainFrame(frame)

    // 启动spring
    SpringApplicationBuilder(JDevToolApplication::class.java).headless(false).run(*args)
}
