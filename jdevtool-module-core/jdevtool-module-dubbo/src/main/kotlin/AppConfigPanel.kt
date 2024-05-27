package com.wxl.jdevtool.dubbo

import com.wxl.jdevtool.component.history.HistoryTextField
import com.wxl.jdevtool.component.history.StorageHistoryList
import com.wxl.jdevtool.dubbo.component.ConfigPanel
import com.wxl.jdevtool.dubbo.component.getString
import org.apache.dubbo.config.ApplicationConfig
import java.io.Serial

/**
 * Create by wuxingle on 2024/04/23
 * 应用配置
 */
class AppConfigPanel : ConfigPanel<ApplicationConfig>("应用配置", ApplicationConfig::class.java) {

    private val nameField = HistoryTextField(StorageHistoryList("dubbo:app:name"))

    init {
        addKV("应用名称：", nameField)
    }

    override fun showKVView(config: Map<String, Any?>) {
        nameField.text = config.getString(NAME_FIELD)
    }

    override fun getJsonFromKV(): Map<String, Any?> {
        return mapOf(NAME_FIELD to nameField.text)
    }

    /**
     * 检查并获取配置
     */
    override fun checkAndGetConfig(): ApplicationConfig {
        val config = super.checkAndGetConfig()
        if (config.name.isNullOrBlank()) {
            throw IllegalArgumentException("应用名称(name)不能为空")
        }
        return config
    }

    companion object {
        @Serial
        private const val serialVersionUID: Long = -8622907443777397803L

        private const val NAME_FIELD = "name"
    }
}
