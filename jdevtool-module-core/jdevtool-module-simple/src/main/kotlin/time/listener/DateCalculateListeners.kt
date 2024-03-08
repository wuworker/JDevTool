package com.wxl.jdevtool.time.listener

import com.wxl.jdevtool.ComponentListener
import com.wxl.jdevtool.time.DateTimeFormatters
import com.wxl.jdevtool.time.DateUnit
import com.wxl.jdevtool.time.TimeOp
import com.wxl.jdevtool.time.TimeTabbedModule
import com.wxl.jdevtool.toast.ToastType
import com.wxl.jdevtool.toast.Toasts
import org.springframework.beans.factory.annotation.Autowired
import java.awt.event.FocusAdapter
import java.awt.event.FocusEvent
import java.awt.event.ItemEvent
import java.awt.event.ItemListener
import java.time.temporal.ChronoUnit

/**
 * Create by wuxingle on 2024/02/04
 * 日期计算
 */

/**
 * 输入失去焦点时计算
 */
@ComponentListener(
    "timeTabbedModule.dateCalculateInText",
    "timeTabbedModule.dateUnitText"
)
class DateCalculateTextFieldFocusListener(
    @Autowired val timeTabbedModule: TimeTabbedModule
) : FocusAdapter() {

    override fun focusLost(e: FocusEvent) {
        calculateDate(timeTabbedModule)
    }
}

/**
 * 选项变化时计算
 */
@ComponentListener(
    "timeTabbedModule.dateOpComboBox",
    "timeTabbedModule.dateUnitComboBox"
)
class DateCalculateComboBoxItemListener(
    @Autowired val timeTabbedModule: TimeTabbedModule
) : ItemListener {

    override fun itemStateChanged(e: ItemEvent) {
        if (e.stateChange == ItemEvent.SELECTED) {
            calculateDate(timeTabbedModule)
        }
    }
}

private fun calculateDate(timeTabbedModule: TimeTabbedModule) {
    val inText = timeTabbedModule.dateCalculateInText.text
    val op = timeTabbedModule.dateOpComboBox.selectedItem as TimeOp
    val unitText = timeTabbedModule.dateUnitText.text
    val dateUnit = timeTabbedModule.dateUnitComboBox.selectedItem as DateUnit

    if (inText.isBlank() || unitText.isBlank()) {
        return
    }

    val localDate = try {
        DateTimeFormatters.parseDate(inText)
    } catch (e: Exception) {
        timeTabbedModule.dateCalculateChecker.showWarn(false)
        Toasts.show(ToastType.ERROR, "日期格式错误")
        return
    }

    val n = try {
        unitText.toLong()
    } catch (e: Exception) {
        return
    }

    val resultDate = localDate.plus(
        if (op == TimeOp.ADD) n else -n,
        when (dateUnit) {
            DateUnit.DAY -> ChronoUnit.DAYS
            DateUnit.WEEK -> ChronoUnit.WEEKS
            DateUnit.MONTH -> ChronoUnit.MONTHS
            DateUnit.YEAR -> ChronoUnit.YEARS
        }
    )

    timeTabbedModule.dateCalculateOutText.text = DateTimeFormatters.formatDate(resultDate)
}
