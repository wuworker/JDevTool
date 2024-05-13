package com.wxl.jdevtool.component.history

import java.util.*

/**
 * Create by wuxingle on 2024/05/11
 * 使用内存缓存历史记录
 */
class ArrayHistoryList(
    override val id: String
) : HistoryList {

    private val history = Collections.synchronizedList(arrayListOf<String>())

    /**
     * 当前历史记录数
     */
    override val size: Int
        get() = history.size

    /**
     * 最大历史记录条数
     */
    override var maxHistorySize = 5

    /**
     * 获取历史
     * @param size 0表示获取全部
     */
    override fun getHistory(size: Int): List<String> {
        synchronized(history) {
            if (history.size <= size) {
                return history.toList()
            }

            return history.subList(0, size).toList()
        }
    }

    /**
     * 新增历史
     */
    override fun addHistory(text: String) {
        synchronized(history) {
            history.remove(text)
            history.add(0, text)
            if (history.size > maxHistorySize) {
                history.removeAt(history.lastIndex)
            }
        }
    }
}
