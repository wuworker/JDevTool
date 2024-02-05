package com.wxl.jdevtool.time

import com.formdev.flatlaf.extras.FlatSVGIcon
import com.wxl.jdevtool.ComponentId
import com.wxl.jdevtool.TabbedModule
import com.wxl.jdevtool.component.ComponentFactory
import com.wxl.jdevtool.extension.TextInputType
import com.wxl.jdevtool.extension.enableClickRequestFocus
import com.wxl.jdevtool.extension.setHint
import com.wxl.jdevtool.extension.setInputType
import com.wxl.jdevtool.message.MessageNotifier
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import java.awt.Color
import java.awt.FlowLayout
import java.awt.Font
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.swing.*

/**
 * Create by wuxingle on 2024/01/29
 * 时间模块
 */
@Order(300)
@Component
@ComponentId("timeTabbedModule")
class TimeTabbedModule(
    @Autowired val messageNotifier: MessageNotifier
) : TabbedModule {

    private val dateTimeChineseFormatter = DateTimeFormatter.ofPattern("yyyy年MM月dd号 HH点mm分ss秒")

    final override val mainPanel: JPanel

    final val timeZoneCombox: JComboBox<TimeZoneEnum>

    final val formatTimeLabel: JLabel

    final val nowTimeText: JTextField

    @ComponentId("nowTimeCopyBtn")
    final val nowTimeCopyBtn: JButton

    final val nowStampText: JTextField

    @ComponentId("nowStampCopyBtn")
    final val nowStampCopyBtn: JButton

    final val stamp2TimeInText: JTextField

    @ComponentId("stamp2TimeConvertBtn")
    final val stamp2TimeConvertBtn: JButton

    final val stamp2TimeOutText: JTextField

    @ComponentId("stamp2TimeCopyBtn")
    final val stamp2TimeCopyBtn: JButton

    final val time2StampInText: JTextField

    @ComponentId("time2StampConvertBtn")
    final val time2StampConvertBtn: JButton

    final val time2StampOutText: JTextField

    @ComponentId("time2StampCopyBtn")
    final val time2StampCopyBtn: JButton

    @ComponentId("dateCalculateInText")
    final val dateCalculateInText: JTextField

    @ComponentId("dateOpComboBox")
    final val dateOpComboBox: JComboBox<TimeOp>

    @ComponentId("dateUnitText")
    final val dateUnitText: JTextField

    @ComponentId("dateUnitComboBox")
    final val dateUnitComboBox: JComboBox<DateUnit>

    final val dateCalculateOutText: JTextField

    @ComponentId("dateDiffInText1")
    final val dateDiffInText1: JTextField

    @ComponentId("dateDiffInText2")
    final val dateDiffInText2: JTextField

    final val dateDiffOutText: JTextField

    @ComponentId("timeCalculateInText")
    final val timeCalculateInText: JTextField

    @ComponentId("timeOpComboBox")
    final val timeOpComboBox: JComboBox<TimeOp>

    @ComponentId("timeUnitText")
    final val timeUnitText: JTextField

    @ComponentId("timeUnitComboBox")
    final val timeUnitComboBox: JComboBox<TimeUnit>

    final val timeCalculateOutText: JTextField

    @ComponentId("timeDiffInText1")
    final val timeDiffInText1: JTextField

    @ComponentId("timeDiffInText2")
    final val timeDiffInText2: JTextField

    final val timeDiffOutText: JTextField

    @ComponentId("timeSText")
    final val timeSText: JTextField

    @ComponentId("timeMText")
    final val timeMText: JTextField

    @ComponentId("timeMSText")
    final val timeMSText: JTextField

    @ComponentId("timeHText")
    final val timeHText: JTextField

    @ComponentId("timeHMText")
    final val timeHMText: JTextField

    @ComponentId("timeHMSText")
    final val timeHMSText: JTextField

    private val timer: Timer

    init {
        mainPanel = JPanel(FlowLayout(FlowLayout.LEFT))
        timeZoneCombox = JComboBox(TimeZoneEnum.values())

        formatTimeLabel = JLabel()

        // 当前时间
        nowTimeText = JTextField(20)
        nowTimeCopyBtn = ComponentFactory.createCopyBtn()

        nowStampText = JTextField(20)
        nowStampCopyBtn = ComponentFactory.createCopyBtn()

        // 时间转换
        stamp2TimeInText = JTextField(20)
        stamp2TimeConvertBtn = JButton("转换")
        stamp2TimeOutText = JTextField(20)
        stamp2TimeCopyBtn = ComponentFactory.createCopyBtn()

        time2StampInText = JTextField(20)
        time2StampConvertBtn = JButton("转换")
        time2StampOutText = JTextField(20)
        time2StampCopyBtn = ComponentFactory.createCopyBtn()

        // 日期计算
        dateCalculateInText = JTextField(10)
        dateOpComboBox = JComboBox(TimeOp.values())
        dateUnitText = JTextField(4)
        dateUnitComboBox = JComboBox(DateUnit.values())
        dateCalculateOutText = JTextField(10)

        dateDiffInText1 = JTextField(10)
        dateDiffInText2 = JTextField(10)
        dateDiffOutText = JTextField(15)

        // 时间计算
        timeCalculateInText = JTextField(10)
        timeOpComboBox = JComboBox(TimeOp.values())
        timeUnitText = JTextField(4)
        timeUnitComboBox = JComboBox(TimeUnit.values())
        timeCalculateOutText = JTextField(10)

        timeDiffInText1 = JTextField(10)
        timeDiffInText2 = JTextField(10)
        timeDiffOutText = JTextField(20)

        timeSText = JTextField(5)
        timeMText = JTextField(4)
        timeMSText = JTextField(4)
        timeHText = JTextField(4)
        timeHMText = JTextField(4)
        timeHMSText = JTextField(4)

        timer = Timer(1000) {
            showCurrentTime()
        }

        initUI()
    }

    /**
     * 初始化布局
     */
    private fun initUI() {
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
        with(nowTimePanel) {
            add(JLabel("现在的当地时间："))
            add(nowTimeText)
            add(nowTimeCopyBtn)
        }

        val nowStampPanel = JPanel(FlowLayout(FlowLayout.LEFT))
        nowStampText.isEditable = false
        with(nowStampPanel) {
            add(JLabel("现在的时间戳："))
            add(nowStampText)
            add(nowStampCopyBtn)
        }

        // 时间戳转换
        val stamp2TimeTitlePanel = JPanel(FlowLayout(FlowLayout.LEFT))
        stamp2TimeTitlePanel.add(JLabel(">> 时间戳和当地时间转换"))

        val stamp2TimePanel = JPanel()
        stamp2TimeInText.setInputType(TextInputType.NUMBER)
        stamp2TimeInText.setHint(System.currentTimeMillis().toString())
        with(stamp2TimePanel) {
            add(stamp2TimeInText)
            add(stamp2TimeConvertBtn)
            add(stamp2TimeOutText)
            add(stamp2TimeCopyBtn)
        }

        val time2StampPanel = JPanel()
        time2StampInText.setInputType(TextInputType.DATETIME)
        time2StampInText.setHint(DateTimeFormatters.formatDateTime(LocalDateTime.now()))
        with(time2StampPanel) {
            add(time2StampInText)
            add(time2StampConvertBtn)
            add(time2StampOutText)
            add(time2StampCopyBtn)
        }

        // 日期计算
        val dateCalculateTitlePanel = JPanel(FlowLayout(FlowLayout.LEFT))
        dateCalculateTitlePanel.add(JLabel(">> 日期计算"))

        val dateCalculatePanel = JPanel(FlowLayout(FlowLayout.LEFT))
        dateCalculateInText.setInputType(TextInputType.DATE)
        dateCalculateInText.setHint(DateTimeFormatters.formatDate(LocalDate.now()))
        dateUnitText.setInputType(TextInputType.NUMBER)
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
        dateDiffInText1.setInputType(TextInputType.DATE)
        dateDiffInText1.setHint(DateTimeFormatters.formatDate(LocalDate.now()))
        dateDiffInText2.setInputType(TextInputType.DATE)
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
        timeCalculateInText.setInputType(TextInputType.TIME)
        timeCalculateInText.setHint(DateTimeFormatters.formatTime(LocalTime.now()))
        timeUnitText.setInputType(TextInputType.NUMBER)
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
        timeDiffInText1.setInputType(TextInputType.TIME)
        timeDiffInText1.setHint(DateTimeFormatters.formatTime(LocalTime.now()))
        timeDiffInText2.setInputType(TextInputType.TIME)
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
        timeSText.setInputType(TextInputType.NUMBER)
        timeMText.setInputType(TextInputType.NUMBER)
        timeMSText.setInputType(TextInputType.NUMBER)
        timeHText.setInputType(TextInputType.NUMBER)
        timeHMText.setInputType(TextInputType.NUMBER)
        timeHMSText.setInputType(TextInputType.NUMBER)
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
            messageNotifier.showMouseCaret("")
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