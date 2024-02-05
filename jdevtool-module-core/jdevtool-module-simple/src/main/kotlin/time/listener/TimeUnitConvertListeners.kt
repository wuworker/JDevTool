package com.wxl.jdevtool.time.listener

import com.wxl.jdevtool.ComponentListener
import com.wxl.jdevtool.time.TimeTabbedModule
import org.springframework.beans.factory.annotation.Autowired
import java.awt.event.FocusAdapter
import java.awt.event.FocusEvent

/**
 * Create by wuxingle on 2024/02/02
 * 时间单位换算监听
 */
@ComponentListener("timeTabbedModule.timeSText")
class TimeSecondFocusListener(
    @Autowired val timeTabbedModule: TimeTabbedModule
) : FocusAdapter() {

    override fun focusLost(e: FocusEvent) {
        val text = timeTabbedModule.timeSText.text
        if (text.isBlank()) {
            return
        }
        val s = text.toLong()

        val m = s / 60
        val ms = s % 60
        val h = s / 3600
        val hm = s % 3600 / 60
        val hms = s % 3600 % 60

        timeTabbedModule.timeMText.text = m.toString()
        timeTabbedModule.timeMSText.text = ms.toString()
        timeTabbedModule.timeHText.text = h.toString()
        timeTabbedModule.timeHMText.text = hm.toString()
        timeTabbedModule.timeHMSText.text = hms.toString()
    }
}

@ComponentListener("timeTabbedModule.timeMText", "timeTabbedModule.timeMSText")
class TimeMinuteFocusListener(
    @Autowired val timeTabbedModule: TimeTabbedModule
) : FocusAdapter() {

    override fun focusLost(e: FocusEvent?) {
        val mText = timeTabbedModule.timeMText.text
        val msText = timeTabbedModule.timeMSText.text
        if (mText.isBlank() || msText.isBlank()) {
            return
        }
        val m = mText.toLong()
        val ms = msText.toLong()

        val s = m * 60 + ms
        val h = s / 3600
        val hm = s % 3600 / 60
        val hms = s % 3600 % 60


        timeTabbedModule.timeSText.text = s.toString()
        timeTabbedModule.timeHText.text = h.toString()
        timeTabbedModule.timeHMText.text = hm.toString()
        timeTabbedModule.timeHMSText.text = hms.toString()
    }
}

@ComponentListener(
    "timeTabbedModule.timeHText",
    "timeTabbedModule.timeHMText",
    "timeTabbedModule.timeHMSText"
)
class TimeHourFocusListener(
    @Autowired val timeTabbedModule: TimeTabbedModule
) : FocusAdapter() {

    override fun focusLost(e: FocusEvent?) {
        val hText = timeTabbedModule.timeHText.text
        val hmText = timeTabbedModule.timeHMText.text
        val hmsText = timeTabbedModule.timeHMSText.text
        if (hText.isBlank() || hmText.isBlank() || hmsText.isBlank()) {
            return
        }

        val h = timeTabbedModule.timeHText.text.toLong()
        val hm = timeTabbedModule.timeHMText.text.toLong()
        val hms = timeTabbedModule.timeHMSText.text.toLong()

        val s = h * 3600 + hm * 60 + hms
        val m = s / 60
        val ms = s % 60

        timeTabbedModule.timeSText.text = s.toString()
        timeTabbedModule.timeMText.text = m.toString()
        timeTabbedModule.timeMSText.text = ms.toString()
    }
}
