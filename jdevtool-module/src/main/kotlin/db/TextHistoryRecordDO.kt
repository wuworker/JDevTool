package com.wxl.jdevtool.db

import java.util.*

/**
 * Create by wuxingle on 2024/05/10
 * 文本输入历史记录
 */
class TextHistoryRecordDO {

    var id: Long? = null

    var cid: String? = null

    var content: String? = null

    var createTime: Date? = null

    constructor()

    constructor(cid: String, content: String, createTime: Date) {
        this.cid = cid
        this.content = content
        this.createTime = createTime
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TextHistoryRecordDO

        if (id != other.id) return false
        if (cid != other.cid) return false
        if (content != other.content) return false
        if (createTime != other.createTime) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (cid?.hashCode() ?: 0)
        result = 31 * result + (content?.hashCode() ?: 0)
        result = 31 * result + (createTime?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "TextHistoryRecordDO(id=$id, cid=$cid, content=$content, createTime=$createTime)"
    }


}
