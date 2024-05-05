package com.wxl.jdevtool.dubbo.listener

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.wxl.jdevtool.ComponentListener
import com.wxl.jdevtool.dubbo.DubboTabbedModule
import com.wxl.jdevtool.toast.ToastType
import com.wxl.jdevtool.toast.Toasts
import io.github.oshai.kotlinlogging.KotlinLogging
import org.apache.dubbo.config.bootstrap.DubboBootstrap
import org.apache.dubbo.rpc.RpcContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.util.NumberUtils
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.SwingUtilities

/**
 * Create by wuxingle on 2024/04/23
 * dubbo执行
 */
@ComponentListener("dubboTabbedModule.methodExeBtn")
class DubboMethodInvokeBtnListener(
    @Autowired val dubboTabbedModule: DubboTabbedModule
) : ActionListener {

    private val log = KotlinLogging.logger {}

    private val gson = GsonBuilder().setPrettyPrinting().create()

    override fun actionPerformed(e: ActionEvent?) {
        val applicationConfig = try {
            dubboTabbedModule.appConfigPanel.config
        } catch (e: Exception) {
            Toasts.show(ToastType.ERROR, "应用配置错误:${e.message}")
            return
        }

        val registryConfig = try {
            dubboTabbedModule.registerConfigPanel.config
        } catch (e: Exception) {
            Toasts.show(ToastType.ERROR, "注册中心配置错误:${e.message}")
            return
        }

        val referenceConfig = try {
            dubboTabbedModule.referenceConfigPanel.config
        } catch (e: Exception) {
            Toasts.show(ToastType.ERROR, "消费者配置错误:${e.message}")
            return
        }

        val attachments = try {
            dubboTabbedModule.attachmentConfigPanel.config
        } catch (e: Exception) {
            Toasts.show(ToastType.ERROR, "附加参数配置错误:${e.message}")
            return
        }

        DubboBootstrap.getInstance()
            .application(applicationConfig)
            .registry(registryConfig)
            .reference(referenceConfig)

        // 请求相关
        val method = dubboTabbedModule.methodField.text
        if (method.isNullOrBlank()) {
            Toasts.show(ToastType.ERROR, "请求方法不能为空")
            return
        }
        val pts = dubboTabbedModule.paramTypeField.copiedComponents.map { it.text }.toTypedArray()
        val args = dubboTabbedModule.paramTextArea.copiedComponents.map { it.text }

        SwingUtilities.invokeLater {
            try {
                val realArgs = getArgs(pts, args)

                val service = referenceConfig.get()

                // 设置附加参数
                val context = RpcContext.getContext()
                for (attachment in attachments.entries) {
                    context.setAttachment(attachment.key, attachment.value)
                }
                val res = service.`$invoke`(method, pts, realArgs)

                if (res == null) {
                    dubboTabbedModule.resultArea.text = "null"
                } else {
                    dubboTabbedModule.resultArea.text = gson.toJson(res)
                }
            } catch (e: Exception) {
                log.error(e) { "dubbo invoke error: ${referenceConfig.`interface`}" }
                Toasts.show(ToastType.ERROR, e.message ?: "执行失败")
            }
        }
    }


    private fun getArgs(pts: Array<String>, args: List<String>): Array<Any?> {
        if (pts.size != args.size) {
            throw IllegalArgumentException("参数类型和参数值个数必须相等")
        }
        if (pts.isEmpty()) {
            return emptyArray()
        }

        val realArgs = arrayListOf<Any?>()
        for (index in pts.indices) {
            val type = pts[index]
            val arg = args[index]

            if (arg.isEmpty()) {
                realArgs.add(null)
                continue
            }

            if (type.startsWith("java")) {
                val clazz = try {
                    Class.forName(type)
                } catch (e: Exception) {
                    throw IllegalArgumentException("不支持该类型：${type}")
                }

                val realArg: Any
                if (Number::class.java.isAssignableFrom(clazz)) {
                    realArg = NumberUtils.parseNumber(arg, clazz as Class<out Number>)
                } else if (CharSequence::class.java.isAssignableFrom(clazz)) {
                    realArg = arg
                } else if (List::class.java.isAssignableFrom(clazz)) {
                    val obj: List<Any?> = try {
                        gson.fromJson(arg, object : TypeToken<List<Any?>>() {})
                    } catch (e: Exception) {
                        throw IllegalArgumentException("参数值解析失败：${arg}")
                    }
                    realArg = obj
                } else if (Set::class.java.isAssignableFrom(clazz)) {
                    val obj: Set<Any?> = try {
                        gson.fromJson(arg, object : TypeToken<Set<Any?>>() {})
                    } catch (e: Exception) {
                        throw IllegalArgumentException("参数值解析失败：${arg}")
                    }
                    realArg = obj
                } else if (Map::class.java.isAssignableFrom(clazz)) {
                    val obj: Map<String, Any?> = try {
                        gson.fromJson(arg, object : TypeToken<Map<String, Any?>>() {})
                    } catch (e: Exception) {
                        throw IllegalArgumentException("参数值解析失败：${arg}")
                    }
                    realArg = obj
                } else {
                    throw IllegalArgumentException("不支持该类型：${type}")
                }
                realArgs.add(realArg)

            } else {
                val obj: Map<String, Any?> = try {
                    gson.fromJson(arg, object : TypeToken<Map<String, Any?>>() {})
                } catch (e: Exception) {
                    throw IllegalArgumentException("参数值解析失败：${arg}")
                }

                realArgs.add(obj)
            }
        }

        return realArgs.toArray()
    }

}

