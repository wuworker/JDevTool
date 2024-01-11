package com.wxl.jdevtool


import com.formdev.flatlaf.extras.FlatSVGIcon
import javax.swing.Icon

/**
 * Create by wuxingle on 2024/01/05
 * 所有小图标
 */
object Icons {

    val newLine: Icon by lazy {
        loadSvg("icons/newLine.svg")
    }

    val newLineHover: Icon by lazy {
        loadSvg("icons/newLineHover.svg")
    }

    val newLineSelected: Icon by lazy {
        loadSvg("icons/newLineSelected.svg")
    }


    private fun loadSvg(path: String): Icon {
        return FlatSVGIcon(path)
    }
}


