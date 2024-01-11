package com.wxl.jdevtool.configuration

import org.springframework.beans.factory.BeanCreationException
import org.springframework.stereotype.Component

/**
 * Create by wuxingle on 2024/01/05
 * 组件注册中心
 */
@Component
class ComponentRegistry {

    private val registry = HashMap<String, Any>()

    val size
        get() = registry.size

    val allIds
        get() = registry.keys.toList()

    /**
     * 注册
     */
    fun register(id: String, component: Any) {
        if (registry.putIfAbsent(id, component) != null) {
            throw BeanCreationException("componentId: $id is repeat!")
        }
    }

    /**
     * 删除
     */
    fun unregister(id: String) {
        registry.remove(id)
    }

    /**
     * 获取组件
     */
    fun get(id: String): Any? {
        return registry[id]
    }

    /**
     * 获取组件
     */
    fun getComponent(id: String): java.awt.Component {
        val component = registry[id] ?: throw BeanCreationException("componentId: $id is not exist")
        if (component !is java.awt.Component) {
            throw BeanCreationException("componentId: $id is not java.awt.Component")
        }
        return component
    }
}
