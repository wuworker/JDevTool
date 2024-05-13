package com.wxl.jdevtool.db.mapper

import com.wxl.jdevtool.db.TextHistoryRecordDO
import org.apache.ibatis.annotations.*

/**
 * Create by wuxingle on 2024/05/10
 * 文本输入历史记录
 */
@Mapper
interface TextHistoryRecordMapper {

    @InsertProvider(value = BatchInsertProvider::class, method = "batchInsert")
    fun batchInsert(list: List<TextHistoryRecordDO>)

    @Select(
        """
        select id, cid, content, create_time 
        from text_history_record 
        where cid = #{cid} 
        order by id desc
    """
    )
    fun selectByCid(@Param("cid") cid: String): List<TextHistoryRecordDO>

    @DeleteProvider(value = BatchDeleteProvider::class, method = "batchDelete")
    fun deleteByIds(@Param("ids") id: List<Long>)
}

class BatchInsertProvider {

    fun batchInsert(list: List<TextHistoryRecordDO>): String {
        val sql = StringBuilder()
        sql.append(
            """
            insert into text_history_record(cid, content, create_time) values
        """.trimIndent()
        )
        for (i in list.indices) {
            sql.append("(#{list[$i].cid}, #{list[$i].content}, #{list[$i].createTime})")
            if (i < list.size - 1) {
                sql.append(",")
            }
        }
        return sql.toString()
    }
}

class BatchDeleteProvider {

    fun batchDelete(ids: List<Long>): String {
        val sql = StringBuilder()
        sql.append(
            """
            delete from text_history_record where id in (
        """.trimIndent()
        )
        for (i in ids.indices) {
            sql.append("#{ids[$i]}")
            if (i < ids.size - 1) {
                sql.append(",")
            }
        }
        return sql.append(")").toString()
    }
}
