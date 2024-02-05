package com.wxl.jdevtool.time.listener

import com.wxl.jdevtool.ComponentListener
import com.wxl.jdevtool.time.DateTimeFormatters
import com.wxl.jdevtool.time.TimeTabbedModule
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import java.awt.event.FocusAdapter
import java.awt.event.FocusEvent
import java.time.Period
import kotlin.math.abs

/**
 * Create by wuxingle on 2024/02/04
 * 日期相差监听
 */
@ComponentListener(
    "timeTabbedModule.dateDiffInText1",
    "timeTabbedModule.dateDiffInText2"
)
class DateDiffTextFieldFocusListener(
    @Autowired val timeTabbedModule: TimeTabbedModule
) : FocusAdapter() {

    private val log = KotlinLogging.logger { }

    override fun focusLost(e: FocusEvent?) {
        val in1 = timeTabbedModule.dateDiffInText1.text
        val in2 = timeTabbedModule.dateDiffInText2.text

        if (in1.isBlank() || in2.isBlank()) {
            return
        }

        val localDate1 = try {
            DateTimeFormatters.parseDate(in1)
        } catch (e: Exception) {
            log.info(e) { "parse date error: $in1" }
            return
        }

        val localDate2 = try {
            DateTimeFormatters.parseDate(in2)
        } catch (e: Exception) {
            log.info(e) { "parse date error: $in2" }
            return
        }

        val period = if (localDate1.isAfter(localDate2))
            Period.between(localDate2, localDate1)
        else
            Period.between(localDate1, localDate2)

        val sb = StringBuilder()
        if (period.years > 0) {
            sb.append("${period.years}年")
        }
        if (period.months > 0) {
            sb.append("${period.months}月")
        }
        sb.append("${period.days}天")
        if (period.years > 0 || period.months > 0) {
            val diffDay = localDate1.toEpochDay() - localDate2.toEpochDay()
            sb.append("（共${abs(diffDay)}天）")
        }

        timeTabbedModule.dateDiffOutText.text = sb.toString()
    }
}