package com.wxl.jdevtool.dubbo

import com.google.gson.reflect.TypeToken
import com.wxl.jdevtool.component.history.HistoryTextField
import com.wxl.jdevtool.component.history.StorageHistoryList
import com.wxl.jdevtool.dubbo.component.ConfigPanel
import com.wxl.jdevtool.dubbo.component.getString
import org.apache.dubbo.config.ReferenceConfig
import org.apache.dubbo.rpc.service.GenericService
import java.io.Serial

/**
 * Create by wuxingle on 2024/04/23
 * 消费者
 */
class ReferenceConfigPanel : ConfigPanel<ReferenceConfig<GenericService>>(
    "消费者配置",
    object : TypeToken<ReferenceConfig<GenericService>>() {}.type
) {

    private val interfaceField = HistoryTextField(StorageHistoryList("dubbo:reference:interface"))

    private val versionField = HistoryTextField(StorageHistoryList("dubbo:reference:version"))

    private val urlField = HistoryTextField(StorageHistoryList("dubbo:reference:url"))

    init {
        addKV("接口名称：", interfaceField)
        addKV("版本：", versionField)
        addKV("直连地址：", urlField)
    }

    override fun initConfig(config: MutableMap<String, Any?>) {
        config["isCheck"] = false
        config["retries"] = 0
        config["timeout"] = 30000
    }

    override fun showKVView(config: Map<String, Any?>) {
        interfaceField.text = config.getString(INTERFACE_NAME_FIELD)
        versionField.text = config.getString(VERSION_FIELD)
        urlField.text = config.getString(URL_FIELD)
    }

    override fun getJsonFromKV(): Map<String, Any?> {
        return mapOf(
            INTERFACE_NAME_FIELD to interfaceField.text,
            VERSION_FIELD to versionField.text,
            URL_FIELD to urlField.text
        )
    }

    override fun checkAndGetConfig(): ReferenceConfig<GenericService> {
        val config = super.checkAndGetConfig()
        if (config.`interface`.isNullOrBlank()) {
            throw IllegalArgumentException("接口名称(interface)不能为空")
        }

        with(config) {
            generic = "true"
        }
        return config
    }

    companion object {
        @Serial
        private const val serialVersionUID: Long = 1826279548573420628L

        const val INTERFACE_NAME_FIELD = "interfaceName"

        const val VERSION_FIELD = "version"

        const val URL_FIELD = "url"
    }

}