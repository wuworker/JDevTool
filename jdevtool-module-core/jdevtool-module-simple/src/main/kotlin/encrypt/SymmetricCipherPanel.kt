package com.wxl.jdevtool.encrypt

import com.wxl.jdevtool.ComponentId
import com.wxl.jdevtool.encrypt.component.ByteAreaPanel
import com.wxl.jdevtool.encrypt.component.ByteAreaPanelChecker
import com.wxl.jdevtool.validate.InputValidateGroup
import java.awt.BorderLayout
import java.awt.FlowLayout
import java.io.Serial
import javax.swing.*

/**
 * Create by wuxingle on 2024/02/27
 * 对称加密页面
 */
class SymmetricCipherPanel : JPanel(BorderLayout()) {

    @ComponentId("algorithmComboBox")
    val algorithmComboBox = JComboBox(SymmetricAlgorithm.values())

    val modeAlgorithmComboBox = JComboBox(AlgorithmMode.values())

    val paddingAlgorithmComboBox = JComboBox(AlgorithmPadding.values())

    @ComponentId("keyGenBtn")
    val keyGenBtn = JButton("随机生成")

    val keyLenLabel = JLabel("密钥长度:")

    val keyLenComboBox = JComboBox(arrayOf(128, 192, 256))

    val keyPanel = ByteAreaPanel()

    val enPanel = ByteAreaPanel()

    @ComponentId("enBtn")
    val enBtn = JButton("加密=>")

    val dePanel = ByteAreaPanel()

    @ComponentId("deBtn")
    val deBtn = JButton("解密<=")

    internal fun initUI() {
        val panel1 = JPanel(BorderLayout())
        val headPanel = JPanel(FlowLayout(FlowLayout.LEFT))
        with(headPanel) {
            add(JLabel("对称加密算法："))
            add(algorithmComboBox)
            add(modeAlgorithmComboBox)
            add(paddingAlgorithmComboBox)
        }

        with(keyPanel) {
            border = BorderFactory.createTitledBorder("密钥")
            textArea.rows = 3
            setShowStyle(KeyShowStyle.BASE64)
            addCopyBtn()
            addCustom(keyGenBtn)
            addCustom(keyLenLabel)
            addCustom(keyLenComboBox)
        }
        panel1.add(headPanel, BorderLayout.NORTH)
        panel1.add(keyPanel, BorderLayout.SOUTH)

        enPanel.border = BorderFactory.createTitledBorder("原文")
        enPanel.addCopyBtn()
        enPanel.addCustom(enBtn)
        dePanel.border = BorderFactory.createTitledBorder("密文")
        dePanel.addCopyBtn()
        dePanel.setShowStyle(KeyShowStyle.BASE64)
        dePanel.addCustom(deBtn)

        val splitPane = JSplitPane(JSplitPane.HORIZONTAL_SPLIT)
        with(splitPane) {
            resizeWeight = 0.5
            leftComponent = enPanel
            rightComponent = dePanel
        }

        add(panel1, BorderLayout.NORTH)
        add(splitPane)
    }

    fun checkEncode(): Boolean {
        val checker = InputValidateGroup(ByteAreaPanelChecker(keyPanel), ByteAreaPanelChecker(enPanel))
        return checker.check(true)
    }

    fun checkDecode(): Boolean {
        val checker = InputValidateGroup(ByteAreaPanelChecker(keyPanel), ByteAreaPanelChecker(dePanel))
        return checker.check(true)
    }

    companion object {
        @Serial
        private const val serialVersionUID: Long = -5490947654028207987L
    }


}