package com.wxl.jdevtool.dubbo

import com.google.gson.reflect.TypeToken
import com.wxl.jdevtool.component.HistoryTextField
import com.wxl.jdevtool.dubbo.component.ConfigPanel
import org.apache.dubbo.config.ReferenceConfig
import org.apache.dubbo.rpc.service.GenericService
import java.io.Serial
import java.lang.reflect.Type

/**
 * Create by wuxingle on 2024/04/23
 * 消费者
 */
class ReferenceConfigPanel : ConfigPanel<ReferenceConfig<GenericService>>("消费者配置") {

    override val config = ReferenceConfig<GenericService>()

    override val configType: Type = object : TypeToken<ReferenceConfig<GenericService>>() {}.type

    private val interfaceField = HistoryTextField(15)

    private val versionField = HistoryTextField(10)

    private val urlField = HistoryTextField(15)

    init {
        addKV("接口名称：", interfaceField)
        addKV("版本：", versionField)
        addKV("直连地址：", urlField)
    }

    override fun merge(mainConfig: ReferenceConfig<GenericService>, subConfig: ReferenceConfig<GenericService>) {
        if (!subConfig.`interface`.isNullOrBlank()) {
            mainConfig.`interface` = subConfig.`interface`
        }
        if (!subConfig.version.isNullOrBlank()) {
            mainConfig.version = subConfig.version
        }
        if (!subConfig.url.isNullOrBlank()) {
            mainConfig.url = subConfig.url
        }
    }

    override fun showKVView(config: ReferenceConfig<GenericService>) {
        interfaceField.text = config.`interface`
        versionField.text = config.version
        urlField.text = config.url
    }

    override fun showJsonView(config: ReferenceConfig<GenericService>) {
        config.`interface` = interfaceField.text
        config.version = versionField.text
        config.url = urlField.text
    }

    override fun checkAndGetConfig(): ReferenceConfig<GenericService> {
        if (config.`interface`.isNullOrBlank()) {
            throw IllegalArgumentException("接口名称(interface)不能为空")
        }

        with(config) {
            generic = "true"
            isCheck = false
            retries = 0
            timeout = 5000
        }

        return config
    }

    companion object {
        @Serial
        private const val serialVersionUID: Long = 1826279548573420628L
    }

}

