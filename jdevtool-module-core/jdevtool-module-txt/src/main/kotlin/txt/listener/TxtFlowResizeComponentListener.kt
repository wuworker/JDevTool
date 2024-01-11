package com.wxl.jdevtool.txt.listener

import com.wxl.jdevtool.ComponentListener
import com.wxl.jdevtool.listener.FlowResizeComponentListener

/**
 * Create by wuxingle on 2024/01/08
 * 动态设置panel高度
 */
@ComponentListener("txtTabbedModule.originHeadPanel", "txtTabbedModule.targetHeadPanel")
class TxtFlowResizeComponentListener : FlowResizeComponentListener()

