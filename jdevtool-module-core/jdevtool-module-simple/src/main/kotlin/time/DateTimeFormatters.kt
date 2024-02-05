package com.wxl.jdevtool.time

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

/**
 * Create by wuxingle on 2024/01/31
 * 日期格式化
 */
object DateTimeFormatters {

    private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")

    fun formatDateTime(dateTime: LocalDateTime): String {
        return dateTimeFormatter.format(dateTime)
    }

    fun formatDate(dateTime: LocalDateTime): String {
        return dateFormatter.format(dateTime)
    }

    fun formatDate(dateTime: LocalDate): String {
        return dateFormatter.format(dateTime)
    }

    fun formatTime(dateTime: LocalDateTime): String {
        return timeFormatter.format(dateTime)
    }

    fun formatTime(dateTime: LocalTime): String {
        return timeFormatter.format(dateTime)
    }

    fun parseDateTime(dateTime: String): LocalDateTime {
        return LocalDateTime.parse(dateTime, dateTimeFormatter)
    }

    fun parseDate(dateTime: String): LocalDate {
        val values = dateTime.split("-")
        if (values.size != 3) {
            throw DateTimeParseException("parse fail", dateTime, 0)
        }
        val sb = StringBuilder()
        // 月日不足两位的补充到2位
        for ((idx, value) in values.withIndex()) {
            if (idx == 0) {
                sb.append(value).append("-")
                continue
            }
            if (value.length > 2 || value.isEmpty()) {
                throw DateTimeParseException("parse fail", value, 0)
            }
            if (value.length == 1) {
                sb.append("0")
            }
            sb.append(value).append("-")
        }
        return LocalDate.parse(sb.subSequence(0, sb.length - 1), dateFormatter)
    }

    fun parseTime(dateTime: String): LocalTime {
        val values = dateTime.split(":")
        if (values.size != 3) {
            throw DateTimeParseException("parse fail", dateTime, 0)
        }
        val sb = StringBuilder()
        // 不足两位的补充到2位
        for (value in values) {
            if (value.length > 2 || value.isEmpty()) {
                throw DateTimeParseException("parse fail", value, 0)
            }
            if (value.length == 1) {
                sb.append("0")
            }
            sb.append(value).append(":")
        }

        return LocalTime.parse(sb.subSequence(0, sb.length - 1), timeFormatter)
    }

    fun formatWeek(date: LocalDateTime): String {
        val weeks = arrayOf("星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日")
        return weeks[date.dayOfWeek.value - 1]
    }
}
