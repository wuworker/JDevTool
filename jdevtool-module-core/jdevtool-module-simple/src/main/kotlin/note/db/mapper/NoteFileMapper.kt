package com.wxl.jdevtool.note.db.mapper

import com.wxl.jdevtool.note.db.NoteFileDO
import org.apache.ibatis.annotations.*
import java.util.*

/**
 * Create by wuxingle on 2024/05/13
 * 记事本
 */
@Mapper
interface NoteFileMapper {

    @Insert(
        """
            insert into note(name, type, content, attrs, create_time, update_time) values
            (#{name}, #{type}, #{content}, #{attrs}, #{createTime}, #{updateTime})
        """
    )
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    fun insert(file: NoteFileDO)

    @Select(
        """
            select id, name, type from note order by id desc
        """
    )
    fun selectList(): List<NoteFileDO>

    @Select(
        """
            select id, name, type, content, attrs from note where id = #{id}
        """
    )
    fun selectDetailById(@Param("id") id: Long): NoteFileDO?

    @Update(
        """
            update note set content = #{content}, update_time = #{updateTime} where id = #{id}
        """
    )
    fun updateContentById(
        @Param("id") id: Long,
        @Param("content") content: String,
        @Param("updateTime") updateTime: Date
    )

    @Update(
        """
            update note set name = #{name}, update_time = #{updateTime} where id = #{id}
        """
    )
    fun updateNameById(
        @Param("id") id: Long,
        @Param("name") name: String,
        @Param("updateTime") updateTime: Date
    )

    @Update(
        """
            update note set attrs = #{attrs}, update_time = #{updateTime} where id = #{id}
        """
    )
    fun updateAttrsById(@Param("id") id: Long, @Param("attrs") attrs: String, @Param("updateTime") updateTime: Date)

    @Delete(
        """
            delete from note where id = #{id}
        """
    )
    fun deleteById(@Param("id") id: Long)
}
