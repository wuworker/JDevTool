package com.wxl.jdevtool.dubbo

import com.wxl.jdevtool.component.HistoryTextField
import com.wxl.jdevtool.dubbo.component.ConfigPanel
import org.apache.dubbo.config.ApplicationConfig
import java.io.Serial

/**
 * Create by wuxingle on 2024/04/23
 * 应用配置
 */
class AppConfigPanel : ConfigPanel<ApplicationConfig>("应用配置") {

    override val config = ApplicationConfig()

    override val configType = ApplicationConfig::class.java

    private val nameField = HistoryTextField(10)

    init {
        addKV("应用名称：", nameField)
    }

    override fun merge(mainConfig: ApplicationConfig, subConfig: ApplicationConfig) {
        if (!subConfig.name.isNullOrBlank()) {
            mainConfig.name = subConfig.name
        }
    }

    override fun showKVView(config: ApplicationConfig) {
        nameField.text = config.name
    }

    override fun showJsonView(config: ApplicationConfig) {
        config.name = nameField.text
    }

    override fun checkAndGetConfig(): ApplicationConfig {
        val name = config.name
        if (name.isNullOrBlank()) {
            throw IllegalArgumentException("应用名称(name)不能为空")
        }
        return config
    }

    companion object {
        @Serial
        private const val serialVersionUID: Long = -8622907443777397803L
    }
}
