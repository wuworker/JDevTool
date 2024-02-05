package com.wxl.jdevtool.time.listener

import com.wxl.jdevtool.ComponentListener
import com.wxl.jdevtool.time.DateTimeFormatters
import com.wxl.jdevtool.time.TimeTabbedModule
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import java.awt.event.FocusAdapter
import java.awt.event.FocusEvent
import kotlin.math.abs

/**
 * Create by wuxingle on 2024/02/04
 * 时间相差监听
 */

@ComponentListener(
    "timeTabbedModule.timeDiffInText1",
    "timeTabbedModule.timeDiffInText2"
)
class TimeDiffTextFieldFocusListener(
    @Autowired val timeTabbedModule: TimeTabbedModule
) : FocusAdapter() {

    private val log = KotlinLogging.logger { }

    override fun focusLost(e: FocusEvent?) {
        val text1 = timeTabbedModule.timeDiffInText1.text
        val text2 = timeTabbedModule.timeDiffInText2.text

        if (text1.isBlank() || text2.isBlank()) {
            return
        }

        val localTime1 = try {
            DateTimeFormatters.parseTime(text1)
        } catch (e: Exception) {
            log.info(e) { "parse time error:$text1" }
            return
        }

        val localTime2 = try {
            DateTimeFormatters.parseTime(text2)
        } catch (e: Exception) {
            log.info(e) { "parse time error:$text2" }
            return
        }

        val s1 = localTime1.toSecondOfDay()
        val s2 = localTime2.toSecondOfDay()
        val diff = abs(s1 - s2)

        // 转为时分秒
        val h = diff / 3600
        val m = diff % 3600 / 60
        val s = diff % 3600 % 60

        val sb = StringBuilder()
        if (h > 0) {
            sb.append("${h}小时")
        }
        if (m > 0) {
            sb.append("${m}分钟")
        }
        if (s > 0) {
            sb.append("${s}秒")
        }

        if (h > 0 || m > 0) {
            sb.append("（共${diff}秒）")
        }
        timeTabbedModule.timeDiffOutText.text = sb.toString()
    }
}

