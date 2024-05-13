package com.wxl.jdevtool.dubbo

import com.wxl.jdevtool.component.history.HistoryTextField
import com.wxl.jdevtool.component.history.StorageHistoryList
import com.wxl.jdevtool.dubbo.component.ConfigPanel
import org.apache.dubbo.config.RegistryConfig
import java.io.Serial

/**
 * Create by wuxingle on 2024/04/23
 * 注册中心配置
 */
class RegisterConfigPanel : ConfigPanel<RegistryConfig>("注册中心") {

    override val config = RegistryConfig()

    override val configType = RegistryConfig::class.java

    private val addressField = HistoryTextField(StorageHistoryList("dubbo:register:address"))

    init {
        addKV("连接地址：", addressField)
    }

    override fun merge(mainConfig: RegistryConfig, subConfig: RegistryConfig) {
        if (!subConfig.address.isNullOrBlank()) {
            mainConfig.address = subConfig.address
        }
    }

    override fun showKVView(config: RegistryConfig) {
        addressField.text = config.address
    }

    override fun showJsonView(config: RegistryConfig) {
        config.address = addressField.text
    }

    override fun checkAndGetConfig(): RegistryConfig {
        showJsonView(config)
        return config
    }

    companion object {
        @Serial
        private const val serialVersionUID: Long = -3218938557289253232L
    }

}
