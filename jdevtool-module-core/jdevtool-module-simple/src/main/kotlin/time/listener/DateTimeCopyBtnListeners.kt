package com.wxl.jdevtool.time.listener

import com.wxl.jdevtool.ComponentListener
import com.wxl.jdevtool.time.TimeTabbedModule
import com.wxl.jdevtool.util.ClipboardUtils
import org.springframework.beans.factory.annotation.Autowired
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.JButton
import javax.swing.Timer

/**
 * Create by wuxingle on 2024/01/31
 * 复制内容到剪切板
 */
abstract class AbstractCopyBtnActionListener(
    private val timeTabbedModule: TimeTabbedModule
) : ActionListener {

    override fun actionPerformed(e: ActionEvent) {
        val btn = e.source as? JButton ?: return
        val text = getText()
        if (text.isNotBlank()) {
            ClipboardUtils.setText(text)
            timeTabbedModule.mainPanel.requestFocus()
            btn.isEnabled = false

            val timer = Timer(1000) {
                btn.isEnabled = true
            }
            timer.isRepeats = false
            timer.start()
        }
    }

    abstract fun getText(): String
}

@ComponentListener("timeTabbedModule.nowTimeCopyBtn")
class NowTimeCopyBtnActionListener(
    @Autowired val timeTabbedModule: TimeTabbedModule
) : AbstractCopyBtnActionListener(timeTabbedModule) {

    override fun getText(): String {
        return timeTabbedModule.nowTimeText.text
    }
}

@ComponentListener("timeTabbedModule.nowStampCopyBtn")
class NowStampCopyBtnActionListener(
    @Autowired val timeTabbedModule: TimeTabbedModule
) : AbstractCopyBtnActionListener(timeTabbedModule) {

    override fun getText(): String {
        return timeTabbedModule.nowStampText.text
    }
}

@ComponentListener("timeTabbedModule.stamp2TimeCopyBtn")
class Stamp2TimeCopyBtnActionListener(
    @Autowired val timeTabbedModule: TimeTabbedModule
) : AbstractCopyBtnActionListener(timeTabbedModule) {

    override fun getText(): String {
        return timeTabbedModule.stamp2TimeOutText.text
    }
}

@ComponentListener("timeTabbedModule.time2StampCopyBtn")
class Time2StampCopyBtnActionListener(
    @Autowired val timeTabbedModule: TimeTabbedModule
) : AbstractCopyBtnActionListener(timeTabbedModule) {

    override fun getText(): String {
        return timeTabbedModule.time2StampOutText.text
    }
}

