package com.wxl.jdevtool.toast

/**
 * Create by wuxingle on 2024/02/29
 */
class ToastProperties {

    companion object {
        const val TOAST_ICON = "Toast.icon"
        const val TOAST_COMPONENT = "Toast.component"
        const val TOAST_SHOW_CLOSE_BUTTON = "Toast.showCloseButton"
        const val TOAST_CLOSE_CALLBACK = "Toast.closeCallback"
        const val TOAST_CLOSE_ICON = "Toast.closeIcon"
        const val TOAST_SUCCESS_ICON = "Toast.success.icon"
        const val TOAST_INFO_ICON = "Toast.info.icon"
        const val TOAST_WARNING_ICON = "Toast.warning.icon"
        const val TOAST_ERROR_ICON = "Toast.error.icon"
        const val TOAST_SHADOW_COLOR = "Toast.shadowColor"
        const val TOAST_SHADOW_INSETS = "Toast.shadowInsets"
        const val TOAST_SHADOW_OPACITY = "Toast.shadowOpacity"
        const val TOAST_ICON_TEXT_GAP = "Toast.iconTextGap"
        const val TOAST_CLOSE_BUTTON = "Toast.closeButton"
        const val TOAST_CLOSE_BUTTON_GAP = "Toast.closeButtonGap"
        const val TOAST_MINIMUM_WIDTH = "Toast.minimumWidth"
        const val TOAST_MAXIMUM_WIDTH = "Toast.maximumWidth"
        const val TOAST_ARC = "Toast.arc"
        const val TOAST_OUTLINE_WIDTH = "Toast.outlineWidth"
        const val TOAST_OUTLINE_COLOR = "Toast.outlineColor"
        const val TOAST_MARGIN = "Toast.margin"
        const val TOAST_CLOSE_ICON_COLOR = "Toast.closeIconColor"
        const val TOAST_USE_EFFECT = "Toast.useEffect"
        const val TOAST_EFFECT_COLOR = "Toast.effectColor"
        const val TOAST_EFFECT_WIDTH = "Toast.effectWidth"
        const val TOAST_EFFECT_OPACITY = "Toast.effectOpacity"
        const val TOAST_EFFECT_ALIGNMENT = "Toast.effectAlignment"
        const val TOAST_BACKGROUND = "Toast.background"
        const val TOAST_ANIMATION = "Toast.animation"
        const val TOAST_ANIMATION_RESOLUTION = "Toast.animationResolution"
        const val TOAST_LIMIT = "Toast.limit"
        const val TOAST_FRAME_INSETS = "Toast.frameInsets"
        const val TOAST_HORIZONTAL_GAP = "Toast.horizontalGap"
        const val TOAST_ANIMATION_MOVE = "Toast.animationMove"
        const val TOAST_DURATION = "Toast.duration";

        fun getIconKey(type: ToastType): String {
            return when (type) {
                ToastType.SUCCESS -> TOAST_SUCCESS_ICON
                ToastType.INFO -> TOAST_INFO_ICON
                ToastType.WARNING -> TOAST_WARNING_ICON
                ToastType.ERROR -> TOAST_ERROR_ICON
            }
        }
    }
}
