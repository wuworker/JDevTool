package com.wxl.jdevtool


import com.formdev.flatlaf.extras.FlatSVGIcon
import javax.swing.Icon

/**
 * Create by wuxingle on 2024/01/05
 * 所有小图标
 */
object Icons {

    // 换行按钮
    val newLine: Icon by lazy {
        loadSvg("icons/newLine.svg")
    }

    val newLineHover: Icon by lazy {
        loadSvg("icons/newLineHover.svg")
    }

    val newLineSelected: Icon by lazy {
        loadSvg("icons/newLineSelected.svg")
    }

    // 复制按钮
    val copy: Icon by lazy {
        loadSvg("icons/copyBtn.svg")
    }

    val copyHover: Icon by lazy {
        loadSvg("icons/copyBtnHover.svg")
    }

    val copySelected: Icon by lazy {
        loadSvg("icons/copyBtnSelected.svg")
    }

    val copyDisable: Icon by lazy {
        loadSvg("icons/copyBtnDisable.svg")
    }

    // 帮助按钮
    val help: Icon by lazy {
        loadSvg("icons/helpButtonInactive.svg")
    }

    val helpHover: Icon by lazy {
        loadSvg("icons/helpButton.svg")
    }

    private fun loadSvg(path: String): Icon {
        return FlatSVGIcon(path)
    }
}


