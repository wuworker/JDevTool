package com.wxl.jdevtool.util

import java.awt.GridBagConstraints
import java.awt.Insets

/**
 * Create by wuxingle on 2024/04/03
 * swing组件相关工具类
 */
object ComponentUtils {

    /**
     * 创建GridBagConstraints
     */
    fun createConstraints(
        gridx: Int = GridBagConstraints.RELATIVE,
        gridy: Int = GridBagConstraints.RELATIVE,
        gridwidth: Int = 1,
        gridheight: Int = 1,
        weightx: Double = 0.0,
        weighty: Double = 0.0,
        anchor: Int = GridBagConstraints.CENTER,
        fill: Int = GridBagConstraints.NONE,
        insets: Insets = Insets(0, 0, 0, 0),
        ipadx: Int = 0,
        ipady: Int = 0
    ): GridBagConstraints = GridBagConstraints(
        gridx, gridy,
        gridwidth, gridheight,
        weightx, weighty,
        anchor, fill,
        insets, ipadx, ipady
    )

}