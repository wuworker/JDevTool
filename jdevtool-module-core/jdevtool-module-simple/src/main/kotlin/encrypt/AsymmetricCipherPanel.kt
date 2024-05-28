package com.wxl.jdevtool.encrypt

import com.wxl.jdevtool.ComponentId
import com.wxl.jdevtool.encrypt.component.ByteAreaPanel
import java.awt.BorderLayout
import java.awt.FlowLayout
import java.io.Serial
import javax.swing.*

/**
 * Create by wuxingle on 2024/03/12
 * 非对称加密
 */
class AsymmetricCipherPanel : JPanel(BorderLayout()) {

    val algorithmComboBox = JComboBox(ASymmetricAlgorithm.values())

    val keyLenComboBox = JComboBox(arrayOf(1024, 2048))

    @ComponentId("genKeyBtn")
    val genKeyBtn = JButton("生成密钥对")

    val publicKeyPanel = ByteAreaPanel()

    val privateKeyPanel = ByteAreaPanel()

    val enPanel = ByteAreaPanel()

    @ComponentId("priEnBtn")
    val priEnBtn = JButton("私钥加密=>")

    @ComponentId("pubEnBtn")
    val pubEnBtn = JButton("公钥加密=>")

    val dePanel = ByteAreaPanel()

    @ComponentId("priDeBtn")
    val priDeBtn = JButton("私钥解密<=")

    @ComponentId("pubDeBtn")
    val pubDeBtn = JButton("公钥解密<=")

    internal fun initUI() {
        val panel1 = JPanel(FlowLayout(FlowLayout.LEFT))
        with(panel1) {
            add(JLabel("非对称加密算法："))
            add(algorithmComboBox)
            add(JLabel("密钥长度："))
            add(keyLenComboBox)
            add(genKeyBtn)
        }

        val panel2 = JPanel(BorderLayout())
        with(publicKeyPanel) {
            border = BorderFactory.createTitledBorder("公钥")
            textArea.rows = 4
            setShowStyle(KeyShowStyle.BASE64)
            addCopyBtn()
        }
        with(privateKeyPanel) {
            border = BorderFactory.createTitledBorder("私钥")
            textArea.rows = 4
            setShowStyle(KeyShowStyle.BASE64)
            addCopyBtn()
        }
        with(panel2) {
            add(publicKeyPanel, BorderLayout.NORTH)
            add(privateKeyPanel, BorderLayout.SOUTH)
        }

        val headPanel = JPanel(BorderLayout())
        with(headPanel) {
            add(panel1, BorderLayout.NORTH)
            add(panel2, BorderLayout.SOUTH)
        }

        with(enPanel) {
            border = BorderFactory.createTitledBorder("原文")
            addCopyBtn()
            addCustom(priEnBtn)
            addCustom(pubEnBtn)
        }
        with(dePanel) {
            border = BorderFactory.createTitledBorder("密文")
            setShowStyle(KeyShowStyle.BASE64)
            addCopyBtn()
            addCustom(pubDeBtn)
            addCustom(priDeBtn)
        }

        val splitPane = JSplitPane(JSplitPane.HORIZONTAL_SPLIT)
        with(splitPane) {
            resizeWeight = 0.5
            leftComponent = enPanel
            rightComponent = dePanel
        }

        add(headPanel, BorderLayout.NORTH)
        add(splitPane)
    }

    companion object {
        @Serial
        private const val serialVersionUID: Long = 719778405591831161L
    }


}