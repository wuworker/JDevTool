package com.wxl.jdevtool.encrypt

import com.wxl.jdevtool.ComponentId
import com.wxl.jdevtool.encrypt.component.ByteAreaPanel
import java.awt.BorderLayout
import java.awt.FlowLayout
import java.io.Serial
import javax.swing.*

/**
 * Create by wuxingle on 2024/02/27
 * 对称加密页面
 */
class SymmetricCipherPanel : JPanel() {

    @ComponentId("algorithmComboBox")
    val algorithmComboBox: JComboBox<SymmetricAlgorithm>

    val modeAlgorithmComboBox: JComboBox<AlgorithmMode>

    val paddingAlgorithmComboBox: JComboBox<AlgorithmPadding>

    @ComponentId("keyGenBtn")
    val keyGenBtn: JButton

    val keyLenLabel: JLabel

    val keyLenComboBox: JComboBox<Int>

    val keyPanel: ByteAreaPanel

    val splitPane: JSplitPane

    val enPanel: ByteAreaPanel

    @ComponentId("enBtn")
    val enBtn: JButton

    val dePanel: ByteAreaPanel

    @ComponentId("deBtn")
    val deBtn: JButton

    init {
        algorithmComboBox = JComboBox(SymmetricAlgorithm.values())
        modeAlgorithmComboBox = JComboBox(AlgorithmMode.values())
        paddingAlgorithmComboBox = JComboBox(AlgorithmPadding.values())
        keyGenBtn = JButton("随机生成")
        keyLenLabel = JLabel("密钥长度:")
        // aes密钥长度
        keyLenComboBox = JComboBox(arrayOf(128, 192, 256))
        keyPanel = ByteAreaPanel()
        splitPane = JSplitPane(JSplitPane.HORIZONTAL_SPLIT)
        enPanel = ByteAreaPanel()
        enBtn = JButton("加密=>")
        dePanel = ByteAreaPanel()
        deBtn = JButton("解密<=")

        initUI()
    }

    private fun initUI() {
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
            addCustom(keyGenBtn)
            addCustom(keyLenLabel)
            addCustom(keyLenComboBox)
        }
        panel1.add(headPanel, BorderLayout.NORTH)
        panel1.add(keyPanel, BorderLayout.SOUTH)

        enPanel.border = BorderFactory.createTitledBorder("原文")
        enPanel.addCustom(enBtn)
        dePanel.border = BorderFactory.createTitledBorder("密文")
        dePanel.setShowStyle(KeyShowStyle.BASE64)
        dePanel.addCustom(deBtn)
        with(splitPane) {
            resizeWeight = 0.5
            leftComponent = enPanel
            rightComponent = dePanel
        }

        layout = BorderLayout()
        add(panel1, BorderLayout.NORTH)
        add(splitPane)
    }

    companion object {
        @Serial
        private const val serialVersionUID: Long = -5490947654028207987L
    }


}