package com.wxl.jdevtool.component.history

import com.wxl.jdevtool.AppContexts
import com.wxl.jdevtool.db.TextHistoryRecordDO
import com.wxl.jdevtool.db.mapper.TextHistoryRecordMapper
import io.github.oshai.kotlinlogging.KotlinLogging
import java.util.*

/**
 * Create by wuxingle on 2024/05/11
 * 历史记录存储到本地
 */
class StorageHistoryList(
    override val id: String
) : HistoryList {

    private val log = KotlinLogging.logger { }

    private val history by lazy {
        val mapper = AppContexts.getMapper(TextHistoryRecordMapper::class.java)
        val historyRecordList = mapper.selectByCid(id)
        if (historyRecordList.isEmpty()) {
            return@lazy Collections.synchronizedList(arrayListOf<String>())
        }
        log.info { "loadHistory $id: $historyRecordList" }

        // 加载历史，去重
        val h = linkedSetOf<String>()
        val maxSize = maxHistorySize
        val delIds = arrayListOf<Long>()
        for ((i, record) in historyRecordList.withIndex()) {
            if (h.size >= maxSize) {
                delIds.addAll(historyRecordList.subList(i, historyRecordList.size).map { it.id!! }.toList())
                break
            }
            val c = record.content
            if (c.isNullOrBlank() || !h.add(c)) {
                delIds.add(record.id!!)
            }
        }

        // 删除重复或者不需要的
        if (delIds.isNotEmpty()) {
            AppContexts.executeSqlAsync(TextHistoryRecordMapper::class.java) {
                it.deleteByIds(delIds)
            }
        }

        log.info { "loadHistory real $id: $h" }
        Collections.synchronizedList(ArrayList(h))
    }

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
            if (history.isNotEmpty() && history[0] == text) {
                return
            }

            history.remove(text)
            history.add(0, text)
            if (history.size > maxHistorySize) {
                history.removeAt(history.lastIndex)
            }
        }

        // 数据落库
        val date = Date()
        AppContexts.executeSqlAsync(TextHistoryRecordMapper::class.java) {
            val recordDO = TextHistoryRecordDO(id, text, date)
            it.batchInsert(arrayListOf(recordDO))

            log.info { "addHistory save $id: $text" }
        }
    }
}

