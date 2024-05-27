package com.wxl.jdevtool.dubbo

import com.wxl.jdevtool.component.history.HistoryTextField
import com.wxl.jdevtool.component.history.StorageHistoryList
import com.wxl.jdevtool.dubbo.component.ConfigPanel
import com.wxl.jdevtool.dubbo.component.getString
import org.apache.dubbo.config.RegistryConfig
import java.io.Serial

/**
 * Create by wuxingle on 2024/04/23
 * 注册中心配置
 */
class RegisterConfigPanel : ConfigPanel<RegistryConfig>("注册中心", RegistryConfig::class.java) {

    private val addressField = HistoryTextField(StorageHistoryList("dubbo:register:address"))

    init {
        addKV("连接地址：", addressField)
    }

    override fun showKVView(config: Map<String, Any?>) {
        addressField.text = config.getString(ADDRESS_FIELD)
    }

    override fun getJsonFromKV(): Map<String, Any?> {
        return mapOf(ADDRESS_FIELD to addressField.text)
    }

    companion object {
        @Serial
        private const val serialVersionUID: Long = -3218938557289253232L

        const val ADDRESS_FIELD = "address"
    }

}
