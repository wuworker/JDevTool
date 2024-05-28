package com.wxl.jdevtool.encrypt

import com.formdev.flatlaf.extras.FlatSVGIcon
import com.wxl.jdevtool.ComponentId
import com.wxl.jdevtool.TabbedModule
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import java.awt.BorderLayout
import javax.swing.JPanel
import javax.swing.JTabbedPane

/**
 * Create by wuxingle on 2024/02/06
 * 加解密/编解码
 */
@Order(500)
@Component
@ComponentId("encryptTabbedModule")
class EncryptTabbedModule : TabbedModule {

    override val mainPanel = JPanel(BorderLayout())

    val tabbedPane = JTabbedPane(JTabbedPane.TOP, JTabbedPane.WRAP_TAB_LAYOUT)

    @ComponentId("digestPanel")
    val digestPanel = DigestPanel()

    @ComponentId("symmetricCipherPanel")
    val symmetricCipherPanel = SymmetricCipherPanel()

    @ComponentId("asymmetricCipherPanel")
    val asymmetricCipherPanel = AsymmetricCipherPanel()

    override fun afterPropertiesSet() {
        digestPanel.initUI()
        symmetricCipherPanel.initUI()
        asymmetricCipherPanel.initUI()

        tabbedPane.addTab("消息摘要", null, digestPanel, "消息摘要")
        tabbedPane.addTab("对称加密", null, symmetricCipherPanel, "对称加密")
        tabbedPane.addTab("非对称加密", null, asymmetricCipherPanel, "非对称加密")

        mainPanel.add(tabbedPane)
    }

    override val title = "加解密/编解码"

    override val icon = FlatSVGIcon("icons/encrypt_dark.svg")

    override val tip = "加解密/编解码"
}
