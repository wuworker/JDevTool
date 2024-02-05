package com.wxl.jdevtool.time

/**
 * Create by wuxingle on 2024/01/31
 * 日期，时间相关常量
 */

/**
 * 时间操作
 */
enum class TimeOp(
    val type: Int,
    val desc: String
) {
    ADD(1, "增加"),
    SUB(2, "减少"),
    ;

    override fun toString(): String {
        return desc
    }
}

/**
 * 日期单位
 */
enum class DateUnit(
    val type: Int,
    val desc: String
) {

    DAY(1, "天"),
    WEEK(2, "星期"),
    MONTH(3, "月"),
    YEAR(4, "年"),
    ;

    override fun toString(): String {
        return desc
    }
}

/**
 * 时间单位
 */
enum class TimeUnit(
    val type: Int,
    val desc: String
) {
    HOUR(1, "小时"),
    MINUTE(2, "分钟"),
    SECOND(3, "秒"),
    ;

    override fun toString(): String {
        return desc
    }
}


/**
 * 时区枚举
 */
enum class TimeZoneEnum(
    val id: String,
    val desc: String

) {

    UTC_N12("UTC-12", "埃尼威托克岛（UTC-12）"),
    UTC_N11("UTC-11", "萨摩亚群岛（UTC-11）"),
    UTC_N10("UTC-10", "夏威夷（UTC-10）"),
    UTC_N9("UTC-9", "阿拉斯加（UTC-9）"),
    UTC_N8("UTC-8", "太平洋时间（UTC-8）"),
    UTC_N7("UTC-7", "山脉时间（UTC-7）"),
    UTC_N6("UTC-6", "中央标准时间（UTC-6）"),
    UTC_N5("UTC-5", "东部时间（UTC-5）"),
    UTC_N4("UTC-4", "大西洋时间（UTC-4）"),
    UTC_N3("UTC-3", "Brazilia（UTC-3）"),
    UTC_N2("UTC-2", "大西洋中部时间（UTC-2）"),
    UTC_N1("UTC-1", "亚述尔群岛（UTC-1）"),
    UTC_0("UTC", "格林威治标准（UTC）"),
    UTC_P1("UTC+1", "罗马（UTC+1）"),
    UTC_P2("UTC+2", "以色列（UTC+2）"),
    UTC_P3("UTC+3", "莫斯科（UTC+3）"),
    UTC_P4("UTC+4", "巴库（UTC+4）"),
    UTC_P5("UTC+5", "New Delhi（UTC+5）"),
    UTC_P6("UTC+6", "Dhakar（UTC+6）"),
    UTC_P7("UTC+7", "曼谷（UTC+7）"),
    UTC_P8("UTC+8", "北京（UTC+8）"),
    UTC_P9("UTC+9", "东京（UTC+9）"),
    UTC_P10("UTC+10", "悉尼（UTC+10）"),
    UTC_P11("UTC+11", "Magadan（UTC+11）"),
    UTC_P12("UTC+12", "惠灵顿（UTC+12）"),
    ;

    override fun toString(): String {
        return desc
    }

}