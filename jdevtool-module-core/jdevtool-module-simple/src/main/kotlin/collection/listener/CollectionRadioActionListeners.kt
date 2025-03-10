package com.wxl.jdevtool.collection.listener

import com.wxl.jdevtool.ComponentListener
import com.wxl.jdevtool.collection.CollectionTabbedModule
import org.springframework.beans.factory.annotation.Autowired
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.BorderFactory

/**
 * Create by wuxingle on 2024/01/09
 * 单选按钮监听
 */

/**
 * 单集合操作
 */
@ComponentListener(
    "collectionTabbedModule.deduplicateRadio",
    "collectionTabbedModule.repeatRadio"
)
class SingleCollectionOpActionListener(
    @Autowired val collectionTabbedModule: CollectionTabbedModule
) : ActionListener {

    private val border by lazy {
        BorderFactory.createTitledBorder("集合")
    }

    override fun actionPerformed(e: ActionEvent?) {
        collectionTabbedModule.leftInputPanel1.border = border
        collectionTabbedModule.leftChecker1.showNormal()

        // 去掉另一个集合
        collectionTabbedModule.leftTextArea2.text = ""
        collectionTabbedModule.leftPanel.rightComponent = null
    }
}

/**
 * 多集合操作
 */
@ComponentListener(
    "collectionTabbedModule.unionRadio",
    "collectionTabbedModule.intersectionRadio",
    "collectionTabbedModule.diff1Radio",
    "collectionTabbedModule.diff2Radio"
)
class MultiCollectionOpActionListener(
    @Autowired val collectionTabbedModule: CollectionTabbedModule
) : ActionListener {

    private val border1 by lazy {
        BorderFactory.createTitledBorder("集合1")
    }

    private val border2 by lazy {
        BorderFactory.createTitledBorder("集合2")
    }

    override fun actionPerformed(e: ActionEvent?) {
        collectionTabbedModule.leftInputPanel1.border = border1
        collectionTabbedModule.leftInputPanel2.border = border2
        collectionTabbedModule.leftChecker1.showNormal()
        collectionTabbedModule.leftChecker2.showNormal()

        collectionTabbedModule.leftPanel.rightComponent = collectionTabbedModule.leftInputPanel2
    }
}
