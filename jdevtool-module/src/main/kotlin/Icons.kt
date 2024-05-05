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

    val copyPress: Icon by lazy {
        loadSvg("icons/copyBtnPress.svg")
    }

    val copySelected: Icon by lazy {
        loadSvg("icons/copyBtnSelected.svg")
    }

    // 帮助按钮
    val help: Icon by lazy {
        loadSvg("icons/helpButtonInactive.svg")
    }

    val helpHover: Icon by lazy {
        loadSvg("icons/helpButton.svg")
    }

    // 方向按钮
    val chevronUp: Icon by lazy {
        loadSvg("icons/chevronUp.svg")
    }

    val chevronLeft: Icon by lazy {
        loadSvg("icons/chevronLeft.svg")
    }

    val chevronDown: Icon by lazy {
        loadSvg("icons/chevronDown.svg")
    }

    val chevronRight: Icon by lazy {
        loadSvg("icons/chevronRight.svg")
    }

    // 新增和删除
    val add: Icon by lazy {
        loadSvg("icons/add.svg")
    }

    val addHover: Icon by lazy {
        loadSvg("icons/addHover.svg")
    }

    val addPress: Icon by lazy {
        loadSvg("icons/addPress.svg")
    }

    val remove: Icon by lazy {
        loadSvg("icons/remove.svg")
    }

    val removeHover: Icon by lazy {
        loadSvg("icons/removeHover.svg")
    }

    val removePress: Icon by lazy {
        loadSvg("icons/removePress.svg")
    }

    // 执行按钮
    val execute: Icon by lazy {
        loadSvg("icons/execute.svg")
    }


    private fun loadSvg(path: String): Icon {
        return FlatSVGIcon(path)
    }
}


