package com.wxl.jdevtool.time

import com.formdev.flatlaf.extras.FlatSVGIcon
import com.wxl.jdevtool.ComponentId
import com.wxl.jdevtool.TabbedModule
import com.wxl.jdevtool.extension.*
import com.wxl.jdevtool.message.MessageBar
import com.wxl.jdevtool.validate.InputChecker
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import java.awt.Color
import java.awt.FlowLayout
import java.awt.Font
import java.awt.event.FocusEvent
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.swing.*
import javax.swing.event.DocumentEvent
import javax.swing.text.JTextComponent

/**
 * Create by wuxingle on 2024/01/29
 * 时间模块
 */
@Order(300)
@Component
@ComponentId("timeTabbedModule")
class TimeTabbedModule : TabbedModule {

    private val dateTimeChineseFormatter = DateTimeFormatter.ofPattern("yyyy年MM月dd号 HH点mm分ss秒")

    override val mainPanel = JPanel(FlowLayout(FlowLayout.LEFT))

    // 时区选择，当前时间
    val timeZoneCombox = JComboBox(TimeZoneEnum.values())

    val formatTimeLabel = JLabel()

    val nowTimeText = JTextField(20)

    val nowStampText = JTextField(20)

    // 时间戳转换
    val stamp2TimeInText = createJTextField(20, TextInputType.NUMBER)

    val stamp2TimeInChecker: InputChecker = NotBlankInputChecker(stamp2TimeInText)

    @ComponentId("stamp2TimeConvertBtn")
    val stamp2TimeConvertBtn = JButton("转换")

    val stamp2TimeOutText = createJTextField(20, null)

    val time2StampInText = createJTextField(20, TextInputType.DATETIME)

    val time2StampInChecker: InputChecker = NotBlankInputChecker(time2StampInText)

    @ComponentId("time2StampConvertBtn")
    val time2StampConvertBtn = JButton("转换")

    val time2StampOutText = createJTextField(20, null)

    // 日期计算
    @ComponentId("dateCalculateInText")
    val dateCalculateInText = createJTextField(10, TextInputType.DATE)

    val dateCalculateChecker: InputChecker = AllTrueInputChecker(dateCalculateInText)

    @ComponentId("dateOpComboBox")
    val dateOpComboBox = JComboBox(TimeOp.values())

    @ComponentId("dateUnitText")
    val dateUnitText = createJTextField(4, TextInputType.NUMBER)

    @ComponentId("dateUnitComboBox")
    val dateUnitComboBox = JComboBox(DateUnit.values())

    val dateCalculateOutText = createJTextField(10, null)

    @ComponentId("dateDiffInText1")
    val dateDiffInText1 = createJTextField(10, TextInputType.DATE)

    val dateDiffChecker1: InputChecker = AllTrueInputChecker(dateDiffInText1)

    @ComponentId("dateDiffInText2")
    val dateDiffInText2 = createJTextField(10, TextInputType.DATE)

    val dateDiffChecker2: InputChecker = AllTrueInputChecker(dateDiffInText2)

    val dateDiffOutText = createJTextField(15, null)

    // 时间计算
    @ComponentId("timeCalculateInText")
    val timeCalculateInText = createJTextField(10, TextInputType.TIME)

    val timeCalculateChecker: InputChecker = AllTrueInputChecker(timeCalculateInText)

    @ComponentId("timeOpComboBox")
    val timeOpComboBox = JComboBox(TimeOp.values())

    @ComponentId("timeUnitText")
    val timeUnitText = createJTextField(4, TextInputType.NUMBER)

    @ComponentId("timeUnitComboBox")
    val timeUnitComboBox = JComboBox(TimeUnit.values())

    val timeCalculateOutText = createJTextField(10, null)

    @ComponentId("timeDiffInText1")
    val timeDiffInText1 = createJTextField(10, TextInputType.TIME)

    val timeDiffChecker1: InputChecker = AllTrueInputChecker(timeDiffInText1)

    @ComponentId("timeDiffInText2")
    val timeDiffInText2 = createJTextField(10, TextInputType.TIME)

    val timeDiffChecker2: InputChecker = AllTrueInputChecker(timeDiffInText2)

    val timeDiffOutText = createJTextField(20, null)

    @ComponentId("timeSText")
    val timeSText = createJTextField(5, TextInputType.NUMBER)

    @ComponentId("timeMText")
    val timeMText = createJTextField(4, TextInputType.NUMBER)

    @ComponentId("timeMSText")
    val timeMSText = createJTextField(4, TextInputType.NUMBER)

    @ComponentId("timeHText")
    val timeHText = createJTextField(4, TextInputType.NUMBER)

    @ComponentId("timeHMText")
    val timeHMText = createJTextField(4, TextInputType.NUMBER)

    @ComponentId("timeHMSText")
    val timeHMSText = createJTextField(4, TextInputType.NUMBER)

    private val timer: Timer by lazy {
        Timer(1000) {
            showCurrentTime()
        }
    }

    /**
     * 初始化布局
     */
    override fun afterPropertiesSet() {
        // 时区选择
        val timeZonePanel = JPanel(FlowLayout(FlowLayout.LEFT))
        // 默认选到东八区
        timeZoneCombox.selectedItem = TimeZoneEnum.UTC_P8
        with(timeZonePanel) {
            add(JLabel("选择时区："))
            add(timeZoneCombox)
        }
        // 当前时间
        val formatTimePanel = JPanel(FlowLayout(FlowLayout.LEFT))
        with(formatTimeLabel) {
            font = Font(font.name, Font.BOLD, (font.size * 1.5).toInt())
            foreground = Color(255, 198, 109)
        }
        formatTimePanel.add(formatTimeLabel)

        // 时间和时间戳
        val nowTimeTitlePanel = JPanel(FlowLayout(FlowLayout.LEFT))
        nowTimeTitlePanel.add(JLabel(">> 当前时间戳"))

        val nowTimePanel = JPanel(FlowLayout(FlowLayout.LEFT))
        nowTimeText.isEditable = false
        nowTimeText.showCopy()
        with(nowTimePanel) {
            add(JLabel("现在的当地时间："))
            add(nowTimeText)
        }

        val nowStampPanel = JPanel(FlowLayout(FlowLayout.LEFT))
        nowStampText.isEditable = false
        nowStampText.showCopy()

        with(nowStampPanel) {
            add(JLabel("现在的时间戳："))
            add(nowStampText)
        }

        // 时间戳转换
        val stamp2TimeTitlePanel = JPanel(FlowLayout(FlowLayout.LEFT))
        stamp2TimeTitlePanel.add(JLabel(">> 时间戳和当地时间转换"))

        val stamp2TimePanel = JPanel(FlowLayout(FlowLayout.LEFT))
        stamp2TimeInText.setHint(System.currentTimeMillis().toString())
        with(stamp2TimePanel) {
            add(stamp2TimeInText)
            add(stamp2TimeConvertBtn)
            add(stamp2TimeOutText)
        }
        stamp2TimeOutText.showCopy()

        val time2StampPanel = JPanel(FlowLayout(FlowLayout.LEFT))
        time2StampInText.setHint(DateTimeFormatters.formatDateTime(LocalDateTime.now()))
        with(time2StampPanel) {
            add(time2StampInText)
            add(time2StampConvertBtn)
            add(time2StampOutText)
        }
        time2StampOutText.showCopy()

        // 日期计算
        val dateCalculateTitlePanel = JPanel(FlowLayout(FlowLayout.LEFT))
        dateCalculateTitlePanel.add(JLabel(">> 日期计算"))

        val dateCalculatePanel = JPanel(FlowLayout(FlowLayout.LEFT))
        dateCalculateInText.setHint(DateTimeFormatters.formatDate(LocalDate.now()))
        with(dateCalculatePanel) {
            add(JLabel("日期加减："))
            add(dateCalculateInText)
            add(dateOpComboBox)
            add(dateUnitText)
            add(dateUnitComboBox)
            add(JLabel("="))
            add(dateCalculateOutText)
        }

        val dateDiffPanel = JPanel(FlowLayout(FlowLayout.LEFT))
        dateDiffInText1.setHint(DateTimeFormatters.formatDate(LocalDate.now()))
        dateDiffInText2.setHint(DateTimeFormatters.formatDate(LocalDate.now()))
        with(dateDiffPanel) {
            add(JLabel("日期相差："))
            add(dateDiffInText1)
            add(JLabel("—"))
            add(dateDiffInText2)
            add(JLabel("="))
            add(dateDiffOutText)
        }

        // 时间计算
        val timmeCalculateTitlePanel = JPanel(FlowLayout(FlowLayout.LEFT))
        timmeCalculateTitlePanel.add(JLabel(">> 时间计算"))

        val timeCalculatePanel = JPanel(FlowLayout(FlowLayout.LEFT))
        timeCalculateInText.setHint(DateTimeFormatters.formatTime(LocalTime.now()))
        with(timeCalculatePanel) {
            add(JLabel("时间加减："))
            add(timeCalculateInText)
            add(timeOpComboBox)
            add(timeUnitText)
            add(timeUnitComboBox)
            add(JLabel("="))
            add(timeCalculateOutText)
        }

        val timeDiffPanel = JPanel(FlowLayout(FlowLayout.LEFT))
        timeDiffInText1.setHint(DateTimeFormatters.formatTime(LocalTime.now()))
        timeDiffInText2.setHint(DateTimeFormatters.formatTime(LocalTime.now()))
        with(timeDiffPanel) {
            add(JLabel("时间相差："))
            add(timeDiffInText1)
            add(JLabel("—"))
            add(timeDiffInText2)
            add(JLabel("="))
            add(timeDiffOutText)
        }

        val timeUnitConvertPanel = JPanel(FlowLayout(FlowLayout.LEFT))
        with(timeUnitConvertPanel) {
            add(JLabel("时间转换："))
            add(timeSText)
            add(JLabel("秒 = "))
            add(timeMText)
            add(JLabel("分"))
            add(timeMSText)
            add(JLabel("秒 = "))
            add(timeHText)
            add(JLabel("时"))
            add(timeHMText)
            add(JLabel("分"))
            add(timeHMSText)
            add(JLabel("秒"))
        }

        val box = Box.createVerticalBox()
        box.enableClickRequestFocus()
        with(box) {
            add(timeZonePanel)
            add(formatTimePanel)
            add(nowTimeTitlePanel)
            add(nowTimePanel)
            add(nowStampPanel)
            add(stamp2TimeTitlePanel)
            add(stamp2TimePanel)
            add(time2StampPanel)
            add(dateCalculateTitlePanel)
            add(dateCalculatePanel)
            add(dateDiffPanel)
            add(timmeCalculateTitlePanel)
            add(timeCalculatePanel)
            add(timeDiffPanel)
            add(timeUnitConvertPanel)
        }

        mainPanel.enableClickRequestFocus()
        mainPanel.add(box)
    }

    override fun selectedChange(select: Boolean) {
        if (select) {
            MessageBar.showMouseCaret("")
            showCurrentTime()
            timer.start()
        } else {
            timer.stop()
        }
    }

    /**
     * 显示当前时间
     */
    fun showCurrentTime() {
        val now = LocalDateTime.now(getSelectedZone())

        formatTimeLabel.text = dateTimeChineseFormatter.format(now) + " " + DateTimeFormatters.formatWeek(now)
        nowStampText.text = Timestamp.valueOf(now).time.toString()
        nowTimeText.text = DateTimeFormatters.formatDateTime(now)
    }

    /**
     * 获取选择的时区
     */
    fun getSelectedZone(): ZoneId {
        val timeZone = timeZoneCombox.selectedItem as TimeZoneEnum
        return ZoneId.of(timeZone.id)
    }

    /**
     * JTextField默认样式
     */
    private fun createJTextField(column: Int = 0, inputType: TextInputType? = null): JTextField {
        val textField = JTextField(column)
        if (inputType != null) {
            textField.setInputType(inputType)
        }
        textField.showClear()
        return textField
    }

    private class NotBlankInputChecker(com: JTextComponent) : InputChecker(com) {

        override fun doCheck(component: JTextComponent) = !component.text.isNullOrBlank()

        override fun focusLost(e: FocusEvent) {
            showNormal()
        }

        override fun documentUpdate(e: DocumentEvent) {
            showNormal()
        }
    }

    private class AllTrueInputChecker(com: JTextComponent) : InputChecker(com) {

        override fun doCheck(component: JTextComponent) = true

        override fun focusLost(e: FocusEvent) {
            showNormal()
        }

        override fun documentUpdate(e: DocumentEvent) {
            showNormal()
        }
    }

    /**
     * 模块标题
     */
    override val title = "时间处理"

    /**
     * 模块图标
     */
    override val icon = FlatSVGIcon("icons/time_dark.svg")

    /**
     * 模块提示
     */
    override val tip = "时间处理"
}