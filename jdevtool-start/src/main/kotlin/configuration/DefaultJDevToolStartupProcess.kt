package com.wxl.jdevtool.configuration

import com.wxl.jdevtool.ComponentId
import com.wxl.jdevtool.ComponentListener
import com.wxl.jdevtool.theme.AppTheme
import com.wxl.jdevtool.theme.AppThemeListener
import io.github.oshai.kotlinlogging.KotlinLogging
import org.fife.ui.rsyntaxtextarea.Theme
import org.springframework.beans.factory.BeanCreationException
import org.springframework.context.ApplicationContext
import org.springframework.core.annotation.AnnotationUtils
import org.springframework.stereotype.Component
import org.springframework.util.ReflectionUtils
import java.awt.event.ActionListener
import java.lang.reflect.Field
import javax.swing.AbstractButton
import javax.swing.event.CaretListener
import javax.swing.text.JTextComponent

/**
 * Create by wuxingle on 2024/01/08
 * JDevlTool启动流程
 */
@Component
class DefaultJDevToolStartupProcess : JDevToolStartupProcess {

    private val log = KotlinLogging.logger { }

    /**
     * 初始化组件注册中心
     */
    override fun initComponentRegistry(context: ApplicationContext) {
        val registry = context.getBean(ComponentRegistry::class.java)
        val beanMap = context.getBeansWithAnnotation(ComponentId::class.java)
        for (bean in beanMap.values) {
            val topComponentId = AnnotationUtils.findAnnotation(bean.javaClass, ComponentId::class.java)!!
            registry.register(topComponentId.value, bean)

            // 遍历字段注册
            val fields = getComponentFields(bean::class.java)
            registerField(registry, topComponentId.value, bean, fields)
        }

        log.info { "init component registry finish: ${registry.allIds}, size: ${registry.size}" }
    }

    private fun registerField(registry: ComponentRegistry, parentId: String, bean: Any, fields: List<Field>) {
        if (fields.isEmpty()) {
            return
        }
        for (field in fields) {
            val componentId = AnnotationUtils.getAnnotation(field, ComponentId::class.java)!!
            field.isAccessible = true
            val component = field.get(bean)

            if (java.awt.Component::class.java.isAssignableFrom(field.type)) {
                registry.register(parentId + "." + componentId.value, component)
            }
            // 递归字段判断
            else {
                val subFields = getComponentFields(field.type)
                registerField(registry, parentId + "." + componentId.value, component, subFields)
            }
        }
    }

    private fun <T> getComponentFields(clazz: Class<T>): List<Field> {
        val fields = arrayListOf<Field>()
        ReflectionUtils.doWithFields(clazz, fields::add) {
            AnnotationUtils.getAnnotation(
                it,
                ComponentId::class.java
            ) != null
        }
        return fields
    }

    /**
     * 初始化组件绑定
     */
    override fun initComponentBind(context: ApplicationContext) {
        val registry = context.getBean(ComponentRegistry::class.java)
        val beanMap = context.getBeansWithAnnotation(ComponentListener::class.java)
        for ((beanName, bean) in beanMap) {
            val componentListener = AnnotationUtils.findAnnotation(bean.javaClass, ComponentListener::class.java)!!
            val ids = componentListener.ids
            for (id in ids) {
                val component = registry.getComponent(id)

                // 注册
                when (bean) {
                    is java.awt.event.ComponentListener -> component.addComponentListener(bean)
                    is ActionListener -> {
                        if (component is AbstractButton) {
                            component.addActionListener(bean)
                        } else {
                            throw BeanCreationException("bean: $beanName is not legal ActionListener")
                        }
                    }

                    is CaretListener -> {
                        if (component is JTextComponent) {
                            component.addCaretListener(bean)
                        } else {
                            throw BeanCreationException("bean: $beanName is not legal CaretListener")
                        }
                    }

                    else -> {
                        throw BeanCreationException("bean: $beanName is not legal ComponentListener")
                    }
                }
            }
        }

        log.info { "init component bind finish, size: ${beanMap.size}" }
    }

    /**
     * 初始化主题
     */
    override fun initTheme(context: ApplicationContext) {
        val textAreaTheme =
            Theme.load(DefaultJDevToolStartupProcess::class.java.getResourceAsStream("/themes/rsyntax_dark.xml"))
        val appTheme = AppTheme(textAreaTheme)
        val themeListeners = context.getBeanProvider(AppThemeListener::class.java).orderedStream().toList()
        for (themeListener in themeListeners) {
            themeListener.themeChange(appTheme)
        }

        log.info { "init component theme finish" }
    }
}
