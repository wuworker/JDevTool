package com.wxl.jdevtool.component.history

/**
 * Create by wuxingle on 2024/05/11
 * 历史列表管理
 */
interface HistoryList {

    /**
     * id
     */
    val id: String

    /**
     * 当前历史记录数
     */
    val size: Int

    /**
     * 最大历史记录条数
     */
    var maxHistorySize: Int

    /**
     * 获取历史
     * @param size 0表示获取全部
     */
    fun getHistory(size: Int = 0): List<String>

    fun getShowHistory() = getHistory(maxHistorySize)

    /**
     * 新增历史
     */
    fun addHistory(text: String)

    fun isEmpty() = size == 0
}