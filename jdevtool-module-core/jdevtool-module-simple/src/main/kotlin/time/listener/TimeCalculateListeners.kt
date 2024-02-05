package com.wxl.jdevtool.time.listener

import com.wxl.jdevtool.ComponentListener
import com.wxl.jdevtool.time.DateTimeFormatters
import com.wxl.jdevtool.time.TimeOp
import com.wxl.jdevtool.time.TimeTabbedModule
import com.wxl.jdevtool.time.TimeUnit
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import java.awt.event.FocusAdapter
import java.awt.event.FocusEvent
import java.awt.event.ItemEvent
import java.awt.event.ItemListener
import java.time.temporal.ChronoUnit

/**
 * Create by wuxingle on 2024/02/04
 * 时间计算
 */

/**
 * 输入失去焦点时计算
 */
@ComponentListener(
    "timeTabbedModule.timeCalculateInText",
    "timeTabbedModule.timeUnitText"
)
class TimeCalculateTextFieldFocusListener(
    @Autowired val timeTabbedModule: TimeTabbedModule
) : FocusAdapter() {

    override fun focusLost(e: FocusEvent) {
        calculateTime(timeTabbedModule)
    }
}

/**
 * 选项变化时计算
 */
@ComponentListener(
    "timeTabbedModule.timeOpComboBox",
    "timeTabbedModule.timeUnitComboBox"
)
class TimeCalculateComboBoxItemListener(
    @Autowired val timeTabbedModule: TimeTabbedModule
) : ItemListener {

    override fun itemStateChanged(e: ItemEvent) {
        calculateTime(timeTabbedModule)
    }
}

private val log = KotlinLogging.logger { }

private fun calculateTime(timeTabbedModule: TimeTabbedModule) {
    val inText = timeTabbedModule.timeCalculateInText.text
    val op = timeTabbedModule.timeOpComboBox.selectedItem as TimeOp
    val unitText = timeTabbedModule.timeUnitText.text
    val timeUnit = timeTabbedModule.timeUnitComboBox.selectedItem as TimeUnit

    if (inText.isBlank() || unitText.isBlank()) {
        return
    }

    val localTime = try {
        DateTimeFormatters.parseTime(inText)
    } catch (e: Exception) {
        log.info(e) { "parse time error: $inText" }
        return
    }
    val n = try {
        unitText.toLong()
    } catch (e: Exception) {
        log.info(e) { "parse time unit error: $unitText" }
        return
    }

    val resultTime = localTime.plus(
        if (op == TimeOp.ADD) n else -n,
        when (timeUnit) {
            TimeUnit.HOUR -> ChronoUnit.HOURS
            TimeUnit.MINUTE -> ChronoUnit.MINUTES
            TimeUnit.SECOND -> ChronoUnit.SECONDS
        }
    )

    timeTabbedModule.timeCalculateOutText.text = DateTimeFormatters.formatTime(resultTime)
}
