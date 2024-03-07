package com.wxl.jdevtool.toast

/**
 * Create by wuxingle on 2024/03/05
 */
class ToastIcons {

    class Path {

        companion object {
            const val CLOSE_ICON = "icons/toast/close.svg"
            const val ERROR_ICON = "icons/toast/error.svg"
            const val INFO_ICON = "icons/toast/info.svg"
            const val SUCCESS_ICON = "icons/toast/success.svg"
            const val WARNING_ICON = "icons/toast/warning.svg"

            fun getIconPath(type: ToastType): String {
                return when (type) {
                    ToastType.SUCCESS -> SUCCESS_ICON
                    ToastType.ERROR -> ERROR_ICON
                    ToastType.INFO -> INFO_ICON
                    ToastType.WARNING -> WARNING_ICON
                }
            }
        }
    }
}
