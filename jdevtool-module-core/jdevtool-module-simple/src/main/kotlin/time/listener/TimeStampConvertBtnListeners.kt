package com.wxl.jdevtool.time.listener

import com.wxl.jdevtool.ComponentListener
import com.wxl.jdevtool.time.DateTimeFormatters
import com.wxl.jdevtool.time.TimeTabbedModule
import org.springframework.beans.factory.annotation.Autowired
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.time.*

/**
 * Create by wuxingle on 2024/01/31
 * 时间转换按钮
 */
@ComponentListener("timeTabbedModule.stamp2TimeConvertBtn")
class Stamp2TimeBtnActionListener(
    @Autowired val timeTabbedModule: TimeTabbedModule
) : ActionListener {

    override fun actionPerformed(e: ActionEvent) {
        val inText = timeTabbedModule.stamp2TimeInText.text.trim()
        if (inText.isBlank()) {
            return
        }

        try {
            val selectedZone = timeTabbedModule.getSelectedZone()
            val localDateTime = Instant.ofEpochMilli(inText.toLong()).atZone(selectedZone).toLocalDateTime()
            timeTabbedModule.stamp2TimeOutText.text = DateTimeFormatters.formatDateTime(localDateTime)
        } catch (e: Exception) {
            timeTabbedModule.stamp2TimeOutText.text = "输入格式错误"
        }
    }
}

@ComponentListener("timeTabbedModule.time2StampConvertBtn")
class Time2StampBtnActionListener(
    @Autowired val timeTabbedModule: TimeTabbedModule
) : ActionListener {

    override fun actionPerformed(e: ActionEvent) {
        val inText = timeTabbedModule.time2StampInText.text.trim()
        if (inText.isBlank()) {
            return
        }

        val selectedZone = timeTabbedModule.getSelectedZone()

        var ms: Long? = null
        try {
            if (inText.length > 11) {
                val dateTime = DateTimeFormatters.parseDateTime(inText)
                ms = ZonedDateTime.of(dateTime, selectedZone).toInstant().toEpochMilli()
            } else if (inText.contains("-")) {
                val dateTime = LocalDateTime.of(DateTimeFormatters.parseDate(inText), LocalTime.MIN)
                ms = ZonedDateTime.of(dateTime, selectedZone).toInstant().toEpochMilli()
            } else if (inText.contains(":")) {
                val dateTime = LocalDateTime.of(LocalDate.now(), DateTimeFormatters.parseTime(inText))
                ms = ZonedDateTime.of(dateTime, selectedZone).toInstant().toEpochMilli()
            }
        } catch (e: Exception) {
            //ignore
        }

        timeTabbedModule.time2StampOutText.text = ms?.toString() ?: "输入格式错误"
    }
}
