package com.wxl.jdevtool.note.db

import com.google.gson.reflect.TypeToken
import com.wxl.jdevtool.AppContexts
import java.util.*

/**
 * Create by wuxingle on 2024/05/13
 * 记事本文件
 */
class NoteFileDO {

    var id: Long? = null

    // 文件名
    var name: String? = null

    // 文件类型
    var type: Int? = null

    // 文件内容
    var content: String? = null

    // 属性，k-v, json结构
    var attrs: String? = null

    var createTime: Date? = null

    var updateTime: Date? = null

    constructor()

    constructor(name: String?, type: Int?, content: String?, createTime: Date?, updateTime: Date?) {
        this.name = name
        this.type = type
        this.content = content
        this.createTime = createTime
        this.updateTime = updateTime
    }

    fun getAttrMap(): Map<String, String?> {
        if (attrs.isNullOrBlank()) {
            return emptyMap()
        }
        return AppContexts.gson.fromJson(attrs, object : TypeToken<Map<String, String?>>() {
        }.type)
    }

    override fun toString(): String {
        return "NoteFileDO(id=$id, name=$name, type=$type, content=$content, attrs=$attrs, createTime=$createTime, updateTime=$updateTime)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NoteFileDO

        if (id != other.id) return false
        if (name != other.name) return false
        if (type != other.type) return false
        if (content != other.content) return false
        if (attrs != other.attrs) return false
        if (createTime != other.createTime) return false
        if (updateTime != other.updateTime) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (type ?: 0)
        result = 31 * result + (content?.hashCode() ?: 0)
        result = 31 * result + (attrs?.hashCode() ?: 0)
        result = 31 * result + (createTime?.hashCode() ?: 0)
        result = 31 * result + (updateTime?.hashCode() ?: 0)
        return result
    }

}

