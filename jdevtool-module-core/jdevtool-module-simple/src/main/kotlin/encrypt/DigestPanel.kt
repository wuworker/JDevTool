package com.wxl.jdevtool.encrypt

import com.wxl.jdevtool.ComponentId
import com.wxl.jdevtool.encrypt.component.ByteAreaPanel
import com.wxl.jdevtool.encrypt.component.SecretTextFieldPanel
import java.awt.BorderLayout
import java.awt.FlowLayout
import java.io.Serial
import javax.swing.*
import kotlin.random.Random

/**
 * Create by wuxingle on 2024/02/06
 * 消息摘要页面
 */
class DigestPanel : JPanel() {

    @ComponentId("headPanel")
    val headPanel: JPanel

    @ComponentId("algorithmComboBox")
    val algorithmComboBox: JComboBox<DigestAlgorithm>

    @ComponentId("hmacAlgorithmComboBox")
    val hmacAlgorithmComboBox: JComboBox<HMacAlgorithm>

    val secretTextField: SecretTextFieldPanel

    @ComponentId("genBtn")
    val genBtn: JButton

    val downPanel: JSplitPane

    val inByteArea: ByteAreaPanel

    val outByteArea: ByteAreaPanel

    init {
        headPanel = JPanel()
        algorithmComboBox = JComboBox(DigestAlgorithm.values())
        hmacAlgorithmComboBox = JComboBox(HMacAlgorithm.values())
        secretTextField = SecretTextFieldPanel {
            when (it) {
                KeyShowStyle.STRING -> {
                    val array = ByteArray(8)
                    for (i in array.indices) {
                        array[i] = Random.nextInt(33, 127).toByte()
                    }
                    array
                }

                else -> Random.nextBytes(8)
            }
        }
        genBtn = JButton("生成")

        downPanel = JSplitPane(JSplitPane.VERTICAL_SPLIT)
        inByteArea = ByteAreaPanel()
        outByteArea = ByteAreaPanel()

        initUI()
    }

    private fun initUI() {
        with(headPanel) {
            layout = FlowLayout(FlowLayout.LEFT)
            add(JLabel("消息摘要算法："))
            add(algorithmComboBox)
            add(hmacAlgorithmComboBox)
            add(secretTextField)
            add(genBtn)
        }
        hmacAlgorithmComboBox.isVisible = false
        secretTextField.isVisible = false

        inByteArea.setShowStyle(KeyShowStyle.STRING)
        inByteArea.border = BorderFactory.createTitledBorder("输入")
        inByteArea.addCopyBtn()
        outByteArea.setShowStyle(KeyShowStyle.BASE64)
        outByteArea.border = BorderFactory.createTitledBorder("结果")
        outByteArea.addCopyBtn()
        with(downPanel) {
            resizeWeight = 0.5
            leftComponent = inByteArea
            rightComponent = outByteArea
        }

        layout = BorderLayout()
        add(headPanel, BorderLayout.NORTH)
        add(downPanel)
    }

    fun checkInput(): Boolean {
        if (!inByteArea.check()) {
            return false
        }
        if (inByteArea.data.isEmpty()) {
            inByteArea.inputChecker.showWarn()
            return false
        }
        return true
    }

    fun checkSecret(): Boolean {
        if (!secretTextField.check()) {
            return false
        }
        if (secretTextField.key.isEmpty()) {
            secretTextField.inputChecker.showWarn()
            return false
        }
        return true
    }

    companion object {
        @Serial
        private const val serialVersionUID: Long = 2761043662734894733L
    }

}
